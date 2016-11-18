var _viewer = this;

_viewer.getBtn("close").bind("click", function() {
	_viewer.backClick();
});

_viewer.getBtn("commit").unbind("click").bind("click", function() {
	var data = {};
	var serid = _viewer.servId;
	var check = _viewer.getItem("NEWS_CHECK").getValue();
	if (check == '2') {
		_viewer.getItem("NEWS_CHECKED").setValue(5);
	} else {
		_viewer.getItem("NEWS_CHECKED").setValue(3);
	}
	_viewer.saveForm();
	var fbtime = _viewer.getItem("NEWS_TIME").getValue();
	var checker = _viewer.getItem("NEWS_CHECKER").getValue();
	var nowDate = rhDate.getCurentTime().substring(0, 11);
	var pkdata = _viewer.getPKCode();
	data['SERV_ID'] = serid;
	data['DATA_ID'] = pkdata;
	data['S_ATIME'] = fbtime;
	data['CK_DATE'] = nowDate;
	if (check == 2) {
		data['CHECKER_ID'] = "";
		data['CK_TYPE'] = 0;
		data['CK_STATE'] = 5;
		data['NEWS_CHECK'] = check;
	} else {
		data['CHECKER_ID'] = checker;
		data['CK_TYPE'] = 1;
		data['CK_STATE'] = 3;
		data['NEWS_CHECK'] = check;
	}

	FireFly.doAct("SY_COMM_NEWS_COUNT", "save", data, true, false, function() {
		alert('提交成功');
	});

	// // _viewer.saveForm();
	// _viewer.backClick();
});