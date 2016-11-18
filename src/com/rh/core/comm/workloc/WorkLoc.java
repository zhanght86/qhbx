package com.rh.core.comm.workloc;

import java.util.List;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import com.rh.core.base.Bean;
import com.rh.core.org.mgr.UserMgr;
import com.rh.core.serv.CommonServ;
import com.rh.core.serv.OutBean;
import com.rh.core.serv.ParamBean;
import com.rh.core.serv.ServDao;
import com.rh.core.serv.dict.DictMgr;
import com.rh.core.util.Constant;
import com.rh.core.util.DateUtils;

/**
 * 工位
 * @author wanghg
 */
public class WorkLoc extends CommonServ {
    /**
     * 工位历史服务ID
     */
    public static final String HIS_SERV_ID = "SY_COMM_WORKLOC_HIS";
    /**
     * 服务ID
     */
    public static final String SERV_ID = "SY_COMM_WORKLOC";
    /**
     * 跳转到工位展示页面
     * @param paramBean 参数
     * @return bean
     */
    public OutBean show(ParamBean paramBean) {
        OutBean outBean = new OutBean();
        outBean.setToDispatcher("/sy/comm/workloc/workloc.jsp");
        outBean.set("PARAM_BEAN", paramBean);
        return outBean; 
    }
    /**
     * 加载组织机构用户XML数据
     * @param paramBean 参数
     * @return bean
     */
    public OutBean loadDeptXml(ParamBean paramBean) {
        List<Bean> list = DictMgr.getTreeList("SY_ORG_DEPT_USER");
        Document doc = DocumentHelper.createDocument();
        Element root = doc.addElement("departFlexTree");
        addToEle(root, list, "rootDepart");
        WorkLocUtils.write(doc);
        return new OutBean();
    }
    /**
     * 添加到元素
     * @param ele 元素
     * @param list 列表
     * @param ename 元素名称
     */
    @SuppressWarnings("unchecked")
    private void addToEle(Element ele, List<Bean> list, String ename) {
        Element cele;
        for (Bean bean : list) {
            if (bean.contains("DEPT_TYPE")) {
                cele = ele.addElement(ename);
                cele.addAttribute("ID", bean.getStr("ID"));
                cele.addAttribute("label", bean.getStr("NAME"));
            } else {
                cele = ele.addElement("user");
                cele.addAttribute("ID", bean.getStr("ID"));
                cele.addAttribute("label", bean.getStr("NAME"));
            }
            if (bean.contains("CHILD")) {
                addToEle(cele, (List<Bean>) bean.get("CHILD"), "depart");
            }
        }
    }
    /**
     * 加载用户信息
     * @param paramBean 参数
     * @return bean
     */
    public OutBean userInfo(ParamBean paramBean) {
        Bean user = UserMgr.getUser(paramBean.getStr("userID"));
        if (user != null) {
            Document doc = DocumentHelper.createDocument();
            Element eleUser = doc.addElement("user");
            eleUser.addElement("userID").setText(user.getId());
            eleUser.addElement("userName").setText(user.getStr("USER_NAME"));
            eleUser.addElement("userImg").setText(user.getStr("USER_IMG"));
            eleUser.addElement("sex").setText(user.getStr("USER_SEX"));
            eleUser.addElement("topDeptName").setText(
                    DictMgr.getItem("SY_ORG_DEPT", user.getStr("TDEPT_CODE")).getStr("NAME"));
            eleUser.addElement("deptName").setText(user.getStr("DEPT_NAME"));
            eleUser.addElement("email").setText(user.getStr("USER_EMAIL"));
            eleUser.addElement("mobile").setText(user.getStr("USER_MOBILE"));
            eleUser.addElement("telephone").setText(user.getStr("USER_OFFICE_PHONE"));
            eleUser.addElement("cmpyId").setText(user.getStr("CMPY_CODE"));
            WorkLocUtils.write(doc);
        }
        return new OutBean();
    }
    /**
     * 用户历史
     * @param paramBean 参数
     * @return bean
     */
    public OutBean userHis(ParamBean paramBean) {
        List<Bean> list = ServDao.finds(HIS_SERV_ID, " and USER_CODE='" + paramBean.getStr("userID") 
                + "' and USER_NAME='" + paramBean.getStr("userName") + "' order by BEGIN_TIME");
        Document doc = DocumentHelper.createDocument();
        Element root = doc.addElement("userHistory");
        Element loc;
        for (Bean bean : list) {
            loc = root.addElement("loc");
            loc.addAttribute("buildingName", bean.getStr("FL_NAME"));
            loc.addAttribute("code", bean.getStr("WL_CODE"));
            loc.addAttribute("beginTime", bean.getStr("BEGIN_TIME"));
            loc.addAttribute("endTime", bean.getStr("END_TIME"));
        }
        WorkLocUtils.write(doc);
        return new OutBean();
    }
    /**
     * 工位历史
     * @param paramBean 参数
     * @return bean
     */
    public OutBean locHis(ParamBean paramBean) {
        List<Bean> list = ServDao.finds(HIS_SERV_ID, 
                " and WL_ID='" + paramBean.getStr("uuid") + "' order by BEGIN_TIME");
        Document doc = DocumentHelper.createDocument();
        Element root = doc.addElement("locHistory");
        Element loc;
        for (Bean bean : list) {
            loc = root.addElement("user");
            loc.addAttribute("userName", bean.getStr("USER_NAME"));
            loc.addAttribute("deptName", bean.getStr("DEPT_NAME"));
            loc.addAttribute("topDeptName", bean.getStr("TDEPT_NAME"));
            loc.addAttribute("beginTime", bean.getStr("BEGIN_TIME"));
            loc.addAttribute("endTime", bean.getStr("END_TIME"));
        }
        WorkLocUtils.write(doc);
        return new OutBean();
    }
    /**
     * 添加工位
     * @param paramBean 参数
     * @return 结果
     */
    public OutBean addWl(ParamBean paramBean) {
        OutBean loc = new OutBean(toLocBean(paramBean));
        ServDao.create(SERV_ID, loc);
        if (!loc.getStr("USER_CODE").equals("0") || loc.getStr("USER_NAME").length() > 0) {
            //记录历史
            Bean hisBean = loc.copyOf();
            hisBean.remove(Constant.KEY_ID);
            hisBean.set("BEGIN_TIME", DateUtils.getDate());
            ServDao.create(HIS_SERV_ID, hisBean);
        }
        return loc;
    }
    /**
     * 转换为工位bean
     * @param paramBean 参数
     * @return 结果
     */
    private Bean toLocBean(ParamBean paramBean) {
        Bean loc = new Bean();
        loc.set("WL_ID", paramBean.get("uuid"));
        loc.set("FL_ID", paramBean.get("floorUuid"));
        loc.set("WL_CODE", paramBean.get("code"));
        loc.set("USER_CODE", paramBean.get("userID"));
        loc.set("USER_NAME", paramBean.get("userName"));
        loc.set("USER_SEX", paramBean.get("sex"));
        loc.set("USER_PHONE", paramBean.get("telephone"));
        loc.set("USER_MOBILE", paramBean.get("mobile"));
        loc.set("USER_EMAIL", paramBean.get("email"));
        loc.set("DEPT_NAME", paramBean.get("deptName"));
        loc.set("TDEPT_NAME", paramBean.get("topDeptName"));
        loc.set("ODEPT_NAME", paramBean.get("deptName"));
        loc.set("WL_X", paramBean.get("x"));
        loc.set("WL_Y", paramBean.get("y"));
        loc.set("WL_LOCK", paramBean.get("lock", "").equals("true") ? Constant.YES : Constant.NO);
        loc.set("WL_MEMO", paramBean.get("memo"));
        loc.set(Constant.KEY_ID, paramBean.get("uuid"));
        return loc;
    }
    /**
     * 编辑工位
     * @param paramBean 参数
     * @return 结果
     */
    public OutBean editWl(ParamBean paramBean) {
        OutBean loc = new OutBean(toLocBean(paramBean));
        if (loc.getStr("USER_CODE").length() > 0) {
            //记录历史
            Bean oldLoc = ServDao.find(SERV_ID, loc.getId());
            if (!oldLoc.getStr("USER_CODE").equals(loc.getStr("USER_CODE")) 
                    || !oldLoc.getStr("USER_NAME").equals(loc.getStr("USER_NAME"))) {
                List<Bean> hisList = ServDao.finds(HIS_SERV_ID, 
                        " and WL_ID='" + loc.getId() + "' and END_TIME is null");
                if (hisList.size() > 0) {
                    Bean hisBean = hisList.get(0);
                    hisBean.set("END_TIME", DateUtils.getDate());
                    ServDao.update(HIS_SERV_ID, hisBean);
                }
                Bean hisBean = loc.copyOf();
                hisBean.remove(Constant.KEY_ID);
                hisBean.set("BEGIN_TIME", DateUtils.getDate());
                ServDao.create(HIS_SERV_ID, hisBean);
            }
        }
        ServDao.update(SERV_ID, loc);
        return loc;
    }
    /**
     * 删除工位
     * @param paramBean 参数
     * @return 结果
     */
    public OutBean delWl(ParamBean paramBean) {
        Bean bean = new Bean();
        bean.setId(paramBean.getStr("uuid"));
        ServDao.delete(SERV_ID, bean);
        //删除历史
        Bean where = new Bean();
        where.set(Constant.PARAM_WHERE, " and WL_ID='" + paramBean.getStr("uuid") + "'");
        ServDao.deletes(HIS_SERV_ID, where);
        return new OutBean().setOk();
    }
    /**
     * 移动工位
     * @param paramBean 参数
     * @return 结果
     */
    public OutBean moveWl(ParamBean paramBean) {
        Bean loc = ServDao.find(SERV_ID, paramBean.getStr("uuid"));
        loc.set("WL_X", paramBean.getStr("x"));
        loc.set("WL_Y", paramBean.getStr("y"));
        ServDao.save(SERV_ID, loc);
        return new OutBean().setOk();
    }
}
