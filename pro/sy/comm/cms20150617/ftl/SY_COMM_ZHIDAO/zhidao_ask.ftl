<!DOCTYPE html>
<!--STATUS OK-->
<html>
<head>
<#include "/SY_COMM_ZHIDAO/config_constant.ftl">
<#include "global.ftl"/>
<script type="text/javascript"> var FireFlyContextPath =''; </script>
<script type="text/javascript" src="/sy/base/frame/jquery-1.8.2.min.js"></script>
<script type="text/javascript" src="/sy/base/frame/constant.js"></script>
<script type="text/javascript" src="/sy/base/frame/tools.js"></script>
<script type="text/javascript" src="/sy/base/frame/platform.js"></script>
<script type="text/javascript" src="/sy/comm/cms/js/zhidao.js"></script>

		<meta http-equiv="X-UA-Compatible" content="IE=Edge">
		<meta http-equiv="content-type" content="text/html; charset=UTF-8">
		<title>
			软虹知道 - 企业互动问答平台
		</title>
		<meta name="description" content="软虹知道是 基于搜索的互动式知识问答分享平台。用户可以根据自身的需求，有针对性地提出问题；同时，这些答案又将作为搜索结果，满足有相同或类似问题的用户需求。">
		<link rel="stylesheet" type="text/css" href="/sy/comm/zhidao/baidu_style_files/module_fe8dd525.css">
		<link rel="stylesheet" type="text/css" href="/sy/comm/zhidao/baidu_style_files/module_c595d1cf.css">
		<link rel="stylesheet" type="text/css" href="/sy/comm/zhidao/baidu_style_files/index_bcb7b9d9.css">
		<link rel="stylesheet" type="text/css" href="/sy/plug/search/rhSearchResult.css">
		
		<script type="text/javascript" src="/sy/comm/zhidao/baidu_style_files/base_aa43e0c6.js">
		</script>
		<script type="text/javascript" src="/sy/comm/zhidao/baidu_style_files/module_7087aaee.js">
		</script>
		<script type="text/javascript" src="/sy/comm/zhidao/js/zhidao_ask.js"></script>
		
		
		<!----------------知乎页面中的CSS------开始--------------------->
		<link rel="stylesheet" href="/sy/comm/zhidao/zhihu_style_files/c60843d5fb7d7aeb2bfa3619734d9b18.css" type="text/css" media="screen,print">
		<link rel="stylesheet" type="text/css" href="/sy/comm/zhidao/css/zhidao_ask.css">
		<!----------------知乎页面中的CSS------结束--------------------->
	</head>
	<body class="layout-center zhi" style='background-color:white;'>
		<!-----------这是个判断是否已经添加过了，如果添加过了就会有问题ID--------->
		<input type="hidden" id="questionIdHid">
	<!-------------知乎的提问页面HMTL----开始---------------->
	
	
	<div style="display:none"> </div>
	
	<div id="zh-tooltip"></div>
	<div style="opacity: 0.5; width: 1349px; height: 3776px;" class="modal-dialog-bg"></div>
	
	<!--------------------------------提问主体内容------------------开始------------------------------>
	<div aria-labelledby=":4e" role="dialog" style="width: 550px; left: 398.5px;" tabindex="0" class="modal-dialog absolute-position">
	  <!-----------------提问两个字的蓝色标题--------开始------------------>
	  <div id=":4e" class="modal-dialog-title modal-dialog-title-draggable">
	  	<span class="modal-dialog-title-text">提问</span>
	  	<span class="modal-dialog-title-close"></span>
	  </div>
	  <!-----------------提问两个字的蓝色标题--------结束------------------>
	  <div class="modal-dialog-content">
	    <div id="zh-add-question-wrap" class="zh-add-question-form">
	    
	      <!----------------提问表单正式开始------------------------->
	      <form style="display: block;" class="js-add-question-form">
	        <!-------------------提问的部分------页面上部分--------开始------------------->
	        <div class="zg-section-big clearfix">
	          <div id="zm-modal-dialog-info-wrapper"></div>
	          
	          <!---------------一个未給问题加问号的提示信息和X按钮-----开始-------------------->
	          <div style="display: none;position: relative;" id="zm-modal-dialog-warnmsg-wrapper">
	            <div class="zm-modal-dialog-warnmsg zm-modal-dialog-guide-warn-message zg-r5px">您还没有给问题添加问号</div>
	            <a name="close" title="关闭" href="javascript:;" class="zu-global-notify-close" style="display:none">x</a> 
	            <span class="zm-modal-dialog-guide-title-msg"></span> 
	          </div>
	          <!---------------一个未給问题加问号的提示信息和X按钮-----结束-------------------->
	          <!---------------提问的信息输入框-----开始-------------------->
	          <div class="zg-form-text-input add-question-title-form" style="position: relative;">
	            <textarea aria-autocomplete="list" role="combobox" aria-activedescendant="" 
	            	style="white-space: pre; height: 22px; min-height: 22px;" 
	            	autocomplete="off" aria-haspopup="true" placeholder="写下你的问题" 
	            	aria-label="写下你的问题" class="zg-editor-input zu-seamless-input-origin-element" 
	            	title="在这里输入问题" id="zh-question-suggest-title-content"></textarea>
	          </div>
	          <!---------------提问的信息输入框-----结束-------------------->
	          <!---------------一个提示问题字数少的提示信息-----开始-------------------->
	          <span style="display: none;" id="js-title-length-err-msg" class="title-length-err-msg zg-right">问题字数太少了吧</span>
	          <!---------------一个提示问题字数少的提示信息-----结束-------------------->
	          <div id="zh-question-suggest-ac-wrap" class="question-suggest-ac-wrap div_hide">
	            <div id="question_list" role="listbox" class="ac-renderer" style="-moz-user-select: none;">
	              <!-----------------------动态搜索的答案列表项--------开始---------------------------->
	              <!---------无答案提示信息---开始------------->
	              <div style="-moz-user-select: none;" class="ac-head zg-gray">你的问题可能已经有答案</div>
	              <!---------无答案提示信息---结束------------->
	              
				  <!-----这里面会动态的加载出提问的列表----------->
	              
	              <!-----------------------动态搜索的答案列表项--------结束---------------------------->
	            </div>
	          </div>
	        </div>
	        <!-------------------提问的部分------页面上部分--------结束------------------->
	        
	        
	        <!-----------------问题说明部分--------开始---------------->
	        <div class="zg-section-big">
	          <div class="add-question-section-title"> 
	          	问题说明（可选）： 
	          	<span id="zh-question-form-detail-err"></span> 
	          </div>
	          <div class="zm-editable-status-editing" id="zh-question-suggest-detail-container">
	            <div style="display: none;" class="zm-editable-content" data-disabled="1">&nbsp;</div>
	            <div style="" class="zm-editable-editor-wrap no-toolbar">
	              <div class="zm-editable-editor-outer">
	                <div class="zm-editable-editor-field-wrap">
	                  <!--------------问题说明的填写框---------开始---------------->
	                  <div style="" g_editable="true" class="zm-editable-editor-field-element editable" 
	                  		id="question_desc" placeholder="写下你对问题的说明" contenteditable="true">
	                  </div>
	                  <!--------------问题说明的填写框---------结束---------------->
	                </div>
	              </div>
	              <!----------问题说明的保存或取消按钮------开始------------>
	              <div class="zm-command">
	              	<a href="javascript:;" name="cancel" class="zm-command-cancel">取消</a>
	              	<a href="javascript:;" name="save" class="zg-r3px zg-btn-blue">保存</a>
	              </div>
	              <!----------问题说明的保存或取消按钮------结束------------>
	            </div>
	          </div>
	        </div>
	        <!-----------------问题说明部分--------结束---------------->
	        
	        
	        <!-----------选择话题的部分----------开始------------>
	        <div class="zm-add-question-form-topic-wrap">
	          <div class="add-question-section-title"> <span class="zg-gray zg-right">分类越精准，越容易让领域专业人士看到你的问题</span> 选择分类： <span id="zh-question-form-tag-err"></span> </div>
	          <div style="" id="zh-question-suggest-topic-container" class="zm-tag-editor zg-section">
	            <div style="display: none;" class="zm-tag-editor-labels"></div>
	            <div class="zm-tag-editor-editor zg-clear">
	              <div class="zg-inline"></div>
	              <div class="zm-tag-editor-command-buttons-wrap zg-left">
	              	<!--------------------静态输入框和其中的信息-----------------开始-------------->
	              	<!-----------输入分类的输入框-------开始---------->
	                <input id="talk_title" readonly="readonly" value="选择分类" aria-activedescendant=":4t" aria-autocomplete="list" role="combobox" placeholder="搜索分类" aria-label="搜索分类" aria-haspopup="true" class="zu-question-suggest-topic-input label-input-label" type="text">
	                <input type="hidden" id="talk_title_id" value="" />
	                <!-----------输入分类的输入框-------结束---------->
	                <!-----------添加完成按钮和提示信息-------开始---------->
	                
	                <a style="display: none;" name="add" href="#" class="zg-mr15 zg-btn-blue">添加</a>
	                <a style="display: none;" name="close" href="#">完成</a>
	                <label id="ask-type" for="topic" class="zg-icon search-icon"></label>
	                <label class="err-tip" style="display:none;">最多添加五个分类</label>
	                
	                <!-----------添加完成按钮和提示信息-------结束---------->
	                <!--------------------静态输入框和其中的信息-----------------结束-------------->
	              </div>
	              
	              <!--------显示到输入框中的提示信息--------开始----------->
	              <div style="display: none;" class="zm-tag-editor-maxcount zg-section">
	              	<span>最多只能为一个问题绑定5 个话题</span>
	              	<a style="display: none;" name="close" href="#">完成</a>
	              </div>
	              <!--------显示到输入框中的提示信息--------结束----------->
	            </div>
	            <div id="zh-question-suggest-autocomplete-container"></div>
	          </div>
	          <!------------------一个推荐添加的东西-----------开始------------->
	          <div id="target_person" class="sug-con zg-clear">
	          	<div class="fix-ask"> 
	          		<span>求助对象：</span>   
	          		<span>  
	          			<a target="_blank" href="#">刘志浩</a> 
	          			<span id="fix-del" title="取消求助"></span> 
	          		</span>  
	          	</div>
	          </div>
	          <!------------------一个推荐添加的东西-----------结束------------->
	        </div>
	        <!-----------选择话题的部分----------结束------------>
	        
	        <!-------------------最后的部分------------开始--------------------->
	        <div class="zm-command">
	          <label class="zm-question-form-hide-in-about-question-el zg-left">
	          	<input id="anonymous" class="zg-addq-isanon" type="checkbox" style="vertical-align:-5px;">匿名 
	          </label>
	          <!--
	          <a href="javascript:void(0);" name="cancel" class="zm-command-cancel">取消</a>
	          -->
	          <a href="javascript:void(0);" id="sub-btn" name="addq" class="zg-r5px zu-question-form-add zg-btn-blue">发布</a> 
	          <a id="look_question" href="javascript:void(0);" name="jumpq" class="zg-r5px zg-btn-blue zu-question-form-jump" style="display:none;">查看问题</a> 
	        </div>
	        <!-------------------最后的部分------------结束--------------------->
	      </form>
	      <!----------------提问表单正式结束------------------------->
	      
	    </div>
	  </div>
	  <div style="display: block;" class="modal-dialog-buttons"></div>
	</div>
	<!--------------------------------提问主体内容------------------结束------------------------------>
	
	
	<span tabindex="0" style="position: absolute; left: 398.5px; top: 38.5px;"></span>
	<div></div>
	<!-------------知乎的提问页面HMTL----结束---------------->
	
	</body>
</html>
