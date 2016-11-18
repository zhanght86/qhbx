<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>文档内容</title>
		<#include "global.ftl"/>
		<#include "/SY_COMM_WENKU/config_constant.ftl">
        <link href="/sy/comm/wenku/css/style_daoh.css" rel="stylesheet" type="text/css"/>
        <link href="/sy/comm/wenku/css/style2.css" rel="stylesheet" type="text/css"/>	
        <link rel="stylesheet" type="text/css" href="/sy/comm/wenku/css/wenku_icon.css"/>	
        <script type="text/javascript">
        	//document.js中用到此变量
        	var $group='${group}',$docId='${data.DOCUMENT_ID!''}';
        </script>
        <script type="text/javascript" src="/sy/comm/cms/js/wenku.js"></script>
		<script type="text/javascript" src="/sy/comm/wenku/js/document.js"></script>
		<!--收藏夹-->
		<script type="text/javascript" src="/sy/base/frame/jquery.tmpl.js"></script>
		<link rel="stylesheet" type="text/css" href="/sy/comm/zhidao/css/favorites.css">
		<script type="text/javascript" src="/sy/comm/zhidao/js/favorites.js"></script> 
<#noparse>
<script id="favorites-dialog-Template" type="text/x-jquery-tmpl">
    <div class="dialog-header">
		<span class="dialog-title">收藏</span>
	    <a href="#" class="js-dialog-close dialog-close"></a>
	</div>
	<div class="dialog-body">
		<div class="form-horizontal">
	    	<div class="control-group">
			    <label class="control-label" for="title">标题</label>
			    <div class="controls">
			      <input type="text" id="title" class="input-xxlarge" placeholder="" value="${title}" />
			      <span class="notnull">*</span>
			    </div>
			</div>
			<div class="control-group">
			    <label class="control-label" for="tags">标签</label>
			    <div class="controls">
			      <input type="text" id="tags" class="input-xlarge" placeholder="">
			      <span>多个标签用空格分隔</span>
			    </div>
			</div>
			<div class="tag-group">
			    	{{each items}}
		    			<a href="#" class="item">${$value.MARK_NAME}</a>
		   			{{/each}}
			</div>
		</div>
		<div class="dialog-info">
		      <span>收藏成功</span>
		      <a href="" class="">去我的收藏夹</a>
		</div>
	</div>
	<div class="dialog-footer">
		<a href="#" class="js-dialog-save btn savebtn">保存</a>
		<a href="#" class="js-dialog-close btn closebtn">关闭</a>
	</div>
</script>
</#noparse>	
    </head>
    <body>
    	<#--
    	<@coms_html comsId="${coms_nav_id}" siteId="${channel.SITE_ID}" comparam="{'titleBar':'portal-title-none'}">
			${coms.AREA}
		</@coms_html>
		-->
	<@channel debugName="文档栏目" channelId="${data.CHNL_ID}"> 	
		<div class="w980 mt20">
		    <div class="l1 fl">
		        <div class="bor13">
		        	<div class="favorite-dialog"></div>
		            <div class="t ${data.DOCUMENT_FILE_SUFFIX!'unknown'}" style="height:30px;margin:10px 0 0 15px;">
						<span>
							<p style='padding-right: 10px'>下载量： 
								<font id="down"> ${data.DOCUMENT_DOWNLOAD_COUNTER!0} </font>次    浏览量： 
								<font id="read"> ${data.DOCUMENT_READ_COUNTER!0} </font>次
							</p>
							
							<!--------------------新加的----------开始-------------------------------->
								
								<span id="imp_span" style="float:left; margin-right:20px; margin-top:0px; padding-top:0px; display:block;  vertical-align:super; display:none;">
									<a href="javascript:void(0);" dataId="${data.DOCUMENT_ID}" servId="SY_COMM_WENKU_DOCUMENT" owner="${data.S_USER}" disname="${data.DOCUMENT_TITLE!""}" class="js-favorite">
									 	收藏
									</a>
									<span style="height:10px; margin-left:3px; margin-right:3px; color:#ff0000; line-height:10px;">
										重要文档
									</span>
									<#if data.IMPORTANT_DOCUMENT == 1>
										<input type="checkbox" value="" name="important_doc" checked="checked" id="imp_checkbox" />
									<#else>
										<input type="checkbox" value="" name="important_doc" id="imp_checkbox" />
									</#if>
								</span>
								<script>
									jQuery(document).ready(function(){
										var param = {};
										var result = FireFly.doAct("SY_COMM_WENKU_DOCUMENT","importantDocumentAdmin",param,true);
										//如果当前用户是重要文档操作员
										if('true' == result["isAdmin"]){
											//显示重要文档操作按钮
											jQuery("#imp_span").css("display","block");
											//给按钮绑定事件
											jQuery("#imp_checkbox").bind("click",function(){
												if(jQuery(this).prop("checked") == true){
													var paramBean = {};
													var imp_docId = "${data.DOCUMENT_ID!''}";
													paramBean["IMPORTANT_DOCUMENT"] = 1 ;
													paramBean["_PK_"] = imp_docId ;
													FireFly.doAct("SY_COMM_WENKU_DOCUMENT","save",paramBean,true);
													jQuery("#imp_img").css("display","inline-block");
												}else{
													var no_paramBean = {};
													var no_imp_docId = "${data.DOCUMENT_ID!''}";
													no_paramBean["IMPORTANT_DOCUMENT"] = 2 ;
													no_paramBean["_PK_"] = no_imp_docId ;
													FireFly.doAct("SY_COMM_WENKU_DOCUMENT","save",no_paramBean,true);
													jQuery("#imp_img").css("display","none");
												}
											});
										}
									});
								</script>
							<!--------------------新加的----------结束-------------------------------->
							
			                <a rel="nofollow" id="wenji" href="javascript:;" style="text-decoration: none;" class="sc">收藏到文辑</a>
							<a style='float:left; width:40px; height:30px;' class="wk-bg-icon wk-fullScreen" href='/file/${data.DOCUMENT_FILE}?act=preview' target='_blank' title='全屏'><span style="padding-top:0px; text-align:center;vertical-align:50%; display:block;">全屏</span></a>
						</span>
		                <h1 style="font-size:18px;">${data.DOCUMENT_TITLE!""}</h1>
		                <!--------------------新加的----------开始-------------------------------->
		                	<#if data.IMPORTANT_DOCUMENT == 1>
		                		<div id="imp_img" class="imp" style="display:inline-block;"></div>
		                	<#else>
		                		<div id="imp_img" class="imp" style="display:none;"></div>
		                	</#if>
		                <!--------------------新加的----------结束-------------------------------->
		            </div>
		            <p style="margin:0 15px;"> 
						简介：${data.DOCUMENT_DESCRIPTION!""}
		            </p>
		        </div>
				<#if data.DOCUMENT_FILE_SUFFIX=="mp3">
				<!-- 音频播放窗口_begin-->
				<link href="/sy/comm/wenku/jPlayer/skin/blue.monday/jplayer.blue.monday.css" rel="stylesheet" type="text/css" />
				<script type="text/javascript" src="http://ajax.googleapis.com/ajax/libs/jquery/1.6/jquery.min.js"></script>
				<script type="text/javascript" src="/sy/comm/wenku/jPlayer/js/jquery.jplayer.min.js"></script>
				<script type="text/javascript">
				//<![CDATA[
				$(document).ready(function(){
				
					$("#jquery_jplayer_1").jPlayer({
						ready: function () {
							$(this).jPlayer("setMedia", {
								mp3:"/file/${data.DOCUMENT_FILE}"
							});
						},
						swfPath: "/sy/comm/wenku/jPlayer/js/",
						supplied: "mp3",
						wmode: "window"
					});
				});
				//]]>
				</script>
				<div id="jquery_jplayer_1" class="jp-jplayer"></div>
				<div id="jp_container_1" class="jp-audio">
					<div class="jp-type-single">
						<div class="jp-gui jp-interface">
							<ul class="jp-controls">
								<li><a href="javascript:;" class="jp-play" tabindex="1">play</a></li>
								<li><a href="javascript:;" class="jp-pause" tabindex="1">pause</a></li>
								<li><a href="javascript:;" class="jp-stop" tabindex="1">stop</a></li>
								<li><a href="javascript:;" class="jp-mute" tabindex="1" title="mute">mute</a></li>
								<li><a href="javascript:;" class="jp-unmute" tabindex="1" title="unmute">unmute</a></li>
								<li><a href="javascript:;" class="jp-volume-max" tabindex="1" title="max volume">max volume</a></li>
							</ul>
							<div class="jp-progress">
								<div class="jp-seek-bar" style="width:100%;">
									<div class="jp-play-bar"></div>
								</div>
							</div>
							<div class="jp-volume-bar">
								<div class="jp-volume-bar-value"></div>
							</div>
							<div class="jp-time-holder">
								<div class="jp-current-time"></div>
								<div class="jp-duration"></div>
								<ul class="jp-toggles">
									<li><a href="javascript:;" class="jp-repeat" tabindex="1" title="repeat">repeat</a></li>
									<li><a href="javascript:;" class="jp-repeat-off" tabindex="1" title="repeat off">repeat off</a></li>
								</ul>
							</div>
						</div>
					</div>
				</div>
				<!-- 音频播放窗口_end--> 
				
				<#elseif data.DOCUMENT_FILE_SUFFIX=="flv">
				<!-- 视频播放窗口_begin -->
				<div id="a1"></div>
				<script type="text/javascript" src="/sy/comm/wenku/ckplayer/ckplayer.js" charset="utf-8"></script>
				<script type="text/javascript">
					var flashvars={
					f:'/file/${data.DOCUMENT_FILE}',//视频地址
					a:'',//调用时的参数，只有当s>0的时候有效
					s:'0',//调用方式，0=普通方法（f=视频地址），1=网址形式,2=xml形式，3=swf形式(s>0时f=网址，配合a来完成对地址的组装)
					c:'0',//是否读取文本配置,0不是，1是
					x:'',//调用xml风格路径，为空的话将使用ckplayer.js的配置
					i:'',//初始图片地址
					d:'',//暂停时播放的广告，swf/图片
					u:'',//暂停时如果是图片的话，加个链接地址
					l:'',//视频开始前播放的广告，swf/图片/视频
					r:'',//视频开始前播放图片/视频时加一个链接地址
					t:'5',//视频开始前播放swf/图片时的时间
					e:'3',//视频结束后的动作，0是调用js函数，1是循环播放，2是暂停播放，3是调用视频推荐列表的插件
					v:'80',//默认音量，0-100之间
					p:'0',//视频默认0是暂停，1是播放
					h:'1',//播放http视频流时采用何种拖动方法，0是按关键帧，1是按关键时间点
					q:'',//视频流拖动时参考函数，默认是start
					m:'0',//默认是否采用点击播放按钮后再加载视频，0不是，1是,设置成1时不要有前置广告
					g:'',//视频直接g秒开始播放
					j:'',//视频提前j秒结束
					k:'',//提示点时间，如 30|60鼠标经过进度栏30秒，60秒会提示n指定的相应的文字
					n:'',//提示点文字，跟k配合使用，如 提示点1|提示点2
					b:'0x000',//播放器的背景色，如果不设置的话将默认透明
				 	w:''//指定调用自己配置的文本文件,不指定将默认调用和播放器同名的txt文件
					//调用播放器的所有参数列表结束
					};
					var params={bgcolor:'#000000',allowFullScreen:true,allowScriptAccess:'always'};//这里定义播放器的其它参数如背景色（跟flashvars中的b不同），是否支持全屏，是否支持交互
					var attributes={id:'ckplayer_a1',name:'ckplayer_a1'};
					//下面一行是调用播放器了，括号里的参数含义：（播放器文件，要显示在的div容器，宽，高，需要flash的版本，当用户没有该版本的提示，加载初始化参数，加载设置参数如背景，加载attributes参数，主要用来设置播放器的id）
					swfobject.embedSWF('/sy/comm/wenku/ckplayer/ckplayer.swf', 'a1', '832', '500', '10.0.0','/sy/comm/wenku/ckplayer/expressInstall.swf', flashvars, params, attributes); //播放器地址，容器id，宽，高，需要flash插件的版本，flashvars,params,attributes	
					//调用播放器结束
				</script>
				<!-- 视频播放窗口_end -->
				
				<#elseif data.DOCUMENT_FILE_SUFFIX=="swf">
					<object classid="clsid:D27CDB6E-AE6D-11cf-96B8-444553540000" codebase="http://download.macromedia.com/pub/shockwave/cabs/flash/swflash.cab#version=7,0,19,0" width="500" height="500">
					  <param name="movie" value="/file/${data.DOCUMENT_FILE}" />
					  <param name="quality" value="high" />
					  <embed src="/file/${data.DOCUMENT_FILE}?act=open" quality="high" pluginspage="http://www.macromedia.com/go/getflashplayer" type="application/x-shockwave-flash" width=100% height="500"></embed>
					</object>
				<#else>
		        <!-- 此处div用做loading效果 -->
			    <div id="frameprogrssBar" align="center" style="height:600px;width:100%;margin-top:50px;">
			        <img src="/sy/comm/wenku/img/load1.gif" />&nbsp;努力加载中...
			    </div>
				<!--iframe属性src中放入需要加载的页面或页面路径    /file/${data.DOCUMENT_FILE}?act=preview-->
	        	<iframe align="center" frameborder="0" height="600" width=100%  style="overflow-x: hidden; overflow-y:auto;display:none;"
				 src="/file/${data.DOCUMENT_FILE}?act=preview" id="framecontent"></iframe>
				 <script type="text/javascript">
			        startload(document.getElementById("frameprogrssBar"), document.getElementById("framecontent"))
			        function startload(loadstr, iframestr){
			            var loadstr = loadstr;
			            var iframestr = iframestr;
			            loadstr.style.display = "block";//显示进度条
			            iframestr.style.display = "none";//隐藏 数据
			            if (iframestr.attachEvent) {  
						    iframestr.attachEvent("onload", function() {  
						                //以下操作必须在iframe加载完后才可进行  
						        loadstr.style.display = "none";
			                    iframestr.style.display = "block";  
						    });  
						} else {  
						    iframestr.onload = function() {  
						                //以下操作必须在iframe加载完后才可进行  
						        loadstr.style.display = "none";
			                    iframestr.style.display = "block"; 
						    };  
						}  
			        }
			    </script>
				
				</#if>
		        <div style="display:none;" class="bor321_xu1" id="docContent">
		            <style type="text/css">
		                .bor321_xu1 {
		                    border: 6px #e7e6e6 solid;
		                    width: 728px;
		                    font-size: 12px;
		                } .bor321_xu2 {
		                    border: 1px #c5c3c3 solid;
		                    background: #f8f8f8;
		                    color: #666;
		                    line-height: 20px;
		                    padding: 12px;
		                    height: 563px;
		                    overflow: auto;
		                }
		            </style>
		            <div class="bor321_xu2">
		                
		            </div>
		        </div>
		        <!-- text end --><!-- flash end -->
				<div style='width:99.9%;height:65px;background: #F8F8F8;border: 1px solid #E4E4E4;'>

					<div class="h46 fr mt10">
						<span class="cb size fl mr10" style='line-height: 2;'>
	                    	大小：#{((data.DOCUMENT_FILE_SIZE!0)/1024/1024)?number;m1M2}MB 
	               		</span>
	                    <a rel="nofollow" class="down mr10" id="downloadId2" href="/file/${data.DOCUMENT_FILE}?act=download"></a>
	                </div>
					<div class='cb' style='padding-top:5px'></div>
				</div>
				<div class='pt15 pb5 h46' style="background-color:#FFF;">
						<div class='fl ml10' style="margin-left:0px;"><img id='currentUsImg' width="40" height="40" src="" class="rh-user-info-circular-bead"></div>
						<div class='fl ml10 pt15' style='color: #259;font-size:13px;font-weight:bold'><span id='currentUsName'></span></div>
					</div>
				<#if data.DOCUMENT_COMMENT_STATUS != 0>
				<div style="width:99.9%;" class="document_comment" id="document_comment">
					
				</div>
				</#if>
				<!-- 评论 end  -->
		    </div>
		    <div class="r1 fl ml10">	
				<!-- 相关文档_begin-->
				<style type='text/css'>
					.zjw-baiduwenku-updoc {width: 240px;display: inline;text-align: center;}
					.zjw-baiduwenku-updoc a{display: inline-block;margin-top:10px;margin-left:10px;width: 145px;height: 32px;background: url(/sy/comm/wenku/baidu_style_files/allbg_cd6bd677.png) no-repeat left -48px;}
					.zjw-baiduwenku-updoc a:hover{background: url(/sy/comm/wenku/baidu_style_files/allbg_cd6bd677.png) no-repeat left -83px;}
				</style>
				<script type="text/javascript" src="/sy/comm/cms/js/wenku.js"></script>
				<div style="background:#F8F8F8;border:1px #ccc solid;padding-left:26px;margin-right:-2px;">
					<div class='zjw-baiduwenku-updoc'>
						<a href="#" onclick="javascript:upload('${upload_tmpl_id}','${site_id}')" target="_self" id="upload-icon"
							data-logsend="{&quot;send&quot;:[&quot;home&quot;,&quot;homeclk&quot;,{&quot;subtype&quot;:&quot;upload&quot;,&quot;login&quot;:&quot;1&quot;}]}"></a>
					</div>
					<div style='border-bottom:1px #ccc dashed;padding-top:5px;'></div>
					<div class='ml5 mt5'>
						<div class='mb5 f13 fb'>
	                                                             文档信息
	                    </div>
						<div>
							<table style='border-collapse:separate;border-spacing:5px;'>
								<tr>
									<td>
										<a href="javascript:othersDocuments('${user_center_tmpl_id}','${data.S_USER}')">
											<img width="50" height="50" src="${data.S_USER__IMG}" class="rh-user-info-circular-bead">
										</a>
									</td>
									<td>
										<a href="javascript:othersDocuments('${user_center_tmpl_id}','${data.S_USER}')">
											${data.S_UNAME!document.S_USER__NAME!document.S_USER}
										</a>
									</td>
								</tr>
							</table>
						</div>
						<div class='cb'>
							  贡献时间：${data.S_CTIME!""}
						</div>
					</div>
					<div style='border-bottom:1px #ccc dashed;margin-top:5px'></div>
				    <div class="manyd mt10 mb15">
		                <div id="currentRatingAvg" class="ll">
		                    #{(data.DOCUMENT_AVG_SCORE!0)?number;m1M1}
		                </div>
		                <div  style="float:left;padding: 15px 0 0 5px;">
		                    <ul>
		                        <li id="rating">
		                            <span class="blue">评分：</span>
		                            <font>
		                                <img width="14" height="12" v="0" src="/sy/comm/wenku/img/star_1.png">
										<img width="14" height="12" v="1" src="/sy/comm/wenku/img/star_1.png">
										<img width="14" height="12" v="2" src="/sy/comm/wenku/img/star_1.png">
										<img width="14" height="12" v="3" src="/sy/comm/wenku/img/star_1.png">
										<img width="14" height="12" v="4" src="/sy/comm/wenku/img/star_1.png">
		                            </font>
									<span class="red">欢迎点评</span>
		                        </li>
		                        <li>
		                            <span class="gray">
		                            	已有<font id="ratingSummary">${data.DOCUMENT_SCORER_COUNTER!0}</font>位用户参与评分
		                            </span>
		                        </li>
		                    </ul>
		                </div>
		            </div>	
				</div>	
				<script type="text/javascript">
					jQuery(document).ready(function(){
						 (function def(){
							jQuery("#rating img").attr("src","/sy/comm/wenku/img/star_1.png");
							jQuery("#rating img:lt(${(data.DOCUMENT_AVG_SCORE!0)?number?int})").attr("src","/sy/comm/wenku/img/star_01.png");
							
						})();
					});
				</script>
				 
				
				<!--------------新加热门文档--开始----------------------->
				<div class="xu_bor6">
		            <div class="tit11">
		                                                   热门文档
		            </div>
		            <ul class="list13">
		            	<@wenku_list debugName="热门文档" order="DOCUMENT_READ_COUNTER desc" channelId="${data.DOCUMENT_CHNL}" count="5">
						<#list tag_list as doc>
		                <li class="unknown ${doc.DOCUMENT_FILE_SUFFIX!'txt'} <#if doc_index==4>last</#if>">
		                    <div class="div1">
		                        <a href="javascript:view('${doc.DOCUMENT_ID}', '${doc.DOCUMENT_TITLE}');">
		                        	<#if doc.DOCUMENT_TITLE?length gt 20>
		                        		${doc.DOCUMENT_TITLE[0..20]}...
		                        	<#else>
		                        		${doc.DOCUMENT_TITLE}
		                        	</#if>
		                        </a>
		                    </div>
		                    <div title="文档被评为 ${(doc.DOCUMENT_AVG_SCORE!0)?number} 分" class="div2">
		                        <span style="width:${(doc.DOCUMENT_AVG_SCORE!0)?number * 100/5}%"></span>
		                    </div>
		                </li>
						</#list>
						</@wenku_list>			                
		            </ul>
		        </div>
				<!--------------新加热门文档--结束----------------------->
				<!--------------新加最新文档--开始----------------------->
				<div class="xu_bor6">
		            <div class="tit11">
		                                                   最新文档
		            </div>
		            <ul class="list13">
		            	<@wenku_list debugName="最新文档" order="S_CTIME desc" channelId="${data.DOCUMENT_CHNL}" count="5">
						<#list tag_list as doc>
		                <li class="unknown ${doc.DOCUMENT_FILE_SUFFIX!'txt'} <#if doc_index==4>last</#if>">
		                    <div class="div1">
		                        <a href="javascript:view('${doc.DOCUMENT_ID}', '${doc.DOCUMENT_TITLE}');">
		                        	<#if doc.DOCUMENT_TITLE?length gt 20>
		                        		${doc.DOCUMENT_TITLE[0..20]}...
		                        	<#else>
		                        		${doc.DOCUMENT_TITLE}
		                        	</#if>
		                        </a>
		                    </div>
		                    <div title="文档被评为 ${(doc.DOCUMENT_AVG_SCORE!0)?number} 分" class="div2">
		                        <span style="width:${(doc.DOCUMENT_AVG_SCORE!0)?number * 100/5}%"></span>
		                    </div>
		                </li>
						</#list>
						</@wenku_list>			                
		            </ul>
		        </div>
				<!--------------新加最新文档--结束----------------------->
				<!-----------------相关文辑--开始------------------------------->
		    	<div class="mod fenyuejinbu userListMod">
		      		<div class="xu_bor6">
			            <div class="tit11">
			                                                   相关文辑
			            </div>
			            <ul class="list13">
			            	<@wenku_doclist_aboutDoc debugName="相关文辑"  page="${page}" count="5" docId="${data.DOCUMENT_ID}">
			            	<#if tag_list?size == 0>
			            		<center><p style="color:red;">无相关文辑</p></center>
			            	<#else> 
							<#list tag_list as doc>
			                <li class="unknown">
			                    <div class="div1">
			                        <a href="#" url="/cms/SY_COMM_WENKU_DOCLIST/${doc.LIST_ID}/index_1.html" class="list_a list_a_tab"
										target="_self" title="${doc.LIST_TITLE}">
										<#if doc.LIST_TITLE?length gt 30>
										${doc.LIST_TITLE[0..30]}...
										<#else>
										${doc.LIST_TITLE}	
										</#if>	
										</a>
			                    </div>
			                    <div class="hot_wenji_div">
			                        <p style="font-size:12px;">${doc.LIST_DOCUMENT_COUNTER}篇文档/浏览${doc.LIST_READ_COUNTER}次</p>
			                    </div>
			                </li>
							</#list>
							</#if>
							</@wenku_doclist_aboutDoc>			                
			            </ul>
			        </div>
				</div>
	          <!-----------------热门文辑--结束------------------------------->
	          
	          
		    </div>
		</div>
		<style type="text/css">
			#d-top {
			    bottom: 10px;
			    float: right;
			    position: fixed;
			    right: 20px;
			    z-index: 2000;<#-- 编辑器覆盖了返回顶部按钮，重置z-index-->
			}
		</style>
		<div id="d-top">
			<a title="回到顶部" onclick="javascript:document.body.scrollTop=0;document.documentElement.scrollTop=0;parent.document.body.scrollTop=0;parent.document.documentElement.scrollTop=0;this.blur();return false;" href="#">
				<img alt="TOP" src="/sy/comm/news/img/top.png">
			</a>
		</div>    
	</@channel>	
    </body>
<!-----------使用新tab来打开单个文辑页面--开始-------------------->
<script>
	jQuery(".list_a_tab").bind("click",function(){
		var url = jQuery(this).attr("url");
		var temp_title = jQuery(this).attr("title");
		var title = "";
		if(temp_title.length > 6){
			title = temp_title.substring(0,6)+"...";
		}else{
			title = temp_title;
		}
		var params = {};
		var options = {"url":url,"tTitle":title,"scrollFlag":true ,"params":params,"menuFlag":3};
		parent.Tab.open(options);
	})
</script>
<!-----------使用新tab来打开单个文辑页面--结束-------------------->
</html>
