package com.rh.core.comm.zhidao;

import com.rh.core.serv.OutBean;
import com.rh.core.serv.ParamBean;
import com.rh.core.serv.ServMgr;
import com.rh.core.serv.comment.CommentServ;

/**
 * 提问 评论 服务类
 * @author liwei
 * 
 */
public class QuestionCommentServ extends CommentServ {

    /**
     * 保存评论后，更新评论次数
     * @param param - 参数bean
     * @return - 结果bean
     */
    public OutBean reply(ParamBean param) {
        //保存评论
        OutBean outBean = super.reply(param);
        
        
        //更新评论次数
         outBean = updateCommentCount(param);
        return outBean;
    }
    
    /**
     * 更新评论次数
     * @param param - 参数bean
     * @return - out bean
     */
    public OutBean updateCommentCount(ParamBean param) {
        // select comment count
        ParamBean queryCount = new ParamBean(ServMgr.SY_SERV_COMMENT, ServMgr.ACT_COUNT);
        queryCount.set("SERV_ID", QuestionServ.ASK_SERVER);
        queryCount.set("DATA_ID", param.getStr("DATA_ID"));
        int count = ServMgr.act(queryCount).getCount();
        
        //update
        ParamBean upBean = new ParamBean(QuestionServ.ASK_SERVER, ServMgr.ACT_SAVE);
        upBean.setId(param.getStr("DATA_ID"));
        upBean.set("Q_COMMENT_COUNTER", count);
        ServMgr.act(upBean);
        
        //response
        OutBean outBean = new OutBean();
        outBean.set("Q_COMMENT_COUNTER", count);
        outBean.setOk();
        return outBean;
    }
    

}
