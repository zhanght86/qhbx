// JavaScript Document
$(document).ready(function(e) {
	//导航添加选中效果
	$("#menu").find("#myfollow").addClass("current");
    //文档贡献---文辑贡献   切换
	var offleft=$("ul#tabControl").offset().left;
	$(".tab-item").on({
		mouseover:function(){
			var t=$(this).offset().left,
				w=$(this).width();
			$(this).siblings(".hover").css({left:t-offleft,width:w});
		},
		mouseleave:function(){
			if(!$(this).hasClass("current")){ 
				var t=$("#tabControl .current").offset().left,
					w=$("#tabControl .current").width();
				$(this).siblings(".hover").css({left:t-offleft,width:w});
			}
		},
		click:function(event){
			event.preventDefault();
			if($(this).hasClass("current")){
				return false;
			}
			var id=$(this).find("a").attr("href")
			    hid=$(this).siblings(".current").find("a").attr("href");
			$(this).siblings(".current").removeClass("current");
			$(this).addClass("current");
			 
			$(hid).hide();
			$(id).show();
			var t=$(this).offset().left,
				w=$(this).width();
			$(this).siblings(".slide").css({left:t-offleft,width:w});
		}
	});
	
	
	
	var pager0={showNum: 20,currPage: 1,totalPages: 0},
		pager1={showNum: 20,currPage: 1,totalPages: 0},
		pager2={showNum: 20,currPage: 1,totalPages: 0},
		pager3={showNum: 20,currPage: 1,totalPages: 0};
	 
	function init(){
		
		//关注的人
		loadFollowPeople();
		//关注的问题
		loadFollowQuestion();
		//我的提问
		loadMyQuestion();
		//关注的分类
		loadFollowCategory();
		//绑定click时间
		bindEvent();
	}
	
	
	
	
	/**初始化**/
	init();
	
	/**点击更多**/
	function bindEvent(){
		$("#js-follow-people").find(".ui-pager>a").on("click",function(){
			pager0.currPage+=1;
			//获取总页数
			loadFollowPeople();
		});
		$("#js-follow-question").find(".ui-pager>a").on("click",function(){
			pager1.currPage+=1;
			//获取总页数
			loadFollowQuestion();
			
		});
		$("#js-my-question").find(".ui-pager>a").on("click",function(){
			pager2.currPage+=1;
			//获取总页数
			loadMyQuestion();
			
		});
		$("#js-follow-category").find(".ui-pager>a").on("click",function(){
			pager3.currPage+=1;
			//获取总页数
			loadFollowCategory();
			
		});
		
		//对人的关注和取消关注
		$(".zd-grid-list").on("click",".js-person-follow",function(event){
			event.preventDefault();
			if($(this).hasClass("zd-btn-unfollow")){
				$(this).removeClass("zd-btn-unfollow").text("关注");
				//取消关注
				var data={
						"DATA_ID":$(this).attr("data-id")
				};
				parent.FireFly.doAct("SY_COMM_ZHIDAO", "deletePersonFollow", data,false,false);
			}else{
				$(this).addClass("zd-btn-unfollow").text("取消关注");
				//关注
				var data= {
						"DATA_ID"		: $(this).attr("data-id")
				};
				parent.FireFly.doAct("SY_COMM_ZHIDAO", "addPersonFollow", data,false,false);
			}
		});
		//对问题的关注和取消关注
		$(".zd-grid-list").on("click",".js-question-follow",function(event){
			event.preventDefault();
			if($(this).hasClass("zd-btn-unfollow")){
				$(this).removeClass("zd-btn-unfollow").text("关注");
				//取消关注
				var data={
						"DATA_ID":$(this).attr("data-id")
				};
				parent.FireFly.doAct("SY_COMM_ZHIDAO", "deleteQuestionFollow", data,false,false);
			}else{
				$(this).addClass("zd-btn-unfollow").text("取消关注");
				//关注
				var data= {
						"DATA_ID"		: $(this).attr("data-id")
				};
				parent.FireFly.doAct("SY_COMM_ZHIDAO", "addQuestionFollow", data,false,false);
			}
		});
		//对分类的关注和取消关注
		$(".zd-grid-list").on("click",".js-category-follow",function(event){
			event.preventDefault();
			if($(this).hasClass("zd-btn-unfollow")){
				$(this).removeClass("zd-btn-unfollow").text("关注");
				//取消关注
				var data={
						"DATA_ID":$(this).attr("data-id")
				};
				parent.FireFly.doAct("SY_COMM_ZHIDAO", "deleteCategoryFollow", data,false,false);
			}else{
				$(this).addClass("zd-btn-unfollow").text("取消关注");
				//关注
				var data= {
						"DATA_ID"		: $(this).attr("data-id")
				};
				parent.FireFly.doAct("SY_COMM_ZHIDAO", "addCategoryFollow", data,false,false);
			}
		}); 
	}
	
	
	/**关注的人**/
	function loadFollowPeople(){
		/**分页参数**/
		var param={
				count:pager0.showNum,	// 每页显示行数
				page:pager0.currPage    // 当前页
		};
		var $li=$("#js-follow-people").find(".ui-pager");
		var result=parent.FireFly.doAct("SY_COMM_ZHIDAO","getMyFollowPerson",param,false,false);
		var data=result._DATA_;
		data.tmplId=tmplId;
		
		$("#js-follow-people-Template").tmpl(data).insertBefore($li);
		
		pager0.totalPages=result._PAGE_.PAGES;
		
		if(pager0.currPage >= pager0.totalPages){
			$("#js-follow-people").find(".ui-pager").find("a").hide();
		}
	}
	
	/**关注的问题**/
	function loadFollowQuestion(){
		/**分页参数**/
		var param={
					count:pager1.showNum,	// 每页显示行数
					page:pager1.currPage    // 当前页
		};
		
		var $li=$("#js-follow-question").find(".ui-pager");
		
		var result=parent.FireFly.doAct("SY_COMM_ZHIDAO","getMyFollowQuestion",param,false,false);
//		var result=parent.FireFly.getPageData("SY_COMM_ZHIDAO_MY_QUESTION_FOLLOW",param);
		$("#js-follow-question-Template").tmpl(result._DATA_).insertBefore($li);
		
		pager1.totalPages=result._PAGE_.PAGES;
		
		if(pager1.currPage >= pager1.totalPages){
			$("#js-follow-question").find(".ui-pager").find("a").hide();
		}
	}
	
	/**我的提问**/
	function loadMyQuestion(){
		/**分页参数**/
		var param={
				_PAGE_:{
					SHOWNUM:pager2.showNum,
					NOWPAGE:pager2.currPage
				}
		};
		
		var $li=$("#js-my-question").find(".ui-pager");
		
		var result=parent.FireFly.getPageData("SY_COMM_ZHIDAO_MYASK",param);
	 
		$("#js-my-question-Template").tmpl(result._DATA_).insertBefore($li);
		
		pager2.totalPages=result._PAGE_.PAGES;
		
		if(pager2.currPage >= pager2.totalPages){
			$("#js-my-question").find(".ui-pager").find("a").hide();
		}
	}
	
	/**我的提问**/
	function loadFollowCategory(){
		/**分页参数**/
		var param={
				count:pager3.showNum,	// 每页显示行数
				page:pager3.currPage    // 当前页
		};
		
		var $li=$("#js-follow-category").find(".ui-pager");
		
		var result=parent.FireFly.doAct("SY_COMM_ZHIDAO","getMyFollowCategory",param,false,false);
//		var result=parent.FireFly.getPageData("SY_COMM_ZHIDAO_MY_CATEGORY_FOLLOW",param);
		
		$("#js-follow-category-Template").tmpl(result._DATA_).insertBefore($li);
		
		pager3.totalPages=result._PAGE_.PAGES;
		
		if(pager3.currPage >= pager3.totalPages){
			$("#js-follow-category").find(".ui-pager").find("a").hide();
		}
	}
	
	
	
	
	//调整图片大小，如果超过最大宽度，等比例缩放
	function adjustImgSize($img,maxWidth,minWidth){
		//原始图像大小
		var width,
			height,
			originWidth=$img.width(),
			originHeight=$img.height();
		if(originWidth > maxWidth){ 
			//如果原始图像大小  大于 maxWidth
		    width=maxWidth;
		    height=( originHeight * maxWidth ) / originWidth;
		  
		} 
		if(originWidth < minWidth){
			width=minWidth;
			height=( originHeight * minWidth ) / originWidth;
		}
		$img.css({"width":width,"height":height});
	}//---- end of adjustImgSize
	$(".zd-right-side").find("img").each(function(){
		   adjustImgSize($(this),110,64);
	});
	
	//从首页跳转过来时携带着参数，让tab页自动选中
	$(".tab-item").eq(tab_num).trigger("click").trigger("mouseover");
	 
});

