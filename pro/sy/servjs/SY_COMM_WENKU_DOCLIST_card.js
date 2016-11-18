var _viewer = this;

var previewBtn = _viewer.getBtn('addDoc');

if(_viewer.getPKCode()){
	previewBtn.show();
	previewBtn.unbind("click").bind("click", function(event) {//选择一个现有模版
		//0.获取文辑类型
		var list_chnl=_viewer.getItem("LIST_CHNL").getValue();
		var extWhereStr = " and DOCUMENT_CHNL=^"+list_chnl+"^";
		//1.构造查询选择参数
		var configStr = "SY_COMM_WENKU_DOCUMENT,{'SOURCE':'DOCUMENT_ID~DOCUMENT_TITLE~DOCUMENT_FILE_SIZE','EXTWHERE':'"+extWhereStr+"','TYPE':'multi'}";
		var options = {
			"title":"相关文档",
			"config" :configStr,
			"parHandler":this,
		    "replaceCallBack":function(rawData) {//回调，idArray为选中记录的相应字段的数组集合
		    		callBack(rawData);
			}
		};
		//3.用系统的查询选择组件 rh.vi.rhSelectListView()
		var queryView = new rh.vi.rhSelectListView(options);
		queryView.show(event);
		
	});
	function callBack(rawData){
		var list_id=_viewer.getPKCode()
		 	doc_ids=rawData.DOCUMENT_ID.split(","),
		 	doc_titles=rawData.DOCUMENT_TITLE.split(",");
		if(doc_ids.length>0){
			var batchData = {},
			    tempArray = [];
			jQuery.each(doc_ids,function(index,obj) {
				   var temp ={};
				   temp.DOCUMENT_ID=obj;
				   temp.DOCUMENT_TITLE=doc_titles[index];
				   temp.LIST_ID=list_id;
				   
				   tempArray.push(temp);
			 });
			batchData["BATCHDATAS"] = tempArray;
			var resultData = FireFly.batchSave("SY_COMM_WENKU_DOCLIST_ITEM",batchData,null,null,false);
			_viewer.refresh();
		}
	 
	}
	 
}else{
	previewBtn.hide();
	
}