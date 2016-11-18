package com.rh.core.comm.news.serv;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.rh.core.base.Bean;
import com.rh.core.comm.cms.mgr.ChannelMgr;
import com.rh.core.comm.news.mgr.HtmlParser;
import com.rh.core.comm.news.mgr.NewsMgr;
import com.rh.core.serv.CommonServ;
import com.rh.core.serv.OutBean;
import com.rh.core.serv.ParamBean;
import com.rh.core.serv.ServDao;
import com.rh.core.serv.ServMgr;
import com.rh.core.util.Constant;
import com.rh.core.util.DateUtils;

/**
 * 消息发布的service，重载beforeSave，用于处理新闻主体里的远程图片
 * @author zhanghailong
 * 
 */
public class NewsServ extends CommonServ {

    /**
     * 获取file中的每一条新闻中图文的个数
     */
    private final String picCount = "1";

    /**
     * 查询新闻
     * @param paramBean 参数Bean
     * @return 查询结果
     */
    public OutBean byid(ParamBean paramBean) {
        OutBean result = super.byid(paramBean);
        NewsMgr.getInstance().setExtendValues(result);

        return result;
    }

    @Override
    protected void beforeSave(ParamBean paramBean) {
        String body = paramBean.getStr("NEWS_BODY");
        if (paramBean.contains("NEWS_BODY")) {
            body = new HtmlParser().parse(body);
            paramBean.set("NEWS_BODY", body);
        }
        if (!paramBean.contains("TMPL_ID") && paramBean.contains("TMPL_NAME")) {
            paramBean.remove("TMPL_NAME");
        }

        if (paramBean.getAddFlag()) {
            String chnlId = paramBean.getStr("CHNL_ID");

            // 新添加时栏目如果为空则继承父栏目的公开范围和可查看人
            int newsScope = paramBean.getInt("NEWS_SCOPE");
            if (newsScope == 0) {
                paramBean.set("NEWS_SCOPE", NewsMgr.getChannelScope(chnlId));
                paramBean.set("NEWS_OWNER", NewsMgr.getChannelOwner(chnlId));
            }

            // 新添加时栏目如果为空则继承父栏目的审核类型和审核人
            int newsCheck = paramBean.getInt("NEWS_CHECK");
            if (newsCheck == 0) {
                paramBean.set("NEWS_CHECK", NewsMgr.getChannelCheck(chnlId));
                paramBean.set("NEWS_CHECKER", NewsMgr.getChannelChecker(chnlId));
            }
        }
    }

    @Override
    protected void afterSave(ParamBean paramBean, OutBean outBean) {

        // 自动填写摘要、短标题
        String body = outBean.getStr("NEWS_BODY");
        body = new HtmlParser().parse(body);
        Bean upBean = new Bean();
        if (outBean.isEmpty("SITE_ID")) {
            String siteId = ChannelMgr.getInstance().getSiteId(outBean.getStr("CHNL_ID"));
            upBean.set("SITE_ID", siteId).setId(outBean.getId());
        }

        // 是否有图
        Bean imgQuery = new Bean().set("SERV_ID", ServMgr.SY_COMM_NEWS).set("DATA_ID", outBean.getId());
        imgQuery.set("FILE_CAT", "TUPIANJI");
        int imgCountTuPian = ServDao.count(ServMgr.SY_COMM_FILE, imgQuery);
        imgQuery.set("FILE_CAT", "FENGMIANJI");
        int imgCountFengMian = ServDao.count(ServMgr.SY_COMM_FILE, imgQuery);
        if (imgCountTuPian > 0 || imgCountFengMian > 0) {
            upBean.set("HAS_IMAGE", 1).setId(outBean.getId());
        }

        // 是否有附件
        Bean attachQuery = new Bean().set("SERV_ID", ServMgr.SY_COMM_NEWS).set("DATA_ID", outBean.getId());
        attachQuery.set("FILE_CAT", "FUJIAN");
        int aCount = ServDao.count(ServMgr.SY_COMM_FILE, attachQuery);
        if (aCount > 0) {
            upBean.set("HAS_ATTACH", 1).setId(outBean.getId());
        }

        if (null != upBean.getId() && 0 < upBean.getId().length()) {
            ServDao.save("SY_COMM_NEWS", upBean);
        }
    }


    /**
     * 获取新闻的精彩图文和新闻标题 @author hdy 2013年1月17日17:06:46
     * @param paramBean 参数 count 获取前几条
     * @return 填充后的Bean集合
     */
    public Bean getNewsChosenPic(Bean paramBean) {
        // 设置查询项
        paramBean.set(Constant.PARAM_SELECT, "NEWS_ID,NEWS_SUBJECT,SITE_ID");
        NewsMgr news = NewsMgr.getInstance(); // 实例化新闻消息
        List<Bean> newsList = news.getNewsList(paramBean); // 获取新闻记录
        List<Bean> outList = new ArrayList<Bean>(); // 设置输出集合
        for (Bean b : newsList) { // 遍历新闻集合，获取响应图文路径
            List<Bean> imgList = news.getNewsAttach(new Bean().set("newsId", b.getId()).set("picCount", picCount))
                    .getList("imgList");
            Bean imgBean = null;
            if (imgList.size() > 0) {
                imgBean = (Bean) imgList.get(0);
                b.set("NEWS_ID", imgBean.getStr("FILE_ID"));
                outList.add(b);
            }
            if (outList.size() == getNewsCount(paramBean)) {
                break;
            }
        }
        return new Bean().set(Constant.RTN_DATA, outList); // 返回数据集合
    }

    /**
     * 获取新闻评论
     * @param paramBean 参数 count 获取前几条
     * @return 123
     */
    public Bean getNewsComment(Bean paramBean) {
        NewsMgr news = NewsMgr.getInstance();
        List<Bean> list = new ArrayList<Bean>();
        paramBean.set("count", getNewsCount(paramBean));
        list = news.getNewsCommentList(paramBean);
        String regEx = "<.+?>"; // 表示含有html标签的正则表达式
        Pattern p = Pattern.compile(regEx);
        for (Bean b : list) {
            Matcher m = p.matcher(b.getStr("C_CONTENT"));
            if (m.replaceAll("").trim().equals("")) {
                b.set("C_CONTENT", "[null]");
            } else {
                b.set("C_CONTENT", m.replaceAll(""));
            }
            b.set("S_CTIME", DateUtils.getDateFromString(b.getStr("S_CTIME"), "yyyy-MM-dd HH:mm"));
        }
        //log.debug(JsonUtils.toJson(list));
        return new Bean().set(Constant.RTN_DATA, list);
    }

    /**
     * 新闻标题图文
     * @param paramBean 参数 count 展示条数
     * @return 返回结果集
     */
    public Bean getNewsTitle(Bean paramBean) {
        NewsMgr news = NewsMgr.getInstance(); // 实例化新闻消息
        // 设置查询项
        paramBean.set(Constant.PARAM_SELECT, "NEWS_SUBJECT,NEWS_TITLE_IMAGE,NEWS_SUMMARY,HAS_IMAGE,HAS_ATTACH")
                // 设置标题图文为显示
                .set("count", getNewsCount(paramBean));
        // 返回数据结果集
        return new Bean().set(Constant.RTN_DATA, news.getNewsList(paramBean));
    }

    /**
     * 判断传入参数count，新闻条数是否为空和赋默认值
     * @param bean 传入参数
     * @return count值
     */
    private int getNewsCount(Bean bean) {
        int count = 0;
        if (bean.isEmpty("COUNT")) {
            count = 5;
        } else {
            count = new Integer(bean.getStr("COUNT")).intValue();
        }
        return count;
    }

    /**
     * 取得普通新闻
     * @param paramBean 参数Bean
     * @return 默认返回普通新闻列表的Bean
     */
    public Bean getGeneralNews(Bean paramBean) {
        return NewsMgr.getInstance().getGeneralNews(paramBean);
    }

    /**
     * 取得标题新闻
     * @param paramBean 参数Bean
     * @return 返回带有标题图片的新闻
     */
    public Bean getTitleNews(Bean paramBean) {
        return NewsMgr.getInstance().getTitleNews(paramBean);
    }

    /**
     * 取得图文(图片+标题)
     * @param paramBean 参数Bean
     * @return 返回新闻列表(带有图片id)
     */
    public Bean getTuWenNews(Bean paramBean) {
        return NewsMgr.getInstance().getTuWenNews(paramBean);
    }

    /**
     * 取得焦点(图片+标题)
     * @param paramBean 参数Bean
     * @return 返回新闻列表(带有图片id)
     */
    public Bean getJiaoDianNews(Bean paramBean) {
        return NewsMgr.getInstance().getJiaoDianNews(paramBean);
    }

    /**
     * 信息批量审核 保存相关的审核状态
     * @param param 参数
     * @return 返回值
     */
    public OutBean batchCheck(ParamBean param) {
        OutBean out = new OutBean();
        Bean bean = new Bean();
        String str = param.getStr(Constant.KEY_ID);
        String[] strdata = str.split(",");
        List<String> data = new ArrayList<String>();
        Bean countBean = new Bean();
        for (int i = 0; i < strdata.length; i++) {
            String[] pkcontent = param.getStr(strdata[i]).split(",");
            if (!pkcontent[0].equals("2")) {
                bean.set(Constant.KEY_ID, strdata[i]);
                bean.set("NEWS_CHECKED", NewsMgr.CHECKING);
                Bean rtbean = ServDao.save(param.getStr("serv"), bean);
                countBean.set("SERV_ID", param.getStr("serv"));
                countBean.set("DATA_ID", strdata[i]);
                countBean.set("CHECKER_ID", pkcontent[3]);
                countBean.set("CK_TYPE", NewsMgr.CHECK_TYPE);
                countBean.set("S_ATIME", pkcontent[1]);
                countBean.set("CK_DATE", pkcontent[2]);
                countBean.set("CK_STATE", NewsMgr.CHECKING);
                countBean.set("NEWS_CHECK", NewsMgr.SIMPLE_CHECK);
                ServDao.save("SY_COMM_NEWS_COUNT", countBean);
                data.add(rtbean.getStr(Constant.KEY_ID));

            } else {
                bean.set(Constant.KEY_ID, strdata[i]);
                bean.set("NEWS_CHECKED", NewsMgr.POST);
                bean.set("NEWS_TIME", pkcontent[2]);
                Bean rtbean = ServDao.save(param.getStr("serv"), bean);
                countBean.set("SERV_ID", param.getStr("serv"));
                countBean.set("DATA_ID", strdata[i]);
                countBean.set("CHECKER_ID", "");
                countBean.set("CK_TYPE", NewsMgr.CHECK_TYPE_NO);
                countBean.set("S_ATIME", pkcontent[1]);
                countBean.set("CK_DATE", pkcontent[2]);
                countBean.set("CK_STATE", NewsMgr.POST);
                countBean.set("NEWS_CHECK", NewsMgr.NO_CHECK);
                ServDao.save("SY_COMM_NEWS_COUNT", countBean);
                data.add(rtbean.getStr(Constant.KEY_ID));
            }

        }
        if (data.size() > 0 && data.size() == strdata.length) {

            out.set(Constant.RTN_MSG_OK, "提交审核成功");
        } else {
            out.set(Constant.RTN_MSG_ERROR, "提交审核失败");
        }

        return out;

    }

    /**
     * 批量退回修改 保存相关的状态
     * @param param 参数
     * @return 返回值
     */
    public OutBean backModify(ParamBean param) {
        OutBean out = new OutBean();
        Bean bean = new Bean();
        String str = param.getStr(Constant.KEY_ID);
        String[] strdata = str.split(",");
        List<String> data = new ArrayList<String>();
        Bean countBean = new Bean();
        for (int i = 0; i < strdata.length; i++) {
            String[] pkcontent = param.getStr(strdata[i]).split(",");
            if (pkcontent[0].equals("1")) {
                bean.set(Constant.KEY_ID, strdata[i]);
                bean.set("NEWS_CHECKED", NewsMgr.BACKMODITY);
                Bean rtbean = ServDao.save(param.getStr("serv"), bean);
                countBean.set("SERV_ID", param.getStr("serv"));
                countBean.set("DATA_ID", strdata[i]);
                countBean.set("CHECKER_ID", pkcontent[3]);
                countBean.set("CK_TYPE", NewsMgr.CHECK_TYPE);
                countBean.set("S_ATIME", pkcontent[1]);
                countBean.set("CK_DATE", pkcontent[2]);
                countBean.set("CK_STATE", NewsMgr.BACKMODITY);
                countBean.set("NEWS_CHECK", NewsMgr.SIMPLE_CHECK);
                ServDao.save("SY_COMM_NEWS_COUNT", countBean);
                data.add(rtbean.getStr(Constant.KEY_ID));

            } else {
                // 流程审核
            }

        }
        if (data.size() > 0 && data.size() == strdata.length) {

            out.set(Constant.RTN_MSG_OK, "退回成功");
        } else {
            out.set(Constant.RTN_MSG_ERROR, "退回失败");
        }

        return out;

    }

    /**
     * 批量审核通过 保存相关的状态
     * @param param 参数
     * @return 返回值
     */
    public OutBean checkPass(ParamBean param) {
        OutBean out = new OutBean();
        Bean bean = new Bean();
        String str = param.getStr(Constant.KEY_ID);
        String[] strdata = str.split(",");
        List<String> data = new ArrayList<String>();
        Bean countBean = new Bean();
        for (int i = 0; i < strdata.length; i++) {
            String[] pkcontent = param.getStr(strdata[i]).split(",");
            if (pkcontent[0].equals("1")) {
                bean.set(Constant.KEY_ID, strdata[i]);
                bean.set("NEWS_CHECKED", NewsMgr.POST);
                bean.set("NEWS_TIME", pkcontent[2]);
                Bean rtbean = ServDao.save(param.getStr("serv"), bean);
                countBean.set("SERV_ID", param.getStr("serv"));
                countBean.set("DATA_ID", strdata[i]);
                countBean.set("CHECKER_ID", pkcontent[3]);
                countBean.set("CK_TYPE", NewsMgr.CHECK_TYPE);
                countBean.set("S_ATIME", pkcontent[1]);
                countBean.set("CK_DATE", pkcontent[2]);
                countBean.set("CK_STATE", NewsMgr.POST);
                countBean.set("NEWS_CHECK", NewsMgr.SIMPLE_CHECK);
                ServDao.save("SY_COMM_NEWS_COUNT", countBean);
                data.add(rtbean.getStr(Constant.KEY_ID));

            } else {
                // 流程审核
            }

        }
        if (data.size() > 0 && data.size() == strdata.length) {

            out.set(Constant.RTN_MSG_OK, "审核成功,同时发布");
        } else {
            out.set(Constant.RTN_MSG_ERROR, "审核失败");
        }

        return out;

    }
    /**
     * 批量审核不通过 保存相关的状态
     * @param param 参数
     * @return 返回值
     */
    public OutBean checkNoPass(ParamBean param) {
        OutBean out = new OutBean();
        Bean bean = new Bean();
        String str = param.getStr(Constant.KEY_ID);
        String[] strdata = str.split(",");
        List<String> data = new ArrayList<String>();
        Bean countBean = new Bean();
        for (int i = 0; i < strdata.length; i++) {
            String[] pkcontent = param.getStr(strdata[i]).split(",");
            if (pkcontent[0].equals("1")) {
                bean.set(Constant.KEY_ID, strdata[i]);
                bean.set("NEWS_CHECKED", NewsMgr.NOPASS);
                Bean rtbean = ServDao.save(param.getStr("serv"), bean);
                countBean.set("SERV_ID", param.getStr("serv"));
                countBean.set("DATA_ID", strdata[i]);
                countBean.set("CHECKER_ID", pkcontent[3]);
                countBean.set("CK_TYPE", NewsMgr.CHECK_TYPE);
                countBean.set("S_ATIME", pkcontent[1]);
                countBean.set("CK_DATE", pkcontent[2]);
                countBean.set("CK_STATE", NewsMgr.NOPASS);
                countBean.set("NEWS_CHECK", NewsMgr.SIMPLE_CHECK);
                ServDao.save("SY_COMM_NEWS_COUNT", countBean);
                data.add(rtbean.getStr(Constant.KEY_ID));
            } else {
                // 流程审核
            }

        }
        if (data.size() > 0 && data.size() == strdata.length) {

            out.set(Constant.RTN_MSG_OK, "审核成功");
        } else {
            out.set(Constant.RTN_MSG_ERROR, "审核失败");
        }

        return out;

    }
  /**
   * 查询栏目权限
   * @param  param 入参 
   * @return 返回值
   */
   public OutBean queryChnl(ParamBean  param) {
       OutBean outbean = new OutBean();
       Bean bean = ServDao.find(NewsMgr.SY_COMM_INFOS_CHNL, param);
       outbean.set("CHNL_CHECK", bean.getStr("CHNL_CHECK"));
       outbean.set("CHNL_CHECKER", bean.getStr("CHNL_CHECKER"));   
       return outbean;
       
   } 
   /**
    * 信息批量发布 保存相关的审核状态
    * @param param 参数
    * @return 返回值
    */
   public OutBean batchPost(ParamBean param) {
       OutBean out = new OutBean();
       Bean bean = new Bean();
       String str = param.getStr(Constant.KEY_ID);
       String[] strdata = str.split(",");
       List<String> data = new ArrayList<String>();
       Bean countBean = new Bean();
       for (int i = 0; i < strdata.length; i++) {
           String[] pkcontent = param.getStr(strdata[i]).split(",");
           if (!pkcontent[0].equals("2")) {
               bean.set(Constant.KEY_ID, strdata[i]);
               bean.set("NEWS_CHECKED", NewsMgr.POST);
               bean.set("NEWS_TIME", pkcontent[2]);
               Bean rtbean = ServDao.save(param.getStr("serv"), bean);
               countBean.set("SERV_ID", param.getStr("serv"));
               countBean.set("DATA_ID", strdata[i]);
               countBean.set("CHECKER_ID", pkcontent[3]);
               countBean.set("CK_TYPE", NewsMgr.CHECK_TYPE);
               countBean.set("S_ATIME", pkcontent[1]);
               countBean.set("CK_DATE", pkcontent[2]);
               countBean.set("CK_STATE", NewsMgr.POST);
               countBean.set("NEWS_CHECK", NewsMgr.SIMPLE_CHECK);
               ServDao.save("SY_COMM_NEWS_COUNT", countBean);
               data.add(rtbean.getStr(Constant.KEY_ID));

           } else {
               //不审核的发布
               bean.set(Constant.KEY_ID, strdata[i]);
               bean.set("NEWS_CHECKED", NewsMgr.POST);
               bean.set("NEWS_TIME", pkcontent[2]);
               Bean rtbean = ServDao.save(param.getStr("serv"), bean);
               countBean.set("SERV_ID", param.getStr("serv"));
               countBean.set("DATA_ID", strdata[i]);
               countBean.set("CHECKER_ID", "");
               countBean.set("CK_TYPE", NewsMgr.CHECK_TYPE_NO);
               countBean.set("S_ATIME", pkcontent[1]);
               countBean.set("CK_DATE", pkcontent[2]);
               countBean.set("CK_STATE", NewsMgr.POST);
               countBean.set("NEWS_CHECK", NewsMgr.NO_CHECK);
               ServDao.save("SY_COMM_NEWS_COUNT", countBean);
               data.add(rtbean.getStr(Constant.KEY_ID));
           }

       }
       if (data.size() > 0 && data.size() == strdata.length) {

           out.set(Constant.RTN_MSG_OK, "提交审核成功");
       } else {
           out.set(Constant.RTN_MSG_ERROR, "提交审核失败");
       }
       return out;
   }

   
}
