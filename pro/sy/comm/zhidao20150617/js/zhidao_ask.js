jQuery(document).ready(function(){
	//给提问输入框绑定键盘输入事件
	jQuery("#zh-question-suggest-title-content").live("keyup",function(){
		//输入的内容，以后使用它作为过滤条件来过滤问题
		//var value = jQuery().trim();
		var value = jQuery.trim(jQuery(this).val());

		//alert(value);
		
		//如果没输入就返回
		if(value.length < 1){
			return;
		}
		
		//过滤条件
		var filterArray = [];
    	var filter = {};
		filter["id"] = "service";
		filter["data"] = "SY_COMM_ZHIDAO_QUESTION";
		filterArray.push(filter);
		//filterArray.concat(filter);
		//alert(jQuery.toJSON(filter));
		//filterArray = "["+jQuery.toJSON(filter)+"]";
		
		//var filterArray = "[{id=service, data=SY_COMM_ZHIDAO_QUESTION}]";
		
		//设置每页显示条数
		var _PAGE_ = {"SHOWNUM":"5","NOWPAGE":"1"}
		
		//传入的参数
		var param = {}
		param["KEYWORDS"] = value;
    	param["FILTER"] = filterArray;
    	param["_PAGE_"] = _PAGE_;
    	param["MBFLAG"] = true;
    	
    	//console.debug(param);
    	
		var result = parent.FireFly.getPageData("SY_PLUG_SEARCH",param);
		
		//console.debug(result);
		
		var ques_list = result["_DATA_"];
		jQuery(".js_question_list_remove").remove();
		jQuery(".js_answer_list").remove();
		//alert(jQuery(ques_list).length);
		if(jQuery(ques_list).length > 0){
			for(var i=0; i<ques_list.length; i++){
				var question = jQuery(ques_list).get(i);
				//返回的ID是这样的：SY_COMM_ZHIDAO_QUESTION,24pSY9td94TofLxK7jldmyn
				var ques_id = question.id.substring(24,question.id.length);
				var ques_title = question.title.replace(/<cite>/g,"");
				ques_title = ques_title.replace(/<\/cite>/g,"")
				jQuery("#question_list").append(
						'<div style="-moz-user-select: none;" aria-expanded="false" role="tab" tabindex="0" class="ac-row goog-zippy-header goog-zippy-collapsed js_question_list js_question_list_remove" data-url_token="20012824" data-answer_count="9">'+
						'<input type="hidden" class="input_q_id" value='+ques_id+' />'+
						'<input type="hidden" class="input_q_title" value='+ques_title+' />'+
						'<a style="-moz-user-select: none;" class="zippy-indicator">'+
						'<i style="-moz-user-select: none;"></i>'+
						'</a>'+
						'<a style="color: rgb(34, 34, 34); -moz-user-select: none;" onclick="view(\''+ques_id+'\',\''+ques_title+'\');" href="#">'+
						question.title+
						'</a>'+
						'<!--<span style="-moz-user-select: none;" class="zm-ac-gray">'+
						question.Q_ANSWER_COUNTER+'个回答'+
						'</span>-->'+
						'</div>'+
						'<div style="" class="goog-zippy-content div_hide js_question_list_remove">'+
						'</div>'
				);
			}
		}
		jQuery("#zh-question-suggest-ac-wrap").removeClass("div_hide");
		backgroundChange();
	});
});


	//给每一个问题绑定鼠标移入/移出事件和绑定鼠标点击事件 
	var backgroundChange = function(){
		jQuery(".js_question_list").live("mouseover",function(){
			jQuery(this).css("backgroundColor","#E7F3F9");
		});
		jQuery(".js_question_list").live("mouseout",function(){
			jQuery(this).css("backgroundColor","#FFFFFF");
		});
		
		jQuery(".js_question_list").toggle(
			function(){
				//拿到了问题ID
				var q_id = jQuery(jQuery(this).children(".input_q_id").get(0)).val();
				var q_title = jQuery(jQuery(this).children(".input_q_title").get(0)).val();
				jQuery(this).css("backgroundColor","#E7F3F9");
				var ask_param = {};
				ask_param["_extWhere"] = " and Q_ID='"+q_id+"'";
				ask_param["_ORDER_"] = "A_LIKE_VOTE DESC";
				ask_param["_PAGE_"] = {"SHOWNUM":"1","NOWPAGE":"1"}
				var ask_result = parent.FireFly.getPageData("SY_COMM_ZHIDAO_ANSWER",ask_param);
				var ask_list = ask_result["_DATA_"];
				var ask_num = ask_result["_OKCOUNT_"]
				//找到当前的div
				var ask_div = jQuery(this).next();
				
				
				//将箭头变成向上的
				jQuery(this).find("i").css({"backgroundPosition":"-12px -21px"});
				
				//做一个答案列表的div
				var ask_list_div = jQuery("<div class='zippy-row js_answer_list'></div>");
				
				
				//先做个3条，以后再限制
				for(var ask_i=0; ask_i<ask_list.length; ask_i++){
					var ask = jQuery(ask_list).get(0);
					var ask_real_content = '';
					var ask_content_num = ask.A_CONTENT.length;
					if(ask_content_num > 25){
						ask_real_content = ask.A_CONTENT.substring(0,25)+"...";
					}else{
						ask_real_content = ask.A_CONTENT;
					}
					ask_list_div.html(
							'<div class="summary-item">'+
								'<a href="javascript:view(\''+q_id+'\',\''+q_title+'\')">'+
									'<span class="zm-item-vote-count">'+
										ask.A_LIKE_VOTE+
									'</span>'+
								'</a>'+
								'<a style="vertical-align: top;" href="#">'+
									'<span class="author">'+
										ask.S_USER__NAME+
									'</span>'+
								'</a>'+
								'<div class="ellipsis" style="max-width: 65%;display: inline-block; ">'+
									ask_real_content+
								'</div>'+
								'<a style="vertical-align: top;" href="javascript:view(\''+q_id+'\',\''+q_title+'\')">'+
									'<span class="readall">'+
										'阅读全部»'+
									'</span>'+
								'</a>'+
							'</div>'
					);
				}
				ask_div.html(ask_list_div);
				var ask_num_str = "";
				if(ask_num > 0){
					ask_num_str = "查看该问题的全部"+ask_num+"个回答";
				}else{
					ask_num_str = "查看该问题";
				}
				ask_div.append(
						'<div class="zippy-row js_answer_list">'+
							'<a class="zg-link-litblue" href="javascript:view(\''+q_id+'\',\''+q_title+'\')">'+
							ask_num_str+
							'</a>'+
						'</div>'
				);
				ask_div.show(500);
			},
			function(){
				jQuery(this).next().hide(500);
				//将箭头变成向上的
				jQuery(this).find("i").css({"backgroundPosition":"-12px -16px"});
			});
		
	}
	
	
jQuery(document).ready(function(){
	//图片点击触发
	jQuery("#ask-type").unbind("click").live("click",function(event){
		//构造树形选择参数
		var configStr = "SY_COMM_ZHIDAO_CHNL_MANAGE,{}";
		var options = {
			"config" :configStr,
			"replaceCallBack":function(idArray,nameArray){ //回调，idArray为选中记录的相应字段的数组集合
				jQuery("#talk_title").val(nameArray);
				jQuery("#talk_title_id").val(idArray);
			}
		};
		//显示树形
		var dictView = new parent.rh.vi.rhDictTreeView(options);
		dictView.show(event);
		parent.jQuery("#SY_COMM_ZHIDAO_CHNL_MANAGE-dictDialog").parent().css("background","white")		
	});
	//获得焦点触发
	jQuery("#talk_title").unbind("focus").live("focus",function(event){
		//构造树形选择参数
		var configStr = "SY_COMM_ZHIDAO_CHNL_MANAGE,{}";
		var extendTreeSetting = "{'childOnly':true}";
		var options = {
			"config" :configStr,
			"extendDicSetting":StrToJson(extendTreeSetting),
			"replaceCallBack":function(idArray,nameArray){ //回调，idArray为选中记录的相应字段的数组集合
				jQuery("#talk_title").val(nameArray);
				jQuery("#talk_title_id").val(idArray);
			}
		};
		//显示树形
		var dictView = new parent.rh.vi.rhDictTreeView(options);
		dictView.show(event);
		parent.jQuery("#SY_COMM_ZHIDAO_CHNL_MANAGE-dictDialog").parent().css("background","white")		
	});
	
	
	//发布问题的JS
	jQuery("#sub-btn").unbind("click").live("click",function(){
		//提问的内容
		var quesitionTtle = jQuery("#zh-question-suggest-title-content").val();
		//话题的ID
		var askType = jQuery("#talk_title_id").val();
		//是否匿名
		var anonymous = 2;
		if(jQuery("#anonymous").attr("checked")){
			anonymous = 1;
		}
		//求助人
		var targetAsk = jQuery("#targetAsk").text();
		//问题的说明
		//var askContent = jQuery(jQuery("#question_desc").html()).trim();
		var askContent = jQuery.trim(jQuery("#question_desc").html());
		//当前用户
		var S_USER = parent.System.getVar("@USER_CODE@");
		if (!jQuery("#questionIdHid").val()) {
			if (quesitionTtle && quesitionTtle!='提问' && quesitionTtle!='写下你的问题') {
				if (askType) {
				//alert(quesitionTtle+"/"+anonymous+"/"+askContent+"/"+S_USER+"/"+targetAsk+"/"+askType);
					var questionData = parent.FireFly.doAct("SY_COMM_ZHIDAO_QUESTION", "save", {
						"Q_TITLE": quesitionTtle,
						"Q_ANONY": anonymous,
						"Q_CONTENT": askContent,
						"S_USER": S_USER,
						"TARGET_ASK": targetAsk,
						"CHNL_ID": askType
					},true);
					//alert(questionData._PK_);
					jQuery("#questionIdHid").val(questionData._PK_);
					alert("保存成功！");
					
					//这句是干什么的？？？
				    //parent.jQuery("#homeTabs").find("ul li[pretabid='cmstmpl3O0F5DwFV5yoWFL5CwR_yb-html-tabDiv'] .tab-close").mousedown();
					
					//显示查看问题按钮
					//jQuery("#look_question").show().click(function(){
						//alert(questionData._PK_);
						//alert(quesitionTtle);
						view(questionData._PK_,quesitionTtle);
					//});
					
					//将提问窗口关闭
					jQuery(".modal-dialog-title-close").trigger("click");
				}
				else {
					alert("请选择问题类型！");
				}
			}
			else {
				alert("请填写问题标题！");
			}
		}else{
			alert("您的问题已经保存成功！");
		}
	});
	
	//为关闭按钮绑定事件
	jQuery(".modal-dialog-title-close").unbind().live("click",function(){
		jQuery("#tiwen_dialog").remove();
	});
});


//将提问页面整体使用js加载进去
var loadTiWen = function(person_name,person_id){
	this.winDialog = jQuery('<div id="tiwen_dialog" style="position:absolute; top:50px;"></div>');
	this.winDialog.append(
			'<div class="layout-center zhi" style="background-color:white;">'+
			'<input type="hidden" id="questionIdHid">'+
		'<div style="display:none"> </div>'+
		'<div id="zh-tooltip"></div>'+
		'<div style="opacity: 0.5; width: 1349px; height: 3776px;" class="modal-dialog-bg"></div>'+
		'<div aria-labelledby=":4e" role="dialog" style="width: 550px; left: 398.5px;" tabindex="0" class="modal-dialog absolute-position">'+
		  '<div id=":4e" class="modal-dialog-title modal-dialog-title-draggable">'+
		  	'<span class="modal-dialog-title-text">提问</span>'+
		  	'<span class="modal-dialog-title-close"></span>'+
		  '</div>'+
		  '<div class="modal-dialog-content">'+
		    '<div id="zh-add-question-wrap" class="zh-add-question-form">'+
		      '<form style="display: block;" class="js-add-question-form">'+
		        '<div class="zg-section-big clearfix">'+
		          '<div id="zm-modal-dialog-info-wrapper"></div>'+
		          '<div style="display: none;position: relative;" id="zm-modal-dialog-warnmsg-wrapper">'+
		            '<div class="zm-modal-dialog-warnmsg zm-modal-dialog-guide-warn-message zg-r5px">您还没有给问题添加问号</div>'+
		            '<a name="close" title="关闭" href="javascript:;" class="zu-global-notify-close" style="display:none">x</a>'+ 
		            '<span class="zm-modal-dialog-guide-title-msg"></span>'+
		          '</div>'+
		          '<div class="zg-form-text-input add-question-title-form" style="position: relative;">'+
		            '<textarea aria-autocomplete="list" role="combobox" aria-activedescendant="" style="white-space: pre; height: 22px; min-height: 22px;" autocomplete="off" aria-haspopup="true" placeholder="写下你的问题" aria-label="写下你的问题" class="zg-editor-input zu-seamless-input-origin-element" title="在这里输入问题" id="zh-question-suggest-title-content"></textarea>'+
		          '</div>'+
		          '<span style="display: none;" id="js-title-length-err-msg" class="title-length-err-msg zg-right">问题字数太少了吧</span>'+
		          '<div id="zh-question-suggest-ac-wrap" class="question-suggest-ac-wrap div_hide">'+
		            '<div id="question_list" role="listbox" class="ac-renderer" style="-moz-user-select: none;">'+
		              '<div style="-moz-user-select: none;" class="ac-head zg-gray">你的问题可能已经有答案</div>'+
		            '</div>'+
		          '</div>'+
		        '</div>'+
		        '<div class="zg-section-big">'+
		          '<div class="add-question-section-title">'+
		          	'问题说明（可选）：'+
		          	'<span id="zh-question-form-detail-err"></span>'+
		          '</div>'+
		          '<div class="zm-editable-status-editing" id="zh-question-suggest-detail-container">'+
		            '<div style="display: none;" class="zm-editable-content" data-disabled="1">&nbsp;</div>'+
		            '<div style="" class="zm-editable-editor-wrap no-toolbar">'+
		              '<div class="zm-editable-editor-outer">'+
		                '<div class="zm-editable-editor-field-wrap">'+
		                  '<div style="" g_editable="true" class="zm-editable-editor-field-element editable" id="question_desc" placeholder="写下你对问题的说明" contenteditable="true">'+
		                  '</div>'+
		                '</div>'+
		              '</div>'+
		              '<div class="zm-command">'+
		              	'<a href="javascript:;" name="cancel" class="zm-command-cancel">取消</a>'+
		              	'<a href="javascript:;" name="save" class="zg-r3px zg-btn-blue">保存</a>'+
		              '</div>'+
		            '</div>'+
		          '</div>'+
		        '</div>'+
		        '<div class="zm-add-question-form-topic-wrap">'+
		          '<div class="add-question-section-title"> <span class="zg-gray zg-right">分类越精准，越容易让领域专业人士看到你的问题</span> 选择分类： <span id="zh-question-form-tag-err"></span> </div>'+
		          '<div style="" id="zh-question-suggest-topic-container" class="zm-tag-editor zg-section">'+
		            '<div style="display: none;" class="zm-tag-editor-labels"></div>'+
		            '<div class="zm-tag-editor-editor zg-clear">'+
		              '<div class="zg-inline"></div>'+
		              '<div class="zm-tag-editor-command-buttons-wrap zg-left">'+
		                '<input id="talk_title" readonly="readonly" value="选择分类" aria-activedescendant=":4t" aria-autocomplete="list" role="combobox" placeholder="搜索分类" aria-label="搜索分类" aria-haspopup="true" class="zu-question-suggest-topic-input label-input-label" type="text">'+
		                '<input type="hidden" id="talk_title_id" value="" />'+
		                '<a style="display: none;" name="add" href="#" class="zg-mr15 zg-btn-blue">添加</a>'+
		                '<a style="display: none;" name="close" href="#">完成</a>'+
		                '<label id="ask-type" for="topic" class="zg-icon search-icon"></label>'+
		                '<label class="err-tip" style="display:none;">最多添加五个分类</label>'+
		              '</div>'+
		              '<div style="display: none;" class="zm-tag-editor-maxcount zg-section">'+
		              	'<span>最多只能为一个问题绑定5 个话题</span>'+
		              	'<a style="display: none;" name="close" href="#">完成</a>'+
		              '</div>'+
		            '</div>'+
		           '<div id="zh-question-suggest-autocomplete-container"></div>'+
		          '</div>'+
		          '<div id="target_person" class="sug-con zg-clear">'+
		          '</div>'+
		        '</div>'+
		        '<div class="zm-command">'+
		          '<label class="zm-question-form-hide-in-about-question-el zg-left">'+
		          	'<input id="anonymous" class="zg-addq-isanon" type="checkbox" style="vertical-align:-5px;">匿名'+
		          '</label>'+
		          '<a href="javascript:void(0);" id="sub-btn" name="addq" class="zg-r5px zu-question-form-add zg-btn-blue">发布</a>'+ 
		          '<a id="look_question" href="javascript:void(0);" name="jumpq" class="zg-r5px zg-btn-blue zu-question-form-jump" style="display:none;">查看问题</a>'+
		        '</div>'+
		      '</form>'+
		    '</div>'+
		  '</div>'+
		 '<div style="display: block;" class="modal-dialog-buttons"></div>'+
		'</div>'+
		'<span tabindex="0" style="position: absolute; left: 398.5px; top: 38.5px;"></span>'+
		'<div></div>'+
		'</div>'
	);
	
	//先把元素放入body中
	this.winDialog.appendTo(jQuery("body"));
	
	//再向div中追加元素
	if(person_name != undefined && person_name != "" && person_id != undefined && person_id != ""){
		//向页面中添加一个"求助对象"的div
		jQuery("#target_person").append(
				'<div class="fix-ask">'+ 
				'<span>求助对象：</span>'+   
				'<span>'+  
				'<a target="_self" href="javascript:othersZhidao(\''+center_temp_id+'\',\''+person_id+'\')">'+person_name+'</a>'+ 
				'<span id="targetAsk" style="display:none;">'+person_id+'</span>'+
				'<span id="fix-del" title="取消求助"></span>'+ 
				'</span>'+  
				'</div>'
		);
		//给"求助对象"的关闭按钮绑定事件
		jQuery("#fix-del").live("click",function(){
			jQuery("#target_person").remove();
		});
	}
}