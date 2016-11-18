var _viewer = this;
/* 结束一条待办*/
function endtodo(newId){
			var param = {};
			param["TODO_OBJECT_ID1"] = newId;
			var outBean=FireFly.doAct("SY_COMM_TODO","finds",param);
			var out =outBean["_DATA_"];
			for(var i = 0;i<out.length;i++) {
			 var todoid ={};
			 todoid["_PK_"]=out[i]["TODO_ID"];
			 FireFly.doAct("SY_COMM_TODO","endToDo",todoid);
			}
}
/*数据拼装*/
function dataSplit(pkdata){
	var datas = {};
	   datas[UIConst.PK_KEY] = pkdata;
		var check =_viewer.getItem("NEWS_CHECK").getValue();
		var fbtime =_viewer.getItem("NEWS_TIME").getValue();
		var checker =_viewer.getItem("NEWS_CHECKER").getValue();
		var nowDate = rhDate.getTime();
		datas[pkdata] = check + "," + fbtime + "," + nowDate
				+ "," + checker;
		//结束一条待办		
		var newId =pkdata;		
		 endtodo(newId);		
	return datas;
	
}
_viewer.getBtn("backModifyCard").unbind("click").bind(
		"click",
		function() {	
			var servid = _viewer.servId;
			var pkdata = _viewer.getPKCode();;
			if (pkdata != '') {
			 var datas= dataSplit(pkdata);
				FireFly.doAct(servid, "backModify", datas, true, false,
						function() {
					_viewer.setParentRefresh();
				   _viewer.backClick();
				});
			} else {		
				alert("获取信息出错");
			}

		});

_viewer.getBtn("passCard").unbind("click").bind("click", function() {
	var servid = _viewer.servId;
	var pkdata = _viewer.getPKCode();
	if (pkdata != '') {
		 var datas= dataSplit(pkdata);
		FireFly.doAct(servid, "checkPass", datas, true, false,
				function() {
					_viewer.setParentRefresh();
					_viewer.backClick();
				});
	} else {
		alert("获取信息出错");
	}

});

_viewer.getBtn("nopassCard").unbind("click").bind("click", function() {
	var servid = _viewer.servId;
	var pkdata = _viewer.getPKCode();
	if (pkdata != '') {
		 var datas= dataSplit(pkdata);
		FireFly.doAct(servid, "checkNoPass", datas, true, false,
					function() {
					_viewer.setParentRefresh();
					_viewer.backClick();
				});
	} else {
		alert("获取信息出错");
	}

});
