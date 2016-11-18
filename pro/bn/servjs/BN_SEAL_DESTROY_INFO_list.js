var _viewer = this;
var num=_viewer.grid.getCheckBox().length;
debugger;
var servId=$("#BN_SEAL_DESTROY-ID").val();
sealtListView();
function sealtListView(){
	var flg=false;
	FireFly.doAct("BN_SEAL_BASIC_INFO","getSaveSealList","",true,false,function(result){	
	flg=result.sealList!="";
});
	if((num==0)&&flg){
		/*
		 * 根据session中保存的印章列表信息来拼写查询条件并刷新到页面显示
		 */
	FireFly.doAct("BN_SEAL_BASIC_INFO","getSaveSealList","",true,false,function(result){				
					var sealList=result.sealList.split(",");
					if(sealList==""){
						var sealListStr="and BN_SEAL_BASIC_INFO.ID=''";
					}else{
							var sealListStr="and BN_SEAL_BASIC_INFO.ID='";
					var sqlStr="' or BN_SEAL_BASIC_INFO.ID='";
					for(var i=0;i< sealList.length;i++){
						if(i<sealList.length-1){
							sealListStr=sealListStr+sealList[i]+sqlStr;
						}else{
							sealListStr=sealListStr+sealList[i]+"'";
						}
						}
					}
	var opts = {
	 _linkWhere:'ADD',
	 sealListStr:sealListStr};
     _viewer.refreshGridBodyNoResize(opts);
			});	

}
}

/**
 * 添加按钮(注意未保存页面与保存后页面区别)
 */
$(_viewer.getBtn("add")).unbind("click").bind("click",function(event) {
	var inputName="LIST_FIELDS";   
	var configStr = "BN_SEAL_BASIC_INFO,{'TARGET':'ID~SEAL_CODE~SEAL_NAME~SEAL_STATE~SEAL_OWNER_USER~KEEP_TDEPT_CODE','HIDE':'ID','HIDEOPTION':'ID','SOURCE':'ID~SEAL_CODE~SEAL_NAME~SEAL_STATE~SEAL_OWNER_USER~KEEP_TDEPT_CODE','EXTWHERE':'and SEAL_STATE!=4'}";	
	//var configStr = "CS_BGS_AIRTICKET_BASE_LIST,{'TARGET':'"+inputName+"~','SOURCE':'AT_ID~AT_FLIGHT_CODE~AT_TRAVELER_NAME~AT_STARTING~AT_DESTN~CONTACT_INFO~AT_DEPARTURE_TIME~AT_TOTAL_AMOUNT','EXTWHERE':'' ,'TYPE':'multi'}";	
   	var options = {"config" :configStr,"rebackCodes":"ID~SEAL_NAME~SEAL_STATE","parHandler":this,"formHandler":this,"replaceCallBack":function(result){
			var actCodes = result.ID.split(",");
			var actNames = result.SEAL_NAME.split(",");
			//未保存页面的添加按钮，需要把选中的信息添加到session中的ticketList（重新拼写覆盖）
		    if((_viewer.getParHandler().getPKCode()=="ADD")||(_viewer.getParHandler().getPKCode()=="ADD1")){
		    	var sealAddList="";
		    	var SEAL_ID=new Array();//存放选中的主键ID值
		    	//session中已有的印章信息添加进ticketAddList
		    	FireFly.doAct("BN_SEAL_BASIC_INFO","getSaveSealList","",true,false,function(result){				
		    		sealAddList=result.sealList;
				     for(var i=0;i < actCodes.length;i++){
				     	if(sealAddList.indexOf(actCodes[i])<0)//防止重复
				     		sealAddList=sealAddList+","+actCodes[i];
		    	}
					});  
					//调用后台方法把印章信息存进session的ticketList
		    	FireFly.doAct("BN_SEAL_BASIC_INFO","saveSealList",{"sealList" : sealAddList},true,false);
			    //ticketListView();
		    	//_viewer.refresh();
		    	}
		 //已保存页面的添加
				else{
					for(var i=0;i < actCodes.length;i++){
				    var data={DEST_ID:servId,SEAL_ID:actCodes[i]};
				   /* var data1={"_PK_":actCodes[i],SEAL_STATE:4,SEAL_DESTORY_TIME:rhDate.getCurentTime()};
				    FireFly.cardModify("BN_SEAL_BASIC_INFO",data1)//更新印章基本信息表中印章状态           	
*/            	    FireFly.doAct(_viewer.servId,"save",data, true);//添加到印章销毁关联表       
				}	
				 _viewer.getParHandler().refresh();
		    }
		   _viewer.refresh();
		},"hideAdvancedSearch":true};
		var queryView = new rh.vi.rhSelectListView(options);
		queryView.show(null,[50,0]);

});


/**
 * 删除按钮（同添加按钮，也需要区别保存和未保存页面）
 */
$(_viewer.getBtn("delete")).unbind("click").bind("click",function(event) {
	if($("input:checked").length==0){
		alert("您未选中印章信息！");
	}else{
	var ID=_viewer.grid.getSelectPKCodes();//获取选中的主键值数组
   if((_viewer.getParHandler().getPKCode()=="ADD")||(_viewer.getParHandler().getPKCode()=="ADD1")){
	 	var SEAL_ID=new Array();
	 	FireFly.doAct("BN_SEAL_BASIC_INFO","getSaveSealList","",true,false,function(result){				
	 		SEAL_ID=result.sealList.split(",");
	 		for(var i=0;i<ID.length;i++){
	 			for(var j=0;j<SEAL_ID.length;j++){
			 		if(ID[i]==SEAL_ID[j]){
			 			SEAL_ID.splice(j,1);
			 		}
	 			}
	 		}
}); 
/*
 * 获取选中的印章ID
 */
	/*$("input:checked").val(function(){
					if(jQuery(this).attr("id")){
						var id=jQuery(this).attr("id");
		                var sealId=id.substr(5);
		                SEAL_ID.pop(sealId);
		               }
		              });*/	
		    	FireFly.doAct("BN_SEAL_BASIC_INFO","saveSealList",{"sealList" : SEAL_ID.toString()},true,false);
		    	SEAL_ID.splice(0,SEAL_ID.length);//清空数组
	 	}else{
	 		if(ID.length==0){
	 			alert("至少要有一条印章信息！");
	 		}else{
	        var data={"_PK_":ID.toString()};
	        FireFly.doAct(_viewer.servId,"delete",data, true,false,function(result){
	        	/*var IDS=new Array();
		    for(var i=0;i<result._OKCOUNT_;i++){
			IDS.push(result._DATA_[i].ID);
			}*/
			/*for(var i=0;i<IDS.length;i++){
				var data1={"_PK_":IDS[i],AT_STATUS:0};
				FireFly.cardModify("BN_SEAL_BASIC_INFO",data1)//更新印章基本信息表中印章状态        
				}*/ 
				});
	 		}
		}
		_viewer.refresh();
		}
});

