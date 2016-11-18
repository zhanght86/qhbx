var _viewer = this;

//重写查询选择的回调函数
_viewer.getItem("OFFICE_NAME")._choose.unbind("click").bind("click",function(event){
	var cmpy =System.getVar("@ODEPT_CODE@");
	var configStr = "OA_OFF_QUOTATION,{" +
							   "'SOURCE':'QUOTATION_ID~OFFICE_NAME~OFFICE_UNITE~OFFICE_PRICE~OFFICE_BRAND~OFFICE_MODEL~OFFICE_SPEC~OFFICE_TYPE~OFFICE_SELLER'," +
							   "'TARGET':'OFFICE_ID~OFFICE_NAME~OFFICE_UNITE~OFFICE_PRICE~OFFICE_BRAND~~~OFFICE_TYPE~','TYPE':'single'," +
							   "'EXTWHERE':\" and 1=1 and S_ODEPT = '" + cmpy + "' and PUBLIC_FLAG = '1'\"" +
							   "}";
	var options = {
			"config":configStr,
			"searchFlag":true,
			"replaceCallBack":function(idArray){
				chooseBackData(idArray);
			}
		};
	var queryView = new rh.vi.rhSelectListView(options);
	queryView.show(event);
});

//给[数量]元素添加键盘监听事件
jQuery("#OA_OFF_OUTSTORAGE_NO_FLOW-OFFICE_STOCK").keyup(function(event){
	getInStroageCount(this);
});

//给[数量]元素添加鼠标事件
jQuery("#OA_OFF_OUTSTORAGE_NO_FLOW-OFFICE_STOCK").bind("blur",function(event){
	getInStroageCount(this);
});

//给[单价]元素添加键盘监听事件
jQuery("#OA_OFF_OUTSTORAGE_NO_FLOW-OFFICE_PRICE").keyup(function(event){
	var amount = _viewer.getItem("OFFICE_STOCK").getValue() * jQuery(this).val();
	_viewer.getItem("OFFICE_AMOUNT").setValue(parseFloat(amount).toFixed(2));
});

//字典表联动
_viewer.getItem("GET_USER")._choose.unbind("click").bind("click",function(event){
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

//用户字典弹出选择回调的方法
function userDictCallBack(idArray,nameArray) {
	var param = {};
	param["USER_CODE"] = idArray.join(",");
	param["USER_NAME"] = nameArray.join(",");
	_viewer.getItem("GET_USER").setValue(param.USER_CODE);
	_viewer.getItem("GET_USER").setText(param.USER_NAME);
	var valObj = FireFly.doAct("OA_OFF_OUTSTORAGE","linkDic",{"USER_CODE":param.USER_CODE,"SERVID":"SY_ORG_USER"});
	_viewer.getItem("GET_DEPT").setValue(valObj.DEPT_CODE);
	_viewer.getItem("GET_DEPT").setText(valObj.DEPT_NAME);
}

//查询选择回调函数
function chooseBackData(idArray){
	_viewer.getItem("OFFICE_ID").setValue(idArray["QUOTATION_ID"]);
	_viewer.getItem("OFFICE_NAME").setValue(idArray["OFFICE_NAME"]);
	_viewer.getItem("OFFICE_UNITE").setValue(idArray["OFFICE_UNITE"]);
	_viewer.getItem("OFFICE_PRICE").setValue(idArray["OFFICE_PRICE"]);
	_viewer.getItem("OFFICE_BRAND").setValue(idArray["OFFICE_BRAND"]);
	_viewer.getItem("OFFICE_TYPE").setValue(idArray["OFFICE_TYPE"]);
	
	initAmount();
}

//当单价变化时，总金额也要变化
function initAmount(){
	var amount = _viewer.getItem("OFFICE_PRICE").getValue() *  _viewer.getItem("OFFICE_STOCK").getValue();
	_viewer.getItem("OFFICE_AMOUNT").setValue(parseFloat(amount).toFixed(2));
}

//获取库存中某个物品的数量，在出库时候进行比对，若出库数量大于库存数量，则显示库存最大数量
function getInStroageCount(thisObj){
	var outBean = FireFly.doAct("OA_OFF_OUTSTORAGE","inStorageCount",{"OFFICE_ID":_viewer.getItem("OFFICE_ID").getValue()});
	var count = parseInt(outBean["_OKCOUNT_"]);
	var number = parseInt(jQuery(thisObj).val());
	number == 0 ? jQuery(thisObj).val("") : "";
	if (count < number) {
		_viewer.cardBarTipError("出库数量大于库存数量");
		jQuery(thisObj).val("");
	}
	var amount = _viewer.getItem("OFFICE_PRICE").getValue() * jQuery(thisObj).val();
	_viewer.getItem("OFFICE_AMOUNT").setValue(parseFloat(amount).toFixed(2));
}




