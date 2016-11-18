package com.rh.oa.http;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.rh.core.base.Context;
import com.rh.core.org.UserBean;
import com.rh.core.serv.OutBean;
import com.rh.core.serv.ParamBean;
import com.rh.core.serv.ServMgr;
import com.rh.core.util.Lang;
import com.rh.core.util.RequestUtils;

/**
 * 监听对page等jsp的请求，处理TODO_USER参数，根据其值切换兼岗用户
 * @author wangchen
 *
 */
public class TodoUserFilter implements Filter {
    
    public void init(FilterConfig arg0) throws ServletException {
        // TODO Auto-generated method stub  
    }

    
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws ServletException,
            IOException {
        if ((!(req instanceof HttpServletRequest)) || (!(res instanceof HttpServletResponse))) {
            throw new ServletException("TodoUserFilter protects only HTTP resources");
        }
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;
      
        // 验证身份
        UserBean userBean = Context.getUserBean(request);
        if (userBean != null) {
            String hexedTodoUserStr = RequestUtils.get(request, "TODO_USER", "");
            if (!hexedTodoUserStr.isEmpty()) {
                // 解码十六进制字符串
                String todoUserCode = new String(Lang.hexToStr(hexedTodoUserStr));
                if (!todoUserCode.equals(userBean.getId())) {
                    Context.setRequest(request);
                    // 改变登陆用户
                    ParamBean changeUserParam = new ParamBean();
                    changeUserParam.setServId("SY_ORG_LOGIN");
                    changeUserParam.setAct("changeUser");
                    changeUserParam.set("TO_USER_CODE", todoUserCode);
                    OutBean result = ServMgr.act(changeUserParam);
                    if (!result.isOk()) {
                        RequestUtils.sendDisp(request, response, "/sy/comm/page/page.jsp");
                        return;
                    }
                }
            }
        }
        chain.doFilter(req, res);
    }
    
    
    public void destroy() {
        // TODO Auto-generated method stub      
    }
}
