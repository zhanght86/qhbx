<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>他的文库</title>
<link rel="stylesheet" type="text/css" href="/sy/comm/wenku/css/userwenku_center.css"/>
<script type="text/javascript" src="/sy/base/frame/jquery-1.8.2.min.js"></script>
<script type="text/javascript" src="/sy/comm/wenku/js/userwenku_center.js"></script>
<script type="text/javascript" src="/sy/comm/cms/js/wenku.js"></script>
</head>
<body>
<div class="wk-wrapper">

  <div class="wk-container">
    <div class="wk-main">
      <div class="wk-m-content">
        <div class="wk-user-info"> 
          <span class="act"> <!--<a href="#" title="查看更多" target="_blank"> 查看更多 <span class="more"> &gt;&gt; </span> </a>--> </span>
          <h2 class="block-title">他在文库</h2>
          <div class="basic-info box">
            <div class="media">
              <div class="title"> 基本信息： </div>
            </div>
            
            <div class="wk-count">
            <@wenku_user_info  debugName="基本信息" userId="${who}">
              <ul>
                <li class="first"> 文档数<b>${publicDocCount}</b> </li>
                <li> 文档被下载<b>${downloadCount}</b> </li>
                <li> 文辑 <b> ${doclistCount} </b> </li>
              </ul>
              </@wenku_user_info>
           </div>
           
          </div>
        </div>
      </div>
       <div class="wk-grid">
        	<ul class="tabControl" id="tabControl">
                <li class="tab-item current">
                    <a href="#js-docs" title="他的文档"> 他的文档 </a>
                </li>
                <li class="line">
                    |
                </li>
                <li class="tab-item">
                    <a href="#js-doclist" title="他的文辑"> 他的文辑 </a>
                </li>
                <li class="slide" style="left:0;">
                    <div class="icon">
                    </div>
                </li>
                <li class="hover" id="hover" style="left: 0px;">
                </li>
            </ul>
            <div id="js-docs">
            	<div class="wk-grid-header">
                    <div class="nomal w250">文档名称 </div>
                    <div class="nomal w140">用户评价</div>    
                    <div class="nomal w60 tc">浏览量</div>
                    <div class="nomal w60 tc">下载量</div>
                    <div class="nomal w88 tr">上传时间</div>
                </div>
                <@wenku_list debugName="他的文档" userId="${who}" page="${page}" count="20"> 
            	<ul class="tabContent">
                    <li>
                       <#list tag_list as list>
                       <div class="wk-grid-list">
                            <div class="nomal w250">
                            	<a href="javascript:view('${list.DOCUMENT_ID}', '${list.DOCUMENT_TITLE}');">
	                        		<#if list.DOCUMENT_TITLE?length gt 20>
		                        		${list.DOCUMENT_TITLE[0..20]}...
		                        	<#else>
		                        		${list.DOCUMENT_TITLE}
		                        	</#if>
                            	</a>
                            </div>
                            <div class="nomal w140">
                            	<div class="wk-rate">
                                 	<div class="wk-rate-tu" style="width:${(list.DOCUMENT_AVG_SCORE!0)?number * 100/5}%;"></div>
                                 	<!--<div class="wk-rate-count">人评</div>-->
                                </div>
                            </div>    
                            <div class="nomal w60 tc">${list.DOCUMENT_READ_COUNTER!0}</div>
                            <div class="nomal w60 tc">${list.DOCUMENT_DOWNLOAD_COUNTER}</div>
                            <div class="nomal w88 tr">${list.S_CTIME[0..10]}</div>
                       </div> 
                       </#list> 
                       <div class="ui-pager pager-center">
                            <div class="pager">
                                <div class="pager-inner">
                                <#if _PAGE_.NOWPAGE gt 0>
                                   <#if _PAGE_.NOWPAGE gt 1>
									   <a href="/cms/SY_COMM_CMS_TMPL/38NrX0dU9dLFVCHSDw0pjYK/index_${_PAGE_.NOWPAGE - 1 }.html?who=${who}&t=0" 
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
										  <a href="/cms/SY_COMM_CMS_TMPL/38NrX0dU9dLFVCHSDw0pjYK/index_${t}.html?who=${who}&t=0" 
										  	class="no1">${t}</a>
										</#if>	
									</#list>
									<#if _PAGE_.NOWPAGE lt _PAGE_.PAGES>
										<a href="/cms/SY_COMM_CMS_TMPL/38NrX0dU9dLFVCHSDw0pjYK/index_${_PAGE_.NOWPAGE + 1 }.html?who=${who}&t=0" 
											class="next">下一页&gt;</a>
									</#if> 
								</#if> 	
                                </div>
                            </div>
                        </div>
                    </li>
                </ul>
                </@wenku_list>
            </div>
            <!-------------------end of js-doc------------------------>
             <div id="js-doclist" style="display:none">
            	<div class="wk-grid-header">
                    <div class="nomal w250">文辑名称 </div>
                    <div class="nomal w140">用户评价</div>    
                    <div class="nomal w60 tc">浏览量</div>
                    <div class="nomal w60 tc">收藏量</div>
                    <div class="nomal w88 tr"> 创建时间</div>
                </div>
                <@wenku_doclist_list debugName="他的文辑" userId="${who}" page="${page}" count="20">
            	<ul class="tabContent">
                    <li>
                       <#list tag_list as list>
                       <div class="wk-grid-list">
                            <div class="nomal w250">
                            	<a href="javascript:viewDoclist('${list.LIST_ID}','${list.LIST_TITLE}');">
                            		<#if list.LIST_TITLE?length gt 30>
										${list.LIST_TITLE[0..30]}...
									<#else>
										${list.LIST_TITLE}	
									</#if>	
                            	</a>
                            </div>
                            <div class="nomal w140">
                            	<div class="wk-rate">
                                 	<div class="wk-rate-tu" style="width:${(list.LIST_SCORE_AVG!0)?number * 100/5}%;"></div>
                                 	<div class="wk-rate-count">:${(list.LIST_SCORE_AVG!0)}人评</div>
                                </div> 
                            </div> 
                            <div class="nomal w60 tc">${list.LIST_READ_COUNTER!0}</div>
                            <div class="nomal w60 tc">16</div>
                            <div class="nomal w88 tr">${list.S_MTIME[0..10]}</div>
                       </div> 
                       </#list> 
                       <div class="ui-pager pager-center">
                            <div class="pager">
                                <div class="pager-inner">
                                <#if _PAGE_.NOWPAGE gt 0>
                                   <#if _PAGE_.NOWPAGE gt 1>
									   <a href="/cms/SY_COMM_CMS_TMPL/38NrX0dU9dLFVCHSDw0pjYK/index_${_PAGE_.NOWPAGE - 1 }.html?who=${who}&t=1" 
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
										   <a href="/cms/SY_COMM_CMS_TMPL/38NrX0dU9dLFVCHSDw0pjYK/index_${t}.html?who=${who}&t=1" class="no1">${t}</a>
										</#if>	
									</#list>
									<#if _PAGE_.NOWPAGE lt _PAGE_.PAGES>
										<a href="/cms/SY_COMM_CMS_TMPL/38NrX0dU9dLFVCHSDw0pjYK/index_${_PAGE_.NOWPAGE + 1 }.html?who=${who}&t=1" 
											class="next">下一页&gt;</a>
									</#if> 
								 </#if>
                                </div>
                            </div>
                        </div>
                    </li>
                </ul>
                </@wenku_doclist_list>
            </div>
            <!-------------------end of js-doclist------------------------>
         </div>
    </div>
    <div class="wk-right-side">
      <div class="wk-rs-content">
        <@user debugName="用户信息" userId="${who}">
        <h2 class="wk-rs-title">他的文库形象</h2>
        <div class="wk-profile">
          <div class="wk-profile-name">${user.USER_NAME}</div>
          <div class="wk-profile-photo"><img src="${user.USER_IMG}"></div>
        </div>
        </@user>
      </div>
    </div>
  </div>
</div>
</body>
</html>
