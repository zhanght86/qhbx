package com.rh.bn.seal;

import java.util.List;

import com.rh.core.base.Bean;
import com.rh.core.serv.CommonServ;
import com.rh.core.serv.ParamBean;
import com.rh.core.serv.ServDao;

public class countChangeServ extends CommonServ{

    public void beforeQuery(ParamBean paramBean){
        String searchWhere = paramBean.getQuerySearchWhere();
        String search = "";
       
        String linkWhere = " and KEEP_ODEPT_CODE in (select DEPT_CODE from SY_ORG_DEPT where dept_type=2 AND ";
        if(searchWhere.length()>0 && searchWhere.indexOf("@@")>0){   
            String keepOdeptCode=searchWhere.substring(searchWhere.indexOf("@@")+2, searchWhere.indexOf("%")-1);
            if("KEEP_ODEPT_CODE".equals(keepOdeptCode)){
                search = searchWhere.substring(searchWhere.indexOf("%")+1, searchWhere.lastIndexOf("%"));
                List<Bean> deptBeans = ServDao.finds("SY_ORG_DEPT", " and DEPT_NAME like '%"+search+"%' and DEPT_TYPE = 2 and DEPT_LEVEL < 4");
              //判断
                  
                if(deptBeans.size()>0){
                    for (int i = 0; i < deptBeans.size(); i++) {
                        Bean deptBean = deptBeans.get(i);
                        linkWhere += " CODE_PATH like '%"+deptBean.getId()+"%' or";
                    }
                    linkWhere = linkWhere.substring(0,linkWhere.lastIndexOf("or")) + ")";
                    paramBean.setQuerySearchWhere("");
                    paramBean.setQueryExtWhere(linkWhere);
                }           
            }
        }
        
        if(searchWhere.length()>0 && searchWhere.indexOf("KEEP_ODEPT_CODE_")>0){
                
                String[] paramArray=searchWhere.split("and");
                String KEEP_ODEPT_CODE_= getSearchParam(paramArray,"KEEP_ODEPT_CODE_");
                String str = "KEEP_ODEPT_CODE_ in (select DEPT_CODE from SY_ORG_DEPT where DEPT_TYPE = 2 and CODE_PATH like '%"+KEEP_ODEPT_CODE_+"%')";
                String whereStr = "";
                for(int i=0;i<paramArray.length;i++){
                     if(paramArray[i].indexOf("KEEP_ODEPT_CODE_")<0 &&paramArray[i].length()>2){
                         whereStr = whereStr +(" and "+paramArray[i]);
                    }else if(paramArray[i].indexOf("KEEP_ODEPT_CODE_")>0){
                        whereStr = whereStr +(" and "+str);
                    }
                }
                paramBean.setQuerySearchWhere(whereStr);
            }           
            
    }
        
        
        
   
    
    private String getSearchParam(String[] paramArray, String paramName) {
        String servId = "";
        for (int i = 0; i < paramArray.length; i++) {
            if (paramArray[i].indexOf(paramName) >= 0) {
                String paramStr = paramArray[i].trim();
                servId = paramStr.substring(paramStr.indexOf("'") + 1,
                        paramStr.length() - 1);
                break;
            }
        }
        return servId;
    }
   
    
}
