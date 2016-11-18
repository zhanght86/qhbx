<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>上传页面</title>
<#include "global.ftl"/>
<#include "/SY_COMM_WENKU/config_constant.ftl">
<link href="/sy/comm/wenku/css/upload/framework.css" type="text/css" rel="stylesheet">
<link href="/sy/comm/wenku/css/upload/pkg_upload.css" type="text/css" rel="stylesheet">
<link href="/sy/comm/wenku/css/upload/module_upload.css" type="text/css" rel="stylesheet">
<script type="text/javascript" src="/sy/comm/cms/js/wenku.js"></script>
<style type="text/css">
	.pageBody .ui-widget-content{
		background:white;
	}
	#upload-flash-container{
		height: 44px;
	    left: 209px;
	    position: absolute;
	    top: 110px;
	    width: 207px;
	    z-index: 1;
		background:url("/sy/comm/wenku/img/upload/upload_1.jpg");
	}
	#upload-flash-container .uploadBtn {
		margin-left:-5px;
	}
	
	#upload-flash-container2{
		line-height:26px;
		font-weight:bolder;
		border:1px #ccc solid;
		height: 26px;
	    left: auto;
	    right: 143px;
	    top: 20px;
	    width: 101px;
		background: url("/sy/comm/wenku/img/upload/button_e2a4b904.png") no-repeat scroll right -66px #39966E;
		cursor:pointer;
	}
	#upload-flash-container2:hover{
		background: url("/sy/comm/wenku/img/upload/button_e2a4b904.png") no-repeat scroll right -93px #32835C;
	}
	.rh-icon-inner{
		background:none;
	}
	.rh-icon,.rh-icon-disable{
		background:none;
	}
	.rh-icon:hover,.rh-icon:active {
	    background: none;
	}
	.rh-icon:hover .rh-icon-inner,.rh-icon:hover .rh-icon-inner:active {
	    background: none;
	}
	.modifyBtn .rh-icon-inner{
		display:none;
	}
</style>
<script type="text/javascript">
	//根据文件名取得文件后缀
	function getSuffix(str){
		if(!str){
			return "";
		}
		if(str.lastIndexOf(".")!=-1){
			str = str.substring(str.lastIndexOf(".")+1);
		}
		return str;
	}
	
	//根据文件名取得文件标题
	function getTitle(str){
		if(!str){
			return "";
		}
		if(str.lastIndexOf(".")!=-1){
			str = str.substring(0,str.lastIndexOf("."));
		}
		return str;
	}
	
	//初始上传：第一次上传
	function initUpload(){
		//文件上传
		var config = {
			"SERV_ID":"SY_COMM_WENKU_DOCUMENT", 
			"FILE_CAT":"SHARE_FILE", "TYPES":"*.pdf;*.doc;*.docx;*.wps;*.xls;*.xlsx;*.ppt;*.pptx;*.txt;*.flv;*.mp3;*.swf;",
			"flash_url":"/sy/base/frame/coms/file/swfupload.swf",
			"button_image_url":"/sy/comm/wenku/img/upload/upload_1.jpg",
			"upload_url":"/file/",
			"QUEUESIZE":10,
			"FILESIZE":"1024MB",
			"WIDTH":211,
			"HEIGHT":45,
			"TEXT":" "
		};
		var rhfile = new rh.ui.File({"config":config});
		rhfile.showNoneFile = function(){};
		jQuery("#upload-flash-container").append(rhfile.obj); 
		
		
		//--------------------在上传之前判断文件重复--开始--------------------------//
		rhfile.beforeUploadCallback = function(file) {
			//只是一个文件上传的情况
			var param = {};
			var fileName = file["name"];
			param["fileName"] = fileName;
			var result = FireFly.doAct("SY_COMM_WENKU_DOCUMENT", "doubleUpload", param, true);
			if(result["repeat"]=='true'){
				if (window.confirm("文库中已存在「" + fileName +"」，是否要继续上传？")) {
			    	return true;
			    }else{
			    	alert("请重新选择...");
			    	return false ;
			    }
			}else{
				return true;
			}
		};
		//--------------------在上传之前判断文件重复--结束--------------------------//
		
		rhfile.initUpload();
		rhfile.fillData([]);
		
		//callback
		rhfile.fillData = function(val) {
			for ( var i = 0; i < val.length; i++) {
				var file = val[i];
				var fileId = file["FILE_ID"];
				var fileName = file["FILE_NAME"];
				var suffix = getSuffix(fileName);
				var defaultTitle = getTitle(fileName);
				if (jQuery("input[name='DOCUMENT_FILE']").val().length == 0) {
					//上传成功，页面重新渲染
					jQuery("input[name='DOCUMENT_FILE']").val(fileId);
					jQuery("#upload-item-0 .item-file-title .ic").removeClass().addClass("ic ic-"+suffix);
					jQuery("#upload-item-0 .item-hd .item-file-title").append(jQuery("<span></span>").text(defaultTitle));
					jQuery(".content input.item-title-input").val(defaultTitle);
					jQuery("input[name='DOCUMENT_FILE_SUFFIX']").val(suffix);
					
					batchEdit();
				} else {
					//上传成功，添加新的文件信息框
					var newBox = jQuery("#upload-item-0").clone().appendTo(jQuery("#upload-list"));
					newBox.find("input[name='DOCUMENT_FILE']").val(fileId);
					newBox.find(".item-hd .item-file-title span").text(defaultTitle);
					newBox.find(".content input.item-title-input").val(defaultTitle);
					newBox.find("input[name='DOCUMENT_FILE_SUFFIX']").val(suffix);
					newBox.find(".item-file-title .ic").removeClass().addClass("ic ic-"+suffix);
					
					var index = jQuery(".upload-item").length - 1;
					newBox.attr("id","upload-item-"+index);
					
					batchEdit();
					open_close();
					doDelete();
					ChannelTree();
					doSubmit();
				}
			}
		};
		
		rhfile.afterQueueComplete = function(){
			jQuery("#upload-flash-container").css("display","none");
			jQuery("#upload-init-container").css("display","none");
			jQuery("#upload-files-container").css("display","block");
			initUpload2();
		};
	}
	
	//继续上传
	function initUpload2(){
		//文件上传
		var config = {
			"SERV_ID":"SY_COMM_WENKU_DOCUMENT", 
			"FILE_CAT":"SHARE_FILE", "TYPES":"*.pdf;*.doc;*.docx;*.wps;*.xls;*.xlsx;*.ppt;*.pptx;*.txt;*.flv;*.mp3;",
			"flash_url":"/sy/base/frame/coms/file/swfupload.swf",
			//"button_image_url":"/sy/comm/wenku/img/upload/upload_3.jpg",
			"upload_url":"/file/",
			"QUEUESIZE":10,
			"FILESIZE":"1024MB",
			"WIDTH":104,
			"HEIGHT":26,
			"TEXT":"<span style='color:#FFFFFF;font-weight:bold;height:26px;line-height:26px;font-size:14px;'>&nbsp;&nbsp;&nbsp;继续上传</span>"
		};
		var rhfile = new rh.ui.File({"config":config});
		rhfile.showNoneFile = function(){}; 
		jQuery("#upload-flash-container2").append(rhfile.obj); 
		
		//--------------------在上传之前判断文件重复--开始--------------------------//
		rhfile.beforeUploadCallback = function(file) {
			//只是一个文件上传的情况
			var param = {};
			var fileName = file["name"];
			param["fileName"] = fileName;
			var result = FireFly.doAct("SY_COMM_WENKU_DOCUMENT", "doubleUpload", param, true);
			if(result["repeat"]=='true'){
				if (window.confirm("文库中已存在「" + fileName +"」，是否要继续上传？")) {
			    	return true;
			    }else{
			    	alert("请重新选择...");
			    	return false ;
			    }
			}else{
				return true;
			}
		};
		//--------------------在上传之前判断文件重复--结束--------------------------//
		
		rhfile.initUpload();
		rhfile.fillData([]);
	  
		//callback
		rhfile.fillData = function(val) {
			for ( var i = 0; i < val.length; i++) {
				var file = val[i];
				var fileId = file["FILE_ID"];
				var fileName = file["FILE_NAME"];
				var suffix = getSuffix(fileName);
				var defaultTitle = getTitle(fileName);
				
				//上传成功，添加新的文件信息框
				var newBox = jQuery("#upload-item-0").clone().appendTo(jQuery("#upload-list"));
				newBox.find("input[name='DOCUMENT_FILE']").val(fileId);
				newBox.find(".item-hd .item-file-title span").text(defaultTitle);
				newBox.find(".content input.item-title-input").val(defaultTitle);
				newBox.find("input[name='DOCUMENT_FILE_SUFFIX']").val(suffix);
				newBox.find(".item-file-title .ic").removeClass().addClass("ic ic-"+suffix);
				
				var index = jQuery(".upload-item").length - 1;
				newBox.attr("id","upload-item-"+index);
			}
			batchEdit();
			open_close();
			doDelete();
			ChannelTree();
			doSubmit();
		};
	}
	
	//按钮：收起，展开
	function open_close(){	
		jQuery(".mod-upload .upload-item .item-hd").unbind("click").click(function(){
			var open_close = jQuery(this).find(".item-ctrl .ctrl-text");
			if(open_close.text()=="展开"){
				open_close.text("收起");
				jQuery(this).next().show();
			}
			else if(open_close.text()=="收起"){
				open_close.text("展开");
				jQuery(this).next().hide();
			}
		});
	}
	
	//展开
	function open(){
		var open_div = jQuery(".mod-upload .upload-item .item-hd").find(".item-ctrl .ctrl-text");
		open_div.text("收起");
		jQuery(".mod-upload .upload-item .item-hd").next().show();
	}
	
	//收起
	function close(){
		var close_div = jQuery(".mod-upload .upload-item .item-hd").find(".item-ctrl .ctrl-text");
		close_div.text("展开");
		jQuery(".mod-upload .upload-item .item-hd").next().hide();
	}
	
	//删除按钮
	function doDelete(){
		jQuery(".mod-upload .upload-item .item-hd .ui-imgBtn-delete ").unbind("click").bind("click",function(event){
			var p = jQuery(this).parentsUntil("#upload-list");
			p.remove();
			event.stopPropagation();
			
			//删除数据库中的文件信息
			var file_id = p.find("input[name='DOCUMENT_FILE']").val();
			var result = parent.FireFly.doAct("SY_COMM_FILE","delete",{"FILE_ID":file_id,"_PK_":file_id});
			
			if (result[UIConst.RTN_MSG].indexOf(UIConst.RTN_OK) == 0) {
	    		alert("删除成功");
				
				//如果没有信息项，就关闭该页面
				var length = jQuery(".mod-upload .upload-item").length - 1;
				if(length==0){
					window.setTimeout("parent.Tab.closeDirect(window.name);",500); 
				}
		   		batchEdit();
	   		}
		});
	}	
	
	//提交信息
	function doSubmit(){
		//jQuery(".mod-upload .upload-item .item-submit").unbind("click").click(function(){
		jQuery(".item-submit").unbind("click").click(function(){
			//var box = jQuery(this).parentsUntil(".upload-item");
			
			//找到对应按钮的upload-item元素
			var box = jQuery(this).parent().parent().parent().parent();
			var important_document = "";
			if(box.find("input[name='IMPORTANT_DOCUMENT']").prop("checked")){
				important_document = 1 ;
			}else{
				important_document = 2 ;
			}
			if(!box.find("input[name='DOCUMENT_CHNL']").val()){
				alert("栏目为空");
				return false;
			}
			var options = {
				"DOCUMENT_FILE":box.find("input[name='DOCUMENT_FILE']").val(),
				"DOCUMENT_TITLE":box.find("input[name='DOCUMENT_TITLE']").val(),
				"DOCUMENT_DESCRIPTION":box.find("textarea[name='DOCUMENT_DESCRIPTION']").val(),
				"DOCUMENT_CHNL":box.find("input[name='DOCUMENT_CHNL']").val(),
				"DOCUMENT_KEYWORDS":box.find("input[name='DOCUMENT_KEYWORDS']").val(),
				"DOCUMENT_FILE_SUFFIX":box.find("input[name='DOCUMENT_FILE_SUFFIX']").val(),
				"SITE_ID":"${SITE_ID!'SY_COMM_CMS'}",
				"IMPORTANT_DOCUMENT":important_document,
				"DOCUMENT_STATUS":box.find("input[name='DOCUMENT_STATUS']").val()
			};
			var result = parent.FireFly.doAct("SY_COMM_WENKU_DOCUMENT","save",options);
			if (result[UIConst.RTN_MSG].indexOf(UIConst.RTN_OK) == 0) {//成功后刷新列表
	    		alert("提交成功");
				//移除自己
				//box.parent().remove();
				box.remove();
				batchEdit();
				var length = jQuery(".mod-upload .upload-item").length - 1;
				if(length==0){
					window.setTimeout("parent.Tab.closeDirect(window.name);",100);
				}
				//显示我的文档
//				myDocuments();
	   		} 
		});
	}
	//栏目信息字典弹出选择
	function ChannelTree(){
		//jQuery(".mod-upload .upload-item .btn-category").unbind("click").bind("click", function(event) {
		jQuery(".btn-category").unbind("click").bind("click", function(event) {
			var _this = jQuery(this);
			var siteId = "${SITE_ID}";
			//1.构造树形选择参数
			var configStr = 'SY_COMM_WENKU_CHNL_MANAGE,{"TYPE":"single","EXTWHERE":" and SITE_ID=\'${SITE_ID}\'"}';//此部分参数说明可参照说明文档的【树形选择】配置说明
			var extendTreeSetting = "{'rhexpand':false,'expandLevel':2,'cascadecheck':false,'checkParent':false,'childOnly':true}";
			var options = {
				"config" :configStr,
				"extendDicSetting":StrToJson(extendTreeSetting),//非必须参数，一般用不到
				"replaceCallBack":function(idArray,nameArray){//回调，idArray为选中记录的相应字段的数组集合
					var param = {};
					param["CHANNEL_ID"] = idArray.join(",");
					param["CHANNEL_NAME"] = nameArray.join(",");
					jQuery(_this).find(".category-text").text(param["CHANNEL_NAME"]);
					jQuery(_this).find(".doc_chnl").val(param["CHANNEL_ID"]);
					if(jQuery(_this).find(".doc_chnl").attr("id") == "temp_doc_chnl"){
						apply_docs();
					}
					
					//写入cookie
					Cookie.set("wenku_last_upload_catId", param["CHANNEL_ID"],"365", "/");
					Cookie.set("wenku_last_upload_catName", param["CHANNEL_NAME"],"365", "/");
				}
			};
			//2.显示树形
			var dictView = new parent.rh.vi.rhDictTreeView(options);			
			dictView.show(event);	
			
			//调整弹出框样式
			var box = parent.jQuery("#SY_COMM_WENKU_CHNL_MANAGE-dictDialog").parent();
			box.css({"z-index":"9999","background":"white","top":"20%","left":"40%","position":"absolute"});
			jQuery(box).find(".ui-widget-content").removeClass("ui-widget-content");
		});
		
		//获取cookie 并写入默认值
					var lastCatId = Cookie.get("wenku_last_upload_catId");
					var lastCatName = Cookie.get("wenku_last_upload_catName");
					if (lastCatId) {
					jQuery(".doc_chnl").val(lastCatId);
					} 
					if (lastCatName) {
					jQuery(".category-text").text(lastCatName);
					}
					
	}
	
	function batchEdit(){
		//控制是否是批量上传，来激活批量编辑的功能
		jQuery("#upload_files_number").html(jQuery(".upload-item").length - 1);
		if(jQuery(".upload-item").length-1 >= 2){
			jQuery("#batch_div").css("display","block");
			close();
		}else{
			jQuery("#batch_div").css("display","none");
			open();
		}
	}
	
	
	jQuery(document).ready(function(){
		batchEdit();
		initUpload();				
		open_close();
		doDelete();
		ChannelTree();
		doSubmit();
			
		//批量上传
		jQuery("#btn-submit-all").unbind("click").click(function(){
			var doc_num = jQuery(".mod-upload .upload-item").length - 1;
			var doc_suc_num = 0;
			//循环提交信息
			jQuery(".item-submit").each(function(index,it){
				var box = jQuery(it).parent().parent().parent().parent();
			var important_document = "";
			if(box.find("input[name='IMPORTANT_DOCUMENT']").prop("checked")){
				important_document = 1 ;
			}else{
				important_document = 2 ;
			}
			if(!box.find("input[name='DOCUMENT_CHNL']").val()){
				alert("栏目为空");
				return false;
			}
			var options = {
				"DOCUMENT_FILE":box.find("input[name='DOCUMENT_FILE']").val(),
				"DOCUMENT_TITLE":box.find("input[name='DOCUMENT_TITLE']").val(),
				"DOCUMENT_DESCRIPTION":box.find("textarea[name='DOCUMENT_DESCRIPTION']").val(),
				"DOCUMENT_CHNL":box.find("input[name='DOCUMENT_CHNL']").val(),
				"DOCUMENT_KEYWORDS":box.find("input[name='DOCUMENT_KEYWORDS']").val(),
				"DOCUMENT_FILE_SUFFIX":box.find("input[name='DOCUMENT_FILE_SUFFIX']").val(),
				"SITE_ID":"${SITE_ID!'SY_COMM_CMS'}",
				"IMPORTANT_DOCUMENT":important_document,
				"DOCUMENT_STATUS":box.find("input[name='DOCUMENT_STATUS']").val()
			};
			var result = parent.FireFly.doAct("SY_COMM_WENKU_DOCUMENT","save",options);
			if (result[UIConst.RTN_MSG].indexOf(UIConst.RTN_OK) == 0) {//成功后刷新列表
				//成功后计数器加一
				doc_suc_num += 1;
				//移除自己
				//box.parent().remove();
				box.remove();
				batchEdit();
				var length = jQuery(".mod-upload .upload-item").length - 1;
				if(length==0){
					//alert("------------debug---------"+doc_num+"--------"+doc_suc_num+"------");
					if(doc_num == doc_suc_num){
						alert("批量上传成功！");
					}
					window.setTimeout("parent.Tab.closeDirect(window.name);",100);
				}
				//显示我的文档
//				myDocuments();
	   		}
			});
		});
	});
</script>
</head>
<body>
<div class="page" id="doc">
    <div id="bd">
        <div class="bd-wrap">
        	<div>&nbsp;</div>
            <div class="body">
                <div class="main">
                    <div class="upload-widget upload-widget-edit" id="upload-widget">
                    	<div id="upload-flash-container" class="upload-flash-container" style="display: block;">
						</div>
                        <div style="display: block;" id="upload-init-container">
                            <div class="mod mod-upload mb20">
                                <div class="hd pv20">
                                    <h3>上传文档</h3>
                                </div>
                                <div class="bd">
                                    <div class="box ext upload-type-info">
                                        <div class="media">
                                            <dl>
                                                <dt class="mb10">
                                                   	 你可以这样...
                                                </dt>
                                                <dd>
                                                    <b class="ic ic-multi mr5"></b>按住ctrl或Shift键可以上传多份文档
                                                </dd>
                                                <dd>
                                                    <b class="ic ic-private mr5"></b>支持私有文档的上传，仅您自己可以查看到
                                                </dd>
                                            </dl>
                                        </div>
                                    </div>
                                </div>
                            </div>
                            <div class="upload-notice ml20 c-gray9">
                                <dl class="mb10">
                                    <dt>
                                        	上传须知
                                    </dt>
                                    <dd>
                                        <b class="ic mr10"></b>每次只能上传一份文档，每份文档不超过20M，支持大部分文档格式
                                    </dd>
                                    <dd>
                                        <b class="ic mr10"></b>上传涉及侵权内容的文档将会被移除。
                                    </dd>
                                    <dd>
                                        <b class="ic mr10"></b>为了保证文档能正常显示，我们支持以下格式的文档上传
                                    </dd>
                                </dl>
                                <div class="data support-type">
                                    <table>
                                        <tbody>
                                            <tr>
                                                <td class="txtR">
                                                    MS Office文档
                                                </td>
                                                <td>
                                                    <b class="ic ic-doc ml10 mr5"></b>doc,docx<b class="ic ic-ppt ml10 mr5"></b>ppt,pptx<b class="ic ic-xls ml10 mr5"></b>xls,xlsx<b class="ic ic-vsd ml10 mr5"></b>vsd<b class="ic ic-pot ml10 mr5"></b>pot<b class="ic ic-pps ml10 mr5"></b>pps<b class="ic ic-rtf ml10 mr5"></b>rtf
                                                </td>
                                            </tr>
                                            <tr>
                                                <td class="txtR">
                                                    WPS office系列
                                                </td>
                                                <td>
                                                    <b class="ic ic-wps ml10 mr5"></b>wps<b class="ic ic-et ml10 mr5"></b>et<b class="ic ic-dps ml10 mr5"></b>dps
                                                </td>
                                            </tr>
                                            <tr>
                                                <td class="txtR">
                                                    PDF
                                                </td>
                                                <td>
                                                    <b class="ic ic-pdf ml10 mr5"></b>pdf
                                                </td>
                                            </tr>
                                            <tr>
                                                <td class="txtR">
                                                    	纯文本
                                                </td>
                                                <td>
                                                    <b class="ic ic-txt ml10 mr5"></b>txt
                                                </td>
                                            </tr>
                                            <tr>
                                                <td class="txtR">
                                                    EPUB
                                                </td>
                                                <td>
                                                    <b class="ic ic-epub ml10 mr5"></b>epub
                                                </td>
                                            </tr>
											<tr>
                                                <td class="txtR">
                                                 	影音
                                                </td>
                                                <td>
                                                    <b class="ic ic-mp3 ml10 mr5" style="width:20px;height:20px;"></b>mp3
													<b class="ic ic-flv ml10 mr5" style="width:20px;height:20px;"></b>flv
                                                </td>
                                            </tr>
                                        </tbody>
                                    </table>
                                </div>
                            </div>
                        </div>
                        <div style="display: none; padding-top: 0px;" class="mod mod-upload" id="upload-files-container">
                            <div class="hd edit-all-head pv20">
                            	<div id="upload-flash-container2" class="upload-flash-container" style="display: block; border:none;">
								</div>
                                <span class="act btn-submit-all-wrap">
                                	<a class="bt ui-btn ml20 ui-btn-26-green" id="btn-submit-all" href="###"><b class="btc"><b class="btText submit-text">全部提交</b></b></a></span>
                                <h3 class="item-message-all item-message-all-ok" id="item-message-all"><b id="upload_files_number"></b>篇文档上传完毕！<span style="color:#2D64B3;">请填写文档信息后点击“完成上传”</span></h3>
                                <p style="" class="item-progress-wrap" id="item-progress-all">
                                    <span class="item-progress"><span style="width: 350px;" class="item-progress-inner"></span></span>
                                    <span class="item-progress-text">100%</span>
                                </p>
                            </div>
                            <div class="bd pv20 pt20">
                        	
                        	
                        	<!----------------------点击批量填写信息弹出的对话框-------开始----------------------------->
                             <div id="batch_div" class="upload-item mb20">
                            	<div class="item-bd clearfix" style="display: block;">
                            		<form action="" method="post" name="upload-form-2" id="upload-form-2">
	                                   	 <p style="color:rgb(61,144,183); font-weight:bold;">您可以在此批量填写信息并应用到下列文档中。</p>
	                                   	 <div class="item-l">
	                                        <!-----------------------新加的-------开始--------------------------->
	                                        <div style="display:none;" class="item-summary mb10 show-tips imp_div">
	                                            <strong>是否为重要文件：</strong>
	                                            <input type="checkbox" id="temp_imp_doc" id="imp_checkbox" />
	                                        </div>
	                                        <!-----------------------新加的-------结束--------------------------->
	                                        <div class="item-category mb10">
	                                            <strong>栏目选择<i>*</i></strong>
	                                            <div class="category-select-wrap">
	                                                <span hidefocus="true" tabindex="-1" class="btn-category">
	                                                	<span id="temp_doc_cate" class="category-text">选择分类</span>
														<input type="hidden" id="temp_doc_chnl" value="" class="doc_chnl"/>
														<b class="ic ic-btn ml5"></b>
													</span>      
	                                            </div>
	                                        </div>
	                                    </div>
	                                    <div class="item-r">
	                                        <div class="item-privacy mb10 item-disabled">
	                                            <strong>是否公开</strong>
	                                            <p>
	                                                <label for="item-public-2">
	                                                    <input id="radio_1" type="radio" value="2" name="temp_pub_pri" checked="checked" class="item-privacy-radio item-public-radio public" tabindex="27">公共
	                                                </label><span class="ml10 c-gray9">任何人都可以检索或查看</span>
	                                            </p>
	                                            <p>
	                                                <label for="item-private-2">
	                                                    <input id="radio_2" type="radio" value="1" name="temp_pub_pri"  class="item-privacy-radio item-private-radio private" tabindex="27">私有
	                                                </label><span class="ml10 c-gray9">只有您本人可以查看</span>
	                                            </p>
	                                        </div>
	                                        <!--------------应用到下列所有文档的按钮注释掉----开始------------------->
	                                        <!--
	                                        <div style="position:absolute; bottom:30px; right:30px;">
	                                            <a tabindex="28" class="bt ui-btn ui-btn-26-green js-apply-docs" href="###"><b class="btc"><b class="btText">应用到下列所有文档</b></b></a>
	                                        </div>
	                                        -->
	                                        <!--------------应用到下列所有文档的按钮注释掉----结束------------------->
	                                    </div>
	                                </form>	
                            	</div>
                            </div>
                        	<!----------------------点击批量填写信息弹出的对话框-------结束----------------------------->
                            <!-----------------------点击批量编写信息的按钮----开始-------------------------------> 
                            <script>
                            	jQuery(document).ready(function(){
                            		
                            		//所有div收起
                            		jQuery(".mod-upload .upload-item .item-hd").trigger("click");
                            		
                            		/********给输入框绑定获取焦点事件************/
                            		jQuery("#temp_doc_des").bind("focus",function(){
                            			toFocus(this);
                            		});
                            		jQuery("#temp_doc_kwd").bind("focus",function(){
                            			toFocus(this);
                            		});
                            		
                            		//给"重要/公开/栏目"绑定事件
                            		jQuery("#temp_imp_doc,#radio_1,#radio_2").change(function(){
                            			apply_docs();
                            		});
                            	});
                            	
                            	//清空输入框
                            	function toFocus(who){
                            		jQuery(who).val("");
                            	}
                            	
                            	//原来"应用到下列所有文档"按钮触发的事件
                            	function apply_docs(){
                            		var temp_imp_doc = "";
                            			if(jQuery("#temp_imp_doc").prop("checked")){
                            				jQuery("input[name='IMPORTANT_DOCUMENT']").attr("checked",true);
                            			}else{
                            				jQuery("input[name='IMPORTANT_DOCUMENT']").attr("checked",false);
                            			}
                            			var temp_doc_cate = jQuery("#temp_doc_cate").text();
                            			var temp_doc_chnl = jQuery("#temp_doc_chnl").val();
                            			var temp_pub_pri = jQuery("input[name='temp_pub_pri']:checked").val();
                            			jQuery("input[name='DOCUMENT_CHNL']").val(temp_doc_chnl);
                            			jQuery(".category-text").text(temp_doc_cate);
                            			if(temp_pub_pri=="2"){
                            				jQuery(".public").attr("checked",true);
											jQuery(".private").attr("checked",false);
                            			}else{
                            				jQuery(".public").attr("checked",false);
											jQuery(".private").attr("checked",true);
                            			}
                            			close();
                            	}
                            </script>
                            <!-----------------------点击批量编写信息的按钮----开始------------------------------->
                              
                              
                                <div id="upload-list" class="upload-list">
                                    <div class="upload-item mb20" id="upload-item-0">
                                        <div class="item-hd">
                                            <span class="act"><span style="display:none;" class="c-gray9 mr5 js-delete-tips">删除成功！</span><a hidefocus="true" title="删除" class="ir ui-imgBtn-delete" href="###">删除</a></span>
                                            <h3 class="mb10">
                                            	<span class="item-file-title">
                                            		<b class="ic ic-pdf mr5"></b>
												</span>
												<i class="ic ic-upload-ok ml5" style="display: inline-block;"></i></h3>
                                            <div>
                                                <span class="item-progress-wrap mr10" id="item-progress-2" style="display: none;"><span class="item-progress"><span class="item-progress-inner" style="width: 195px;"></span></span><span class="item-progress-text">100%</span></span>
                                                <span class="item-msg msg-ok" style="display: inline;">文档附件上传成功！</span>
                                            </div>
                                            <a class="item-ctrl" href="###"><span class="ctrl-text">收起</span><b class="js-gbma gbma-u"></b></a>
                                        </div>
                                        <div class="item-bd clearfix" style="display: block;">
                                            <form action="" method="post" name="upload-form-2" id="upload-form-2">
                                                <div class="item-l">
                                                    <div class="item-title clearfix mb10">
                                                        <strong>标题<i>*</i></strong>
                                                        <div class="input-wrap">
                                                            <div class="box">
                                                                <div class="media">
                                                                    <span class="item-title-ext"></span>
                                                                </div>
                                                                <div class="content">
                                                                    <input name="DOCUMENT_TITLE" type="text" class="item-title-input" tabindex="22">
																	<input name="DOCUMENT_FILE" type="hidden">
																	<input name="DOCUMENT_FILE_SUFFIX" type="hidden">
                                                                </div>
                                                            </div>
                                                        </div>
                                                    </div>
                                                    <!-----------------------新加的-------开始--------------------------->
                                                    <div style="display:none;" class="item-summary mb10 show-tips imp_div">
                                                        <strong>是否为重要文件：</strong>
                                                        <input type="checkbox" name="IMPORTANT_DOCUMENT" id="imp_checkbox" />
                                                    </div>
                                                    <!-----------------------新加的-------结束--------------------------->
                                                    <div class="item-summary mb10 show-tips">
                                                        <strong>简介</strong>
                                                        <div class="input-wrap tips-wrap">
                                                            <textarea name="DOCUMENT_DESCRIPTION" tabindex="23" class="item-summary-input"></textarea>
                                                        </div>
                                                    </div>
                                                    <div class="item-category mb10">
                                                        <strong>栏目选择<i>*</i></strong>
                                                        <div class="category-select-wrap">
                                                            <span hidefocus="true" tabindex="-1" class="btn-category">
                                                            	<span class="category-text">-- 请选择 --</span>
																<input type="hidden" name="DOCUMENT_CHNL" class="doc_chnl"/>
																<b class="ic ic-btn ml5"></b>
															</span>      
                                                        </div>
                                                    </div>
                                                    <div class="item-keyword show-tips">
                                                        <strong>关键词</strong>
                                                        <div class="input-wrap tips-wrap">
                                                            <input type="text" name="DOCUMENT_KEYWORDS" class="item-keyword-input" tabindex="25">
                                                        </div>
                                                        <div class="keyword-sug-wrap">
                                                        </div>
                                                    </div>
                                                </div>
                                                <div class="item-r">
                                                    <div class="item-privacy mb10 item-disabled">
                                                        <strong>是否公开</strong>
                                                        <p>
                                                            <label for="item-public-2">
                                                                <input type="radio" name="DOCUMENT_STATUS" value="2" checked="checked" class="item-privacy-radio item-public-radio public" tabindex="27">公共
                                                            </label><span class="ml10 c-gray9">任何人都可以检索或查看</span>
                                                        </p>
                                                        <p>
                                                            <label for="item-private-2">
                                                                <input type="radio" name="DOCUMENT_STATUS" value="1"  class="item-privacy-radio item-private-radio private" tabindex="27">私有
                                                            </label><span class="ml10 c-gray9">只有您本人可以查看</span>
                                                        </p>
                                                        <input type="hidden" value="0" name="privacy_arr[0]" id="item-privacy-2">
                                                    </div>
                                                    <div class="item-submit">
                                                        <a tabindex="28" class="bt ui-btn js-btn-submit ui-btn-26-green" href="###"><b class="btc"><b class="btText submit-text">提交信息</b></b></a>
                                                    </div>
                                                </div>
                                                <input type="hidden" value="20002" name="ct"><input type="hidden" value="json" name="oe"><input type="hidden" value="newResponse" name="retType"><input type="hidden" value="f74ce6a3d1f34693daef3eae" name="doc_id_arr[0]" id="item-docid-2"><input type="hidden" value="1" name="new_upload"><input type="hidden" value="1" name="mod"><input type="hidden" value="utf8" name="encode">
                                            </form>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>           
                </div>
            </div>
        </div>
    </div>
</div>
<!-----------------新加的----开始----------------------->
<script>
	jQuery(document).ready(function(){
		var param = {};
		var result = FireFly.doAct("SY_COMM_WENKU_DOCUMENT","importantDocumentAdmin",param,true);
		if('true' == result["isAdmin"]){
			jQuery(".imp_div").show();
		}
	});
</script>
<!-----------------新加的----结束----------------------->
</body>
</html>