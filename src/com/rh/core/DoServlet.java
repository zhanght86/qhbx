/*
 * Copyright (c) 2011 Ruaho All rights reserved.
 */
package com.rh.core;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.NDC;

import com.rh.core.base.Bean;
import com.rh.core.base.Context;
import com.rh.core.base.Context.THREAD;
import com.rh.core.base.TipException;
import com.rh.core.org.UserBean;
import com.rh.core.serv.OutBean;
import com.rh.core.serv.ParamBean;
import com.rh.core.serv.ServMgr;
import com.rh.core.serv.ServUtils;
import com.rh.core.util.Constant;
import com.rh.core.util.JsonUtils;
import com.rh.core.util.RequestUtils;
import com.rh.core.util.XmlUtils;

/**
 * 通用响应Servlet
 * @author Jerry Li
 * @version $Id$
 */
public class DoServlet extends HttpServlet {

    /** UID */
    private static final long serialVersionUID = 5012111888412187742L;
    /** log */
    private static Log log = LogFactory.getLog(DoServlet.class);

    /**
     * 请求处理，要求url格式为：http://....:80/SY_SERV.list.do?..... (SY_SERV为servId,list为act)
     * @param request 请求头
     * @param response 响应头
     * @throws ServletException ServletException
     * @throws IOException IOException
     */
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException,
            IOException {
        Context.cleanThreadData();
        ParamBean paramBean = null;  //参数信息
        OutBean resultBean = null;  //结果信息
        try {
            request.setCharacterEncoding("UTF-8");
            // =================处理界面传入参数===================
            String uri = request.getRequestURI();
            String[] sa = uri.substring(uri.lastIndexOf("/") + 1).split("\\.");
            String serv = null;
            String act = "query";
            if (sa.length > 1 && sa[0].length() > 0) {
                serv = sa[0].toUpperCase();
                if (sa.length > 2 && sa[1].length() > 0) {
                    act = sa[1];
                }
            } else {
                throw new RuntimeException(Context.getSyMsg("SY_REQUEST_ERROR", request.getRequestURI()));
            }
            paramBean = RequestUtils.transParam(request);
            //log.debug(uri + "  param=" + paramBean);
            Context.setThread(THREAD.PARAMBEAN, paramBean); // 将参数信息放入线程变量
            Context.setThread(THREAD.SERVID, serv); // 将参数信息放入线程变量
            Context.setRequest(request); // 将request放入线程变量供userInfo等session的设置
            Context.setResponse(response); // 将response放入线程变量供下载等调用
            Bean servDef = ServUtils.getServDef(serv); // 获取服务定义信息
            // =================处理用户权限判断===================
            Boolean toLogin = false;
            UserBean userBean = Context.getUserBean(request);
            if (userBean == null) {
                if (servDef.getInt("SERV_AUTH_FLAG") != Constant.AUTH_FLAG_NONE) {
                    toLogin = true;
                }
            }
            //设置log跟踪信息处理
            StringBuilder ipName = new StringBuilder(serv);
            ipName.append(" ").append(act).append(" ");
            if (userBean != null) {
                ipName.append(userBean.getCurrentIpAddress()).append(" ").append(userBean.getCode());
                //动态跟踪用户的当前功能、当前操作、当前数据，供在线用户监控查看
                Context.getOnlineUserState().set("SERV_ID", serv).set("ACT_CODE", act)
                    .set("DATA_ID", paramBean.getId());
            } else {
                ipName.append(RequestUtils.getIpAddr(request));
            }
            NDC.push(ipName.toString());
            if (!toLogin) { // 直接执行服务，不跳转登录页面
            	paramBean.setServId(serv).setAct(act).setTransFlag(true); //设置服务、操作参数并强制启用事务
            	//增加客户端请求标记
            	paramBean.set("_CLIENT_REQ_", "TRUE");
                resultBean = ServMgr.act(paramBean);
                if (resultBean.contains(OutBean.TO_DIRE)) { //如果约定了跳转URL等
                    RequestUtils.sendDir(response, resultBean.getToRedirect());
                } else if (resultBean.contains(OutBean.TO_DISP)) { //如果约定了跳转JSP等
                    request.setAttribute(Constant.RTN_DISP_DATA, resultBean);
                    RequestUtils.sendDisp(request, response, resultBean.getToDispatcher());
                } else {
                    String msg = resultBean.getMsg();
                    if (!msg.startsWith(Constant.RTN_MSG_OK)) { //错误信息要过滤html标签避免安全漏洞
                        resultBean.set(Constant.RTN_MSG, RequestUtils.escapeHTML(msg));
                    }
                }
            } else { // 跳转到登录页面
                resultBean = new OutBean();
                if (request.getHeader("User-Agent").startsWith("RhClient")) { //为客户端程序提供单独的错误信息
                    resultBean.set(Constant.RTN_MSG, Constant.RTN_MSG_LOGIN);
                } else { //浏览器进行登录跳转
                    RequestUtils.sendDisp(request, response, "/sy/comm/login/jumpToIndex.jsp");
                }
            }
            
        } catch (TipException e) { // 不记录Log，直接传递错误信息给界面
            if (log.isDebugEnabled()) { //调试模式下也输出exception到log中
                log.debug(e.getMessage(), e);
            }
            if (resultBean == null) {
                resultBean = new OutBean();
            }
            resultBean.setError(RequestUtils.escapeHTML(e.getMessage()));
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            if (resultBean == null) {
                resultBean = new OutBean();
            }
            resultBean.setError(RequestUtils.escapeHTML(e.getMessage()));
        }
        
        NDC.pop();
        // =================处理返回信息===================
        if (!response.isCommitted()) {
            // 返回信息
            String header;
            String content;
            if (resultBean.contains(OutBean.TO_HTML)) {
                header = "text/html; charset=utf-8";
                content = resultBean.getToHtml();
            } else  if (resultBean.contains(OutBean.TO_XML)) {
                header = "text/xml; charset=utf-8";
                content = resultBean.getToXml();
            } else if (paramBean.getStr(Constant.PARAM_FORMAT).equals("xml")) { //指定xml格式
                header = "text/xml; charset=utf-8";
                content = XmlUtils.toFullXml(resultBean);
            } else if (!RequestUtils.getStr(request, "callback").isEmpty()) {
                header = "text/xml; charset=utf-8";
                String callbackStr = RequestUtils.getStr(request, "callback");
                content = callbackStr 
                        + "(" + JsonUtils.toJson(resultBean, false, paramBean.getEmptyFlag()) + ")"; //支持压缩空值输出
            } else {
                header = "text/html; charset=utf-8";
                content = JsonUtils.toJson(resultBean, false, paramBean.getEmptyFlag()); //支持压缩空值输出
            }
            response.setContentType(header);
            PrintWriter out = response.getWriter();
            out.write(content);
            out.flush();
            out.close();
        }
    }
}
