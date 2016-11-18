package com.rh.core.serv.flow;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.rh.core.base.Bean;
import com.rh.core.comm.entity.EntityMgr;
import com.rh.core.org.UserBean;
import com.rh.core.org.mgr.UserMgr;
import com.rh.core.serv.ServDao;
import com.rh.core.serv.ServMgr;
import com.rh.core.serv.bean.SqlBean;
import com.rh.core.util.Constant;

/**
 * 流经表常用操作类
 * 
 */
public class FlowMgr {
	
	/** 流经类型  - 流经 */ 
	public static final int FLOW_TYPE_FLOW = 1;
	
	/** 流经类型  - 传阅 */ 
	public static final int FLOW_TYPE_CHUANYUE = 2;
	
	
    /**
     * 分发都发到人了，
     * @param docId 数据ID
     * @param userCode 用户编码
     * @param flowFlag 流经类型
     */
    public static void addUserFlow(String docId, String userCode, int flowFlag) {
        UserBean userBean = UserMgr.getUser(userCode);
        
        addUserFlow(docId, userBean, flowFlag);
    }

    /**
     * 增加用户流经信息
     * @param docId 数据ID
     * @param userBean 流经用户
     * @param flowFlag 流经类型
     */
    public static void addUserFlow(String docId, UserBean userBean, int flowFlag) {
        int result = addUserFlowIgnoreEntity(docId, userBean, flowFlag);

        if (result == 0) { // result == 0 表示指定单位一条流经记录都没有，则复制Entity实例
            EntityMgr.copyEntity(docId, userBean.getODeptCode());
        }
    }
    
    /**
     * 仅用于数据迁移，提高速度。
     * @param docId 数据ID
     * @param userBean 用户Bean
     * @param flowDataSet 已经存在的数据ID + ownerIds
     * @deprecated
     */
    public static void addUserFlowIgnoreCheck(String docId, UserBean userBean, HashSet<String> flowDataSet) {
        String[] ownerIds = new String[4];
        ownerIds[0] = userBean.getId();
        ownerIds[1] = userBean.getDeptCode();
        ownerIds[2] = userBean.getTDeptCode();
        ownerIds[3] = userBean.getODeptCode();
                
		String odeptKey = docId + "@" + userBean.getODeptCode();
		if (!flowDataSet.contains(odeptKey)) {
			EntityMgr.copyEntity(docId, userBean.getODeptCode());
		}

        for (int i = 0; i < 4; i++) {
            String key = docId + "@" + ownerIds[i];
            if (!flowDataSet.contains(key)) { //
                if (addFlowData(docId, userBean.getODeptCode(), ownerIds[i], FLOW_TYPE_FLOW)) {
                    flowDataSet.add(key);
                }
            }
        }
    }

    /**
     * 添加 流经记录，忽略对Entity表数据的处理。用于数据迁移。
     * @param docId 审批单ID
     * @param userBean 用户对象
     * @param flowFlag 流经类型
     * @return 存在哪种类型的数据
     * @deprecated
     */
    public static int addUserFlowIgnoreEntity(String docId, UserBean userBean, int flowFlag) {

        String[] ownerIds = new String[4];
        ownerIds[0] = userBean.getId();
        ownerIds[1] = userBean.getDeptCode();
        ownerIds[2] = userBean.getTDeptCode();
        ownerIds[3] = userBean.getODeptCode();

        List<Bean> flowList = getUserFlow(docId, ownerIds);
        HashMap<String, Bean> flowMap = getFlowBeans(flowList);
        
        for (String ownderId: ownerIds) {
            if (flowMap.containsKey(ownderId)) { //以前有, 需要判断flowFlag
            	Bean oldFlow = flowMap.get(ownderId);
            	
            	if (oldFlow.getInt("FLOW_FLAG") == Constant.YES_INT || oldFlow.getInt("FLOW_FLAG") == flowFlag) {
            		continue;
            	} else {
            		oldFlow.set("FLOW_FLAG", Constant.YES_INT);
            		
            		ServDao.update(ServMgr.SY_SERV_FLOW, oldFlow);
            	}
            } else { //以前没有
            	addFlowData(docId, userBean.getODeptCode(), ownderId, flowFlag);
            	
            	Bean bean = new Bean();
            	bean.set("FLOW_FLAG", flowFlag);
            	bean.set("OWNER_ID", ownderId);
            	bean.set("DATA_ID", docId);
            	flowMap.put(ownderId, bean);
            }
        }


        /*int result = existUserFlow(docId, ownerIds);
        if (result == 15) { // result == 15 表示所有数据都存在，则不用创建了。
            return result;
        }

        HashSet<String> owners = new HashSet<String>();

        for (int i = 0; i < 4; i++) {
            int val = (int) Math.pow(2, i);
            // 想与的结果为0表示此类数据不存在，且没有没有重复数据（用于排除部门ID等于处室ID情况）
            if ((val & result) == 0 && !owners.contains(ownerIds[i])) { //
                addFlowData(docId, userBean.getODeptCode(), ownerIds[i], flowFlag);
                owners.add(ownerIds[i]);
            }
        }*/

        return flowList.size();
    }
    
    /**
     * 
     * @param flowList 流经的列表
     * @return 将列表转成Map<OWNER_ID, flowBean>
     */
    private static HashMap<String, Bean> getFlowBeans(List<Bean> flowList) {
    	HashMap<String, Bean> flowMap = new HashMap<String, Bean>();
    	for (Bean flowBean: flowList) {
    		if (!flowMap.containsKey(flowBean.getStr("OWNER_ID"))) {
    			flowMap.put(flowBean.getStr("OWNER_ID"), flowBean);
    		}
    	}
    	
    	
    	return flowMap;
    	
    }

    /**
     * 
     * @param dataId 审批单ID
     * @param odeptCode 用户/部门所属机构
     * @param ownerId 所属对象：用户、处室、部门和机构
     * @param flowFlag 流经类型， 1，流经，2，传阅
     * @return 是否添加使用
     */
    private static boolean addFlowData(String dataId, String odeptCode, String ownerId, int flowFlag) {
        if (StringUtils.isEmpty(ownerId) || StringUtils.isEmpty(dataId)) {
            return false;
        }
        Bean flowBean = new Bean();
        flowBean.set("DATA_ID", dataId);
        flowBean.set("OWNER_ID", ownerId);
        flowBean.set("S_ODEPT", odeptCode);
        flowBean.set("FLOW_FLAG", flowFlag);
        ServDao.create(ServMgr.SY_SERV_FLOW, flowBean);
        return true;
    }

    /**
     * 
     * @param dataId 数据ID
     * @param ownerIds 所属数据对象
     * @return 指定数据 的流经记录
     */
    private static List<Bean> getUserFlow(String dataId, String[] ownerIds) {
        SqlBean sql = new SqlBean();
        sql.andIn("OWNER_ID", ownerIds);
        sql.and("DATA_ID", dataId);

        return ServDao.finds(ServMgr.SY_SERV_FLOW, sql);
    }
    
    
    
    /**
     * 
     * @param dataId 数据ID
     * @param ownerIds 所属数据对象
     * @return 是否存在指定数据
     */
    private static int existUserFlow(String dataId, String[] ownerIds) {
        SqlBean sql = new SqlBean();
        sql.andIn("OWNER_ID", ownerIds);
        sql.and("DATA_ID", dataId);

        List<Bean> list = ServDao.finds(ServMgr.SY_SERV_FLOW, sql);
        
        int result = 0;

        for (Bean bean : list) {
            result = result + existOwner(ownerIds, bean);
        }

        return result;
    }

    /**
     * 
     * @param ownerIds 所有者ID
     * @param bean FlowBean 对象
     * @return 返回所有则标识位
     */
    private static int existOwner(String[] ownerIds, Bean bean) {
        int result = 0;
        for (int i = 0; i < 4; i++) {
            if (bean.getStr("OWNER_ID").equals(ownerIds[i])) {
                result += (int) Math.pow(2, i);
            }
        }
        return result;
    }
    
    /**
     * 
     * @param dataId 数据ID（审批单ID）
     * @return 指定数据ID对应的流经记录。
     */
    public static List<Bean> find(String dataId) {
        SqlBean sql = new SqlBean();
        sql.and("DATA_ID", dataId);

        return ServDao.finds(ServMgr.SY_SERV_FLOW, sql);
    }
}
