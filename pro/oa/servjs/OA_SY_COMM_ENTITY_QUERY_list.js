var _viewer = this;
// 对列表上选中行双击查看绑定事件
_viewer.grid.unbindTrdblClick();
_viewer.grid.dblClick(function(value, node) {
	// 双击打开该选中行的服务卡片页面
	Todo.openEntity(_viewer);
}, _viewer);
var colDef = this.grid.getColumnDef("S_WF_USER_STATE");
//如果字段存在且为显示
if(colDef && colDef.ITEM_LIST_FLAG == "1"){
	var titleStr = System.getVar("@C_SY_LIST_PROMPT_CONFIG@") || "";
	var titleStyle = "font-size:12px;margin:0px 10px;color:red;text-align:left;padding:6px;";
	_viewer.addRedHeader(titleStr,titleStyle);
}
jQuery("div[class='redHeader']").find("span").eq(0).css({"color":"black","font-weight":"bold","font-family":"宋体"});
//将字典项设成只读
//判断当前操作是否为相关文件查询选择，如果不存在平铺查询对象，则不执行只读操作
var isExiteSearchObj = _viewer.advSearch;
if (isExiteSearchObj) {
	//所属机构
	_viewer.advSearch.getItem("S_ODEPT").find("input[type='text']").eq(0).attr("readonly",true);
	//拟稿人
	_viewer.advSearch.getItem("S_USER").find("input[type='text']").eq(0).attr("readonly",true);
	//拟稿部门
	_viewer.advSearch.getItem("S_TDEPT").find("input[type='text']").eq(0).attr("readonly",true);
}

/** 批量传阅按钮 **/
var cmQianShou = _viewer.getBtn("batchSend");
cmQianShou.unbind().bind("click", function() {
	if (jQuery("td.checkTD input").is(':checked')) {
		batchFenfa();
	} else {
		alert("请选择需要传阅的文件。");
	}
});


/** 批量分发 **/
function batchFenfa(){
	var _self = this;
	var dataIds = _viewer.grid.getSelectItemValues("DATA_ID");
	var pkCodes = dataIds.join("@@");
	var servIds = _viewer.grid.getSelectItemValues("SERV_ID").join("@@");

	var params = {
			"DATA_ID" : dataIds.length == 1?dataIds[0]:"",  //只有一个则人员姓名后面出现传阅字样
			"userSelectDict":"SY_ORG_DEPT_USER",
			"displaySendSchm":false
	};
		
	var configStr = "@com.rh.core.serv.send.SimpleFenfaDict,{'TYPE':'multi','MODEL':'link'}"; // multi

	var options = {
		"config" : configStr,
		"hide" : "explode",
		"show" : "blind",
		"rebackCodes" : "inputName",
		"replaceCallBack" : function(id,value){
			var sendObj = {
				"SERV_IDS" : servIds,
				"DATA_IDS" : pkCodes
			};

			sendObj.fromScheme = "yes"; // 来源于方案
			sendObj.ifFirst = "yes";
			//sendObj._extWhere = " and DATA_ID = '" + _self._parHandler._pkCode + "'";
			sendObj.SEND_ITEM = [{"sendId":id.join(",")}];
			_viewer.shield();
			
			setTimeout(function() { // 适当加上延迟提升用户感觉
				try{
					var data = jQuery.toJSON(sendObj);
					var result = FireFly.doAct("SY_COMM_SEND_SHOW_USERS", "batchAutoSend",{"data":data}, true);
					if(!Tools.actIsSuccessed(result)){
						alert("发送失败");
					}
				} catch (e) {
					throw e;
				} finally {
					_viewer.shieldHide();
				}
			}, 50);
			
		},
		"dialogName" : "传阅",
		"params" : params
	};

	var dictView = new rh.vi.rhDictTreeView(options);
	dictView.show(event);
}