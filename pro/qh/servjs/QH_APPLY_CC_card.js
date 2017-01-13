var _viewer = this;
var CC_START_TIME = _viewer.getItem("CC_START_TIME");
var CC_END_TIME = _viewer.getItem("CC_END_TIME");
var CC_NUMBER = _viewer.getItem("CC_NUMBER");
jQuery(CC_START_TIME.obj).addClass("Wdate").unbind("click").bind("click", function(){
	WdatePicker({
		dateFmt:"yyyy-MM-dd",
		startDate:"%yyyy-%MM-%dd",
		maxDate:CC_END_TIME.getValue(),
		onpicked:function(){
			countDay();
		}
	});
});

jQuery(CC_END_TIME.obj).addClass("Wdate").unbind("click").bind("click", function(){
	WdatePicker({
		dateFmt:"yyyy-MM-dd",
		startDate:"%yyyy-%MM-%dd",
		minDate:CC_START_TIME.getValue(),
		onpicked:function(){
			countDay();
		}
	});
});

/**计算出差天数*/
function countDay(){
		if(CC_START_TIME.getValue().length> 0 && CC_END_TIME.getValue().length>0){
		CC_NUMBER.setValue(1+rhDate.doDateDiff("D", CC_START_TIME.getValue(), CC_END_TIME.getValue(), 0));
		}
}

