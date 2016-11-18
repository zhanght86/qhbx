var _viewer = this;
_viewer.getBtn("add").unbind("click");
_viewer.getBtn("add").bind("click", function(event) { //绑定add的act
	var configStr = "OA_GW_TMPL" + ",{'TARGET':'~TMPL_CODE','SOURCE':'TMPL_NAME~TMPL_CODE'," 
	             +"'EXTWHERE':' and 1=1','TYPE':'multi'}";  
	var options = {"config" : configStr, //树形配置字符串
	"replaceCallBack":confirmSend, //回调方法
	"parHandler":_viewer}; //用于回调的上下文
	var dictView = new rh.vi.rhSelectListView(options); //初始化字典对象
	dictView.show(event); //显示字典
})
/**
 * 选择数据后的回调函数
 */
var confirmSend = function(idArray) {
	var data = {};
	var array = [];
	jQuery.each(idArray["TMPL_CODE"].split(","), function(i, n) {
		var tmp = {};
		tmp["CODE_ID"] = _viewer.links["CODE_ID"];
		tmp["TMPL_CODE"] = n;
		array.push(tmp);
	})
	data["BATCHDATAS"] = array;
	FireFly.batchSave(_viewer.servId, data, null, true);
	_viewer._refreshGridBody();
};