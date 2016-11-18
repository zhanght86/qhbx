var focus_width=294
var focus_height=210

var text_height=0
var swf_height = focus_height+text_height

//展示图片
var pics='P1.jpg|P2.jpg|P3.jpg|P4.jpg'
var link1='';
var link2='../../../../Agency/webShop/integralShop/mw/MW0001.jsp'
var link3='../../../../Agency/webShop/integralShop/sj/SJ0001.jsp'
var link4='../../../../Agency/webShop/integralShop/gj/GJ0002.jsp'
var link5='../../../../Agency/webShop/integralShop/jj/JJ0001.jsp'
//链接地址
var links=link1+'|'+'|'+'|'+'|';

var texts='';
document.write('<object classid="clsid:d27cdb6e-ae6d-11cf-96b8-444553540000" codebase="http://fpdownload.macromedia.com/pub/shockwave/cabs/flash/swflash.cab#version=6,0,0,0" width="'+ focus_width +'" height="'+ swf_height +'">');
document.write('<param name="allowScriptAccess" value="sameDomain"><param name="movie" value="20060604.swf"><param name="quality" value="high"><param name="bgcolor" value="#ffffff">');
document.write('<param name="menu" value="false">');
document.write('<param name="wmode" value="opaque">');
document.write('<param name="FlashVars" value="pics='+pics+'&links='+links+'&texts='+texts+'&borderwidth='+focus_width+'&borderheight='+focus_height+'&textheight='+text_height+'">');
document.write('<embed src="20060604.swf" wmode="opaque" FlashVars="pics='+pics+'&links='+links+'&texts='+texts+'&borderwidth='+focus_width+'&borderheight='+focus_height+'&textheight='+text_height+'" menu="false" bgcolor="#ffffff" quality="high" width="'+ focus_width +'" height="'+ focus_height +'" allowScriptAccess="sameDomain" type="application/x-shockwave-flash" pluginspage="http://www.macromedia.com/go/getflashplayer" />');		
document.write('</object>');