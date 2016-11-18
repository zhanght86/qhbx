<div id="tinyPanel-wrapper" class="tiny-vertical" style="height:${hei};width:auto;margin:4px auto;position:relative;">
	
    <a title="问卷" id="webPre" style="filter:alpha(opacity=0);color:#1b1b1b;background-color:black;position:absolute;top:0px;left:0px;width:30%;height:${hei}; display: block;" href="javascript:openContacturl('W');">问卷</a>
	<a title="评优" id="webNext" style="filter:alpha(opacity=0);color:#1b1b1b;background-color:black;position:absolute;top:0px;right:0px;width:70%;height:${hei}; display: block;" href="javascript:openContacturl('P');">评优</a>
	<div style="width:auto;">
		<a href="#">
			 <img style='border: 0;width:${width}; height:${hei};' src="${src}" alt="" />
		</a>
	</div>
</div>

<script>
jQuery(document).ready(function(jQuery) {
		var boxHtml = jQuery("#${id}__temp #tinyPanel-wrapper").html();
		jQuery("#${id}__temp").html("<div id='tinyPanel-wrapper' class='tiny-vertical' style='height:${hei};width:auto;margin:4px auto;position:relative;'>" + boxHtml + "</div>");
	});
function openContacturl(href) {
				var url = "";
				var tTitle = "";
				if(href=="W"){
					var result = FireFly.doAct("BN_QS_INVEST","query",{"_SELECT_":"INVEST_ID","_ORDER_":"S_ATIME desc"});
					var data = result["_DATA_"];
					if(data){
						url = "BN_QS_INVEST.showQuestionNaire.do?INVEST_ID="+data[0].INVEST_ID;;
						tTitle = "问卷调查";
					}
				}else if(href=="P"){
					var result = FireFly.doAct("BN_VOTE_SELECTION","query",{"_SELECT_":"SELECTION_ID","_ORDER_":"S_ATIME desc"});
					var data = result["_DATA_"];
					if(data){
						url = "BN_VOTE_SELECTION.startVote.do?SELECTION_ID="+data[0].SELECTION_ID;
						tTitle = "发起投票";
					}
				}else{
					url = "${SERV_ID}.list.do";
					tTitle = "${tTitle}";
				}
				var options = {"url":url,"tTitle":tTitle,"menuFlag":3,top:true};
				var tabP = jQuery.toJSON(options);
				tabP = tabP.replace(/\"/g,"'");
				/*window.open("/sy/comm/page/page.jsp?openTab="+(tabP));*/
				Tab.open(options);
}
function openLocation() {
	var opts = {"url":"SY_COMM_WORKLOC.show.do?func=view","tTitle":"工位图","params":'',"menuFlag":3};
    parent.Tab.open(opts); 
}
</script>