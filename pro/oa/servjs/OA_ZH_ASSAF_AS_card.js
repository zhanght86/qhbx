var _viewer = this;
//重写添加字典方法，添加清除老字典
_viewer.getItem("AS_SERV_TYPES").addOptionsByDict = function(dictId) {
	var data = FireFly.getDict(dictId)[0].CHILD;
	this.removeOptionsOld();
	this.addOptions(data);
};
//主单项的值
var typeVal = _viewer.getItem("AS_SERV").getValue();
if (typeVal == "NET") {
	_viewer.getItem("AS_ASP_NAME").hide();
} else {
	_viewer.getItem("AS_ASP_NAME").show();
}
// 遍历下拉框值做隐藏判断
_viewer.getItem("AS_SERV_TYPES").obj.find("option").each(function() {
			var me = jQuery(this);
			if (me.val().indexOf(typeVal) != 0) {
				me.css("display", "none");
			}
		});
//主单项绑定change事件		
_viewer.form.getItem("AS_SERV").obj.unbind("change").bind("change", function() {
			var typeVal2 = _viewer.getItem("AS_SERV").getValue();
			var reDictCode2 = "AS_SERV-" + typeVal2;
			if (typeVal2 == "NET") {
				_viewer.getItem("AS_ASP_NAME").hide();
			} else {
				_viewer.getItem("AS_ASP_NAME").show();
			}
			_viewer.getItem("AS_SERV_TYPES").addOptionsByDict(reDictCode2);
		});

// //备注显示样式
//_viewer.form.getItem("MEMO_TITLE").obj.parent().removeClass("disabled").css({"background":"url('')"});
//_viewer.form.getItem("MEMO_TITLE").obj.css({"color":"black","font-weight":"bolder","background-color":"white","font-size":"14px"});