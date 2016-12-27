var _viewer = this;
var QJ_START_TIME = _viewer.getItem("QJ_START_TIME");
var QJ_END_TIME = _viewer.getItem("QJ_END_TIME");
var QJ_NUMBER = _viewer.getItem("QJ_NUMBER");
jQuery(QJ_START_TIME.obj).addClass("Wdate").unbind("click").bind("click", function(){
	WdatePicker({
		dateFmt:"yyyy-MM-dd",
		startDate:"%yyyy-%MM-%dd",
		maxDate:QJ_END_TIME.getValue(),
		onpicked:function(){
			countDay();
		}
	});
});

jQuery(QJ_END_TIME.obj).addClass("Wdate").unbind("click").bind("click", function(){
	WdatePicker({
		dateFmt:"yyyy-MM-dd",
		startDate:"%yyyy-%MM-%dd",
		minDate:QJ_START_TIME.getValue(),
		onpicked:function(){
			countDay();
		}
	});
});

/**计算出差天数*/
function countDay(){
		if(QJ_START_TIME.getValue().length> 0 && QJ_END_TIME.getValue().length>0){
		QJ_NUMBER.setValue(1+rhDate.doDateDiff("D", QJ_START_TIME.getValue(), QJ_END_TIME.getValue(), 0));
		}
}
