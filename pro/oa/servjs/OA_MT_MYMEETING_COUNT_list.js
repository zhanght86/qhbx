var _viewer = this;

var params = _viewer.getParams() || "";

if ("" != params) {
	var links = params.links || "";
	if ("" != links) {
		if (links["MORE"]) {
			//
		}
	} else {
		modifyCss();
	}
} else {
	modifyCss();
}

//修改样式
function modifyCss(){
	//去除列表阴影效果
	jQuery(".rh-bottom-right-radius").css({"-webkit-box-shadow":"0px 0px 0px","box-shadow":"0px 0px 0px"});
	//重置列表背景色
	jQuery(".content-mainCont").css({"background-color":"#FFF"});
	//为列表添加边框
	jQuery(".rhGrid").css({"border":"1px #CCC solid"});
	//减小列表头部高度
	jQuery(".rhGrid-thead-th").css({"padding":"0px 0px"});
}
