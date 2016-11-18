<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.util.*"%>
<%@ page import="com.rh.core.base.Bean"%>
<%@ page import="com.rh.core.util.Constant"%>
<%@ page import="com.rh.core.util.JsonUtils"%>
<%@page import="com.rh.core.base.Context"%>
<%@ include file="/sy/base/view/inHeader.jsp"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" %>
<html>
<head>
<title>投票</title>
<style type="text/css">
html,body{
background: white;
font-size: 15px;
display:inline; 
text-align:center;
}

.div_question {padding: 4px 4px 5px;clear: both;margin: 2px auto;width: 96%;height: auto;}
.div_title_question_all {padding-top: 2px;font-size: 16px;font-weight: bold;height: auto;line-height: 20px;text-indent:2em;}
.div_title_question {overflow: hidden;text-indent:2em;}
.div_question {clear: both;width: 85%;padding-top: 5px;padding-left: 0px;padding-bottom: 2px;font-size: 12px;color: rgb(51, 51, 51);}
.div_question .div_question_1 {padding-bottom:2px; padding-left:33px;font-size: 16px;}
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
  background: url("") no-repeat scroll center top transparent;
  margin: 0 auto;
  padding-top: 156px;
  width: 100%;
}
.title_head{
line-height: 20px;margin: 10px auto;font-size: 16px;width: 85%;text-indent:2em;
}
.remark{
height: auto;line-height: 20px;text-indent:2em;
}
#myTable input{
  vertical-align:middle;
  margin:0 auto;
  line-height: 40px;
}
.trClass{
	line-height: 40px;
}
.tdClass{
	line-height: 40px;
	text-indent:6em;
}

</style>
<%
	Bean outBean = (Bean)request.getAttribute(Constant.RTN_DISP_DATA);
	Bean selectionBean = outBean.getBean("SELECTION_INFO");
	List<Bean> themeList = outBean.getList("THEME_LIST");
	List<Bean> optionsList = outBean.getList("OPTION_LIST");
	String themeIds = outBean.getStr("THEME_IDS");
%>

<script type="text/javascript">
jQuery(document).ready(function(){
	jQuery("#doSubmit").click(function(){
		var themeStr = "<%=themeIds%>";
		var quesArr = themeStr.split(",");
		if (quesArr && quesArr.length > 0) {
			var param = {};
			param.SELECTION_ID = "<%=selectionBean.getStr("SELECTION_ID")%>";
			var answer = "{";
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
			if (answer.length > 1) {
				answer = answer.substring(0,answer.length -1) + "}";
			} else {
				answer += "}";
			}
			
			param.ANS_RESULT = answer;
			
			FireFly.doAct("BN_VOTE_SELECTION","getSubmitBtnInsertIntoAnswer",param,false,false,function(data){
				if (data[UIConst.RTN_MSG] && data[UIConst.RTN_MSG].indexOf(UIConst.RTN_OK) == 0) {
					alert("您的投票已提交成功，感谢您的参与！");
					window.opener=null;
					window.close();
				}else{
					alert(data[UIConst.RTN_MSG]);
				}
			});
		}
	})
});

//已投票数判断
function checkSelection(objName,limit){
	var count = 0;
	jQuery(document).find("input[name='"+objName+"'").each(function(i){
		if (jQuery(this).attr("checked") && jQuery(this).attr("checked") == 'checked'){
			count++;
		}
	});
	if(limit<count){
		alert("对不起，您最多只能投"+limit+"票");
		jQuery("#doSubmit").attr("disabled",true);
	}else{
		jQuery("#doSubmit").removeAttr("disabled");
	}
}

</script>
<body>
<div id="jqContent"  style="text-align: left; ">
	<div id="headerCss" style="overflow-x: hidden; overflow-y: hidden;">
		<div id="ctl00_header"></div>
	</div>
	<div id="mainCss">
		<div class="title"><%=selectionBean.getStr("SELECTION_NAME") %></div>
		<div class="title_head">
			<div class="remark">
				<font style="font-weight:bold;color:#19A8EE;">备注：</font>
				<%=selectionBean.getStr("REMARK") %>
			</div>
		</div>
	<%
		for (int i=0; i<themeList.size(); i++) {
			Bean ques = themeList.get(i);
			if (ques.getInt("THEME_TYPE")==1) {
	
	%>
	<div class="div_question" id="<%=ques.getId()%>">
		<div class="div_title_question_all">
			<div class="div_title_question">
				<%=ques.getStr("VOTE_THEME") %>&nbsp;<font color="red">(每人限投<%=ques.getStr("SELECTION_COUNT") %>票)</font>
			</div>
			<div style="clear: both;"></div>
		</div>
		<div class="div_question_1" >
			<table id="myTable"  width="60%">
			<tr class="trClass">
					<td>
			<%
				for (Bean optionBean : optionsList) {
					String quesId = optionBean.getStr("VOTE_ID");
					if(ques.getId().equals(quesId)){
			%>
				
					<input type="checkbox" onclick="javascript:checkSelection('<%=ques.getId()%>',<%=ques.getStr("SELECTION_COUNT") %>);" id="<%=optionBean.getId() %>" name="<%=ques.getId()%>" value="<%=optionBean.getId()%>"><a href="BN_VOTE_SELECTION.showCandidateInfo.do?data={'SELECTION_ID':'<%=selectionBean.getStr("SELECTION_ID")%>','OPTION_ID':'<%=optionBean.getId() %>'}" style="text-decoration:none;"><%=optionBean.getStr("CANDIDATE") %></a>
					
			<%	
				}}
			%>
				</td>
			</tr>
			</table>
		</div>
	</div>
		<%} else if(ques.getInt("THEME_TYPE")==2){
			
	%>
	<div class="div_question" id="<%=ques.getId()%>">
		<div class="div_title_question_all">
			<div class="div_title_question">
				<%=ques.getStr("VOTE_THEME") %>(单选)&nbsp;<font color="red">每人限投<%=ques.getStr("SELECTION_COUNT") %>票</font>
			</div>
			<div style="clear: both;"></div>
		</div>
		<div class="div_question_1">
			<table id="myTable"  width="60%">
			<tr class="trClass">
					<td class="tdClass">
			<%
				for (Bean optionBean : optionsList) {
					String quesId = optionBean.getStr("VOTE_ID");
					if(ques.getId().equals(quesId)){
			%>
				
					<div class="tdClass">
						<input class="tdClass" type="radio" onclick="javascript:checkSelection('<%=ques.getId()%>',<%=ques.getStr("SELECTION_COUNT") %>);" id="<%=optionBean.getId() %>" name="<%=ques.getId()%>" value="<%=optionBean.getId()%>"> 
						<a href="BN_VOTE_SELECTION.showCandidateInfo.do?data={'SELECTION_ID':'<%=selectionBean.getStr("SELECTION_ID")%>','OPTION_ID':'<%=optionBean.getId() %>'}" style="text-decoration:none;" ><%=optionBean.getStr("CANDIDATE") %></a></div>
			<%	
				}}
			%>
				</td>
			</tr>
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
	<div style="margin-left: 45%;"><input type="button" id="doSubmit" value="投票" class="submitbutton"></div>
	<input type="hidden" value="<%=selectionBean.getStr("SELECTION_ID") %>" id="SELECTION_ID">
	</div>
	<div id="footercss"></div>
</div>
</body>