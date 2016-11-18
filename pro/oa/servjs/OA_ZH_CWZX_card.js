 _viewer= this;
  var dataid = _viewer.getByIdData("LT_ID");//数据主键
  var servid = _viewer.servId;
  var filecat = "CWZX_XGWJ";
 _viewer.getItem("CWZX_XGWJ").getBlank().find(".uploadBtn").hide();
 var addreport = "<span class='addreportBtn'><a class='rh-icon rhGrid-btnBar-a' actcode='add'>" +
			"<span class='rh-icon-inner' style='padding: 0px 2px 2px 0px !important'>添加报表</span></a></span>";
_viewer.getItem("CWZX_XGWJ").getBlank().find(".uploadBtn").parent().append(addreport);
_viewer.getItem("CWZX_XGWJ").getBlank().find(".uploadBtn").parent().find(".addreportBtn").unbind("click").bind("click",function(){
	//2.构造查询选择参数
	var configStr = "OA_ZH_CWZX_REPORT,{'SOURCE':'FILE_ID~RT_ID~RT_NAME~RT_UPTIME','TYPE':'multi','HIDE':'RT_ID~FILE_ID'}";
	var options = {
		"config" :configStr,
		"parHandler":_viewer,
		"formHandler":_viewer.form,
	    "replaceCallBack":function(idArray) {//回调，idArray为选中记录的相应字段的数组集合    	
	    	var fileid = idArray["FILE_ID"].split(",");//数据主键        
	        var filldata="";
	        for(var i = 0;i<fileid.length;i++ ){          
	        var param = {};
	        param["FILE_ID"]=fileid[i];//文件库 文件id
	        param["serv"] = servid;
	        param["FILE_CAT"] = filecat;
	        param["DATA_ID"] = dataid;
	        var filldata = FireFly.doAct("OA_ZH_CWZX","copyReport",param);
	        _viewer.getItem("CWZX_XGWJ").fillData(filldata["_DATA_"]);	      
	        }
		}
	};
	//3.用系统的查询选择组件 rh.vi.rhSelectListView()
	var queryView = new rh.vi.rhSelectListView(options);
	queryView.show(event);
});
//重载文件删除方法，去掉上传空间 
_viewer.getItem("CWZX_XGWJ").deleteFile = function(fileId) {
	if (confirm("确定删除该文件？")) {
		// 从数据库中删除
		var data = {};
		data[UIConst.PK_KEY] = fileId;
		FireFly.doAct("SY_COMM_FILE", "delete", data, false);
		
		//注释
//		this.getUpload().setUploadFiles(this.getUpload().getUploadFiles() - 1);
		
		// 从页面上删除
		this.remove(fileId.replace(".", "_"));
		
		// 删除之后
		this.afterDeleteCallback.call(this, this._fileData[fileId]);
		
		// 从内存里删除
		delete this._fileData[fileId];
		
		this.showNoneFile();
		
		// 触发change事件
		this.onchange();
		
		// 重新对文件进行排序
		this._container.find(".fileSortNum").each(function(index, node){
			jQuery(node).html(index + 1);
		});
	}
};
 _viewer.getItem("CWZX_XGWJ").afterDeleteCallback= function(fileData) {
 //刷新文件列表
 _viewer.getItem("CWZX_XGWJ").refresh(servid,dataid,filecat);
 }
 
 //修改修改文件按钮信息
 _viewer.getItem("CWZX_XGWJ").getBlank().find(".modifyBtn").eq(0).find("span").text("修改报表信息");
 
 // //备注显示样式
_viewer.form.getItem("MEMO_TITLE").obj.parent().removeClass("disabled");
_viewer.form.getItem("MEMO_TITLE").obj.css({"color":"black","font-weight":"bolder","background-color":"white","font-size":"14px"});