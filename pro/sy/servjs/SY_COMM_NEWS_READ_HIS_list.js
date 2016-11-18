var _viewer = this;
var pServId = _viewer.getParHandler().servId;
//用户服务  下批量添加角色
if (pServId.indexOf("SY_ORG_USER") >= 0) {
	_viewer.getBtn("selectAdd").unbind("click").bind("click",function(event) {
	    var extWhereStr = "and (S_PUBLIC=1 or CMPY_CODE=^@CMPY_CODE@^) and S_FLAG=1";
		var inputName = "roleCodes";
		var configStr = "SY_ORG_ROLE,{'TARGET':'ROLE_CODE~','SOURCE':'ROLE_CODE~ROLE_NAME','PKHIDE':true,'EXTWHERE':'"+extWhereStr+"','TYPE':'multi'}";
		var options = {"itemCode":inputName,
		"config" :configStr,
		"rebackCodes":inputName,
		"parHandler":this,
		"formHandler":this,
		"replaceCallBack":function(roleObjs){
				batchAddRoles(roleObjs.ROLE_CODE);
			}
		};
		var queryView = new rh.vi.rhSelectListView(options);
		queryView.show(event);	
	});
	_viewer.getBtn("copyRoleUser").hide();
} else {  //选择添加用户按钮的方法绑定,   角色服务  下批量添加用户
	var dictId = "SY_ORG_DEPT_USER";
	var userScope = "ALL";
	if (pServId.indexOf("_IN_ORG") > 0) {
		dictId = dictId + "_IN_ORG";
		userScope = "IN_ORG";
	} else if (pServId.indexOf("_SUB_ORG") > 0) {
		dictId = dictId + "_SUB_ORG";
		userScope = "SUB_ORG";
	}
	_viewer.getBtn("selectAdd").unbind("click").bind("click",function(event) {//选择添加用户按钮
		var configStr = dictId + ",{'TYPE':'multi'}";
		var extendTreeSetting = {'cascadecheck':true,"childOnly":true};
		var options = {"itemCode":"hello","config" : configStr,"hide":"explode","show":"blind",'childOnly':true,
		"extendDicSetting":extendTreeSetting,
		"replaceCallBack":function(idArray,nameArray) {
			   var newsId =  _viewer.getParHandler().getItem("NEWS_ID").getValue();
			   var newTitle =  _viewer.getParHandler().getItem("NEWS_SUBJECT").getValue();
			   if (idArray.length > 0) {
				   var batchData = {};
				   var tempArray = [];
				   jQuery.each(idArray,function(i,n) {
					   var temp = {"NEWS_ID":newsId,"USER_CODE":n, "USER_NAME":nameArray[i]};
					   tempArray.push(temp);
				   });
				   batchData["BATCHDATAS"] = tempArray;
				   var resultData = FireFly.batchSave("SY_COMM_NEWS_READ_HIS",batchData,null,_viewer.getNowDom());
				   _viewer.refreshGrid();
				   
				   
//				   var batchData = {};
//				   var tempArray = [];
//				   var userCodes = "";
//				   jQuery.each(idArray,function(i,n) {
//					   userCodes  += (n +","); 
//				   });
//				   batchData["NEWS_ID"] = newsId;
//				   batchData["USER_CODES"] = userCodes;
//				   var resultData = FireFly.doAct("SY_COMM_NEWS","createReadHis", batchData);
//				   _viewer.refreshGrid();
			   }
		  }
		};
		var dictView = new rh.vi.rhDictTreeView(options);
		dictView.show(event);
	});
	
	_viewer.getBtn("copyRoleUser").unbind("click").bind("click",function(event) {//复制角色用户
		var inputName = "roleCodes";
		
		var roleServId = "SY_ORG_ROLE";
		if (pServId == "LW_ORG_ROLE_ZONG") {
		    roleServId = "LW_ORG_ROLE_ZONG";
		} else if (pServId == "LW_ORG_ROLE_FEN") {
		    roleServId = "LW_ORG_ROLE_FEN";
		}
		
		var configStr = roleServId + ",{'TARGET':'ROLE_CODE~','SOURCE':'ROLE_CODE~ROLE_NAME','TYPE':'single'}";
		var options = {"itemCode":inputName,
		"config" :configStr,
		"rebackCodes":inputName,
		"parHandler":this,
		"formHandler":this,
		"replaceCallBack":function(roleObjs){
				copyRoleUser(roleObjs.ROLE_CODE, userScope);
			}
		};
		var queryView = new rh.vi.rhSelectListView(options);
		queryView.show(event);	
	});
	_viewer.getBtn("add").hide();
}

/**
 * 批量添加角色
 */
function batchAddRoles(roleObjs) {
	var userCode = _viewer.getParHandler().getItem("USER_CODE").getValue();
	if (roleObjs.length > 0) {
	   var batchData = {};
	   var tempArray = [];
	   jQuery.each(roleObjs.split(","),function(i,n) {
		   var temp = {"USER_CODE":userCode,"ROLE_CODE":n};
		   tempArray.push(temp);
	   });
	   batchData["BATCHDATAS"] = tempArray;
	   
	   var resultData = FireFly.batchSave(_viewer.servId,batchData,null,_viewer.getNowDom());
	   _viewer.refreshGrid();
	}	
}

/**
 * 批量复制角色下用户
 */
function copyRoleUser(roleObjs, userScope) {
	if (roleObjs.length > 0) {
		var data = {};
		data["ROLE_CODE"] = _viewer.getParHandler().getItem("ROLE_CODE").getValue();
		data["USER_SCOPE"] = userScope;
		data["FROM_ROLE_CODE"] = roleObjs;
		var resultData = FireFly.doAct(_viewer.servId, "copyRoleUser", data);
		_viewer.refreshGrid();
	}	
}