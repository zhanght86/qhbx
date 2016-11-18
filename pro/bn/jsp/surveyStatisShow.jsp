<%@ page import="java.util.*"%>
<%@ page import="java.text.DecimalFormat"%>
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
  padding-top: 156px;
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
%>


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
			List<Bean> optionsList = ques.getList("OPTIONS_LIST");
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
					double percent = 0;
					if (ques.getDouble("TOTAL_CHOOSED") > 0) {
						percent = (optionBean.getDouble("CHOOSED_COUNT") / ques.getDouble("TOTAL_CHOOSED")) * 100;
					}
					DecimalFormat df = new DecimalFormat(".00");
			%>
				<tr height="35px">
					<td width="50%">
					 <%=optionBean.getStr("OPTION_VALUE") %>
					</td>
					<td width="25%">
					 <div style="width:<%=Math.floor(percent)%>px;height:15px;background-color:#19A8EE;"></div>
					</td>
					<td width="25%">
					<%=optionBean.getStr("CHOOSED_COUNT")%>/<%=ques.getStr("TOTAL_CHOOSED")%>&nbsp;&nbsp;<%=percent > 0 ? df.format(percent) : 0 %>%
					</td>
				</tr>
			<%	
				}
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
					double percent = 0;
					if (ques.getDouble("TOTAL_CHOOSED") > 0) {
						percent = (optionBean.getDouble("CHOOSED_COUNT") / ques.getDouble("TOTAL_CHOOSED")) * 100;
					}
					DecimalFormat df = new DecimalFormat(".00");
			%>
				<tr height="35px">
					<td width="50%">
					 <%=optionBean.getStr("OPTION_VALUE") %>
					</td>
					<td width="25%">
					 <div style="width:<%=Math.floor(percent)%>px;height:15px;background-color:#19A8EE;"></div>
					</td>
					<td width="25%">
					<%=optionBean.getStr("CHOOSED_COUNT")%>/<%=ques.getStr("TOTAL_CHOOSED")%>&nbsp;&nbsp;<%=percent > 0 ? df.format(percent) : 0 %>%
					</td>
				</tr>
			<%	
				}
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
					<td width="50px" valign="top">&nbsp;</td>
					<td><span style="color:red">这是一道简答题！</span></td>
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
	<input type="hidden" value="<%=shijuanBean.getStr("INVEST_ID") %>" id="INVEST_ID">
	</div>
	<div id="footercss"></div>
</div>
</body>
</html>