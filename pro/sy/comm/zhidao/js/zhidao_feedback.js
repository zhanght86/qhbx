//JavaScript Document
//知道反馈功能
jQuery(document).ready(function(){
	//向页面追加"意见反馈"的按钮
	jQuery("#js-jump-feedback").append(
		'<div class="jump-feedback">'+
			'<a id="js-jump-feedback-a" target="_self" href="#">意见反馈</a>'+
			'<s class="s-point-lt"></s>'+
			'<s class="s-point-rt"></s>'+
			'<s class="s-point-lb"></s>'+
			'<s class="s-point-rb"></s>'+
		'</div>'
	);
	
	//给"意见反馈"按钮绑定点击事件
	jQuery("#js-jump-feedback-a").live("click",function(){
		//意见反馈，动态添加提意见页面
		loadFeedBack();
	});
});


//将"意见和反馈"页面加载进页面
var loadFeedBack = function(){
	this.winDialog = jQuery('<div id="feedBackDiv" class="modal-dialog with-button " tabindex="0" style="left: 463.5px; top: 200px;" role="dialog" aria-labelledby=":c8"></div>');
	
	//获取建议反馈类型字典的内容
	var reasonList = parent.FireFly.getDict("SY_COMM_FEEDBACK_TYPE")[0]["CHILD"];
	var optionsStr = "";
	jQuery(reasonList).each(function(index,temp){
		optionsStr += '<option value="'+temp["ITEM_CODE"]+'">'+temp["ITEM_NAME"]+'</option>';
	});
	
	this.winDialog.append(
			  '<div class="modal-dialog-title modal-dialog-title-draggable" id=":c8">'+
			  	'<span class="modal-dialog-title-text">建议和反馈</span>'+
				'<span class="modal-dialog-title-close"></span>'+
			  '</div>'+
			  '<div class="modal-dialog-content">'+
			    '<select class="zg-editor-type zg-right">'+
			      optionsStr+
			    '</select>'+
			    '<div class="zg-report-title">'+
					'<span class="zg-gray-darker">请填写您的反馈内容：</span>'+ 
					'<span style="display:none;" class="zg-report-msg">请选择反馈种类：</span>'+
			    '</div>'+
			    '<div class="zg-editor-simple-wrap zg-form-text-input zg-report-content">'+
			      '<textarea name="content" class="zg-editor-input zu-seamless-input-origin-element" style="white-space: pre; height: 66px; min-height: 66px;"></textarea>'+
			    '</div>'+
			  '</div>'+
			  '<div class="modal-dialog-buttons">'+
			    '<button name="cancel" class="zg-link-btn zm-command-cancel">取消</button>'+
			    '<button name="yes" class="zg-btn-blue">确认</button>'+
			  '</div>'
	);
	
	//向body中追加反馈输入框
	this.winDialog.appendTo(jQuery("body"));
	
	//给关闭按钮绑定事件
	jQuery(".modal-dialog-title-close").bind("click",function(){
		jQuery("#feedBackDiv").remove();
	});
	
	//给取消按钮绑定事件
	jQuery("button[name='cancel']").bind("click",function(){
		jQuery("#feedBackDiv").remove();
	});
	
	//给确认按钮绑定事件
	jQuery("button[name='yes']").bind("click",function(){
		var content = jQuery("textarea[name='content']").val();
		if(content == ''){
			alert("输入的内容不能为空！");
		}else{
			//向后台添加数据
			var param = {};
			if(content.length > 10){
				param["F_TITLE"] = content.substring(0,10);
			}else{
				param["F_TITLE"] = content;
			}
			param["F_CONTENT"] = content;
			param["F_TYPE"] = jQuery(".zg-editor-type").val();
			parent.FireFly.doAct("SY_COMM_ZHIDAO_FEEDBACK","addFeedBack",param,false);
			
			//成功后，关闭出题框
			jQuery("button[name='cancel']").trigger("click");
		}
	});
}
