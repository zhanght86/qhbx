var _viewer = this;
/*if(_viewer.getItem("BN_LY_FLAG").getValue()==3){

	
}*/
if(_viewer.getItem("BN_LY_FLAG").getValue()!=3){
	jQuery("ul>li[sid='BN_LY_ANSWER']").css("display","none");
	jQuery("div[id='BN_LY_ANSWER']").css("display","none");
}else{
	_viewer.form.disabledAll();
}
_viewer.getBtn("approval").unbind("click").bind("click",function(){
	FireFly.doAct(_viewer.servId,"approval",{"_PK_":_viewer.getPKCode()});
	alert("发布成功");
	_viewer.refresh();
});
_viewer.getBtn("commit").unbind("click").bind("click",function(){
	FireFly.doAct(_viewer.servId,"commit",{"_PK_":_viewer.getPKCode()});
});