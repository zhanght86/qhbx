package com.rh.core.org.serv;

import java.util.ArrayList;
import java.util.List;

import com.rh.core.base.Bean;
import com.rh.core.base.Context;
import com.rh.core.comm.txl.TxlSecurer;
import com.rh.core.comm.txl.TxlSecurerFactory;
import com.rh.core.org.DeptBean;
import com.rh.core.org.UserBean;
import com.rh.core.org.mgr.OrgMgr;
import com.rh.core.org.mgr.UserMgr;
import com.rh.core.plug.im.ImMgr;
import com.rh.core.serv.CommonServ;
import com.rh.core.serv.OutBean;
import com.rh.core.serv.ParamBean;
import com.rh.core.serv.ServMgr;
import com.rh.core.util.Constant;

/**
 * 用户通讯录服务类
 * 
 * @author Jerry Li
 * 
 */
public class AddressServ extends CommonServ {

    /**
     * 发送提醒信息
     * @param paramBean 参数
     * @return 发送是否成功
     */
    public OutBean sendNotify(ParamBean paramBean) {
        OutBean outBean = new OutBean();
        if (ImMgr.getIm().sendNotify(paramBean.getId(),
                "来自【" + Context.getUserBean().getName() + "】的消息", paramBean.getStr("SEND_MSG"))) {
            outBean.setOk();
        } else {
            outBean.setError();
        }
        return outBean;
    }

    /**
     * 列表记录查询之后的拦截方法，控制列表用户控制字段的显示方式，若未进行控制信息控制，则直接返回
     * @param paramBean 参数信息
     * @param outBean 输出信息
     */
    protected void afterQuery(ParamBean paramBean, OutBean outBean) {
        TxlSecurer txlSecurer = TxlSecurerFactory.getTxlSender("SY_COMM_ADDRESS_SECURED_CLASS");
        if (txlSecurer != null) {
            List<Bean> datasList = outBean.getDataList();
            List<Bean> outDatas = new ArrayList<Bean>();
            for (Bean eventBean : datasList) {
                eventBean = txlSecurer.getSecuredUserInfo(eventBean);
                outDatas.add(eventBean);
            }
            outBean.setData(outDatas);
        }
    }

    /**
     * 单条记录显示之后的拦截方法，由子类重载 控制用户个人名片的显示方式，若未进行控制信息控制，则直接返回
     * @param paramBean 参数信息
     * @param outBean 输出信息
     */
    protected void afterByid(ParamBean paramBean, OutBean outBean) {
        //获得剪裁后的照片
        outBean.set("USER_IMG", UserMgr.getUser(outBean.getId()).getImg());
        TxlSecurer txlSecurer = TxlSecurerFactory.getTxlSender("SY_COMM_ADDRESS_SECURED_CLASS");
        if (txlSecurer != null) {
            outBean = (OutBean) txlSecurer.getSecuredUserInfo(outBean);
        } else {
            outBean.set("USER_RESUME", "1");
            outBean.set("USER_PHONE_OK", "OK");
        }
    }
    /**
     * 获取上下级及本级部门codes值
     * @param userBean 当前登录用户
     * @return sql
     */
    private String getOdeptCodes(UserBean userBean) {
        StringBuffer odptCodes = new StringBuffer(" and odept_code in(");
        //获取本级部门code值
        odptCodes.append("'").append(userBean.getODeptCode()).append("'");
        
        //获取上级部门code值
        odptCodes.append(Constant.SEPARATOR);
        odptCodes.append("'").append(userBean.getODeptBean().getPcode()).append("'");
        
        //获取下级部门code值
        List<DeptBean> deptBeanList = OrgMgr.getSubOrgs(userBean.getCmpyCode(),
                    userBean.getODeptCode());
        for (DeptBean dept : deptBeanList) {
            odptCodes.append(Constant.SEPARATOR).append("'").append(dept.getCode()).append("'");
        }
        
        odptCodes.append(")");

        //返回格式化的code字符串
        return odptCodes.toString();
      }
    /**
     * 创建带有总公司虚拟节点的tree，供通讯录左侧使用
     * @param paramBean
     * @return
     */
    public OutBean createTree(ParamBean paramBean) {
        String dictId = paramBean.getStr("dictId");
        String extWhere = paramBean.getStr("extWhere");
        
        ParamBean param = new ParamBean().set("DICT_ID", dictId).set("_extWhere", extWhere);
        // 查询数据
        OutBean outBean = ServMgr.act("SY_COMM_INFO", "dict", param);
        List<Bean> tempList = outBean.getList("CHILD");
        
        if( tempList.size() != 0 ){
        	  Bean tempBean = tempList.get(0);
              List<Bean> dataList = tempBean.getList("CHILD");
              
              // 添加虚拟节点
              List<Bean> returnList = new ArrayList<Bean>();
              String odeptCode = Context.getUserBean().getODeptCode();
              if("0001B210000000000BU3".equals(odeptCode)){
                  Bean node = new Bean();
                  List<Bean> childNodes = new ArrayList<Bean>();
                  
                  
                  for (int i = 0; i < dataList.size(); i++) {
                      Bean itemBean = dataList.get(i);
                      if (itemBean.getInt("PID") == 24 && itemBean.getInt("DEPT_TYPE") == 1) {
                          itemBean.set("PID", 12);
                          childNodes.add(itemBean);
                      } else {
                          returnList.add(itemBean);
                      }
                  }
                  tempBean.set("CHILD",returnList);
              } else {
                  tempBean.set("CHILD",dataList);
              }
        }
      
        
        return outBean;
    }
}
