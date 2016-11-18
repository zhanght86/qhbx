package com.rh.oa.msg;

import java.util.List;

import org.dom4j.Document;

import com.rh.core.base.Bean;
import com.rh.core.base.Context;
import com.rh.core.comm.msg.AbstractMsgSender;
import com.rh.core.org.UserBean;
import com.rh.core.org.mgr.UserMgr;
import com.rh.core.util.Strings;
import com.rh.oa.zh.seal.SealMgr;
import com.rh.oa.zh.sms.ws.SmsMgr;

/**
 * 短信接口
 * @author ruaho_hdy
 *
 */
public class SmsMsgSender extends AbstractMsgSender {

    @Override
    public void sendMsg(Bean msgBean) {
        //获取要发送消息的用户列表
        List<UserBean> receivers = msgBean.getList(MsgItem.RECEIVER_LIST);
        //获取配置中的WSDL地址
        String ucStarWsdlAddr = Context.getSyConf("SMS_WSDL_ADDR", "");
        //发送人机构部门编码
        String odeptNum = "";
        //发送人
        UserBean sendUser = null;
        //消息主键
        String remindId = msgBean.getId();
        try {
            //获取发送人对象
            sendUser = UserMgr.getUser(msgBean.getStr("S_USER"));
            odeptNum = SealMgr.getODeptNum(sendUser.getODeptCode());
        } catch (Exception e) {
            //发送人不存在机构部门编码，则从接收人中获取
            odeptNum = SealMgr.getODeptNum(receivers.get(0).getODeptCode());
        }
        try {
            //创建短信接口报文
            Document doc = SmsMgr.createSmsXML(remindId);
            int havaNoMobileNO = 0; //推送短信用户没有手机号个数
            for (UserBean user : receivers) {
                //校验手机号
                String mobileNum = user.getMobile().replaceAll("\\s*", "");
                //手机号不存在写入log，进行下一条记录
                if (null == mobileNum || mobileNum.equals("")) {
                    havaNoMobileNO += 1;
                    this.addFailtureExecResult(user.getCode(), "手机号为空");
                    continue;
                //手机号不合法写入log，进行下一条记录
                } else if (!SmsMgr.isMobileNO(mobileNum)) {
                    havaNoMobileNO += 1;
                    this.addFailtureExecResult(user.getCode(), "手机号为：" + mobileNum + "不合法");
                    continue;
                }
                //添加发送记录
                SmsMgr.addMsgList(doc.getRootElement(), mobileNum,
                        Strings.unescapeHtml(msgBean.getStr(MsgItem.REM_TITLE)), odeptNum);
            }
            //如果接收人中一个手机号都没有，则没有必要再去调用接口
            if (havaNoMobileNO == receivers.size()) {
                return;
            }
            //发送消息并获取返回报文
            String result = SmsMgr.sendMessage(doc, ucStarWsdlAddr);
            //获取返回报文数据对象
            Bean out = SmsMgr.parseXmlStr(result);
            
            //如果返回信息成功，则写入成功记录
            if (out.getStr("RESULTCODE").equals("0000")) {
                for (UserBean user : receivers) {
                    if (!existsExecResult(user.getCode())) {
                        this.addSuccessExecResult(user.getId(),
                                out.getStr("RESULTCODE") + "," + out.getStr("SERIALNUMBER"));
                    }
                }
            //如果返回信息失败，则写入记录并添加日志
            } else {
                String errorStr = "编码为：" + remindId + "消息推送出错。错误编号：" + out.getStr("RESULTCODE") + ",错误信息为："
                        + out.getStr("RESULT_INFO") + "," + "交易流水号为：" + out.getStr("SERIALNUMBER");
                log.warn(errorStr);
                for (UserBean user : receivers) {
                    if (!existsExecResult(user.getCode())) {
                        this.addFailtureExecResult(user.getId(),
                                out.getStr("RESULTCODE") + "," + out.getStr("SERIALNUMBER"));
                    }
                }
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }
}
