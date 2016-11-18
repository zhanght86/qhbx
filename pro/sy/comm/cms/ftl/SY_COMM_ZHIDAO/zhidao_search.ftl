<!-----提问需要引入的文件----开始----->
<link rel="stylesheet" type="text/css" href="/sy/plug/search/rhSearchResult.css">
<link rel="stylesheet" href="/sy/comm/zhidao/zhihu_style_files/c60843d5fb7d7aeb2bfa3619734d9b18.css" type="text/css" media="screen,print">
<link rel="stylesheet" type="text/css" href="/sy/comm/zhidao/css/zhidao_ask.css">
<script type="text/javascript" src="/sy/comm/zhidao/js/zhidao_ask.js"></script>
<script>
	var center_temp_id = '${userZhidao_center_tmpl_id}';
</script>
<!-----提问需要引入的文件----开始----->
<header id="header" class="container">
	<div id="search-box" class="search-box line pt-20" style="padding-top:3px;">
	<div class="logo" style="margin-top:10px;">
	</div>
		<ul class="channel" style="width:500px;">
			<!-- channel --> 
		</ul>
		<form action="/SY_PLUG_SEARCH.query.do" name="searchForm" method="get" id="searchForm"
		class="search-form" style="margin-top:18px;">
			<div class="box" style="position:relative;width:580px">
				<span class="search-box-bg">
				</span>
				<input class="hdi" id="kw" maxlength="256" tabindex="1" size="46" name="word">
				<span class="btn-wrap">
					<input alog-action="g-search-anwser" id="search-btn" hidefocus="true"
						onclick="javascript:search();"
					onmouseover="this.className='btn-global btn-hover';" onmouseout="this.className='btn-global';"
					onmousedown="this.className='btn-global btn-active'" tabindex="2" value="搜索答案"
					class="btn-global" type="submit">
				</span>
				
			</div>
			<input name="data" id="data" value="" type="hidden">
		</form>
		<span>提问<b id="ask_num" class="updoc-number-font"></b>/回答<b id="answer_num" class="updoc-number-font"></b></span>
		<a href="javascript:loadTiWen();" class="img"></a>
	</div>
	
	<script>
		//页面加载完成后查询出所有已解答的问题数量
		jQuery(document).ready(function(){
			//查询所有的问题个数
			var questionCount = parent.FireFly.doAct("SY_COMM_ZHIDAO","getAskCount",{},false)["_OKCOUNT_"];
			var answerCount = parent.FireFly.doAct("SY_COMM_ZHIDAO","getAnswerCount",{},false)["_OKCOUNT_"];
			jQuery("#ask_num").text(questionCount);
			jQuery("#answer_num").text(answerCount);
		});
	</script>
</header>