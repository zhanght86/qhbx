/**
 * 
 * @param _viewer 当前页句柄
 * @param titleName 刷新、返回行左侧的标题名
 * @param BtnName  按钮名称，点击后弹出子机构目录
 */
function daListServ(_viewer , titleName , BtnName){
	//查看下级公司档案操作
	var subDeptDA = _viewer.getBtn(BtnName);
	subDeptDA.unbind("click").bind("click",function(){
		subOdept(_viewer , titleName);
	});
	//点击节点弹出服务
	openClickNode(_viewer);
	//节点选中状态
	selectedNode(_viewer);
	
	//按钮显示隐藏
	checkBtnDisplay(_viewer);
	
	
	setTitle(titleName);
}


/**
 * 添加和删除的按钮只有本机构才能显示
 * @param _viewer 当前页句柄
 */
function checkBtnDisplay(_viewer) {
	
	if (System.getVar("@ODEPT_CODE@") != getOdeptCode(_viewer)) { //不相等，则隐藏
		_viewer.getBtn(UIConst.ACT_ADD).hide();
		
		_viewer.getBtn(UIConst.ACT_DELETE).hide();
		
		var btns11 = _viewer._btnBar.find(".rhGrid-btnBar-a");
		
		jQuery.each(btns11, function(index, actItem){
			if (jQuery(actItem).attr("actcode") != "subDeptDA") {
				jQuery(actItem).hide();
			}
		});
	}
	
}

function setTitle(titleName) {
	var odeptName = window.parent.jQuery(window.parent.document).data("shareOdeptShowName");
	
	if (odeptName != undefined && odeptName != "") {
		jQuery(".conHeaderTitle-span , .rh-slide-flagYellow").text(titleName + "(" + odeptName + ")");	
	}
}


/**
 * 得到下属公司的机构树
 * @Param _viewer 当前页面句柄
 * @Param setTitle 刷新、返回一行左侧的 标题
 */
function subOdept(_viewer , setTitle){
	var configStr = "SY_ORG_ODEPT_SUB" ;
	var options={
			"config":configStr,
			"replaceCallBack":function(idArray,nameArray){
				selectCallback(idArray,nameArray,_viewer , setTitle);
			}
	};
	var dictView = new rh.vi.rhDictTreeView(options);
	dictView.show(event);	
};

/**
 * 回调函数
 * @param idArray
 * @param nameArray
 * @Param _viewer
 * @Param setTitle 刷新、返回一行左侧的 标题
 */
function selectCallback(idArray,nameArray,_viewer , setTitle){
	var cmpyCode = idArray.join(",");
	var cmpyName = nameArray.join(",");
	_viewer.whereData[_viewer.EXT_WHERE] = " and S_ODEPT = '"+cmpyCode+"'";
	
	//TODO 将所选择 的 机构 放到 临时变量里面去
	window.parent.jQuery(window.parent.document).data("shareOdeptShow", cmpyCode);
	window.parent.jQuery(window.parent.document).data("shareOdeptShowName", cmpyName);
	
	openOdeptDefaultTab(_viewer, cmpyCode, setTitle);
};

/**
 * 是否存在_extWhere属性
 * @Param _viewer 当前页面句柄
 * @returns {Boolean} 没有 whereData 返回false.有值返回true
 */
function hasWhereData(_viewer){
	var whereStr = _viewer.whereData._extWhere || "";
	if(whereStr == ""){
		return false;
	} else{
		return true;
	}
};

/**
 * 获取所选机构code，如果没选择机构，则获取本公司code
 * @Param _viewer 当前页句柄
 * @returns 机构code
 */
function getOdeptCode(_viewer){
	
	var odept = window.parent.jQuery(window.parent.document).data("shareOdeptShow");
	
	if(odept == undefined || odept == ""){ //如果没取到，则用默认当前人的机构编码
		odept = System.getVar("@ODEPT_CODE@");
	} else { //设置了 shareOdeptShow 值， 但是是通过点击菜单进到页面的
		if (window.location.href.indexOf("ODEPT_CODE") < 0) {
			odept = System.getVar("@ODEPT_CODE@");
		}
	} 

	return odept;
};

/**
 * 选择子公司之后，打开默认的右侧的服务页面
 */
function openOdeptDefaultTab(_viewer, odept, setTitle) {
	var parVarStr = "{'ODEPT_CODE':'" + odept + "'}";
	var extWhere = " and S_ODEPT = '" + odept + "'";
	
	extWhere = encodeURI(extWhere);
	
	//先判断选择的子公司创建了档案目录了没
	
	var suffix = "";
	if (_viewer.servId.indexOf("_ARCH") > 0) {
		suffix = "_ARCH";
	} else if (_viewer.servId.indexOf("_PLAT") > 0) {
		suffix = "_PLAT";
	}
	
	var navTree = _viewer.getNavTreeObj("@com.rh.da.dir.ExtSubDirDict");
	navTree = null;
	
	Tab.open({
		"url" : "DA_DANGAN" + suffix + ".list.do?parVar=" + parVarStr + "&extWhere="+extWhere,
		"tTitle" : setTitle,
		"menuFlag" : 4,
		"extWhere" : extWhere
	});
	
	
	//if ("DA_DANGAN" != _viewer.servId.replace(suffix, "")) { //默认打开的是 文书档案， 当前的类型和文书不一致的时候，关闭
	    _viewer.backImg.mousedown(); //关闭前面那个TAB
	//}
}

/**
 * 导航树节点被点击时打开对应的服务
 * @param _viewer 
 */
function openClickNode(_viewer){
	_viewer.beforeTreeNodeClickLoad = function(item, id, dictId){
		if(item["ID"] == "@com.rh.da.dir.ExtSubDirDict"){
			var odept = getOdeptCode(_viewer);
			_viewer.whereData[_viewer.EXT_WHERE] = " and S_ODEPT = '"+odept+"'";
			
			return;
		}
		
		var suffix = "";
		if (_viewer.servId.indexOf("_ARCH") > 0) {
			suffix = "_ARCH";
		} else if (_viewer.servId.indexOf("_PLAT") > 0) {
			suffix = "_PLAT";
		}
		
		var param = {"DIR_ID": item["ID"]};
		var entryObj = FireFly.doAct("DA_DIR", "getDirCateServ", param, false);
		
		if (entryObj["SERV_ID"] != _viewer.servId.replace(suffix, "")) {
			var odept = getOdeptCode(_viewer);
			var parVarStr = "{'ODEPT_CODE':'" + odept + "','P_ID':'" + item["ID"] + "'}";
			var extWhere = " and S_ODEPT = '" + odept + "'";
			
			_viewer.setParParams(StrToJson(parVarStr));
			
			Tab.open({
				"url" : entryObj["SERV_ID"] + suffix + ".list.do?parVar=" + parVarStr,
				"tTitle" : entryObj["CAT_NAME"],
				"menuFlag" : 4,
				"extWhere" : extWhere
			});
			_viewer.backImg.mousedown();
		}
	}
	
}

/**
 * 默认选中 的 按钮， 
 */
function selectedNode(_viewer){
	var param = {};
	param["SERV_ID"] = _viewer.servId;
	param["S_ODEPT"] = getOdeptCode(_viewer);
	var retData = FireFly.doAct("DA_DIR", "getCodePath", param, false);
	var codePath = retData["CODE_PATH"];
	if (codePath.length > 0) {
		var navTree = _viewer.getNavTreeObj("@com.rh.da.dir.ExtSubDirDict");
		var treeId = navTree.getId();
		var ids = codePath.split("^");
		var len = ids.length;
		for (var i = 0; i < (len - 1); i++) {
			var id = ids[i];
			if (i == len - 2) {
				if (!navTree.getCurrentNode()) {
					navTree.setCurrentNode(navTree.getNode(id));
		            //jQuery("#" + treeId + "_" + id.replace(/[^\w]/gi, "_")).addClass("bbit-tree-selected");
					jQuery("#" + treeId + "_" + id.replace(/[^\w]/gi, "_")).click();
				}
			} else {
				// 挨个展开
				var img = jQuery("#" + treeId + "_" + id.replace(/[^\w]/gi, "_") + " img.bbit-tree-ec-icon");
	            if (img.length > 0 && img.parent().hasClass("bbit-tree-node-collapsed")) {// 该节点没有被展开
	                img.click();
	            }
			}
		} 
	}
};

function setPID(_viewer){
 var treeID = _viewer.getItem("P_ID").getValue();
 var treeIDStr = treeID.split("___");
 _viewer.getItem("P_ID")._cancel.click()
 _viewer.getItem("P_ID").setValue(treeIDStr[0]);
};