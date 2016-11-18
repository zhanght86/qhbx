<div id="tinyPanel-wrapper" class="tiny-vertical" style="height:${hei};width:100%;margin:4px auto;position:relative;">
	
   <div style="width:50%;float:left;">
		<div><style type="text/css">
.list_ywkb_left{width:209px;}
.h3_ywkb_left{width:259px;height:30px;background: url(/image/image_gallery?uuid=8fbc90d0-c3c6-4aad-82fe-a4a7d394f24e&groupId=10136) no-repeat scroll 0 0 transparent;}
.contentDiv_ywkb_left{width:237px;height:286px;margin-bottom:7px;padding-bottom:10px;
background:url(/image/image_gallery?uuid=3b67c5b0-455b-4b5a-bf31-acf29b9b820a&groupId=10136) no-repeat bottom;}
.content_title_ywkb_left{color:#3399ff;text-align:center;}
.content_info_ywkb_left{width:40px;height:15px;margin:3px 8px 3px 3px;}</style></div>
<div class="list_ywkb_left">
<div class="h3_ywkb_left">&nbsp;</div>
<div class="contentDiv_ywkb_left">
<div class="content_ywkb_left">
<div class="content_title_ywkb_left">年累计承保APE达成</div>
<div align="center"><iframe height="155" src="http://10.10.7.172:8080/report/fastReportEfAction.do?action=homePage_samePeriod" frameborder="0" width="185"></iframe></div>
<div style="margin-top: 5px" align="center">
<table>
    <tbody>
        <tr>
            <td>
            <div class="content_info_ywkb_left" style="background-color: #ff0000">&nbsp;</div>
            </td>
            <td>80%以下</td>
        </tr>
        <tr>
            <td>
            <div class="content_info_ywkb_left" style="background-color: #ffff00">&nbsp;</div>
            </td>
            <td>80%-100%</td>
        </tr>
        <tr>
            <td>
            <div class="content_info_ywkb_left" style="background-color: #00ff00">&nbsp;</div>
            </td>
            <td>100%以上</td>
        </tr>
    </tbody>
</table>
</div>
</div>
</div>
</div>
	</div>
	<div style="width:50%;float:left;">
		<div><style type="text/css">
.list_ywkb{width:209px;}
.h3_ywkb{width:209px;height:30px;background: url(/image/image_gallery?uuid=8fbc90d0-c3c6-4aad-82fe-a4a7d394f24e&groupId=10136) no-repeat scroll 0 0 transparent;}
.contentDiv_ywkb{width:207px;margin-bottom:7px;padding-bottom:10px;
background:url(/image/image_gallery?uuid=3b67c5b0-455b-4b5a-bf31-acf29b9b820a&groupId=10136) no-repeat bottom;}
.content_title_ywkb{color:#3399ff;text-align:center;}</style></div>
<div class="list_ywkb">
<div class="h3_ywkb">&nbsp;</div>
<div class="contentDiv_ywkb">
<div class="content_ywkb">
<div class="content_title_ywkb">当月承保APE达成</div>
<div align="center"><iframe height="270" src="http://10.10.7.172:8080/report/sinochart/StBar2D.jsp?year=2012" frameborder="0" width="185"></iframe></div>
</div>
</div>
</div>
	</div>
</div>

<script>
jQuery(document).ready(function(jQuery) {
		var boxHtml = jQuery("#${id}__temp #tinyPanel-wrapper").html();
		jQuery("#${id}__temp").html("<div id='tinyPanel-wrapper' class='tiny-vertical' style='height:${hei};width:auto;margin:4px auto;position:relative;'>" + boxHtml + "</div>");
	});
function openContacturl(href) {
				var url = "";
				var tTitle = "";
				alert(href);
				var options = {"url":url,"tTitle":tTitle,"menuFlag":3,top:true};
				var tabP = jQuery.toJSON(options);
				tabP = tabP.replace(/\"/g,"'");
				/*window.open("/sy/comm/page/page.jsp?openTab="+(tabP));*/
				window.open(href,"_blank");
}
function openLocation() {
	var opts = {"url":"SY_COMM_WORKLOC.show.do?func=view","tTitle":"工位图","params":'',"menuFlag":3};
    parent.Tab.open(opts); 
}
</script>