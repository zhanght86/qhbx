/**
 * 知道提问页面，在保存成功后自动跳转
 */
var _viewer = this;

//保存后执行
_viewer.afterSave = function() {
	var qId = _viewer.getItem("Q_ID").getValue();
	var qTitle = _viewer.getItem("Q_TITLE").getValue();
	
	//打开新页面
	FireFly.doAct("SY_COMM_ZHIDAO_QUESTION","increaseReadCounter",{"Q_ID":qId});
	var url = "/cms/SY_COMM_ZHIDAO_QUESTION/" + qId + ".html" ;
    var opts={'scrollFlag':false , 'url':url,'tTitle':qTitle,'menuFlag':3};
    Tab.open(opts);
	
    //关闭原来页面
	Tab.close();
	
};
