/* 知道问题分类中动态加载精选答案的JS */
jQuery(document).ready(function(){
	jQuery(".js-best-answer").bind("click",function(){
		
		var askQId = jQuery(this).attr("qId");
		
		//判断是否已经加载过了！
		if(jQuery.trim(jQuery("#tr-"+askQId).text()) == ''){
			var param = {}
			param["id"] = askQId;
			param["sort"] = " A_BEST DESC,A_LIKE_VOTE DESC";
			param["count"] = 1;
			var answer = FireFly.doAct("SY_COMM_ZHIDAO","getAnswerList",param,false)["_DATA_"][0];
			//console.debug(tag_list);
			
			var anonyStr = "";
			if(answer["A_ANONY"] == 1){
				anonyStr =  '<a data-tip="p$t$leonardshar" class="zm-item-link-avatar">'+
				'<img src="/sy/theme/default/images/common/rh-male-icon.png" class="zm-list-avatar">'+
				'</a>'+ 
				'<a data-tip="p$t$leonardshar" title="">匿名用户</a>';
			}else{
				anonyStr =  '<a data-tip="p$t$leonardshar" class="zm-item-link-avatar" >'+ 
				'<img src="'+answer["S_USER__IMG"]+'" class="zm-list-avatar">'+
				'</a>'+ 
				'<a data-tip="p$t$leonardshar" title="">'+answer["S_USER__NAME"]+'</a>'+
				'<strong title="'+answer["S_DEPT__NAME"]+'"	class="zu-question-my-bio">'+
				answer["S_DEPT__NAME"]+
				'</strong>';
			}
			
			var voteStr = "";
			if(answer["A_LIKE_VOTE"] != 0){
				voteStr += "，来自&nbsp;";
				var tag_list = FireFly.doAct("SY_COMM_ZHIDAO","getAnswerLikeVote",{"answerId":answer["A_ID"],"best":"best"},false)["_DATA_"];
				//console.debug(tag_list);
				jQuery(tag_list).each(function(item_index,item){
					if(item_index == 0){
						voteStr += item["VOTE_USER__NAME"];
					}else{
						if(item_index == 2){
							voteStr +=  '<a href="javascript:void(0);" class="more_a">&nbsp;更多</a>'+
							'<span style="display:none;">';
						}
						voteStr += '、'+item["VOTE_USER__NAME"];
					}
				});
				if(tag_list.length > 3){
					voteStr += '</span>';
				}
			}
			
			var counterStr = "";
			if(answer["A_COMMENT_COUNTER"] > 0){
				counterStr +=   '<a id="answer-'+answer["A_ID"]+'"  name="addcomment" class=" meta-item zu-question-answer-meta-comment">'+ 
				'<i class="z-icon-comment"></i>'+answer["A_COMMENT_COUNTER"]+' 条评论 </a>';
			}else{
				counterStr +=   '<a id="answer-'+answer["A_ID"]+'"  name="addcomment" class=" meta-item zu-question-answer-meta-comment">'+
				'<i class="z-icon-comment"></i>添加评论 </a>';
			}
			
			//清空div
			jQuery("#tr-"+askQId).html("");
			
			jQuery("#tr-"+askQId).append(
					'<td colspan=4 style="text-align:left;">'+
					'<div id="zh-question-answer-wrap" class="zh-question-answer-wrapper navigable">'+
					'<div tabindex="-1" class="zm-item-answer " data-aid="1530099" data-atoken="16002573" data-collapsed="0" data-created="1358856931" data-deleted="0" data-isfriend="0" data-helpful="1" data-isowner="0" data-score="16.12788">'+
					'<a class="zg-anchor-hidden" name="1530099"></a>'+
					
					'<div class="zm-votebar">'+
					'<button id="like-'+answer["A_ID"]+'" data-tip="s$r$赞同" aria-pressed="false" class="up">'+
					'<span class="vote-arrow"></span>赞同'+
					'</button>'+
					'<button id="unlike-'+answer["A_ID"]+'" data-tip="s$r$反对，不会显示你的姓名"	aria-pressed="false" class="down">'+
					'<span class="vote-arrow"></span>反对，不会显示你的姓名'+
					'</button>'+
					'</div>'+
					
					'<div class="zm-item-vote">'+
					'<a name="expand" class="zm-item-vote-count" href="javascript:;" data-votecount="9">9</a>'+
					'</div>'+
					'<div class="answer-head">'+
					'<div class="zm-item-answer-author-info">'+
					'<h3 class="zm-item-answer-author-wrap">'+
					anonyStr+
					'</h3>'+
					'</div>'+
					'<div id="vote-'+answer["A_ID"]+'" class="zm-item-vote-info " data-votecount="9">'+
					'<span>'+answer["A_LIKE_VOTE"]+' 票</span>'+
					voteStr+
					'</div>'+
					'</div>'+
					'<div class="zm-item-rich-text" data-resourceid="'+answer["A_ID"]+'" data-action="/answer/content">'+
					'<div class="zm-editable-content clearfix">'+
					answer["A_CONTENT"]+
					'</div>'+
					'</div>'+
					'<a class="zg-anchor-hidden ac" name="1530099-comment"></a>'+
					'<div class="zm-item-meta zm-item-comment-el answer-actions clearfix">'+
					'<span class="answer-date-link-wrap">'+
					'<a class="answer-date-link meta-item" target="_self" >'+
					Format.limit(10, answer["S_ATIME"])+
					'</a>'+
					'</span>'+
					counterStr+								  
					'<div class="panel-container">'+
					'<div class="question-invite-panel" style="display: none;"></div>'+
					'<div id="comment-'+answer["A_ID"]+'" class="zm-comment-box" data-count="2"></div>'+								
					'<script type="text/javascript">'+
					'bindAnswerComment(\''+answer["A_ID"]+'\',\'answer-'+answer["A_ID"]+'\',\'comment-'+answer["A_ID"]+'\');'+
					'</script>'+
					'</div>'+
					
					'<span class="copyright zu-autohide"></span>'+
					
					'</div>'+
					'</div>'+
					'</div>'+
					'</td>'
			);
			
			//赞同按钮
			var likeBtn = jQuery("#like-"+answer["A_ID"]);
			likeBtn.live("click", function() {
				var data = {};
				data["_PK_"] = answer["A_ID"];
				var likeVoteCount = answer["A_LIKE_VOTE"];
				var resultData = parent.FireFly.doAct("SY_COMM_ZHIDAO_ANSWER", "increaseLikevote", data);
				if (resultData["_MSG_"] && resultData["_MSG_"].indexOf("OK") == 0) {
					var afterVote = Number(likeVoteCount) + 1;
					Tip.show("操作成功!");
					
					jQuery("#vote-"+answer["A_ID"]).find("span").first().text(afterVote + " 票");
				} else {
					Tip.showError(resultData["_MSG_"], true);
				}
			});							
			
			//反对按钮
			var unlikeBtn = jQuery("#unlike-"+answer["A_ID"]);
			unlikeBtn.live("click", function() {
				var data = {};
				data["_PK_"] = answer["A_ID"];
				var unlikeVoteCount = answer["A_DISLIKE_VOTE"];
				var resultData = parent.FireFly.doAct("SY_COMM_ZHIDAO_ANSWER", "increaseUnlikevote", data);
				if (resultData["_MSG_"] && resultData["_MSG_"].indexOf("OK") == 0) {
					var afterVote = Number(unlikeVoteCount) + 1;
					Tip.show("操作成功!");
				} else {
					Tip.showError(resultData["_MSG_"], true);
				}
			});
		}
	});
	
	
});