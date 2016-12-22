

var _viewer = this;
// 构建审批单头部
var gwHeader = new rh.vi.gwHeader({
	"servId" : _viewer.opts.sId,
	"parHandler" : _viewer
});
gwHeader.init();

// 如果存在主键，则可以构建文件编号框
if (_viewer._pkCode) {

	var gwExtCard = new rh.vi.gwExtCardView({
		"parHandler" : _viewer
	});
	gwExtCard.init();
}

/**
 * 相关文件
 */
//获取相关文件的字段
var relate = _viewer.getItem("GW_RELATE");
//修改相关文件链接为按钮形式
relate.obj.css({"border":"0px #91bdea solid"}).parent().css({"margin-left": "2px"});
/*//单击确定后进行回调处理，如果不被覆盖则保存关联相关文件。
relate.callBack = function (arr){
	gwExtCard.getRelate(arr,_viewer,this);
}*/
relate.afterDelete = function(relateId){
	var datas={};
	datas["_NOPAGE_"] = true;
	datas["_searchWhere"] = " and RELATE_ID='"+relateId+"'";
	//根据当前删除的相关ID获取关联的数据
	var getData = FireFly.getListData("OA_GW_RELATE_FILE",datas);
	var data =getData._DATA_;
	//如果有关联的数据才进行删除
	if(data.length){
		var files ="";
		var relateFiles = "";
		//获取当前要删除的关联的文件ID以及主键
		for(var i=0; i<data.length;i++){
			files += data[i].FILE_ID+",";
			relateFiles += data[i].RF_ID+",";
		}
		//删除关联的文件以及关联表中记录
		FireFly.listDelete("SY_COMM_FILE",{"_PK_":files});
		FireFly.listDelete("OA_GW_RELATE_FILE",{"_PK_":relateFiles});
		_viewer.getItem("FUJIAN").refresh();
	}
};
