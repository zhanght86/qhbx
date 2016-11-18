package com.rh.oa.comm;

import java.io.UnsupportedEncodingException;
import java.util.List;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.rh.core.base.Bean;
import com.rh.core.base.TipException;
import com.rh.core.comm.FileMgr;
import com.rh.core.org.DeptBean;
import com.rh.core.org.UserBean;
import com.rh.core.org.mgr.OrgMgr;
import com.rh.core.org.mgr.UserMgr;
import com.rh.core.plug.search.IndexAttachment;
import com.rh.core.plug.search.RhIndex;
import com.rh.core.plug.search.client.RhFileClient;
import com.rh.core.serv.flow.FlowMgr;
import com.rh.core.util.Constant;
import com.rh.core.util.RequestUtils;
import com.rh.oa.gw.util.GwRightUtils;

/**
 * 用于为审批单创建索引的抽象类
 * @author yangjy
 * 
 */
public abstract class AbstractIndexListener {
    /** 日志记录 **/
    protected Log log = LogFactory.getLog(this.getClass());

    /**
     * 生成访问审批单的URL
     * @param title 标题
     * @param servId 服务ID
     * @param dataId 审批单ID
     * @return 访问审批单的URL
     */
    protected String createAccessUrl(String title, String servId, String dataId) {
        StringBuilder result = new StringBuilder();
        result.append("{'tTitle':'").append(title);
        result.append("','url':'").append(servId);
        result.append(".card.do?pkCode=").append(dataId);
        result.append("','menuFlag':4");
        result.append("}");

        try {
            return RequestUtils.getSysHost() + "/sy/comm/page/page.jsp?openTab="
                    + Hex.encodeHexString(result.toString().getBytes("UTF-8")) + "&hexToStr=true";
        } catch (UnsupportedEncodingException e) {
            throw new TipException(e.getMessage());
        }
    }

    /**
     * 
     * @param iaMsg 索引对象
     * @param dataId 审批单ID
     * @param servId 服务ID
     */
    protected void addFileList(RhIndex iaMsg, String dataId, String servId) {
        List<Bean> fileList = FileMgr.getFileListBean("", dataId);

        addFileList(iaMsg, fileList);
    }

    /**
     * 
     * @param iaMsg 索引对象
     * @param fileList 文件列表
     */
    protected void addFileList(RhIndex iaMsg, List<Bean> fileList) {
        for (Bean file : fileList) {
            addFile(iaMsg, file);
        }
    }

    /**
     * 
     * @param iaMsg 索引对象
     * @param file 文件Bean
     */
    protected void addFile(RhIndex iaMsg, Bean file) {
        try {
            if (FileMgr.exists(file)) {
                StringBuilder fileUrl = new StringBuilder();
                fileUrl.append(Constant.FILE_INNER_URL_PREFIX);
                fileUrl.append(file.getId());

                final String ext = FilenameUtils.getExtension(file.getStr("FILE_NAME"));
                if (RhFileClient.aviableFileType(ext)) { // 是可用的文件类型
                    IndexAttachment attachment = new IndexAttachment();
                    attachment.setId(file.getId());
                    attachment.setIndexPath(fileUrl.toString());
                    attachment.setTitle(file.getStr("DIS_NAME"));

                    String extName = FilenameUtils.getExtension(file.getStr("FILE_NAME"));
                    attachment.setExtension(extName);
                    if (file.isNotEmpty("_ACCESS_URL")) {
                        attachment.setAccessPath(file.getStr("_ACCESS_URL"));
                    } else {
                        String accessUrl = RequestUtils.getSysHost() + "/file/" + file.getId();
                        attachment.setAccessPath(accessUrl);
                    }
                    iaMsg.addAtt(attachment);
                }
            } else {
                log.warn("File Not Found:" + file.getId());
            }
        } catch (Exception e) {
            log.warn(e.getMessage());
        }
    }

    /**
     * 增加流经权限
     * @param iaMsg 索引对象
     * @param data 数据Bean
     */
    protected void grant(RhIndex iaMsg, Bean data) {
        final String dataId = data.getId();
        List<Bean> flowList = FlowMgr.find(dataId);

        for (Bean flowBean : flowList) {
            final String ownerId = flowBean.getStr("OWNER_ID");
            UserBean user = getUserBean(ownerId);
            if (user != null) {
                iaMsg.grantUser(ownerId);
            } else {
                DeptBean dept = OrgMgr.getDept(ownerId);
                if (dept != null) {
                    grantDept(iaMsg, dept);
                }
            }
        }
    }

    /**
     * 
     * @param iaMsg 索引对象
     * @param dept 部门数据对象
     */
    protected void grantDept(RhIndex iaMsg, DeptBean dept) {
        if (dept.getType() == Constant.DEPT_TYPE_ORG) { // 部门为机构
            String adminRole = GwRightUtils.getAdminRole();
            if (adminRole.length() > 0) {
                iaMsg.grant(null, dept.getId(), adminRole);
            }
        } else {
            String deptAdminRole = GwRightUtils.getDeptAdminRole();
            if (deptAdminRole.length() > 0) {
                iaMsg.grant(null, dept.getId(), deptAdminRole);
            }
        }
    }

    /**
     * 
     * @param userCode 用户编码
     * @return 用户编码对应的用户Bean
     */
    private UserBean getUserBean(String userCode) {
        try {
            UserBean user = UserMgr.getUser(userCode);
            return user;
        } catch (Exception e) {
            return null;
        }
    }

}
