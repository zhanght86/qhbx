package com.rh.core.comm.msg;

import java.util.List;

import org.apache.log4j.Logger;

import com.rh.core.base.Bean;
import com.rh.core.comm.ConfMgr;
import com.rh.core.org.UserBean;
import com.rh.core.org.mgr.UserMgr;
import com.rh.core.serv.ServDao;
import com.rh.core.util.email.MailSender;
import com.rh.core.util.lang.Assert;

/**
 * 邮件系统消息发送实现类
 * @author yangjy
 * 
 */
public class MailMsgSender extends AbstractMsgSender {
    private Logger log = Logger.getLogger(this.getClass());
    /**
     * 发送邮件提醒的邮件服务器地址
     */
    private static final String HOST = "MAIL_MESSAGE_HOST";
    
    /**
     * 发送邮件提醒的邮件服务器端口
     */
    private static final String PORT = "MAIL_MESSAGE_PORT";

    /**
     * 发送邮件提醒的邮箱用户名
     */
    private static final String USER_NAME = "MAIL_MESSAGE_USER_NAME";

    /**
     * 发送邮件系统的邮箱密码
     */
    private static final String PASSWORD = "MAIL_MESSAGE_PASSWORD";

    /**
     * 发送邮件提醒的邮箱地址
     */
    private static final String SENDER = "MAIL_MESSAGE_SENDER";

    /**
     * 发送邮件提醒的邮箱是否要求用户验证
     */
    private static final String IF_AUTH = "MAIL_MESSAGE_IF_AUTH";
    
    @Override
    public void sendMsg(Bean msgBean) {
        MailSender mailSender = createMailSender();
        List<UserBean> receivers = msgBean.getList(MsgItem.RECEIVER_LIST);

        StringBuilder str = new StringBuilder();
        for (UserBean userBean : receivers) {
            if (userBean.getEmail().length() > 0) {
            	str.append(userBean.getEmail()).append(",");
            } else if (userBean.isNotEmpty("JIANGANG_FLAG")
                    && userBean.getInt("JIANGANG_FLAG") == 1) { // 是否为兼岗用户，则取主账号的邮箱
            	// 找出兼岗数据
            	Bean jianGangBean = ServDao.find("SY_ORG_USER_JIANGANG", (new Bean()).set("USER_CODE", userBean.getCode()));
            	if (jianGangBean != null && jianGangBean.isNotEmpty("ORIGIN_USER_CODE")) {
            		// 找出主账号信息
            		UserBean originUser = UserMgr.getUser(jianGangBean.getStr("ORIGIN_USER_CODE"));
            		// 主账号邮箱不为空
            		if (originUser != null && originUser.isNotEmpty("USER_EMAIL")) {
            			str.append(originUser.getEmail()).append(",");
            		}
            	}
            }
        }
        String mailTo = "";
        if (str.length() > 0) {
            mailTo = str.substring(0, str.length() - 1);
        }

        Assert.hasText(mailTo, "接收人的邮箱不能为空！");
        // 接收人
        mailSender.setMailTo(mailTo);

        // 标题
        mailSender.setSubject(msgBean.getStr(MsgItem.REM_TITLE));
        // 内容
        mailSender.setBody(msgBean.getStr(MsgItem.REM_CONTENT));
        
        try {
            mailSender.send();
            addLog(receivers, true, mailTo);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            addLog(receivers, false, mailTo);
        }
       
    }
    
    /**
     * 增加日志
     * @param receivers 邮件接收用户
     * @param isOk 是否成功
     * @param mailTo 操作日志
     */
    private void addLog(List<UserBean> receivers, boolean isOk, String mailTo) {
        for (UserBean userBean : receivers) {
            if (isOk) {
                super.addSuccessExecResult(userBean.getId(), "[mail:" + mailTo + "]");
            } else {
                super.addFailtureExecResult(userBean.getId(), "[mail:" + mailTo + "]");
            }
        }
    }

    /**
     * 
     * @return 邮件发送对象
     */
    private MailSender createMailSender() {

        String userName = ConfMgr.getConf(USER_NAME, "");
        String password = ConfMgr.getConf(PASSWORD, "");
        String host = ConfMgr.getConf(HOST, "");
        String port = ConfMgr.getConf(PORT, "");
        String senderAddress = ConfMgr.getConf(SENDER, "");
        boolean ifAuth = ConfMgr.getConf(IF_AUTH, false);

        MailSender mailSender = new MailSender(host, port, userName, password);
        mailSender.setHost(host);
        mailSender.setMailFrom(senderAddress);
        mailSender.setAuth(ifAuth);
        mailSender.setBodyIsHTML(true);

        return mailSender;
    }

}
