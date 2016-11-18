var _viewer = this;
_viewer.setParentRefresh();
/*因为显示出来的印章大类是自定义字段，所以需要把隐藏起来的值传递显示出来。
var type1 = _viewer.getItem("SEAL_TYPE1");
if(type1.getValue() != ""){
	_viewer.getItem("SEAL_TYPE1_").setValue(type1.getValue());
	var sealDict = FireFly.getDict("BN_SEAL_INFO_LEAF",type1.getValue(),"","",{});
	_viewer.getItem("SEAL_TYPE1_").setText(sealDict[0].CHILD[0].NAME);
}
*/
_viewer.getBtn("makeSeal").unbind("click").bind("click",function(event){
	window.open("/oa/gw/wfeExt/软航科技电子印章管理工具.html?SignUser=ceshi&SealId="+_viewer.getPKCode()+"&SignName="+_viewer.getItem("SEAL_NAME").getValue()+"");
});
//根据印章状态显示启用或者停用按钮
var status = _viewer.getItem("SEAL_STATE").getValue();
if(status != 0 && status != 2 || _viewer._byIdData()._ADD_=="true"){
	_viewer.getBtn("sealStart").hide();
}else{
	
}
//绑定启用按钮点击事件
_viewer.getBtn("sealStart").unbind("click").bind("click",function(){
	FireFly.doAct(_viewer.servId,"sealStart",{"_PK_":_viewer.getPKCode(),"SEAL_STATE":status});
	_viewer.getBtn("sealStart").hide();
	_viewer.getItem("SEAL_STATE").setValue(1);
	alert("启用成功");
	_viewer._parentRefreshFlag = true;
});
/*
 * 根据大类的类型，自动查询出印章的规格并将数据填充
 */
var sealType = _viewer.getItem("SEAL_TYPE1");
sealType.change(function(){
	/*_viewer.getItem("SEAL_TYPE2").setValue("");*/
	/*jQuery("#BN_SEAL_BASIC_INFO-SEAL_TYPE2__NAME").val("");*/
	//_viewer.getItem("SEAL_TYPE1").setValue(sealType.getValue());
	var specWhere = "";
	var sealType1 = _viewer.getItem("SEAL_TYPE1").getValue() || "";
	/*var sealType2 = _viewer.getItem("SEAL_TYPE2").getValue() || "";*/
	if(sealType1 != ""){
		specWhere += " and SEAL_TYPE1='"+sealType1+"'";
	}
	/*if(sealType2 != ""){
		specWhere += " and SEAL_TYPE2='"+sealType2+"'";
	}*/
	var result = FireFly.doAct("BN_SEAL_STYLE","query",{"_extWhere":specWhere});
	var list = result._DATA_;
	if(list.length>0){
		for(var i=0;i<list.length;i++){
			var data = list[i];
			jQuery("#BN_SEAL_BASIC_INFO-SEAL_FONT").val(data.SEAL_FONT);
			jQuery("#BN_SEAL_BASIC_INFO-SEAL_QUALITY").val(data.SEAL_QUALITY);
			jQuery("#BN_SEAL_BASIC_INFO-SEAL_WIDTH").val(data.SEAL_WIDTH);
			jQuery("#BN_SEAL_BASIC_INFO-SEAL_HEIGHT").val(data.SEAL_HEIGHT);
			jQuery("#BN_SEAL_BASIC_INFO-SEAL_FORM").val(data.SEAL_FORM);
			jQuery("#BN_SEAL_BASIC_INFO-SEAL_COLOR").val(data.SEAL_COLOR);
			/*jQuery("#BN_SEAL_BASIC_INFO-SEAL_HEIGHT").val(data.SEAL_HEIGHT);
			jQuery("#BN_SEAL_BASIC_INFO-SEAL_WIDTH").val(data.SEAL_WIDTH);*/
			
		}
	}else{
		jQuery("#BN_SEAL_BASIC_INFO-SEAL_FONT").val("");
		jQuery("#BN_SEAL_BASIC_INFO-SEAL_QUALITY").val("");
		jQuery("#BN_SEAL_BASIC_INFO-SEAL_WIDTH").val("");
		jQuery("#BN_SEAL_BASIC_INFO-SEAL_HEIGHT").val("");
		jQuery("#BN_SEAL_BASIC_INFO-SEAL_FORM").val("");
		jQuery("#BN_SEAL_BASIC_INFO-SEAL_COLOR").val("");
	}
});
//重写save方法。判断相同序列号下的索引是否重复
_viewer.getBtn("save").unbind("click").bind("click",function(){
	var ekey = _viewer.getItem("SEAL_CODE").getValue();
	var ekeyAddress = _viewer.getItem("EKEY_ADDRESS").getValue();
	var result = FireFly.doAct(_viewer.servId,"query",{"_WHERE_":" and ID != '"+_viewer.getPKCode()+"' and SEAL_CODE = '"+ekey+"'","_SELECT_":" ID,SEAL_NAME,SEAL_OWNER_USER,SEAL_RESPONS_USER,SEAL_STATE,EKEY_ADDRESS"});
	var data = result._DATA_;
	if(data.length>0){
		for(var i=0;i<data.length;i++){
			if(data[i].EKEY_ADDRESS==ekeyAddress){
				alert("序列号:"+ekey+"下的索引位置:"+ekeyAddress+"已经被占用,请选择其他位置");
			}else{
				_viewer.doAct("save");
			}
		}
	}else{
		_viewer.doAct("save");
	}
});



