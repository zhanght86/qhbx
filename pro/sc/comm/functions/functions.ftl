/*<link rel="stylesheet" type="text/css" href="/sc/comm/functions/css/functions.css"/>
<script type="text/javascript" src="/sc/comm/functions/js/functions.js"></script>
 <div class="portal-box">
	<div class='portal-box-title'>
    	<span class="portal-box-title-pre"></span>
        <span class="portal-box-title-label">常用功能</span>
		<span class='portal-box-title-fix'></span>
	</div>
<div class="functions-wrapper" style="height:${height}">
	<div class="functions-content clearfix">
		<div class="functions-item">
			<a href="javascript:openContact();" class="functions-thumbnail">
           		<img src="/sc/comm/functions/sysimg/tongxunlu.png" alt="通讯录"/>
                <span title="共享文档收藏">通讯录</span>
        	</a>
        </div>
       <div id = "canChangeableItem" class="functions-item">
            <#--<a href="javascript:openLocation()" class="functions-thumbnail">	
               <img src="/sc/comm/functions/sysimg/gongweitu.png" alt="工位图"/>
                <span title="OA文件收藏">工位图</span>
            </a>-->
        </div>
        <div class="functions-item">
            <a href="javascript:openPhotoWall();" class="functions-thumbnail">
            	<img src="/sc/comm/functions/sysimg/zhaopianqiang.png" alt="照片墙" />
                <span title="意见反馈">照片墙</span>
            </a>
            <i id="photo-new" class="new-icon" title="新信息"></i>
        </div>
	</div>
</div>
</div>
<script>
/**
 * 页面加载执行的方法
 * @param {Object} 
 */
$(document).ready(function(){
	var odeptCode = System.getVar("@ODEPT_CODE@");
	if("24"==odeptCode){
		var alabe =  "<a href='javascript:openLocation()' class='functions-thumbnail'>"	
					+"   <img src='/sc/comm/functions/sysimg/gongweitu.png' alt='工位图'/>"
					+"    <span title='OA文件收藏'>工位图</span>"
					+"</a>";
		$(alabe).appendTo($("#canChangeableItem"));
	}else{
		var alabe =  "<a href='javascript:openInfos()' class='functions-thumbnail'>"	
					+"   <img src='/sy/comm/desk/css/images/app_rh-icons/xinxiliulan.png' alt='信息浏览'/>"
					+"    <span title='信息浏览'>信息浏览</span>"
					+"</a>";
		$(alabe).appendTo($("#canChangeableItem"));
	}

});
function openPhotoWall(){
	var odept = System.getVar("@ODEPT_CODE@");
	var url = "SC_PHOTO_WALL.show.do?chnlId=3PXaHB1VRdCqqQ4XL1hfXV&sOdept="+odept;
	var params = {};  
	var options = {"url":url,"params":params,"menuFlag":3,"tTitle":"照片墙"}; 
	Tab.open(options);
}
function openContact() {
	var opts = {"url":"SY_COMM_TEMPL.show.do?model=view&pkCode=SC_TXL","tTitle":"通讯录","params":'',"menuFlag":3};
    parent.Tab.open(opts); 
}
function openLocation() {
	var opts = {"url":"SY_COMM_WORKLOC.show.do?func=view","tTitle":"工位图","params":'',"menuFlag":3};
    parent.Tab.open(opts); 
}

function openInfos(){
	var opts = {"url":"SY_COMM_INFOS_VIEW.list.do","tTitle":"信息浏览","params":'',"menuFlag":3};
    parent.Tab.open(opts); 
}

<#-- 加载后查询数据库，确定是否显示提示标记 -->
(function ($) {
	$("#photo-new").hide();
	setTimeout(function () {
	    var newDayNum = ${newDayNum};
	    var param = {};
	    param['CHNL_ID'] = '3PXaHB1VRdCqqQ4XL1hfXV';
	    param['S_ODEPT'] = System.getVar("@ODEPT_CODE@");
	    FireFly.doAct('SC_PHOTO_WALL','getNewNews',param,false,true,function(newBean) {
	    	if (newBean != null && newBean != undefined) {
		    	var newsTime = newBean['NEWS_TIME'];
		    	if (rhDate.doDateDiff('D',newsTime.substring(0,10),rhDate.getCurentTime().substring(0, 10))<=newDayNum) {
		    		$("#photo-new").show();
		    	}
		    }
	    });
	},5000);
})(jQuery);
</script>*/