package com.rh.oa.mt;

import java.util.Date;
import java.util.List;

import com.rh.core.base.Bean;
import com.rh.core.base.Context;
import com.rh.core.base.TipException;
import com.rh.core.comm.msg.MsgSender.MsgItem;
import com.rh.core.comm.todo.TodoBean;
import com.rh.core.comm.todo.TodoUtils;
import com.rh.core.serv.CommonServ;
import com.rh.core.serv.OutBean;
import com.rh.core.serv.ParamBean;
import com.rh.core.serv.ServDao;
import com.rh.core.serv.ServUtils;
import com.rh.core.serv.bean.SqlBean;
import com.rh.core.util.Constant;
import com.rh.core.util.TaskLock;
import com.rh.core.util.lang.Assert;
import com.rh.oa.mt.util.MeetingRoomConstant;

/**
 * 会议室预定服务.
 * @author hdy
 */
public class BookingNowfeServ extends CommonServ {
    
    /**
     * 会议室预定(不绑定工作流)服务id
     */
    private static final String OA_MT_BOOKING_NOWFE = "OA_MT_BOOKING_NOWFE";
    
    /**
     * 锁.
     */
    private TaskLock lock = null;

    /**
     * 查看当前会议室是否已被预订
     * @param paramBean 传入的参数
     * @return 被占用，返回true；没有被占用，返回false
     */
    private boolean findMeetingRoomIsBooked(ParamBean paramBean) {
        String firstDate = paramBean.getStr(MeetingRoomConstant.START_TIME);
        if (firstDate.length() == 16) {
            firstDate += ":00";
        }
        String secondDate = paramBean.getStr(MeetingRoomConstant.END_TIME);
        if (secondDate.length() == 16) {
            secondDate += ":00";
        }
        Bean oldBean = paramBean.getSaveOldData();
        SqlBean sql = new SqlBean().and("MR_ID", paramBean.getStr("MR_ID"))
                .andLT("STATUS", MeetingRoomConstant.BOOKING_STATUS_NOT_AGREE)
                .and("S_ODEPT", Context.getUserBean().getODeptCode());
        if (!isModified(paramBean, oldBean, Constant.KEY_ID)) {
            sql.and("BOOKING_ID", "<>", paramBean.getId());
        }
        sql.appendWhere(" and ((START_TIME > ?  and  START_TIME < ?)", firstDate, secondDate)
            .appendWhere(" or (START_TIME <= ? and END_TIME > ?))", firstDate, firstDate);
        int count = ServDao.count(paramBean.getServId(), sql);
        return count > 0;
    }

    @Override
    protected void beforeSave(ParamBean paramBean) {
        if (paramBean.isNotEmpty("MR_ID") || paramBean.isNotEmpty(MeetingRoomConstant.START_TIME)
                || paramBean.isNotEmpty(MeetingRoomConstant.END_TIME)) {
            
            //如果不为【取消】操作，则直接保存修改状态
            if (!paramBean.getStr("STATUS").equals(MeetingRoomConstant.BOOKING_STATUS_CANCEL)) {
                checkOccupy(paramBean);
            }
        }
    }
    
    /**
     * 是否为修改
     * @param newBean 修改后的参数
     * @param oldBean 修改前的参数
     * @param fieldName 判断字段
     * @return 是否修改
     */
    private boolean isModified(Bean newBean, Bean oldBean, String fieldName) {
        String oldVal = oldBean.getStr(fieldName);
        String newVal = newBean.getStr(fieldName);

        if (oldVal == null && newVal == null) {
            return false;
        }
        if (newVal != null && newVal.equals(oldVal)) {
            return false;
        }
        return true;
    }
        

    /**
     * 
     * @param paramBean 参数
     */
    public void checkOccupy(ParamBean paramBean) {
        
        if (paramBean.getId() != null && paramBean.getId().length() > 0) {  
            // 修改记录
            Bean oldBean = paramBean.getSaveOldData();
            //判断是否修改了会议室ID，开始结束时间
            if (!isModified(paramBean, oldBean, "MR_ID")
                    && !isModified(paramBean, oldBean, MeetingRoomConstant.START_TIME)
                    && !isModified(paramBean, oldBean, MeetingRoomConstant.END_TIME)) {
                return;
            }
        }
                
        // 会议室ID
        String meetingRoomId = paramBean.getStr("MR_ID");

        // 会议开始时间
        String startTime = paramBean.getStr(MeetingRoomConstant.START_TIME);
        if (startTime.length() == 16) {
            startTime += ":00";
        }
        // 会议结束时间
        String endTime = paramBean.getStr(MeetingRoomConstant.END_TIME);
        if (endTime.length() == 16) {
            endTime += ":00";
        }
        
        if ((MeetingRoomConstant.getNewTimeNum(startTime) < new Date().getTime())
                || (MeetingRoomConstant.getNewTimeNum(endTime) < new Date().getTime())) {
            throw new TipException(Context.getSyMsg(MeetingRoomConstant.AFTERSYDATE_ERROR));
        } 
        
        paramBean.set(MeetingRoomConstant.START_TIME, startTime).set(MeetingRoomConstant.END_TIME, endTime);
        
        // 加锁
        lock = new TaskLock("meetingRoom", meetingRoomId);

        boolean lockSuccess = lock.lock(); // 加锁成功
        if (lockSuccess) {
            boolean isOccupy = true;
            try {
                isOccupy = findMeetingRoomIsBooked(paramBean);
            } catch (Exception e) {
                lock.release();
                throw new RuntimeException(e);
            }
            if (isOccupy) {
                lock.release();
                throw new TipException(Context.getSyMsg(MeetingRoomConstant.BOOKING_DATE_ERROR));
            }
        } else {
            throw new TipException(Context.getSyMsg(MeetingRoomConstant.BOOKING_LOCKED));
        }
    }

    
    @Override
    public OutBean save(ParamBean paramBean) {
        try {
            return super.save(paramBean);
        } finally {
            if (lock != null) {
                lock.release();
            }
        }
    }

    
    /**
     * 取消预定。把申请状态改成已取消，并在标题前面加上“已取消”标示。
     * @param paramBean 传入的参数
     * @return 返回参数
     */
    public OutBean cancelBooking(ParamBean paramBean) {
        if (!(paramBean.getId() != null && paramBean.getId().length() > 0)) {
            // 提示【请预定】信息
            throw new TipException(Context.getSyMsg(MeetingRoomConstant.BOOKING_UNBOOKED));
        } 
        
        Bean query = new Bean(); // 查询参数
        query.set("BOOKING_ID", paramBean.getId()).set("STATUS", MeetingRoomConstant.BOOKING_STATUS_CANCEL);
        if (ServDao.count(MeetingRoomConstant.OA_MT_BOOKING_CODE, query) > 0) {
            //如果已经取消，则提示用户
            throw new TipException(Context.getSyMsg(MeetingRoomConstant.BOOKING_CANCELLED));
        }
        
        //修改申请的状态和标题
        Bean oldBean = ServDao.find(paramBean.getServId(), paramBean.getId());
        paramBean.set("TITLE", "已取消：" + oldBean.getStr("TITLE"));
        paramBean.set("STATUS", MeetingRoomConstant.BOOKING_STATUS_CANCEL);
        return super.save(paramBean);
    }
    
    /**
     * 发送会议通知提醒
     * @param paramBean 参数
     * @return 返回结果集
     */
    public OutBean sendMeetingRemindMsg(ParamBean paramBean) {
        TodoBean msg = new TodoBean();
        msg.setTitle(paramBean.getStr("TODO_TITLE")); //发送标题
        msg.setSender(Context.getUserBean().getCode()); //发送用户
        msg.setCode("TODO_REMIND");
        // 取得服务ID
        String servId = paramBean.getServId();
        msg.setServId(servId);
        // 取得服务名称
        msg.setCodeName(ServUtils.getServDef(OA_MT_BOOKING_NOWFE).getName());
        msg.setUrl(servId + ".showDialog.do");
        msg.setObjectId1(paramBean.getId());
        msg.setObjectId2(paramBean.getId());
        if (paramBean.isNotEmpty("TODO_CONTENT")) {
            msg.setContent(paramBean.getStr("TODO_CONTENT"));
        }
        msg.setCatalog(TodoUtils.TODO_CATLOG_MSG);
        
        List<Bean> receivers = getConfereesUsers(paramBean.getId());
        if (null == receivers || receivers.size() < 1) {
            return new OutBean().setError("会议通知还没有下发到个人！");
        }
        Assert.notNull(receivers, "接收人列表" + MsgItem.RECEIVER_LIST + "的值不能为NULL");

        int successCount = 0; //发送成功条数
        int receiversCount = receivers.size(); //要发送的用户个数
        /**
         * 设置接收人
         */
        for (Bean user : receivers) {
            TodoBean newToDoBean = new TodoBean(msg.copyOf());
            newToDoBean.setOwner(user.getStr("USER_CODE"));
            try {
                // 加入标识，通知此待办是由提醒触发的，待办发完后不再发提醒，防止嵌套循环
                newToDoBean.set("remindFlag", true);
                TodoUtils.insert(newToDoBean);
                successCount += 1; //发送成功条数记录
            } catch (Exception e) {
                log.error(e.getMessage(), e);
                return new OutBean().setError();
            }
        }
        if (successCount == receiversCount) { //发送条数和成功条数一致
            return new OutBean().setOk();
        }
        return new OutBean().setError("所选通知用户没有全部发送成功！");
    }
    
    /**
     * 获取当前会议与会人员列表，用户code
     * @param bookingId 会议室预定id
     * @return 用户id结合
     */
    private List<Bean> getConfereesUsers(String bookingId) {
        SqlBean sql1 = new SqlBean();
        sql1.selects("MEETING_ID").and("S_FLAG", 1).and("S_ODEPT", Context.getUserBean().getODeptCode());
        sql1.andIn("BOOKING_ID", bookingId);
        Bean meetingBean = ServDao.find("OA_MT_MEETING", sql1);
        if (null != meetingBean && !meetingBean.getId().isEmpty()) {
            SqlBean sql2 = new SqlBean();
            sql2.selects("USER_CODE").and("MEETING_ID", meetingBean.getId()).and("S_FLAG", 1).and("S_ODEPT",
                    Context.getUserBean().getODeptCode());
            return ServDao.finds("OA_MT_CONFEREE", sql2);
        }
        return null;
    }
}
