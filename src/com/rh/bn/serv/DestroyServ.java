package com.rh.bn.serv;


import com.rh.core.base.Bean;
import com.rh.core.serv.CommonServ;
import com.rh.core.serv.OutBean;
import com.rh.core.serv.ParamBean;
/**
 * 印章销毁服务类
 * @author Lidongdong
 *
 */
public class DestroyServ extends CommonServ{
    
    public OutBean save(ParamBean paramBean) {
        if(paramBean.getId().equals("ADD")||paramBean.getId().equals("ADD1")){
            paramBean.setId("");
            paramBean.setAddFlag(true);
            
        }
        return super.save(paramBean);
    }
    
    /**
     * 单条记录显示之前的拦截方法，由子类重载
     * @param paramBean 参数信息
     */
    protected void beforeByid(ParamBean paramBean) {
        if(paramBean.getId().equals("ADD")||paramBean.getId().equals("ADD1")){
            paramBean.setId("");
            paramBean.setAddFlag(true);
        }
    }
    /**
     * @param paramBean 参数信息
     *      可以通过paramBean获取数据库中的原始数据信息：
     *          Bean oldBean = getParamOldBean(paramBean);
     *      可以通过方法paramBean.getFullData()获取数据库原始数据加上修改数据的完整的数据信息：
     *          Bean fullBean = paramBean.getFullData();
     *      可以通过paramBean.getBoolean(Constant.PARAM_ADD_FLAG)是否为true判断是否为添加模式
     */
    protected void beforeSave(ParamBean paramBean) {
        if(paramBean.getId().equals("ADD")||paramBean.getId().equals("ADD1")){
            paramBean.set("CARD_MARK", paramBean.getId());//密级此处用来当作页面标志使用，ADD为管理用户操作的申请单页面，ADD1为普通用户操作的申请单页面,这里暂定只有管理员有权限
            paramBean.setId("");
        }
    }
    
    protected void afterSave(ParamBean paramBean, OutBean outBean) {
        if(paramBean.getId().equals("")){
            outBean.set("flg", "ADD");
        }else{
            outBean.set("flg", "");
        }
       
    }
    
    public OutBean destroySeal(final ParamBean paramBean){
       /* 
         * 先根据销毁单ID查询出待销毁的印章信息，然后更新印章基本信息表的状态为销毁状态
         */
        String[] sealIds = paramBean.getStr("SEAL_ID").split(",");
        for (String sealId:sealIds) {
            Bean sealOutBean = BnUtils.changeSealStatus(sealId,"4");
            paramBean.set("SEAL_ID",sealOutBean.getId());
        }
        return new OutBean().setOk("销毁成功");
    }

}
