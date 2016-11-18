package com.rh.bn.message.sms;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.Date;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.rh.core.base.Bean;
import com.rh.core.base.Context;
import com.rh.core.comm.msg.AbstractMsgSender;
import com.rh.core.org.UserBean;
import com.rh.core.util.DateUtils;

/**
 * 百年人寿项目短信发送类
 * @author Tanyh
 *
 */
public class BnSmsSender extends AbstractMsgSender {
    /** 日志对象 **/
    private Log log = LogFactory.getLog(BnSmsSender.class);
    @Override
    public void sendMsg(Bean msgBean) {
        //获取要发送消息的用户列表
        List<UserBean> receivers = msgBean.getList(MsgItem.RECEIVER_LIST);
        StringBuffer smsHead = new StringBuffer("");
        //公共信息
        smsHead.append("<?xml version='1.0' encoding='GBK'?>" +
        		"<Messages>" +
        		"<PublicInfo>" +
        		"<SystemCode>LIS</SystemCode>" +
        		"<ServiceCode>LIS000154</ServiceCode>" +
        		"</PublicInfo>");
        StringBuffer smsBody;
        for (UserBean userBean : receivers) {
            smsBody = new StringBuffer("");
            smsBody.append(smsHead);
            //主体信息
            smsBody.append("<Message>");
            //流水号
            smsBody.append("<MessageId>86" + DateUtils.getStringFromDate(new Date(), "yyyyMMddHHmmssms") + "</MessageId>");
            // 手机号
            if (userBean.getMobile().length() <= 0) {
                super.addFailtureExecResult(userBean.getId(), "[SMS TO:" + userBean.getName() + "，手机号为空]");
                continue;
            }
            smsBody.append("<MobileNums><MobileNum>" + userBean.getMobile() + "</MobileNum></MobileNums>");
            // 短信主题
            smsBody.append("<MessTopic>" + msgBean.getStr(MsgItem.REM_TITLE) + "</MessTopic>");
            // 短信内容
            smsBody.append("<SendData>您好，您有一项新的待办事务：" + msgBean.getStr(MsgItem.REM_TITLE) + "，请及时处理，谢谢！</SendData>");
            smsBody.append("<DataType>0</DataType><SendWay>0</SendWay><FixedDate></FixedDate><FixedTime></FixedTime>");
            // 管理机构
            smsBody.append("<UnitCode>86</UnitCode>");
            smsBody.append("<DealOrder>0</DealOrder><AnswerMatch>1</AnswerMatch>");
            smsBody.append("</Message>");
            smsBody.append("</Messages>");
            if (send(smsBody.toString())) {
                super.addSuccessExecResult(userBean.getId(), "[SMS TO:" + userBean.getName() + "，手机号：" + userBean.getMobile() + "]");
            } else {
                super.addFailtureExecResult(userBean.getId(), "[SMS TO:" + userBean.getName() + "，手机号：" + userBean.getMobile() + "]");
            }
        }
    }
    
    /**
     * 构造短信发送流，往短信服务器推送数据
     * @param content 短信报文 
     * @return boolean 是否发送成功
     */
    private boolean send(String content) {
        //获取配置中的短信服务器地址
        String bnSmsAddr = Context.getSyConf("BN_SMS_ADDR", "http://10.10.7.159:8080/sms/");
        //获取配置的加密后的用户名
        String userName = Context.getSyConf("BN_SMS_USERNAME", "ECC1AC1C8BBBE26063AB974BB48FC666");
        //获取配置的加密后的密码
        String password = Context.getSyConf("BN_SMS_PASSWORD", "ECC1AC1C8BBBE26063AB974BB48FC666");
        String smsUrl = bnSmsAddr + "interface/SysSendMsg.jsp?userName=" + userName + "&password=" + password;
        ByteArrayInputStream tByteArrayInputStream = null;
        try
        {
            int tConnectTimeOut = 2000; //连接超时
            int tReadTimeOut = 2000; //读取数据超时
            tByteArrayInputStream = new ByteArrayInputStream(content.getBytes("GBK"));
            URL jspUrl = new URL(smsUrl);
            URLConnection tURLConnection = jspUrl.openConnection();
            // （单位：毫秒）连接超时
            System.setProperty("sun.net.client.defaultConnectTimeout", String.valueOf(tConnectTimeOut));
            // （单位：毫秒）读操作超时
            System.setProperty("sun.net.client.defaultReadTimeout", String.valueOf(tReadTimeOut));
            tURLConnection.setDoOutput(true);
            tURLConnection.setRequestProperty("content-type", "text/html");
            OutputStream tOutputStream = tURLConnection.getOutputStream();
            int tLength = 0;
            byte[] buffer = new byte[4096];
            while ((tLength = tByteArrayInputStream.read(buffer)) != -1)
            {
                tOutputStream.write(buffer, 0, tLength);
            }
            tOutputStream.flush(); // 将输出流提交到服务器端
            tOutputStream.close(); // 关闭输出流对象
            tByteArrayInputStream.close();
            InputStream tInputStream = tURLConnection.getInputStream();
            String tMessage = "";
            while ((tLength = tInputStream.read(buffer)) != -1)
            {
                tMessage += new String(buffer, "GBK").trim();
            }
            tInputStream.close();
            if (tMessage.indexOf("<TransResult>1</TransResult>") >= 0) {
                return true;
            } else {
                return false;
            }
        } catch (IOException e) {
            log.error(e.getMessage());
            try {
                if (tByteArrayInputStream != null) {
                    tByteArrayInputStream.close();
                }
            } catch (IOException e1) {
                log.error(e1.getMessage());
            }
            return false;
        }
    }
}
