var _viewer = this;
//来自pro/da/da/SubDirTree.js的方法daListServ("","","")
daListServ(_viewer , "声像档案库" , "subDeptDA");
var daBtn = new rh.da.rhDABtns({"servId" : _viewer.opts.sId, "parHandler" : _viewer});
daBtn.init();
