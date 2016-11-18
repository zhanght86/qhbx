package com.rh.oa.zh.cw.imp;

import java.io.IOException;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import com.rh.core.base.Bean;
import com.rh.core.org.UserBean;
import com.rh.core.org.mgr.UserMgr;
import com.rh.core.serv.ServDao;
import com.rh.core.serv.ServMgr;
import com.rh.core.serv.bean.SqlBean;
import com.rh.core.util.Constant;
import com.rh.core.util.Dom4JHelper;
import com.rh.oa.zh.cw.service.CwFlowServ;

/**
 * 接口实现
 * @author ruaho_hdy
 *
 */
public class CwFlowServImp implements CwFlowServ {
    
    private static Log log = LogFactory.getLog(CwFlowServImp.class);

    /**流经表服务id*/
    private static final String OA_SY_SERV_FLOW_EXT = "OA_SY_SERV_FLOW_EXT";
    
    /**流经log服务id*/
    private static final String EXT_SERV_FLOW_LOG = "EXT_SERV_FLOW_LOG";
    
    /**非流经非传阅，财务系统过来的数据*/
    private static final int FLOW_FLAG = 3;
    
    public String addFlow(String workNum, String dataId) {
      //获取用户工号
        String userWorkNum = workNum;
        //判断工号是否存在
        if (StringUtils.isBlank(userWorkNum)) {
            return createXml("N", "error", "this worlNum is empty");
        }
        
        //获取此工号用户信息
        UserBean user = UserMgr.getUserByWorkNum(userWorkNum);
        //不存在该用户，返回错误信息
        if (user.isEmpty()) {
            return createXml("N", "error", "the user who has workNum :" + userWorkNum + " is empty");
        }
        
        Bean bean = new Bean();
        //是否传递了表单数据信息
        if (StringUtils.isBlank(dataId)) {
            return createXml("N", "error", "this dataId is empty");
        }
        
        SqlBean sql = new SqlBean();
        //查询此流经信息在流经表中是否存在
        sql.and("DATA_ID", dataId).and("OWNER_ID", user.getCode()).and("S_ODEPT", user.getODeptCode());
        if (ServDao.count(OA_SY_SERV_FLOW_EXT, sql) > 0) {
            return createXml("W", "warn", "this flow is exit");
        }
        
        //添加流经记录信息
        bean.set("DATA_ID", dataId).set("OWNER_ID", user.getCode()).set("S_ODEPT", user.getODeptCode())
            .set("FLOW_FLAG", FLOW_FLAG);
        Bean out = ServMgr.act(OA_SY_SERV_FLOW_EXT, "save", bean);
        
        //保存log
        Bean logBean = new Bean();
        logBean.set("DATA_ID", dataId).set("USER_WORK_NUM", userWorkNum).set("LOG_FLAG", Constant.NO);
        Bean logResult = ServMgr.act(EXT_SERV_FLOW_LOG, "save", logBean);
        
        //返回成功信息
        if (!out.isEmpty()) {
            logResult.set("LOG_FLAG", Constant.YES);
            ServDao.save(EXT_SERV_FLOW_LOG, logResult);
            return createXml("Y", "successful", "successful");
        }
        return null;
    }
    
    /**
     * 获取xml返回报文数据
     * @param status 状态
     * @param message 消息
     * @param description 描述
     * @return 格式化之后的xml字符串
     */
    private String createXml(String status, String message, String description) {
        Document doc = DocumentHelper.createDocument();
        Element result = doc.addElement("result");
        result.addElement("status").setText(status);
        result.addElement("message").setText(message);
        result.addElement("description").setText(description);
        String xml = "";
        try {
            xml = Dom4JHelper.doc2String(doc, "UTF-8");
        } catch (IOException e) {
            log.error("转换xml字符串异常：" + e.getMessage());
            e.printStackTrace();
        }
        return xml;
    }
}
