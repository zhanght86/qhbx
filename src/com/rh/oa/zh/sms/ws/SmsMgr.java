package com.rh.oa.zh.sms.ws;

import java.io.IOException;
import java.rmi.RemoteException;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.rpc.ServiceException;

import org.apache.commons.lang.RandomStringUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import com.rh.core.base.Bean;
import com.rh.core.util.DateUtils;
import com.rh.core.util.Dom4JHelper;
import com.rh.core.util.Lang;
import com.rh.oa.zh.sms.ws.impl.SmsServiceImplServiceLocator;

/**
 * 短信接口调用
 * @author ruaho_hdy
 *
 */
public class SmsMgr {
    /**
     * 系统代码
     */
    private static final String SYSTEM_CODE = "OA";
    
    /**
     * 发送短信
     * 短信返回报文
     * <?xml version="1.0" encoding="GBK"?>
     * <INSUREQRET>
     *   <!--主结点信息-->
     *   <MAIN>
     *     <!--交易流水号-->
     *     <SERIALDECIMAL />
     *     <!--返回码-->
     *     <RESULTCODE />
     *     <!—返回信息 -->
     *     <ERR_INFO />
     *     <!--业务类型-->
     *     <BUSINESS_CODE />
     *   </MAIN>
     * </INSUREQRET>
     * @param doc 调用报文
     * @param address 接口地址
     * @throws ServiceException 异常
     * @throws IOException 异常
     * @return 短信返回报文
     */
    public static String sendMessage(Document doc, String address) throws IOException, ServiceException {
        String xml = Dom4JHelper.doc2String(doc, "GBK");
        SmsServiceImplServiceLocator locator = new SmsServiceImplServiceLocator();
        locator.setSmsServiceImplPortEndpointAddress(address);
        return locator.getSmsServiceImplPort().insertInto10001(xml);
    }
    
    /**
     * 创建短信请求报文
     * <?xml version="1.0"encoding="GBK"?>
     * <INSUREQ>
     *   <!--报文头信息-->
     *   <HEAD>
     *      <!--请求用户名-->
     *      <USER_NAME/>
     *      <!--请求用户密码-->
     *      <USER_PSW/>
     *      <!--系统代码-->
     *      <SYSTEM_CODE/>
     *   </HEAD>
     *   <!--主结点信息-->
     *   <MAIN>
     *       <!--交易码-->
     *       <TRANSRNO>10001</TRANSRNO>
     *       <!--交易流水号唯一性-->
     *       <SERIALNUMBER/>
     *       <!--交易时间-->
     *       <TRANSRDATE/>
     *   </MAIN>
     *   <!--短信列表-->
     *   <SMS_SEND_LIST>
     *       <!-- 短信(循环体)-->
     *       <SMS_SEND_DATE>
     *           <!--短信编号-->
     *           <ID/>
     *           <!--接收人电话号码-->
     *           <PHONECODE/>
     *           <!--发送时间-->
     *           <SENDTIME/>
     *           <!--发送文本-->
     *           <SENDTEXT/>
     *       </SMS_SEND_DATE>
     *   </SMS_SEND_LIST>
     * </INSUREQ>
     * @param msgId 日志ID
     * @return 返回短信报文
     */
    public static Document createSmsXML(String msgId) {
        Document doc = DocumentHelper.createDocument();
        Element root = doc.addElement("INSUREQ");
        Element head = root.addElement("HEAD");
        head.addElement("USER_NAME").setText("NetSales");
        head.addElement("USER_PSW").setText("SMS95585");
        head.addElement("SYSTEM_CODE").setText(SYSTEM_CODE);
        Element main = root.addElement("MAIN");
        main.addElement("TRANSRNO").setText("10001");
        main.addElement("SERIALNUMBER").setText(msgId); // 交易流水号
        String datetime = DateUtils.getStringFromDate(new Date(), DateUtils.FORMAT_DATETIME);
        main.addElement("TRANSRDATE").setText(datetime);
        
        return doc;
    }
    
    /**
     * 添加发送人对象
     * @param root 根节点
     * @param cellphone 手机号码
     * @param content 发送内容
     * @param deptCode 机构部门编码
     */
    public static void addMsgList(Element root, String cellphone, String content, String deptCode) {
        Element sendList = root.element("SMS_SEND_LIST");
        if (sendList == null) {
            sendList = root.addElement("SMS_SEND_LIST");
        }
        Element sms = sendList.addElement("SMS_SEND_DATE");
        String id = SYSTEM_CODE + DateUtils.getStringFromDate(new Date(), "yyyyMMddHHmmssSSS") 
                + RandomStringUtils.randomNumeric(6);
        sms.addElement("ID").setText(id); // 短信编号
        sms.addElement("PHONECODE").setText(cellphone);
        sms.addElement("SENDTIME").setText(DateUtils.getStringFromDate(new Date(), "yyyyMMddHHmmss"));
        sms.addElement("SENDTEXT").setText(content);
        sms.addElement("SMSTYPE").setText(SYSTEM_CODE + "TX00");
        sms.addElement("SERVICEID").setText("1001");
        sms.addElement("COMPANY").setText(deptCode);
    }
    
    /**
     * 解析xml字符串
     * @param xml xml字符串
     * @return xml节点对象
     * @throws DocumentException 异常
     */
    public static Bean parseXmlStr(String xml) throws DocumentException {
        Document doc = DocumentHelper.parseText(xml);
        Element rootElt = doc.getRootElement(); // 获取根节点
        Element mainret = rootElt.element("MAINRET"); // 获取根节点下的子节点MAINRET
        @SuppressWarnings("unchecked")
        List<Element> list = mainret.elements();
        Bean out = new Bean();
        for (Element e : list) {
            out.set(e.getName(), e.getText());
        }
        return out;
    }
    
    /**
     * 手机号有效性校验
     * @param mobile 手机号
     * @return 是否是正确的手机号码
     */
    public static boolean isMobileNO(String mobile) {
        boolean flag = false;
        try {
            //Pattern p = Pattern.compile("^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$");
            Pattern p = Pattern.compile("^\\d{11}$");
            Matcher m = p.matcher(mobile);
            flag = m.matches();
        } catch (Exception e) {
            flag = false;
        }
        return flag;
    }
    
    /**
     * 测试短信接口
     * @param args 参数
     * @throws ServiceException 异常
     * @throws RemoteException 异常
     */
    public static void main(String[] args) throws RemoteException, ServiceException {
        // 生成日志ID
        String msgId = Lang.getUUID();
        //http://100.16.2.5:8080/flexWszh/sms/SmsNetApp
        //http://65.0.9.10:7006/flexWszh/sms/SmsNetApp
        //http://65.0.9.10:7006/flexsmsInterface/sms/SmsNetApp
        //String address = "http://65.0.9.10:7006/flexsmsInterface/sms/SmsNetApp";
        String address = "http://10.192.48.23:8001/flexsmsInterface/sms/SmsNetApp";
        // 创建短信接口报文
        Document doc = SmsMgr.createSmsXML(msgId);
        /*for (int i = 0; i < 10; i++) {
            String cellphone = "1851016695";
            cellphone = cellphone + i;
            cellphone = isMobileNO(cellphone) ? cellphone : "";
            System.out.println(cellphone);
            SmsMgr.addMsgList(doc.getRootElement(), cellphone, "您的验证码为：" + RandomStringUtils.randomNumeric(6),
                    "000001");
        }*/
        //String cellphone = "18610217193";
        //String cellphone = "18610488037"; //安安
        //String cellphone = "15801503235"; //马哥
        //String cellphone = "13811620098"; //杨瑜
        String cellphone = "18810869108"; //翠翠
        cellphone = isMobileNO(cellphone) ? cellphone : "";
        System.out.println(cellphone);
        SmsMgr.addMsgList(doc.getRootElement(), cellphone, "您的验证码为：" + RandomStringUtils.randomNumeric(6),
                "000001");
        System.out.println(doc.asXML());
        // 插入日志
        // 调用短信接口
        String responseXML = "";
        try {
            responseXML = SmsMgr.sendMessage(doc, address);
            System.out.println("responseXML:" + responseXML);
            Bean out = parseXmlStr(responseXML);
            System.out.println(out.getStr("RESULTCODE") + "," + out.getStr("RESULT_INFO") + ","
                    + out.getStr("SERIALNUMBER"));
        } catch (IOException e) {
            e.printStackTrace();
        } catch (DocumentException d) {
            d.printStackTrace();
        }
    }
}
