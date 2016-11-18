var _viewer = this;
//合同查询双击列表跳到合同起草卡片页面

_viewer.grid.unbindTrdblClick();
_viewer.grid.dblClick(function(value,node){
	
	var CT_DATA = FireFly.doAct("LW_CT_CONTRACT","byid",{"_PK_":value},false,false);
	if(CT_DATA.CT_CHANGE_ID.length>0){
		
		var params={};
		var options = {
				"url" : "LW_CT_CONTRACT_CHANGE.card.do?pkCode=" + value+"&readOnl=true",
				"tTitle" : "合同变更审批单",
				"params" : params,
				"menuFlag" : 3
			};
		 Tab.open(options);	
	}else{
		
		var params={};
		var options = {
				"url" : "LW_CT_CONTRACT.card.do?pkCode=" + value+"&readOnl=true",
				"tTitle" : "合同审批单",
				"params" : params,
				"menuFlag" : 3
			};
		 Tab.open(options);	
	}
	
})


