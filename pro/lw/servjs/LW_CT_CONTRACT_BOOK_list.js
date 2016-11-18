(function(_viewer){
	var servId = _viewer.servId;
	var authorizeBtn = _viewer.getBtn("authorize");
	if (authorizeBtn) {
		authorizeBtn.unbind("click").click(function(event){
			var pks = _viewer.grid.getSelectPKCodes();
			if (pks.length > 0) {
				var ct = {};
				var len = pks.length;
				for (var i = 0; i < len; i++) {
					var ctName = _viewer.grid.getRowItemValue(pks[i], "CT_NAME");
					ct[pks[i]] = ctName;
				}
				buildTree(event, ct);
			} else {
				alert("请选择要授权的合同");
			}
		});
	}
	
	/**
	 * 构建部门用户树
	 */
	function buildTree(event, ct) {
		var options = {
			"itemCode" : "",
			"config" : "SY_ORG_TDEPT_USER",
			"hide" : "explode",
			"show" : "blind",
			"replaceCallBack" : function(userId, value) {
				addAuthority(ct, userId);
			}
		};
		var dictView = new rh.vi.rhDictTreeView(options);
		dictView.show(event);
	};
	
	/**
	 * 添加授权记录
	 */
	function addAuthority(ct, userId) {
		var datas = [];
		for (var ctId in ct) {
			datas.push({"CT_ID":ctId,"CT_NAME":ct[ctId],"AUTH_OWNER":userId[0]});
		}
		FireFly.doAct("LW_CT_CONTRACT_BOOK", "saveAuthority", {"BATCHDATAS":jQuery.toJSON(datas)}, true, true);
	}
})(this);