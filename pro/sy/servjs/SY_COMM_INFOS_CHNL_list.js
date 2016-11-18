var _viewer = this;
//获取选中的栏目的id，作为目标栏目
 var itemid = "";
_viewer.afterTreeNodeClick = function(item,id,dictId){
    itemid = item.ID; 
}
 var copy = _viewer.getBtn("copyCommChnl");		
 if(copy){
 copy.unbind("click").bind("click",function(){
 	// 1.构造树形选择参数
    var extwhere = "AND SITE_ID = 'SY_COMM_SITE_INFOS'";
    var configStr = '@com.rh.core.comm.news.InfosChnlDict,'
                  + '{"params":{"REP_DICT_ID":"SY_COMM_INFOS_CHNL_MANAGE","_extWhere":"'+extwhere+'"},"TYPE":"multi"}';
	var extendTreeSetting = "{'cascadecheck':true}";
	var options = {
		"config" : configStr,
		"dialogName": "待复制栏目",
		"extendDicSetting" : StrToJson(extendTreeSetting),// 非必须参数，一般用不到
		"replaceCallBack" : function(idArray, nameArray) {// 回调，idArray为选中记录的相应字段的数组集合
         var param = {};
         param["COPY_ID"] = idArray.join(",");
         param["TARGET_ID"] = itemid;
	       FireFly.doAct("SY_COMM_INFOS_CHNL", "copyBatchChnl", param);
	       _viewer.refreshTreeAndGrid();
		   _viewer.refresh();			
		}
	};
	// 2.显示树形
	var dictView = new rh.vi.rhDictTreeView(options);
	dictView.show(event);
 });
 }



