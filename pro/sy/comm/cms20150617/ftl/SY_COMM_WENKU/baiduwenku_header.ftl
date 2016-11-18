<link rel="stylesheet" type="text/css" href="/sy/comm/wenku/baidu_style_files/baiduWenKu_zjw_header.css">
<link rel="stylesheet" type="text/css" href="/sy/comm/wenku/css/wenku_icon.css"/>
<div id="hd">
	<div class="box top-search-box ">
		<div class="media">
		<a href="#">
		<img width="167" height="63" alt="软虹文库_文档分享平台" src="/sy/comm/wenku/baidu_style_files/logo-wk-137-46_3164f22b.png">
		</a>
		</div>
		<div style="z-index: 10; padding-left:35px;" class="content">
			<div class="s_nav"></div>
			<!--------更改上传文件按钮不能左边不能出现手势图标的原因，将临近的元素的宽度改小，width:660px改成width:590px------------->
			<form style="z-index: 10; width: 590px;" action="/SY_PLUG_SEARCH.query.do" id="searchForm"
				name="ftop1" id="topSearchBox" method="get">
				
				<span class="s_ipt_wr"> 
					<input type="text" id="kw" name="word" class="s_ipt" maxlength="256" tabindex="1" autocomplete="off">
				</span>
				<span class="s_btn_wr"> 
					<input type="submit" id="sb" value="搜索文档"
						class="s_btn" onmouseover="this.className='s_btn s_btn_h'"
						onclick="javascript:search(this);"
						onmousedown="this.className='s_btn s_btn_d'"
						onmouseout="this.className='s_btn'" >
				</span>
				<div class="g-sl">
					<input type="radio"name="lm" value="0" id="bot_all" checked="checked">
					<label for="bot_all">全部</label> 
					
					<input type="radio" name="lm" value="1" id="bot_doc"> 
					<label for="bot_doc">DOC</label>
					
					<input type="radio" name="lm" value="3" id="bot_ppt">
					<label for="bot_ppt">PPT</label> 
					
					<input type="radio" name="lm" value="5" id="bot_txt">
					<label for="bot_txt">TXT</label>
					
					<input type="radio" name="lm" value="2" id="bot_pdf">
					<label for="bot_pdf">PDF</label>
					
					<input type="radio" name="lm" value="4" id="bot_xls" >
					<label for="bot_xls">XLS</label>
					
					<div style="clear: both"></div>
				</div>
				<input type="hidden" name="od" value="0" > 
				<input type="hidden" name="fr" value="top_home">
				<input type="hidden" name="data" id="data" value="">
			</form>
		</div>	
		<!--[if IE 7]>
		<script>
			jQuery(document).ready(function(){
				jQuery("#ie-7").css("margin-left","0px");
			});
		</script>
		<![endif]-->
		<div class='cb' id="ie-7" style="position:absolute;margin-left:700px;margin-top:-70px">
					<#include "/SY_COMM_WENKU/baiduwenku_upload.ftl">
		</div>	
	</div>				
</div>