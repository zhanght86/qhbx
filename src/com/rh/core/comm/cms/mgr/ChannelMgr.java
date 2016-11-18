package com.rh.core.comm.cms.mgr;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.rh.core.base.Bean;
import com.rh.core.serv.ServDao;
import com.rh.core.serv.ServMgr;

/**
 * channel manager
 * @author liwei
 * 
 */
public class ChannelMgr {

 //   private static final String CACHE_TYPE_CHANNEL_EXTENDS_PROPERTIES = "SY_COMM_CMS_CHANNEL_EXTENDS_PROPERTIES";
    /** chnl serv id */
    private static final String CHANNEL_SERVICE = ServMgr.SY_COMM_CMS_CHNL;

    @SuppressWarnings("unused")
    private static Log log = LogFactory.getLog(ChannelMgr.class);

    private static ChannelMgr instance = new ChannelMgr();

    /** 所有继承关系字段对象集合 */
    public static final List<ExtendsField> EXTENDS_FIELDS = new ArrayList<ExtendsField>();
    static {
        // 栏目封面模版继承
        ExtendsField tmpl = new ExtendsField();
        tmpl.setIdField("CHNL_TMPL");
   //     tmpl.setNameField("CHNL_TMPL_NAME");
        tmpl.setExtendsField("CHNL_TMPL_EXTENDS");
        tmpl.setParentIdField(tmpl.getIdField());
  //      tmpl.setParentNameField(tmpl.getNameField());
        EXTENDS_FIELDS.add(tmpl);

        // 栏目列表模版继承
        ExtendsField listTmpl = new ExtendsField();
        listTmpl.setIdField("CHNL_LIST_TMPL");
  //      listTmpl.setNameField("CHNL_LIST_TMPL_NAME");
        listTmpl.setExtendsField("CHNL_LIST_TMPL_EXTENDS");
        listTmpl.setParentIdField(listTmpl.getIdField());
  //      listTmpl.setParentNameField(listTmpl.getNameField());
        EXTENDS_FIELDS.add(listTmpl);

        // 栏目下内容模版继承
        ExtendsField contentTmpl = new ExtendsField();
        contentTmpl.setIdField("CHNL_CONTENT_TMPL");
 //       contentTmpl.setNameField("CHNL_CONTENT_TMPL_NAME");
        contentTmpl.setExtendsField("CHNL_CONTENT_TMPL_EXTENDS");
        contentTmpl.setParentIdField(contentTmpl.getIdField());
 //       contentTmpl.setParentNameField(contentTmpl.getNameField());
        EXTENDS_FIELDS.add(contentTmpl);

        // 栏目下内容评论状态继承
        ExtendsField comment = new ExtendsField();
        comment.setIdField("CHNL_CONTENT_COMMENT_STATUS");
        comment.setExtendsField("CHNL_CONTENT_COMMENT_EXTENDS");
        EXTENDS_FIELDS.add(comment);
        
        // 标题模板继承
        ExtendsField titleTmple = new ExtendsField();
        titleTmple.setIdField("CHNL_TITLE_TMPL");
        titleTmple.setExtendsField("CHNL_TITLE_TMPL_EXTENDS");
        EXTENDS_FIELDS.add(titleTmple);
        
        // 审核级别继承
        ExtendsField check = new ExtendsField();
        check.setIdField("CHNL_CHECK");
        check.setExtendsField("CHNL_CHECK_EXTENDS");
        EXTENDS_FIELDS.add(check);
        
        // 审核人继承
        ExtendsField checker = new ExtendsField();
        checker.setIdField("CHNL_CHECKER");
        checker.setExtendsField("CHNL_CHECKER_EXTENDS");
        EXTENDS_FIELDS.add(checker);
        
        // 公开范围继承
        ExtendsField chnlScope = new ExtendsField();
        chnlScope.setIdField("CHNL_SCOPE");
        chnlScope.setExtendsField("CHNL_SCOPE_EXTENDS");
        EXTENDS_FIELDS.add(chnlScope);
        
        // 可查看人继承
        ExtendsField chnlOwner = new ExtendsField();
        chnlOwner.setIdField("CHNL_OWNER");
        chnlOwner.setExtendsField("CHNL_OWNER_EXTENDS");
        EXTENDS_FIELDS.add(chnlOwner);
    }

    /**
     * Singleton
     * @return - instance
     */
    public static ChannelMgr getInstance() {
        return instance;
    }

    /**
     * get channel bean from cache and database(cache first)
     * @param id - channel pk
     * @return - cache bean
     */
    public Bean getChannel(String id) { 
        Bean result = ServDao.find(CHANNEL_SERVICE, new Bean().setId(id));
        if (null == result) {
            return null;
        }
        setExtendValues(result);
        return result;
    }

    /**
     * create channel
     * @param bean - channel
     * @return out bean
     */
    public Bean create(Bean bean) {
        ServDao.create(CHANNEL_SERVICE, bean);
//        if (null != bean.getId()) {
//            ChannelMgr.getInstance().putInCache(bean);
//        }
        return bean;
    }

    /**
     * update channel
     * @param bean - channel
     * @return out bean
     */
    public Bean update(Bean bean) {
        ServDao.update(CHANNEL_SERVICE, bean);
//        if (null != bean.getId()) {
//            ChannelMgr.getInstance().putInCache(bean);
//        }
        return bean;
    }

    /**
     * delete channel bean
     * @param bean - channel
     * @return is deleted
     */
    public boolean delete(Bean bean) {
        boolean deleted = ServDao.delete(CHANNEL_SERVICE, bean);
//        if (deleted) {
//            ChannelMgr.getInstance().deleteFromCache(bean.getId());
//        }
        return deleted;
    }

    /**
     * get site id by channel 
     * @param chnlId - channel id
     * @return site id
     */
    public String getSiteId(String chnlId) {

        Bean chnl = getChannelPropertiesBean(chnlId);
       Bean siteChnl = getAncestorInDepth(chnl);
       return siteChnl.getStr("SITE_ID");
    }

    /**
     *  recursive get parent
     * @param channel - channel bean
     * @return - ancestor parent bean
     */
    private Bean getAncestorInDepth(Bean channel) {
        Bean parent = getChannelPropertiesBean(channel.getStr("CHNL_PID"));
        if (parent.isEmpty("CHNL_PID")) {
            return parent;
        } else {
            return getAncestorInDepth(parent);
        }
    }

    /**
     * recursive
     * @param key - field name
     * @param channel - channel bean
     * @return parent value
     */
    private Bean getParentInDepth(String key, Bean channel) {
        if (channel.isEmpty("CHNL_PID")) {
//            return null;
            return channel;
        } 
         Bean parent = getChannelPropertiesBean(channel.getStr("CHNL_PID"));
        if (parent.isEmpty(key)) {
            return getParentInDepth(key, parent);
        } else {
            return parent;
        }
    }

    /**
     * can not new instance
     */
    private ChannelMgr() {
    }

    /**
     * get channel bean from cache and database(cache first)
     * @param id - channel pk
     * @return - cache bean
     */
    private Bean getChannelPropertiesBean(String id) {
//        Bean bean = getFromCache(id);
//        if (null == bean) {
            Bean chnl = ServDao.find(CHANNEL_SERVICE, new Bean().setId(id));
//            bean = putInCache(chnl);
//        }
            
        return chnl;
    }

//    /**
//     * put channel bean in cache
//     * @param chnlBean - channel bean
//     * @return cache bean
//     */
//    public Bean putInCache(Bean chnlBean) {
//        String key = chnlBean.getId();
//        Bean cacheBean = new Bean();
//        cacheBean.setId(key);
//        for (ExtendsField exte : EXTENDS_FIELDS) {
//            cacheBean.set(exte.getIdField(), chnlBean.getStr(exte.getIdField()));
//            if (null != exte.getNameField()) {
//                cacheBean.set(exte.getNameField(), chnlBean.getStr(exte.getNameField()));
//            }
//        }
//        cacheBean.set("CHNL_PID", chnlBean.getStr("CHNL_PID"));
//        CacheMgr.getInstance().set(key, cacheBean, CACHE_TYPE_CHANNEL_EXTENDS_PROPERTIES);
//        return cacheBean;
//    }
//
//    /**
//     * get data from cache
//     * @param key - channel id
//     * @return - channel bean
//     */
//    public Bean getFromCache(String key) {
//        Object obj = CacheMgr.getInstance().get(key, CACHE_TYPE_CHANNEL_EXTENDS_PROPERTIES);
//        if (null == obj) {
//            return null;
//        } else {
//            return (Bean) obj;
//        }
//    }
//
//    /**
//     * delete channel bean from cache
//     * @param key - channel id
//     */
//    public void deleteFromCache(String key) {
//        CacheMgr.getInstance().remove(key, CACHE_TYPE_CHANNEL_EXTENDS_PROPERTIES);
//    }
//
//    /**
//     * clear all cache
//     */
//    public void clearCache() {
//        CacheMgr.getInstance().clearCache(CACHE_TYPE_CHANNEL_EXTENDS_PROPERTIES);
//    }

    /**
     * set extend field value
     * @param channel - channel bean
     */
    public void setExtendValues(Bean channel) {
        for (ExtendsField extendsField : ChannelMgr.EXTENDS_FIELDS) {
            setExtendValue(channel, extendsField);
        }
    }

    /**
     * set extend field value
     * @param channel - channel bean
     * @param field - field properties
     */
    private void setExtendValue(Bean channel, ExtendsField field) {
        String idField = field.getIdField();
        String nameField = field.getNameField();
        String extendsField = field.getExtendsField();
        if (channel == null || (channel.isNotEmpty(extendsField) && channel.isNotEmpty(idField))) {
            return;
        }
        Bean target = null;
        // 如果目标字段没有值，则继承父栏目
        if (channel.isEmpty(idField)) {
            Bean parent = getParentInDepth(idField, channel);
            //如果是一级栏目
            if (parent.getId().equals(channel.getId())) {
                target = new Bean();
                target.set(extendsField, 2);
            } else {
                //target = parent.copyOf(); 
                target = new Bean();
                target.set(idField, parent.getStr(idField));
                target.set(nameField, parent.getStr(nameField));
                target.set(extendsField, parent.getStr(extendsField));
                
                target.set(extendsField, 1);
            }
        } else {
            target = channel;
            target.set(extendsField, 2);
        }

        channel.set(idField, target.getStr(idField));
        if (null != nameField) {
            channel.set(nameField, target.getStr(nameField));
        }
        channel.set(extendsField, target.getStr(extendsField));
    }

}
