package com.rh.core.comm.workloc;


import java.util.List;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import com.rh.core.base.Bean;
import com.rh.core.comm.FileMgr;
import com.rh.core.org.UserBean;
import com.rh.core.org.mgr.UserMgr;
import com.rh.core.serv.CommonServ;
import com.rh.core.serv.OutBean;
import com.rh.core.serv.ParamBean;
import com.rh.core.serv.ServDao;
import com.rh.core.util.Constant;

/**
 * 工位楼层
 * @author wanghg
 */
public class WorkLocFloor extends CommonServ {
    /**
     * 服务ID
     */
    public static final String SERV_ID = "SY_COMM_WORKLOC_FLOOR";
    /**
     * 加载XML数据
     * @param paramBean 参数
     * @return 结果
     */
    public OutBean loadXml(ParamBean paramBean) {
        Document doc = DocumentHelper.createDocument();
        Element root = doc.addElement("floors");
        List<Bean> floors = ServDao.finds(SERV_ID, " order by FL_SORT");
        Element eleFloor, eleLoc;
        UserBean user;
        String userCode;
        for (Bean floor : floors) {
            eleFloor = root.addElement("floor");
            eleFloor.addAttribute("uuid", floor.getId());
            eleFloor.addAttribute("buildingName", floor.getStr("FL_NAME"));
            eleFloor.addAttribute("lock", String.valueOf(floor.getStr("FL_LOCK").equals(Constant.YES)));
            eleFloor.addAttribute("memo", floor.getStr("FL_MEMO"));
            List<Bean> locs = ServDao.finds(WorkLoc.SERV_ID, " and FL_ID='" + floor.getId() + "'");
            for (Bean loc : locs) {
                eleLoc = eleFloor.addElement("loc");
                eleLoc.addAttribute("uuid", loc.getStr("WL_ID"));
                eleLoc.addAttribute("x", loc.getStr("WL_X"));
                eleLoc.addAttribute("y", loc.getStr("WL_Y"));
                eleLoc.addAttribute("code", loc.getStr("WL_CODE"));
                userCode = loc.getStr("USER_CODE");
                eleLoc.addAttribute("userID", userCode);
                eleLoc.addAttribute("userName", loc.getStr("USER_NAME"));
                if (!userCode.equals("0")) {
                    user = UserMgr.getUser(userCode);
                    eleLoc.addAttribute("userImg", user.getStr("USER_IMG"));
                } else {
                    eleLoc.addAttribute("userImg", "");
                }
                eleLoc.addAttribute("sex", loc.getStr("USER_SEX"));
                eleLoc.addAttribute("topDeptName", loc.getStr("ODEPT_NAME"));
                eleLoc.addAttribute("deptName", loc.getStr("DEPT_NAME"));
                eleLoc.addAttribute("email", loc.getStr("USER_EMAIL"));
                eleLoc.addAttribute("mobile", loc.getStr("USER_MOBILE"));
                eleLoc.addAttribute("telephone", loc.getStr("USER_PHONE"));
                eleLoc.addAttribute("memo", loc.getStr("WL_MEMO"));
                eleLoc.addAttribute("lock", String.valueOf(loc.getStr("WL_LOCK").equals(Constant.YES)));
            }
        }
        WorkLocUtils.write(doc);
        return new OutBean();
    }
    /**
     * 删除楼层
     * @param paramBean 参数
     * @return 结果
     */
    public OutBean delFloor(ParamBean paramBean) {
        Bean bean = new Bean();
        bean.setId(paramBean.getStr("uuid"));
        ServDao.delete(SERV_ID, bean);
        //删除图片
        FileMgr.deleteFile(paramBean.getStr("uuid"));
        //删除工位
        Bean where = new Bean();
        where.set(Constant.PARAM_WHERE, " and WL_ID='" + paramBean.getStr("uuid") + "'");
        ServDao.deletes(WorkLoc.SERV_ID, where);
        //删除工位历史
        ServDao.deletes(WorkLoc.HIS_SERV_ID, where);
        return new OutBean().setOk();
    }
    /**
     * 添加楼层
     * @param paramBean 参数
     * @return 结果
     */
    public OutBean addFloor(ParamBean paramBean) {
        return new OutBean(ServDao.create(SERV_ID, toFloorBean(paramBean)));
    }
    /**
     * 转换为floorBean
     * @param paramBean 参数
     * @return 结果
     */
    private Bean toFloorBean(ParamBean paramBean) {
        Bean bean = new Bean();
        bean.set("FL_ID", paramBean.get("uuid"));
        bean.set("FL_NAME", paramBean.get("buildingName"));
        bean.set("FL_MEMO", paramBean.get("memo"));
        bean.set("FL_LOCK", paramBean.get("lock", "").equals("true") ? Constant.YES : Constant.NO);
        bean.set(Constant.KEY_ID, paramBean.get("uuid"));
        return bean;
    }
    /**
     * 编辑楼层
     * @param paramBean 参数
     * @return 结果
     */
    public OutBean editFloor(ParamBean paramBean) {
        return new OutBean(ServDao.update(SERV_ID, toFloorBean(paramBean)));
    }
    /**
     * 添加楼层
     * @param paramBean 参数
     * @return 结果
     */
    public OutBean orderFloor(ParamBean paramBean) {
        String[] ids = paramBean.getStr("uuids").split(",");
        Bean floor;
        for (int i = 0; i < ids.length; i++) {
            floor = ServDao.find(SERV_ID, ids[i]);
            floor.set("FL_SORT", String.valueOf(i + 1));
            ServDao.save(SERV_ID, floor);
        }
        return new OutBean();
    }
}
