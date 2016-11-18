var _viewer = this;
var _self=this;

//默认收起导航区
_self.navTreeContainer.data("orWid",_self.navTreeContainer.width());
_self.contentMainCont.data("orWid",_self.contentMainCont.width());
_self.navTreeContainer.width("2%");
_self.contentMainCont.width("98%");
//连接服务打开的title显示为百年人寿+栏目名称
$(".conHeaderTitle-span.rh-slide-flagYellow").text("百年人寿 > "+_viewer.params.title);
_self.navTreeContainer.hover(
 	   function(event) {
    	   if (jQuery(".content-navTree-close").length > 0) {
		   jQuery(".content-navTree-close").addClass("content-navTree-expand");		  
	   }
   },
   function(event) {
   if (jQuery(".content-navTree-expand").length == 1) {
    } else {
 	   jQuery(".content-navTree-close").remove();
        }
	   }
  );




function newsView(id) {
	var url = "/cms/SY_COMM_INFOS/" + id + ".html";
	window.open(url);
}
_viewer.grid.unbindTrdblClick();
_viewer.grid.unbindIndexTDClick();
_viewer.grid.dblClick(function(id, node) {
	//var dateStr = _viewer.grid.getSelectItemVal("NEWS_TIME");
	newsView(id);
}, _viewer);
	
	//var dataId = _viewer.grid.getSelectItemVal("NEWS_ID");
	//var dateStr = _viewer.grid.getSelectItemVal("NEWS_TIME");
	
	//alert(dateStr);
	//window.open("/html/" + dateStr.replace(new RegExp("-","gm"),"") + "/" + dataId + ".html");