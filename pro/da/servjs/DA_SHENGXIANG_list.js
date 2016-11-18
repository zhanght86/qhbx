var _viewer = this;
//来自pro/da/da/SubDirTree.js的方法daListServ("","","")
daListServ(_viewer , "声像档案库" , "subDeptDA");
var daBtn = new rh.da.rhDABtns({"servId" : _viewer.opts.sId, "parHandler" : _viewer});
daBtn.init();

//去掉最早的 按钮分组功能，用系统的功能
//if (jQuery(document).data("existBtnGroup") != "true") {
//	var groupAct = new rh.da.rhGroupAct({"parHandler":_viewer});
//	groupAct.addGroupsName();	
//}

