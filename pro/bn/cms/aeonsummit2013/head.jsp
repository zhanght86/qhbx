<%@ page language="java" contentType="text/html;charset=GBK"%>
<div class="agentlogo">
	<div  onmouseover="doList();" onmouseout="docloseList();"  id="user">
		<img  style="margin-right:0px;margin-top:12px;position:relative;cursor:pointer; "src="<%=request.getContextPath()%>/bn/cms/global/images/aeonsummit/userbutton.jpg" width="78px" height="26px"/>
			<div  id="openitem" class="useropen2" >
				<p><a class="usera" href="<%=request.getContextPath()%>/passport/index.jsp"></a></p>
				<p><a class="userg" style="margin-bottom:4px;display:block;" href="<%=request.getContextPath()%>/bn/cms/groupPassport/index.jsp"></a></p>
		   	</div>  
	</div>
</div>
<div class="agentflash2">
	<embed src="<%=request.getContextPath()%>/bn/cms/global/flash/aeonsummit2013.swf" quality="high" wmode="transparent" bgcolor="#FFFFFF" width="980" height="295"  align="middle" allowScriptAccess="sameDomain" type="application/x-shockwave-flash" pluginspage="http://www.macromedia.com/go/getflashplayer"></embed>
</div>
<script type="text/javascript">
	function doList(){
				document.getElementById("openitem").style.display="block";
		}
		function docloseList(){
			document.getElementById("openitem").style.display="none";
		
		}
</script>

