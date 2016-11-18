/*知道分享的JS，在用到分享的页面同CSS一起引入*/
jQuery(document).ready(function(){
	jQuery(".js-zhidao-share").bind("click",function(){
		//获取回答内容
		var servId = jQuery(this).attr("servId");
		var content = "";
		var title = "";
		var allContent = "";
		var q_title = jQuery("#js-qTitle-span").text().trim();
		if(servId == 'SY_COMM_ZHIDAO_ANSWER'){
			content = jQuery(this).parent().prev().find("span").text();
			content = content.trim();
			
			if(content.length > 30){
				content = content.substring(0,30) + "...";
			}
			title = "分享答案";
			//获取问题内容
			var userName = jQuery(this).parent().prev().prev().find(".js-userName").text();
			allContent = "["+q_title+"]"+"@"+userName.trim()+":"+content+"(分享自@"+parent.System.getVar("@USER_NAME@")+")";
			
		}else{
			title="分享问题";
			allContent = q_title+"(分享自@"+parent.System.getVar("@USER_NAME@")+")";
		}
		var dataId = jQuery(this).attr("dataId");
		
		
		
		
		this.winDialog = jQuery('<div id="js_zhidao_share_dialog" style="position:absolute; top:50px;"></div>');
		this.winDialog.append(
				'<div class="modal-dialog" tabindex="0" style="left: 463.5px; top: 83.5px;" role="dialog" aria-labelledby=":aa">'+
				  '<div class="modal-dialog-title modal-dialog-title-draggable" id=":aa">'+
				  	'<span class="modal-dialog-title-text">'+title+'</span>'+
					'<span id="js-share-close" class="modal-dialog-title-close"></span>'+
				  '</div>'+
				  '<div class="modal-dialog-content">'+
				    '<div class="zh-webshare-dialog" id="zh-webshare-dialog">'+
				      '<ul class="zm-common-nav-bar zg-clear">'+
				        '<li class="zm-common-nav-bar-item">'+ 
							'<a id="tb-webshare-sina" class="zm-common-nav-bar-link zm-common-nav-bar-current-light" href="javascript:;">知道分享</a>'+ 
						'</li>'+
				      '</ul>'+
				      '<div style="" class="zm-tab-content" id="tb-webshare-letter-content">'+
				        '<div class="zm-pm-wrap">'+
				          '<dl class="zm-form-table zm-form-table-medium">'+
				            '<dt class="zm-form-table-head zm-form-table-head-align-middle">'+
				              '<label class="zg-medium-gray">发给：</label>'+
				            '</dt>'+
				            '<dd class="zm-form-table-field">'+
				              '<div class="zm-pm-selector-wrap">'+
				                '<div id="js-div-choise" style="display:none;padding:4px 0 0 0" class="zg-user-name"></div>'+
				                '<input id="js-input-choise" type="text" class="zg-form-text-input zm-pm-user-selector label-input-label" placeholder="选择用户" aria-haspopup="true">'+
				              '</div>'+
				            '</dd>'+
				            '<dt class="zm-form-table-head zm-form-table-head-align-middle">'+
				              '<label class="zg-medium-gray">内容：</label>'+
				            '</dt>'+
				            '<dd class="zm-form-table-field zm-form-table-field-last">'+
				              '<div class="zg-editor-simple-wrap zg-form-text-input">'+
				                '<textarea id="js-share-content" style="font-weight: normal; white-space: pre;" class="zg-editor-input zu-seamless-input-origin-element">'+
				                allContent+
				                '</textarea>'+
				              '</div>'+
				            '</dd>'+
				          '</dl>'+
				          '<div style="display:none;text-align:right;color:#C3412F;" class="zh-pm-warnmsg"></div>'+
				          '<div class="zm-command zg-clear">'+
				            '<div style="margin: 0 0 0 57px;" class="zm-item-meta zg-left">'+
								'<span id="js-share-msg" class="zg-text-counter"></span>'+
								'<span class="additional-info"></span>'+
							'</div>'+
				            '<!--<a class="zm-command-cancel" href="javascript:;" name="cancel">取消</a>-->'+
							'<a id="js-submit-a" class="zg-btn-blue zg-r3px" href="javascript:;" name="send">发送</a>'+
						  '</div>'+
				        '</div>'+
				      '</div>'+
				    '</div>'+
				  '</div>'+
				  '<div class="modal-dialog-buttons" style="display: none;"><span id="dataId-span">'+dataId+'</span><span id="servId-span">'+servId+'</span></div>'+
				'</div>'	
		);
		//先把元素放入body中
		this.winDialog.appendTo(jQuery("body"));
		//再向div中追加元素

	});
	
	
	
	//关闭按钮绑定事件
	jQuery("#js-share-close").live("click",function(){
		jQuery("#js_zhidao_share_dialog").remove();
	});
	
	
	//给提交按钮绑定事件
	jQuery("#js-submit-a").live("click",function(){
		var targetUser = jQuery("#js-div-choise").text();
		var param = {};
		param["TARGET_USER"] = targetUser;
		param["DATA_ID"] = jQuery("#dataId-span").text();
		param["SERV_ID"] = jQuery("#servId-span").text();
		param["SHARE_CONTENT"] = jQuery("#js-share-content").val();
		if(targetUser.length <= 0){
			alert("请选择用户！");
			return false;
		}
		if(jQuery("#js-share-content").val().length <= 0){
			alert("请填写分享内容！");
			return false;
		}
		parent.FireFly.doAct("SY_COMM_ZHIDAO_SHARE","addShare",param,false);
		jQuery("#js-share-msg").text("分享成功！");
		jQuery("#js-submit-a").remove();
	});
	
	//给"选择用户"绑定事件
	jQuery("#js-input-choise").live("focus",function(event){
		//构造树形选择参数
		var configStr = "SY_ORG_DEPT_USER_SYNC,{'TYPE':'multi'}";
		var extendTreeSetting = "{'childOnly':true}";
		var options = {
				"config" :configStr,
				"extendDicSetting":StrToJson(extendTreeSetting),
				"replaceCallBack":function(idArray,nameArray){ //回调，idArray为选中记录的相应字段的数组集合
					jQuery("#js-div-choise").text(idArray);
					jQuery("#js-input-choise").val(nameArray);
				}
		};
		//显示树形
		var dictView = new parent.rh.vi.rhDictTreeView(options);
		dictView.show(event);
		parent.jQuery("#SY_ORG_DEPT_USER_SYNC-dictDialog").parent().css("background","white");
	});
	
});

