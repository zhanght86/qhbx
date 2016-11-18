package com.rh.oa.ldr;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.rh.core.base.Bean;
import com.rh.core.base.Context;
import com.rh.core.org.UserBean;
import com.rh.core.org.mgr.UserMgr;
import com.rh.core.serv.CommonServ;
import com.rh.core.serv.ExportExcel;
import com.rh.core.serv.OutBean;
import com.rh.core.serv.ParamBean;
import com.rh.core.serv.ServDao;
import com.rh.core.serv.ServDefBean;
import com.rh.core.serv.ServMgr;
import com.rh.core.serv.ServUtils;
import com.rh.core.util.Constant;

/**
 * 领导活动安排服务对应的服务器端Serv
 * @author yangjy
 * 
 */
public class LeaderActServ extends CommonServ {
  
    /**
     * 记录日志信息的函数
     */
    protected Log log = LogFactory.getLog(this.getClass());
    
    /**每次获取数据条数*/
    private static final int ONETIME_EXP_NUM = 5000;
    /**excel最大行数*/
    private static final int EXCEL_MAX_NUM = 65536;
    
    /**角色用户表*/
    private static final String SY_ORG_ROLE_USER = "SY_ORG_ROLE_USER";
    
    /**
     * 处理领导ID字段为多个值的时候的查询
     * @param paramBean 入参
     */
    @Override
    protected void beforeQuery(ParamBean paramBean) {     
        if (paramBean.containsKey("_searchWhere")) {
            String searchWhere = paramBean.getStr("_searchWhere");          
            if (searchWhere.indexOf("@@LDR_ID") >= 0) { // and @@LDR_ID@%xxx%@@
                String extWhere = paramBean.getStr("_extWhere");
                String pn = "@@(.+)@(.+)@@";
                Pattern pattern = Pattern.compile(pn);
                Matcher mt = pattern.matcher(searchWhere);
                while (mt.find()) {
                    searchWhere = mt.group(2);
                    extWhere = extWhere + "and ACT_ID IN ("
                        + "SELECT ACT_ID FROM OA_LDR_ACT_USERS b WHERE B.USER_CODE IN ("
                        + "select USER_CODE from SY_ORG_USER_RELATION_V where USER_NAME like '"
                        + mt.group(2)
                        + "'"
                        + "))";
                }
                paramBean.set("_extWhere", extWhere);
                paramBean.remove("_searchWhere");
            }
        }
    }

    /**
     * 
     * @param paramBean 参数
     * @return 领导办公的jsp页面
     */
    public Bean show(ParamBean paramBean) {
        OutBean outBean = new OutBean();
        String jspName = "/oa/ldract/ldrActionCal.jsp?sId=" + paramBean.getServId();
        // URL跳转
        outBean.setToDispatcher(jspName);
        setOk(outBean);
        return outBean;
    }
    
    /**
     * 
     * @param paramBean 参数Bean。包含送审核的记录ID
     * @return 处理结果outBean
     */
    public OutBean sendToChk(ParamBean paramBean) {
        String strChkIds = paramBean.getStr("chkIds");
        String[] chkIds = strChkIds.split(",");
        for (String chkId : chkIds) {
            ParamBean bean = new ParamBean(LeaderActConstant.LDR_ACT, ServMgr.ACT_SAVE, chkId);
            bean.set("CHK_STATE", LeaderActConstant.CHK_STATE_SONGSHENHE);
            ServMgr.act(bean);
        }
        return new OutBean().setOk("有" + chkIds.length + "项成功送审。");
    }

    /**
     * 领导活动发布
     * @param paramBean 参数Bean
     * @return 处理结果outBean
     */
    public OutBean publish(ParamBean paramBean) {
        String ids = paramBean.getStr("ids");
        String[] chkIds = ids.split(",");
        for (String chkId : chkIds) {
            ParamBean bean = new ParamBean(LeaderActConstant.LDR_ACT, ServMgr.ACT_SAVE, chkId);
            bean.set("ACT_STATE", Constant.YES);
            ServMgr.act(bean);
        }
        return new OutBean().setOk("有" + chkIds.length + "项成功发布。");
    }
    
    

    @Override
    protected void afterSave(ParamBean paramBean, OutBean outBean) {
        // 如果包含领导参数，则把领导保存到ACT_USERS表中，便于查询。
        if (outBean.contains("LDR_ID")) { 
            removeActUsers(outBean.getId());
            if (outBean.isNotEmpty("LDR_ID")) {
                String strUsers = outBean.getStr("LDR_ID");
                String[] userArr = strUsers.split(",");
                for (String userCode : userArr) {
                    addActUser(outBean.getId(), userCode);
                }
            }
        }
    }

    /**
     * 指定的领导活动项增加对应的领导
     * @param actId 领导活动主键
     * @param userCode 活动对应领导的用户ID
     */
    private void addActUser(String actId, String userCode) {
        ParamBean insertBean = new ParamBean(LeaderActConstant.LDR_ACT_USERS, "save");
        insertBean.set("ACT_ID", actId);
        insertBean.set("USER_CODE", userCode);
        ServMgr.act(insertBean);
    }

    /**
     * 清除活动对应的领导
     * @param actId 领导活动主键
     */
    private void removeActUsers(String actId) {
        Bean whereBean = new Bean();
        whereBean.set("ACT_ID", actId);
        ServDao.destroys(LeaderActConstant.LDR_ACT_USERS, whereBean);
    }

    @Override
    protected void beforeSave(ParamBean paramBean) {
        boolean needChk = Context.getSyConf(LeaderActConstant.CONF_NEED_CHECK, true);
        if (!needChk) {
            paramBean.set("CHK_STATE", LeaderActConstant.CHK_STATE_NOTSHENHE);
        }
    }
    
    /**
     * 获得当前人的领导列表
     * @param paramBean 入参
     * @return OutBean 领导列表
     */
    public OutBean getLeaderList(ParamBean paramBean) {
        ParamBean query = new ParamBean();
        query.setServId("SY_ORG_USER");
        query.setAct(ServMgr.ACT_QUERY);
        String sql = 
                "select " 
                        + "user_code " 
                        + "from " 
                        + "SY_ORG_USER_RELATION_V " 
                        + "where 1=1 " 
                        +  "and RELATION_TYPE = 3 " 
                        + "AND CMPY_CODE = '" + Context.getCmpy() + "' " 
                        + "and ORIGIN_USER_CODE IN " 
                        + "(SELECT USER_CODE " 
                        + "FROM SY_ORG_USER_V " 
                        + "WHERE CODE_PATH LIKE '" + Context.getUserBean().getODeptCodePath() + "%')";
        /*query.setQueryExtWhere(" and user_code in
         * (select user_code from SY_ORG_ROLE_USER_V where ROLE_CODE='RGSLD')");*/
        query.setQueryExtWhere(" and user_code in(" + sql + ")");
        query.setQueryPageShowNum(0);
        query.setSelect("*");
        OutBean res = ServMgr.act(query);
        return res.setOk();
    }
    
    
    /**
     * 提供导出服务 
     * 导出当前所选的角色下领导日程
     * @param paramBean 参数bean
     * @return 参数bean
     */
    @Override
    public OutBean exp(ParamBean paramBean) {
        String servId = paramBean.getServId();
        ServDefBean serv = ServUtils.getServDef(servId);
        int count = 0;
        int times = 0;
        paramBean.setQueryPageShowNum(ONETIME_EXP_NUM); // 设置每页最大导出数据量
        //本周开始时间
        String startDate = paramBean.getStr("WEEK_BEGIN");
        //本周结束时间
        String endDate = paramBean.getStr("WEEK_END");
        String searchWhere = "";
//        String searchWhere = " and LDR_ID in( select USER_CODE from  SY_ORG_ROLE_USER  " 
//                + "where CMPY_CODE='zhbx' and ROLE_CODE  = '" + paramBean.getStr("LEADER_ROLE_CODE") + "' ";  
      //活动时间开始于周一之前，结束于周日之前
        searchWhere += " and ((BEGIN_TIME <='" + startDate + " 00:00:00' and END_TIME >='" 
                + startDate + " 00:00:00' and END_TIME <='" + endDate + " 23:59:59')";
        //活动时间开始于周一之前，结束于周日之后
        searchWhere += " or (BEGIN_TIME <='" + startDate + " 00:00:00' and END_TIME >='" + endDate + " 23:59:59')";
        //活动时间开始于周一之后、周日之前，结束于周日之前
        searchWhere += " or (BEGIN_TIME >='" + startDate + " 00:00:00' and END_TIME <='" + endDate + " 23:59:59')";
        //活动时间开始于周一之后、周日之前，结束于周日之后
        searchWhere += " or (BEGIN_TIME >='" + startDate + " 00:00:00' and BEGIN_TIME <='" 
                + endDate + " 23:59:59' and END_TIME >='" + endDate + " 23:59:59'))";
       
        //根据前台传入的领导角色查找是否匹配
        ParamBean queryBean = new ParamBean();
        queryBean.set("ROLE_CODE", paramBean.getStr("LEADER_ROLE"));
        //领导活动暂时存放map对象
        Map<String, List> ldrMap = new HashMap<String, List>();
        List<Bean> ldrList = ServDao.finds(SY_ORG_ROLE_USER, queryBean);
        if (ldrList.size() > 0) {
            searchWhere += " and (";
            for (int i = 0; i < ldrList.size(); i++) {
                if (i == 0) {
                    searchWhere += " (LDR_ID like '%" + ldrList.get(i).getStr("USER_CODE") + "%')";
                } else {
                    searchWhere += " or (LDR_ID like '%" + ldrList.get(i).getStr("USER_CODE") + "%')";
                }
                ldrMap.put(ldrList.get(i).getStr("USER_CODE"), new ArrayList());
            }
            searchWhere += " )";
        } else {
            searchWhere += " and 1=2";
        }
        paramBean.setQuerySearchWhere(searchWhere);
        //}
        ExportExcel expExcel = new ExportExcel(serv);
        try {
            OutBean outBean = query(paramBean);
            count = outBean.getCount();
            //总数大于excel可写最大值
            if (count > EXCEL_MAX_NUM) {
                return new OutBean().setError("导出数据总条数大于Excel最大行数：" + EXCEL_MAX_NUM);
            }
            //创建excel  表头  
            expExcel.createHeader(outBean.getCols());
            List<Bean> ldrActionList = new ArrayList<Bean>();
            List<Bean> dataList = outBean.getDataList();
            
            for (Bean actionBean : dataList) {
                String[] ldrIds = actionBean.getStr("LDR_ID").split(",");
                if (ldrIds != null && ldrIds.length > 0) {
                    for (String idStr : ldrIds) {
                        //判断领导是否在当前角色下
                        if (!ldrMap.containsKey(idStr)) {
                            continue;
                        }
                        //设置部门、用户名
                        UserBean ldrUser = UserMgr.getUser(idStr);
                        List oneLdrList = ldrMap.get(idStr);
                        Bean oneLdrAction = new Bean();
                        oneLdrAction.copyFrom(actionBean);
                        oneLdrAction.set("LDR_DEPT_NAME", ldrUser.getDeptName());
                        oneLdrAction.set("LDR_ID__NAME", ldrUser.getName());
                        oneLdrList.add(oneLdrAction);
                        ldrMap.put(idStr, oneLdrList);
                    }
                }
            }
            //将当期角色下的所有领导的活动取出来，放入ldrActionList中
            Iterator it = ldrMap.keySet().iterator();
            while (it.hasNext()) {
                ldrActionList.addAll(ldrMap.get(it.next()));
            }
            expExcel.appendData(ldrActionList, paramBean);
            // 存在多页数据
            if (ONETIME_EXP_NUM < count) {
                times = count / ONETIME_EXP_NUM;
                // 如果获取的是整页数据
                if (ONETIME_EXP_NUM * times == count && count != 0) {
                    times = times - 1;
                }
                for (int i = 1; i <= times; i++) {
                    paramBean.setQueryPageNowPage(i + 1); // 导出当前第几页
                    OutBean out = query(paramBean);
                    expExcel.appendData(out.getDataList(), paramBean);
                }
            }
            expExcel.addSumRow();
        } catch (Exception e) {
            log.error("导出Excel文件异常" + e.getMessage(), e);
        } finally {
            expExcel.close();
        }
        return new OutBean().setOk();
    }
}
