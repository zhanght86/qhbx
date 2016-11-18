var _viewer = this;

//初始化当前机构的目录树根据列表页的查询条件设置树形的查询条件
getTreeAndData(_viewer  , "归档目录");
//选择下属机构
var subDir = _viewer.getBtn("subDir");
subDir.unbind("click").bind("click",function(){
	subOdept(_viewer , "归档目录");
});
/**复制归档目录**/
var copyParDir = _viewer.getBtn("copyParDir");
if(System.getVar("@ODEPT_CODE@") != getOdeptCode(_viewer)){
	copyParDir.hide();
	_viewer.getBtn("add").hide();
	_viewer.getBtn("delete").hide();
} else {
	copyParDir.show();
	_viewer.getBtn("add").show();
	_viewer.getBtn("delete").show();
}
copyParDir.unbind("click").bind("click",function(){
	var resultBean = FireFly.doAct(_viewer.servId , "copyParDir");
	if( resultBean["_MSG_"].indexOf("OK") == 0){
		_viewer.refreshTreeAndGrid();
		_viewer.listBarTip("复制成功");
	} else {
		_viewer.listBarTipError(resultBean["_MSG_"]);
	}
});

/**
 * 过滤左侧导航
 * @param _viewer
 * @setTitle 刷新、返回栏一行左侧的标题
 */
function getTreeAndData(_viewer , setTitle){
	if( !hasWhereData(_viewer)){
		jQuery(".conHeaderTitle-span , .rh-slide-flagYellow").text(setTitle);
		
		//根据列表页的查询条件设置树形的查询条件
		_viewer._data.ITEMS.P_ID.ITEM_INPUT_CONFIG = 'DA_DIR_MANAGE,{"TYPE":"single","EXTWHERE":" and S_ODEPT=\'' + getOdeptCode(_viewer) + '\'"}';	
		_viewer._refreshNavTree();
		_viewer.onRefreshGridAndTree();
		
	}
};

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
	_viewer.whereData[_viewer.EXT_WHERE] = " and S_ODEPT='"+cmpyCode+"'";
	_viewer._data.ITEMS.P_ID.ITEM_INPUT_CONFIG = 'DA_DIR_MANAGE,{"TYPE":"single","EXTWHERE":" and S_ODEPT=\'' + cmpyCode + '\'"}';
	_viewer.refreshTreeAndGrid();
	jQuery(".conHeaderTitle-span , .rh-slide-flagYellow").text(setTitle + "(" + cmpyName + ")");
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

