package com.rh.core.comm.calendar;

import java.util.ArrayList;
import java.util.List;

import com.rh.core.base.Bean;
import com.rh.core.base.Context;
import com.rh.core.org.UserBean;
import com.rh.core.org.mgr.UserMgr;
import com.rh.core.serv.CommonServ;
import com.rh.core.serv.ParamBean;
import com.rh.core.serv.ServDao;
import com.rh.core.serv.ServMgr;
import com.rh.core.util.Constant;

/**
 * 日程共享设置类
 * @author zzx
 * 
 */
public class CalendarShareServ extends CommonServ {

    /**服务定义**/
    public static final String SY_COMM_CAL_SHARE = "SY_COMM_CAL_SHARE"; 
   
    /**
     * 列表页中保存用户设置的类型
     * @param paramBean 参数
     * @return Bean
     */
    public Bean calendarSet(Bean paramBean) {

        String[] calendarID = paramBean.getStr("PK_CODE").split(",");
        String[] calendarCode = paramBean.getStr("ITEM_CODE").split(",");
        String[] calendarName = paramBean.getStr("ITEM_NAME").split(",");

        for (int i = 0; i < calendarID.length; i++) {
            Bean dataBean = new Bean();
            dataBean.set("CAL_ID", calendarID[i])
                    .set("CAL_TYPE", calendarCode[i]).set("CAL_NAME", calendarName[i]);
            ServDao.create(SY_COMM_CAL_SHARE, dataBean);
        }

        return new Bean().set(Constant.RTN_MSG, Constant.RTN_MSG_OK);
    }

    /**
     * 卡片页中点击“保存”按钮后，保存值。
     * @param paramBean 前台值
     * @return Bean
     */
    public Bean shareObjectSet(Bean paramBean) {

        // 获取共享类型主键
        String[] shareID = paramBean.getStr("SHARE_ID").split(",");
        String[] fullyOpen = paramBean.getStr("FULLY_OPEN").split(",");

        // 如果是完全公开，则向数据库插入数据
        if ("1".equals(fullyOpen[0])) {
            Bean dataBean = new Bean();
            dataBean.set("SHARE_ID", shareID[0]).set("OBJ_CODE", "RPUB")
                    .set("OBJ_NAME", "*公共角色").set("SHARE_TYPE", "all");
			Bean deleteBean = new Bean();
			deleteBean.set(Constant.PARAM_WHERE, " and SHARE_ID = '"
					+ shareID[0] + "'");
			//如果完全公开，则删除之前设置过的信息。
			ServDao.deletes(ServMgr.SY_COMM_CAL_OBJ, deleteBean);
			ServDao.create(ServMgr.SY_COMM_CAL_OBJ, dataBean);
        } else {
            // 否则获取用户、角色、部门，循环将值存入数据库
            // 获取共享用户ID
            String[] userID = paramBean.getStr("shareTypeUserId").split(",");
            // 获取共享用户名称
            String[] userName = paramBean.getStr("shareTypeUser").split(",");

            // 获取部门ID
            String[] deptID = paramBean.getStr("shareTypeDeptId").split(",");
            // 获取部门名称
            String[] deptName = paramBean.getStr("shareTypeDept").split(",");

            //如果设置共享为用户和部门，则删除完全公开的设置。
			Bean whereBean = new Bean();
			whereBean.set(Constant.PARAM_WHERE, " and SHARE_ID = '"
					+ shareID[0] + "' and SHARE_TYPE = 'all'");
			List<Bean> resultBeans = ServDao.finds(ServMgr.SY_COMM_CAL_OBJ,
					whereBean);
			if (resultBeans.size() > 0) {
				ServDao.deletes(ServMgr.SY_COMM_CAL_OBJ, whereBean);
			}
            
            if (userID.length > 0 && userID[0] != "") {
                for (int i = 0; i < userID.length; i++) {
                    Bean dataBean = new Bean();
                    dataBean.set("SHARE_ID", shareID[0]).set("OBJ_CODE", userID[i])
                            .set("OBJ_NAME", userName[i]).
                            set("SHARE_TYPE", "user");
                    ServDao.create(ServMgr.SY_COMM_CAL_OBJ, dataBean);
                }
            }

            if (deptID.length > 0 && deptID[0] != "") {
                for (int i = 0; i < deptID.length; i++) {
                    Bean dataBean = new Bean();
                    dataBean.set("SHARE_ID", shareID[0]).set("OBJ_CODE", deptID[i])
                            .set("OBJ_NAME", deptName[i]).
                            set("SHARE_TYPE", "dept");
                    ServDao.create(ServMgr.SY_COMM_CAL_OBJ, dataBean);
                }
            }
        }

        return new Bean().set(Constant.RTN_MSG, Constant.RTN_MSG_OK);
    }

    /**
     * 在列表页点击“共享设置”按钮后，弹出卡片页，查询出该类型日程设置过的历史记录
     * @param paramBean 传入参数
     * @return Bean
     */
    public Bean selectObjSet(Bean paramBean) {

        String[] shareID = paramBean.getStr("SHARE_ID").split(",");
        List<Bean> arrBean = new ArrayList<Bean>();
        String shID = "";
        for (int i = 0; i < shareID.length; i++) {
            if (i == 0) {
                shID = "'" + shareID[i] + "',";
            } else {
                shID += "'" + shareID[i] + "',";
            }
        }
        shID = shID.substring(0, shID.length() - 1);
        String whereStr = " and SHARE_ID in (" + shID + ")";
        arrBean = ServDao.finds(ServMgr.SY_COMM_CAL_OBJ, whereStr);

        return new Bean().set(Constant.RTN_DATA, arrBean);
    }

    /**
     * 查询共享给指定用户的
     * @param paramBean 当前用户信息
     * @return 共享给我的用户信息
     */
    public Bean loadShareUsers(Bean paramBean) {
        String currUserCode = Context.getUserBean().getCode();
        // 获取指定用户的角色code，部门code，用户code
        String actCodes = paramBean.getStr("actCodes");
        // String actCodes = paramBean.getStr("");
        // 如果用户的code信息为空则根据用户code去查询出角色code，部门code
        if (actCodes == "") {
            String userCode = paramBean.getStr("userCode");
            if (!"".equals(userCode)) {
                // 根据用户code获取用户信息
                UserBean userBean = UserMgr.getUser(userCode);
                // 获取用户的角色code
                String[] userRole = userBean.getRoleCodes();
                // 获取部门的code
                String userDept = userBean.getTDeptCode();
                // 将用户code、部门code拼起来
                actCodes = "'" + userCode + "," + userDept + ",";
                // 将角色的code拼起来
                for (int i = 0; i < userRole.length; i++) {
                    actCodes += userRole[i] + ",";
                }
                actCodes = actCodes.substring(0, actCodes.length() - 1);
                actCodes = actCodes + "'";
            } else {
                return new Bean();
            }
        }
        Bean dataBean = selectData(actCodes, "multi", "");
        // 存储Bean的list
        List<Bean> listBean = new ArrayList<Bean>();
        // 获取dataBean中的值
        List<Bean> userList = dataBean.getList("_DATA_");
        // 循环将Bean中的s_user取出来。
        for (int i = 0; i < userList.size(); i++) {
            // 得到用户code
            String userCode = userList.get(i).getStr("S_USER");
            // 如果共享用户中包含自己则忽略
            if (userCode.equals(currUserCode)) {
                continue;
            }
            Bean resBean = new Bean();
            // 根据userCode得到用户信息
            UserBean userBean = UserMgr.getUser(userCode);
            resBean.set("userCode", userCode).set("userName", userBean.getName())
                    .set("deptCode", userBean.getTDeptCode()).set("deptName", userBean.getTDeptName());
            listBean.add(resBean);
        }
        return new Bean().set(Constant.RTN_DATA, listBean);
    }

    /**
     * 查询指定用户共享给我了哪些类型
     * @param paramBean 我的code，共享给我的用户的code
     * @return Bean 共享给我了哪些类型
     */
    public Bean typeForUser(Bean paramBean) {
        //查询A用户共享给B用户了那些类型的日程。actCodes指B用户的用户code，角色code，部门code。。。shareUserCode指A用户的code
        Bean dataBean = selectData(paramBean.getStr("actCodes"), "", paramBean.getStr("shareUserCode"));
        List<Bean> typeList = dataBean.getList("_DATA_");
        //将查询的数据处理后放在listBean中
        List<Bean> listBean = new ArrayList<Bean>();
        for (int i = 0; i < typeList.size(); i++) {
            Bean typeBean = new Bean();
            typeBean.set("typeCode", typeList.get(i).getStr("CAL_TYPE"))
                    .set("typeName", typeList.get(i).getStr("CAL_NAME"));
            listBean.add(typeBean);
        }
        return new Bean().set(Constant.RTN_DATA, listBean);
    }

    /**
     * 根据提供的actCodes（用户的角色code、用户code、部门code）查询出共享给actCodes的用户
     * @param paramBean paramBean
     * @return 包含用户信息的Bean
     */
    public Bean selectUserCode(Bean paramBean) {
        // 查询出用户code
        Bean dataBean = selectData(paramBean.getStr("actCodes"), "multi", "");
        List<Bean> userList = dataBean.getList("_DATA_");
        String userCodeStr = "";
        String currUserCode = Context.getUserBean().getCode();
        for (int i = 0; i < userList.size(); i++) {
            String userCode = userList.get(i).getStr("S_USER");
            //如果共享用户中包含自己则忽略
            if (userCode.equals(currUserCode)) {
                continue;
            }
            if (i == 0) {
                userCodeStr += userCode;
            } else {
                userCodeStr += Constant.SEPARATOR + userCode;
            }
        }

        return new Bean().set(Constant.RTN_DATA, userCodeStr);
    }

    /**
     * 
     * @param actCodes 用户code信息
     * @param qyeryFlag 区分调用那个函数
     * @param shareUserCode 共享用户CODE
     * @return Bean
     */
    private Bean selectData(String actCodes, String qyeryFlag, String shareUserCode) {
        // 设置查询条件
        ParamBean selectBean = new ParamBean(SY_COMM_CAL_SHARE);

        if ("multi".equals(qyeryFlag)) {
            // 添加要查询的字段
            selectBean.set(Constant.PARAM_SELECT, "distinct S_USER");
            // 添加查询条件
            selectBean.set(Constant.PARAM_WHERE,
                    " and SHARE_ID in (select  SHARE_ID FROM SY_COMM_CAL_OBJ WHERE OBJ_CODE in ('RPUB',"
                            + actCodes + ")) order by S_USER desc");
        } else {
            // 添加要查询的字段
            selectBean.set(Constant.PARAM_SELECT, " CAL_TYPE,CAL_NAME");
            selectBean.set(Constant.PARAM_WHERE, " and S_USER='" + shareUserCode
                    + "' and SHARE_ID in ( select  SHARE_ID FROM SY_COMM_CAL_OBJ WHERE OBJ_CODE "
                    + "in ('RPUB'," + actCodes + "))");
        }
        // 根据条件查询数据库，返回一个Bean
        Bean dataBean = finds(selectBean);
        return dataBean;
    }

}
