package com.rh.oa.gw;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.rh.core.base.Bean;
import com.rh.core.base.Context;
import com.rh.core.org.UserBean;
import com.rh.core.org.mgr.UserMgr;
import com.rh.core.serv.ParamBean;
import com.rh.core.serv.ServDefBean;
import com.rh.core.serv.ServUtils;
import com.rh.core.wfe.WfAct;
import com.rh.core.wfe.WfFilter;
import com.rh.core.wfe.serv.DoingOutBean;
import com.rh.core.wfe.serv.WfOut;
import com.rh.core.wfe.util.WfBtnConstant;
import com.rh.oa.gw.util.GwConstant;
import com.rh.oa.gw.util.GwFileHelper;
import com.rh.oa.gw.util.GwUtils;

/**
 * 从流程定义的节点上取出的 表单按钮 ， 根据实际情况进行过滤
 * 
 */
public class GwWfFliter implements WfFilter {

	/** 文件管理 */
	public static final String BUTTON_FILE_MANAGE = "fileGuanLi";

	/** 锁定 */
	public static final String BUTTON_LOCK = "lock";

	/** 解除锁定 */
	public static final String BUTTON_UNLOCK = "unlock";

	/** 文档一体 */
	public static final String BUTTON_WENDANGYITI = "wenDangYiTi";

	/** 转换类型 */
	public static final String BUTTON_CONVERSION = "conversion";

	/** 套红头 */
	public static final String BUTTON_REDHEAD = "redHead";

	/** 扫描正文 */
	public static final String BUTTON_SCAN = "scan";

	/** 电子印章 */
	public static final String BUTTON_SEAL = "seal";

	/** 取消印章 */
	public static final String BUTTON_UNDOSEAL = "undoSeal";

	/** 加密 */
	public static final String BUTTON_ENCRYPT = "encrypt";

	/** 解密 */
	public static final String BUTTON_DECRYPT = "decrypt";
	
	/** 打印正文 */
    public static final String BUTTON_PRINTZW = "printZw";
    
    /** 批量打印 **/
    public static final String BUTTON_WFPRINTDAGDINFO = "printDaGdInfo";
    
    /** 追加打印份数 */
    public static final String BUTTON_APPEND_PRINT_NUM = "appendPrintNum";
    
    /** 打印正文角色 **/
    public static final String ROLE_OA_GW_PRINT_ROLE = "OA_GW_PRINT_ROLE";
    
    /** 数据管理按钮角色 **/
    public static final String ROLE_OA_GW_WFDATAMGR_ROLE = "OA_GW_WFDATAMGR_ROLE";

    /** 办结后批量打印按钮角色 **/
    public static final String SY_WFE_SPRINT_AUDIT_ROLE = "SY_WFE_SPRINT_AUDIT_ROLE";
    
	/**
	 * 服务ID
	 */
	private String servId;

	/**
	 * 服务上定义的所有的act
	 */
	private Map<String, Bean> allActMap = new HashMap<String, Bean>();

	//private List<Bean> defBtnList;

	private WfOut wfOutBean = null;

	/**
	 * 
	 */
	public GwWfFliter() {
	}

	/**
	 * @param aWfAct
	 *            节点实例
	 * @param aWfOutBean
	 *            工作流输出到前台的数据对象
	 */
	public void doButtonFilter(WfAct aWfAct, WfOut aWfOutBean) {
		// this.wfAct = aWfAct;
		wfOutBean = aWfOutBean;
		//this.defBtnList = wfOutBean.getBtnBeanList();

		this.servId = wfOutBean.getWfProc().getProcDef().getStr("SERV_ID");
		
		// 初始化 节点上 定义的 所有的act
		getServACTs();

		// 增加管理员对应的按钮
		addAdminUserBtns();

		//为“文印岗”增加“打印正文、打印稿纸”按钮
		addPrintBtn(aWfOutBean);
		
		//为制定角色添加“数据管理”按钮
		addWfDataMgrBtn(aWfOutBean);
		
	    //文件办结后为制定角色添加“批量打印”按钮 20150205
		addSPRINTMgrBtn(aWfOutBean);
		
		// 添加节点上定义的ACT 操作
		addDefActs(aWfOutBean.getDoUser());
		
		if (!wfOutBean.getOutBean().isEmpty("SEND_ID")) {
		    aWfOutBean.addBtnBean(allActMap.get("toShouWen"));
		}
		
		
	}

	/**
	 * 增加管理员有权限的按钮
	 */
	private void addAdminUserBtns() {
		if (wfOutBean.getWfProc().isProcManage()) {
			addConversionBtn();
		}
	}

	/**
	 * 添加节点定义上定义的ACT
	 * @param doUser 办理用户
	 */
	private void addDefActs(UserBean doUser) {

		if (!wfOutBean.existBtnBean(WfBtnConstant.BUTTON_DELETE) && canDel(doUser)) {
		    wfOutBean.addDeleteBtn();
		}

		if (isLocked() && wfOutBean.existBtnBean(BUTTON_LOCK)) {
		    wfOutBean.removeBtnBean(wfOutBean.getBtnBean(BUTTON_LOCK));
		}

		if (canUnLock() && !wfOutBean.existBtnBean(BUTTON_UNLOCK)) {
		    wfOutBean.addBtnBean(allActMap.get(BUTTON_UNLOCK));
		}

		if (!isLocked() && wfOutBean.existBtnBean(BUTTON_UNLOCK)) {
		    wfOutBean.removeBtnBean(wfOutBean.getBtnBean(BUTTON_UNLOCK));
		}

		addSealBtn();

		addDecryptBtn();
	}
	
	/**
	 * 1，流程中定义了按钮，但是没有PDF文件，移除按钮。
	 * 2，如果符合权限（例如"文印岗"），且有PDF文件，则增加此按钮
	 * 为"文印岗"增加"打印正文、打印稿纸"按钮
	 * @param aWfOutBean 
	 */
    private void addPrintBtn(WfOut aWfOutBean) {
        final Bean gwParam = GwServ.getGwParamBean(aWfOutBean.getParamBean());
        //1.没有PDF文件，且流程中定义了 打印正文按钮，则移除“打印正文”按钮
        if (gwParam.isEmpty(GwConstant.EXIST_SEAL_PDF_FILE)) {
            // 判断是否存在“打印正文”按钮
            if (aWfOutBean.existBtnBean(BUTTON_PRINTZW)) {
                //移除按钮
                wfOutBean.removeBtnBean(BUTTON_PRINTZW);
                wfOutBean.removeBtnBean(BUTTON_APPEND_PRINT_NUM);
            }
            return;
        }
        
        // 得到用户的code
        String userCode = wfOutBean.getParamBean().getDoUserBean().getCode();
        // 获取系统配置的角色：OA_GW_PRINT_BUTTON
        String roleCodeStr = Context.getSyConf(ROLE_OA_GW_PRINT_ROLE, "");
        if (StringUtils.isEmpty(roleCodeStr)) {
            return;
        }
        
        // 如果符合权限，且有pdf文件，则有“打印正文”按钮。判断用户是否有...角色
        if (UserMgr.existInRoles(userCode, roleCodeStr)) {
            // 获取服务定义信息，拿到 打印正文的Bean
            ServDefBean servDef = ServUtils.getServDef(this.wfOutBean.getOutBean().getStr("TMPL_CODE"));
            // 添加“打印正文”按钮
            Bean printZwBean = servDef.getAct(BUTTON_PRINTZW);
            this.wfOutBean.addBtnBean(printZwBean);
            
//            //批量打印
//            Bean printsZwBean = servDef.getAct(BUTTON_WFPRINTDAGDINFO);
//            this.wfOutBean.addBtnBean(printsZwBean);
            
            //“打印审批单”按钮
            Bean printAudit = this.wfOutBean.getProcServDefAct(WfBtnConstant.BUTTON_PRINTAUDIT);
            this.wfOutBean.addBtnBean(printAudit);
            
            GwSealProcess gwSeal = GwUtils.createGwSeal();
            
            if (gwParam.isNotEmpty("gwFileHelper")) {
                GwFileHelper gfh = (GwFileHelper) gwParam.get("gwFileHelper");
                if (gwSeal.canAppendPrintNum(gfh.getZhengwen())) {
                    // "追加打印份数"按钮
                    Bean appendPrintNum = servDef.getAct(BUTTON_APPEND_PRINT_NUM);
                    this.wfOutBean.addBtnBean(appendPrintNum);
                }
            }
        }
    }
    
    /**
     * 为指定角色增加“数据管理”按钮
     * @param aWfOutBean 
     */
    private void addWfDataMgrBtn(WfOut aWfOutBean) {
        String roleCodes = Context.getSyConf(ROLE_OA_GW_WFDATAMGR_ROLE, "");
        String userCode = aWfOutBean.getParamBean().getDoUserBean().getCode();
        if (StringUtils.isEmpty(roleCodes)) {
            return;
        }
        Bean wfDataMgrBean = this.wfOutBean.getProcServDefAct(WfBtnConstant.BUTTON_WFDATAMGR);
        if(wfDataMgrBean!=null){
        	if (UserMgr.existInRoles(userCode, roleCodes) && wfOutBean.isGteSUserODeptLevel()) {
        		wfOutBean.addBtnBean(wfDataMgrBean);
        	} else {
        		wfOutBean.removeBtnBean(wfDataMgrBean);
        	}
        }
    }
    /**
     * 文件办结后为指定角色增加“批量打印”按钮20150205
     * @param aWfOutBean 
     */
    private void addSPRINTMgrBtn(WfOut aWfOutBean) {
//        String roleCodes = Context.getSyConf(SY_WFE_SPRINT_AUDIT_ROLE, "");
//        String userCode = aWfOutBean.getParamBean().getDoUserBean().getCode();
//        if (StringUtils.isEmpty(roleCodes)) {
//            return;
//        }
//        Bean wfDataMgrBean = this.wfOutBean.getProcServDefAct(WfBtnConstant.BUTTON_WFPRINTDAGDINFO);
//        if (UserMgr.existInRoles(userCode, roleCodes) && wfOutBean.isGteSUserODeptLevel()) {
//            wfOutBean.addBtnBean(wfDataMgrBean);
//        } else {
//            wfOutBean.removeBtnBean(wfDataMgrBean);
//        }
//

        final Bean gwParam = GwServ.getGwParamBean(aWfOutBean.getParamBean());
            
        // 得到用户的code
        String userCode = wfOutBean.getParamBean().getDoUserBean().getCode();
        // 获取系统配置的角色：OA_GW_PRINT_BUTTON
        String roleCodeStr = Context.getSyConf(SY_WFE_SPRINT_AUDIT_ROLE, "");
        if (StringUtils.isEmpty(roleCodeStr)) {
            return;
        }
        
        // 如果符合权限，且有pdf文件，则有“打印正文”按钮。判断用户是否有...角色
        if (UserMgr.existInRoles(userCode, roleCodeStr)) {
            // 获取服务定义信息，拿到 打印正文的Bean
            ServDefBean servDef = ServUtils.getServDef(this.wfOutBean.getOutBean().getStr("TMPL_CODE"));
//            // 添加“打印正文”按钮
//            Bean printZwBean = servDef.getAct(BUTTON_PRINTZW);
//            this.wfOutBean.addBtnBean(printZwBean);
            
            //批量打印
            Bean printsZwBean = servDef.getAct(BUTTON_WFPRINTDAGDINFO);
            this.wfOutBean.addBtnBean(printsZwBean);
            
//            //“打印审批单”按钮
//            Bean printAudit = this.wfOutBean.getProcServDefAct(WfBtnConstant.BUTTON_PRINTAUDIT);
//            this.wfOutBean.addBtnBean(printAudit);
            
//            GwSealProcess gwSeal = GwUtils.createGwSeal();
            
//            if (gwParam.isNotEmpty("gwFileHelper")) {
//                GwFileHelper gfh = (GwFileHelper) gwParam.get("gwFileHelper");
//                if (gwSeal.canAppendPrintNum(gfh.getZhengwen())) {
//                    // "追加打印份数"按钮
//                    Bean appendPrintNum = servDef.getAct(BUTTON_APPEND_PRINT_NUM);
//                    this.wfOutBean.addBtnBean(appendPrintNum);
//                }
//            }
        }
    
    }
	/**
	 * 增加印章按钮和取消印章按钮
     * // TODO 有盖章按钮，且已经盖章，则显示取消印章按钮。
     * // TODO 未套红头不能盖章，提示用户。_btnDisabledMsg
	 */
	private void addSealBtn() {
	    ParamBean paramBean = this.wfOutBean.getParamBean();
        if (!this.wfOutBean.existBtnBean("seal")) {
            return;
        }
        if (paramBean.isNotEmpty(GwConstant.GW_PARAM)) {
            Bean gwParam = paramBean.getBean(GwConstant.GW_PARAM);
            //增加取消印章按钮
            if (gwParam.getBoolean("CAN_UNDOSEAL") && isDoingMode()) {
                addUndoSealBtn();
                this.wfOutBean.removeBtnBean(BUTTON_SEAL);
            }
        }
	}
	
    /**
     * 
     * @return 是否是办理模式
     */
    private boolean isDoingMode() {
        String strMode = this.wfOutBean.getOutBean().getStr("WF_DISPLAY_MODE");
        if (strMode.equals(DoingOutBean.MODE_DOING)) {
            return true;
        }

        return false;
    }
	
	/**
	 * 增加取消印章按钮
	 */
    private void addUndoSealBtn() {
        ServDefBean servDef = ServUtils.getServDef(this.wfOutBean.getOutBean().getStr("TMPL_CODE"));
        Bean actBean = servDef.getAct(BUTTON_UNDOSEAL);
        this.wfOutBean.addBtnBean(actBean);
    }

	/**
	 * 判断是否显示解密按钮
	 */
	private void addDecryptBtn() {
		// TODO 如果没有解密文件，则按钮变灰，显示提示信息
	}

	/**
	 * 
	 * @return 能否取消锁定
	 */
	private boolean canUnLock() {

		return false;
	}

	/**
	 * 
	 * @return 是否已锁定
	 */
	private boolean isLocked() {
		return false;
	}

	/**
	 * @param doUser 办理用户
	 * @return 能否删除
	 */
	private boolean canDel(UserBean doUser) {
		if (wfOutBean.getWfProc().canDelete(doUser)) {
			return true;
		}
		return false;
	}

	/**
	 * 
	 * 表单上定义的所有的操作 列表, 也需要查父服务的ACT
	 */
	private void getServACTs() {
	    allActMap = ServUtils.getServDef(servId).getAllActs();
	}

	/**
	 * 管理员权限：增加转换类型按钮
	 */
	private void addConversionBtn() {
//		if (!wfOutBean.existBtnBean(BUTTON_CONVERSION)) {
//			this.defBtnList.add(allActMap.get(BUTTON_CONVERSION));
//		}
	}

}
