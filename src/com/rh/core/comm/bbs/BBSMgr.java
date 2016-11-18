package com.rh.core.comm.bbs;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.rh.core.base.Bean;
import com.rh.core.base.TipException;
import com.rh.core.comm.CacheMgr;
import com.rh.core.comm.cms.mgr.ChannelMgr;
import com.rh.core.comm.cms.mgr.ExtendsField;
import com.rh.core.serv.OutBean;
import com.rh.core.serv.ParamBean;
import com.rh.core.serv.ServDao;
import com.rh.core.serv.ServMgr;
import com.rh.core.serv.bean.SqlBean;
import com.rh.core.util.Constant;
import com.rh.core.util.DateUtils;

/**
 * bbs
 * @author liwei
 * 
 */
public class BBSMgr {
    // 日志记录
    @SuppressWarnings("unused")
    private static Log log = LogFactory.getLog(BBSMgr.class);

    private static BBSMgr instance = new BBSMgr();

    private static final String CACHE_TYPE_BBS_CHNL_PROPERTIES = "CACHE_TYPE_BBS_CHNL_PROPERTIES";

    // 文库服务CODE
    private static final String TOPIC_SERV = ServMgr.SY_COMM_BBS_TOPIC;
    
    /** 当前日期 (格式) */
    private static final String TODAY_FORMAT = "yyyy-MM-dd";

    /** 所有继承关系字段对象集合 */
    public static final List<ExtendsField> EXTENDS_FIELDS = new ArrayList<ExtendsField>();
    static {
        // 栏目下内容模版继承
        ExtendsField topicTmpl = new ExtendsField();
        topicTmpl.setIdField("TMPL_ID");
 //       topicTmpl.setNameField("TMPL_NAME");
        topicTmpl.setExtendsField("TMPL_EXTENDS");
        topicTmpl.setParentIdField("CHNL_CONTENT_TMPL");
 //       topicTmpl.setParentNameField("CHNL_CONTENT_TMPL_NAME");
        EXTENDS_FIELDS.add(topicTmpl);

        // 栏目下内容评论状态继承
        ExtendsField comment = new ExtendsField();
        comment.setIdField("TOPIC_COMMENT_STATUS");
        comment.setExtendsField("TOPIC_COMMENT_EXTENDS");
        comment.setParentIdField("CHNL_CONTENT_COMMENT_STATUS");
        EXTENDS_FIELDS.add(comment);
    }

    /**
     * Singleton
     * @return - Singleton instance
     */
    public static BBSMgr getInstance() {
        return instance;
    }

    /**
     * can not new instance
     */
    private BBSMgr() {
    }
    
    
    
    /**
     * 回复量 + 1
     * @param param - 参数bean
     */
    public void increaseCommentCounter(Bean param) {
        //更新主题回复量
        String key = "COMMENT_COUNTER";
        Bean topic = ServDao.find(ServMgr.SY_COMM_BBS_TOPIC, param);
        topic.set(key, topic.get(key, 0) + 1);
        ServDao.update(ServMgr.SY_COMM_BBS_TOPIC, topic);
        
        //更新版块下回复数量
        String chnlId = topic.getStr("CHNL_ID");
        if (cacheCategoryCommentCouner(chnlId)) {
            int counter =  getCategoryCommentCouner(chnlId);
            counter += 1;
            updateCategoryCommentCouner(chnlId, counter);
        }
    }


    /**
     * 阅读量 + 1
     * @param param - 参数bean
     */
    public void increaseReadCounter(Bean param) {
        String key = "TOPIC_READ_COUNTER";
        Bean topic = ServDao.find(ServMgr.SY_COMM_BBS_TOPIC, param);
        if (null == topic || topic.isEmpty()) {
            throw new TipException("topic not found!");
        }
        topic.set(key, topic.get(key, 0) + 1);
        ServDao.update(ServMgr.SY_COMM_BBS_TOPIC, topic);
        
        // 更新阅读历史
//        String act = "read";
//        ReqhisMgr.save(ServMgr.SY_COMM_BBS_TOPIC, topic.getId(), act, topic.getStr("TOPIC_TITLE"));
    }

    /**
     * set extend field value
     * @param bean - topic bean
     */
    public void setExtendValues(Bean bean) {
        for (ExtendsField extendsField : EXTENDS_FIELDS) {
            setExtendValue(bean, extendsField);
        }
    }


    /**
     * get channel bean from cache and database(cache first)
     * @param id - channel pk
     * @return - cache bean
     */
    public Bean getTopic(String id) {
        Bean result = ServDao.find(TOPIC_SERV, new Bean().setId(id));
        if (null == result) {
            return null;
        }
        setExtendValues(result);
        return result;
    }
    
    /**
     * 获取版块今日新增主题数量
     * @param chnlId - 版块ID
     * @return count
     */
    public int getCategoryNewTopicCouner(String chnlId) {
        String fieldName = DateUtils.getStringFromDate(new Date(), TODAY_FORMAT);
        Bean bean = getCategoryBean(chnlId);
        if (bean.isEmpty(fieldName)) {
            ParamBean queryBean = new ParamBean(TOPIC_SERV).setAct(ServMgr.ACT_COUNT);
            queryBean.setQueryLinkWhere(" AND S_MTIME = '" + fieldName + "'");

            OutBean result = ServMgr.act(queryBean);
            int counter = result.getInt(Constant.RTN_DATA);
            bean.set(fieldName, counter);
            updateInCache(bean);
        }
        return bean.getInt(fieldName);
    }
    
    /**
     * 是否缓存今日新增主题数量
     * @param chnlId - 版块ID
     * @return - boolean
     */
    public boolean cacheCategoryNewTopicCouner(String chnlId) {
        String fieldName = DateUtils.getStringFromDate(new Date(), TODAY_FORMAT);
        Bean bean = getCategoryBean(chnlId);
        return !bean.isEmpty(fieldName);
    }

    /**
     *  更新版块今日新增主题数量
     * @param chnlId - 版块ID
     * @param counter - 今日主题数量
     * @return - count
     */
    public int updateCategoryNewTopicCouner(String chnlId, int counter) {
        String fieldName = DateUtils.getStringFromDate(new Date(), TODAY_FORMAT);
        Bean bean = getCategoryBean(chnlId);
        bean.set(fieldName, counter);
        updateInCache(bean);
        return counter;
    }
    
    
    
    /**
     * 获取版块评论数量
     * @param chnlId - 版块ID
     * @return - 该版块下所有主题评论总数
     */
    public int getCategoryCommentCouner(String chnlId) {
        String fieldName = "COMMENT_COUNTER";
        Bean bean = getCategoryBean(chnlId);
        if (bean.isEmpty(fieldName)) {
            Bean queryBean = new Bean();
            //queryBean.set("serv", TOPIC_SERV);
            queryBean.set(Constant.PARAM_SELECT, "SUM(COMMENT_COUNTER) as COUNTER");
            queryBean.set(Constant.PARAM_WHERE, " AND CHNL_ID = '" + chnlId + "'");
            //Bean result = new CommonServ().query(queryBean);
                                  
            Bean result = ServDao.find(TOPIC_SERV, queryBean);
            int counter = result.get("COUNTER", 0);
            
            bean.set(fieldName, counter);
            updateInCache(bean);
        }
        return bean.getInt(fieldName);
    }
    
    /**
     * 是否缓存版块评论数量
     * @param chnlId - 版块ID
     * @return - boolean
     */
    public boolean cacheCategoryCommentCouner(String chnlId) {
        String fieldName = "COMMENT_COUNTER";
        Bean bean = getCategoryBean(chnlId);
        return !bean.isEmpty(fieldName);
    }

    /**
     * 更新版块评论数量
     * @param chnlId - 版块ID
     * @param counter - 评论量
     * @return count
     */
    public int updateCategoryCommentCouner(String chnlId, int counter) {
        Bean bean = getCategoryBean(chnlId);
        bean.set("COMMENT_COUNTER", counter);
        updateInCache(bean);
        return counter;
    }
    
    
    /**
     * 是否缓存版块下主题数量
     * @param chnlId - 版块ID
     * @return - boolean
     */
    public boolean cacheCategorTopicCouner(String chnlId) {
        String fieldName = "TOPIC_COUNTER";
        Bean bean = getCategoryBean(chnlId);
        return !bean.isEmpty(fieldName);
    }

    /**
     * 获取版块下主题数量
     * @param chnlId - 版块ID
     * @return count
     */
    public int getCategoryTopicCouner(String chnlId) {
        String fieldName = "TOPIC_COUNTER";
        Bean bean = getCategoryBean(chnlId);
        if (bean.isEmpty(fieldName)) {
            Bean queryBean = new Bean();
            queryBean.set("CHNL_ID", chnlId);
            int topicCounter = ServDao.count(TOPIC_SERV, queryBean);
            bean.set(fieldName, topicCounter);
            updateInCache(bean);
        }
        return bean.getInt(fieldName);
    }


    /**
     * 更新目标版块主题数量
     * @param chnlId - 版块ID
     * @param counter - 主题数量
     * @return - 主题数量
     */
    public int updateCategoryTopicCouner(String chnlId, int counter) {
        Bean bean = getCategoryBean(chnlId);
        bean.set("TOPIC_COUNTER", counter);
        updateInCache(bean);
        return counter;
    }

    /**
     * put data in cache
     * @param bean - data
     * @return data
     */
    private Bean updateInCache(Bean bean) {
        String pk = bean.getId();
        
      //merage
        Bean current = getFromCache(pk);
        if (null != current) {
            Set<Object> allKeys = current.keySet();
            for (Object key : allKeys) {
                if (!bean.contains(key.toString())) {
                    bean.set(key, current.get(key.toString()));
                }
            }
        }
        bean.set("UPDATE_TIME", System.currentTimeMillis());
        CacheMgr.getInstance().set(pk, bean, CACHE_TYPE_BBS_CHNL_PROPERTIES);
        return bean;
    }

    /**
     * get data from cache
     * @param key - key
     * @return data
     */
    private Bean getFromCache(String key) {
        Object obj = CacheMgr.getInstance().get(key, CACHE_TYPE_BBS_CHNL_PROPERTIES);
        if (null == obj) {
            return null;
        } else {
            return (Bean) obj;
        }
    }
    
    /**
     * 获取版块缓存信息，如果没有返回new Bean();
     * @param chnlId - 版块ID
     * @return 缓存bean
     */
    private Bean getCategoryBean(String chnlId) {
        Bean bean = getFromCache(chnlId);
        if (bean == null) {
            bean = new Bean().setId(chnlId);
            updateInCache(bean);
        }
        return bean;
    }
    
    /**
     * set topic extends value
     * @param topicBean - topic bean
     * @param extendsField - extends fields properties
     */
    private void setExtendValue(Bean topicBean, ExtendsField extendsField) {
        String chnlId = topicBean.getStr("CHNL_ID");
        String idField = extendsField.getIdField();
        String nameField = extendsField.getNameField();
        String exField = extendsField.getExtendsField();
        if (null == chnlId || 0 == chnlId.length()) {
            return;
        }
        if (topicBean.isNotEmpty(extendsField)) {
            return;
        }
        
        if (topicBean.isEmpty(idField)) {
            Bean parent = ChannelMgr.getInstance().getChannel(chnlId);
            topicBean.set(exField, 1);
            topicBean.set(idField, parent.getStr(extendsField.getParentIdField()));
            if (null != nameField) {
                topicBean.set(nameField, parent.getStr(extendsField.getParentNameField()));
            }
        } else {
            topicBean.set(exField, 2);
        }
    }
    
    /**
     * 取得指定条数的最新主题列表
     * @param paramBean 含有"_ROWNUM_"值的参数Bean
     * @return 返回给定条数的最新主题列表
     */
    public List<Bean> getNewTopics(Bean paramBean) {
        SqlBean bean = new SqlBean(paramBean).and("TOPIC_SHOW_TYPE", 1).and("TOPIC_CHECKED", 1)
                .selects("TOPIC_TITLE,S_CTIME,S_UNAME,S_USER")
                .orders("S_CTIME desc,S_MTIME desc");
        if (bean.isEmpty(Constant.PARAM_ROWNUM)) {
            bean.limit(5);
        }
        
        List<Bean> topicList = ServDao.finds(ServMgr.SY_COMM_BBS_TOPIC, bean);
        return topicList;
    }
    
    /**
     * 取得指定条数的最新评论列表，其中每条评论携带主题信息
     * @param paramBean 含有"_ROWNUM_"值的参数Bean
     * @return 最新评论列表，评论携带主题信息
     */
    public List<Bean> getNewReplyTopics(Bean paramBean) {
        SqlBean bean = new SqlBean(paramBean).and("TOPIC_CHECKED", 1)
                .selects("TOPIC_TITLE,S_UNAME,S_USER,S_MTIME")
                .orders("S_MTIME desc,S_CTIME desc");
        if (bean.isEmpty(Constant.PARAM_ROWNUM)) {
            bean.limit(5);
        }
        
        List<Bean> topicList = ServDao.finds(ServMgr.SY_COMM_BBS_TOPIC, bean);
        StringBuilder sb = new StringBuilder("(");
        
        for (Object o:topicList) {
            Bean b = (Bean) o;
            sb.append("'" + b.getId() + "',");
        }
        sb.deleteCharAt(sb.length() - 1);
        sb.append(")");
        
        List<Bean> commentList = null;
        if (!topicList.isEmpty()) {
            Bean b = new Bean();
            b.set(Constant.PARAM_SELECT, "DATA_ID,S_UNAME,S_USER,S_MTIME");
            b.set(Constant.PARAM_ORDER, "S_MTIME desc");
            b.set(Constant.PARAM_WHERE, 
                    " and DATA_ID in " + sb.toString() + " and C_STATUS=1");
            commentList = ServDao.finds(ServMgr.SY_SERV_COMMENT, b);
          //对评论和主题进行绑定
            for (Object o1:topicList) {
                Bean topic = (Bean) o1;
                for (Object o2:commentList) {
                    Bean comment = (Bean) o2;
                    if (topic.getId().equals(comment.getStr("DATA_ID"))) {
                        topic.put("S_UNAME", comment.getStr("S_UNAME"));
                        topic.put("S_USER", comment.getStr("S_USER"));
                        topic.put("S_MTIME", comment.getStr("S_MTIME"));
                        break;
                    }
                }
            }
        }
        return topicList;
    }
    
    /**
     * 取得指定条数的最热主题列表
     * @param paramBean 含有"_ROWNUM_"值的参数Bean
     * @return 返回给定条数的最新主题列表
     */
    public List<Bean> getHotTopics(Bean paramBean) {
        SqlBean bean = new SqlBean(paramBean).and("TOPIC_SHOW_TYPE", 1).and("TOPIC_CHECKED", 1)
                .selects("TOPIC_TITLE,COMMENT_COUNTER,TOPIC_READ_COUNTER")
                .orders("TOPIC_READ_COUNTER desc,COMMENT_COUNTER desc");
        if (bean.isEmpty(Constant.PARAM_ROWNUM)) {
            bean.limit(5);
        }
        
        List<Bean> topicList = ServDao.finds(ServMgr.SY_COMM_BBS_TOPIC, bean);
        return topicList;
    }

    /**
     * 根据一级栏目id，取得下面二级栏目信息（回复数，主题数）
     * @param paramBean 含有一级栏目id的Bean
     * @return 返回一级栏目下二级栏目信息
     */
    public Bean getListByChannel(Bean paramBean) {
        String channelId = paramBean.getId();
        //Bean outBean = new Bean(paramBean);
        if (!channelId.isEmpty()) {
            Bean bean = new Bean();
            bean.set("CHNL_PID", channelId);
            bean.set(Constant.PARAM_SELECT, "CHNL_NAME,CHNL_IMAGE");
            List<Bean> channelList = ServDao.finds("SY_COMM_BBS_CHNL", bean);
            
            //定义父栏目回复数、主题
            int comment = 0;
            int topic = 0;
            for (Bean b:channelList) {
                //添加栏目回复数、主题数
                int commentTotal = getCategoryCommentCouner(b.getId());
                int topicTotal = getCategoryTopicCouner(b.getId());
                b.set("commentTotal", commentTotal);
                b.set("topicTotal", topicTotal);
                
                comment += commentTotal;
                topic += topicTotal;
                
                //处理栏目图片
                String chnlImg = b.getStr("CHNL_IMAGE");
                if (!chnlImg.isEmpty() && chnlImg.contains(",")) {
                    b.set("CHNL_IMAGE", chnlImg.substring(0, chnlImg.indexOf(",")));
                }
            }
            paramBean.set("commentTotal", comment);
            paramBean.set("topicTotal", topic);
            paramBean.set(Constant.RTN_DATA, channelList);
        }
        
        return paramBean;
    }
}
