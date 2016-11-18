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
import com.rh.core.base.Bean;
import com.rh.core.base.TipException;
import com.rh.core.base.db.Transaction;
import com.rh.core.comm.FileMgr;
import com.rh.core.util.RequestUtils;

/**
 * 百年人寿项目旧OA图片链接处理类
 * @author tanyh 20151208
 *
 */
public class ImageLinkTransfer implements LinkTransfer{

    /**
     * 根据请求头，获取图片信息
     * @param request 请求头
     * @param response 响应头
     */

    public void transferByRequest(HttpServletRequest request, HttpServletResponse response) 
        throws TipException{
        //获取图片uuid
        String uuid = RequestUtils.getStr(request, "uuid");
        //获取图片groupid
        String groupid = RequestUtils.getStr(request, "groupId");
        //根据uuid、groupid获取图片信息
        String sqlStr = "select a.imageid,a.groupid,a.folderid,a.smallimageid,b.type_ from igimage a,image b where a.uuid_='"
                + uuid + "' and a.groupid=" + groupid + " and a.smallimageid=b.imageid ";
        Bean imageBean = Transaction.getExecutor().queryOne(sqlStr);
        if (imageBean != null && !imageBean.isEmpty()) {
            //拼接文件夹路径
            String filePath = FileMgr.getRootPath() + "zhbx" + File.separator + "BN_DT_IGMAGE" + File.separator;
            int subCount = 0;
            String imageid = imageBean.getStr("IMAGEID");
            //根据图片id拼接路径
            if (imageid.length() % 2 != 0) { //图片id长度为奇数，则最后一位忽略
                subCount = (imageid.length() - 1) / 2;
            } else { //图片id长度为偶数，则最后两位忽略
                subCount = imageid.length() / 2 - 1;
            }
            for (int j = 0; j < subCount; j ++) {
                filePath += imageid.substring(j * 2, j * 2 + 2) + File.separator;
            }
            File imageFile = new File(filePath + imageBean.getStr("SMALLIMAGEID") + "." + imageBean.getStr("TYPE_"));
            //图片存在
            if (imageFile.exists() && imageFile.isFile()) {
                //以文件流的形式传回前端
                InputStream is = null;
                try {
                    is = new FileInputStream(imageFile);
                    OutputStream out = response.getOutputStream();
                    IOUtils.copyLarge(is, out);
                    IOUtils.closeQuietly(is);
                    IOUtils.closeQuietly(out);
                    out.flush();
                } catch (FileNotFoundException e) {
                    throw new TipException(e.getMessage());
                } catch (IOException e) {
                    throw new TipException(e.getMessage());
                }
            }
        }
    }

}
