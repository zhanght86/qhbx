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
            function doChannel(id, name){
                window.location = "/cms/SY_COMM_CMS_CHNL/" + id + "/index_1.html?group=${group}";
                //var opts = {'scrollFlag': true,'url': url,'tTitle': name,'menuFlag': 1};
                //top.Tab.open(opts);
            }
        </script>
    </head>
    <body>
        <div id="content_main_container" class="app_group_inner app_group_w_content">
            <div class="app_qun_inner cl">
                <div class="page_content_bbs page_content_bg cl">
                 <@channel debugName="主题所属栏目" channelId="${data.CHNL_ID}">
                    <div class="app_qun_path_share cl">
                        <div class="left qun_pt">
                        <!--
						<a title="首页" class="nvhm" href="/sy/comm/page/SY_COMM_TEMPL.show.do?model=view&pkCode=3vcRyLKjt6YVkMO2Y1wHsy&$SITE_ID$=${SITE_ID}">软虹BBS</a>
                        <em>›</em>
                        <a href="javascript:doChannel('${channel.CHNL_ID}','${channel.CHNL_NAME}');">${channel.CHNL_NAME}</a>
                        <em>›</em>
                        ${data.TOPIC_TITLE}</a>-->
                    </div>
                  </@channel>  
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
													<img alt="${data.S_UNAME!topic.S_USER}" src="" id="huati" width="100" height="100">
                                                </div>
                                            </div>
                                            <div class="user_info">
                                                <div class="authi">
                                                    ${data.S_UNAME!topic.S_USER}
                                                </div>
                                            </div>
                                        </div>
                                    </td>
                                    <td id="plc_0">
                                    	<!-- 话题_begin -->
                                        <div class="plc" style="margin-bottom:10px;">
                                            <div class="arrow_l">
                                            </div>
                                            <div class="mtm pbm bbda">
                                                <h1 class="ts"><a id="thread_subject">${data.TOPIC_TITLE}</a></h1>
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
                                            <div id="p_btn">
                                                <a onclick="UE.getEditor('comment_content').focus();" id="huifu" title="回复" id="post_reply" href="javascript:;"><i><img alt="回复" src="/sy/comm/bbs/topic/fastreply.gif">回复</i></a>
                                            </div>
                                            <div class="po pbm" id="po_0"></div>
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
			jQuery("#huati").attr("src", "${data.S_USER__IMG}");
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
	    });
	</script>
	
	<style type="text/css">
		#d-top {
		    bottom: 10px;
		    float: right;
		    position: fixed;
		    right: 20px;
		    z-index: 2000;<#-- 编辑器覆盖了返回顶部按钮，重置z-index-->
		}
	</style>
	<div id="d-top">
		<a title="回到顶部" onclick="javascript:document.body.scrollTop=0;document.documentElement.scrollTop=0;this.blur();return false;" href="#">
			<img alt="TOP" src="/sy/comm/news/img/top.png">
		</a>
	</div>
</body>
</html>
