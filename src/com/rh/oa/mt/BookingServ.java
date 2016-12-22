package com.rh.oa.mt;

import java.util.Date;
import java.util.List;

import com.rh.core.base.Bean;
import com.rh.core.base.Context;
import com.rh.core.base.TipException;
import com.rh.core.serv.CommonServ;
import com.rh.core.serv.OutBean;
import com.rh.core.serv.ParamBean;
import com.rh.core.serv.ServDao;
import com.rh.core.serv.bean.SqlBean;
import com.rh.core.util.Constant;
import com.rh.core.util.DateUtils;
import com.rh.core.util.TaskLock;
import com.rh.oa.mt.util.MeetingRoomConstant;

/**
 * 会议室预定服务
 * @author hdy
 */
public class BookingServ extends CommonServ {

    private TaskLock lock = null;
    
    /**审批通过*/
    private static final String STATUS_PASS = "2";
    /**起草通知*/
    private static final String NOTICE_PASS = "1";
    /**没有起草通知*/
    private static final String NO_NOTICE = "2";
    /**此单子没有办结*/
    private static final String IS_STATE = "1";

    @Override
    protected void afterQuery(ParamBean paramBean, OutBean outBean) {
        List<Bean> outList = outBean.getDataList();
        for (Bean b : outList) {
            b.set("SHOW_FLAG", showListFlag(b.getStr("STATUS"), b.getStr("NOTICE_FLAG"), b.getStr("S_WF_STATE")));
        }
    }
    
    /**
     * 显示状态文字
     * @param status 审批状态
     * @param noticeFlag 是否发送通知
     * @param isState 此单子是否办结
     * @return 状态文字
     */
    private String showListFlag(String status, String noticeFlag, String isState) {
        //审批通过
        if (status.equals(STATUS_PASS)) {
            //发送通知
            if (noticeFlag.equals(NOTICE_PASS)) {
                return "已起草";
            } else if (isState.equals(IS_STATE) && noticeFlag.equals(NO_NOTICE)) {
                return "未起草";
            }
        }
        return "";
    }
    
    /**
     * 查看当前会议室是否已被预订
     * @param paramBean 传入的参数
     * @return 被占用，返回true；没有被占用，返回false
     */
    private boolean findMeetingRoomIsBooked(ParamBean paramBean) {
        String firstDate = paramBean.getStr(MeetingRoomConstant.START_TIME) + ":00";
        String secondDate = paramBean.getStr(MeetingRoomConstant.END_TIME) + ":00";
        //MeetingRoomConstant.isCross4TwoDateArea(startTime1, endTime1, startTime2, endTime2);
        SqlBean sql = new SqlBean();
        sql.and("MR_ID", paramBean.getStr("MR_ID")).andLT("STATUS", MeetingRoomConstant.BOOKING_STATUS_NOT_AGREE);
        sql.and("S_ODEPT", Context.getUserBean().getODeptCode()).and("S_FLAG", Constant.YES_INT);
        sql.andGTE("END_TIME", DateUtils.getStringFromDate(new Date(), DateUtils.FORMAT_DATETIME_HM));
        List<Bean> outList = ServDao.finds(paramBean.getServId(), sql);
        boolean isBookingFlag = false;
        for (Bean b : outList) {
            //当前修改记录
            if (b.getId().equals(paramBean.getId())) {
                continue;
            }
            if (MeetingRoomConstant.isCross4TwoDateArea(firstDate, secondDate,
                    b.getStr("START_TIME") + ":00", b.getStr("END_TIME") + ":00")) {
                isBookingFlag = true;
            }
        }
        return isBookingFlag;
        /*SqlBean sql = new SqlBean().and("MR_ID", paramBean.getStr("MR_ID"))
                .appendWhere(" and ((START_TIME > ?  and  START_TIME < ?)", firstDate, secondDate)
                .appendWhere(" or (START_TIME <= ? and END_TIME > ?))", firstDate, firstDate)
                .andLT("STATUS", MeetingRoomConstant.BOOKING_STATUS_NOT_AGREE)
                .and("S_ODEPT", Context.getUserBean().getODeptCode());
        int count = ServDao.count(paramBean.getServId(), sql);
        return count > 0;*/
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
     * @return 是否修改
     */
    private boolean isModified(ParamBean newBean) {
        if (newBean.isEmpty("MR_ID")) {
            newBean.set("MR_ID", newBean.getSaveOldData().getStr("MR_ID"));
        }
        if (newBean.isEmpty("START_TIME")) {
            newBean.set("START_TIME", newBean.getSaveOldData().getStr("START_TIME"));
        }
        if (newBean.isEmpty("END_TIME")) {
            newBean.set("END_TIME", newBean.getSaveOldData().getStr("END_TIME"));
        }
        return newBean.isEmpty("MR_ID") && newBean.isEmpty(MeetingRoomConstant.START_TIME)
                && newBean.isEmpty(MeetingRoomConstant.END_TIME);
    }
        

    /**
     * 
     * @param paramBean 参数
     */
    public void checkOccupy(ParamBean paramBean) {
        if (paramBean.getId() != null && paramBean.getId().length() > 0) {  
            //判断是否修改了会议室ID，开始结束时间
            if (isModified(paramBean)) {
                return;
            }
        }
                
        // 会议室ID
        String meetingRoomId = paramBean.getStr("MR_ID");

        // 会议开始时间
        String startTime = paramBean.getStr(MeetingRoomConstant.START_TIME);
        startTime += ":00";

        // 会议结束时间
        String endTime = paramBean.getStr(MeetingRoomConstant.END_TIME);
        endTime += ":00";
        
        if ((MeetingRoomConstant.getNewTimeNum(startTime) < new Date().getTime())
                || (MeetingRoomConstant.getNewTimeNum(endTime) < new Date().getTime())) {
            throw new TipException("预定时间早于系统时间，不可预订");
        }
        
        if(meetingRoomId.length()>0 && false){//选择了会议室才，加锁.暂时不用
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
                    throw new TipException("此会议室在" + startTime + "到" + endTime + "时间段已存在预定信息！");
                }
            } else {
                throw new TipException("此会议室正在预定！不可同时预定");
            }
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
        if (paramBean.getId().length() == 0) {
            // 提示【请预定】信息
            throw new TipException("会议室还未预定，取消错误");
        } 
        
        Bean query = new Bean(); // 查询参数
        query.set("BOOKING_ID", paramBean.getId()).set("STATUS", MeetingRoomConstant.BOOKING_STATUS_CANCEL);
        if (ServDao.count(MeetingRoomConstant.OA_MT_BOOKING_CODE, query) > 0) {
            //如果已经取消，则提示用户
            throw new TipException("此会议室预定已被其他人取消！");
        }
        
        //修改申请的状态和标题
        Bean oldBean = ServDao.find(paramBean.getServId(), paramBean.getId());
        paramBean.set("TITLE", "已取消：" + oldBean.getStr("TITLE"));
        paramBean.set("STATUS", MeetingRoomConstant.BOOKING_STATUS_CANCEL);
        return super.save(paramBean);
    }

    
    /**
     * 部门和用户字典表联动
     * @param paramBean 传入参数
     * @return 字典参数
     */
    public Bean linkDic(Bean paramBean) {
        Bean query = new Bean();
        query.set(Constant.PARAM_SELECT, "DEPT_CODE,DEPT_NAME")
                .set(Constant.PARAM_WHERE, " AND USER_CODE = ?")
                .set(Constant.PARAM_PRE_VALUES, paramBean.getStr("USER_CODE"));
        return ServDao.find(paramBean.getStr("SERVID"), paramBean);
    }
    
    /**
     * 修改当前会议同时是否起草状态位
     * @param paramBean 参数
     * @return 返回结果集
     */
    public OutBean updataBooking(ParamBean paramBean) {
        Bean updateObj = new Bean();
        updateObj.setId(paramBean.getStr("PK_CODE")).set("NOTICE_FLAG", paramBean.getStr("NOTICE_FLAG"));
        Bean returnBean = ServDao.update(paramBean.getStr("SERV_ID"), updateObj);
        if (returnBean.getStr("NOTICE_FLAG").equals(paramBean.getStr("NOTICE_FLAG"))) {
            return new OutBean().setMsg("OK");
        }
        return new OutBean().setError("ERROR");
    }
}
