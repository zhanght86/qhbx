package com.rh.oa.off;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.rh.core.base.Bean;
import com.rh.core.base.Context;
import com.rh.core.base.TipException;
import com.rh.core.serv.CommonServ;
import com.rh.core.serv.OutBean;
import com.rh.core.serv.ParamBean;
import com.rh.core.serv.ServDao;
import com.rh.core.serv.bean.SqlBean;
import com.rh.core.serv.dict.DictMgr;
import com.rh.core.util.Constant;
import com.rh.core.util.DateUtils;
import com.rh.core.util.NumberUtils;

/**
 * 申请物品汇总
 * @author hdy
 */
public class OffApplyPurchase extends CommonServ {
    
    private final String oaOffInStorage = "OA_OFF_INSTORAGE"; //库存服务ID
    private final String oaOffQuotation = "OA_OFF_QUOTATION"; //用品报价ID
    private static final String OA_OFF_APPLY = "OA_OFF_APPLY"; //审批单服务id
    private final String applyTraitsStatus = "1"; //申请单下的申请物品审批状态为[同意]
    /** 
     * 申请汇总数大于库存数，标示为 00，页面显示红色
     * 申请汇总数小于库存数，并且差值不大于库存报警数数，标示为10 
     * 申请汇总数小于库存数，并且差值大于库存最低报警数，标示为11 橙色
     * 库存表中不存在此物品，标示为 111
     */
    private String valFlag = ""; 
    
    @Override
    protected void afterQuery(ParamBean paramBean, OutBean outBean) {
      //合并相同的记录值
      List<Bean> oldList = getNoQueryPageList(paramBean);
      List<Bean> newList = new ArrayList<Bean>(); //新列表数据
      Set<String> distinct = new HashSet<String>(); //去掉重复的物品ID
      for (Bean b : oldList) { //过滤物品种类
          distinct.add(b.getStr("OFFICE_ID"));
      }
      for (String s : distinct) {  //合并形同物品的数据
          Bean thisBean = new Bean(); //当前数据对象
          for (Bean b : oldList) { //便遍历老数据
              if (s.equals(b.getStr("OFFICE_ID"))) {
                  if (thisBean.isEmpty()) {
                      thisBean = b;
                      //插入报警值
                      Bean quotation = ServDao.find(oaOffQuotation, s);
                      if (null == quotation || quotation.isEmpty()) {
                          thisBean.set("OFFICE_WARN", 0);
                      } else {
                          thisBean.set("OFFICE_WARN", quotation.getStr("OFFICE_WARN"));
                      }
                  } else {
                      int thisOffNum = b.getInt("OFFICE_NUMBER") + thisBean.getInt("OFFICE_NUMBER");
                      thisBean.set("OFFICE_NUMBER", thisOffNum); //数量
                      double thisOffAmount = b.getDouble("OFFICE_AMOUNT") + thisBean.getDouble("OFFICE_AMOUNT");
                      thisBean.set("OFFICE_AMOUNT", thisOffAmount); //总额
                  }
              }
          }
          thisBean.set("OFFICE_AMOUNT", NumberUtils.formatDouble(thisBean.getDouble("OFFICE_AMOUNT"), 2));
          thisBean.set("OFFICE_UNITE__NAME", DictMgr.getFullName("OA_OFF_UNIT", thisBean.getStr("OFFICE_UNITE")));
          thisBean.set("OFFICE_PRICE", NumberUtils.formatDouble(thisBean.getDouble("OFFICE_PRICE"), 2));
          newList.add(thisBean);
      }
      //添加库存字段
      SqlBean query = new SqlBean().selects("OFFICE_ID,sum(RESIDUE_NUMBER) ALL_NUM")
              .and("S_ODEPT", Context.getUserBean().getODeptCode())
              .groups("OFFICE_ID");
      List<Bean> inStorageList = ServDao.finds(oaOffInStorage, query); //获取入库表中的物品库存数量
      if (inStorageList.size() == 0) {
          for (Bean d : newList) { //将剩余数量添加到列表中显示
              //如果库存表中没有此物品，将剩余数量赋值为0
              if (d.getStr("DIFF_VAL").isEmpty()) {
                  d.set("DIFF_VAL", "-" + d.getStr("OFFICE_NUMBER"));
              }
              
              //如果库存表中没有此物品，将剩余数量赋值为0
              if (d.getStr("RESIDUE_NUMBER").isEmpty()) {
                  d.set("RESIDUE_NUMBER", "0");
              }
          }
      } else {
          for (Bean c : inStorageList) {
             for (Bean d : newList) { //将剩余数量添加到列表中显示
                 if (d.getStr("OFFICE_ID").equals(c.getStr("OFFICE_ID"))) {
                     d.set("RESIDUE_NUMBER", c.getStr("ALL_NUM"));
                     //申请汇总数大于库存
                     int diffVal = c.getInt("ALL_NUM") - d.getInt("OFFICE_NUMBER"); //获取申请数量与库存数量差值
                     if (diffVal > 0) {
                         valFlag = "00";
                     } else if (diffVal <= d.getInt("OFFICE_WARN")) {
                         valFlag = "10";
                     } else if (diffVal > d.getInt("OFFICE_WARN")) {
                         valFlag = "11";
                     } else {
                         valFlag = "111";
                     }
                     d.set("VAL_FLAG", valFlag);
                     if (valFlag.equals("111")) { //入库中不存在此物品
                         d.set("DIFF_VAL", "0");
                     } else {
                         d.set("DIFF_VAL", diffVal + "");
                     }
                 }
                 //如果库存表中没有此物品，将剩余数量赋值为0
                 if (d.getStr("DIFF_VAL").isEmpty()) {
                     d.set("DIFF_VAL", "-" + d.getStr("OFFICE_NUMBER"));
                 }
                 
                 //如果库存表中没有此物品，将剩余数量赋值为0
                 if (d.getStr("RESIDUE_NUMBER").isEmpty()) {
                     d.set("RESIDUE_NUMBER", "0");
                 }
             }
          }
        }
      isInstorageOFF(paramBean, newList); //加入标识位
      outBean.setData(newList);
      outBean.setPage(newList.size());
    }
    
    /**
     * 获取不分页数据列表
     * @param paramBean 参数
     * @return 结果跑列表
     */
    private List<Bean> getNoQueryPageList(ParamBean paramBean) {
        if (paramBean.getStr("leftDate").isEmpty()) {
            paramBean.set("leftDate", new SimpleDateFormat("yyyy-MM").format(new Date()));
        }
        if (paramBean.getStr("rightDate").isEmpty()) {
            paramBean.set("rightDate", new SimpleDateFormat("yyyy-MM").format(new Date()));
        }
        StringBuffer sql = new StringBuffer();
        sql.append(" ").append("and APPLY_ID in (").append(getApplyId(paramBean.getStr("leftDate"),
                paramBean.getStr("rightDate"))).append(")").append(" ").append("and APPLY_STATUS = '")
                .append(applyTraitsStatus).append("'").append(" ").append("and OFFICE_ID is not null");
        return ServDao.finds(paramBean.getServId(), sql.toString());
    }
    
    /**
     * 查看当前记录是否已入库
     * @param paramBean 参数集合
     * @param dataList 数据结果集
     */
    private void isInstorageOFF(ParamBean paramBean, List<Bean> dataList) {
        for (int i = 0; i < dataList.size(); i++) {
            SqlBean sql = new SqlBean(); //查询条件
            sql.and("OFFICE_ID", dataList.get(i).getStr("OFFICE_ID"));
            sql.and("substr(S_ATIME, 1 ,7)", paramBean.getStr("NEW_DATE"));
            sql.and("APPLY_FLAG", "1"); //查看已入库的记录条数
            int count = ServDao.count(oaOffInStorage, sql);
            dataList.get(i).set("IS_OK", count > 0 ? "是" : "否");
        }
    }
    
    /**
     * 获取这个时间点的审批单
     * @param leftDate 开始时间
     * @param rightDate 结束时间
     * @return 审批单id字符串
     */
    private String getApplyId(String leftDate, String rightDate) {
        SqlBean sql = new SqlBean();
        //获取配置中从每个月的第几天汇总天数，比如：28号，默认是31号
        String dateConfig = Context.getSyConf("OA_OFF_APPLY_DATE", "31");
        try {
            int dateConfigVal = Integer.parseInt(dateConfig);
            if (dateConfigVal <= 0) {
                dateConfig = "01";
            } else if (dateConfigVal > 31) {
                dateConfig = "31";
            }
        } catch (Exception e) {
            throw new TipException(dateConfig + "不为数字型，请重新配置" + e.getMessage());
        }
        String priorDate = "";  //获取开始日期字符串
        String thisDate = ""; //结束时间日期字符串
        //时间相同
        if (leftDate.equals(rightDate)) {
          //获取上一个月的日期字符串
          priorDate = DateUtils.getYearMonth(DateUtils.getCalendar(leftDate + "-01"), -1) + "-" + dateConfig;
          //本月的日期字符串
          thisDate = leftDate + "-" + dateConfig;
          sql.andGTE("substr(SS_TIME, 1, 10)", priorDate).andLTE("substr(SS_TIME, 1, 10)", thisDate);
        } else {
          //获取上一个月的日期字符串
          priorDate = leftDate + "-" + dateConfig;
          //本月的日期字符串
          thisDate = rightDate + "-" + dateConfig;
          //比较两个日期的大小
          long deffVal = DateUtils.getDiffTime(priorDate, thisDate, DateUtils.FORMAT_DATE);
          if (deffVal < 0) {
              sql.andGTE("substr(SS_TIME, 1, 10)", thisDate).andLTE("substr(SS_TIME, 1, 10)", priorDate);
          } else {
              sql.andGTE("substr(SS_TIME, 1, 10)", priorDate).andLTE("substr(SS_TIME, 1, 10)", thisDate);
          }
        }
        sql.selects("APPLY_ID").and("S_FLAG", Constant.YES).and("S_ODEPT", Context.getUserBean().getODeptCode());
        List<Bean> beanList = ServDao.finds(OA_OFF_APPLY, sql);
        if (beanList.size() > 0) {
            StringBuffer applyids = new StringBuffer();
            for (Bean b : beanList) {
                applyids.append(",").append("'").append(b.getId()).append("'");
            }
            return applyids.toString().substring(1);
        }
        return "''";
    }
}
