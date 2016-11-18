<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>论坛发帖</title>
<#include "global.ftl"/>
<#include "/SY_COMM_BBS/config_constant.ftl" />
<script type="text/javascript" src="/sy/comm/bbs/js/editor.js"></script>
<script type="text/javascript">
	jQuery(document).ready(function(){
		var opts = {"SERV_ID":"SY_COMM_BBS","pCon":jQuery("#target")};
	    var editorView = new rh.vi.editor(opts);
		
		jQuery("#submit").unbind("click").click(function(event){
			var content = editorView.editor.getContent();
			var title = jQuery("#title").val();
			
			if(title.length==0){
				alert("标题为空");
				return false;
			}
			else if(content.length==0){
				alert("帖子内容为空");
				return false;
			}
			
			var param = {
				"TOPIC_TITLE":title,
				"TOPIC_BODY":content,
				"CHNL_ID":"${CHNL_ID}",
				"SITE_ID":"${SITE_ID}"
			};
			
			var resultData = parent.FireFly.doAct("SY_COMM_BBS_TOPIC", "save", param);
			if (resultData[UIConst.RTN_MSG] && resultData[UIConst.RTN_MSG].indexOf(UIConst.RTN_OK) == 0) {
		  		alert("保存成功!");
				
				//转到新发表的帖子页面
				window.location = "/cms/SY_COMM_BBS_TOPIC/" + resultData["TOPIC_ID"] + ".html";
			} else {
				Tip.showError(resultData[UIConst.RTN_MSG], true);
			}
		});
	});
</script>
<style type="text/css">
	html,body{background:white;}
	#title{
		width:100%;
		height:2em;
		line-height:1.1em;
		font-size:1.1em;
	}
	table.publish {
		margin:10px auto;
		line-height:2em;
	}
	table.publish td {
		vertical-align:top;
		padding:10px;
	} 
	table.publish td h2{
		font-weight:bold;
		font-size:22px;
	}
	.iconChoose {
	    display: inline-block;
	    height: 16px;
	    position: relative;
	    width: 16px;
		left: 94%;
		bottom:22px;
	}
	.icon-input-dict {
	    background: url("/sy/theme/default/images/icons/rh-btns.png") no-repeat scroll right -984px transparent;
	}
	td select#view{
		height:2em;
		line-height:1.5em;
		border:1px #ccc solid;
		font-size:14px;
	}
	#channel_name{
		height:2em;
		line-height:1.5em;
		font-size:14px;
		width:100%;
	}
</style>
</head>
<body>

<table border="0" cellpadding="8" cellpadding="4" width="80%" class="publish">
	<tr>
		<td colspan="2" align="center">
			<h2>帖子发表</h2>
		</td>
	</tr>
	<tr>
		<td align="right" width="80">标题<font color="red">*</font></td>
		<td>
			<input type="text" id="title" name="title">
		</td>
	</tr>
	<tr>
		<td align="right">内容体<font color="red">*</font></td>
		<td id="target"></td>
	</tr>
</table>

</body>
</html>