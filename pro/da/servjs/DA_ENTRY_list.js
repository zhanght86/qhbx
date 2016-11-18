var _viewer = this;



_viewer.afterTreeNodeClick = function(item,id,dictId){
	
	//jQuery("div[itemid='"+item.ID+"']").parent().find("ul[class='bbit-tree-node-ct']").css("display","");
	//jQuery("div[itemid='"+item.ID+"']").parent().find("img[class='bbit-tree-ec-icon bbit-tree-elbow-end-plus']").removeClass("bbit-tree-elbow-end-plus").addClass("bbit-tree-elbow-end-minus");
	var sID = _viewer.servId;
	var entryObj = FireFly.byId(sID,item["ID"]);
	var aa = entryObj["CAT_ID"];
	var catObj = FireFly.byId("DA_CATEGORY",entryObj["CAT_ID"]);
	
	Tab.open({
		"url" : catObj["SERV_ID"]+".list.do",
		"tTitle" : catObj["CAT_NAME"],
		"menuFlag" : 3,
	});	
	_viewer.backImg.mousedown();

}

function digui(id){
	if(id != ""){
//		var ul = jQuery("div[itemid='"+id+"']").parent().find("ul[class='bbit-tree-node-ct']");
//		ul.css("display","");
		alert( jQuery("div[itemid='"+id+"']").parent().parent().parent().first().attr("itemid"));
		  
		
		//digui(item.CHILD);
	}
	
}