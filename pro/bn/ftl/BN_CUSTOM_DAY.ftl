<div id="tinyPanel-wrapper" class="tiny-vertical" style="height:${hei};width:auto;margin:4px auto;position:relative;">
	
	<div style="width:auto;">
		<a href="http://www.aeonlife.com.cn/cms/customer2015/" target="_blank">
			 <img style='border: 0;width:${width}; height:${hei};' src="${src}" alt="客户节" />
		</a>
	</div>
</div>

<script>
function openContacturl(href) {
				var url = "";
				var tTitle = "";
				
				var options = {"url":url,"tTitle":tTitle,"menuFlag":3,top:true};
				var tabP = jQuery.toJSON(options);
				tabP = tabP.replace(/\"/g,"'");
				/*window.open("/sy/comm/page/page.jsp?openTab="+(tabP));*/
				Tab.open(options);
}
function openLocation() {
	var opts = {"url":"SY_COMM_WORKLOC.show.do?func=view","tTitle":"工位图","params":'',"menuFlag":3};
    parent.Tab.open(opts); 
}
</script>