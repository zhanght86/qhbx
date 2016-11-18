var _viewer = this;

//隐藏 [保存] 按钮
_viewer.getBtn("save").remove();

//备注显示样式
_viewer.form.getItem("MEMO_TITLE").obj.parent().removeClass("disabled").css({"background":"url('')"});
_viewer.form.getItem("MEMO_TITLE").obj.css({"color":"black","font-weight":"bolder","background-color":"white","font-size":"14px"});

var actFlag = _viewer.getParams()["ACT_FLAG"]; //是各部门或者是本部门导出
if (actFlag == "EACH") { //各部门导出
	_viewer.getBtn("this_count").remove();
	//给 [统计] 按钮绑定方法
	_viewer.getBtn("each_count").unbind("click").bind("click", function(){
		outPutXdocPage("EACH");
	});
} else if (actFlag == "THIS") { //本部门导出
	_viewer.getBtn("each_count").remove();
	_viewer.getItem("CHOOSE_DEPT").getContainer().remove();
	_viewer.getItem("MEMO_TITLE").getContainer().remove();
	_viewer.setTitle("本部门办公用品月统计");
	//给 [统计] 按钮绑定方法
	_viewer.getBtn("this_count").unbind("click").bind("click", function(){
		outPutXdocPage("THIS");
	});
}

rhDate.compareTwoDate(_viewer.getItem("BEGIN_TIME").obj, _viewer.getItem("END_TIME").obj, "yyyy-MM"); //绑定开始时间和结束时间联动

//调用xdoc文件方法
function outPutXdocPage(act_flag){
	var beginTime = _viewer.getItem("BEGIN_TIME").getValue(); //开始时间
	var endTime = _viewer.getItem("END_TIME").getValue(); //结束时间
	var fileName = ""; //文件显示名称
	var applyTypeText = "[" + _viewer.getItem("APPLY_TYPE").getText() + "]"; //领用类型
	//必填字段没有值，直接返回
	if (!beforeCount(beginTime, endTime, _viewer.getItem("APPLY_TYPE").getValue())) {
		_viewer.cardBarTipError("校验未通过");
		return;
	} else {
		var chooseDept = _viewer.getItem("CHOOSE_DEPT"); //选择部门对象
		//统计每一个部门
		if (act_flag == "EACH" && ("" == (chooseDept.getValue() || ""))) {
			var titleStr = chooseDept.getText() + formatDateStr(beginTime, endTime) + applyTypeText + "办公用品领用统计";
			var data = {"FULL_NAME":titleStr, 
						"FILE_NAME":titleStr, 
						"BEGIN_TIME":beginTime,
						"END_TIME":endTime, 
						"TYPE_CODE":_viewer.getItem("APPLY_TYPE").getValue(),
						"CMPY_CODE":System.getVar("@CMPY_CODE@"), 
						"PDEPT_CODE":System.getVar("@ODEPT_CODE@"),
						"ODEPT_CODE":System.getVar("@ODEPT_CODE@"),
						"XDOC_KEY":"OA_OFF_EACH_DEPT_COUNT"
					   };
			var out = FireFly.doAct("SY_XDOC_FALG","isExiteData",data);
			if (out["_MSG_"].indexOf("ERROR,") >= 0) {
				_viewer.cardBarTipError(out["_MSG_"].replace("ERROR,",""));
			} else {
				//FireFly.doFormAct("SY_XDOC_FALG", "run",data);
				var paramBean = FireFly.doAct("SY_XDOC_FALG","initData",data);
				runXdoc(paramBean["data"]);
			}
		//统计某个或者本部门
		} else {
			var chooseDeptVal = chooseDept.getValue() || "";
			var chooseDeptText = chooseDept.getText() || "";
			//本部门
			if ("" == chooseDeptVal) {
				chooseDeptVal = System.getVar("@TDEPT_CODE@");
				chooseDeptText = System.getVar("@TDEPT_NAME@");
			}
			var titleStr = chooseDeptText + formatDateStr(beginTime, endTime) + applyTypeText + "办公用品领用统计";
			var data = {"FULL_NAME":titleStr, 
						"FILE_NAME":titleStr, "BEGIN_TIME":beginTime,
						"END_TIME":endTime, 
						"TYPE_CODE":_viewer.getItem("APPLY_TYPE").getValue(),
						"CMPY_CODE":System.getVar("@CMPY_CODE@"), 
						"TDEPT_CODE":chooseDeptVal,
						"ODEPT_CODE":System.getVar("@ODEPT_CODE@"),
						"XDOC_KEY":"OA_OFF_WHICH_DEPT_COUNT"
					   };
			var out = FireFly.doAct("SY_XDOC_FALG","isExiteData",data);
			if (out["_MSG_"].indexOf("ERROR,") >= 0) {
				_viewer.cardBarTipError(out["_MSG_"].replace("ERROR,",""));
			} else {
				//FireFly.doFormAct("SY_XDOC_FALG", "run",data);
				var paramBean = FireFly.doAct("SY_XDOC_FALG","initData",data);
				runXdoc(paramBean["data"]);
			}
		}
	}
}

//统计之前判断必填字段是否已填写
function beforeCount(beginTime, endTime, applyTypeText){
	if (beginTime == "" || endTime == "" || applyTypeText == "") {
		return false;
	}
	return true;
}

//获取显示某段时间字符串
function formatDateStr(beginTime, endTime){
	if (beginTime == endTime) {
		return beginTime;
	} else {
		return beginTime + "至" + endTime;
	}
}

function runXdoc(data){
	XDoc.server = data["URL"];
	var jdbcName = data["jdbc_name"] || "rhoajdbc/rhoa";
	data["jdbc_name"] = jdbcName;
	data["_format"] = data["format"] || "docx";
	data["_filename"] = data["fullName"] || "data";
	XDoc.run(data["filePath"], data, "newwindow");
}