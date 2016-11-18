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
<link rel="stylesheet" type="text/css" href="/sy/comm/wenku/baidu_style_files/uploadBox_d6fe1324.css">
<link rel="stylesheet" type="text/css" href="/sy/comm/wenku/baidu_style_files/userList_6b1e06e8.css">
<link rel="stylesheet" type="text/css" href="/sy/comm/wenku/baidu_style_files/list_35637000.css">
<link rel="stylesheet" type="text/css" href="/sy/comm/wenku/baidu_style_files/childClassBlock_79b2739b.css">
<link rel="stylesheet" type="text/css" href="/sy/comm/wenku/baidu_style_files/cpmsg_b717a84f.css">
<link rel="stylesheet" type="text/css" href="/sy/comm/wenku/baidu_style_files/dataList_d8492a2c.css">
<link rel="stylesheet" type="text/css" href="/sy/comm/wenku/baidu_style_files/pager_1cd6d3a7.css">
<link rel="stylesheet" type="text/css" href="/sy/comm/wenku/css/style2.css" />
<link rel="stylesheet" type="text/css" href="/sy/comm/wenku/css/doclist-list.css" />
<script type="text/javascript" src="/sy/comm/wenku/baidu_style_files/base_75690991.js"></script>
<script type="text/javascript" src="/sy/comm/wenku/baidu_style_files/wenku.js"></script>
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

<!-- 二级 导航 -->
<#include "/SY_COMM_WENKU/baiduwenku_list_navigation.ftl">
<@list_navigation data/>
			
					
					
					 <@wenku_doclist_list debugName="获取文辑下数据" channelId="${data.CHNL_ID}" page="${page}" count="20"> 
						<div class="mod dataList">
							<ul class="docAlbumTab">
								<li><a href="<@chnlUrl data.CHNL_ID 1 />?selectMenu=${data.CHNL_ID}" class="Doclist" hidefocus="true">文档</a></li>
								<li class="selected"><a href="#" class="Doclist">文辑</a></li>
								<div style="margin-left:630px;margin-bottom:10px;">
									<div id ='showtype' class='pos'>
									</div>
								</div>
							</ul>
							<div class="tab-line"></div>

							<div id="tabContent" class="tabContent">
									<table class="list" id="docList" cellpadding="0" cellspacing="0" width="100%">
										<tbody>
											<tr>
												<th width="300">文辑名称</th>
												<th width="126">发布部门</th>
												<th width="100">创建者</th>
												<th width="70">文档数量</th>
												<!-- 
												<th width="70"><a title="按下载量排序"
													href="">下载量</a></th>
												 -->
													
												<th width="70">
												<!--	<a title="按创建时间排序" href="">  -->
														创建时间
												<!--	</a>  -->
												</th>
											</tr>
								
										<#list tag_list as doc>
												<#if doc_index % 2 = 0 >
												<tr>
												<#else>
												<tr class="hasb">
												</#if>
													<td>
													<!-------------单个文辑的链接--开始------------------->
													<a href="#" url="/cms/SY_COMM_WENKU_DOCLIST/${doc.LIST_ID}/index_1.html" class="list_a list_a_tab"
														target="_self" title="${doc.LIST_TITLE}">
														<#if doc.LIST_TITLE?length gt 30>
														${doc.LIST_TITLE[0..30]}...
														<#else>
														${doc.LIST_TITLE}	
														</#if>	
													</a>
													
													<!--------------单个文辑的链接--结束------------------>
													</td>
													<td class="un">
															${doc.PUBLISH_DEPT__NAME}
													</td>
													<td class="un"><div>
															<a href="#"	class="Author logSend" target="_self">${doc.S_UNAME}</a>
														</div></td>
													<td><span>${doc.LIST_DOCUMENT_COUNTER}篇</span></td>
													<td><span>
													${doc.S_MTIME[0..10]}
													</span></td>
												</tr>
										</#list>
									
										
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
										</tbody>
								
									</table>
							</div>
							
								<#if tag_list?size ==0 >
											无相关记录!
								</#if>	
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
					</@wenku_doclist_list>
						 
					</div>
					
	<!-----------begin aside----------------->				
			<div style="height:38px;width:auto;background-color:#FFFFFF;"></div>
			<div id="aside" class="aside" style="">
				<!----- my create ----->
				<style>
					.upbox .content {
					    background-color: #FBFBFF;
					    padding: 19px 10px 16px;
					}
					.upbox {
					    background: none repeat scroll 0 0 #F3F8FC;
					    border: 1px solid #D5E8F5;
					    padding: 3px;
					}
					.upbox .album-btn {
					    background: url("/sy/comm/wenku/img/album_btns.gif") no-repeat scroll 0 0 transparent;
					    display: block;
					    height: 42px;
					    margin: 0 auto;
					    width: 175px;
					}
					a {
					    color: #2D64B3;
					    text-decoration: none;
					}
					.hot_wenji_div{
						height: 20px;
						width: 160px;
						}
				</style>
		<!-------------------------创建文辑的按钮--开始-------------------------------->
				<div class="upbox">
					<div class="content">
						<a href="javascript:createDoclist();" class="album-btn albumbtn"></a>
					</div>
				</div>
		<!--------------------------创建文辑按钮--结束------------------------------->
		<!-----------------热门文辑--开始------------------------------->
	    	<div class="mod fenyuejinbu userListMod">
	      		<div class="xu_bor6">
		            <div class="tit11">
		                                                   热门文辑
		            </div>
		            <ul class="list13">
		            	<@wenku_doclist_readTop debugName="热门文辑"  page="${page}" count="5"> 
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
						</@wenku_doclist_readTop>			                
		            </ul>
		        </div>
			</div>
          <!-----------------热门文辑--结束------------------------------->
          <!-----------------最新文辑--开始------------------------------->
	    	<div class="mod fenyuejinbu userListMod">
	      		<div class="xu_bor6">
		            <div class="tit11">
		                                                   最新文辑
		            </div>
		            <ul class="list13">
		            	<@wenku_doclist_list debugName="最新文辑"  page="${page}" count="5">
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
						</@wenku_doclist_list>			                
		            </ul>
		        </div>
			</div>
          <!-----------------最新文辑--结束------------------------------->
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
		
		//去除a标签点击后的边框
		$('a').bind('focus',function(){
			if(this.blur){
				this.blur();
			}
		});
		
	});
	
	
</script>
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

<!---------------用来控制"创建文辑"的图片的位置--开始---------->
<script>
jQuery(document).ready(function(){
	if(jQuery("#threeChannel").length == 0){
		jQuery("#aside").css("margin-top","16px");
	}else{
		jQuery("#aside").css("margin-top","-10px");
	}
});
</script>
<!---------------用来控制"创建文辑"的图片的位置--开始---------->
</body>

</html>