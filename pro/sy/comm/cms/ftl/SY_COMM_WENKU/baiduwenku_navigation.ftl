<link rel="stylesheet" type="text/css" href="/sy/comm/wenku/baidu_style_files/baiduWenKu_zjw_nav.css">
<style>
.outside{
	background-clip: border-box;
    background-color: transparent;
    background-image: url("/sy/comm/wenku/baidu_style_files/wenku_index_imgs.png");
    background-origin: padding-box;
    background-position: 0 -190px;
    background-size: auto auto;
    font-size: 12px;
    height: 42px;
    width: 1200px;
    margin-left:0px;
    margin-bottom:20px;
}
.leftFloat{
	float:left;
	text-align:center;
	line-height:30px;
	height:30px;
	padding-left:10px;padding-right:10px;
	color:black;
}
.insideUpload{
	width:160px;
	height:23px;
	margin-top:5px;
	background:url(/sy/comm/wenku/baidu_style_files/wenku_index_imgs.png);
	background-position: 0 -240px;
	background-repeat: no-repeat;
	float:right;
	display:block;
	padding-right:0px;
	padding-left:0px;
}
#uploadA:hover{
	background-position: 0 -264px;
}
.fontA{
	color:black;
}
.fontA:hover{
	text-decoration:none;
}
.floatCategory{
	background: none repeat scroll 0 0 black;
    min-height: 30px;
    left: 140px;
    position: absolute;
    top: 6px;
    max-width: 700px;
    z-index:700;
    opacity:0.8;
}
</style>
<div class="outside">
	<ul style="width:1160px;height:30px;margin-left:20px;margin-top:6px;">
		<li class="leftFloat"><a href="<@chnlUrl root_channel_id 1 />" class="fontA">文档中心首页</a></li>
		<li class="leftFloat">|</li>
		<li class="leftFloat"><a id="allCategory" href="#" class="fontA">全部分类</a></li>
		<li class="leftFloat">|</li>
		<li class="leftFloat"><a href="<@tmplUrl hot_document_tmpl_id />" class="fontA">热门文档</a></li>
		<li class="leftFloat">|</li>
		<li class="leftFloat"><a href="<@tmplUrl new_document_tmpl_id />" class="fontA">最新文档</a></li>
		<li class="leftFloat">|</li>
		<li class="leftFloat"><a href="#" onclick="javascript:myWenkuCenter();" class="fontA">个人中心</a></li>
		<li class="leftFloat" style="float:right;padding-right:0px;"><a href="#" onclick="javascript:upload('${upload_tmpl_id}','${site_id}')" id="uploadA" class="insideUpload"></a></li>
	</ul>
</div>
<div class="floatCategory" style="display:none;">
	<ul>
		<@channel_list debugName="首页导航" channelId="${root_channel_id}"> 
			<li class="leftFloat"><a id="allCategory" href="#" class="fontA" style="color:white;">全部分类</a></li>
			<#list tag_list as channel>
				<li class="leftFloat" style="color:white;">|</li>
				<li class="leftFloat"><a href="<@chnlUrl channel.CHNL_ID 1 />" class="fontA" style="color:white;">${channel.CHNL_NAME}(<span class="${channel.CHNL_ID}">0</span>)</a></li>
			</#list>
		</@channel_list>
	</ul>
</div>
<#-- 1.给全部分类添加效果 -->
<#-- 2.给每个分类添加文档数 -->
<script>
	jQuery(document).ready(function(){
		jQuery("#allCategory").bind("mouseenter",function(){
			jQuery(".floatCategory").show(500);
		});
		jQuery(".floatCategory").bind("mouseleave",function(){
			jQuery(this).hide(500);
		});
		
		var datas = {};
	    datas["_NOPAGE_"] = true;
		datas["_SELECT_"] = "DOCUMENT_CHNL , COUNT(DOCUMENT_ID) DOC_COUNT";
		datas["_searchWhere"] = " GROUP BY DOCUMENT_CHNL";
		
		var result = parent.FireFly.getListData("SY_COMM_WENKU_DOCUMENT", datas)._DATA_;
		jQuery(result).each(function(index, item) {
			jQuery("." + item["DOCUMENT_CHNL"]).html(item["DOC_COUNT"]);
		});
		
	});
</script>
