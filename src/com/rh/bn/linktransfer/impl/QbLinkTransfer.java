package com.rh.bn.linktransfer.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;

import com.rh.bn.linktransfer.LinkTransfer;
import com.rh.core.base.Context;
import com.rh.core.base.TipException;
import com.rh.core.comm.FileMgr;
import com.rh.core.util.RequestUtils;

/**
 * 签报相关链接处理类，如：查看签报信息、下载签报附件
 * @author Tanyh 20151223
 *
 */
public class QbLinkTransfer implements LinkTransfer{


    public void transferByRequest(HttpServletRequest request, HttpServletResponse response) {
        //获取签报id
        String qbId = RequestUtils.getStr(request, "_1_WAR_workflowportlet_instanceId");
        //获取附件id
        String attachId = RequestUtils.getStr(request, "p_p_resource_id");
        if (qbId.trim().length() <= 0 && attachId.trim().length() <= 0) {
            throw new TipException("缺少必要参数");
        }
        //查看签报信息
        if (attachId.trim().length() <= 0) {
            RequestUtils.sendDisp(request, response, "/sy/base/view/stdCardView.jsp?sId=BN_QB_DATA_QUERY&pkCode=" + qbId);
        } else {
            //查看附件
            //倒叙拼接附件所在路径
            String[] pathArr = attachId.split(",");
            if (pathArr.length < 3) {
                throw new TipException("传递的附件参数有误，请检查");
            }
            //签报附件迁移之后的存放路径
            String filePath = Context.getSyConf("BN_QB_ATTACHMENT_PATH", "") + File.separator
                    + pathArr[pathArr.length - 1] + File.separator + pathArr[pathArr.length - 2];
            filePath += File.separator + "1.0";
            File attachFile = new File(filePath);
            if (attachFile.exists() && attachFile.isFile()) {
                //以文件流的形式传回前端
                InputStream is = null;
                try {
                    String downFileMimeType = "";
                    if (pathArr[0].indexOf(".") >= 0) {
                        downFileMimeType = pathArr[0].substring(pathArr[0].lastIndexOf(".") + 1);
                    }
                    response.reset();
                    RequestUtils.setDownFileName(request, response, pathArr[0]);
                    response.setContentType(FileMgr.getMTypeBySuffix(downFileMimeType));
                    is = new FileInputStream(attachFile);
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
            } else {
                throw new TipException("签报附件不存在");
            }
        }
    }

}
