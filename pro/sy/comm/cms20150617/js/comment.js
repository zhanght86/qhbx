/**
 *  分页获取某新闻评论内容
 */
GLOBAL.namespace("rh.vi");

rh.vi.comment = function(options) {
	var defaults={
		id:options.sId+"-rhCommentView",
		sId:"SY_SERV_COMMENT",
		aId:"getCommentList",
		"SERV_ID":"",
		"NOWPAGE":0,
		"SHOWNUM":10,
		"DATA_ID":"",
		"cardObj":null,
		pCon:"",
		width:null
	}
	this.opts = jQuery.extend(defaults,options);
	this._id = this.opts.id;
	this._pCon = this.opts.pCon;
	this._data = 0;
	this.servId = this.opts.SERV_ID;
	this.dataId = this.opts.DATA_ID;
	this.nowPage = this.opts.NOWPAGE;
	this.showNum = this.opts.SHOWNUM;
	this.cardObj = this.opts.cardObj;
	
	// 布局宽度设置，可没有
	this.width = this.opts.width;
	
	this.pages = 0;
	//回复评论ID
	this._replyId = "";
	
	//初始化当前数据页数
	if (this.nowPage > 0) {
		this.nowPage = 0;
	}
	
	//布局初始化
	this._layout();
}

rh.vi.comment.prototype.show = function() {
	var _self = this;
	
	//获取数据
	this._loadComment();	
}

/** 
 * 加载评论数据
 */
rh.vi.comment.prototype._loadComment = function() {
	var _self = this;
	//获取数据
	var param = {};
	param["SERV_ID"] = this.servId;
	param["DATA_ID"] = this.dataId;
	param["NOWPAGE"] = this.nowPage + 1;
	param["SHOWNUM"] = this.showNum;
	param["_extWhere"] = " and C_STATUS=1";
	param["ORDER"] = "S_CTIME DESC";

	var data = top.FireFly.doAct("SY_SERV_COMMENT","getCommentList",param);
	
	var pageBean = data['_PAGE_'];
	this.nowPage = Number(pageBean.NOWPAGE);
	this.pages = Number(pageBean.PAGES);
	if (this.nowPage >= this.pages) {
		jQuery("#more").unbind().text("已加载全部评论.").removeClass().parent().removeClass().addClass("tc pt10 pb10");
	}
	
	var items = jQuery('<div></div>').addClass("reply_items").appendTo(_self.listContainer);
	
	jQuery.each(data['_DATA_'],function(index,comment){
		//显示顺序：评论人、时间   支持、反对、回复
		var item = jQuery('<div></div>').addClass("reply").appendTo(items);
		var div1 = jQuery('<div></div>').addClass("pull-left face").appendTo(item);
		//用户头像
		var userIcon = jQuery('<img width="50" height="50">')
		.attr("src",parent.FireFly.getContextPath()+comment.S_USER__IMG)
		.attr("alt",comment.S_USER__NAME)
		.bind("mouseover", function(event){
			 new rh.vi.userInfo(event, comment.S_USER);
		}).addClass("rh-user-info-circular-bead").appendTo(div1);
		
		var div2 = jQuery('<div></div>').addClass("infos").appendTo(item);
		var infoDiv = jQuery('<div></div>').addClass("infoDiv").appendTo(div2);
		
		//评论人
		jQuery('<span></span>').addClass("name").text(comment.S_USER__NAME).appendTo(infoDiv);
		
		//N楼
		//var replyIndex = (_self.nowPage -1) *  _self.showNum  + (index + 1) ;
		var replyIndex =comment.C_NUMBER;
		jQuery('<span></span>').addClass("opts").text(replyIndex + "楼").appendTo(infoDiv);
		
		//评论时间
		var timeago = "";
		if (comment.S_CTIME) {
			timeago = comment.S_CTIME;
			timeago = timeago.substring(0, 19);
			timeago = jQuery.timeago(timeago); 
		}
		
		var time = jQuery('<abbr title="'+comment.S_CTIME+'" style="color:#F26C4F;"></abbr>').addClass("time").text(timeago).appendTo(infoDiv);
		time.attr("title", comment.S_CTIME);
		
		var bottom = jQuery('<div></div>').addClass("bottom");
		//支持
		var likeVoteCount = 0;
		if (comment.LIKE_VOTE) {
			likeVoteCount = comment.LIKE_VOTE;
		}
		var likeVote = jQuery("<span><i class='like' title='支持'>&nbsp;&nbsp;</i><a href='javascript:;'> "+likeVoteCount+" </a></span>").addClass("zhichi").appendTo(bottom);
		likeVote.bind("click", function() {
			var data = {};
			data["_PK_"] = comment.C_ID;
			var resultData = parent.FireFly.doAct("SY_SERV_COMMENT", "increaseLikevote", data);
			if (resultData[UIConst.RTN_MSG] && resultData[UIConst.RTN_MSG].indexOf(UIConst.RTN_OK) == 0) {
				var afterVote = Number(likeVoteCount) + 1;
				likeVote.text(afterVote + "人支持");
				likeVote.unbind();
		  		Tip.show("操作成功!");
			} else {
				Tip.showError(resultData[UIConst.RTN_MSG], true);
			}
			return false;
	    });
		
		jQuery("<span> </span>").appendTo(infoDiv);
		
		//反对
		var dislikeVoteCount = 0;
		if (comment.DISLIKE_VOTE) {
			dislikeVoteCount = comment.DISLIKE_VOTE;
		}
		var dislikeVote = jQuery("<span><i class='unlike' title='反对'>&nbsp;&nbsp;</i><a href='javascript:;'> "+dislikeVoteCount+" </a></span>").addClass("fandui").appendTo(bottom);
		dislikeVote.bind("click", function() {
			var data = {};
			data["_PK_"] = comment.C_ID;
			var resultData = parent.FireFly.doAct("SY_SERV_COMMENT", "increaseDislikevote", data);
			if (resultData[UIConst.RTN_MSG] && resultData[UIConst.RTN_MSG].indexOf(UIConst.RTN_OK) == 0) {
				var afterVote = Number(dislikeVoteCount) + 1;
				dislikeVote.text(afterVote + "人反对");
				dislikeVote.unbind();
		  		Tip.show("操作成功!");
			} else {
				Tip.showError(resultData[UIConst.RTN_MSG], true);
			}
			return false;
	    });				
		
		var top = jQuery('<div></div>').addClass("top");
		
		//是否当前用户
		if(comment.S_USER==parent.System.getVar("@USER_CODE@")){
			top.appendTo(infoDiv);
		}	
		
		//修改
		var modifyBtn = jQuery("<span><i class='icon'>&nbsp;&nbsp;</i><a href='javascript:;'>修改</a></span>").addClass("modify").appendTo(top);
		modifyBtn.bind("click", function(event) {
			
			if (_self.modifyEditor) {
				_self.modifyEditor.destroy();
				_self.modifyEditor = null;
			}
			
			var wid = 600;
			var hei = 300;
			
			var modifyArea = "modify_" + comment.C_ID;
			//修改意见弹出框
			var dialog = jQuery("<div><textarea id='" + modifyArea + "'></textarea></div>").addClass("dictDialog").attr("title","修改评论");
			dialog.appendTo(jQuery("body"));
			
			var scroll = RHWindow.getScroll(parent.window);
		    var viewport = RHWindow.getViewPort(parent.window);
		    var top = scroll.top + viewport.height / 2 - hei / 2 - 88;
			
		    var posArray = [];
		    
		    posArray[0] = "";
		    posArray[1] = top;

//		    if (event) {
//			    var cy = event.clientY;
//			    posArray[0] = "";
//			    posArray[1] = cy-280;
//		    }
		    
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
				    	data["_PK_"] = comment.C_ID;
				    	data["C_CONTENT"] = _self.modifyEditor.getContent();
				    	var resultData = parent.FireFly.doAct("SY_SERV_COMMENT", "updateReply", data);
				    	if (resultData[UIConst.RTN_MSG] && resultData[UIConst.RTN_MSG].indexOf(UIConst.RTN_OK) == 0) {
				      		Tip.show("保存成功!");
				      		_self.modifyEditor.destroy();
				      		_self.modifyEditor = null;
				      		dialog.remove();
				      		//刷新页面
				      		_self.refresh();
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
		
			_self.createEditor(modifyArea);
			_self.modifyEditor = UE.getEditor(modifyArea);
			
			// 获取引用数据
			var modifyContent = "";
			if (comment.REPLY_TO) {
				var data = {};
				data["C_ID"] =  comment.C_ID;
				data["_PK_"] =  comment.C_ID;
				var replyComment = parent.FireFly.doAct("SY_SERV_COMMENT", "byid", data);
				var replyNumber = replyComment.C_NUMBER;
				modifyContent = "#回复 " + replyNumber +"楼# ";
			}
			
			// 截取原始评论内容
			var content = comment.C_CONTENT;
			var subIndex = content.lastIndexOf("[/quote]");
			if (0 < subIndex) {
				content = content.substring(subIndex + "[/quote]".length);
			}
			
			modifyContent += content;
			_self.modifyEditor.setContent(modifyContent);
			_self.modifyEditor.focus(true);

			return false;
	    });
		
		//删除
		var deleteBtn = jQuery("<span><i class='icon'>&nbsp;&nbsp;</i><a href='javascript:;'>删除</a></span>").addClass("delete").appendTo(top);
		deleteBtn.bind("click", function() {
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
		
		
		
		jQuery("<span></span>").appendTo(infoDiv);
		//回复
		var reply = jQuery("<span><a href='javascript:;'>回复</a></span>").addClass("huifu").appendTo(bottom);
		reply.bind("click", function() {
			 _self.editor.setContent("#回复" + replyIndex + "楼#");
			 _self.editor.focus(true);
			 _self._replyId = comment.C_ID;
	    });
	 	
		//评论内容
	 	var bodyDiv = jQuery('<div></div>').addClass("comment").appendTo(div2);
		var current = bodyDiv;
		function do_quote(str) {
			var result = _self._get_quot(str);
			if (result.quote) {
				current = jQuery("<div class='quote_content'></div>").html(result.quote).appendTo(current);
			} else {
				current = jQuery("<div class='quote'></div>").appendTo(current);
				var start = result.start_content;
				start["time"] = start["time"].substring(0, 19);
				var quote = start["quote"];
				var time = jQuery.timeago(start["time"]);
				var user = start["user"] || "";
				var username = start["username"] || user;
			
				current.append(
						jQuery("<div></div>").html(quote+" "+username+" 在 <span title='"+start['time']+"' style='color:#F26C4F;'>"+time+"</span> 说：")
				)
				
				current.parent().append(
					jQuery("<div class='end'></div>").html(result.end_content)
				)
				do_quote(result.str);
			}
		}
		do_quote(comment.C_QUOTE_CONTENT+comment.C_CONTENT);
		bodyDiv.append(bottom);
	});
	
	if (_self.cardObj) {
		//调整评论框大小
		_self.cardObj.resetSize();
	}

};



/**
 * submit
 */
rh.vi.comment.prototype.submit = function() {
	var _self = this;
	
	var content =  this.editor.getContent();
	if (content.length == 0) {
		alert("请输入评论内容!");
		return;
	}
	var data = {};
	data["SERV_ID"] = _self.servId;
	data["DATA_ID"] = _self.dataId;
	data["C_CONTENT"] = content;
	data["REPLY_TO"] = _self._replyId;
	var resultData = parent.FireFly.doAct("SY_SERV_COMMENT", "reply", data);
	if (resultData[UIConst.RTN_MSG] && resultData[UIConst.RTN_MSG].indexOf(UIConst.RTN_OK) == 0) {
  		Tip.show("保存成功!");
  		//刷新页面
  		_self.refresh();
  		_self.editor.setContent("");
	} else {
		Tip.showError(resultData[UIConst.RTN_MSG], true);
	}
	
};

/*
 * 刷新
 */
rh.vi.comment.prototype.refresh = function() {
	var _self = this;
	_self.listContainer.empty();
	_self._replyId = "";
	_self.nowPage = 0;
	_self.show();
};
/**
 * 创建Editor
 */
rh.vi.comment.prototype.createEditor = function(id) {
	var _self = this;
	// 初始化editor
	var fileUrl = "/file/";
	var params = "?SERV_ID=" + this.SERV_ID + "&DATA_ID=" + this.DATA_ID + "&FILE_CAT=";

	// 简单模式
	toolbars = [
                ['undo', 'redo', 'bold', 'italic', 'underline', 'strikethrough', 'link', 'unlink', 'insertimage', 'emotion', '|','attachment', 'map']
          ];
	
	var config = {
		// 比容器宽两像素
		initialFrameWidth:"100%"			// 初始化编辑器宽度
		,initialFrameHeight:191 			// 初始化编辑器高度
		,minFrameHeight:0					// 最小高度
		,autoHeightEnabled:false			// 关闭默认长高，使用滚动条
		,zIndex:1050						// 编辑器层级的基数
		,imageUrl:RHFile.uploadUrl.imageUrl + params + "IMAGE_CAT"            			// 图片上传提交地址
	    ,scrawlUrl:RHFile.uploadUrl.scrawlUrl + params + "SCRAWL_IMG_CAT"           	// 涂鸦上传地址
	    ,fileUrl:RHFile.uploadUrl.fileUrl + params +  "ATTACHMENT_CAT"            		// 附件上传提交地址
	    ,catcherUrl:RHFile.uploadUrl.catcherUrl + params + "REMOTE_IMG_CAT"   			// 处理远程图片抓取的地址
	    ,imageManagerUrl:RHFile.uploadUrl.imageManagerUrl + params						// 图片在线管理的处理地址
	    ,snapscreenHost: '127.0.0.1'                    						// 屏幕截图的server端文件所在的网站地址或者ip，请不要加http://
	    ,snapscreenServerUrl:RHFile.uploadUrl.snapscreenServerUrl + "SNAP_IMG_CAT" 	// 屏幕截图的server端保存程序，UEditor的范例代码为“URL +"server/upload/jsp/snapImgUp.jsp"”
	    ,wordImageUrl:RHFile.uploadUrl.wordImageUrl + params + "WORD_IMG_CAT"         	// word转存提交地址
	    ,getMovieUrl:RHFile.uploadUrl.getMovieUrl + params + "MOVIE_CAT"             	// 视频数据获取地址
	    ,imagePath:fileUrl                // 图片修正地址，引用了fixedImagePath,如有特殊需求，可自行配置
	    ,scrawlPath:fileUrl               // 图片修正地址，同imagePath
	    ,filePath:fileUrl                 // 附件修正地址，同imagePath
	    ,catcherPath:fileUrl              // 图片修正地址，同imagePath
	    ,imageManagerPath:fileUrl         // 图片修正地址，同imagePath
	    ,snapscreenPath:fileUrl			  // 图片修正地址，同imagePath
	    ,wordImagePath:fileUrl            // 图片修正地址，同imagePath
	    ,toolbars:toolbars
	    ,initialContent:''
	    ,maximumWords:2000 
	};
	
	UE.getEditor(id, config).ready(function(){
		// 渲染完了之后重置页面高度
		if (_self.cardObj) {
			_self.cardObj.resetSize();
		}
	});
};
/*
 * 销毁
 */
rh.vi.comment.prototype.destroy = function() {
	if (this.editor) {
		this.editor.destroy();
		this.editor = null;
	}
};
/*
 * 构建列表页面布局
 */
rh.vi.comment.prototype._layout = function() {
	var _self = this;
	
	//添加我的评论
	_self.myComment = jQuery("<div></div>").addClass("myComment");
	
	
//	var aclDiv = jQuery("<div style='width:50%;max-width:700px;' class='inner' id=''></div>");
//	var left = jQuery("<span style='width:30%;' class='left'><div class='ui-label-default' id='SY_COMM_INFOS_BASE-NEWS_SCOPE_label'><div class='container'><span style='cursor:pointer;' class='name'>公开范围</span><span class='star'></span></div></div></span>").appendTo(aclDiv)
//	var right = jQuery("<span style='width:70%;' class='right'></span>").appendTo(aclDiv);
//	var selectWarp = jQuery("<div style='padding-left:0;' class='blank fl wp ' tip=''></div>").appendTo(right);
//	var select = jQuery("<select class='ui-select-default' name='SY_COMM_INFOS_BASE-NEWS_SCOPE' id='SY_COMM_INFOS_BASE-NEWS_SCOPE'></select>").appendTo(selectWarp);
//	select.append("<option value='0'>全部</option>");
//	select.append("<option value='1'>仅自己</option>");
//	select.append("<option value='2'>指定人员</option>");
	
	var select= new rh.ui.Select({
			id : "",
			name : "",
			_default : "",
			data : [0,1,2],
			width : "50%",
			isNotNull : false,
			isReadOnly : false,
			style : "",
			regular : "",
			hint : "",
			isHidden : false,
			tip : "",
			cardObj : _self.cardObj
		});
	
	
	// 创建editor对象
	jQuery("<textarea id='comment_content'></textarea>").appendTo(_self.myComment);
	
	commitBtn = jQuery("<a><span class='rh-icon-inner'> + 评论</span></a>").addClass("rh-icon rhGrid-btnBar-a").css({"margin-top":"1px"}).appendTo(_self.myComment);
 	commitBtn.bind("click", function() {
 		_self.submit();
    });
	
	//评论列表
	_self.listContainer = jQuery("<div></div>").addClass("listComment");
	
	
	//更多评论加载
	var moreBtn = jQuery("<div class='moreComment'><span class='rh-icon-inner' id='more'>更多评论</span></div>").addClass("rh-icon rhGrid-btnBar-a");
	 moreBtn.bind("click", function() {
		 _self._loadComment();
     });
	
	if (this.width) {
		var $strArr = [];
		$strArr.push("<div class='inner' style='width:");
		$strArr.push(this.width.ITEM_WIDTH);
		$strArr.push("%;max-width:");
		$strArr.push(this.width.MAX_WIDTH);
		$strArr.push("px;'>");
		$strArr.push("<span class='left' style='width:");
		$strArr.push(this.width.LEFT_WIDTH);
		$strArr.push("%;'><div class='ui-label-default'><div class='container'></div></div></span>");
		$strArr.push("<span class='right' style='width:");
		$strArr.push(this.width.RIGHT_WIDTH);
		$strArr.push("%;'></span>");
		$strArr.push("</div>");
		container = jQuery($strArr.join("")).appendTo(this._pCon);
		container.find(".right").first().append(_self.myComment).append(_self.listContainer).append(moreBtn);
	} else {
		this._pCon.append(_self.myComment).append(_self.listContainer).append(moreBtn);
	}
	_self.myComment.parent().parent().before(select.getContainer());
	this.createEditor("comment_content");
	this.editor = UE.getEditor("comment_content");
};

//对引用内容进行解析
rh.vi.comment.prototype._get_quot = function(str) {
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
