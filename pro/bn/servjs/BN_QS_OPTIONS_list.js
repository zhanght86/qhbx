var _viewer = this;
if (_viewer.getParHandler() != null){
	var question_type = _viewer.getParHandler().getItem("QUESTION_TYPE").getValue();
	//alert(_viewer.getParHandler().getItem("QUESTION_TYPE").getValue());
	if(question_type =='3'){
		jQuery(".rhGrid-btnBar").hide();
	}
}