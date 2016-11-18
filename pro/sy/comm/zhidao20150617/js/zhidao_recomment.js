$(document).ready(function(){
	//导航添加选中效果
	$("#menu").find("#recommend").addClass("current");
	
	//推荐导航切换
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
	/**
	 * 返回顶部
	 */
	/*$(window).scroll(function(){
		var offsetToTop=$(this).scrollTop();
		if(offsetToTop>100){
			$('#zd-goto-top').fadeIn(500);
		}else{
			$('#zd-goto-top').fadeOut(500);
		}
	});
	$('#zd-goto-top a').click(function(event){
		//event.preventDefault();
		$('html,body').animate({scrollTop:0},500);
	});*/
	
	
	
	
	
	
	
	var pager0={showNum: 5,currPage: 1,totalPages: 0},
		pager1={showNum: 5,currPage: 1,totalPages: 0},
		pager2={showNum: 5,currPage: 1,totalPages: 0},
		pager3={showNum: 5,currPage: 1,totalPages: 0};
	
	var param0={
			pager:pager0,
			answerCount:1,
			summaryLength:100,
			specType:0,
			selector:"#js-latest-qa"
	};
	var param1={
			pager:pager1,
			answerCount:1,
			summaryLength:100,
			specType:1,
			selector:"#js-invited-specialist-qa"
	};
	var param2={
			pager:pager2,
			answerCount:1,
			summaryLength:100,
			specType:2,
			selector:"#js-dept-qa"
	};
	var param3={
			pager:pager3,
			answerCount:1,
			summaryLength:100,
			specType:3,
			selector:"#js-bzn-specialist-qa"
	};
	function init(){
		//最近问答
		loadQusetionsAndAnswers(param0);
		//特邀专家的问答
		loadQusetionsAndAnswers(param1);
		//职能部门权威问答
		loadQusetionsAndAnswers(param2);
		//业务专家问答
		loadQusetionsAndAnswers(param3);
		
		//绑定click时间
		bindEvent();
	}
	/**初始化**/
	init();
	
	/**点击更多**/
	function bindEvent(){
		$("#js-latest-qa").find(".ui-pager>a").on("click",function(event){
			event.preventDefault();
			//当前页数+1
			param0.pager.currPage+=1;
			//获取总页数
			loadQusetionsAndAnswers(param0);
		});
		$("#js-invited-specialist-qa").find(".ui-pager>a").on("click",function(event){
			event.preventDefault();
			param1.pager.currPage+=1;
			//获取总页数
			loadQusetionsAndAnswers(param1);
			
		});
		$("#js-dept-qa").find(".ui-pager>a").on("click",function(event){
			event.preventDefault();
			param2.pager.currPage+=1;
			//获取总页数
			loadQusetionsAndAnswers(param2);
			
		});
		$("#js-bzn-specialist-qa").find(".ui-pager>a").on("click",function(event){
			event.preventDefault();
			param3.pager.currPage+=1;
			//获取总页数
			loadQusetionsAndAnswers(param3);
			
		});
	}
	
	

	
	/**最新问答**/
	function loadQusetionsAndAnswers(param){
		
		var p={
				page			: param.pager.currPage,		//当前页
				count			: param.pager.showNum,		//显示行数
				answerCount 	: param.answerCount,        //获取答案数
				summaryLength	: param.summaryLength,      //摘要字数
				specType		: param.specType			//类别
		};
		var $pager=$(param.selector).find(".ui-pager");
		var result=parent.FireFly.doAct("SY_COMM_ZHIDAO","getQaList",p,false,false);
		var data=result._DATA_;

		if(data.length > 0){
			$("#js-qa-Template").tmpl(data).insertBefore($pager);
		}else{
			$("#js-noqa-Template").tmpl({msg:"暂无数据！"}).insertBefore($pager);
			$(param.selector).find(".ui-pager").find("a").unbind("click");
		}
		
		// 设置总页数
		param.pager.totalPages=result._PAGE_.PAGES;
		
		if(param.pager.currPage >= param.pager.totalPages){
			$(param.selector).find(".ui-pager").find("a").hide();
		}
	}
	
	/**
	 * 滚动鼠标自动加载
	 */
    $(window).scroll(function(){
    	var a = $(document).scrollTop() + $(window).height();
    	var b = $(document).height();
        if(a == b) {
            //jQuery("#zd-load-more").trigger("click");
            var $showDiv = jQuery(jQuery("#zd-container-js").children("div:visible").get(1));
            jQuery($showDiv).find(".ui-pager>a").trigger("click");
        }
    });
	
});