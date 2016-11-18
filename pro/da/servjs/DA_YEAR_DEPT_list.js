var _viewer = this;
_viewer.onRefreshGridAndTree();

if(!hasWhereData()){
	jQuery(".conHeaderTitle-span , .rh-slide-flagYellow").text("年度归档部门");
}

var subDept = _viewer.getBtn("subDept");
subDept.unbind("click").bind("click",function(){
	var configStr = "SY_ORG_ODEPT_SUB";
	var options = {
			"config":configStr,
			"replaceCallBack":function(idArray,nameArray){
				selectCallBack(idArray,nameArray);
			}
	};
	var dictView = new rh.vi.rhDictTreeView(options);
	dictView.show();	
});

function selectCallBack(idArray,nameArray){
	var odeptCode = idArray.join(",");
	var odeptName = nameArray.join(",");
	_viewer.whereData[_viewer.EXT_WHERE] = " and S_ODEPT = '" + odeptCode + "'";
	_viewer._refreshGridBody();
	jQuery(".conHeaderTitle-span , .rh-slide-flagYellow").text("年度归档部门("+ odeptName +")");
}

/**
 * 
 * @returns {Boolean} 是否存在 whereData._extWhere,存在返回true
 */
function hasWhereData(){
	var hasData = _viewer.whereData[_viewer.EXT_WHERE] || "";
	if(hasData == ""){
		return false;
	}else{
		return true;
	}
}

//批量添加归档部门
var addAllDept = _viewer.getBtn("addAllDept");
addAllDept.unbind("click").bind("click",function(){
	var daYearTree = _viewer.getNavTreeObj("DA_YEAR");
	var selectedNode = daYearTree.getCurrentNode() || "";
	//如果用户选择了年度，则根据所选年度添加。
	if(selectedNode != ""){
		var dataArr = new Array();
		dataArr.push(selectedNode.ID);
		selectYear(dataArr,"",_viewer);
	}
	//如果没选年度，则让用户选择年度。
	else {
		var configStr = "DA_YEAR";
		var options = {
				"config":configStr,
				"replaceCallBack":function(idArray,nameArray){
					selectYear(idArray,nameArray,_viewer);
				}
		};
		var dictYear = new rh.vi.rhDictTreeView(options);
		dictYear.show();		
	}
});
/**
 * 从已有年度中复制部门
 */
var copyBeforeDept = _viewer.getBtn("copyBeforeDept");
copyBeforeDept.unbind("click").bind("click",function(){
	var yearTree = _viewer.getNavTreeObj("DA_YEAR");
	var nodeID = yearTree.getCurrentNode() || "";
	if(nodeID == ""){
		_viewer.listBarTipError("请在左侧选择要添加归档部门的年度");
		return false;
	}
	var yearID = nodeID.ID;
	
	var configStr = 'DA_YEAR_HIS,{"EXTWHERE":" and S_ODEPT = \''+System.getVar("@ODEPT_CODE@")+'\'"}';
	var options = {
			"config":configStr,
			"replaceCallBack":function(idArray,nameArray){
				copyDept(idArray,nameArray,yearID,_viewer);
			}
	};
	var dictYear = new rh.vi.rhDictTreeView(options);
	dictYear.show();
});
/**
 * 选择年度后的回调方法
 * @param idArray
 * @param nameArray
 */
function selectYear(idArray,nameArray,_viewer){
	var yearID = idArray.join(",");
	var datas = {};
	datas["YEAR_ID"] = yearID;
	datas["S_ODEP"] = getOdeptCode(_viewer);
	var resultObj = FireFly.doAct(_viewer.servId,"addAllDept",datas);
	if(resultObj["_MSG_"].indexOf("OK")>=0){
		_viewer.refresh();
		_viewer.listBarTip("批量添加成功");
	}
	
};

/**
 * 获取所选机构code，如果没选择机构，则获取本公司code
 * @Param _viewer 当前页句柄
 * @returns 机构code
 */
function getOdeptCode(_viewer){
	//如果用户选择了子公司，则获取子公司的odept，如果没选择，则默认查询本公司，_card.js在添加s_odept的时候也这样处理的
	var whereStr = _viewer.whereData._extWhere || "";
	if(whereStr == ""){
		odept = System.getVar("@ODEPT_CODE@");
	} else{
		var odepts = whereStr.split("'");
		 odept = odepts[1];
	}
	return odept;
};

/**
 * 将历史年度中的归档部门 保存到指定年度中
 * @param idArray 历史年度
 * @param nameArray
 * @param yearID  要添加归档部门的年度
 */
function copyDept(idArray,nameArray,yearID,_viewer){
	var datas = {};
	datas["YEAR_HIS"] = idArray.join(",");
	datas["S_ODEP"] = getOdeptCode(_viewer);
	datas["YEAR_ID"] = yearID ;
	var resultObj = FireFly.doAct(_viewer.servId,"copyBeforeDept",datas);
	if(resultObj["_MSG_"].indexOf("OK")>=0){
		_viewer.refresh();
		_viewer.listBarTip("成功复制归档部门");
	}
}