<!DOCTYPE html>
<!--STATUS OK-->
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta http-equiv="X-UA-Compatible" content="IE=Edge">
<title>全部文档</title>
<#include "/SY_COMM_WENKU/config_constant.ftl">
<script type="text/javascript"> var FireFlyContextPath =''; </script>
<script type="text/javascript" src="/sy/base/frame/jquery-1.8.2.min.js"></script>
<script type="text/javascript" src="/sy/base/frame/constant.js"></script>
<script type="text/javascript" src="/sy/base/frame/tools.js"></script>
<script type="text/javascript" src="/sy/base/frame/platform.js"></script>
<script type="text/javascript" src="/sy/comm/cms/js/wenku.js"></script>
<meta name="description" content="在线互动式文档分享平台，在这里，您可以和千万网友分享自己手中的文档，全文阅读其他用户的文档 ">
<link rel="stylesheet" type="text/css" href="/sy/comm/wenku/baidu_style_files/framework_48fb02e1.css">
<link rel="stylesheet" type="text/css" href="/sy/comm/wenku/baidu_style_files/framework_7d9033ac.css">
<link rel="stylesheet" type="text/css" href="/sy/comm/wenku/baidu_style_files/uploadBox_d6fe1324.css">
<link rel="stylesheet" type="text/css" href="/sy/comm/wenku/baidu_style_files/userList_6b1e06e8.css">
<link rel="stylesheet" type="text/css" href="/sy/comm/wenku/baidu_style_files/list_35637000.css">
<link rel="stylesheet" type="text/css" href="/sy/comm/wenku/baidu_style_files/childClassBlock_79b2739b.css">
<link rel="stylesheet" type="text/css" href="/sy/comm/wenku/baidu_style_files/cpmsg_b717a84f.css">
<link rel="stylesheet" type="text/css" href="/sy/comm/wenku/baidu_style_files/dataList_d8492a2c.css">
<link rel="stylesheet" type="text/css" href="/sy/comm/wenku/baidu_style_files/pager_1cd6d3a7.css">
<link rel="stylesheet" type="text/css" href="/sy/comm/wenku/css/document-list.css">
<link rel="stylesheet" type="text/css" href="/sy/comm/wenku/baidu_style_files/module_index_777ae95d.css">
<link href="/sy/comm/wenku/css/style2.css" rel="stylesheet" type="text/css"/>
<script type="text/javascript" src="/sy/comm/wenku/baidu_style_files/base_75690991.js"></script>

<script src="/sy/comm/wenku/baidu_style_files/wenku.js"></script>
 
</head>
<body>

	<!-- page begin -->
<div id="body">	
	<!-- user bar -->
	<div id="doc" class="page">
<#include "/SY_COMM_WENKU/baiduwenku_header.ftl">			
<#include "/SY_COMM_WENKU/baiduwenku_navigation.ftl">

		<div id="bd">
			<div class="bd-wrap">
				<!-----------加一小段CSS，一会儿移走-----开始--------------->
				<style>
				.skin-category-list .hd {
				    background: none repeat scroll 0 0 #E7F2F9;
				    border-bottom: 1px solid #E7F2F9;
				    font-size: 12px;
				    height: 33px;
				    line-height: 33px;
				    padding: 0 11px;
				    margin-bottom:5px;
				}
				.hd b, .hd span {
				    float: left;
				}
				.hd span.r {
				    color: #DEDDDD;
				    float: right;
				}
				.hd .r {
				    font-size: 12px;
				}
				</style>
				<!-----------加一小段CSS，一会儿移走-----结束--------------->
				
				<div class="body" style="margin-top:0;">		
					<div class="main">
						<#include "/SY_COMM_WENKU/baiduwenku_list_navigation.ftl">
						<@list_navigation data/>
					 <@wenku_list debugName="获取栏目下数据" channelId="${data.CHNL_ID}" page="${page}" count="20"> 
						<div class="mod dataList">
						<!-------------------内容部分----开始---------------------->
							<!--精品文档-->
			
							<div class="clear"></div>
							<!--幼儿小学教育推荐-->
							<div class="mod mb20 skin-category-list" id="edu-cate">
						   		<@channel_list debugName="栏目列表" channelId="${data.CHNL_ID}" count="5"> 
						    	<#list tag_list as obj>
								<div class="inner" style="height:270px; border:3px rgb(231,242,249) solid;">
									<div class="hd">
										<b><a href="<@chnlUrl obj.CHNL_ID 1 />"
											target="_self">${obj.CHNL_NAME}</a></b> <span class="r" id="edu-class">
											 <a
											href="<@chnlUrl obj.CHNL_ID 1 />"
											class="icon-more">更多</a></span>
									</div>
									<div class="cd c-align hotRead">
										<div id="edu-hot-cate bd">
											<div class="hot-img mod classBox-big">
												<div class="gd-g classRow-big catalog-76-big">
													<@wenku_list debugName="栏目下文档:${obj.CHNL_NAME}" channelId="${obj.CHNL_ID}" count="5"> 
														<#list tag_list as doc>
															<div class="gd-g-u gd-u-1-6-big classItem-big">
																<@setDocPic doc "-big"/>
															</div>
														</#list>
													 </@wenku_list>
											 	</div>
											</div>
										</div>
									</div>
								</div>
								</#list>
								</@channel_list>
							</div>
						
						<!-------------------内容部分----结束---------------------->	
 						</div>
 					</@wenku_list>
						
					</div>
					
	<!-----------begin aside----------------->				
	<div id="aside" class="aside" style="margin-top:27px;">
			<div class="mod fenyuejinbu userListMod">
				<!--------------新加热门文档--开始----------------------->
				<div class="xu_bor6">
		            <div class="tit11">
		                                                   热门文档
		            </div>
		            <ul class="list13">
		            	<@wenku_list debugName="热门文档" order="DOCUMENT_READ_COUNTER desc" channelId="${data.DOCUMENT_CHNL}" count="5">
						<#list tag_list as doc>
		                <li class="unknown ${doc.DOCUMENT_FILE_SUFFIX!'txt'}">
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
			</div>
			<div class="mod fenyuejinbu userListMod">
				<!--------------新加最新文档--开始----------------------->
				<div class="xu_bor6">
		            <div class="tit11">
		                                                   最新文档
		            </div>
		            <ul class="list13">
		            	<@wenku_list debugName="最新文档" order="S_CTIME desc" channelId="${data.DOCUMENT_CHNL}" count="5">
						<#list tag_list as doc>
		                <li class="unknown ${doc.DOCUMENT_FILE_SUFFIX!'txt'}">
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
			</div>
			</div>
			
		</div>
		<div class="clear"></div>
		<!----------end aside--------------->			
				</div>
			</div>
		</div>
	</div>
	<#include "/SY_COMM_WENKU/baiduwenku_footer.ftl">
	
	<div data-guid="TANGRAM$3" id="tangram-suggestion--TANGRAM$3-main"
		class="tangram-suggestion-main"
		style="position: absolute; display: none; left: 294px; top: 73px; width: 408Fpx;">
		<div id="tangram-suggestion--TANGRAM$3" class="tangram-suggestion"
			style="position: relative; top: 0px; left: 0px"></div>
	</div>
	<div id="docBubble" class="docBubble">
		<i class="triangle-t"></i><i title="关闭" class="close markSend">关闭</i>
		<div class="tl">
			<div class="inner"></div>
		</div>
		<div class="tr"></div>
		<div class="bl"></div>
	</div>
</div>
<script type='text/javascript'>
	$(document).ready(function(){
		
		$(".insidea").hover(
			function(){
				$(".outside_inside").removeClass("re").addClass("wa");
				$(this).parent().removeClass("wa").addClass("re");
			},
			function(){
			    $(this).parent().removeClass("re").addClass("wa");
			}
		);
		
		//去除a标签点击后的边框
		$('a').bind('focus',function(){
			if(this.blur){
				this.blur();
			}
		});
		
		readCookie();
		
		
		jQuery(".list_cookie").bind("click",function(){
			//写cookie操作 sName：cookie名称.sValue：cookie值,oExpires：过期时间
			Cookie.set("wenku_doc_show_type", "list","365", "/");
			//alert("---debug---ok");
			readCookie();
		});
		
		jQuery(".nolist_cookie").bind("click",function(){
			Cookie.set("wenku_doc_show_type", "nolist","365", "/");
			readCookie();
		});
	});
	
	var readCookie = function(){
		//读cookie操作,参数：cookie名称 返回值：字符串
		var showStr = Cookie.get("wenku_doc_show_type");
		//alert(showStr);
		//console.debug(showStr);
		
		if(jQuery(".list").hasClass() != true){
			jQuery(".list").addClass("hidden");
		}
		if(jQuery(".nolist").hasClass() != true){
			jQuery(".nolist").addClass("hidden");
		}
		if('list' == showStr || '' == showStr || null == showStr){
			
			//显示列表
			jQuery(".list").removeClass("hidden");
		}else{
			jQuery("#wk-tab-left").removeClass("wk-tab-left-left").addClass("wk-tab-right-left");
			jQuery("#wk-tab-right").removeClass("wk-tab-left-right").addClass("wk-tab-right-right");
			
			//显示缩略图
			jQuery(".nolist").removeClass("hidden");
			//激活图片加载
			window.scroll(0,1);
			window.scroll(0,-1);
		} 
	}
	
	
</script>
<!--------------加上一段延时加载图片的JS--------开始--------------->
<script src="/sy/comm/wenku/js/lazy-load-img.js"></script>
<!--------------加上一段延时加载图片的JS--------结束--------------->
</body>

</html>