jQuery(document).ready(function(){
	
		var USER_CODE = parent.System.getVar("@USER_CODE@");
		//检查是否有满意答案
		var len = jQuery(".best-answer").length;
		if(len){
			//去除 选为满意答案 btn
			jQuery(".zm-item-answer").find(".js-zd-btn-best").remove();
			//高亮 满意答案
			jQuery(jQuery(".best-answer")).each(function(index,item){
				var who = jQuery(item).attr("who");
				if(who == 'askerAndAdmin'){
					jQuery(item).closest(".zm-item-answer").css({"background-color":"rgb(251,227,228)","padding":"10px","border-color":"rgb(251,227,228)"});
				}else if(who == 'asker'){
					jQuery(item).closest(".zm-item-answer").css({"background-color":"#E0F1C5","padding":"10px"});
				}else if(who == 'admin'){
					jQuery(item).closest(".zm-item-answer").css({"background-color":"rgb(252,255,232","padding":"10px","border-color":"rgb(252,255,232)"});
				}
				
			});
		}
		
		//删除掉多余的"其它XX个回答"
		jQuery(jQuery(".js-more-other-answer")).each(function(index,item){
			if(index != 0){
				jQuery(item).remove();
			}
		});
		//判断是否是该用户，如果不是，不允许选为满意答案
	 	if(userId!=USER_CODE){
			//去除 选为满意答案 btn
			jQuery(".zm-item-answer").find(".js-zd-btn-best").remove();
		}
	 	
	 	//判断是否当前用户具有管理员选择权限
	 	
	 	
	 	

		/**加载相似问题**/
		loadRelatedQuestions();
		/**加载待回答的问题**/
		loadWaitAnswerQuestions();
	
	
		
		var eQ_USER = jQuery(this).find("input[name='editQ_USER']").val();
		if (eQ_USER != USER_CODE) {
			jQuery("a[name='questionEdit']").hide();				
		}
		
		jQuery("a[name='answerEdit']").each(function(){
			var eS_USER = jQuery(this).find("input[name='editS_USER']").val();
			if (eS_USER != USER_CODE) {
				jQuery(this).hide();
			}
		})
		
		jQuery("a[name='answerDel']").each(function(){
			var eS_USER = jQuery(this).find("input[name='delS_USER']").val();
			if (eS_USER != USER_CODE) {
				jQuery(this).hide();
			}
		})
		
		//渲染编辑器
		function createEditor(id,Width,Height){
	        var _self = this;
	        toolbars = [['undo', 'redo', 'bold', 'italic', 'underline', 'strikethrough', 'link', 'unlink', 'insertimage', 'emotion', '|', 'attachment', 'map']];   
	        var config = {
	            initialFrameWidth: Width,
				initialFrameHeight: Height,
	            minFrameHeight: "0",
	            autoHeightEnabled:false,
	            zIndex: 1050,
	            toolbars: toolbars,
	            initialContent: '',
				wordCount:false,
				elementPathEnabled:false
	        };
	        UE.getEditor(id, config).ready(function(){
				
	        });
			return UE.getEditor(id);
		}
		
		var editor = createEditor("editor",680,140);
		//对保存按钮绑定事件
		jQuery("a[name='save']").unbind("click").bind("click",function(){
			var checkBoxState = 2;
			if(jQuery("input[name='anno-checkbox']").attr("checked")=="checked"){
				checkBoxState = 1;
			}
			var A_CONTENT = editor.getContent();
			parent.FireFly.doAct("SY_COMM_ZHIDAO_ANSWER", "save", {"Q_ID":Q_ID,"A_CONTENT":A_CONTENT,"A_ANONY":checkBoxState});
			window.location.reload();
		});
		//对本人答案修改按钮绑定事件
		jQuery("a[name='answerEdit']").unbind("click").bind("click",function(event){
			var editS_USER = jQuery(this).find("input[name='editS_USER']").val();
			if (editS_USER == parent.System.getVar("@USER_CODE@")) {
				var editA_ID = jQuery(this).find("input[name='editA_ID']").val();
				var A_CONTENT = "";
				jQuery(this).prevAll().each(function(i, n){
					A_CONTENT = n.innerHTML + A_CONTENT;
				});
				getDialog(event,310,650);
				showAnswerItems(editA_ID,A_CONTENT);
			}else{
				alert("您没有权限！");
			}
		});
		
		//渲染dialog
		var ModiEditor, 
			ModiQuestionEditor;
		function getDialog(event,height,width){
			var dialogId = "answerEditDia";
			var winDialog = jQuery("<div></div>").attr("id",dialogId).attr("title","修改信息");
			winDialog.appendTo(jQuery("body"));
			var hei = height;
		    var wid = width;
		    var posArray = [30,30];
		    if (event) {
			    var cy = event.clientY;
			    posArray[0] = "";
			    posArray[1] = 30;
		    }
			jQuery("#answerEditDia").dialog({
				autoOpen: false,
				height: hei,
				width: wid,
				modal: true,
				resizable: false,
				position:posArray,
				open: function() {
		
				},
				close: function() {
					if(ModiEditor){
						ModiEditor.destroy();
					}
					if(ModiQuestionEditor){
						ModiQuestionEditor.destroy();
					}
					jQuery("#" + dialogId).remove();					
				}
			});
			var dialogObj = jQuery("#" + dialogId);
			dialogObj.dialog("open");
			dialogObj.focus();
		    jQuery(".ui-dialog-titlebar").last().css("display", "block");
			dialogObj.parent().addClass("rh-bottom-right-radius rhSelectWidget-content");
			Tip.showLoad("努力加载中...", null, jQuery(".ui-dialog-title", winDialog).last());
		}
		//展示答案修改dialog
		function showAnswerItems(editA_ID,A_CONTENT){
			jQuery("<div id='ModiEditor'></div>").appendTo(jQuery("#answerEditDia"));
			var answerEditSave = jQuery("<div></div>").appendTo(jQuery("#answerEditDia"));
			var answerBtn = jQuery("<a class='rh-icon rhGrid-btnBar-a' style='margin-left: 45%;margin-top: 10px;'></a>").appendTo(answerEditSave);
			jQuery("<span class='rh-icon-inner'> 保 存 </span>").appendTo(answerBtn);
			jQuery("<span class='rh-icon-img btn-batchSave'></span>").appendTo(answerBtn);
			answerBtn.unbind("click").bind("click",function(){
				parent.FireFly.doAct("SY_COMM_ZHIDAO_ANSWER", "save", {"_PK_":editA_ID,"A_CONTENT":ModiEditor.getContent()});
				window.location.reload();
			});
			ModiEditor = createEditor("ModiEditor","100%",200);
			ModiEditor.ready(function(){
				ModiEditor.setContent(A_CONTENT);
			})
		}
		//对删除本人回答绑定事件
		jQuery("a[name='answerDel']").unbind("click").bind("click",function(event){
			var delS_USER = jQuery(this).find("input[name='delS_USER']").val();
			if (delS_USER == parent.System.getVar("@USER_CODE@")) {
				var delA_ID = jQuery(this).find("input[name='delA_ID']").val();
				var r = confirm("您确定删除么？");
				if(r){
					parent.FireFly.doAct("SY_COMM_ZHIDAO_ANSWER", "delete", {"_PK_":delA_ID});
					window.location.reload();
				}
			}else{
				alert("您没有权限！");
			}
		});
		//对问题的修改绑定事件
		jQuery("a[name='questionEdit']").unbind("click").bind("click",function(event){
			var editQ_USER = jQuery(this).find("input[name='editQ_USER']").val();
			if (editQ_USER == parent.System.getVar("@USER_CODE@")) {
				var editQ_ID = jQuery(this).find("input[name='editQ_ID']").val();
				var editQ_TITLE = jQuery(this).prev("span").html();
				var editQ_CONTENT = jQuery("#zh-question-detail").children().html();
				getDialog(event,330,650);
				showQuestionItems(editQ_ID,editQ_TITLE,editQ_CONTENT);				
			}else{
				alert("您没有权限！");
			}
		});
		//展示问题修改dialog
		function showQuestionItems(editQ_ID,editQ_TITLE,editQ_CONTENT){
			var questionTitle = jQuery("<div style='margin: 2% 0 2% 2%'></div>").appendTo(jQuery("#answerEditDia"));
			jQuery("<span>问题标题：</span><input id='editQ_TITLE' type='text' style='width: 500px;' value='"+editQ_TITLE+"'>").appendTo(questionTitle);
			jQuery("<div id='ModiQuestionEditor'></div>").appendTo(jQuery("#answerEditDia"));
			var questionEditSave = jQuery("<div></div>").appendTo(jQuery("#answerEditDia"));
			var questionBtn = jQuery("<a class='rh-icon rhGrid-btnBar-a' style='margin-left: 45%;margin-top: 10px;'></a>").appendTo(questionEditSave);
			jQuery("<span class='rh-icon-inner'> 保 存 </span>").appendTo(questionBtn);
			jQuery("<span class='rh-icon-img btn-batchSave'></span>").appendTo(questionBtn);
			questionBtn.unbind("click").bind("click",function(){
				if (editQ_TITLE != "") {
					parent.FireFly.doAct("SY_COMM_ZHIDAO_QUESTION", "save", {
						"_PK_": editQ_ID,
						 "Q_TITLE": jQuery("#editQ_TITLE").val(),
						 "Q_CONTENT": ModiQuestionEditor.getContent()
					});
					window.location.reload();
				}else{
					alert("问题标题不能为空！");
				}	
				
			});
			ModiQuestionEditor = createEditor("ModiQuestionEditor","100%",200);
			ModiQuestionEditor.ready(function(){
				ModiQuestionEditor.setContent(editQ_CONTENT);
			})
		}
		
		
		//将原来的关注的代码放进一个方法里，在这里调用这个方法
		attention();
		getAttentionPerson();
		
		
		//邀请回答
		jQuery("a[name='invite']").unbind("click").bind("click",function(event){
			//构造树形选择参数
			var configStr = "SY_ORG_DEPT_USER,{'TYPE':'multi'}";
			//var extendTreeSetting = "{'rhexpand':false,'expandLevel':2,'cascadecheck':false,'checkParent':false,'childOnly':false}";
			var extendTreeSetting = "{'childOnly':true}";
			var options = {
				"config" :configStr,
				"extendDicSetting":StrToJson(extendTreeSetting),
				//非必须参数，一般用不到
				"replaceCallBack":function(idArray,nameArray){ //回调，idArray为选中记录的相应字段的数组集合
					var idArray = idArray.join(",");
					var result = parent.FireFly.doAct("SY_COMM_ZHIDAO_QUESTION", "reqUserAnsQ", {"userCodes":idArray,"Q_ID":Q_ID});
					if(result["msg"]){
						jQuery("#show_msg").html("").html(result["msg"]);
						setTimeout(function(){
							jQuery("#show_msg").html("");
						},5000);
					}
				}
			};
			//显示树形
			var dictView = new parent.rh.vi.rhDictTreeView(options);
			dictView.show(event);
			parent.jQuery("#SY_ORG_DEPT_USER-dictDialog").parent().css("background","white")		
		});
		jQuery("#current_user").find("img").attr("src",FireFly.getContextPath() + parent.System.getVar("@USER_IMG@"));
		jQuery("#current_user").find(".zm-item-link-avatar").attr("title",parent.System.getVar("@USER_NAME@"));
		jQuery("#current_user").find(".zu-answer-form-title").find("a").text(parent.System.getVar("@USER_NAME@"));
		
		//给img和a标签绑定事件
		var imgObj = jQuery("#current_user").find("img");
		var aObj = jQuery("#current_user").find("a");
		var img_a_userCode = parent.System.getVar("@USER_CODE@");
		//tempId  已在页面中定义
		jQuery(imgObj).live("click",function(){
			othersZhidao(tempId,img_a_userCode);
		});
		jQuery(aObj).live("click",function(){
			othersZhidao(tempId,img_a_userCode);
		});
		
		
	//	jQuery(".zhi").css("height",jQuery(document).height());
		
		//为查看更多投票人绑定事件
		jQuery(".more_a").click(function(){
			jQuery(this).hide();
			jQuery(this).next().show();
		});
		
		var dialogInstance=jQuery("#zd-dialog").dialog({
			autoOpen: false,
			modal: true,
			height: 270,
			width: 450,
			dialogClass:"zd-dialog",  //改写dialog样式
			resizable:false,
			closeOnEscape: false 
		});
		dialogInstance.find(".js-dialog-close").on("click",handler);
		dialogInstance.find(".zd-dialog-btn-ths").on("click",handler);
		
		function handler(){
			var answerId=dialogInstance.dialog("option","A_ID");
			var feedback= dialogInstance.find("textarea").val();
				feedback= feedback =="" ? "谢谢!":feedback;
			var param={
					"_PK_": answerId,
					Q_FEEDBACK:feedback
			};
			parent.FireFly.doAct("SY_COMM_ZHIDAO_ANSWER", "updateBestAnswer", param,false,false);
			window.location.reload();
		}
		
		
		//选为满意答案
		jQuery(".js-best-answer").on("click",function(event){
			event.preventDefault();
			//设置回答id
			var answerId=jQuery(this).attr("data-id");
			jQuery("#zd-dialog").dialog("option","A_ID",answerId)
								.dialog("open");
	   });
		//管理员选为满意答案
		jQuery(".js-admin-best-answer").on("click",function(event){
			var answerId = jQuery(this).attr("data-id");
			var param = {
					"_PK_":answerId,
			};
			parent.FireFly.doAct("SY_COMM_ZHIDAO_ANSWER", "updateAdminBestAnswer", param,false,false);
			window.location.reload();
		});
		//问题收藏
	/*	jQuery(".js-favorite").on("click",function(){
			var dataId=jQuery(this).attr("dataId"),
				servId=jQuery(this).attr("servId"),
				act="";
			if(jQuery(this).html() == "收藏"){
				act="addFavorite";
				jQuery(this).html("取消收藏");
			}else{
				act="deleteFavorite";
				jQuery(this).html("收藏");
			}
			parent.FireFly.doAct("SY_COMM_ZHIDAO", act, {"DATA_ID":dataId,"SERV_ID":servId});
		});*/
});//end of ready

/**
 * TODO 有待真实数据，此处仅为模拟
 * 加载相似问题**/
function loadRelatedQuestions(){
	/**分页参数**/
	var param={
			count:5,	// 每页显示行数
			page:1    // 当前页
	};
	var result=parent.FireFly.doAct("SY_COMM_ZHIDAO","getAskList",param,false,false);
	$("#js-related-questions-Template").tmpl(result._DATA_).appendTo("#js-related-questions");
}


/**
 * TODO 有待真实数据，此处仅为模拟
 * 加载待回答的问题**/
function loadWaitAnswerQuestions(){
	/**分页参数**/
	var param={
			count:5,	// 每页显示行数
			page:2    // 当前页
	};
	var result=parent.FireFly.doAct("SY_COMM_ZHIDAO","getAskList",param,false,false);
 
	$("#js-wait-answer-questions-Template").tmpl(result._DATA_).appendTo("#js-wait-answer-questions");
}

//点击关注或取消关注时调用的方法
function attention(){
	//没有的参数重新拿一下
	var USER_CODE = parent.System.getVar("@USER_CODE@");
	
	//控制问题的关注状态(通过当前用户和问题ID判断关注状态)
	var fllowDataWhere ={
				"_NOPAGE_":true,
				"_searchWhere":" and DATA_ID='"+Q_ID+"' and USER_CODE='"+USER_CODE+"'"
	};
	var fllowData = parent.FireFly.getListData("SY_COMM_ZHIDAO_QUESTION_FOLLOW",fllowDataWhere)._DATA_;
	if(fllowData.length){
		jQuery("#fllowID").val(fllowData[0]._PK_);
		jQuery("#questionFllow").addClass("zg-btn-white").text("取消关注");
	}
	
	
	jQuery("#questionFllow").unbind("click").bind("click",function(){
		if(jQuery(this).text() == "关注"){
			jQuery(this).addClass("zg-btn-white").text("取消关注");
			var fllowID = parent.FireFly.doAct("SY_COMM_ZHIDAO", "addQuestionFollow", {"DATA_ID":Q_ID});
			jQuery("#fllowID").val(fllowID._PK_);
			//window.location.reload();
			getAttentionPerson();
		}else{
			jQuery(this).removeClass("zg-btn-white").text("关注");
			parent.FireFly.doAct("SY_COMM_ZHIDAO", "deleteQuestionFollow", {"DATA_ID":Q_ID});
			//window.location.reload();
			getAttentionPerson();
		}
	});
}
	
//获取问题关注人和图片的方法
function getAttentionPerson(){
	//获取关注该问题的人
	var followList = parent.FireFly.doAct("SY_COMM_ZHIDAO_QUESTION_FOLLOW", "getFollowList", {"DATA_ID":Q_ID})._DATA_;
	jQuery("#fllowUsers").html("");
	for(var i=0; i<followList.length; i++){
		var userImgSrc = followList[i].USER_CODE__IMG;
		var userCode = followList[i].USER_CODE;
		var aTag = jQuery("<a title='" + followList[i].USER_CODE__NAME + "' class='zm-item-link-avatar myUseImg' href='#' userCode="+userCode+"></a>").appendTo(jQuery("#fllowUsers"));
		jQuery("<img src='" + userImgSrc + "' class='zm-item-img-avatar'>").appendTo(aTag);
	}
	if (followList.length) {
		jQuery("#fllowUsersSize").text(followList.length);
	}else{
		jQuery("#fllowUsersSize").text("暂时没有");
	}
	
	//给所有的图片绑定点击事件
	jQuery(".myUseImg").live("click",function(){
		var userCode = jQuery(this).attr("userCode");
		othersZhidao(tempId,userCode);
	});
}