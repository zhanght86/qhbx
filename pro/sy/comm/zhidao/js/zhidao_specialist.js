// JavaScript Document
//知道专家页面JS
jQuery(document).ready(function(){
	//给分类列表绑定事件
	jQuery(".js-spec-chnl").bind("click",function(){
		var chnlId = jQuery(this).attr("dataId");
		var paramTotal = {
			type		:		"total",
			count		:		"5",
			chnlId		:		chnlId
		};
		var paramMonth = {
			type		:		"month",
			count		:		"5",
			chnlId		:		chnlId
		};
		var paramWeek = {
			type		:		"week",
			count		:		"5",
			chnlId		:		chnlId
		};
		var resultTotalList = parent.FireFly.doAct("SY_COMM_ZHIDAO","topStatisticsByChnl",paramTotal,false)._DATA_;
		var resultMonthList = parent.FireFly.doAct("SY_COMM_ZHIDAO","topStatisticsByChnl",paramMonth,false)._DATA_;
		var resultWeekList = parent.FireFly.doAct("SY_COMM_ZHIDAO","topStatisticsByChnl",paramWeek,false)._DATA_;
		//console.debug(resultTotalList);
		
		//清空ul的内容
		jQuery("#js-total-ul").html("");
		jQuery("#js-month-ul").html("");
		jQuery("#js-week-ul").html("");
		
		//追加内容
		if(resultTotalList.length > 0){
			jQuery(resultTotalList).each(function(index,temp){
				jQuery("#js-total-ul").append(
						'<li>'+
						'<dl style="padding-top:10px;">'+
						'<dt class="rank-color">'+
						'<div class="custom-left">'+
						'<a rel="nofollow" href="javascript:othersZhidao(\''+tempId+'\',\''+temp["USER"]["USER_CODE"]+'\')">'+
						formatUserImg(temp["USER"]["USER_CODE"],temp["USER"]["USER_IMG"],'','','60px','60px','imgUserCode="'+temp["USER"]["USER_CODE"]+'"')+
						'</a>'+
						'<a alog-action="user-rank-help" href="#" onclick="javascript:loadTiWen(\''+temp["USER_NAME"]+'\',\''+temp["USER"]["USER_CODE"]+'\');" class="btn btn-24-white fixed-ask">'+
						'<em>'+
						'<b style="padding:0 5px;">'+
						'<span>向TA求助</span>'+
						'</b>'+
						'</em>'+
						'</a>'+
						'</div>'+
						'</dt>'+
						'<dd>'+
						'<div class="custom-right">'+
						'<a href="javascript:othersZhidao(\''+tempId+'\',\''+temp["USER"]["USER_CODE"]+'\')">'+temp["USER_NAME"]+'</a>'+
						'<p style="font-size:12px;">积分：<strong style="color:red;">'+temp["INTEGRAL_VALUE"]+'</strong></p>'+
						'</div>'+	 
						'</dd>'+
						'</dl>'+
						'</li>'	
				);
			});
		}
		
		if(resultMonthList.length > 0){
			jQuery(resultMonthList).each(function(index,temp){
				jQuery("#js-month-ul").append(
						'<li>'+
						'<dl style="padding-top:10px;">'+
						'<dt class="rank-color">'+
						'<div class="custom-left">'+
						'<a rel="nofollow" href="javascript:othersZhidao(\''+tempId+'\',\''+temp["USER"]["USER_CODE"]+'\')">'+
						formatUserImg(temp["USER"]["USER_CODE"],temp["USER"]["USER_IMG"],'','','60px','60px','imgUserCode="'+temp["USER"]["USER_CODE"]+'"')+
						'</a>'+
						'<a alog-action="user-rank-help" href="#" onclick="javascript:loadTiWen(\''+temp["USER_NAME"]+'\',\''+temp["USER"]["USER_CODE"]+'\');" class="btn btn-24-white fixed-ask">'+
						'<em>'+
						'<b style="padding:0 5px;">'+
						'<span>向TA求助</span>'+
						'</b>'+
						'</em>'+
						'</a>'+
						'</div>'+
						'</dt>'+
						'<dd>'+
						'<div class="custom-right">'+
						'<a href="javascript:othersZhidao(\''+tempId+'\',\''+temp["USER"]["USER_CODE"]+'\')">'+temp["USER_NAME"]+'</a>'+
						'<p style="font-size:12px;">积分：<strong style="color:red;">'+temp["SCORE"]+'</strong></p>'+
						'</div>'+	 
						'</dd>'+
						'</dl>'+
						'</li>'
				);
			});
		}
		
		if(resultWeekList.length > 0){
			jQuery(resultWeekList).each(function(index,temp){
				jQuery("#js-week-ul").append(
						'<li>'+
						'<dl style="padding-top:10px;">'+
						'<dt class="rank-color">'+
						'<div class="custom-left">'+
						'<a rel="nofollow" href="javascript:othersZhidao(\''+tempId+'\',\''+temp["USER"]["USER_CODE"]+'\')">'+
						formatUserImg(temp["USER"]["USER_CODE"],temp["USER"]["USER_IMG"],'','','60px','60px','imgUserCode="'+temp["USER"]["USER_CODE"]+'"')+
						'</a>'+
						'<a alog-action="user-rank-help" href="#" onclick="javascript:loadTiWen(\''+temp["USER_NAME"]+'\',\''+temp["USER"]["USER_CODE"]+'\');" class="btn btn-24-white fixed-ask">'+
						'<em>'+
						'<b style="padding:0 5px;">'+
						'<span>向TA求助</span>'+
						'</b>'+
						'</em>'+
						'</a>'+
						'</div>'+
						'</dt>'+
						'<dd>'+
						'<div class="custom-right">'+
						'<a href="javascript:othersZhidao(\''+tempId+'\',\''+temp["USER"]["USER_CODE"]+'\')">'+temp["USER_NAME"]+'</a>'+
						'<p style="font-size:12px;">积分：<strong style="color:red;">'+temp["SCORE"]+'</strong></p>'+
						'</div>'+	 
						'</dd>'+
						'</dl>'+
						'</li>'	
				);
			});
		}
	});
});

//专家最新动态中点击标题后的链接
var linkActivity = function(actCode,dataId,dataDisName,qId,aId){
	//关注了主题
	if("ZHIDAO_FOLLOW_ASK" == actCode){
		view(dataId,dataDisName);
	}
	//提出了主题
	if("ZHIDAO_CREATE_ASK" == actCode){
		view(dataId,dataDisName);
	}
	//回复了主题
	if("ZHIDAO_CREATE_ANSWER" == actCode){
		viewAnswer(qId,dataDisName,aId);
	}
	//关注了分类
	if("ZHIDAO_FOLLOW_CATEGORY" == actCode){
		viewCategory(dataId);
	}
	//关注了(人)
	if("ZHIDAO_FOLLOW_USER" == actCode){
		othersZhidao(tempId,dataId);
	}
	//赞同了回复
	if("ZHIDAO_LIKE_ANSWER" == actCode){
		viewAnswer(qId,dataDisName,aId);
	}
}