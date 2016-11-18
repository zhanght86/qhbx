var _viewer = this;
//FireFly.doAct("SY_COMM_CAL_TYPE","loadShareUsers",{});
var addType = _viewer.getBtn("addType");
addType.unbind("click").bind("click",function(event){
	_openDialog(event);	
	_selectType();
});
function _openDialog(event){
	var _self = _viewer;
	this.dialogId = "sy_comm_calendar_type-dial";
	this.winDialog = jQuery("<div></div>").addClass("selectDialog").attr("id",this.dialogId).attr("title","添加类型");
	this.winDialog.appendTo(jQuery("body"));
	var bodyWid = jQuery("body").width();
	var hei = 400;
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
			_viewer.refresh();
		}
	});
	//手动打开dialog
	var dialogObj = jQuery("#" + this.dialogId);
	dialogObj.dialog("open");
	dialogObj.focus();
    jQuery(".ui-dialog-titlebar").last().css("display","block");//设置标题显示
    dialogObj.parent().addClass("rh-bottom-right-radius rhSelectWidget-content");
}
function _selectType(){

	var temp="";
	//如果用户已经添加了类型，则弹出dialog不在添加此类型。如果用户没有添加类型，则在dialog中添加所有类型
//无法取到值----------------------开始----------------------------
	var typeStr="'";
//	//获取用户添加的类型的ID，如果获取到类型ID则将该ID拼成字符串，在查询所有类型的时候去除字符串中的类型
	jQuery("td[icode='CAL_ID']",_viewer.grid.getTable()).each(function(i,n){	
			typeStr+=jQuery(this).text()+"','";
	});
	var types=typeStr.substring(0,typeStr.length-2);
//	if(types=="''"){
//		 temp=FireFly.getDict("SY_COMM_CALENDAR");
//	} else {
//		
//		temp = FireFly.getDict("SY_COMM_CALENDAR",null," and ITEM_ID not in ("+types+")");
//		
//	}
//无法取到值-----------------------结束---------------------------
	
	//temp=FireFly.getDict("SY_COMM_CAL_TYPE");
	//记录查询出的共享类型的总数，及最大索引数
	//var data={};
	//data["_searchWhere"]=" and TYPE_ID not in ("+types+")";
	temp=FireFly.doAct("SY_COMM_CAL_TYPE","query");

	var count = temp["_DATA_"].length;
	jQuery("td[icode='CAL_ID']",_viewer.grid.getTable()).each(function(n){
		//循环将已经添加过的值去掉。去掉以后temp的索引将没有规则，并且temp的值长度也会变化。只能记住最大索引值去循环
		for(var i = 0 ; i < count; i++){
			//第一次循环的时候去掉一个temp["_DATA_"][i]后，第二次循环的时候，循环到去掉的索引的时候会出现undefinde
			if(temp["_DATA_"][i]!=undefined){
				if(jQuery(this).text()==temp["_DATA_"][i]["TYPE_ID"]){
					delete temp["_DATA_"][i];
				}
			} 
		}
	});
	//创建dialog内容
	var table = jQuery("<table></table>").addClass("rhGrid").attr("id","bookType_table");
	var thead = jQuery("<thead></thead>").addClass("rhGrid-thead").attr("id","bookType_thead");
	var tbody = jQuery("<tbody></tbody>").addClass("rhGrid-tbody").attr("id","bookType_tbody");
	var headTr = jQuery("<tr></tr>");
	var headTd = jQuery("<th>请选择</th>").addClass("rhGrid-thead-th").attr("id","head_td").attr("colspan","2");
	headTd.appendTo(headTr);
	headTr.appendTo(thead);
	thead.appendTo(table);	
	table.appendTo(jQuery("#sy_comm_calendar_type-dial"));
	//循环取值并输出到dialog中
	var caltypeTable = jQuery("<table></table>").addClass("rhGrid").attr("id","calType_table").attr("style","padding-left:10px;");
	//times为计数器，当times%2=0的时候换行，及没行显示两个类型
	var times = 0;
	for(var i =0; i < count ; i++){
		//循环取值，如果不是undefined，则将值添加到dialog中
		if(temp["_DATA_"][i]!=undefined){
			if(times%2==0){
				var caltypeTr = jQuery("<tr></tr>").addClass("tBody-tr").attr("id",temp["_DATA_"][i]["TYPE_ID"]);
			}
			var caltypeTd= jQuery("<td align='center'></td>").addClass("tBody-td").attr("id",temp["_DATA_"][i]["TYPE_ID"]);
			var caltypeChecked = jQuery("<input type='checkbox' name='caltypeCheck' />").attr("id",temp["_DATA_"][i]["TYPE_ID"]).attr("value",temp["_DATA_"][i]["TYPE_CODE"]).attr("text",temp["_DATA_"][i]["TYPE_NAME"]).addClass("rowIndex");
			var calValue = jQuery("<td>"+temp["_DATA_"][i]["TYPE_NAME"]+"</td>").addClass("rhGrid-td-left");
			jQuery(temp["_DATA_"][i]["TYPE_CODE"]).val(temp["_DATA_"][i]["TYPE_NAME"]);
			caltypeTd.append(caltypeChecked);
			caltypeTr.append(caltypeTd);
			caltypeTr.append(calValue);
			caltypeTable.append(caltypeTr);
			times+=1;
		}
	}
	caltypeTable.appendTo(jQuery("#sy_comm_calendar_type-dial"));
	
	var btnContainer = jQuery("<div></div>").addClass("rhGrid-btnBar").attr("id","btn_ctn").css({"width":"50%","padding-left":"120px"});
	
	//为确定按钮绑定事件
	var enterBtn = jQuery("<a></a>").addClass("rh-icon rhGrid-btnBar-a").attr("id","enter_btn").bind("click",function(event){
		var param = {};
		jQuery("input:checkbox[name='caltypeCheck']:checked").each(function(n){
			if(n==0){
				param["ITEM_CODE"] = jQuery(this).attr("value") + ",";
				param["PK_CODE"] = jQuery(this).attr("id") + ",";
				param["ITEM_NAME"] = jQuery(this).attr("text") + ",";
			}
			else{
				param["ITEM_CODE"] += jQuery(this).attr("value") + ",";
				param["PK_CODE"] += jQuery(this).attr("id") + ",";
				param["ITEM_NAME"] += jQuery(this).attr("text") + ",";
				}
		});
		//alert(jQuery.toJSON(param));
		if(JsonToStr(param).length > 2){
			FireFly.doAct(_viewer.servId,"calendarSet",param);
			jQuery("#" + dialogId).remove();
			_viewer.refresh();
		}
		else {
			alert("请选择类型");
			return false;
		}
		
	});
	var enterText = jQuery("<span>确认</span>").addClass("rh-icon-inner");
	var enterImg = jQuery("<span></span>").addClass("rh-icon-img btn-add");
	enterText.appendTo(enterBtn);
	enterImg.appendTo(enterBtn);
	enterBtn.appendTo(btnContainer);
	
	/**函数内部--设置取消按钮名称*/
	var cancelText = jQuery("<span>清空</span>").addClass("rh-icon-inner");
	var cancelImg = jQuery("<span></span>").addClass("rh-icon-img btn-delete");
	/**函数内部绑定事件--取消按钮绑定事件*/
	var cancelBtn = jQuery("<a></a>").addClass("rh-icon rhGrid-btnBar-a").attr("id","cancel_btn").bind("click",function(event){
		jQuery("input[name='caltypeCheck']").removeAttr("checked");
	});
	cancelText.appendTo(cancelBtn);
	cancelImg.appendTo(cancelBtn);
	cancelBtn.appendTo(btnContainer);
	btnContainer.appendTo(jQuery("#sy_comm_calendar_type-dial"));
}

var setShare= _viewer.grid.getBtn("setShare");
setShare.unbind("click").bind("click",function(setShare){
	var PKCode = jQuery(this).attr("rowpk");
	var tTitle = "设置共享对象";
	var URL = "SY_COMM_CAL_SHARE.card.do?pkCode="+PKCode;
	var options = {
			"url":URL,
			"tTitle":tTitle,
			"menuFlag":"4"};
	Tab.open(options);
});
