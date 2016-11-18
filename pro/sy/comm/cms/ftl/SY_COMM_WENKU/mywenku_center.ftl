<!DOCTYPE html>
<!--STATUS OK-->
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta http-equiv="X-UA-Compatible" content="IE=Edge">
<meta name="description" content="在线互动式文档分享平台，在这里，您可以和千万网友分享自己手中的文档，全文阅读其他用户的文档 ">
<title>全部文档</title>
<#include "/SY_COMM_WENKU/config_constant.ftl">
<link rel="stylesheet" type="text/css" href="/sy/comm/wenku/baidu_style_files/framework_nt_9e7e1a8e.css">
<link rel="stylesheet" type="text/css" href="/sy/comm/wenku/baidu_style_files/index_ba87abf5.css">
<link rel="stylesheet" type="text/css" href="/sy/comm/wenku/css/mywenku_center.css">
<script type="text/javascript"> var FireFlyContextPath =''; </script>
<script type="text/javascript" src="/sy/base/frame/jquery-1.8.2.min.js"></script>
<script type="text/javascript" src="/sy/base/frame/constant.js"></script>
<script type="text/javascript" src="/sy/base/frame/tools.js"></script>
<script type="text/javascript" src="/sy/base/frame/platform.js"></script>
<script type="text/javascript" src="/sy/base/frame/coms/rh.ui.openTab.js"></script>
<script type="text/javascript" src="/sy/comm/cms/js/wenku.js"></script>
<script type="text/javascript" src="/sy/comm/wenku/baidu_style_files/base_75690991.js"></script>
<script type="text/javascript" src="/sy/comm/wenku/baidu_style_files/wenku.js"></script>
<script type="text/javascript" src="/sy/comm/wenku/js/mywenku_center.js"></script>
</head>
<body>
<div id="body" style="background-color:#FFF!important;">	
	<div id="doc" class="page" style="margin:20px;width:890px;">

	<div class="main" style="">
	    <div class="base-info">
	        <div class="hd">
	            <span class="act">
	                 
	            </span>
	            <h3>
	                基本信息
	            </h3>
	            <span class="title-bg">
	            </span>
	        </div>
	        <div class="bd">
	          <div style="width:100%;height:30px;">
	           <div style="float:left;display:inline;width:18%;">
	        	<table style='border-collapse:separate;border-spacing:5px;'>
					<tr>
						<td>
							<img id='currentUsImg' style="width:auto;height:50px;" src="/sy/theme/default/images/common/user1.png" class="rh-user-info-circular-bead">
						</td>
						<td>
							<span id='currentUsName' style='font-weight: 600;color: #666;float: left;'>系统管理员</span>
						</td>
					</tr>
				</table>
				</div>
	            <div class="data" style="width:50%;float:left;margin-left:1%;">
	                <table>
	                    <tbody>
	                        <tr>
	                            <td>
	                              	 积分值
	                                <br>
	                                <strong class="c-orange wealth">
	                                     <a href='#' target='_self' style="color: #e87301;"></a>
	                                </strong>
	                            </td>
	                            <td>
	                               	 公共文档
	                                <br>
	                                <strong class="c-orange public">
	                                     <a href='#' target='_self' style="color: #e87301;"></a>
	                                </strong>
	                            </td>
	                        </tr>
	                    </tbody>
	                </table>
	            </div>
	           </div>  
	        </div>
	    </div>
    <!-------------end of baseInfo------------------->
	    <div class="uc-mod course-info">
	        <div class="hd">
	            <span class="act">
	                <a href="#" id="myContribution-more" onclick="javascript:myDocuments();">
	                    更多
	                </a>
	            </span>
		        <h3 style="display:inline;"> 我的贡献 </h3>
		        <div class="tabControl uc-tabs-control">
	                <a class="current" href="#js-doc">文档贡献</a>
	            </div>
	        </div>
	       <div class="bd content" id="js-doc">
		    <div class="gd-g gd-g-head">
		        <div class="gd-g-u gd-u-title" id="custom-name">文档名称</div>
		        <div class="gd-g-u wp10">浏览量</div>
		        <div class="gd-g-u wp10">文档评分</div>
		        <div class="gd-g-u wp10 tc">状态</div>
		        <div class="gd-g-u wp15">上传时间</div>
		    </div>
		     <ul class="tabContent uc-tabs-content">
			    <li id="doc-list">
			    </li>
			 </ul>
		  </div>
		  
		  <!--
		  <div class="bd content" id="js-doclist" style="display:none;">
		    <div class="gd-g gd-g-head">
		        <div class="gd-g-u gd-u-title" id="custom-name">文辑名称</div>
		        <div class="gd-g-u wp10">浏览量</div>
		        <div class="gd-g-u wp10">文辑评分</div>
		        <div class="gd-g-u wp10 tc">状态</div>
		        <div class="gd-g-u wp15">上传时间</div>
		    </div>
		     <ul class="tabContent uc-tabs-content">
			    <li id="doclist-list"></li>
			 </ul>
		  </div>
		  -->
		  
	    </div>
    <!----------------end of 我的贡献-------------------------->
    <!----------------start of 我下载的文档------------------------->
	    <div class="uc-mod contribute-info mt20">
	        <div class="hd">
	            <span class="act">
	                <a href="#" onclick="javascript:myDownload();">
	                    更多
	                </a>
	            </span>
	            <h3>
	                我下载的文档
	            </h3>
	        </div>
	        <div class="bd" id="myDownload">
				    <div class="gd-g gd-g-head">
				        <div class="gd-g-u gd-u-title">文档名称</div>
				        <div class="gd-g-u wp10">文档下载量</div>
				        <div class="gd-g-u wp10">文档评分</div>
				        <div class="gd-g-u wp10 tc">上传者</div>
				        <div class="gd-g-u wp15">下载时间</div>
				    </div>
				</div>
	    </div>
	     <!----------------start of 最近浏览------------------------->
	    <div class="uc-mod contribute-info mt20">
	        <div class="hd">
	        	<span class="act">
	                <a href="#" onclick="javascript:myReadHis();">
	                    更多
	                </a>
	            </span>
	            <h3>
	                    最近浏览
	            </h3>
	        </div>
	        <div class="bd" id="recentlyRead">
			    <div class="gd-g gd-g-head">
			        <div class="gd-g-u gd-u-title">文档名称</div>
			        <div class="gd-g-u wp10">文档浏览量</div>
			        <div class="gd-g-u wp10">文档评分</div>
			        <div class="gd-g-u wp10 tc">上传者</div>
			        <div class="gd-g-u wp15">浏览时间</div>
			    </div>
			</div>
        <!----------------end of 最近浏览------------------------->
	         
	    </div> 
    <!----------------end of 上传文档--------------------->
    <!-- my wenku info -->
</div>

	 
 </div>
 </div>
	<#include "/SY_COMM_WENKU/baiduwenku_footer.ftl">
</body>
<!---------------------控制外层滚动条-------开始------------------->
<script>
	//将当前iFrame中的Id设置成全局变量，因为在Tab中要用到它来定位高度
	var pid = $(window.top.document).find(".ui-tabs-selected").attr("pretabid");
	var windowName = $("#"+pid,window.top.document).children("iframe").attr("id");
	GLOBAL.setFrameId(windowName);
	jQuery(document).ready(function(){
		//Tab对象的方法，用来定位页面的高度
		Tab.setFrameHei(10);
		Tab.setFrameHei();
	});
</script>
<!---------------------控制外层滚动条-------结束------------------->

</html>