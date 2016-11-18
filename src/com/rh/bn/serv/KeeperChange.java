package com.rh.bn.serv;


import com.rh.core.base.Bean;
import com.rh.core.base.Context;
import com.rh.core.serv.CommonServ;
import com.rh.core.serv.OutBean;
import com.rh.core.serv.ParamBean;
import com.rh.core.serv.ServDao;
import com.rh.core.serv.ServMgr;
import com.rh.core.util.DateUtils;
/**
 * 保管人变更服务类
 * @author Lidongdong
 *
 */
public class KeeperChange extends CommonServ{
    /**
     * 保存之后的拦截方法
     * @param paramBean 参数信息
     *      可以通过paramBean获取数据库中的原始数据信息：
     *          Bean oldBean = paramBean.getOldData();
     *      可以通过方法getFullData(paramBean)获取数据库原始数据加上修改数据的完整的数据信息：
     *          Bean fullBean = getFullData(paramBean);
     *      可以通过paramBean.getBoolean(Constant.PARAM_ADD_FLAG)是否为true判断是否为添加模式
     * @param outBean 输出信息
     */
    protected void afterSave(ParamBean paramBean, OutBean outBean) {
        
    }
    /**
     * 印章信息相关人员变更申请
     * @param paramBean
     * @return
     */
    public OutBean hange(ParamBean paramBean){
        OutBean outBean = new OutBean();
        int changeType = paramBean.getInt("CHANGE_TYPE");
        String servId = paramBean.getServId();
        String[] sealIds = paramBean.getStr("SEAL_ID").split(",");
        String userCol = "";
        if(changeType==3){
            userCol = "XZ_MANAGER";//行政经理
        }else if(changeType==4){
            userCol = "CHARGE";//公司负责人
        }else if(changeType==5){
            userCol = "SEAL_OWNER_USER";//保 管 人
        }
        outBean.set("changeUserName", paramBean.getStr("CHARGE__NAME"));
        Bean keeperBean = ServDao.find("SY_ORG_USER", paramBean.getStr("CHARGE"));
        Bean keeperDeptBean = ServDao.find("SY_ORG_DEPT",keeperBean.getStr("DEPT_CODE"));
        for(String sealId:sealIds){
            Bean sealBean = ServDao.find("BN_SEAL_BASIC_INFO", sealId);
            /*//判断如果为第一次修改保管人，则先写入原始保管人信息
            isFirstModifyKeeper(sealBean,paramBean.getServId());*/
            //获取修改类型
            //由更改类型决定需要更新的人员字段
            sealBean.set(userCol, keeperBean.get("USER_CODE"));//变更人员
            sealBean.set("KEEP_ODEPT_CODE", keeperDeptBean.get("ODEPT_CODE"));//保管机构
            sealBean.set("SEAL_USER_PHONE", keeperBean.get("USER_MOBILE"));//保管人电话
            Bean bean = ServDao.update("BN_SEAL_BASIC_INFO", sealBean);
        }
        paramBean.set("CHANGE_STATE","2");
        ServDao.update(servId, paramBean);
        return outBean;
    }
    /**
     * 插入初始保管人信息
     * @param sealBean
     */
    private void insertFirstKeeper(Bean sealBean) {
        ParamBean changeKeeperBean = new ParamBean();
        changeKeeperBean.set("SEAL_ID",sealBean.getId());//相关印章
        changeKeeperBean.set("SEAL_OWNER_USER",sealBean.get("SEAL_OWNER_USER"));//保管人
        changeKeeperBean.set("KEEP_BEGIN_TIME",sealBean.get("SEAL_KZ_TIME"));//开始保管时间
        changeKeeperBean.set("KEEP_END_TIME",DateUtils.getDate());//保管结束时间
        changeKeeperBean.set("KEEP_TDEPT_CODE",sealBean.get("KEEP_TDEPT_CODE"));//保管人所在部门
        ServDao.save("BN_SEAL_KEEPER_CHANGE", changeKeeperBean);
    }
    /**
     * 判断用户本次修改保管人是否是第一次修改
     *      第一次修改则添加将用户初始保管人信息写入数据库中
     * @param sealId 印章ID
     * @param str 用户编码
     * @return
     */
    private boolean isFirstModifyKeeper(Bean sealBean,String servId) {
        int count = ServDao.count(servId, new Bean().set("SEAL_ID", sealBean.getId()));
        if(count==0){
            insertFirstKeeper(sealBean);
            return true;
        }
        return false;
    }
    /**
     * 获取印章ID
     * @param paramBean
     * @return
     */
    public OutBean getSealId(ParamBean paramBean){
        paramBean.setAct("query");
        paramBean.setSelect("SEAL_ID");
        OutBean out = ServMgr.act(paramBean);
        return new OutBean();
    }
}
