jQuery(document).ready(function(){
	/* 知道 */
	var result = parent.FireFly.doAct("SY_COMM_ZHIDAO_QUESTION","getMyInfo");
	
	var integral=result.USER_INTEGRAL,
		answcount=result.ANSWER_COUNT,
		questcount=result.QUESTION_COUNT,
	    answlist=result.ANSWER_LIST,
		quesList=result.QUESTION_LIST;
	//回答数量
	jQuery(".js-answer").html(answcount);
	//提问数量
	jQuery(".js-question").html(questcount);
	//积分
	jQuery(".js-integral").html(integral);
	
	 //我的回答
	jQuery.each(answlist,function(index,obj) {
		jQuery("<div class='zd-grid-list'>"+
			        "<div class='nomal wp55 pl'>"+
			    		"<a href='#' title='"+obj.Q_TITLE+"' target='_self'>"+Format.limit(20, obj.Q_TITLE)+"</a>"+
				    "</div>"+
				    "<div class='nomal wp10'>"+obj.A_COMMENT_COUNTER+"</div>"+
				    "<div class='nomal wp10'>"+obj.A_LIKE_VOTE+"</div>"+
				    "<div class='nomal wp10'>"+obj.A_DISLIKE_VOTE+"</div>"+
				    "<div class='nomal wp10'>"+obj.S_ATIME.split(" ")[0]+"</div>"+
				"</div>").appendTo(jQuery("#js-answ-list"))
						 .find("a").on("click",function(){
	   		   				view(obj.Q_ID, obj.Q_TITLE);
				});
	});
	 //我的提问
	jQuery.each(quesList,function(index,obj) {
		jQuery("<div class='zd-grid-list'>"+
			        "<div class='nomal wp55 pl'>"+
			    		"<a href='#' title='"+obj.Q_TITLE+"' target='_self'>"+Format.limit(20, obj.Q_TITLE)+"</a>"+
				    "</div>"+
				    "<div class='nomal wp40'>"+obj.S_MTIME.split(" ")[0]+"</div>"+
				"</div>").appendTo(jQuery("#js-ques-list"))
				.find("a").on("click",function(){
		   				view(obj.Q_ID, obj.Q_TITLE);
		});
	});
   

});