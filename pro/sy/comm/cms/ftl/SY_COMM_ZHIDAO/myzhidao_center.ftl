<!DOCTYPE html>
<!--STATUS OK-->
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta http-equiv="X-UA-Compatible" content="IE=Edge">
<meta name="description" content="在线互动式文档分享平台，在这里，您可以和千万网友分享自己手中的文档，全文阅读其他用户的文档 ">
<title>全部文档</title>
<#include "/SY_COMM_ZHIDAO/config_constant.ftl">
<link rel="stylesheet" type="text/css" href="/sy/comm/zhidao/css/myzhidao_center.css">
<script type="text/javascript"> var FireFlyContextPath =''; </script>
<script type="text/javascript" src="/sy/base/frame/jquery-1.8.2.min.js"></script>
<script type="text/javascript" src="/sy/base/frame/constant.js"></script>
<script type="text/javascript" src="/sy/base/frame/tools.js"></script>
<script type="text/javascript" src="/sy/base/frame/platform.js"></script>
<script type="text/javascript" src="/sy/comm/cms/js/zhidao.js"></script>
<script type="text/javascript" src="/sy/comm/zhidao/js/myzhidao_center.js"></script>
<!---------------返回顶部的Js/CSS------开始--------------->
<link rel="stylesheet" type="text/css" href="/sy/comm/zhidao/css/zd-goto-top.css">
<script type="text/javascript" src="/sy/comm/zhidao/js/zd-goto-top.js"></script>
<!---------------返回顶部的Js/CSS------结束--------------->
</head>
<body>
<div class="zd-container">	
<div class="zd-grid-770 zd-right">
      <div class="header">
        <h2>我的知道</h2>
      </div>
      <h3><a href="#" class="more"></a>基本情况</h3>
      <div class="base-info clearfix">
      	<ul>
        	<li>
            	<span>提问数</span>
                <i class="js-question">0</i>
            </li>
            <li>
            	<span>回答数</span>
                <i class="js-answer">0</i>
            </li>
            <li>
            	<span>我的积分</span>
                <i class="js-integral">0</i>
            </li>
        </ul>
      </div>
   	 
     <div class="zd-grid">
     	<h3><a href="#" class="more" onclick="javascript:openAnswer();">更多</a>我的回答</h3>
     	<div class="zd-grid-header">
        	<div class="nomal wp55 pl">问题标题</div>
            <div class="nomal wp10">评论次数</div>
            <div class="nomal wp10">支持数量</div>
            <div class="nomal wp10">反对数量</div>
            <div class="nomal wp10">回答时间</div>
        </div>
        <ul>
        	<li id="js-answ-list"></li>
        </ul>
     </div>
     
     <div class="zd-grid">
    	<h3><a href="#" class="more" onclick="javascript:openMyAsk();">更多</a>我的提问</h3>
     	<div class="zd-grid-header">
        	<div class="nomal wp55 pl">问题标题</div>
            <div class="nomal wp40">更新时间</div>
        </div>
        <ul>
        	<li id="js-ques-list"></li>
        </ul>
     </div>
    </div>
  </div>
<!------引入footer-------->
<!---------------返回顶部的div------开始--------------->
<div id="zd-goto-top"></div>
<!---------------返回顶部的div------结束--------------->
</body>

</html>