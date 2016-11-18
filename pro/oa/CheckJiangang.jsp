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
	<style>
		html,body {
			width:100%;
			height:100%;
		} 
		.list {
			float: left;
			overflow: scroll;
		}
		#btnList {
			background-color: gray;
			width: 100%;
			height: 10%;
		}
		#mainList {			
			background-color: rgb(196, 196, 238);
			width: 50%;
			height: 70%;
		}
		#jianList {
			background-color: rgb(180, 241, 180);
			width: 50%;
			height: 70%;
		}
		#stateList {
			background-color: rgb(226, 180, 241);
			width: 100%;
			height: 20%;
		}
		.NAME {
			display: inline-block;
			width: 15%;
		}
		.COUNT {
			display: inline-block;
			width: 5%;
		}
		.MAINORG {
			display: inline-block;
			width: 40%;
		}
		.MAINDEPT {
			display: inline-block;
			width: 40%;
		}
		.ORG {
			display: inline-block;
			width: 40%;
		}
		.DEPT {
			display: inline-block;
			width: 40%;
		}
		.AUXCODE {
			display: inline-block;
			width: 20%;
		}
		.USERCODE {
			display: inline-block;
			width: 20%;
		}
		.JIANCODE {
			display: inline-block;
			width: 80%;
		}
		.MainLi {
			color: blue;
		}
		.AuxLi {
			color: blue;
		}
		.JianLi {
			color: blue;
		}
		.MainLi:hover{
			cursor: pointer;
			background-color: orange;
		}
		.MAINCODE {
			background-color: orange;
		}
		.JSPAN {
			margin-right: 2px;
		}
		#shield {
			z-index: 10000;
			opacity: 0.3;
			position: absolute;
			width: 100%;
			height: 100%;
			background-color: black;
			text-align: center;
			font-size: 30px;
			color: white;
			display: none;
		}
		#result {
			z-index: 9999;
			position: absolute;
			width: 20%;
			height: 50%;
			background-color: white;
			color: black;
			display: none;
			left: 40%;
			top: 25%;
			overflow: scroll;
		}
		#result_close {
			position: absolute;
			right: 0px;
			top: 0px;
			width: 60px;
			height: 30px;
			visibility: hidden;
		}
	</style>
	<body>
		<div class="list" id="btnList" title="按钮列">
			<button id="autoTest">自动检测</button>
			<button id="autoRecreate">重建用户状态</button>
		</div>
		<div class="list" id="mainList" title="存在兼岗的主用户列表">
		</div>
		<div class="list" id="jianList" title="对应主用户的兼岗用户">
		</div>
		<div class="list" id="stateList" title="用户状态列表">
		</div>
		<div id="shield">请稍后。。。</div>
		<div id="result"><div id="res_div"></div><button id="result_close">关闭</button></div>
	</body>
	<script type="text/javascript">
	(function() {
	    jQuery(document).ready(function(){
	        //构造主用户列表
	        var param = {
        		"_ROWNUM_":  10000,
        		"NOWPAGE": 1,
				"_SELECT_": " ORIGIN_USER_CODE,ORIG_USER_NAME,ORIG_ODEPT_CODE,ORIG_TDEPT_CODE,count(ORIGIN_USER_CODE) as COUNT",
        		"_groupBy": " ORIGIN_USER_CODE,ORIG_USER_NAME,ORIG_ODEPT_CODE,ORIG_TDEPT_CODE",
        		"_ORDER_": " ORIG_USER_NAME"
   	        }
	        var result = FireFly.doAct("SY_ORG_USER_JIANGANG", "query", param, false);
	        var mainUsers = result._DATA_;
	      	if (mainUsers && mainUsers.length > 0) {
		      	//标题
		        var mainUserUl = jQuery("<ul id=\"mainUserList\"></ul>");
		        var li = jQuery("<li></li>");
				var nSpan = jQuery("<span class=\"NAME\"></span>").text("姓名").appendTo(li);
				var cSpan = jQuery("<span class=\"COUNT\"></span>").text("数量").appendTo(li);
				var oSpan = jQuery("<span class=\"MAINORG\"></span>").text("机构").appendTo(li);
				var dSpan = jQuery("<span class=\"MAINDEPT\"></span>").text("部门").appendTo(li);
				li.appendTo(mainUserUl);
				//内容
		        jQuery.each(mainUsers, function(i,n){
					var li = jQuery("<li class=\"MainLi\" userId=\"" + n.ORIGIN_USER_CODE + "\"></li>");
					var nSpan = jQuery("<span class=\"NAME\"></span>").text(n.ORIG_USER_NAME).appendTo(li);
					var cSpan = jQuery("<span class=\"COUNT\"></span>").text(n.COUNT).appendTo(li);
					var oSpan = jQuery("<span class=\"MAINORG\"></span>").text(n.ORIG_ODEPT_CODE__NAME).appendTo(li);
					var dSpan = jQuery("<span class=\"MAINDEPT\"></span>").text(n.ORIG_TDEPT_CODE__NAME).appendTo(li);
					li.appendTo(mainUserUl);
					var userid = n.ORIGIN_USER_CODE;
					//事件
					li.bind("click",function(event){
						//兼岗部门
						var param = {
			        		"_NOPAGE_":  true,
							"_WHERE_": " and ORIGIN_USER_CODE = '" + userid + "'"
			   	        }
				        var result = FireFly.doAct("SY_ORG_USER_JIANGANG", "query", param, false);
						var auxUsers = result._DATA_;
						var auxUserStr = "";
						var auxUserUl = jQuery("<ul id=\"auxUserList\"></ul>");
						var mainUserDiv = jQuery("<div class=\"MAINCODE\"></div>").text("主用户编码:" + userid);
						mainUserDiv.appendTo(auxUserUl);
				        var li = jQuery("<li></li>");
						var oSpan = jQuery("<span class=\"ORG\"></span>").text("机构").appendTo(li);
						var dSpan = jQuery("<span class=\"DEPT\"></span>").text("部门").appendTo(li);
						var cSpan = jQuery("<span class=\"AUXCODE\"></span>").text("兼岗用户编码").appendTo(li);
						li.appendTo(auxUserUl);
						if (auxUsers && auxUsers.length > 0) {					        
							jQuery.each(auxUsers, function(i,n){
								var li = jQuery("<li class=\"AuxLi\"></li>");
								var oSpan = jQuery("<span class=\"ORG\"></span>").text(n.ODEPT_CODE__NAME).appendTo(li);
								var dSpan = jQuery("<span class=\"DEPT\"></span>").text(n.TDEPT_CODE__NAME).appendTo(li);
								var cSpan = jQuery("<span class=\"AUXCODE\"></span>").text(n.USER_CODE).appendTo(li);
								li.appendTo(auxUserUl);
								if (i == 0) {
									auxUserStr = userid + "," + n.USER_CODE;
								} else {
									auxUserStr = auxUserStr + "," + n.USER_CODE;
								}
							});
							if (auxUserStr.length > 0) {
								auxUserStr = "'" + auxUserStr.replace(/,/g,"','") + "'";
							}
							jQuery("#jianList").html(auxUserUl);
						} else {
							jQuery("#jianList").html(result._MSG_);
						}
						//用户状态
						if (auxUserStr.length > 0) {						
							var param2 = {
				        		"_NOPAGE_":  true,
				        		"_SELECT_": "USER_CODE,JIAN_CODES",
								"_WHERE_": " and USER_CODE in (" + auxUserStr + ")"
				   	        }
							var result = FireFly.doAct("SY_ORG_USER_STATE", "finds", param2, false);
							var userStates = result._DATA_;
							var userStateUl = jQuery("<ul id=\"auxUserList\"></ul>");
							var mainUserDiv = jQuery("<div class=\"MAINCODE\"></div>").text("主用户编码:" + userid);
							mainUserDiv.appendTo(userStateUl);
					        var li = jQuery("<li></li>");
							var uSpan = jQuery("<span class=\"USERCODE\"></span>").text("用户编号").appendTo(li);
							var jSpan = jQuery("<span class=\"JIANCODE\"></span>").text("兼岗组编码").appendTo(li);
							li.appendTo(userStateUl);
					      	if (userStates && userStates.length > 0) {	
					      		jQuery.each(userStates, function(i,n){
					      			var li = jQuery("<li class=\"JianLi\"></li>");
					      			var uSpan = jQuery("<span class=\"USERCODE\"></span>").text(n.USER_CODE).appendTo(li);
					      			if (userid == n.USER_CODE) {
					      				uSpan.css("background-color","orange");
					      			}
					      			var jianArr = n.JIAN_CODES.split(",");
					      			if (jianArr.length > 0) {
										jQuery.each(jianArr, function(i,n){
											var jSpan = jQuery("<span class=\"JSPAN\"></span>").text(n).appendTo(li);
											if (auxUserStr.indexOf(n) >= 0 || userid.indexOf(n) >= 0) {
												var color = "green";
											} else {
												var color = "red";
											}
											jSpan.css("background-color",color);
										});
							      	}
									li.appendTo(userStateUl);
					      		});
					      	}
					      	jQuery("#stateList").html(userStateUl);
						}
			        });
		        });	        
		        mainUserUl.appendTo(jQuery("#mainList"));
	      	}

	      	jQuery("#autoTest").bind("click",autoTest);
	      	jQuery("#autoRecreate").bind("click",autoRecreate);
	      	jQuery("#result_close").bind("click",function(){
	      		jQuery("#res_div").html("");
	      		jQuery("#result").hide();
	      	});
	      	jQuery("#result").bind("mouseover",function(){
	      		jQuery("#result_close").css("visibility", "visible");
	      	});
	      	jQuery("#result").bind("mouseout",function(){
	      		jQuery("#result_close").css("visibility", "hidden");
	      	});
	    });
	})();

	function autoTest() {
		if (!confirm("将会持续几分钟，确定执行？")) {
			return false;
		}
		jQuery("#shield").show();
		jQuery("#result").show();
		setTimeout(function(){
			var mainUserUl = jQuery("#mainUserList");
			var res = {
				"total": 0,
				"bad": 0
			};
			jQuery.each(mainUserUl.children(), function(i,n){
				res.total = i;
				var userid = jQuery(this).attr("userid");
				if (userid) {
					var param = {
		        		"_NOPAGE_":  true,
						"_WHERE_": " and ORIGIN_USER_CODE = '" + userid + "'"
		   	        }
			        var result = FireFly.doAct("SY_ORG_USER_JIANGANG", "query", param, false);
					var auxUsers = result._DATA_;
					var auxUserStr = "";
					var auxUserUl = jQuery("<ul id=\"auxUserList\"></ul>");
					var mainUserDiv = jQuery("<div class=\"MAINCODE\"></div>").text("主用户编码:" + n.ORIGIN_USER_CODE);				
					if (auxUsers && auxUsers.length > 0) {					        
						jQuery.each(auxUsers, function(i,n){
							if (i == 0) {
								auxUserStr = userid + "," + n.USER_CODE;
							} else {
								auxUserStr = auxUserStr + "," + n.USER_CODE;
							}
						});
						if (auxUserStr.length > 0) {
							auxUserStr = "'" + auxUserStr.replace(/,/g,"','") + "'";
						}
						var param2 = {
			        		"_NOPAGE_":  true,
			        		"_SELECT_": "USER_CODE,JIAN_CODES",
							"_WHERE_": " and USER_CODE in (" + auxUserStr + ")"
			   	        }
						var result = FireFly.doAct("SY_ORG_USER_STATE", "finds", param2, false);
						var userStates = result._DATA_;
						if (userStates && userStates.length == (auxUsers.length + 1)) {
							var flag = true;
				      		jQuery.each(userStates, function(i,n){
				      			var jianArr = n.JIAN_CODES.split(",");
				      			if (jianArr.length > 0) {
									jQuery.each(jianArr, function(i,n){
										if (auxUserStr.indexOf(n) >= 0 || userid.indexOf(n) >= 0) {
											//
										} else {
											flag = false;
										}
									});
						      	} else {
						      		flag = false;
						      	}				      	
				      		});
				      		if (!flag) {
				      			jQuery(this).css("background-color","red");
				      			res.bad = res.bad + 1;
				      		}
				      	} else {
				      		jQuery(this).css("background-color","red");
				      		res.bad = res.bad + 1;
				      	}
					} else {
						jQuery(this).css("background-color","red");
						res.bad = res.bad + 1;
					}
				}
			});
			showResult(res);
			jQuery("#shield").hide();
		}, 1000);
	}
	
	function autoRecreate(){
		if (!confirm("执行前请确保备份数据库表SY_ORG_USER_STATE！")) {
			return false;
		}
		if (!confirm("确定执行？")) {
			return false;
		}
		window.open("/oa/ModifyJiangangUserState.jsp?action=1");
	}

	function showResult(res){
		var html = "<div>总计：" + res.total + "个</div>";
		html = html + "<div>问题：" + res.bad + "个</div>";
		jQuery("#res_div").html(html);
	}
	</script>
</html>