var _viewer = this;
//构建审批单头部
var gwHeader = new rh.vi.gwHeader({
	"servId" : _viewer.opts.sId,
	"parHandler" : _viewer
});
gwHeader.init();
var gwExtCard = new rh.vi.gwExtCardView({
	"parHandler" : _viewer
});
gwExtCard.init();
_viewer.form.getItem("GW_YEAR_CODE").obj.parent().children().addClass("gw-xf");

_viewer.form.getItem("GW_BEGIN_TIME").obj.bind("focus",function(){
	var beginDate = _viewer.form.getItem("GW_BEGIN_TIME").getValue();
	if(beginDate){
		if(beginDate>System.getVar("@DATE@")){
			alert("来访日期有误");
		}
	}
})
_viewer.form.getItem("GW_END_TIME").obj.bind("focus",function(){
	var beginDate = _viewer.form.getItem("GW_BEGIN_TIME").getValue();
	var endDate = _viewer.form.getItem("GW_END_TIME").getValue();
	if(endDate){
		if(endDate<beginDate){
			alert("办理时限日期有误");
		}	
	}
})
_viewer.beforeSave = function(){
	var beginDate = _viewer.form.getItem("GW_BEGIN_TIME").getValue();
	var endDate = _viewer.form.getItem("GW_END_TIME").getValue();
	if(beginDate>System.getVar("@DATE@")){
		_viewer.cardBarTipError("来访日期有误");
		return false;
	}
	if(endDate<beginDate){
		_viewer.cardBarTipError("办理时限日期有误");
		return false;
	}	
}
var searchFlag = _viewer.form.getItem("GW_PRINT");
var setSearch = jQuery(" <a href='#' style='padding-right: 20px;padding-top: 10px;' class='icon-input-select'></a>")
						.appendTo(searchFlag.obj.parent());
var opts = {"typeCode":"GWXF"
	,"optionType":"multi"
	,"fieldCode":"GW_PRINT"
	,"optObj":setSearch};
Select.usualContent(opts,_viewer);