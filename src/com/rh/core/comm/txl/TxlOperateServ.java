package com.rh.core.comm.txl;

import javax.mail.MessagingException;

import com.rh.core.base.Bean;
import com.rh.core.comm.todo.TodoBean;
import com.rh.core.comm.todo.TodoUtils;
import com.rh.core.comm.todo.TodoUtils.ToDoItem;
import com.rh.core.serv.CommonServ;
import com.rh.core.serv.ServDao;
import com.rh.core.serv.ServMgr;
import com.rh.core.serv.ServUtils;
import com.rh.core.util.Constant;
import com.rh.core.util.DateUtils;
import com.rh.core.util.email.MailSender;

/**
 * 用户通讯录操作处理类
 * @author ZJW
 * 
 */
public class TxlOperateServ extends CommonServ {
    /**
     * 向用户申请密级信息权限方法，用于用户权限申请按钮
     * @param paramBean 申请内容参数
     */
    public void applyAdd(Bean paramBean) {
        // 取得服务ID
        String servId = paramBean.getStr("serv");
        Bean applyBean = new Bean();
        applyBean.set("USER_CODE", paramBean.getStr("USER_CODE"));
        applyBean.set("APPLY_USER", paramBean.getStr("APPLY_USER"));
        applyBean.set("APPLY_CONTENT", paramBean.getStr("APPLY_CONTENT"));
        applyBean.set("APPLY_MARK", paramBean.getStr("APPLY_MARK"));
        applyBean.set("APPLY_TIME", DateUtils.getDatetime());
        applyBean.set("USER_AUDIT_FLAG", "2");

        applyBean = ServDao.save(servId, applyBean);

        TodoBean todoBean = new TodoBean();
        if (paramBean.getStr("APPLY_CONTENT").equals("USER_MOBILE=1")) {
            todoBean.setTitle("手机号码查看申请");
        } else {
            todoBean.setTitle("简历查看申请");
        }
        todoBean.setSender(applyBean.getStr("APPLY_USER"));

        todoBean.setCode(servId);
        // 取得服务名称
        Bean servBean = ServUtils.getServDef(servId);
        todoBean.setCodeName(servBean.getStr("SERV_NAME"));

        todoBean.setUrl("SY_COMM_TEMPL.show.do?pkCode=SY_COMMU_CENTER&typeNum=2&model=view&"
                + paramBean.getStr("APPLY_CONTENT") + "&DATA_ID=" + applyBean.getStr("APPLY_ID"));
        todoBean.setObjectId1(applyBean.getStr("APPLY_ID"));
        todoBean.setObjectId2(applyBean.getStr("APPLY_ID"));
        todoBean.setContent(applyBean.getStr("APPLY_CONTENT"));
        todoBean.setOwner(applyBean.getStr("USER_CODE"));
        TodoUtils.insert(todoBean);

    }

    /**
     * 被申请人对请求处理操作方法，用于对用户请求处理
     * @param paramBean 处理的操作参数
     */
    public void doApply(Bean paramBean) {
        // 取得服务ID
        String servId = paramBean.getStr("serv");
        Bean applyBean = ServDao.find(servId, paramBean.getStr("APPLY_ID"));
        applyBean.set("APPLY_CONTENT", paramBean.getStr("APPLY_CONTENT"));
        applyBean.set("USER_AUDIT_FLAG", "1");
        applyBean = ServDao.save(servId, applyBean);

        // 插入共享表
        Bean sharBean = new Bean();
        String sharServId = "SY_COMM_ADDRESS_ASSIST";
        sharBean.set("USER_CODE", applyBean.getStr("USER_CODE"));
        sharBean.set("SHARE_TO", applyBean.getStr("APPLY_USER"));

        Bean sharTemp = sharBean.copyOf();
        sharTemp = ServDao.find(sharServId, sharBean);
        String shareContent = "";
        String applyContent = applyBean.getStr("APPLY_CONTENT");
        String[] ctrs = null;
        // 如果共享权限表中存在则更新权限内容，否则插入权限内容
        if (sharTemp != null) {
            sharBean.setId(sharTemp.getId());
            shareContent = sharTemp.getStr("SHARE_CONTENT");
            for (String content : applyContent.split(Constant.SEPARATOR)) {
                ctrs = content.split("=");
                String fields = ctrs[0];
                if (!shareContent.contains(fields) && "1".equals(ctrs[1])) {
                    shareContent = shareContent + "," + fields;
                }
            }
        } else {
            for (String content : applyContent.split(Constant.SEPARATOR)) {
                ctrs = content.split("=");
                String fields = ctrs[0];
                if (!shareContent.contains(fields) && "1".equals(ctrs[1])) {
                    shareContent = shareContent + fields + ",";
                }
            }
            if (!"".equals(shareContent)) {
                shareContent = shareContent.substring(0, shareContent.length() - 1);
            }
        }

        sharBean.set("SHARE_CONTENT", shareContent);
        sharBean.set("SHARE_TYPE", "1");
        ServDao.save(sharServId, sharBean);

        // 处理完成后将待办信息生成已办
        Bean todoBean = new Bean();
        todoBean.set(ToDoItem.TODO_CODE, servId);
        todoBean.set(ToDoItem.SEND_USER_CODE, applyBean.getStr("APPLY_USER"));
        todoBean.set(ToDoItem.TODO_OBJECT_ID1, applyBean.getStr("APPLY_ID"));
        todoBean.set(ToDoItem.OWNER_CODE, applyBean.getStr("USER_CODE"));
        todoBean = ServDao.find(ServMgr.SY_COMM_TODO, todoBean);
        TodoUtils.endById(todoBean);

    }

    /**
     * 发送邮件提醒 暂时保留
     * @param paramBean 参数
     */
    public void sendMailToUser(Bean paramBean) {
        // 插入提醒消息表
        /*
         * Bean remindBean = new Bean(); String sharServId = "SY_COMM_REMIND"; UserBean user = Context.getUserBean();
         * remindBean.set("USER_CODE", user.getCode()); remindBean.set("REM_TITLE", "您有一封新邮件");
         * 
         * remindBean.set("REM_CONTENT", paramBean.getStr("MAIL_CONTENT")); remindBean.set("S_USER", user.getCode());
         * remindBean.set("TYPE", "EMAIL"); remindBean.set("SERV_ID", sharServId); RemindMgr.add(remindBean,
         * user.getStr("USER_CODE"));
         */

        MailSender mailSender = new MailSender("smtp.126.com", "jinshun108108@126.com", "js,zjw844771");
        mailSender.setMailTo("zhangjinwei@staff.zotn.com");
        StringBuffer content = new StringBuffer(paramBean.getStr("MAIL_CONTENT"));
        /*
         * content.append("<div>张金伟："); content.append("<div style='margin-left:40px'>你好！很高兴收到你的测试邮件！</div>");
         * content.append("<div style='margin-left:40px'>这是一个测试</div>"); content.append("</div>");
         */
        mailSender.setBody(content.toString());
        mailSender.setSubject("测试邮件");
        mailSender.setAuth(true);
        mailSender.setBodyIsHTML(true);
        mailSender.setDebug(true);
        mailSender.setMailFrom("jinshun<jinshun108108@126.com>");

        try {
            mailSender.send();
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
}
