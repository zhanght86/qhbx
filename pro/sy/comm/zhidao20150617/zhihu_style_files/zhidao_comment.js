/**
 *  分页获取某新闻评论内容
 */
GLOBAL.namespace("rh.vi");

rh.vi.zhidao_comment = function(options) {
	var defaults={
		id:options.sId+"-rhCommentView",
		"SERV_ID":"SY_COMM_ZHIDAO",
		"NOWPAGE":0,
		"SHOWNUM":10,
		"DATA_ID":"",
		"COMMENT_SERV":"SY_SERV_COMMENT",
		"SAVE_METHOD":"reply",
		pCon:""
	}
	this.opts = jQuery.extend(defaults,options);
	this._id = this.opts.id;
	this._pCon = this.opts.pCon;
	this._data = 0;
	this.servId = this.opts.SERV_ID;
	this.dataId = this.opts.DATA_ID;
	this.nowPage = this.opts.NOWPAGE;
	this.showNum = this.opts.SHOWNUM;
	//评论服务
	this.COMMENT_SERV = this.opts.COMMENT_SERV;
	//评论服务保存方法名
	this.SAVE_METHOD = this.opts.SAVE_METHOD;
	this.pages = 0;
	this._replyId = "";
	
	//初始化当前数据页数
	if (this.nowPage > 0) {
		this.nowPage = 0;
	}
	this.loaded = false;
	
}

rh.vi.zhidao_comment.prototype.show = function() {
	var _self = this;
	_self.loaded = true;
	
	//获取数据
	this._loadComment();
}
/**
 * 创建Editor
 */
rh.vi.zhidao_comment.prototype.createEditor = function(id) {
	
	// 初始化editor
	var fileUrl = "/file/";
	var params = "?SERV_ID=" + this.SERV_ID + "&DATA_ID=" + this.DATA_ID + "&FILE_CAT=";

};
/** 
 * 加载评论数据
 */
rh.vi.zhidao_comment.prototype._loadComment = function() {
	var _self = this;
	//获取数据 
	var param = {};
	param["SERV_ID"] = this.servId;
	param["DATA_ID"] = this.dataId;
	param["NOWPAGE"] = this.nowPage + 1;
	param["SHOWNUM"] = this.showNum;
	param["_extWhere"] = " and C_STATUS=1";
	param["ORDER"] = "S_CTIME ASC";
	var data = top.FireFly.doAct("SY_SERV_COMMENT","getCommentList",param);
	
	var pageBean = data['_PAGE_'];
	this.nowPage = Number(pageBean.NOWPAGE);
	this.pages = Number(pageBean.PAGES);
	if (this.nowPage >= this.pages) {
		 _self.moreBtn.unbind().text("评论已全部加载!").attr("id","nomore");
	}else {
		_self.moreBtn.unbind().text("加载更多评论").bind("click", function() {
			 _self._loadComment();
	     });
	}
	
	jQuery.each(data['_DATA_'],function(index,comment){

		var item = jQuery("<div class='zm-item-comment'></div>").appendTo(_self.listContainer);
		//评论人
		var user = jQuery("<a title='" +comment.S_USER__NAME + "' class='zm-item-link-avatar' href='#'></a>").appendTo(item);
		var img = jQuery("<img class='zm-item-img-avatar'>").appendTo(user);
		img.attr("src", FireFly.getContextPath() + comment.S_USER__IMG);
		
		//评论头 用户信息
		var contentWarp = jQuery("<div class='zm-comment-content-wrap'></div>").appendTo(item);
		var header = jQuery("<div class='zm-comment-hd'>").appendTo(contentWarp);
		  jQuery("<a href='#' class='zg-link' title='" + comment.S_USER__NAME + "'></a>").text(comment.S_USER__NAME).appendTo(header);
		  if (comment.REPLY_TO && comment.REPLY_TO_USER) {
			  jQuery("<span class='desc'>回复 </span>").appendTo(header);
			 //显示回复目标信息 
				var replyToUser = comment.REPLY_TO_USER;
				jQuery("<a href='#' class='zg-link' title='" + replyToUser.USER_NAME + "'></a>").text(replyToUser.USER_NAME).appendTo(header);
		  }
		  
		//评论内容
		var content = jQuery("<div class='zm-comment-content'></div>").appendTo(contentWarp);  
		content.text(comment.C_CONTENT);
		 
		//评论尾 按钮区域
		var footer = jQuery("<div class='zm-comment-ft'></div>").appendTo(contentWarp);
		var time = jQuery("<span class='date'>2013-03-23</span></span>").appendTo(footer);
		var likeVoteCount = 0;
		if (comment.LIKE_VOTE) {
			likeVoteCount = comment.LIKE_VOTE;
		}
		
		
		//回复 按钮 
		var reply = jQuery("<a class='reply zm-comment-op-link' name='reply_comment'><i class='zg-icon zg-icon-comment-reply'></i>回复</a>").appendTo(footer);
		reply.bind("click", function() {
		 _self._replyId = comment.C_ID;
		 //编辑内容
		 var replyDiv = jQuery("<div class='zm-comment-form zm-comment-form-editing' style=''></div>").appendTo(contentWarp);
		 var wrap = jQuery("<div class='zm-comment-editable-wrap'></div>").appendTo(replyDiv);
		 var replyCont = jQuery("<div contenteditable='true' aria-label='写下你的回复' class='zm-comment-editable editable' g_editable='true'></div>").appendTo(wrap);
		 var replyBtn = jQuery("<div class='zm-command zg-clear'></div>").appendTo(replyDiv);
		 var replySubBtn = jQuery("<a class='zm-comment-submit zg-right zg-btn-blue' href='javascript:;'>评论</a>").appendTo(replyBtn);
		 var replyCanBtn = jQuery("<a class='zm-comment-close zm-command-cancel' href='javascript:;'>取消</a>").appendTo(replyBtn);
		 //提交回复
		 replySubBtn.bind("click", function() {
			 var replyText = "#回复x楼#" + replyCont.text();
			 _self.submit(comment.C_ID, replyText)
	      });			
		 
		 //取消回复
		 replyCanBtn.bind("click", function() {
			 replyDiv.remove();
	      });	
		 
      });
		
		//支持 按钮
		var likeVote = jQuery("<a class='like zm-comment-op-link' name='like_comment'><i class='zg-icon zg-icon-comment-like'></i>赞 </a> ").appendTo(footer);
		likeVote.bind("click", function() {
			var data = {};
			data["_PK_"] = comment.C_ID;
			var resultData = parent.FireFly.doAct("SY_SERV_COMMENT", "increaseLikevote", data);
			if (resultData["_MSG_"] && resultData["_MSG_"].indexOf("OK") == 0) {
				var afterVote = Number(likeVoteCount) + 1;
				likeVote.unbind();
				likeVote.text("已赞");
				Tip.show("操作成功!");
			} else {
				Tip.showError(resultData["_MSG_"], true);
			}
		});
		
		//是否当前用户
		if(parent && comment.S_USER==parent.System.getVar("@USER_CODE@")){
		//删除按钮
		var delBtn = jQuery("<a name='delcomment' class='del zm-comment-op-link' ><i class='zg-icon zg-icon-comment-del'></i>删除</a>").appendTo(footer);
		delBtn.bind("click", function() {
			if (confirm("确认删除？")) {
				var data = {};
 				data["_PK_"] =  comment.C_ID;
				var resultData = parent.FireFly.doAct("SY_SERV_COMMENT", "delete", data);
				if (resultData[UIConst.RTN_MSG] && resultData[UIConst.RTN_MSG].indexOf(UIConst.RTN_OK) == 0) {
			  		Tip.show("删除成功!");
			  		//刷新页面
			  		_self.refresh();
				} else {
					Tip.showError(resultData[UIConst.RTN_MSG], true);
				}
			}
			return false;
	    });
		}
		
		
		
		//支持人数
		if (0 < likeVoteCount) {
		 jQuery("<span class='like-num'> <em>" + likeVoteCount + "</em><span>赞</span></span>").appendTo(footer);
		}
		

	});	 
	
	//当前用户
	var user = parent.System.getVar("@USER_CODE@");
	var user_img = parent.System.getVar("@USER_IMG@");
	jQuery("#currentUser").attr("src", user_img);
};



/**
 * submit
 */
rh.vi.zhidao_comment.prototype.submit = function(replyId, content) {
	var _self = this;
	
	if (content.length == 0) {
		alert("请输入内容!");
		return;
	}
	var data = {};
	data["SERV_ID"] = _self.servId;
	data["DATA_ID"] = _self.dataId;
	data["C_CONTENT"] = content;
	data["REPLY_TO"] = replyId;
	data["S_CTIME"] = rhDate.getTime();
	var resultData = top.FireFly.doAct(_self.COMMENT_SERV, _self.SAVE_METHOD, data);
	if (resultData["_MSG_"] && resultData["_MSG_"].indexOf("OK") == 0) {
  		top.Tip.show("保存成功!");
  		//刷新页面
  		_self.refresh();
  		if(jQuery("#answer-"+_self.dataId).length == 0){
  			console.debug(resultData);
  			jQuery("#ask-"+_self.dataId).html("").html("<i class='z-icon-comment'></i>"+resultData['Q_COMMENT_COUNTER']+"条评论");
  		}else{
  			jQuery("#answer-"+_self.dataId).html("").html("<i class='z-icon-comment'></i>"+resultData['A_COMMENT_COUNTER']+"条评论");
  		}
	} else {
		Tip.showError(resultData["_MSG_"], true);
	}
};

/*
 * 刷新
 */
rh.vi.zhidao_comment.prototype.refresh = function() {
	var _self = this;
	_self.reset();
	_self.show();
};


/**
 * 重置
 */
rh.vi.zhidao_comment.prototype.reset = function() {
	var _self = this;
	_self.listContainer.empty();
	_self._replyId = "";
	_self.nowPage = 0;
};


/*
 * 构建列表页面布局
 */
rh.vi.zhidao_comment.prototype.layout = function() {
	var _self = this;
	//评论框
	var form = jQuery("<div class='zm-comment-form zm-comment-box-ft zm-comment-form-expanded'></div>").appendTo(_self._pCon);
	var content = jQuery("<div aria-label='写下你的评论…' class='zm-comment-editable editable' contenteditable='true'></div>").appendTo(form);
	var btns = jQuery("<div class='zm-command zg-clear'></div>").appendTo(form);
	var addBtn = jQuery("<a class='zg-right zg-btn-blue'></a>").text("评论").appendTo(btns);
	addBtn.bind("click", function() {
		  var commentText = content.text();
		 _self.submit("", commentText)
		 //清空内容框
		 content.text("");
      });		
	
	var cancelBtn = jQuery("<a name='closeform' class='zm-command-cancel'></a>").text("取消").appendTo(btns);
	jQuery("<div class='zm-comment-info'></div>").appendTo(form);
	cancelBtn.bind("click", function() {
		content.text("");
	  });		
	
	
	jQuery("<i class='icon icon-spike zm-comment-bubble'></i>").appendTo(_self._pCon);
	var commentList = jQuery("<div class='zm-comment-list'></div>").appendTo(_self._pCon);
	//列表页面
	_self.listContainer = commentList;
	
	
	//更多评论加载
	var moreBtn = jQuery("<a name='load-more' class='load-more' href='javascript:;'></a>").appendTo(_self._pCon);
	jQuery("<span class='text'>加载更多评论<img class='spinner' src='/sy/comm/zhidao/zhihu_style_files/spinner2_002.gif'></span>").appendTo(moreBtn);
	 moreBtn.bind("click", function() {
		 _self._loadComment();
     });
    _self.moreBtn = moreBtn;

//	 this.createEditor("comment_content");
//	 this.editor = UE.getEditor("comment_content"); 
};

/**
 * 评论面板是否已渲染
 */
rh.vi.zhidao_comment.prototype.isShow = function() {
	var _self = this;
	return _self.loaded ;
}

/**
 * 清空布局
 */
rh.vi.zhidao_comment.prototype.remove = function() {
	var _self = this;
	//列表页面
	_self.listContainer.remove();
	_self._pCon.empty();
	_self.reset();
	_self.loaded = false;
};



//对引用内容进行解析
rh.vi.zhidao_comment.prototype._get_quot = function(str) {
	if(!str){
		return {
			"quote" : ""
		};
	}
	var start1 = str.indexOf("[");
	var start2 = str.indexOf("]");
	var end = str.lastIndexOf("[/quote]");

	if (start1<0 || start2<0 || end<0 ) {
		return {
			"quote" : str
		};
	}

	else {
		return {
			"start_content" : getObject(str.substring(start1 + 1, start2)),
			"end_content" : str.substring(end + 8),
			"str" : str.substring(start2 + 1, end)
		}
	}
	
	function getObject(content) {
		var returnObject = {};
		var jsons = content.split(",");
		for(var i=0; i<jsons.length; i++){
			var json = jsons[i].split("=");
			returnObject[json[0]] = json[1];
		}
		return returnObject;
	}
};

//对主题进行修改
rh.vi.zhidao_comment.prototype.modifyTopic = function(obj){
	var _self = this;
	obj.unbind("click").click(function(event){
		
		if (_self.modifyEditor) {
			_self.modifyEditor.destroy();
			_self.modifyEditor = null;
		}
		
		var modifyArea = "modify_${topic.TOPIC_ID}";
		//修改意见弹出框
		var dialog = jQuery("<div><textarea id='" + modifyArea + "'></textarea></div></div>").addClass("dictDialog").attr("title","修改主题");
		dialog.appendTo(jQuery("body"));
		
		var hei = 300;
		var wid = 900;
		
		var scroll = Window.getScroll(parent.window);
	    var viewport = Window.getViewPort(parent.window);
	    var top = scroll.top + viewport.height / 2 - hei / 2 - 88;
		
		var posArray = ["", top];
	  
		dialog.dialog({
			autoOpen: true,
			height: hei,
			width: wid,
			show: "bounce", 
	        hide: "puff",
			modal: true,
			resizable: false,
			position: posArray,
			buttons: {
				"保存": function() {
			    	var data = {};
			    	data["_PK_"] = _self.dataId;
			    	data["TOPIC_BODY"] = _self.modifyEditor.getContent();
			    	var resultData = parent.FireFly.doAct("SY_COMM_BBS_TOPIC", "save", data);
			    	if (resultData[UIConst.RTN_MSG] && resultData[UIConst.RTN_MSG].indexOf(UIConst.RTN_OK) == 0) {
			      		Tip.show("保存成功!");
			      		_self.modifyEditor.destroy();
			      		_self.modifyEditor = null;
			      		dialog.remove();
			      		//刷新页面
			      		window.location.reload();
			    	} else {
			    		Tip.showError(resultData[UIConst.RTN_MSG], true);
			    	}
				},
				"关闭": function() {
					_self.modifyEditor.destroy();
		      		_self.modifyEditor = null;
					dialog.remove();
				}
			}
		});
		// 注释掉头部关闭按钮
		dialog.parent().find(".ui-dialog-titlebar-close").hide();
		var btns = jQuery(".ui-dialog-buttonpane button",dialog.parent()).attr("onfocus","this.blur()");
		btns.first().addClass("rh-small-dialog-ok");
		btns.last().addClass("rh-small-dialog-close");
		dialog.parent().addClass("rh-small-dialog").addClass("rh-bottom-right-radius");
	    jQuery(".ui-dialog-titlebar").last().css("display","block");//设置标题显示

	//	_self.createEditor(modifyArea);
	//	_self.modifyEditor = UE.getEditor(modifyArea);
		
		// 获取主题内容
		var modifyContent = "";
		var data = {
			"_PK_":_self.dataId,
			"TOPIC_ID":_self.dataId,
			"_SELECT_":"TOPIC_BODY"
		};
		
		var resultData = parent.FireFly.doAct("SY_COMM_BBS_TOPIC", "byid", data);
		if(resultData.TOPIC_BODY){
			modifyContent = resultData.TOPIC_BODY;
		}
		_self.modifyEditor.setContent(modifyContent);
		_self.modifyEditor.focus(true);
		
		return false;
	});
};
