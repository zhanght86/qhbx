/** 单个领导日程 */
GLOBAL.namespace("mb.vi");
/*待解决问题：
 * 
 */
 
mb.vi.oneLdrActionDetail = function(options) {
	var defaults = {
		"sId":""//服务ID
	};
	this.opts = jQuery.extend(defaults,options);
	this.servId = this.opts.sId;
};



mb.vi.oneLdrActionDetail.prototype.show = function() {
	var actions = this.opts.data;
	this._order(actions);
	this._afterLoad();
};


/*
 * 构建列表页面布局
 */
mb.vi.oneLdrActionDetail.prototype._order = function(actions) {
	var _self = this;
    for(var i=0;i<actions.length-1;i++){
    	for(var j=0;j<actions.length-1-i;j++){	
    		var timeDiff = rhDate.doDateDiff("S",actions[j]["BEGIN_TIME"],actions[j+1]["BEGIN_TIME"],0);
        	if(timeDiff<0){
             	var tmp = actions[j];
             	actions[j] = actions[j+1];
             	actions[j+1] = tmp;
         	}
    	}
	}
	
	this._renderTitle(actions);
};

/*
 * 绑定标题 当前查看日期数据  渲染页面
 */
mb.vi.oneLdrActionDetail.prototype._renderTitle = function(actions) {
	var trString = "";
	$("#oneDayActionTable").empty();
	if(actions.length>0){
		for(var i=0;i<actions.length;i++){
			var num = i+1;
			var ids = actions[i]["OTHER_USER"];
			var param = {
				"_OTHERUSER_":ids,
				"_TITLE_":actions[i]["TITLE"],
				"_ADDRESS_":actions[i]["ADDRESS"],
				"_ACT_TYPE_":actions[i]["ACT_TYPE"],
				"_CONTENT_":actions[i]["CONTENT"],
				"_BEGIN_TIME_":actions[i]["BEGIN_TIME"],
				"_END_TIME_":actions[i]["END_TIME"],
				"_NUM_":num
			}		
			//替换参与人员ID 为 姓名
			//if(ids!=""){
			    FireFly.doAct("BN_LDR_QUERY_USER","changeOtherUserToName",param).then(function(result){
					var trString1 = "<tr height='30px'><td width='100%' bgcolor='#3bb1ee'><font style='font-weight:bold;' color='#ffffff'>活动"+result.NUM+"</font></td></tr>";
					trString1 += "<tr ><td width='1000px' style='word-break:break-all'><font style='font-weight:bold;'>时间：</font>"+result.BEGIN_TIME+"——"+result.END_TIME+"</br>"
					+"<font style='font-weight:bold;'>类型：</font>"+result.ACT_TYPE+"</br>"
					+"<font style='font-weight:bold;'>标题：</font>"+result.TITLE+"</br>"
					+"<font style='font-weight:bold;'>地点：</font>"+result.ADDRESS+"</br>"
					+"<font style='font-weight:bold;'>安排：</font>"+result.CONTENT+"</br>"
					+"<font style='font-weight:bold;'>人员：</font>"+result.NAMES+"</td></tr>";
					$("#oneDayActionTable").append(trString1);
				});
			//}else{
				//trString = "<tr height='30px'><td width='100%' bgcolor='#3bb1ee'><font style='font-weight:bold;' color='#ffffff'>活动"+num+"</font></td></tr>";
				//trString += "<tr ><td width='1000px' style='word-break:break-all'><font style='font-weight:bold;'>时间：</font>"+actions[i]["BEGIN_TIME"]+"——"+actions[i]["END_TIME"]+"</br>"
				//+"<font style='font-weight:bold;'>类型：</font>"+actions[i]["ACT_TYPE"]+"</br>"
				//+"<font style='font-weight:bold;'>标题：</font>"+actions[i]["TITLE"]+"</br>"
				//+"<font style='font-weight:bold;'>地点：</font>"+actions[i]["ADDRESS"]+"</br>"
				//+"<font style='font-weight:bold;'>安排：</font>"+actions[i]["CONTENT"]+"</br>"
				//+"<font style='font-weight:bold;'>人员：</font>"+actions[i]["OTHER_USER"]+"</td></tr>";
				//$("#oneDayActionTable").append(trString);
			//}
		}
	}
};

/*
 * 加载后执行
 */
mb.vi.oneLdrActionDetail.prototype._afterLoad = function() {
	$.mobile.pageContainer.pagecontainer("change","#oneLdrAction");
};