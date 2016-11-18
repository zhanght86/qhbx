var _viewer = this;

//_viewer.getItem("YJ_XZDL_S").addOptionsByDict = function(dictId) {
//	var data = FireFly.getDict(dictId)[0].CHILD;
//	this.removeOptionsOld();
//	this.addOptions(data);
//};
//
////一级险种
//var typeVal1 = _viewer.getItem("YJ_XZDL_F").getValue();
//var reDictCode1 = "OA_ZH_YJXYW_YJ-" + typeVal1;
//_viewer.getItem("YJ_XZDL_S").addOptionsByDict(reDictCode1);
//
////一级险种绑定change事件
//_viewer.form.getItem("YJ_XZDL_F").obj.unbind("change").bind("change",function(){
//	
//	var typeVal2 = _viewer.getItem("YJ_XZDL_F").getValue();
//	var reDictCode2 = "OA_ZH_YJXYW_YJ-" + typeVal2;
//	_viewer.getItem("YJ_XZDL_S").addOptionsByDict(reDictCode2);
//});

 //备注显示样式
_viewer.form.getItem("MEMO_TITLE").obj.parent().removeClass("disabled");
_viewer.form.getItem("MEMO_TITLE").obj.css({"color":"black","font-weight":"bolder","background-color":"white","font-size":"14px"});