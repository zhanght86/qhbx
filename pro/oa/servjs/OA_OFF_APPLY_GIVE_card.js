var _viewer = this;

var param = _viewer.params;

_viewer.getItem("GIVE_DEPT").setValue(param["DEPT_CODE"]);//字典类型字段的code赋值
_viewer.getItem("GIVE_DEPT").setText(param["DEPT_NAME"]);//字典类型字段的name赋值

_viewer.getItem("GIVE_USER").setValue(param["USER_CODE"]);//字典类型字段的code赋值
_viewer.getItem("GIVE_USER").setText(param["USER_NAME"]);//字典类型字段的name赋值

_viewer.getItem("GIVE_DATE").setValue(param["NEW_DATE"]);

//字典表联动
_viewer.getItem("GIVE_USER")._choose.unbind("click").bind("click",function(event){
	var cmpy =System.getVar("@CMPY_CODE@");
		//1.构造树形选择参数
		//此部分参数说明可参照说明文档的【树形选择】配置说明
	var configStr = 	"SY_ORG_DEPT_USER,{'TYPE':'single'," +
						"'EXTWHERE':' and 1=1 and CMPY_CODE = '"+ cmpy +"' and (DEPT_CODE in (select b.dept_code from SY_ORG_USER b group by b.dept_code) " +
						"or DEPT_CODE ='' or DEPT_CODE in (select c.dept_pcode from SY_ORG_DEPT c group by c.dept_pcode))'," +
						"'rtnLeaf':true,'extendDicSetting':{'rhexpand':false,'cascadecheck':true,'checkParent':true,'childOnly':true}}";
		var options = {
			"config" :configStr,
			"hide" : "explode",
			"show" : "blind",
			"replaceCallBack":function(idArray,nameArray){//回调，idArray为选中记录的相应字段的数组集合
			 userDictCallBack(idArray,nameArray);
		}
	};
	
	//2.显示树形
	var dictView = new rh.vi.rhDictTreeView(options);
	dictView.show(event);
});

//重新绑定[保存]按钮
_viewer.getBtn("save").unbind("click").bind("click",function(){
	var outBean = FireFly.doAct("OA_OFF_OUTSTORAGE","saveApply",{"APPLY_ID":_viewer.getItem("APPLY_ID").getValue()});
	if (outBean["OUTSTORAGE_STATUS"] == "2") { //更新成功，将发放状态修改成已发放
		_viewer.refresh();//刷新列表
		_viewer.cardBarTip("出库成功");
	}
});

//用户字典弹出选择回调的方法
function userDictCallBack(idArray,nameArray) {
	var param = {};
	param["USER_CODE"] = idArray.join(",");
	param["USER_NAME"] = nameArray.join(",");
	_viewer.getItem("GIVE_USER").setValue(param.USER_CODE);
	_viewer.getItem("GIVE_USER").setText(param.USER_NAME);
	var valObj = FireFly.doAct("OA_OFF_OUTSTORAGE","linkDic",{"USER_CODE":param.USER_CODE,"SERVID":"SY_ORG_USER"});
	_viewer.getItem("GIVE_DEPT").setValue(valObj.DEPT_CODE);
	_viewer.getItem("GIVE_DEPT").setText(valObj.DEPT_NAME);
}





