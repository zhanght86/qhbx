package com.rh.core.comm.cms.serv;

import com.rh.core.base.Bean;
import com.rh.core.base.Context;
import com.rh.core.org.UserBean;
import com.rh.core.serv.CommonServ;
import com.rh.core.serv.OutBean;
import com.rh.core.serv.ParamBean;
import com.rh.core.serv.ServDao;
import com.rh.core.serv.ServMgr;
import com.rh.core.serv.bean.SqlBean;
import com.rh.core.util.Lang;
import java.util.List;

public class InfosChannelServ
  extends CommonServ
{
  private String siteid = "";
  private static final String SERV_ACL = "SY_SERV_DACL_ITEM";
  
  private String getInfosAdminRole()
  {
    return Context.getSyConf("CMS_INFOS_ADMIN_ROLE", "CJADMIN");
  }
  
  protected void beforeQuery(ParamBean paramBean)
  {
    if (paramBean.isNotEmpty("@com.rh.core.comm.news.InfosChnlDict__EXTWHERE"))
    {
      this.siteid = paramBean.getStr("SITE_ID");
      String extwhere = paramBean.getStr("@com.rh.core.comm.news.InfosChnlDict__EXTWHERE");
      paramBean.setWhere(extwhere);
    }
    UserBean userBean = Context.getUserBean();
    if ((userBean != null) && (userBean.existInRole(getInfosAdminRole()))) {
      Context.getThread("_IS_SERV_DACL_ADMIN", true);
    } else {
      Context.setThread("_IS_SERV_DACL_ADMIN", Boolean.valueOf(false));
    }
  }
  
  protected void afterSave(ParamBean paramBean, OutBean outBean)
  {
    addUserAcl(paramBean, outBean);
    
    Bean upBean = new Bean();
    if (outBean.isEmpty("SITE_ID")) {
      upBean.set("SITE_ID", this.siteid).setId(outBean.getId());
    }
    if ((upBean.getId() != null) && (upBean.getId().length() > 0)) {
      ServDao.save("SY_COMM_INFOS_CHNL", upBean);
    }
    copyChnlPidAcl(paramBean, outBean);
  }
  
  public void addUserAcl(ParamBean paramBean, OutBean outBean)
  {
    if (!paramBean.getAddFlag()) {
      return;
    }
    UserBean userBean = Context.getUserBean();
    String usercode = "U_" + userBean.getCode();
    String dataid = paramBean.getId();
    ParamBean dataBean = new ParamBean().set("SERV_ID", "SY_COMM_INFOS_CHNL")
      .set("ACL_TYPE", "SY_COMM_INFOS_CHNL_MANAGE")
      .set("ACL_OWNER", usercode).set("DATA_ID", dataid);
    ServMgr.act("SY_SERV_DACL_ITEM", "save", dataBean);
  }
  
  public void copyChnlPidAcl(ParamBean paramBean, OutBean outBean)
  {
    if (!paramBean.getAddFlag()) {
      return;
    }
    ParamBean param = new ParamBean();
    param.set("CHNL_PID", outBean.getStr("CHNL_PID"));
    param.set("W_COPY", paramBean.getId());
    
    copyChnlAcl(param);
  }
  
  public void copyChnlAcl(ParamBean param)
  {
    if ((param.isNotEmpty("CHNL_PID")) && (param.isNotEmpty("W_COPY")))
    {
      List<Bean> plist = ServDao.finds("SY_SERV_DACL_ITEM", new ParamBean().set("DATA_ID", param.getStr("CHNL_PID")));
      for (Bean beans : plist)
      {
        beans.set("DATA_ID", param.get("W_COPY"));
        if (!existItem(beans))
        {
          beans.set("_PK_", "");
          beans.set("ACL_ID", "");
          
          ServMgr.act("SY_SERV_DACL_ITEM", "save", new ParamBean(beans));
        }
      }
    }
  }
  
  public OutBean getDeptSF(ParamBean paramBean)
  {
    ParamBean queryBean = new ParamBean();
    queryBean.setSelect("DEPT_CODE, DEPT_NAME, DEPT_PCODE, ODEPT_CODE, DEPT_TYPE, DEPT_SORT");
    String extWhere = " and DEPT_LEVEL = 3 and DEPT_TYPE = 2";
    queryBean.setQueryExtWhere(extWhere);
    OutBean outBean = ServMgr.act("OA_SY_ORG_DEPT_ALL", "query", queryBean);
    return outBean;
  }
  
  public OutBean getDeptAll(ParamBean paramBean){
    ParamBean queryBean = new ParamBean();
    queryBean.setSelect("DEPT_CODE, DEPT_NAME, DEPT_PCODE, DEPT_TYPE, DEPT_SORT");
    String extWhere = " and DEPT_TYPE = 1";
    
    if(!paramBean.isEmpty("ODEPT_CODE")){
    	Bean deptBean = ServDao.find("OA_SY_ORG_DEPT_ALL", paramBean.getStr("ODEPT_CODE"));
    	extWhere = extWhere + " and DEPT_LEVEL in("+deptBean.getInt("DEPT_LEVEL")+","+(deptBean.getInt("DEPT_LEVEL")+1)+") and ODEPT_CODE = '"+paramBean.getStr("ODEPT_CODE")+"'";
    }
    queryBean.setQueryExtWhere(extWhere);
    OutBean outBean = ServMgr.act("OA_SY_ORG_DEPT_ALL", "query", queryBean);
    return outBean;
  }
  
  public OutBean copyBatchChnl(ParamBean param){
    OutBean outBean = new OutBean();
    
    String[] pks = param.getStr("COPY_ID").split(",");
    
    String tarid = param.getStr("TARGET_ID");
    for (int i = 0; i < pks.length; i++)
    {
      String where = "AND CHNL_ID = '" + pks[i] + "'";
      List<Bean> listbeans = ServDao.finds("SY_COMM_CMS_CHNL", where);
      for (Bean bean : listbeans)
      {
        bean.set("_PK_", "");
        bean.set("CHNL_ID", "");
        bean.set("CHNL_PID", tarid);
        
        bean.set("CHNL_CODE", Lang.getUUID());
        
        bean.set("CHNL_SORT", Integer.valueOf(i));
        bean.set("serv", "SY_COMM_INFOS_CHNL");
        
        bean.set("CHNL_PATH", "");
        
        OutBean beans = ServMgr.act("SY_COMM_INFOS_CHNL", "save", new ParamBean(bean));
        
        param.set("CHNL_PID", tarid);
        param.set("W_COPY", beans.getId());
        copyChnlAcl(param);
      }
    }
    return outBean;
  }
  
  public boolean existItem(Bean aclBean)
  {
    SqlBean sql = new SqlBean();
    sql.and("ACL_OWNER", aclBean.getStr("ACL_OWNER"));
    sql.and("DATA_ID", aclBean.getStr("DATA_ID"));
    sql.and("ACL_TYPE", aclBean.getStr("ACL_TYPE"));
    int count = ServDao.count("SY_SERV_DACL_ITEM", sql);
    if (count == 0) {
      return false;
    }
    return true;
  }
}
