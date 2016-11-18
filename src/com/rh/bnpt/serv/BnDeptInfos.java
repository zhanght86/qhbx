package com.rh.bnpt.serv;

import java.util.List;

import javax.servlet.ServletContext;

import com.rh.core.base.Bean;
import com.rh.core.serv.CommonServ;
import com.rh.core.serv.OutBean;
import com.rh.core.serv.ParamBean;
import com.rh.core.serv.ServDao;
import com.rh.core.serv.ServMgr;
import com.rh.core.serv.dict.DictMgr;

public class BnDeptInfos extends CommonServ{
	
	
    public OutBean getDeptInfos(ParamBean paramBean){
        OutBean outBean = new OutBean();
        String where = paramBean.getStr("where");
        if(!paramBean.isEmpty("TDEPT_CODE")){
            where = where + " and S_TDEPT = '"+paramBean.getStr("TDEPT_CODE")+"'";
        }
        paramBean.set("_WHERE_", where);
        paramBean.setShowNum(6);
        List<Bean> listBeans = ServDao.finds("SY_COMM_INFOS_VIEW", paramBean);
        outBean.set("listBeans",listBeans);
        return outBean;
    }
    /**
     * 获取部门栏目下的文件
     * @param paramBean
     * @return
     */
    public OutBean getChnlFile(ParamBean paramBean){
        OutBean outBean = new OutBean();
        /*String sqlWhere = " and serv_id = 'SY_COMM_INFOS_BASE'"
                +" and file_cat = 'FUJIAN' " 
                +" and data_id in"
                +" (select NEWS_ID"
                +" from SY_COMM_INFOS_V"
                +" where 1=1 "
                +" and S_TDEPT = '" + paramBean.getStr("DEPT_CODE") + "')";*/
        ParamBean uploadBean = new ParamBean();
        uploadBean.setAct("query");
        uploadBean.setServId("BN_FILE_UPLOAD");
        
        if( !paramBean.getStr("DEPT_CODE").equals("") ){
        	uploadBean.setWhere(" AND S_TDEPT = '" +paramBean.getStr("DEPT_CODE") + "'");
        }
        
        if(!paramBean.getStr("CHNL_ID").equals("")){
            uploadBean.setWhere(" AND CHNL_ID = '" +paramBean.getStr("CHNL_ID") + "'");
        }
        
        List<Bean> uploadFile = ServMgr.act(uploadBean).getDataList();
        String fileCode = "";
        String fileSql = "";
        for (int i = 0; i < uploadFile.size(); i++) {
            fileCode += "'"+uploadFile.get(i).get("ID")+"',";
        }
        if(fileCode.length()>0){
            fileCode = fileCode.substring(0, fileCode.lastIndexOf(","));
        }
        
        
//        ParamBean sqlBean = new ParamBean();
//        String s_dept = paramBean.getStr("DEPT_CODE");
//        sqlBean.setServId("SY_COMM_FILE");
//        sqlBean.set("serv_id", "BN_FILE");
//        sqlBean.set("file_cat", "FUJIAN");
//        sqlBean.set("S_DEPT", s_dept);
//        sqlBean.setShowNum(SHOW_NUM);
//        sqlBean.setNowPage(SHOW_NOW_PAGE);
   
        String sqlWhere = "and serv_id = 'BN_FILE'" +
        		" and file_cat = 'FUJIAN'" ;
        		
    
        if (fileCode.length()>0) {
        	sqlWhere = sqlWhere + " and data_id in ("+fileCode+")";
		}
       // sqlBean.setWhere(sqlWhere);
        
        List<Bean> listBeans = ServDao.finds("SY_COMM_FILE", sqlWhere);
        outBean.set("listBeans", listBeans);
        return outBean;
    }
    
    /**
     * 获取部门下的文件
     * @param paramBean
     * @return
     */
    public OutBean getDeptFile(ParamBean paramBean){
    	 OutBean outBean = new OutBean();	 
        String sqlWhere = "";
         
        ParamBean uploadBean = new ParamBean();
        uploadBean.setAct("query");
        uploadBean.setServId("BN_FILE_UPLOAD");
        uploadBean.setSelect(" ID ,CHNL_ID,S_USER,S_DEPT,S_TDEPT,FILE_NAME,FILE_SIZE,FILE_ID,S_MTIME");
        
        if( !paramBean.getStr("DEPT_CODE").equals("") ){
        	uploadBean.setWhere(" AND S_TDEPT = '" +paramBean.getStr("DEPT_CODE") + "'");
        }
        
        
        //如果点击栏目，直接按部门和栏目查询
        if(!paramBean.getStr("CHNL_ID").equals("")){
            uploadBean.setWhere(" AND CHNL_ID = '" +paramBean.getStr("CHNL_ID") + "'");
            sqlWhere += " and S_TDEPT = '"+ paramBean.getStr("DEPT_CODE")+"'";
            sqlWhere += " and CHNL_ID = '"+ paramBean.getStr("CHNL_ID")+"'";
            
            List<Bean> listBeans = ServDao.finds("BN_FILE_UPLOAD", sqlWhere);
            
            for(Bean bean :listBeans ){ 
            	String s_user = bean.getStr("S_USER");
            	 String s_name = DictMgr.getFullName("SY_ORG_USER", s_user);
            	 bean.set("S_USER", s_name);
             }
             outBean.set("listBeans", listBeans);
        
        
        }else{
        	//按照左侧部门查询
        	 List<Bean> uploadFile = ServMgr.act(uploadBean).getDataList();
             
             for(Bean bean :uploadFile ){ 
            	String s_user = bean.getStr("S_USER");
            	 String s_name = DictMgr.getFullName("SY_ORG_USER", s_user);
            	 bean.set("S_USER", s_name);
             }
             outBean.set("listBeans", uploadFile);
        }
        
         return outBean;
    }
    
 
    
    /**
     * 获取总公司所有的文件
     * @param paramBean
     * @return
     */
    public OutBean getChnlFileAll(ParamBean paramBean){
        OutBean outBean = new OutBean();
        String sqlWhere = "and serv_id = 'BN_FILE'" +
                " and file_cat = 'FUJIAN'" +
                " and data_id in" +
                " (select ID from BN_FILE where S_ODEPT = '"+ paramBean.getStr("DEPT_CODE") +"'";
        if(!paramBean.getStr("CHNL_ID").equals("")){
            sqlWhere = sqlWhere + " AND CHNL_ID = '" +paramBean.getStr("CHNL_ID") + "'";
        }
        sqlWhere += ")";
        List<Bean> listBeans = ServDao.finds("SY_COMM_FILE", sqlWhere);
        outBean.set("listBeans", listBeans);
        return outBean;
    }
}
