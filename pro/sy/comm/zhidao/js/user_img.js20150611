//定义一个方法，将所有的userImg统一格式
var formatUserImg = function(url,id,clazz,width,height,online,onlineLeft,onlineTop,others){
	var imgStr = "";
	imgStr += "<img ";
	if(id != null && id != ''){
		imgStr += "id='"+id+"'";
	}
	if(clazz != null && clazz != ''){
		var clazzes = clazz.split(" ");
		imgStr += "class='";
		jQuery(clazzes).each(function(index,item){
			imgStr += item+" ";
		});
		imgStr += "'";
	}
	if(width != null && width != ''){
		imgStr += "width='"+width+"'";
	}
	if(height != null && height != ''){
		imgStr += "height='"+height+"'";
	}
	if(others != null && others != ''){
		imgStr += " "+others+" ";
	}
	imgStr += "src='"+url+"' />";
	if(online != null && online != ''){
		imgStr += "<div style='top:"+onlineTop+"; left:"+onlineLeft+";' class='rh-user-info-list-online rh-user-info-on-line' title='在线'>&nbsp;</div>";
	}else{
		imgStr += "<div style='top:"+onlineTop+"; left:"+onlineLeft+";' class='rh-user-info-list-offline rh-user-info-on-line' title='离线'>&nbsp;</div>";
	}
	
	//alert(imgStr);
	return imgStr;
}