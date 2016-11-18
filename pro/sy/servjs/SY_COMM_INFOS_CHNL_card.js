var _viewer = this;
//动态替换掉外部的extwhere
var extwhere = _viewer.getParHandler().slimParams()["@com.rh.core.comm.news.InfosChnlDict__EXTWHERE"];
if(extwhere){
_viewer.getItem("CHNL_PID")._getConfigVal = function() {
	var config ='@com.rh.core.comm.news.InfosChnlDict,{"params":{"REP_DICT_ID":"SY_COMM_INFOS_CHNL_MANAGE","_extWhere":"'+extwhere+'"}}';
	return config;
};
}

/*
 * 打开授权页面
 * @param {} aclType 权限定义CODE
 * @param {} dataName 所授权限数据名称，可随意，只是显示在Dialog的左上角
 */
function doDelegate(aclType, dataName) {
	var servId = "SY_COMM_INFOS_CHNL";
	var dataId = _viewer._pkCode;

	new rh.ui.Delegate({"servId":servId,"dataId":dataId,"aclType":aclType,"title":dataName + "-授权"}).open();
};

var MANAGE = _viewer.getBtn("SY_COMM_INFOS_CHNL_MANAGE");
if (MANAGE) {
	MANAGE.unbind("click").bind("click",function() {
		doDelegate('SY_COMM_INFOS_CHNL_MANAGE','信息栏目-管理权限');
	});
}

var POST = _viewer.getBtn("SY_COMM_INFOS_CHNL_POST");
if (POST) {
	POST.unbind("click").bind("click",function() {
		doDelegate('SY_COMM_INFOS_CHNL_POST','信息栏目-发布权限');
	});
}

var VIEW = _viewer.getBtn("SY_COMM_INFOS_CHNL_VIEW");
if (VIEW) {
	VIEW.unbind("click").bind("click",function() {
		doDelegate('SY_COMM_INFOS_CHNL_VIEW','信息栏目-查看权限');
	});
}
//
//var CHECK = _viewer.getBtn("SY_COMM_NEWS_CHNL_CHECK");
//if (CHECK) {
//	CHECK.unbind("click").bind("click",function() {
//		doDelegate('SY_COMM_NEWS_CHNL_CHECK','新闻栏目-审核权限');
//	});
//}




