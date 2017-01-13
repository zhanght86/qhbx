//@ sourceURL=OA_GW_TYPE_FW_card_mb.js

// 发文管理 总服务
var _viewer = this;

//重载传阅参数
if (_viewer.wfCard != null) {
	_viewer.wfCard.beforeCmSimpleFenFa = function(actItem,params){
		params["userSelectDict"] = "SY_ORG_DEPT_USER";
		params["displaySendSchm"] = true;
		params["includeSubOdept"] = false;
		return true;
	};
}

// 控制意见只能看本机构内的
var odeptCode = System.getUser('@ODEPT_CODE@');
var pkCode = _viewer.getPKCode();
$("div[id='" + pkCode + "_MIND']").find('.mind-list-item-js').hide();
$("div[id='" + odeptCode + "_list']").show();

// 控制发文时显示什么文件
if (_viewer.secondStep == 'toread') { // 如果是从待阅中打开的文儿
	// 只显示一个正文，顺序是 正文>红头文件>文稿
	if ($("div[code='ZHENGWEN']").find("li[data-filename='正文']").length > 0) {
		$("div[code='ZHENGWEN']").find("li[data-filename='红头文件']").remove();
		$("div[code='ZHENGWEN']").find("li[data-filename='文稿']").remove();
	} else if ($("div[code='ZHENGWEN']").find("li[data-filename='红头文件']").length > 0) {
		$("div[code='ZHENGWEN']").find("li[data-filename='文稿']").remove();
	}
} else {
	if ($("div[code='ZHENGWEN']").find("li[data-filename='正文']").length > 0) {
		$("div[code='ZHENGWEN']").find("li[data-filename='红头文件']").remove();
	}
}

// 控制主送和抄送的显示形式
var mTCDiv = $(_viewer.getItem('GW_MAIN_TO').getContainer())
,	mTECDiv = $(_viewer.getItem('GW_MAIN_TO_EXT').getContainer())
,	cTCDiv = $(_viewer.getItem('GW_COPY_TO').getContainer())
,	cTECDiv = $(_viewer.getItem('GW_COPY_TO_EXT').getContainer());

var mTCField = mTCDiv.parent()
,	mTECField = mTECDiv.parent()
,	cTCField = cTCDiv.parent()
,	cTECField = cTECDiv.parent();

var mainToField = $("<div data-role='fieldcontain' class='ui-field-contain'></div>")
					.append(mTCField.html()).append("<label></label>").append(mTECDiv);
mTECField.after(mainToField);

var copyToField = $("<div data-role='fieldcontain' class='ui-field-contain'></div>")
					.append(cTCField.html()).append("<label></label>").append(cTECDiv);
cTECField.after(copyToField);

mTCField.hide();
mTECField.hide();
cTCField.hide();
cTECField.hide();