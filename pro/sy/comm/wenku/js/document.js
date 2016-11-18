jQuery(document).ready(function(){
		function doDocList(id,name){
			var url = "/wenku/doclist/" + id + "/index.html?group="+$group;	
			var opts={'scrollFlag':true , 'url':url,'tTitle':name,'menuFlag':1};
			top.Tab.open(opts);
		}
		/* 监听左侧“展开边栏”按钮 */
		var status = top.jQuery(".leftMenu-expand,.leftMenu-close");
		if(!status.hasClass("leftMenu-expand")){

			//jQuery("table.w980").css("width","100%");
		}

		var imgSrc = parent.System.getVar("@USER_IMG@");
		$("#currentUsImg").attr("src",imgSrc);
		$("#currentUsName").text(parent.System.getVar("@USER_NAME@"));
		
		/* 阅读+1*/
		top.FireFly.doAct("SY_COMM_WENKU_DOCUMENT","increaseReadCounter",{"DOCUMENT_ID":$docId},false,true);
		
		/* 下载+1*/
		jQuery("#downloadId2").click(function(){
			top.FireFly.doAct("SY_COMM_WENKU_DOCUMENT","increaseDownloadCounter",{
				"DOCUMENT_ID": $docId
			},false,true);
		});
		
		/* 评论_begin */
		(function wenku(){
			var opts = {"DATA_ID": $docId, "SERV_ID":"SY_COMM_WENKU_DOCUMENT", "SHOWNUM":10,"NOWPAGE":1,"pCon":jQuery("#document_comment")};
		    var listView = new rh.vi.comment(opts);
		    listView.show();
		})();
		/* 评论_end */
		
		jQuery("#wenji").click(function(event){
			
			//1.构造查询选择参数
			var configStr = "SY_COMM_WENKU_DOCLIST,{'SOURCE':'LIST_ID~LIST_TITLE'," +
					                     "'EXTWHERE':' and 	S_USER=^@USER_CODE@^','TYPE':'single','PKHIDE':'true'}";
			var options = {"itemCode":"我的文辑",
				"config" :configStr,
				"parHandler":this,
			    "replaceCallBack":function(idArray) {//回调，idArray为选中记录的相应字段的数组集合
			    	var id = idArray["LIST_ID"];
					var param = {
						"DOCUMENT_ID":"${data.DOCUMENT_ID}",
						"DOCUMENT_TITLE":"${data.DOCUMENT_TITLE}",
						"LIST_ID":id
					}
					//2.根据当前文档ID查询查询所选文辑中是否已存在
					var rtnBean = top.FireFly.doAct("SY_COMM_WENKU_DOCLIST_ITEM","count",param,false);
					if(rtnBean["_DATA_"]!="0"){
					//	alert("文辑中已存在");
						Tip.showError("文辑中已存在", true);
						return false;
					}
					var result = top.FireFly.doAct("SY_COMM_WENKU_DOCLIST_ITEM","save",param,false);
					if(result["_MSG_"]){
					//	alert("收藏到文辑成功");
						Tip.show("收藏成功");
					}
				}
			};
			//3.用系统的查询选择组件 rh.vi.rhSelectListView()
			var queryView = new top.rh.vi.rhSelectListView(options);
			queryView.show(event);
		
			var d = top.jQuery("#SY_COMM_WENKU_DOCLIST-selectDialog").parent("div");
			d.css({"z-index":"9999","background":"white","top":"40%","left":"40%","width":"400px","height":"240px","position":"absolute"});
		});
		
		
		//-------------------评分------------------------
		var votes = ["很差","较差","还行","推荐","力荐"];
		
		jQuery("#rating img").mouseover(function(){
			var _this = jQuery(this);
			jQuery("#rating img").attr("src","/sy/comm/wenku/img/star_1.png");
			jQuery("#currentRatingAvg").text(_this.attr("v")-0+1+".0");
			jQuery("#rating img:lt("+(_this.attr('v')-0+1)+")").attr("src","/sy/comm/wenku/img/star_01.png");
			jQuery("#rating .red").text(votes[_this.attr("v")-0]);
			jQuery("#rating img").unbind('click').click(function(){
				if(!jQuery("#currentRatingAvg").text()){
					return false;
				}
				var options = {
					"DATA_ID":$docId,
					"VOTE_SCORE":jQuery("#currentRatingAvg").text()
				}
				var result = top.FireFly.doAct("SY_COMM_WENKU_SCORE","voteScore",options,true);
				if(!!result["_MSG_"]){
					//不弹框，直接使用系统提示信息！
					//alert(result["_MSG_"]);
					jQuery("#rating .red").text("谢谢评分");
				}
				if(result["avg"]){
					jQuery("#currentRatingAvg").text(result["avg"]);
				}
				if(result["counter"]){
					jQuery("#ratingSummary").html(result["counter"]+"&nbsp;");
				}
				jQuery("#rating").attr("id","blue1");
				jQuery("#currentRatingAvg").attr("id","currentRatingAvg1");
			});
		});
		/* 判断是否已评 */
		var doc = top.FireFly.doAct("SY_COMM_WENKU_SCORE","show",{"_PK_":$docId});
		if(doc["VOTED"]==1){
			jQuery("#rating img").unbind("mouseover");
			jQuery("#rating .red").text("已点评");
		}
 
		//文档收藏
		/*jQuery(".js-favorite").on("click",function(){
			var dataId=jQuery(this).attr("dataId"),
				servId=jQuery(this).attr("servId"),
				act="";
			if(jQuery(this).html() == "收藏"){
				act="addFavorite";
				jQuery(this).html("取消收藏");
			}else{
				act="deleteFavorite";
				jQuery(this).html("收藏");
			}
			parent.FireFly.doAct("SY_COMM_WENKU_DOCUMENT", act, {"DATA_ID":dataId,"SERV_ID":servId});
		});*/
});