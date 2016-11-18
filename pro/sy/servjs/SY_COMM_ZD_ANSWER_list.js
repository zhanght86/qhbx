var _viewer = this;

//判断如果当前显示不显示  最佳答案的按钮
showBestAnsBtn();

function showBestAnsBtn() {
	var reqObj = {};
	reqObj.Q_ID = _viewer.getParHandler()._pkCode;
	
	var resultData = FireFly.doAct("OA_ZD_ANSWER","getBestState",reqObj);
	if (resultData.rtnStr == "notShow") {
		//隐藏最后一列的选择
		_viewer.grid.getBtn("commentBest").hide();
	}
}

/**
 * 隐藏 最前面的一行  多选框
 */
_viewer.grid.getHeadCheckBox().hide(); 
_viewer.grid.getCheckBox().hide();



/**
 * 设置最佳答案
 */
var commentBestBtn = _viewer.grid.getBtn("commentBest");

if (commentBestBtn) {
	commentBestBtn.unbind("click").bind("click",function() {
		var ansPk = jQuery(this).attr("rowpk");//获取主键信息
		var reqObj = {};
		reqObj.A_ID = ansPk;
	    reqObj.Q_ID = _viewer.getParHandler()._pkCode;		
		
		var resultData = FireFly.doAct("OA_ZD_ANSWER","setBestAnswer",reqObj);	

        if (resultData.rtnStr == "success") {
		    alert("已选择最佳答案");
			//刷新页面
			_viewer.getParHandler().refresh();
		}		
	}); 
}


/**
 * 对留言就行评论
 */
var commentAnsBtn = _viewer.grid.getBtn("commentAns");

/**
 * 在评论的按钮后面显示条数
 */
showAnsCount(); 
function showAnsCount() {
    var ansIds = _viewer.grid.getPKCodes().join(",");
	
	//获取评论条数
	var reqObj = {};
	reqObj.ansIds = ansIds;
	var resultData = FireFly.doAct("OA_ZD_ANSWER", "getAnsCommCount", reqObj);	
	
	jQuery.each(resultData.rtnBean, function(i, commentItem) {
		//找到后面的按钮
		jQuery("#" + commentItem._PK_).find("[icode='commentAns']").find(".rh-icon-inner").append("("+commentItem.count+")");   
	});	
}


if (commentAnsBtn) {
	commentAnsBtn.unbind("click").bind("click",function(event) {
		var ansPk = jQuery(this).attr("rowpk");//获取主键信息
		
		showCommentList(ansPk);
	}); 
}

/**
 * 显示评论列表
 */
function showCommentList(ansPkValue) {
	var reqObj = {};
	reqObj.DATA_ID = ansPkValue;
	reqObj.SERV_ID = "OA_ZD_ANSWER";

	//判断是否是打开的，如果是打开的，则收缩
	if(jQuery("#inputTr" + ansPkValue).length > 0) {
	    jQuery(".commentFlag").remove();		
	    return;   
	}
	
    //删除之前的,评论的行
	jQuery(".commentFlag").remove();		
	
	var resultData = FireFly.doAct("SY_SERV_COMMENT", "getCommentsByAid", reqObj);
	
	var colNum = jQuery("#" + ansPkValue).find("td").length - 1;
	
	//在下面显示一行
	var lineObj = jQuery("#" + ansPkValue);
	jQuery.each(resultData.rtnBean, function(i, commentItem) {
		var newTrObj = jQuery("<tr class='tBody-tr commentFlag' id='"+commentItem.C_ID+"'></tr>").insertAfter(lineObj);
		jQuery("<td></td>").appendTo(newTrObj);
		jQuery("<td colspan='"+colNum+"'>"+commentItem.S_USER__NAME+"&nbsp;&nbsp;"+commentItem.C_TIME+"<br>"+commentItem.C_CONTENT+"</td>").appendTo(newTrObj);
	});
	
	//第一行搞成输入框，
	var inputTrObj = jQuery("<tr class='tBody-tr commentFlag' id='inputTr"+ansPkValue+"'></tr>").insertAfter(lineObj);
	jQuery("<td></td>").appendTo(inputTrObj);
	jQuery("<td colspan='"+colNum+"'><input name='commContent' id='commContent' size='90' value=''>&nbsp;<a href='#' id='input"+ansPkValue+"'>发布</a>&nbsp;</td>").appendTo(inputTrObj);
	var deployBtn = jQuery("#input" + ansPkValue);
	deployBtn.bind("click",function() {
	    var commentVal = jQuery('#commContent').val();
	    addNewComment(ansPkValue, commentVal);
	});
	
	_viewer._resetHeiWid();
}

/**
 * 添加一条评论
 */
function addNewComment(ansPkValue, commentVal) {
	var reqObj = {};
	reqObj.DATA_ID = ansPkValue;
	reqObj.SERV_ID = "OA_ZD_ANSWER";
	reqObj.C_CONTENT = commentVal;
	reqObj.C_TIME = rhDate.getTime();

	var resultData = FireFly.doAct("SY_SERV_COMMENT", "save", reqObj);
	
	//添加之后刷新评论列表
	jQuery(".commentFlag").remove();	
	showCommentList(ansPkValue);
}

