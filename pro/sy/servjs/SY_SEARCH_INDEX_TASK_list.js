var _viewer = this;
/**
 * 打开迷你卡片
 */
var setTaskBtn = _viewer.getBtn("setTask");
if (setTaskBtn) {
setTaskBtn.unbind("click").bind("click", function() {
//构造服务		
var options = {"sId": "SY_SEARCH_INDEX_TASK","widHeiArray": [1000,300],"xyArray": [100,50]};
//设置服务方法、A服务句柄
var opts = {"act":UIConst.ACT_CARD_ADD,"parHandler":_viewer}
//确定按钮对象
var deleteBtn = {
	ACT_CODE: "confirmEve",//按钮代码
	ACT_CSS: "save",//按钮样式类型
	ACT_EXPRESSION: "",//按钮操作表达式
	ACT_EXPRESSION_FLAG: "",
	ACT_MEMO: "",//按钮操作脚本
	ACT_NAME: "确定",//按钮名称
	ACT_ORDER: "0",
	ACT_TYPE: "2",
	ACT_WS_FLAG: "2",
	ACT_WS_RESULT: "",
	S_FLAG: "1"
}
//初始化卡片引擎
var cardView = new rh.vi.cardView(jQuery.extend(opts,options));
//添加按钮对象到B服务卡片页的对象属性中
cardView._data.BTNS.deleteEve = deleteBtn;
//展示卡片
cardView.show();
//隐藏保存按钮
jQuery("[actcode='save']").hide();
//确定按鈕事件
jQuery("[actcode='confirmEve']").unbind("click").bind("click", function(){
	    var task_name = cardView.getItem("TASK_NAME").getValue();
	    var plan_end_time = cardView.getItem("PLAN_END_TIME").getValue();
			if (task_name != "") {
				var param = {};
				param["TASK_NAME"] = task_name;
				if (plan_end_time != "") {
					param["PLAN_END_TIME"] = plan_end_time;
				} else {
					param["PLAN_END_TIME"] = "";
				}
				FireFly.doAct("SY_SEARCH_INDEX_TASK", "runIndexTask", param);
				//卡片关闭
				cardView.backClick();
				//列表刷新
				_viewer.refresh();
				}
		});
	});
}