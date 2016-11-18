var _viewer = this;

_viewer.getBtn("batchCommit").unbind("click").bind("click", function() {
	var servid = _viewer.servId;
	var pkdata = _viewer.grid.getSelectPKCodes();
	if (pkdata != '') {
		var datas = {};
		datas[UIConst.PK_KEY] = pkdata.join(",");
		for (var i = 0; i < pkdata.length; i++) {
			var check = _viewer.grid.getRowItemValue(pkdata[i], "NEWS_CHECK");
			var fbtime = _viewer.grid.getRowItemValue(pkdata[i], "NEWS_TIME");
			var checker = _viewer.grid.getRowItemValue(pkdata[i],
					"NEWS_CHECKER");
			var nowDate = rhDate.getTime();
			datas[pkdata[i]] = check + "," + fbtime + "," + nowDate + ","
					+ checker;
			//发起一条待办
            var newstitle = _viewer.grid.getRowItemValue(pkdata[i],"NEWS_SUBJECT"); // 信息标题
			// var TODO_CODE = TODO_OBJECT_ID1 TODO_CATALOG TODO_URL
			//var newsid = _viewer.getItem("NEWS_ID").getValue();;
			var senduser = _viewer.grid.getRowItemValue(pkdata[i],"NEWS_USER");
			if (check == '1') {
				var data = {};
				data["OWNER_CODE"] = checker; // 代办用户
				data["SEND_USER_CODE"] = senduser;
				data["TODO_TITLE"] = newstitle;
				data["TODO_CODE"] = servid;
				data["SERV_ID"] = "SY_COMM_INFOS_DEAL";
				data["TODO_OBJECT_ID1"] = pkdata[i];
				data["TODO_CATALOG"] = 1;
				data["TODO_URL"] = "SY_COMM_INFOS_DEAL.byid.do?data={_PK_:"
						+ pkdata[i] + "}";
				FireFly.doAct("SY_COMM_TODO", "addToDo", data);
			}
		}
		FireFly.doAct(servid, "batchCheck", datas, true, false, function() {
					_viewer.refreshTreeAndGrid();
				});
	} else {

		alert("请选择要提交的数据");
	}
});
_viewer.getBtn("post").unbind("click").bind("click", function() {
	var servid = _viewer.servId;
	var pkdata = _viewer.grid.getSelectPKCodes();
	if (pkdata != '') {
		var datas = {};
		datas[UIConst.PK_KEY] = pkdata.join(",");
		for (var i = 0; i < pkdata.length; i++) {
			var check = _viewer.grid.getRowItemValue(pkdata[i], "NEWS_CHECK");
			var fbtime = _viewer.grid.getRowItemValue(pkdata[i], "NEWS_TIME");
			var checker = _viewer.grid.getRowItemValue(pkdata[i],
					"NEWS_CHECKER");
			var nowDate = rhDate.getTime();
			datas[pkdata[i]] = check + "," + fbtime + "," + nowDate + ","
					+ checker;
		}
		FireFly.doAct(servid, "batchPost", datas, true, false, function() {
					_viewer.refreshTreeAndGrid();
				});
	} else {

		alert("请选择要发布的信息");
	}
});
/* 对[操作]列添加操作链接 */
_viewer.grid.getBodyTr().each(function() {

			// 获取每一列的[操作]列
			var operate = jQuery(this).find("[icode='OPERATE']");

			var pCon = jQuery("<div></div>");
			var newId = jQuery(this).find("[icode='NEWS_ID']").html();
			// alert(newId);
			appendAObjs(newId, pCon);
			pCon.appendTo(operate);
		});

/* 向[操作]列添加操作链接代码 */
function appendAObjs(newId, pCon) {
	var newsChecked = _viewer.grid.getRowItemValue(newId, "NEWS_CHECKED");
    var check = _viewer.grid.getRowItemValue(newId, "NEWS_CHECK");
    var  sflag =_viewer.grid.getRowItemValue(newId,"S_FLAG");
	var edit = jQuery("<a href='javascript:void(0);'></a>").css({"margin" : "0px 3px 0px 5px"});
	var debtn = jQuery("<a href='javascript:void(0);'></a>").css({"margin" : "0px 3px 0px 5px"});
	var checkhtml = jQuery("<a href='javascript:void(0);'></a>").css({"margin" : "0px 3px 0px 5px"});
	if (newsChecked == '1' || newsChecked == '2') {
		edit.html("编辑").css({"color" : "#008000"}).unbind("click").bind("click", function(event) {
					Tab.open({
								"url" : "SY_COMM_INFOS.card.do?pkCode=" + newId,
								"tTitle" : "我填报的信息",
								"menuFlag" : 4,
								 "readOnly" :false
							});
				});
		debtn.html("  删除   ").css({"color" : "#008000"}).unbind("click").bind("click", function(event) {
					var servid = _viewer.servId;
					var data = {};
					data[UIConst.PK_KEY] = newId;
					data['serv'] = servid;
					FireFly.doAct(servid, "delete", data, true, false,
							function() {
								_viewer.refreshTreeAndGrid();
							});
				});
	if(check =='1'){		
		    checkhtml.html("  提交审核   ").css({"color" : "0000ff"}).unbind("click").bind("click", function(event) {
			var servid = _viewer.servId;
			var data = {};
			data[UIConst.PK_KEY] = newId;	
			var fbtime = _viewer.grid.getRowItemValue(newId, "NEWS_TIME");
			var checker = _viewer.grid.getRowItemValue(newId, "NEWS_CHECKER");
			var nowDate = rhDate.getCurentTime().substring(0, 11);
			data[newId] = check + "," + fbtime + "," + nowDate + "," + checker;
			//发起一条待办
			var newstitle = _viewer.grid.getRowItemValue(newId,"NEWS_SUBJECT"); // 信息标题
			var senduser = _viewer.grid.getRowItemValue(newId,"NEWS_USER");
				var param = {};
				param["OWNER_CODE"] = checker; // 代办用户
				param["SEND_USER_CODE"] = senduser;
				param["TODO_TITLE"] = newstitle;
				param["TODO_CODE"] = servid;
				param["SERV_ID"] = "SY_COMM_INFOS_DEAL";
				param["TODO_OBJECT_ID1"] = newId;
				param["TODO_CATALOG"] = 1;
				param["TODO_URL"] = "SY_COMM_INFOS_DEAL.byid.do?data={_PK_:"
						+ newId + "}";
			   FireFly.doAct("SY_COMM_TODO", "addToDo", param);	
			   FireFly.doAct(servid, "batchCheck", data, true, false, function() {
						_viewer.refreshTreeAndGrid();
					});
			
		}); 
		}
		else {
		checkhtml.html("  发布   ").css({"color" : "0000ff"}).unbind("click").bind("click", function(event) {
			var servid = _viewer.servId;
			var datapost = {};
			datapost[UIConst.PK_KEY] = newId;	
			var fbtime = _viewer.grid.getRowItemValue(newId, "NEWS_TIME");
			var checker = _viewer.grid.getRowItemValue(newId, "NEWS_CHECKER");
			var nowDate = rhDate.getTime();
			datapost[newId] = check + "," + fbtime + "," + nowDate + "," + checker;		
		   FireFly.doAct(servid,"batchPost", datapost, true, false, function() {
						_viewer.refreshTreeAndGrid();
					});		
				}); 
		
				
		}
	} else if(newsChecked == '4') {
   		debtn.html("  删除   ").css({"color" : "#008000"}).unbind("click").bind("click", function(event) {
					var servid = _viewer.servId;
					var data = {};
					data[UIConst.PK_KEY] = newId;
					data['serv'] = servid;
					FireFly.doAct(servid, "delete", data, true, false,
							function() {
								_viewer.refreshTreeAndGrid();
							});

				});
		
	} else if (newsChecked == '6') {
	   //出编辑按钮
	   if(check =='2'){
	   		edit.html("编辑").css({	"color" : "#008000"}).unbind("click").bind("click", function(event) {
					Tab.open({
								"url" : "SY_COMM_INFOS.card.do?pkCode=" + newId,
								"tTitle" : "我填报的信息",
								"menuFlag" : 4
							});
				});
	   }	    
	   		   debtn.html("  删除   ").css({"color" : "#008000"}).unbind("click").bind("click", function(event) {
					var servid = _viewer.servId;
					var data = {};
					data[UIConst.PK_KEY] = newId;
					data['serv'] = servid;
					FireFly.doAct(servid, "delete", data, true, false,
							function() {
								_viewer.refreshTreeAndGrid();
							});

				});	
		if(sflag=='1'){		//禁用
		   	checkhtml.html("  禁用   ").css({"color" : "0000ff"}).unbind("click").bind("click", function(event) {
			var servid = _viewer.servId;
			var dataflag = {};
			dataflag[UIConst.PK_KEY] = newId;
			dataflag["S_FLAG"] =2;
		    FireFly.doAct(servid,"save", dataflag, true, false, function() {
						_viewer.refreshTreeAndGrid();
					});		
				}); 
		} else {
		  checkhtml.html("  启用   ").css({"color" : "0000ff"}).unbind("click").bind("click", function(event) {
			var servid = _viewer.servId;
			var nowdate = rhDate.getTime();
			var dataflag = {};
			dataflag[UIConst.PK_KEY] = newId;
			dataflag["S_FLAG"] =1;
			dataflag["NEWS_TIME"] = nowdate;
		    FireFly.doAct(servid,"save", dataflag, true, false, function() {
						_viewer.refreshTreeAndGrid();
					});		
				}); 
		}	
	}

	edit.appendTo(pCon);
	debtn.appendTo(pCon);
	checkhtml.appendTo(pCon);
}