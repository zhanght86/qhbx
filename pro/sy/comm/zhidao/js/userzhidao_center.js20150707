$(document).ready(function(){
	
		//判断当前登录用户和当前页面是否是一个人
		if(otherId != myCode){
			jQuery(".myself").hide();
		}
	 
		//1.查看是否已关注此人，据此设置关注按钮
		checkFollow();
		//2.加载最新动态
		
		
	 
 
	//对人的关注
	$("#zd-person-follow").on("click",function(event){
		var counter=$("#followedConter").html();
			counter=parseInt(counter,10);
		if($(this).hasClass("zd-btn-unfollow")){
			$(this).removeClass("zd-btn-unfollow").text("关注");
			//取消关注
			var data={
					"DATA_ID":otherId
			};
			parent.FireFly.doAct("SY_COMM_ZHIDAO", "deletePersonFollow", data,false,false,function(rawData){
				$("#followedConter").html(counter-1);
			});
		}else{
			$(this).addClass("zd-btn-unfollow").text("取消关注");
			//关注
			var data= {
					"DATA_ID"		: otherId
			};
			parent.FireFly.doAct("SY_COMM_ZHIDAO", "addPersonFollow", data,false,false,function(rawData){
				$("#followedConter").html(counter+1);
			});
		}
	});
	/**
	 * 关注主题
	 */
	$("#zd-ask-list").on("click",".zd-question-follow",function(){
		var $this=$(this);
		followQuestionHandler($this);
	})
	function followQuestionHandler($this){
		var data_id			  = $this.attr("data-id"),
		 	data_dis_name	  = $this.attr("data-dis-name"),
		    quesFollowCounter = $this.siblings(".quesFollowCounter"),
			followCount		  = 0;
		if(quesFollowCounter){
			followCount=parseInt(quesFollowCounter.text(),10);
		}
		
		if($this.hasClass("zd-unfollow")){
			$this.removeClass("zd-unfollow").find("span").text("关注主题");
			//取消关注
			var data={
					"DATA_ID":data_id
			};
			parent.FireFly.doAct("SY_COMM_ZHIDAO", "deleteQuestionFollow", data,false,false,function(rawData){
				if(quesFollowCounter){
					quesFollowCounter.html(followCount-1);
				}
			});
			 
		}else{
			$this.addClass("zd-unfollow").find("span").text("取消关注");
			//关注
			var data= {
					"DATA_ID"		: data_id
			};
			parent.FireFly.doAct("SY_COMM_ZHIDAO", "addQuestionFollow", data,false,false,function(rawData){
				if(quesFollowCounter){
					quesFollowCounter.html(followCount+1);
				}
			});
			 
		}
	}
	
	/**
	 * 查看是否已关注
	 */
	function checkFollow(){
		if(otherId==myCode){
			$("#zd-person-follow").hide();
			return false;
		}
		var data={
			"DATA_ID":otherId,
			"USER_CODE":myCode	
		};
		parent.FireFly.doAct("SY_COMM_ZHIDAO_PERSON_FOLLOW", "finds",data,false,false,function(result){
			if(result._DATA_.length > 0){
				//如果已关注，设置按钮样式，并保存_PK_
				$("#zd-person-follow").addClass("zd-btn-unfollow").text("取消关注");
			}
		});
	}
	function Pager(options){
		 if ( options ) {
	         $.extend(this.defaults, options);
	     }
	}
	Pager.prototype={
			defaults:{
					showNum 	: 10,
					currPage	: 1,
					totalPages  : 1
		    },
		    /**获取总页数**/
		    getTotalPages : function(){
		    	return this.defaults.totalPages;
		    },
		    /**设置总页数**/
		    setTotalPages : function(totalPages){
		    	this.defaults.totalPages=totalPages;
		    },
		    getCurrPage : function(){
		    	return this.defaults.currPage;
		    },
		    /**获取要显示的记录条数**/
		    getShowNum	: function(){
		    	return this.defaults.showNum;
		    },
		    /**获取下一页**/
			getNextPage : function(){
				this.defaults.currPage++;
				return this.defaults.currPage;
		    },
		    /**获取上一页**/
		    getPrevPage : function(){
			    this.defaults.currPage--;
				return this.defaults.currPage;
		    },
		    hasPrevPage : function(){
		    	return this.getCurrPage() > 1;
		    },
		    hasNextPage : function(){
		    	return this.getTotalPages() > this.getCurrPage();
		    }
		    
	};
	
		var answerPager=new Pager();
	 
		/**
		 * 我的回复 下一页
		 */
		$(".zd-profile-answer-wrap .zd-next").on("click",function(event){
			// 设置 此按钮  不可用
			if($(this).hasClass("zd-disabled")){
				return false;
			}
			var self=this;
			
			//获取下一页数据
			var param={
					userId:otherId,							// 所在知道个人页面的owner
					count:answerPager.defaults.showNum,		// 每页显示行数
					page:answerPager.getNextPage(),			// 获取下一页
					summaryLength:20						// 用于截取字符串
			};
			
			parent.FireFly.doAct("SY_COMM_ZHIDAO", "getAnswerList",param,false,false,function(result){
				// 设置总页数
				answerPager.setTotalPages(result._PAGE_.PAGES);
				if(result._DATA_.length > 0){
					// 如果有数据，显示 上一页 按钮
					$(self).next(".zd-prev").show();
					// 渲染数据
					$("#zd-answers-list").html("").append($("#js-myAnswer-Template").tmpl(result._DATA_));
				} 
			});
			
			//如果没有下一页，则此按钮设置为不可用
			if(!answerPager.hasNextPage()){
				$(this).addClass("zd-disabled");
			}
			
		});
		/**
		 * 我的回复 上一页
		 */
		$(".zd-profile-answer-wrap .zd-prev").on("click",function(event){
			var self=this;
			//获取下一页数据
			var param={
					userId:otherId,							// 所在知道个人页面的owner
					count:answerPager.defaults.showNum,		// 每页显示行数
					page:answerPager.getPrevPage(),			// 获取上一页
					summaryLength:20 						// 用于截取字符串
			};
			
			parent.FireFly.doAct("SY_COMM_ZHIDAO", "getAnswerList",param,false,false,function(result){
				// 设置总页数
				answerPager.setTotalPages(result._PAGE_.PAGES);
				if(result._DATA_.length > 0){
					// 渲染数据
					$("#zd-answers-list").html($("#js-myAnswer-Template").tmpl(result._DATA_));
				}
			});
			
			// 显示 下一页  按钮
			$(self).prev(".zd-next").removeClass("zd-disabled");
			
			// 如果没有上一页，则隐藏
			if(!answerPager.hasPrevPage()){
				$(this).hide();
			}
		});
		
		
		
		// 出题分页
	    var askPager=new Pager();
	 
		/**
		 *  我的出题 下一页
		 */
		$(".zd-profile-ask-wrap .zd-next").on("click",function(event){
			// 设置 此按钮  不可用
			if($(this).hasClass("zd-disabled")){
				return false;
			}
			
			var self=this;
			
			//获取下一页数据
			var param={	
					userId:otherId,						// 所在知道个人页面的owner
					count:askPager.defaults.showNum,	// 每页显示行数
					page:askPager.getNextPage(),        // 获取下一页
					isFollowCount:'isFollowCount'
			};
			
			parent.FireFly.doAct("SY_COMM_ZHIDAO", "getAskList",param,false,false,function(result){
				// 设置总页数
				askPager.setTotalPages(result._PAGE_.PAGES);
				
				if(result._DATA_.length > 0){
					// 如果有数据，显示 上一页 按钮
					$(self).next(".zd-prev").show();
					// 渲染数据
					$("#zd-ask-list").empty();
					$("#js-myAsk-Template").tmpl(result._DATA_).appendTo("#zd-ask-list");
				} 
			});
			
			if(!askPager.hasNextPage()){
				$(this).addClass("zd-disabled");
			}
			
		});
		/**
		 * 我的出题 上一页
		 */
		$(".zd-profile-ask-wrap .zd-prev").on("click",function(event){
			var self=this;
			//获取下一页数据
			var param={
					userId:otherId,						// 所在知道个人页面的owner
					count:askPager.defaults.showNum,	// 每页显示行数
					page:askPager.getPrevPage(),		// 获取上一页
					isFollowCount:'isFollowCount'
			};
			
			parent.FireFly.doAct("SY_COMM_ZHIDAO", "getAskList",param,false,false,function(result){
				// 设置总页数
				askPager.setTotalPages(result._PAGE_.PAGES);
				if(result._DATA_.length > 0){
					// 渲染数据
					$("#zd-ask-list").empty();
					$("#js-myAsk-Template").tmpl(result._DATA_).appendTo("#zd-ask-list");
				}
			});
			// 显示 下一页  按钮 
			$(self).prev(".zd-next").removeClass("zd-disabled");
			// 如果没有上一页，则隐藏
			if(!askPager.hasPrevPage()){
				$(this).hide();
			}
		});
	
		var activityPage=new Pager({showNum:15,currPage:0});
		/**
		 *  加载最新动态 
		 */
		function loadUserActivity(){
			
			var param={
					userId:otherId,						// 所在知道个人页面的owner
					count:activityPage.getShowNum(),	// 每页显示行数
					page:activityPage.getNextPage(),	// 获取下一页
					summaryLength:160					// 摘要长度
			};
			parent.FireFly.doAct("SY_COMM_ZHIDAO", "getUserActivity",param,false,false,function(result){
				//设置最新动态数
				jQuery("#activity-span").text(result["_OKCOUNT_"]);
				
				// 设置总页数
				activityPage.setTotalPages(result._PAGE_.PAGES);
				
				if(result._DATA_.length > 0){
					//设置模板id
					result._DATA_.tmplId=tmplId;
					// 渲染数据
					//alert("--------debug----------");
					//console.debug(result._DATA_);
					$("#js-myActivity-Template").tmpl(result._DATA_).appendTo("#zd-activity-list");
				} 
			});
		 
			if(!activityPage.hasNextPage()){
				if(activityPage.getCurrPage()==1){
					$("#zd-load-more").html("没有数据！").addClass("zd-btn-more-disabled");
				}else{
					$("#zd-load-more").html("已加载全部数据！").addClass("zd-btn-more-disabled");
				}
				
			}
		}


		
		loadUserActivity();
		
		
		
		/**	加载更多 **/
		$("#zd-load-more").on("click",function(){
			if($(this).hasClass("zd-btn-more-disabled")){
				return false;
			}
			loadUserActivity();
		});
		/**
		 * 最新动态中关注主题
		 */
		$("#zd-activity-list").on("click",".zd-question-follow",function(){
			var $this=$(this);
			followQuestionHandler($this);
		})
		/**
		 * 最新动态中显示全部
		 */
		$("#zd-activity-list").on("click",".zd-profile-section-item .toggle-expand",function(){
			var $this=$(this),
			 	$wrapper=$this.closest(".zd-profile-section-item");
				//隐藏显示全部
				$this.hide();
				if($wrapper.find(".zd-full-content").length == 0){
					//加载全部内容
					
					//获取answer id               
					var rid=$this.attr("data-resourceid");
					
					var data=parent.FireFly.byId("SY_COMM_ZHIDAO_ANSWER",rid);
					
					$("<div></div>").addClass("zd-full-content")
									.html(data.A_CONTENT)
									.insertBefore($this.parent());
				}
					
				$wrapper.addClass("zd-item-expanded");
				$wrapper.find(".collapse").on("click",function(event){
					event.preventDefault();
					//显示"显示全部"
					$this.show();
					$wrapper.removeClass("zd-item-expanded");
				});			
			
		});
	 
		/**
		 * 最新动态中点击 姓名和头像 进入其个人知道页面
		 */
		$("#zd-activity-list").on("click",".js-zd-view",function(){ 
			othersZhidao(tmplId,$(this).attr("data-id"));
		})
		
		
		/**
		 * 滚动鼠标自动加载
		 */
	    $(window).scroll(function(){
	    	var a = $(document).scrollTop() + $(window).height();
	    	var b = $(document).height();
	        if(a == b) {
	            setTimeout('jQuery("#zd-load-more").trigger("click")',1000);
	            setTimeout('jQuery("#js-mark-followed-more").trigger("click")',1000);
	            setTimeout('jQuery("#js-mark-answer-more").trigger("click")',1000);
	            setTimeout('jQuery("#js-mark-ask-more").trigger("click")',1000);
	            setTimeout('jQuery("#js-mark-follow-more").trigger("click")',1000);
	            setTimeout('jQuery("#js-mark-favorites-more").trigger("click")',1000);
	        }
	    });
		
		/**
		 * 查询"赞同数/被采纳数/被分享数"
		 */
		function count(){
			var param = {}
			param["userId"] = otherId;
			var result = parent.FireFly.doAct("SY_COMM_ZHIDAO","countAnswer",param,false);
			jQuery("#aLikeVoteCounter-strong").text(result["aLikeVoteCounter"]);
			jQuery("#aBestCounter-strong").text(result["aBestCounter"]);
			jQuery("#aAdminBestCounter").text(result["aAdminBestCounter"]);
			jQuery("#shareCounter").text(result["shareCounter"]);
		}
		
		/**
		 * 调用count方法
		 */
		count();

		/**
		 * 给图片上面的名字加上擅长领域
		 */
		/*function spec(){
			var items = jQuery(".js-spec-item");
			if(items.length > 0){
				var specStr = "";
				jQuery(items).each(function(item_index,item_temp){
					specStr += jQuery(item_temp).text() + "、";
				});
				specStr = specStr.substring(0,specStr.length-1);
				jQuery("#spec-span").text(specStr);
			}else{
				var username = jQuery("#js-username-span").text();
				jQuery("#js-username-span").text(username.substring(0,username.length-1));
				jQuery("#js-spec-outside").text(" ");
			}
		}*/
		
		/**
		 * 调用spec方法
		 */
		/*spec();*/
		
		/**
		 * 个人主页浏览量+1
		 */
		parent.FireFly.doAct("SY_COMM_ZHIDAO","increasePersonalIndexCounter",{"userId":otherId},false,false);
		
		/**
		 * 获取个人主页浏览人数/浏览次数/最后浏览时间
		 */
		function getReqCounter(){
			var result = parent.FireFly.doAct("SY_COMM_ZHIDAO","getReqCounter",{"userId":otherId},false,false);
			jQuery("#js-countUser-strong").text(result["countUser"]);
			jQuery("#js-countReq-strong").text(result["countReq"]);
			jQuery("#js-time-strong").text(result["time"]);
		}
		
		/**
		 * 调用getReqCounter方法
		 */
		getReqCounter();
		
		/**
		 * 分别给'回复/出题/关注了/关注者/收藏/最新动态'添加href链接
		 */
		function url(){
			var url = "/cms/SY_COMM_CMS_TMPL/" + tmplId + ".html?who="+otherId;
			jQuery(".js-mark-answers").attr("href",url+"&mark=answers&active=js-mark-answers");
			jQuery(".js-mark-answers-i").bind("click",function(){
				jQuery(".js-mark-answer").trigger("click");
			});
			jQuery(".js-mark-questions").attr("href",url+"&mark=questions&active=js-mark-questions");
			jQuery(".js-mark-questions-i").bind("click",function(){
				jQuery(".js-mark-questions").trigger("click");
			});
			jQuery(".js-mark-favorites").attr("href",url+"&mark=favorites&active=js-mark-favorites");
			jQuery(".js-mark-activity").attr("href",url+"&mark=activity&active=js-mark-activity");
			jQuery(".js-mark-feedback").attr("href",url+"&mark=feedback&active=js-mark-feedback");
			jQuery(".js-mark-report").attr("href",url+"&mark=report&active=js-mark-report");
			jQuery(".js-mark-follows").attr("href",url+"&mark=follows");
			jQuery(".js-mark-followed").attr("href",url+"&mark=followed");
			
			jQuery(".js-mark-default").attr("href",url);
			
			//判断并设置选项的阴影效果
			if(active == 'js-mark-answers'){
				jQuery(".js-mark-default").removeClass("active");
				jQuery(".js-mark-answers").addClass("active");
			}else if(active == 'js-mark-questions'){
				jQuery(".js-mark-default").removeClass("active");
				jQuery(".js-mark-questions").addClass("active");
			}else if(active == 'js-mark-favorites'){
				jQuery(".js-mark-default").removeClass("active");
				jQuery(".js-mark-favorites").addClass("active");
			}else if(active == 'js-mark-activity'){
				jQuery(".js-mark-default").removeClass("active");
				jQuery(".js-mark-activity").addClass("active");
			}else if(active == 'js-mark-feedback'){
				jQuery(".js-mark-default").removeClass("active");
				jQuery(".js-mark-feedback").addClass("active");
			}else if(active == 'js-mark-report'){
				jQuery(".js-mark-default").removeClass("active");
				jQuery(".js-mark-report").addClass("active");
			}
		}
		/**
		 * 调用url方法
		 */
		url();
		
		/**
		 * 点击回复-更多
		 */
		function jsMarkAnswersMore(){
			jQuery("#js-mark-answer-more").bind("click",function(){
				var param = {}
				param["userId"] = otherId;
				param["summaryLength"] = 50;
				param["count"] = 10;
				param["page"] = parseInt(jQuery("#js-mark-answer-list").attr("page"))+1;
				//alert("----debug--------");
				var result = parent.FireFly.doAct("SY_COMM_ZHIDAO","getAnswerList",param,false);
				//console.debug(result);
				jQuery("#js-mark-answer-list").attr("page",result["_PAGE_"]["NOWPAGE"])
				jQuery(result["_DATA_"]).each(function(answer_index,answer_temp){
					jQuery("#js-mark-answer-list")
					.append(
							'<div class="zd-profile-section-item clearfix">'+ 
							'<a class="zd-profile-vote-count" href="javascript:void(0);">'+
							'<div class="zd-profile-vote-num">'+answer_temp["A_LIKE_VOTE"]+'</div>'+
							'<div class="zd-profile-vote-type">赞同</div>'+
							'</a>'+
							'<div class="zd-profile-section-main">'+
							'<h2 class="zd-profile-question">'+
							'<a class="question_link" target="_self" href="javascript:view(\''+answer_temp["Q_ID"]+'\',\''+answer_temp["Q_TITLE"]+'\');">'+
							answer_temp["Q_TITLE"]+	
							'</a>'+ 
							'</h2>'+
							'<div>'+
							answer_temp["A_CONTENT"]+
							'</div>'+
							'</div>'+
							'</div>'
					);
				});
				if(result["_PAGE_"]["NOWPAGE"] >= result["_PAGE_"]["PAGES"]){
					jQuery("#js-mark-answer-more").remove();
				}
			});
		}
		/**
		 * 点击出题-更多
		 */
		function jsMarkAskMore(){
			jQuery("#js-mark-ask-more").bind("click",function(){
				var param = {}
				param["userId"] = otherId;
				param["count"] = 10;
				param["page"] = parseInt(jQuery("#js-mark-ask-list").attr("page"))+1;
				//alert("----debug--------");
				var result = parent.FireFly.doAct("SY_COMM_ZHIDAO","getAskList",param,false);
				//console.debug(result);
				jQuery("#js-mark-ask-list").attr("page",result["_PAGE_"]["NOWPAGE"])
				jQuery(result["_DATA_"]).each(function(index,temp){
					var q_id = temp["Q_ID"];
					var quesfollowCounter = parent.FireFly.doAct("SY_COMM_ZHIDAO","getAskFollowCount",{"questionId":q_id},false);
					//console.debug(quesfollowCounter);
					jQuery("#js-mark-ask-list")
					.append(
							'<div class="zd-profile-section-item zd-clear">'+
			                '<span class="zd-profile-vote-count">'+
			                    '<div class="zd-profile-vote-num">'+temp["Q_READ_COUNTER"]+'</div>'+
			                    '<div class="zd-profile-vote-type">浏览</div>'+
			                '</span>'+
			                '<div class="zd-profile-section-main">'+
			                    '<h2 class="zd-profile-question">'+
			                        '<a class="question_link" target="_self" href="javascript:view(\''+temp["Q_ID"]+'\',\''+temp["Q_TITLE"]+'\');">'+
			                        	temp["Q_TITLE"]+
			                        '</a>'+
			                    '</h2>'+
			                    '<div class="zd-gray">'+
			                         temp["Q_ANSWER_COUNTER"]+' 个回复'+
			                        '<span class="zd-bull"> • </span>'+
			                        '<span class="quesFollowCounter">'+
			                        	quesfollowCounter["_OKCOUNT_"]+
			                        '</span> 人关注'+
			                        '<span class="zd-bull"> • </span>'+
			                        '<a class="zd-question-follow meta-item" data-id="'+temp["Q_ID"]+'" data-dis-name="'+temp["Q_TITLE"]+'">'+
			                            '<i class="z-icon-follow"></i>'+
			                            	'<span>关注主题</span>'+
			                        '</a>'+
			                    '</div>'+
			                '</div>'+
			             '</div>'
					);
				});
				if(result["_PAGE_"]["NOWPAGE"] >= result["_PAGE_"]["PAGES"]){
					jQuery("#js-mark-ask-more").remove();
				}
			});
		}
		
		/**
		 * 点击收藏-更多
		 */
		function jsMarkFavoriteMore(){
			jQuery("#js-mark-favorites-more").bind("click",function(){
				var param = {}
				param["userId"] = otherId;
				param["count"] = 10;
				param["page"] = parseInt(jQuery("#js-mark-favorite-list").attr("page"))+1;
				//alert("----debug--------");
				//console.debug(param);
				var result = parent.FireFly.doAct("SY_COMM_ZHIDAO","getFavoritesList",param,false);
				//console.debug(result);
				jQuery("#js-mark-favorite-list").attr("page",result["_PAGE_"]["NOWPAGE"])
				jQuery(result["_DATA_"]).each(function(index,temp){
					var q_id = temp["question"]["Q_ID"];
					var quesfollowCounter = parent.FireFly.doAct("SY_COMM_ZHIDAO","getAskFollowCount",{"questionId":q_id},false);
					//console.debug(quesfollowCounter);
					jQuery("#js-mark-favorite-list")
					.append(
							'<div class="zd-profile-section-item zd-clear">'+
			                '<span class="zd-profile-vote-count">'+
			                    '<div class="zd-profile-vote-num">'+temp["question"]["Q_READ_COUNTER"]+'</div>'+
			                    '<div class="zd-profile-vote-type">浏览</div>'+
			                '</span>'+
			                '<div class="zd-profile-section-main">'+
			                    '<h2 class="zd-profile-question">'+
			                        '<a class="question_link" target="_self" href="javascript:view(\''+temp["question"]["Q_ID"]+'\',\''+temp["question"]["Q_TITLE"]+'\');">'+
			                        	temp["question"]["Q_TITLE"]+
			                        '</a>'+
			                    '</h2>'+
			                    '<div class="zd-gray">'+
			                         temp["question"]["Q_ANSWER_COUNTER"]+' 个回复'+
			                        '<span class="zd-bull"> • </span>'+
			                        '<span class="quesFollowCounter">'+
			                        	quesfollowCounter["_OKCOUNT_"]+
			                        '</span> 人关注'+
			                        '<span class="zd-bull"> • </span>'+
			                        '<a class="zd-question-follow meta-item" data-id="'+temp["question"]["Q_ID"]+'" data-dis-name="'+temp["question"]["Q_TITLE"]+'">'+
			                            '<i class="z-icon-follow"></i>'+
			                            	'<span>关注主题</span>'+
			                        '</a>'+
			                    '</div>'+
			                '</div>'+
			             '</div>'
					);
				});
				if(result["_PAGE_"]["NOWPAGE"] >= result["_PAGE_"]["PAGES"]){
					jQuery("#js-mark-favorites-more").remove();
				}
			});
		}
		
		/**
		 * 点击关注了-更多
		 */
		function jsMarkFollowMore(){
			jQuery("#js-mark-follow-more").bind("click",function(){
				var param = {}
				param["userId"] = otherId;
				param["count"] = 10;
				param["page"] = parseInt(jQuery("#js-mark-follow-list").attr("page"))+1;
				param["isFollow"]="isFollow";
				//alert("----debug--------");
				var result = parent.FireFly.doAct("SY_COMM_ZHIDAO","getFollowList",param,false);
				//console.debug(result);
				jQuery("#js-mark-follow-list").attr("page",result["_PAGE_"]["NOWPAGE"])
				jQuery(result["_DATA_"]).each(function(index,temp){
					jQuery("#js-mark-follow-list")
					.append(
							'<div class="zm-profile-card zm-profile-section-item zg-clear">'+
					          '<div class="zg-right">'+ 
					          	'<!--<a class="zg-btn zg-btn-follow zm-rich-follow-btn small nth-0" id="tf-043ff01e5d03c529c268d50f388012c2" href="javascript:;" data-follow="m:button">关注</a>-->'+ 
					          '</div>'+
					          '<a href="#" userName='+temp["user"]["USER_CODE"]+' class="zm-item-link-avatar" data-tip="p$t$kaifulee" data-original_title="">'+ 
					          	'<img class="zm-item-img-avatar" src="'+temp["user"]["USER_IMG"]+'">'+ 
					          '</a>'+
					          '<div class="zm-list-content-medium">'+
					            '<h2 class="zm-list-content-title">'+
					            	'<a class="zg-link" href="#" userName='+temp["user"]["USER_CODE"]+' data-tip="p$t$kaifulee" data-original_title="">'+temp["user"]["USER_NAME"]+'</a>'+
					            '</h2>'+
					            '<div class="zg-big-gray">'+temp["user"]["DEPT_NAME"]+'</div>'+
					            '<div class="details zg-gray">'+ 
					            	'<a class="zg-link-gray-normal" href="#" target="_blank">'+temp["followCount"]+' 关注者</a> /'+ 
					            	'<a class="zg-link-gray-normal" href="#" target="_blank">'+temp["askCount"]+' 出题</a> /'+
					            	'<a class="zg-link-gray-normal" href="#" target="_blank">'+temp["answerCount"]+'回复</a> /'+ 
					            	'<a class="zg-link-gray-normal" href="#" target="_blank">'+temp["likeVoteCount"]+'赞同</a>'+ 
					            '</div>'+
					          '</div>'+
					        '</div>'
					);
				});
				if(result["_PAGE_"]["NOWPAGE"] >= result["_PAGE_"]["PAGES"]){
					jQuery("#js-mark-follow-more").remove();
				}
			});
		}
		/**
		 * 点击关注者-更多
		 */
		function jsMarkFollowedMore(){
			jQuery("#js-mark-followed-more").bind("click",function(){
				var param = {}
				param["userId"] = otherId;
				param["count"] = 10;
				param["page"] = parseInt(jQuery("#js-mark-followed-list").attr("page"))+1;
				param["isFollow"]="Followed";
				//alert("----debug--------");
				var result = parent.FireFly.doAct("SY_COMM_ZHIDAO","getFollowList",param,false);
				//console.debug(result);
				jQuery("#js-mark-followed-list").attr("page",result["_PAGE_"]["NOWPAGE"])
				jQuery(result["_DATA_"]).each(function(index,temp){
					jQuery("#js-mark-followed-list")
					.append(
							'<div class="zm-profile-card zm-profile-section-item zg-clear">'+
					          '<div class="zg-right">'+ 
					          	'<!--<a class="zg-btn zg-btn-follow zm-rich-follow-btn small nth-0" id="tf-e592f20aad872ef9aa0b72db5fc65ab8" href="javascript:;" data-follow="m:button">关注</a>-->'+ 
					          '</div>'+
					          '<a href="#" userName='+temp["user"]["USER_CODE"]+' class="zm-item-link-avatar" data-tip="p$t$hugo-wong-9" title="Hugo Wong">'+ 
					          	'<img class="zm-item-img-avatar" src="'+temp["user"]["USER_IMG"]+'">'+ 
					          '</a>'+
					          '<div class="zm-list-content-medium">'+
					            '<h2 class="zm-list-content-title">'+
					            	'<a title="" userName='+temp["user"]["USER_CODE"]+' class="zg-link" href="#" data-tip="p$t$hugo-wong-9">'+
					            		temp["user"]["USER_NAME"]+
					            	'</a>'+
					            '</h2>'+
					            '<div class="zg-big-gray">'+temp["user"]["DEPT_NAME"]+'</div>'+
					            '<div class="details zg-gray">'+ 
						            '<a class="zg-link-gray-normal" href="#" target="_blank">'+temp["followCount"]+' 关注者</a> /'+ 
					            	'<a class="zg-link-gray-normal" href="#" target="_blank">'+temp["askCount"]+' 出题</a> /'+
					            	'<a class="zg-link-gray-normal" href="#" target="_blank">'+temp["answerCount"]+'回复</a> /'+ 
					            	'<a class="zg-link-gray-normal" href="#" target="_blank">'+temp["likeVoteCount"]+'赞同</a>'+
					            '</div>'+
					          '</div>'+
					        '</div>'	
					);
				});
				if(result["_PAGE_"]["NOWPAGE"] >= result["_PAGE_"]["PAGES"]){
					jQuery("#js-mark-followed-more").remove();
				}
			});
		}
		/**
		 * 调用js-mark-answers-more方法
		 */
		jsMarkAnswersMore();
		jsMarkAskMore();
		jsMarkFavoriteMore();
		jsMarkFollowMore();
		jsMarkFollowedMore();
		
		
		/**
		 * 给所有有userName属性的元素绑定事件
		 */
		jQuery("[userName]").live("click",function(){
			othersZhidao(tmplId,jQuery(this).attr("userName"));
		});
		jQuery("[userName]").bind("click",function(){
			othersZhidao(tmplId,jQuery(this).attr("userName"));
		});
		
		
		
		//给编辑擅长领域按钮绑定鼠标事件
		jQuery("#spec-edit").bind("hover",function(){
			jQuery(this).css("cursor","pointer");
		});
		//给编辑擅长领域按钮绑定事件
		jQuery("#spec-edit").bind("click",function(event){
			if(otherId != myCode){
				return false;
			}
			//1.构造树形选择参数
			var configStr = "SY_COMM_ZHIDAO_CHNL_MANAGE,{'TYPE':'multi'}";
			var options = {
				"config" :configStr,
				"replaceCallBack":function(idArray,nameArray){//回调，idArray为选中记录的相应字段的数组集合
					dictCallBack(idArray,nameArray);
				}
			};
			//2.显示树形
			var dictView = new parent.rh.vi.rhDictTreeView(options);
			dictView.show(event);	
			
			//构造回写的值
			var nodes = [];
			var ids = jQuery("input[name='categoryID']");
			var names = jQuery("input[name='categoryName']");
			if(ids.length > 0){
				for(var category_i=0; category_i<ids.length; category_i++){
					var node = {};
					node["ID"] = jQuery(ids[category_i]).val();
					node["NAME"] = jQuery(names[category_i]).val();
					nodes.push(node);
				}
				dictView.setRightSelect(nodes);
			}
			
			//设置样式
			var box = parent.jQuery("#SY_COMM_ZHIDAO_CHNL_MANAGE-dictDialog").parent();
			box.css({"z-index":"9999","background":"white","top":"20%","left":"20%","position":"absolute"});
			jQuery(box).find(".ui-widget-content").removeClass("ui-widget-content");					
		});
		
		
		//回调函数
		var dictCallBack = function(idArray,nameArray){
			//批量增加新数据
			var new_dataIds = idArray.toString();
			if(new_dataIds.length > 0){
				var new_param = {"chnlIds":new_dataIds,"USER_ID":otherId};
				parent.FireFly.doAct("SY_COMM_ZHIDAO_USER_SUBJECT","addBatchSubject",new_param,false);
			}
			
			//动态加载显示新数据
			jQuery("#js-subject-ul").html("");
			var ids = idArray.toString().split(",");
			var names = nameArray.toString().split(",");
			for(var index=0; index < ids.length; index++){
				jQuery("#js-subject-ul").append(
						'<li class="li-css">'+names[index]+'</li>'+
		  				'<input type="hidden" name="categoryID" value="'+ids[index]+'" />'+
		  				'<input type="hidden" name="categoryName" value="'+names[index]+'" />'
				);
			}
			
		}
		
		
		
		/**
		 * 再次修改个人页面，在头部添加上个人简介信息和文本编辑框
		 */
		
		//点击"显示全部"按钮的事件
		jQuery("a[name='unfold']").live("click",function(){
			jQuery(this).parent().hide();
			jQuery("a[name='fold']").parent().show();
		});
		//点击"收起"按钮的事件
		jQuery("a[name='fold']").live("click",function(){
			jQuery(this).parent().hide();
			jQuery("a[name='unfold']").parent().show();
		});
		//点击"修改/填写个人简介"按钮的事件
		jQuery("a[name='edit']").live("click",function(){
			//隐藏掉所有的信息框
			jQuery(".unfold-item").hide();
			jQuery(".fold-item").hide();
			jQuery(".zu-edit-button").hide();
			jQuery(".info-empty-wrap").hide();
			jQuery(".edit-wrap").show();
		});
		//编辑完成后，给"确定"按钮绑定事件
		jQuery("a[name='save']").live("click",function(){
			var content = jQuery("#profile-header-description-input").val();
			var param = {
					"USER_ID":otherId,
					"USER_DESC":content
			};
			parent.FireFly.doAct("SY_COMM_ZHIDAO_USER","save",param,false,false);
			
			//隐藏所有的输入框
			jQuery(this).parent().hide();
			jQuery(".zu-edit-button").show();
			if(content != ''){
				jQuery("span.unfold-item").hide().find(".content").html(content);
				if(content.length > 100){
					if(jQuery("a[name='unfold']").length > 0){
						jQuery("span.fold-item").find(".content").html(content.substring(0,100)+"...");
					}else{
						jQuery("span.fold-item").find(".content").html(content.substring(0,100)+"...");
						jQuery("span.fold-item").append('<a name="unfold" class="unfold" href="javascript:;">显示全部»</a>');
					}
				}else{
					jQuery("span.fold-item").find(".content").html(content);
					jQuery("a[name='unfold']").remove();
				}
				jQuery("span.fold-item").show();
			}else{
				jQuery(".info-empty-wrap").show();
				jQuery(".zu-edit-button").hide();
			}
		});
		
});