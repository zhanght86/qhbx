/**
 * 
 * @author wangchen
 *
 */

/*********************工程级代码目录**********************
 * 当前作用域句柄
 * 字段配置--设置BOOKING_FLAG<会议室预定标志>字段下拉框的值
 * 字段配置--设置CONFEREES_CODES<本单位与会人员ID>字段调用树形字典 的回调函数
 * 字段配置--设置NOTIFIED_CODES<被通知人ID>字段调用树形字典 的回调函数
 * 选择会议室地点
 * 字段绑定事件--绑定点击<会议地点>输入框的事件,字段绑定图标--绑定点击<会议地点>输入框的内侧后部并复制其事件
 * 判断是否获取工作流按钮对象，工作流卡片中才生效
 * 按钮绑定事件--卡片按钮<保存>插入监听事件
 * 按钮绑定事件--工作流按钮<保存>插入监听事件
 * 按钮绑定事件--工作流按钮<办结>插入监听事件
 * 按钮绑定事件--工作流自定义按钮<取消会议>插入监听事件并绑定事件
 * 内部函数--构建弹出框页面布局
 * 内部函数--构造输入<取消会议>原因对话框
 * 内部函数--调用<会议室查询统计>服务，预定新会议室
 * 内部函数--查询本人已选定且未启用的会议室预定记录
 * 内部函数--提交数据前检查
 * 内部函数--比较 “本单位与会人员名单” 和 “被通知人”是否存在交集并返回交集
 * 知识总结
 */

/**当前作用域句柄*/
var _viewer = this;

//跨机构处理会议室名称
/*var mrName = _viewer.getItem("PLACE");
if (mrName.getValue().length > 0 && mrName.getValue() == mrName.getText()) {
	var out = FireFly.doAct("OA_MT_MEETING", "getMtRoomName", {"MR_ID":mrName.getValue()});
	if (out["_MSG_"].indexOf("OK") >= 0) {
		mrName.setText(out["NAME"]);
	}
}*/

//html模板中将输入框width设为100%
//jQuery(".right").css({"width":"100%"});

//html模板中div width设为100%
//jQuery(".right div").css({"width":"100%"});

//输入框宽度
//jQuery(".right input").css({"width":"100%","border":"0px #CCC solid","margin-left":"0px"});

//表格边距
//jQuery(".rh-tmpl-tabel td").css({"padding":"3px 6px 3px 5px"});

//文件头部表格内部输入框
//jQuery(".gwHeaderTable td span").css({"line-height":"2"});

//jQuery(".ui-textarea-default").css({"margin-left":"0px","width":"100%","border":"0px #CCC solid"});

/*修改输入框背景颜色*/
//jQuery(".blank").css({"background-color":"#fbf3e6;","background-image":"none"});
//jQuery(".blank *").css({"background-color":"#fbf3e6;"});

//去除上部[保存]按钮
//jQuery("#"+_viewer.servId+"-mainTab .rhCard-btnBar").css({"margin-left":"-1000px","height":"0px"});