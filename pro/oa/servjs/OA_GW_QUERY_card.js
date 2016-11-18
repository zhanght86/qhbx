var _viewer = this;

var data = {};
var resultData = FireFly.doAct("OA_GW_QUERY","getTmplTypeList",data);	

var typeTmplListStr = eval(resultData._DATA_);

//默认进页面，隐藏所有的
_viewer.form.hideAll();
showCommField();

//在类型变得时候，清除已经填入的值 TODO
genTmplOptions();
	
//文件类型变化的时候，修改其显示的字段  TMPL_TYPE_CODE
jQuery("#OA_GW_QUERY-TMPL_TYPE_CODE").change(function(){
    showPageByType();
	//在类型变得时候，清除已经填入的值 TODO
	genTmplOptions();
});

var queryDetailBtn = _viewer.getBtn('detailMode');
var queryFastBtn = _viewer.getBtn('fastMode');

queryDetailBtn.bind("click",function() {
   changeQueryPageMode();
   showQueryMode = "detail";
   showPageByType();
   refreshBottomBtn();
});	
queryFastBtn.bind("click",function() {
   changeQueryPageMode();
   showQueryMode = "fast";
   showPageByType();
   refreshBottomBtn();
});

var gwQueryBtn = _viewer.getBtn('gwQuery');
gwQueryBtn.bind("click",function() {
   queryGw(_viewer.form.getItemsValues());
});	

/**
 * 清除下面的按钮，重新生成
 * @param _viewer
 */	
function refreshBottomBtn() {
	jQuery(".rhCard-btnBar-bottom").remove();
	//_viewer._bldBtnBarBottom();	
}

/**
 * 切换查找模式  rh-icon-inner
 */
var queryModeFast = 0; 
function changeQueryPageMode(){
	queryModeFast++;
	var queryDetailBtn = _viewer.getBtn('detailMode');
	var queryFastBtn = _viewer.getBtn('fastMode');
	jQuery("#OA_GW_QUERY-GW_YEAR_CODE").parent().find(".flagForRemove").remove();
	if (queryModeFast%2 == 0) { //目前是快速，点之后变成详细
		queryDetailBtn.show();
		queryFastBtn.hide();
	} else { //目前是详细，点之后变成快速
		queryDetailBtn.hide();
		queryFastBtn.show();	    
	}
}

/**
 * 显示公共的项
 */
function showCommField() {
	_viewer.getItem("TMPL_CODE").show();
	_viewer.getItem("TMPL_TYPE_CODE").show();
	_viewer.getItem("GW_TITLE").show();
	_viewer.getItem("S_WF_STATE").show();
	_viewer.getItem("S_UNAME").show();
	_viewer.getItem("S_EMERGENCY").show();
	_viewer.getItem("GW_SECRET").show();
	//_viewer.getItem("GW_YEAR_CODE").show();
	setJiGuanDaiZi();
}

//设置机关代字
function setJiGuanDaiZi(){
	//调整显示顺序
	_viewer.form.getItem("S_WF_STATE").getContainer().after(_viewer.form.getItem("GW_CODE").getContainer());
	_viewer.getItem("GW_CODE").show();
}

/**
 * 时间段的显示
 */ 
function genTimePeriod(timeField){
	var timeFieldObj = jQuery("#" + _viewer.servId + "-" + timeField).parent();
	jQuery("<span style='float:left;height:27px;line-height:27px;'>从</span>").appendTo(timeFieldObj);
	jQuery("<span id='OA_GW_QUERY-"+timeField+"_span1' style='float:left;'><input onfocus='datePicker(\"\")' class='Wdate' style='height: 27px;line-height: 27px;border-width:0px 0px;' id='OA_GW_QUERY-"+timeField+"_1' type='text'></span>").appendTo(timeFieldObj);
	jQuery("<span style='float:left;height:27px;line-height:27px;'>到</span>").appendTo(timeFieldObj);
	jQuery("<span id='OA_GW_QUERY-"+timeField+"_span2'><input onfocus='datePicker(\"\")' class='Wdate' style='height: 27px;line-height: 27px;border-width:0px 0px;' id='OA_GW_QUERY-"+timeField+"_2' type='text'></span>").appendTo(timeFieldObj);	
	
    jQuery("#" + _viewer.servId + "-" + timeField).remove();
}

/**
 * 按照文件类别显示不同的页面项
 */ 
var showQueryMode = "fast"; 
function showPageByType() {
	_viewer.form.hideAll();
	showCommField(); 
	
	if(showQueryMode == "fast"){
	    _viewer._resetHeiWid();
		return false;
	}
	
	var gwType = _viewer.itemValue("TMPL_TYPE_CODE");
	if (gwType == "OA_GW_TYPE_FW") {
		clearOtherItems();
		_viewer.getItem("GW_MAIN_TO").show();
		_viewer.getItem("GW_COPY_TO").show();
		_viewer.getItem("GW_COSIGN_TO").show();
		//_viewer.getItem("GW_TOPIC").show();
		//设置机关代字的三列
		setJiGuanDaiZi();		
	    _viewer.getItem("S_TNAME").show();	 // 拟稿部门			
		//_viewer.getItem("GW_FILE_TYPE").show();	 // 文种 
		_viewer.getItem("GW_BEGIN_TIME").show();    //拟稿时间
		genTimePeriod("GW_BEGIN_TIME");    //从 xxx 到 yyy  时间段的
		_viewer.getItem("GW_SIGN_TIME").show();     //签发时间
		genTimePeriod("GW_SIGN_TIME");     //从 xxx 到 yyy  时间段的		
		_viewer.getItem("GW_CW_TIME").show();       //成文时间
		genTimePeriod("GW_CW_TIME");       //从 xxx 到 yyy  时间段的		
	} else if(gwType == "OA_GW_TYPE_SW") {
		clearOtherItems();
		_viewer.getItem("GW_MAIN_TO").show();
		_viewer.getItem("GW_COPY_TO").show();
		_viewer.getItem("GW_COSIGN_TO").show();
		//_viewer.getItem("GW_TOPIC").show();	
		//设置机关代字的三列
		setJiGuanDaiZi();				
		_viewer.getItem("GW_SIGN_TIME").show();     //签发时间
		genTimePeriod("GW_SIGN_TIME");     //从 xxx 到 yyy  时间段的		
		_viewer.getItem("GW_BEGIN_TIME").show();    //拟稿时间
		genTimePeriod("GW_BEGIN_TIME");    //从 xxx 到 yyy  时间段的		 TODO这里其实是收文时间，显示的是拟稿时间
		_viewer.getItem("GW_SW_CNAME").show(); //来文单位
		_viewer.getItem("GW_ZB_TNAME").show(); //主办部门
		//_viewer.getItem("GW_FILE_TYPE").show();// 文种 		
		_viewer.getItem("GW_SW_TYPE").show();  //来文类别
	} else if(gwType == "OA_GW_TYPE_JY") {
		clearOtherItems();
		_viewer.getItem("GW_MAIN_TO").show();
		_viewer.getItem("GW_COPY_TO").show();
		//_viewer.getItem("GW_TOPIC").show();	
		//设置机关代字的三列
		setJiGuanDaiZi();				
		//_viewer.getItem("GW_JIYAO_TYPE").show();	// 会议类型  		//GW_FILE_TYPE 
	} else if(gwType == "OA_GW_TYPE_CZ") { //传真电报
		clearOtherItems();
		_viewer.getItem("GW_COPY_TO").show();	    
		//设置机关代字的三列
		setJiGuanDaiZi();				
		_viewer.getItem("GW_BEGIN_TIME").show();    //拟稿时间
		genTimePeriod("GW_BEGIN_TIME");    //从 xxx 到 yyy  时间段的
		_viewer.getItem("GW_SIGN_TIME").show();     //签发时间
	} else if(gwType == "OA_GW_TYPE_QB") { //标题，主题词
		clearOtherItems();
		//设置机关代字的三列
		setJiGuanDaiZi();			    
	} else {
	    //_viewer.getItem("GW_TOPIC").show();
	    _viewer.getItem("GW_BEGIN_TIME").show();    //拟稿时间
		genTimePeriod("GW_BEGIN_TIME");    //从 xxx 到 yyy  时间段的
	}
	_viewer._resetHeiWid();
}

/**
 * 生成公文模板下拉框
 */
function genTmplOptions() {
	//初始显示相应项
    var typeVal = _viewer.getItem("TMPL_TYPE_CODE").getValue();
	if (typeVal == "OA_GW_TYPE_FW") {
		_viewer.getItem("TMPL_CODE").addOptionsByDict("OA_GW_TMPL_FW_CODE");
	} else if (typeVal == "OA_GW_TYPE_SW") {
		_viewer.getItem("TMPL_CODE").addOptionsByDict("OA_GW_TMPL_SW_CODE");
	} else if (typeVal == "OA_GW_TYPE_QS_BM") {
		_viewer.getItem("TMPL_CODE").addOptionsByDict("OA_GW_TMPL_QS_BM_CODE");
	} else if (typeVal == "OA_GW_TYPE_QS_YW") {
		_viewer.getItem("TMPL_CODE").addOptionsByDict("OA_GW_TMPL_QS_YW_CODE");
	} else {
		_viewer.getItem("TMPL_CODE").removeOptions();
	}
}

/**
 * 查询公文
 */
function queryGw(itemsValues){
	//itemsValues.GW_YEAR = jQuery("#OA_GW_QUERY-GW_YEAR").val();
	//itemsValues.GW_YEAR_NUMBER = jQuery("#OA_GW_QUERY-GW_YEAR_NUMBER").val();
	itemsValues.GW_CODE = jQuery("#OA_GW_QUERY-GW_CODE").val();
	
	if (jQuery("#OA_GW_QUERY-GW_BEGIN_TIME_1").val() != undefined) {
	    itemsValues.GW_BEGIN_TIME_1 = jQuery("#OA_GW_QUERY-GW_BEGIN_TIME_1").val();
	}
	if (jQuery("#OA_GW_QUERY-GW_BEGIN_TIME_2").val() != undefined) {
		var beginTime2 = jQuery("#OA_GW_QUERY-GW_BEGIN_TIME_2").val() || "";
		if (beginTime2 != "") {
			itemsValues.GW_BEGIN_TIME_2 = beginTime2 + " 23:59:60";
		}
	}	
	if (jQuery("#OA_GW_QUERY-GW_SIGN_TIME_1").val() != undefined) {
	    itemsValues.GW_SIGN_TIME_1 = jQuery("#OA_GW_QUERY-GW_SIGN_TIME_1").val();
	}
	if (jQuery("#OA_GW_QUERY-GW_SIGN_TIME_2").val() != undefined) {
		var signTime2 = jQuery("#OA_GW_QUERY-GW_SIGN_TIME_2").val() || "";
		if (signTime2 != "") {
			itemsValues.GW_SIGN_TIME_2 = signTime2 + " 23:59:60";
		}
	}
	if (jQuery("#OA_GW_QUERY-GW_CW_TIME_1").val() != undefined) {
	    itemsValues.GW_CW_TIME_1 = jQuery("#OA_GW_QUERY-GW_CW_TIME_1").val();
	}
	if (jQuery("#OA_GW_QUERY-GW_CW_TIME_2").val() != undefined) {
		var cwTime2 = jQuery("#OA_GW_QUERY-GW_CW_TIME_2").val() || "";
		if (cwTime2 != "") {
			itemsValues.GW_CW_TIME_2 = cwTime2 + " 23:59:60";
		}
	}
	var opts = {"url":"OA_GW_QUERY.list.do","tTitle":"查询结果","params":itemsValues};
	
	Tab.open(opts);
}

//清楚当前表单中隐藏的其他数据
function clearOtherItems() {
	var items = _viewer.form.getItems();
	for (var key in items) {// 遍历所有表单域
		var item = items[key];
		if (!item.obj.is(":visible")) {
			if (item.clear) {
				item.clear();
			}
		}
	}
}

//一进去是简单的 
queryDetailBtn.show();
queryFastBtn.hide();

