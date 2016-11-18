<link rel="stylesheet" type="text/css" href="/bn/employee/css/employee.css"/>
<link rel="stylesheet" type="text/css" href="/bn/style/css/common.css"/>
<script>
function newsView(id){
		var url = "/cms/SY_COMM_INFOS/" + id + ".html";			
		window.open(url);
	}
</script>
<style>
.sub_conlr p{height:auto;width: auto;}
.newslists ul li{background:none;height:auto;}
.banner-bg .banner-bg-bn{position:absolute;top:42px;left:216px;z-index:1;height:45px;width:165px;background:url(/bn/style/images/bainian_bg.gif) no-repeat center;background-size:100% 100%;}
.newslists ul li{padding-left:0px;line-height:20px;border-bottom:0px solid;overflow:visible;text-align:left;}
</style>
<div class="employeeDiv">
<div class="banner-bg">
	<div class="banner-bg-bn"></div>
</div>
<div class="banner-bg-banner"></div>
<div class="content" style="position:relative; z-index:2;">
        <div class="sub_main">
        	<div class="f_l sub_left">
	<h2>关于百年</h2>
	<ul class="sub_lflist">
		
		<li onclick="changeEmpBox(this)"><a href="javascript:void(0);"  title="${_DATA_0.CHNL_NAME}" target="_self">${_DATA_0.CHNL_NAME}</a></li>
		
		<li class="first" onclick="changeEmpBox(this)"><a href="javascript:void(0);"  title="${_DATA_1.CHNL_NAME}" target="_self">${_DATA_1.CHNL_NAME}</a></li>
		
		<li onclick="changeEmpBox(this)"><a href="javascript:void(0);" title="${_DATA_2.CHNL_NAME}" target="_self">${_DATA_2.CHNL_NAME}</a></li>
		
		<li onclick="changeEmpBox(this)"><a href="javascript:void(0);" title="${_DATA_3.CHNL_NAME}" target="_self">${_DATA_3.CHNL_NAME}</a></li>
		
		<li onclick="changeEmpBox(this)"><a href="javascript:void(0);" title="${_DATA_4.CHNL_NAME}" target="_self">${_DATA_4.CHNL_NAME}</a></li>

		<li onclick="changeEmpBox(this)"><a href="javascript:void(0);" title="${_DATA_5.CHNL_NAME}" target="_self">${_DATA_5.CHNL_NAME}</a></li>
		
		
	</ul>
    <div class="cl"></div>
    <div><img src="/bn/employee/img/left_titbgbot.jpg"></div>
</div>
            <div class="f_l sub_conlr">
            	<h2>
                	<strong id="chnlName">公司股东</strong>
                    <div class="lu" style="font-weight:normal;">
                        <a href="/">首页</a> &gt; <a href="javascript:void(0);">关于百年</a> &gt; <a href="#">公司股东</a> 
                    </div>
                </h2>
				<div class="cl ht10"></div>
                <div class="newslists">
					 <ul chnlId="${_DATA_0.CHNL_ID}" num="0" class="none">
					 <#list _DATA_0._DATA_ as content>
						<#if (content.NEWS_BODY?length<=1050)>
							<li><a id = '${CHNL_ID}${content.NEWS_ID}' href="####" onclick="newsView('${content.NEWS_ID}')" title="${content.NEWS_SUBJECT}" >${content.NEWS_BODY}</a></li>
						<#else>
							<li><a id = '${CHNL_ID}${content.NEWS_ID}' href="####" onclick="newsView('${content.NEWS_ID}')" title="${content.NEWS_SUBJECT}" >${content.NEWS_BODY?substring(0,1050)}...</a></li>
						</#if>
					 </#list> 
					</ul>
					<ul chnlId="${_DATA_1.CHNL_ID}" num="1" class="none block" >
					 <#list _DATA_1._DATA_ as content>
						<#if (content.NEWS_BODY?length<=1050)>
							<li><a id = '${CHNL_ID}${content.NEWS_ID}' href="####" onclick="newsView('${content.NEWS_ID}')" title="${content.NEWS_SUBJECT}" >${content.NEWS_BODY}</a></li>
						<#else>
							<li><a id = '${CHNL_ID}${content.NEWS_ID}' href="####" onclick="newsView('${content.NEWS_ID}')" title="${content.NEWS_SUBJECT}" >${content.NEWS_BODY?substring(0,1050)}...</a></li>
						</#if>
					 </#list>
					</ul>
					<ul chnlId="${_DATA_2.CHNL_ID}" num="2" class="none">
					 <#list _DATA_2._DATA_ as content>
						<#if (content.NEWS_BODY?length<=1050)>
							<li><a id = '${CHNL_ID}${content.NEWS_ID}' href="####" onclick="newsView('${content.NEWS_ID}')" title="${content.NEWS_SUBJECT}" >${content.NEWS_BODY}</a></li>
						<#else>
							<li><a id = '${CHNL_ID}${content.NEWS_ID}' href="####" onclick="newsView('${content.NEWS_ID}')" title="${content.NEWS_SUBJECT}" >${content.NEWS_BODY?substring(0,1050)}...</a></li>
						</#if>
					 </#list>
					</ul>
					<ul chnlId="${_DATA_3.CHNL_ID}" num="3" class="none">
					 <#list _DATA_3._DATA_ as content>
						<#if (content.NEWS_BODY?length<=1050)>
							<li><a id = '${CHNL_ID}${content.NEWS_ID}' href="####" onclick="newsView('${content.NEWS_ID}')" title="${content.NEWS_SUBJECT}" >${content.NEWS_BODY}</a></li>
						<#else>
							<li><a id = '${CHNL_ID}${content.NEWS_ID}' href="####" onclick="newsView('${content.NEWS_ID}')" title="${content.NEWS_SUBJECT}" >${content.NEWS_BODY?substring(0,1050)}...</a></li>
						</#if>
					 </#list>
					</ul>
					<ul chnlId="${_DATA_4.CHNL_ID}" num="4" class="none">
					 <#list _DATA_4._DATA_ as content>
						<#if (content.NEWS_BODY?length<=1050)>
							<li><a id = '${CHNL_ID}${content.NEWS_ID}' href="####" onclick="newsView('${content.NEWS_ID}')" title="${content.NEWS_SUBJECT}" >${content.NEWS_BODY}</a></li>
						<#else>
							<li><a id = '${CHNL_ID}${content.NEWS_ID}' href="####" onclick="newsView('${content.NEWS_ID}')" title="${content.NEWS_SUBJECT}" >${content.NEWS_BODY?substring(0,1050)}...</a></li>
						</#if>
					 </#list>
					</ul>
					<ul chnlId="${_DATA_5.CHNL_ID}" num="5" class="none">
					 <#list _DATA_5._DATA_ as content>
						<#if (content.NEWS_BODY?length<=1050)>
							<li><a id = '${CHNL_ID}${content.NEWS_ID}' href="####" onclick="newsView('${content.NEWS_ID}')" title="${content.NEWS_SUBJECT}" >${content.NEWS_BODY}</a></li>
						<#else>
							<li><a id = '${CHNL_ID}${content.NEWS_ID}' href="####" onclick="newsView('${content.NEWS_ID}')" title="${content.NEWS_SUBJECT}" >${content.NEWS_BODY?substring(0,1050)}...</a></li>
						</#if>
					 </#list>
					</ul>
					
				</div>
                
            </div>
        </div>
    </div>
<script type="text/javascript">
jQuery(document).ready(function(jQuery) {
		
		var num = jQuery(".block").attr("num");
		
	});
function changeEmpBox(obj){
	var MsgInx = $(obj).index();
	$("#chnlName").html($(obj).children().attr("title"));
	$(".lu").html("<a href='/'>首页</a> &gt; <a href='javascript:void(0);'>关于百年</a> &gt; <a href='#'>"+$(obj).children().attr('title')+"</a>");
	$(obj).addClass("first");
	$(obj).siblings().removeClass("first");
	$(obj).parent().parent().siblings(".f_l").children(".newslists").children().removeClass("block");
	$(obj).parent().parent().siblings(".f_l").children(".newslists").children().eq(MsgInx).addClass("block");
	
	
}
function newsView1(element){
var id = $(element).attr("news-id");
		var url = "/cms/SY_COMM_INFOS/" + id + ".html";			
		window.open(url);
	}
function showPage(num){
	if(num == 0){
		var allNum = "${_DATA_0.ALLNUM}";
		showOption(allNum);
	}else if(num == 1){
		var allNum = "${_DATA_1.ALLNUM}";
		showOption(allNum);
	}else if(num == 2){
		var allNum = "${_DATA_2.ALLNUM}";
		showOption(allNum);
	}else if(num == 3){
		var allNum = "${_DATA_3.ALLNUM}";
		showOption(allNum);
	}else if(num == 4){
		var allNum = "${_DATA_4.ALLNUM}";
		showOption(allNum);
	}
		
		
}
function showOption(allNum){
		var pages = 1;
		var page = 1;
		var options = "";
		if(allNum%10==0){
			pages=parseInt(allNum/10);
		}else{
			pages=parseInt(allNum/10+1);
		}
		$("#pageInfo").html(" 共"+pages+" 页 页次:1/"+pages+" 页");
		$("#pages").text(pages);
		for(var i=0;i<pages;i++){
		 options += "<option value='"+(i+1)+"'>"+(i+1)+"</option>";
		}
		$("#select").html(options);	
}
function toPage(page){
	var pages = $("#pages").text();
	showList(page);
}
function nextPage(){
	var pages = $("#pages").text();
	var nowPage = parseInt($("#page").text());
	var page = $("#select option:selected").text();
	if(nowPage==pages){
	alert("已经是最后一页了！");
	}else{
		showList(nowPage+1);
	}
	
}
function prePage(){
	var pages = $("#pages").text();
	var nowPage = parseInt($("#page").text());
	var page = $("#select option:selected").text();
	if(nowPage==1){
	alert("已经是第一页了！");
	}else{
		showList(nowPage-1);
	}
}
function showList(page){
	var pages = $("#pages").text();
	var chnlId = $(".block").attr("chnlId");
	FireFly.doAct("SY_COMM_INFOS", "getTopInfos", {"SITE_ID":"SY_COMM_SITE_INFOS","COUNT":"10","CHNL_ID":chnlId,"NOWPAGE":page},false,false,function(result){
	jQuery(".block").html("");
		var listBeans = result._DATA_;
		
		for(var i=0;i<listBeans.length;i++){
			var listBean = listBeans[i];
			var lists = "";
			if(listBean.NEWS_BODY.length<=1050){
				lists = "<li><a news-id='"+listBean.NEWS_ID+"' href='####' onclick='newsView1(this)' title='"+listBean.NEWS_SUBJECT+"' >"+listBean.NEWS_BODY+"</a><span class='time'>["+listBean.NEWS_TIME.substring(0,10)+"]</span></li>";
			}else{
				lists = "<li><a news-id='"+listBean.NEWS_ID+"' href='####' onclick='newsView1(this)' title='"+listBean.NEWS_SUBJECT+"' >"+listBean.NEWS_BODY.substring(0,1050)+"...</a><span class='time'>["+listBean.NEWS_TIME.substring(0,10)+"]</span></li>";
			}
			jQuery(".block").append(lists);
			}
	});
	$("#page").text(page);
	$("#pageInfo").html(" 共"+pages+" 页 页次:"+page+"/"+pages+" 页");
	$("#select").children().eq(page-1).attr("selected","selected");
	$("#select").children().eq(page-1).siblings().removeAttr("selected","selected");
}
</script>
</div>