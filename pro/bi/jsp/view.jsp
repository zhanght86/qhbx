<%@include file="init.jsp" %>
<%
//BI首页显示链接地址
String defaultHomeUrl = Context.getSyConf("bi_default_home_url", "");
//BI系统是否在维护
boolean isDebug = "true".equals(Context.getSyConf("bi_is_debug", Const.DEFAULT_IS_DEBUG));
//用户是否登录
boolean isLogin = userBean == null ? false : true;
%>

<script type="text/javascript">
function biSubmitAction(url) {
	jQuery('#mainFrame').remove();
	var clone = jQuery('#mainFrameOrg').clone(true);
	clone.attr('id', 'mainFrame');
	clone.attr('name', 'mainFrame');
	clone.attr('style', 'width:100%; height:100%;');
	clone.insertAfter(jQuery('#mainFrameOrg'));
	clone.attr('src', url);
}
</script>
	<%if(isDebug || !isLogin){ %>
		<%if(isDebug) {%>
		<table style="width:100%; height:100%; background:url(/bi/images/bg_debug.jpg) no-repeat;">
			<tr>
				<td>&nbsp;</td>
			</tr>
		</table>
		<%}else{ %>
		<table style="width:100%; height:100%;
			background:url(/bi/images/bg_nologin.jpg) no-repeat center center;">
			<tr>
				<td width="670px"></td>
				<td>
					<a href="">
						<img src="/bi/images/login.png" />
					</a>
				</td>
			</tr>
		</table>
		<%} %>
	<%}else{ %>
		<input type="hidden" id="bi_theme_wrapper_width" />
		<input type="hidden" id="bi_portlet_height" value="528" />
		
		<div id="bi_div" style="width:100%; height:100%;">
		<table style="width:100%; height:100%; border-color:#0080FF;" border="2px">
			<tr>
				<td style="width:300px; height:100%;"
					id="bi_treeViewTD" valign="top">
					<div style="height:730px;width:300px;overflow:auto;font-size:13px;">
						<%@ include file="tree.jsp" %>
						<br />
					</div>
				</td>
				<td style="width:8px; height:100%; cursor:hand" bgcolor="#C3D5EB" onclick="styleTreeView()">
					<div>
						<img src="/bi/images/divider.gif"/>
					</div>
				</td>
				<td style="height:100%;" valign="top">
					<iframe style="display:none;height:100%;" scrolling="auto" frameborder="0"
						id="mainFrameOrg" name="mainFrameOrg"
						src="">
					</iframe>
					<iframe style="width:100%; height:700px;" scrolling="auto" frameborder="0"
						id="mainFrame" name="mainFrame"
						src="<%= defaultHomeUrl %>">
					</iframe>
				</td>
			</tr>
		</table>
		</div>
	<%} %>