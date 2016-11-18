<#-- 我的会议列表查看-->
<#-- 表头信息-->
<div id='OA_MT_MYLIST_TAB' class='portal-box ${boxTheme}'>
<div id="OA_MT_MYLIST_TAB_CON" class='portal-box-con portal-box-tab' style="float:hidden;">
<#-- 标签-->
<ul class='portal-box-title'>
<li><a href="#OA_MT_MYLIST_TAB_TODAY" id="OA_MT_MYLIST_TAB_TODAY_A">今天会议</a></li>
<li><a href="#OA_MT_MYLIST_TAB_WEEK" id="OA_MT_MYLIST_TAB_WEEK_A">本周会议</a></li>
<li><a href="#OA_MT_MYLIST_TAB_MONTH" id="OA_MT_MYLIST_TAB_MONTH_A">本月会议</a></li>
<#-- [更多]按钮-->
<span class="portal-box-more"><a href="javascript:void(0);" onclick="openMoreList()"></a></span>
</ul>
<#-- 标签div-->
<div id="OA_MT_MYLIST_TAB_TODAY" style='width:100%;height:260px;float:left;'></div>
<div id="OA_MT_MYLIST_TAB_WEEK" style='width:100%;height:260px;float:left;'></div>
<div id="OA_MT_MYLIST_TAB_MONTH" style='width:100%;height:260px;float:left;'></div>
</div>
</div>
<script type="text/javascript">
<#-- 引用[我的会议]服务-->
function initShowList(thisObj,date){
  var temp = {"sId":"OA_MT_MYMEETING_COUNT","pCon":thisObj,"reset":"false","showPageFlag":false,
  "showSearchFlag":"false","showTitleBarFlag":"false","showButtonFlag":false,"links":date};
  var listView = new rh.vi.listView(temp);
  listView.show();
}

<#-- 点击[更多]按钮连接跳转-->
function openMoreList(){
	Tab.open({
		"url" : "OA_MT_MYMEETING_COUNT.list.do",
		"params":{
			"links":{
				"MORE":true
			}
		},
		"tTitle" : "我的会议",
		"menuFlag" : 4
	});
}

jQuery(document).ready(function(){
	<#-- 加载模板样式，并初始化默认标签页-->
    setTimeout(function() {
		  initShowList(jQuery("#OA_MT_MYLIST_TAB_TODAY"),{"TODAY":"TODAY"});
	      jQuery("#OA_MT_MYLIST_TAB_CON").tabs({});
	},0);
    
	<#-- 我的会议[今天会议]标签绑定事件-->
    jQuery("#OA_MT_MYLIST_TAB_TODAY_A").unbind("click").bind("click",function(){
    	jQuery("#OA_MT_MYLIST_TAB_TODAY").html("");
		initShowList(jQuery("#OA_MT_MYLIST_TAB_TODAY"),{"TODAY":"TODAY"});
	});
    <#-- 我的会议[本周会议]标签绑定事件-->
	jQuery("#OA_MT_MYLIST_TAB_WEEK_A").unbind("click").bind("click",function(){
		jQuery("#OA_MT_MYLIST_TAB_WEEK").html("");
		initShowList(jQuery("#OA_MT_MYLIST_TAB_WEEK"),{"WEEK":"WEEK"});
	});
	<#-- 我的会议[本月会议]标签绑定事件-->
	jQuery("#OA_MT_MYLIST_TAB_MONTH_A").unbind("click").bind("click",function(){
		jQuery("#OA_MT_MYLIST_TAB_MONTH").html("");
		initShowList(jQuery("#OA_MT_MYLIST_TAB_MONTH"),{"MONTH":"MONTH"});
	});
});

</script>
