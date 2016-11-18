package com.rh.oa.zh.cw;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.codec.binary.Hex;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import com.rh.core.base.Bean;
import com.rh.core.base.Context;
import com.rh.core.org.UserBean;
import com.rh.core.serv.CommonServ;
import com.rh.core.serv.OutBean;
import com.rh.core.serv.ParamBean;
import com.rh.core.serv.ServDao;
import com.rh.core.serv.ServMgr;
import com.rh.core.util.Constant;
import com.rh.core.util.Dom4JHelper;
import com.rh.core.util.JsonUtils;
import com.rh.core.util.httpclient.HttpClientUtils;

/**
 * 费控系统查询oa接口
 * @author ruaho_hdy
 * 
 */
public class FkQueryServ extends CommonServ {

    /** 费控接口地址 */
    private static final String OA_FK_WSDL_ADDRESS = "OA_FK_WSDL_ADDRESS";

    /** 费控关联oa签报日志服务 */
    private static final String OA_FOR_CW_QUERY_LOG = "OA_FOR_CW_QUERY_LOG";
    
    /** 费控关联oa签报  回显服务 */
    private static final String OA_FOR_CW_QUERY_ECHO = "OA_FOR_CW_QUERY_ECHO";

    /**
     * 回写财务系统接口
     * @param paramBean 参数
     * @return 字符串
     */
    public OutBean sendRequest(ParamBean paramBean) {
        String result = null;
        OutBean out = new OutBean();
        Map<String, String> params = new HashMap<String, String>();
        Bean log = new Bean();
        try {
            List<Bean> dataList = JsonUtils.toBeanList(paramBean.getStr("DATA_ARRAY"));
            Document doc = DocumentHelper.createDocument();
            Element list = doc.addElement("list");
            for (Bean b : dataList) {
                Element bean = list.addElement("bean");
                bean.addAttribute("order_num", paramBean.getStr("order_num"));
                bean.addAttribute("oa_core", b.getStr("ENTITY_CODE"));
                bean.addAttribute("oa_title", b.getStr("TITLE"));
                bean.addAttribute("data_id", b.getStr("DATA_ID"));
                bean.addAttribute("oa_url", formatUrl(b));
                bean.addAttribute("oa_type", b.getStr("TYPE"));
            }
            String xml = Dom4JHelper.doc2String(doc, "UTF-8");
            params.put("dataList", xml);
            result = HttpClientUtils.getWebContent(Context.getSyConf(OA_FK_WSDL_ADDRESS, ""), params, "UTF-8");
            Bean resultBean = JsonUtils.toBean(result);
            log.set("ORDER_NUM", paramBean.getStr("order_num"));
            boolean isTrue = resultBean.getBoolean("success");
            if (isTrue) {
                log.set("STATE", formatState("Y"));
                out.setOk();
            } else {
                log.set("STATE", formatState("N"));
            }
            log.set("MESSAGE", xml);
        } catch (IOException e) {
            out.set("exception", e.getMessage());
            e.printStackTrace();
        }
        addLog(log);
        addRelateBack(paramBean);
        return out;
    }

//    @Override
//    protected void afterQuery(ParamBean paramBean, OutBean outBean) {
//        List<Bean> list = outBean.getDataList();
//        List<Bean> newList = new ArrayList<Bean>();
//        for (Bean b : list) {
//            if (b.getInt("S_WF_START") == Constant.NO_INT) { //已办结
//                newList.add(b);
//            }
//        }
//        outBean.setData(newList);
//    }
    
    /**
     * 格式化加密url
     * @param param 参数
     * @return url字符串
     */
    private String formatUrl(Bean param) {
        // "{'tTitle':'OA待阅列表','url':'OA_SY_COMM_TODO_READ.card.do?pkCode=""','menuFlag':4}";
        StringBuffer url = new StringBuffer("");
        url.append("{'tTitle':'").append(param.getStr("TITLE")).append("','url':'").append(param.getStr("SERV_ID"));
        url.append(".card.do?pkCode=").append(param.getStr("DATA_ID")).append("','menuFlag':3}");
        try {
            return "/sy/comm/page/page.jsp?openTab=" + Hex.encodeHexString(url.toString().getBytes("UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return url.toString();
    }

    /**
     * 添加关联日志
     * @param log 日志对象
     */
    private void addLog(Bean log) {
        ServMgr.act(OA_FOR_CW_QUERY_LOG, "save", log);
    }

    /**
     * 
     * addRelateBack FkQueryServ 费控关联OA签报列表选中回显 数据保存 2014-10-20 void
     * @param paramBean 费控单据
     */
    private void addRelateBack(ParamBean paramBean) {
        ParamBean back = new ParamBean();
        UserBean user = Context.getUserBean();
        back.set("USER_CODE", user.getCode());
        back.set("USER_WORK_NUM", user.getStr("USER_WORK_NUM"));
        back.set("ORDER_NUM", paramBean.getStr("order_num"));
        List<Bean> orders = ServDao.finds(OA_FOR_CW_QUERY_ECHO, back);
        back.set("DATA_IDS", paramBean.getStr("data_ids"));
        back.setServId(OA_FOR_CW_QUERY_ECHO);
        back.setAct(ServMgr.ACT_SAVE);
        if (orders.size() == 0) {
            back.setAddFlag(true);
        } else {
            back.setAddFlag(false);
            back.setId(orders.get(0).getId());
        }
        ServMgr.act(back);
    }

    /**
     * 获取状态显示
     * @param stateName 状态标识
     * @return 状态文字
     */
    private String formatState(String stateName) {
        if (stateName.equals("Y")) {
            return "成功";
        } else if (stateName.equals("N")) {
            return "失败";
        } else if (stateName.equals("W")) {
            return "警告";
        }
        return "失败";
    }

}
