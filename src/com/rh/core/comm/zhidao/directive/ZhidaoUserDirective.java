package com.rh.core.comm.zhidao.directive;

import com.rh.core.comm.cms.directive.DirectiveUtils;
import com.rh.core.org.UserBean;
import com.rh.core.serv.ParamBean;
import com.rh.core.comm.zhidao.ZhidaoServ;
import freemarker.core.Environment;
import freemarker.template.ObjectWrapper;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateDirectiveModel;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class ZhidaoUserDirective
  implements TemplateDirectiveModel
{
  private static Log log = LogFactory.getLog(ZhidaoUserDirective.class);

  public void execute(Environment paramEnvironment, Map paramMap, TemplateModel[] paramArrayOfTemplateModel, TemplateDirectiveBody paramTemplateDirectiveBody)
    throws TemplateException, IOException
  {
    String str1 = DirectiveUtils.getString("debugName", paramMap);
    String str2 = DirectiveUtils.getString("userId", paramMap);

    log.debug(str1 + ":开始");

    ParamBean localParamBean = new ParamBean();
    localParamBean.set("userId", str2);

    UserBean localUserBean = new ZhidaoServ().getZhidaoUser(localParamBean);

    HashMap localHashMap = new HashMap(paramMap);
    localHashMap.put("user", ObjectWrapper.DEFAULT_WRAPPER.wrap(localUserBean));
    Map localMap = DirectiveUtils.addParamsToVariable(paramEnvironment, localHashMap);
    if (paramTemplateDirectiveBody != null) {
      paramTemplateDirectiveBody.render(paramEnvironment.getOut());
    }
    DirectiveUtils.removeParamsFromVariable(paramEnvironment, localHashMap, localMap);
    log.debug(str1 + ":结束");
  }
}