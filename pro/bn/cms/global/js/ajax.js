function newAjaxRequest(){
	if (window.XMLHttpRequest) {
		return new XMLHttpRequest();
	}else if (window.ActiveXObject) {
		return new ActiveXObject("Microsoft.XMLHTTP");
	}
}

function doGetAjaxRequest(ajaxUrl){
	var obj = null;
	obj = newAjaxRequest();
	obj.open("get",ajaxUrl, false );
	obj.send(null);
	if(obj.readyState==4&&obj.status==200){
		return obj.responseText;
	}else{
		return "error";
	}
}

function doPostAjaxRequest(ajaxUrl,ajaxParametersName,ajaxParametersValue){
	var sentValue = ajaxParametersName+"="+encodeURIComponent(ajaxParametersValue);//encode
	var obj = null;
	obj = newAjaxRequest();
	obj.open("POST",ajaxUrl,false );
	obj.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
	obj.send(sentValue);
	if(obj.readyState==4&&obj.status==200){
		return obj.responseText;
	}else{
		return "error";
	}
}

function doPostAjaxRequestMore(ajaxUrl,ajaxPostValue){
	//var sentValue = ajaxParametersName+"="+encodeURIComponent(ajaxParametersValue);//encode
	var obj = null;
	obj = newAjaxRequest();
	obj.open("POST",ajaxUrl,false );
	obj.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
	obj.send(ajaxPostValue);
	if(obj.readyState==4&&obj.status==200){
		return obj.responseText;
	}else{
		return "error";
	}
}

String.prototype.trim = function() 

{ 

return this.replace(/(^\s*)|(\s*$)/g, ""); 

} 

//encodeURIComponent
//java.net.URLDecoder