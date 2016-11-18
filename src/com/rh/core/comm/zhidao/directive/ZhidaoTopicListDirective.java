package com.rh.core.comm.zhidao.directive;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.rh.core.base.Bean;
import com.rh.core.comm.cms.directive.DirectiveUtils;
import com.rh.core.org.UserBean;
import com.rh.core.org.mgr.UserMgr;
import com.rh.core.serv.ParamBean;
import com.rh.core.serv.ServMgr;
import com.rh.core.util.Lang;

import freemarker.core.Environment;
import freemarker.template.ObjectWrapper;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateDirectiveModel;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;

/**
 * 获取最新知道提问
 * @author liwei
 * 
 */
public class ZhidaoTopicListDirective implements TemplateDirectiveModel {
    /** log */
    private static Log log = LogFactory.getLog(ZhidaoTopicListDirective.class);

    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Override
    public void execute(Environment env, Map params,
            TemplateModel[] loopVars, TemplateDirectiveBody body)
            throws TemplateException, IOException {
        String debugName = DirectiveUtils.getString("debugName", params);
        int count = DirectiveUtils.getInt("count", params);

        log.debug(debugName + ":开始");

        ParamBean param = new ParamBean(ServMgr.SY_COMM_ZHIDAO_TOPIC, ServMgr.ACT_FINDS);
        if (count > 0) {
            param.setShowNum(count);
        } else {
            param.setShowNum(3);
        }
        List<Bean> topicList = ServMgr.act(param).getDataList();

        for (Bean topic : topicList) {
            //替换图片url
            String img = topic.getStr("TOPIC_IMAGE");
            if (-1 < img.indexOf(",")) {
                img = img.substring(0, img.indexOf(","));
                topic.set("TOPIC_IMAGE", img);
            }
            ParamBean itemParam = new ParamBean(ServMgr.SY_COMM_ZHIDAO_TOPIC_ITEM, ServMgr.ACT_FINDS)
                    .setOrder("ITEM_SORT asc");
            itemParam.set("TOPIC_ID", topic.getId());
            itemParam.setShowNum(4);
            List<Bean> askList = ServMgr.act(itemParam).getDataList();
            topic.set("ITEMS", askList);
            // 为第一条提问增加回答
            if (null != askList && askList.size() > 0) {
                Bean firstAsk = askList.get(0);
                ParamBean answQuery = new ParamBean(ServMgr.SY_COMM_ZHIDAO_ANSWER, ServMgr.ACT_FINDS);
                answQuery.set("Q_ID", firstAsk.getStr("Q_ID"));
                answQuery.setOrder(" A_BEST DESC");
                answQuery.setShowNum(1);
                List<Bean> answList = ServMgr.act(answQuery).getDataList();
                if (null != answList && 0 < answList.size()) {
                    Bean bestAnsw = answList.get(0);
                    String content = bestAnsw.getStr("A_CONTENT");
                    content = Lang.getSummary(content, 60);
                    bestAnsw.set("A_CONTENT", content);
                    firstAsk.set("ANSWER", bestAnsw);
                    //添加回答用户信息
                    UserBean answUser = UserMgr.getUser(bestAnsw.getStr("S_USER"));
                    bestAnsw.set("S_UNAME", answUser.getName());
                } else {
                    firstAsk.set("ANSWER", new Bean());
                }
            }

        }

        Map<String, TemplateModel> paramWrap = new HashMap<String, TemplateModel>(params);
        paramWrap.put("tag_list", ObjectWrapper.DEFAULT_WRAPPER.wrap(topicList));
        Map<String, TemplateModel> origMap = DirectiveUtils.addParamsToVariable(env, paramWrap);
        if (body != null) {
            body.render(env.getOut());
        }
        DirectiveUtils.removeParamsFromVariable(env, paramWrap, origMap);
        log.debug(debugName + ":结束");

    }
}
