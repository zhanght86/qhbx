package com.rh.core.comm.zhidao;

import com.rh.core.base.Bean;
import com.rh.core.base.Context;
import com.rh.core.base.TipException;
import com.rh.core.comm.cms.CacheableServ;
import com.rh.core.comm.event.mgr.EventMgr;
import com.rh.core.comm.todo.TodoBean;
import com.rh.core.comm.todo.TodoUtils;
import com.rh.core.org.UserBean;
import com.rh.core.org.mgr.UserMgr;
import com.rh.core.serv.OutBean;
import com.rh.core.serv.ParamBean;
import com.rh.core.serv.ServDao;
import com.rh.core.serv.ServMgr;
import com.rh.core.serv.comment.CommentServ;

/**
 * 提问 服务类
 * @author anan
 * 
 */
public class QuestionServ extends CacheableServ {
    /**
     * 提问服务
     */
    public static final String ASK_SERVER = ServMgr.SY_COMM_ZHIDAO_QUESTION;

    /**
     * 部门内可见
     */
    public static final int SHOW_DEPT = 1;

    /**
     * 机构内可见
     */
    public static final int SHOW_ORG = 2;

    /**
     * 公共 机构外可见
     */
    public static final int SHOW_PUBLIC = 3;

    /**
     * 提问者可见
     */
    public static final int SHOW_QUSER = 4;
    
    
    @Override
    public OutBean byid(ParamBean paramBean) {
        paramBean.set("serv", ServMgr.SY_COMM_ZHIDAO_QUESTION);
        OutBean result = super.byid(paramBean);
        //TODO 从分类读取
        //result.set("TMPL_ID", "26b1CEGLB23FJNpZaDFFHW");
        if (result.getId() != null && result.getId().length() > 0) {
        	//获取栏目信息
            Bean chnlBean = ServDao.find(ServMgr.SY_COMM_CMS_CHNL, result.getStr("CHNL_ID"));
            //根据栏目信息，读取内容模板
            if (chnlBean.isEmpty()) {
            	throw new TipException("栏目不存在");
            }
            if (chnlBean.getStr("CHNL_CONTENT_TMPL").length() <= 0) {
            	throw new TipException("栏目未设置内容模板");
            }
            result.set("TMPL_ID", chnlBean.getStr("CHNL_CONTENT_TMPL"));
        }
        return result;
    }
    

    /**
     * 
     * @param paramBean 参数Bean
     * @return 设置最佳答案
     */
    public Bean getBestAns(ParamBean paramBean) {
        String qid = paramBean.getStr("Q_ID");

        // 问题
        Bean questionBean = ServDao.find(ASK_SERVER, qid);

        // 答案为最佳
        Bean answerBean = ServDao.find(AnswerServ.SY_COMM_ZHIDAO_ANSWER, questionBean.getStr("ANS_ID"));

        UserBean ansUser = UserMgr.getUser(answerBean.getStr("S_USER"));
        answerBean.set("S_USER__NAME", ansUser.getName());

        // 查询最佳答案 评论的条数
        int bestCommtCount =
                CommentServ.getCommentsCount(questionBean.getStr("ANS_ID"), "OA_ZD_ANSWER");

        answerBean.set("comCount", bestCommtCount);

        Bean rtnBean = new Bean();
        rtnBean.set("rtnBean", answerBean);

        return rtnBean;
    }

    /**
     * 邀请回答
     * @param paramBean 参数Bean
     * @return 发送状态
     */
    public Bean reqUserAnsQ(ParamBean paramBean) {
        String userCodes = paramBean.getStr("userCodes");
        String qId = paramBean.getStr("Q_ID");

        Bean qBean = ServDao.find(ASK_SERVER, qId);
        String qTitle = qBean.getStr("Q_TITLE");

        UserBean currUser = Context.getUserBean();

        int users = 0;
        for (String userCode : userCodes.split(",")) {
            //获取将存入invite服务中的信息
            String sUser = currUser.getCode();
            String targetUser = userCode;
            
            //查询服务中是否已存在同样的记录
            ParamBean selectQuery = new ParamBean();
            selectQuery.set("S_USER", sUser);
            selectQuery.set("Q_ID", qId);
            selectQuery.set("TARGET_USER", targetUser);
            selectQuery.setServId(ServMgr.SY_COMM_ZHIDAO_INVITE);
            selectQuery.setAct(ServMgr.ACT_FINDS);
            OutBean selectOutBean = ServMgr.act(selectQuery);
            
            //不存在重复数据
            if (selectOutBean.getInt("_OKCOUNT_") == 0) {
              //存入数据库
                ParamBean saveParam = new ParamBean();
                saveParam.set("S_USER", sUser);
                saveParam.set("Q_ID", qId);
                saveParam.set("TARGET_USER", targetUser);
                saveParam.setServId(ServMgr.SY_COMM_ZHIDAO_INVITE);
                saveParam.setAct(ServMgr.ACT_SAVE);
                ServMgr.act(saveParam);

                //写入代办
                TodoBean dataBean = new TodoBean();
                dataBean.setSender(currUser.getCode());
                dataBean.setTitle(currUser.getName() + " 邀请您回答：" + qTitle);
                dataBean.setCode(ASK_SERVER);
                dataBean.setCodeName("知道");
                dataBean.setObjectId1(qId);
                // dataBean.setUrl(ASK_SERVER + ".byid.do?data={_PK_:" + qId + "}");
                dataBean.setUrl("/cms/tmpl/" + qId + ".html");
                dataBean.setOwner(userCode);
                TodoUtils.insert(dataBean);
            }
            users++;
        }

        Bean rtnBean = new Bean();
        rtnBean.set("rtnStr", "success");
        rtnBean.set("msg", "已成功邀请" + users + "人回答此问题!");
        return rtnBean;
    }

    /**
     * 如果指定目标回答用户，我们将进行通知
     * @param paramBean - 参数Bean
     * @param outBean - 结果Bean
     */
    public void afterSave(ParamBean paramBean, OutBean outBean) {
        
        //邀请指定用户回答
       String targetUser = paramBean.getStr("TARGET_ASK");
       if (null != targetUser && 0 < targetUser.length()) {
       ParamBean queryParam = new ParamBean();
       queryParam.set("userCodes", targetUser);
       queryParam.set("Q_ID", paramBean.getStr("Q_ID"));
       reqUserAnsQ(queryParam);
       }
       String qTitle = outBean.getStr("Q_TITLE");
       String user = outBean.getStr("S_USER");
       
       if (paramBean.getAddFlag()) {
           //创建问题
           String act = "ZHIDAO_CREATE_ASK";
           //保存事件
           EventMgr.save(ZhidaoServ.SY_COMM_ZHIDAO, ASK_SERVER, paramBean.getId(), act, qTitle, user);
       }
    }

    /**
     * 阅读量 + 1
     * @param param - 参数bean
     */
    public void increaseReadCounter(Bean param) {
        String key = "Q_READ_COUNTER";
        Bean bean = ServDao.find(ServMgr.SY_COMM_ZHIDAO_QUESTION, param);
        bean.set(key, bean.get(key, 0) + 1);
        ServDao.update(ServMgr.SY_COMM_ZHIDAO_QUESTION, bean);
    }


    @Override
    protected String getCacheServId() {
        return ASK_SERVER;
    }

}
