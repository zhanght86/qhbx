package com.rh.oa.zh.seal;

import java.io.InputStream;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;

import com.rh.core.base.Bean;
import com.rh.core.base.Context;
import com.rh.core.comm.FileMgr;
import com.rh.core.org.UserBean;
import com.rh.core.serv.OutBean;
import com.rh.core.serv.ParamBean;
import com.rh.core.serv.ServMgr;
import com.rh.core.util.RequestUtils;
import com.rh.core.util.http.HttpGetResponse;
import com.rh.core.util.http.HttpUtils;
import com.rh.oa.gw.GwSealProcess;
import com.rh.oa.gw.GwServ;
import com.rh.oa.gw.util.GwFileHelper;

/**
 * 
 * 中华电子印章接口的抽象类
 * @author yangjy
 * 
 */
public abstract class AbstractZhJdSealProcess implements GwSealProcess {
    /**
     * 检查是否是符合规范的盖章文件
     * @param fileBean 文件对象
     */
    public abstract void checkSealFileType(Bean fileBean);

    /**
     * 
     * @return 返回实现类所属的的模块名。仅用于中华集成的吉大正元电子印章。
     */
    public abstract String getModelName();

    /**
     * 
     * @param gwBean 审批单Bean
     * @return 人力资源系统机构编码
     */
    public abstract String getODeptNum(Bean gwBean);

    public OutBean getSealFileInfo(Bean gwBean, Bean fileBean, int printNum) {
        OutBean out = new OutBean();

        checkSealFileType(fileBean);

        out.set("sealFileId", fileBean.getId() + ";" + gwBean.getId() + ";" + getModelName());

        // 取得员工编号
        UserBean user = Context.getUserBean();
        out.put("USER_WORK_NUM", user.getStr("USER_WORK_NUM"));
        out.put("USER_LOGIN_NAME", user.getLoginName());
        // FILE_PATH
        out.put("FILE_PATH", "/file/" + fileBean.getId());
        // 印章系统主机地址
        out.put("SEAL_SYS_HOST", Context.getSyConf(SealMgr.CONF_SEAL_SYS_HOST, ""));
        // 本机地址，兼容集群情况
        out.put("SYS_HOST_ADDR", RequestUtils.getSysHost());
        out.put("DEFAULT_PRINT_NUM", printNum);
        // 机构编号
        out.put("ODEPT_NUM", getODeptNum(gwBean));
        // 文件编号
        out.put("APP_CODE", gwBean.getStr("GW_CODE"));
        // 文档标题
        out.put("APP_TITLE", gwBean.getStr("GW_TITLE"));

        return out;
    }

    public OutBean getPrintFileInfo(Bean gwBean, Bean fileBean) {
        OutBean out = new OutBean();
        Bean sealFile = SealMgr.getSealFileBean(fileBean);

        if (sealFile == null) {
            out.setError("没有签章文件");
            return out;
        }

        out.set("GW_ID", sealFile.getStr("DATA_ID"));
        out.set("SEAL_FILE_ID", sealFile.getStr("SEAL_FILE_ID"));
        out.set("ORIG_FILE_ID", sealFile.getStr("FILE_ID"));
        // 印章系统主机地址
        out.put("SEAL_SYS_HOST", Context.getSyConf(SealMgr.CONF_SEAL_SYS_HOST, ""));

        UserBean bean = Context.getUserBean();
        // 打印用户
        out.put("USER_WORK_NUM", bean.getStr("USER_WORK_NUM"));
        out.put("USER_LOGIN_NAME", bean.getLoginName());
        out.put("ODEPT_NUM", getODeptNum(gwBean));

        return out;
    }

    public void appendPrintNum(Bean gwBean, Bean zwFileBean) {
        Bean sealFile = SealMgr.getSealFileBean(zwFileBean);
        if (sealFile != null && sealFile.isNotEmpty("SEAL_FILE_ID")) {
            // 设置打印份数
            String odeptNum = getODeptNum(gwBean);
            SealMgr.lssuedDocPrint(sealFile.getStr("SEAL_FILE_ID"), odeptNum);
        }
    }

    public void setPrintNum(Bean gwBean, Bean zwFileBean) {
        Bean sealFile = SealMgr.getSealFileBean(zwFileBean);
        if (sealFile != null && sealFile.isNotEmpty("SEAL_FILE_ID")) {
            // 设置打印份数
            String odeptNum = getODeptNum(gwBean);
            SealMgr.lssuedDocPrint(sealFile.getStr("SEAL_FILE_ID"), odeptNum);
        }
    }

    public boolean hasSealed(ParamBean paramBean) {
        GwFileHelper gwFile = GwServ.getFileHelper(paramBean);

        Bean zwBean = gwFile.getZhengwen();
        if (zwBean == null) {
            return false;
        }

        String fileId = zwBean.getId();
        if (zwBean.isNotEmpty("ORIG_FILE_ID")) {
            fileId = zwBean.getStr("ORIG_FILE_ID");
        }

        Bean sealFile = SealMgr.findSealFile(fileId);
        if (null == sealFile || sealFile.isEmpty("SEAL_FILE_ID")) {
            return false;
        }

        return true;
    }

    /**
     * 下载黑头黑章文件到OA系统，作为公文的正文。
     * @param newZwBean 下载文件，并保持到newZwBean对应的记录中
     * @param sealFileId 印章系统盖章文件ID
     * @return 返回新文件
     */
    protected static Bean downloadSealFile(Bean newZwBean, String sealFileId) {
        // 印章系统下载文件的地址
        final String strUrl = Context.getSyConf("SEAL_SYS_HOST", "")
                + "/general/modules/wendang/anonDoc/downloadFile.jsp?fileNo="
                + sealFileId;

        InputStream is = null;
        try {
            HttpGetResponse res = HttpUtils.httpGet(strUrl);
            is = res.getInputStream();
            return FileMgr.upload(newZwBean, is);
        } finally {
            if (is != null) {
                IOUtils.closeQuietly(is);
            }
        }
    }

    /**
     * 保存印章系统红头红章文件与对应公文的关系
     * @param zwBean 正文文件ID
     * @param oldDocFileId 原始盖章Doc文件ID
     * @param sealFileId 印章系统盖章文件ID
     */
    protected static void saveSealFileData(Bean zwBean, String oldDocFileId, String sealFileId) {
        // 清除文件已盖章的记录
        // removeOldSealFileData(origBean.getId());

        ParamBean bean = new ParamBean();
        // OA系统灰头灰章文件ID
        bean.set("FILE_ID", zwBean.getId());
        bean.set("SERV_ID", zwBean.getStr("SERV_ID"));
        bean.set("DATA_ID", zwBean.getStr("DATA_ID"));
        // 盖章前的文件ID
        bean.set("BEFORE_SEAL_ID", oldDocFileId);
        if (StringUtils.isBlank(sealFileId) || sealFileId.toLowerCase().equals("failed")) {
            throw new RuntimeException(sealFileId + "：印章系统盖章文件保存失败，请重新盖章；或者联系印章系统管理员！");
        }
        // 印章系统红头红章文件ID
        bean.set("SEAL_FILE_ID", sealFileId);

        UserBean userBean = Context.getUserBean();
        if (userBean == null) {
            throw new RuntimeException("Session 已过期");
        }

        bean.set("S_ODEPT", userBean.getODeptCode());

        ServMgr.act(SealMgr.OA_GW_SEAL_FILE, ServMgr.ACT_SAVE, bean);
    }

    /**
     * @param zwFileBean 盖章后的灰头灰长文件ID
     * @return 是否能追加打印份数。
     */
    public boolean canAppendPrintNum(Bean zwFileBean) {
        Bean bean = SealMgr.getSealFileBean(zwFileBean);
        if (bean == null) {
            return false;
        }

        UserBean userBean = Context.getUserBean();
        if (userBean == null) {
            return false;
        }

        // 获取盖章单位
        final String odept = bean.getStr("S_ODEPT");
        // 盖章单位与当前用户所在单位一致，则返回true
        if (odept.equals(userBean.getODeptCode())) {
            return true;
        }

        return false;
    }
}
