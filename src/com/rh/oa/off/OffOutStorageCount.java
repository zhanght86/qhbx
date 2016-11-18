package com.rh.oa.off;

import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.hg.xdoc.XDoc;
import com.hg.xdoc.XDocIO;
import com.rh.core.base.Bean;
import com.rh.core.base.Context;
import com.rh.core.base.Context.APP;
import com.rh.core.org.UserBean;
import com.rh.core.serv.CommonServ;
import com.rh.core.serv.OutBean;
import com.rh.core.serv.ParamBean;
import com.rh.core.serv.ServDao;
import com.rh.core.serv.bean.SqlBean;
import com.rh.core.util.Constant;
import com.rh.core.util.DateUtils;

/**
 * 办公用品部门表，统计
 * @author hdy
 */
public class OffOutStorageCount extends CommonServ {
    
    private final String oaOffQuotation = "OA_OFF_QUOTATION"; //报价表服务ID
    private final String oaOffOutStorage = "OA_OFF_OUTSTORAGE"; //入库用品服务ID
    private final String outStorageStatus = "2"; //发放状态，2标示[已发放]
    
    private final String oaOffEachDeptCount = Context.appStr(APP.SYSPATH); //各部门办公用品统计XDOC文件路径
    private final String oaOffLocalDeptCount = ""; //本部门办公用品统计XDOC文件路径 
    
   @Override
    protected void afterQuery(ParamBean paramBean, OutBean outBean) {
       double count = 0.00; //初始化总金额count值
       //获取查询列表数据
       List<Bean> outBeanList = outBean.getList(Constant.RTN_DATA);
       //获取符合条件的数据集
       List<Bean> queryList = getGroupByList(paramBean);
       //重置数据
       for (int i = 0; i < queryList.size(); i++) {
           //获取总结count值
           count += Double.parseDouble(queryList.get(i).getStr("OFFICE_AMOUNT"));
           //获取某个物品的名称
           Bean objBean = ServDao.find(oaOffQuotation, queryList.get(i).getStr("OFFICE_ID"));
           //将符合条件的数据替换掉对应原有列表数据
           outBeanList.get(i).set("OFFICE_NAME", objBean.getStr("OFFICE_NAME"))
                                        .set("OFFICE_STOCK", queryList.get(i).getStr("OFFICE_STOCK"))
                                        .set("OFFICE_AMOUNT", queryList.get(i).getStr("OFFICE_AMOUNT"));
       }
       //删除原有列表多余数据
       int delCount = outBeanList.size() - queryList.size();
       for (int i = 0; i < delCount; i++) {
           outBeanList.remove(outBeanList.size() - 1);
       }
       //将获取时间回传到前台
       outBean.set("LEFT_DATE", paramBean.getStr("LEFT_DATE")).set("RIGHT_DATE", paramBean.getStr("RIGHT_DATE"))
                     .set("COUNT", count + "");
    }
   
   /**
    * 获取按用品ID分组之后的数据
    * @param paramBean 参数集合
    * @return 符合条件的记录
    */
   private List<Bean> getGroupByList(Bean paramBean) {
        SqlBean query = new SqlBean(); // 查询参数
        //设置查询项
        query.selects("OFFICE_ID,sum(OFFICE_STOCK) OFFICE_STOCK,sum(OFFICE_AMOUNT) OFFICE_AMOUNT");
        //判断页面第一次加载时
        if (paramBean.getStr("_searchWhere").isEmpty()) {
            //获取当前格式化时间yyyy-MM
            UserBean userBean = Context.getUserBean();
            String newDate = DateUtils.getYearMonth();
            query.andLikeRT("S_ATIME", newDate).and("OUTSTORAGE_STATUS", outStorageStatus)
                .and("S_ODEPT", userBean.getODeptCode()).and("S_TDEPT", userBean.getTDeptCode());
        } else {
            //点击[查询]按钮时调用
            query.appendWhere(paramBean.getStr("_searchWhere"));
        }
        query.groups("OFFICE_ID");
        //返回查询结果集
        return ServDao.finds(oaOffOutStorage, query);
   }
   
   /**
    * xdoc输出设置
    * @param paramBean 参数
    */
   public void outPutXdocFlow(Bean paramBean) {
       HttpServletResponse  response = Context.getResponse();
       HttpServletRequest request = Context.getRequest();
       try {
           //定义传入参数集合
           HashMap<String, String> map = new HashMap<String, String>();
           XDoc xdoc = null; //初始化XDOC
           //注入参数
           map.put("beforeDate", paramBean.getStr("beforeDate"));
           map.put("afterDate", paramBean.getStr("afterDate"));
           map.put("dept_name", paramBean.getStr("dept_name"));
           map.put("dept_code", paramBean.getStr("dept_code"));
           map.put("cmpy_code", paramBean.getStr("cmpy_code"));
           //本部门导出数据
           if (map.get("cmpy_code").isEmpty()) {
               xdoc = XDocIO.read(oaOffLocalDeptCount).run(map);
           } else {
               //各部门导出数据
               xdoc = XDocIO.read(oaOffEachDeptCount).run(map);
           }
           //输出流文件
           XDocIO.write(xdoc, response.getOutputStream(), "xls");
           } catch (Exception e) {
               throw new RuntimeException(Context.getSyMsg("SY_REQUEST_ERROR", request.getRequestURI()));
           }
   }
}
