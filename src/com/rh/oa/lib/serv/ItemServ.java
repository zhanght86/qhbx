package com.rh.oa.lib.serv;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;

import com.rh.core.base.Bean;
import com.rh.core.base.Context;
import com.rh.core.base.TipException;
import com.rh.core.comm.FileMgr;
import com.rh.core.org.UserBean;
import com.rh.core.serv.CommonServ;
import com.rh.core.serv.OutBean;
import com.rh.core.serv.ParamBean;
import com.rh.core.serv.ServMgr;
import com.rh.core.util.Lang;
import com.rh.core.util.RequestUtils;
import com.rh.core.util.file.Zip;
import com.rh.oa.lib.LibConstants;
import com.rh.oa.zh.seal.SecureFile;

/**
 * 文库条目
 * @author yangjy
 * 
 */
public class ItemServ extends CommonServ {
    /** 部门文档管理员 **/
    private static final String TDEPT_MGR = "CONF_OA_LIB_TDEPT_MGR";

    /** 机构文档管理员 **/
    private static final String ODEPT_MGR = "CONF_OA_LIB_ODEPT_MGR";

    private static final int LEVEL_TDEPT = 2;

    private static final int LEVEL_ODEPT = 1;

    @Override
    protected void beforeSave(ParamBean paramBean) {
        super.beforeSave(paramBean);
        if (paramBean.isNotEmpty("FILE_SCOPE_DEF")) { // 根据前台数据，合并角色显示范围
            paramBean.set("FILE_SCOPE", Lang.mergeBitVal(paramBean.getStr("FILE_SCOPE_DEF")));
        }
    }

    @Override
    protected void afterSave(ParamBean paramBean, OutBean outBean) {
        super.afterSave(paramBean, outBean);
        if (paramBean.getAddFlag()) { // 添加记录
            ParamBean aclBean = new ParamBean();
            aclBean.set("ACL_TYPE", "R");
            aclBean.set("OBJ_ID", outBean.getId());
            aclBean.set("SERV_ID", paramBean.getServId());
            aclBean.set("OWNER_ID", "RPUB");
            aclBean.set("OWNER_NAME", "公共角色");

            ServMgr.act("OA_LIB_ACL", ServMgr.ACT_SAVE, aclBean);
        } else {
            // 修改记录
            if (paramBean.isNotEmpty("READ_TYPE")) {
                Bean oldBean = paramBean.getSaveOldData();
                if (oldBean.getInt("READ_TYPE") != paramBean.getInt("READ_TYPE")) {
                    convertSecureFile(paramBean);
                }
            }

        }
    }

    /**
     * 转换安全文档
     * @param paramBean 参数Bean
     */
    private void convertSecureFile(ParamBean paramBean) {
        List<Bean> list = LibFileMgr.getFileList(paramBean.getId());
        if (paramBean.getInt("READ_TYPE") == LibConstants.READ_TYPE_READONLY) {
            // 由下载改成只读，重新转换文件
            // for (Bean libFileBean : list) {
            // Bean fileBean = FileMgr.getFile(libFileBean.getStr("FILE_ID"));
            // SecureFile sfile = new SecureFile();
            // sfile.setSealSysHost(Context.getSyConf("SEAL_SYS_HOST", ""));
            // sfile.setOaSysHost(RequestUtils.getSysHost());
            // sfile.createFile(fileBean);
            // }
        } else if (paramBean.getInt("READ_TYPE") == LibConstants.READ_TYPE_DOWNLOAD) {
            // 由只读改成下载：1，删除安全文档
            for (Bean libFileBean : list) {
                SecureFile sfile = new SecureFile();
                sfile.setSealSysHost(Context.getSyConf("SEAL_SYS_HOST", ""));
                sfile.setOaSysHost(RequestUtils.getSysHost());
                sfile.deleteFile(libFileBean.getStr("FILE_ID"));
                // 2，清空LibFile表的状态
                ParamBean modifyBean = new ParamBean();
                modifyBean.setId(libFileBean.getId());
                modifyBean.set("CONVERT_OK", 2);
                modifyBean.set("ERROR_MSG", "");
                ServMgr.act("OA_LIB_FILE", ServMgr.ACT_SAVE, modifyBean);
            }

        }
    }

    @Override
    protected void beforeQuery(ParamBean paramBean) {
        super.beforeQuery(paramBean);
        UserBean user = Context.getUserBean();
        boolean isTDeptMgr = isTDeptMgr();
        boolean isODeptMgr = isODeptMgr();

        if (isTDeptMgr && isODeptMgr) {
            StringBuilder sql = new StringBuilder();
            sql.append(" and ((S_ODEPT='").append(user.getODeptCode()).append("'");
            sql.append(" and FILE_LEVEL=").append(LEVEL_ODEPT).append(")");
            sql.append(" or (S_TDEPT='").append(user.getTDeptCode()).append("'");
            sql.append(" and FILE_LEVEL=").append(LEVEL_TDEPT).append("))");
            paramBean.setQueryExtWhere(sql.toString());
        } else if (isODeptMgr) {
            StringBuilder sql = new StringBuilder();
            sql.append(" and S_ODEPT = '").append(user.getODeptCode()).append("'");
            sql.append(" and FILE_LEVEL=").append(LEVEL_ODEPT);
            paramBean.setQueryExtWhere(sql.toString());
        } else if (isTDeptMgr) {
            paramBean.setQueryExtWhere(" and S_TDEPT = '" + user.getTDeptCode() + "' ");
        } else {
            // 无权限，则查询不出数据
            paramBean.setQueryExtWhere(" and ITEM_ID = 'NO_RIGHT' ");
            paramBean.set("_NO_RIGHT", true);
        }
    }

    @Override
    protected void afterQuery(ParamBean paramBean, OutBean outBean) {
        super.afterQuery(paramBean, outBean);
        if (paramBean.isNotEmpty("_NO_RIGHT")) {
            outBean.set("_NO_RIGHT", true);
        }
    }

    @Override
    protected void afterByid(ParamBean paramBean, OutBean outBean) {
        super.afterByid(paramBean, outBean);
        boolean isTDeptMgr = isTDeptMgr();
        boolean isODeptMgr = isODeptMgr();

        outBean.set("DISPLAY_LEVEL", false);

        // 设置文件级别权限
        if (StringUtils.isEmpty(paramBean.getId())) {
            if (isTDeptMgr && isODeptMgr) {
                outBean.set("DISPLAY_LEVEL", true); // 允许修改文件级别
                outBean.set("FILE_LEVEL", LEVEL_ODEPT); // 文件级别默认值设置为机构级
            } else if (isODeptMgr) {
                outBean.set("FILE_LEVEL", LEVEL_ODEPT); // 文件级别默认值设置为机构级
            } else if (isTDeptMgr) {
                outBean.set("FILE_LEVEL", LEVEL_TDEPT); // 文件级别默认值设置为部门级
            } else {
                // 无权限，则前台去掉保存按钮，并让整个页面只读
                outBean.set("NO_ACT_RIGHT", true);
            }
        } else {
            if (isTDeptMgr && isODeptMgr) {
                outBean.set("DISPLAY_LEVEL", true); // 允许修改文件级别
            } else if (!isODeptMgr && !isTDeptMgr) {
                // 无权限，则前台去掉保存按钮，并让整个页面只读
                outBean.set("NO_ACT_RIGHT", true);
            }
        }

        // 根据合并的数据拆分出可供多选框反选的值。
        if (outBean.getInt("FILE_SCOPE") > 0) {
            outBean.set("FILE_SCOPE_DEF", Lang.splitBitVal(outBean.getInt("FILE_SCOPE")));
        }

        if (!paramBean.getAddFlag()) {
            // 文件列表
            List<Bean> list = LibFileMgr.getFileList(paramBean.getId());
            outBean.set("LIB_FILE_LIST", list);
        }

        // Bean convertInfo = new Bean();
        // convertInfo.set("OA_HOST_URL", RequestUtils.getSysHost());
        // convertInfo.set("SEAL_HOST_URL", Context.getSyConf(SealMgr.CONF_SEAL_SYS_HOST, ""));
        // outBean.set("convertInfo", convertInfo);
    }

    /**
     * 
     * @return 部门文件管理员
     */
    private boolean isTDeptMgr() {
        UserBean user = Context.getUserBean();
        String tdeptMgrRole = Context.getSyConf(TDEPT_MGR, "");
        if (tdeptMgrRole.length() > 0 && user.existInRole(tdeptMgrRole)) {
            return true;
        }

        return false;
    }

    /**
     * 
     * @return 机构文件管理员
     */
    private boolean isODeptMgr() {
        UserBean user = Context.getUserBean();
        String odeptMgrRole = Context.getSyConf(ODEPT_MGR, "");
        if (odeptMgrRole.length() > 0 && user.existInRole(odeptMgrRole)) {
            // outBean.set("IS_ODEPT_MGR", true);
            return true;
        }

        return false;
    }

    /**
     * 
     * @param paramBean 参数Bean
     * @return 下载文件
     */
    public OutBean download(ParamBean paramBean) {
        Bean itemBean = ItemMgr.find(paramBean.getId());
        if (itemBean == null) {
            throw new TipException("File not found.");
        }

        if (itemBean.getInt("READ_TYPE") == LibConstants.READ_TYPE_READONLY) {
            throw new TipException("不允许下载此文件.");
        }

        List<Bean> list = FileMgr.getFileListBean("", itemBean.getId());
        OutBean outBean = new OutBean();
        if (list.size() == 0) {
            throw new TipException("File not found.");
        } else if (list.size() == 1) {
            outBean.setToDispatcher("/file/" + list.get(0).getId());

            return outBean;
        }

        InputStream is = null;
        OutputStream out = null;
        Zip zip = null;
        HttpServletResponse res = Context.getResponse();
        try {
            out = Context.getResponse().getOutputStream();
            res.setContentType("application/x-zip-compressed");
            RequestUtils.setDownFileName(Context.getRequest(), res, itemBean.getStr("TITLE") + ".zip");
            zip = new Zip(out);
            for (Bean fileBean : list) {
                is = FileMgr.download(fileBean);
                zip.addOneFile2Zip(is, fileBean.getStr("FILE_NAME"), null);
            }
            zip.close();
            zip = null;
            res.flushBuffer();
        } catch (IOException e) {
            log.error(e.getMessage(), e);
            throw new TipException(e.getMessage());
        } finally {
            IOUtils.closeQuietly(is);
            IOUtils.closeQuietly(out);
            if (zip != null) {
                zip.close();
            }
        }

        return null;
    }

}
