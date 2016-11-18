var _viewer = this;

var yeWuServId = _viewer.links.SERV_ID; //关联的 业务数据的 服务ID

var params = {};

params.SERV_ID = yeWuServId;
var servItems = FireFly.doAct("DA_ORIGINAL_RELATION_ITEM", "getServItem", params);

//选择字段对应
var trObjs = jQuery(".tBody-tr").find("td[icode='WEIZL_ITEM__NAME']");

jQuery.each(trObjs, function(i, select1){
	var pkStr = jQuery(select1).find(".batchModify").attr("pk");
    var htmlStr = "<select class='batchModify' icode='WEIZL_ITEM' pk='"+pkStr+"'>";
    htmlStr += "<option value=''></option>";
    //循环字段
    jQuery.each(servItems._DATA_, function(j, item){
    	var rowItemValue = _viewer.grid.getRowItemValue(pkStr, "WEIZL_ITEM_CODE");
    	var selected = "";
    	if (item.ITEM_CODE == rowItemValue) {
    		selected = "selected";
    	}
    	
    	htmlStr += "<option value='" + item.ITEM_CODE + "-" + item.ITEM_NAME + "' " + selected + ">" + item.ITEM_CODE + "-" + item.ITEM_NAME + "</option>";
    });
    
    htmlStr += "</select>"

	jQuery(select1).html(htmlStr);
	
    _viewer.grid._bindTrEvent();
    
});

