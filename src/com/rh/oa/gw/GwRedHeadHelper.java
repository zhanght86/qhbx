package com.rh.oa.gw;

import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.rh.core.base.Bean;
import com.rh.core.org.DeptBean;
import com.rh.core.org.mgr.OrgMgr;
import com.rh.core.serv.ServDao;
import com.rh.core.serv.dict.DictMgr;
import com.rh.core.util.Constant;
import com.rh.core.util.DateUtils;
import com.rh.oa.gw.util.GwConstant;
import com.rh.oa.gw.util.GwFileHelper;

/**
 * 公文套红头帮助类
 * @author yangjy
 * 
 */
public class GwRedHeadHelper {

    private Bean gwBean = null;
    private GwFileHelper fileHelper = null;

    /**
     * 
     * @param servId 服务名称
     * @param gwId 公文ID
     */
    public GwRedHeadHelper(String servId, String gwId) {
        gwBean = ServDao.find(GwConstant.OA_GW_TYPE_FW, gwId);
        fileHelper = new GwFileHelper(gwId, servId);
    }

    /**
     * 
     * @return 取得文件所属的机构名称
     */
    public String getOdeptName() {
        if (gwBean.isNotEmpty("S_ODEPT")) {
            DeptBean odept = OrgMgr.getDept(gwBean.getStr("S_ODEPT"));
            return odept.getName();
        }

        return "";
    }
    
    /**
     * 
     * @param key 参数名
     * @return 参数值
     */
    public String getValue(String key) {
        return gwBean.get(key, "");
    }

    /**
     * 
     * @return 签发用户姓名
     */
    public String getSignUserName() {
        return gwBean.getStr("GW_SIGN_UNAME");
    }
    
    /**
     * @return 格式化抄送数据的格式
     */
    public String getCopyTo() {
        String copyTo = gwBean.getStr("GW_COPY_TO").trim();
        String copyToExt = gwBean.getStr("GW_COPY_TO_EXT").trim();

        if (copyTo.length() > 0 && copyToExt.length() > 0) {
            copyTo += "," + copyToExt;
        } else if (copyToExt.length() > 0) {
            copyTo = copyToExt;
        }
        if (copyTo.length() > 0) {
            copyTo = copyTo.replaceAll(",", "，") + "。";
        }
        gwBean.set("GW_COPY_TO", copyTo);
        return copyTo;
    }
    
    /**
     * @return 格式化主送数据的格式
     */
    public String getMainTo() {
        String mainTo = gwBean.getStr("GW_MAIN_TO").trim();
        String mainToExt = gwBean.getStr("GW_MAIN_TO_EXT").trim();

        if (mainTo.length() > 0 && mainToExt.length() > 0) {
            mainTo += "," + mainToExt;
        } else if (mainToExt.length() > 0) {
            mainTo = mainToExt;
        }

        if (mainTo.length() > 0) {
            mainTo = mainTo.replaceAll(",", "，");
        }

        gwBean.set("GW_MAIN_TO", mainTo);
        return mainTo;
    }

    /**
     * 
     * @return 附件列表字符串
     */
    public String getFujian() {

        List<Bean> fujianList = fileHelper.getFujianList();

        if (fujianList.size() == 0) {
            return "";
        }
        StringBuilder rtn = new StringBuilder();
        rtn.append("附件:");
        //rtn.append("\\r\\n");
        for (int i = 0; i < fujianList.size(); i++) {
            Bean fujianBean = fujianList.get(i);
            if (i > 0) {
                rtn.append("       ");
            }
            rtn.append(i + 1).append(".");
            rtn.append(fujianBean.getStr("DIS_NAME"));
            rtn.append("\\r\\n");
        }
        return rtn.toString();
    }
    
    /**
     * 
     * @return 签发日期
     */
    public String getSignTime() {
        String signTime = this.gwBean.getStr("GW_SIGN_TIME");
        if (signTime.length() > 0) {
            return DateUtils.getChineseTwoDate(signTime);
        }
        return "";
    }
    
    /**
     * 获取联合发文部门
     * @return 联合发文部门名称
     */
    public String getTDept() {
        // 判断是否联合发文
        String sealDepts = this.getValue("SEAL_DEPTS");
        StringBuffer lastSealDepts = new StringBuffer();
        if (StringUtils.isNotBlank(sealDepts)) { //不为空
            for (String dept : sealDepts.split(Constant.SEPARATOR)) {
                //联合发文中存在拟稿部门，剔除此部门,不存在则加入联合发文部门
                if (!dept.equals(this.getValue("S_TDEPT")) && StringUtils.isNotBlank(dept)) {
                    lastSealDepts.append(dept).append(Constant.SEPARATOR);
                }
            }
            String deptNames = DictMgr.getFullNames("SY_ORG_DEPT_ALL", lastSealDepts.toString());
            StringBuffer lastName = new StringBuffer();
            String[] deptNamesArray = getShortDeptNames(deptNames.split(Constant.SEPARATOR));
            //返回格式为 [办公室）\\r\\n（人力资源）\\r\\n（财务部]
            lastName.append(this.getValue("S_TNAME")).append("）").append("\\r\\n");
            for (int i = 0; i < deptNamesArray.length; i++) {
                if (i + 1 == deptNamesArray.length) { //最后一个
                    lastName.append("（").append(deptNamesArray[i]);
                } else {
                    lastName.append("（").append(deptNamesArray[i]).append("）").append("\\r\\n");
                }
            }
            return lastName.toString();
        }
        //不是，则直接返回。联合发文部门字段如果是空，则不是联合发文
        return this.getValue("S_TNAME");
    }
    
    /**
     * 获取盖章部门名称
     * @return 盖章部门名称字符串
     */
    public String getDeptNames() {
     // 判断是否联合发文
        String sealDepts = this.getValue("SEAL_DEPTS");
        StringBuffer lastSealDepts = new StringBuffer();
        StringBuffer lastSealDeptsName = new StringBuffer();
        if (StringUtils.isNotBlank(sealDepts)) { //不为空
            for (String dept : sealDepts.split(Constant.SEPARATOR)) {
                //联合发文中存在拟稿部门，剔除此部门,不存在则加入联合发文部门
                if (!dept.equals(this.getValue("S_TDEPT")) && StringUtils.isNotBlank(dept)) {
                    lastSealDepts.append(dept).append(Constant.SEPARATOR);
                }
            }
            String lastDepts = this.getValue("S_TNAME") + Constant.SEPARATOR + DictMgr.getFullNames("SY_ORG_DEPT_ALL"
                               , lastSealDepts.toString());
            String[] lastDeptNames = getShortDeptNames(lastDepts.split(Constant.SEPARATOR));
            for (String deptName : lastDeptNames) {
                lastSealDeptsName.append(Constant.SEPARATOR).append(deptName);
            }
            return lastSealDeptsName.toString().substring(1);
        }
        return this.getValue("S_TNAME");
    }
    
    /**
     * 获取部门简称，去除部门名称中的[/]
     * @param fullDeptNames 参数
     * @return 部门简称
     */
    private String[] getShortDeptNames(String[] fullDeptNames) {
        int fullDeptLength = fullDeptNames.length;
        String[] lastDeptNames = new String[fullDeptLength];
        for (int i = 0; i < fullDeptLength; i++) {
            String deptName = fullDeptNames[i];
            int lastIndex = deptName.lastIndexOf("/") + 1;
            int length = deptName.length();
            if (lastIndex > 0 && lastIndex <= length) {
                lastDeptNames[i] = deptName.substring(lastIndex, length);
            } else {
                lastDeptNames[i] = deptName;
            }
        }
        return lastDeptNames;
    }
}
