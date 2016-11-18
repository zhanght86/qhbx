package com.rh.bn.linktransfer.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;

import com.rh.bn.linktransfer.LinkTransfer;
import com.rh.core.base.Bean;
import com.rh.core.base.TipException;
import com.rh.core.base.db.Transaction;
import com.rh.core.comm.FileMgr;
import com.rh.core.util.RequestUtils;

/**
 * 百年人寿项目旧OA文档链接处理类
 * @author tanyh 20151208
 *
 */
public class DocumentLinkTransfer implements LinkTransfer{

    /**
     * 根据请求头获取文档信息
     * @param request 请求头
     * @param response 响应头
     */

    public void transferByRequest(HttpServletRequest request, HttpServletResponse response) {
        //获取文档groupid
        String groupId = RequestUtils.getStr(request, "groupId");
        //获取文档uuid
        String uuId = RequestUtils.getStr(request, "uuid");
        //文档所在文件夹
        String folderId = "";
        //文档名称
        String docName = "";
        String strWhere = " where groupid=" + groupId;
        //如果没有uuid，则获取folderId、docName
        if (uuId.trim().length() <= 0) {
            folderId = RequestUtils.getStr(request, "folderId");
            docName = RequestUtils.getStr(request, "name");
            strWhere = strWhere + " and folderid=" + folderId + " and name='" + docName + "'";
        } else {
            strWhere = strWhere + " and uuid_='" + uuId + "'";
        }
        //获取文档信息
        Bean docBean = Transaction.getExecutor().queryOne(
                "select name, groupid,companyid, folderid,title, version from dlfileentry " + strWhere);
        if (docBean != null && !docBean.isEmpty()) {
            //拼接文件路径
            String filePath = FileMgr.getRootPath() + "zhbx" + File.separator + "BN_DT_DLFILEENTRY" + File.separator;
            filePath += docBean.getStr("COMPANYID") + File.separator + docBean.getStr("FOLDERID") + File.separator
                    + docBean.getStr("NAME") + File.separator + "1.0";
            // 获取文档
            File docFile = new File(filePath);
            if (docFile.exists() && docFile.isFile()) {
                //以文件流的形式传回前端
                InputStream is = null;
                try {
                    String downFileMimeType = "";
                    if (docBean.getStr("NAME").indexOf(".") >= 0) {
                        downFileMimeType = docBean.getStr("NAME").substring(docBean.getStr("NAME").lastIndexOf(".") + 1);
                    }
                    response.reset();
                    RequestUtils.setDownFileName(request, response, docBean.getStr("TITLE") + "." + downFileMimeType);
                    response.setContentType(FileMgr.getMTypeBySuffix(downFileMimeType));
                    is = new FileInputStream(docFile);
                    OutputStream out = response.getOutputStream();
                    IOUtils.copyLarge(is, out);
                    IOUtils.closeQuietly(is);
                    IOUtils.closeQuietly(out);
                    out.flush();
                } catch (FileNotFoundException e) {
                    throw new TipException(e.getMessage());
                } catch (IOException e) {
                    throw new TipException(e.getMessage());
                } finally {
                    try {
                        response.flushBuffer();
                    } catch (IOException e) {
                        throw new TipException(e.getMessage());
                    }
                }
            }
        }
    }

}
