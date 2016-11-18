var _viewer=this;

jQuery("th[icode='startVote']").remove();
jQuery("th[icode='seeVote']").attr("colspan","2").text("操作");
var startVoteBtn=_viewer.grid.getBtn("startVote");
if(startVoteBtn){
	startVoteBtn.unbind("click").bind("click",function(){
		var id=jQuery(this).attr("rowPk");
		var param = {};
		param["SELECTION_ID"] = _viewer.grid.getRowItemValue(id, "SELECTION_ID");
		FireFly.doAct("BN_VOTE_SELECTION","votedCheck",param,false,false,function(data){
			//alert(data["HAS_VATE"]);
			if (data["HAS_VATE"] && data["HAS_VATE"] == "yes") {
				alert("您已投票，感谢您的参与！");
			
			}else{
				
				var selectionID=_viewer.grid.getRowItemValue(id,"SELECTION_ID");
				var opts = {
						"url":"BN_VOTE_SELECTION.startVote.do?data={'SELECTION_ID':'"+selectionID+"'}",
						"tTitle":"发起投票",
						"menuFlag":3,
						"top":true
						
				};
				Tab.open(opts);
			}
		});
	});
}

var seeVoteBtn=_viewer.grid.getBtn("seeVote");
if(seeVoteBtn){
	seeVoteBtn.unbind("click").bind("click",function(){
		var id=jQuery(this).attr("rowPk");
	
		var selectionID=_viewer.grid.getRowItemValue(id,"SELECTION_ID");
		var opts = {	
				"url":"BN_VOTE_SELECTION.showVoteResult.do?data={'SELECTION_ID':'"+selectionID+"'}",
				"tTitle":"预览投票结果",
				"menuFlag":3,
				"top":true
				
		};
		Tab.open(opts);
	});
}