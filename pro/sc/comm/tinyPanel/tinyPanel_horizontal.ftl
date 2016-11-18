<div class='portal-box'>
	<div class='portal-box-title' style='position:relative;'>
    	<span class='portal-box-title-pre'></span>
        <span class='portal-box-title-label'>${title}</span>
		<span class='portal-box-title-fix'></span>
    </div>
    <div class="portal-box-con tinyPanel-wrapper" style="height:${height}">
      	<div class="tinyPanel-content clearfix">
      	 	<a href="javascript:openContact();">
		         <img src="/sc/comm/tinyPanel/img/txl.png" alt="" />
		    </a>
		    <a href="javascript:openLocation();" class="tiny-seat-position">
		         <img src="/sc/comm/tinyPanel/img/gw.png" alt="" />
		    </a>
      	</div>
    </div>
</div>
<script>
function openContact() {
	var opts = {"url":"SY_COMM_TEMPL.show.do?model=view&pkCode=SC_TXL","tTitle":"通讯录","params":'',"menuFlag":3};
    parent.Tab.open(opts); 
}
function openLocation() {
	var opts = {"url":"SY_COMM_WORKLOC.show.do?func=view","tTitle":"工位图","params":'',"menuFlag":3};
    parent.Tab.open(opts); 
}
</script>