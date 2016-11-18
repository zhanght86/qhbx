package com.rh.core.comm.news.serv;

import java.util.List;
import org.apache.commons.lang.StringUtils;
import com.rh.core.base.Bean;
import com.rh.core.comm.news.mgr.HtmlParser;
import com.rh.core.comm.news.mgr.NewsMgr;
import com.rh.core.serv.CommonServ;
import com.rh.core.serv.OutBean;
import com.rh.core.serv.ParamBean;
import com.rh.core.serv.ServDao;
import com.rh.core.serv.ServMgr;
import com.rh.core.util.var.DateVar;

/**
 * 信息上报
 * @author zhl
 */
public class NewsAuditServ extends CommonServ {

    /** 信息上报*/
	public static final String SERV_SY_COMM_INFOS_AUDIT = "SY_COMM_INFOS_AUDIT";
	/**栏目管理*/
	public static final String CHANNEL_SERV = ServMgr.SY_COMM_INFOS_CHNL;
	/** 信息服务*/
	public static final String SERV_SY_COMM_INFOS = "SY_COMM_INFOS_BASE";
	/** 文件服务*/
	public static final String SERV_SY_COMM_FILE = "SY_COMM_FILE";
	/** 已发布栏目*/
	public static final String COL_CHNL_POST_ID = "CHNL_POST_ID";
	/** 已发布状态*/
	public static final int IS_POSTED = 6;
	/** 主键*/
	public static final String KEY_ID = "_PK_";
	/**站点 */
	private String siteid = "";
	/**
	 * 设置发布到的栏目
	 * @param paramBean   参数
	 * @return 返回结果
	 */
	public OutBean setPostChnlId(ParamBean paramBean) {
		// 设置栏目之前判断设置的栏目是否相同不相同
		OutBean outBean = ServMgr.act(SERV_SY_COMM_INFOS_AUDIT, ServMgr.ACT_SAVE, paramBean);
		outBean.set("CHNL_ID", paramBean.get("CHNL_ID"));
		outBean.setOk("成功选择发布栏目！");
		return outBean;
	}

	@Override
	protected void beforeQuery(ParamBean paramBean) {
		// 接收菜单传的站点，
		if (paramBean.isNotEmpty(NewsMgr.DICT_EXTWHERE)) {
			siteid = paramBean.getStr("SITE_ID");
			paramBean.setWhere(paramBean.getStr(NewsMgr.DICT_EXTWHERE));
		}
	}

	@Override
	protected void afterSave(ParamBean paramBean, OutBean outBean) {
		// 自动填写摘要、短标题
		String body = outBean.getStr("NEWS_BODY");
		body = new HtmlParser().parse(body);
		Bean upBean = new Bean();

		// 保存站点
		if (outBean.isEmpty("SITE_ID")) {
			upBean.set("SITE_ID", siteid).setId(outBean.getId());
		}

		// 是否有图
		Bean imgQuery = new Bean().set("SERV_ID", ServMgr.SY_COMM_INFOS_AUDIT).set("DATA_ID", outBean.getId());
		imgQuery.set("FILE_CAT", "TUPIANJI");
		int imgCount = ServDao.count(ServMgr.SY_COMM_FILE, imgQuery);
		if (imgCount > 0) {
			upBean.set("HAS_IMAGE", 1).setId(outBean.getId());
		}

		// 是否有附件
		Bean attachQuery = new Bean().set("SERV_ID", ServMgr.SY_COMM_INFOS_AUDIT).set("DATA_ID", outBean.getId());
		attachQuery.set("FILE_CAT", "FUJIAN");
		int aCount = ServDao.count(ServMgr.SY_COMM_FILE, attachQuery);
		if (aCount > 0) {
			upBean.set("HAS_ATTACH", 1).setId(outBean.getId());
		}

		if (null != upBean.getId() && 0 < upBean.getId().length()) {
			ServDao.save("SY_COMM_INFOS_AUDIT", upBean);
		}

	}

	@Override
	public OutBean byid(ParamBean paramBean) {
		OutBean result = super.byid(paramBean);
		NewsMgr.getInstance().setExtendValues(result);
		return result;
	}

	/**
	 * 发布到审核指定的栏目里同时复制信息到news表
	 * @param paramBean 参数     
	 * @return 返回结果
	 */
	public OutBean postToChnl(ParamBean paramBean) {
		String auditId = paramBean.getId();
		Bean auditBean = ServDao.find(SERV_SY_COMM_INFOS_AUDIT, new ParamBean().setId(auditId));
		// 取得发布的栏目
		String chnlId = auditBean.getStr("CHNL_ID");
		OutBean outBean = new OutBean();
		if (StringUtils.isNotBlank(chnlId)) {
			auditBean.set("CHNL_ID", chnlId);
			auditBean.set("NEWS_CHECKED", NewsMgr.POST);
			ServMgr.act(SERV_SY_COMM_INFOS_AUDIT, ServMgr.ACT_SAVE,
					new ParamBean().set(KEY_ID, auditId).set("NEWS_CHECKED", NewsMgr.POST));
			Bean newsout = ServDao.find(SERV_SY_COMM_INFOS,
					new ParamBean().set("AUDIT_ID", auditId).set("CHNL_ID", chnlId));
			if (newsout == null) {
				auditBean.set(KEY_ID, "");
			} else {
				auditBean.set(KEY_ID, newsout.get(KEY_ID));
			}

			// 流程用户暂时保存流程实例，1、解决信息发布过不能删除问题，2、用于查询当前实例是否已经复制过
			auditBean.set("S_WF_USER", auditBean.getStr("S_WF_INST"));
			auditBean.set("S_WF_INST", "");
			auditBean.set("S_WF_NODE", "");
			auditBean.set("S_WF_STATE", 0);
			String nowdate = DateVar.getInst().get("@DATETIME@");
			auditBean.set("NEWS_TIME", nowdate);
			Bean out = ServMgr.act(SERV_SY_COMM_INFOS, ServMgr.ACT_SAVE, new ParamBean(auditBean));
			String newid = out.getStr("NEWS_ID");
			String auditid = out.getStr("AUDIT_ID");
			// 同时复制图片集
			List<Bean> tupianBeans = ServDao.finds(SERV_SY_COMM_FILE,
					new ParamBean().set("DATA_ID", auditid).set("FILE_CAT", "TUPIANJI"));
			if (tupianBeans.size() > 0) {
				for (Bean tupian : tupianBeans) {
					tupian.set("_PK_", "");
					tupian.set("FILE_ID", "");
					tupian.set("DATA_ID", newid);
					tupian.set("SERV_ID", SERV_SY_COMM_INFOS);
					ServDao.save(SERV_SY_COMM_FILE, tupian);
				}
			}
			// 同时复制附件信息
			List<Bean> fujianBeans = ServDao.finds(SERV_SY_COMM_FILE,
					new ParamBean().set("DATA_ID", auditid).set("FILE_CAT", "FUJIAN"));
			if (fujianBeans.size() > 0) {
				for (Bean fujian : fujianBeans) {
					fujian.set("_PK_", "");
					fujian.set("FILE_ID", "");
					fujian.set("DATA_ID", newid);
					fujian.set("SERV_ID", SERV_SY_COMM_INFOS);
					ServDao.save(SERV_SY_COMM_FILE, fujian);
				}
			}

			if (!out.equals("")) {
				outBean.setOk("成功发布信息！");
			}
		} else {
			outBean.setError("要发布到的栏目为空！");
		}

		return outBean;
	}
}
