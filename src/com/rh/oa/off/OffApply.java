package com.rh.oa.off;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.rh.core.base.Bean;
import com.rh.core.base.Context;
import com.rh.core.comm.msg.MsgSender.MsgItem;
import com.rh.core.comm.todo.TodoBean;
import com.rh.core.comm.todo.TodoUtils;
import com.rh.core.org.UserBean;
import com.rh.core.org.mgr.UserMgr;
import com.rh.core.serv.CommonServ;
import com.rh.core.serv.OutBean;
import com.rh.core.serv.ParamBean;
import com.rh.core.serv.ServDao;
import com.rh.core.serv.ServMgr;
import com.rh.core.serv.bean.SqlBean;
import com.rh.core.util.lang.Assert;

/**
 * 办公用品申请
 * @author ruaho_hdy
 *
 */
public class OffApply extends CommonServ {

    /**
     * 发送办公用品领用通知
     * @param paramBean 参数
     * @return 返回结果集
     */
    public OutBean sendNoticeMsg(ParamBean paramBean) {
        TodoBean msg = new TodoBean();
        msg.setTitle(paramBean.getStr("TODO_TITLE")); //发送标题
        //如果不是从任务调度中取的
        if (StringUtils.isBlank(paramBean.getStr("SEND_USER"))) {
            msg.setSender(Context.getUserBean().getCode()); //获取当前用户，作为发送用户
        } else {
            msg.setSender(paramBean.getStr("SEND_USER")); //发送用户
        }
        msg.setCode("TODO_REMIND");
        // 取得服务ID
        String servId = paramBean.getServId();
        msg.setServId(servId);
        // 取得服务名称
        msg.setCodeName("办公用品领用");
        msg.setUrl(servId + ".showDialog.do");
        msg.setObjectId1(paramBean.getStr("APPLY_ID"));
        msg.setObjectId2(paramBean.getStr("APPLY_ID"));
        if (paramBean.isNotEmpty("TODO_CONTENT")) {
            msg.setContent(paramBean.getStr("TODO_CONTENT"));
        }
        msg.setCatalog(TodoUtils.TODO_CATLOG_MSG);
        
        List<UserBean> receivers = getSqlString(paramBean.getStr("ids"));
        Assert.notNull(receivers, "接收人列表" + MsgItem.RECEIVER_LIST + "的值不能为NULL");

        int successCount = 0; //发送成功条数
        int receiversCount = receivers.size(); //要发送的用户个数
        /**
         * 设置接收人
         */
        for (UserBean rUserBean : receivers) {
            TodoBean newToDoBean = new TodoBean(msg.copyOf());
            newToDoBean.setOwner(rUserBean.getCode());
            try {
                // 加入标识，通知此待办是由提醒触发的，待办发完后不再发提醒，防止嵌套循环
                //newToDoBean.set("remindFlag", true);
                TodoUtils.insert(newToDoBean);
                successCount += 1; //发送成功条数记录
            } catch (Exception e) {
                log.error(e.getMessage(), e);
                return new OutBean().setError();
            }
        }
        if (successCount == receiversCount) { //发送条数和成功条数一致
            if (!updateOffApplyData(paramBean)) {
                return new OutBean().setError("ERROR,此通知已经被发送！");
            }
            if (ServMgr.act(paramBean).isOk()) {
                return new OutBean().setOk();
            }
        } else {
            return new OutBean().setError("ERROR,所选通知用户没有全部发送成功！");
        }
        return new OutBean().setError();
    }
    
    /**
     * 格式化sql语句
     * @param ids 传过来的id
     * @return 格式化之后的SqlBean
     */
    private List<UserBean> getSqlString(String ids) {
        String[] idsArray = ids.split(",");
        List<UserBean> result = new ArrayList<UserBean>();
        for (String userId :idsArray) {
            if (userId.length() <= 0) {
                continue;
            }
            UserBean ub = UserMgr.getUser(userId);
            result.add(ub);
        }
        return result;
    }
    
    /**
     * 修改审批单数据
     * @param paramBean 参数
     * @return 返回修改成功状态
     */
    private boolean updateOffApplyData(ParamBean paramBean) {
        SqlBean sql = new SqlBean();
        sql.and("APPLY_ID", paramBean.getStr("pk")).and("NOTICE_STATUS", 2);
        Bean bean = ServDao.find(paramBean.getStr("servId"), sql);
        if (null == bean || bean.isEmpty()) {
            paramBean.setServId(paramBean.getStr("servId"));
            paramBean.setId(paramBean.getStr("pk"));
            paramBean.set("NOTICE_STATUS", "2");
            paramBean.setAddFlag(false);
            paramBean.setAct(ServMgr.ACT_SAVE);
            return true;
        }
        return false;
    }
}
