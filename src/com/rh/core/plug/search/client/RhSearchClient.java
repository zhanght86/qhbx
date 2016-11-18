/*
 * Copyright (c) 2012 Ruaho All rights reserved.
 */
package com.rh.core.plug.search.client;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.client.solrj.request.FieldAnalysisRequest;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;
import org.apache.solr.common.util.NamedList;
import org.apache.solr.common.util.SimpleOrderedMap;

import com.rh.core.base.Context;
import com.rh.core.org.UserBean;
import com.rh.core.plug.search.ExtractResult;
import com.rh.core.plug.search.IndexTransformer;
import com.rh.core.plug.search.RhIndex;
import com.rh.core.plug.search.IndexAttachment;
import com.rh.core.plug.search.SearchHelper;

/**
 * ruaho search client
 * 
 * @author liwei
 * 
 */
// TODO code reconsitution, add index handler
public class RhSearchClient {
    /** use granee filter ? */
    public static final boolean GRANTEE_FILTER_ENABLE = true;

    /** log */
    private static Log log = LogFactory.getLog(RhSearchClient.class);

    /** solr schema index id key */
    public static final String SOLR_ID = RhIndex.INDEX_ID;

    /** solr schema display title key */
    public static final String SOLR_TITLE = RhIndex.DISPLAY_TITLE;

    /** solr schema content key */
    public static final String SOLR_CONTENT = RhIndex.CONTENT;

    /** solr schema abstract key */
    public static final String SOLR_ABSTRACT = RhIndex.DISPLAY_ABSTRACT;

    /** solr schema preview key */
    public static final String SOLR_PREVIEW = RhIndex.DISPLAY_PREVIEW;

    /** */
    public static final String SOLR_RELATIVE = RhIndex.RELATIVE;

    /** */
    public static final String SOLR_KEYWORDS = RhIndex.KEYWORDS;

    /** solr schema owner key */
    public static final String SOLR_OWNER = RhIndex.OWNER;

    /** solr schema department key */
    public static final String SOLR_DEPARTMENT = RhIndex.DEPARTMENT;

    /** solr schema company key */
    public static final String SOLR_COMPANY = RhIndex.COMPANY;

    /** solr schema create time key */
    public static final String SOLR_CREATETIME = RhIndex.CREATE_TIME;

    /** solr schema last modified time key */
    public static final String SOLR_LASTMODIFIED = RhIndex.LAST_MODIFIED;

    /** solr schema service key */
    public static final String SOLR_SERVICE = RhIndex.SERVICE;

    /** solr schema url key */
    public static final String SOLR_URL = RhIndex.URL;

    /** solr schema grantee key */
    public static final String SOLR_GRANTEE = RhIndex.GRANTEE;

    /** solr schema attachment key */
    public static final String SOLR_ATTACHMENT = RhIndex.ATTACHMENT;

    /** solr schema attachment title key */
    public static final String SOLR_ATTACHMENT_TITLE = RhIndex.ATTACHMENT_TITLE;

    /** solr schema attachment id key */
    public static final String SOLR_ATTACHMENT_ID = RhIndex.ATTACHMENT_ID;

    /** solr schema attachment path key */
    public static final String SOLR_ATTACHMENT_PATH = RhIndex.ATTACHMENT_PATH;

    /** solr schema attachment mime type key */
    public static final String SOLR_ATTACHMENT_MTYPE = RhIndex.ATTACHMENT_MTYPE;

    /** attachment id key */
    public static final String ATTACHMENT_ID = "att_id";

    /** attachment index in the list */
    public static final String ATTACHMENT_INDEX = "att_index";

    /** attachment title key */
    public static final String ATTACHMENT_TITLE = "att_title";

    /** attachment title key */
    public static final String ATTACHMENT_CONTENT = "att_content";

    /** attachment path key */
    public static final String ATTACHMENT_PATH = "att_path";

    /** attachment mime type */
    public static final String ATTACHMENT_MTYPE = SOLR_ATTACHMENT_MTYPE;

    /** attachment split tag */
    public static final String ATT_SPLIT_TAG = "<att_split/>";

    /** dynamic string field tag */
    public static final String DYNAMIC_STR_FIELD = RhIndex.DYNAMIC_STR_FIELD;

    /** null value expre */
    private static final String NULL_EXPR = "${N_V}";

    /** 默认search server url配置Key */
    private static final String RHSEARCH_SERVER = "SY_PLUG_SEARCH_SERVER";

    /**  **/
    private Map<String, SolrServer> solrServers = new ConcurrentHashMap<String, SolrServer>();

    /**
     * can not new instance
     */
    public RhSearchClient() {

    }

    /**
     * search query
     * 
     * @param query query params
     * @return response
     * @throws MalformedURLException thrown to indicate that a malformed URL has occurred. Either no legal protocol
     *             could be found in a specification string or the string could not be parsed.
     * @throws SolrServerException Exception to catch all types of communication / parsing issues associated with
     *             talking to search server
     */
    public QueryResponse query(SolrQuery query) throws MalformedURLException, SolrServerException {
        SolrServer solr = this.getServer();
        String source = query.getQuery();
        
        String granteeQuery = getGranteeQuery();
        if ("*:*".equals(source)) {
            if (granteeQuery.length() > 0) {
                String scopes = this.getGranteeScopeQuery();
                granteeQuery = "(" + granteeQuery + ") and (" + scopes + ")"; 
                query.setQuery(granteeQuery);
            }
        } else {
            if (granteeQuery.length() > 0) {
                String scopes = this.getGranteeScopeQuery();
                query.setQuery("(" + source + ") AND (" + granteeQuery + ") AND (" + scopes + ")");
            }
        }
        log.debug("search query:" + query.getQuery());
        return solr.query(query);
    }

    /**
     * seg query text
     * @param text - query text string
     * @return segment list
     * @throws SolrServerException - Exception to catch all types of communication / parsing issues associated with
     *             talking to search server
     * @throws IOException - io erroe
     */
    @SuppressWarnings("unchecked")
    public List<String> querySeg(String text) throws SolrServerException, IOException {
        List<String> segResult = new ArrayList<String>();
        if (0 == text.length()) {
            return segResult;
        }
        SolrServer solr = this.getServer();
        FieldAnalysisRequest req = new FieldAnalysisRequest();
        req.setPath("/analysis/field");
        req.addFieldType("query_text_seg");
        // req.setFieldValue("keywords");
        req.setQuery(text);
        NamedList<Object> result = solr.request(req);
        NamedList<Object> all = (NamedList<Object>) result.get("analysis");
        NamedList<Object> title = (NamedList<Object>) ((NamedList<Object>) all.get("field_types"))
                .get("query_text_seg");
        NamedList<Object> segment = (NamedList<Object>) ((NamedList<Object>) title.get("query"));
        List<Object> segmentList = segment.getAll("org.wltea.analyzer.lucene.IKTokenizer");
        List<Object> words = (List<Object>) segmentList.get(0);
        for (Object word : words) {
            SimpleOrderedMap<String> map = (SimpleOrderedMap<String>) word;
            Object wordObj = map.get("text");
            if (null != wordObj) {
                segResult.add(wordObj.toString());
            }
        }
        return segResult;
    }

    /**
     * add index with message object
     * 
     * @param index message bean contain DISPLAY_TITLE,CONTENT,KEYWORDS
     * @throws IOException io error
     * @throws SolrServerException Exception to catch all types of communication / parsing issues associated with
     *             talking to search server
     */
    public void addIndex(RhIndex index) throws SolrServerException, IOException {
        SolrServer solr = this.getServer(index.getCoreName());
        SolrInputDocument doc = new SolrInputDocument();
        doc = buildInputDocument(index, doc);
        solr.add(doc);
    }

    /**
     * 
     * 软提交，不马上生效，等一段时间之后，或者到达一定条数，才生效。
     * @throws IOException io error
     * @throws SolrServerException Exception to catch all types of communication / parsing issues associated with
     *             talking to search server
     */
    public void commit() throws SolrServerException, IOException {
        SolrServer solr = this.getServer();
        solr.commit(false, false);
    }
    
    /**
     * 强制提交，硬提交，马上生效。
     * @throws IOException io error
     * @throws SolrServerException Exception to catch all types of communication / parsing issues associated with
     *             talking to search server
     */
    public void forceCommit() throws SolrServerException, IOException {
        SolrServer solr = this.getServer();
        solr.commit(true, true);
    }

    /**
     * update index
     * @param index - index message
     * @throws IOException io error
     * @throws SolrServerException Exception to catch all types of communication / parsing issues associated with
     *             talking to search server
     */
    public void updateIndex(RhIndex index) throws SolrServerException, IOException {
        SolrQuery query = new SolrQuery();
        query.setQuery("id:\"" + index.getStr(SOLR_ID) + "\"");
        SolrServer solrServ = getServer();
        QueryResponse response = solrServ.query(query);
        SolrDocumentList list = response.getResults();
        if (0 < list.size()) {
            SolrDocument currentDoc = list.get(0);
            SolrInputDocument newDoc = new SolrInputDocument();
            Map<String, Object> dataMap = currentDoc.getFieldValueMap();
            for (String key : dataMap.keySet()) {
                newDoc.addField(key, dataMap.get(key));
            }
            // update document value
            newDoc = buildInputDocument(index, newDoc);
            solrServ.add(newDoc);
            log.debug("update index...");
            solrServ.commit();
        } else {
            throw new IOException("the data not found, id:" + index.getId());
        }

    }

    /**
     * build solrinput document
     * @param index - rh index object
     * @param doc - source
     * @return solrinputDocument
     */
    private SolrInputDocument buildInputDocument(RhIndex index, SolrInputDocument doc) {
        // index transform
        IndexTransformer transformer = index.getTransformer();
        if (null != transformer) {
            transformer.transform(index);
        }

        String content = index.getContent();
        // set document boost
        if (1 < index.getBoost()) {
            doc.setDocumentBoost(index.getBoost());
        }
        // handle attachment
        handleAttachment(index, doc);

        // handle gongwen index
        String file = index.getFilePath();
        if (null != file && 0 < file.length()) {
            ExtractResult extractrResult = SearchHelper.getFileContent(file);
            if (null != extractrResult) {
                content = extractrResult.getContent();
            }
        }

        // set id
        if (null == doc.getFieldValue(SOLR_ID) || 0 == doc.getFieldValue(SOLR_ID).toString().length()) {
            doc.addField(SOLR_ID, index.getIndexId());
        }

        // set title
        String title = index.getTitle();
        if (null != title && 0 < title.length()) {
            doc.remove(SOLR_TITLE);
            doc.addField(SOLR_TITLE, title, 2f);
        }

        // set content
        if (null != content && 0 < content.length()) {
            doc.remove(SOLR_CONTENT);
            doc.addField(SOLR_CONTENT, content);
        }

        // set owner
        if (null != index.getOwner() && 0 < index.getOwner().length()) {
            doc.remove(SOLR_OWNER);
            doc.addField(SOLR_OWNER, index.getOwner());
        }

        // set deparment
        if (null != index.getDept() && 0 < index.getDept().length()) {
            doc.remove(SOLR_DEPARTMENT);
            doc.addField(SOLR_DEPARTMENT, index.getDept());
        }

        // set create time
        if (null != index.getCreateTime()) {
            doc.remove(SOLR_CREATETIME);
            doc.addField(SOLR_CREATETIME, index.getCreateTime());
        }

        // set modified time
        if (null != index.getLastModified()) {
            doc.remove(SOLR_LASTMODIFIED);
            doc.addField(SOLR_LASTMODIFIED, index.getLastModified());
        }

        // set service id
        if (null != index.getService() && 0 < index.getService().length()) {
            doc.remove(SOLR_SERVICE);
            doc.addField(SOLR_SERVICE, index.getService());
        }

        // set company
        if (null != index.getCmpy() && 0 < index.getCmpy().length()) {
            doc.remove(SOLR_COMPANY);
            doc.addField(SOLR_COMPANY, index.getCmpy());
        }

        // set abstract
        if (null != index.getAbstract() && 0 < index.getAbstract().length()) {
            doc.remove(SOLR_ABSTRACT);
            doc.addField(SOLR_ABSTRACT, index.getAbstract());
        }

        // set url
        if (null != index.getUrl() && 0 < index.getUrl().length()) {
            doc.remove(SOLR_URL);
            doc.addField(SOLR_URL, index.getUrl());
        }

        // set preview
        if (null != index.getPreview() && 0 < index.getPreview().length()) {
            doc.remove(SOLR_PREVIEW);
            doc.addField(SOLR_PREVIEW, index.getPreview().replace("${content}", index.getContent()));
        }

        // add all dynamic fields
        Map<String, Object> dynamicFields = index.getFields();
        if (null != dynamicFields) {
            Set<String> fieldKeys = dynamicFields.keySet();
            for (String field : fieldKeys) {
                doc.addField(field, dynamicFields.get(field));
            }
        }

        // ------------------------------multivalued

        // add keywords
        List<String> keywords = index.getKeywords();
        if (index.isOverwriteModel() && 0 < keywords.size()) {
            doc.removeField(SOLR_KEYWORDS);
        }
        for (String keyword : keywords) {
            doc.addField(SOLR_KEYWORDS, keyword, 3f);
        }

        // add grantee
        List<String> granList = index.getGranteeList();
        if (index.isOverwriteModel() && 0 < granList.size()) {
            doc.removeField(SOLR_GRANTEE);
        }
        for (String grantee : granList) {
            doc.addField(SOLR_GRANTEE, grantee);
        }
        
        List<String> grantScopeList = index.getGranteeScope();
        if (index.isOverwriteModel() && 0 < grantScopeList.size()) {
            doc.removeField(RhIndex.GRANTEE_SCOPE);
        }
        for (String grantee : grantScopeList) {
            doc.addField(RhIndex.GRANTEE_SCOPE, grantee);
        }
        
        
        //增加扩展索引数据
        List<String> extendIdxs = index.getExtendIndex();
        if (index.isOverwriteModel() && 0 < extendIdxs.size()) {
            doc.removeField(RhIndex.EXTEND_INDEX);
        }
        for (String idxVal : extendIdxs) {
            doc.addField(RhIndex.EXTEND_INDEX, idxVal, 1.5f);
        }

        // add relative
        List<String> relatives = index.getRelatives();
        if (index.isOverwriteModel() && 0 < relatives.size()) {
            doc.removeField(SOLR_RELATIVE);
        }
        for (String relative : relatives) {
            doc.addField(SOLR_RELATIVE, relative);
        }

        return doc;
    }

    /**
     * handle attachment of message <BR>
     * add attachment field to message dynamic field and add attachment's content to doc object
     * @param message index message
     * @param doc SolrDocument
     */
    private void handleAttachment(RhIndex message, SolrInputDocument doc) {
        List<IndexAttachment> attList = message.getAttList();

        if (null == attList) {
            return;
        }

        if (message.isOverwriteModel() && 0 < attList.size()) {
            doc.removeField(SOLR_ATTACHMENT_ID);
            doc.removeField(SOLR_ATTACHMENT_PATH);
            doc.removeField(SOLR_ATTACHMENT_TITLE);
            doc.removeField(SOLR_ATTACHMENT_MTYPE);
            doc.removeField(SOLR_ATTACHMENT);
        }

        for (int i = 0; i < attList.size(); i++) {
            IndexAttachment att = attList.get(i);
            
            final String attPath = att.getIndexPath();
            // 设置文件ID
            addMulitValue(doc, SOLR_ATTACHMENT_ID, att.getId());
            
            //设置文件访问路径
            if (att.getAccessPath() != null && att.getAccessPath().length() > 0) {
                addMulitValue(doc, SOLR_ATTACHMENT_PATH, att.getAccessPath());
            } else {
                addMulitValue(doc, SOLR_ATTACHMENT_PATH, att.getIndexPath());
            }
            
            //设置文件标题
            addMulitValue(doc, SOLR_ATTACHMENT_TITLE, att.getTitle());
            
            //提取文件内容中的文字信息
            ExtractResult extracted = SearchHelper.getFileContent(attPath);
            StringBuilder attBuilder = new StringBuilder();
            if (null != extracted) {
                attBuilder.append(extracted.getContent());
            }
            addMulitValue(doc, SOLR_ATTACHMENT, attBuilder.toString());
            
            //取得文件扩展名
            String attMtype = "";
            if (att.getExtension() != null && att.getExtension().length() > 0) {
                attMtype = att.getExtension();
            } else if (extracted != null) {
                attMtype = extracted.getMType();
            }
            
            //设置文件扩展名
            addMulitValue(doc, SOLR_ATTACHMENT_MTYPE, attMtype);
        }
    }

    /**
     * add mulit-value for document, if the value is null, we well add default null value.
     * @param doc - SolrInputDocument
     * @param key - field key
     * @param value - field value
     */
    private void addMulitValue(SolrInputDocument doc, String key, String value) {
        if (null == value || 0 == value.length()) {
            value = NULL_EXPR;
        }
        doc.addField(key, value);
    }

    /**
     * delete index by id
     * 
     * @param indexMsg contain rrn (Ruaho Resource Name)
     * 
     *            TODO: rrn: rws:<vendor>:<region>:<namespace>:<relative-id> <br>
     *            • vendor identifies the YWS product(forexample, objectstorage, category) <br>
     *            • region is the Region the resource resides in (for example,us-east-1), if any <br>
     *            • namespace is the YWS account ID <br>
     *            • relative-id is the portion that identifies the specific resource <br>
     * @throws IOException io error
     * @throws SolrServerException Exception to catch all types of communication / parsing issues associated with
     *             talking to search server
     */
    public void delIndex(RhIndex indexMsg) throws SolrServerException, IOException {
        String id = indexMsg.getIndexId();
        SolrServer solr = this.getServer();
        solr.deleteById(id);
        log.debug("delete index...");
        solr.commit();
    }

    /**
     * delete index by query
     * @param query - query str
     * @throws IOException io error
     * @throws SolrServerException Exception to catch all types of communication / parsing issues associated with
     *             talking to search server
     */
    public void delIndexByQuery(String query) throws SolrServerException, IOException {
        SolrServer solr = this.getServer();
        solr.deleteByQuery(query);
        log.info("delete index by query..." + query);
        // test code. TODO code optimization
        solr.commit();
    }

    /**
     * delete index by service
     * @param service - servId
     * @throws IOException io error
     * @throws SolrServerException Exception to catch all types of communication / parsing issues associated with
     *             talking to search server
     */
    public void delIndex(String service) throws SolrServerException, IOException {
        String query = "service:" + service;
        delIndexByQuery(query);
    }

    /**
     * get solr server
     * 
     * @return <CODE>SolrServer</CODE>
     * @throws MalformedURLException <CODE>MalformedURLException</CODE>
     */
    private SolrServer getServer() throws MalformedURLException {
        return this.getServer(null);
    }

    /**
     * 
     * @param coreName 索引库名称
     * @return SolrServer 实例
     * @throws MalformedURLException <CODE>MalformedURLException</CODE>
     */
    private SolrServer getServer(String coreName) throws MalformedURLException {
        String cName = coreName;
        if (cName == null) {
            cName = "";
        }

        SolrServer solr = this.solrServers.get(cName);
        if (solr == null) {
            solr = new HttpSolrServer(getServerUri(coreName));
            this.solrServers.put(cName, solr);
        }
        return solr;
    }

    /**
     * get grantee query filter string
     * @return query string
     */
    private String getGranteeQuery() {
        if (!GRANTEE_FILTER_ENABLE) {
            return "";
        }

        List<String> roles = new ArrayList<String>();
        List<String> depts = new ArrayList<String>();
        String currentCmpy = "";
        String currentUser = "";

        // set current user data
        UserBean userBean = Context.getUserBean();
        if (null != userBean) {
            currentUser = userBean.getCode();
            currentCmpy = userBean.getCmpyCode();
            depts = userBean.getDeptCodeList();
            roles = userBean.getRoleCodeList();
        }

        // build granee string
        StringBuilder grantBuilder = new StringBuilder();
        String splitTag = " OR ";
        // merge roles
        for (int i = 0; i < roles.size(); i++) {
            String role = roles.get(i);
            grantBuilder.append(" grantee:");
            grantBuilder.append(SearchHelper.getRoleGranteeExpr(role));
            grantBuilder.append(splitTag);
        }
        // merge depts
        for (int i = 0; i < depts.size(); i++) {
            String dept = depts.get(i);
            grantBuilder.append(" grantee:");
            grantBuilder.append("\"" + SearchHelper.getDeptGranteeExpr(dept) + "\"");
            grantBuilder.append(splitTag);
        }

        // TODO 跨公司
        // merge mulit grantee (dept&role)
        for (String dept : depts) {
            for (String role : roles) {
                String muliGrant = SearchHelper.getMulitGranteeExpr("", dept, role);
                grantBuilder.append(" grantee:");
                grantBuilder.append("\"" + muliGrant + "\"");
                grantBuilder.append(splitTag);
            }
        }

        // merge cmpy
        if (currentCmpy != null && currentCmpy.length() > 0) {
            grantBuilder.append(" grantee:");
            grantBuilder.append(SearchHelper.getCmpyGranteeExpr(currentCmpy));
            grantBuilder.append(" OR ");
        }

        // merge user
        if (currentUser != null && currentUser.length() > 0) {
            grantBuilder.append(" grantee:");
            grantBuilder.append(SearchHelper.getUserGranteeExpr(currentUser));
            grantBuilder.append(" OR ");
        }

        // merge group
        List<String> groups = userBean.getGroupCodeList();
        for (int i = 0; i < groups.size(); i++) {
            String group = groups.get(i);
            grantBuilder.append(" grantee:");
            grantBuilder.append("\"" + SearchHelper.getGroupGranteeExpr(group) + "\"");
            grantBuilder.append(splitTag);
        }

        grantBuilder.append(" grantee:");
        grantBuilder.append(SearchHelper.ALL_USERS_GRANTEE);

        if (grantBuilder.toString().endsWith(splitTag)) {
            // delete the last split tag
            return grantBuilder.substring(0, (grantBuilder.length() - splitTag.length()));
        } else {
            return grantBuilder.toString();
        }
    }
    
    /**
     * 
     * @return 返回授权范围
     */
    private String getGranteeScopeQuery() {
        StringBuilder query = new StringBuilder();
        
        UserBean userBean = Context.getUserBean();
        
        if (userBean == null) {
            query.append(RhIndex.GRANTEE_SCOPE).append(":").append("ALL");
        } else {
            List<String> scopes = userBean.getAclScopes();
            for (int i = 0; i < scopes.size(); i++) {
                if (i > 0) {
                    query.append(" OR ");
                }
                String scope = scopes.get(i);
                query.append(RhIndex.GRANTEE_SCOPE).append(":").append(scope);
            }
        }
        
        return query.toString();
    }

    /**
     * 
     * @param coreName 指定索引库名称
     * @return 取得服务器访问URL
     */
    public String getServerUri(String coreName) {
        String result = Context.getSyConf(RHSEARCH_SERVER, "http://staff.zotn.com:8888/searchserver/rhsearch");

        if (!result.endsWith("/")) {
            result += "/";
        }

        if (StringUtils.isNotEmpty(coreName)) {
            int pos = result.lastIndexOf("/", result.length() - 2);
            result = result.substring(0, pos + 1) + coreName + "/";
        }

        return result;
    }

    /**
     * get search server uri
     * 
     * @return server uri
     */
    public String getServerUri() {
        String result = "";
        result = Context.getSyConf(RHSEARCH_SERVER, "http://staff.zotn.com:8888/searchserver/rhsearch");
        if (!result.endsWith("/")) {
            result += "/";
        }
        return result;
    }

    /**
     * 测试方法
     * @param args 入参
     * @throws SolrServerException 例外
     * @throws IOException 例外
     */
    public static void main(String[] args) throws SolrServerException, IOException {
        String[] delServ = { "GW_FW", "CM_CALENDAR", "TBL_MSV_FAWEN", "LW_CONTRACT", "TBL_MSV_SHOUWEN",
                "TBL_MSV_QIANBAO", "CM_KM", "LW_AUTH_FILE", "TBL_MSV_JIYAO", "TBL_MSV_CUIBAN", "TBL_MSV_ITFWD",
                "NS_NEWS", "GW_FW" };
        RhSearchClient rsc = new RhSearchClient();
        for (String serv : delServ) {
            rsc.delIndex(serv);
        }
    }

}
