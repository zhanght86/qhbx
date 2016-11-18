<%@ page import="java.util.*"%>
<%@ page import="com.rh.core.base.Bean"%>
<%@ page import="com.rh.core.util.Constant"%>
<%@ page import="com.rh.core.util.JsonUtils"%>
<%@page import="com.rh.core.base.Context"%>
<%@ include file="/sy/base/view/inHeader.jsp"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>问卷调查</title>
<style type="text/css">
html,body{background: white;font-size: 15px;}
.div_question {padding: 4px 4px 5px;clear: both;margin: 2px auto;width: 96%;height: auto;}
.div_title_question_all {padding-top: 2px;font-size: 14px;color: rgb(55, 58, 61);font-weight: bold;height: auto;line-height: 20px;}
.div_title_question {overflow: hidden;}
.div_question {clear: both;width: 85%;padding-top: 5px;padding-left: 0px;padding-bottom: 2px;font-size: 12px;color: rgb(51, 51, 51);}
.div_question .div_question_1 {padding:0px 0 2px 20px;font-size: 15px;}
.div_question ul {clear: both;}
.div_question li {float: left;list-style-type: none;}
.div_question li {margin-bottom: 2px;}
.title{text-align: center;font-size: 24px;color: #19A8EE;padding: 30px 0;}
.title_1{text-align: center;color: #aaaaaa;}
.memo{margin: 10px;}
input.submitbutton {background: none repeat scroll 0 0 #13A5EC;border: 1px solid #0492D6;border-radius: 5px;color: #FFFFFF;cursor: pointer;display: inline-block;font-size: 15px;font-weight: bold;height: 30px;line-height: 30px;overflow: visible;padding: 0 15px;margin: 0 auto;}
input.submitbutton:hover
{
    background: none repeat scroll 0 0 #1EB0F6;
    color: #FFFFFF !important;
}
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
  width: 800px;
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
  width: 800px;
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
  width: 800px;
  margin-bottom: 30px;
}
#jqContent {
  background: url("/rm/images/common.jpg") no-repeat scroll center top transparent;
  margin: 0 auto;
  padding-top: 10px;
  width: 100%;
}
.title_head{
color: #555555;
line-height: 20px;
margin: 10px auto;
text-align: left;
font-size: 18px;
width: 85%;
}
#myTable input{
  vertical-align:middle;
}
</style>

<%
		Bean outBean = (Bean)request.getAttribute(Constant.RTN_DISP_DATA);
		Bean shijuanBean = outBean.getBean("SHIJUAN_INFO");
		List<Bean> quesList = outBean.getList("QUES_LIST");
		List<Bean> optionsList = outBean.getList("OPTION_LIST");
		String quesIds = outBean.getStr("QUESTION_IDS");
%>
<script type="text/javascript">
jQuery(document).ready(function(){
	jQuery("#doSubmit").click(function(){
		var questStr = "<%=quesIds%>";
		var quesArr = questStr.split(",");
		if (quesArr && quesArr.length > 0) {
			var param = {};
			param.INVEST_ID = "<%=shijuanBean.getStr("INVEST_ID")%>";
			var answer = "{";
			var content = "{";
			for(var i = 0; i < quesArr.length; i++){
				var obj = jQuery.find("[name='" + quesArr[i] + "'");
				if (obj && obj.length > 0) {
					var inputType = jQuery(obj[0]).attr("type");
					var chestr="";
					for(var j=0;j<obj.length;j++){
						// 单选题
						if('radio' == inputType && obj[j].checked == true){ 
							answer += "'" + quesArr[i] + "':" + "'" + obj[j].value + "',";
							break;
						} else if ('checkbox' == inputType && obj[j].checked == true) {//多选题
							chestr += obj[j].value + ";";
						} else if ('textarea' == inputType) { // 简答题
							content += "'" + quesArr[i] + "':" + "'" + obj[j].value + "',";
						}
					}
					// 多选题
					if ('checkbox' == inputType) {
						if(chestr.length > 0){
							chestr=chestr.substring(0,chestr.length -1);
						 	answer += "'" + quesArr[i] + "':" + "'" + chestr + "',";
					 	}
					}
				}
			}
			if (content.length > 1) {
				content = content.substring(0,content.length -1) + "}";
			} else {
				content += "}";
			}
			param.ANS_CONTENT = content;
			
			if (answer.length > 1) {
				answer = answer.substring(0,answer.length -1) + "}";
			} else {
				answer += "}";
			}
			param.ANS_RESULT = answer;
			
			FireFly.doAct("BN_QS_INVEST","getSubmitBtnInsertIntoAnswer",param,false,false,function(data){
				if (data[UIConst.RTN_MSG] && data[UIConst.RTN_MSG].indexOf(UIConst.RTN_OK) == 0) {
					alert("您的答卷已提交成功，感谢您的参与！");
					window.opener=null;
					window.close();
				}
				else{
					alert(data[UIConst.RTN_MSG]);
				}
			});
		}
	});
});
	
</script>
</head>
<body>
<div id="jqContent"  style="text-align: left; ">
	<div id="headerCss" style="overflow-x: hidden; overflow-y: hidden;">
		<div id="ctl00_header"></div>
	</div>
	<div id="mainCss">

	<div class="title"><%=shijuanBean.getStr("INVEST_NAME") %></div>
	<div class="title_head">
		<div style="vertical-align: middle;margin-top: 10px;">
			<%=shijuanBean.getStr("INVEST_OBJECTIVE") %>
		</div>
		<div style="vertical-align: middle;margin-top: 10px;">
			<font style="font-weight:bold;color:#19A8EE;">备注：</font>
			<%=shijuanBean.getStr("REMARK") %>
		</div>
	</div>	
	<%
		for (int i=0; i<quesList.size(); i++) {
			Bean ques = quesList.get(i);
			if (ques.getInt("QUESTION_TYPE")==1) {
	
	%>
	<div class="div_question" id="<%=ques.getId()%>">
		<div class="div_title_question_all">
			<div class="div_title_question">
				<%=i+1 %>. <%=ques.getStr("QUESTION_HEAD") %>(单选题)&nbsp;<font color="red">*</font>
			</div>
			<div style="clear: both;"></div>
		</div>
		<div class="div_question_1">
			<table id="myTable"  width="100%">
			<%
				for (Bean optionBean : optionsList) {
					String quesId = optionBean.getStr("QUESTION_ID");
					if(ques.getId().equals(quesId)){
			%>
				<tr height="35px">
					<td>
					<input type="radio" id="<%=optionBean.getId() %>" name="<%=ques.getId()%>" value="<%=optionBean.getId()%>"> <%=optionBean.getStr("OPTION_VALUE") %>
					</td>
				</tr>
			<%	
				}}
			%>
			</table>
		</div>
	</div>
	<%			
			} else if(ques.getInt("QUESTION_TYPE")==2){%>
	<div class="div_question" id="<%=ques.getId()%>">
		<div class="div_title_question_all">
			<div class="div_title_question">
				<%=i+1 %>. <%=ques.getStr("QUESTION_HEAD") %>(多选题)&nbsp;<font color="red">*</font>
			</div>
			<div style="clear: both;"></div>
		</div>
		<div class="div_question_1">
			<table id="myTable"  width="100%">
			<%
				for (Bean optionBean : optionsList) {
					String quesId = optionBean.getStr("QUESTION_ID");
					if(ques.getId().equals(quesId)){
			%>
				<tr height="35px">
					<td>
					<input type="checkbox" name="<%=ques.getId() %>" value="<%=optionBean.getId() %>"> <%=optionBean.getStr("OPTION_VALUE") %>
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
	<div class="div_question" id="<%=ques.getId()%>">
		<div class="div_title_question_all">
			<div class="div_title_question">
				<%=i+1 %>. <%=ques.getStr("QUESTION_HEAD") %>(简答题)
			</div>
			<div style="clear: both;"></div>
		</div>
		<div class="div_question_1">
			<div style="clear: both;"></div>
			<table id="myTable"  width="100%" style="margin-top: 10px;">
				<tr>
					<td><textarea type="textarea" name="<%=ques.getId() %>" rows="10" cols="90"></textarea></td>
				</tr>
			</table>
		</div>
	</div>
	<%			
			}
		}
	%>
	<br>
	<div class="title_head">
	<div style="vertical-align: middle;">
			<%=shijuanBean.getStr("INVEST_END_DESCRIPT") %>
	</div>
	</div>
	<div style="margin-left: 45%;"><input type="button" id="doSubmit" value="提交答卷" class="submitbutton"></div>
	<input type="hidden" value="<%=shijuanBean.getStr("INVEST_ID") %>" id="INVEST_ID">
	</div>
	<div id="footercss"></div>
</div>
</body>
</html>