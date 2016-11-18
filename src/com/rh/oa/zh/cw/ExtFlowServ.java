package com.rh.oa.zh.cw;

import org.apache.commons.lang.StringUtils;

import com.rh.core.base.Bean;
import com.rh.core.org.UserBean;
import com.rh.core.org.mgr.UserMgr;
import com.rh.core.serv.CommonServ;
import com.rh.core.serv.OutBean;
import com.rh.core.serv.ParamBean;
import com.rh.core.serv.ServDao;
import com.rh.core.serv.ServMgr;
import com.rh.core.serv.bean.SqlBean;
import com.rh.core.util.Constant;

/**
 * 财务系统调用接口
 * @author ruaho_hdy
 *
 */
public class ExtFlowServ extends CommonServ {

    /**流经表服务id*/
    private static final String OA_SY_SERV_FLOW_EXT = "OA_SY_SERV_FLOW_EXT";
    
    /**流经log服务id*/
    private static final String EXT_SERV_FLOW_LOG = "EXT_SERV_FLOW_LOG";
    
    /**非流经非传阅，财务系统过来的数据*/
    private static final int FLOW_FLAG = 3;
    
    /**返回状态key*/
    private static final String STATUS = "STATUS";
    
    /**
     * 添加人员流经权限
     * @param paramBean 参数
     * @return 数据对象
     */
    public OutBean addFlow(ParamBean paramBean) {
        //获取用户工号
        String userWorkNum = paramBean.getStr("USER_WORK_NUM");
        //判断工号是否存在
        if (StringUtils.isBlank(userWorkNum)) {
            return new OutBean().set("_MSG_", "this worlNum is empty").set(STATUS, false);
        }
        
        //获取此工号用户信息
        UserBean user = UserMgr.getUserByWorkNum(userWorkNum);
        //不存在该用户，返回错误信息
        if (user.isEmpty()) {
            return new OutBean().set("_MSG_", "the user who has workNum :" + userWorkNum + " is empty")
                                .set(STATUS, false);
        }
        
        Bean bean = new Bean();
        String dataId = paramBean.getStr("DATA_ID");
        //是否传递了表单数据信息
        if (StringUtils.isBlank(dataId)) {
            return new OutBean().set("_MSG_", "this dataId is empty").set(STATUS, false);
        }
        
        SqlBean sql = new SqlBean();
        //查询此流经信息在流经表中是否存在
        sql.and("DATA_ID", dataId).and("OWNER_ID", user.getCode()).and("S_ODEPT", user.getODeptCode());
        if (ServDao.count(OA_SY_SERV_FLOW_EXT, sql) > 0) {
            return new OutBean().set("_MSG_", "this flow is exit").set(STATUS, true);
        }
        
        //添加流经记录信息
        bean.set("DATA_ID", dataId).set("OWNER_ID", user.getCode()).set("S_ODEPT", user.getODeptCode())
            .set("FLOW_FLAG", FLOW_FLAG);
        Bean out = ServMgr.act(OA_SY_SERV_FLOW_EXT, "save", bean);
        
        //保存log
        Bean logBean = new Bean();
        logBean.set("DATA_ID", dataId).set("USER_WORK_NUM", userWorkNum).set("LOG_FLAG", Constant.NO);
        Bean log = ServMgr.act(EXT_SERV_FLOW_LOG, "save", logBean);
        
        OutBean outBean = new OutBean();
        
        //返回成功信息
        if (!out.isEmpty()) {
            outBean.set("_MSG_", "OK").set(STATUS, true);
            log.set("LOG_FLAG", Constant.YES);
            ServDao.save(EXT_SERV_FLOW_LOG, log);
        }
        return outBean;
    }
}
