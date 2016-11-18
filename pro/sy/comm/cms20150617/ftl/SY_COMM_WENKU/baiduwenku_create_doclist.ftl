<!DOCTYPE html>
<!--STATUS OK-->
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta http-equiv="X-UA-Compatible" content="IE=Edge">
<title>全部文档</title>
<script type="text/javascript"> var FireFlyContextPath =''; </script>
 
<#include "/SY_COMM_WENKU/config_constant.ftl">
<link rel="stylesheet" type="text/css" href="/sy/comm/wenku/baidu_style_files/framework_48fb02e1.css">
<link rel="stylesheet" type="text/css" href="/sy/comm/wenku/css/baiduwenku-create-wenji.css">
<link rel="stylesheet" type="text/css" href="/sy/base/frame/plugs/jquery-ui/rh.jquery-ui.min.css">
<script type="text/javascript" src="/sy/base/frame/jquery-1.8.2.min.js"></script>
<script type="text/javascript" src="/sy/base/frame/plugs/jquery-ui/rh.jquery-ui.min.js"></script>
<script type="text/javascript" src="/sy/comm/cms/js/wenku.js"></script>
<script type="text/javascript" src="/sy/base/frame/constant.js"></script>
<script type="text/javascript" src="/sy/base/frame/tools.js"></script>
<script type="text/javascript" src="/sy/base/frame/platform.js"></script>
<script type="text/javascript" src="/sy/comm/wenku/js/baiduwenku_create_doclist.js"></script>
</head>
<body>
<div id="body">	
<!-------------------页面开始----------------------->
<div id="doc" class="page">
	<#include "/SY_COMM_WENKU/baiduwenku_header.ftl">			
	<#include "/SY_COMM_WENKU/baiduwenku_navigation.ftl">
  <div id="bd">
    <div class="bd-wrap">
      <div class="body">
        <div class="main">
          <!--------------------前端导航--开始----------------------->
          <div class="list_a_div">
          	<span class="list_a_span">
          		<a href="<@chnlUrl root_channel_id 1 />?selectMenu=first">软虹文库</a> 〉文辑
          	</span>
          </div>
          <!--------------------前端导航--结束----------------------->
          <div class="album-boxes">
            <form id="doclist-form" method="post" action="">
              <div id="info-part">
                <dl class="clearfix">
                  <dt><span class="required">*</span> 标题</dt>
                  <dd class="clearfix">
                    <input id="list-title" name="LIST-TITLE" class="normal-input" type="text">
                    <span class="tips" id="title-tip"></span> 
                  </dd>
                </dl>
                <dl class="clearfix">
                  <dt>介绍</dt>
                  <dd class="clearfix">
                    <textarea id="describe" name="LIST_DESCRIPTION" class="normal-input" placeholder="简要的介绍资料的主要内容，以获得更多的关注"></textarea>
                    <span class="tips" id="describe-tip"></span> </dd>
                </dl>
                <dl class="clearfix">
                  <dt>关键词</dt>
                  <dd class="clearfix">
                    <input type="text" name="LIST_KEYWORD" id="keyword" class="normal-input" placeholder="选填，多个关键词用逗号分隔" >
                    <span class="tips"></span> 
                  </dd>
                </dl>
                <dl class="clearfix">
                  <dt>发布部门</dt>
                  <dd class="clearfix">
                    <p class="cate-line">
                    	<a href="###" class="cate-btn" id="dept-btn">请点击选择部门</a>
						<input type="hidden" name="PUBLISH_DEPT" id="publish_dept"/>
						<span class="tips"></span> 
                    </p>
                  </dd>
                </dl>
                <dl class="clearfix">
                  <dt><span class="required">*</span> 分类</dt>
                  <dd class="clearfix">
                    <p class="cate-line p-add-doc">
                    	<a href="###" class="cate-btn" id="cate-btn">请点击选择分类</a>
						<input type="hidden" name="LIST_CHNL" id="list_chnl"/>
						<a href="###" class="doc-add-more" id="add-more">添加文档</a>
                    </p>
                    <span class="tips"></span> 
                  </dd>
                </dl>
                <dl class="clearfix document-dl">
                   <dt></dt>
                   <dd class="clearfix">
                   	<!------------添加文档---------------->
	                <div id="document-part">
					    <div class="bd" id="list-doc">
						    <div class="list-doc-head">
						    	<div class="list-heading">我的文档</div>
						        <div class="doc-common doc-title">
						                          文件名称
						        </div>
						        <div class="doc-common doc-size">
						           	文件大小(MB)
						        </div>
						        <div class="doc-common doc-opt">
						           	操作
						        </div>
						    </div>
						    <ul>
						        <li class="list-doc-content">
						         
						        </li>
						    </ul>
						</div>
						<!--------------end of my-document---------------------->
                   </dd>
                </dl>
                <dl>
                  <dt></dt>
                  <dd>
					    <a href="###" class="bt ui-btn ui-btn-26-green" id="submit-btn">
					        <b class="btc"><b class="btText">提交</b></b>
					    </a>
					    <span id="submit-tip" class="tips">
					    </span>
					</dd>
                </dl>
				</div>
              </div>
            </form>
          </div>
        </div>
      </div>
    </div>
  </div>
  <!----------------foot开始--------------------->
  <#include "/SY_COMM_WENKU/baiduwenku_footer.ftl">
  <!----------------foot结束--------------------->
</div>
</div>
</body>
</html>
