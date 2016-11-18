package com.rh.core.comm.news.mgr;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.rh.core.base.Bean;
import com.rh.core.base.Context;
import com.rh.core.comm.cms.mgr.ChannelMgr;
import com.rh.core.comm.cms.mgr.ExtendsField;
import com.rh.core.org.UserBean;
import com.rh.core.serv.OutBean;
import com.rh.core.serv.ParamBean;
import com.rh.core.serv.ServDao;
import com.rh.core.serv.ServMgr;
import com.rh.core.serv.bean.SqlBean;
import com.rh.core.serv.comment.CommentServ;
import com.rh.core.util.Constant;
import com.rh.core.util.Lang;

/**
 * 新闻
 * @author chensheng
 * 
 */
public class NewsMgr {
    // 日志记录
    @SuppressWarnings("unused")
    private static Log log = LogFactory.getLog(NewsMgr.class);

    private static NewsMgr instance = new NewsMgr();

    private static final String SERV_NEWS = ServMgr.SY_COMM_NEWS;
    
    
    // 栏目ID
    private static final String CHNL_ID = "CHNL_ID";

    /** 所有继承关系字段对象集合 */
    public static final List<ExtendsField> EXTENDS_FIELDS = new ArrayList<ExtendsField>();
    static {
        // 栏目下内容模版继承
        ExtendsField newsTmpl = new ExtendsField();
        newsTmpl.setIdField("TMPL_ID");
        // newsTmpl.setNameField("TMPL_NAME");
        newsTmpl.setExtendsField("TMPL_EXTENDS");
        newsTmpl.setParentIdField("CHNL_CONTENT_TMPL");
        // newsTmpl.setParentNameField("CHNL_CONTENT_TMPL_NAME");
        EXTENDS_FIELDS.add(newsTmpl);

        // 栏目下内容评论状态继承
        ExtendsField comment = new ExtendsField();
        comment.setIdField("NEWS_COMMENT_STATUS");
        comment.setExtendsField("NEWS_COMMENT_EXTENDS");
        comment.setParentIdField("CHNL_CONTENT_COMMENT_STATUS");
        EXTENDS_FIELDS.add(comment);

        // 新闻继承栏目公开范围
        // ExtendsField newsScope = new ExtendsField();
        // newsScope.setIdField("NEWS_SCOPE");
        // newsScope.setExtendsField("NEWS_SCOPE_EXTENDS");
        // newsScope.setParentIdField("CHNL_SCOPE");
        // EXTENDS_FIELDS.add(newsScope);

        // 新闻继承栏目可查看人
        // ExtendsField newsOwner = new ExtendsField();
        // newsOwner.setIdField("NEWS_OWNER");
        // newsOwner.setExtendsField("NEWS_OWNER_EXTENDS");
        // newsOwner.setParentIdField("CHNL_OWNER");
        // EXTENDS_FIELDS.add(newsOwner);

        // 新闻继承栏目审核级别
        // ExtendsField newsCheck = new ExtendsField();
        // newsCheck.setIdField("NEWS_CHECK");
        // newsCheck.setExtendsField("NEWS_CHECK_EXTENDS");
        // newsCheck.setParentIdField("CHNL_CHECK");
        // EXTENDS_FIELDS.add(newsCheck);

        // 新闻继承栏目审核人
        // ExtendsField newsChecker = new ExtendsField();
        // newsChecker.setIdField("NEWS_CHECKER");
        // newsChecker.setExtendsField("NEWS_CHECKER_EXTENDS");
        // newsChecker.setParentIdField("CHNL_CHECKER");
        // EXTENDS_FIELDS.add(newsChecker);
    }
    
    /**
     * 菜单传的字典__extwhere
     */
    public static final String DICT_EXTWHERE = "@com.rh.core.comm.news.InfosChnlDict__EXTWHERE";
    /**
     * 退回修改
     */
    public static final int BACKMODITY = 2;

    /**
     * 审核中
     */
    public static final int CHECKING = 3;
    /**
     * 审核不通过
     */
    public static final int NOPASS = 4;

    /**
     * 审核通过
     */
    public static final int CHECKED = 5;
    /**
     * 发布
     */
    public static final int POST = 6;
    /**
     * 全系统
     */
    public static final int IN_SYSTEM = 1;
    /**
     * 本级公司
     */
    public static final int IN_ORG = 2;
    /**
     * 本部门
     */
    public static final int IN_TDEPT = 3;
    /**
     * 业务条线
     */
    public static final int IN_BUSINESS = 4;
    /**
     * 角色
     */
    public static final int IN_ROLE = 5;
    /**
     * 简单审核
     */
    public static final int SIMPLE_CHECK = 1;
    /**
     * 不审核
     */
    public static final int NO_CHECK = 2;

    /**
     * 标识 审核类型 1：审核人
     */
    public static final int CHECK_TYPE = 1;
    /**
     * 无审核人
     */
    public static final int CHECK_TYPE_NO = 0;

    /**
     * 流程审核
     */
    public static final int FLOW_CHECK = 3;
    /**
     * 公开范围
     */
    public static final String COL_NEWS_SCOPE = "NEWS_SCOPE";

    /**
     * 可查看人
     */
    public static final String COL_NEWS_OWNER = "NEWS_OWNER";
    /**
     * 审核级别
     */
    public static final String COL_NEWS_CHECK = "NEWS_CHECK";
    /**
     * 审核人
     */
    public static final String COL_NEWS_CHECKER = "NEWS_CHECKER";
    /**
     * 栏目服务
     */
    public static final String SY_COMM_INFOS_CHNL = "SY_COMM_INFOS_CHNL";

    /**
     * Singleton
     * @return - Singleton instance
     */
    public static NewsMgr getInstance() {
        return instance;
    }

    /**
     * can not new instance
     */
    private NewsMgr() {
    }

    /**
     * 取得最新评论和与之关联的新闻
     * @param paramBean - 参数bean
     * @return 评论列表
     */
    public List<Bean> getNewsCommentList(Bean paramBean) {

        String count = paramBean.getStr("count");
        String siteId = paramBean.getStr("SITE_ID");
        if (siteId.length() == 0) {
            siteId = "SY_COMM_CMS";
        }

        ParamBean bean = new ParamBean(ServMgr.SY_SERV_COMMENT)
                .setSelect("DATA_ID,C_CONTENT,S_CTIME,S_USER,C_ID,SERV_ID")
                .setQueryPageOrder("S_CTIME desc")
                .setQueryExtWhere(" and SERV_ID='" + ServMgr.SY_COMM_NEWS + "' and C_STATUS=1");

        if (count != null && count.length() > 0) {
            bean.setQueryPageShowNum(Integer.parseInt(count));
        }
        List<Bean> commentList = new CommentServ().query(bean).getDataList();
        StringBuilder sb = new StringBuilder("( ");
        for (Bean b : commentList) {
            sb.append("'" + b.getStr("DATA_ID") + "',");
        }
        // sb.deleteCharAt(sb.length() - 1).append(")");
        sb.append("'-1')");
        List<Bean> newsList = ServDao.finds(ServMgr.SY_COMM_NEWS,
                " and NEWS_ID in " + sb.toString());
        // " and SITE_ID='" + siteId + "' and NEWS_ID in " + sb.toString());
        for (int i = 0; i < commentList.size(); i++) {
            for (int j = 0; j < newsList.size(); j++) {
                if (commentList.get(i).getStr("DATA_ID").equals(newsList.get(j).getStr("NEWS_ID"))) {
                    commentList.get(i).set("news", newsList.get(j));
                    break;
                }
            }
        }
        // 评论摘要
        for (Bean cb : commentList) {
            String comment = cb.getStr("C_CONTENT");
            String summary = Lang.getSummary(comment, 30);
            cb.set("C_CONTENT", summary);
        }

        return commentList;
    }

    /**
     * 根据新闻id取得新闻信息、附件信息、图片集、投票
     * @param paramBean - 参数bean
     * @return 新闻及附属信息
     */
    public Bean getNewsAttach(Bean paramBean) {
        String newsId = paramBean.getStr("newsId");
        String picCount = paramBean.getStr("picCount");
        String attachCount = paramBean.getStr("attachCount");
        String pollCount = paramBean.getStr("pollCount");

        Bean news = new Bean(newsId);
        // 图片集
        if (picCount != null && picCount.length() > 0) {
            Bean tpjQuery = new Bean().set("SERV_ID", ServMgr.SY_COMM_NEWS)
                    .set("DATA_ID", news.getId()).set("FILE_CAT", "TUPIANJI")
                    .set(Constant.PARAM_SELECT, "FILE_ID").set(Constant.PARAM_ORDER, "S_MTIME desc");

            if (!"-1".equals(picCount)) {
                tpjQuery.set(Constant.PARAM_ROWNUM, picCount);
            }
            List<Bean> imgList = ServDao.finds(ServMgr.SY_COMM_FILE, tpjQuery);
            news.set("imgList", imgList);
        }

        // 附件
        if (attachCount != null && attachCount.length() > 0) {
            Bean attachBean = new Bean().set("SERV_ID", ServMgr.SY_COMM_NEWS)
                    .set("DATA_ID", newsId).set("FILE_CAT", "XINWENFUJIAN").set(Constant.PARAM_ORDER, "S_MTIME desc");
            List<Bean> attachList = ServDao.finds(ServMgr.SY_COMM_FILE, attachBean);
            if (!"-1".equals(attachCount)) {
                attachBean.set(Constant.PARAM_ROWNUM, attachCount);
            }
            news.set("attachList", attachList);
        }
        // 投票
        if (pollCount != null && pollCount.length() > 0) {
            Bean pollBean = new Bean().set("SERV_ID", ServMgr.SY_COMM_NEWS)
                    .set("DATA_ID", newsId);
            List<Bean> pollList = ServDao.finds(ServMgr.SY_COMM_POLL, pollBean);
            if (!"-1".equals(pollCount)) {
                pollBean.set(Constant.PARAM_ROWNUM, pollCount);
            }
            if (!pollList.isEmpty()) {
                news.set("pollId", pollList.get(0).getId());
            } else {
                news.set("pollId", "");
            }

        }
        return news;
    }
    /**
     * 根据中华信息id取得新闻信息、附件信息、图片集、投票 id【信息审签单和信息】
     * @param paramBean - 参数bean
     * @return 新闻及附属信息
     */
    public Bean getInfosAttach(Bean paramBean) {
        String newsId = paramBean.getStr("newsId");
        String picCount = paramBean.getStr("picCount");
        String attachCount = paramBean.getStr("attachCount");
        String pollCount = paramBean.getStr("pollCount");
        //动态获取serv 服务
        String servid = paramBean.getStr("servid");
        String auditid = paramBean.getStr("auditid");
        Bean news = new Bean();
        if (servid.equals(ServMgr.SY_COMM_INFOS)) {
            news = new Bean(newsId);
        } else {
             servid = ServMgr.SY_COMM_INFOS_AUDIT;
             news = new Bean(auditid);
        }
        // 图片集
        if (picCount != null && picCount.length() > 0) {
            Bean tpjQuery = new Bean().set("DATA_ID", news.getId()).set("FILE_CAT", "TUPIANJI")
                    .set(Constant.PARAM_SELECT, "FILE_ID").set(Constant.PARAM_ORDER, "S_MTIME asc");

            if (!"-1".equals(picCount)) {
                tpjQuery.set(Constant.PARAM_ROWNUM, picCount);
            }
            List<Bean> imgList = ServDao.finds(ServMgr.SY_COMM_FILE, tpjQuery);
            news.set("imgList", imgList);
        }

        // 附件
        if (attachCount != null && attachCount.length() > 0) {
            SqlBean attachBean = new SqlBean();
            attachBean.and("DATA_ID", news.getId())
                    .and("FILE_CAT", "FUJIAN").orders("FILE_SORT asc, S_MTIME asc");
            List<Bean> attachList = ServDao.finds(ServMgr.SY_COMM_FILE, attachBean);
            if (!"-1".equals(attachCount)) {
                attachBean.set(Constant.PARAM_ROWNUM, attachCount);
            }
            news.set("attachList", attachList);
        }
        // 投票
        if (pollCount != null && pollCount.length() > 0) {
            Bean pollBean = new Bean().set("SERV_ID", servid)
                    .set("DATA_ID", news.getId());
            List<Bean> pollList = ServDao.finds(ServMgr.SY_COMM_POLL, pollBean);
            if (!"-1".equals(pollCount)) {
                pollBean.set(Constant.PARAM_ROWNUM, pollCount);
            }
            if (!pollList.isEmpty()) {
                news.set("pollId", pollList.get(0).getId());
            } else {
                news.set("pollId", "");
            }

        }
        return news;
    }

    /**
     * 根据条件取得相应的新闻
     * @param paramBean - 参数bean
     * @return 新闻列表
     */
    public List<Bean> getNewsList(Bean paramBean) {
        StringBuffer str = new StringBuffer();
        String siteId = paramBean.getStr("SITE_ID");
        String channelId = paramBean.getStr("channelId");
        String newsType = paramBean.getStr("newsType");
        String count = paramBean.getStr("count");
        String titleImg = paramBean.getStr("titleImg");
        String select = paramBean.getStr(Constant.PARAM_SELECT);

        Bean bean = new Bean().set("NEWS_CHECKED", 1)
                .set(Constant.PARAM_ORDER, "NEWS_SORT asc,NEWS_TIME desc");
        if (select != null && select.length() > 0) {
            bean.set(Constant.PARAM_SELECT, select);
        }
        if (siteId != null && siteId.length() > 0) {
            str.append(" and SITE_ID = '" + siteId + "'");
        }
        if (channelId != null && channelId.length() > 0) {
            str.append(" and CHNL_ID = '" + channelId + "'");
        }
        if (newsType != null && newsType.length() > 0) {
            str.append(" and NEWS_TYPE = '" + newsType + "'");
        }
        if (count != null && count.length() > 0) {
            bean.set(Constant.PARAM_ROWNUM, count);
        }
        if ("true".equals(titleImg)) {
            str.append(" and NEWS_TITLE_IMAGE is not null");
        }
        bean.set(Constant.PARAM_WHERE, str.toString());
        List<Bean> newsList = ServDao.finds("SY_COMM_NEWS", bean);
        return newsList;
    }

    /**
     * get channel bean from cache and database(cache first)
     * @param id - channel pk
     * @return - cache bean
     */
    public Bean getNews(String id) {
        Bean result = ServDao.find(SERV_NEWS, new Bean().setId(id));
        setExtendValues(result);
        return result;
    }

    /**
     * +阅读次数 并返回当前新闻阅读次数
     * @param param 入参
     * @return currentHis 
     */
    public Bean increaseReadCounter(Bean param) {
        // String key = "NEWS_READ_COUNTER";
        // Bean news = ServDao.find(ServMgr.SY_COMM_NEWS, param);
        // news.set(key, news.get(key, 0) + 1);
        // ServDao.update(ServMgr.SY_COMM_NEWS, news);

        // 更新阅读历史
//        UserBean userbean = Context.getUserBean();
//        if (null == userbean) {
//            return userbean;
//            
//        }
//        String currentUser = userbean.getCode();
        Bean queryBean = new Bean();
        queryBean.set("NEWS_ID", param.getId());
       // queryBean.set("USER_CODE", currentUser);
        Bean currentHis = new Bean();
        currentHis.set("NEWS_ID", param.getId());
        currentHis = ServDao.find(ServMgr.SY_COMM_INFOS, queryBean);
        if (null != currentHis) {
            int currentCounter = currentHis.get("COUNTER", 0);
            currentHis.set("COUNTER", currentCounter + 1);
            ServDao.save(ServMgr.SY_COMM_INFOS, currentHis);
        } 
        return currentHis;
    }

    /**
     * set extend field value
     * @param newsBean - news bean
     */
    public void setExtendValues(Bean newsBean) {
        for (ExtendsField extendsField : EXTENDS_FIELDS) {
            setExtendValue(newsBean, extendsField);
        }
    }

    /**
     * set news extends value
     * @param newsBean - news bean
     * @param field - extends fields properties
     */
    private void setExtendValue(Bean newsBean, ExtendsField field) {
        String chnlId = newsBean.getStr(CHNL_ID);
        String idField = field.getIdField();
        String nameField = field.getNameField();
        String extendsField = field.getExtendsField();

        if (null == chnlId || 0 == chnlId.length()) {
            return;
        }
        if (newsBean.isNotEmpty(extendsField)) {
            return;
        }
        if (newsBean.isEmpty(idField)) {
            Bean parent = ChannelMgr.getInstance().getChannel(chnlId);
            newsBean.set(extendsField, 1);
            newsBean.set(idField, parent.getStr(field.getParentIdField()));
            if (null != nameField) {
                newsBean.set(nameField, parent.getStr(field.getParentNameField()));
            }
        } else {
            newsBean.set(extendsField, 2);
        }
    }

    /****************************** 新闻调整 *******************************/
    /**
     * 取得普通新闻
     * @param paramBean 参数Bean
     * @return 默认返回普通新闻列表的Bean
     */
    public Bean getGeneralNews(Bean paramBean) {
        Bean outBean = new Bean();
        Bean bean = new Bean().set("NEWS_TYPE", 1);
        bean.set("NEWS_CHECKED", 1);
        bean.set(Constant.PARAM_SELECT,
                "NEWS_SUBJECT,NEWS_TIME,NEWS_SUMMARY,S_UNAME");
        bean.set(Constant.PARAM_ORDER, "NEWS_SORT asc,NEWS_TIME desc");
        bean.putAll(paramBean);

        outBean.set(Constant.RTN_DATA, ServDao.finds(ServMgr.SY_COMM_NEWS, bean));
        return outBean;
    }
    /**
     * 取得普通新闻
     * @param paramBean 参数Bean
     * @return 默认返回普通新闻列表的Bean
     */
    public Bean getGeneralInfos(Bean paramBean) {
    	ParamBean param = new ParamBean();
    	Bean outBean = new Bean();
        int image = paramBean.getInt("HAS_IMAGE");
        int newstype = paramBean.getInt("NEWS_TYPE");
        String siteid = paramBean.getStr("SITE_ID");
        String extwhere = "";
        if (paramBean.isNotEmpty("_PCHNL_ID_")) {
        	String pid = paramBean.getStr("_PCHNL_ID_");
        	extwhere = " AND CHNL_PID ='" + pid + "'  AND SITE_ID ='" + siteid + "' AND HAS_IMAGE = " + image + " " 
        			+  " AND NEWS_TYPE = " + newstype;
        } else if(paramBean.isNotEmpty("CHNL_ID")){
        	String chnlId = paramBean.getStr("CHNL_ID");
        	extwhere = " AND CHNL_ID='"+ chnlId +"' AND SITE_ID='" + siteid + "'  AND HAS_IMAGE =" + image + " AND NEWS_TYPE = " + newstype;
        } else{
        	extwhere = " AND SITE_ID='" + siteid + "'  AND HAS_IMAGE =" + image + " AND NEWS_TYPE = " + newstype;
        } 
        param.setQueryExtWhere(extwhere);
        if(paramBean.getStr("_ORDER_").length()>0){
        	param.setOrder(paramBean.getStr("_ORDER_"));
        }
        if(paramBean.getInt("_ROWNUM_")>0){
        	param.setShowNum(paramBean.getInt("_ROWNUM_"));
        }
        if(paramBean.getInt("NOWPAGE")>0){
        	param.setNowPage(paramBean.getInt("NOWPAGE"));
        }
        if(paramBean.getInt("SHOWNUM")>0){
        	param.setShowNum(paramBean.getInt("SHOWNUM"));
        }
        //取父栏目下的所有信息，如果没有父栏目就取所有的信息例如总公司
        outBean = ServMgr.act("SY_COMM_INFOS_VIEW", "query", param);
        return outBean;
    }
    /**
     * 取得标题新闻
     * @param paramBean 参数Bean
     * @return 返回带有标题图片的新闻
     */
    public Bean getTitleNews(Bean paramBean) {
        Bean outBean = new Bean();
        String str = "";
        if (paramBean.isNotEmpty("SITE_ID")) {
            str = " and SITE_ID='" + paramBean.getStr("SITE_ID") + "'";
        }
        paramBean.set(Constant.PARAM_WHERE,
                str + " and NEWS_TYPE=1 and NEWS_CHECKED=1 and NEWS_TITLE_IMAGE is not null");
        paramBean.set(Constant.PARAM_SELECT,
                "NEWS_SUBJECT,NEWS_SUMMARY,NEWS_TITLE_IMAGE");
        outBean = getGeneralNews(paramBean);

        // 处理标题图片
        for (Object o : outBean.getList(Constant.RTN_DATA)) {
            Bean b = (Bean) o;
            String imgUrl = b.getStr("NEWS_TITLE_IMAGE");
            if (imgUrl.contains(",")) {
                b.set("NEWS_TITLE_IMAGE", imgUrl.substring(0, imgUrl.indexOf(",")));
                System.out.println(0);
            }
        }
        return outBean;
    }

    /**
     * 取得图文(图片+标题)
     * @param paramBean 参数Bean
     * @return 返回新闻列表(带有图片id)
     */
    public Bean getTuWenNews(Bean paramBean) {
        Bean outBean = new Bean();

        // 1.取得图文类型新闻
        if (paramBean.isEmpty("NEWS_TYPE")) {
            paramBean.set("NEWS_TYPE", 3);
        }
        if(paramBean.isEmpty("HAS_IMAGE")){
        	paramBean.set("HAS_IMAGE", 1);
        }
        
        //paramBean.set(Constant.PARAM_SELECT, "NEWS_SUBJECT");
        //新闻引用调整
        Bean out = getGeneralInfos(paramBean);
        List<Object> newsList = out.getList(Constant.RTN_DATA);
        int length = newsList.size();
        String chnlName="";
        // 2.遍历取得新闻图片
        for (Object o : newsList) {
            Bean b = (Bean) o;
            chnlName = b.getStr("CHNL_NAME");
            Bean bean = new Bean().set("DATA_ID", b.getId());
            if(paramBean.getInt("HAS_FENGMIAN")==1){
            	bean.set("FILE_CAT", "FENGMIANJI");
            }else{
            	bean.set("FILE_CAT", "TUPIANJI");
            }
            bean.set(Constant.PARAM_SELECT, "FILE_ID");
            bean.set(Constant.PARAM_ROWNUM, "1");
            //添加获取新闻图片排序,默认升序，保证每次显示的是第一张上传的图片
            bean.set(Constant.PARAM_ORDER, "S_MTIME ASC");
            List<Bean> pictureList = ServDao.finds(ServMgr.SY_COMM_FILE, bean);
            if (!pictureList.isEmpty()) {
                if (!pictureList.get(0).isEmpty()) {
                b.set("picture", pictureList.get(0));
                }
            }
        }
        outBean.set("CHNL_NAME", chnlName);
        outBean.set("CHNL_ID", paramBean.getStr("CHNL_ID"));
        outBean.set("ALLNUM", out.getBean("_PAGE_").get("ALLNUM"));
        return outBean.set(Constant.RTN_DATA, newsList);
    }

    /**
     * 取得焦点(图片+标题)
     * @param paramBean 参数Bean
     * @return 返回新闻列表(带有图片id)
     */
    public Bean getJiaoDianNews(Bean paramBean) {
        if (paramBean.isEmpty("NEWS_TYPE")) {
            paramBean.set("NEWS_TYPE", 2);
        }
        return getTuWenNews(paramBean);
    }

    /**
     * 获取父栏目
     * @param chnlId 栏目主键
     * @return 返回栏目，如果没有则返回null
     */
    public static Bean getChannel(String chnlId) {
        if (StringUtils.isNotBlank(chnlId)) {
            return ServDao.find(ServMgr.SY_COMM_CMS_CHNL, chnlId);
        }
        return null;
    }

    /**
     * 获取栏目的公开范围，没有则找父栏目，直到最后一个父栏目 如果还没有则默认全系统公开
     * @param chnlId 栏目ID
     * @return 范围公开范围
     */
    public static int getChannelScope(String chnlId) {
        Bean chnlBean = getChannel(chnlId);
        if (chnlBean != null) {
            int scope = chnlBean.getInt("CHNL_SCOPE");
            if (scope == 0) {
                getChannelScope(chnlBean.getStr("CHNL_PID"));
            } else {
                return scope;
            }
        }
        return 1;
    }

    /**
     * 获取栏目的可查看人，没有则找父栏目，直到最后一个父栏目 如果还没有则默认空
     * @param chnlId 栏目ID
     * @return 范围公开范围
     */
    public static String getChannelOwner(String chnlId) {
        Bean chnlBean = getChannel(chnlId);
        if (chnlBean != null) {
            String owner = chnlBean.getStr("CHNL_OWNER");
            if (StringUtils.isBlank(owner)) {
                getChannelOwner(chnlBean.getStr("CHNL_PID"));
            } else {
                return owner;
            }
        }
        return "";
    }

    /**
     * 获取栏目的审核类型，没有则找父栏目，直到最后一个父栏目 如果还没有则不审核
     * @param chnlId 栏目ID
     * @return 审核类型
     */
    public static int getChannelCheck(String chnlId) {
        Bean chnlBean = getChannel(chnlId);
        if (chnlBean != null) {
            int check = chnlBean.getInt("CHNL_CHECK");
            if (check == 0) {
                getChannelCheck(chnlBean.getStr("CHNL_PID"));
            } else {
                return check;
            }
        }
        return 2;
    }

    /**
     * 获取栏目的审核人，没有则找父栏目，直到最后一个父栏目 如果还没有则返回空
     * @param chnlId 栏目ID
     * @return 审核人
     */
    public static String getChannelChecker(String chnlId) {
        Bean chnlBean = getChannel(chnlId);
        if (chnlBean != null) {
            String checker = chnlBean.getStr("CHNL_CHECKER");
            if (StringUtils.isBlank(checker)) {
                getChannelChecker(chnlBean.getStr("CHNL_PID"));
            } else {
                return checker;
            }
        }
        return "";
    }

    /**
     * 获取我可查看的信息的查询SQL
     * @return 返回查询SQL
     */
    public static String getMyNewsSql() {
        StringBuffer strBuf = new StringBuffer();
        UserBean userBean = Context.getUserBean();
        // 本级公司CODE
        String odeptCode = userBean.getODeptCode();
        // 本部门CODE
        String tdeptCode = userBean.getTDeptCode();
        String userName = userBean.getCode();
        // 我具有的角色
        String roles = userBean.getRoleCodeStr();
        roles = "'" + roles.replaceAll(",", "','") + "'";

        strBuf.append(" and (");

        strBuf.append(" NEWS_SCOPE=").append(IN_SYSTEM);
        // 本级公司的信息
        strBuf.append(" or (NEWS_SCOPE=").append(IN_ORG).append(" and S_ODEPT='").append(odeptCode)
          .append("'").append(" or NEWS_USER='").append(userName).append("')");
        // 本部门的信息
        strBuf.append(" or (NEWS_SCOPE=").append(IN_TDEPT).append(" and S_TDEPT='").append(tdeptCode)
        .append("'").append(" or NEWS_USER='").append(userName).append("')");
        // 我所属角色能查看的信息
        strBuf.append(" or (NEWS_SCOPE=").append(IN_ROLE).append(" and NEWS_OWNER in (").append(roles)
        .append(")").append(" or NEWS_USER='").append(userName).append("')");
        // todo 我所属的业务条线能查看的信息
        // todo 我所属的群组能查看的信息

        strBuf.append(")");

        return strBuf.toString();
    }
   
    /**
     * 获取有查看权限的Sql
     * @return 查询sql
     */
     public static String getAclSql() {
        StringBuffer strBuf = new StringBuffer();
        UserBean userBean = Context.getUserBean();  
       
        // 本级公司
        String odeptCode = userBean.getODeptCode();
        // 本部门CODE
        String tdeptCode = userBean.getTDeptCode();
        //当前用户CODE
        String userCode = userBean.getCode();
        //业务条线
        String tdeptline = userBean.getTDeptBean().getLine();
        // 具有的角色
        String roles = userBean.getRoleCodeStr();
        roles = "'" + roles.replaceAll(",", "','") + "'";
        strBuf.append(" AND  NEWS_ID IN (SELECT DATA_ID FROM SY_COMM_NEWS_ACL WHERE OWNER_CODE IN (").append("'");
        strBuf.append(userCode).append("','").append(odeptCode).append("','").append(tdeptCode).append("','")
        .append(userCode).append("','").append(tdeptline).append("',").append(roles);
        strBuf.append("))");
        return strBuf.toString();
    }
}
