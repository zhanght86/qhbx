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
	datas[UIConst.PK_KEY] = pkdata.join(",");
	for ( var i = 0; i < pkdata.length; i++) {
		var check = _viewer.grid.getRowItemValue(pkdata[i],
				"NEWS_CHECK");
		var fbtime = _viewer.grid.getRowItemValue(pkdata[i],
				"NEWS_TIME");
		var checker = _viewer.grid.getRowItemValue(pkdata[i],
				"NEWS_CHECKER");
		var nowDate = rhDate.getTime();
		datas[pkdata[i]] = check + "," + fbtime + "," + nowDate
				+ "," + checker;
		//结束一条待办		
	  var newId = pkdata[i];
	  endtodo(newId);
	}
	return datas;
	
}


_viewer.getBtn("backModify").unbind("click").bind(
		"click",
		function() {	
			var servid = _viewer.servId;
			var pkdata = _viewer.grid.getSelectPKCodes();
			if (pkdata != '') {
			 var datas= dataSplit(pkdata);
				FireFly.doAct(servid, "backModify", datas, true, false,
						function() {
							_viewer.refreshTreeAndGrid();
						});
			} else {
				alert("请选择要退回修改的数据");
			}

		});

_viewer.getBtn("pass").unbind("click").bind("click", function() {
	var servid = _viewer.servId;
	var pkdata = _viewer.grid.getSelectPKCodes();
	if (pkdata != '') {
		 var datas= dataSplit(pkdata);
		FireFly.doAct(servid, "checkPass", datas, true, false,
				function() {
					_viewer.refreshTreeAndGrid();
				});
	} else {
		alert("请选择数据");
	}

});

_viewer.getBtn("nopass").unbind("click").bind("click", function() {
	var servid = _viewer.servId;
	var pkdata = _viewer.grid.getSelectPKCodes();
	if (pkdata != '') {
		 var datas= dataSplit(pkdata);
		FireFly.doAct(servid, "checkNoPass", datas, true, false,
				function() {
					_viewer.refreshTreeAndGrid();
				});
	} else {
		alert("请选择数据");
	}

});

/* 对[操作]列添加操作链接 */
_viewer.grid.getBodyTr().each(function() {
	
	// 获取每一列的[操作]列
	var operate = jQuery(this).find("[icode='OPERATE']");
	var pCon = jQuery("<div></div>");
	var newId = jQuery(this).find("[icode='NEWS_ID']").html();
	// alert(newId);
	appendAObjs(newId,pCon);
	pCon.appendTo(operate);
});

/* 向[操作]列添加操作链接代码 */
function appendAObjs(newId,pCon){
	var newsChecked = _viewer.grid.getRowItemValue(newId,"NEWS_CHECKED");
    var auditid = _viewer.grid.getRowItemValue(newId,"AUDIT_ID");
	var edit = jQuery("<a href='javascript:void(0);'></a>").css({"margin":"0px 3px 0px 5px"});
	var debtn=jQuery("<a href='javascript:void(0);'></a>").css({"margin":"0px 3px 0px 5px"});
	var check=jQuery("<a href='javascript:void(0);'></a>").css({"margin":"0px 3px 0px 5px"});
	var checkNoPass=jQuery("<a href='javascript:void(0);'></a>").css({"margin":"0px 3px 0px 5px"});
	if (newsChecked == '3' && auditid =='') {
		edit.html("编辑").css({"color":"#008000"}).unbind("click").bind("click",function(event){
			Tab.open({
				"url" : "SY_COMM_INFOS_DEAL.card.do?pkCode="+newId,
				"tTitle" : "需要我处理的信息",
				"menuFlag" : 4
			});
		});
		debtn.html("  退回修改   ").css({"color":"#008000"}).unbind("click").bind("click",function(event){
		 
		  var servid=_viewer.servId;
		   var data={};
			data[UIConst.PK_KEY] =newId;
			var check =_viewer.grid.getRowItemValue(newId,"NEWS_CHECK");
			var fbtime=_viewer.grid.getRowItemValue(newId,"NEWS_TIME");
			var checker =_viewer.grid.getRowItemValue(newId,"NEWS_CHECKER");
			var nowDate = rhDate.getTime();
			data[newId] = check + "," + fbtime + "," + nowDate
			+ "," + checker;
	       FireFly.doAct(servid, "backModify", data, true, false,
					function() {
						//alert("xx3");
						_viewer.refreshTreeAndGrid();
					});
			//结束待办，然代办转换成已办		
			endtodo(newId);
			//操作显示已退回
			var tdContent=jQuery("#"+newId).find("[icode='OPERATE']").find("DIV").eq(0).html("已退回");
		});
		check.html("  审核通过   ").css({"color":"#008000"}).unbind("commit").bind("click",function(event){
			var servid = _viewer.servId;
			var data={};
			data[UIConst.PK_KEY] =newId;
			var check =_viewer.grid.getRowItemValue(newId,"NEWS_CHECK");
			var fbtime=_viewer.grid.getRowItemValue(newId,"NEWS_TIME");
			var checker =_viewer.grid.getRowItemValue(newId,"NEWS_CHECKER");
			var nowDate = rhDate.getTime();
			data[newId] = check + "," + fbtime + "," + nowDate
			+ "," + checker;
			FireFly.doAct(servid, "checkPass", data, true, false,
					function() {
						_viewer.refreshTreeAndGrid();
					});
			//结束待办，然代办转换成已办		
			endtodo(newId);	
		var tdContent=jQuery("#"+newId).find("[icode='OPERATE']").find("DIV").eq(0).html("已审核通过，并发布");	
		});
		checkNoPass.html("  审核不通过   ").css({"color":"#008000"}).unbind("commit").bind("click",function(event){
			var servid = _viewer.servId;
			var data={};
			data[UIConst.PK_KEY] =newId;
			var check =_viewer.grid.getRowItemValue(newId,"NEWS_CHECK");
			var fbtime=_viewer.grid.getRowItemValue(newId,"NEWS_TIME");
			var checker =_viewer.grid.getRowItemValue(newId,"NEWS_CHECKER");
			var nowDate = rhDate.getTime();
			data[newId] = check + "," + fbtime + "," + nowDate
			+ "," + checker;
			FireFly.doAct(servid, "checkNoPass", data, true, false,
					function() {
						_viewer.refreshTreeAndGrid();
					});
			//结束待办，然代办转换成已办		
			endtodo(newId);	
			var tdContent=jQuery("#"+newId).find("[icode='OPERATE']").find("DIV").eq(0).html("已做审核不通过操作");	
		});
	} else {
		
	}
	
	edit.appendTo(pCon);
	debtn.appendTo(pCon);
	check.appendTo(pCon);
	checkNoPass.appendTo(pCon);
}
