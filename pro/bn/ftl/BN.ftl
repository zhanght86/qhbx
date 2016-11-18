<link rel="stylesheet" type="text/css" href="/sc/css/system_reset.css"/>
<link rel="stylesheet" type="text/css" href="/sc/comm/todo/css/todo.css"/>

<style>
.pt-todo-wrapper .on {border-bottom:0px;}
.pt-todo-wrapper #hover {}
</style>
<div  class='portal-box pt-gsyw-wrapper' style="width:98%" id='${id}__box'>

<#assign hasDate = hasDate!"2">
<#assign conhei = hei!"auto">
<script>
function newsViewY(id){
		var url = "/cms/SY_COMM_INFOS/" + id + ".html";			
		window.open(url);
	}
</script>
<div class='portal-box-title doubanner pt-todo-wrapper' style='position:relative;margin-left:4px;width:99%;'>
	
	<ul>
		<li class="${C} tab-item on current " chnlId="" id="li1" style="float:left;margin:0px 10px;padding-left:20px;" onclick="changeContent(this)">
		<span style="cursor:pointer;">业务快报</span>
		</li>
		<li class="${C}" style="float:left;margin:0px 10px;padding-left:15px;" id="li2" chnlId="${$CHNL_ID$}" onclick="changeContent(this)">
		<span style="cursor:pointer;">公司新闻</span>
		
		</li>
		
		 <li class="slide" style="left:16px;">
                 <div class="icon">
                 </div>
        </li>
		<li class="hover" id="hover" style="left:23px;">
		</li>
		
		
	</ul>
	<#if hasDate=='2'>
			<span class="portal-box-more-sc">&nbsp<a href="#" onclick="openListMoreByChnl('${$CHNL_ID$}','${title}')">更多</a></span>
		<#elseif hasDate=='3'>
			<span class="portal-box-more-sc">&nbsp<a href="#" onclick="openListMoreByChnlNomal('${$CHNL_ID$}','${title}')">更多</a></span>
		<#else>
			<span id = "ywkb" class="portal-box-more-sc">&nbsp<a href="#" onclick="openMoreByChnlY('${C}')">更多</a></span>
		</#if>
	
</div>
<#if conhei=='auto'>
<div class='portal-box-con' style='height:auto;'>
<#else>
<div class='portal-box-con' style='height:${hei}px;max-height:${hei}px;'>
</#if>
<div id="1">

<ul class="uly">


	<li style="width:100%;position: relative;">
		<#include "BN_YWKB.ftl"/>
	</li>

</ul>
</div>
<div id="2" style="display:none;">
<#if (_DATA_?size == 0)>
<tr><td align="center">该栏目下没有信息！</td></tr>
<#else>
<ul id="${$CHNL_ID$}" class="uly">
<#list _DATA_ as content>
	<li style="width:100%;position: relative;">
		<a id = '${$CHNL_ID$}${content.NEWS_ID}' title='${content.NEWS_SUBJECT}' style="margin-left:7px;width:97.5%;padding-left:8px;display: block;height:25px;margin-top:3px;" href="javascript:void(0);" onclick="newsViewY('${content.NEWS_ID}')">
			<span class="icon" style="float:left;"></span>

			<span class="elipd" style="width:64%;float:left;height:25px;line-height:25px;">${content.NEWS_SUBJECT}</span>
			<#--
			<script>
			var newQx = "${NEW_QX!'0'}";
			if (rhDate.doDateDiff('D',"${content.NEWS_TIME?substring(0,10)}",rhDate.getCurentTime().substring(0, 10))<=newQx) {
				$("<i class='new'></i>").appendTo($("#${$CHNL_ID$}${content.NEWS_ID}"));
			}
			</script>
			-->
			
			<span style="float:right;margin-right:6px;line-height:25px;color:#999999;">
				[${content.NEWS_TIME?substring(0,10)}]
		</span>
		</a>
	</li>

</#list>
	</ul>
</#if>
</div>
</div>
</div>
<script type ="text/javascript" >
	jQuery(document).ready(function(jQuery) {
		
		if(System.getVar("@ROLE_CODES@").indexOf("'${ROLE}'")>0){
		
			$("#ywkb").css("display","none");
		}else{
			$("#li2").addClass("on");
			$("#li1").removeClass("on");
			$("#li1").css("display","none");
			$("#2").css("display","block");
			$("#1").css("display","none");
		}
	});
	
	/*点击事件*/
	function changeContent(obj){
			
			if($(obj).hasClass("current")){
				return false;
			}
			
			var MsgInx = $(obj).index();
			$(obj).parent().parent().siblings(".portal-box-con").children().eq(MsgInx).show();
			$(obj).parent().parent().siblings(".portal-box-con").children().eq(MsgInx).siblings().hide();
			
			$(obj).siblings(".current").removeClass("current");
			$(obj).siblings(".on").removeClass("on");
			$(obj).addClass("current");
			$(obj).addClass("on");
			
			if("1"==MsgInx){
				$(obj).siblings(".slide").css({left:'116px'});
				$(obj).siblings(".hover").css({left:'125px'});
			}else{
				$(obj).siblings(".slide").css({left:'16px'});
				$(obj).siblings(".hover").css({left:'23px'});
			}
			if($("#li2").hasClass("on")){
				$("#ywkb").css("display","block");
			}else{
				$("#ywkb").css("display","none");
			}
	}
	
   
	function openMoreByChnlY(dd){
		var chnlId = $("."+dd+".on").attr("chnlId");
		var chnlName = $("."+dd+".on>span").text();
		if(chnlId==""){
		}else{
			openDateListMoreByChnl(chnlId,chnlName);
		}
	
	}
</script>
