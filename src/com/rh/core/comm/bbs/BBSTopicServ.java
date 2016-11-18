package com.rh.core.comm.bbs;

import java.util.List;

import com.rh.core.base.Bean;
import com.rh.core.comm.cms.mgr.ChannelMgr;
import com.rh.core.org.UserBean;
import com.rh.core.org.mgr.UserMgr;
import com.rh.core.serv.CommonServ;
import com.rh.core.serv.OutBean;
import com.rh.core.serv.ParamBean;
import com.rh.core.serv.ServDao;
import com.rh.core.serv.ServMgr;
import com.rh.core.serv.comment.CommentServ;
import com.rh.core.util.Constant;

/**
 * bbs
 * @author liwei
 * 
 */
public class BBSTopicServ extends CommonServ {

    @Override
    public OutBean byid(ParamBean paramBean) {
        OutBean result = super.byid(paramBean);
        String chnlId = result.getStr("CHNL_ID");
        if (null == chnlId || 0 == chnlId.length()) {
            log.warn("CHNL_ID is null! ,topic id:" + result.getId());
        }
        BBSMgr.getInstance().setExtendValues(result);
//        String chnlId = result.getStr("CHNL_ID");
//        if (null != chnlId && 0 < chnlId.length()) {
//            int commentTotal = BBSMgr.getInstance().getCategoryCommentCouner(chnlId);
//            int newTopicCount = BBSMgr.getInstance().getCategoryNewTopicCouner(chnlId);
//            int topicTotal = BBSMgr.getInstance().getCategoryTopicCouner(chnlId);
//        }
        return result;
    }

    @Override
    protected void afterSave(ParamBean paramBean, OutBean outBean) {
        String chnlId = outBean.getStr("CHNL_ID");
        //更新版块下主题数量
        if (BBSMgr.getInstance().cacheCategorTopicCouner(chnlId)) {
            int counter =  BBSMgr.getInstance().getCategoryTopicCouner(chnlId);
            counter += 1;
            BBSMgr.getInstance().updateCategoryTopicCouner(chnlId, counter);
        }
        //更新版块下新增主题数量
        if (BBSMgr.getInstance().cacheCategoryNewTopicCouner(chnlId)) {
            int counter =  BBSMgr.getInstance().getCategoryNewTopicCouner(chnlId);
            counter += 1;
            BBSMgr.getInstance().updateCategoryNewTopicCouner(chnlId, counter);
        }
        
        Bean upBean = new Bean();
        // TODO 栏目跨站点移动时,新闻所属SITE_ID
        if (outBean.isEmpty("SITE_ID")) {
            String siteId = ChannelMgr.getInstance().getSiteId(outBean.getStr("DOCUMENT_CHNL"));
            upBean.set("SITE_ID", siteId).setId(outBean.getId());
        }
        if (null != upBean.getId() && 0 < upBean.getId().length()) {
            ServDao.save(ServMgr.SY_COMM_BBS_TOPIC, upBean);
        }
    }
    
    /**
     * 评论量 +1
     * @param param - 参数bean
     * @return out bean
     */
    public OutBean increaseCommentCounter(ParamBean param) {
        BBSMgr.getInstance().increaseReadCounter(param);
        return new OutBean(param).setOk();
    }

    /**
     * 阅读量 +1
     * @param param - 参数bean
     * @return out bean
     */
    public OutBean increaseReadCounter(ParamBean param) {
        BBSMgr.getInstance().increaseReadCounter(param);
        return new OutBean(param).setOk();
    }
    
    /**
     * 回帖
     * @param param - 参数bean
     * @return out bean
     */
    public OutBean reply(ParamBean param) {
        //评论回复
        OutBean result = new  CommentServ().reply(param);
        
        //评论量+1
        String pk = param.getStr("DATA_ID");
        BBSMgr.getInstance().increaseCommentCounter(new Bean().setId(pk));
        return result;
    }
    
    /**
     * 取得包含指定条数的最新主题列表、最新评论列表、最热主题列表的Bean
     * @param paramBean 含有"_ROWNUM_"值的参数Bean
     * @return 返回包含最新主题列表、最新评论列表、最热主题列表的Bean
     */
    public Bean getNewAndHotMsg(Bean paramBean) {
        Bean outBean = new Bean();
        
        outBean.set("hot", BBSMgr.getInstance().getHotTopics(paramBean));
        outBean.set("reply", BBSMgr.getInstance().getNewReplyTopics(paramBean));
        outBean.set("new", BBSMgr.getInstance().getNewTopics(paramBean));
        return outBean;
    }
    /**
     * 取得包含指定条数的最热主题列表的Bean
     * @param paramBean 含有"_ROWNUM_"值的参数Bean
     * @return 返回最热主题列表的Bean
     */
    public Bean getHotTopics(Bean paramBean) {
        Bean outBean = new Bean();
        outBean.set("hot", BBSMgr.getInstance().getHotTopics(paramBean));
        return outBean;
    }
    /**
     * 取得包含指定条数的最新评论列表的Bean
     * @param paramBean 含有"_ROWNUM_"值的参数Bean
     * @return 返回包含最新评论列表的Bean
     */
    public Bean getNewReplyTopics(Bean paramBean) {
        Bean outBean = new Bean();
        outBean.set("reply", BBSMgr.getInstance().getNewReplyTopics(paramBean));
        return outBean;
    }
    /**
     * 取得包含指定条数的最新主题列表的Bean
     * @param paramBean 含有"_ROWNUM_"值的参数Bean
     * @return 返回包含最新主题列表的Bean
     */
    public Bean getNewTopics(Bean paramBean) {
        Bean outBean = new Bean();
        outBean.set("new", BBSMgr.getInstance().getNewTopics(paramBean));
        return outBean;
    }
    /**
     * 根据一级栏目id，取得下面二级栏目信息（回复数，主题数）
     * @param paramBean 含有一级栏目id的Bean
     * @return 返回一级栏目下二级栏目信息
     */
    public Bean getListByChannel(Bean paramBean) {
        return BBSMgr.getInstance().getListByChannel(paramBean);
    }
    
    /**
     * 根据站点ID和显示栏目数目，取得相应二级栏目列表，每一个二级栏目下带三级栏目列表，三级栏目下带主题列表
     * @param paramBean 带有站点ID的参数Bean
     * @return 返回站点下三级栏目及栏目主题列表
     */
    public Bean getChannelTopic(Bean paramBean) {
        paramBean.set(Constant.PARAM_SELECT, "CHNL_NAME,SITE_ID");
        paramBean.set("SERV_ID", "SY_COMM_BBS");
        paramBean.set("CHNL_LEVEL", 2);
        
        if (paramBean.isEmpty("SITE_ID")) {
            paramBean.set("SITE_ID", "SY_COMM_CMS");
        }
        
        //查询指定数目的栏目信息
        List<Bean> channelList = ServDao.finds(ServMgr.SY_COMM_BBS_CHNL, paramBean);
        
        //遍历栏目id，查询栏目下文档信息
        for (Bean b:channelList) {
            BBSMgr.getInstance().getListByChannel(b);
        }
        return new Bean().set(Constant.RTN_DATA, channelList);
    }
    
    /**
     * 根据站点ID取得站点下二级栏目及栏目主题列表
     * @param paramBean 带有站点ID的参数Bean
     * @return 返回站点下二级栏目及主题列表
     */
    public Bean getTopicList(Bean paramBean) {
        paramBean.set(Constant.PARAM_SELECT, "CHNL_NAME,SITE_ID");
        paramBean.set("SERV_ID", "SY_COMM_BBS");
        paramBean.set("CHNL_LEVEL", 2);
        
        if (paramBean.isEmpty("SITE_ID")) {
            paramBean.set("SITE_ID", "SY_COMM_CMS");
        }
        
        //查询指定数目的栏目信息
        List<Bean> channelList = ServDao.finds(ServMgr.SY_COMM_BBS_CHNL, paramBean);
        
        //遍历栏目id，查询栏目下文档信息
        for (Bean b:channelList) {
            Bean topicBean = new Bean();
            topicBean.set(Constant.PARAM_SELECT, 
                    "TOPIC_TITLE,COMMENT_COUNTER,S_USER,S_UNAME,S_CTIME,TOPIC_READ_COUNTER");
            topicBean.set("TOPIC_CHECKED", 1);
            topicBean.set("CHNL_ID", b.getId());
            topicBean.set(Constant.PARAM_ORDER, " S_CTIME desc");
            List<Bean> topicList = ServDao.finds(ServMgr.SY_COMM_BBS_TOPIC, topicBean);
            b.set("topicList", topicList);
        }
        return new Bean().set(Constant.RTN_DATA, channelList);
    }
    /**
     * 获取论坛发帖量
     * @param paramBean 参数
     * @return 查询结果
     */
    public Bean getTopicNumByUser(ParamBean paramBean) {
        ParamBean queryBean = new ParamBean();
        queryBean.setServId(ServMgr.SY_COMM_BBS_TOPIC);
        queryBean.setSelect("S_USER,COUNT(TOPIC_ID) as TOPIC_NUM");
        queryBean.setWhere("GROUP BY S_USER");
        queryBean.setOrder("TOPIC_NUM DESC");
        queryBean.setAct(ServMgr.ACT_FINDS);
        //查询第一条
        ParamBean queryTopic = new ParamBean();
        queryTopic.setServId(ServMgr.SY_COMM_BBS_TOPIC);
        queryTopic.setSelect("TOPIC_ID,TOPIC_TITLE");
        queryTopic.setShowNum(1);
        queryTopic.setAct(ServMgr.ACT_FINDS);
        
        OutBean result = ServMgr.act(queryBean);
        List<Bean> list = result.getDataList();
        for (Bean bean : list) {
            UserBean user = UserMgr.getUser(bean.getStr("S_USER"));
            bean.set("USER", user);
            queryTopic.set("S_USER", bean.getStr("S_USER"));
            OutBean resultTopic = ServMgr.act(queryTopic);
            List<Bean> topicList = resultTopic.getDataList();
            bean.set("TOPIC", topicList.get(0));
        }
        return result;
    }    
}
