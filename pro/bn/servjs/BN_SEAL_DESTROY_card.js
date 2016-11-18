var _viewer = this;
var destroy = _viewer.getBtn("destroy");
//初始化公文/表单页面头部信息，如公文/表单标题，编号，缓急，密级等信息
var opts = {
	servId:this.servId,
	servName:"印章销毁",
	parHandler:_viewer
};

var params = _viewer.getParams();
if(params != undefined && params.toString().length>0){
	_viewer.getItem("SEAL_ID").setValue(params.sealList);
	/*_viewer.getItem("SEAL_ID").setName(params.sealListName);*/
	jQuery("#BN_SEAL_DESTROY-SEAL_ID__NAME").val(params.sealListName);
}
//alert(14);
//alert(_viewer.getPKCode());
/*if((_viewer.getPKCode()=="ADD")||(_viewer.getPKCode()=="ADD1")){
	destroy.hide();
}else if(_viewer.getItem("DEST_FLAG").getValue()==1){
	destroy.hide();
}*/
/**
 * 在填写申请单页面进行保存前对选中的印章列表进行判断，若为0，则停止保存按钮的执行
 * @param {} result
 * @return {}
 */
/*_viewer.beforeSave  =  function(result){
	if((_viewer.getPKCode()=="ADD")||(_viewer.getPKCode()=="ADD1")){
	var flg=true;
	FireFly.doAct("BN_SEAL_BASIC_INFO","getSaveSealList","",true,false,function(result){				
		     //通过查询session中保存的印章列表信息判断
		     if(result.sealList==""){
		     	alert("您未选择要销毁的印章！");
		     	flg = false;
		     	_viewer.cardClearTipLoad();//清除卡片的提示
		     }
		     });
		     return flg;
	}
}*/
/**
 * 表单填写页面在执行保存按钮后要把选中的印章列表信息保存到关联表，同时清除session中的sealList
 * @param {} result
 */
/*_viewer.afterSave  =  function(result){
	//在后台写了一个保存后的方法，result中的flg即赋得值。
	if(result.flg=="ADD"){//此ADD只是赋予的一个标志值，也可以在后台赋其他值，对应起来。
		var SEAL_ARRAY=new Array();
			var applyId=result.APPLY_ID;
	 	FireFly.doAct("BN_SEAL_BASIC_INFO","getSaveSealList","",true,false,function(result){				
		     SEAL_ARRAY=result.sealList.split(",");
		     });   
					for(var i=0;i < SEAL_ARRAY.length;i++){
				    var data={"DEST_ID":result.ID,"SEAL_ID":SEAL_ARRAY[i]};
            	    FireFly.doAct("BN_SEAL_DESTROY_INFO","save",data, true);//添加到印章销毁关联表
            	    FireFly.doAct("BN_SEAL_BASIC_INFO","saveSealList",{"sealList" : ""},true,false);
				}	
	}
}*/



if(destroy){
	destroy.unbind("click").bind("click",function(){
		_viewer.getItem("DEST_FLAG").setValue(1);
		_viewer.doAct("save");
		FireFly.doAct(
				_viewer.servId,
				"destroySeal",
					{"_PK_":_viewer.getPKCode(),
					"SEAL_ID":_viewer.getItem("SEAL_ID").getValue().toString(),
					"TITLE":_viewer.getItem("DEST_REASON").getValue()}
				);
		Tab.close();
		params.handler.refresh();
	});
}