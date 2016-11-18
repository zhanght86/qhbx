/*
 * Copyright (c) 2012 Ruaho All rights reserved.
 */
package com.rh.core.plug.search;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.StopWatch;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.FacetField;
import org.apache.solr.client.solrj.response.FacetField.Count;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;

import com.rh.core.base.Bean;
import com.rh.core.base.BeanUtils;
import com.rh.core.base.TipException;
import com.rh.core.comm.zhidao.ZhidaoServ;
import com.rh.core.plug.search.RhIndex.RELATIVE_TYPE;
import com.rh.core.plug.search.client.RhFileClient;
import com.rh.core.plug.search.client.RhSearchClient;
import com.rh.core.serv.CommonServ;
import com.rh.core.serv.OutBean;
import com.rh.core.serv.ParamBean;
import com.rh.core.serv.ServDefBean;
import com.rh.core.serv.ServMgr;
import com.rh.core.serv.ServUtils;
import com.rh.core.serv.dict.DictMgr;
import com.rh.core.util.Constant;
import com.rh.core.util.DateUtils;

/**
 * search service extends <CODE>CommonServ</CODE>
 * 
 */
public class SearchServ extends CommonServ {
    
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
    public static final String ATTACHMENT_MTYPE = RhIndex.ATTACHMENT_MTYPE;    

    public static final String HL_PRE_TAG = "<cite>";

    public static final String HL_POST_TAG = "</cite>";

    /** log */
    private static Log log = LogFactory.getLog(SearchServ.class);

    /**
     * 搜索入口页面
     * @param paramBean 传入的参数
     * @return 传出的参数
     */
    public OutBean show(ParamBean paramBean) {
        OutBean outBean = new OutBean();
        outBean.setToDispatcher("/sy/plug/search/searchIndex.jsp");
        return outBean;
    }

    /**
     * 提供基于列表的查询服务
     * 
     * @param paramBean 参数Bean
     * @return 查询结果
     */
    @SuppressWarnings("unchecked")
    public OutBean query(ParamBean paramBean) {
        StopWatch sw = new StopWatch();
        sw.start();
        ServDefBean servDef = ServUtils.getServDef(paramBean.getServId());
        Bean page = paramBean.getQueryPage();
        if (!page.contains("SHOWNUM")) { // 初始化每页记录数设定
            if (paramBean.getQueryNoPageFlag()) { // 设置了不分页处理
                page.set("SHOWNUM", paramBean.getShowNum()); // 从参数中获取需要取多少条记录，如果没有则取所有记录
            } else {
                page.set("SHOWNUM", servDef.getPageCount(10));
            }
        }
        if (!page.contains("NOWPAGE")) { // 初始化当前页数设定
            page.set("NOWPAGE", 1);
        }
        // get query string
        String sourceQuery = "*:*";
        String keyWords = paramBean.getStr("KEYWORDS");
        keyWords = SearchHelper.keywordsFilter(keyWords);
        if (0 < keyWords.length()) {
            sourceQuery = buildeCommQuery(keyWords);
        }
        if (paramBean.contains("QUERY") && 0 < paramBean.getStr("QUERY").length()) {
            sourceQuery = paramBean.getStr("QUERY");
        }
        // selected category list (level2)
        List<Bean> selectedCatList = new ArrayList<Bean>();
        if (paramBean.contains("SELECTED_CATS")) {
            selectedCatList = (List<Bean>) paramBean.get("SELECTED_CATS");
        }
        String queryStr = "(" + sourceQuery + ")";
        // append selected category filter (level2)
        for (Bean seleCat : selectedCatList) {
            String filter = seleCat.getStr("query");
            if (filter.length() > 0) {
                //为查询关键字前后增加双引号，避免特殊字符影响搜索结果
                int pos = filter.indexOf(":");
                if (pos > 0) {
                    String fieldName = filter.substring(0, pos).trim();
                    if (pos < filter.length()) {
                        String fieldValue = filter.substring(pos + 1).trim();
                        queryStr = queryStr + " AND " + fieldName + ":\"" + fieldValue + "\"";
                    }
                }
            }
        }

        // parse sort param
        String sort = paramBean.getStr("SORT");

        // parse time param
        Date st = null;
        Date et = null;
        String startTime = paramBean.getStr("STARTTIME");
        String endTime = paramBean.getStr("ENDTIME");
        if (null != startTime && 0 < startTime.length()) {
            st = DateUtils.getDateFromString(startTime + " 00:00:00", "yyyy-MM-dd HH:mm:ss");
        }
        if (null != endTime && 0 < endTime.length()) {
            et = DateUtils.getDateFromString(endTime + " 23:59:60", "yyyy-MM-dd HH:mm:ss");
        }
        // append time filter
        try {
            if (st != null && et != null) {
                queryStr += " AND last_modified:[" + DateUtils.getUTCTime(st) + " TO " + DateUtils.getUTCTime(et) + "]";
            }
        } catch (IOException ioe) {
//            ioe.printStackTrace();
        }

        // selected filter list (level1)
        List<Bean> filterList = new ArrayList<Bean>();
        if (paramBean.contains("FILTER")) {
            filterList = (List<Bean>) paramBean.get("FILTER");
        }

        // append selected filter (level1)
        for (Bean filterBean : filterList) {
            if (filterBean.getStr("id").startsWith("date")) {
                continue;
            } else if (filterBean.getStr("id").startsWith("service")) {
                continue;
            } else if (filterBean.getStr("id").startsWith("company")) {
                continue;
            } else {
                queryStr += " AND " + filterBean.getStr("id") + ":" + filterBean.getStr("data");
            }
        }

        // /////////////////suport multi service/company selected ////////////////////////////
        List<String> servFilter = new ArrayList<String>();
        List<String> cmpyFilter = new ArrayList<String>();
        for (Bean filterBean : filterList) {
            if (filterBean.getStr("id").equals("service")) {
                servFilter.add(filterBean.getStr("data"));
            } else if (filterBean.getStr("id").equals("company")) {
                cmpyFilter.add(filterBean.getStr("data"));
            }
        }
        String servFilterStr = "";
        for (String serv : servFilter) {
            servFilterStr += " service:" + serv + " OR";
        }
        if (servFilterStr.endsWith("OR")) {
            servFilterStr = servFilterStr.substring(0, servFilterStr.length() - " OR".length());
            servFilterStr = "(" + servFilterStr + ")";
        }
        if (servFilterStr.length() > 0) {
            queryStr += " AND " + servFilterStr;
        }

        String cmpyFilterStr = "";
        for (String cmpy : cmpyFilter) {
            cmpyFilterStr += " company:" + cmpy + " OR";
        }
        if (cmpyFilterStr.endsWith("OR")) {
            cmpyFilterStr = cmpyFilterStr.substring(0, cmpyFilterStr.length() - " OR".length());
            cmpyFilterStr = "(" + cmpyFilterStr + ")";
        }

        if (cmpyFilterStr.length() > 0) {
            queryStr += " AND " + cmpyFilterStr;
        }

        // /////////////////END ////////////////////////////

        boolean isMobile = paramBean.contains("MBFLAG");
        
        log.warn("----------P1---" + sw.getTime());
        
        
        // query
        OutBean resultBean = query(queryStr, keyWords, page, sort, isMobile);

        if (isMobile) {
            return resultBean;
        } else {
            resultBean.set("SELECTED_CATS", selectedCatList);
            resultBean.set("SEARCH_QUERY", sourceQuery);
            resultBean.set("SEARCH_STARTTIME", startTime);
            resultBean.set("SEARCH_ENDTIME", endTime);
            resultBean.set("SEARCH_FILTER", filterList);
            log.warn("----------P1 end---" + sw.getTime());
            resultBean.setToDispatcher("/sy/plug/search/searchResult.jsp");
            return resultBean;
        }
        
        
    }

    /**
     * 提供基于facet的分组功能
     * 
     * @param paramBean 参数Bean
     * @return 查询结果
     */
    @SuppressWarnings("unchecked")
    public Bean groupBy(Bean paramBean) {
        String queryStr = "*:*";
        // facet field
        String groupBy = paramBean.getStr("GROUP_BY");
        if (paramBean.contains("QUERY")) {
            queryStr = paramBean.getStr("QUERY");
        }

        // selected category list
        List<Bean> selectedCatList = new ArrayList<Bean>();
        if (paramBean.contains("SELECTED_CATS")) {
            selectedCatList = (List<Bean>) paramBean.get("SELECTED_CATS");
        }
        if (selectedCatList.size() > 0) {
            queryStr = "(" + queryStr + ")";
        }
        // append selected category filter
        for (Bean seleCat : selectedCatList) {
            String filter = seleCat.getStr("query");
            if (filter.length() > 0) {
                queryStr = queryStr + " AND " + filter;
            }
        }

        // field type
        // String type = paramBean.getStr("GROUP_TYPE");

        SolrQuery groupQuery = new SolrQuery();
        // groupQuery.setQuery("title:"+keyWords);
        groupQuery.setQuery(queryStr);
        groupQuery.setStart(0);
        groupQuery.setRows(0);
        groupQuery.setFacet(true);
        // groupQuery.setFacetLimit(3);
        groupQuery.addFacetField(groupBy);
        QueryResponse groupResponse = null;
        try {
            RhSearchClient rsc = new RhSearchClient();
            groupResponse = rsc.query(groupQuery);
        } catch (Exception e) {
            log.warn(e.getMessage(), e);
        }
        FacetField groupResult = groupResponse.getFacetField(groupBy);
        List<Bean> dataList = new ArrayList<Bean>();
        List<Count> groupValue = groupResult.getValues();
        if (null != groupValue) {
            for (int i = 0; i < groupValue.size(); i++) {
                Count count = groupValue.get(i);
                String name = count.getName();
                long value = count.getCount();
                if (name.length() > 0 && value > 0) {
                    dataList.add(new Bean().set("NAME", name).set("COUNT", value));
                }
            }
        }
        // merge number facets
        if (groupBy.endsWith("_numfield") && 0 < dataList.size()) {
            dataList = mergeNumGroup(dataList);
        }

        Bean outBean = new Bean();
        outBean.set(Constant.RTN_DATA, dataList);
        return outBean;

    }

    /**
     * get data by id
     * @param paramBean param
     * @return Bean out Bean
     */
    public Bean preview(Bean paramBean) {
        String id = paramBean.getId();
        String previewField = paramBean.getStr("preview_field");
        String queryStr = "id:\"" + id + "\"";
        String keyWords = paramBean.getStr("KEYWORDS");
        keyWords = SearchHelper.keywordsFilter(keyWords);
        if (keyWords.length() > 0) {
            queryStr += " AND (" + buildeCommQuery(keyWords) + ")";
        }
        SolrQuery query = new SolrQuery();
        query.setQuery(queryStr);
        // query.setHighlight(true).setHighlightSnippets(1000); // set other params as
        // query.setParam("hl.simple.pre", HL_PRE_TAG);
        // query.setParam("hl.simple.post", HL_POST_TAG);
        // query.setParam("hl.mergeContiguous", "true");
        // query.setParam("hl.fl", RhSearchClient.SOLR_TITLE + "," + RhSearchClient.SOLR_CONTENT + ","
        // + RhSearchClient.SOLR_ATTACHMENT);
        QueryResponse response = null;
        try {
            RhSearchClient rsc = new RhSearchClient();
            response = rsc.query(query);
        } catch (MalformedURLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (SolrServerException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        List<Bean> dataList = new ArrayList<Bean>();
        SolrDocumentList list = response.getResults();
        // String service = "";
        if (0 < list.size()) {
            SolrDocument resultDoc = list.get(0);
            Bean data = new Bean();
            String title = resultDoc.getFieldValue(RhSearchClient.SOLR_TITLE).toString();
            // service = resultDoc.getFieldValue(RhSearchClient.SOLR_SERVICE).toString();
            String content = "";
            // preview attachment
            if (previewField.startsWith(RhSearchClient.SOLR_ATTACHMENT)) {
                Collection<Object> attachmentList = resultDoc.getFieldValues(RhSearchClient.SOLR_ATTACHMENT);
                Collection<Object> attachmentTitles = resultDoc.getFieldValues(RhSearchClient.SOLR_ATTACHMENT_TITLE);

                try {
                    String indexStr = previewField.substring(RhSearchClient.SOLR_ATTACHMENT.length());
                    int index = Integer.valueOf(indexStr);

                    if (null != attachmentTitles && index < attachmentTitles.size()) {
                        title = attachmentTitles.toArray()[index].toString();
                    }

                    content = attachmentList.toArray()[index].toString();
                } catch (Exception e) {
                    log.warn(" perview faild. the data not found, perviewField:" + previewField + " ," + " doc_id:"
                            + id, e);
                    e.printStackTrace();
                }

            } else {
                if (null != resultDoc.getFieldValue(RhSearchClient.SOLR_CONTENT)) {
                    content = resultDoc.getFieldValue(RhSearchClient.SOLR_CONTENT).toString();
                }
                title = resultDoc.getFieldValue(RhSearchClient.SOLR_TITLE).toString();
            }
            content = content.replace("\n", "<br>");

            // set hightlight
            // if (null != response.getHighlighting() && response.getHighlighting().get(id) != null) {
            // Map<String, List<String>> highlightMap = response.getHighlighting().get(id);
            // // handle highlight
            // handleHighlight(highlightMap, data);
            // // preview attachement
            // if (previewField.startsWith(RhSearchClient.SOLR_ATTACHMENT)) {
            // List<Bean> attList = (List<Bean>) data.get((RhSearchClient.SOLR_ATTACHMENT));
            // String targetIndex = previewField.substring(RhSearchClient.SOLR_ATTACHMENT.length());
            // // find target attachment
            // for (Bean attBean : attList) {
            // if (targetIndex.equals(attBean.getStr(RhSearchClient.ATTACHMENT_INDEX))) {
            // String attTitle = attBean.getStr(RhSearchClient.ATTACHMENT_TITLE);
            // String attCont = attBean.getStr(RhSearchClient.ATTACHMENT_CONTENT);
            // if (attTitle.length() > 0) {
            // title = attTitle;
            // }
            // attCont = attCont.replace("\n", "<br>");
            // if (attCont.length() > 0) {
            // content = attCont;
            // }
            // }
            //
            // }
            // } else {
            // // set highlight content
            // List<String> hlTitle = response.getHighlighting().get(id).get(RhSearchClient.SOLR_TITLE);
            // if (null != hlTitle && 0 < hlTitle.size()) {
            // title = hlListToStr(hlTitle);
            // }
            // List<String> hlContent = response.getHighlighting().get(id).get(RhSearchClient.SOLR_CONTENT);
            // if (null != hlContent && 0 < hlContent.size()) {
            // content = hlListToStr(hlContent);
            // }
            // }
            //
            // }

            // first use preview field
            if (!previewField.startsWith(RhSearchClient.SOLR_ATTACHMENT)) {

                String solrId = resultDoc.getFieldValue(RhSearchClient.SOLR_ID).toString();
                String[] tags = solrId.split(",");
                String servId = "unknow";
                String dataId = "unknow";
                if (tags.length > 1) {
                    servId = tags[0];
                    dataId = tags[1];
                }

                // 获取当前服务配置
                Bean searchServ = null;
                try {
                    searchServ = ServMgr.act(ServMgr.SY_SERV_SEARCH, ServMgr.ACT_BYID, new ParamBean().setId(servId));
                } catch (Exception e) {
                    log.warn("get search def error", e);
                }
                String previewDef = "";
                if (null != searchServ) {
                    previewDef = searchServ.getStr("SEARCH_PREVIEW");
                }
                if (0 < previewDef.length()) {
                    Bean dataBean = getData(servId, dataId);
                    // 替换字段变量
                    String preview = ServUtils.replaceValues(previewDef, servId, dataBean);

                    // Object preview = resultDoc.getFieldValue(RhSearchClient.SOLR_PREVIEW);
                    if (null != preview && 0 < preview.toString().length()
                            && -1 == preview.toString().indexOf("src=''")) {
                        content = preview.toString();
                    }
                }
            }

            data.set(RhSearchClient.SOLR_TITLE, title);
            String fileServer = RhFileClient.getInstance().getServerUri();
            content = content.replace(RhFileClient.FILE_SERVER_EXPR, fileServer);
            data.set(RhSearchClient.SOLR_CONTENT, content);

            // get kewords seg
            List<String> wordList = null;
            try {
                RhSearchClient rsc = new RhSearchClient();
                wordList = rsc.querySeg(keyWords);
            } catch (SolrServerException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            // hl
            highlight(data, wordList);

            dataList.add(data);
        }
        Bean outBean = new Bean();
        outBean.set(Constant.RTN_DATA, dataList);
        return outBean;
    }

    /**
     * delete data by id
     * @param paramBean param
     * @return Bean out Bean
     */
    public Bean delete(Bean paramBean) {
        String id = paramBean.getId().trim();
        if (null == id || 0 == id.length()) {
            throw new TipException("id can not be null");
        }
        // String queryStr = "id:\"" + id + "\"";
        try {
            RhSearchClient rsc = new RhSearchClient();
            rsc.delIndex(new RhIndex(id));
        } catch (SolrServerException e) {
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return paramBean;
    }

    /**
     * get relevant Search
     * @param paramBean param
     * @return Bean out Bean
     */
    public Bean relevantSearch(Bean paramBean) {
        String queryStr = paramBean.getStr("QUERY");
        String dataServ = paramBean.getStr("SERVICE");
        ServDefBean servDef = ServUtils.getServDef(dataServ);
        List<Bean> dataList = new ArrayList<Bean>();
        String[] relativeList = queryStr.split("_@_&_");
        for (String relativeQuery : relativeList) {
            // query form search server
            if (relativeQuery.startsWith(RELATIVE_TYPE.SY_PLUG_SEARCH_SERV.toString())) {
                String solrQueryStr = relativeQuery.substring((RELATIVE_TYPE.SY_PLUG_SEARCH_SERV.toString() + "://")
                        .length());
                SolrQuery query = new SolrQuery();
                query.setQuery(solrQueryStr);
                query.setFields("id,service,title,url");
                query.setRows(3);
                QueryResponse response = null;
                try {
                    RhSearchClient rsc = new RhSearchClient();
                    response = rsc.query(query);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (SolrServerException e) {
                    e.printStackTrace();
                }

                // parse search result
                SolrDocumentList solrDocumentList = response.getResults();
                List<Bean> searchResult = new ArrayList<Bean>();
                Iterator<SolrDocument> iter = solrDocumentList.iterator();
                while (iter.hasNext()) {
                    SolrDocument resultDoc = iter.next();
                    Collection<String> fieldsName = resultDoc.getFieldNames();
                    Bean data = new Bean();
                    for (String field : fieldsName) {
                        Object value = resultDoc.getFieldValue(field);
                        data.set(field, value);
                    }
                    String docId = data.getStr("id");
                    String id = "";
                    String[] tags = docId.split(",");
                    if (tags.length > 1) {
                        id = tags[1];
                    }

                    data.set("url", "internal:" + data.getStr("service") + ":" + id);
                    searchResult.add(data);
                }
                if (searchResult.size() > 0) {
                    String keywords = paramBean.getStr("KEYWORDS");
                    keywords = SearchHelper.keywordsFilter(keywords);
                    Bean more = new Bean();
                    more.set("title", "更多搜索相关数据");
                    more.set("url", "/SY_PLUG_SEARCH.query.do?data={'QUERY':'" + solrQueryStr + "'}");
                    more.set("type", "more");
                    searchResult.add(more);
                }
                dataList.addAll(searchResult);
            } else {
                String tag = "://";
                int index = relativeQuery.indexOf(tag);
                String service = relativeQuery.substring(0, index);
                String serviceQuery = relativeQuery.substring(index + "://".length());
                Bean linkBean = ServUtils.getSearchLinkDef(servDef, service);
                
                int rows = linkBean.get("LINK_COUNT", 3);
                ParamBean param = new ParamBean(service, ServMgr.ACT_FINDS)
                        .setShowNum(rows).setSelect(BeanUtils.getFieldCodes(linkBean.getStr("LINK_TITLE")))
                        .setWhere(serviceQuery);
                List<Bean> list = ServMgr.act(param).getDataList();
                List<Bean> serviceResult = new ArrayList<Bean>();
                for (Bean bean : list) {
                    String title = ServUtils.replaceValues(linkBean.getStr("LINK_TITLE"), service, bean);
                    String id = bean.getId();
                    serviceResult.add(new Bean().set("title", title).set("service", service)
                            .set("url", "inernal:" + service + ":" + id));
                }
                if (serviceResult.size() >= rows) {
                    String servName = DictMgr.getName("SY_SERV", service);
                    Bean more = new Bean();
                    more.set("title", "更多" + servName + "相关数据");
                    more.set(
                            "url",
                            "javascript:var opts={'url':'" + service + ".list.do?','extWhere':'"
                                    + serviceQuery.replace("'", "\\'") + "','tTitle':'" + "更多" + servName + "相关数据"
                                    + "','menuFlag':3};openNewTab(opts);");
                    // more.set("url", "/core/view/stdListView.jsp?frameId=" + service + "-tabsIframe&extWhere="
                    // + serviceQuery + "&sId=" + service + "&paramsFlag=true");
                    more.set("type", "more");
                    serviceResult.add(more);
                }
                dataList.addAll(serviceResult);

            }
        }

        Bean outBean = new Bean();
        outBean.set(Constant.RTN_DATA, dataList);
        return outBean;
    }

    /**
     * is contains object?
     * @param list list
     * @param filterName target bean's id
     * @return is contains?
     */
    public static boolean contains(List<Bean> list, String filterName) {
        boolean result = false;
        for (Bean bean : list) {
            if (bean.getStr("id").equals(filterName)) {
                result = true;
                break;
            }
        }
        return result;
    }

    /**
     * is contains date object?
     * @param list list
     * @param datefilter date filter
     * @return is contains date?
     */
    public static boolean containsDateFilter(List<Bean> list, String datefilter) {
        boolean result = false;
        for (Bean bean : list) {
            if (bean.getStr("id").equals("date")) {
                result = bean.getStr("data").equals(datefilter);
                break;
            }
        }
        return result;
    }

    /**
     * search by query string
     * 
     * @param queryStr query string [Solr Query Syntax] @see http://wiki.apache.org/solr/SolrQuerySyntax <br>
     * @param displayKeywords display keywords
     * @param page page bean
     * @param sort sort string example: CREATE_TIME ASC (options)
     * @param isMobile - is Mobile clien
     * @return out Bean
     */
    private OutBean query(String queryStr, String displayKeywords, Bean page, String sort, boolean isMobile) {
        StopWatch sw = new StopWatch();
        sw.start();
        SolrQuery query = new SolrQuery();
        query.setQuery(queryStr);
        sort = sort.replace("+", " ");
        query.setParam("sort", sort);
        // set hight light param // needed
        query.setHighlight(true); // set other params as
        query.setParam("hl.fl", RhSearchClient.SOLR_TITLE + "," + RhSearchClient.SOLR_CONTENT + ","
        + RhSearchClient.SOLR_ATTACHMENT);
        query.setParam("hl.simple.pre", HL_PRE_TAG);
        query.setParam("hl.simple.post", HL_POST_TAG);
        query.setParam("hl.preserveMulti", "true");
        query.setParam("hl.fragsize", "100");

        query.setFields("abstract,attachment_mtype,attachment_title,attachment_path,company,"
                + "content,deparment,id,service,title,url,relative,*_strfield,file_path_strfield");
        int rows = page.get(Constant.SHOWNUM, 10);
        query.setStart((page.get(Constant.NOWPAGE, 1) - 1) * rows);
        query.setRows(rows);
        QueryResponse response = null;
        RhSearchClient rsc = new RhSearchClient();
        try {
            response = rsc.query(query);
        } catch (Exception e) {
            throw new RuntimeException("server connection error.", e);
        }
        // result wrap
        long count = response.getResults().getNumFound();
        long pages = count / page.getInt(Constant.SHOWNUM);
        if (0 < count % page.getInt(Constant.SHOWNUM)) {
            pages++;
        }
        page.set(Constant.PAGES, pages);
        page.set(Constant.ALLNUM, count);
        

        // parse result
        SolrDocumentList solrDocumentList = response.getResults();
        Iterator<SolrDocument> iter = solrDocumentList.iterator();
        String fileServer = RhFileClient.getInstance().getServerUri();
        List<Bean> dataList = new ArrayList<Bean>();
        // get kewords seg
//        List<String> wordList = new ArrayList<String>();
//        try {
//            wordList = rsc.querySeg(displayKeywords);
//        } catch (Exception e) {
//            log.warn(e.getMessage(), e);
//        }
        
        log.warn("before while1:" + sw.getTime());
        
        while (iter.hasNext()) {
            SolrDocument resultDoc = iter.next();
            Collection<String> fieldsName = resultDoc.getFieldNames();
            Bean data = new Bean();
            for (String field : fieldsName) {
                Object value = resultDoc.getFieldValue(field);
                if (field.endsWith(RhIndex.DYNAMIC_STR_FIELD)) {
                    field = field.substring(0, field.length() - RhIndex.DYNAMIC_STR_FIELD.length());
                } else if (field.endsWith(RhIndex.DYNAMIC_NUM_FIELD)) {
                    field = field.substring(0, field.length() - RhIndex.DYNAMIC_NUM_FIELD.length());
                } else if (field.endsWith(RhIndex.DYNAMIC_DATE_FIELD)) {
                    field = field.substring(0, field.length() - RhIndex.DYNAMIC_DATE_FIELD.length());
                }
                
                data.set(field, value);
            }

            String id = data.getStr(RhSearchClient.SOLR_ID);
            
            Map<String, List<String>> highlightMap = response.getHighlighting().get(id);
            if (highlightMap != null) {
                serverHandleHighlight(highlightMap, data);
            } 
            
            String[] tags = id.split(",");
            String servId = "unknow";
            String dataId = "unknow";
            if (tags.length == 2) {
                servId = tags[0];
                dataId = tags[1];
            } else if (tags.length == 3) {
                servId = tags[1];
                dataId = tags[2];
            }
            
            // 获取当前服务配置
            Bean searchServ = null;
            try {
                ServDefBean servDefBean = ServUtils.getServDef(servId);
                searchServ = servDefBean.getFirstSearchDef();
            } catch (Exception e) {
                log.warn("get search def error", e);
            }
            String abstractText = "";
            if (null != searchServ) {
                String abstractDef = searchServ.getStr("SEARCH_DIGEST");
                if (abstractDef.length() > 0) {
                    // 替换字段变量
                    abstractText = ServUtils.replaceValues(abstractDef, servId, data);
                }

                // 替换摘要变量
                if (abstractText.length() > 0) {
                    abstractText = abstractText.replace("${dynamic-content}", data.getStr(RhSearchClient.SOLR_CONTENT));
                    abstractText = abstractText.replace(RhFileClient.FILE_SERVER_EXPR, fileServer);
                }
            }

            if (abstractText.length() == 0) {
                abstractText = data.getStr(RhSearchClient.SOLR_CONTENT);
            }
            data.set(RhSearchClient.SOLR_ABSTRACT, abstractText);

            // replace file content
            String contentFile = data.getStr("file_path" + RhSearchClient.DYNAMIC_STR_FIELD);
            contentFile = contentFile.replace(RhFileClient.FILE_SERVER_EXPR, fileServer);
            data.set("file_path" + RhSearchClient.DYNAMIC_STR_FIELD, contentFile);

            dataList.add(data);

            // 设置连接地址
            if (servId.equals(ServMgr.SY_COMM_WENKU_DOCUMENT)) {
                if (isMobile) {
                    String docFile = data.getStr("DOCUMENT_FILE");
                    data.set(RhSearchClient.SOLR_URL, "/file/" + docFile + "?act=preview");
                } else {
                    data.set(RhSearchClient.SOLR_URL, "/cms/" + servId + "/" + dataId + ".html");
                }
            } else if (servId.equals(ServMgr.SY_COMM_ZHIDAO_QUESTION)) {
                data.set(RhSearchClient.SOLR_URL, "/cms/" + servId + "/" + dataId + ".html");
            }
        }

        OutBean resultBean = new OutBean();
        resultBean.setPage(page);
        resultBean.setData(dataList);
        resultBean.set("SEARCH_RESPONSE", response);
        resultBean.set("SEARCH_SORT", sort);
        resultBean.set("SEARCH_KEYWORDS", displayKeywords);
        resultBean.set("SEARCH_LAST_QUERY", queryStr);
        log.debug("all search query time:" + (sw.getTime()));
        return resultBean;
    }

    /** default search fields */
    private static final String[] DEFAULT_SEARCH_FIELDS = { "title", "content", "attachment", "attachment_title",
            "attachment_mtype", "abstract", "keywords", "extendIndex" };

    /**
     * builde common query
     * @param keyWords - keywords
     * @return common query string
     */
    private String buildeCommQuery(final String keyWords) {
        List<String> keywordList = new ArrayList<String>();
        String temp = keyWords.replaceAll("\"", "");
        final String[] kws = temp.split(" ");
        for (String key : kws) {
            key = key.trim();
            if (key.length() > 0) {
                keywordList.add(key);
            }
        }
        StringBuilder queryBuilder = new StringBuilder();

        // if search keywords format is : xxxx doc
        if (keywordList.size() == 2 && SearchHelper.isMimeType(keywordList.get(1))) {
            for (String searchField : DEFAULT_SEARCH_FIELDS) {
                queryBuilder.append("OR (");
                String fieldQuery = "";
                fieldQuery += "OR " + searchField + ":" + keywordList.get(0) + " ";
                queryBuilder.append(fieldQuery.substring(2));
                queryBuilder.append(") ");
            }
            return "( " + queryBuilder.substring(2).toString() + ") AND attachment_mtype:" + keywordList.get(1);
        } else {
            // 正常情况
            for (String searchField : DEFAULT_SEARCH_FIELDS) {
                queryBuilder.append("OR (");
                String fieldQuery = "";
                for (String keyWord : keywordList) {
                    if (keyWord.length() == 0) {
                        continue;
                    }
                    fieldQuery += "AND " + searchField + ":" + keyWord + " ";
                }
                queryBuilder.append(fieldQuery.substring(3));
                queryBuilder.append(") ");
            }

            // format
            if (queryBuilder.toString().startsWith("OR")) {
                return queryBuilder.substring(2);
            } else {
                return queryBuilder.toString();
            }

        }

    }

    /**
     * merge numberic group
     * @param dataList - data list
     * @return new result List with gap
     */
    private List<Bean> mergeNumGroup(List<Bean> dataList) {
        // merge number facets
        Bean max = dataList.get(dataList.size() - 1);
        int maxKey = Double.valueOf(max.getStr("NAME")).intValue();
        int gapValue = maxKey / 5;
        List<Bean> result = new ArrayList<Bean>();
        for (int i = 0; i < 6; i++) {
            int start = gapValue * i;
            int offset = gapValue * i + gapValue;
            Bean newBean = new Bean().set("NAME", start + "-" + offset);
            newBean.set("QUERY", "[" + start + " TO " + offset + "]");
            result.add(i, newBean);
        }
        for (Bean bean : dataList) {
            String key = bean.getStr("NAME");
            int keyInt = Double.valueOf(key).intValue();
            int index = keyInt / gapValue;
            Bean target = result.get(index);
            target.set("COUNT", target.get("COUNT", 0) + bean.get("COUNT", 0));
        }

        List<Bean> newResult = new ArrayList<Bean>();
        for (Bean bean : result) {
            String key = bean.getStr("NAME");
            String[] values = key.split("-");
            String newName = "";
            for (String val : values) {
                int keyInt = Integer.valueOf(val);
                final double gap10000 = 10000.0;
                if (gap10000 <= keyInt) {
                    double douVal = (double) (Math.round(keyInt * 10) / 10.0 / gap10000);
                    double newVal = new BigDecimal(douVal).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
                    newName += newVal + "万-";
                } else {
                    newName += keyInt + "-";
                }
            }
            if (newName.length() > 0) {
                newName = newName.substring(0, newName.length() - 1);
                bean.set("NAME", newName);
            }
            if (bean.get("COUNT", 0) > 0) {
                newResult.add(bean);
            }
        }
        return newResult;
    }
    
    /**
     * get abstract content
     * @param content - content string
     * @param maxLeng - max abstract text leng
     * @return abstract content
     */
    private String getAbstract(String content, int maxLeng) {
        if (content == null) {
            return "";
        }
        if (content.length() > 5000) {
            content = content.substring(0, 5000);
        }
        String result = SearchHelper.getAbstract(content, maxLeng, HL_PRE_TAG, HL_POST_TAG);
        return result;
    }    
    
    /**
     * handle highlight data
     * @param data result data
     * @param wordList hl keyword
     */
    private void highlight(Bean data, List<String> wordList) {
        StopWatch sw = new StopWatch();
        sw.start();
        @SuppressWarnings("unchecked")
        List<String> attachmentList = (List<String>) data.get(RhSearchClient.SOLR_ATTACHMENT);
        List<Bean> attList = new ArrayList<Bean>();
        if (null != attachmentList) {
            int indexVal = 0;
            
            for (String attachment : attachmentList) {
                HighlightHelper hlh = new HighlightHelper(attachment, wordList, 100);
                
                // 过滤附件内容搜索未命中的附件
                if (hlh.getFirstPos() >= 0) {
                    attList.add(createAttachBean(data, indexVal, hlh, wordList));
                } else {
                    // 过滤附件类型搜索未命中的附件
                    // get attachment mime-type
                    String mtype = "";
                    @SuppressWarnings("unchecked")
                    List<String> attMtypeList = (List<String>) data.get(RhSearchClient.SOLR_ATTACHMENT_MTYPE);
                    if (null != attMtypeList && indexVal <= attMtypeList.size()) {
                        mtype = attMtypeList.get(indexVal);
                    }
                    for (String word : wordList) {
                        if (word.equals(mtype)) {
                            Bean attBean = createAttachBean(data, indexVal, null, wordList);
                            attBean.set(RhSearchClient.ATTACHMENT_CONTENT, attachment.substring(0, 100));
                            attList.add(attBean);
                        }
                    }

                }
                indexVal++;
            }
        }
        String highlightTitle = data.getStr(RhSearchClient.SOLR_TITLE);
        String highLightContent = data.getStr(RhSearchClient.SOLR_CONTENT);
        for (String word : wordList) {
            highlightTitle = StringUtils.replace(highlightTitle, word, HL_PRE_TAG + word + HL_POST_TAG);
            highLightContent = StringUtils.replace(highLightContent, word, HL_PRE_TAG + word + HL_POST_TAG);
        }  
        
        data.set(RhSearchClient.SOLR_ATTACHMENT, attList);
        data.set(RhSearchClient.SOLR_TITLE, highlightTitle);
        data.set(RhSearchClient.SOLR_CONTENT, highLightContent);
        
        log.warn("##findFirstPostion" + sw.getTime());
    }

    /**
     * 将附件结果添加至制定列表
     * @param data - 数据源
     * @param indexVal - 附件索引， 代表第几个附件
     * @param hlh - 附件内容(高亮)
     * @wordList  关键字
     * @return 附件Bean
     */
    private Bean createAttachBean(Bean data, int indexVal, HighlightHelper hlh, List<String> wordList) {
        Bean attBean = new Bean();
        // get id
        String attId = "";
        @SuppressWarnings("unchecked")
        List<String> attIdList = (List<String>) data.get(RhSearchClient.SOLR_ATTACHMENT_ID);
        if (null != attIdList && indexVal <= attIdList.size()) {
            attId = attIdList.get(indexVal);
        }

        // get path
        String attPath = "";
        @SuppressWarnings("unchecked")
        List<String> attPathList = (List<String>) data.get(RhSearchClient.SOLR_ATTACHMENT_PATH);
        if (null != attPathList && indexVal <= attPathList.size()) {
            attPath = attPathList.get(indexVal);
            attPath = attPath.replace("internal://", "/file/");

            String fileServer = RhFileClient.getInstance().getServerUri();
            attPath = attPath.replace(RhFileClient.FILE_SERVER_EXPR, fileServer);
        }

        // get title
        String attTitle = "";
        @SuppressWarnings("unchecked")
        List<String> attTitleList = (List<String>) data.get(RhSearchClient.SOLR_ATTACHMENT_TITLE);
        if (null != attTitleList && indexVal <= attTitleList.size()) {
            attTitle = attTitleList.get(indexVal);
        }

        // get mime type
        String attType = "";
        @SuppressWarnings("unchecked")
        List<String> mtypeList = (List<String>) data.get(RhSearchClient.SOLR_ATTACHMENT_MTYPE);
        if (null != mtypeList && indexVal <= mtypeList.size()) {
            attType = mtypeList.get(indexVal);
        } else if (attTitle.length() > 4 && attTitle.substring(attTitle.length() - 4).startsWith(".")) {
            attType = attTitle.substring(attTitle.length() - 3);
        }
        
        if (hlh != null) {
            attBean.set(RhSearchClient.ATTACHMENT_CONTENT, hlh.getHightligtText());
        }
        
        attTitle = SearchHelper.getAbstract(attTitle, 28, 0, wordList);
        attBean.set(RhSearchClient.ATTACHMENT_TITLE, attTitle);        
        
        attBean.set(RhSearchClient.ATTACHMENT_ID, attId);
//        attBean.set(RhSearchClient.ATTACHMENT_TITLE, attTitle);
        attBean.set(RhSearchClient.ATTACHMENT_PATH, attPath);
        attBean.set(RhSearchClient.ATTACHMENT_MTYPE, attType);
        attBean.set(RhSearchClient.ATTACHMENT_INDEX, indexVal);
//        attBean.set(RhSearchClient.ATTACHMENT_CONTENT, hlAttachment);
//        attList.add(attBean);
        return attBean;
    }

    /**
     * 服务端处理高亮
     * @param highlightMap highlight data
     * @param data data bean
     */
    private void serverHandleHighlight(Map<String, List<String>> highlightMap, Bean data) {
        List<Bean> attList = new ArrayList<Bean>();
        List<String> highLightAtt = null;
        Set<String> hlKeys = highlightMap.keySet();
        for (String hlKey : hlKeys) {
            if (hlKey.equals(RhSearchClient.SOLR_TITLE)) {
                List<String> highlightTitle = highlightMap.get(hlKey);
                if (highlightTitle != null && highlightTitle.size() > 0) {
                    data.set(RhSearchClient.SOLR_TITLE, highlightTitle.get(0));
                }
            } else if (hlKey.equals(RhSearchClient.SOLR_CONTENT)) {
                List<String> highLightContent = highlightMap.get(hlKey);
                if (highLightContent != null && highLightContent.size() > 0) {
                    data.set(RhSearchClient.SOLR_CONTENT, hlListToStr(highLightContent));
                }
            } else if (hlKey.startsWith(RhSearchClient.SOLR_ATTACHMENT)) {
                List<String> hlAttList = highlightMap.get(hlKey);
                
                for (int indexVal = 0; indexVal < hlAttList.size(); indexVal++) {
                    String attStr = hlAttList.get(indexVal);
                    // 跳过关键词没有匹配的附件
                    if (-1 == attStr.indexOf(HL_PRE_TAG)) {
                        continue;
                    }
                   

                    // get id
                    String attId = "";
                    @SuppressWarnings("unchecked")
                    List<String> attIdList = (List<String>) data.get(RhSearchClient.SOLR_ATTACHMENT_ID);
                    if (null != attIdList && indexVal < attIdList.size()) {
                        attId = attIdList.get(indexVal);
                    }

                    // get path
                    String attPath = "";
                    @SuppressWarnings("unchecked")
                    List<String> attPathList = (List<String>) data.get(RhSearchClient.SOLR_ATTACHMENT_PATH);
                    if (null != attPathList && indexVal < attPathList.size()) {
                        attPath = attPathList.get(indexVal);
                    }

                    // get title
                    String attTitle = "";
                    @SuppressWarnings("unchecked")
                    List<String> attTitleList = (List<String>) data.get(RhSearchClient.SOLR_ATTACHMENT_TITLE);
                    if (null != attTitleList && indexVal < attTitleList.size()) {
                        attTitle = attTitleList.get(indexVal);
                    }

                    // get mime type
                    String attType = "";
                    @SuppressWarnings("unchecked")
                    List<String> mtypeList = (List<String>) data.get(RhSearchClient.SOLR_ATTACHMENT_MTYPE);
                    if (null != mtypeList && indexVal < mtypeList.size()) {
                        attType = mtypeList.get(indexVal);
                    } else if (attTitle.length() > 4 && attTitle.substring(attTitle.length() - 4).startsWith(".")) {
                        attType = attTitle.substring(attTitle.length() - 3);
                    }
                    AttachmentBean attBean = new AttachmentBean();
                    attBean.setAttId(attId);
                    attBean.setTitle(attTitle);
                    attBean.setPath(attPath);
                    attBean.setMtype(attType);
                    attBean.setIndex(indexVal);
                    attBean.setContent(attStr);
                    attList.add(attBean);
                }
            }

        }
        
        if (highLightAtt == null) {
            data.set(RhSearchClient.SOLR_ATTACHMENT, attList);
        }
    }

    /**
     * highlight content list to string
     * @param highLightContent hl contents
     * @return hl string
     */
    private String hlListToStr(List<String> highLightContent) {
        if (null == highLightContent || highLightContent.size() == 0) {
            return "";
        }
        StringBuilder hlBuilder = new StringBuilder();
        for (String hl : highLightContent) {
            hlBuilder.append(hl);
        }
        return hlBuilder.toString();
    }

    /**
     * 服务byid
     * @param servId - 服务id
     * @param dataId - data id
     * @return data bean
     */
    private Bean getData(String servId, String dataId) {
        Bean dataBean = new Bean();
        try {
            dataBean = ServMgr.act(servId, ServMgr.ACT_BYID, new ParamBean().setId(dataId));
        } catch (Exception e) {
            log.warn(" get data error", e);
        }
        
        if (servId.equals(ServMgr.SY_COMM_ZHIDAO_QUESTION)) {
            String qId = dataBean.getId();
            
            ParamBean query = new ParamBean().set("Q_ID", qId);
            query.set("summaryLength", 200);
            OutBean answerResult = new ZhidaoServ().getAnswer(query);
            Bean bestAnswer = (Bean) answerResult.get("bestAnswer");
            String answerTime = bestAnswer.getStr("S_MTIME");
            if (answerTime.length() > 10) {
                answerTime = answerTime.substring(0, 10);
                bestAnswer.set("S_MTIME", answerTime);
            }
            dataBean.putAll(bestAnswer);
        }
        return dataBean;
    }
    
    /**
     * 
     * @param filterList 过滤列表
     * @param paramBean 参数
     * @return 开始日期
     */
    public static String getStartDate(List<Bean> filterList, OutBean paramBean) {
        if(filterList == null){
            return "";
        }
        for (Bean bean : filterList) {
            if (bean.getStr("id").equals("date") && bean.getStr("data").equals("date_c")) {
                return paramBean.getStr("SEARCH_STARTTIME");
            }
        }
        return "";
    }
    
    /**
     * 
     * @param filterList 过滤列表
     * @param paramBean 参数
     * @return 结束日期
     */
    public static String getEndDate(List<Bean> filterList, OutBean paramBean) {
        if(filterList == null){
            return "";
        }
        for (Bean bean : filterList) {
            if (bean.getStr("id").equals("date") && bean.getStr("data").equals("date_c")) {
                return paramBean.getStr("SEARCH_ENDTIME");
            }
        }
        return "";
    }
}
