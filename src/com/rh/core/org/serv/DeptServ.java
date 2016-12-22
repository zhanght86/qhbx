package com.rh.core.org.serv;

import java.util.ArrayList;
import java.util.List;

import com.rh.core.base.Bean;
import com.rh.core.base.Context;
import com.rh.core.base.Context.APP;
import com.rh.core.base.db.Transaction;
import com.rh.core.org.UserBean;
import com.rh.core.org.util.OrgConstant;
import com.rh.core.plug.im.ImMgr;
import com.rh.core.serv.CommonServ;
import com.rh.core.serv.OutBean;
import com.rh.core.serv.ParamBean;
import com.rh.core.serv.ServDao;
import com.rh.core.serv.ServDefBean;
import com.rh.core.serv.ServMgr;
import com.rh.core.serv.bean.SqlBean;
import com.rh.core.serv.ServUtils;
import com.rh.core.util.Constant;
import com.rh.core.util.PinyinUtils;
import com.rh.ldap.ADMgr;

/**
 * 部门服务类
 * 
 * @author cuihf
 * 
 */
public class DeptServ extends CommonServ {

    /**
     * 与RTX通用部门和用户
     * @param paramBean 参数信息
     * @return 同步结果
     */
    public OutBean syncIm(ParamBean paramBean) {
        String servId = paramBean.getServId();
        int count = 0;
        if (Context.appBoolean(APP.IM)) { //只有启动了IM才进行同步
            //添加部门信息
            ParamBean queryBean = new ParamBean();
            UserBean userBean = Context.getUserBean();
            queryBean.set("CMPY_CODE", userBean.getCmpyCode());
            queryBean.set(Constant.PARAM_ORDER, "DEPT_LEVEL,DEPT_SORT");
            List<Bean> deptList = ServDao.finds(servId, queryBean);
            for (Bean data : deptList) {
                if (ImMgr.getIm().saveDept(data)) {
                    count++;
                }
            }
            //添加用户信息
            queryBean = new ParamBean(ServMgr.SY_ORG_USER, ServMgr.ACT_FINDS);
            queryBean.set("CMPY_CODE", userBean.getCmpyCode());
            List<Bean> userList = ServMgr.act(queryBean).getDataList();
            for (Bean data : userList) {
                data.set("USER_PASSWORD_REAL", Context.getSyConf("SY_USER_PASSWORD_INIT", "123456"));
                if (ImMgr.getIm().saveUser(data)) {
                    count++;
                }
            }
        }
        OutBean outBean = new OutBean();
        if (count > 0) {
            outBean.setOk(Context.getSyMsg("SY_SYNC_OK", String.valueOf(count)));
        } else {
            outBean.setError(Context.getSyMsg("SY_SYNC_ERROR"));
        }
        return outBean;
    }

    /**
     * 删除之后更新IM处理
     * @param paramBean 参数信息
     * @param outBean 删除结果信息
     */
    protected void afterDelete(ParamBean paramBean, OutBean outBean) {
        if (Context.appBoolean(APP.IM)) { //启动了IM进行同步
            ImMgr.getIm().deleteDept(paramBean.getId());
        }
        
        
        /**
         * 删除域组织机构
         */
        String conf = Context.getSyConf("AD_DEPT_OPEN", "2");
        log.info("-------删除部门获取是否同步值-----AD_DEPT_OPEN==" + conf + "-----");
		if ("1".equals(conf)) {
			ADMgr adMgr = new ADMgr();
			log.info("-------AD:删除部门-----begin-----");
			OutBean createBean = adMgr.delDept(paramBean);
			log.info("-------AD:删除部门-----end-----");
			outBean.setMsg(outBean.getMsg() + createBean.getMsg());
		}        
    }
    
    /**
     * 保存之前处理
     * @param paramBean 参数信息
     */
    protected void beforeSave(ParamBean paramBean) {
        String servId = paramBean.getServId();
        Bean newData = paramBean.getSaveFullData(); //获取全数据
        if (newData.isEmpty("DEPT_FULL_NAME")) { //部门全称不设置缺省等于部门名称
            paramBean.set("DEPT_FULL_NAME", newData.getStr("DEPT_NAME"));
        }
        if (paramBean.contains("DEPT_NAME")) { //计算部门简称
            paramBean.set("DEPT_SHORT_NAME", PinyinUtils.getHeadStr(paramBean.getStr("DEPT_NAME")));
        }
        if (paramBean.contains("DEPT_PCODE") || paramBean.contains("DEPT_TYPE")) { //存在父部门或设置部门类型
            String curCode = newData.getStr("DEPT_CODE");
            if (newData.isEmpty("DEPT_PCODE")) { //没有设置父部门
                paramBean.set("DEPT_TYPE", OrgConstant.DEPT_TYPE_ORG); //没有父部门的缺省为机构
                paramBean.set("TDEPT_CODE", curCode); //有效部门为自己
                paramBean.set("ODEPT_CODE", curCode); //机构为自己
            } else {
                Bean pDept = ServDao.find(servId, newData.getStr("DEPT_PCODE"));
                if (newData.getInt("DEPT_TYPE") == OrgConstant.DEPT_TYPE_ORG) { //当前部门为机构
                    paramBean.set("TDEPT_CODE", curCode); //有效部门为自己
                    paramBean.set("ODEPT_CODE", curCode); //机构为自己
                } else { //当前部门不是机构
                    if (pDept.getInt("DEPT_TYPE") == OrgConstant.DEPT_TYPE_ORG) { //父部门为机构，则子部门为有效部门
                        paramBean.set("TDEPT_CODE", curCode); //当前有效部门为自己
                    } else {
                        paramBean.set("TDEPT_CODE", pDept.getStr("TDEPT_CODE")); //有效部门继承父部门的
                    }
                    paramBean.set("ODEPT_CODE", pDept.getStr("ODEPT_CODE")); //机构继承父部门的
                }
            }
        }
    }
    
    /**
     * 保存之后处理
     * @param paramBean 参数信息
     * @param outBean  参数信息
     */
    protected void afterSave(ParamBean paramBean, OutBean outBean) {
        String servId = paramBean.getServId();
        if (!paramBean.getAddFlag()) { //修改模式
            Bean oldBean = paramBean.getSaveOldData();
            Bean setBean = new Bean();
            SqlBean whereBean = new SqlBean();
            //根据当前部门类型更新所有类型为部门的子孙部门
            
            if (paramBean.contains("DEPT_TYPE") 
                    && (paramBean.getInt("DEPT_TYPE") != oldBean.getInt("DEPT_TYPE"))) { //变更了部门类型
                if (paramBean.getInt("DEPT_TYPE") == OrgConstant.DEPT_TYPE_ORG) { //部门变机构
                    //找到所有直接子部门（类型为部门）
                    whereBean.and("DEPT_TYPE", OrgConstant.DEPT_TYPE_DEPT).and("DEPT_PCODE", oldBean.getId());
                    List<Bean> subList = ServDao.finds(servId, whereBean);
                    for (Bean sub : subList) {
                        whereBean = new SqlBean().and("DEPT_TYPE", OrgConstant.DEPT_TYPE_DEPT)
                                .and("ODEPT_CODE", sub.getStr("ODEPT_CODE"))
                                .andLikeRT("CODE_PATH", sub.getStr("CODE_PATH")); //向下修改所有子部门及子的子孙部门
                        setBean.set("ODEPT_CODE", outBean.getStr("ODEPT_CODE"))
                            .set("TDEPT_CODE", sub.getId()); //将子部门设为有效部门
                        ServDao.updates(servId, setBean, whereBean);
                    }
                } else { //机构变部门,找到本机构下所有子孙部门（类型为部门）
                    whereBean.and("DEPT_TYPE", OrgConstant.DEPT_TYPE_DEPT)
                        .and("ODEPT_CODE", oldBean.getStr("ODEPT_CODE"))
                        .andLikeRT("CODE_PATH", oldBean.getStr("CODE_PATH")); //向下修改所有子部门及子的子孙部门
                    setBean.set("ODEPT_CODE", outBean.getStr("ODEPT_CODE"))
                        .set("TDEPT_CODE", outBean.getStr("TDEPT_CODE"));
                    ServDao.updates(servId, setBean, whereBean);
                }
            } else if (paramBean.contains("TDEPT_CODE")
                    && !(paramBean.getStr("TDEPT_CODE").equals(oldBean.getStr("TDEPT_CODE")))) { //变更了有效部门
                whereBean.and("DEPT_TYPE", OrgConstant.DEPT_TYPE_DEPT).and("ODEPT_CODE", oldBean.getStr("ODEPT_CODE"))
                    .andLikeRT("CODE_PATH", oldBean.getStr("CODE_PATH"));
                setBean.set("ODEPT_CODE", outBean.getStr("ODEPT_CODE")).set("TDEPT_CODE", outBean.getStr("TDEPT_CODE"));
                ServDao.updates(servId, setBean, whereBean);
            }
        }
        if (Context.appBoolean(APP.IM)) { //启动了IM进行同步
            ImMgr.getIm().saveDept(outBean);
        }
        
        
        /**
         * 创建域组织机构
         */
        String conf = Context.getSyConf("AD_DEPT_OPEN", "2");
        log.info("-------修改部门获取是否同步值-----AD_DEPT_OPEN==" + conf + "-----");
		if ("1".equals(conf)) {
			ADMgr adMgr = new ADMgr();
			if (paramBean.getAddFlag()) {
				log.info("-------AD:创建部门-----begin-----");
				OutBean createBean = adMgr.createDept(paramBean);
				log.info("-------AD:创建部门-----end-----");
				outBean.setMsg(outBean.getMsg() + createBean.getMsg());
			} else {
				log.info("-------AD:修改部门-----begin-----");
				OutBean modifyBean = adMgr.modifyDept(paramBean);
				log.info("-------AD:修改部门-----end-----");
				outBean.setMsg(outBean.getMsg() + modifyBean.getMsg());
			}
		}        
        

        /**
         * 如果父部门OU串改变，就更新子节点OU串
         */
        String memoStr =paramBean.getStr("DEPT_MEMO");
        if(!memoStr.equals("")){
	        List<Bean> deptlist = ServDao.finds(servId, "and DEPT_PCODE='" + paramBean.getId() + "' and S_FLAG=1");
	        if (deptlist.size()>0) {
	        	for (int i = 0; i < deptlist.size(); i++) {
	        		ParamBean deptMemo = new ParamBean();
	        		deptMemo.setId(deptlist.get(i).getStr("DEPT_CODE"));
	        		String ou = "OU="+deptlist.get(i).getStr("DEPT_NAME") +"," + memoStr;
	        		deptMemo.set("DEPT_MEMO", ou);
					Bean b =ServDao.save(servId, deptMemo);
					
					
					//孙子部门节点（处室）
					List<Bean> deptlist2 = ServDao.finds(servId, "and DEPT_PCODE='" + b.getId() + "' and S_FLAG=1");
					if (deptlist2.size() > 0) {
						for (int j = 0; j < deptlist2.size(); j++) {
			        		ParamBean deptMemo2 = new ParamBean();
			        		deptMemo2.setId(deptlist2.get(j).getStr("DEPT_CODE"));
			        		String ou2 = "OU="+deptlist2.get(j).getStr("DEPT_NAME") +"," + b.getStr("DEPT_MEMO");
			        		deptMemo2.set("DEPT_MEMO", ou2);
							ServDao.save(servId, deptMemo2);
							List<Bean> listUser = ServDao.finds(ServMgr.SY_ORG_USER, " and DEPT_CODE='"+deptlist2.get(j).getStr("DEPT_CODE")+"' and S_FLAG=1");
							modifyUserCn(listUser,ou2);
						}
					}
										
					List<Bean> listUser = ServDao.finds(ServMgr.SY_ORG_USER, " and DEPT_CODE='"+deptlist.get(i).getStr("DEPT_CODE")+"'");
					modifyUserCn(listUser,ou);
				}
	        }
	        
	        List<Bean> userlist = ServDao.finds(ServMgr.SY_ORG_USER, "and DEPT_CODE='" + paramBean.getId() + "' and S_FLAG=1");
	        modifyUserCn(userlist,memoStr);
	        ServDefBean servDef = ServUtils.getServDef(ServMgr.SY_ORG_DEPT_ALL);
	        servDef.clearDictCache();
	        ServDefBean servDef2 = ServUtils.getServDef(ServMgr.SY_ORG_USER);
	        servDef2.clearDictCache();
        }
        
    }

    /**
     * 修改用户ou串
     */
    public void modifyUserCn(List<Bean> listUser ,String ou) {
		if (listUser.size()>0) {
			for (int j = 0; j < listUser.size(); j++) {
				String cn = "CN="+ listUser.get(j).getStr("USER_NAME")+"," + ou;
				ParamBean userOu = new ParamBean();
				userOu.setId(listUser.get(j).getStr("USER_CODE"));
				userOu.set("USER_AD_OU", cn);
				ServDao.save(ServMgr.SY_ORG_USER, userOu);
		        String conf = Context.getSyConf("AD_DEPT_USER_OPEN", "2");
		        log.info("-------修改部门用户名称获取是否同步值-----AD_DEPT_USER_OPEN==" + conf + "-----");
		        if ("1".equals(conf)) {
					ParamBean saveBean = new ParamBean();
					saveBean.set("DEPT_CODE",listUser.get(j).getStr("DEPT_CODE"));
					saveBean.set("USER_AD_OU", cn);
					ADMgr adMgr = new ADMgr();
					adMgr.modifyAttr(saveBean);
		        }
			}
		}		
	}
    
    
    /**
     * 初始化部门简称
     * @param paramBean 参数
     * @return 更新数量
     */
    public OutBean initPinyin(ParamBean paramBean) {
        ServDefBean servDef = ServUtils.getServDef(paramBean.getServId());
        ParamBean param = new ParamBean(ServMgr.SY_ORG_DEPT_ALL, ServMgr.ACT_FINDS);
        param.setWhere(servDef.getServDefWhere());
        List<Bean> list = ServMgr.act(param).getDataList();
        List<Bean> dataList = new ArrayList<Bean>(list.size());
        for (Bean dept : list) {
            Bean data = new Bean();
            data.set("DEPT_SHORT_NAME", PinyinUtils.getHeadStr(dept.getStr("DEPT_NAME")))
                .set("DEPT_CODE", dept.getId());
            dataList.add(data);
        }
        String sql = "update SY_ORG_DEPT set DEPT_SHORT_NAME=#DEPT_SHORT_NAME# where DEPT_CODE=#DEPT_CODE#";
        int count = Transaction.getExecutor().executeBatchBean(sql, dataList);
        return new OutBean().setOk(Context.getSyMsg("SY_BATCHSAVE_OK", count+""));
    }
}
