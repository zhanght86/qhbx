var _viewer = this;
_viewer.getBtn("backModify").unbind("click").bind(
		"click",
		function() {
			var servid = _viewer.servId;
			var pkdata = _viewer.grid.getSelectPKCodes();
			if (pkdata != '') {
				var datas = {};
				datas[UIConst.PK_KEY] = pkdata.join(",");
				for ( var i = 0; i < pkdata.length; i++) {
					var check = _viewer.grid.getRowItemValue(pkdata[i],
							"NEWS_CHECK");
					var fbtime = _viewer.grid.getRowItemValue(pkdata[i],
							"NEWS_TIME");
					var checker = _viewer.grid.getRowItemValue(pkdata[i],
							"NEWS_CHECKER");
					var nowDate = rhDate.getCurentTime().substring(0, 11);
					datas[pkdata[i]] = check + "," + fbtime + "," + nowDate
							+ "," + checker;
				}
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
		var datas = {};
		datas[UIConst.PK_KEY] = pkdata.join(",");
		for ( var i = 0; i < pkdata.length; i++) {
			var check = _viewer.grid.getRowItemValue(pkdata[i],
					"NEWS_CHECK");
			var fbtime = _viewer.grid.getRowItemValue(pkdata[i],
					"NEWS_TIME");
			var checker = _viewer.grid.getRowItemValue(pkdata[i],
					"NEWS_CHECKER");
			var nowDate = rhDate.getCurentTime().substring(0, 11);
			datas[pkdata[i]] = check + "," + fbtime + "," + nowDate
					+ "," + checker;
		}
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
		var datas = {};
		datas[UIConst.PK_KEY] = pkdata.join(",");
		for ( var i = 0; i < pkdata.length; i++) {
			var check = _viewer.grid.getRowItemValue(pkdata[i],
					"NEWS_CHECK");
			var fbtime = _viewer.grid.getRowItemValue(pkdata[i],
					"NEWS_TIME");
			var checker = _viewer.grid.getRowItemValue(pkdata[i],
					"NEWS_CHECKER");
			var nowDate = rhDate.getCurentTime().substring(0, 11);
			datas[pkdata[i]] = check + "," + fbtime + "," + nowDate
					+ "," + checker;
		}
		FireFly.doAct(servid, "checkNoPass", datas, true, false,
				function() {
					_viewer.refreshTreeAndGrid();
				});
	} else {
		alert("请选择数据");
	}

});
