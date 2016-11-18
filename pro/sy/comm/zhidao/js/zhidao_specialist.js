// JavaScript Document
//֪��ר��ҳ��JS
jQuery(document).ready(function(){
	//�������б���¼�
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
		
		//���ul������
		jQuery("#js-total-ul").html("");
		jQuery("#js-month-ul").html("");
		jQuery("#js-week-ul").html("");
		
		//׷������
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
						'<span>��TA����</span>'+
						'</b>'+
						'</em>'+
						'</a>'+
						'</div>'+
						'</dt>'+
						'<dd>'+
						'<div class="custom-right">'+
						'<a href="javascript:othersZhidao(\''+tempId+'\',\''+temp["USER"]["USER_CODE"]+'\')">'+temp["USER_NAME"]+'</a>'+
						'<p style="font-size:12px;">���֣�<strong style="color:red;">'+temp["INTEGRAL_VALUE"]+'</strong></p>'+
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
						'<span>��TA����</span>'+
						'</b>'+
						'</em>'+
						'</a>'+
						'</div>'+
						'</dt>'+
						'<dd>'+
						'<div class="custom-right">'+
						'<a href="javascript:othersZhidao(\''+tempId+'\',\''+temp["USER"]["USER_CODE"]+'\')">'+temp["USER_NAME"]+'</a>'+
						'<p style="font-size:12px;">���֣�<strong style="color:red;">'+temp["SCORE"]+'</strong></p>'+
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
						'<span>��TA����</span>'+
						'</b>'+
						'</em>'+
						'</a>'+
						'</div>'+
						'</dt>'+
						'<dd>'+
						'<div class="custom-right">'+
						'<a href="javascript:othersZhidao(\''+tempId+'\',\''+temp["USER"]["USER_CODE"]+'\')">'+temp["USER_NAME"]+'</a>'+
						'<p style="font-size:12px;">���֣�<strong style="color:red;">'+temp["SCORE"]+'</strong></p>'+
						'</div>'+	 
						'</dd>'+
						'</dl>'+
						'</li>'	
				);
			});
		}
	});
});

//ר�����¶�̬�е������������
var linkActivity = function(actCode,dataId,dataDisName,qId,aId){
	//��ע������
	if("ZHIDAO_FOLLOW_ASK" == actCode){
		view(dataId,dataDisName);
	}
	//���������
	if("ZHIDAO_CREATE_ASK" == actCode){
		view(dataId,dataDisName);
	}
	//�ظ�������
	if("ZHIDAO_CREATE_ANSWER" == actCode){
		viewAnswer(qId,dataDisName,aId);
	}
	//��ע�˷���
	if("ZHIDAO_FOLLOW_CATEGORY" == actCode){
		viewCategory(dataId);
	}
	//��ע��(��)
	if("ZHIDAO_FOLLOW_USER" == actCode){
		othersZhidao(tempId,dataId);
	}
	//��ͬ�˻ظ�
	if("ZHIDAO_LIKE_ANSWER" == actCode){
		viewAnswer(qId,dataDisName,aId);
	}
}