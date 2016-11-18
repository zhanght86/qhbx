/*
 * Copyright (c) 2013 Ruaho All rights reserved.
 */
package com.rh.core.comm.wenku.msg;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.rh.core.base.Bean;
import com.rh.core.comm.FileMgr;
import com.rh.core.plug.search.client.RhFileClient;
import com.rh.core.serv.ServDao;
import com.rh.core.util.Lang;
import com.rh.core.util.http.HttpGetResponse;
import com.rh.core.util.http.HttpUtils;
import com.rh.core.util.msg.Msg;
import com.rh.core.util.msg.TypeMsgListener;

/**
 * 异步生成文档快照(缩略图)
 * @author liwei
 * 
 */
@SuppressWarnings("unchecked")
public class DocumentSnapshotListener extends TypeMsgListener {

    /** log */
    private static Log log = LogFactory.getLog(DocumentSnapshotListener.class);
    
    @SuppressWarnings("rawtypes")
    private static final List TIMAGE_SUFFIX_WHITE_LIST = new ArrayList();

    static {
        // ms word
        TIMAGE_SUFFIX_WHITE_LIST.add("doc");
        TIMAGE_SUFFIX_WHITE_LIST.add("dot");
        TIMAGE_SUFFIX_WHITE_LIST.add("docx");
        TIMAGE_SUFFIX_WHITE_LIST.add("dotx");
        TIMAGE_SUFFIX_WHITE_LIST.add("docm");
        TIMAGE_SUFFIX_WHITE_LIST.add("dotm");
        TIMAGE_SUFFIX_WHITE_LIST.add("xls");
        TIMAGE_SUFFIX_WHITE_LIST.add("xlt");
        TIMAGE_SUFFIX_WHITE_LIST.add("xla");
        TIMAGE_SUFFIX_WHITE_LIST.add("xlsx");
        TIMAGE_SUFFIX_WHITE_LIST.add("xltx");
        TIMAGE_SUFFIX_WHITE_LIST.add("xlsm");
        TIMAGE_SUFFIX_WHITE_LIST.add("xltm");
        TIMAGE_SUFFIX_WHITE_LIST.add("xlam");
        TIMAGE_SUFFIX_WHITE_LIST.add("xlsb");
        TIMAGE_SUFFIX_WHITE_LIST.add("ppt");
        TIMAGE_SUFFIX_WHITE_LIST.add("pot");
        TIMAGE_SUFFIX_WHITE_LIST.add("pps");
        TIMAGE_SUFFIX_WHITE_LIST.add("ppa");
        TIMAGE_SUFFIX_WHITE_LIST.add("pptx");
        TIMAGE_SUFFIX_WHITE_LIST.add("potx");
        TIMAGE_SUFFIX_WHITE_LIST.add("ppsx");
        TIMAGE_SUFFIX_WHITE_LIST.add("ppam");
        TIMAGE_SUFFIX_WHITE_LIST.add("pptm");
        TIMAGE_SUFFIX_WHITE_LIST.add("potm");
        // text
        TIMAGE_SUFFIX_WHITE_LIST.add("txt");
        // pdf
        TIMAGE_SUFFIX_WHITE_LIST.add("pdf");
    }

    @Override
    public void init(String conf) {
        log.info("document listener start...");
        super.init(conf);
    }

    @Override
    protected void onTypeMsg(Msg msg) {
        log.info(" document snapshot...");
        snapshot(msg.getBody());
    }

    /**
     * 生成快照并持久化
     * @param documentBean - docbean
     */
    private void snapshot(Bean documentBean) {
        String fileId = documentBean.getStr("DOCUMENT_FILE");
        if (-1 < fileId.lastIndexOf(",")) {
            fileId = fileId.substring(0, fileId.lastIndexOf(","));
        }
        if (null == fileId || fileId.length() == 0) {
            log.warn("file id is null, document: " +  documentBean);
            return;
        }
        String suffix = "";
        if (-1 < fileId.lastIndexOf(".")) {
            suffix = fileId.substring(fileId.lastIndexOf(".") + 1);
        }
        
        Bean file = FileMgr.getFile(fileId);
        
        //Do not support suffix
        if (!TIMAGE_SUFFIX_WHITE_LIST.contains(suffix)) {
            updateDocument(file, documentBean.getId(), "", 0);
            return;
        }
      
        InputStream fileInput = null;
        try {
            fileInput = FileMgr.download(file);
        } catch (IOException e) {
            e.printStackTrace();
        }

        String snapshotXml = "";
        try {
            snapshotXml = snapshot(fileInput);
        } catch (IOException e) {
            e.printStackTrace();
            if (e.getMessage().contains("Do not support suffix")) {
                updateDocument(file, documentBean.getId(), "", 0);
            }
            return;
        }
        String url = Lang.subString(snapshotXml, "<url>", "</url>");
        String pagesStr =  Lang.subString(snapshotXml, "<pages>", "</pages>");
        int pages = 0;
        try {
             pages = Integer.valueOf(pagesStr);
        } catch (Exception e) {
            pages = 0;
        }
        IOUtils.closeQuietly(fileInput);
        HttpGetResponse response = HttpUtils.httpGet(url);
        InputStream snapshotInput = response.getInputStream();
        // 创建、更新快照
        String snapshotId = fileId + "_ss.png";
        Bean snapshotFile = FileMgr.getFile(snapshotId);
        if (null == snapshotFile) {
            FileMgr.upload("SY_COMM_WENKU_DOCUMENT", documentBean.getId(), snapshotId, "snapshot", "SNAPSHOT.png",
                    snapshotInput, "snapshot.png", "image/png");
        } else {
            FileMgr.overWrite(snapshotId, snapshotInput, "SNAPSHOT.png", true);
        }
        IOUtils.closeQuietly(snapshotInput);

        updateDocument(file, documentBean.getId(), snapshotId, pages);
    }

  

    /**
     * 更新文档属性
     * @param file - 文件bean
     * @param documentId - document id
     * @param snapshotId - snapshot file id
     * @param pages - file pages
     */
    private void updateDocument(Bean file, String documentId, String snapshotId, int pages) {
        String suffix = "";
        if (file.getStr("FILE_NAME") != null && file.getStr("FILE_NAME").indexOf(".") > -1) {
            suffix = file.getStr("FILE_NAME").substring(file.getStr("FILE_NAME").lastIndexOf(".") + 1);
            suffix = suffix.toLowerCase();
        }
        Bean upBean = new Bean().set("DOCUMENT_FILE_SNAPSHOT", snapshotId).setId(documentId);
        // 文件大小
        upBean.set("DOCUMENT_FILE_SIZE", file.getStr("FILE_SIZE"));
        // 文件页数
        upBean.set("DOCUMENT_FILE_PAGES", pages);
        
        // 文件名称
        upBean.set("DOCUMENT_FILE_NAME", file.getStr("FILE_NAME"));
        // 文件后缀
        upBean.set("DOCUMENT_FILE_SUFFIX", suffix);
        // 文件效验码
        upBean.set("DOCUMENT_FILE_CHECKSUM", file.getStr("FILE_CHECKSUM"));
        upBean.set("PARSED_FLAG", 1);
        // 更新文档快照
        ServDao.update("SY_COMM_WENKU_DOCUMENT", upBean);
    }

    /**
     * 文档快照
     * @param src - inputstream
     * @return 快照地址
     * @throws IOException - io exception
     */
    private String snapshot(InputStream src) throws IOException {
        String xml = "";
        xml = RhFileClient.getInstance().snapshot(src, "");
        String file = Lang.subString(xml, "<url>", "</url>");
        String url = RhFileClient.getInstance().getServerUri() + file;
        xml = xml.replace(file, url);
        return xml;
    }

}
