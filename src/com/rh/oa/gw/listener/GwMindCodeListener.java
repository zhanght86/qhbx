package com.rh.oa.gw.listener;

import java.util.Enumeration;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.rh.core.base.Context;
import com.rh.core.serv.ParamBean;
import com.rh.core.util.RequestUtils;

/**
 * 意见编码监听
 * 
 * @author cuihf
 *
 */
public class GwMindCodeListener {
	protected Log log = LogFactory.getLog(this.getClass());

	public void beforeSave(ParamBean paramBean) {
		if (paramBean.isNotEmpty("MIND_CODE")) {
			log.warn("意见类型存在。mindCode=" + paramBean.getStr("MIND_CODE"));
		} else {			
			StringBuilder paramsBuilder = new StringBuilder();
			@SuppressWarnings("unchecked")
			Enumeration<String> paramNames = Context.getRequest().getParameterNames();
	        while (paramNames.hasMoreElements()) {
	            String pName = paramNames.nextElement();
	            if (!pName.equals("data")) { // 不再处理data中的数据
	                String value = RequestUtils.getStr(Context.getRequest(), pName);
	                paramsBuilder.append(pName+"@@"+value+"@@~~");	                
	            }
	        }
	        log.warn("意见类型不存在。前端参数："+paramsBuilder.toString());
		}
	}
}
