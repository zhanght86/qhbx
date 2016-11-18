<%@ page language="java" contentType="text/html;charset=GBK"%>
<link href="<%=request.getContextPath()%>/global/css/leftpannel.css" rel="stylesheet" type="text/css" />
<div id="leftPannel_title"><img src="<%=request.getContextPath()%>/global/images/leftpannel/leftpannel_title_1.jpg"/></div>
<div id="leftPannel_list">
	<ul id="leftPannel_ul">
		<li><img src="<%=request.getContextPath()%>/global/images/leftpannel/leftpanel_closed.jpg"><a href="<%=request.getContextPath()%>/aboutAEON/intro/">公司简介</a></li>
		<li><img src="<%=request.getContextPath()%>/global/images/leftpannel/leftpanel_closed.jpg"><a href="<%=request.getContextPath()%>/aboutAEON/shareholdersIntro/">股东介绍</a></li>
		<li><img src="<%=request.getContextPath()%>/global/images/leftpannel/leftpanel_closed.jpg"><a href="<%=request.getContextPath()%>/aboutAEON/speech/">董事长致辞</a></li>
		<li><img src="<%=request.getContextPath()%>/global/images/leftpannel/leftpanel_closed.jpg"><a href="<%=request.getContextPath()%>/aboutAEON/organization/">组织架构</a></li>
		<li><img src="<%=request.getContextPath()%>/global/images/leftpannel/leftpanel_closed.jpg"><a href="<%=request.getContextPath()%>/aboutAEON/culture/">企业文化</a></li>
		<li><img src="<%=request.getContextPath()%>/global/images/leftpannel/leftpanel_closed.jpg"><span onclick="showBranch(this)">公益活动</span>
			<ul style="display:none">
				<li><img src="<%=request.getContextPath()%>/global/images/leftpannel/leftpanel_closed_1.jpg"><a href="<%=request.getContextPath()%>/aboutAEON/activity/2009/">发现关爱2009公益摄影大赛</a></li>
				<li><img src="<%=request.getContextPath()%>/global/images/leftpannel/leftpanel_closed_1.jpg"><a href="<%=request.getContextPath()%>/aboutAEON/activity/aeon/">百年宝宝百岁寿星</a></li>
			</ul>
		</li>
		<li><img src="<%=request.getContextPath()%>/global/images/leftpannel/leftpanel_closed.jpg"><a href="<%=request.getContextPath()%>/aboutAEON/development/">百年历程</a></li>
		<li><img src="<%=request.getContextPath()%>/global/images/leftpannel/leftpanel_closed.jpg"><span onclick="showBranch(this)">广告专区</span>
			<ul style="display:none">
				<li><img src="<%=request.getContextPath()%>/global/images/leftpannel/leftpanel_closed_1.jpg"><a href="<%=request.getContextPath()%>/aboutAEON/zone/brand/">品牌广告</a></li>
				<li><img src="<%=request.getContextPath()%>/global/images/leftpannel/leftpanel_closed_1.jpg"><a href="<%=request.getContextPath()%>/aboutAEON/zone/product/">产品广告</a></li>
			</ul>
		</li>
	</ul>
</div>
<div id="leftPannel_bottom"><span><img src="<%=request.getContextPath()%>/global/images/leftpannel/back_index.jpg"/>&nbsp;<a href="<%=request.getContextPath()%>/index.jsp">首页</a></span></div>
<script type="text/javascript"">
	var currentHref = window.location.href;
	var imgAll =document.getElementById("leftPannel_ul").getElementsByTagName("a");
	for(var i=0;i<imgAll.length;i++){
		var imgHref=imgAll[i].href;
		if(currentHref.indexOf(imgHref)>-1 && imgHref.indexOf("#t")==-1){
			imgAll[i].className="current_channel";
			var liShow = imgAll[i].parentNode;
			if(liShow.getElementsByTagName("img")[0].src.indexOf("leftpanel_closed_1.jpg")>-1){
				var ulShow = liShow.parentNode;
				showBranch(ulShow);
			}
		}
	}
	function showBranch(obj){
		var liShow = obj.parentNode;
		var ulShow = liShow.getElementsByTagName("ul");
		var imgShow = liShow.getElementsByTagName("img");
		if(ulShow.length>0){
			if(ulShow[0].style.display=="none"){
				ulShow[0].style.display="";
				imgShow[0].src="<%=request.getContextPath()%>/global/images/leftpannel/leftpanel_open.jpg";
			}else{
				ulShow[0].style.display="none";
				imgShow[0].src="<%=request.getContextPath()%>/global/images/leftpannel/leftpanel_closed.jpg";
			}
			
		}
	}
</script>