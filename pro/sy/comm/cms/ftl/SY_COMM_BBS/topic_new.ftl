<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8" />
        <title>${data.TOPIC_TITLE!'帖子'}</title>
		<#include "global.ftl"/>
        <link href="/sy/comm/bbs/css/app_group.css" rel="stylesheet">
        <link href="/sy/comm/bbs/css/app_qunbbs.css" rel="stylesheet">
		<link rel="stylesheet" type="text/css" href="/sy/comm/poll/poll.css" />
		<script type="text/javascript" src="/sy/comm/poll/poll.js"></script>
		<script type="text/javascript" src="/sy/comm/bbs/js/discuss.js"></script>
        <script type="text/javascript">
            function doChannel(id,name,type){
				var url = "/cms/SY_COMM_CMS_CHNL/" + id + "/index_1.html";
				if(type=='1'){
					url = "/cms/SY_COMM_CMS_CHNL/" + id + "/index_1.html?type=1&chnl_name="+name;
				}	
				var opts={'scrollFlag':true , 'url':url,'tTitle':name,'menuFlag':3};
				top.Tab.open(opts);
			}
        	function winclose(){
				parent.Tab.closeDirect(window.name); 
			}
			function publish(){
				var url = "/cms/SY_COMM_CMS_TMPL/0O1WepMWN2DbljgA5sXg7T.html?CHNL_ID=${data.CHNL_ID}&SITE_ID=${data.SITE_ID}";			
				var opts={'scrollFlag':true , 'url':url,'tTitle':"发帖",'menuFlag':3};
				parent.Tab.open(opts);
			}
			function doIndex(id){
				var url = "SY_COMM_TEMPL.show.do?model=view&pkCode="+id;	
				var opts={'scrollFlag':true , 'url':url,'tTitle':'论坛首页','menuFlag':4};
				top.Tab.open(opts);
			}
        </script>
       <style type="text/css">
       		body{background: #E8EFF6;}       		
			.plc {
				padding: 0 20px;
				background-color: white;
				-moz-transition: box-shadow 0.5s ease 0s;
				border: 1px solid #D4D7D9;
				border-radius: 4px 4px 4px 4px;
				box-shadow: 1px 1px 2px rgba(121, 121, 121, 0.2);
				position: relative;}
			.app_qun_path_share {
				position: relative;
				z-index: 2;
				font-size: 14px;
				overflow: hidden;
				height:29px;
				padding-top:3px;
				margin-top:8px;
				padding-left:10px;
				margin-bottom:10px;
				}
			.bbda {
				border-bottom: 1px dotted #DFDFDF;
				_margin-top: 0px !important;
				_padding-bottom: 5px !important;
				}
			.pi {
				border-bottom: 1px dotted #DFDFDF;
				overflow: hidden;
				margin-bottom: 10px;
				_margin-top: 0px !important;
				padding: 10px 0;
				_padding: 5px;
				height: 16px;
				}
			.app_qun_path_share_index{
				width:20px;
				height:30px;
				float:left;
				background:url(/sy/theme/default/images/menu/search.png) no-repeat 0px -5px ;
			}
			#d-top {
			    bottom: 10px;
			    float: right;
			    position: fixed;
			    right: 20px;
			    z-index: 2000;<#-- 编辑器覆盖了返回顶部按钮，重置z-index-->
			}
			.chnl_btn_tr{
				height:30px;width:100%;margin-bottom:10px;
			}
			.thread_subject_h{
				font-family: SimHei;color:black; line-height: 1.6;margin-left: 20px;text-decoration: none;
			}
			.thread_top_sub{
				height: 30px;border-bottom: 1px dotted;background: white;
			}
			.chnl_huati{width:100px;height:100px;}
			.chnl_btn_ft{float:left;width:60%}
			.btn-chat_return {background:url(/sy/theme/default/images/icons/rh-icons.png) no-repeat scroll 0px -144px transparent;}
		    .btn-chat_return {background:url(/sy/theme/default/images/icons/rh-icons.png) no-repeat scroll 0px -144px transparent;}
       </style>
    </head>
    <body>
        <div id="content_main_container" class="app_group_inner app_group_w_content">
            <div class="app_qun_inner cl">
                <div class="page_content_bbs page_content_bg cl">
                    <div class="app_qun_path_share cl">
                    <@channel_navigation debugName="论坛导航" channelId="${data.CHNL_ID}" >
	                  <#list tag_list as bean>
	                  	<#if bean.CHNL_NAME =='新平台(论坛站点)'>
	                  		<em>&nbsp;&nbsp;›&nbsp;&nbsp;</em>
	                  	<#else>
	                  		<a href="javascript:doChannel('${data.CHNL_ID}','${bean.CHNL_NAME}','1');">${bean.CHNL_NAME}</a><em>&nbsp;&nbsp;›&nbsp;&nbsp;</em>
	                  	</#if>
	                  </#list>
	                </@channel_navigation>
                    <@channel debugName="主题所属栏目" channelId="${data.CHNL_ID}">
                       <a href="javascript:doIndex('2GJ9dwYoR57rQdp3UHACdR');" title="论坛首页"><span class="app_qun_path_share_index"></span></a><a href="javascript:doChannel('${channel.CHNL_ID}','${channel.CHNL_NAME}');">${channel.CHNL_NAME}</a><em>&nbsp;&nbsp;›&nbsp;&nbsp;</em><a href="#">${data.TOPIC_TITLE}</a>
                   	 </@channel>
                    </div>
                </div>
                <div id="chnl_btn_tr" class="chnl_btn_tr">
                	<div class="chnl_btn_ft">
			        	<a class="rh-icon rhGrid-btnBar-a" href="javascript:publish();">
							<span class="rh-icon-inner"> 发 贴&nbsp&nbsp</span>
							<span class="rh-icon-img btn-chat"></span>
						</a>
		        	</div>
		        	<div style='float:right'>
		        		<a class="rh-icon rhGrid-btnBar-a" href="javascript:winclose();">
							<span class="rh-icon-inner">返回&nbsp&nbsp</span>
							<span class="rh-icon-img btn-chat_return"></span>
						</a>
		        	</div>
                </div>
                <div class="thread_top_sub">
                    <h1 class="ts"><a id="thread_subject" class="thread_subject_h">${data.TOPIC_TITLE}</a></h1>
                </div>				
                <div class="mn">
                    <div class="pl">
                        <div id="post_0">
                            <table cellspacing="0" cellpadding="0" summary="pId0" id="pId0">
                                <tr>
                                    <td class="pls">
                                        <div>
                                            <div class="avatar">
                                                <div class="qz_feed_avatar f_user_head">
                                                    <span class="skin_portrait_round"></span>
													<img alt="${data.S_UNAME!data.S_USER}" src="" id="huati" class="chnl_huati">
                                                </div>
                                            </div>
                                            <div class="user_info">
                                                <div class="authi">
                                                    ${data.S_UNAME!data.S_USER}
                                                </div>
                                            </div>
                                        </div>
                                    </td>
                                    <td id="plc_0">
                                    	<!-- 话题_begin -->
                                        <div class="plc">
                                            <div class="arrow_l">
                                            </div>

                                            <div class="pi">
                                                <strong class="xg1"><em>楼主</em></strong>
												<div class="top" style="display:none;"><a id="bbs_modify" class="modify">修改</a></div>
                                                <div class="pti">
                                                    <div class="pdbt">
                                                    </div>
                                                    <div class="authi xg1">
                                                        <span id="authorposton0" class="ct" title="${data.S_MTIME}">发表于 <em id="fabiao"></em></span>
                                                        <span class="pipe">|</span>
                                                      	  查看: ${data.TOPIC_READ_COUNTER!0}<span class="pipe">|</span>回复: ${data.COMMENT_COUNTER!0}
                                                    </div>
                                                </div>
                                            </div>
                                            <div class="pct">
                                                <div class="pcb">
                                                    <div class="t_fsz">
                                                        <table cellspacing="0" cellpadding="0">
                                                            <tr>
                                                                <td class="t_f" id="postmessage_0">
                                                                    <div>
                                                                        <div>
                                                                            ${data.TOPIC_BODY}
                                                                            <br>
                                                                        </div>
                                                                    </div>
                                                                </td>
                                                            </tr>
                                                        </table>
                                                    </div>
                                                </div>
                                            </div>
											<!-- 投票_begin -->
											<@poll_list debugName="投票" topicId="${data.TOPIC_ID}">
											<#if tag_list?size gt 0>
											<div id="poll"></div>
											<script type="text/javascript">
												jQuery(document).ready(function(){
												    var opts = {
												        "sId": "SY_PLUG_SEARCH",
												        "pollId": "${tag_list[0].POLL_ID}",
												        "pCon": jQuery("#poll")
												    };
												    listView = new rh.vi.poll(opts);
												    listView.show();
												});
											</script>
											</#if>
											</@poll_list>
											<!-- 投票_end -->
                                            <div class="po" id="p_btn">
                                                <a id="huifu" title="回复" href="javascript:;">回复</a>
                                            </div>
                                        </div>
                                    </td>
                                </tr>
                            </table>
                        </div>
                    </div>
					<!-- 话题_end -->
					
					<!-- 回帖列表_begin -->
					<div class="pl" id="postlist">
						
					</div>
					<!-- 回帖列表_end -->
                </div>
            </div>
        </div>
    </div>
	<script type="text/javascript">
	    jQuery(document).ready(function(){
	    	/* 阅读+1*/
			top.FireFly.doAct("SY_COMM_BBS_TOPIC","increaseReadCounter",{"TOPIC_ID":"${data.TOPIC_ID!''}"},false,true);
				
			/* 评论_begin */
		    var opts = {"COMMENT_SERV": "SY_COMM_BBS_TOPIC","SAVE_METHOD": "reply","DATA_ID": "${data.TOPIC_ID}","SERV_ID":"SY_COMM_BBS_TOPIC", "SHOWNUM":10,"NOWPAGE":1,"pCon":jQuery("#postlist")};
		    var listView = new rh.vi.comment(opts);
			
			//话题修改
			if("${data.S_USER}"==parent.System.getVar("@USER_CODE@")){
				jQuery("#bbs_modify").parent(".top").css("display","block");
				listView.modifyTopic(jQuery("#bbs_modify"));
			}
			
			//话题人头像显示
			jQuery("#huati").attr("src","${data.S_USER__IMG}");
			jQuery("#huati").bind("mouseover", function(event){
							 new top.rh.vi.userInfo(event, "${data.S_USER}");
						});
			//话题发表时间
			var timeago = "";
			if ("${data.S_MTIME}") {
				timeago = "${data.S_MTIME}";
				timeago = timeago.substring(0, 19)
				timeago = jQuery.timeago(timeago); 
			}		
			jQuery("#fabiao").text(timeago);					
		    listView.show();
			/* 评论_end */
			/*调整样式*/
			jQuery("#postlist").prev().css({"background":"white","margin-bottom": "10px"});
			//jQuery(".qz_feed_avatar .f_user_head")
	    	jQuery("#content_main_container").find(".f_user_head").css({"margin-left": "15px","margin-top": "15px"});
			jQuery("#p_btn").parent().removeAttr("style");
			jQuery(".po").attr("style","border-top :1px dotted #DFDFDF");
			jQuery("#currentUser").parent().parent().css("background","white");
			jQuery("#currentUser").css({"margin-top": "15px","margin-left": "15px"});
			jQuery("#f_pst>table>tbody>tr:eq(1)").before("<tr><td colspan='2' height='50px'>"+jQuery("#chnl_btn_tr").html()+"</td></tr>");
			jQuery("#huifu").unbind("click").click("click",function(){
				jQuery("body").scrollTop(jQuery(document).height());
				UE.getEditor("comment_content").focus();
			});
			
		});
	
	</script>
	<div id="d-top">
		<a title="回到顶部" onclick="javascript:document.body.scrollTop=0;document.documentElement.scrollTop=0;this.blur();return false;" href="#">
			<img alt="TOP" src="/sy/comm/news/img/top.png">
		</a>
	</div>
</body>
</html>
