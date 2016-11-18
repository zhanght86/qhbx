var _viewer=this;

jQuery("th[icode='fill']").remove();
jQuery("th[icode='see']").attr("colspan","2").text("操作");
var fillBtn=_viewer.grid.getBtn("fill");	
if(fillBtn){
	
	fillBtn.unbind("click").bind("click",function(){
		var id=jQuery(this).attr("rowPk");
	
		var investID=_viewer.grid.getRowItemValue(id,"INVEST_ID");
		var opts = {	
				"url":"BN_QS_INVEST.showQuestionNaire.do?data={'INVEST_ID':'"+investID+"'}",
				"tTitle":"问卷调查",
				"menuFlag":3,
				"top":true
				
		};
		Tab.open(opts);
	});
}

var seeBtn=_viewer.grid.getBtn("see");
if(seeBtn){
	
	seeBtn.unbind("click").bind("click",function(){
		var id=jQuery(this).attr("rowPk");
	
		var investID=_viewer.grid.getRowItemValue(id,"INVEST_ID");
		var opts = {	
				"url":"BN_QS_INVEST.surveyStatisShow.do?data={'INVEST_ID':'"+investID+"'}",
				"tTitle":"问卷统计",
				"menuFlag":3,
				"top":true
				
		};
		Tab.open(opts);
	});
}
