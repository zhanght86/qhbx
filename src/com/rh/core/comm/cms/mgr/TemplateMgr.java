package com.rh.core.comm.cms.mgr;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.rh.core.base.Bean;
import com.rh.core.base.Context;
import com.rh.core.comm.bbs.directive.ChannelLevelDirective;
import com.rh.core.comm.bbs.directive.ChannelMessageDirective;
import com.rh.core.comm.bbs.directive.HotTopicDirective;
import com.rh.core.comm.bbs.directive.NewReplyTopicDirective;
import com.rh.core.comm.bbs.directive.NewTopicDirective;
import com.rh.core.comm.bbs.directive.PollDirective;
import com.rh.core.comm.bbs.directive.TopicDirective;
import com.rh.core.comm.bbs.directive.TopicNewCommentDirective;
import com.rh.core.comm.cms.directive.ChannelDirective;
import com.rh.core.comm.cms.directive.ChannelHomeDirective;
import com.rh.core.comm.cms.directive.ChannelNavigationDirective;
import com.rh.core.comm.cms.directive.CurrentUserDirective;
import com.rh.core.comm.cms.directive.GetComsHtmlDirective;
import com.rh.core.comm.cms.directive.SiteLinkerDirective;
import com.rh.core.comm.cms.directive.TextCutDirective;
import com.rh.core.comm.cms.directive.UserDirective;
import com.rh.core.comm.news.directive.ChnlNewsListDirective;
import com.rh.core.comm.news.directive.InfosAttachDirective;
import com.rh.core.comm.news.directive.NewCommentDirective;
import com.rh.core.comm.news.directive.NewsAttachDirective;
import com.rh.core.comm.news.directive.NewsListDirective;
import com.rh.core.comm.wenku.directive.DoclistListAboutDocDirective;
import com.rh.core.comm.wenku.directive.DoclistListDirective;
import com.rh.core.comm.wenku.directive.DoclistListReadTopDirective;
import com.rh.core.comm.wenku.directive.DoclistReadTopDirective;
import com.rh.core.comm.wenku.directive.UserOtherDocDirective;
import com.rh.core.comm.wenku.directive.WenJiDocslistListDirective;
import com.rh.core.comm.wenku.directive.WenKuDownloadTopDirective;
import com.rh.core.comm.wenku.directive.WenKuListDirective;
import com.rh.core.comm.wenku.directive.WenKuReadTopDirective;
import com.rh.core.comm.wenku.directive.WenKuScoreTopDirective;
import com.rh.core.comm.wenku.directive.WenKuTotalDirective;
import com.rh.core.comm.wenku.directive.WenkuIntegralTopDirective;
import com.rh.core.comm.wenku.directive.WenkuNoticeDirective;
import com.rh.core.comm.wenku.directive.WenkuNoticeListDirective;
import com.rh.core.comm.wenku.directive.WenkuUserInfoDirective;
import com.rh.core.comm.zhidao.directive.AskListDirective;
import com.rh.core.comm.zhidao.directive.ZhidaoAnswerLikeVoteListDirective;
import com.rh.core.comm.zhidao.directive.ZhidaoAnswerListDirective;
import com.rh.core.comm.zhidao.directive.ZhidaoAskCountDirective;
import com.rh.core.comm.zhidao.directive.ZhidaoAskDirective;
import com.rh.core.comm.zhidao.directive.ZhidaoAskListDirective;
import com.rh.core.comm.zhidao.directive.ZhidaoChannelCountDirective;
import com.rh.core.comm.zhidao.directive.ZhidaoFavoritesListDirective;
import com.rh.core.comm.zhidao.directive.ZhidaoFollowDirective;
import com.rh.core.comm.zhidao.directive.ZhidaoFollowListDirective;
import com.rh.core.comm.zhidao.directive.ZhidaoFollowPersonDirective;
import com.rh.core.comm.zhidao.directive.ZhidaoHotListDirective;
import com.rh.core.comm.zhidao.directive.ZhidaoIntegralTopByChnlDirective;
import com.rh.core.comm.zhidao.directive.ZhidaoIntegralTopDirective;
import com.rh.core.comm.zhidao.directive.ZhidaoMyFollowQuestionDirective;
import com.rh.core.comm.zhidao.directive.ZhidaoNoticeDirective;
import com.rh.core.comm.zhidao.directive.ZhidaoNoticeListDirective;
import com.rh.core.comm.zhidao.directive.ZhidaoOnlySpecialistDirective;
import com.rh.core.comm.zhidao.directive.ZhidaoOnlySpecialistSubjectDirective;
import com.rh.core.comm.zhidao.directive.ZhidaoQuesShareCountDirective;
import com.rh.core.comm.zhidao.directive.ZhidaoQuestionAskFollowCounterDirective;
import com.rh.core.comm.zhidao.directive.ZhidaoQuestionFollowCounterDirective;
import com.rh.core.comm.zhidao.directive.ZhidaoRecommendListDirective;
import com.rh.core.comm.zhidao.directive.ZhidaoSpecialistSubjectDirective;
import com.rh.core.comm.zhidao.directive.ZhidaoThreeSpecDirective;
import com.rh.core.comm.zhidao.directive.ZhidaoTopicListDirective;
import com.rh.core.comm.zhidao.directive.ZhidaoUserAdoptionRateDirective;
import com.rh.core.comm.zhidao.directive.ZhidaoUserAllIntegralDirective;
import com.rh.core.comm.zhidao.directive.ZhidaoUserDescDirective;
import com.rh.core.comm.zhidao.directive.ZhidaoUserDirective;
import com.rh.core.comm.zhidao.directive.ZhidaoUserSubjectDirective;
import com.rh.core.comm.zhidao.directive.ZhidaoWorkmateShareDirective;
import com.rh.core.serv.ServDao;
import com.rh.core.serv.ServMgr;

import freemarker.cache.StringTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.Template;
import freemarker.template.TemplateException;

/**
 * template manager
 * @author liwei
 * 
 */
public class TemplateMgr {

    /** tmplate service id */
    private static final String TMPL_SERVICE = ServMgr.SY_COMM_NEWS_TMPL;
    private static Log log = LogFactory.getLog(TemplateMgr.class);
    private static TemplateMgr instance = new TemplateMgr();
    private static Configuration cfg = null;

    // private static String ftlPath = Context.appStr(Context.APP.SYSPATH) + "sy/comm/news/ftl/";
    private static String ftlPath = Context.appStr(Context.APP.SYSPATH) + "sy/comm/cms/ftl/";

    private static Configuration localFileCfg = null;

    // 模板源码
    private static final String COL_TMPL_SOURCE = "TMPL_SOURCE";

    static {
        cfg = new Configuration();
        cfg.setTemplateLoader(new StringTemplateLoader());
        cfg.setObjectWrapper(new DefaultObjectWrapper());
        cfg.setDefaultEncoding("UTF-8");

        localFileCfg = new Configuration();
        try {
            localFileCfg.setDirectoryForTemplateLoading(new File(ftlPath));
        } catch (IOException e) {
            e.printStackTrace();
        }
        localFileCfg.setObjectWrapper(new DefaultObjectWrapper());
        // 默认编码
        localFileCfg.setDefaultEncoding("UTF-8");
        // 处理变量中的null值,自动转为空字符串
        localFileCfg.setClassicCompatible(true);

        // cms公共
        localFileCfg.setSharedVariable("channel_list", new ChannelHomeDirective());
        localFileCfg.setSharedVariable("channel_navigation", new ChannelNavigationDirective());
        localFileCfg.setSharedVariable("channel", new ChannelDirective());
        localFileCfg.setSharedVariable("linker_list", new SiteLinkerDirective());
        localFileCfg.setSharedVariable("text_cut", new TextCutDirective());
        localFileCfg.setSharedVariable("coms_html", new GetComsHtmlDirective());
        localFileCfg.setSharedVariable("current_user", new CurrentUserDirective());
        localFileCfg.setSharedVariable("user", new UserDirective());

        // 新闻宏
        localFileCfg.setSharedVariable("news_list", new NewsListDirective());
        localFileCfg.setSharedVariable("news_chnlnews_list", new ChnlNewsListDirective());
        localFileCfg.setSharedVariable("news_attach", new NewsAttachDirective());
        localFileCfg.setSharedVariable("news_comment", new NewCommentDirective());
        // 中华信息宏
        localFileCfg.setSharedVariable("infos_attach", new InfosAttachDirective());

        // 文库宏
        localFileCfg.setSharedVariable("wenku_list", new WenKuListDirective());
        localFileCfg.setSharedVariable("wenku_user_info", new WenkuUserInfoDirective());
        localFileCfg.setSharedVariable("other_list", new UserOtherDocDirective());
        localFileCfg.setSharedVariable("wenku_total", new WenKuTotalDirective());
        localFileCfg.setSharedVariable("wenku_readTop", new WenKuReadTopDirective());
        localFileCfg.setSharedVariable("wenku_downloadTop", new WenKuDownloadTopDirective());
        localFileCfg.setSharedVariable("wenku_scoreTop", new WenKuScoreTopDirective());
        localFileCfg.setSharedVariable("wenku_doclist_list", new DoclistListDirective());
        localFileCfg.setSharedVariable("wenku_doclist_readTop", new DoclistListReadTopDirective());
        localFileCfg.setSharedVariable("wenku_doclist_aboutDoc", new DoclistListAboutDocDirective());
        localFileCfg.setSharedVariable("wenji_docs_list", new WenJiDocslistListDirective());
        localFileCfg.setSharedVariable("doclist_readTop", new DoclistReadTopDirective());
        localFileCfg.setSharedVariable("wenku_integralTop", new WenkuIntegralTopDirective());
        localFileCfg.setSharedVariable("wenku_notice_list", new WenkuNoticeListDirective());
        localFileCfg.setSharedVariable("wenku_notice", new WenkuNoticeDirective());

        // 知道宏
        localFileCfg.setSharedVariable("ask_list", new AskListDirective());
        localFileCfg.setSharedVariable("ask_count", new ZhidaoAskCountDirective());
        localFileCfg.setSharedVariable("ask_count", new ZhidaoAskCountDirective());
        localFileCfg.setSharedVariable("zhidao_notice_list", new ZhidaoNoticeListDirective());
        localFileCfg.setSharedVariable("zhidao_notice", new ZhidaoNoticeDirective());
        localFileCfg.setSharedVariable("zhidao_integralTop", new ZhidaoIntegralTopDirective());
        localFileCfg.setSharedVariable("zhidao_integralTopByChnl", new ZhidaoIntegralTopByChnlDirective());
        localFileCfg.setSharedVariable("zhidao_user_allIntegral", new ZhidaoUserAllIntegralDirective());
        localFileCfg.setSharedVariable("zhidao_user_adoptionRate", new ZhidaoUserAdoptionRateDirective());
        localFileCfg.setSharedVariable("zhidao_specialist_subject", new ZhidaoSpecialistSubjectDirective());
        localFileCfg.setSharedVariable("zhidao_only_specialist_subject", new ZhidaoOnlySpecialistSubjectDirective());
        localFileCfg.setSharedVariable("zhidao_topic_list", new ZhidaoTopicListDirective());
        localFileCfg.setSharedVariable("zhidao_hotlist", new ZhidaoHotListDirective());
        localFileCfg.setSharedVariable("zhidao_ask", new ZhidaoAskDirective());
        localFileCfg.setSharedVariable("zhidao_ask_list", new ZhidaoAskListDirective());
        localFileCfg.setSharedVariable("zhidao_favorites_list", new ZhidaoFavoritesListDirective());
        localFileCfg.setSharedVariable("zhidao_answer_list", new ZhidaoAnswerListDirective());
        localFileCfg.setSharedVariable("zhidao_follow_list", new ZhidaoFollowListDirective());
        localFileCfg.setSharedVariable("zhidao_vote_list", new ZhidaoAnswerLikeVoteListDirective());
        localFileCfg.setSharedVariable("zhidao_follow", new ZhidaoFollowDirective());
        localFileCfg.setSharedVariable("zhidao_recommend_list", new ZhidaoRecommendListDirective());
        localFileCfg.setSharedVariable("zhidao_question_follow_counter", new ZhidaoQuestionFollowCounterDirective());
        localFileCfg.setSharedVariable("zhidao_question_ask_follow_counter",
                new ZhidaoQuestionAskFollowCounterDirective());
        localFileCfg.setSharedVariable("zhidao_channel_count", new ZhidaoChannelCountDirective());
        localFileCfg.setSharedVariable("zhidao_workmate_share", new ZhidaoWorkmateShareDirective());
        localFileCfg.setSharedVariable("zhidao_three_spec", new ZhidaoThreeSpecDirective());
        localFileCfg.setSharedVariable("zhidao_only_specialist", new ZhidaoOnlySpecialistDirective());
        localFileCfg.setSharedVariable("zhidao_follow_person", new ZhidaoFollowPersonDirective());
        localFileCfg.setSharedVariable("zhidao_my_follow_question", new ZhidaoMyFollowQuestionDirective());
        localFileCfg.setSharedVariable("zhidao_user_desc", new ZhidaoUserDescDirective());
        localFileCfg.setSharedVariable("zhidao_user_subject", new ZhidaoUserSubjectDirective());
        localFileCfg.setSharedVariable("zhidao_user", new ZhidaoUserDirective());
        localFileCfg.setSharedVariable("zhidao_ques_share_count", new ZhidaoQuesShareCountDirective());
        

        // BBS宏
        localFileCfg.setSharedVariable("channel_msg", new ChannelMessageDirective());
        localFileCfg.setSharedVariable("topic_list", new TopicDirective());
        localFileCfg.setSharedVariable("new_topic_list", new NewTopicDirective());
        localFileCfg.setSharedVariable("reply_topic_list", new NewReplyTopicDirective());
        localFileCfg.setSharedVariable("hot_topic_list", new HotTopicDirective());
        localFileCfg.setSharedVariable("poll_list", new PollDirective());
        localFileCfg.setSharedVariable("topic_new_comment_list", new TopicNewCommentDirective());
        localFileCfg.setSharedVariable("bbs_channel_level", new ChannelLevelDirective());

    }

    /**
     * Singleton
     * @return - instance
     */
    public static TemplateMgr getInstance() {
        return instance;
    }

    /**
     * can not new instance
     */
    private TemplateMgr() {

    }

    /**
     * 生成模板文件
     * @param paramBean 前端参数bean
     * @param dataBean 数据库里的bean
     */
    public static void createTmpl(Bean paramBean, Bean dataBean) {
        File ftlFile = new File(getTmplFilePath(paramBean));
        // 如果存在则生成模板文件
        if (paramBean.contains(COL_TMPL_SOURCE)) {
            if (ftlFile.exists()) { // 该模板文件存在则删除
                ftlFile.delete();
            }
        }

        if (!ftlFile.exists()) {
            // 写入文件
            writeFile(ftlFile, dataBean.getStr(COL_TMPL_SOURCE));
        }

    }

    /**
     * get tmpl file String
     * @param paramBean - tmpl bean
     * @return tmpl file string
     */
    public static String getTmplString(Bean paramBean) {
        String tmplPath = paramBean.getStr("TMPL_PATH");
        String servId = paramBean.getStr("SERV_ID");
        String path = servId + File.separator + tmplPath;
        String filePath = ftlPath + path;
        File ftlFile = new File(filePath);
        String result = "";
        if (ftlFile.exists()) {
            InputStream is = null;
            try {
                is = new FileInputStream(ftlFile);
                result = IOUtils.toString(is, "UTF-8");
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                IOUtils.closeQuietly(is);
            }
        }
        return result;
    }

    /**
     * 写入文件
     * @param ftlFile 模板文件
     * @param fileStr 文件内容
     */
    private static void writeFile(File ftlFile, String fileStr) {
        if (!ftlFile.getParentFile().exists()) {
            ftlFile.getParentFile().mkdir();
        }
        // 写文件
        Writer bw = null;
        try {
            bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(ftlFile.getPath()), "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            log.info(e.getMessage());
        } catch (FileNotFoundException e) {
            log.info(e.getMessage());
        }

        try {
            bw.write(fileStr);
            bw.flush();
            bw.close();
        } catch (IOException e) {
            log.info(e.getMessage());
        }
    }

    /**
     * get Html String
     * @param tmplId - template id
     * @param root - params
     * @param writer - writer we will close it
     */
    public void buildHtml(String tmplId, Map<String, Object> root, Writer writer) {
        if (null == tmplId || 0 == tmplId.length()) {
            throw new RuntimeException(" 栏目没有配置模版!");
        }
        Bean tmpl = ServDao.find(TMPL_SERVICE, new Bean().setId(tmplId));
        String servId = tmpl.getStr("SERV_ID");
        String tmplPath = tmpl.getStr("TMPL_PATH");
        String path = servId + File.separator + tmplPath;

        buildHtmlFromLocalTmpl(path, root, writer);
    }

    /**
     * get html string
     * @param templateStr - template
     * @param root - root map\
     * @param writer - writer
     */
    public void getHtmlFromTmplString(String templateStr, Map<String, Object> root, Writer writer) {
        try {
            Template t = new Template("name", new StringReader(templateStr), cfg);
            t.process(root, writer);
        } catch (IOException ioe) {
            log.error("template error", ioe);
        } catch (TemplateException e) {
            log.error("template error", e);
        } finally {
            // writer.flush();
            IOUtils.closeQuietly(writer);
        }
    }

    /**
     * get Html String
     * @param tmplId - template id
     * @param root - params
     * @param writer - writer
     */
    public void buildHtmlFromLocalTmpl(String tmplId, Map<String, Object> root, Writer writer) {
        try {
            Template tmpl = localFileCfg.getTemplate(tmplId, "UTF-8");
            tmpl.process(root, writer);
        } catch (TemplateException e) {
            log.info("模板和数据模型合并失败：" + e);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            // writer.flush();
            IOUtils.closeQuietly(writer);
        }
    }

    /**
     *  获取模板的全路径
     * @param paramBean 参数Bean
     * @return 返回模板文件全路径
     */
    public static String getTmplFilePath(Bean paramBean) {
        Bean dataBean = ServDao.find(TMPL_SERVICE, paramBean);
        String tmplPath = dataBean.getStr("TMPL_PATH");
        String servId = dataBean.getStr("SERV_ID");
        String path = servId + File.separator + tmplPath;
        String filePath = ftlPath + path;
        return filePath;
    }

}
