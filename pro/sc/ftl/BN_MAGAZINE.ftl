<div id="tinyPanel-wrapper" class="tiny-vertical" style="height:${hei};width:auto;margin:4px auto;position:relative;">
	
    <!--
	<div title="百年内刊" style="width:48%;float:left;">
	<div title="上一期" id="nkpred" style="width:35px;height:35px;position:absolute;top:80px;left:0px;background:url('/bn/style/images/bnnk_arrow_left.png') no-repeat"></div>
		<a href="javascript:void(0);" news_id="">
			 <img id="leftPic" style='border: 0;height:${imgHeight};' src="${src}" alt="" />
		</a>
	</div>
	
	
	<div style="width:7px;background-color:white;float:left;">&nbsp;</div>
	
	-->
	
	
	
	 <div title="百年内刊" id="p" style="width:48%;float:left;">
	 <div title="下一期" id="nknext" style="width:35px;height:35px;position:absolute;top:80px;right:0px;background:url('/bn/style/images/bnnk_arrow_right.png') no-repeat;"></div>
		<a href="javascript:void(0);" news_id="">
			 <img id="rightPic" periodNum="0" style='border: 0;height:${imgHeight};' src="/bn/style/images/u171.jpg" alt="" />
		</a>
	</div> 
	<!--<div title="百年内刊" id="w" style="width:48%;float:left;display:none;">
		<a href="javascript:openContacturl('P');">
			 <img style='border: 0;height:${imgHeight};' src="${src}" alt="" />
		</a>
	</div>-->
</div>

<script>
jQuery(document).ready(function(jQuery) {
/*将内刊图片及新闻ID信息存入数组*/
var newsArr=new Array();
FireFly.doAct("SY_COMM_INFOS", "getJiaoDianNews", {"_ROWNUM_":12,"SITE_ID":"SY_COMM_SITE_INFOS","CHNL_ID":"0zxAeymodc48XnETTylY1K","_ORDER_":"NEWS_TIME desc","HAS_FENGMIAN":1},false,false,function(result){
for(var i=0;i<result._DATA_.length;i++){
	newsArr.unshift({"imgUrl":"/file/"+result._DATA_[i].picture.FILE_ID,"newsId":result._DATA_[i].NEWS_ID});
}
});


/*左箭头隐藏*/
$("#nkpred").fadeTo("fast",0.0,function(){});
/*左箭头绑定悬浮事件*/
$("#nkpred").hover(function(){
	$("#nkpred").fadeTo("fast",1.0,function(){});
},function(){
   $("#nkpred").fadeTo("fast",0.0,function(){});
	});
/*右箭头隐藏*/
$("#nknext").fadeTo("fast",0.0,function(){});
/*右箭头绑定悬浮事件*/

/*
$("#nknext").hover(function(){
	$("#nknext").fadeTo("fast",1.0,function(){});
},function(){
   $("#nknext").fadeTo("fast",0.0,function(){});
	});
*/


	
/*初始化图片以及绑定事件*/

$("#rightPic").attr("src",newsArr[newsArr.length-1].imgUrl);
$("#leftPic").attr("src",newsArr[newsArr.length-2].imgUrl);

$("#leftPic").parent().unbind("click").bind("click",function(){
	if(parseInt($("#rightPic").attr("periodNum"))==newsArr.length-2){
		window.open("/bn/link/cms/aeonNews/intertek/index.jsp","_blank");
	}else{
		var url = "/cms/SY_COMM_INFOS/" + newsArr[newsArr.length-2].newsId + ".html";		
		window.open(url);
	}
});
$("#rightPic").parent().unbind("click").bind("click",function(){
		var url = "/cms/SY_COMM_INFOS/" + newsArr[newsArr.length-1].newsId + ".html";		
		window.open(url);
});
/*初始化图片以及绑定事件结束*/
/*左箭头绑定点击事件*/
$("#nkpred").click(function(){
	if(parseInt($("#rightPic").attr("periodNum"))>=(newsArr.length-2)){
	}else{
		$("#rightPic").attr("src",newsArr[newsArr.length-(parseInt($("#rightPic").attr("periodNum"))+2)].imgUrl);
		/*$("#rightPic").parent().attr("news_id",newsArr[newsArr.length-(parseInt($("#rightPic").attr("periodNum"))+2)].newsId);*/
		$("#leftPic").attr("src",newsArr[newsArr.length-(parseInt($("#rightPic").attr("periodNum"))+3)].imgUrl);
		/*$("#leftPic").parent().attr("news_id",newsArr[newsArr.length-(parseInt($("#rightPic").attr("periodNum"))+3)].newsId);*/
		
		$("#leftPic").parent().unbind("click").bind("click",function(){
			if(parseInt($("#rightPic").attr("periodNum"))==newsArr.length-2){
				window.open("/bn/link/cms/aeonNews/intertek/index.jsp","_blank");
			}else{
				var url = "/cms/SY_COMM_INFOS/" + newsArr[newsArr.length-(parseInt($("#rightPic").attr("periodNum"))+2)].newsId + ".html";		
				window.open(url);
			}
		});
		$("#rightPic").parent().unbind("click").bind("click",function(){
				var url = "/cms/SY_COMM_INFOS/" + newsArr[newsArr.length-(parseInt($("#rightPic").attr("periodNum"))+1)].newsId + ".html";		
				window.open(url);
		});
		$("#rightPic").attr("periodNum",parseInt($("#rightPic").attr("periodNum"))+1);
	}
});
/*右箭头绑定点击事件*/
$("#nknext").click(function(){
	if(parseInt($("#rightPic").attr("periodNum"))<=0){
	}else{
		$("#rightPic").attr("src",newsArr[newsArr.length-(parseInt($("#rightPic").attr("periodNum")))].imgUrl);
		$("#rightPic").parent().attr("news_id",newsArr[newsArr.length-(parseInt($("#rightPic").attr("periodNum")))].newsId);
		$("#leftPic").attr("src",newsArr[newsArr.length-(parseInt($("#rightPic").attr("periodNum"))+1)].imgUrl);
		$("#leftPic").parent().attr("news_id",newsArr[newsArr.length-(parseInt($("#rightPic").attr("periodNum"))+1)].newsId);
		
		$("#leftPic").parent().unbind("click").bind("click",function(){
				var url = "/cms/SY_COMM_INFOS/" + newsArr[newsArr.length-(parseInt($("#rightPic").attr("periodNum"))+2)].newsId + ".html";		
				window.open(url);
		});
		$("#rightPic").parent().unbind("click").bind("click",function(){
				var url = "/cms/SY_COMM_INFOS/" + newsArr[newsArr.length-(parseInt($("#rightPic").attr("periodNum"))+1)].newsId + ".html";		
				window.open(url);
		});
		$("#rightPic").attr("periodNum",parseInt($("#rightPic").attr("periodNum"))-1);
	}
});
});
/*打开新闻*/
	function newsViewNK(element){
		var url = "/cms/SY_COMM_INFOS/" + $(element).attr("news_id") + ".html";			
		window.open(url);
	}
function openContacturlp(h,r) {
/*jQuery("#"+h).css('display','none');
	jQuery("#"+r).css('display','block');
	window.location.href="/bn/link/cms/aeonNews/intertek/index.jsp";*/
}
function openContacturl(href) {
				var url = "";
				var tTitle = "";
				if(href=="W"){
					var result = FireFly.doAct("BN_QS_INVEST","query",{"_SELECT_":"INVEST_ID","_ORDER_":"S_ATIME desc"});
					var data = result["_DATA_"];
					if(data){
						url = "BN_QS_INVEST.showQuestionNaire.do?INVEST_ID="+data[0].INVEST_ID;
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