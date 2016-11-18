var _viewer = this;
_viewer.getBtn("add").unbind("click");
_viewer.getBtn("add").bind("click", function(event) { //绑定add的act
	var pDCode = _viewer.getParHandler().getItem("S_ODEPT_PATH").getValue();
	var oCode = _viewer.getParHandler().getItem("S_ODEPT").getValue();
	var configStr = "SY_ORG_DEPT_ALL" + ",{\"TYPE\":\"multi\"" 
	+ ",\"EXTWHERE\":\" and CODE_PATH LIKE '" +pDCode +"%' and ODEPT_CODE = '" + oCode + "'\"}";  
	var options = {"config" : configStr, //树形配置字符串
	"replaceCallBack":confirmSend, //回调方法
	"parHandler":_viewer}; //用于回调的上下文
	var dictView = new rh.vi.rhDictTreeView(options); //初始化字典对象
	dictView.show(event); //显示字典
})

/**
 * 选择数据后的回调函数
 */
var confirmSend = function(idArray, nameArray) {
	var data = {};
	var array = [];
	jQuery.each(idArray, function(i, n) {
		var tmp = {};
		tmp["CODE_ID"] = _viewer.links["CODE_ID"];
		tmp["DEPT_ID"] = n;
		array.push(tmp);
	})
	data["BATCHDATAS"] = array;
	FireFly.batchSave(_viewer.servId, data, null, true);
	_viewer._refreshGridBody();
};