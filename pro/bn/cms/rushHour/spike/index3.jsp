<%@ page language="java" contentType="text/html; charset=GBK"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3c.org/TR/1999/REC-html401-19991224/loose.dtd">
<html xml:lang="en" xmlns="http://www.w3.org/1999/xhtml">
	<head>
	<meta http-equiv="Content-Type" content="text/html; charset=gbk" />
	<meta name="keywords" content="�������ٱ��չɷ����޹�˾,����,��������,�������ٱ���,��������" />
	<meta name="verify-v1" content="ku5eYujDwJor922aXLh2tNUokeTi14dftgw2Igobok0=" />
	<meta http-equiv="x-ua-compatible" content="ie=7" />
	<title>�������ٱ��չɷ����޹�˾</title>
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
								<li class="DnavLi"><a  href="<%=request.getContextPath()%>/bn/cms/rushHour/spike/index.jsp"  onfocus="this.blur()">���չʾ</a></li>
								<li class="DnavLi"><a  href="<%=request.getContextPath()%>/bn/cms/rushHour/spike/index2.jsp" onfocus="this.blur()">�Ŷ���Ŀ</a></li>
								<li class="Dnavhold"><a  href="<%=request.getContextPath()%>/bn/cms/rushHour/spike/index3.jsp"  onfocus="this.blur()">������Ŀ</a></li>
							</ul>
						</div>
						<div class="Dcontent">
							<%
								String LSUserCode = (String)session.getAttribute("LSUserCode");
								if(!com.sinosoft.common.Data.hasValue(LSUserCode)){ 
							%>
							<div class="input_area" style="margin-top:20px;">
								<div style="color:#666666;">��ʹ�����Ĵ������ʺŵ�¼��鿴</div>
								<div style="color:#666666;">
									�û�����<input type="text" style="width:150px;margin-top:5px;" name="UserID" value=""/><br/>
									��&nbsp;&nbsp;�룺<input type="password" name="Password" style="width:150px;margin-top:5px;" value=""/><br/>
									<span style="margin-top:1px;"><img alt="���������֤��" name="Rand" style="cursor:pointer;" id="mFrame" name="mFrame" src="<%=request.getContextPath()%>/passport/registration/image.jsp" onclick="changeImage();"></span>��֤�룺<input type="text" name="Rand" style="width:75px;margin-top:4px;" value=""/><br style="clear:both"/>
								</div>
								<div class="submit_area">
									<a href="#" onclick="logon();"></a>						
								</div>
							</div>
							<%}else{ %>
								<div class="topic">
									<P class="topicone">���ꡰ���ʱ�̡�ר��</P>
									<P class="topictwo">���ꡰ���ʱ�̡�������¼Ϊ�������ٸ�������ϵ��������Ŀ������ּ��Ϊ������Խ�Ļ��������ƴ����ȡ����Ӫ�첻��ˢ�¼�¼����ս�¸ߵ�ҵ���Χ��</P>
								</div>
								<div id="showMonth"></div>
								 <div id="2011-01" class="shownone">
									 <div class="notetitle2"></div>
									 <div class="note3">
									 	<div class="notedetail">
									 		<div class="notepic">
									 			<p><a href="images/40_1.jpg" title="��־��" class="thickbox"><img src="images/40.jpg" style="display:block;width:165px;height:105px;"/></a></p>
									 			<p style="margin-top:20px;color:white;font-weight:bolder;font-size:13px;">���±�׼����</p>
									 		</div>
									 		<div class="noteline"></div>
									 		<div class="notetext">
									 			<p class="notetextone">���¼�¼��<font style="color:#cc0000;">349746.25Ԫ</font></p>
									 			<p class="notetexttwo">ˢ�¼�¼ʱ�䣺2011��01��</p>
									 			<p class="notetextone">ˢ�¼�¼�ߣ���־��</p>
									 			<p class="notetexttwo">���������� �����ֹ�˾</p>
									 		</div>
									 	</div>
									 	<div class="Middleline2"></div>
									 	<div class="notedetail">
									 		<div class="notepic">
									 			<p><a href="images/36.jpg" title="��Ӱ" class="thickbox"><img src="images/36_p.jpg" style="display:block;width:165px;height:105px;"/></a></p>
									 			<p style="margin-top:20px;color:white;font-weight:bolder;font-size:13px;">���¼���</p>
									 		</div>
									 		<div class="noteline"></div>
									 		<div class="notetext">
									 			<p class="notetextone">��ս��¼��<font style="color:#cc0000;">20��</font></p>
									 			<p class="notetexttwo">ˢ�¼�¼ʱ�䣺2010��10��</p>
									 			<p class="notetextone">ˢ�¼�¼�ߣ���Ӱ</p>
									 			<p class="notetexttwo">���������� �����ֹ�˾</p>
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
									 			<p><a href="images/31.jpg" title="��ΰ��" class="thickbox"><img src="images/31_1.jpg" style="display:block;width:165px;height:105px;"/></a></p>
									 			<p style="margin-top:20px;color:white;font-weight:bolder;font-size:13px;">���±�׼����</p>
									 		</div>
									 		<div class="noteline"></div>
									 		<div class="notetext">
									 			<p class="notetextone">���¼�¼��<font style="color:#cc0000;">190393.5Ԫ</font></p>
									 			<p class="notetexttwo">ˢ�¼�¼ʱ�䣺2010��6��</p>
									 			<p class="notetextone">ˢ�¼�¼�ߣ���ΰ��</p>
									 			<p class="notetexttwo">���������� �ܹ�˾Ӫҵ��</p>
									 		</div>
									 	</div>
									 	<div class="Middleline2"></div>
									 	<div class="notedetail">
									 		<div class="notepic">
									 			<p><a href="images/36.jpg" title="�" class="thickbox"><img src="images/36_p.jpg" style="display:block;width:165px;height:105px;"/></a></p>
									 			<p style="margin-top:20px;color:white;font-weight:bolder;font-size:13px;">���¼���</p>
									 		</div>
									 		<div class="noteline"></div>
									 		<div class="notetext">
									 			<p class="notetextone">��ս��¼��<font style="color:#cc0000;">20��</font></p>
									 			<p class="notetexttwo">ˢ�¼�¼ʱ�䣺2010��10��</p>
									 			<p class="notetextone">ˢ�¼�¼�ߣ���Ӱ</p>
									 			<p class="notetexttwo">���������� �����ֹ�˾</p>
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
									 			<p><a href="images/31.jpg" title="��ΰ��" class="thickbox"><img src="images/31_1.jpg" style="display:block;width:165px;height:105px;"/></a></p>
									 			<p style="margin-top:20px;color:white;font-weight:bolder;font-size:13px;">���±�׼����</p>
									 		</div>
									 		<div class="noteline"></div>
									 		<div class="notetext">
									 			<p class="notetextone">���¼�¼��<font style="color:#cc0000;">190393.5Ԫ</font></p>
									 			<p class="notetexttwo">ˢ�¼�¼ʱ�䣺2010��6��</p>
									 			<p class="notetextone">ˢ�¼�¼�ߣ���ΰ��</p>
									 			<p class="notetexttwo">���������� �ܹ�˾Ӫҵ��</p>
									 		</div>
									 	</div>
									 	<div class="Middleline2"></div>
									 	<div class="notedetail">
									 		<div class="notepic">
									 			<p><a href="images/33.jpg" title="�" class="thickbox"><img src="images/33_1.jpg" style="display:block;width:165px;height:105px;"/></a></p>
									 			<p style="margin-top:20px;color:white;font-weight:bolder;font-size:13px;">���¼���</p>
									 		</div>
									 		<div class="noteline"></div>
									 		<div class="notetext">
									 			<p class="notetextone">��ս��¼��<font style="color:#cc0000;">12��</font></p>
									 			<p class="notetexttwo">ˢ�¼�¼ʱ�䣺2010��6��</p>
									 			<p class="notetextone">ˢ�¼�¼�ߣ��</p>
									 			<p class="notetexttwo">���������� �����ֹ�˾</p>
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
									 			<p><a href="images/31.jpg" title="��ΰ��" class="thickbox"><img src="images/31_1.jpg" style="display:block;width:165px;height:105px;"/></a></p>
									 			<p style="margin-top:20px;color:white;font-weight:bolder;font-size:13px;">���±�׼����</p>
									 		</div>
									 		<div class="noteline"></div>
									 		<div class="notetext">
									 			<p class="notetextone">���¼�¼��<font style="color:#cc0000;">190393.5Ԫ</font></p>
									 			<p class="notetexttwo">ˢ�¼�¼ʱ�䣺2010��6��</p>
									 			<p class="notetextone">ˢ�¼�¼�ߣ���ΰ��</p>
									 			<p class="notetexttwo">���������� �ܹ�˾Ӫҵ��</p>
									 		</div>
									 	</div>
									 	<div class="Middleline2"></div>
									 	<div class="notedetail">
									 		<div class="notepic">
									 			<p><a href="images/33.jpg" title="�" class="thickbox"><img src="images/33_1.jpg" style="display:block;width:165px;height:105px;"/></a></p>
									 			<p style="margin-top:20px;color:white;font-weight:bolder;font-size:13px;">���¼���</p>
									 		</div>
									 		<div class="noteline"></div>
									 		<div class="notetext">
									 			<p class="notetextone">��ս��¼��<font style="color:#cc0000;">12��</font></p>
									 			<p class="notetexttwo">ˢ�¼�¼ʱ�䣺2010��6��</p>
									 			<p class="notetextone">ˢ�¼�¼�ߣ��</p>
									 			<p class="notetexttwo">���������� �����ֹ�˾</p>
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
									 			<p><a href="images/31.jpg" title="��ΰ��" class="thickbox"><img src="images/31_1.jpg" style="display:block;width:165px;height:105px;"/></a></p>
									 			<p style="margin-top:20px;color:white;font-weight:bolder;font-size:13px;">���±�׼����</p>
									 		</div>
									 		<div class="noteline"></div>
									 		<div class="notetext">
									 			<p class="notetextone">���¼�¼��<font style="color:#cc0000;">190393.5Ԫ</font></p>
									 			<p class="notetexttwo">ˢ�¼�¼ʱ�䣺2010��6��</p>
									 			<p class="notetextone">ˢ�¼�¼�ߣ���ΰ��</p>
									 			<p class="notetexttwo">���������� �ܹ�˾Ӫҵ��</p>
									 		</div>
									 	</div>
									 	<div class="Middleline2"></div>
									 	<div class="notedetail">
									 		<div class="notepic">
									 			<p><a href="images/33.jpg" title="�" class="thickbox"><img src="images/33_1.jpg" style="display:block;width:165px;height:105px;"/></a></p>
									 			<p style="margin-top:20px;color:white;font-weight:bolder;font-size:13px;">���¼���</p>
									 		</div>
									 		<div class="noteline"></div>
									 		<div class="notetext">
									 			<p class="notetextone">��ս��¼��<font style="color:#cc0000;">12��</font></p>
									 			<p class="notetexttwo">ˢ�¼�¼ʱ�䣺2010��6��</p>
									 			<p class="notetextone">ˢ�¼�¼�ߣ��</p>
									 			<p class="notetexttwo">���������� �����ֹ�˾</p>
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
									 			<p><a href="images/31.jpg" title="��ΰ��" class="thickbox"><img src="images/31_1.jpg" style="display:block;width:165px;height:105px;"/></a></p>
									 			<p style="margin-top:20px;color:white;font-weight:bolder;font-size:13px;">���±�׼����</p>
									 		</div>
									 		<div class="noteline"></div>
									 		<div class="notetext">
									 			<p class="notetextone">���¼�¼��<font style="color:#cc0000;">182795Ԫ</font></p>
									 			<p class="notetexttwo">ˢ�¼�¼ʱ�䣺2010��5��</p>
									 			<p class="notetextone">ˢ�¼�¼�ߣ���ΰ��</p>
									 			<p class="notetexttwo">���������� �ܹ�˾Ӫҵ��</p>
									 		</div>
									 	</div>
									 	<div class="Middleline2"></div>
									 	<div class="notedetail">
									 		<div class="notepic">
									 			<p><a href="images/23_p.jpg" title="�����" class="thickbox"><img src="images/23.jpg" style="display:block;width:165px;height:105px;"/></a></p>
									 			<p style="margin-top:20px;color:white;font-weight:bolder;font-size:13px;">���¼���</p>
									 		</div>
									 		<div class="noteline"></div>
									 		<div class="notetext">
									 			<p class="notetextone">��ս��¼��<font style="color:#cc0000;">10��</font></p>
									 			<p class="notetexttwo">ˢ�¼�¼ʱ�䣺2010��1��</p>
									 			<p class="notetextone">ˢ�¼�¼�ߣ������</p>
									 			<p class="notetexttwo">���������� �ܹ�˾Ӫҵ��</p>
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
									 			<p><a href="images/22_p.jpg" title="���" class="thickbox"><img src="images/22.jpg" style="display:block;width:165px;height:105px;"/></a></p>
									 			<p style="margin-top:20px;color:white;font-weight:bolder;font-size:13px;">���±�׼����</p>
									 		</div>
									 		<div class="noteline"></div>
									 		<div class="notetext">
									 			<p class="notetextone">���¼�¼��<font style="color:#cc0000;">132601.30Ԫ</font></p>
									 			<p class="notetexttwo">ˢ�¼�¼ʱ�䣺2010��1��</p>
									 			<p class="notetextone">ˢ�¼�¼�ߣ����</p>
									 			<p class="notetexttwo">���������� �ܹ�˾Ӫҵ��</p>
									 		</div>
									 	</div>
									 	<div class="Middleline2"></div>
									 	<div class="notedetail">
									 		<div class="notepic">
									 			<p><a href="images/23_p.jpg" title="�����" class="thickbox"><img src="images/23.jpg" style="display:block;width:165px;height:105px;"/></a></p>
									 			<p style="margin-top:20px;color:white;font-weight:bolder;font-size:13px;">���¼���</p>
									 		</div>
									 		<div class="noteline"></div>
									 		<div class="notetext">
									 			<p class="notetextone">��ս��¼��<font style="color:#cc0000;">10��</font></p>
									 			<p class="notetexttwo">ˢ�¼�¼ʱ�䣺2010��1��</p>
									 			<p class="notetextone">ˢ�¼�¼�ߣ������</p>
									 			<p class="notetexttwo">���������� �ܹ�˾Ӫҵ��</p>
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
									 			<p><a href="images/22_p.jpg" title="���" class="thickbox"><img src="images/22.jpg" style="display:block;width:165px;height:105px;"/></a></p>
									 			<p style="margin-top:20px;color:white;font-weight:bolder;font-size:13px;">���±�׼����</p>
									 		</div>
									 		<div class="noteline"></div>
									 		<div class="notetext">
									 			<p class="notetextone">���¼�¼��<font style="color:#cc0000;">132601.30Ԫ</font></p>
									 			<p class="notetexttwo">ˢ�¼�¼ʱ�䣺2010��1��</p>
									 			<p class="notetextone">ˢ�¼�¼�ߣ����</p>
									 			<p class="notetexttwo">���������� �ܹ�˾Ӫҵ��</p>
									 		</div>
									 	</div>
									 	<div class="Middleline2"></div>
									 	<div class="notedetail">
									 		<div class="notepic">
									 			<p><a href="images/23_p.jpg" title="�����" class="thickbox"><img src="images/23.jpg" style="display:block;width:165px;height:105px;"/></a></p>
									 			<p style="margin-top:20px;color:white;font-weight:bolder;font-size:13px;">���¼���</p>
									 		</div>
									 		<div class="noteline"></div>
									 		<div class="notetext">
									 			<p class="notetextone">��ս��¼��<font style="color:#cc0000;">10��</font></p>
									 			<p class="notetexttwo">ˢ�¼�¼ʱ�䣺2010��1��</p>
									 			<p class="notetextone">ˢ�¼�¼�ߣ������</p>
									 			<p class="notetexttwo">���������� �ܹ�˾Ӫҵ��</p>
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
									 			<p><a href="images/22_p.jpg" title="���" class="thickbox"><img src="images/22.jpg" style="display:block;width:165px;height:105px;"/></a></p>
									 			<p style="margin-top:20px;color:white;font-weight:bolder;font-size:13px;">���±�׼����</p>
									 		</div>
									 		<div class="noteline"></div>
									 		<div class="notetext">
									 			<p class="notetextone">���¼�¼��<font style="color:#cc0000;">132601.30Ԫ</font></p>
									 			<p class="notetexttwo">ˢ�¼�¼ʱ�䣺2010��1��</p>
									 			<p class="notetextone">ˢ�¼�¼�ߣ����</p>
									 			<p class="notetexttwo">���������� �ܹ�˾Ӫҵ��</p>
									 		</div>
									 	</div>
									 	<div class="Middleline2"></div>
									 	<div class="notedetail">
									 		<div class="notepic">
									 			<p><a href="images/23_p.jpg" title="�����" class="thickbox"><img src="images/23.jpg" style="display:block;width:165px;height:105px;"/></a></p>
									 			<p style="margin-top:20px;color:white;font-weight:bolder;font-size:13px;">���¼���</p>
									 		</div>
									 		<div class="noteline"></div>
									 		<div class="notetext">
									 			<p class="notetextone">��ս��¼��<font style="color:#cc0000;">10��</font></p>
									 			<p class="notetexttwo">ˢ�¼�¼ʱ�䣺2010��1��</p>
									 			<p class="notetextone">ˢ�¼�¼�ߣ������</p>
									 			<p class="notetexttwo">���������� �ܹ�˾Ӫҵ��</p>
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
									 			<p><a href="images/22_p.jpg" title="���" class="thickbox"><img src="images/22.jpg" style="display:block;width:165px;height:105px;"/></a></p>
									 			<p style="margin-top:20px;color:white;font-weight:bolder;font-size:13px;">���±�׼����</p>
									 		</div>
									 		<div class="noteline"></div>
									 		<div class="notetext">
									 			<p class="notetextone">���¼�¼��<font style="color:#cc0000;">132601.30Ԫ</font></p>
									 			<p class="notetexttwo">ˢ�¼�¼ʱ�䣺2010��1��</p>
									 			<p class="notetextone">ˢ�¼�¼�ߣ����</p>
									 			<p class="notetexttwo">���������� �ܹ�˾Ӫҵ��</p>
									 		</div>
									 	</div>
									 	<div class="Middleline2"></div>
									 	<div class="notedetail">
									 		<div class="notepic">
									 			<p><a href="images/23_p.jpg" title="�����" class="thickbox"><img src="images/23.jpg" style="display:block;width:165px;height:105px;"/></a></p>
									 			<p style="margin-top:20px;color:white;font-weight:bolder;font-size:13px;">���¼���</p>
									 		</div>
									 		<div class="noteline"></div>
									 		<div class="notetext">
									 			<p class="notetextone">��ս��¼��<font style="color:#cc0000;">10��</font></p>
									 			<p class="notetexttwo">ˢ�¼�¼ʱ�䣺2010��1��</p>
									 			<p class="notetextone">ˢ�¼�¼�ߣ������</p>
									 			<p class="notetexttwo">���������� �ܹ�˾Ӫҵ��</p>
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
									 			<p><a href="images/18_p.jpg" title="����" class="thickbox"><img src="images/18.jpg" style="display:block;width:165px;height:105px;"/></a></p>
									 			<p style="margin-top:20px;color:white;font-weight:bolder;font-size:13px;">���±�׼����</p>
									 		</div>
									 		<div class="noteline"></div>
									 		<div class="notetext">
									 			<p class="notetextone">���¼�¼��<font style="color:#cc0000;">53219.75Ԫ</font></p>
									 			<p class="notetexttwo">ˢ�¼�¼ʱ�䣺2009��11��</p>
									 			<p class="notetextone">ˢ�¼�¼�ߣ�����</p>
									 			<p class="notetexttwo">���������� ����</p>
									 		</div>
									 	</div>
									 	<div class="Middleline2"></div>
									 	<div class="notedetail">
									 		<div class="notepic">
									 			<p><a href="images/2_p.jpg" title="������" class="thickbox"><img src="images/2.jpg" style="display:block;width:165px;height:105px;"/></a></p>
									 			<p style="margin-top:20px;color:white;font-weight:bolder;font-size:13px;">���¼���</p>
									 		</div>
									 		<div class="noteline"></div>
									 		<div class="notetext">
									 			<p class="notetextone">��ս��¼��<font style="color:#cc0000;">9��</font></p>
									 			<p class="notetexttwo">ˢ�¼�¼ʱ�䣺2009��7��</p>
									 			<p class="notetextone">ˢ�¼�¼�ߣ�������</p>
									 			<p class="notetexttwo">���������� �ܹ�˾Ӫҵ��</p>
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
									 			<p><a href="images/18_p.jpg" title="����" class="thickbox"><img src="images/18.jpg" style="display:block;width:165px;height:105px;"/></a></p>
									 			<p style="margin-top:20px;color:white;font-weight:bolder;font-size:13px;">���±�׼����</p>
									 		</div>
									 		<div class="noteline"></div>
									 		<div class="notetext">
									 			<p class="notetextone">���¼�¼��<font style="color:#cc0000;">53219.75Ԫ</font></p>
									 			<p class="notetexttwo">ˢ�¼�¼ʱ�䣺2009��11��</p>
									 			<p class="notetextone">ˢ�¼�¼�ߣ�����</p>
									 			<p class="notetexttwo">���������� ����</p>
									 		</div>
									 	</div>
									 	<div class="Middleline2"></div>
									 	<div class="notedetail">
									 		<div class="notepic">
									 			<p><a href="images/2_p.jpg" title="������" class="thickbox"><img src="images/2.jpg" style="display:block;width:165px;height:105px;"/></a></p>
									 			<p style="margin-top:20px;color:white;font-weight:bolder;font-size:13px;">���¼���</p>
									 		</div>
									 		<div class="noteline"></div>
									 		<div class="notetext">
									 			<p class="notetextone">��ս��¼��<font style="color:#cc0000;">9��</font></p>
									 			<p class="notetexttwo">ˢ�¼�¼ʱ�䣺2009��7��</p>
									 			<p class="notetextone">ˢ�¼�¼�ߣ�������</p>
									 			<p class="notetexttwo">���������� �ܹ�˾Ӫҵ��</p>
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
									 			<p><a href="images/11_p.jpg" title="����" class="thickbox"><img src="images/11.jpg" style="display:block;width:165px;height:105px;"/></a></p>
									 			<p style="margin-top:20px;color:white;font-weight:bolder;font-size:13px;">���±�׼����</p>
									 		</div>
									 		<div class="noteline"></div>
									 		<div class="notetext">
									 			<p class="notetextone">���¼�¼��<font style="color:#cc0000;">41060Ԫ</font></p>
									 			<p class="notetexttwo">ˢ�¼�¼ʱ�䣺2009��10��</p>
									 			<p class="notetextone">ˢ�¼�¼�ߣ�����</p>
									 			<p class="notetexttwo">����������  �ܹ�˾Ӫҵ��</p>
									 		</div>
									 	</div>
									 	<div class="Middleline2"></div>
									 	<div class="notedetail">
									 		<div class="notepic">
									 			<p><a href="images/2_p.jpg" title="������" class="thickbox"><img src="images/2.jpg" style="display:block;width:165px;height:105px;"/></a></p>
									 			<p style="margin-top:20px;color:white;font-weight:bolder;font-size:13px;">���¼���</p>
									 		</div>
									 		<div class="noteline"></div>
									 		<div class="notetext">
									 			<p class="notetextone">��ս��¼��<font style="color:#cc0000;">9��</font></p>
									 			<p class="notetexttwo">ˢ�¼�¼ʱ�䣺2009��7��</p>
									 			<p class="notetextone">ˢ�¼�¼�ߣ�������</p>
									 			<p class="notetexttwo">���������� �ܹ�˾Ӫҵ��</p>
									 		</div>
									 	</div>
									</div>
									<div class="notetitle2"></div>
								</div>
								<div  id="uboxstyle" >
									<span style="display:block;color:#666666;font-weight:bolder;font-size:13px;float:left;width:80px;height:24px;padding-top:8px;text-align:left;">�鿴��¼</span>
									<select name="language" id="language"  >
										<option value="2011-01" selected="selected" >2011��01��</option>
										<option value="2010-10">2010��10��</option>
										<option value="2010-08">2010��8��</option>
										<option value="2010-07">2010��7��</option>
										<option value="2010-06">2010��6��</option>
										<option value="2010-05">2010��5��</option>
										<option value="2010-04">2010��4��</option>
										<option value="2010-03">2010��3��</option>
										<option value="2010-02">2010��2��</option>
										<option value="2010-01">2010��1��</option>
										<option value="2009-12">2009��12��</option>
										<option value="2009-11">2009��11��</option>
										<option value="2009-10">2009��10��</option>
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