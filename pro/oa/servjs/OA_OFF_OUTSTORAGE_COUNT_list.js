var _viewer = this;

jQuery("#off-instorage-count-div").remove();

_viewer._searchText.remove(); //去除查询条件输入框
_viewer._advancedSearchBtn.remove(); //去除高级查询按钮
_viewer._searchSel.remove(); //去除查询条件的下拉列表

_viewer.getSearchBtn().unbind("click").bind("click",function(){
	//获取日期输入框的值
	var leftDate = dateInpLeft.val() || dateInpRight.val();
	var rightDate = dateInpRight.val() || dateInpLeft.val();
	//页面刷新时保留原页面参数
	var dept_code = jQuery("#OA_OFF_OUTSTORAGE_COUNT-search_code").val();
	var dept_name = jQuery("#OA_OFF_OUTSTORAGE_COUNT-search_name").val();
	_viewer.setParParams({"leftDate":leftDate,"rightDate":rightDate,"deptCode":dept_code,"deptName":dept_name});
	var odeptCode = System.getVar("@ODEPT_CODE@");
	var extWhere = "";
	if (_viewer.getParams()["DEPT_CODE_FLAG"] == "LOCAL") { //本部门统计
		var tdeptCode = System.getVar("@TDEPT_CODE@"); //本部门编号
		//页面初始化
		extWhere = " and substr(S_ATIME,1,7) between '" + leftDate + "' and '" + rightDate + "' and S_ODEPT = '" + odeptCode + "' and S_TDEPT = '" + tdeptCode + "'";
	} else {
		//查询按钮查询
		extWhere = " and substr(S_ATIME,1,7) between '" + leftDate + "' and '" + rightDate + "' and S_ODEPT = '" + odeptCode + "' and S_TDEPT = '" + dept_code + "'";
	}
	_viewer.setSearchWhereAndRefresh(extWhere,true); //调用查询条件
});

//获取父页面参数
var leftDateInp = System.getParParams()["leftDate"] || compareNewDate();
var rightDateInp = System.getParParams()["rightDate"] || "";

//添加日期选择控件，添加部门查询
var dateDiv = jQuery("<div style='overflow:auto;' id='off-instorage-count-div'></div>").appendTo(jQuery(".rhGrid-btnBar"));

//日期选择查询[右边]
var dateInpRight = jQuery("<input id='off-instorage-datepicker-right' class='Wdate ui-date-default' type='text' style='float:right;' value = ''/>");
dateInpRight.css({"max-width" : "100px","cursor" : "pointer","border":"1px solid #91BDEA","margin":"0px 10px 0px 10px"}).val(rightDateInp);
dateInpRight.attr("readonly",true).appendTo(dateDiv);

jQuery("<span style='float:right;dsplay:block;position:relative;top:3px;'>--</span>").appendTo(dateDiv);

//日期选择查询[左边]
var dateInpLeft = jQuery("<input id='off-instorage-datepicker-left' class='Wdate ui-date-default' type='text' style='float:right;' value = ''/>");
dateInpLeft.css({"max-width" : "100px","cursor" : "pointer","border":"1px solid #91BDEA","margin":"0px 10px 0px 0px"}).val(leftDateInp);
dateInpLeft.attr("readonly",true).appendTo(dateDiv);

dateInpLeft.bind("focus", function() { //给日期控件绑定事件
	WdatePicker({
		onpicked : function() {
			dateInpLeft.blur();
		},
		dateFmt : 'yyyy-MM',
		maxDate : '%y-%M'
	});
});

dateInpRight.bind("focus", function() { //给日期控件绑定事件
	WdatePicker({
		onpicked : function() {
			dateInpRight.blur();
		},
		dateFmt : 'yyyy-MM',
		maxDate : '%y-%M'
	});
});

if (_viewer.getParams()["DEPT_CODE_FLAG"] == "EACH") { //各部门统计
	//对导出按钮管理
	_viewer.getBtn("expLocal").remove();
	_viewer.getBtn("expEach").unbind("click").bind("click",function(){
		var data = {"beforeDate":dateInpLeft.val(),"afterDate":(dateInpRight.val() || dateInpLeft.val()),"format":"xls",
					"filePath":"oa/xdoc/oa_dept_each.xdoc","cmpy_code":System.getVar("@CMPY_CODE@"),
					"odept_code":System.getVar("@ODEPT_CODE@"),
					"fileName":dateInpLeft.val() + "—" + (dateInpRight.val() || dateInpLeft.val()) + "各部门月度办公用品领用统计"
	  			   }
		//填充数据源配置
		data = jQuery.extend(data, jQuery.parseJSON((System.getVar("@C_SY_XDOC_JDBC_CONF@") || {})));
		FireFly.doFormAct("SY_COMM_OUTPUT_XDOC", "outputXdoc",data);
	});
	
	var dept_code = System.getParParams()["deptName"] || System.getVar("@TDEPT_NAME@"); 
	
	//添加部门查询框
	var hiddenInpVal = jQuery("<input type='hidden' value='' id='OA_OFF_OUTSTORAGE_COUNT-search_code'/>")
											.val(System.getParParams()["deptCode"] || System.getVar("@TDEPT_CODE@")).appendTo(dateDiv); 
	var dateInpSearch = jQuery("<input id='OA_OFF_OUTSTORAGE_COUNT-search_name' type='text' style='float:right;height:27px;' value = ''/>");
	dateInpSearch.css({"max-width" : "120px","cursor" : "pointer","border":"1px solid #91BDEA","margin":"0px 10px 0px 10px"})
											.val(System.getParParams()["deptName"] || System.getVar("@TDEPT_NAME@")).addClass("ui-dict-default");
	dateInpSearch.unbind("click").bind("click",function(){
		//字典表联动
		var cmpy =System.getVar("@CMPY_CODE@");
			//1.构造树形选择参数
			//此部分参数说明可参照说明文档的【树形选择】配置说明
		var configStr = 	"SY_ORG_DEPT,{'TYPE':'single'," +
							"'EXTWHERE':' and 1=1 and CMPY_CODE = '"+ cmpy +"'," +
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
	dateInpSearch.attr("readonly",true).appendTo(dateDiv);
	
} else {
	//对导出按钮管理
	_viewer.getBtn("expEach").remove();
	_viewer.getBtn("expLocal").unbind("click").bind("click",function(){
		var data = {"dept_code":System.getVar("@DEPT_CODE@"),"dept_name":System.getVar("@DEPT_NAME@"),
					"beforeDate":dateInpLeft.val(),"afterDate":(dateInpRight.val() || dateInpLeft.val()),"format":"xls",
					"filePath":"oa/xdoc/oa_dept_local.xdoc","fileName":System.getVar("@DEPT_NAME@") + dateInpLeft.val() 
					+ "—" + (dateInpRight.val() || dateInpLeft.val()) + "月度办公用品领用统计"
				  }
		//填充数据源配置
		data = jQuery.extend(data, jQuery.parseJSON((System.getVar("@C_SY_XDOC_JDBC_CONF@") || {})));
		FireFly.doFormAct("SY_COMM_OUTPUT_XDOC", "outputXdoc",data);
	});
}

//格式化数字
_viewer.grid.getBodyTr().each(function() {
	var officeAmount = jQuery(this).find("[icode='OFFICE_AMOUNT']").html();
	jQuery(this).find("[icode='OFFICE_AMOUNT']").html(parseFloat(officeAmount).toFixed(2));
});

jQuery(document).ready(function(){
	jQuery("<tr class='tBody-tr tBody-trOdd' id='' style = 'height:30px;font-weight:bold;background-color:#DDD;'> " 
	+ " 	<td class='rhGrid-td-center ' icode='COUNT' style='text-align:right;' colspan = '11'>统计总额</td>"
	+ " 	<td class='rhGrid-td-center ' icode='COUNT' style='text-align:right;' colspan = '1'>" + parseFloat(_viewer.getListData()["COUNT"]).toFixed(2) + "</td> "
	+ "</tr>").appendTo(_viewer.grid.getTable());
});

//系统日期
function compareNewDate() {
	var date = new Date();
	var year = date.getFullYear();
	var month = date.getMonth() + 1 + "";
	if (month.length == 1) {
		month = "0" + month;
	}
	var newDate = year + "-" + month;
	return newDate;
}

//用户字典弹出选择回调的方法
function userDictCallBack(idArray,nameArray) {
	var param = {};
	param["DEPT_CODE"] = idArray.join(",");
	param["DEPT_NAME"] = nameArray.join(",");
	jQuery("#OA_OFF_OUTSTORAGE_COUNT-search_code").val(param["DEPT_CODE"]);
	jQuery("#OA_OFF_OUTSTORAGE_COUNT-search_name").val(param["DEPT_NAME"]);
}