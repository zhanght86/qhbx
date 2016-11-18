<link rel="stylesheet" type="text/css" href="/sy/comm/wenku/baidu_style_files/baiduWenKu_zjw_nav.css">
<script type='text/javascript'>
	$(document).ready(function(){
		var selMenu ="${selectMenu}";
		if(selMenu.length>0&&selMenu!='first'){
			$("#"+selMenu).attr("class","four current");
			$("#nav-index").attr("class","first");
		}else if(selMenu=='first'){
			$("#nav-index").attr("class","first current");
		}
		
		jQuery("#myWenku").hover(
			function(){
				$("#imgId").attr("src","/sy/comm/wenku/img/selected.png");
			},
			function(){
				$("#imgId").attr("src","/sy/comm/wenku/img/notSelect.png");
			}
		);
		
	});
</script>
<!----------------全部分类的css--开始----------------------------->
<style>
	.ui-nav ul li b.d-ic {
	    border-color: #FFFFFF #1E9966;
	    border-style: solid;
	    border-width: 4px 4px 0;
	    height: 0;
	    left: 195px;
	    overflow: hidden;
	    position: absolute;
	    top: 15px;
	    width: 0;
	    z-index: 12;
	}
	.ui-nav ul li:hover b.d-ic, .ui-nav ul li.current b.d-ic {
	    transform: rotate(180deg);
	    transform-origin: 50% 30% 0;
	    transition: transform 0.2s ease-in 0s;
	}
	.ui-nav ul li:hover b.d-ic {
	    border-color: #FFFFFF #168E5C;
	}
	.ui-nav ul li.current b.d-ic {
	    border-color: #FFFFFF #12784E;
	}

	.ui-nav ul li .drop-sub-nav {
	    -moz-border-bottom-colors: none;
	    -moz-border-left-colors: none;
	    -moz-border-right-colors: none;
	    -moz-border-top-colors: none;
	    background-color: #FFFFFF;
	    border-color: -moz-use-text-color #14804E #14804E;
	    border-image: none;
	    border-right: 1px solid #14804E;
	    border-style: none solid solid;
	    border-width: 0 1px 1px;
	    display: none;
	    font-weight: normal;
	    left: 110px;
	    margin-right: -2px;
	    overflow: hidden;
	    padding: 3px 0 3px;
	    top: 34px;
	}
	.ui-nav ul li .drop-sub-nav a {
		background-color:#FFFFFF;
	    background-image: none;
	    border: 0 none;
	    color: #333333;
	    display: inline-block;
	    padding: 0 auto;
	    
	    white-space: nowrap;
	}
	.ui-nav ul li .drop-sub-nav a:hover {
	    background-color: #EAF7EE;
	    background-image: none;
	    border: 0 none;
	    color: #117A51;
	}
	.ui-sub-nav {
	    -moz-border-bottom-colors: none;
	    -moz-border-left-colors: none;
	    -moz-border-right-colors: none;
	    -moz-border-top-colors: none;
	    border-color: -moz-use-text-color #CCCCCC #CCCCCC;
	    border-image: none;
	    border-right: 1px solid #CCCCCC;
	    border-style: none solid solid;
	    border-width: 0 1px 1px;
	    height: 30px;
	    line-height: 30px;
	    padding: 0 20px;
	}
	.ui-sub-nav li {
	    float: left;
	    position: relative;
	}
	.ui-sub-nav li a {
	    color: #FFFFFF;
	    background-color:#FFFFFF;
	}
	.ui-sub-nav .line {
	    color: #CCCCCC;
	    padding: 0 20px;
	}									
</style>
<!----------------全部分类的css--结束----------------------------->

<div class="nav-wrap mb10">
	<div class="ui-nav">
		<div class="inner clearfix">
			<ul>
				<li id="nav-index" class="first ">
					<a class="fst ac" href="<@chnlUrl root_channel_id 1 />">
						<span>文库首页</span>
					</a>
				</li>
				
				<!---------新加的一个全部分类--开始------------>
				<li id="zone-menu" class="four">
					<a href="javascript:void(0);" id="fenlei_a" class="ac"><span>全部分类</span></a>
					
					<b class="d-ic"></b>
					<div id="Zuniqueid__1" class="drop-sub-nav" style="z-index:9030; width:100px; display:none; ">
						
						<@channel_list debugName="首页导航" channelId="${root_channel_id}"> 
						<#list tag_list as channel>
						<a id="nav-topic" style="width:100px; height:36px; z-index:9000;" href="<@chnlUrl channel.CHNL_ID 1 />">${channel.CHNL_NAME}</a>
						</#list>
						 </@channel_list>
						
					</div>
				</li>
				<!---------新加的一个全部分类--结束------------>
				
				<li id="nav-index" class="four ">
					<a class="fst ac" href="<@tmplUrl hot_document_tmpl_id />">
						<span>热门文档</span>
					</a>
				</li>
				
				<li id="nav-index" class="four ">
					<a class="fst ac" href="<@tmplUrl hot_doclist_tmpl_id />">
						<span>热门文辑</span>
					</a>
				</li>
				
				<li id="nav-index" class="four ">
					<a class="fst ac" href="<@tmplUrl new_document_tmpl_id />">
						<span>最新文档</span>
					</a>
				</li>
				
				<li id="nav-index" class="four ">
					<a class="fst ac" href="<@tmplUrl new_doclist_tmpl_id />">
						<span>最新文辑</span>
					</a>
				</li>
				<!--[if IE 7]>
				<li style="margin-left:15%;float:left;display:block;background-image:none;background-color:rgb(29,150,101);">
				<a href="#" onclick="javascript:myWenkuCenter();" target="_self" id="myWenku" class="logSend">
				<span>
					<img id="imgId" alt="我的文库" src="/sy/comm/wenku/img/notSelect.png" />
				</span>
				</a>
				</li>
				<script>
					jQuery(document).ready(function(){
						jQuery("#nav-uc").remove();
					});
				</script>
				<![endif]-->
				
				<p id="nav-uc" class="four logSend" style="margin-left:15%;display:block;float:left;">
				<a href="#" onclick="javascript:myWenkuCenter();" target="_self" id="myWenku" class="logSend">
				<span>
					<img id="imgId" alt="我的文库" style="padding-top:5px;" src="/sy/comm/wenku/img/notSelect.png" />
				</span>
				</a>
				</p>
			</ul>
			<!-- 
			<div class="new-tip"></div>
			 -->
		</div>
	</div>
	
</div>

				<!------------增加的script--开始--------------------->
				<script>
					jQuery("#fenlei_a").hover(function(){jQuery("#Zuniqueid__1").css("display","block")},function(){jQuery("#Zuniqueid__1").css("display","none")});
					jQuery("#Zuniqueid__1").hover(function(){jQuery("#Zuniqueid__1").css("display","block")},function(){jQuery("#Zuniqueid__1").css("display","none")});
				</script>
				<!-------------增加的script--结束--------------------->