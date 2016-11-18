//首页的JS

jQuery(document).ready(function(){
	findMyPerson();
	my_ask();
	my_follow();
	imgHover();
	my_tab_hover();
	my_tab_change();
	imgClick();
	triClick();
	aClick();
	//ThreeSpec();
	tabAllOrNoAnswer();
	tabAllOrNoAnswerInvite();
	tabAllOrInvite();
	tabAllOrShare();
	imgAuto();
	imgHoverImg();
	jsGoAnswer();
});



//查找我关注的人，并加载到图片集中
var findMyPerson = function(){
	var person_param = {
			_PAGE_:{
				SHOWNUM:3,
				NOWPAGE:1
			},
			S_USER:parent.System.getVar("@USER_CODE@"),
			_SELECT_:"*"
	};
	var person_result = parent.FireFly.doAct("SY_COMM_ZHIDAO_MY_PERSON_FOLLOW","query",person_param,false);
	var person_list = person_result["_DATA_"];
	//console.debug(person_list);
	jQuery("#img-list").append(
		'<div class="img-list-up" style="text-align:center; margin-bottom:5px;"><a class="js-a-click" href="javascript:void(0);">换一批</a></div>'
	);
	jQuery(person_list).each(function(person_index,person_temp){
		var personImg = person_temp["DATA_ID__IMG"];
		if (personImg != '' && personImg != undefined) {
			personImg = personImg.split("?")[0] + "?size=70x70&t=" + (new Date()).getTime();
		} else {
			personImg = "/sy/theme/default/images/common/rh-male-icon.png?size=70x70";
		}
		if(person_index > 4){
			jQuery("#img-list").append(
					'<div class="img-list-div img-hide '+person_index+'" index="'+person_index+'">'+
						'<img src="'+personImg+'" class="img-list-img" onerror="changeImgOnError(this);" style="width:auto;" />'+
						'<span style="display:none;">'+person_temp["DATA_ID"]+'</span>'+
					'</div>'
			);
		}else{
			jQuery("#img-list").append(
					'<div class="img-list-div img-show '+person_index+'" index="'+person_index+'">'+
						'<img src="'+personImg+'" class="img-list-img" onerror="changeImgOnError(this);" style="width:auto;" />'+
						'<span style="display:none;">'+person_temp["DATA_ID"]+'</span>'+
					'</div>'
			);
		}
	});
};


var changeImgOnError = function(self) {
	self.src = "/sy/theme/default/images/common/rh-male-icon.png?size=70x70";
}

//页面加载后，找到第一个图片，触发它的点击事件
var triClick = function(){
	//alert("--------debug--------");
	jQuery(jQuery(".img-list-div").children().get(0)).trigger("click");
}

//页面加载完后，为所有图片绑定点击事件，从后台查询出关于这个用户的信息，并显示
var imgClick = function(){
	jQuery(".img-list-div").live("click",function(){
		//清除所有有img-list-img-selected的元素的样式
		jQuery(".img-list-img").removeClass("img-list-img-selected");
		//给选中的img标签添加一个样式
		jQuery(this).find("img").addClass("img-list-img-selected");
		var dataId = jQuery(this).find("span").html();
		//隐藏所有用户的信息
		jQuery(".js-top-content-css").hide();
		//查找以前是否加载过
		if(jQuery("#"+dataId).length>0){
			jQuery("#"+dataId).show();
		}else{
			var param = {
					"DATA_ID":dataId,
					"summaryLength":30
			};
			var result = parent.FireFly.doAct("SY_COMM_ZHIDAO","getNewQuestionAnswerByPerson",param,false);
			//console.debug(result);
			var question_title = result["question_title"];
			var question_id = result["question_id"];
			var answer_content = result["answer_content"];
			var my_answer_q_id = result["my_answer_q_id"];
			var my_answer_q_title = result["my_answer_q_title"];
			var my_answer_content = result["my_answer_content"];
			var img = jQuery(this).find("img").attr("src");
			var userCode = result["USER"]["USER_CODE"];
			var userName = result["USER"]["USER_NAME"];
			var deptName = result["USER"]["DEPT_NAME"];
			var userPost = result["USER"]["USER_POST"];
			var sub = Format.limit(65, result["SUB"]);
			
			if(result["ANSWER"] == undefined){
				var answerPersonName = "暂无！";
			}else{
				var answerPersonName = result["ANSWER"]["_DATA_"]["0"]["S_USER__NAME"];
			}
			if(question_title == undefined){
				question_title = "此人暂无出题！";
			}
			if(answer_content == undefined){
				answer_content = "无";
			}
			if(my_answer_q_title == undefined){
				my_answer_q_title = "无";
			}
			if(my_answer_content == undefined){
				my_answer_content = "此人暂无任何回复！";
			}
			
			
			//向div中追加信息
			jQuery("#my-follow-person").append(
				'<div id="'+dataId+'" class="top-content-css js-top-content-css">'+
					'<div class="top-content-img-css">'+
						'<div class="top-content-inside-css">'+
							formatUserImg(userCode,img,'','content-img','88px','88px','imgUserCode="'+dataId+'"')+
						'</div>'+
						'<div style="color:#666666; width:86px; margin-top:100px;">'+
							'<div style="font-size:14px;">'+userName+'</div>'+
							'<div>'+deptName+'</div>'+
							'<div>'+userPost+'</div>'+
							'<!--<div style="text-align:center;">'+result["SUB"]+'</div>-->'+
						'</div>'+
					'</div>'+
					'<p class="top-content-middle-span-css">'+
					'<span class="small-font">'+result["SPEC"]+'</span>'+
					'</p>'+
					'<p id="spec_'+dataId+'" class="top-content-middle-span-css">'+
					'</p>'+
					'<h3 class="red-color" style="margin-left:100px;border-bottom: 1px dashed #EECCEE;">最新动态</h3>'+
				'</div>'
			);
			//在向里面追加最新动态
			jQuery(result["activityList"]).each(function(activity_index,activity_temp){
				if(activity_temp["DATA_DIS_NAME"] != undefined){
					activity_temp["DATA_DIS_NAME"] = Format.limit(25,activity_temp["DATA_DIS_NAME"]);
				}
				if(activity_temp["ACT_CODE"]=="ZHIDAO_CREATE_ANSWER" || activity_temp["ACT_CODE"]=="ZHIDAO_LIKE_ANSWER"){
					jQuery("#"+dataId).append(
							'<div style="width:auto; margin-left:100px;line-height:30px;border-bottom:1px dashed #EEccEE;">'+ 
							'<!--<a class="zd-link" href=""> 他 </a>--> ' + activity_temp["ACT_CODE__NAME"] +
							'<a href="javascript:view(\''+activity_temp["answer"]["Q_ID"]+'\',\''+activity_temp["answer"]["Q_TITLE"]+'\');"> '+activity_temp["DATA_DIS_NAME"]+' </a>'+ 
							'</div>'
					);
				}else if(activity_temp["ACT_CODE"]=="ZHIDAO_FOLLOW_CATEGORY"){
					jQuery("#"+dataId).append(
							'<div style="width:auto; margin-left:100px;line-height:30px;border-bottom:1px dashed #EEccEE;">'+ 
							'<!--<a class="zd-link" href=""> 他 </a>--> ' + activity_temp["ACT_CODE__NAME"] +
							'<a href="javascript:viewCategory(\''+activity_temp["DATA_ID"]+'\');"> '+activity_temp["DATA_DIS_NAME"]+' </a>'+ 
							'</div>'
					);
				}else if(activity_temp["ACT_CODE"]=="ZHIDAO_FOLLOW_USER"){
					jQuery("#"+dataId).append(
							'<div style="width:auto; margin-left:100px;line-height:30px;border-bottom:1px dashed #EEccEE;">'+ 
							'<!--<a class="zd-link" href=""> 他 </a>--> ' + activity_temp["ACT_CODE__NAME"] +
							'<a href="javascript:othersZhidao(\''+center_temp_id+'\',\''+activity_temp["DATA_ID"]+'\');"> '+activity_temp["DATA_DIS_NAME"]+' </a>'+ 
							'</div>'
					);
				}else{
					jQuery("#"+dataId).append(
							'<div style="width:auto; margin-left:100px;line-height:30px;border-bottom:1px dashed #EEccEE;">'+ 
							'<!--<a class="zd-link" href=""> 他 </a>--> ' + activity_temp["ACT_CODE__NAME"] +
							'<a href="javascript:javascript:view(\''+activity_temp["DATA_ID"]+'\',\''+activity_temp["DATA_DIS_NAME"]+'\');"> '+activity_temp["DATA_DIS_NAME"]+' </a>'+ 
							'</div>'
					);
				}
				
			});
			
			//判断是否是专家，来向spec_id中追加擅长领域
			if(result["SPEC_TYPE"]==1 || result["SPEC_TYPE"]==2 || result["SPEC_TYPE"]==3){
				jQuery("#spec_"+dataId).append(
					'<span class="red-color">擅长：</span><span class="small-font">'+sub+'</span>'	
				);
			}
			
			
			
			//给图片绑定事件
			jQuery("[imgUserCode]").live("hover",function(){
				jQuery(this).css("cursor","pointer");
			}).live("click",function(){
				var userCode = jQuery(this).attr("imgUserCode");
				othersZhidao(center_temp_id,userCode);
			});
			
			
			
			
			
			
			
			/*if(answer_content == undefined){
				answer_content = "抱歉，这个主题还没有人回复！";
			}else{
				answer_content = "答案："+answer_content;
			}
			var img = jQuery(this).find("img").attr("src");
			if(question_title == undefined){
				question_title = "抱歉，此人还没有提出过主题！";
				追加没有主题链接的div
				jQuery("#my-follow-person").append(
						'<div id="'+dataId+'" class="top-content-css js-top-content-css">'+
						'<div class="top-content-img-css">'+
						'<div class="top-content-inside-css">'+
						'<a href="javascript:othersZhidao(\''+center_temp_id+'\',\''+dataId+'\')">'+
							'<img class="content-img" src="'+img+'" />'+
						'</a>'+
						'</div>'+
						'</div>'+
						'<div class="top-content-font-css">'+
						'<dl>'+
						'<dd class="top-content-dd"><span>'+question_title+'</span></dd>'+
						'<dt style="font:14px;"><span>'+answer_content+'</span></dt>'+
						'</dl>'+
						'</div>'+
						'</div>'
				);
			}else{
				追加有主题链接的div
				jQuery("#my-follow-person").append(
						'<div id="'+dataId+'" class="top-content-css js-top-content-css">'+
						'<div class="top-content-img-css">'+
						'<div class="top-content-inside-css">'+
						'<a href="javascript:othersZhidao(\''+center_temp_id+'\',\''+dataId+'\')">'+
							'<img class="content-img" src="'+img+'" />'+
						'</a>'+
						'</div>'+
						'</div>'+
						'<div class="top-content-font-css">'+
						'<dl>'+
						'<dd class="top-content-dd">主题：<a href="javascript:view(\''+question_id+'\',\''+question_title+'\')">'+question_title+'</a></dd>'+
						'<dt style="font:14px;"><span>'+answer_content+'</span></dt>'+
						'</dl>'+
						'</div>'+
						'</div>'
				);
			}*/
		}
	});
}

//为每个img-list-div绑定鼠标移入事件
var imgHover = function(){
	jQuery(".img-list-div").live("hover",function(){
		jQuery(this).css("cursor","pointer");
	});
}

//为每个img-list-img绑定高亮显示
//鼠标进入这个div后就停止图片轮转
var imgHoverImg = function(){
	/*jQuery(".img-list-img").live("mouseover",function(){
		window.clearInterval(time);
	});
	jQuery(".img-list-img").live("mouseout",function(){
		imgAuto();
	});*/
	jQuery(".content-left").live("mouseover",function(){
		window.clearInterval(time);
	});
	jQuery(".content-left").live("mouseout",function(){
		imgAuto();
	});
}


//点击换一批后更换三个关注人
var aClick = function(){
	
	//给按钮绑定hover鼠标手型事件
	jQuery(".js-up-click").bind("hover",function(){
		jQuery(this).css("cursor","pointer");
	});
	jQuery(".js-down-click").bind("hover",function(){
		jQuery(this).css("cursor","pointer");
	});
	
	//点击上箭头
	jQuery(".js-up-click").live("click",function(){
		for(var i=0; i<5; i++){
			if(jQuery(".img-list-div").length > 5){
				var ready_show = jQuery(jQuery(".img-hide").get(0));
				var first_show = jQuery(jQuery(".img-show").get(0)).removeClass("img-show").addClass("img-hide");
				ready_show.removeClass("img-hide").addClass("img-show");
				jQuery(first_show).remove();
				jQuery("#img-list").append(first_show);
			}
		}
		jQuery(jQuery(".img-show").get(0)).trigger("click");
	});
	//点击下箭头
	jQuery(".js-down-click").live("click",function(){
		for(var i=0; i<5; i++){
			if(jQuery(".img-list-div").length > 5){
				//要隐藏的元素
				var ready_hide = jQuery(jQuery(".img-show").get(4));
				var ready_show = jQuery(jQuery(".img-list-div").get(jQuery(".img-list-div").length-1)).removeClass("img-hide").addClass("img-show");
				
				ready_hide.removeClass("img-show").addClass("img-hide");
				jQuery(ready_show).remove();
				jQuery("#img-list").prepend(ready_show);
			}
		}
		jQuery(jQuery(".img-show").get(0)).trigger("click");
	});
	
}


//图片自动轮转
var imgAuto = function(){
	time = setInterval(function(){
		//如果关注的人大于5个
		if(jQuery(".img-list-div").length > 5){
			//获取当前选中图片的下一个div
			var nextDiv = jQuery(jQuery(".img-list-img-selected").get(0)).parent().next();
			if(jQuery(nextDiv).hasClass("img-hide")){
				//alert("隐藏！");
					for(var i=0; i<jQuery(".img-show").length; i++){
						var ready_show = jQuery(jQuery(".img-hide").get(0));
						var first_show = jQuery(jQuery(".img-show").get(0)).removeClass("img-show").addClass("img-hide");
						ready_show.removeClass("img-hide").addClass("img-show");
						jQuery(first_show).remove();
						jQuery("#img-list").append(first_show);
					}
					jQuery(jQuery(".img-show").get(0)).trigger("click");
			}else{
				//alert("显示的！");
				jQuery(".img-list-img").removeClass("img-list-img-selected");
				jQuery(nextDiv).find(".img-list-img").addClass("img-list-img-selected");
				jQuery(nextDiv).trigger("click");
			}
		}else{
			var next = jQuery(jQuery(".img-list-img-selected").get(0)).parent().next();
			if(jQuery(next).hasClass("img-show")){
				jQuery(".img-list-img").removeClass("img-list-img-selected");
				jQuery(next).find(".img-list-img").addClass("img-list-img-selected");
				jQuery(next).trigger("click");
			}else{
				jQuery(".img-list-img").removeClass("img-list-img-selected");
				jQuery(jQuery(".img-list-img").get(0)).addClass("img-list-img-selected");
				jQuery(jQuery(".img-list-img").get(0)).parent().trigger("click");
			}
		}
		
	},5000);
}


//查找我提出的主题，并加载到我的出题div中
var my_ask = function(){
	var ask_param={
			_PAGE_:{
				SHOWNUM:14,
				NOWPAGE:1
			},
			//因为传同一个paramBean得到数据一样，所以传不太userCode得到不太数据
			//SY_COMM_ZHIDAO_MYASK服务 的S_ATIME 需要在列表显示
			S_USER:parent.System.getVar("@USER_CODE@"),
			_SELECT_:"*"
			
	};
	//alert(parent.System.getVar("@USER_CODE@"));
	var ask_result=parent.FireFly.getPageData("SY_COMM_ZHIDAO_MYASK",ask_param);
	var my_ask_list = ask_result["_DATA_"];
//	console.debug(my_ask_list);
	var qIds = "";
	var userCode = "";
	jQuery(my_ask_list).each(function(index,temp){
		qIds += temp["Q_ID"]+",";
	});
	var activityList = parent.FireFly.doAct("SY_COMM_ZHIDAO","getNewActivity",{"qIds":qIds},false)._DATA_;
	//console.debug(activityList);
	
	jQuery(my_ask_list).each(function(ask_index,ask_temp){
		var a_content = "";
		if(jQuery(ask_temp["SY_COMM_ZHIDAO_ANSWER"]).length > 0){
			a_content = jQuery(ask_temp["SY_COMM_ZHIDAO_ANSWER"])[0]["A_CONTENT"] ;
			var lengthContent = jQuery(ask_temp["SY_COMM_ZHIDAO_ANSWER"])[0]["A_CONTENT"] ;
			if(lengthContent.length > 45){
				a_content = lengthContent.substring(0,45)+"...";
			}
		}else{
			a_content = "主题暂无回复";
		}
		
		var q_content = "";
		if(ask_temp["Q_TITLE"].length > 14){
			q_content = ask_temp["Q_TITLE"].substring(0,14)+"...";
		}else{
			q_content = ask_temp["Q_TITLE"];
		}
		
		//如果是提出了此主题，将不显示这句话，只显示时间
		var spanStr = "";
		if(activityList[ask_index]["ACT_CODE_STR"] == '提出了'){
			spanStr = 
				'<span class="answer-num-span">'+
					jQuery.timeago(ask_temp["S_ATIME"].substring(0,19))+
				'</span>'
		}else{
			spanStr = 
				'<span class="answer-num-span">'+
					activityList[ask_index]["USER_NAME"]+
				'</span>'+
				'<span class="answer-num-span" style="width:40px;">'+
					activityList[ask_index]["ACT_CODE_STR"]+
				'</span>'+
				'<span class="answer-num-span" style="width:40px;">'+
					'此主题'+
				'</span>'+
				'<span class="answer-num-span">'+
					jQuery.timeago(activityList[ask_index]["S_ATIME"].substring(0,19))+
				'</span>'
		}
		
		jQuery("#my_ask_list").append('<dl class="my-ask-dl-css">'+
			'<dt class="my-ask-dd-css">'+
				'<span class="red-mark-a"></span>'+
				"<a class='right_font' href=javascript:view(\'"+ask_temp["Q_ID"]+"\',\'"+ask_temp["Q_TITLE"]+"\'); onclick='' title="+ask_temp["Q_TITLE"]+" class='logSend' target='_self'>"+q_content+"</a>"+
				'<span class="answer-num">'+
					spanStr+
				'</span>'+
			"</dt>"+
		"</dl>");
	});
}


//查找我关注的主题，并加载到我的关注div中
var my_follow = function(){
	//我关注的主题
	var param={
			_PAGE_:{
				SHOWNUM:14,
				NOWPAGE:1
			}
	};
	
	var follow_result = parent.FireFly.getPageData("SY_COMM_ZHIDAO_MY_QUESTION_FOLLOW",param);
	var my_follow_list = follow_result["_DATA_"];
	//console.debug(my_follow_list);
	var qIds = "";
	jQuery(my_follow_list).each(function(index,temp){
		qIds += temp["DATA_ID"]+",";
	});
	var activityList = parent.FireFly.doAct("SY_COMM_ZHIDAO","getNewActivity",{"qIds":qIds},false)._DATA_;
	//console.debug(activityList);
	
	jQuery(my_follow_list).each(function(index,temp){
		var q_content = "";
		if(temp["DATA_DIS_NAME"].length > 14){
			q_content = temp["DATA_DIS_NAME"].substring(0,14)+"...";
		}else{
			q_content = temp["DATA_DIS_NAME"];
		}
		
		jQuery("#my_follow_list").append(
		'<dl class="my-follow-dl-css">'+
			'<dt class="my-follow-dd-css">'+
				'<span class="red-mark-a"></span>'+
				"<a class='right_font' href=javascript:view(\'"+temp["DATA_ID"]+"\',\'"+temp["DATA_DIS_NAME"]+"\'); title="+temp["DATA_DIS_NAME"]+" class='logSend' target='_self'>"+q_content+"</a>"+
				'<span class="answer-num">'+
					'<span class="answer-num-span">'+
						activityList[index]["USER_NAME"]+
					'</span>'+
					'<span class="answer-num-span" style="width:40px;">'+
						activityList[index]["ACT_CODE_STR"]+
					'</span>'+
					'<span class="answer-num-span" style="width:40px;">'+
						'此主题'+
					'</span>'+
					'<span class="answer-num-span">'+
						jQuery.timeago(activityList[index]["S_ATIME"].substring(0,19))+
					'</span>'+
				'</span>'+
			"</dt>"+
		"</dl>"
		);
	});
	
}
	

// 默认参数
var param={
		_PAGE_:{
			SHOWNUM:3,
			NOWPAGE:1
		}
};


//为"我的关注"的tab切换按钮绑定事件
var my_tab_change = function(){
	jQuery(".js-follow").bind("mouseover",function(){
		jQuery(".js-follow").removeClass("documentHotFontSelect");
		jQuery(".js-follow i").hide();
		jQuery(this).addClass("documentHotFontSelect");
		jQuery(this).children("i").show();
		
		var num = jQuery(this).attr("num");
		if(num == "1"){
			jQuery("#my_ask_list").show();
			jQuery("#my_follow_list").hide();
			jQuery("#zh-pm-detail-item-wrap").hide();
		}else if(num == "2"){
			jQuery("#my_follow_list").show();
			jQuery("#my_ask_list").hide();
			jQuery("#zh-pm-detail-item-wrap").hide();
		}else{
			jQuery("#zh-pm-detail-item-wrap").show();
			jQuery("#my_follow_list").hide();
			jQuery("#my_ask_list").hide();
		}
	});
}

//为"我的关注"鼠标显示样式
var my_tab_hover = function(){
	jQuery(".default").bind("hover",function(){
		jQuery(this).css("cursor","pointer");
	});
}


//没用了！！！
var ThreeSpec = function(){
	var threeSpec = parent.FireFly.doAct("SY_COMM_ZHIDAO","getThreeSpec",param,false);
	//console.debug(threeSpec);
	if(threeSpec["leader"] != undefined && threeSpec["leader"]["leader_spec"] != undefined){
		jQuery("#threeSpec").append(
			'<div id="leader" class="recommend-user" style="width:250px; float:left;">'+
				'<div style="width:100%;"><h1 style="width:100%;font-size:14px; font-weight:700; color:rgb(51,51,51);">'+threeSpec["leader"]["S_USER__NAME"]+'</h1></div>'+
				'<div class="inner" style="height:110px;">'+
			     '<div class="inner-left">'+
			        '<div class="leader-logo"></div>'+
			        '<a style="width:80px; height:80px;display:block;" data-img="#" target="_self" href="javascript:othersZhidao(\''+center_temp_id+'\',\''+threeSpec["leader"]["S_USER"]+'\')" rel="nofollow" alog-action="famous-img">'+
			        	formatUserImg(threeSpec["leader"]["S_USER"],threeSpec["leader"]["S_USER__IMG"],'','','','','')+
			        '</a>'+
			      '</div>'+
			      '<div class="inner-right" style="width:100px;">'+
			      	'<div>'+threeSpec["leader"]["S_DEPT__NAME"]+'</div>'+
			      	'<div>程序员</div>'+
			      	'<div>回复数：'+threeSpec["leader"]["leader_spec"]["SPEC_ANSWER_COUNTER"]+'</div>'+
			      	'<div>帮助了30人</div>'+
			      '</div>'+
			    '</div>'+
			    '<div style="height:auto;">'+
			    	'<div style="float:left; height:10px;">问：</div><div threeQID='+threeSpec["leader"]["Q_ID"]+'>'+threeSpec["leader"]["Q_TITLE"]+'</div>'+
			    	'<div style="float:left; height:30px;">答：</div><div>'+threeSpec["leader"]["A_CONTENT"]+'</div>'+
			    '</div>'+
			'</div>'
		);
	}
	
	if(threeSpec["dept"] != undefined && threeSpec["dept"]["dept_spec"] != undefined){
		jQuery("#threeSpec").append(
			'<div id="dept" class="recommend-user" style="width:250px; float:left;">'+
				'<div style="width:100%;"><h1 style="width:100%;font-size:14px; font-weight:700; color:rgb(51,51,51);">'+threeSpec["dept"]["S_USER__NAME"]+'</h1></div>'+
				'<div class="inner" style="height:110px;">'+
			     '<div class="inner-left">'+
			        '<div class="dept-logo"></div>'+
			        '<a data-img="#" target="_self" href="javascript:othersZhidao(\''+center_temp_id+'\',\''+threeSpec["dept"]["S_USER"]+'\')" rel="nofollow" alog-action="famous-img">'+
			        	formatUserImg(threeSpec["dept"]["S_USER"],threeSpec["dept"]["S_USER__IMG"],'','','','','')+
			        '</a>'+
			      '</div>'+
			      '<div class="inner-right" style="width:100px;">'+
			      	'<div>'+threeSpec["dept"]["S_DEPT__NAME"]+'</div>'+
			      	'<div>程序员</div>'+
			      	'<div>回复数：'+threeSpec["dept"]["dept_spec"]["SPEC_ANSWER_COUNTER"]+'</div>'+
			      	'<div>帮助了30人</div>'+
			      '</div>'+
			    '</div>'+
			    '<div style="height:auto;">'+
			    	'<div style="float:left; height:10px;">问：</div><div threeQID='+threeSpec["dept"]["Q_ID"]+'>'+threeSpec["dept"]["Q_TITLE"]+'</div>'+
			    	'<div style="float:left; height:30px;">答：</div><div>'+threeSpec["dept"]["A_CONTENT"]+'</div>'+
			    '</div>'+
			'</div>'
		);
	}
	
	if(threeSpec["busi"] != undefined && threeSpec["busi"]["busi_spec"] != undefined){
		jQuery("#threeSpec").append(
			'<div id="busi" class="recommend-user" style="width:250px; float:left;">'+
				'<div style="width:100%;"><h1 style="width:100%;font-size:14px; font-weight:700; color:rgb(51,51,51);">'+threeSpec["busi"]["S_USER__NAME"]+'</h1></div>'+
				'<div class="inner" style="height:110px;">'+
			     '<div class="inner-left">'+
			        '<div class="busi-logo"></div>'+
			        '<a data-img="#" target="_self" href="javascript:othersZhidao(\''+center_temp_id+'\',\''+threeSpec["busi"]["S_USER"]+'\')" rel="nofollow" alog-action="famous-img">'+
			        	formatUserImg(threeSpec["busi"]["S_USER"],threeSpec["busi"]["S_USER__IMG"],'','','','','')+
			        '</a>'+
			      '</div>'+
			      '<div class="inner-right" style="width:100px;">'+
			      	'<div>'+threeSpec["busi"]["S_DEPT__NAME"]+'</div>'+
			      	'<div>程序员</div>'+
			      	'<div>回复数：'+threeSpec["busi"]["busi_spec"]["SPEC_ANSWER_COUNTER"]+'</div>'+
			      	'<div>帮助了30人</div>'+
			      '</div>'+
			    '</div>'+
			    '<div style="height:auto;">'+
			    	'<div style="float:left; height:10px;">问：</div><div threeQID='+threeSpec["busi"]["Q_ID"]+'>'+threeSpec["busi"]["Q_TITLE"]+'</div>'+
			    	'<div style="float:left; height:30px;">答：</div><div>'+threeSpec["busi"]["A_CONTENT"]+'</div>'+
			    '</div>'+
			'</div>'
		);
	}
	
	//给主题绑定鼠标形状
	jQuery("[threeQID]").live("hover",function(){
		jQuery(this).css("cursor","pointer");
	});
	//给主题绑定事件
	jQuery("[threeQID]").live("click",function(){
		var q_title = jQuery(this).text();
		var q_id = jQuery(this).attr("threeQID");
		if(q_id != "undefined" && q_id != "" && q_id.length > 0 && q_title != "" && q_title.length > 0){
			view(q_id,q_title);
		}
	});
}

var tabAllOrNoAnswer = function(){
	//给显示全部绑定事件
	jQuery("#all_question").bind("click",function(){
		noAnswer = "";
		var all_param = {};
		all_param["count"] = 14;
		all_param["noAnswer"] = "";
		var all_result = parent.FireFly.doAct("SY_COMM_ZHIDAO","getAskList",all_param,false);
		var all_result_list = all_result["_DATA_"];
		//清空Div中的元素
		jQuery("#question-list-div").html("");
		jQuery(all_result_list).each(function(all_index,all_temp){
			var all_title = "";
			if(all_temp.Q_TITLE.length > 27){
				all_title = all_temp.Q_TITLE.substring(0,27)+"...";
			}else{
				all_title = all_temp.Q_TITLE;
			}
			jQuery("#question-list-div").append(
				'<dl data-qid="535450661" class="">'+
					'<dt>'+
						'<span class="red-mark-a"></span>'+
						'<a title="'+all_temp.Q_TITLE+'" href="javascript:view(\''+all_temp.Q_ID+'\',\''+all_temp.Q_TITLE+'\');" target="_self">'+
							all_title+
						'</a>'+
						'<span class="answer-num">'+
							all_temp.Q_ANSWER_COUNTER+'回复'+
						'</span>'+
					'</dt>'+
				'</dl>'
			);
		});
	});
	
	//给零回复绑定事件
	jQuery("#noAnswer_question").bind("click",function(){
		noAnswer = "noAnswer";
		var all_param = {};
		all_param["count"] = 14;
		all_param["noAnswer"] = "noAnswer";
		var all_result = parent.FireFly.doAct("SY_COMM_ZHIDAO","getAskList",all_param,false);
		var all_result_list = all_result["_DATA_"];
		//清空Div中的元素
		jQuery("#question-list-div").html("");
		jQuery(all_result_list).each(function(all_index,all_temp){
			var all_title = "";
			if(all_temp.Q_TITLE.length > 27){
				all_title = all_temp.Q_TITLE.substring(0,27)+"...";
			}else{
				all_title = all_temp.Q_TITLE;
			}
			jQuery("#question-list-div").append(
				'<dl data-qid="535450661" class="">'+
					'<dt>'+
						'<span class="red-mark-a"></span>'+
						'<a title="'+all_temp.Q_TITLE+'" href="javascript:view(\''+all_temp.Q_ID+'\',\''+all_temp.Q_TITLE+'\');" target="_self">'+
							all_title+
						'</a>'+
						'<span class="answer-num">'+
							all_temp.Q_ANSWER_COUNTER+'回复'+
						'</span>'+
					'</dt>'+
				'</dl>'
			);
		});
	});
	
	//给刷新绑定事件
	jQuery("#refresh_question").bind("click",function(){
		var all_param = {};
		all_param["count"] = 14;
		all_param["noAnswer"] = noAnswer;
		var all_result = parent.FireFly.doAct("SY_COMM_ZHIDAO","getAskList",all_param,false);
		var all_result_list = all_result["_DATA_"];
		//清空Div中的元素
		jQuery("#question-list-div").html("");
		jQuery(all_result_list).each(function(all_index,all_temp){
			var all_title = "";
			if(all_temp.Q_TITLE.length > 27){
				all_title = all_temp.Q_TITLE.substring(0,27)+"...";
			}else{
				all_title = all_temp.Q_TITLE;
			}
			jQuery("#question-list-div").append(
				'<dl data-qid="535450661" class="">'+
					'<dt>'+
						'<span class="red-mark-a"></span>'+
						'<a title="'+all_temp.Q_TITLE+'" href="javascript:view(\''+all_temp.Q_ID+'\',\''+all_temp.Q_TITLE+'\');" target="_self">'+
							all_title+
						'</a>'+
						'<span class="answer-num">'+
							all_temp.Q_ANSWER_COUNTER+'回复'+
						'</span>'+
					'</dt>'+
				'</dl>'
			);
		});
	});
}

//"请我回复"tab的三个右侧按钮
var tabAllOrNoAnswerInvite = function(){
	//给显示全部绑定事件
	jQuery("#invite_question").bind("click",function(){
		noAnswer = "";
		var invite_param = {};
		invite_param["noAnswer"] = "";
		var invite_result = parent.FireFly.doAct("SY_COMM_ZHIDAO","getInviteMeAskList",invite_param,false);
		var all_result_list = invite_result["_DATA_"];
		//清空Div中的元素
		jQuery("#question-list-div").html("");
		jQuery(all_result_list).each(function(all_index,all_temp){
			var all_title = "";
			if(all_temp.Q_TITLE.length > 15){
				all_title = all_temp.Q_TITLE.substring(0,15)+"...";
			}else{
				all_title = all_temp.Q_TITLE;
			}
			jQuery("#question-list-div").append(
				'<dl data-qid="535450661" class="">'+
					'<dt>'+
						'<span class="red-mark-a"></span>'+
						'<a title="'+all_temp.Q_TITLE+'" href="javascript:view(\''+all_temp.Q_ID+'\',\''+all_temp.Q_TITLE+'\');" target="_self">'+
							all_title+
						'</a>'+
						'<span class="answer-num" style="width:70px;text-align:right;">'+
							jQuery.timeago(all_temp.S_ATIME.substring(0,19))+
						'</span>'+
						'<span class="answer-num" style="width:70px;text-align:right;">'+
							all_temp.S_USER__NAME+
						'</span>'+
						'<span class="answer-num" style="width:70px;text-align:right;">'+
							'邀请来自：'+
						'</span>'+
					'</dt>'+
				'</dl>'
			);
		});
	});
	
	//给零回复绑定事件
	jQuery("#invite-noAnswer_question").bind("click",function(){
		noAnswer = "noAnswer";
		var all_param = {};
		all_param["noAnswer"] = "noAnswer";
		var all_result = parent.FireFly.doAct("SY_COMM_ZHIDAO","getInviteMeAskList",all_param,false);
		var all_result_list = all_result["_DATA_"];
		//清空Div中的元素
		jQuery("#question-list-div").html("");
		jQuery(all_result_list).each(function(all_index,all_temp){
			var all_title = "";
			if(all_temp.Q_TITLE.length > 15){
				all_title = all_temp.Q_TITLE.substring(0,15)+"...";
			}else{
				all_title = all_temp.Q_TITLE;
			}
			jQuery("#question-list-div").append(
				'<dl data-qid="535450661" class="">'+
					'<dt>'+
						'<span class="red-mark-a"></span>'+
						'<a title="'+all_temp.Q_TITLE+'" href="javascript:view(\''+all_temp.Q_ID+'\',\''+all_temp.Q_TITLE+'\');" target="_self">'+
							all_title+
						'</a>'+
						'<span class="answer-num" style="width:70px;text-align:right;">'+
							jQuery.timeago(all_temp.S_ATIME.substring(0,19))+
						'</span>'+
						'<span class="answer-num" style="width:70px;text-align:right;">'+
							all_temp.S_USER__NAME+
						'</span>'+
						'<span class="answer-num" style="width:70px;text-align:right;">'+
							'邀请来自：'+
						'</span>'+
					'</dt>'+
				'</dl>'
			);
		});
	});
	
	//给刷新绑定事件
	jQuery("#invite-refresh_question").bind("click",function(){
		var all_param = {};
		all_param["noAnswer"] = noAnswer;
		var all_result = parent.FireFly.doAct("SY_COMM_ZHIDAO","getInviteMeAskList",all_param,false);
		var all_result_list = all_result["_DATA_"];
		//清空Div中的元素
		jQuery("#question-list-div").html("");
		jQuery(all_result_list).each(function(all_index,all_temp){
			var all_title = "";
			if(all_temp.Q_TITLE.length > 15){
				all_title = all_temp.Q_TITLE.substring(0,15)+"...";
			}else{
				all_title = all_temp.Q_TITLE;
			}
			jQuery("#question-list-div").append(
				'<dl data-qid="535450661" class="">'+
					'<dt>'+
						'<span class="red-mark-a"></span>'+
						'<a title="'+all_temp.Q_TITLE+'" href="javascript:view(\''+all_temp.Q_ID+'\',\''+all_temp.Q_TITLE+'\');" target="_self">'+
							all_title+
						'</a>'+
						'<span class="answer-num" style="width:70px;text-align:right;">'+
							jQuery.timeago(all_temp.S_ATIME.substring(0,19))+
						'</span>'+
						'<span class="answer-num" style="width:70px;text-align:right;">'+
							all_temp.S_USER__NAME+
						'</span>'+
						'<span class="answer-num" style="width:70px;text-align:right;">'+
							'邀请来自：'+
						'</span>'+
					'</dt>'+
				'</dl>'
			);
		});
	});
}

//切换"全部主题"和"请我回复的主题"
var tabAllOrInvite = function(){
	jQuery(".js-waiting").bind("mouseover",function(){
		jQuery(".js-waiting").removeClass("documentHotFontSelect");
		jQuery(".js-waiting i").hide();
		jQuery(this).addClass("documentHotFontSelect");
		jQuery(this).children("i").show();
		var waiting_num = jQuery(this).attr("num");
		
		//如果是显示全部
		if(waiting_num == 1){
			//控制右边三个按钮的显示和隐藏
			jQuery("#all-opt-sort").show();
			jQuery("#invite-opt-sort").hide();
			
			var all_param = {};
			all_param["count"] = 14;
			all_param["noAnswer"] = noAnswer;
			var all_result = parent.FireFly.doAct("SY_COMM_ZHIDAO","getAskList",all_param,false);
			var all_result_list = all_result["_DATA_"];
			//清空Div中的元素
			jQuery("#question-list-div").html("");
			jQuery(all_result_list).each(function(all_index,all_temp){
				var all_title = Format.limit(27,all_temp.Q_TITLE);
				jQuery("#question-list-div").append(
					'<dl data-qid="535450661" class="">'+
						'<dt>'+
							'<span class="red-mark-a"></span>'+
							'<a title="'+all_temp.Q_TITLE+'" href="javascript:view(\''+all_temp.Q_ID+'\',\''+all_temp.Q_TITLE+'\');" target="_self">'+
								all_title+
							'</a>'+
							'<span class="answer-num">'+
								all_temp.Q_ANSWER_COUNTER+'回复'+
							'</span>'+
						'</dt>'+
					'</dl>'
				);
			});
		}else{ //如果是显示"请我回复"
			//控制右边三个按钮的显示和隐藏
			jQuery("#invite-opt-sort").show();
			jQuery("#all-opt-sort").hide();
			
			var invite_param = {}
			invite_param["noAnswer"] = noAnswer;
			invite_param["count"] = 14;
			var invite_result = parent.FireFly.doAct("SY_COMM_ZHIDAO","getInviteMeAskList",invite_param,false);
			//console.debug(invite_result);
			var invite_result_list = invite_result["_DATA_"];
			//清空Div中的元素
			jQuery("#question-list-div").html("");
			if(invite_result_list.length <= 0){
				jQuery("#question-list-div").append("<h3 style='height:70px;line-height:70px;margin-left:250px;'>抱歉!现在没有同事向您出题!</h3>");
			}else{
				jQuery(invite_result_list).each(function(invite_index,invite_temp){
					var invite_title = Format.limit(15,invite_temp.Q_TITLE);
					jQuery("#question-list-div").append(
							'<dl data-qid="535450661" class="">'+
							'<dt>'+
							'<span class="red-mark-a"></span>'+
							'<a title="'+invite_temp.Q_TITLE+'" href="javascript:view(\''+invite_temp.Q_ID+'\',\''+invite_temp.Q_TITLE+'\');" target="_self">'+
							invite_title+
							'</a>'+
							'<span class="answer-num" style="width:70px;text-align:right;">'+
								jQuery.timeago(invite_temp.S_ATIME.substring(0,19))+
							'</span>'+
							'<span class="answer-num" style="width:70px;text-align:right;">'+
								invite_temp.S_USER__NAME+
							'</span>'+
							'<span class="answer-num" style="width:70px;text-align:right;">'+
								'邀请来自：'+
							'</span>'+	
							'</dt>'+
							'</dl>'
					);
				});
			}
		}
	});
}

//切换"热门推荐"和"同事分享"
var tabAllOrShare = function(){
	jQuery(".js-share").bind("mouseover",function(){
		jQuery(".js-share").removeClass("documentHotFontSelect");
		jQuery(this).addClass("documentHotFontSelect");
		var num = jQuery(this).attr("num");
		
		if(num == 1){
			jQuery("#zh-pm-detail-item-wrap").hide();
			jQuery("#hot-div").show();
		}else{
			jQuery("#zh-pm-detail-item-wrap").show();
			jQuery("#hot-div").hide();
		}
	});
}

//给"去答题，开启知道之旅吧!"绑定事件
var jsGoAnswer = function(){
	jQuery("#js-go-answer").bind("click",function(event){
		jQuery("#inviteMe").trigger("mouseover");
		parent.jQuery('html,body').animate({scrollTop:650},500);
	});
}

//前台控制时间显示的格式的方法，引入这段脚本，将需要显示时间的span标签设置成class="timeago"即可
jQuery(document).ready(function(){
	jQuery(".timeago").each(function(index,temp){
		var temp_time = jQuery(this).html().trim();
		var time = "";
		if(temp_time.length > 19){
			time = temp_time.substring(0,19);
		}else{
			time = temp_time;
		}
		var timeago =  jQuery.timeago(time);
		jQuery(this).html(timeago);
	});
});

//注册"我的关注"滚动条移动事件
//var scrollToMyFollow = function(event){
//	parent.jQuery("html,body").animate({scrollTop:350},500);
//	jQuery("#my-follow-question").trigger("mouseover");
//}
