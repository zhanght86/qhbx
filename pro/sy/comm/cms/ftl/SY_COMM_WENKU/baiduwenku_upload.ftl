<style type='text/css'>
	.zjw-baiduwenku-updoc {float: right;width: 240px;margin: 0 0 0 0;display: inline;text-align: center;}
	.zjw-baiduwenku-updoc a{display: inline-block;margin-top: 2px;width: 145px;height: 32px;background: url(/sy/comm/wenku/baidu_style_files/allbg_cd6bd677.png) no-repeat left -48px;}
	.zjw-baiduwenku-updoc a:hover{background: url(/sy/comm/wenku/baidu_style_files/allbg_cd6bd677.png) no-repeat left -83px;}
	.updoc-span-font{color: #999;font-size: 12px;}
	.updoc-number-font{color: #e87301;font-weight: bold;margin: 0 5px;font-size: 16px;font-family: verdana, arial, sans-serif;word-spacing: 15px;}
</style>
<div class="clearfix">
	<div class="zjw-baiduwenku-updoc">
		<@wenku_total debugName="文档统计" siteId="${site_id}"> <span class='updoc-span-font'>当前已有<b class='updoc-number-font'>${total}</b>份文档
		</span>
		 </@wenku_total>
		  <a href="#" onclick="javascript:upload('${upload_tmpl_id}','${site_id}')" target="_self" id="upload-icon"
			data-logsend="{&quot;send&quot;:[&quot;home&quot;,&quot;homeclk&quot;,{&quot;subtype&quot;:&quot;upload&quot;,&quot;login&quot;:&quot;1&quot;}]}"></a>
	</div>
</div>