// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   DaServ.java

package com.rh.da.da;

import com.rh.core.base.*;
import com.rh.core.org.UserBean;
import com.rh.core.serv.*;
import com.rh.core.serv.bean.SqlBean;
import com.rh.core.serv.dict.DictMgr;
import com.rh.core.util.*;
import com.rh.da.DaUtils;
import com.rh.da.dir.DaCategory;
import com.rh.da.dir.serv.DaDirServ;
import java.util.*;
import org.apache.commons.lang.StringUtils;

public class DaServ extends CommonServ
{

	public static final String DA_CATEGORY = "DA_CATEGORY";
	public static final String DA_DIR = "DA_DIR";
	public static final String DA_BOX_MAX_PAGE = "DA_BOX_MAX_PAGE";

	public DaServ()
	{
	}

	public void beforeSave(ParamBean paramBean)
	{
		Bean dataBean = getCodeRuleVal(paramBean.getSaveFullData());
		paramBean.set("CAT_ID", dataBean.getStr("CAT_ID"));
		if (paramBean.getAddFlag()) {
			paramBean.set("DA_CODE", setDaCode(paramBean.getSaveFullData()));
		}
	}

	public void beforeQuery(ParamBean paramBean)
	{
		if (paramBean.isNotEmpty("_treeWhere") && paramBean.getList("_treeWhere").size() > 0)
		{
			List dictList = paramBean.getList("_treeWhere");
			Iterator i$ = dictList.iterator();
			do
			{
				if (!i$.hasNext())
					break;
				Bean dict = (Bean)i$.next();
				if (dict.getStr("DICT_VALUE").endsWith("____"))
				{
					String dictId = dict.getStr("DICT_VALUE");
					String dirId = dictId.substring(0, dictId.indexOf("___"));
					String customStr = dictId.substring(dictId.indexOf("___"));
					dict.set("DICT_VALUE", dirId);
					customFilter(paramBean, customStr);
				}
			} while (true);
		} else
		{
			String extWhere = paramBean.getStr("_extWhere");
			extWhere = RequestUtils.decodeStr(extWhere);
			String servId = paramBean.getServId();
			String where = "";
			if (paramBean.isEmpty("V_ID"))
			{
				String catId = DaCategory.getCatId(servId);
				String odept = Context.getUserBean().getODeptCode();
				if (extWhere.indexOf("S_ODEPT") > 0)
					odept = getOdept(extWhere);
				Bean dirBean = DaDirServ.getDirBean(catId, odept);
				Bean dict = DictMgr.getDict("DA_DIR_MANAGE");
				String dictSubWhere = "''";
				if (null != dirBean)
					dictSubWhere = DictMgr.getDictSubSql(dict, dirBean.getId());
				where = (new StringBuilder()).append(" and P_ID in (").append(dictSubWhere).append(")").toString();
			}
			paramBean.set("_extWhere", (new StringBuilder()).append(extWhere).append(where).toString());
		}
	}

	private String getOdept(String orgStr)
	{
		String tmpStr = orgStr.substring(orgStr.indexOf("S_ODEPT"));
		return tmpStr.split("'")[1];
	}

	private void customFilter(ParamBean paramBean, String customStr)
	{
		String cusWhere = "";
		String arr$[] = customStr.split("____");
		int len$ = arr$.length;
		for (int i$ = 0; i$ < len$; i$++)
		{
			String levelStr = arr$[i$];
			String fields[] = levelStr.split("___");
			String field = fields[1];
			String value = DaUtils.decryTreeNodeStr(fields[2]);
			cusWhere = (new StringBuilder()).append(cusWhere).append(" and ").append(field).append(" = '").append(value).append("'").toString();
		}

		paramBean.set("_extWhere", (new StringBuilder()).append(paramBean.getStr("_extWhere")).append(cusWhere).toString());
	}

	public ArrayList getMarkVal(String codeRuleVal)
	{
		ArrayList markArr = new ArrayList();
		String subCodeRuleVal = codeRuleVal.substring(1, codeRuleVal.length() - 1);
		String subString[] = subCodeRuleVal.split("#");
		for (int i = 0; i < subString.length; i++)
			if (i % 2 != 0)
				markArr.add(subString[i]);

		return markArr;
	}

	public ArrayList getCodeVal(String codeRuleVal)
	{
		ArrayList codeArr = new ArrayList();
		String subCodeArr = codeRuleVal.substring(1, codeRuleVal.length() - 1);
		String subString[] = subCodeArr.split("#");
		for (int i = 0; i < subString.length; i++)
			if (i % 2 == 0)
				codeArr.add(subString[i]);

		return codeArr;
	}

	public Bean getCodeRuleVal(Bean paramBean)
	{
		String pID = paramBean.getStr("P_ID");
		Bean dataBean = ServDao.find("DA_DIR", (new Bean()).set("_WHERE_", (new StringBuilder()).append(" AND DIR_ID='").append(pID).append("'").toString()));
		paramBean.set("CAT_ID", dataBean.getStr("CAT_ID")).set("DIR_ID", pID).set("P_ID", pID).set("NUM_MARK", dataBean.getStr("NUM_MARK")).set("CODE_RULE_VAL", dataBean.getStr("CODE_RULE_VAL"));
		return paramBean;
	}

	public int selectMaxNum(Bean paramBean)
	{
		Bean valueBean = getCodeRuleVal(paramBean);
		ArrayList codeRule = getCodeVal(valueBean.getStr("CODE_RULE_VAL"));
		StringBuffer whereStr = new StringBuffer();
		for (int i = 0; i < codeRule.size(); i++)
			if (!"DA_NUM".equals(codeRule.get(i)) && !"".equals(paramBean.getStr(codeRule.get(i))))
			{
				whereStr.append((new StringBuilder()).append(" and ").append((String)codeRule.get(i)).append("='").toString());
				whereStr.append(paramBean.getStr(codeRule.get(i)));
				whereStr.append("'");
			}

		Bean dataBean = ServDao.find(paramBean.getStr("serv"), (new Bean()).set("_SELECT_", "MAX(DA_NUM)").set("_WHERE_", whereStr));
		return dataBean.getInt("DA_NUM");
	}

	public String setDaCode(Bean paramBean)
	{
		String codeToValue = selectDicVal(paramBean);
		if (StringUtils.isEmpty(codeToValue))
			return paramBean.getStr("DA_CODE");
		int maxDaNum = 0;
		if (paramBean.getInt("DA_NUM") == 0)
		{
			maxDaNum = selectMaxNum(paramBean);
			//档号流水号应自动加1，cuihf 2016-03-20
			paramBean.set("DA_NUM", Integer.valueOf(maxDaNum)+1);
		} else
		{
			maxDaNum = paramBean.getInt("DA_NUM");
		}
		return (new StringBuilder()).append(codeToValue).append(DaUtils.getDaCodeByNum(maxDaNum)).toString();
	}

	public String selectDicVal(Bean paramBean)
	{
		StringBuffer dataStr = new StringBuffer();
		Bean dataBean = getCodeRuleVal(paramBean);
		ArrayList markArr = getMarkVal(dataBean.getStr("CODE_RULE_VAL"));
		ArrayList codeNameArr = getCodeVal(dataBean.getStr("CODE_RULE_VAL"));
		ServDefBean servDef = ServUtils.getServDef(paramBean.getStr("serv"));
		for (int i = 0; i < codeNameArr.size(); i++)
		{
			String codeValue = paramBean.getStr(codeNameArr.get(i));
			if (!StringUtils.isNotEmpty(codeValue))
				continue;
			Bean itemBean = servDef.getItem(codeNameArr.get(i));
			if (itemBean.isNotEmpty("DICT_ID"))
			{
				String dictId = itemBean.getStr("DICT_ID");
				if (dictId.startsWith("@"))
				{
					ParamBean param = null;
					String config = itemBean.getStr("ITEM_INPUT_CONFIG");
					Bean configBean = JsonUtils.toBean(config.substring(config.indexOf(",") + 1));
					param = new ParamBean(configBean.getBean("params"));
					dictId = DictMgr.getDictId(dictId.substring(1), param);
				}
				String dictName = DictMgr.getFullName(dictId, codeValue);
				dataStr.append(dictName);
			} else
			{
				dataStr.append(codeValue);
			}
			if (i != markArr.size())
				dataStr.append((String)markArr.get(i));
		}

		String resultStr = dataStr.toString();
		if (StringUtils.isNotEmpty(resultStr))
		{
			String subStr = resultStr.substring(resultStr.length() - 1);
			int j = 0;
			do
			{
				if (j >= markArr.size())
					break;
				if (subStr.equals(markArr.get(j)))
				{
					resultStr = resultStr.substring(0, resultStr.length() - 1);
					break;
				}
				j++;
			} while (true);
		}
		return (new StringBuilder()).append(resultStr).append(dataBean.getStr("NUM_MARK")).toString();
	}

	public OutBean updateJianHao(ParamBean paramBean)
	{
		OutBean outBean = new OutBean();
		String dataStr = paramBean.getStr("dataValue");
		String dataArr[] = dataStr.split(",");
		HashMap dataMap = new HashMap();
		String whereStr = "'";
		for (int i = 0; i < dataArr.length; i++)
		{
			String idAndValue[] = dataArr[i].split("--");
			whereStr = (new StringBuilder()).append(whereStr).append(idAndValue[0]).append("','").toString();
			int daNum = Integer.parseInt(idAndValue[1]);
			dataMap.put(idAndValue[0], DaUtils.getDaCodeByNum(daNum));
		}

		whereStr = whereStr.substring(0, whereStr.length() - 2);
		ServDefBean serInfo = ServUtils.getServDef(paramBean.getStr("serv"));
		List beanInfo = ServDao.finds(paramBean.getStr("serv"), (new StringBuilder()).append(" and ").append(serInfo.getPKey()).append(" in (").append(whereStr).append(")").toString());
		for (int j = 0; j < beanInfo.size(); j++)
		{
			String daCode = ((Bean)beanInfo.get(j)).getStr("DA_CODE");
			if (StringUtils.isNotEmpty(daCode))
			{
				String daCodeArr[] = daCode.split("-");
				if (daCodeArr.length > 0)
				{
					daCodeArr[daCodeArr.length - 1] = (String)dataMap.get(((Bean)beanInfo.get(j)).getStr(serInfo.getPKey()));
					StringBuffer str = new StringBuffer();
					for (int k = 0; k < daCodeArr.length; k++)
					{
						str.append(daCodeArr[k]);
						str.append("-");
					}

					((Bean)beanInfo.get(j)).set("DA_CODE", str.toString().substring(0, str.toString().length() - 1));
				}
			}
			((Bean)beanInfo.get(j)).set("DA_NUM", Integer.valueOf(Integer.parseInt((String)dataMap.get(((Bean)beanInfo.get(j)).getStr(serInfo.getPKey())))));
			ServDao.update(paramBean.getStr("serv"), (Bean)beanInfo.get(j));
		}

		return outBean.setOk();
	}

	public OutBean clearDH(ParamBean paramBean)
	{
		String servID = paramBean.getServId();
		ServDefBean sdb = ServUtils.getServDef(servID);
		String tablePK = sdb.getPKey();
		List listBean = selectDataForWhere(paramBean.getStr("dataValue"), servID);
		for (int i = 0; i < listBean.size(); i++)
			ServDao.update(servID, (new Bean(((Bean)listBean.get(i)).getStr(tablePK))).set("DA_CODE", ""));

		return (new OutBean()).setOk();
	}

	public OutBean danganZH(ParamBean paramBean)
	{
		String prototypeBean[] = paramBean.getStr("DATA_VALUE").split(",");
		int inputBoxNum = paramBean.getInt("INPUT_BOX_NUM");
		for (int i = 0; i < prototypeBean.length; i++)
		{
			String splitprototypeBean[] = prototypeBean[i].split("--");
			ServDao.update(paramBean.getStr("serv"), (new Bean(splitprototypeBean[0])).set("BOX_NUM", Integer.valueOf(inputBoxNum)));
		}

		return (new OutBean()).setOk();
	}

	public OutBean selectMaxBoxNum(ParamBean paramBean)
	{
		OutBean outBean = new OutBean();
		UserBean userBean = Context.getUserBean();
		String odeptCode = userBean.getODeptCode();
		Bean boxNumBean = ServDao.find(paramBean.getStr("serv"), (new Bean()).set("_SELECT_", " MAX(BOX_NUM) BOX_NUM").set("_WHERE_", (new StringBuilder()).append(" and S_ODEPT = '").append(odeptCode).append("'").toString()));
		int maxNum = boxNumBean.getInt("BOX_NUM");
		if (maxNum == 0)
		{
			maxNum = 1;
			outBean.set("LAST_PAGE", Integer.valueOf(getTotalPage()));
		} else
		{
			Bean lastBean = ServDao.find(paramBean.getStr("serv"), (new Bean()).set("_SELECT_", " count(PAGE_COUNT) PAGE_COUNT").set("_WHERE_", (new StringBuilder()).append(" and BOX_NUM=").append(maxNum).append(" and S_ODEPT = '").append(odeptCode).append("'").toString()));
			outBean.set("LAST_PAGE", Integer.valueOf(getTotalPage() - lastBean.getInt("PAGE_COUNT")));
		}
		outBean.set("MAX_BOX_NUM", Integer.valueOf(maxNum));
		return outBean;
	}

	public int getTotalPage()
	{
		int page = 500;
		String totalPage = Context.getSyConf("DA_BOX_MAX_PAGE", "");
		if (!"".equals(totalPage))
			page = Integer.parseInt(totalPage);
		return page;
	}

	public int getLastPage(int boxNum, ParamBean paramBean)
	{
		UserBean userInfo = Context.getUserBean();
		String odeptCode = userInfo.getODeptCode();
		Bean dataBean = ServDao.find(paramBean.getStr("serv"), (new Bean()).set("_SELECT_", " count(PAGE_COUNT) PAGE_COUNT").set("_WHERE_", (new StringBuilder()).append(" and S_ODEPT ='").append(odeptCode).append("' AND BOX_NUM=").append(boxNum).toString()));
		int returnData = getTotalPage() - dataBean.getInt("PAGE_COUNT");
		return returnData;
	}

	public OutBean shengchengDH(ParamBean paramBean)
	{
		String servID = paramBean.getStr("serv");
		ServDefBean sdb = ServUtils.getServDef(servID);
		String tablePK = sdb.getPKey();
		List listBean = selectDataForWhere(paramBean.getStr("dataValue"), servID);
		for (int i = 0; i < listBean.size(); i++)
		{
			Bean daCodeValue = getCodeRuleVal((new Bean()).set("P_ID", ((Bean)listBean.get(i)).getStr("P_ID")));
			Bean dataBean = ServDao.find(servID, new Bean(((Bean)listBean.get(i)).getStr(tablePK)));
			Bean fullBean = BeanUtils.mergeBean(daCodeValue, dataBean);
			fullBean.set("serv", servID);
			String daCode = selectDicVal(fullBean);
			daCode = (new StringBuilder()).append(daCode).append(dataBean.getStr("DA_NUM")).toString();
			ServDao.update(servID, (new Bean(((Bean)listBean.get(i)).getStr(tablePK))).set("DA_CODE", daCode));
		}

		return (new OutBean()).setOk();
	}

	public OutBean gdToVolume(ParamBean paramBean)
	{
		String servID = paramBean.getServId();
		ServDefBean sdb = ServUtils.getServDef(servID);
		String tablePK = sdb.getPKey();
		List listBean = selectDataForWhere(paramBean.getStr("DANG_AN_PK"), servID);
		for (int i = 0; i < listBean.size(); i++)
			ServDao.update(servID, (new Bean(((Bean)listBean.get(i)).getStr(tablePK))).set("V_ID", paramBean.getStr("V_ID")).set("P_ID", paramBean.getStr("P_ID")).set("V_NUM", paramBean.getStr("V_DA_CODE")).set("DA_CODE", (new StringBuilder()).append(paramBean.getStr("V_DA_CODE")).append(getNumMark(paramBean)).append(((Bean)listBean.get(i)).getStr("DA_NUM")).toString()));

		return (new OutBean()).setOk();
	}

	public String getNumMark(ParamBean paramBean)
	{
		String pID = paramBean.getStr("P_ID");
		Bean resultBean = ServDao.find("DA_DIR", new Bean(pID));
		return resultBean.getStr("NUM_MARK");
	}

	public OutBean getItemName(ParamBean paramBean)
	{
		OutBean outBean = new OutBean();
		ServDefBean servDef = ServUtils.getServDef(paramBean.getStr("SERV_NAME"));
		LinkedHashMap itemAll = servDef.getAllItems();
		List resultBean = new ArrayList();
		Iterator iterator = itemAll.keySet().iterator();
		int i = 0;
		do
		{
			if (!iterator.hasNext())
				break;
			String key = (String)iterator.next();
			Bean itemBean = new Bean();
			itemBean = (Bean)itemAll.get(key);
			if (itemBean.getInt("ITEM_HIDDEN") == 2)
			{
				Bean nameCodeBean = new Bean();
				nameCodeBean.set("ITEM_CODE", key);
				nameCodeBean.set("ITEM_NAME", itemBean.getStr("ITEM_NAME"));
				resultBean.add(nameCodeBean);
				i++;
			}
		} while (true);
		outBean.setCount(i);
		outBean.setData(resultBean);
		return outBean;
	}

	public OutBean batchUpdateData(ParamBean paramBean)
	{
		List dataBean = selectDataForWhere(paramBean.getStr("DATA_IDS"), paramBean.getServId());
		if (paramBean.getStr("MODIFY_BEFORE").isEmpty())
		{
			for (int i = 0; i < dataBean.size(); i++)
				ServDao.update(paramBean.getServId(), (new Bean(((Bean)dataBean.get(i)).getId())).set(paramBean.getStr("MODIFY_FIELD"), paramBean.getStr("MODIFY_AFTER")));

		} else
		{
			ServDefBean servDef = ServUtils.getServDef(paramBean.getServId());
			String servPK = servDef.getPKey();
			Bean setBean = new Bean();
			setBean.set(paramBean.getStr("MODIFY_FIELD"), paramBean.getStr("MODIFY_AFTER"));
			for (int i = 0; i < dataBean.size(); i++)
				ServDao.updates(paramBean.getServId(), setBean, (new Bean()).set(servPK, ((Bean)dataBean.get(i)).getStr(servPK)).set(paramBean.getStr("MODIFY_FIELD"), paramBean.getStr("MODIFY_BEFORE")));

		}
		return (new OutBean()).setOk();
	}

	public OutBean relationDA(ParamBean paramBean)
	{
		String servID = paramBean.getServId();
		ServDefBean sdb = ServUtils.getServDef(servID);
		String tablePK = sdb.getPKey();
		List listBean = selectDataForWhere(paramBean.getStr("DATA_INFO"), servID);
		for (int i = 0; i < listBean.size(); i++)
		{
			Bean bean = new Bean();
			Bean dirBean = ServDao.find("DA_DIR", ((Bean)listBean.get(i)).getStr("P_ID"));
			Bean categoryBean = ServDao.find("DA_CATEGORY", (new SqlBean()).and("CAT_ID", dirBean.getStr("CAT_ID")));
			bean.set("DA_ID", ((Bean)listBean.get(i)).getStr(tablePK)).set("DA_TITLE", ((Bean)listBean.get(i)).getStr("TITLE")).set("DA_TYPE", categoryBean.getStr("CAT_NAME")).set("DA_SERV", paramBean.getStr("SERV_ID")).set("YJ_ID", paramBean.getStr("YJ_ID")).set("OPER_TIME", DateUtils.getDatetime());
			ServDao.create("DA_YIJIAO_ITEM", bean);
		}

		return (new OutBean()).setOk();
	}

	public OutBean yjToDAKu(ParamBean paramBean)
	{
		String servID = paramBean.getServId();
		ServDefBean sdb = ServUtils.getServDef(servID);
		String tablePK = sdb.getPKey();
		List listBean = selectDataForWhere(paramBean.getStr("DATAS_PK"), servID);
		for (int i = 0; i < listBean.size(); i++)
		{
			ServDao.update(paramBean.getServId(), (new SqlBean()).setId(((Bean)listBean.get(i)).getStr(tablePK)).set("DA_FILE_TYPE", Integer.valueOf(2)));
			if (servID.indexOf("DA_VOLUME") == 0)
			{
				String servidSub = servID.substring(10);
				ServDao.updates(servidSub, (new SqlBean()).set("DA_FILE_TYPE", Integer.valueOf(2)), (new SqlBean()).set("V_ID", ((Bean)listBean.get(i)).getStr(tablePK)));
			}
		}

		return (new OutBean()).setOk();
	}

	public List selectDataForWhere(String whereStr, String servID)
	{
		Bean sqlBean = new Bean();
		StringBuffer buf = new StringBuffer();
		ServDefBean sdb = ServUtils.getServDef(servID);
		String tablePK = sdb.getPKey();
		if (whereStr.indexOf("whereStr:") == 0)
		{
			String where = whereStr.replace("whereStr:", "");
			String whereArr[] = where.split("\\^");
			if (StringUtils.isNotEmpty(whereArr[0]))
			{
				String treewhereServwhere = DaUtils.getDaTreeWhere(whereArr[0], sdb);
				buf.append((new StringBuilder()).append(" ").append(treewhereServwhere).append(" ").toString());
			}
			buf.append(whereArr[1]);
		}
		if (whereStr.indexOf("idStr:") == 0)
		{
			String where = whereStr.replace("idStr:", "");
			buf.append((new StringBuilder()).append(" and ").append(tablePK).append(" in ('").toString());
			where = where.replace(",", "','");
			buf.append(where).append("') ");
		}
		sqlBean.set("_WHERE_", buf.toString());
		List resultBean = ServDao.finds(servID, sqlBean);
		return resultBean;
	}

	public OutBean getZwInfo(ParamBean paramBean)
	{
		Bean queryBean = new Bean();
		String servId = paramBean.getStr("DA_SERV");
		String dataId = paramBean.getStr("DATA_ID");
		String servIdSplit[] = servId.split("_");
		queryBean.set("SERV_ID", (new StringBuilder()).append(servIdSplit[0]).append("_").append(servIdSplit[1]).append("_BASE").toString());
		queryBean.set("DATA_ID", dataId);
		queryBean.set("FILE_CAT", "ZHENGWEN");
		queryBean.set("_ORDER_", "FILE_SORT");
		Bean fileBean = null;
		List fileBeans = ServDao.finds("SY_COMM_FILE", queryBean);
		if (fileBeans.size() > 0)
			fileBean = (Bean)fileBeans.get(0);
		Iterator i$ = fileBeans.iterator();
		do
		{
			if (!i$.hasNext())
				break;
			Bean bean = (Bean)i$.next();
			if (bean.getStr("FILE_NAME").endsWith(".gw"))
			{
				fileBean = bean;
				break;
			}
			if (!bean.getStr("FILE_NAME").endsWith(".pdf"))
				continue;
			fileBean = bean;
			break;
		} while (true);
		if (null == fileBean)
			fileBean = new Bean();
		return new OutBean(fileBean);
	}

	public OutBean getAttachInfo(ParamBean paramBean)
	{
		Bean queryBean = new Bean();
		String servId = paramBean.getStr("DA_SERV");
		String dataId = paramBean.getStr("DATA_ID");
		String servIdSplit[] = servId.split("_");
		queryBean.set("SERV_ID", (new StringBuilder()).append(servIdSplit[0]).append("_").append(servIdSplit[1]).append("_BASE").toString());
		queryBean.set("DATA_ID", dataId);
		queryBean.set("FILE_CAT", "FUJIAN");
		queryBean.set("_ORDER_", "FILE_SORT");
		List fileBeans = ServDao.finds("SY_COMM_FILE", queryBean);
		return (new OutBean()).setData(fileBeans);
	}

	protected void afterByid(ParamBean paramBean, OutBean outBean)
	{
		super.afterByid(paramBean, outBean);
		if (outBean.containsKey("BOX_NUM") && outBean.getInt("BOX_NUM") == 0)
			outBean.set("BOX_NUM", "");
		if (outBean.containsKey("DA_NUM") && outBean.getInt("DA_NUM") == 0)
			outBean.set("DA_NUM", "");
		if (outBean.containsKey("SUB_NUM") && outBean.getInt("SUB_NUM") == 0)
			outBean.set("SUB_NUM", "");
		if (outBean.containsKey("PAGE_COUNT") && outBean.getInt("PAGE_COUNT") == 0)
			outBean.set("PAGE_COUNT", "");
	}
}
