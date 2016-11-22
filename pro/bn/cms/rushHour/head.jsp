<%@ page language="java" contentType="text/html;charset=GBK"%>
<div class="agentlogo">
	<div  onmouseover="doList();" onmouseout="docloseList();"  id="user">
		<img  style="margin-right:0px;margin-top:12px;position:relative;cursor:pointer; "src="<%=request.getContextPath()%>/bn/cms/global/images/aeonrushHour/userbg.jpg" width="78px" height="22px"/>
			<div  id="openitem" class="useropen2" >
				<p><a class="usera" href="<%=request.getContextPath()%>/bn/cms/passport/index.jsp"></a></p>
				<p><a class="userg" style="margin-bottom:4px;display:block;" href="<%=request.getContextPath()%>/bn/cms/groupPassport/index.jsp"></a></p>
		   	</div>  
	</div>
</div>
<div class="agentflash">
</div>
<div class="agentnav">
	<div class="sidenav" id="sidenav">
		<ul >
			<li class="Snav"><a href="<%=request.getContextPath()%>/sy/comm/page/portal.jsp">前海再保险首页</a></li>
	<!-- 		<li class="Snav"><a href="<%=request.getContextPath()%>/bn/cms/rushHour/spike/index.jsp">尖峰时刻</a></li>
			<li class="Tnav"><a href="<%=request.getContextPath()%>/bn/cms/rushHour/cresta/index.jsp">百年登峰荣誉会</a></li>
			<li class="Snav"><a target="_blank" href="<%=request.getContextPath()%>/bn/cms/aeonsummit2013/index.jsp">百年峰会</a></li>-->
		</ul>
	</div>
</div>
<script type="text/javascript">
 var currentHref = window.location.href;
	var imgAll =document.getElementById("sidenav").getElementsByTagName("a");
	for(var i=0;i<imgAll.length;i++){
		if(i!=0){
			if(i==2){
				var imgHref=imgAll[i].href;
	 			if(currentHref.indexOf("/bn/cms/rushHour/cresta/")>-1){
		 			//a标签变色
		 			imgAll[i].className="Tnavhold";
		 			imgAll[i].style.cssText="color:white;";
		 		}
		 		
			}else{
				var imgHref=imgAll[i].href;
	 			if(currentHref.indexOf(imgHref)>-1){
		 			//a标签变色
		 			imgAll[i].className="Snavhold";
		 			imgAll[i].style.cssText="color:white;";
		 		}
			}
		}
	}
		function doList(){
				document.getElementById("openitem").style.display="block";
		}
		function docloseList(){
			document.getElementById("openitem").style.display="none";
		
		}
		
</script>

