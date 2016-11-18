package com.rh.core.comm.zhidao.directive;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.rh.core.base.Bean;
import com.rh.core.comm.cms.directive.DirectiveUtils;
import com.rh.core.org.UserBean;
import com.rh.core.org.mgr.UserMgr;
import com.rh.core.serv.OutBean;
import com.rh.core.serv.ParamBean;
import com.rh.core.serv.ServMgr;
import com.rh.core.serv.dict.DictMgr;
import com.rh.core.util.DateUtils;

import freemarker.core.Environment;
import freemarker.template.ObjectWrapper;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateDirectiveModel;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;

/**
 * 专家
 * @author liwei
 *
 */
public class ZhidaoSpecialistDirective implements TemplateDirectiveModel {
    /** log */
    private static Log log = LogFactory.getLog(ZhidaoSpecialistDirective.class);
    
    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Override
    public void execute(Environment env, Map params,
            TemplateModel[] loopVars, TemplateDirectiveBody body)
            throws TemplateException, IOException {
        String debugName = DirectiveUtils.getString("debugName", params);
        int count = DirectiveUtils.getInt("count", params);
        String order = DirectiveUtils.getString("order", params);
        
        log.debug(debugName + ":开始");
        
        ParamBean param = new ParamBean(ServMgr.SY_COMM_ZHIDAO_SPECIALIST, ServMgr.ACT_FINDS);
        param.setSelect(" USER_ID,SPEC_SORT");
       
        if (count > 0) {
            param.setShowNum(count);
        } else {
            param.setShowNum(15);
        }
        if (order != null && order.length() > 0) {
            param.setOrder(order);
        }
        param.setOrder("SPEC_SORT");
        List<Bean> result = new ArrayList<Bean>();
        List<Bean> list = ServMgr.act(param).getDataList();
        
        //获取上周回答问题个数
        ParamBean queryQuestionBean = new ParamBean(ServMgr.SY_COMM_ZHIDAO_ANSWER, ServMgr.ACT_COUNT);
        
        //获取专家领域
        ParamBean querySpecBean = new ParamBean(ServMgr.SY_COMM_ZHIDAO_SPEC_SUBJECT, ServMgr.ACT_FINDS);
        querySpecBean.setSelect("CHNL_ID");
        
        for (Bean b : list) {
           String uId = b.getStr("USER_ID");
           UserBean user = UserMgr.getUser(uId);
           
           //获取上周日期
           Date lastWeek = DateUtils.addWeeks(DateUtils.getDateFromString(DateUtils.getFirstDateOfWeek()), -1);
           Date lastSunWeek = DateUtils.addDays(lastWeek, 6);
           String lastWeekStr =  DateUtils.getStringFromDate(lastWeek, DateUtils.FORMAT_TIMESTAMP);
           String lastSunWeekStr =  DateUtils.getStringFromDate(lastSunWeek, DateUtils.FORMAT_TIMESTAMP);
           queryQuestionBean.setWhere("and S_USER='" + b.getStr("USER_ID") 
                   + "' and S_ATIME >'" + lastWeekStr + "' and S_ATIME < '" + lastSunWeekStr + "'");
           OutBean questionOutBean = ServMgr.act(queryQuestionBean);
           user.set("A_COUNTER", questionOutBean.getInt("_OKCOUNT_"));
           
           //获取专家领域
           querySpecBean.set("SPEC_ID", b.getStr("USER_ID"));
           OutBean specBean = ServMgr.act(querySpecBean);
           List<Bean> specListBean = specBean.getDataList();
           StringBuffer speclists = new StringBuffer();
           for (int i = 0; i < specListBean.size(); i++) {
               Bean spectBean  = specListBean.get(i);
               String chnlName = DictMgr.getFullName("SY_COMM_ZHIDAO_CHNL_MANAGE", spectBean.getStr("CHNL_ID"));
               if (i == specListBean.size() - 1) {
                   speclists.append(chnlName);
               } else {
                   speclists.append(chnlName + ",");
               }
               
           }
          
           user.set("SPEC_SUB", speclists);
           result.add(user);
        }
        list.clear();

        Map<String, TemplateModel> paramWrap = new HashMap<String, TemplateModel>(params);
        paramWrap.put("tag_list", ObjectWrapper.DEFAULT_WRAPPER.wrap(result));
        Map<String, TemplateModel> origMap = DirectiveUtils.addParamsToVariable(env, paramWrap);
        if (body != null) {
            body.render(env.getOut());
        }
        DirectiveUtils.removeParamsFromVariable(env, paramWrap, origMap);
        log.debug(debugName + ":结束");

    }
}
