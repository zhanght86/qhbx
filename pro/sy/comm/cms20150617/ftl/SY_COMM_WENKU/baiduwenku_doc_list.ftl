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
			
				<div class="body" style="margin-top:0;">		
					<div class="main">
						<#include "/SY_COMM_WENKU/baiduwenku_list_navigation.ftl">
						<@list_navigation data/>
					<!-- 
						<div class="cpmsg-wrap">
							<img class="cpmsg-icon" alt=""
								src="/sy/comm/wenku/baidu_style_files/cpmsg-alert.gif">我们不允许任何未经著作权人同意而将其作品进行上传和公开分享的行为。一旦您发现了此类文档，希望您能前往<a
								href="#" target="_self">投诉区</a>进行投诉,我们非常感谢您为维护软虹文库无盗版环境作出的贡献。
						</div>
					 -->	 
					 <@wenku_list debugName="获取栏目下数据" channelId="${data.CHNL_ID}" page="${page}" count="20"> 
						<div class="mod dataList">
							<ul class="docAlbumTab">
								<li class="selected"><a
									href="" class="Doclist"
									hidefocus="true">文档</a></li>
								<li><a href="/cms/SY_COMM_WENKU_DOCLIST_CHNL/${data.CHNL_ID}.html?&selectMenu=${data.CHNL_ID}" class="Doclist">文辑</a></li>
								<style>
									.style_1{
										margin-left:630px;margin-bottom:10px;
									}
									.style_2{
										*margin-left:0px;
										*margin-bottom:0px;
									}
								</style>
								<div class="style_1 style_2">
									<div id ='showtype' class='pos'>
									<div class="show">
										<a id="wk-tab-left"  class="list_cookie wk-bg-icon wk-tab wk-tab-left-left" title='列表' href='javascript:void(0);'>
										
										</a>
										<a id="wk-tab-right" class="nolist_cookie wk-bg-icon wk-tab wk-tab-left-right" title='缩略图' href='javascript:void(0);'>
										</a>
									</div>
									</div>
								</div>
							</ul>
							<div class="tab-line"></div>

							<div id="tabContent" class="tabContent">
									<table class="list hidden" id="docList" cellpadding="0" cellspacing="0" width="100%">
										<tbody>
											<tr>
												<th width="32">格式</th>
												<th width="394">文档名称</th>
												<th width="100">上传者</th>
												<th width="70">下载量</th>
												<th width="70">上传时间</th>
												<!-- 
												<th width="70"><a title="按下载量排序"
													href="">下载量</a></th>
													
												<th width="70">上传时间<span title="按上传时间排序" class="icon sort"></span></th>
												 -->
											</tr>
								
										<#list tag_list as doc>
												<#if doc_index % 2 = 0 >
												<tr>
												<#else>
												<tr class="hasb">
												</#if>
													<td class="i"><span title="${doc.DOCUMENT_FILE_SUFFIX}" class="ic ic-list-${doc.DOCUMENT_FILE_SUFFIX}"></span></td>
													<td><a
														href="#" class="list_a"
														onclick="javascript:view('${doc.DOCUMENT_ID}','${doc.DOCUMENT_TITLE}')"; 
														target="_self" title="${doc.DOCUMENT_TITLE}">
														<#if doc.DOCUMENT_TITLE?length gt 30>
														${doc.DOCUMENT_TITLE[0..30]}...
														<#else>
														${doc.DOCUMENT_TITLE}	
														</#if>	
														</a><span>${doc.DOCUMENT_FILE_PAGES!0}页</span></td>
													<td class="un"><div>
															<a href="#"
																data-logsend="{'send':['view','user',{'l':''}]}"
																class="Author logSend" target="_self">${doc.S_UNAME}</a>
														</div></td>
													<td><span>${doc.DOCUMENT_DOWNLOAD_COUNTER}次</span></td>
													<td><span class="timeago">
													${doc.S_CTIME}
													</span></td>
												</tr>
										</#list>
										
										
										</tbody>
									</table>
										
										
										<!------------------缩略图部分内容----开始---------------------->
										<!--
										<table class="nolist hidden" style="width:100%; height:auto;">
											<br />
										<#list tag_list as doc>
												<#if doc_index % 5 == 0>
													<tr>
												</#if>
												<td width="140px" height="215px" class="nolist_td">
												<div class="outside_inside wa">
													<a class="a insidea <#if doc.DOCUMENT_FILE_SUFFIX=='ppt'||doc.DOCUMENT_FILE_SUFFIX=='pptx'>isPPT</#if>" href="#" onclick="javascript:view('${doc.DOCUMENT_ID}','${doc.DOCUMENT_TITLE}')"; target="_self" title="${doc.DOCUMENT_TITLE}">
															<p class="inside">
																<img name="a" alt="${doc.DOCUMENT_TITLE!''}" src="" url="<@setPic doc middle/>">
																<b class="ic-${doc.DOCUMENT_FILE_SUFFIX}"></b>
															</p>
													</a>
												</div>
													<a class="a nolist_a" href="#" onclick="javascript:view('${doc.DOCUMENT_ID}','${doc.DOCUMENT_TITLE}')"; target="_self" title="${doc.DOCUMENT_TITLE}">
															<p class="inside_content">
																<#if doc.DOCUMENT_TITLE?length gt 12>
																	${doc.DOCUMENT_TITLE[0..12]}...
																<#else>
																	${doc.DOCUMENT_TITLE}	
																</#if>
															</p>
													</a>
												</td>
												<#if doc_index % 5 == 4>
													</tr>
												</#if>
												
										</#list>
										
										<#if tag_list?size ==0 >
											无相关记录!
										</#if>	
										</table>
										-->
										<!------------------缩略图部分内容----结束---------------------->
							</div>
										<!----------------缩略图宏部分---开始---------------------->
										<div class="nolist hidden mod hotRead">
											<div class="bd">
												<div class="mod classBox-big">
													<div class="gd-g classRow-big catalog-76-big">
														<#list tag_list as doc>
															<div class="gd-g-u gd-u-1-6-big classItem-big">
																<@setDocPic doc "-big"/>
															</div>
														</#list>
													</div>
												</div>
											</div>
										</div>
										<!----------------缩略图宏部分---结束---------------------->
						</div>
						<!-- index_1.html _PAGE_ -->
						<div class="page-nav page-center">
							<div class="page-inner">
								<div class="page-content">
								<!-- 如果当前页>1 显示上一页 -->
								   <#if _PAGE_.NOWPAGE gt 1>
								   <a
										href="/cms/${channel_serv}/${data.CHNL_ID}/index_${_PAGE_.NOWPAGE - 1 }.html"
										class="next">上一页&gt;</a>
								   </#if>
								   
								   <#assign start=1 />
								   <#assign end= _PAGE_.NOWPAGE+9 />
								   
								   <#if _PAGE_.NOWPAGE gte 11>
								     <#assign start = _PAGE_.NOWPAGE - 10 />
								   </#if>
								   
								   <#if end gte _PAGE_.PAGES >
								      <#assign end = _PAGE_.PAGES/>
								   </#if>
								   
								
									<#list start..end as t>
									<#if t == _PAGE_.NOWPAGE >
									<span class="cur no1">${t}</span>
									<#else>
									  <a
										href="/cms/${channel_serv}/${data.CHNL_ID}/index_${t}.html"
										class="no1">${t}</a>
									</#if>	
									</#list>
									
									<#if _PAGE_.NOWPAGE lt _PAGE_.PAGES>
										<a
										href="/cms/${channel_serv}/${data.CHNL_ID}/index_${_PAGE_.NOWPAGE + 1 }.html"
										class="next">下一页&gt;</a>
									</#if>
										<!-- 
										<a
										href=""
										class="last">尾页</a>
										 -->
								</div>
							</div>
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
		<!--------------引入底部搜索------------>
		<#include "/SY_COMM_WENKU/baiduwenku_bottom_search.ftl">
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