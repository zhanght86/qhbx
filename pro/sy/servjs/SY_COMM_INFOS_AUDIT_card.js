(function(_viewer){
	var servId = _viewer.servId;
	var auditpk = _viewer.getPKCode();
//		var extwhere = _viewer.getParHandler().slimParams()["SY_COMM_INFOS_CHNL_POST__EXTWHERE"];
		
	var chnl_id = _viewer.getItem("CHNL_ID").getValue();
	if (_viewer.wfCard) {
		var chooseChnlBtn = _viewer.wfCard._getBtn("chooseChnl");
		var doPostBtn =  _viewer.wfCard._getBtn("doPost");
		var preview = _viewer.wfCard._getBtn("preview");
		
		// 选择审核栏目
		if (chooseChnlBtn) {
			chooseChnlBtn.layoutObj.unbind("click").click(function(event){
				   var param = {};
				   var extwhere ="";
				   var configStr = "";
				   param["_PK_"] = auditpk;
				   var out = FireFly.doAct("SY_COMM_INFOS_AUDIT","finds",param);
				   if(out["_DATA_"].length > 0){
				   	extwhere = " AND SITE_ID = '"+out["_DATA_"][0]["SITE_ID"]+"'";
				    configStr = '@com.rh.core.comm.news.InfosChnlDict,{"params":{"REP_DICT_ID":"SY_COMM_INFOS_CHNL_POST",' +
				   		'"_extWhere":"'+extwhere+'"}}';
				   } else {
				     configStr = '@com.rh.core.comm.news.InfosChnlDict,{"params":{"REP_DICT_ID":"SY_COMM_INFOS_CHNL_POST"}}';
				   }
					var extendTreeSetting = "{'rhexpand':false,'expandLevel':2,'cascadecheck':false,'checkParent':false,'childOnly':true}";
					
					var auditId = _viewer.getItem("AUDIT_ID").getValue();
					var options = {
						"config" :configStr,
						"extendDicSetting":StrToJson(extendTreeSetting),
						
						"replaceCallBack":function(idArray,nameArray){
							var param = {};
							param[UIConst.PK_KEY] = auditId;
							param["CHNL_ID"] = idArray[0];
							
							var out = FireFly.doAct(servId, "setPostChnlId", param);	
							chnl_id = out["CHNL_ID"];
							_viewer.refresh();
						}
					};
					var dictView = new rh.vi.rhDictTreeView(options);
					dictView.show(event);	 
			});
		}
		
		// 发布信息
		if (doPostBtn) {
			doPostBtn.layoutObj.unbind("click").click(function(){
				var auditId = _viewer.getItem("AUDIT_ID").getValue();
				if(chnl_id !=""){
				var param = {};
				param[UIConst.PK_KEY] = auditId;
				FireFly.doAct(servId, "postToChnl", param);
				} else  {
				alert("未选择栏目不能发布");
				}
			});
		}
		
		if (preview) {
			preview.layoutObj.unbind("click").click(function(){
			var auditid = _viewer.getItem("AUDIT_ID").getValue();
			//var chnl_id =FireFly.doAct();
			if(chnl_id != ""){
		     var url = "/cms/SY_COMM_INFOS_AUDIT/" + auditid+ ".html?" + new Date().getTime();			
	          window.open(url);
			} else {
			 alert("未选择栏目不能预览");
			}
			});
		}	
		
	}
})(this);