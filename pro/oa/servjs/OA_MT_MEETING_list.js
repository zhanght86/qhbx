/**
 * 
 * @author wangchen
 *
 */

/*********************工程级代码目录**********************
 * 当前作用域句柄
 * 按钮绑定事件--绑定自定义的添加按钮<起草会议通知单>的事件
 * 内部函数--构建弹出框页面布局
 * 内部函数--构造会议室类型选项页面内容
 * 知识总结
 */

/**当前作用域句柄*/
var _viewer = this;

/**按钮绑定事件--绑定自定义的添加按钮<起草会议通知单>的事件*/
/*_viewer.getBtn("addByMT").unbind("click" ).bind("click",function(event){
	//构建弹出框页面布局
	_getDialog(event);
	_showSelectItems();
});*/

/**内部函数--构建弹出框页面布局*/
function _getDialog(event) {
	var _self = _viewer;
	this.dialogId = "mt-meetingroom-bookType-dial";
	//jQuery("#" + this.dialogId).dialog("destroy");
	
	//设置jqueryUi的dialog参数
	this.winDialog = jQuery("<div></div>").addClass("selectDialog").attr("id",this.dialogId).attr("title","申请会议室类型");
	this.winDialog.appendTo(jQuery("body"));
	var bodyWid = jQuery("body").width();
	var hei = 180;
    var wid = 400;
    var posArray = [30,30];
    if (event) {
	    var cy = event.clientY;
	    posArray[0] = "";
	    posArray[1] = cy;
    }
    
    //生成jqueryUi的dialog
	jQuery("#" + this.dialogId).dialog({
		autoOpen: false,
		height: hei,
		width: wid,
		modal: true,
		resizable:false,
		position:posArray,
		open: function() { 

		},
		close: function() {
			jQuery("#" + dialogId).remove();
		}
	});
	
	//手动打开dialog
	var dialogObj = jQuery("#" + this.dialogId);
	dialogObj.dialog("open");
	dialogObj.focus();
    jQuery(".ui-dialog-titlebar").last().css("display","block");//设置标题显示
    dialogObj.parent().addClass("rh-bottom-right-radius rhSelectWidget-content");
    //showBarTipLoad("努力加载中...",null,jQuery(".ui-dialog-title",this.windialog).last());
}

/**内部函数--构造会议室类型选项页面内容*/
function _showSelectItems(){
	//var data = FireFly.getDict("OA_MT_BOOKING_FLAG","0","and 1=1");
	//alert(JSON.stringify(data));
	/**函数内部--构造选项列表-start*/
	var table = jQuery("<table></table>").addClass("rhGrid").attr("id","bookType_table");
	var thead = jQuery("<thead></thead>").addClass("rhGrid-thead").attr("id","bookType_thead");
	var tbody = jQuery("<tbody></tbody>").addClass("rhGrid-tbody").attr("id","bookType_tbody");
	var headTr = jQuery("<tr></tr>");
	var headTd = jQuery("<th>请选择</th>").addClass("rhGrid-thead-th").attr("id","head_td").attr("colspan","2");
	var bodyTr1 = jQuery("<tr></tr>").addClass("tBody-tr").attr("id","bookType_tr1");
	var bodyTr2 = jQuery("<tr></tr>").addClass("tBody-tr").attr("id","bookType_tr2");
	var bodyTr3 = jQuery("<tr></tr>").addClass("tBody-tr").attr("id","bookType_tr3");
	var radionTd1 = jQuery('<td><input type="radio" id="radio1" name="roomtype" value="1"></td>').addClass("rhGrid-td-center").attr("id","radion_td1");
	var textTd1 = jQuery("<td>审批会议通知同时预订会议室</td>").addClass("rhGrid-td-left").attr("id","text_td1");
	var radionTd2 = jQuery('<td><input type="radio" id="radio2" name="roomtype" value="2"></td>').addClass("rhGrid-td-center").attr("id","radion_td2");
	var textTd2 = jQuery("<td>已预订会议室</td>").addClass("rhGrid-td-left").attr("id","text_td2");
	var radionTd3 = jQuery('<td><input type="radio" id="radio3" name="roomtype" value="3"></td>').addClass("rhGrid-td-center").attr("id","radion_td3");
	var textTd3 = jQuery("<td>不预订会议室</td>").addClass("rhGrid-td-left").attr("id","text_td3");
	
	headTd.appendTo(headTr);
	headTr.appendTo(thead);
	thead.appendTo(table);
	
	radionTd1.appendTo(bodyTr1);
	textTd1.appendTo(bodyTr1);
	radionTd2.appendTo(bodyTr2);
	textTd2.appendTo(bodyTr2);
	radionTd3.appendTo(bodyTr3);
	textTd3.appendTo(bodyTr3);
	bodyTr1.appendTo(tbody);
	bodyTr2.appendTo(tbody);
	bodyTr3.appendTo(tbody);
	tbody.appendTo(table);	
	table.appendTo(jQuery("#mt-meetingroom-bookType-dial"));
	var btnContainer = jQuery("<div></div>").addClass("rhGrid-btnBar").attr("id","btn_ctn").css({"width":"100%"});
	
	/**函数内部绑定事件--构造确认按钮并绑定事件*/
	var enterBtn = jQuery("<a></a>").addClass("rh-icon rhGrid-btnBar-a").attr("id","enter_btn").bind("click",function(event){
		var roomtype = jQuery('input:radio[name="roomtype"]:checked').val(); 
		if(roomtype==null){
			return;
		}
		//传递参数
		var links = {"roomtype":roomtype};
		//new rh.vi.listView({"sId":"OA_MT_MEETING"})._openCardView("add","MEETING_ID","OA_MT_MEETING",false);
		//手动打开本服务的卡片页面（模拟添加按钮）-start
			var cardData = {
					"act":UIConst.ACT_CARD_ADD, 
					"sId":_viewer.servId, 
					"parHandler":_viewer, 
					"links":links, 
					"readOnly":false 
			};
			cardData[UIConst.PK_KEY] = "MEETING_ID";
		    var mtCardView = new rh.vi.cardView(cardData);
		    mtCardView.show();
		    rhBldDestroyBase(mtCardView);
	    //手动打开本服务的卡片页面（模拟添加按钮）-end
	    jQuery("#mt-meetingroom-bookType-dial").empty();
	    jQuery("#mt-meetingroom-bookType-dial").remove();
	});
	var enterText = jQuery("<span>确认</span>").addClass("rh-icon-inner");
	var enterImg = jQuery("<span></span>").addClass("rh-icon-img btn-add");
	enterText.appendTo(enterBtn);
	enterImg.appendTo(enterBtn);
	enterBtn.appendTo(btnContainer);
	
	/**函数内部--设置取消按钮名称*/
	var cancelText = jQuery("<span>取消</span>").addClass("rh-icon-inner");
	var cancelImg = jQuery("<span></span>").addClass("rh-icon-img btn-delete");
	/**函数内部绑定事件--取消按钮绑定事件*/
	var cancelBtn = jQuery("<a></a>").addClass("rh-icon rhGrid-btnBar-a").attr("id","cancel_btn").bind("click",function(event){
		jQuery("#mt-meetingroom-bookType-dial").empty();
		jQuery("#mt-meetingroom-bookType-dial").remove();
	});
	cancelText.appendTo(cancelBtn);
	cancelImg.appendTo(cancelBtn);
	cancelBtn.appendTo(btnContainer);
	
	btnContainer.appendTo(jQuery("#mt-meetingroom-bookType-dial"));
	
	/**函数内部绑定事件--radio所在行绑定事件--*/
	jQuery("#bookType_tbody > .tBody-tr").each(function(i){
		jQuery(this).unbind("click").bind("click",function(event){
			jQuery("#radio"+(i+1)).attr("checked","true");
		});
	});
}