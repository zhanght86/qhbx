<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<!--OaTransmitGongwen.jsp 平台迁移文件页面-->
<%@ page import="com.rh.core.base.Context" %>
<%@ page import="com.rh.core.serv.ServDao" %>
<%@ page import="com.rh.core.serv.ServMgr" %>
<%@ page import="com.rh.core.serv.bean.SqlBean" %>
<%@ page import="com.rh.core.serv.flow.FlowMgr" %>
<%@ page import="com.rh.core.org.mgr.UserMgr" %>
<%@ page import="com.rh.core.comm.entity.EntityMgr" %>

<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
	    <title>智能平台系统</title>
	    <%@ include file= "/sy/base/view/inHeader.jsp" %>
	</head>
	
<%
	//
 	if (request.getQueryString() != null) {
 		request.getSession().setAttribute("GOTO_URL", request.getRequestURI().toString() + "?" + request.getQueryString());
	} else {
	  	request.getSession().setAttribute("GOTO_URL", request.getRequestURI().toString());
	}
	// 如果没有登录则导向首页去登录
	if(userBean == null) {
		 String loginUrl = Context.getSyConf("SY_LOGIN_URL","/");
		 RequestUtils.sendDisp(request, response, loginUrl);
	}
%>
<%
	//提交标识
	String submitFlag = RequestUtils.getStr(request,"submitFlag");
	//用户编码
	String userCode = RequestUtils.getStr(request,"srcUserCode");
	//源机构编码
	String srcOdeptCode = RequestUtils.getStr(request,"srcOdeptCode");
	String warnStr = "";
	int count = 0;
	String resultStr = "";
	if (!srcOdeptCode.isEmpty() && !userCode.isEmpty()) {
	    UserBean toUserBean = UserMgr.getUser(userCode);
	    String userOdeptCode = toUserBean.getODeptCode();
	    if (userOdeptCode.equals(srcOdeptCode)) {
	        resultStr = "同一机构内不需要使用此功能";
	    } else {
	        /**迁移文件--开始*/    
	        //批量添加流经信息
	        SqlBean countQuery = new SqlBean();
	        countQuery.set("OWNER_ID", userCode); //当前人的流经
	        countQuery.set("S_ODEPT", srcOdeptCode);
	        //每页数据量
	        final int perPageItemSize = 100;
	        //总数据量
	        final int itemCount = ServDao.count(ServMgr.SY_SERV_FLOW, countQuery);
	        //总页数
	        if (itemCount > 0) {
	            final int pageNum = itemCount / perPageItemSize + 1;
	            for (int i = 0; i < pageNum; i++) {
	                SqlBean queryBean = new SqlBean();
	                queryBean.and("OWNER_ID", userCode);
	                queryBean.and("S_ODEPT", srcOdeptCode);
	                queryBean.asc("FLOW_ID");
	                queryBean.limit(perPageItemSize);  //页数
	                queryBean.page(i + 1); // 每页数据量
	                List<Bean> flowList =  ServDao.finds(ServMgr.SY_SERV_FLOW, queryBean);
	                for (Bean b : flowList) {
	                    EntityMgr.copyEntity(b.getStr("DATA_ID"), userOdeptCode);
	                }
	                count += flowList.size();
	            }
	        }
		    /**迁移文件--结束*/   
	        resultStr = "结果：共处理" + count + "条流经信息";
	    }	    
	}
%>
<%if (!submitFlag.isEmpty()) {%>
	<script>
		alert("<%=resultStr%>");
		window.location.href = "/oa/OaTransmitGongwen.jsp";
	</script>
<%} else {%>
	<body>
		<form method="post" id="operation" action="/oa/OaTransmitGongwen.jsp" style="text-align:center;padding-top:30px;">
			<label>待迁移用户</label>
			<input type="text" name="srcUserName" value="" readonly="readonly" class="batchModify batchDict">
			<input type="hidden" name="srcUserCode">
			<a id="selectUser" class="batchDict-select">选择</a>&nbsp;<a id="cancelUser" class="batchDict-clear">取消</a>
			<br/>
			<label>待迁移机构</label>
			<input type="text" name="srcOdeptName" value="" readonly="readonly" class="batchModify batchDict">
			<input type="hidden" name="srcOdeptCode">
			<a id="selectOdept" class="batchDict-select">选择</a>&nbsp;<a id="cancelOdept" class="batchDict-clear">取消</a>
			<br/>
			<input id="submitBtn" type="button" value="开始迁移">
			<input type="hidden" name="submitFlag" value="false">
		</form>
	</body>
	<script>
		jQuery("#selectUser").unbind("click").bind("click", function(event){
			//1.构造树形选择参数
			var configStr = "SY_ORG_DEPT_USER_ALL,{'TYPE':'single'}";//此部分参数说明可参照说明文档的【树形选择】配置说明
			var extendTreeSetting = "{}";
			var options = {
				"config" :configStr,
				"extendDicSetting":StrToJson(extendTreeSetting),//非必须参数，一般用不到
				"replaceCallBack":function(idArray,nameArray){//回调，idArray为选中记录的相应字段的数组集合
					selectUserCallBack(idArray,nameArray);
				}
			};
			//2.显示树形
			var dictView = new rh.vi.rhDictTreeView(options);
			dictView.show(event);
		});
	
		//字典弹出选择回调的方法
		function selectUserCallBack(idArray,nameArray) {
			var code = idArray.join(",");
			var name = nameArray.join(",");
			jQuery("input[name='srcUserCode']").val(code);
			jQuery("input[name='srcUserName']").val(name);
		};
	
		jQuery("#cancelUser").unbind("click").bind("click", function(event){
			jQuery("input[name='srcUserCode']").val("");
			jQuery("input[name='srcUserName']").val("");
		});
	
		jQuery("#selectOdept").unbind("click").bind("click", function(event){
			//1.构造树形选择参数
			var configStr = "SY_ORG_ODEPT_ALL,{'TYPE':'single'}";//此部分参数说明可参照说明文档的【树形选择】配置说明
			var extendTreeSetting = "{'expandLevel':2}";
			var options = {
				"config" :configStr,
				"extendDicSetting":StrToJson(extendTreeSetting),//非必须参数，一般用不到
				"replaceCallBack":function(idArray,nameArray){//回调，idArray为选中记录的相应字段的数组集合
					selectOdeptCallBack(idArray,nameArray);
				}
			};
			//2.显示树形
			var dictView = new rh.vi.rhDictTreeView(options);
			dictView.show(event);
		});

		//字典弹出选择回调的方法
		function selectOdeptCallBack(idArray,nameArray) {
			var code = idArray.join(",");
			var name = nameArray.join(",");
			jQuery("input[name='srcOdeptCode']").val(code);
			jQuery("input[name='srcOdeptName']").val(name);
		};

		jQuery("#cancelOdept").unbind("click").bind("click", function(event){
			jQuery("input[name='srcOdeptCode']").val("");
			jQuery("input[name='srcOdeptName']").val("");
		});

		jQuery("#submitBtn").unbind("click").bind("click", function(event){
			if(jQuery("input[name='srcUserCode']").val() == ""){
				alert("用户不能为空！");
				return false;
			}
			if(jQuery("input[name='srcOdeptCode']").val() == ""){
				alert("机构不能为空！");
				return false;
			}
			if(!confirm("该迁移工作可能会持续几分钟，请您耐心等候，是否确认开始？")){
				return false;
			}
			jQuery("input[name='submitFlag']").val("true");
			jQuery("form").submit();
		});

		setTimeout(function(){
			jQuery("#warn").css("display","none");
		}, 2000);
	</script>
<%}%>
</html>