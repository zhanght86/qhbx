var _viewer = this;
//添加回车标签
_viewer.form.getItem("CB_BXXS").obj.find("label").each(function(){
	var thisLabelObj = jQuery(this);
	if (thisLabelObj.html() == "其他") {
		thisLabelObj.after("<br/>");
	}
});

//绑定[业务类型]复选框事件
_viewer.form.getItem("CB_YW_TYPE").obj.find("input").each(function(){
	var _selftype = this;
	jQuery(_selftype).unbind("change").bind("change", function() {
		if (_selftype.checked == true) {
			_viewer.form.getItem("CB_BXXS").obj.find("input").each(function(){
				var _selfbxxsok = this;
				if (_selfbxxsok.value.indexOf(_selftype.value) == 0) {
					_selfbxxsok.checked = true;
				}
			});
		} else {
			_viewer.form.getItem("CB_BXXS").obj.find("input").each(function(){
				var _selfbxxscancel = this;
				if (_selfbxxscancel.value.indexOf(_selftype.value) == 0) {
					_selfbxxscancel.checked = false;
				}
			});
		}
	});
});

//绑定[表现形式]每个复选框选中事件
_viewer.form.getItem("CB_BXXS").obj.find("input").each(function() {
	var _selfbxxs = this;
	jQuery(_selfbxxs).unbind("change").bind("change",function(){
		if (_selfbxxs.checked == true) {
			_viewer.form.getItem("CB_YW_TYPE").obj.find("input").each(function(){
				var _selftypeobj = this;
				if (_selfbxxs.value.indexOf(_selftypeobj.value) == 0) {
					_selftypeobj.checked = true;
				}
			});
		} else {
			var selfObjStarWidthVal = _selfbxxs.value.substring(0, _selfbxxs.value.indexOf("-"));
			var thisbxxsObjChecked = false;
			_viewer.form.getItem("CB_BXXS").obj.find("input").each(function() {
				var thisbxxsObjCheck = this;
				if (thisbxxsObjCheck.value.indexOf(selfObjStarWidthVal) == 0 && thisbxxsObjCheck.checked == true) {
					thisbxxsObjChecked = true;
				}
			});
			if (!thisbxxsObjChecked) {
				_viewer.form.getItem("CB_YW_TYPE").obj.find("input").each(function(){
					var thistypeObj = this;
					if (thistypeObj.value.indexOf(selfObjStarWidthVal) == 0) {
						thistypeObj.checked = false;
					}
				});
			}
		}
	});
});
//修改备注显示样式
_viewer.form.getItem("MEMO_TITLE").obj.parent().removeClass("disabled");
_viewer.form.getItem("MEMO_TITLE").obj.css({"color":"black","font-family": "宋体","font-weight":"bolder","background-color":"white","font-size":"12px"});
//隐藏[表格展现]
_viewer.form.getItem("CB_TABLE").obj.parent().hide();

 var ftlContent = _viewer.getByIdData("Ftl_Content");//数据主键
 //追加ftl
 _viewer.form.getItem("CB_TABLE").obj.parent().parent().append(ftlContent);

 var type_bxxs =_viewer.form.getItem("CB_TABLE").obj.parent().parent().find("#OA_ZH_NYCBYW_CB-TYPE-BXXS");
//注册【业务类型】清除选中事件
  _viewer.form.getItem("CB_YW_TYPE").clearChecked = function(val) {
	var _self = this;
	this._obj.find(":checkbox").each(function() {
		var me = jQuery(this);
		if(me.val()==val){
		me.attr("checked", false);
	}
	});
};
//注册【表现形式】清除选中事件
  _viewer.form.getItem("CB_BXXS").clearChecked = function(val) {
	var _self = this;
	this._obj.find(":checkbox").each(function() {
		var me = jQuery(this);
		if(me.val()==val){
		me.attr("checked", false);
	}
	});
};

 _viewer.beforeSave = function(){
 	//[业务类型]对象
 var _objType = type_bxxs.children().children().children().children().find("input[name='OA_ZH_NYCBYW-CB_YW_TYPE']").parent();
 var _objBxxs = type_bxxs.children().children().children().children().find("input[name='OA_ZH_NYCBYW-CB_BXXS']").parent();
    if(_objType.find(":checkbox[checked]").length == 0 &&_objBxxs.find(":checkbox[checked]").length ==0){
    _objType.showError("该项必须输入！");
    _objBxxs.showError("该项必须输入！");
    return false;
   } 
   return true;
 };
 
 //获取[业务类型]选中的值
 type_bxxs.children().children().children().children().find("input[name='OA_ZH_NYCBYW-CB_YW_TYPE']").each(function(){
 var _self = this;
 var me = jQuery(_self);
 jQuery(_self).unbind("change").bind("change",function(){   
 	  if(_self.checked){
 	  _viewer.form.getItem("CB_YW_TYPE").fillData(me.val());
 	  } else {
 	   _viewer.form.getItem("CB_YW_TYPE").clearChecked(me.val());
 	  }
  }); 		
 });
 
 //获取[表现形式]选中的值
 type_bxxs.children().children().children().children().find("input[name='OA_ZH_NYCBYW-CB_BXXS']").each(function(index,element){
 var bxxhSelf = this;
  var me = jQuery(bxxhSelf);
 jQuery(bxxhSelf).unbind("change").bind("change",function(){   
 	  if(bxxhSelf.checked){
 	  _viewer.form.getItem("CB_BXXS").fillData(me.val());
 	  } else {
 	   _viewer.form.getItem("CB_BXXS").clearChecked(me.val());
 	  }
  }); 
 });

//获取类型的值【CB_YW_TYPE】,同时反选
 var typeValue = _viewer.getItem("CB_YW_TYPE").getValue();
 if(typeValue != ""){
  var typeValues = typeValue.split(",");
  for(var i=0;i<typeValues.length;i++){
       jQuery("#OA_ZH_NYCBYW_CB-TYPE-BXXS").find("[value='"+typeValues[i]+"']").attr("checked","checked");
   } 	
 }
 //获取表现形式的值【CB_BXXS】，同时反选
 var bxxsValue = _viewer.getItem("CB_BXXS").getValue();
 if(bxxsValue != ""){
  var bxxsValues = bxxsValue.split(",");
  for(var i = 0; i< bxxsValues.length; i++){
       jQuery("#OA_ZH_NYCBYW_CB-TYPE-BXXS").find("[value='"+bxxsValues[i]+"']").attr("checked","checked");
  }	
 }
