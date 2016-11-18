<%@ page language="java" contentType="text/html; charset=GBK"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3c.org/TR/1999/REC-html401-19991224/loose.dtd">
<html xml:lang="en" xmlns="http://www.w3.org/1999/xhtml">
	<head>
	<meta http-equiv="Content-Type" content="text/html; charset=gbk" />
	<meta name="keywords" content="百年人寿保险股份有限公司,百年,百年人寿,百年人寿保险,大连百年" />
	<meta name="verify-v1" content="ku5eYujDwJor922aXLh2tNUokeTi14dftgw2Igobok0=" />
	<meta http-equiv="x-ua-compatible" content="ie=7" />
	<title>百年人寿保险股份有限公司</title>
	<link href="<%=request.getContextPath()%>/global/cssnew/rushHour.css" rel="stylesheet" type="text/css" /> 
	<script type="text/javascript" src="<%=request.getContextPath()%>/global/js/jquery-1.3.2.min.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath()%>/global/js/ajax.js"></script>
	<link rel="stylesheet" href="<%=request.getContextPath()%>/global/css/galleriffic-spike.css" type="text/css" />
	<link rel="stylesheet" href="<%=request.getContextPath()%>/grandOpening/pictures/thickbox.css" type="text/css" media="screen" charset="utf-8" />
	<script type="text/javascript" src="thickbox.js"></script>
	</head>
	<body onkeydown="onkey()" >
		<div id="summitbg">
			<div id="AllHtml">
				<div id="agentheader">
					<jsp:include page="/bn/cms/rushHour/head.jsp" flush="true" />
				</div>
				<div id="maincontent">
					<div class="detailcontent">
						<div class="Dnav">
							<ul style="margin-left:10px;">
								<li class="DnavLi"><a  href="<%=request.getContextPath()%>/bn/cms/rushHour/spike/index.jsp"  onfocus="this.blur()">风采展示</a></li>
								<li class="Dnavhold"><a  href="<%=request.getContextPath()%>/bn/cms/rushHour/spike/index2.jsp" onfocus="this.blur()">团队项目</a></li>
								<li class="DnavLi"><a  href="<%=request.getContextPath()%>/bn/cms/rushHour/spike/index3.jsp"  onfocus="this.blur()">个人项目</a></li>
							</ul>
						</div>
						<div class="Dcontent">
							<%
								String LSUserCode = (String)session.getAttribute("LSUserCode");
								if(!com.sinosoft.common.Data.hasValue(LSUserCode)){ 
							%>
							<div class="input_area" style="margin-top:20px;">
								<div style="color:#666666;">请使用您的代理人帐号登录后查看</div>
								<div style="color:#666666;">
									用户名：<input type="text" style="width:150px;margin-top:5px;" name="UserID" value=""/><br/>
									密&nbsp;&nbsp;码：<input type="password" name="Password" style="width:150px;margin-top:5px;" value=""/><br/>
									<span style="margin-top:1px;"><img alt="点击更换验证码" name="Rand" style="cursor:pointer;" id="mFrame" name="mFrame" src="<%=request.getContextPath()%>/passport/registration/image.jsp" onclick="changeImage();"></span>验证码：<input type="text" name="Rand" style="width:75px;margin-top:4px;" value=""/><br style="clear:both"/>
								</div>
								<div class="submit_area">
									<a href="#" onclick="logon();"></a>						
								</div>
							</div>
							<%}else{ %>
								<div class="topic">
									<P class="topicone">百年“尖峰时刻”专题</P>
									<P class="topictwo">百年“尖峰时刻”荣誉记录为百年人寿个险销售系列荣誉项目，其宗旨俄为倡导超越文化理念，弘扬拼搏进取精神，营造不断刷新记录、挑战新高的业务氛围。</P>
								</div>
								<div id="showMonth"></div>
								 <div id="2011-01"  class="shownone">
									 <div class="notetitle">营业区</div>
									 <div class="note">
									 	<div class="notedetail">
									 		<div class="notepic">
									 			<p><a href="images/37_1.jpg" title="王宏总监区" class="thickbox"><img src="images/37.jpg" style="display:block;width:165px;height:105px;"/></a></p>
									 			<p style="margin-top:20px;color:white;font-weight:bolder;font-size:13px;">总监区单月标准保费</p>
									 		</div>
									 		<div class="noteline"></div>
									 		<div class="notetext">
									 			<p class="notetextone">最新记录：<font style="color:#cc0000;">1234954.95元</font></p>
									 			<p class="notetexttwo">刷新记录时间：2011年01月</p>
									 			<p class="notetextone">刷新纪录者：王宏总监区</p>
									 			<p class="notetexttwo">所属机构： 总公司营业部</p>
									 		</div>
									 	</div>
									 	<div class="Middleline"></div>
									 	<div class="notedetail">
									 		<div class="notepic">
									 			<p><a href="images/38_1.jpg" title="李艳杰总监区" class="thickbox"><img src="images/38.jpg" style="display:block;width:165px;height:105px;"/></a></p>
									 			<p style="margin-top:20px;color:white;font-weight:bolder;font-size:13px;">总监区单月件数</p>
									 		</div>
									 		<div class="noteline"></div>
									 		<div class="notetext">
									 			<p class="notetextone">挑战记录：<font style="color:#cc0000;">279件</font></p>
									 			<p class="notetexttwo">刷新记录时间：2011年01月</p>
									 			<p class="notetextone">刷新纪录者：李艳杰总监区</p>
									 			<p class="notetexttwo">所属机构： 黑龙江分公司</p>
									 		</div>
									 	</div>
									</div>
									<div class="notebottom"></div>
									<div class="notetitle">营业部</div>
									<div class="note">
									 	<div class="notedetail">
									 		<div class="notepic">
									 			<p><a href="images/39_1.jpg" title="石宝银营业部" class="thickbox"><img src="images/39.jpg" style="display:block;width:165px;height:105px;"/></a></p>
									 			<p style="margin-top:20px;color:white;font-weight:bolder;font-size:13px;">单月标准保费</p>
									 		</div>
									 		<div class="noteline"></div>
									 		<div class="notetext">
									 			<p class="notetextone">最新记录：<font style="color:#cc0000;">809501.95元</font></p>
									 			<p class="notetexttwo">刷新记录时间：2011年01月</p>
									 			<p class="notetextone">刷新纪录者：石宝银营业部</p>
									 			<p class="notetexttwo">所属机构： 辽宁分公司</p>
									 		</div>
									 	</div>
									 	<div class="Middleline"></div>
									 	<div class="notedetail">
									 		<div class="notepic">
									 			<p><a href="images/19_p.jpg" title="吴成祥营业部" class="thickbox"><img src="images/19.jpg" style="display:block;width:165px;height:105px;"/></a></p>
									 			<p style="margin-top:20px;color:white;font-weight:bolder;font-size:13px;">单月人均件数</p>
									 		</div>
									 		<div class="noteline"></div>
									 		<div class="notetext">
									 			<p class="notetextone">最新记录：<font style="color:#cc0000;">3.43件</font></p>
									 			<p class="notetexttwo">刷新记录时间：2011年01月</p>
									 			<p class="notetextone">刷新纪录者：吴成祥营业部</p>
									 			<p class="notetexttwo">所属机构： 总公司营业部</p>
									 		</div>
									 	</div>
									</div>
									<div class="notebottom"></div>
									<div class="notetitle">营业组</div>
									<div class="note2">
									 	<div class="notedetail">
									 		<div class="notepic">
									 			<p><a href="images/40_1.jpg" title="高志刚营业组" class="thickbox"><img src="images/40.jpg" style="display:block;width:165px;height:105px;"/></a></p>
									 			<p style="margin-top:20px;color:white;font-weight:bolder;font-size:13px;">单月标准保费</p>
									 		</div>
									 		<div class="noteline"></div>
									 		<div class="notetext">
									 			<p class="notetextone">最新记录：<font style="color:#cc0000;">700028.7元</font></p>
									 			<p class="notetexttwo">刷新记录时间：2011年01月</p>
									 			<p class="notetextone">刷新纪录者：高志刚营业组</p>
									 			<p class="notetexttwo">所属机构： 辽宁分公司</p>
									 		</div>
									 	</div>
									 	<div class="Middleline"></div>
									 	<div class="notedetail">
									 		<div class="notepic">
									 			<p><a href="images/41_1.jpg" title="李艳杰营业组" class="thickbox"><img src="images/41.jpg" style="display:block;width:165px;height:105px;"/></a></p>
									 			<p style="margin-top:20px;color:white;font-weight:bolder;font-size:13px;">单月件数</p>
									 		</div>
									 		<div class="noteline"></div>
									 		<div class="notetext">
									 			<p class="notetextone">最新记录：<font style="color:#cc0000;">63件</font></p>
									 			<p class="notetexttwo">刷新记录时间：2011年01月</p>
									 			<p class="notetextone">刷新纪录者：李艳杰营业组</p>
									 			<p class="notetexttwo">所属机构： 黑龙江分公司</p>
									 		</div>
									 	</div>
									 	<div class="notedetail" >
									 		<div class="notepic">
									 			<p><a href="images/42_1.jpg" title="王忠福营业组" class="thickbox"><img src="images/42.jpg" style="display:block;width:165px;height:105px;"/></a></p>
									 			<p style="margin-top:20px;color:white;font-weight:bolder;font-size:13px;">单月活动人力</p>
									 		</div>
									 		<div class="noteline"></div>
									 		<div class="notetext">
									 			<p class="notetextone">最新记录：<font style="color:#cc0000;">20人</font></p>
									 			<p class="notetexttwo">刷新记录时间：2011年01月</p>
									 			<p class="notetextone">刷新纪录者：王忠福营业组</p>
									 			<p class="notetexttwo">所属机构： 总公司营业部</p>
									 		</div>
									 	</div>
									</div>
								<div class="notebottom"></div>
								</div>
								 <div id="2010-10" class="shownone">
									 <div class="notetitle">营业区</div>
									 <div class="note">
									 	<div class="notedetail">
									 		<div class="notepic">
									 			<p><a href="images/25_p.jpg" title="杨春辉总监区" class="thickbox"><img src="images/25.jpg" style="display:block;width:165px;height:105px;"/></a></p>
									 			<p style="margin-top:20px;color:white;font-weight:bolder;font-size:13px;">总监区单月标准保费</p>
									 		</div>
									 		<div class="noteline"></div>
									 		<div class="notetext">
									 			<p class="notetextone">最新记录：<font style="color:#cc0000;">635840元</font></p>
									 			<p class="notetexttwo">刷新记录时间：2010年1月</p>
									 			<p class="notetextone">刷新纪录者：杨春辉总监区</p>
									 			<p class="notetexttwo">所属机构： 总公司营业部</p>
									 		</div>
									 	</div>
									 	<div class="Middleline"></div>
									 	<div class="notedetail">
									 		<div class="notepic">
									 			<p><a href="images/34.jpg" title="张尧巍总监区" class="thickbox"><img src="images/34_p.jpg" style="display:block;width:165px;height:105px;"/></a></p>
									 			<p style="margin-top:20px;color:white;font-weight:bolder;font-size:13px;">总监区单月件数</p>
									 		</div>
									 		<div class="noteline"></div>
									 		<div class="notetext">
									 			<p class="notetextone">挑战记录：<font style="color:#cc0000;">160件</font></p>
									 			<p class="notetexttwo">刷新记录时间：2010年8月</p>
									 			<p class="notetextone">刷新纪录者：张尧巍总监区</p>
									 			<p class="notetexttwo">所属机构： 黑龙江分公司</p>
									 		</div>
									 	</div>
									</div>
									<div class="notebottom"></div>
									<div class="notetitle">营业部</div>
									<div class="note">
									 	<div class="notedetail">
									 		<div class="notepic">
									 			<p><a href="images/14_p.jpg" title="梁爽营业部" class="thickbox"><img src="images/14.jpg" style="display:block;width:165px;height:105px;"/></a></p>
									 			<p style="margin-top:20px;color:white;font-weight:bolder;font-size:13px;">单月标准保费</p>
									 		</div>
									 		<div class="noteline"></div>
									 		<div class="notetext">
									 			<p class="notetextone">最新记录：<font style="color:#cc0000;">388832.61元</font></p>
									 			<p class="notetexttwo">刷新记录时间：2009年11月</p>
									 			<p class="notetextone">刷新纪录者：梁爽营业部</p>
									 			<p class="notetexttwo">所属机构： 湖北</p>
									 		</div>
									 	</div>
									 	<div class="Middleline"></div>
									 	<div class="notedetail">
									 		<div class="notepic">
									 			<p><a href="images/19_p.jpg" title="吴成祥营业部" class="thickbox"><img src="images/19.jpg" style="display:block;width:165px;height:105px;"/></a></p>
									 			<p style="margin-top:20px;color:white;font-weight:bolder;font-size:13px;">单月人均件数</p>
									 		</div>
									 		<div class="noteline"></div>
									 		<div class="notetext">
									 			<p class="notetextone">最新记录：<font style="color:#cc0000;">3.43件</font></p>
									 			<p class="notetexttwo">刷新记录时间：2010年1月</p>
									 			<p class="notetextone">刷新纪录者：吴成祥营业部</p>
									 			<p class="notetexttwo">所属机构： 总公司营业部</p>
									 		</div>
									 	</div>
									</div>
									<div class="notebottom"></div>
									<div class="notetitle">营业组</div>
									<div class="note2">
									 	<div class="notedetail">
									 		<div class="notepic">
									 			<p><a href="images/32.jpg" title="袁海燕营业组" class="thickbox"><img src="images/32_1.jpg" style="display:block;width:165px;height:105px;"/></a></p>
									 			<p style="margin-top:20px;color:white;font-weight:bolder;font-size:13px;">单月标准保费</p>
									 		</div>
									 		<div class="noteline"></div>
									 		<div class="notetext">
									 			<p class="notetextone">最新记录：<font style="color:#cc0000;">238715.1元</font></p>
									 			<p class="notetexttwo">刷新记录时间：2010年10月</p>
									 			<p class="notetextone">刷新纪录者：袁海燕营业组</p>
									 			<p class="notetexttwo">所属机构： 总公司营业部</p>
									 		</div>
									 	</div>
									 	<div class="Middleline"></div>
									 	<div class="notedetail">
									 		<div class="notepic">
									 			<p><a href="images/35.jpg" title="庞丽莉营业组" class="thickbox"><img src="images/35_p.jpg" style="display:block;width:165px;height:105px;"/></a></p>
									 			<p style="margin-top:20px;color:white;font-weight:bolder;font-size:13px;">单月件数</p>
									 		</div>
									 		<div class="noteline"></div>
									 		<div class="notetext">
									 			<p class="notetextone">最新记录：<font style="color:#cc0000;">33件</font></p>
									 			<p class="notetexttwo">刷新记录时间：2010年10月</p>
									 			<p class="notetextone">刷新纪录者：庞丽莉营业组</p>
									 			<p class="notetexttwo">所属机构： 黑龙江分公司</p>
									 		</div>
									 	</div>
									 	<div class="notedetail"  >
									 		<div class="notepic">
									 			<p><a id="testNO" href="images/35.jpg" title="庞丽莉营业组" class="thickbox"><img src="images/35_p.jpg" style="display:block;width:165px;height:105px;"/></a></p>
									 			<p style="margin-top:20px;color:white;font-weight:bolder;font-size:13px;">单月活动人力</p>
									 		</div>
									 		<div class="noteline"></div>
									 		<div class="notetext">
									 			<p class="notetextone">最新记录：<font style="color:#cc0000;">20人</font></p>
									 			<p class="notetexttwo">刷新记录时间：2010年10月</p>
									 			<p class="notetextone">刷新纪录者：庞丽莉营业组</p>
									 			<p class="notetexttwo">所属机构： 黑龙江分公司</p>
									 		</div>
									 	</div>
									</div>
								<div class="notebottom"></div>
								</div>
								<div id="2010-08" class="shownone">
									 <div class="notetitle">营业区</div>
									 <div class="note">
									 	<div class="notedetail">
									 		<div class="notepic">
									 			<p><a href="images/25_p.jpg" title="杨春辉总监区" class="thickbox"><img src="images/25.jpg" style="display:block;width:165px;height:105px;"/></a></p>
									 			<p style="margin-top:20px;color:white;font-weight:bolder;font-size:13px;">总监区单月标准保费</p>
									 		</div>
									 		<div class="noteline"></div>
									 		<div class="notetext">
									 			<p class="notetextone">最新记录：<font style="color:#cc0000;">635840元</font></p>
									 			<p class="notetexttwo">刷新记录时间：2010年1月</p>
									 			<p class="notetextone">刷新纪录者：杨春辉总监区</p>
									 			<p class="notetexttwo">所属机构： 总公司营业部</p>
									 		</div>
									 	</div>
									 	<div class="Middleline"></div>
									 	<div class="notedetail">
									 		<div class="notepic">
									 			<p><a href="images/34.jpg" title="白慧文总监区" class="thickbox"><img src="images/34_p.jpg" style="display:block;width:165px;height:105px;"/></a></p>
									 			<p style="margin-top:20px;color:white;font-weight:bolder;font-size:13px;">总监区单月件数</p>
									 		</div>
									 		<div class="noteline"></div>
									 		<div class="notetext">
									 			<p class="notetextone">挑战记录：<font style="color:#cc0000;">160件</font></p>
									 			<p class="notetexttwo">刷新记录时间：2010年8月</p>
									 			<p class="notetextone">刷新纪录者：张尧巍总监区</p>
									 			<p class="notetexttwo">所属机构： 黑龙江分公司</p>
									 		</div>
									 	</div>
									</div>
									<div class="notebottom"></div>
									<div class="notetitle">营业部</div>
									<div class="note">
									 	<div class="notedetail">
									 		<div class="notepic">
									 			<p><a href="images/14_p.jpg" title="梁爽营业部" class="thickbox"><img src="images/14.jpg" style="display:block;width:165px;height:105px;"/></a></p>
									 			<p style="margin-top:20px;color:white;font-weight:bolder;font-size:13px;">单月标准保费</p>
									 		</div>
									 		<div class="noteline"></div>
									 		<div class="notetext">
									 			<p class="notetextone">最新记录：<font style="color:#cc0000;">388832.61元</font></p>
									 			<p class="notetexttwo">刷新记录时间：2009年11月</p>
									 			<p class="notetextone">刷新纪录者：梁爽营业部</p>
									 			<p class="notetexttwo">所属机构： 湖北</p>
									 		</div>
									 	</div>
									 	<div class="Middleline"></div>
									 	<div class="notedetail">
									 		<div class="notepic">
									 			<p><a href="images/19_p.jpg" title="吴成祥营业部" class="thickbox"><img src="images/19.jpg" style="display:block;width:165px;height:105px;"/></a></p>
									 			<p style="margin-top:20px;color:white;font-weight:bolder;font-size:13px;">单月人均件数</p>
									 		</div>
									 		<div class="noteline"></div>
									 		<div class="notetext">
									 			<p class="notetextone">最新记录：<font style="color:#cc0000;">3.43件</font></p>
									 			<p class="notetexttwo">刷新记录时间：2010年1月</p>
									 			<p class="notetextone">刷新纪录者：吴成祥营业部</p>
									 			<p class="notetexttwo">所属机构： 总公司营业部</p>
									 		</div>
									 	</div>
									</div>
									<div class="notebottom"></div>
									<div class="notetitle">营业组</div>
									<div class="note2">
									 	<div class="notedetail">
									 		<div class="notepic">
									 			<p><a href="images/30_1.jpg" title="郭伟冬营业组" class="thickbox"><img src="images/30.jpg" style="display:block;width:165px;height:105px;"/></a></p>
									 			<p style="margin-top:20px;color:white;font-weight:bolder;font-size:13px;">单月标准保费</p>
									 		</div>
									 		<div class="noteline"></div>
									 		<div class="notetext">
									 			<p class="notetextone">最新记录：<font style="color:#cc0000;">205564.5元</font></p>
									 			<p class="notetexttwo">刷新记录时间：2010年6月</p>
									 			<p class="notetextone">刷新纪录者：郭伟冬营业组</p>
									 			<p class="notetexttwo">所属机构： 总公司营业部</p>
									 		</div>
									 	</div>
									 	<div class="Middleline"></div>
									 	<div class="notedetail">
									 		<div class="notepic">
									 			<p><a href="images/32.jpg" title="袁海燕营业组" class="thickbox"><img src="images/32_1.jpg" style="display:block;width:165px;height:105px;"/></a></p>
									 			<p style="margin-top:20px;color:white;font-weight:bolder;font-size:13px;">单月件数</p>
									 		</div>
									 		<div class="noteline"></div>
									 		<div class="notetext">
									 			<p class="notetextone">最新记录：<font style="color:#cc0000;">20件</font></p>
									 			<p class="notetexttwo">刷新记录时间：2010年6月</p>
									 			<p class="notetextone">刷新纪录者：袁海燕营业组</p>
									 			<p class="notetexttwo">所属机构： 总公司营业部</p>
									 		</div>
									 	</div>
									 	<div class="notedetail"  >
									 		<div class="notepic">
									 			<p><a href="images/27_p.jpg" title="夏洪军营业组" class="thickbox"><img src="images/27_2.jpg" style="display:block;width:165px;height:105px;"/></a></p>
									 			<p style="margin-top:20px;color:white;font-weight:bolder;font-size:13px;">单月活动人力</p>
									 		</div>
									 		<div class="noteline"></div>
									 		<div class="notetext">
									 			<p class="notetextone">最新记录：<font style="color:#cc0000;">8人</font></p>
									 			<p class="notetexttwo">刷新记录时间：2010年3月</p>
									 			<p class="notetextone">刷新纪录者：夏洪军营业组</p>
									 			<p class="notetexttwo">所属机构： 总公司营业部</p>
									 		</div>
									 	</div>
									</div>
								<div class="notebottom"></div>
								</div>
								<div id="2010-07" class="shownone">
									 <div class="notetitle">营业区</div>
									 <div class="note">
									 	<div class="notedetail">
									 		<div class="notepic">
									 			<p><a href="images/25_p.jpg" title="杨春辉总监区" class="thickbox"><img src="images/25.jpg" style="display:block;width:165px;height:105px;"/></a></p>
									 			<p style="margin-top:20px;color:white;font-weight:bolder;font-size:13px;">总监区单月标准保费</p>
									 		</div>
									 		<div class="noteline"></div>
									 		<div class="notetext">
									 			<p class="notetextone">最新记录：<font style="color:#cc0000;">635840元</font></p>
									 			<p class="notetexttwo">刷新记录时间：2010年1月</p>
									 			<p class="notetextone">刷新纪录者：杨春辉总监区</p>
									 			<p class="notetexttwo">所属机构： 总公司营业部</p>
									 		</div>
									 	</div>
									 	<div class="Middleline"></div>
									 	<div class="notedetail">
									 		<div class="notepic">
									 			<p><a href="images/29_1.jpg" title="白慧文总监区" class="thickbox"><img src="images/29.jpg" style="display:block;width:165px;height:105px;"/></a></p>
									 			<p style="margin-top:20px;color:white;font-weight:bolder;font-size:13px;">总监区单月件数</p>
									 		</div>
									 		<div class="noteline"></div>
									 		<div class="notetext">
									 			<p class="notetextone">挑战记录：<font style="color:#cc0000;">127件</font></p>
									 			<p class="notetexttwo">刷新记录时间：2010年7月</p>
									 			<p class="notetextone">刷新纪录者：白慧文总监区</p>
									 			<p class="notetexttwo">所属机构： 河北</p>
									 		</div>
									 	</div>
									</div>
									<div class="notebottom"></div>
									<div class="notetitle">营业部</div>
									<div class="note">
									 	<div class="notedetail">
									 		<div class="notepic">
									 			<p><a href="images/14_p.jpg" title="梁爽营业部" class="thickbox"><img src="images/14.jpg" style="display:block;width:165px;height:105px;"/></a></p>
									 			<p style="margin-top:20px;color:white;font-weight:bolder;font-size:13px;">单月标准保费</p>
									 		</div>
									 		<div class="noteline"></div>
									 		<div class="notetext">
									 			<p class="notetextone">最新记录：<font style="color:#cc0000;">388832.61元</font></p>
									 			<p class="notetexttwo">刷新记录时间：2009年11月</p>
									 			<p class="notetextone">刷新纪录者：梁爽营业部</p>
									 			<p class="notetexttwo">所属机构： 湖北</p>
									 		</div>
									 	</div>
									 	<div class="Middleline"></div>
									 	<div class="notedetail">
									 		<div class="notepic">
									 			<p><a href="images/19_p.jpg" title="吴成祥营业部" class="thickbox"><img src="images/19.jpg" style="display:block;width:165px;height:105px;"/></a></p>
									 			<p style="margin-top:20px;color:white;font-weight:bolder;font-size:13px;">单月人均件数</p>
									 		</div>
									 		<div class="noteline"></div>
									 		<div class="notetext">
									 			<p class="notetextone">最新记录：<font style="color:#cc0000;">3.43件</font></p>
									 			<p class="notetexttwo">刷新记录时间：2010年1月</p>
									 			<p class="notetextone">刷新纪录者：吴成祥营业部</p>
									 			<p class="notetexttwo">所属机构： 总公司营业部</p>
									 		</div>
									 	</div>
									</div>
									<div class="notebottom"></div>
									<div class="notetitle">营业组</div>
									<div class="note2">
									 	<div class="notedetail">
									 		<div class="notepic">
									 			<p><a href="images/30_1.jpg" title="郭伟冬营业组" class="thickbox"><img src="images/30.jpg" style="display:block;width:165px;height:105px;"/></a></p>
									 			<p style="margin-top:20px;color:white;font-weight:bolder;font-size:13px;">单月标准保费</p>
									 		</div>
									 		<div class="noteline"></div>
									 		<div class="notetext">
									 			<p class="notetextone">最新记录：<font style="color:#cc0000;">205564.5元</font></p>
									 			<p class="notetexttwo">刷新记录时间：2010年6月</p>
									 			<p class="notetextone">刷新纪录者：郭伟冬营业组</p>
									 			<p class="notetexttwo">所属机构： 总公司营业部</p>
									 		</div>
									 	</div>
									 	<div class="Middleline"></div>
									 	<div class="notedetail">
									 		<div class="notepic">
									 			<p><a href="images/32.jpg" title="袁海燕营业组" class="thickbox"><img src="images/32_1.jpg" style="display:block;width:165px;height:105px;"/></a></p>
									 			<p style="margin-top:20px;color:white;font-weight:bolder;font-size:13px;">单月件数</p>
									 		</div>
									 		<div class="noteline"></div>
									 		<div class="notetext">
									 			<p class="notetextone">最新记录：<font style="color:#cc0000;">20件</font></p>
									 			<p class="notetexttwo">刷新记录时间：2010年6月</p>
									 			<p class="notetextone">刷新纪录者：袁海燕营业组</p>
									 			<p class="notetexttwo">所属机构： 总公司营业部</p>
									 		</div>
									 	</div>
									 	<div class="notedetail"  >
									 		<div class="notepic">
									 			<p><a href="images/27_p.jpg" title="夏洪军营业组" class="thickbox"><img src="images/27_2.jpg" style="display:block;width:165px;height:105px;"/></a></p>
									 			<p style="margin-top:20px;color:white;font-weight:bolder;font-size:13px;">单月活动人力</p>
									 		</div>
									 		<div class="noteline"></div>
									 		<div class="notetext">
									 			<p class="notetextone">最新记录：<font style="color:#cc0000;">8人</font></p>
									 			<p class="notetexttwo">刷新记录时间：2010年3月</p>
									 			<p class="notetextone">刷新纪录者：夏洪军营业组</p>
									 			<p class="notetexttwo">所属机构： 总公司营业部</p>
									 		</div>
									 	</div>
									</div>
								<div class="notebottom"></div>
								</div>
								<div id="2010-06" class="shownone">
									 <div class="notetitle">营业区</div>
									 <div class="note">
									 	<div class="notedetail">
									 		<div class="notepic">
									 			<p><a href="images/25_p.jpg" title="杨春辉总监区" class="thickbox"><img src="images/25.jpg" style="display:block;width:165px;height:105px;"/></a></p>
									 			<p style="margin-top:20px;color:white;font-weight:bolder;font-size:13px;">总监区单月标准保费</p>
									 		</div>
									 		<div class="noteline"></div>
									 		<div class="notetext">
									 			<p class="notetextone">最新记录：<font style="color:#cc0000;">635840元</font></p>
									 			<p class="notetexttwo">刷新记录时间：2010年1月</p>
									 			<p class="notetextone">刷新纪录者：杨春辉总监区</p>
									 			<p class="notetexttwo">所属机构： 总公司营业部</p>
									 		</div>
									 	</div>
									 	<div class="Middleline"></div>
									 	<div class="notedetail">
									 		<div class="notepic">
									 			<p><a href="images/29_1.jpg" title="白慧文总监区" class="thickbox"><img src="images/29.jpg" style="display:block;width:165px;height:105px;"/></a></p>
									 			<p style="margin-top:20px;color:white;font-weight:bolder;font-size:13px;">总监区单月件数</p>
									 		</div>
									 		<div class="noteline"></div>
									 		<div class="notetext">
									 			<p class="notetextone">挑战记录：<font style="color:#cc0000;">123件</font></p>
									 			<p class="notetexttwo">刷新记录时间：2010年5月</p>
									 			<p class="notetextone">刷新纪录者：白慧文总监区</p>
									 			<p class="notetexttwo">所属机构： 河北</p>
									 		</div>
									 	</div>
									</div>
									<div class="notebottom"></div>
									<div class="notetitle">营业部</div>
									<div class="note">
									 	<div class="notedetail">
									 		<div class="notepic">
									 			<p><a href="images/14_p.jpg" title="梁爽营业部" class="thickbox"><img src="images/14.jpg" style="display:block;width:165px;height:105px;"/></a></p>
									 			<p style="margin-top:20px;color:white;font-weight:bolder;font-size:13px;">单月标准保费</p>
									 		</div>
									 		<div class="noteline"></div>
									 		<div class="notetext">
									 			<p class="notetextone">最新记录：<font style="color:#cc0000;">388832.61元</font></p>
									 			<p class="notetexttwo">刷新记录时间：2009年11月</p>
									 			<p class="notetextone">刷新纪录者：梁爽营业部</p>
									 			<p class="notetexttwo">所属机构： 湖北</p>
									 		</div>
									 	</div>
									 	<div class="Middleline"></div>
									 	<div class="notedetail">
									 		<div class="notepic">
									 			<p><a href="images/19_p.jpg" title="吴成祥营业部" class="thickbox"><img src="images/19.jpg" style="display:block;width:165px;height:105px;"/></a></p>
									 			<p style="margin-top:20px;color:white;font-weight:bolder;font-size:13px;">单月人均件数</p>
									 		</div>
									 		<div class="noteline"></div>
									 		<div class="notetext">
									 			<p class="notetextone">最新记录：<font style="color:#cc0000;">3.43件</font></p>
									 			<p class="notetexttwo">刷新记录时间：2010年1月</p>
									 			<p class="notetextone">刷新纪录者：吴成祥营业部</p>
									 			<p class="notetexttwo">所属机构： 总公司营业部</p>
									 		</div>
									 	</div>
									</div>
									<div class="notebottom"></div>
									<div class="notetitle">营业组</div>
									<div class="note2">
									 	<div class="notedetail">
									 		<div class="notepic">
									 			<p><a href="images/30_1.jpg" title="郭伟冬营业组" class="thickbox"><img src="images/30.jpg" style="display:block;width:165px;height:105px;"/></a></p>
									 			<p style="margin-top:20px;color:white;font-weight:bolder;font-size:13px;">单月标准保费</p>
									 		</div>
									 		<div class="noteline"></div>
									 		<div class="notetext">
									 			<p class="notetextone">最新记录：<font style="color:#cc0000;">205564.5元</font></p>
									 			<p class="notetexttwo">刷新记录时间：2010年6月</p>
									 			<p class="notetextone">刷新纪录者：郭伟冬营业组</p>
									 			<p class="notetexttwo">所属机构： 总公司营业部</p>
									 		</div>
									 	</div>
									 	<div class="Middleline"></div>
									 	<div class="notedetail">
									 		<div class="notepic">
									 			<p><a href="images/32.jpg" title="袁海燕营业组" class="thickbox"><img src="images/32_1.jpg" style="display:block;width:165px;height:105px;"/></a></p>
									 			<p style="margin-top:20px;color:white;font-weight:bolder;font-size:13px;">单月件数</p>
									 		</div>
									 		<div class="noteline"></div>
									 		<div class="notetext">
									 			<p class="notetextone">最新记录：<font style="color:#cc0000;">20件</font></p>
									 			<p class="notetexttwo">刷新记录时间：2010年6月</p>
									 			<p class="notetextone">刷新纪录者：袁海燕营业组</p>
									 			<p class="notetexttwo">所属机构： 总公司营业部</p>
									 		</div>
									 	</div>
									 	<div class="notedetail"  >
									 		<div class="notepic">
									 			<p><a href="images/27_p.jpg" title="夏洪军营业组" class="thickbox"><img src="images/27_2.jpg" style="display:block;width:165px;height:105px;"/></a></p>
									 			<p style="margin-top:20px;color:white;font-weight:bolder;font-size:13px;">单月活动人力</p>
									 		</div>
									 		<div class="noteline"></div>
									 		<div class="notetext">
									 			<p class="notetextone">最新记录：<font style="color:#cc0000;">8人</font></p>
									 			<p class="notetexttwo">刷新记录时间：2010年3月</p>
									 			<p class="notetextone">刷新纪录者：夏洪军营业组</p>
									 			<p class="notetexttwo">所属机构： 总公司营业部</p>
									 		</div>
									 	</div>
									</div>
								<div class="notebottom"></div>
								</div>
								<div id="2010-05" class="shownone">
									 <div class="notetitle">营业区</div>
									 <div class="note">
									 	<div class="notedetail">
									 		<div class="notepic">
									 			<p><a href="images/25_p.jpg" title="杨春辉总监区" class="thickbox"><img src="images/25.jpg" style="display:block;width:165px;height:105px;"/></a></p>
									 			<p style="margin-top:20px;color:white;font-weight:bolder;font-size:13px;">总监区单月标准保费</p>
									 		</div>
									 		<div class="noteline"></div>
									 		<div class="notetext">
									 			<p class="notetextone">最新记录：<font style="color:#cc0000;">635840元</font></p>
									 			<p class="notetexttwo">刷新记录时间：2010年1月</p>
									 			<p class="notetextone">刷新纪录者：杨春辉总监区</p>
									 			<p class="notetexttwo">所属机构： 总公司营业部</p>
									 		</div>
									 	</div>
									 	<div class="Middleline"></div>
									 	<div class="notedetail">
									 		<div class="notepic">
									 			<p><a href="images/29_1.jpg" title="白慧文总监区" class="thickbox"><img src="images/29.jpg" style="display:block;width:165px;height:105px;"/></a></p>
									 			<p style="margin-top:20px;color:white;font-weight:bolder;font-size:13px;">总监区单月件数</p>
									 		</div>
									 		<div class="noteline"></div>
									 		<div class="notetext">
									 			<p class="notetextone">挑战记录：<font style="color:#cc0000;">123件</font></p>
									 			<p class="notetexttwo">刷新记录时间：2010年5月</p>
									 			<p class="notetextone">刷新纪录者：白慧文总监区</p>
									 			<p class="notetexttwo">所属机构： 河北</p>
									 		</div>
									 	</div>
									</div>
									<div class="notebottom"></div>
									<div class="notetitle">营业部</div>
									<div class="note">
									 	<div class="notedetail">
									 		<div class="notepic">
									 			<p><a href="images/14_p.jpg" title="梁爽营业部" class="thickbox"><img src="images/14.jpg" style="display:block;width:165px;height:105px;"/></a></p>
									 			<p style="margin-top:20px;color:white;font-weight:bolder;font-size:13px;">单月标准保费</p>
									 		</div>
									 		<div class="noteline"></div>
									 		<div class="notetext">
									 			<p class="notetextone">最新记录：<font style="color:#cc0000;">388832.61元</font></p>
									 			<p class="notetexttwo">刷新记录时间：2009年11月</p>
									 			<p class="notetextone">刷新纪录者：梁爽营业部</p>
									 			<p class="notetexttwo">所属机构： 湖北</p>
									 		</div>
									 	</div>
									 	<div class="Middleline"></div>
									 	<div class="notedetail">
									 		<div class="notepic">
									 			<p><a href="images/19_p.jpg" title="吴成祥营业部" class="thickbox"><img src="images/19.jpg" style="display:block;width:165px;height:105px;"/></a></p>
									 			<p style="margin-top:20px;color:white;font-weight:bolder;font-size:13px;">单月人均件数</p>
									 		</div>
									 		<div class="noteline"></div>
									 		<div class="notetext">
									 			<p class="notetextone">最新记录：<font style="color:#cc0000;">3.43件</font></p>
									 			<p class="notetexttwo">刷新记录时间：2010年1月</p>
									 			<p class="notetextone">刷新纪录者：吴成祥营业部</p>
									 			<p class="notetexttwo">所属机构： 总公司营业部</p>
									 		</div>
									 	</div>
									</div>
									<div class="notebottom"></div>
									<div class="notetitle">营业组</div>
									<div class="note2">
									 	<div class="notedetail">
									 		<div class="notepic">
									 			<p><a href="images/30_1.jpg" title="郭伟冬营业组" class="thickbox"><img src="images/30.jpg" style="display:block;width:165px;height:105px;"/></a></p>
									 			<p style="margin-top:20px;color:white;font-weight:bolder;font-size:13px;">单月标准保费</p>
									 		</div>
									 		<div class="noteline"></div>
									 		<div class="notetext">
									 			<p class="notetextone">最新记录：<font style="color:#cc0000;">198039元</font></p>
									 			<p class="notetexttwo">刷新记录时间：2010年5月</p>
									 			<p class="notetextone">刷新纪录者：郭伟冬营业组</p>
									 			<p class="notetexttwo">所属机构： 总公司营业部</p>
									 		</div>
									 	</div>
									 	<div class="Middleline"></div>
									 	<div class="notedetail">
									 		<div class="notepic">
									 			<p><a href="images/21_p.jpg" title="梁聪营业组" class="thickbox"><img src="images/21.jpg" style="display:block;width:165px;height:105px;"/></a></p>
									 			<p style="margin-top:20px;color:white;font-weight:bolder;font-size:13px;">单月件数</p>
									 		</div>
									 		<div class="noteline"></div>
									 		<div class="notetext">
									 			<p class="notetextone">最新记录：<font style="color:#cc0000;">19件</font></p>
									 			<p class="notetexttwo">刷新记录时间：2010年1月</p>
									 			<p class="notetextone">刷新纪录者：梁聪营业组</p>
									 			<p class="notetexttwo">所属机构： 总公司营业部</p>
									 		</div>
									 	</div>
									 	<div class="notedetail"  >
									 		<div class="notepic">
									 			<p><a href="images/27_p.jpg" title="夏洪军营业组" class="thickbox"><img src="images/27_2.jpg" style="display:block;width:165px;height:105px;"/></a></p>
									 			<p style="margin-top:20px;color:white;font-weight:bolder;font-size:13px;">单月活动人力</p>
									 		</div>
									 		<div class="noteline"></div>
									 		<div class="notetext">
									 			<p class="notetextone">最新记录：<font style="color:#cc0000;">8人</font></p>
									 			<p class="notetexttwo">刷新记录时间：2010年3月</p>
									 			<p class="notetextone">刷新纪录者：夏洪军营业组</p>
									 			<p class="notetexttwo">所属机构： 总公司营业部</p>
									 		</div>
									 	</div>
									</div>
								<div class="notebottom"></div>
								</div>	
								<div id="2010-04" class="shownone">
									 <div class="notetitle">营业区</div>
									 <div class="note">
									 	<div class="notedetail">
									 		<div class="notepic">
									 			<p><a href="images/25_p.jpg" title="杨春辉总监区" class="thickbox"><img src="images/25.jpg" style="display:block;width:165px;height:105px;"/></a></p>
									 			<p style="margin-top:20px;color:white;font-weight:bolder;font-size:13px;">总监区单月标准保费</p>
									 		</div>
									 		<div class="noteline"></div>
									 		<div class="notetext">
									 			<p class="notetextone">最新记录：<font style="color:#cc0000;">635840元</font></p>
									 			<p class="notetexttwo">刷新记录时间：2010年1月</p>
									 			<p class="notetextone">刷新纪录者：杨春辉总监区</p>
									 			<p class="notetexttwo">所属机构： 总公司营业部</p>
									 		</div>
									 	</div>
									 	<div class="Middleline"></div>
									 	<div class="notedetail">
									 		<div class="notepic">
									 			<p><a href="images/25_p.jpg" title="杨春辉总监区" class="thickbox"><img src="images/25.jpg" style="display:block;width:165px;height:105px;"/></a></p>
									 			<p style="margin-top:20px;color:white;font-weight:bolder;font-size:13px;">总监区单月件数</p>
									 		</div>
									 		<div class="noteline"></div>
									 		<div class="notetext">
									 			<p class="notetextone">挑战记录：<font style="color:#cc0000;">102件</font></p>
									 			<p class="notetexttwo">刷新记录时间：2010年1月</p>
									 			<p class="notetextone">刷新纪录者：杨春辉总监区</p>
									 			<p class="notetexttwo">所属机构： 总公司营业部</p>
									 		</div>
									 	</div>
									</div>
									<div class="notebottom"></div>
									<div class="notetitle">营业部</div>
									<div class="note">
									 	<div class="notedetail">
									 		<div class="notepic">
									 			<p><a href="images/14_p.jpg" title="梁爽营业部" class="thickbox"><img src="images/14.jpg" style="display:block;width:165px;height:105px;"/></a></p>
									 			<p style="margin-top:20px;color:white;font-weight:bolder;font-size:13px;">单月标准保费</p>
									 		</div>
									 		<div class="noteline"></div>
									 		<div class="notetext">
									 			<p class="notetextone">最新记录：<font style="color:#cc0000;">388832.61元</font></p>
									 			<p class="notetexttwo">刷新记录时间：2009年11月</p>
									 			<p class="notetextone">刷新纪录者：梁爽营业部</p>
									 			<p class="notetexttwo">所属机构： 湖北</p>
									 		</div>
									 	</div>
									 	<div class="Middleline"></div>
									 	<div class="notedetail">
									 		<div class="notepic">
									 			<p><a href="images/19_p.jpg" title="吴成祥营业部" class="thickbox"><img src="images/19.jpg" style="display:block;width:165px;height:105px;"/></a></p>
									 			<p style="margin-top:20px;color:white;font-weight:bolder;font-size:13px;">单月人均件数</p>
									 		</div>
									 		<div class="noteline"></div>
									 		<div class="notetext">
									 			<p class="notetextone">最新记录：<font style="color:#cc0000;">3.43件</font></p>
									 			<p class="notetexttwo">刷新记录时间：2010年1月</p>
									 			<p class="notetextone">刷新纪录者：吴成祥营业部</p>
									 			<p class="notetexttwo">所属机构： 总公司营业部</p>
									 		</div>
									 	</div>
									</div>
									<div class="notebottom"></div>
									<div class="notetitle">营业组</div>
									<div class="note2">
									 	<div class="notedetail">
									 		<div class="notepic">
									 			<p><a  href="images/28_p.jpg" title="李吉庆营业组" class="thickbox"><img src="images/28_2.jpg" style="display:block;width:165px;height:105px;"/></a></p>
									 			<p style="margin-top:20px;color:white;font-weight:bolder;font-size:13px;">单月标准保费</p>
									 		</div>
									 		<div class="noteline"></div>
									 		<div class="notetext">
									 			<p class="notetextone">最新记录：<font style="color:#cc0000;">144483元</font></p>
									 			<p class="notetexttwo">刷新记录时间：2010年4月</p>
									 			<p class="notetextone">刷新纪录者：李吉庆营业组</p>
									 			<p class="notetexttwo">所属机构： 总公司营业部</p>
									 		</div>
									 	</div>
									 	<div class="Middleline"></div>
									 	<div class="notedetail">
									 		<div class="notepic">
									 			<p><a href="images/21_p.jpg" title="梁聪营业组" class="thickbox"><img src="images/21.jpg" style="display:block;width:165px;height:105px;"/></a></p>
									 			<p style="margin-top:20px;color:white;font-weight:bolder;font-size:13px;">单月件数</p>
									 		</div>
									 		<div class="noteline"></div>
									 		<div class="notetext">
									 			<p class="notetextone">最新记录：<font style="color:#cc0000;">19件</font></p>
									 			<p class="notetexttwo">刷新记录时间：2010年1月</p>
									 			<p class="notetextone">刷新纪录者：梁聪营业组</p>
									 			<p class="notetexttwo">所属机构： 总公司营业部</p>
									 		</div>
									 	</div>
									 	<div class="notedetail"  >
									 		<div class="notepic">
									 			<p><a href="images/27_p.jpg" title="夏洪军营业组" class="thickbox"><img src="images/27_2.jpg" style="display:block;width:165px;height:105px;"/></a></p>
									 			<p style="margin-top:20px;color:white;font-weight:bolder;font-size:13px;">单月活动人力</p>
									 		</div>
									 		<div class="noteline"></div>
									 		<div class="notetext">
									 			<p class="notetextone">最新记录：<font style="color:#cc0000;">8人</font></p>
									 			<p class="notetexttwo">刷新记录时间：2010年3月</p>
									 			<p class="notetextone">刷新纪录者：夏洪军营业组</p>
									 			<p class="notetexttwo">所属机构： 总公司营业部</p>
									 		</div>
									 	</div>
									</div>
								<div class="notebottom"></div>
								</div>
								<div id="2010-03" class="shownone">
									 <div class="notetitle">营业区</div>
									 <div class="note">
									 	<div class="notedetail">
									 		<div class="notepic">
									 			<p><a href="images/25_p.jpg" title="杨春辉总监区" class="thickbox"><img src="images/25.jpg" style="display:block;width:165px;height:105px;"/></a></p>
									 			<p style="margin-top:20px;color:white;font-weight:bolder;font-size:13px;">总监区单月标准保费</p>
									 		</div>
									 		<div class="noteline"></div>
									 		<div class="notetext">
									 			<p class="notetextone">最新记录：<font style="color:#cc0000;">635840元</font></p>
									 			<p class="notetexttwo">刷新记录时间：2010年1月</p>
									 			<p class="notetextone">刷新纪录者：杨春辉总监区</p>
									 			<p class="notetexttwo">所属机构： 总公司营业部</p>
									 		</div>
									 	</div>
									 	<div class="Middleline"></div>
									 	<div class="notedetail">
									 		<div class="notepic">
									 			<p><a href="images/25_p.jpg" title="杨春辉总监区" class="thickbox"><img src="images/25.jpg" style="display:block;width:165px;height:105px;"/></a></p>
									 			<p style="margin-top:20px;color:white;font-weight:bolder;font-size:13px;">总监区单月件数</p>
									 		</div>
									 		<div class="noteline"></div>
									 		<div class="notetext">
									 			<p class="notetextone">挑战记录：<font style="color:#cc0000;">102件</font></p>
									 			<p class="notetexttwo">刷新记录时间：2010年1月</p>
									 			<p class="notetextone">刷新纪录者：杨春辉总监区</p>
									 			<p class="notetexttwo">所属机构： 总公司营业部</p>
									 		</div>
									 	</div>
									</div>
									<div class="notebottom"></div>
									<div class="notetitle">营业部</div>
									<div class="note">
									 	<div class="notedetail">
									 		<div class="notepic">
									 			<p><a href="images/14_p.jpg" title="梁爽营业部" class="thickbox"><img src="images/14.jpg" style="display:block;width:165px;height:105px;"/></a></p>
									 			<p style="margin-top:20px;color:white;font-weight:bolder;font-size:13px;">单月标准保费</p>
									 		</div>
									 		<div class="noteline"></div>
									 		<div class="notetext">
									 			<p class="notetextone">最新记录：<font style="color:#cc0000;">388832.61元</font></p>
									 			<p class="notetexttwo">刷新记录时间：2009年11月</p>
									 			<p class="notetextone">刷新纪录者：梁爽营业部</p>
									 			<p class="notetexttwo">所属机构： 湖北</p>
									 		</div>
									 	</div>
									 	<div class="Middleline"></div>
									 	<div class="notedetail">
									 		<div class="notepic">
									 			<p><a href="images/19_p.jpg" title="吴成祥营业部" class="thickbox"><img src="images/19.jpg" style="display:block;width:165px;height:105px;"/></a></p>
									 			<p style="margin-top:20px;color:white;font-weight:bolder;font-size:13px;">单月人均件数</p>
									 		</div>
									 		<div class="noteline"></div>
									 		<div class="notetext">
									 			<p class="notetextone">最新记录：<font style="color:#cc0000;">3.43件</font></p>
									 			<p class="notetexttwo">刷新记录时间：2010年1月</p>
									 			<p class="notetextone">刷新纪录者：吴成祥营业部</p>
									 			<p class="notetexttwo">所属机构： 总公司营业部</p>
									 		</div>
									 	</div>
									</div>
									<div class="notebottom"></div>
									<div class="notetitle">营业组</div>
									<div class="note2">
									 	<div class="notedetail">
									 		<div class="notepic">
									 			<p><a  href="images/20_p.jpg" title="杨春辉营业组" class="thickbox"><img src="images/20.jpg" style="display:block;width:165px;height:105px;"/></a></p>
									 			<p style="margin-top:20px;color:white;font-weight:bolder;font-size:13px;">单月标准保费</p>
									 		</div>
									 		<div class="noteline"></div>
									 		<div class="notetext">
									 			<p class="notetextone">最新记录：<font style="color:#cc0000;">140757.30元</font></p>
									 			<p class="notetexttwo">刷新记录时间：2010年1月</p>
									 			<p class="notetextone">刷新纪录者：李吉庆营业组</p>
									 			<p class="notetexttwo">所属机构： 总公司营业部</p>
									 		</div>
									 	</div>
									 	<div class="Middleline"></div>
									 	<div class="notedetail">
									 		<div class="notepic">
									 			<p><a href="images/21_p.jpg" title="梁聪营业组" class="thickbox"><img src="images/21.jpg" style="display:block;width:165px;height:105px;"/></a></p>
									 			<p style="margin-top:20px;color:white;font-weight:bolder;font-size:13px;">单月件数</p>
									 		</div>
									 		<div class="noteline"></div>
									 		<div class="notetext">
									 			<p class="notetextone">最新记录：<font style="color:#cc0000;">19件</font></p>
									 			<p class="notetexttwo">刷新记录时间：2010年1月</p>
									 			<p class="notetextone">刷新纪录者：梁聪营业组</p>
									 			<p class="notetexttwo">所属机构： 总公司营业部</p>
									 		</div>
									 	</div>
									 	<div class="notedetail"  >
									 		<div class="notepic">
									 			<p><a href="images/27_p.jpg" title="夏洪军营业组" class="thickbox"><img src="images/27_2.jpg" style="display:block;width:165px;height:105px;"/></a></p>
									 			<p style="margin-top:20px;color:white;font-weight:bolder;font-size:13px;">单月活动人力</p>
									 		</div>
									 		<div class="noteline"></div>
									 		<div class="notetext">
									 			<p class="notetextone">最新记录：<font style="color:#cc0000;">8人</font></p>
									 			<p class="notetexttwo">刷新记录时间：2010年3月</p>
									 			<p class="notetextone">刷新纪录者：夏洪军营业组</p>
									 			<p class="notetexttwo">所属机构： 总公司营业部</p>
									 		</div>
									 	</div>
									</div>
								<div class="notebottom"></div>
								</div>
								<div id="2010-02" class="shownone">
									 <div class="notetitle">营业区</div>
									 <div class="note">
									 	<div class="notedetail">
									 		<div class="notepic">
									 			<p><a href="images/25_p.jpg" title="杨春辉总监区" class="thickbox"><img src="images/25.jpg" style="display:block;width:165px;height:105px;"/></a></p>
									 			<p style="margin-top:20px;color:white;font-weight:bolder;font-size:13px;">总监区单月标准保费</p>
									 		</div>
									 		<div class="noteline"></div>
									 		<div class="notetext">
									 			<p class="notetextone">最新记录：<font style="color:#cc0000;">635840元</font></p>
									 			<p class="notetexttwo">刷新记录时间：2010年1月</p>
									 			<p class="notetextone">刷新纪录者：杨春辉总监区</p>
									 			<p class="notetexttwo">所属机构： 总公司营业部</p>
									 		</div>
									 	</div>
									 	<div class="Middleline"></div>
									 	<div class="notedetail">
									 		<div class="notepic">
									 			<p><a href="images/25_p.jpg" title="杨春辉总监区" class="thickbox"><img src="images/25.jpg" style="display:block;width:165px;height:105px;"/></a></p>
									 			<p style="margin-top:20px;color:white;font-weight:bolder;font-size:13px;">总监区单月件数</p>
									 		</div>
									 		<div class="noteline"></div>
									 		<div class="notetext">
									 			<p class="notetextone">挑战记录：<font style="color:#cc0000;">102件</font></p>
									 			<p class="notetexttwo">刷新记录时间：2010年1月</p>
									 			<p class="notetextone">刷新纪录者：杨春辉总监区</p>
									 			<p class="notetexttwo">所属机构： 总公司营业部</p>
									 		</div>
									 	</div>
									</div>
									<div class="notebottom"></div>
									<div class="notetitle">营业部</div>
									<div class="note">
									 	<div class="notedetail">
									 		<div class="notepic">
									 			<p><a href="images/14_p.jpg" title="梁爽营业部" class="thickbox"><img src="images/14.jpg" style="display:block;width:165px;height:105px;"/></a></p>
									 			<p style="margin-top:20px;color:white;font-weight:bolder;font-size:13px;">单月标准保费</p>
									 		</div>
									 		<div class="noteline"></div>
									 		<div class="notetext">
									 			<p class="notetextone">最新记录：<font style="color:#cc0000;">388832.61元</font></p>
									 			<p class="notetexttwo">刷新记录时间：2009年11月</p>
									 			<p class="notetextone">刷新纪录者：梁爽营业部</p>
									 			<p class="notetexttwo">所属机构： 湖北</p>
									 		</div>
									 	</div>
									 	<div class="Middleline"></div>
									 	<div class="notedetail">
									 		<div class="notepic">
									 			<p><a href="images/19_p.jpg" title="吴成祥营业部" class="thickbox"><img src="images/19.jpg" style="display:block;width:165px;height:105px;"/></a></p>
									 			<p style="margin-top:20px;color:white;font-weight:bolder;font-size:13px;">单月人均件数</p>
									 		</div>
									 		<div class="noteline"></div>
									 		<div class="notetext">
									 			<p class="notetextone">最新记录：<font style="color:#cc0000;">3.43件</font></p>
									 			<p class="notetexttwo">刷新记录时间：2010年1月</p>
									 			<p class="notetextone">刷新纪录者：吴成祥营业部</p>
									 			<p class="notetexttwo">所属机构： 总公司营业部</p>
									 		</div>
									 	</div>
									</div>
									<div class="notebottom"></div>
									<div class="notetitle">营业组</div>
									<div class="note2">
									 	<div class="notedetail">
									 		<div class="notepic">
									 			<p><a  href="images/20_p.jpg" title="杨春辉营业组" class="thickbox"><img src="images/20.jpg" style="display:block;width:165px;height:105px;"/></a></p>
									 			<p style="margin-top:20px;color:white;font-weight:bolder;font-size:13px;">单月标准保费</p>
									 		</div>
									 		<div class="noteline"></div>
									 		<div class="notetext">
									 			<p class="notetextone">最新记录：<font style="color:#cc0000;">140757.30元</font></p>
									 			<p class="notetexttwo">刷新记录时间：2010年1月</p>
									 			<p class="notetextone">刷新纪录者：李吉庆营业组</p>
									 			<p class="notetexttwo">所属机构： 总公司营业部</p>
									 		</div>
									 	</div>
									 	<div class="Middleline"></div>
									 	<div class="notedetail">
									 		<div class="notepic">
									 			<p><a href="images/21_p.jpg" title="梁聪营业组" class="thickbox"><img src="images/21.jpg" style="display:block;width:165px;height:105px;"/></a></p>
									 			<p style="margin-top:20px;color:white;font-weight:bolder;font-size:13px;">单月件数</p>
									 		</div>
									 		<div class="noteline"></div>
									 		<div class="notetext">
									 			<p class="notetextone">最新记录：<font style="color:#cc0000;">19件</font></p>
									 			<p class="notetexttwo">刷新记录时间：2010年1月</p>
									 			<p class="notetextone">刷新纪录者：梁聪营业组</p>
									 			<p class="notetexttwo">所属机构： 总公司营业部</p>
									 		</div>
									 	</div>
									 	<div class="notedetail"  >
									 		<div class="notepic">
									 			<p><a href="images/6_p.jpg" title="曲宗斌营业组" class="thickbox"><img src="images/6.jpg" style="display:block;width:165px;height:105px;"/></a></p>
									 			<p style="margin-top:20px;color:white;font-weight:bolder;font-size:13px;">单月活动人力</p>
									 		</div>
									 		<div class="noteline"></div>
									 		<div class="notetext">
									 			<p class="notetextone">最新记录：<font style="color:#cc0000;">6人</font></p>
									 			<p class="notetexttwo">刷新记录时间：2009年7月</p>
									 			<p class="notetextone">刷新纪录者：曲宗斌营业组</p>
									 			<p class="notetexttwo">所属机构： 总公司营业部</p>
									 		</div>
									 	</div>
									</div>
								<div class="notebottom"></div>
								</div>
								<div id="2010-01" class="shownone">
									 <div class="notetitle">营业区</div>
									 <div class="note">
									 	<div class="notedetail">
									 		<div class="notepic">
									 			<p><a href="images/25_p.jpg" title="杨春辉总监区" class="thickbox"><img src="images/25.jpg" style="display:block;width:165px;height:105px;"/></a></p>
									 			<p style="margin-top:20px;color:white;font-weight:bolder;font-size:13px;">总监区单月标准保费</p>
									 		</div>
									 		<div class="noteline"></div>
									 		<div class="notetext">
									 			<p class="notetextone">最新记录：<font style="color:#cc0000;">635840元</font></p>
									 			<p class="notetexttwo">刷新记录时间：2010年1月</p>
									 			<p class="notetextone">刷新纪录者：杨春辉总监区</p>
									 			<p class="notetexttwo">所属机构： 总公司营业部</p>
									 		</div>
									 	</div>
									 	<div class="Middleline"></div>
									 	<div class="notedetail">
									 		<div class="notepic">
									 			<p><a href="images/25_p.jpg" title="杨春辉总监区" class="thickbox"><img src="images/25.jpg" style="display:block;width:165px;height:105px;"/></a></p>
									 			<p style="margin-top:20px;color:white;font-weight:bolder;font-size:13px;">总监区单月件数</p>
									 		</div>
									 		<div class="noteline"></div>
									 		<div class="notetext">
									 			<p class="notetextone">挑战记录：<font style="color:#cc0000;">102件</font></p>
									 			<p class="notetexttwo">刷新记录时间：2010年1月</p>
									 			<p class="notetextone">刷新纪录者：杨春辉总监区</p>
									 			<p class="notetexttwo">所属机构： 总公司营业部</p>
									 		</div>
									 	</div>
									</div>
									<div class="notebottom"></div>
									<div class="notetitle">营业部</div>
									<div class="note">
									 	<div class="notedetail">
									 		<div class="notepic">
									 			<p><a href="images/14_p.jpg" title="梁爽营业部" class="thickbox"><img src="images/14.jpg" style="display:block;width:165px;height:105px;"/></a></p>
									 			<p style="margin-top:20px;color:white;font-weight:bolder;font-size:13px;">单月标准保费</p>
									 		</div>
									 		<div class="noteline"></div>
									 		<div class="notetext">
									 			<p class="notetextone">最新记录：<font style="color:#cc0000;">388832.61元</font></p>
									 			<p class="notetexttwo">刷新记录时间：2009年11月</p>
									 			<p class="notetextone">刷新纪录者：梁爽营业部</p>
									 			<p class="notetexttwo">所属机构： 湖北</p>
									 		</div>
									 	</div>
									 	<div class="Middleline"></div>
									 	<div class="notedetail">
									 		<div class="notepic">
									 			<p><a href="images/19_p.jpg" title="吴成祥营业部" class="thickbox"><img src="images/19.jpg" style="display:block;width:165px;height:105px;"/></a></p>
									 			<p style="margin-top:20px;color:white;font-weight:bolder;font-size:13px;">单月人均件数</p>
									 		</div>
									 		<div class="noteline"></div>
									 		<div class="notetext">
									 			<p class="notetextone">最新记录：<font style="color:#cc0000;">3.43件</font></p>
									 			<p class="notetexttwo">刷新记录时间：2010年1月</p>
									 			<p class="notetextone">刷新纪录者：吴成祥营业部</p>
									 			<p class="notetexttwo">所属机构： 总公司营业部</p>
									 		</div>
									 	</div>
									</div>
									<div class="notebottom"></div>
									<div class="notetitle">营业组</div>
									<div class="note2">
									 	<div class="notedetail">
									 		<div class="notepic">
									 			<p><a  href="images/20_p.jpg" title="杨春辉营业组" class="thickbox"><img src="images/20.jpg" style="display:block;width:165px;height:105px;"/></a></p>
									 			<p style="margin-top:20px;color:white;font-weight:bolder;font-size:13px;">单月标准保费</p>
									 		</div>
									 		<div class="noteline"></div>
									 		<div class="notetext">
									 			<p class="notetextone">最新记录：<font style="color:#cc0000;">140757.30元</font></p>
									 			<p class="notetexttwo">刷新记录时间：2010年1月</p>
									 			<p class="notetextone">刷新纪录者：杨春辉营业组</p>
									 			<p class="notetexttwo">所属机构： 总公司营业部</p>
									 		</div>
									 	</div>
									 	<div class="Middleline"></div>
									 	<div class="notedetail">
									 		<div class="notepic">
									 			<p><a href="images/21_p.jpg" title="梁聪营业组" class="thickbox"><img src="images/21.jpg" style="display:block;width:165px;height:105px;"/></a></p>
									 			<p style="margin-top:20px;color:white;font-weight:bolder;font-size:13px;">单月件数</p>
									 		</div>
									 		<div class="noteline"></div>
									 		<div class="notetext">
									 			<p class="notetextone">最新记录：<font style="color:#cc0000;">19件</font></p>
									 			<p class="notetexttwo">刷新记录时间：2010年1月</p>
									 			<p class="notetextone">刷新纪录者：梁聪营业组</p>
									 			<p class="notetexttwo">所属机构： 总公司营业部</p>
									 		</div>
									 	</div>
									 	<div class="notedetail"  >
									 		<div class="notepic">
									 			<p><a href="images/6_p.jpg" title="曲宗斌营业组" class="thickbox"><img src="images/6.jpg" style="display:block;width:165px;height:105px;"/></a></p>
									 			<p style="margin-top:20px;color:white;font-weight:bolder;font-size:13px;">单月活动人力</p>
									 		</div>
									 		<div class="noteline"></div>
									 		<div class="notetext">
									 			<p class="notetextone">最新记录：<font style="color:#cc0000;">6人</font></p>
									 			<p class="notetexttwo">刷新记录时间：2009年7月</p>
									 			<p class="notetextone">刷新纪录者：曲宗斌营业组</p>
									 			<p class="notetexttwo">所属机构： 总公司营业部</p>
									 		</div>
									 	</div>
									</div>
								<div class="notebottom"></div>
								</div>
								<div id="2009-12" class="shownone">
									<div class="notetitle">营业部</div>
									<div class="note">
									 	<div class="notedetail">
									 		<div class="notepic">
									 			<p><a href="images/14_p.jpg" title="梁爽营业部" class="thickbox"><img src="images/14.jpg" style="display:block;width:165px;height:105px;"/></a></p>
									 			<p style="margin-top:20px;color:white;font-weight:bolder;font-size:13px;">单月标准保费</p>
									 		</div>
									 		<div class="noteline"></div>
									 		<div class="notetext">
									 			<p class="notetextone">最新记录：<font style="color:#cc0000;">388832.61元</font></p>
									 			<p class="notetexttwo">刷新记录时间：2009年11月</p>
									 			<p class="notetextone">刷新纪录者：梁爽营业部</p>
									 			<p class="notetexttwo">所属机构： 湖北</p>
									 		</div>
									 	</div>
									 	<div class="Middleline"></div>
									 	<div class="notedetail">
									 		<div class="notepic">
									 			<p><a href="images/15_p.jpg" title="胡敏营业部 " class="thickbox"><img src="images/15.jpg" style="display:block;width:165px;height:105px;"/></a></p>
									 			<p style="margin-top:20px;color:white;font-weight:bolder;font-size:13px;">单月人均件数</p>
									 		</div>
									 		<div class="noteline"></div>
									 		<div class="notetext">
									 			<p class="notetextone">最新记录：<font style="color:#cc0000;">3.25件</font></p>
									 			<p class="notetexttwo">刷新记录时间：2009年11月</p>
									 			<p class="notetextone">刷新纪录者：胡敏营业部</p>
									 			<p class="notetexttwo">所属机构： 湖北</p>
									 		</div>
									 	</div>
									</div>
									<div class="notebottom"></div>
									<div class="notetitle"></div>
									<div class="note2">
									 	<div class="notedetail">
									 		<div class="notepic">
									 			<p><a  href="images/16_p.jpg" title="李玲营业组" class="thickbox"><img src="images/16.jpg" style="display:block;width:165px;height:105px;"/></a></p>
									 			<p style="margin-top:20px;color:white;font-weight:bolder;font-size:13px;">单月标准保费</p>
									 		</div>
									 		<div class="noteline"></div>
									 		<div class="notetext">
									 			<p class="notetextone">最新记录：<font style="color:#cc0000;">70115.85元</font></p>
									 			<p class="notetexttwo">刷新记录时间：2009年11月</p>
									 			<p class="notetextone">刷新纪录者：李玲营业组</p>
									 			<p class="notetexttwo">所属机构： 湖北</p>
									 		</div>
									 	</div>
									 	<div class="Middleline"></div>
									 	<div class="notedetail">
									 		<div class="notepic">
									 			<p><a href="images/17_p.jpg" title="周红成营业组" class="thickbox"><img src="images/17.jpg" style="display:block;width:165px;height:105px;"/></a></p>
									 			<p style="margin-top:20px;color:white;font-weight:bolder;font-size:13px;">单月件数</p>
									 		</div>
									 		<div class="noteline"></div>
									 		<div class="notetext">
									 			<p class="notetextone">最新记录：<font style="color:#cc0000;">13件</font></p>
									 			<p class="notetexttwo">刷新记录时间：2009年11月</p>
									 			<p class="notetextone">刷新纪录者：周红成营业组</p>
									 			<p class="notetexttwo">所属机构： 湖北</p>
									 		</div>
									 	</div>
									 	<div class="notedetail"  >
									 		<div class="notepic">
									 			<p><a href="images/6_p.jpg" title="曲宗斌营业组" class="thickbox"><img src="images/6.jpg" style="display:block;width:165px;height:105px;"/></a></p>
									 			<p style="margin-top:20px;color:white;font-weight:bolder;font-size:13px;">单月活动人力</p>
									 		</div>
									 		<div class="noteline"></div>
									 		<div class="notetext">
									 			<p class="notetextone">最新记录：<font style="color:#cc0000;">6人</font></p>
									 			<p class="notetexttwo">刷新记录时间：2009年7月</p>
									 			<p class="notetextone">刷新纪录者：曲宗斌营业组</p>
									 			<p class="notetexttwo">所属机构： 总公司营业部</p>
									 		</div>
									 	</div>
									</div>
								<div class="notebottom"></div>
								</div>
								<div id="2009-11" class="shownone">
									<div class="notetitle">营业部</div>
									<div class="note">
									 	<div class="notedetail">
									 		<div class="notepic">
									 			<p><a href="images/14_p.jpg" title="梁爽营业部" class="thickbox"><img src="images/14.jpg" style="display:block;width:165px;height:105px;"/></a></p>
									 			<p style="margin-top:20px;color:white;font-weight:bolder;font-size:13px;">单月标准保费</p>
									 		</div>
									 		<div class="noteline"></div>
									 		<div class="notetext">
									 			<p class="notetextone">最新记录：<font style="color:#cc0000;">388832.61元</font></p>
									 			<p class="notetexttwo">刷新记录时间：2009年11月</p>
									 			<p class="notetextone">刷新纪录者：梁爽营业部</p>
									 			<p class="notetexttwo">所属机构： 湖北</p>
									 		</div>
									 	</div>
									 	<div class="Middleline"></div>
									 	<div class="notedetail">
									 		<div class="notepic">
									 			<p><a href="images/15_p.jpg" title="胡敏营业部 " class="thickbox"><img src="images/15.jpg" style="display:block;width:165px;height:105px;"/></a></p>
									 			<p style="margin-top:20px;color:white;font-weight:bolder;font-size:13px;">单月人均件数</p>
									 		</div>
									 		<div class="noteline"></div>
									 		<div class="notetext">
									 			<p class="notetextone">最新记录：<font style="color:#cc0000;">3.25件</font></p>
									 			<p class="notetexttwo">刷新记录时间：2009年11月</p>
									 			<p class="notetextone">刷新纪录者：胡敏营业部</p>
									 			<p class="notetexttwo">所属机构： 湖北</p>
									 		</div>
									 	</div>
									</div>
									<div class="notebottom"></div>
									<div class="notetitle">营业组</div>
									<div class="note2">
									 	<div class="notedetail">
									 		<div class="notepic">
									 			<p><a  href="images/16_p.jpg" title="李玲营业组" class="thickbox"><img src="images/16.jpg" style="display:block;width:165px;height:105px;"/></a></p>
									 			<p style="margin-top:20px;color:white;font-weight:bolder;font-size:13px;">单月标准保费</p>
									 		</div>
									 		<div class="noteline"></div>
									 		<div class="notetext">
									 			<p class="notetextone">最新记录：<font style="color:#cc0000;">70115.85元</font></p>
									 			<p class="notetexttwo">刷新记录时间：2009年11月</p>
									 			<p class="notetextone">刷新纪录者：李玲营业组</p>
									 			<p class="notetexttwo">所属机构： 湖北</p>
									 		</div>
									 	</div>
									 	<div class="Middleline"></div>
									 	<div class="notedetail">
									 		<div class="notepic">
									 			<p><a href="images/17_p.jpg" title="周红成营业组" class="thickbox"><img src="images/17.jpg" style="display:block;width:165px;height:105px;"/></a></p>
									 			<p style="margin-top:20px;color:white;font-weight:bolder;font-size:13px;">单月件数</p>
									 		</div>
									 		<div class="noteline"></div>
									 		<div class="notetext">
									 			<p class="notetextone">最新记录：<font style="color:#cc0000;">13件</font></p>
									 			<p class="notetexttwo">刷新记录时间：2009年11月</p>
									 			<p class="notetextone">刷新纪录者：周红成营业组</p>
									 			<p class="notetexttwo">所属机构： 湖北</p>
									 		</div>
									 	</div>
									 	<div class="notedetail"  >
									 		<div class="notepic">
									 			<p><a href="images/6_p.jpg" title="曲宗斌营业组" class="thickbox"><img src="images/6.jpg" style="display:block;width:165px;height:105px;"/></a></p>
									 			<p style="margin-top:20px;color:white;font-weight:bolder;font-size:13px;">单月活动人力</p>
									 		</div>
									 		<div class="noteline"></div>
									 		<div class="notetext">
									 			<p class="notetextone">最新记录：<font style="color:#cc0000;">6人</font></p>
									 			<p class="notetexttwo">刷新记录时间：2009年7月</p>
									 			<p class="notetextone">刷新纪录者：曲宗斌营业组</p>
									 			<p class="notetexttwo">所属机构： 总公司营业部</p>
									 		</div>
									 	</div>
									</div>
								<div class="notebottom"></div>
								</div>
								<div id="2009-10" class="shownone" >
									<div class="notetitle">营业部</div>
									<div class="note">
									 	<div class="notedetail">
									 		<div class="notepic">
									 			<p><a href="images/12_p.jpg" title="李鹏营业部" class="thickbox"><img src="images/12.jpg" style="display:block;width:165px;height:105px;"/></a></p>
									 			<p style="margin-top:20px;color:white;font-weight:bolder;font-size:13px;">单月标准保费</p>
									 		</div>
									 		<div class="noteline"></div>
									 		<div class="notetext">
									 			<p class="notetextone">最新记录：<font style="color:#cc0000;">70559.2元</font></p>
									 			<p class="notetexttwo">刷新记录时间：2009年10月</p>
									 			<p class="notetextone">刷新纪录者：李鹏营业部</p>
									 			<p class="notetexttwo">所属机构：总公司营业部</p>
									 		</div>
									 	</div>
									 	<div class="Middleline"></div>
									 	<div class="notedetail">
									 		<div class="notepic">
									 			<p><a href="images/3_p.jpg" title="康秀伟营业部" class="thickbox"><img src="images/3.jpg" style="display:block;width:165px;height:105px;"/></a></p>
									 			<p style="margin-top:20px;color:white;font-weight:bolder;font-size:13px;">单月人均件数</p>
									 		</div>
									 		<div class="noteline"></div>
									 		<div class="notetext">
									 			<p class="notetextone">最新记录：<font style="color:#cc0000;">1.6件</font></p>
									 			<p class="notetexttwo">刷新记录时间：2009年7月</p>
									 			<p class="notetextone">刷新纪录者：康秀伟营业部</p>
									 			<p class="notetexttwo">所属机构：总公司营业部</p>
									 		</div>
									 	</div>
									</div>
									<div class="notebottom"></div>
									<div class="notetitle">营业组</div>
									<div class="note2">
									 	<div class="notedetail">
									 		<div class="notepic">
									 			<p><a  href="images/13_p.jpg" title="李鹏营业组" class="thickbox"><img src="images/13.jpg" style="display:block;width:165px;height:105px;"/></a></p>
									 			<p style="margin-top:20px;color:white;font-weight:bolder;font-size:13px;">单月标准保费</p>
									 		</div>
									 		<div class="noteline"></div>
									 		<div class="notetext">
									 			<p class="notetextone">最新记录：<font style="color:#cc0000;">48860.6元</font></p>
									 			<p class="notetexttwo">刷新记录时间：2009年10月</p>
									 			<p class="notetextone">刷新纪录者：李玲营业组</p>
									 			<p class="notetexttwo">所属机构： 湖北</p>
									 		</div>
									 	</div>
									 	<div class="Middleline"></div>
									 	<div class="notedetail">
									 		<div class="notepic">
									 			<p><a href="images/4_p.jpg" title="刘惠敏营业组" class="thickbox"><img src="images/4.jpg" style="display:block;width:165px;height:105px;"/></a></p>
									 			<p style="margin-top:20px;color:white;font-weight:bolder;font-size:13px;">单月件数</p>
									 		</div>
									 		<div class="noteline"></div>
									 		<div class="notetext">
									 			<p class="notetextone">最新记录：<font style="color:#cc0000;">12件</font></p>
									 			<p class="notetexttwo">刷新记录时间：2009年7月</p>
									 			<p class="notetextone">刷新纪录者：刘惠敏营业组</p>
									 			<p class="notetexttwo">所属机构： 总公司营业部</p>
									 		</div>
									 	</div>
									 	<div class="notedetail"  >
									 		<div class="notepic">
									 			<p><a href="images/6_p.jpg" title="曲宗斌营业组" class="thickbox"><img src="images/6.jpg" style="display:block;width:165px;height:105px;"/></a></p>
									 			<p style="margin-top:20px;color:white;font-weight:bolder;font-size:13px;">单月活动人力</p>
									 		</div>
									 		<div class="noteline"></div>
									 		<div class="notetext">
									 			<p class="notetextone">最新记录：<font style="color:#cc0000;">6人</font></p>
									 			<p class="notetexttwo">刷新记录时间：2009年7月</p>
									 			<p class="notetextone">刷新纪录者：曲宗斌营业组</p>
									 			<p class="notetexttwo">所属机构： 总公司营业部</p>
									 		</div>
									 	</div>
									</div>
								<div class="notebottom"></div>
								</div>
																			
								<div  id="uboxstyle" >
									<span style="display:block;color:#666666;font-weight:bolder;font-size:13px;float:left;width:80px;height:24px;padding-top:8px;text-align:left;">查看记录</span>
									<select name="language" id="language"  >
										<option value="2011-01" selected="selected" >2011年01月</option>
										<option value="2010-10">2010年10月</option>
										<option value="2010-08">2010年8月</option>
										<option value="2010-07">2010年7月</option>
										<option value="2010-06">2010年6月</option>
										<option value="2010-05">2010年5月</option>
										<option value="2010-04">2010年4月</option>
										<option value="2010-03">2010年3月</option>
										<option value="2010-02">2010年2月</option>
										<option value="2010-01">2010年1月</option>
										<option value="2009-12">2009年12月</option>
										<option value="2009-11">2009年11月</option>
										<option value="2009-10">2009年10月</option>
									</select>
								</div>
							<%} %>
							<form id="inputForm" name="inputForm" method="post" action="index2.jsp"></form>
							
						</div>
						<div class="DcontentBottom"></div>
					</div>
					
				</div>
			   <div id="copyright">
					<jsp:include page="/bn/cms/rushHour/copyright.jsp" flush="true" />
				</div>
			</div>
		</div> 
	</body>
	<script type="text/javascript">
				function onkey(){
				<%if(!com.sinosoft.common.Data.hasValue(LSUserCode)){ %>
				    if(window.event.keyCode==13){
				        logon();
				    }   
				<%}%>
				}
				function logon(){
					var UserID = $("input[name=UserID]").val();
			 		var Password = $("input[name=Password]").val();;
					var Rand = $("input[name=Rand]").val();;
					var nowtime = new Date();
					$.ajax({
						type:"post",
						url:"login.jsp",
						dataType:"html",
						data:"random="+nowtime+"&UserCode="+UserID+"&Password="+Password+"&Rand="+Rand,
						success:function(htmlContent) {
							if(htmlContent=="correct"){
								document.getElementById("inputForm").submit();
							}else{
								changeImage();
								alert(htmlContent);
							}
						},
						error:function() {
							alert("error");
						}
					});
				}
				
				var id = 0;
				function changeImage(){
					id = 2 - id; 
					document.getElementById("mFrame").src="<%=request.getContextPath()%>/passport/registration/image.jsp?id="+id;
					id = id -1;
				}
				 

		var selects = document.getElementsByTagName('select');
		var isIE = (document.all && window.ActiveXObject && !window.opera) ? true : false;
		function getObj(id) {
			return document.getElementById(id);
		}
		function stopBubbling (ev) {	
			ev.stopPropagation();
		}
		function rSelects() {
		
			for (i=0;i<selects.length;i++){
				selects[i].style.display = 'none';
				select_tag = document.createElement('div');
					select_tag.id = 'select_' + selects[i].name;
					select_tag.className = 'select_box';
				selects[i].parentNode.insertBefore(select_tag,selects[i]);
				select_info = document.createElement('div');	
					select_info.id = 'select_info_' + selects[i].name;
					select_info.className='tag_select';
					select_info.style.cursor='pointer';
				select_tag.appendChild(select_info);
				select_ul = document.createElement('ul');	
					select_ul.id = 'options_' + selects[i].name;
					select_ul.className = 'tag_options';
					select_ul.style.position='absolute';
					select_ul.style.display='none';
					select_ul.style.zIndex='999';
				select_tag.appendChild(select_ul);
				rOptions(i,selects[i].name);
				mouseSelects(selects[i].name);
				if (isIE){
					selects[i].onclick = new Function("clickLabels3('"+selects[i].name+"');window.event.cancelBubble = true;");
				}
				else if(!isIE){
					selects[i].onclick = new Function("clickLabels3('"+selects[i].name+"')");
					selects[i].addEventListener("click", stopBubbling, false);
				}		
			}
		}
		
		function rOptions(i, name) {
		
			var options = selects[i].getElementsByTagName('option');
			var options_ul = 'options_' + name;
			for (n=0;n<selects[i].options.length;n++){	
				option_li = document.createElement('li');
					option_li.style.cursor='pointer';
					option_li.className='open';
				getObj(options_ul).appendChild(option_li);
		
				option_text = document.createTextNode(selects[i].options[n].text);
				option_li.appendChild(option_text);
		
				option_selected = selects[i].options[n].selected;
		
				if(option_selected){
					option_li.className='open_selected';
					option_li.id='selected_' + name;
					getObj('select_info_' + name).appendChild(document.createTextNode(option_li.innerHTML));
				}
				
				option_li.onmouseover = function(){	this.className='open_hover';}
				option_li.onmouseout = function(){
					if(this.id=='selected_' + name){
						this.className='open_selected';
					}
					else {
						this.className='open';
					}
				} 
			
				option_li.onclick = new Function("clickOptions("+i+","+n+",'"+selects[i].name+"')");
			}
		}
		
		function mouseSelects(name){
		
			var sincn = 'select_info_' + name;
		
			getObj(sincn).onmouseover = function(){ if(this.className=='tag_select') this.className='tag_select_hover'; }
			getObj(sincn).onmouseout = function(){ if(this.className=='tag_select_hover') this.className='tag_select'; }
		
			if (isIE){
				getObj(sincn).onclick = new Function("clickSelects('"+name+"');window.event.cancelBubble = true;");
			}
			else if(!isIE){
				getObj(sincn).onclick = new Function("clickSelects('"+name+"');");
				getObj('select_info_' +name).addEventListener("click", stopBubbling, false);
			}
		
		}
		
		function clickSelects(name){
			var sincn = 'select_info_' + name;
			var sinul = 'options_' + name;	
		
			for (i=0;i<selects.length;i++){	
				if(selects[i].name == name){				
					if( getObj(sincn).className =='tag_select_hover'){
						getObj(sincn).className ='tag_select_open';
						getObj(sinul).style.display = '';
					}
					else if( getObj(sincn).className =='tag_select_open'){
						getObj(sincn).className = 'tag_select_hover';
						getObj(sinul).style.display = 'none';
					}
				}
				else{
					getObj('select_info_' + selects[i].name).className = 'tag_select';
					getObj('options_' + selects[i].name).style.display = 'none';
				}
			}
		
		}
		
		function clickOptions(i, n, name){		
			var li = getObj('options_' + name).getElementsByTagName('li');
			getObj('selected_' + name).className='open';
			getObj('selected_' + name).id='';
			li[n].id='selected_' + name;
			li[n].className='open_hover';
			getObj('select_' + name).removeChild(getObj('select_info_' + name));
			select_info = document.createElement('div');
				select_info.id = 'select_info_' + name;
				select_info.className='tag_select';
				select_info.style.cursor='pointer';
			getObj('options_' + name).parentNode.insertBefore(select_info,getObj('options_' + name));
			mouseSelects(name);
			getObj('select_info_' + name).appendChild(document.createTextNode(li[n].innerHTML));
			getObj( 'options_' + name ).style.display = 'none' ;
			getObj( 'select_info_' + name ).className = 'tag_select';
			selects[i].options[n].selected = 'selected';
		   // getObj("showMonth").innerHTML=getObj(selects[i].options[n].value).innerHTML; 取得第一个符合匹配元素的html内容，返回的是string
		    //tb_init('a.thickbox, area.thickbox, input.thickbox');
		    //取得并设置每一个匹配元素的html内容，返回的是对象
		    $("#showMonth").html($("#"+selects[i].options[n].value).html());
		    tb_init('a.thickbox');
		    }
			$("#showMonth").html($("#2011-01").html());
		
			window.onload = function(e) {
		
			bodyclick = document.getElementsByTagName('body').item(0);
			rSelects();
			bodyclick.onclick = function(){
				for (i=0;i<selects.length;i++){	
					getObj('select_info_' + selects[i].name).className = 'tag_select';
					getObj('options_' + selects[i].name).style.display = 'none';
				}
			}
		}
			
			
		</script>
</html>