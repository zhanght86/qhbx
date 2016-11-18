/**
 * 收藏夹
 * @author jason
 */
(function($){
	
	"use strict";  
	
	var MyFavorites=function(element,options){
		this.init('myFavorites',element,options);
	}
	MyFavorites.prototype={
			
		constructor:MyFavorites
		
	   ,init : function(type,element,options){
		  	this.type = type;
			this.$element = $(element);
		    this.options = this.getOptions(options);
			this.dialogEl=$(this.options.dialogEl);
			this.tmplEl=$(this.options.tmplEl);
			this.dataParam={
		    		"DATA_ID" 	 :	this.$element.attr("dataId"),
		    		"DATA_OWNER" :	this.$element.attr("owner"),
		    		"SERV_ID"    :  this.$element.attr("servId") 
			};
			this._bindEvent();
			
		}
	   	, getOptions : function (options) {
		  	return $.extend({}, $.fn[this.type].defaults, this.$element.data(), options);
		}
		, _bindEvent : function(){
			var $this=this;
			/**
			 * 弹出对话框
			 */
			this.$element.on("click",function(event){
				$this._render();
				$this.dialogEl.find("a.item").each(function(){
					$(this).one("click",function(event){
						event.preventDefault();
						var inputTags=$("#tags"),
						tags=inputTags.val();
						tags+=" "+$(this).html();
						tags=$.trim(tags)+" ";
						inputTags.val(tags);
					}); 
				});
		    });
			/**
			 * 输入框失去焦点验证是否为空
			 */
			this.dialogEl.on("blur","#title",function(){
				if(!$.trim($(this).val())){
					$(this).closest(".control-group").addClass("error");
				}else{
					$(this).closest(".control-group").removeClass("error");
				}
				return false;
			}); 
			/**
			 * 保存
			 */
			this.dialogEl.on("click",".js-dialog-save",function(event){
				event.preventDefault();
				var title = $this.dialogEl.find("#title");
				if($.trim(title.val())){
					$this.dataParam["DATA_DIS_NAME"]=title.val();
				}else{
					title.closest(".control-group").addClass("error");
					return false;
				}
				$this.dataParam["TAGS"]=$.trim($("#tags").val());
				console.debug($this.dataParam["TAGS"]);
				parent.FireFly.doAct("SY_COMM_FAVORITES","save",$this.dataParam,false,false,function(result){
					$this.dataParam["TAGS"]="";
					$this.dialogEl.addClass("success");
					//$this._closeDialog();
				});
			});
			
			/**
			 * 关闭
			 */
			this.dialogEl.on("click",".js-goto",function(event){
				event.preventDefault();
				var url,
					title,
					servId = $this.$element.attr("servId");
					
				if(servId =="SY_COMM_WENKU_DOCUMENT"){
					url="SC_WENKU_FAVORITES.list.do";
					title="文档收藏夹";
				}else if(servId =="SY_COMM_ZHIDAO_QUESTION"){
					url="SY_COMM_FAVORITES_MARK.list.do";
					title="知道问答收藏夹";
				}
				var opts = {"url":url,"tTitle":title,"params":{"SERV_ID":servId},"menuFlag":3};
			    Tab.open(opts); 
			});
			
			/**
			 * 关闭
			 */
			this.dialogEl.on("click",".js-dialog-close",function(event){
				event.preventDefault();
				$this._closeDialog();
			});
			 
			/**
			 * 按esc 退出
			 */
			$(document).on("keydown",function(event){
				if($this.options.closeOnEscape && event.which==27 ){
					event.preventDefault();
					$this._closeDialog();
					return false;
				}
			});
		}
		,_render : function(){
			//加载标签数据
			this._loadTagsData();
			this.dataParam.DATA_DIS_NAME=this.$element.attr("disname");
			var param={
					title:this.dataParam.DATA_DIS_NAME,//获取原始标题
					items:this.items
			};
			
			//显示对话框
			this.dialogEl.html(this.tmplEl.tmpl(param)).show();
			
		}
		/**close 弹出窗**/
		,_closeDialog : function(){
			this.dialogEl.hide(100,function(){
				if($(this).hasClass("success")){
					$(this).removeClass("success");	
				}
			});
		}
		/***加载标签数据***/
		,_loadTagsData : function(){
			var param={
					"S_USER":System.getVar("@USER_CODE@"),
					"MARK_LEVEL" :"PRIVATE"
			};
			
			var result=parent.FireFly.doAct("SY_COMM_MARK","query",param,false,false);
			this.items=result._DATA_;
			
		}
	};//end of prototype
    
	/*
	 *	myFavorites PLUGIN DEFINITION
  	 * ========================= 
	 */
	var old = $.fn.myFavorites ;
	
	$.fn.myFavorites=function(options){	
		return this.each(function() {
			var $this=$(this)
			 	,data = $this.data('myFavorites')
			    ,opts=$.extend({},$.fn.myFavorites.defaults,options);
			if(!data){
				$this.data('myFavorites',(data=new MyFavorites(this,opts)));
			}
		});
		
	};
	$.fn.myFavorites.Constructor = MyFavorites;
 	/*
	 * myFavorites NO CONFLICT
  	 * ====================
  	 */ 
    $.fn.myFavorites.noConflict = function () {
      $.fn.myFavorites = old ;
      return this ;
    }
	
	
	$.fn.myFavorites.defaults={
		closeOnEscape:true, 			// 按esc 退出
		dialogEl  	: ".favorite-dialog" ,
		tmplEl		: "#favorites-dialog-Template"
    };
	
	
    $(function () {
    	$('.js-favorite').myFavorites();
    })
})(window.jQuery);
