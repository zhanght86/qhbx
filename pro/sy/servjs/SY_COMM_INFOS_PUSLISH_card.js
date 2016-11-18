var _viewer = this;
//信息发布
var servId=_viewer.servId;


//$($(document).find('#SY_COMM_INFOS_PUSLISH-FENGMIANJI .file_uploadTable tr:first')).after("<span id='imgSize2' class='imgSize_c' style='color:red;'></span>");
$($(document).find('#'+servId+'-FENGMIANJI .file_uploadTable tr:first')).after("<span id='imgSize2' class='imgSize_c' style='color:red;'></span>");
	
	var CHNL_ID=_viewer.getItem("CHNL_ID").getValue();
	if(""!=CHNL_ID){
		findMsg(CHNL_ID);
	}

	//选择栏目
	_viewer.getItem("CHNL_ID").change(function(){
		var chnl_ID=_viewer.getItem("CHNL_ID").getValue();
		findMsg(chnl_ID);
	})
	
	
		//选择图片时执行
	_viewer.getItem("NEWS_TYPE").change(function(){
		newType();
	})


	//判断是否选择图片
	function newType(){
		var news_TYPE=_viewer.getItem("NEWS_TYPE").getValue()
		var chnl_ID=_viewer.getItem("CHNL_ID").getValue();
		if("2"==news_TYPE){
			var param = {};
			param["_PK_"]=chnl_ID;
			var returnDate =FireFly.doAct("SY_COMM_INFOS_CHNL", "byid", param, true);
			var msg=returnDate.CHNL_IMG_SIZE;
			emptyMsg();
			addmsg(msg);
		}else{
			emptyMsg();
		}
	}
	
	
	function findMsg(CHNL_ID){
		var param = {};
		param["_PK_"]=CHNL_ID;
		var returnDate =FireFly.doAct("SY_COMM_INFOS_CHNL", "byid", param, true);
		var msg=returnDate.CHNL_IMG_SIZE;
		
		if(""!=msg){
			_viewer.getItem("NEWS_TYPE").setValue(2);
			emptyMsg();
			addmsg(msg);
		}else{
			_viewer.getItem("NEWS_TYPE").setValue(1);
			emptyMsg();
		}
	}
	
	
	
	
	
	
	
	//写成方法 自定义内容
	function addmsg(msg){
		$($(document).find('#'+servId+'-FENGMIANJI .file_uploadTable .imgSize_c')).text(msg);
	}
	
	function emptyMsg(){	
		$($(document).find('#'+servId+'-FENGMIANJI .file_uploadTable .imgSize_c')).text("");
	}
	


//设置可查看中文名
//将NEWS_OWNER中的编码用NEWS_OWNER_NAME中的中文替换掉 
/*try{
	var newsOwnerItem = _viewer.form.getItem('NEWS_OWNER');
	if (newsOwnerItem && newsOwnerItem.type=='DictChoose' && !newsOwnerItem.isHidden) {
		newsOwnerItem.setText(_viewer.form.getItem('NEWS_OWNER_NAME').getValue());
	}
}catch(e){
	console.error("SY_COMM_INFOS_PUSLISH_card.js:" + e.message);
}*/


var newsOwner = _viewer.getItem("NEWS_OWNER").getValue(); // 可查看人

var newsScopeObj = _viewer.getItem("NEWS_SCOPE");
var data = {"NEWS_OWNER": newsOwner};

var outbean = FireFly.doAct(_viewer.servId,"getNewsOwner",data,true,false,function(){});
//alert(jQuery.toJSON(outbean["_DATA_"]));
var newsOwnerName = "";
$.each (outbean["_DATA_"],function (index,item) {//给文本域显示可查看人名字
	if (index != outbean["_DATA_"].length-1) {
		
		newsOwnerName = newsOwnerName+item.NAME+",";
	}else{
		newsOwnerName = newsOwnerName+item.NAME;
	}
	
});

_viewer.getItem("NEWS_OWNER").getContainer()
.find("#SY_COMM_INFOS_PUSLISH-NEWS_OWNER__NAME").val(newsOwnerName);


newsScopeObj.obj.change(function() {
	if (newsScopeObj.getValue() == "5") {
		
		_viewer.getItem("NEWS_OWNER").setValue(newsOwner);
		_viewer.getItem("NEWS_OWNER").getContainer()
			.find("#SY_COMM_INFOS_PUSLISH-NEWS_OWNER__NAME").val(newsOwnerName);
		
		// 重写字典选择事件
		_viewer.getItem("NEWS_OWNER").getContainer()
			.find('textarea, .iconChoose')
			.unbind('click').bind('click', function() {
			
			var dictOpt = {
					"ROLE"		:	{"id":"SY_ORG_ROLE","showcheck":false},
					"DEPT_USER"	:	{"id":"SY_ORG_DEPT_USER_ALL","showcheck":false}
					};
			var options = {
					"servId"	:	"SY_COMM_INFOS_PUSLISH",
					"dataId"	:	_viewer.getPKCode(),
					"TYPE"		:	"multi",
					"uniteTree"	:	dictOpt, // 必须参数，确定哪些字典组合
					"parHandler":	_viewer,
					"replaceCallBack" : function (id, value, prefixId) {
						_viewer.getItem('NEWS_OWNER').setValue(id);
						_viewer.getItem('NEWS_OWNER').setText(value);
					}
			};
			var uniteTree = new rh.ui.UniteTree(options);
			uniteTree.open();
			uniteTree.setRightSelect(outbean["_DATA_"], '');
		});

	}
});

$(newsScopeObj.obj).trigger("change");



setTimeout(function() {
	$("body").find(".cardDialog").css(
			{
				"background-color" : "#f4f4f4"
			}
	).parent().removeClass("bodyBack_default").css(
			{
				"background-color" : "#f4f4f4"
			}
	).end().find(".item").css(
			{
				"background-color" : "#f4f4f4"
				//"margin-top": "0",
				//"margin-bottom": "0"
			}
	).find(".fieldsetContainer").css(
			{
				"background-color" : "#f4f4f4"
			}
	).end().parent().css(
			{
				"background-color" : "#f4f4f4"
			}
	).end().find(".formContent");
	
	var $editerBody = $(_viewer.getItem("NEWS_BODY").getContainer()).find("iframe").contents().find("body");
	
	$editerBody.css(
			{
				//"width":"555px",
				"margin-left":"auto",
				"margin-right":"auto",
				"background-color":"#fff",
				//"margin-top":"80px",
				"background":"url('/sy/comm/infos/img/wordbg.gif') no-repeat scroll transparent",
				"background-position":"center 55px"
			}
	);
},200);


