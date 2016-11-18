<div id="tinyPanel-wrapper" class="tiny-vertical" style="height:${hei};width:auto;margin:4px auto;position:relative;">
	
    <a title="问卷" id="webPre" style="filter:alpha(opacity=0);color:#1b1b1b;background-color:black;position:absolute;top:0px;left:0px;width:30%;height:${hei}; display: block;" href="javascript:openContacturl('W');">问卷</a>
	<a title="评优" id="webNext" style="filter:alpha(opacity=0);color:#1b1b1b;background-color:black;position:absolute;top:0px;right:0px;width:70%;height:${hei}; display: block;" href="javascript:openContacturl('P');">评优</a>
	<div style="width:auto;">
		<a href="#">
			 <img style='border: 0;width:${width}; height:${hei};' src="${src}" alt="" onclick="openContacturl('W')"/>
		</a>
	</div>
</div>

<script>
jQuery(document).ready(function(jQuery) {
		
	});
function openContacturl(href) {
				window.open("/SY_COMM_TEMPL.show.do?pkCode=EMPLOYEE_ACTIVITY1");
}
function openLocation() {
	var opts = {"url":"SY_COMM_WORKLOC.show.do?func=view","tTitle":"工位图","params":'',"menuFlag":3};
    parent.Tab.open(opts); 
}
</script>