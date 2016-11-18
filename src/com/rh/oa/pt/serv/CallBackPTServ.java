package com.rh.oa.pt.serv;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import org.apache.commons.codec.binary.Hex;

import com.rh.core.base.Bean;
import com.rh.core.base.Context;
import com.rh.core.base.Context.THREAD;
import com.rh.core.comm.todo.TodoUtils;
import com.rh.core.org.UserBean;
import com.rh.core.org.mgr.UserMgr;
import com.rh.core.serv.CommonServ;
import com.rh.core.serv.OutBean;
import com.rh.core.serv.ParamBean;
import com.rh.core.serv.ServMgr;
import com.rh.core.util.Constant;
import com.rh.core.util.EncryptUtils;
import com.rh.core.util.JsonUtils;
import com.rh.core.util.RequestUtils;

/**
 * 门户跨域执行返回值
 * @author ruaho_hdy
 *
 */
public class CallBackPTServ extends CommonServ {
    
    //过期时间
    private final long pastDueTime = Long.parseLong(Context.getSyConf("CROSS_DOMAIN_OVERTIME", "1800000"));
    
    /**
     *获取指定数据返回值 
     * @param paramBean 参数bean
     * @return 结果集
     */
    public OutBean getCallBackJson(ParamBean paramBean) {
        
        //获取重新封装的数据
        Map<String, Object> map = parseUrl(paramBean.getStr("info"));
        //如果验证通过
        if (decideUrl(map)) {
//            String workNum = (String) map.get("workNum");
//            if (workNum.isEmpty()) {
//                return new OutBean().setError("用户工号为空");
//            }
//            if (workNum.isEmpty()) {
//                return new OutBean().setError("用户工号为空");
//            }
//            paramBean.set("workNum", workNum);
            String userCode = (String) map.get("userCode");
            if (userCode == null || userCode.isEmpty()) {
                return new OutBean().setError("门户传递用户编号为空");
            }
            if (Context.getCmpy().isEmpty()) {
                String cmpyCode = UserMgr.getUser(userCode).getCmpyCode();
                Context.setThread(THREAD.CMPYCODE, cmpyCode);
            }
            paramBean.set("userCode", userCode);
            //返回查询数据集
            return getData(paramBean);
        //如果验证不通过
        } else {
            //返回错误信息
            return new OutBean().setError("OA验证不通过");
        }
    }
    
    /**
     * 获取当前用户的待办事务
     * @param paramBean 参数集合
     * @return 待办事务列表集合
     */
    private OutBean getData(ParamBean paramBean) {
        /*参数TODO_TYPE，为当前组件为待办(OA_DB)或者待阅(OA_DY)*/      
//        String workNum = paramBean.getStr("workNum");
//        UserBean mainUser = UserMgr.getMainUserByWorkNum(workNum);
//        boolean existJiangang = false;
//        String jianGroup = UserMgr.getJiangangUserGroupStrByWorkNum(workNum);
//        if (jianGroup.isEmpty() || mainUser == null) {
//            return new OutBean().setError("OA系统中用户不存在或无工号");
//        }
        String mainUserCode = paramBean.getStr("userCode");
        UserBean mainUser = UserMgr.getUser(mainUserCode);
        boolean existJiangang = false;
        String jianGroup = UserMgr.getJiangangUserStrByMainUser(mainUserCode);
        if (jianGroup.isEmpty() || mainUser == null) {
            return new OutBean().setError("OA系统中用户不存在");
        }
        int jianCount = jianGroup.split(Constant.SEPARATOR).length;
        if (jianCount > 1) {
            existJiangang = true;
        }
        
        //获取本人兼岗组的待办、待阅数据
        ParamBean queryTodo = new ParamBean();
        queryTodo.set("TODO_TYPE", paramBean.getStr("TODO_TYPE"));
        queryTodo.set("_PAGE_", new Bean().set("SHOWNUM", paramBean.getStr("SHOWNUM")));
        queryTodo.setSelect("*");
        queryTodo.set("OWNER_CODE", jianGroup);
        queryTodo.set("SHOWNUM", paramBean.getStr("SHOWNUM"));
        queryTodo.set("existJiangang", existJiangang);
        OutBean outBean = ServMgr.act("SY_COMM_TODO_PT", "query", queryTodo);
        List<Bean> outDataList = outBean.getDataList();
        String serverAddre = RequestUtils.getSysHost(); // 服务器地址
        //追加url
        for (Bean todo : outDataList) {
            todo.set("URL", TodoUtils.createTodoUrl(todo, serverAddre, existJiangang));
        }
        outBean.set("OPEN_TODO", openTodoListUrl(serverAddre, paramBean.getStr("TODO_TYPE"), new Bean()));
        
        //获取本人兼岗组的委托数据
        //待办
        if (paramBean.getStr("TODO_TYPE").equals("OA_DB")) {
            //得到委托人列表
            ParamBean param = new ParamBean();
            //param.set("FROM_USER_CODE", jianGroup);
            param.set("FROM_USER_CODE", mainUser.getId());
            //param.set("existJiangang", existJiangang);
            param.set("existJiangang", false);
            OutBean agtUsersBean = ServMgr.act("SY_ORG_USER_TYPE_AGENT", "getAgtUser", param);
            List<Bean> agtUserList = agtUsersBean.getDataList();
            if (agtUserList.size() == 0) {
                //无委托人则返回
                return outBean;
            }
            
            //构造每个委托人的委托待办数据
            List<Bean> agentList = new ArrayList<Bean>();
            outBean.set("AGENT", agentList);
            
            for (Bean agtUser : agtUserList) {
                String userCode = agtUser.getStr("aCode");
                String userName = agtUser.getStr("aName");
                //查询委托人待办数据
                ParamBean para = new ParamBean();
                para.setSelect("*").set("_PAGE_", new Bean().set("SHOWNUM", paramBean.getStr("SHOWNUM")));
                para.set("OWNER_CODE", userCode);
                para.set("AGT_USER_CODE", userCode);
                //para.set("TO_USER_CODE", jianGroup);
                para.set("TO_USER_CODE", mainUser.getId());
                //para.set("existJiangang", existJiangang);
                para.set("existJiangang", false);
                para.set("agentFlag", true);
                para.set("_extWhere", " and TODO_CATALOG in (1 , 3)");
                OutBean data = ServMgr.act("SY_COMM_TODO_PT", "query", para);
                //添加用户code
                data.set("USER_CODE", userCode);
                //添加用户名
                data.set("USER_NAME", userName);
                //添加更多url
                data.set("OPEN_TODO", openTodoListUrl(serverAddre, "OA_DB", new Bean().set("agentFlag", true).set(
                        "AGT_USER_CODE", userCode)));
                //处理每条记录的跳转url
                for (Bean b : data.getDataList()) {
                    b.set("_agtFLag", true); // 生成url时针对代他人办理情况设置标识
                    b.set("URL", TodoUtils.createTodoUrl(b, serverAddre, existJiangang));
                }
                agentList.add(data);
            }
       }
       
       return outBean;
    }

    /**
     * 解析url
     * @param url 要被解析的url
     * @return 加密之后的封装数据
     */
    private Map<String, Object> parseUrl(String url) {
        //获取解密的url字符转
        String parseUrl = EncryptUtils.decrypt(url, EncryptUtils.DES);
        //获取相应的值
        Map<String, Object> map = new HashMap<String, Object>();
        //封装数据
        String[] parseUrlArry = parseUrl.split("&");
        //map.put("workNum", parseUrlArry[0]);
        map.put("userCode", parseUrlArry[0]);
        map.put("getMillis", Long.parseLong(parseUrlArry[1]));
        return map;
    }
    
    
    /**
     * 判断url的正确性
     * @param map 解码之后的url对象集合
     * @return trur，为真；false，为假
     */
    private boolean decideUrl(Map<String, Object> map) {
        //获取当前时间戳
        long thisMillis = Calendar.getInstance(TimeZone.getTimeZone("GMT-8:00")).getTimeInMillis();
        //如果超时，则返回false
        if ((thisMillis - (Long) map.get("getMillis")) > pastDueTime) {
            return false;
        }
        return true;
    }
    
    /**
     * 打开待办列表url
     * @param serverAddre 主机服务地址
     * @param todoType 访问数据类型(待办，或者待阅)
     * @param jsonParam json参数
     * @return 访问字符串
     */
    private String openTodoListUrl(String serverAddre, String todoType, Bean jsonParam) {
        String openParamStr = "";
        Bean openParamBean = new Bean();
        //如果为待办事务
        if (todoType.equals("OA_DB")) {
            //openParamStr = "{'tTitle':'OA待办列表','url':'OA_SY_COMM_TODO.list.do','menuFlag':4}";           
            openParamBean.set("tTitle", "OA待办列表");
            openParamBean.set("url", "OA_SY_COMM_TODO.list.do");
        } else if (todoType.equals("OA_DY")) {
            //openParamStr = "{'tTitle':'OA待阅列表','url':'OA_SY_COMM_TODO_READ.list.do','menuFlag':4}";           
            openParamBean.set("tTitle", "OA待阅列表");
            openParamBean.set("url", "OA_SY_COMM_TODO_READ.list.do");
        }
        openParamBean.set("menuFlag", 4);
        openParamBean.set("params", jsonParam);
        openParamStr = JsonUtils.toJson(openParamBean);
        openParamStr = openParamStr.replaceAll("\"", "'");
        String url = "";
        try {
            url = serverAddre + "/sy/comm/page/page.jsp?openTab=" + Hex.encodeHexString(openParamStr.getBytes("UTF-8"));
            return url;
        } catch (UnsupportedEncodingException e) {
            log.error(e.getMessage() + ": " + url, e);
            return null;
        }
    }
}
