<div id="tinyPanel-wrapper" class="tiny-vertical" style="height:${height};width:100%;margin:4px auto;">
    <a href="javascript:openContact();">
         <img style='border: 0;width:100%;height:50px;margin-left:1px;' src="/sc/comm/tinyPanel/img/bntxl.png" alt="通讯录" />
    </a>
    
</div>

<script>
function openContact() {
	var opts = {"url":"OA_COMM_ADDRESS_LIST_SUB.list.do","tTitle":"通讯录","menuFlag":3};
   var opts = jQuery.toJSON(opts);
			opts = opts.replace(/\"/g,"'");
			debugger;
			window.open("/sy/comm/page/page.jsp?openTab="+opts);
}
function openLocation() {
	var opts = {"url":"SY_COMM_WORKLOC.show.do?func=view","tTitle":"工位图","params":'',"menuFlag":3};
    parent.Tab.open(opts); 
}
</script>