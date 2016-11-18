<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ page import="com.rh.core.serv.CommonServ" %>
<%@ page import="com.rh.core.serv.ServDao" %>
<%@ page import="com.rh.core.serv.OutBean" %>
<%@ page import="com.rh.core.base.Bean" %>
<%@ page import="com.rh.core.serv.ParamBean" %>
<%@ page import="com.rh.core.util.JsonUtils" %>
<%@ include file= "/sy/base/view/inHeader.jsp" %>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <title>候选人信息</title>
<style type="text/css">
body{margin:0px;padding:0px;}
#headerCss {
  -moz-border-bottom-colors: none;
  -moz-border-left-colors: none;
  -moz-border-right-colors: none;
  -moz-border-top-colors: none;
  background: none no-repeat scroll 0 0 white;
  border-color: #77D3FF;
  border-image: none;
  border-style: solid solid none;
  border-top-left-radius: 3px;
  border-top-right-radius: 3px;
  border-width: 3px 3px medium;
  margin: 0 auto;
  width: 1000px;
}
#mainCss {
  -moz-border-bottom-colors: none;
  -moz-border-left-colors: none;
  -moz-border-right-colors: none;
  -moz-border-top-colors: none;
  background: none repeat scroll 0 0 white;
  border-color: -moz-use-text-color #77D3FF;
  border-image: none;
  border-left: 3px solid #77D3FF;
  border-right: 3px solid #77D3FF;
  border-style: none solid;
  border-width: medium 3px;
  margin: 0 auto;
  width: 1000px;
}
#footercss {
  -moz-border-bottom-colors: none;
  -moz-border-left-colors: none;
  -moz-border-right-colors: none;
  -moz-border-top-colors: none;
  background: none repeat scroll 0 0 white;
  border-bottom-left-radius: 3px;
  border-bottom-right-radius: 3px;
  border-color: #77D3FF;
  border-image: none;
  border-right: 3px solid #77D3FF;
  border-style: none solid solid;
  border-width: medium 3px 3px;
  height: 20px;
  margin: 0 auto;
  width: 1000px;
  margin-bottom: 30px;
}
#jqContent {
  background: no-repeat scroll center top transparent;
  margin: 0 auto;
  padding-top: 10px;
  width: 100%;
  font-size: 18px;
}
.title{text-align: center;font-size: 24px;color: #19A8EE;padding: 30px 0;}
.title_head{
text-align: center;line-height: 10px;margin: 10px auto;font-size: 16px;width: 85%;
}

input.submitbutton {background: none repeat scroll 0 0 #13A5EC;border: 1px solid #0492D6;border-radius: 5px;color: #FFFFFF;cursor: pointer;display: inline-block;font-size: 15px;font-weight: bold;height: 30px;line-height: 30px;overflow: visible;padding: 0 15px;margin: 0 auto;}
input.submitbutton:hover
{
    background: none repeat scroll 0 0 #1EB0F6;
    color: #FFFFFF !important;
}
#myTable input{
  vertical-align:middle;
  margin:0 auto; 
text-indent:2em;
}
.trClass{
	line-height: 40px;
	text-indent:6em;
}
</style>
<%
	Bean outBean = (Bean)request.getAttribute(Constant.RTN_DISP_DATA);
	Bean selectionBean = outBean.getBean("SELECTION_INFO");
	List<Bean> themeList = outBean.getList("THEME_LIST");
	List<Bean> optionsList = outBean.getList("OPTION_LIST");
	String photoId= outBean.getStr("PHOTO_ID");
	
%>
	
</head>
<body>
<div id="jqContent"  style="text-align: left; ">
	<div id="headerCss" style="overflow-x: hidden; overflow-y: hidden;">
		<div id="ctl00_header"></div>
	</div>
	<div id="mainCss">
	<div class="title"><%=selectionBean.getStr("SELECTION_NAME") %></div>
	<%
		for (int i=0; i<themeList.size(); i++) {
			Bean ques = themeList.get(i);
			if (ques.getInt("THEME_TYPE")==1) {
	
	%>
	<div class="div_question" id="<%=ques.getId()%>">
		<div>
			<table id="myTable"  width="100%">
			<%
				for (Bean optionBean : optionsList) {
					String quesId = optionBean.getStr("VOTE_ID");
					if(ques.getId().equals(quesId)){
			%>
				<tr class="trClass">
					<td>
						<div class="trClass"><img src="../../../file/<%=photoId%>"></img></div>
						<div class="trClass"><%=optionBean.getStr("CANDIDATE") %></div>
					</td>
					<td>
						<div class="trClass"><%=optionBean.getStr("CANDIDATE_INTRODUCTION") %></div>
					</td>
				</tr>
			<%	
				}}
			%>
			</table>
		</div>
	</div>
		<%} else if(ques.getInt("THEME_TYPE")==2){
			
	%>
	<div id="<%=ques.getId()%>">
		<div>
			<table width="100%">
			<%
				for (Bean optionBean : optionsList) {
					String quesId = optionBean.getStr("VOTE_ID");
					if(ques.getId().equals(quesId)){
						
			%>
				<tr class="trClass">
					<td>
						<div class="trClass"><img src="../../../file/<%=photoId%>"></img></div>
						<div class="trClass"><%=optionBean.getStr("CANDIDATE") %></div>
					</td>
					<td>
						<div class="trClass"><%=optionBean.getStr("CANDIDATE_INTRODUCTION") %></div>
					</td>
				</tr>
			<%	
				}}
			%>
			</table>
		</div>
	</div>
			<% }else {
	%>
	<%			
			}	
		}
	%>
	<br>
	<div style="margin-left: 45%;"><input type=button value=返回 onclick="window.history.go(-1)" class="submitbutton"></div>
	<input type="hidden" value="<%=selectionBean.getStr("SELECTION_ID") %>" id="SELECTION_ID">
	</div>
	<div id="footercss"></div>
</div>
</body>
</html>

