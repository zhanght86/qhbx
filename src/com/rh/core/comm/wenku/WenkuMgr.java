/*
 * Copyright (c) 2013 Ruaho All rights reserved.
 */
package com.rh.core.comm.wenku;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.rh.core.base.Bean;
import com.rh.core.base.start.MsgLisLoader;
import com.rh.core.comm.cms.mgr.ChannelMgr;
import com.rh.core.comm.cms.mgr.ExtendsField;
import com.rh.core.comm.cms.mgr.ReqhisMgr;
import com.rh.core.comm.integral.IntegralMgr;
import com.rh.core.org.UserBean;
import com.rh.core.serv.ParamBean;
import com.rh.core.serv.ServDao;
import com.rh.core.serv.ServMgr;
import com.rh.core.util.msg.CommonMsg;
import com.rh.core.util.msg.MsgCenter;

/**
 * 文库
 * @author liwei
 * 
 */
public class WenkuMgr {
    // 日志记录
    @SuppressWarnings("unused")
    private static Log log = LogFactory.getLog(WenkuMgr.class);

    private static WenkuMgr instance = new WenkuMgr();

    // 文库服务CODE
    private static final String SERV_DOCUMENT = ServMgr.SY_COMM_WENKU_DOCUMENT;

    /** 所有继承关系字段对象集合 */
    public static final List<ExtendsField> EXTENDS_FIELDS = new ArrayList<ExtendsField>();
    static {
        // 栏目下内容模版继承
        ExtendsField documentTmpl = new ExtendsField();
        documentTmpl.setIdField("TMPL_ID");
        // documentTmpl.setNameField("TMPL_NAME");
        documentTmpl.setExtendsField("TMPL_EXTENDS");
        documentTmpl.setParentIdField("CHNL_CONTENT_TMPL");
        // documentTmpl.setParentNameField("CHNL_CONTENT_TMPL_NAME");
        EXTENDS_FIELDS.add(documentTmpl);

        // 栏目下内容评论状态继承
        ExtendsField comment = new ExtendsField();
        comment.setIdField("DOCUMENT_COMMENT_STATUS");
        comment.setExtendsField("DOCUMENT_COMMENT_EXTENDS");
        comment.setParentIdField("CHNL_CONTENT_COMMENT_STATUS");
        EXTENDS_FIELDS.add(comment);
    }

    /**
     * 是否为重要文档管理员
     * @param userbean - user
     * @return boolean
     */
    public boolean importantDocumentAdmin(UserBean userbean) {
        // TODO 通过授权
        return userbean.isAdminRole();
    }

    /**
     * Singleton
     * @return - Singleton instance
     */
    public static WenkuMgr getInstance() {
        return instance;
    }

    /**
     * can not new instance
     */
    private WenkuMgr() {
    }

    /**
     * get channel bean from cache and database(cache first)
     * @param id - channel pk
     * @return - cache bean
     */
    public Bean getDocument(String id) {
        Bean result = ServDao.find(SERV_DOCUMENT, new Bean().setId(id));
        if (null == result) {
            return null;
        }
        setExtendValues(result);
        return result;
    }

    /**
     * 获取文档列表
     * @param param 参数Bean
     * @return 文档列表
     * @deprecated
     */
    public List<Bean> getDocumentList(ParamBean param) {
        param.setServId(ServMgr.SY_COMM_WENKU_DOCUMENT);
        param.setAct(ServMgr.ACT_QUERY);

        int showNum = param.getShowNum();
        param.setQueryPageShowNum(showNum);

        List<Bean> list = ServMgr.act(param).getDataList();

        for (int i = 0; i < list.size(); i++) {
            Bean b = list.get(i);
            b.set("DOCUMENT_FILE", this.getFile(b.getStr("DOCUMENT_FILE")));
            if (2 == b.get("PARSED_FLAG", 2)) {
                // 生成缩略图
                // AsyncMessage message = new AsyncMessage(b, AsyncMgr.HandlerName.DOCUMENT_SNAPSHOT_HANDLER);
                // AsyncMgr.getInstance().addMessage(message);
                CommonMsg msg = new CommonMsg(b, MsgLisLoader.DOCSNAPSHOT_MSG_TYPE);
                MsgCenter.getInstance().addMsg(msg);

            }
        }
        return list;
    }

    /**
     * 下载量 + 1
     * @param param - 参数bean
     */
    public void increaseDownloadCounter(Bean param) {
        String key = "DOCUMENT_DOWNLOAD_COUNTER";
        Bean document = ServDao.find(ServMgr.SY_COMM_WENKU_DOCUMENT, param);
        document.set(key, document.get(key, 0) + 1);
        ServDao.update(ServMgr.SY_COMM_WENKU_DOCUMENT, document);

        // 更新下载历史
        String act = "download";
        ReqhisMgr.save(ServMgr.SY_COMM_WENKU_DOCUMENT, document.getId(), act, document.getStr("DOCUMENT_TITLE"),
                document.getStr("S_USER"));

        String owner = document.getStr("S_USER");
        
        // 增加积分
        IntegralMgr.getInstance().handle(owner, WenkuServ.SY_COMM_WENKU, ServMgr.SY_COMM_WENKU_DOCUMENT,
                document.getId(), document.getStr("DOCUMENT_TITLE"), "SY_COMM_WENKU_DOCUMENT_DOWNLOAD");
    }

    /**
     * 阅读量 + 1
     * @param param - 参数bean
     */
    public void increaseReadCounter(Bean param) {
        String key = "DOCUMENT_READ_COUNTER";
        Bean document = ServDao.find(ServMgr.SY_COMM_WENKU_DOCUMENT, param);
        document.set(key, document.get(key, 0) + 1);
        ServDao.update(ServMgr.SY_COMM_WENKU_DOCUMENT, document);

        // 更新阅读历史
        String act = "read";
        ReqhisMgr.save(ServMgr.SY_COMM_WENKU_DOCUMENT, document.getId(), act, document.getStr("DOCUMENT_TITLE"),
                document.getStr("S_USER"));
    }

    /**
     * set extend field value
     * @param documentBean - document bean
     */
    public void setExtendValues(Bean documentBean) {
        for (ExtendsField extendsField : EXTENDS_FIELDS) {
            setExtendValue(documentBean, extendsField);
        }
    }

    /**
     * set document extends value
     * @param documentBean - document bean
     * @param extendsField - extends fields properties
     */
    private void setExtendValue(Bean documentBean, ExtendsField extendsField) {
        String chnlId = documentBean.getStr("DOCUMENT_CHNL");
        String idField = extendsField.getIdField();
        String nameField = extendsField.getNameField();
        String exField = extendsField.getExtendsField();

        if (null == chnlId || 0 == chnlId.length()) {
            return;
        }

        if (documentBean.isNotEmpty(extendsField)) {
            return;
        }

        if (documentBean.isEmpty(idField)) {
            Bean parent = ChannelMgr.getInstance().getChannel(chnlId);
            if (null == parent) {
                return;
            }
            documentBean.set(exField, 1);
            documentBean.set(idField, parent.getStr(extendsField.getParentIdField()));
            if (null != nameField) {
                documentBean.set(nameField, parent.getStr(extendsField.getParentNameField()));
            }
        } else {
            documentBean.set(exField, 2);
        }
    }

    /**
     * @param file ：要处理的字符串
     * @return 处理之后的字符串
     */
    private String getFile(String file) {
        if (file.contains(",")) {
            return file.substring(0, file.indexOf(","));
        } else {
            return file;
        }
    }
}
