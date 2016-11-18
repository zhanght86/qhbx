var _viewer = this;

//显示 最佳答案
if (_viewer.itemValue("ANS_ID").length > 0) {
	bestRender(); 
	showBestToPage(); 
}

function showBestToPage() {
    jQuery("#"+_viewer.servId+"_BestTable").remove();//清除

	var reqdata = {};
	var reqObj = {};
	reqObj.Q_ID = _viewer._pkCode;
	
	var resultData = FireFly.doAct("SY_COMM_ZD_QUESTION","getBestAns",reqObj);	

	if (resultData.rtnBean) {
	    var bestBean = resultData.rtnBean;
		var typeTable = jQuery("<table id='"+_viewer.servId+"_BestTable'></table>").appendTo(_PSugComCon);
		var typeTr = jQuery("<tr id='bestAnsTr'></tr>").appendTo(typeTable);
		jQuery("<td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>").appendTo(typeTr);

		jQuery("<td>&nbsp;&nbsp;&nbsp;"+bestBean.S_USER__NAME + "</td>").appendTo(typeTr);
		jQuery("<td>&nbsp;&nbsp;&nbsp;"+bestBean.A_CONTENT + "</td>").appendTo(typeTr);
		
		jQuery("<td>&nbsp;&nbsp;&nbsp;<a href='#' id='bestCom"+bestBean.A_ID+"'>评论("+bestBean.comCount+")</a>&nbsp;</td>").appendTo(typeTr);
		
		var bestCommBtn = jQuery("#bestCom"+bestBean.A_ID);
		bestCommBtn.bind("click",function() {
			showBestComment(bestBean.A_ID);
		});		
	}
	_viewer._resetHeiWid();
}

/**
 * 显示最佳答案的评论
 */
function showBestComment(ansPkValue) {
	var reqObj = {};
	reqObj.DATA_ID = ansPkValue;
	reqObj.SERV_ID = "OA_ZD_ANSWER";

	//判断是否是打开的，如果是打开的，则收缩
	if(jQuery("#inputBestTr" + ansPkValue).length > 0) {
	    jQuery(".bestCommentFlag").remove();		
	    return;   
	}
	
    //删除之前的,评论的行
	jQuery(".bestCommentFlag").remove();		
	
	var resultData = FireFly.doAct("SY_SERV_COMMENT", "getCommentsByAid", reqObj);
	
	//在下面显示一行
	var bestTrObj = jQuery("#bestAnsTr");
	
	jQuery.each(resultData.rtnBean, function(i, commentItem) {
		var newTrObj = jQuery("<tr class='tBody-tr bestCommentFlag' id='best"+commentItem.C_ID+"'></tr>").insertAfter(bestTrObj);
		jQuery("<td></td>").appendTo(newTrObj);
		jQuery("<td colspan='3'>"+commentItem.S_USER__NAME+"&nbsp;&nbsp;"+commentItem.C_TIME+"&nbsp;&nbsp;&nbsp;&nbsp;"+commentItem.C_CONTENT+"</td>").appendTo(newTrObj);
	});
	
	//第一行搞成输入框，
	var inputTrObj = jQuery("<tr class='tBody-tr bestCommentFlag' id='inputBestTr"+ansPkValue+"'></tr>").insertAfter(bestTrObj);
	jQuery("<td></td>").appendTo(inputTrObj);
	jQuery("<td colspan='3'><input name='bestCommContent' id='bestCommContent' size='90' value=''>&nbsp;<a href='#' id='inputBest"+ansPkValue+"'>发布</a>&nbsp;</td>").appendTo(inputTrObj);
	var deployBtn = jQuery("#inputBest" + ansPkValue);
	deployBtn.bind("click",function() {
	    var commentVal = jQuery('#bestCommContent').val();
	    addNewComment(ansPkValue, commentVal);
	});
	
	_viewer._resetHeiWid();
}

/**
 * 添加一条评论 , 因为这个是对最佳答案的评论，所以serv_id 设置成 OA_ZD_ANSWER
 */
function addNewComment(ansPkValue, commentVal) {
	var reqObj = {};
	reqObj.DATA_ID = ansPkValue;
	reqObj.SERV_ID = "OA_ZD_ANSWER";
	reqObj.C_CONTENT = commentVal;
	reqObj.C_TIME = rhDate.getTime();

	var resultData = FireFly.doAct("SY_SERV_COMMENT", "save", reqObj);
	
	//添加之后刷新评论列表
	jQuery(".bestCommentFlag").remove(); //先清除	
	showBestComment(ansPkValue);
}


var _PSugComCon;
function bestRender() {
    var SugConObj = jQuery("<div class='ui-form-default' id='sugComContainer'></div>");
	_PSugComCon = jQuery("<div class='formContent'></div>");
	var item = jQuery("<div class='item ui-corner-5' id='"+_viewer.servId+"SugCom'></div>")
		.append(
			jQuery("<fieldset></fieldset>")
				.append("<span class='legend'><span class='name'>最佳答案</span><span class='close icon-card-close'></span></span>")
				.append(_PSugComCon)
		).appendTo(SugConObj);
		
	/**
	 * 收起分组框
	 */
	var legend = SugConObj.find(".legend").first();
	legend.click(function() {
		var close = jQuery(this).find(".close").first();
		if (close) {
			if (close.hasClass("icon-card-close")) {
				close.removeClass("icon-card-close").addClass("icon-card-open");
				_PSugComCon.slideToggle("slow", function() {
					_viewer._resetHeiWid();
				});
			} else {
				close.removeClass("icon-card-open").addClass("icon-card-close");
				_PSugComCon.slideDown("slow", function() {
					_viewer._resetHeiWid();
				});
			}
		}
	});
	
	SugConObj.insertAfter(_viewer.form.obj);
}


/**
 * 邀请人员回答问题
 */
var reqUserAnsBtn = _viewer.getBtn('reqUserAns');

if (reqUserAnsBtn) {
	reqUserAnsBtn.unbind("click").bind("click",function(event) {
	    var configStr = "SY_ORG_DEPT_USER" + ",{'TYPE':'multi','rtnLeaf':true} " //multi , single
		var options = {"config" : configStr, //树形配置字符串
		"replaceCallBack":addUserAndSendTodo, //回调方法
		"itemCode":"REQ_USERS",
		"extendDicSetting":{'cascadecheck':true},	
		"parHandler":this}; //用于回调的上下文
		var dictView = new rh.vi.rhDictTreeView(options); //初始化字典对象
		dictView.show(event); //显示字典 
	}); 
	
	/**
	 * 发送申请
	 */
	function addUserAndSendTodo(idArray,nameArray) {
	    var reqObj = {};
		reqObj.userCodes = idArray.join(",");
		reqObj.Q_ID = _viewer._pkCode;
		
		var resultData = FireFly.doAct("SY_COMM_ZD_QUESTION", "reqUserAnsQ", reqObj);
		
		if (resultData.rtnStr == "success") {
		    alert("已成功向 " + nameArray.join(",") + "发送申请!");
		}
	}
}



 