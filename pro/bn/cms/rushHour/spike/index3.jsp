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
	<body onkeydown="onkey()">
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
								<li class="DnavLi"><a  href="<%=request.getContextPath()%>/bn/cms/rushHour/spike/index2.jsp" onfocus="this.blur()">团队项目</a></li>
								<li class="Dnavhold"><a  href="<%=request.getContextPath()%>/bn/cms/rushHour/spike/index3.jsp"  onfocus="this.blur()">个人项目</a></li>
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
								 <div id="2011-01" class="shownone">
									 <div class="notetitle2"></div>
									 <div class="note3">
									 	<div class="notedetail">
									 		<div class="notepic">
									 			<p><a href="images/40_1.jpg" title="高志刚" class="thickbox"><img src="images/40.jpg" style="display:block;width:165px;height:105px;"/></a></p>
									 			<p style="margin-top:20px;color:white;font-weight:bolder;font-size:13px;">单月标准保费</p>
									 		</div>
									 		<div class="noteline"></div>
									 		<div class="notetext">
									 			<p class="notetextone">最新记录：<font style="color:#cc0000;">349746.25元</font></p>
									 			<p class="notetexttwo">刷新记录时间：2011年01月</p>
									 			<p class="notetextone">刷新纪录者：高志刚</p>
									 			<p class="notetexttwo">所属机构： 辽宁分公司</p>
									 		</div>
									 	</div>
									 	<div class="Middleline2"></div>
									 	<div class="notedetail">
									 		<div class="notepic">
									 			<p><a href="images/36.jpg" title="田影" class="thickbox"><img src="images/36_p.jpg" style="display:block;width:165px;height:105px;"/></a></p>
									 			<p style="margin-top:20px;color:white;font-weight:bolder;font-size:13px;">单月件数</p>
									 		</div>
									 		<div class="noteline"></div>
									 		<div class="notetext">
									 			<p class="notetextone">挑战记录：<font style="color:#cc0000;">20件</font></p>
									 			<p class="notetexttwo">刷新记录时间：2010年10月</p>
									 			<p class="notetextone">刷新纪录者：田影</p>
									 			<p class="notetexttwo">所属机构： 辽宁分公司</p>
									 		</div>
									 	</div>
									</div>
									<div class="notetitle2"></div>
								</div>
								<div id="2010-10" class="shownone">
									<div class="notetitle2"></div>
									 <div class="note3">
									 	<div class="notedetail">
									 		<div class="notepic">
									 			<p><a href="images/31.jpg" title="郭伟冬" class="thickbox"><img src="images/31_1.jpg" style="display:block;width:165px;height:105px;"/></a></p>
									 			<p style="margin-top:20px;color:white;font-weight:bolder;font-size:13px;">单月标准保费</p>
									 		</div>
									 		<div class="noteline"></div>
									 		<div class="notetext">
									 			<p class="notetextone">最新记录：<font style="color:#cc0000;">190393.5元</font></p>
									 			<p class="notetexttwo">刷新记录时间：2010年6月</p>
									 			<p class="notetextone">刷新纪录者：郭伟冬</p>
									 			<p class="notetexttwo">所属机构： 总公司营业部</p>
									 		</div>
									 	</div>
									 	<div class="Middleline2"></div>
									 	<div class="notedetail">
									 		<div class="notepic">
									 			<p><a href="images/36.jpg" title="杨寒" class="thickbox"><img src="images/36_p.jpg" style="display:block;width:165px;height:105px;"/></a></p>
									 			<p style="margin-top:20px;color:white;font-weight:bolder;font-size:13px;">单月件数</p>
									 		</div>
									 		<div class="noteline"></div>
									 		<div class="notetext">
									 			<p class="notetextone">挑战记录：<font style="color:#cc0000;">20件</font></p>
									 			<p class="notetexttwo">刷新记录时间：2010年10月</p>
									 			<p class="notetextone">刷新纪录者：田影</p>
									 			<p class="notetexttwo">所属机构： 辽宁分公司</p>
									 		</div>
									 	</div>
									</div>
									<div class="notetitle2"></div>
								</div>
								<div id="2010-08" class="shownone">
									<div class="notetitle2"></div>
									 <div class="note3">
									 	<div class="notedetail">
									 		<div class="notepic">
									 			<p><a href="images/31.jpg" title="郭伟冬" class="thickbox"><img src="images/31_1.jpg" style="display:block;width:165px;height:105px;"/></a></p>
									 			<p style="margin-top:20px;color:white;font-weight:bolder;font-size:13px;">单月标准保费</p>
									 		</div>
									 		<div class="noteline"></div>
									 		<div class="notetext">
									 			<p class="notetextone">最新记录：<font style="color:#cc0000;">190393.5元</font></p>
									 			<p class="notetexttwo">刷新记录时间：2010年6月</p>
									 			<p class="notetextone">刷新纪录者：郭伟冬</p>
									 			<p class="notetexttwo">所属机构： 总公司营业部</p>
									 		</div>
									 	</div>
									 	<div class="Middleline2"></div>
									 	<div class="notedetail">
									 		<div class="notepic">
									 			<p><a href="images/33.jpg" title="杨寒" class="thickbox"><img src="images/33_1.jpg" style="display:block;width:165px;height:105px;"/></a></p>
									 			<p style="margin-top:20px;color:white;font-weight:bolder;font-size:13px;">单月件数</p>
									 		</div>
									 		<div class="noteline"></div>
									 		<div class="notetext">
									 			<p class="notetextone">挑战记录：<font style="color:#cc0000;">12件</font></p>
									 			<p class="notetexttwo">刷新记录时间：2010年6月</p>
									 			<p class="notetextone">刷新纪录者：杨寒</p>
									 			<p class="notetexttwo">所属机构： 湖北分公司</p>
									 		</div>
									 	</div>
									</div>
									<div class="notetitle2"></div>
								</div>
								<div id="2010-07" class="shownone">
									<div class="notetitle2"></div>
									 <div class="note3">
									 	<div class="notedetail">
									 		<div class="notepic">
									 			<p><a href="images/31.jpg" title="郭伟冬" class="thickbox"><img src="images/31_1.jpg" style="display:block;width:165px;height:105px;"/></a></p>
									 			<p style="margin-top:20px;color:white;font-weight:bolder;font-size:13px;">单月标准保费</p>
									 		</div>
									 		<div class="noteline"></div>
									 		<div class="notetext">
									 			<p class="notetextone">最新记录：<font style="color:#cc0000;">190393.5元</font></p>
									 			<p class="notetexttwo">刷新记录时间：2010年6月</p>
									 			<p class="notetextone">刷新纪录者：郭伟冬</p>
									 			<p class="notetexttwo">所属机构： 总公司营业部</p>
									 		</div>
									 	</div>
									 	<div class="Middleline2"></div>
									 	<div class="notedetail">
									 		<div class="notepic">
									 			<p><a href="images/33.jpg" title="杨寒" class="thickbox"><img src="images/33_1.jpg" style="display:block;width:165px;height:105px;"/></a></p>
									 			<p style="margin-top:20px;color:white;font-weight:bolder;font-size:13px;">单月件数</p>
									 		</div>
									 		<div class="noteline"></div>
									 		<div class="notetext">
									 			<p class="notetextone">挑战记录：<font style="color:#cc0000;">12件</font></p>
									 			<p class="notetexttwo">刷新记录时间：2010年6月</p>
									 			<p class="notetextone">刷新纪录者：杨寒</p>
									 			<p class="notetexttwo">所属机构： 湖北分公司</p>
									 		</div>
									 	</div>
									</div>
									<div class="notetitle2"></div>
								</div>
								<div id="2010-06" class="shownone">
									<div class="notetitle2"></div>
									 <div class="note3">
									 	<div class="notedetail">
									 		<div class="notepic">
									 			<p><a href="images/31.jpg" title="郭伟冬" class="thickbox"><img src="images/31_1.jpg" style="display:block;width:165px;height:105px;"/></a></p>
									 			<p style="margin-top:20px;color:white;font-weight:bolder;font-size:13px;">单月标准保费</p>
									 		</div>
									 		<div class="noteline"></div>
									 		<div class="notetext">
									 			<p class="notetextone">最新记录：<font style="color:#cc0000;">190393.5元</font></p>
									 			<p class="notetexttwo">刷新记录时间：2010年6月</p>
									 			<p class="notetextone">刷新纪录者：郭伟冬</p>
									 			<p class="notetexttwo">所属机构： 总公司营业部</p>
									 		</div>
									 	</div>
									 	<div class="Middleline2"></div>
									 	<div class="notedetail">
									 		<div class="notepic">
									 			<p><a href="images/33.jpg" title="杨寒" class="thickbox"><img src="images/33_1.jpg" style="display:block;width:165px;height:105px;"/></a></p>
									 			<p style="margin-top:20px;color:white;font-weight:bolder;font-size:13px;">单月件数</p>
									 		</div>
									 		<div class="noteline"></div>
									 		<div class="notetext">
									 			<p class="notetextone">挑战记录：<font style="color:#cc0000;">12件</font></p>
									 			<p class="notetexttwo">刷新记录时间：2010年6月</p>
									 			<p class="notetextone">刷新纪录者：杨寒</p>
									 			<p class="notetexttwo">所属机构： 湖北分公司</p>
									 		</div>
									 	</div>
									</div>
									<div class="notetitle2"></div>
								</div>
								<div id="2010-05" class="shownone">
									<div class="notetitle2"></div>
									 <div class="note3">
									 	<div class="notedetail">
									 		<div class="notepic">
									 			<p><a href="images/31.jpg" title="郭伟冬" class="thickbox"><img src="images/31_1.jpg" style="display:block;width:165px;height:105px;"/></a></p>
									 			<p style="margin-top:20px;color:white;font-weight:bolder;font-size:13px;">单月标准保费</p>
									 		</div>
									 		<div class="noteline"></div>
									 		<div class="notetext">
									 			<p class="notetextone">最新记录：<font style="color:#cc0000;">182795元</font></p>
									 			<p class="notetexttwo">刷新记录时间：2010年5月</p>
									 			<p class="notetextone">刷新纪录者：郭伟冬</p>
									 			<p class="notetexttwo">所属机构： 总公司营业部</p>
									 		</div>
									 	</div>
									 	<div class="Middleline2"></div>
									 	<div class="notedetail">
									 		<div class="notepic">
									 			<p><a href="images/23_p.jpg" title="吴成祥" class="thickbox"><img src="images/23.jpg" style="display:block;width:165px;height:105px;"/></a></p>
									 			<p style="margin-top:20px;color:white;font-weight:bolder;font-size:13px;">单月件数</p>
									 		</div>
									 		<div class="noteline"></div>
									 		<div class="notetext">
									 			<p class="notetextone">挑战记录：<font style="color:#cc0000;">10件</font></p>
									 			<p class="notetexttwo">刷新记录时间：2010年1月</p>
									 			<p class="notetextone">刷新纪录者：吴成祥</p>
									 			<p class="notetexttwo">所属机构： 总公司营业部</p>
									 		</div>
									 	</div>
									</div>
									<div class="notetitle2"></div>
								</div>
								<div id="2010-04" class="shownone">
									<div class="notetitle2"></div>
									 <div class="note3">
									 	<div class="notedetail">
									 		<div class="notepic">
									 			<p><a href="images/22_p.jpg" title="杨春辉" class="thickbox"><img src="images/22.jpg" style="display:block;width:165px;height:105px;"/></a></p>
									 			<p style="margin-top:20px;color:white;font-weight:bolder;font-size:13px;">单月标准保费</p>
									 		</div>
									 		<div class="noteline"></div>
									 		<div class="notetext">
									 			<p class="notetextone">最新记录：<font style="color:#cc0000;">132601.30元</font></p>
									 			<p class="notetexttwo">刷新记录时间：2010年1月</p>
									 			<p class="notetextone">刷新纪录者：杨春辉</p>
									 			<p class="notetexttwo">所属机构： 总公司营业部</p>
									 		</div>
									 	</div>
									 	<div class="Middleline2"></div>
									 	<div class="notedetail">
									 		<div class="notepic">
									 			<p><a href="images/23_p.jpg" title="吴成祥" class="thickbox"><img src="images/23.jpg" style="display:block;width:165px;height:105px;"/></a></p>
									 			<p style="margin-top:20px;color:white;font-weight:bolder;font-size:13px;">单月件数</p>
									 		</div>
									 		<div class="noteline"></div>
									 		<div class="notetext">
									 			<p class="notetextone">挑战记录：<font style="color:#cc0000;">10件</font></p>
									 			<p class="notetexttwo">刷新记录时间：2010年1月</p>
									 			<p class="notetextone">刷新纪录者：吴成祥</p>
									 			<p class="notetexttwo">所属机构： 总公司营业部</p>
									 		</div>
									 	</div>
									</div>
									<div class="notetitle2"></div>
								</div>
								<div id="2010-03" class="shownone">
									<div class="notetitle2"></div>
									 <div class="note3">
									 	<div class="notedetail">
									 		<div class="notepic">
									 			<p><a href="images/22_p.jpg" title="杨春辉" class="thickbox"><img src="images/22.jpg" style="display:block;width:165px;height:105px;"/></a></p>
									 			<p style="margin-top:20px;color:white;font-weight:bolder;font-size:13px;">单月标准保费</p>
									 		</div>
									 		<div class="noteline"></div>
									 		<div class="notetext">
									 			<p class="notetextone">最新记录：<font style="color:#cc0000;">132601.30元</font></p>
									 			<p class="notetexttwo">刷新记录时间：2010年1月</p>
									 			<p class="notetextone">刷新纪录者：杨春辉</p>
									 			<p class="notetexttwo">所属机构： 总公司营业部</p>
									 		</div>
									 	</div>
									 	<div class="Middleline2"></div>
									 	<div class="notedetail">
									 		<div class="notepic">
									 			<p><a href="images/23_p.jpg" title="吴成祥" class="thickbox"><img src="images/23.jpg" style="display:block;width:165px;height:105px;"/></a></p>
									 			<p style="margin-top:20px;color:white;font-weight:bolder;font-size:13px;">单月件数</p>
									 		</div>
									 		<div class="noteline"></div>
									 		<div class="notetext">
									 			<p class="notetextone">挑战记录：<font style="color:#cc0000;">10件</font></p>
									 			<p class="notetexttwo">刷新记录时间：2010年1月</p>
									 			<p class="notetextone">刷新纪录者：吴成祥</p>
									 			<p class="notetexttwo">所属机构： 总公司营业部</p>
									 		</div>
									 	</div>
									</div>
									<div class="notetitle2"></div>
								</div>
								<div id="2010-02" class="shownone">
									<div class="notetitle2"></div>
									 <div class="note3">
									 	<div class="notedetail">
									 		<div class="notepic">
									 			<p><a href="images/22_p.jpg" title="杨春辉" class="thickbox"><img src="images/22.jpg" style="display:block;width:165px;height:105px;"/></a></p>
									 			<p style="margin-top:20px;color:white;font-weight:bolder;font-size:13px;">单月标准保费</p>
									 		</div>
									 		<div class="noteline"></div>
									 		<div class="notetext">
									 			<p class="notetextone">最新记录：<font style="color:#cc0000;">132601.30元</font></p>
									 			<p class="notetexttwo">刷新记录时间：2010年1月</p>
									 			<p class="notetextone">刷新纪录者：杨春辉</p>
									 			<p class="notetexttwo">所属机构： 总公司营业部</p>
									 		</div>
									 	</div>
									 	<div class="Middleline2"></div>
									 	<div class="notedetail">
									 		<div class="notepic">
									 			<p><a href="images/23_p.jpg" title="吴成祥" class="thickbox"><img src="images/23.jpg" style="display:block;width:165px;height:105px;"/></a></p>
									 			<p style="margin-top:20px;color:white;font-weight:bolder;font-size:13px;">单月件数</p>
									 		</div>
									 		<div class="noteline"></div>
									 		<div class="notetext">
									 			<p class="notetextone">挑战记录：<font style="color:#cc0000;">10件</font></p>
									 			<p class="notetexttwo">刷新记录时间：2010年1月</p>
									 			<p class="notetextone">刷新纪录者：吴成祥</p>
									 			<p class="notetexttwo">所属机构： 总公司营业部</p>
									 		</div>
									 	</div>
									</div>
									<div class="notetitle2"></div>
								</div>
								<div id="2010-01" class="shownone">
									<div class="notetitle2"></div>
									 <div class="note3">
									 	<div class="notedetail">
									 		<div class="notepic">
									 			<p><a href="images/22_p.jpg" title="杨春辉" class="thickbox"><img src="images/22.jpg" style="display:block;width:165px;height:105px;"/></a></p>
									 			<p style="margin-top:20px;color:white;font-weight:bolder;font-size:13px;">单月标准保费</p>
									 		</div>
									 		<div class="noteline"></div>
									 		<div class="notetext">
									 			<p class="notetextone">最新记录：<font style="color:#cc0000;">132601.30元</font></p>
									 			<p class="notetexttwo">刷新记录时间：2010年1月</p>
									 			<p class="notetextone">刷新纪录者：杨春辉</p>
									 			<p class="notetexttwo">所属机构： 总公司营业部</p>
									 		</div>
									 	</div>
									 	<div class="Middleline2"></div>
									 	<div class="notedetail">
									 		<div class="notepic">
									 			<p><a href="images/23_p.jpg" title="吴成祥" class="thickbox"><img src="images/23.jpg" style="display:block;width:165px;height:105px;"/></a></p>
									 			<p style="margin-top:20px;color:white;font-weight:bolder;font-size:13px;">单月件数</p>
									 		</div>
									 		<div class="noteline"></div>
									 		<div class="notetext">
									 			<p class="notetextone">挑战记录：<font style="color:#cc0000;">10件</font></p>
									 			<p class="notetexttwo">刷新记录时间：2010年1月</p>
									 			<p class="notetextone">刷新纪录者：吴成祥</p>
									 			<p class="notetexttwo">所属机构： 总公司营业部</p>
									 		</div>
									 	</div>
									</div>
									<div class="notetitle2"></div>
								</div>
								<div id="2009-12" class="shownone">
									<div class="notetitle2"></div>
									 <div class="note3">
									 	<div class="notedetail">
									 		<div class="notepic">
									 			<p><a href="images/18_p.jpg" title="李玲" class="thickbox"><img src="images/18.jpg" style="display:block;width:165px;height:105px;"/></a></p>
									 			<p style="margin-top:20px;color:white;font-weight:bolder;font-size:13px;">单月标准保费</p>
									 		</div>
									 		<div class="noteline"></div>
									 		<div class="notetext">
									 			<p class="notetextone">最新记录：<font style="color:#cc0000;">53219.75元</font></p>
									 			<p class="notetexttwo">刷新记录时间：2009年11月</p>
									 			<p class="notetextone">刷新纪录者：李玲</p>
									 			<p class="notetexttwo">所属机构： 湖北</p>
									 		</div>
									 	</div>
									 	<div class="Middleline2"></div>
									 	<div class="notedetail">
									 		<div class="notepic">
									 			<p><a href="images/2_p.jpg" title="王卫国" class="thickbox"><img src="images/2.jpg" style="display:block;width:165px;height:105px;"/></a></p>
									 			<p style="margin-top:20px;color:white;font-weight:bolder;font-size:13px;">单月件数</p>
									 		</div>
									 		<div class="noteline"></div>
									 		<div class="notetext">
									 			<p class="notetextone">挑战记录：<font style="color:#cc0000;">9件</font></p>
									 			<p class="notetexttwo">刷新记录时间：2009年7月</p>
									 			<p class="notetextone">刷新纪录者：王卫国</p>
									 			<p class="notetexttwo">所属机构： 总公司营业部</p>
									 		</div>
									 	</div>
									</div>
									<div class="notetitle2"></div>
								</div>
								<div id="2009-11" class="shownone">
									<div class="notetitle2"></div>
									 <div class="note3">
									 	<div class="notedetail">
									 		<div class="notepic">
									 			<p><a href="images/18_p.jpg" title="李玲" class="thickbox"><img src="images/18.jpg" style="display:block;width:165px;height:105px;"/></a></p>
									 			<p style="margin-top:20px;color:white;font-weight:bolder;font-size:13px;">单月标准保费</p>
									 		</div>
									 		<div class="noteline"></div>
									 		<div class="notetext">
									 			<p class="notetextone">最新记录：<font style="color:#cc0000;">53219.75元</font></p>
									 			<p class="notetexttwo">刷新记录时间：2009年11月</p>
									 			<p class="notetextone">刷新纪录者：李玲</p>
									 			<p class="notetexttwo">所属机构： 湖北</p>
									 		</div>
									 	</div>
									 	<div class="Middleline2"></div>
									 	<div class="notedetail">
									 		<div class="notepic">
									 			<p><a href="images/2_p.jpg" title="王卫国" class="thickbox"><img src="images/2.jpg" style="display:block;width:165px;height:105px;"/></a></p>
									 			<p style="margin-top:20px;color:white;font-weight:bolder;font-size:13px;">单月件数</p>
									 		</div>
									 		<div class="noteline"></div>
									 		<div class="notetext">
									 			<p class="notetextone">挑战记录：<font style="color:#cc0000;">9件</font></p>
									 			<p class="notetexttwo">刷新记录时间：2009年7月</p>
									 			<p class="notetextone">刷新纪录者：王卫国</p>
									 			<p class="notetexttwo">所属机构： 总公司营业部</p>
									 		</div>
									 	</div>
									</div>
									<div class="notetitle2"></div>
								</div>
								<div id="2009-10" class="shownone">
									<div class="notetitle2"></div>
									 <div class="note3">
									 	<div class="notedetail">
									 		<div class="notepic">
									 			<p><a href="images/11_p.jpg" title="李鹏" class="thickbox"><img src="images/11.jpg" style="display:block;width:165px;height:105px;"/></a></p>
									 			<p style="margin-top:20px;color:white;font-weight:bolder;font-size:13px;">单月标准保费</p>
									 		</div>
									 		<div class="noteline"></div>
									 		<div class="notetext">
									 			<p class="notetextone">最新记录：<font style="color:#cc0000;">41060元</font></p>
									 			<p class="notetexttwo">刷新记录时间：2009年10月</p>
									 			<p class="notetextone">刷新纪录者：李鹏</p>
									 			<p class="notetexttwo">所属机构：  总公司营业部</p>
									 		</div>
									 	</div>
									 	<div class="Middleline2"></div>
									 	<div class="notedetail">
									 		<div class="notepic">
									 			<p><a href="images/2_p.jpg" title="王卫国" class="thickbox"><img src="images/2.jpg" style="display:block;width:165px;height:105px;"/></a></p>
									 			<p style="margin-top:20px;color:white;font-weight:bolder;font-size:13px;">单月件数</p>
									 		</div>
									 		<div class="noteline"></div>
									 		<div class="notetext">
									 			<p class="notetextone">挑战记录：<font style="color:#cc0000;">9件</font></p>
									 			<p class="notetexttwo">刷新记录时间：2009年7月</p>
									 			<p class="notetextone">刷新纪录者：王卫国</p>
									 			<p class="notetexttwo">所属机构： 总公司营业部</p>
									 		</div>
									 	</div>
									</div>
									<div class="notetitle2"></div>
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
		</script>
		<script type="text/javascript">
		$("#showMonth").html($("#2011-01").html());
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
		
			 // getObj("showMonth").innerHTML=getObj(selects[i].options[n].value).innerHTML;
		    
		    $("#showMonth").html($("#"+selects[i].options[n].value).html());
		    tb_init('a.thickbox');
		}
	 	
		
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