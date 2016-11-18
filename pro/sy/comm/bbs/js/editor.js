GLOBAL.namespace("rh.vi");

rh.vi.editor = function(options) {
	var defaults={
		id:options.sId+"-rhEditorView",
		"SERV_ID":"",
		pCon:""
	}
	this.opts = jQuery.extend(defaults,options);
	
	this._pCon = this.opts.pCon;
	this.servId = this.opts.SERV_ID;
	
	// 布局宽度设置，可没有
	this.width = this.opts.width;
	
	//布局初始化
	this._layout();
}

rh.vi.editor.prototype.show = function() {
	
}

/**
 * submit
 */
rh.vi.editor.prototype.submit = function() {
	
};

/**
 * 创建Editor
 */
rh.vi.editor.prototype.createEditor = function(id) {
	var _self = this;
	// 初始化editor
	var fileUrl = "/file/";
	var params = "?SERV_ID=" + this.SERV_ID + "&FILE_CAT=";

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
		
	});
};
/*
 * 销毁
 */
rh.vi.editor.prototype.destroy = function() {
	if (this.editor) {
		this.editor.destroy();
		this.editor = null;
	}
};
/*
 * 构建页面布局
 */
rh.vi.editor.prototype._layout = function() {
	var _self = this;
	
	//添加我的发布框
	_self.myBox = jQuery("<div></div>").addClass("box");
	
	// 创建editor对象
	jQuery("<textarea id='content'></textarea>").appendTo(_self.myBox);
	
	commitBtn = jQuery("<a><span class='rh-icon-inner' id='submit'>发表</span></a>").addClass("rh-icon rhGrid-btnBar-a").css({"margin-top":"1px"}).appendTo(_self.myBox);
 	commitBtn.bind("click", function() {
 		_self.submit();
    });
	
	
	this._pCon.append(_self.myBox);
	
	this.createEditor("content");
	this.editor = UE.getEditor("content");
};

