<!DOCTYPE html>
<!--STATUS OK-->
<html style='background:#fff'>
<head>
<#include "/SY_COMM_ZHIDAO/config_constant.ftl">
<script type="text/javascript"> var FireFlyContextPath =''; </script>
<script type="text/javascript" src="/sy/base/frame/jquery-1.8.2.min.js"></script>
<script type="text/javascript" src="/sy/base/frame/constant.js"></script>
<script type="text/javascript" src="/sy/base/frame/tools.js"></script>
<script type="text/javascript" src="/sy/base/frame/platform.js"></script>
<script type="text/javascript" src="/sy/comm/cms/js/zhidao.js"></script>
<script type="text/javascript" src="/sy/comm/zhidao/js/user_img.js"></script>
<link rel="stylesheet" type="text/css" href="/sy/theme/default/common.css">
<!---------------返回顶部的Js/CSS------开始--------------->
<link rel="stylesheet" type="text/css" href="/sy/comm/zhidao/css/zd-goto-top.css">
<script type="text/javascript" src="/sy/comm/zhidao/js/zd-goto-top.js"></script>
<!---------------返回顶部的Js/CSS------结束--------------->
		<meta http-equiv="X-UA-Compatible" content="IE=Edge">
		<meta http-equiv="content-type" content="text/html; charset=UTF-8">
		<title>
			软虹知道 - 企业互动问答平台
		</title>
		<meta name="description" content="软虹知道是 基于搜索的互动式知识问答分享平台。用户可以根据自身的需求，有针对性地提出问题；同时，这些答案又将作为搜索结果，满足有相同或类似问题的用户需求。">
		<link rel="stylesheet" type="text/css" href="/sy/comm/zhidao/baidu_style_files/module_fe8dd525.css">
		<link rel="stylesheet" type="text/css" href="/sy/comm/zhidao/baidu_style_files/module_c595d1cf.css">
		<link rel="stylesheet" type="text/css" href="/sy/comm/zhidao/baidu_style_files/module_2d32dc49.css">
		<link rel="stylesheet" type="text/css" href="/sy/comm/zhidao/baidu_style_files/bdimBubble.css">
		<link rel="stylesheet" type="text/css" href="/sy/comm/wenku/baidu_style_files/framework_7d9033ac.css">
		<link rel="stylesheet" type="text/css" href="/sy/comm/wenku/baidu_style_files/module_index_777ae95d.css">
		<link rel="stylesheet" type="text/css" href="/sy/comm/wenku/baidu_style_files/index_50da8c9a.css">
		<link rel="stylesheet" type="text/css" href="/sy/comm/zhidao/css/zhidao_index.css">
		<link rel="stylesheet" type="text/css" href="/sy/comm/zhidao/css/zhidao_specialist.css">
		<script type="text/javascript" src="/sy/comm/zhidao/js/zhidao_index.js"></script>
		
		</script>
		<style>
		.prof-thanks-ctn {
		    float: left;
		    height: 253px;
		    margin-right: 67px;
		    position: relative;
		    width: 194px;
		}
		.prof-thanks-info {
		    background: url("/sy/comm/zhidao/baidu_style_files/pgc-mask.png") no-repeat scroll 0 0 transparent;
		    display: block;
		    height: 190px;
		    left: 0;
		    position: absolute;
		    top: 0;
		    width: 255px;
		    z-index: 9;
		}
		
		.prof-thanks-info p {
		    color: #999999;
		    font-family: 'Microsoft YaHei';
		    font-size: 12px;
		    padding: 9px 12px 0;
		}
		.prof-thanks-info p.prof-thanks-txt {
		    color: #333333;
		    font-size: 14px;
		    line-height: 19px;
		    padding: 15px 20px 0 130px;
		    text-indent: 1em;
		    width:120px;
		    height:180px;
		    
		}
		.prof-thanks-info p {
		    color: #999999;
		    font-family: 'Microsoft YaHei';
		    font-size: 12px;
		    padding: 9px 12px 0;
		}
		
		.prof-thanks-info p.prof-thanks-pro {
		    bottom: 12px;
		    color: #333333;
		    position: absolute;
		    text-align: center;
		    width: 120px;
		    top:145px;
		}
		.prof-thanks-info p {
		    color: #999999;
		    font-family: 'Microsoft YaHei';
		    font-size: 12px;
		    padding: 9px 12px 0 20px;
		}
		
		.prof-thanks-info p.prof-thanks-pro strong {
		    margin-right: 5px;
		}
		
		.prof-thanks-ctn img {
		    height: 96px;
		    left: 35px;
		    position: absolute;
		    top: 53px;
		    width: 96px;
		    z-index: 0;
		}
		fieldset, img {
		    border: 0 none;
		}
		.img_show{
			display:block;
		}	
		.img_hide{
			display:none;
		}
		
		</style>
		
		<!-------------显示问答的CSS--开始---------------->
		<style>
		#aq {
		    -moz-box-orient: vertical;
		    display: inline-block;
		    float: left;
		    margin-left: 37px;
		    vertical-align: middle;
		    width: 425px;
		}
		#aq dt {
		    background: url("/sy/comm/zhidao/baidu_style_files/combine_0fa1fdab.png") no-repeat scroll 2px -360px transparent;
		    padding: 11px 0 0 25px;
		}
		#aq dt a {
		    color: #000000;
		    font-family: "Microsoft YaHei";
		    font-size: 16px;
		    font-weight: bold;
		}
		#aq dd.info-content {
		    color: #666666;
		    font-family: SimSun;
		    font-size: 14px;
		    padding-top: 8px;
		    width: 410px;
		}
		#aq dd.info-content .answer {
		    background: url("/sy/comm/zhidao/baidu_style_files/combine_0fa1fdab.png") no-repeat scroll 2px -420px transparent;
		    float: left;
		    height: 20px;
		    margin: 2px 7px 0 0;
		    width: 20px;
		}
		#aq dd.info-content {
		    color: #666666;
		    font-family: SimSun;
		    font-size: 14px;
		}
		#aq dd.info-user {
		    color: #999999;
		    font-size: 12px;
		}
		.h4_css{
			font-family:"微软雅黑";
			font-size:18px;
			color: #2D64B3;
		}
		.h6_css{
			padding-top:5px;
			padding-bottom:5px;
			padding-left:5px;
			color:#999999;
			font-size:13px;
		}
		.h5_css{
			color:#999999;
			font-size:12px;
			float:right;
		}
		</style>
		
	</head>
	<body class="layout-center">
		<div class='layout-center-div'>
		<div class="line">
			<!-- userbar -->
		</div>
<!--------------search---------------------->				
		<#include "/SY_COMM_ZHIDAO/zhidao_search.ftl">
		<#include "/SY_COMM_ZHIDAO/zhidao_navigation.ftl">
		
		
		<!------------------图片轮转效果-----开始------------------>
	
		<!----------------相关的CSS代码------开始---------------------->
		<style type="text/css">
		.tangram-carousel{overflow:hidden;margin:0 auto;}.tangram-carousel-item{float:left;height:500px;}#content-wrap{background:#F2F2F2;overflow:hidden;}#banner{overflow:hidden;position:relative;height:300px;}#banner .banner-img{display:block;position:absolute;height:500px;top:0;right:0;}#banner .show{z-index:1;}.banner-img span{display:block;height:500px;}.banner-tab{width:639px;height:57px;border-top-left-radius:5px;border-top-right-radius:5px;margin:-70px auto 0 auto;padding-top:13px;position:relative;z-index:2;overflow:hidden;}.banner-tab-wrap{position:relative;top:70px;width:639px;}.banner-tab-bg{position:absolute;top:-13px;left:0;width:590px;height:70px;background:#000;opacity:.4;filter:alpha(opacity=40);border-top-left-radius:5px;border-top-right-radius:5px;}.banner-tab-inner{margin:0 auto;overflow:hidden;}.banner-item{float:left;height:40px;width:40px;border:1px solid #5d5d5d;margin:1px 11px;opacity:.3;filter:alpha(opacity=30);cursor:pointer;position:relative;}.banner-item:hover{border:1px solid #5d5d5d;opacity:1;filter:alpha(opacity=100);}.banner-tab .selected{border:2px solid #444!important;margin:0 10px;opacity:1;filter:alpha(opacity=100);}.banner-item img{height:40px;width:40px;}
		#body h1{font-size:30px;font-family:"Microsoft Yahei","SimHei";_font-family:"SimHei";font-weight:normal;color:#398D03;line-height:30px;margin:0;}
		#body h2{color:#666;line-height:22px;font-size:22px;font-weight:normal;font-family:"Microsoft Yahei",Arial;_font-family:Arial;margin:0;}#body a.more-enterprise{color:#999;font-size:12px;margin-left:20px;font-family:"宋体";vertical-align:middle;}#body h3{color:#555;line-height:20px;_line-height:22px;font-size:18px;font-weight:normal;font-family:"Microsoft Yahei",Arial;_font-family:Arial;margin:0;}.icon{display:inline-block;background:url(http://img.baidu.com/img/iknow/openiknow/home/icons.gif) no-repeat;}.answer{background-position:0 -40px;width:10px;height:10px;margin-right:1px;*margin-right:3px;vertical-align:middle;}.good{background-position:0 -20px;width:10px;height:10px;margin-right:1px;*margin-right:3px;vertical-align:middle;}.answer-gray{background-position:-20px -40px;width:10px;height:10px;margin-right:1px;*margin-right:3px;vertical-align:middle;}.good-gray{background-position:-20px -20px;width:10px;height:10px;margin-right:1px;*margin-right:3px;vertical-align:middle;}.answer-count,.good-count{color:#999;font-size:12px;margin-right:15px;vertical-align:middle;_position:relative;_top:-3px;}img{border:none;}#logo-section{height:443px;margin-top:60px;}.logo-tab{margin-top:25px;font-family:"Microsoft Yahei",Arial;_font-family:Arial;font-size:16px;color:#888;}.logo-tab .logo-tab-item{padding:5px 10px;border:1px solid #F2F2F2;cursor:pointer;*float:left;}.logo-tab .logo-tab-item:hover{color:#444;}.logo-tab .selected,.logo-tab .selected:hover{font-weight:bold;color:#555;border-color:#D8D8D8;border-radius:2px;background:#FAFAFA;cursor:default;}.logo-tab .logo-more{float:right;color:#888;text-decoration:none;}.logo-tab .logo-more:hover{text-decoration:underline;}.logo-wall{clear:both;margin-top:21px;display:inline-block;}.logo-wall .main{float:left;background:#fff;border:1px solid #EBEBEB;border-bottom-color:#EAEAEA;height:343px;position:relative;}.logo-wall .main-item{width:156px;height:326px;padding:8px;border-bottom:1px solid #DADADA;}.logo-wall .main img{display:inline-block;}#body .logo-wall .main h2{color:#555;line-height:15px;font-size:15px;font-weight:bold;font-family:"Microsoft Yahei",Arial;_font-family:Arial;margin:15px 0 10px 0;}#body .logo-wall .main a{display:block;text-decoration:none;}#body .logo-wall .main a:hover p{color:#6AAF14;}.logo-wall .main p{color:#666;font-size:12px;font-family:宋体;line-height:20px;}.logo-wall .main .info{font-size:12px;position:absolute;bottom:10px;}.logo-wall .wall{float:right;width:826px;_width:822px;height:100%;margin-top:-9px;}.logo-wall .play{opacity:0;filter:alpha(opacity=0);position:relative;top:-100px;}.logo-wall .play .img-wrap{opacity:0;filter:alpha(opacity=0);}.logo-wall .wall-item{float:left;border:1px solid #EBEBEB;border-bottom-color:#EAEAEA;position:relative;margin-left:10px;_margin-left:9px;margin-top:9px;height:107px;}.logo-wall .wall-item a{display:inline-block;padding:3px;background:#fff;height:100px;border-bottom:1px solid #DADADA;color:#fff;text-decoration:none;cursor:pointer;overflow:hidden;}.logo-wall .text-bg{display:none;position:absolute;top:0;left:0;height:86px;width:86px;background:#000;opacity:.8;filter:alpha(opacity=80);padding:10px;}.logo-wall .hover .text-bg,.logo-wall .hover .wall-item-text{display:block;}.logo-wall .wall-item-text{display:none;position:absolute;top:0;left:0;height:94px;width:84px;border:1px solid #333;padding:5px 10px;}.logo-wall .wall-item-text .name{color:#fff;font-size:14px;_font-size:12px;font-family:"Microsoft Yahei",Arial;_font-family:Arial;font-weight:bold;margin:0;position:relative;}.logo-wall .wall-item-text .intro{color:#fff;font-size:12px;margin:0;margin-top:10px;position:relative;}.logo-wall .wall-item .img-wrap{display:inline-block;height:100px;width:100px;overflow:hidden;position:relative;}.logo-wall .wall-item .left{position:absolute;top:0;left:-100px;}.logo-wall .wall-item .right{position:absolute;top:0;left:100px;}.logo-wall .wall-item .top{position:absolute;top:-100px;left:0;}.logo-wall .wall-item .bottom{position:absolute;top:100px;left:0;}#feed-section{float:left;width:645px;height:544px;margin:85px 75px 30px 0;}.feed-box{height:460px;overflow:hidden;padding-top:20px;margin-top:10px;position:relative;}.feed-top{background:url(http://img.baidu.com/img/iknow/openiknow/home/fade.png) 0 0;_background:none;_filter:progid:DXImageTransform.Microsoft.AlphaImageLoader(enabled=true,sizingMethod=crop,src="http://img.baidu.com/img/iknow/openiknow/home/feed-top.png");width:645px;height:20px;position:absolute;top:0;left:0;z-index:10;}.feed-bottom{background:url(http://img.baidu.com/img/iknow/openiknow/home/fade.png) 0 -40px;_background:none;_filter:progid:DXImageTransform.Microsoft.AlphaImageLoader(enabled=true,sizingMethod=crop,src="http://img.baidu.com/img/iknow/openiknow/home/feed-bottom.png");width:645px;height:30px;position:absolute;bottom:0;left:0;z-index:10;}#feed-wrap{position:relative;top:-100px;height:560px;}.feed-item{margin:30px 0;height:40px;font-size:12px;}.feed-item img{float:left;border:1px solid #DADADA;margin-right:15px;}.feed-item .feed-content{float:left;color:#555;}.feed-item .feed-content .name{font-weight:bold;font-size:14px;margin-right:10px;text-decoration:none;color:#555;}.feed-item .feed-content .name:hover{text-decoration:underline;}.feed-item .feed-content a{color:#555;text-decoration:none;}.feed-item .feed-content a:hover{text-decoration:underline;}.feed-item .feed-content .answer{display:inline-block;background-position:-60px 0;width:19px;height:17px;margin:0;margin-bottom:-4px;margin-left:5px;vertical-align:baseline;}.feed-item .feed-content .ask{display:inline-block;background-position:-40px 0;width:19px;height:17px;margin-bottom:-4px;margin-left:5px;}.feed-item .feed-content p{color:#999;margin:0;margin-top:10px;*margin-top:8px;_margin-top:6px;}.feed-item .feed-content p a{color:#999;}.feed-item .time{float:right;color:#aaa;margin-top:13px;line-height:15px;height:15px;}.clock{background-position:-60px -20px;height:12px;width:12px;vertical-align:top;margin-right:5px;}#rank-section{float:left;width:244px;height:544px;margin:85px 0 30px 0;font-size:12px;padding-left:35px;border-left:1px solid #E7E7E7;}#rank-section .number{float:left;margin-right:18px;font-size:16px;font-family:Arial;font-weight:bold;color:#AAA;line-height:16px;height:16px;}#rank-section .top .number{color:#F2A628;}#rank-section .first .number{font-size:32px;font-weight:normal;line-height:32px;height:32px;margin-left:-3px;margin-right:12px;}#rank-section .info{float:left;}#rank-section .first .info{width:135px;}#rank-section .top-intern .info{width:auto;}.top-plat .rank-item{margin-top:15px;*margin-top:18px;display:inline-block;width:100%;}.top-plat .first{margin-top:20px;}.top-plat .rank-item img{float:left;border:1px solid #DADADA;margin-right:15px;}.top-plat .name{color:#666;font-weight:bold;display:inline-block;line-height:12px;*line-height:14px;height:12px;text-decoration:none;}.top-plat .name:hover{text-decoration:underline;}.top-plat .first .name{font-size:14px;line-height:14px;*line-height:16px;height:14px;margin-bottom:10px;}.top-plat .rank-item .level{display:inline-block;width:14px;height:16px;background:url(http://img.baidu.com/img/iknow/openiknow/home/small-level.png);margin-bottom:-5px;margin-left:5px;vertical-align:top;}.top-plat .rank-item .info{height:30px;}.top-plat .rank-item .info div{height:12px;color:#999;margin-top:2px;}.top-intern{margin-top:35px;}.top-intern .fans-item{margin-top:5px;*margin-top:7px;display:inline-block;width:100%;}.top-intern .fans-item .info{color:#999;}.top-intern .name{color:#666;font-weight:bold;display:inline-block;line-height:16px;height:16px;text-decoration:none;}.top-intern .name:hover{text-decoration:underline;}.top-intern .first{margin-top:20px;}.top-intern .first .name{margin-bottom:9px;}.top-intern .answer-count{margin-top:5px;margin-bottom:7px;}.top-intern .name a{color:#666;}.top-intern a{color:#999;text-decoration:none;}.top-intern a:hover{text-decoration:underline;}.top-plat .rank-item .level-1{background-position:0 0;}.top-plat .rank-item .level-2{background-position:0 -18px;}.top-plat .rank-item .level-3{background-position:0 -36px;}.top-plat .rank-item .level-4{background-position:0 -54px;}.top-plat .rank-item .level-5{background-position:0 -72px;}.top-plat .rank-item .level-6{background-position:0 -90px;}.top-plat .rank-item .level-7{background-position:0 -108px;}.top-plat .rank-item .level-8{background-position:0 -126px;}.top-plat .rank-item .level-9{background-position:0 -144px;}.top-plat .rank-item .level-10{background-position:0 -162px;}.top-plat .rank-item .level-11{background-position:0 -180px;}.top-plat .rank-item .level-12{background-position:0 -198px;}.top-plat .rank-item .level-13{background-position:0 -216px;}.top-plat .rank-item .level-14{background-position:0 -234px;}.top-plat .rank-item .level-15{background-position:0 -252px;}.top-plat .rank-item .level-16{background-position:0 -270px;}.top-plat .rank-item .level-17{background-position:0 -288px;}.top-plat .rank-item .level-18{background-position:0 -306px;}.top-plat .rank-item .level-19{background-position:0 -324px;}.top-plat .rank-item .level-20{background-position:0 -342px;}#bottom-section{clear:both;height:198px;border:1px solid #E0E0E0;border-bottom-color:#D5D5D5;background:#F9F9F9;padding-top:30px;padding-bottom:20px;overflow:hidden;}#bottom-section .ads-block{float:left;padding-left:35px;width:403px;height:100%;}#bottom-section .ads-block div{height:138px;margin-top:-10px;}#bottom-section .ads-block .go{display:inline-block;height:32px;width:130px;margin-top:0;background:#68B81B;border-radius:3px;color:#fff;text-decoration:none;text-align:center;line-height:30px;font-family:"Microsoft Yahei",Arial;_font-family:Arial;font-size:14px;}#bottom-section .ads-block .go:hover{background:#6FC31E;}#bottom-section .join-block{float:left;border-left:1px solid #E7E7E7;padding-left:35px;width:244px;height:100%;}#bottom-section .join-block .item{color:#555;font-size:14px;font-family:"Microsoft Yahei",Arial;_font-family:Arial;display:inline-block;margin-bottom:12px;*margin-bottom:15px;}#bottom-section .join-block .item span{float:left;margin-right:15px;}#bottom-section .join-block .join{display:inline-block;height:32px;width:130px;background:#68B81B;border-radius:3px;color:#fff;text-decoration:none;text-align:center;line-height:30px;margin-top:1px;font-family:"Microsoft Yahei",Arial;_font-family:Arial;font-size:14px;}#bottom-section .join-block .join:hover{background:#6FC31E;}#bottom-section .contact-block{float:left;border-left:1px solid #E7E7E7;padding-left:35px;width:244px;height:100%;}#bottom-section .contact-block .item4{padding-top:10px;}#bottom-section .contact-block .item{color:#555;font-size:14px;font-family:"Microsoft Yahei",Arial;_font-family:Arial;display:inline-block;margin-bottom:25px;*margin-bottom:28px;}#bottom-section .contact-block .item span{float:left;margin-right:15px;}#bottom-section .contact-block .item a{text-decoration:none;color:#555;display:inline-block;cursor:pointer;}#bottom-section .contact-block .item a:hover{color:#444;}#bottom-section .item1 .icon{background-position:0 -60px;width:32px;}#bottom-section .item1 span{height:26px;line-height:26px;}#bottom-section .item2 .icon{background-position:0 -90px;width:32px;}#bottom-section .item2 span{height:33px;line-height:33px;}#bottom-section .item3 .icon{background-position:0 -130px;width:32px;}#bottom-section .item3 span{height:23px;line-height:23px;}#bottom-section .item4 .icon{background-position:-40px -60px;width:28px;}#bottom-section .item4 a:hover .icon{background-position:-70px -60px;}#bottom-section .item4 span{height:21px;line-height:21px;}#bottom-section .item5 .icon{background-position:-40px -90px;width:28px;}#bottom-section .item5 a:hover .icon{background-position:-70px -90px;}#bottom-section .item5 span{height:27px;line-height:27px;}#bottom-section .item6 .icon{background-position:-40px -130px;width:28px;}#bottom-section .item6 a:hover .icon{background-position:-70px -130px;}#bottom-section .item6 span{height:24px;line-height:24px;}#bottom-section .item7 .icon{background-position:-40px -160px;width:28px;}#bottom-section .item7 span{height:24px;line-height:24px;}#body .body-bottom{background:url(http://img.baidu.com/img/iknow/openiknow/home/bottom-bg.png) 0 0 no-repeat;height:10px;margin-bottom:30px;}
		</style>
		<!----------------相关的CSS代码------结束---------------------->
		
		<!----------------相关的HTML代码------开始---------------------->
		<div id="content-wrap">
		<div id="banner"></div>
		<textarea id="tpl-banner" style="display:none;"> 
			<%for (var i = 0; i < list.length; i++) {%> 
				<%if (list[i].url) {%> 
					<div href="javascript:void(0);" target="_self" class="banner-img <%=(i==0) ? 'show' : ''%>" style="display:none;width:<%=width%>px;"> 
						<span style="background: url(<%=list[i].img%>) center center no-repeat;">
							<div style="width:103px;height:103px;position:absolute;top:28px;left:15.3%;">
								<img style="width:103px; height:103px;" src="<%=list[i].item%>" />
							</div>
							<div class="card">
								<div class="card-left">
									<div class="card-left-top">
										<!--<img style="width:103px; height:103px;" src="<%=list[i].item%>" />-->
									</div>
									<div class="card-left-bottom">
										<h4 class="card-left-bottom-h4"><%=list[i].userName%></h4>
										<p class="card-left-bottom-p"><%=list[i].deptName%></p>
										<p class="card-left-bottom-p"><%=list[i].userPost%></p>
									</div>
								</div>
								<div class="card-right">
									<div class="card-right-top">
										<%=list[i].spec%>
									</div>
									<h3 class="card-right-middle">擅长</h3>
									<div class="card-right-bottom">
										<%=list[i].content%>
									</div>
								</div>
							</div>
							<div class="activity">
							</div>
							<div class="activity-font">
								<div class="activity-top">最新动态</div>
								<div class="activity-line"></div>
								<div class="activity-bottom">
								<%
									var activity = list[i].activityList;
									if(activity != undefined){
										jQuery(activity).each(function(activity_index,activity_item){
								%>
									<div>
										<!--<p style="width:60px;float:left;"><%=activity_item.USER_CODE__NAME%></p>-->
										<p style="width:15px;float:left;">@</p>
										<p style="width:100px;float:left;"><%=activity_item.ACT_CODE__NAME%></p>
										<p style="width:370px;float:left;"><%=activity_item.DATA_DIS_NAME%></p>
										<p style="width:60px;float:left;"><%=activity_item.S_ATIME%></p>
									</div>
									<div style="float:left;">
										<p></p>
									</div>
									
								<%
										});
									}
								%>
								</div>
							</div>
						</span> 
					</div> 
				<%} else {%> 
					<span class="banner-img <%=(i==0) ? 'show' : ''%>" style="background: url(<%=list[i].img%>) center center no-repeat;display:none;width:<%=width%>px"></span>
				<%}%> <%}%> 
		</textarea>
		
		<textarea id="tpl-banner-tab" style="display:none;"> 
			<div id="banner-tab" class="banner-tab"> 
				<div class="banner-tab-wrap" style="top:70px;"> 
					<div class="banner-tab-bg"></div> 
					<div class="banner-tab-inner" style="width:<%=width%>px"> 
						
						<span id="prev" class="banner-item" style="width:14px; height:40px; background:url(/sy/comm/zhidao/baidu_style_files/v_v8.png);background-position:-129px -290px;">
						</span>
						
						<%for (var i = 0; i < list.length; i++) {%>
							<%if (T.browser.ie && T.browser.ie < 7) {%> 
								<a href="javascript:void(0)" onFocus="this.blur()" class="aaa banner-item <%if (i == 0) {%>selected<%}%> <%if (i>=8){%>img_hide<%}else{%>img_show<%}%>">
									<img src="<%=list[i].item%>" content="<%=list[i].content%>" userName="<%=list[i].userName%>" deptName="<%=list[i].deptName%>" question="<%=list[i].question%>" answer="<%=list[i].answer%>" >
								</a> 
							<%} else {%> 
								<span class="aaa banner-item <%if (i == 0) {%>selected<%}%> <%if (i>=8){%>img_hide<%}else{%>img_show<%}%>">
									<img src="<%=list[i].item%>" content="<%=list[i].content%>" userName="<%=list[i].userName%>" deptName="<%=list[i].deptName%>" question="<%=list[i].question%>" answer="<%=list[i].answer%>" >
								</span> 
							<%}%> 
						<%}%>
						
						<span id="next" class="banner-item" style="width:14px; height:40px; background:url(/sy/comm/zhidao/baidu_style_files/v_v8.png);background-position:-145px -290px;">
						</span>
						
					</div> 
				</div> 
			</div> 
		</textarea>
		<!-----------------前一页和后一页的按钮-----------开始---------------------->
		<script>
		jQuery(document).ready(function(){
			jQuery("#prev").live("click",function(){
				for(var i=0; i<8; i++){
					jQuery(jQuery(".img_show").get(0)).prev(".aaa").removeClass("img_hide").addClass("img_show");
					if(jQuery(".img_show").length < 9){
	
					}else{
						jQuery(jQuery(".img_show").get(jQuery(".img_show").length-1)).removeClass("img_show").addClass("img_hide");
					}
				}
			});
			jQuery("#next").live("click",function(){
				for(var j=0; j<8; j++){
					jQuery(jQuery(".img_show").get(jQuery(".img_show").length-1)).next(".aaa").removeClass("img_hide").addClass("img_show");
					if(jQuery(".img_show").length < 9){
						jQuery("#next").unbind();
					}else{
						jQuery(jQuery(".img_show").get(0)).removeClass("img_show").addClass("img_hide");
					}
				}
			});
			
			//给点击图片上的问题时绑定一个事件
			jQuery(".js_onclick").live("click",function(){
				var q_id = jQuery(this).attr("q_id");
				var q_title = jQuery(this).html();
				view(q_id,q_title);
			});
			
			//鼠标移入后变成手型
			jQuery(".js_onclick").live("hover",function(){
				jQuery(this).css("cursor","pointer");
			});
			
			//鼠标离开后图片继续轮转
			jQuery("#banner").live("mouseleave",function(){
				jQuery(".banner-item.selected").focus();
			});
			//鼠标移入后停止图片轮转
			jQuery("#banner").live("mouseenter",function(){
				jQuery(".banner-item.selected").blur();
			});
		});
		
		</script>
		<!-----------------前一页和后一页的按钮-----------结束---------------------->
		
		</div>
		<!----------------相关的HTML代码------结束---------------------->
		
		<!----------------相关的JS代码------开始---------------------->
		<script type="text/javascript">
		(function(t) {
		    var q = {
		        version: "1.0.7",
		        debug: false
		    };
		    function u(a, b) {
		        if (a instanceof Array) {
		            for (var c = 0,
		            d = a.length; c < d; c++) {
		                if (b.call(a[c], a[c], c) === false) {
		                    return
		                }
		            }
		        } else {
		            for (var c in a) {
		                if (a.hasOwnProperty(c)) {
		                    if (b.call(a[c], a[c], c) === false) {
		                        return
		                    }
		                }
		            }
		        }
		    }
		    function s(c, a) {
		        if (Array.prototype.indexOf) {
		            return c.indexOf(a)
		        }
		        for (var b = 0,
		        d = c.length; b < d; b++) {
		            if (c[b] === a) {
		                return b
		            }
		        }
		        return - 1
		    }
		    function B(c, a) {
		        if (Array.prototype.filter) {
		            return c.filter(a)
		        }
		        var b = [];
		        u(c,
		        function(d, e, f) {
		            if (a(d, e, f)) {
		                b.push(d)
		            }
		        });
		        return b
		    }
		    function x(b, a) {
		        return B(a,
		        function(c) {
		            return ! v.loadingPaths[c] || !o(v.cache[c], b)
		        })
		    }
		    function o(c, b) {
		        if (!c || c._loaded) {
		            return false
		        }
		        var a = c.deps || [];
		        if (a.length) {
		            if (s(a, b) > -1) {
		                return true
		            } else {
		                for (var d = 0; d < a.length; d++) {
		                    if (o(v.cache[a[d]], b)) {
		                        return true
		                    }
		                }
		                return false
		            }
		        }
		        return false
		    }
		    function v(a, b) {
		        this.name = b;
		        this.path = a;
		        this.fn = null;
		        this.exports = {};
		        this._loaded = false;
		        this._requiredStack = [];
		        this._readyStack = [];
		        v.cache[this.name] = this
		    }
		    v.loadedPaths = {};
		    v.loadingPaths = {};
		    v.cache = {};
		    v.paths = {};
		    v.moduleFileMap = {};
		    v.requiredPaths = {};
		    v.lazyLoadPaths = {};
		    v.prototype = {
		        init: function() {
		            if (!this._inited) {
		                this._inited = true;
		                if (!this.fn) {
		                    throw new Error('Module "' + this.name + '" not found!')
		                }
		                var a;
		                if (a = this.fn.call(null, y, this.exports)) {
		                    this.exports = a
		                }
		            }
		        },
		        load: function() {
		            v.loadingPaths[this.path] = true;
		            var a = q.debug ? this.path: (v.moduleFileMap[this.name] || this.path);
		            p.create({
		                src: a,
		                charset: "gbk"
		            })
		        },
		        lazyLoad: function() {
		            var b = this.name,
		            a = this.path;
		            if (v.lazyLoadPaths[b]) {
		                this.define();
		                delete v.lazyLoadPaths[b]
		            } else {
		                if (v.loadedPaths[a]) {
		                    this.triggerStack()
		                } else {
		                    if (!v.loadingPaths[a]) {
		                        v.requiredPaths[this.name] = true;
		                        this.load()
		                    }
		                }
		            }
		        },
		        ready: function(b, a) {
		            var c = a ? this._requiredStack: this._readyStack;
		            if (b) {
		                if (this._loaded) {
		                    this.init();
		                    b()
		                } else {
		                    c.push(b)
		                }
		            } else {
		                this._loaded = true;
		                v.loadedPaths[this.path] = true;
		                delete v.loadingPaths[this.path];
		                this.triggerStack()
		            }
		        },
		        triggerStack: function() {
		            if (this._readyStack.length > 0) {
		                this.init();
		                u(this._readyStack,
		                function(a) {
		                    if (!a.doing) {
		                        a.doing = true;
		                        a()
		                    }
		                });
		                this._readyStack = []
		            }
		            if (this._requiredStack.length > 0) {
		                u(this._requiredStack,
		                function(a) {
		                    if (!a.doing) {
		                        a.doing = true;
		                        a()
		                    }
		                });
		                this._requiredStack = []
		            }
		        },
		        define: function() {
		            var a = this,
		            c = this.deps,
		            b = [];
		            if (!c && q.debug) {
		                c = getDependents(a.fn)
		            }
		            c = x(a.path, c);
		            if (c.length) {
		                v.loadingPaths[this.path] = true;
		                u(c,
		                function(d) {
		                    var e = z(d);
		                    b.push(e.path)
		                });
		                u(c,
		                function(d) {
		                    var e = z(d);
		                    e.ready(function() {
		                        if (r(b)) {
		                            a.ready()
		                        }
		                    },
		                    true);
		                    e.lazyLoad()
		                })
		            } else {
		                this.ready()
		            }
		        }
		    };
		    function y(b) {
		        var a = z(b);
		        a.init();
		        return a.exports
		    }
		    function r(a) {
		        var b = true;
		        u(a,
		        function(c) {
		            if (! (c in v.loadedPaths)) {
		                return b = false
		            }
		        });
		        return b
		    }
		    function w(a) {
		        return t ? (t + a) : a
		    }
		    function z(b) {
		        var a = b.indexOf(":") > -1 ? b: w(b);
		        if (v.cache[b]) {
		            return v.cache[b]
		        }
		        return new v(a, b)
		    }
		    if (t && t.charAt(t.length - 1) == "/") {
		        t = t.substr(0, t.length - 1)
		    }
		    var p = {
		        create: function(b) {
		            if (b.src in this._paths) {
		                return
		            }
		            this._paths[b.src] = true;
		            u(this._rules,
		            function(d) {
		                d.call(null, b)
		            });
		            var c = document.getElementsByTagName("head")[0];
		            var a = document.createElement("script");
		            a.type = b.type || "text/javascript";
		            b.charset && (a.charset = b.charset);
		            a.src = b.src;
		            a.onload = a.onerror = a.onreadystatechange = function() {
		                if ((!this.readyState || this.readyState === "loaded" || this.readyState === "complete")) {
		                    a.onload = a.onerror = a.onreadystatechange = null;
		                    if (a.parentNode && !q.debug) {
		                        c.removeChild(a)
		                    }
		                    a = undefined;
		                    b.loaded && b.loaded()
		                }
		            };
		            c.insertBefore(a, c.firstChild)
		        },
		        _paths: {},
		        _rules: [],
		        addPathRule: function(a) {
		            this._rules.push(a)
		        }
		    };
		    q.use = function(a, b) {
		        if (typeof a === "string") {
		            a = [a]
		        }
		        var c = [];
		        var d = [];
		        u(a,
		        function(f, e) {
		            d[e] = false
		        });
		        u(a,
		        function(g, e) {
		            var f = z(g);
		            f.ready(function() {
		                c[e] = f.exports;
		                d[e] = true;
		                var h = true;
		                u(d,
		                function(i) {
		                    if (i === false) {
		                        return h = false
		                    }
		                });
		                if (b && h) {
		                    b.apply(null, c)
		                }
		            });
		            f.lazyLoad()
		        })
		    };
		    q.module = function(d, b, a) {
		        var c = z(d);
		        c.fn = b;
		        c.deps = a;
		        if (v.requiredPaths[d]) {
		            c.define()
		        } else {
		            v.lazyLoadPaths[d] = true
		        }
		    };
		    q.pathRule = function(a) {
		        p.addPathRule(a)
		    };
		    q._fileMap = function(a, b) {
		        if (typeof a === "object") {
		            u(a,
		            function(d, c) {
		                q._fileMap(c, d)
		            })
		        } else {
		            if (typeof b === "string") {
		                b = [b]
		            }
		            u(b,
		            function(c) {
		                v.moduleFileMap[c] = a
		            })
		        }
		    };
		    var A = {};
		    q.context = function(c, a) {
		        var b = arguments.length;
		        if (b > 1) {
		            A[c] = a
		        } else {
		            if (b == 1) {
		                if (typeof c == "object") {
		                    for (var d in c) {
		                        if (c.hasOwnProperty(d)) {
		                            A[d] = c[d]
		                        }
		                    }
		                } else {
		                    return A[c]
		                }
		            }
		        }
		    };
		    "F" in window || (window.F = q)
		})("/sy/comm/zhidao/baidu_style_files/");
		F.context("user", {
		    isLogin: "0"
		});
		</script>
		<script type="text/javascript">
		
		var temp_banner = [];
		var canshuInit = function(){
			var param={
				showNum:16,
				curPage:1,
				"summaryLength":80
			}
			var specialPages = parent.FireFly.doAct("SY_COMM_ZHIDAO_SPECIALIST", "getSpecialist", param,false,false);
			var specialistData = specialPages["_DATA_"];
			
			//alert(specialistData);
			//console.debug(specialistData);
			
			jQuery.each(specialistData,function(i,obj){
				var temp = {};
				if(i%3 == 0){
					temp["img"] = "/sy/comm/zhidao/baidu_style_files/pic01.jpg";
				}
				if(i%3 == 1){
					temp["img"] = "/sy/comm/zhidao/baidu_style_files/pic02.jpg";
				}
				if(i%3 == 2){
					temp["img"] = "/sy/comm/zhidao/baidu_style_files/pic03.jpg";
				}
				temp["item"] = obj.USER_ID__IMG;
				temp["url"] = "index.html";
				temp["content"] = Format.limit(33, obj.SPEC_SUB);
				temp["userName"] = obj.USER_ID__NAME;
				temp["userPost"] = obj.USER_INFO.USER_POST;
				if(obj.USER_INFO != undefined){
					temp["deptName"] = obj.USER_INFO.DEPT_NAME;
				}
				temp["spec"] = Format.limit(46,obj.q_and_a.SPEC);
				var activityList = obj.activityList;
				var activitys = [];
				jQuery(activityList).each(function(j,item){
					var activityList = {};
					activityList["USER_CODE__NAME"] = item.USER_CODE__NAME;
					activityList["ACT_CODE__NAME"] = item.ACT_CODE__NAME;
					activityList["DATA_DIS_NAME"] = item.DATA_DIS_NAME;
					activityList["S_ATIME"] = jQuery.timeago(item.S_ATIME.substring(0,19));
					activitys.push(activityList);
				});	
				temp["activityList"] = activitys;			
				
				temp_banner.push(temp);
			});
		}
			
			
		
		</script>
		<script type="text/javascript">
		canshuInit();
		F.module("/static/enterprise/banner.js",
		function(l, p) {
		    var f = l("/static/common/lib/tangram/base/base.js"),
		    q = l("/static/common/ui/template/template.js"),
		    o = temp_banner,
		    v = null,
		    e = null,
		    m = -1,
		    r = 0,
		    n = null,
		    c = 5000,
		    k = 300,
		    j = (1920 > document.body.offsetWidth) ? document.body.offsetWidth: 1920,
		    s = false,
		    i = [];
		    var a = function() {
		        d();
		        var w = q("tpl-banner", {
		            list: o,
		            width: j
		        });
		        f("#banner").append(w);
		        e = f(".banner-img").get();
		        f.show(e[0]);
		        var x = null;
		        window.onresize = function() {
		            clearTimeout(x);
		            x = setTimeout(function() {
		                j = (1920 > document.body.offsetWidth) ? document.body.offsetWidth: 1920;
		                f.each(f(".banner-img").get(),
		                function(y, z) {
		                    f(z).css("width", j + "px");
		                    if (f.browser.ie && f.browser.ie < 10) {
		                        f(z).css("clip", "rect(0px," + j + "px,500px,0px)")
		                    }
		                })
		            },
		            100)
		        };
		        if (o.length < 2) {
		            return
		        }
		        setInterval(function() {
		            if (window.blurred || s) {
		                return
		            }
		            b(k)
		        },
		        c);
		        u()
		    };
		    var b = function(x) {
		        var w = (r + 1) % v;
		        g(w, x)
		    };
		    var g = function(H, G) {
		        if (i[H].load_ok != 1) {
		            return
		        }
		        h();
		        t(H);
		        var I = e[r];
		        var B = e[m];
		        var A = 10;
		        var C = parseInt(G / A);
		        var J = parseInt(j / C);
		        var y = j;
		        f.show(I);
		        var z = 0;
		        var E = parseInt((100 - z) / C);
		        f(I).css("opacity", z / 100);
		        var D = J * 2;
		        var w = parseInt(D / C);
		        f(I).css("left", D + "px");
		        var x = setInterval(function() {
		            if (x != n) {
		                clearInterval(x)
		            }
		            if (y > 0 || D > 0 || z < 100) {
		                z += E;
		                f(I).css("opacity", (z > 100 ? 100 : z) / 100);
		                D -= w;
		                f(I).css("left", (D < 0 ? 0 : D) + "px");
		                y -= J;
		                if (y < D) {
		                    y = D
		                }
		                f(B).css("clip", "rect(0px," + (y < 0 ? 0 : y) + "px,500px,0px)")
		            } else {
		                h()
		            }
		        },
		        A);
		        n = x
		    };
		    var h = function() {
		        if (m < 0) {
		            return
		        }
		        clearInterval(n);
		        var x = e[r];
		        var w = e[m];
		        f.hide(w);
		        if (f.browser.ie && f.browser.ie < 10) {
		            f(w).css("clip", "rect(0px," + j + "px,500px,0px)");
		            f(x).css("opacity", 1).css("left", "0")
		        } else {
		            f(w).css("clip", "");
		            f(x).css("opacity", "").css("left", "")
		        }
		        f.removeClass(w, "show");
		        f.addClass(x, "show")
		    };
		    var u = function() {
		        var y = q("tpl-banner-tab", {
		            list: o,
		            width: 85 * o.length
		        });
		        f("#banner").after(y);
		        f.each(f(".aaa img").get(),
		        function(E, G) {
		            f(G).on("mouseover",
		            function(H) {
		                s = true;
		                if (r != E) {
		                    g(E, k)
		                }
		            });
		            f(G).on("mouseout",
		            function(H) {
		                s = false
		            })
		        });
		        var D = null,
		        B = false;
		        f("#banner").on("mouseover",
		        function() {
		            clearTimeout(D);
		            D = setTimeout(function() {
		                x()
		            },
		            100)
		        });
		        f("#banner").on("mouseout",
		        function() {
		            clearTimeout(D);
		            D = setTimeout(function() {
		
		                z()
		            },
		            100)
		        });
		        f("#banner-tab").on("mouseover",
		        function() {
		            clearTimeout(D);
		            D = setTimeout(function() {
		                x()
		            },
		            100)
		        });
		        f("#banner-tab").on("mouseout",
		        function() {
		            clearTimeout(D);
		            D = setTimeout(function() {
		                z()
		            },
		            100)
		        });
		        var A = f(".banner-tab-wrap").get(0);
		        var w = null,
		        C = 70;
		        function x() {
		            if (B) {
		                return
		            }
		            clearInterval(w);
		            B = true;
		            w = setInterval(function() {
		                if (C > 0) {
		                    f(A).css("top", C + "px");
		                    C -= 10
		                } else {
		                    clearInterval(w);
		                    f(A).css("top", "0");
		                    C = 0
		                }
		            },
		            10)
		        }
		        function z() {
		            if (!B) {
		                return
		            }
		            clearInterval(w);
		            B = false;
		            w = setInterval(function() {
		                C += 10;
		                if (C < 70) {
		                    f(A).css("top", C + "px")
		                } else {
		                    clearInterval(w);
		                    f(A).css("top", "");
		                    C = 70
		                }
		            },
		            10)
		        }
		    };
		    var t = function(x) {
		        var w = f(".aaa").get();
		        f.each(w,
		        function(y, z) {
		            f.removeClass(z, "selected")
		        });
		        var $span_jq = f.addClass(w[x], "selected");
		        
		        //自己添加的方法
		        var img_src = jQuery(jQuery(jQuery($span_jq).children()).get(0)).attr("src");
		        var content = jQuery(jQuery(jQuery($span_jq).children()).get(0)).attr("content");
		        var userName = jQuery(jQuery(jQuery($span_jq).children()).get(0)).attr("userName");
		        var deptName = jQuery(jQuery(jQuery($span_jq).children()).get(0)).attr("deptName");
		        var question = jQuery(jQuery(jQuery($span_jq).children()).get(0)).attr("question");
		        var answer = jQuery(jQuery(jQuery($span_jq).children()).get(0)).attr("answer");
		        jQuery("user_img").attr("src",img_src);
		        //jQuery("#user_content").html(content);
		        if(answer.length > 100){
		        	answer = answer.substring(0,100)+"...";
		        }
		        
		        jQuery("user_content").html("");
		        if(answer.length > 0 && question.length > 0){
			        //将需要记录的信息放到一个div中
					jQuery("user_content").html(
						'<dl id="aq">'+
							'<dt><a href="#" target="_self" >'+question+'</a></dt>'+
							'<dd class="info-content"><span class="answer"></span>'+answer+'</dd>'+
							'<dd class="info-user">回答者：'+userName+'</dd>'+
						'</dl>'
					);
		        }
		        jQuery("userName").html(userName);
		        jQuery("deptName").html(deptName);
		        
		        if (r != x) {
		            m = r;
		            r = x
		        }
		    };
		    var d = function() {
		        f.each(o,
		        function(w, x) {
		            i[w] = new Image();
		            i[w].src = o[w].img;
		            if (i[w].complete) {
		                i[w].load_ok = 1
		            } else {
		                i[w].onload = function() {
		                    i[w].load_ok = 1;
		                    i[w].onload = null
		                };
		                i[w].onerror = function() {
		                    i[w].load_ok = 1;
		                    i[w].onerror = null
		                }
		            }
		        })
		    };
		    p.init = function() {
		        o.pop();
		        v = o.length;
		        if (o.length > 0) {
		            a()
		        }
		    }
		},
		["/static/common/lib/tangram/base/base.js", "/static/common/ui/template/template.js"]);
		F.module("/static/enterprise/wall.js",
		function(i, l) {
		    var g = i("/static/common/lib/tangram/base/base.js"),
		    p = i("/static/common/ui/template/template.js"),
		    q = window.logo_tab,
		    d = window.good_plat,
		    c = window.wall_data,
		    n = {},
		    r = true,
		    j,
		    h,
		    s,
		    m = ["left", "right", "top", "bottom"],
		    e = [];
		    var b = function() {
		        n.main = {};
		        n.wall = {};
		        for (var w = 0; w < d.length; w++) {
		            n.main[d[w].tab] = d[w]
		        }
		        for (var v = 0; v < c.length; v++) {
		            var x = c[v].tab;
		            if (!n.wall[x]) {
		                n.wall[x] = [c[v]]
		            } else {
		                n.wall[x].push(c[v])
		            }
		        }
		        g(".logo-tab-item:eq(0)").trigger("click");
		        a();
		        g(".wall:eq(0)").on("click",
		        function(y) {
		            r && g.event.preventDefault(y)
		        })
		    };
		    var a = function() {
		        setInterval(function() {
		            if (window.blurred || r) {
		                return
		            }
		            if (n.wall[j].length > 21) {
		                var x = g(".wall-item").get();
		                var y = parseInt(Math.random() * 21);
		                while (y == s) {
		                    y = parseInt(Math.random() * 21)
		                }
		                var w = parseInt(Math.random() * m.length);
		                u(y, m[w])
		            }
		            if (n.wall[j].length > 22) {
		                var z = parseInt(Math.random() * 21);
		                var v = parseInt(Math.random() * m.length);
		                while (z == y || z == s) {
		                    z = parseInt(Math.random() * 21)
		                }
		                while (v == w) {
		                    v = parseInt(Math.random() * m.length)
		                }
		                setTimeout(function() {
		                    u(z, m[v])
		                },
		                500)
		            }
		        },
		        3000)
		    };
		    var t = function(v) {
		        g(".logo-wall .main .answer-count").html(v.answer);
		        g(".logo-wall .main .good-count").html(v.good)
		    };
		    var k = function() {
		        g.each(g(".wall-item").get(),
		        function(v, w) {
		            var x = setTimeout(function() {
		                var z = 0;
		                var y = setInterval(function() {
		                    if (!w || !g(".img-wrap", w).get(0)) {
		                        clearInterval(y);
		                        return
		                    }
		                    z += 10;
		                    if (z <= 100) {
		                        if (g.browser.ie && g.browser.ie < 9) {
		                            g(".img-wrap:eq(0)", w).css("filter", "alpha(opacity=" + z + ")");
		                            g(w).css("filter", "alpha(opacity=" + z + ")")
		                        } else {
		                            g(".img-wrap:eq(0)", w).css("opacity", z / 100);
		                            g(w).css("opacity", z / 100)
		                        }
		                        var A = "-" + (100 - z) + "px";
		                        g(w).css("top", A)
		                    } else {
		                        clearInterval(y);
		                        g(w).css("top", "").removeClass("play")
		                    }
		                },
		                15)
		            },
		            parseInt(Math.random() * 200) * 5)
		        });
		        setTimeout(function() {
		            var v = g(".wall-item").get(0);
		            for (var w = 0; w < v.length; w++) {
		                if (g.dom.hasClass(v[w], "play")) {
		                    setTimeout(arguments.callee, 500);
		                    return
		                }
		            }
		            h = 21;
		            r = false
		        },
		        500)
		    };
		    var u = function(z, C) {
		        var x = n.wall[j];
		        var v = g(".wall-item").get(z);
		        var y = '<img class="' + C + '" src="' + x[h].logo + '" height="100" width="100"/>';
		        g(".img-wrap:eq(0)", v).append(y);
		        var A = p("tpl-wall-item-content", x[h]);
		        var D = x[z];
		        x[z] = x[h];
		        x[h] = D;
		        h = ((h - 21 + 1) % (x.length - 21)) + 21;
		        var B = 100;
		        var w = g("." + C, v).get(0);
		        setTimeout(function() {
		            if (r) {
		                return
		            }
		            if (B > 0) {
		                B -= 2;
		                switch (C) {
		                case "left":
		                    g(w).css("left", "-" + B + "px");
		                    break;
		                case "right":
		                    g(w).css("left", B + "px");
		                    break;
		                case "top":
		                    g(w).css("top", "-" + B + "px");
		                    break;
		                case "bottom":
		                    g(w).css("top", B + "px");
		                    break
		                }
		                setTimeout(arguments.callee, 5)
		            } else {
		                try {
		                    v.innerHTML = A
		                } catch(E) {}
		            }
		        },
		        5)
		    };
		    var f = function(x) {
		        var w = n.wall[x];
		        for (var y = 0; y < w.length; y++) {
		            var v = new Image();
		            v.src = w[y].logo
		        }
		    };
		    l.init = function() {
		       // q.pop();
		       // d.pop();
		       // c.pop();
		       // o();
		       // b()
		    }
		},
		["/static/common/lib/tangram/base/base.js", "/static/common/ui/template/template.js"]);
		F.module("/static/enterprise/feed.js",
		function(e, g) {
		    var f = e("/static/common/lib/tangram/base/base.js"),
		    k = e("/static/common/ui/template/template.js"),
		    c = null,
		    j = 8,
		    i = false,
		    l = false,
		    h = null;
		    var n = function(p) {
		        if (!f.type(p) === "array") {
		            return []
		        }
		        p.reverse();
		        var o = 0;
		        while (o < p.length) {
		            if (!p[o].plat || !p[o].user || !p[o].event) {
		                p.splice(o, 1);
		                continue
		            }
		            if (!p[o].plat.name || !p[o].plat.domain) {
		                p.splice(o, 1);
		                continue
		            }
		            if (!p[o].user.name) {
		                p.splice(o, 1);
		                continue
		            }
		            if (!p[o].event.type || !p[o].event.title || !p[o].event.questionid || !p[o].event.time) {
		                p.splice(o, 1);
		                continue
		            }
		            if (!p[o].plat.logo) {
		                p[o].plat.logo = window.default_logo
		            }
		            o++
		        }
		        return p
		    };
		    var b = function(q) {
		        if (!q || q.length == 0) {
		            return
		        }
		        var p = "";
		        for (var o = 0; o < 8; o++) {
		            if (!q[o]) {
		                break
		            }
		            p = k("tpl-feed", q[o]) + p
		        }
		        f.G("feed-wrap").innerHTML = p;
		        if (q.length > 8) {
		            c = q;
		            h = setTimeout(a, 3000)
		        }
		        f("#feed-wrap").on("mouseover",
		        function() {
		            i = true
		        });
		        f("#feed-wrap").on("mouseout",
		        function() {
		            i = false
		        })
		    };
		    var a = function() {
		        if (window.blurred || i) {
		            h = setTimeout(arguments.callee, 3000);
		            return
		        }
		        var o = -100;
		        setTimeout(function() {
		            if (window.blurred || i) {
		                setTimeout(arguments.callee, 20);
		                return
		            }
		            if (o < -30) {
		                o += 2;
		                f("#feed-wrap").css("top", o + "px");
		                setTimeout(arguments.callee, 20)
		            } else {
		                var r = c[j++];
		                while (!r) {
		                    j %= c.length;
		                    r = c[j++]
		                }
		                var q = k("tpl-feed", r);
		                f("#feed-wrap").prepend(q);
		                f("#feed-wrap").css("top", "");
		                var p = f(".feed-item", "feed-wrap").get();
		                f.dom.remove(p[p.length - 1]);
		                j %= c.length;
		                if ((c.length - j) <= 10 && !l) {
		                    m()
		                }
		            }
		        },
		        20);
		        clearTimeout(h);
		        h = setTimeout(arguments.callee, 3000)
		    };
		    var d = function(s) {
		        var r = 0,
		        p = 0,
		        o = [];
		        while (r < c.length && p < s.length) {
		            if (c[r].event.time <= s[p].event.time) {
		                o.push(c[r++])
		            } else {
		                o.push(s[p++])
		            }
		        }
		        while (r < c.length) {
		            o.push(c[r++])
		        }
		        while (p < s.length) {
		            o.push(s[p++])
		        }
		        r = 0;
		        var t = {};
		        while (r < o.length) {
		            var q = o[r].event.questionid + ":" + o[r].event.time;
		            if (t[q]) {
		                o.splice(r, 1)
		            } else {
		                t[q] = true;
		                r++
		            }
		        }
		        if (o.length - j < 40) {
		            j = j - (o.length - 40);
		            c = o.slice( - 40)
		        } else {
		            j = 0;
		            c = o.slice(j)
		        }
		    };
		    g.focus = function() {
		        if (l || !h) {
		            return
		        }
		        if (((new Date()).getTime() / 1000 - c[j].event.time) < 600) {
		            return
		        }
		        l = true;
		        clearTimeout(h);
		        g.init();
		        setTimeout(function() {
		            l = false
		        },
		        10000)
		    }
		},
		["/static/common/lib/tangram/base/base.js", "/static/common/ui/template/template.js"]);
		F.module("/static/enterprise/rank.js",
		function(c, d) {
		    var b = c("/static/common/lib/tangram/base/base.js"),
		    a = c("/static/common/ui/template/template.js");
		    var e = function(h) {
		        var g = a("tpl-top-plat", {
		            list: h
		        });
		        b("#top-plat").append(g)
		    };
		    var f = function(h) {
		        var g = a("tpl-top-intern", {
		            list: h
		        });
		        b("#top-intern").append(g)
		    };
		    d.init = function() {
		    }
		},
		["/static/common/lib/tangram/base/base.js", "/static/common/ui/template/template.js"]);
		F.use(["/static/common/lib/tangram/base/base.js", "/static/enterprise/banner.js", "/static/enterprise/wall.js", "/static/enterprise/feed.js", "/static/enterprise/rank.js"],
		function(d, c, a, e, b) {
		    window.T = d;
		    window.ik = {};
		    ik.util = {
		        formatTime: function(f) {
		            var g = parseInt((new Date()).getTime() / 1000) - f;
		            if (g < 60) {
		                return g + "秒前"
		            } else {
		                if (g < 3600) {
		                    return parseInt(g / 60) + "分钟前"
		                } else {
		                    if (g < 3600 * 24) {
		                        return parseInt(g / 3600) + "小时前"
		                    } else {
		                        return d.date.format(new Date(f * 1000), "yyyy-MM-dd")
		                    }
		                }
		            }
		        },
		        proxy: function(f, i, h, g) {
		            if (d.type(f) === "string") {
		                f = d(f).get(0)
		            }
		            if (f && d.type(g) === "function") {
		                d(f).on(h,
		                function(m) {
		                    if (d.type(i) === "string") {
		                        _target = d(i, f).get()
		                    }
		                    if (!d.type(_target) === "array") {
		                        _target = [_target]
		                    }
		                    if (!_target || _target.length <= 0) {
		                        return
		                    }
		                    var l = m.target || m.srcElement;
		                    for (var k = 0,
		                    j = _target.length; k < j; k++) {
		                        if (l === _target[k] || d.dom.contains(_target[k], l)) {
		                            g.call(_target[k], m);
		                            break
		                        }
		                    }
		                })
		            }
		        },
		        subByte: function(f, g, h) {
		            f = String(f);
		            h = h || "";
		            if (g < 0 || d.string.getByteLength(f) <= g) {
		                return f
		            } else {
		                f = f.substr(0, g).replace(/([^\x00-\xff])/g, "\x241 ").substr(0, g).replace(/[^\x00-\xff]$/, "").replace(/([^\x00-\xff]) /g, "\x241");
		                return f + h
		            }
		        }
		    };
		    d(function() {
		        window.default_logo = "http://zhidao.baidu.com/riostatic/common/static/images/default-big-logo.jpg";
		        baiduTemplate.LEFT_DELIMITER = "<%";
		        baiduTemplate.RIGHT_DELIMITER = "%>";
		        c.init();
		        a.init();
		       // e.init();
		        b.init();
		        window.onblur = function() {
		            window.blurred = true
		        };
		        window.onfocus = function() {
		            setTimeout(function() {
		                window.blurred = false
		            },
		            100);
		            e.focus()
		        };
		        d("#footer").css({
		            margin: 0,
		            background: "#F2F2F2",
		            width: "auto",
		            "padding-bottom": "30px"
		        })
		    })
		});
		</script>
		<!----------------相关的JS代码------结束---------------------->
		
		
		<!----------------自己写的一些JS代码-----开始---------------------->
		<script>
			jQuery(document).ready(function(){
				//jQuery("#banner").append("<div class='div_1' style='width:200px; height:auto;left:300px;top:30px;'><div class='prof-thanks-ctn'><div class='prof-thanks-info'><p>香*****：</p><p class='prof-thanks-txt'>谢谢汤律师耐心为我答疑，当初我只是抱了尝试心态问了一下，没想到你居然回答的那么仔细！</p><p class='prof-thanks-pro'><strong id='userName'>张金伟</strong><span id='deptName'>项目中心</span></p></div><img id='user_img' src='/sy/theme/default/images/common/user0.png' alt=''></div></div>");
				//jQuery("#banner").append("<div class="div_2" id='user_content' style='width:300px; height:auto;left:600px;top:30px;'></div>");
			});
		</script>
		<!----------------自己写的一些JS代码-----结束---------------------->
		<!------------------图片轮转效果-----结束------------------>
		
		
		
	<div id="body" class="container">
		<!--<div id="mouseover-div" style="background-color:red;height:300px; left:0px; width:1347px; height:300; z-index:99999; position:absolute; top:109px;"></div>-->
<!--------------navigation---------------------->				
	<script type="text/javascript">
		jQuery("#menu").find("#specialist").addClass("current");
	</script>
<!--------------expertBanner---------------------->		
	
	   
	<!-- 专家类别 -->
	<div class="expert-bottom-left w160 mr20">
		<div class="expert-category">
			<@channel_list debugName="专家类别" channelId="${root_channel_id}">
		          <div class="info-left-leftside">
		            <div id="category-list">
		            <h3>
						 专家类别
					</h3>
					<dt> 
						<a href="javascript:void(0)" id="category-all">全部</a> 
						<span></span> 
					</dt>
		           <#list tag_list as channel>
		              <dl alog-group="hm-cate" data-cid="${channel_index + 100}">
		                <dt> 
		                	<a href="javascript:void(0)" onclick="javascript:renderSpecByChnl(1,'${channel.CHNL_ID}','${channel.CHNL_NAME}')"> 
		                		<@zhidao_channel_count debugName="专家类别数量" channelId="${channel.CHNL_ID}">
		                		${channel.CHNL_NAME}(${channelCount}) 
		                		</@zhidao_channel_count>
		                	</a> 
		                </dt>
		                <dd>
		                  <@channel_list debugName="${channel.CHNL_NAME}子分类" channelId="${channel.CHNL_ID}"> 
		                  	<#list tag_list as sub>
		                        	<a href="javascript:void(0)" onclick="javascript:renderSpecByChnl(1,'${sub.CHNL_ID}','${sub.CHNL_NAME}')"> 
		                            	<@zhidao_channel_count debugName="子分类数量" channelId="${sub.CHNL_ID}">
		                            	${sub.CHNL_NAME}(${channelCount}) 
		                            	</@zhidao_channel_count>
		                            </a> 
		                    </#list> 
		                   </@channel_list>
		                </dd>
		              </dl>
		           </#list> 
		             </div>
		  		</div>
			</@channel_list>
		</div>
	</div>
	<script>
		jQuery("#category-all").on("click",function(){
			jQuery("#expertAll").html(jQuery(this).text());
			jQuery("#expertList").empty();
			jQuery("#specia_page").empty();
			speciaFun();
		});
	
		function renderSpecByChnl(curPage,chnl_id,chnl_name){
		
			//1.
			 	jQuery("#expertAll").html(chnl_name);
			//2.
				jQuery("#expertList").empty();
				jQuery("#specia_page").empty();
				speciaFun(curPage,chnl_id,chnl_name);
			 
		}
				 
	</script>
<!--------------------------------------全部专家--------------------------->        
	<div class="expert-bottom-right w800">
	    <div class="expert-category-user">
	        <h4 id="expertAll">
	            全部专家
	            <span>
	                (3989)
	            </span>
	        </h4>
	        <ul id="expertList">
	             
	        </ul>
	        <div id="expertPager" class="pager tang-pager">
	            <div id="specia_page" class="tang-pager-main">
	               
	            </div>
	        </div>
	    </div>
		<script type="text/javascript">
				speciaFun();
				function speciaFun(curPage,chnl_id,chnl_name){
					if(curPage==undefined){
						curPage=1;
					}
					var param={
						 showNum:8,
						 curPage:curPage
					};
					if(chnl_id!==undefined){
						param.chnlId=chnl_id;
					}
					if(chnl_name==undefined){
						chnl_name="全部";
					}
					var specialPages = parent.FireFly.doAct("SY_COMM_ZHIDAO_SPECIALIST", "getSpecialist", param,false,false);
					
					var allnum=specialPages._PAGE_.ALLNUM;
					jQuery("#expertAll").html(chnl_name+"专家<span>("+allnum+")</span>");
					
					
					var speciaPageNum = specialPages._PAGE_.PAGES;
					if(allnum>0){//如果有数据
						var specialistData = specialPages._DATA_;
						//console.debug(specialistData);
						jQuery.each(specialistData,function(i,obj){
							var	spec_sub = Format.limit(60,obj.SPEC_SUB);
							//是否在线
							var online = "";
							if(obj.USER_ID__STATUS == 1){
								online = "online";
							}
							jQuery("<li style='padding-top: 0px;'>"+
									"<div>"+
										"<span>"+
											"<a href='javascript:othersZhidao(&apos;${userZhidao_center_tmpl_id}&apos;,&apos;"+obj.USER_ID+"&apos;)'>"+
												//"<img src='"+obj.USER_ID__IMG+"' height='80' width='80'>"+
												formatUserImg(obj.USER_ID__IMG,'','','80px','80px',online,'65px','65px','')+
											"</a>"+
										"</span>"+
										"<a href='#' class='expert-icons direct-ask'></a>"+
									"</div>"+
									"<h3><a href='javascript:othersZhidao(&apos;${userZhidao_center_tmpl_id}&apos;,&apos;"+obj.USER_ID+"&apos;)'>"+obj.USER_ID__NAME+"</a>"+
									"<span style='color:rgb(102,102,102);font-size:12px;padding-left:20px;'>"+obj.USER_INFO.USER_POST+"/"+obj.USER_INFO.DEPT_NAME+"</span>"+
									"</h3>"+
									"<p>"+obj.q_and_a.SPEC+"</p>"+
									"<p title="+obj.SPEC_SUB+">擅长:"+spec_sub+"</p>"+
									"<!--<p>上周回答："+obj._OKCOUNT_+"</p>-->"+
								"</li>").appendTo(jQuery("#expertList"))
									    .find(".direct-ask").on("click",function(){
											loadTiWen(obj.USER_ID__NAME,obj.USER_ID);
									    });
						});
					}else{
						jQuery("#expertList").text("抱歉，这个领域还没有专家！");
					}
					
					var starpage = 0;
					if(parseInt(curPage)-5>0){
						starpage = starpage +(curPage-5);
						if(starpage + 10 > speciaPageNum){
							starpage = speciaPageNum - 10;
						}
					}

					if(parseInt(curPage)>1){
						jQuery("#specia_page").append(jQuery("<a></a>").addClass("tang-pager-normal pager-next")
															 .css("cursor","pointer")
															 .text("<上一页").attr("onclick","goPage("+(parseInt(curPage)-1)+")"));
															 
					}

					for (var i = starpage, j=0; i < speciaPageNum; i++,j++) {
						if (j < 10) {
							var $pagination=jQuery("<a></a>").addClass("tang-pager-normal")
															 .css("cursor","pointer")
															 .text((i + 1)).attr("onclick","goPage("+(i + 1)+")");
							 
							if (i + 1 == curPage) {
								$pagination=jQuery("<b></b>").text((i+1));
							}
							
							jQuery("#specia_page").append($pagination);
						}
					}
					if(curPage<speciaPageNum){
						jQuery("#specia_page").append(jQuery("<a></a>").addClass("tang-pager-normal pager-next")
															 .css("cursor","pointer")
															 .text("下一页>").attr("onclick","goPage("+(parseInt(curPage)+1)+")"))
					}
				}
				function goPage(page){
					jQuery("#expertList").empty();
					jQuery("#specia_page").empty();
					speciaFun(page);
				}
			</script>
	</div>
	
	<!-------------------后加的右侧边栏--------开始----------------------->
	<div style="width:280px; height:auto; float:left; margin-left:20px;">
		<!----------放一个撑开的div---开始------------->
		<div style="width:100%; height:30px; margin-bottom:13px;"></div>
		<!----------放一个撑开的div---结束------------->
		<!-------------优秀专家排行----开始-------------------->
		<div class="rank-block" id="expert-rank" style="width:100%; height:auto;">
			<h3>
				专家最新知道
			</h3>
			<!-----------先注释掉--------开始--------------->
			<!--
			<a class="more-expert">
				更多
			</a>
			-->
			<!-----------先注释掉--------结束--------------->
			<br />							
			<div id="threeSpec">
				<@zhidao_three_spec debugName="专家最新知道">
					<!--------------公司领导-------------------->
					<div id="leader" class="recommend-user" style="width:250px; float:left;">
						<div class="inner" style="height:110px;">
					      <div class="inner-left">
					        <div class="leader-logo"></div>
					        <a style="width:80px; height:80px;display:block;" data-img="#" target="_self" href="javascript:othersZhidao('${userZhidao_center_tmpl_id}','${threeSpec.leader.S_USER}')" rel="nofollow" alog-action="famous-img">
					        	<@formatUserImg threeSpec.leader '' '' '' '' '65px' '65px' />
					        </a>
					      </div>
					      <div class="inner-right" style="width:100px;">
					      	<h1 style="height:40px;width:100%;font-size:14px; font-weight:700; color:rgb(51,51,51);">${threeSpec.leader.S_USER__NAME}</h1>
					      	<div>${threeSpec.leader.S_DEPT_NAME_1}</div>
					      	<div>${threeSpec.leader.S_DEPT_NAME_2}</div>
					      </div>
					    </div>
					    <div style="height:auto;">
					    	<div class="ask_font"></div><div threeQID='${threeSpec.leader.Q_ID}'>${threeSpec.leader.Q_TITLE}</div>
					    	<div class="answer_font"></div><div>${threeSpec.leader.A_CONTENT}</div>
					    </div>
					</div>
					
					<!--------------职能部门-------------------->
					<div id="dept" class="recommend-user" style="width:250px; float:left;">
						<div class="inner" style="height:110px;">
					     <div class="inner-left">
					        <div class="dept-logo"></div>
					        <a data-img="#" target="_self" href="javascript:othersZhidao('${userZhidao_center_tmpl_id}','${threeSpec.dept.S_USER}')" rel="nofollow" alog-action="famous-img">
					        	<@formatUserImg threeSpec.dept '' '' '' '' '65px' '65px' />
					        </a>
					      </div>
					      <div class="inner-right" style="width:100px;">
					      	<h1 style="height:40px;width:100%;font-size:14px; font-weight:700; color:rgb(51,51,51);">${threeSpec.dept.S_USER__NAME}</h1>
					      	<div>${threeSpec.dept.S_DEPT_NAME_1}</div>
					      	<div>${threeSpec.dept.S_DEPT_NAME_2}</div>
					      </div>
					    </div>
					    <div style="height:auto;">
					    	<div class="ask_font"></div><div threeQID='${threeSpec.dept.Q_ID}'>${threeSpec.dept.Q_TITLE}</div>
					    	<div class="answer_font"></div><div>${threeSpec.dept.A_CONTENT}</div>
					    </div>
					</div>
					
					<!--------------业务专家-------------------->
					<div id="busi" class="recommend-user" style="width:250px; float:left;">
						<div class="inner" style="height:110px;">
					     <div class="inner-left">
					        <div class="busi-logo"></div>
					        <a data-img="#" target="_self" href="javascript:othersZhidao('${userZhidao_center_tmpl_id}','${threeSpec.busi.S_USER}')" rel="nofollow" alog-action="famous-img">
					        	<@formatUserImg threeSpec.busi '' '' '' '' '65px' '65px' />
					        </a>
					      </div>
					      <div class="inner-right" style="width:100px;">
					      	<h1 style="height:40px;width:100%;font-size:14px; font-weight:700; color:rgb(51,51,51);">${threeSpec.busi.S_USER__NAME}</h1>
					      	<div>${threeSpec.busi.S_DEPT_NAME_1}</div>
					      	<div>${threeSpec.busi.S_DEPT_NAME_2}</div>
					      </div>
					    </div>
					    <div style="height:auto;">
					    	<div class="ask_font"></div><div threeQID='${threeSpec.busi.Q_ID}'>${threeSpec.busi.Q_TITLE}</div>
					    	<div class="answer_font"></div><div>${threeSpec.busi.A_CONTENT}</div>
					    </div>
					</div>
					</@zhidao_three_spec>
			</div>
		</div>
		<!-------------优秀专家排行----结束-------------------->
		
		<!------------分类专家排行-----开始-------------->
		<div class="mod rank tabs ui-tabs-aside user rank3" style="background-color:white;border-left:0px;border-right:0px;">
			<h3>分类专家排行</h3>
			<div class="hd topper">
				<ul class="tabControl">
					<li class="current show" num="1">
						<a>总积分</a>
					</li>
					<li class="disabled show" num="2">
						<a>本月</a>
					</li>
					<li class="disabled show" num="3">
						<a>本周</a>
					</li>
				</ul>
			</div>
			<div class="bd">
				<ul class="tabContent clearfix">
					<li class="current show_1" id="content_1">
						<ul>
						<@zhidao_integralTop debugName="总积分排行" type="total" count="5"> 
						<#list tag_list as integral>
						<li>
							<dl style="padding-top:10px;">
								<dt class="rank-color">
									<div class="custom-left">
									   	 <a rel="nofollow" href="javascript:othersZhidao('${userZhidao_center_tmpl_id}','${integral.USER.USER_CODE}')">
									        <img src="/sy/theme/default/images/common/rh-lady-icon.png" data-original="<@setUserImg integral.USER/>" style="1px solid #e5e5e5" width="60px;" height="60px;">
									    </a>
									    <a alog-action="user-rank-help" href="#" onclick="javascript:loadTiWen('${integral.USER_NAME}','${integral.USER.USER_CODE}');"
										class="btn btn-24-white fixed-ask">
											<em>
												<b style="padding:0 5px;">
													<span>向TA求助</span>
												</b>
											</em>
										</a>
									</div>
								</dt>
								<dd>
									<div class="custom-right">
										<a href="javascript:othersZhidao('${userZhidao_center_tmpl_id}','${integral.USER.USER_CODE}')">${integral.USER_NAME}</a>
										<p>积分：${integral.INTEGRAL_VALUE}</p>
									</div>	 
								</dd>
							</dl>
						</li>
						</#list>
						</@zhidao_integralTop>
						</ul>
					</li>
					<li class="disabled show_2" id="content_2">
						<ul>
							<@zhidao_integralTop debugName="本月积分排行" type="month" count="5">
							<#list tag_list as integral>
							<li>
							   <dl style="padding-top:10px;">
									<dt class="rank-color">
										<div class="custom-left">
											   	 <a rel="nofollow" href="javascript:othersZhidao('${userZhidao_center_tmpl_id}','${integral.USER.USER_CODE}');">
											        <img src="/sy/theme/default/images/common/rh-lady-icon.png" data-original="<@setUserImg integral.USER/>" style="1px solid #e5e5e5" width="60px;" height="60px;">
											    </a>
											    <a alog-action="user-rank-help" href="#" onclick="javascript:loadTiWen('${integral.USER_NAME}','${integral.USER.USER_CODE}');" 
												class="btn btn-24-white fixed-ask">
													<em>
														<b style="padding:0 5px;">
															<span>向TA求助</span>
														</b>
													</em>
												</a>
										</div>
									</dt>
									<dd>
										<div class="custom-right">
												<a href="javascript:othersZhidao('${userZhidao_center_tmpl_id}','${integral.USER.USER_CODE}')">${integral.USER.USER_NAME}</a>
												<p>积分：${integral.SCORE}</p>
											</div>	
									</dd>
								 </dl>
								</li>
							</#list>
							</@zhidao_integralTop>
							</ul>
					 </li>
					 <li class="disabled show_3" id="content_3">
						<ul>
							<@zhidao_integralTop debugName="本周积分排行" type="week" count="5"> 
							<#list tag_list as integral>
							<li>
							   <dl style="padding-top:10px;">
									<dt class="rank-color">
										<div class="custom-left">
											   	 <a rel="nofollow" href="javascript:othersZhidao('${userZhidao_center_tmpl_id}','${integral.USER.USER_CODE}');">
											        <img src="/sy/theme/default/images/common/rh-lady-icon.png" data-original="<@setUserImg integral.USER/>" style="1px solid #e5e5e5" width="60px;" height="60px;">
											    </a>
											    <a alog-action="user-rank-help" href="#" onclick="javascript:loadTiWen('${integral.USER_NAME}','${integral.USER.USER_CODE}');"
												class="btn btn-24-white fixed-ask">
													<em>
														<b style="padding:0 5px;">
															<span>向TA求助</span>
														</b>
													</em>
												</a>
											</div>
									</dt>
									<dd>
										<div class="custom-right">
												<a href="javascript:othersZhidao('${userZhidao_center_tmpl_id}','${integral.USER.USER_CODE}')">${integral.USER_NAME}</a>
												<p>积分：${integral.SCORE}</p>
											</div>	
									</dd>
								 </dl>
								</li>
							</#list>
							</@zhidao_integralTop>
							</ul>
						</li>
				</ul>
			</div>
		</div>
		<!------------分类专家排行-----结束-------------->
		
		
	</div>
	<!-------------------后加的右侧边栏--------结束----------------------->
	<!---------------返回顶部的div------开始--------------->
	<div id="zd-goto-top"></div>
	<!---------------返回顶部的div------结束--------------->
<!-----------------footer------------------------->
</div>
<!------------------原来文库的三个tab页切换的功能-------开始------------------------------>
<script>
	jQuery(document).ready(function(){
		//tab切换的图片加载事件
		jQuery(window).bind("scroll",function(){
			var scrollTop = window.pageYOffset  
			                || document.documentElement.scrollTop  
			                || document.body.scrollTop  
			                || 0;
			if(scrollTop >= 500){
				jQuery(".tabContent li img").each(function(index,img){
					if(jQuery(img).attr("src") == '/sy/theme/default/images/common/rh-lady-icon.png' && jQuery(img).attr("data-original") != '/sy/theme/default/images/common/rh-lady-icon.png'){
						jQuery(img).attr("src",jQuery(img).attr("data-original"));
					}
				});
			}
		});
		
		//tab切换的方法
		jQuery(".show").mouseover(function(){
			jQuery(".current").removeClass("current").addClass("disabled");
			var num = jQuery(this).addClass("current").attr("num");
			jQuery("#content_"+num).addClass("current");
		})
	});
</script>
<!------------------原来文库的三个tab页切换的功能-------开始------------------------------>
<!--end of container---->
	</body>
</html>
