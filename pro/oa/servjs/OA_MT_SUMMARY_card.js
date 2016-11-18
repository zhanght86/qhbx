var _viewer = this;

//输入框宽度为 100%
jQuery(".right").css({"width":"100%"});

//输入框宽度
jQuery(".right input").css({"width":"100%","border":"0px #CCC solid","margin-left":"0px","background-color":"#fbf3e6;"});

//将文本域的左外边框距离为0
jQuery(".ui-textarea-default").css({"margin-left":"0px","width":"100%","border":"0px #CCC solid"});

//设置文本编辑器的边框色
jQuery(".ui-form-default div.blank .ui-bigtext-default").css({"border":"1px #CCC solid"});

//去除上部[保存]按钮
jQuery("#"+_viewer.servId +"-mainTab .rhCard-btnBar").css({"margin-left":"-1000px","height":"0px"});


//重写保存之前方法
_viewer.beforeSave = function() {
	var meetingId = _viewer.getItem("MEETING_ID").getValue();
	var summaryObj = FireFly.doAct(_viewer.servId,"finds",{"_WHERE_":" and MEETING_ID = '" + meetingId + "'","_SELECT_":"SUMMARY_ID"});
	var dataList = summaryObj["_DATA_"];
	//如果存在此会议的会议纪要，则不可保存
	if (dataList.length > 0) {
		alert("此会议已有会议纪要！");
		_viewer.cardBarTipError("此会议已有会议纪要！");
		return false;
	}
};





