var _viewer = this;
var daBtn = new rh.da.rhDABtns({"servId" : _viewer.opts.sId, "parHandler" : _viewer});
daBtn.init();
////将按钮分组  去掉最早的 按钮分组功能，用系统的功能
//if (jQuery(document).data("existBtnGroup") != "true") {
//	var groupAct = new rh.da.rhGroupAct({"parHandler":_viewer});
//	groupAct.addGroupsName();	
//}