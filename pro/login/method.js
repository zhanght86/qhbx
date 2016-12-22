//初始化界面
function initShow(){
	document.getElementById("ncradio").checked=true;
	document.getElementById("ncunitcode").style.display="";
	document.getElementById("iufounitcode").style.display="none";
	document.getElementById("width").value=window.screen.width ;
	document.getElementById("height").value=window.screen.height;
}
 
  //Ukey登录验证
function doAuthRequest() {
  var jsBase64 = new JavaScriptBase64;
	  jsBase64.JavaScriptBase64("");
	var cert = jQuery("#cert").val();
	var pass = jQuery("#ukey_password").val();	
	var rand = jQuery("#randnum").val();
	if(!pass.length > 0 ){ //登录是否输入密码 
		alert("请填写智能卡密码!"); 
		jQuery("#ukey_password").focus(); 
		jQuery("#ukey_password").disabled=false;
		return false; 
	}
	
 
 
	try{
		if(SafeCtrl.USB_OpenDevice(0)) {
			SafeCtrl.USB_CloseDevice();
			alert("未检测到智能卡！");
			return false;
		}
		
		if(SafeCtrl.USB_GenAuthReq(pass,cert,rand)) {
			SafeCtrl.USB_CloseDevice();
			var errmsg = SafeCtrl.USB_GetLastError();
			alert("产生认证请求失败，原因为："+errmsg);
			return false;
		}
		
	    pass=SafeCtrl.USB_GetAuthReq();
	    SafeCtrl.USB_CloseDevice();
		jsBase64.string = jQuery("#ukey_password").val();
		
		jQuery("#userPIN").val() = jsBase64.encode();
		jQuery("#ukey_password").val() = pass;
		return true;
	}catch(e){
		alert("认证控件没有正确安装，请下载安装后再试！");
		return false;
	}
	
//	var addinput="<img src='images/progress.gif' width='149'>";
//	//添加该图片
//	document.getElementById("progress").innerHTML=addinput;
//	
//	document.getElementById("submit").disabled=true;
	
	return true;
}

function doPfxAuthRequest() {
	var file_upl = document.getElementById('SAFE_File_CONTENT');
    file_upl.select();
    var realpath = document.selection.createRange().text;
	var pfxcert = realpath;
	var cert = document.all.cert.value;
	var pass = document.all.cert_password.value;
	var rand = document.all.randnum.value;
  	
	if(pfxcert==""){ //登录是否选择证书
		alert("请选择证书文件!"); 
		return false; 
	}
	
	if(document.getElementById("cert_password").value==""){ //登录是否输入密码 
		alert("请填写证书密码!"); 
		document.getElementById("cert_password").focus(); 
//		document.getElementById("submit").disabled=false;
		return false; 
	}
	
	try {
		if(SafeCtrl.FILE_GenAuthReq(pass,pfxcert,cert,rand)) {
			var errmsg = SafeCtrl.USB_GetLastError();
			alert("产生认证请求失败，原因为："+errmsg);
			return false;
		}
	
		var authreq = SafeCtrl.USB_GetAuthReq();
		document.all.cert_password.value = authreq;
	} catch(e) {
		alert("认证控件没有正确安装，请下载安装后再试！");
		return false;	
	}
	
//	var addinput="<img src='images/progress.gif' width='149'>";
//	//添加该图片
//	document.getElementById("progress").innerHTML=addinput;
//	
//	document.getElementById("submit").disabled=true;
 		
 	return true;
}
//打开新窗口
function openWin(name, url, width, height, scrollbars){
    var l = (screen.availWidth-width)/2;
    var t = (screen.availHeight-height)/2;  
    window.open(url,name,"top="+t+",left="+l+",width="+width+",height="+height+",status=no,toolbar=no,menubar=no,location=no,scrollbars="+scrollbars+"");
}
 

//修改证书密码
function modifyPass() {	  
	  var pfxcert = document.all.p12certfile.value;
	  var oldpass=document.all.oldpass.value;
	  var newpass=document.all.newpass.value;
	  var newpass2=document.all.newpass2.value;
	  if(pfxcert == ""){
		    alert("请上传证书文件！");
		    return false;
	  }
	  if(oldpass == "" || newpass == "" ||newpass2 == ""){
		    alert("请正确输入密码！");
		    return false;
	  }
	  if(newpass != newpass2){
		    alert("确认密码与新密码不一致");
		    return false;
	  }

	  try {
		    if(SafeCtrl.FILE_ChangePFXPass(pfxcert, oldpass, newpass)) {
			      var errmsg = SafeCtrl.USB_GetLastError();
			      alert("修改PFX证书密码失败，原因为："+errmsg);
			      return false;
		    }
		    alert("修改PFX证书密码成功");
		    self.close();
		    return true;
	  } catch(e) {
		    alert("客户端没有正确安装，请下载安装后再试！");
		    return false;
  	}
}

//修改key密码
function changeKeyPIN() {
	  var pin_old = document.all.SAFE_PLUGIN_CHANGEPASSWORD_OLD.value;
	  var pin_new1 = document.all.SAFE_PLUGIN_CHANGEPASSWORD_NEW1.value;
	  var pin_new2 = document.all.SAFE_PLUGIN_CHANGEPASSWORD_NEW2.value;
	
	  if(pin_old == "" || pin_new1 == "" || pin_new2 == ""){
		    alert("请正确输入智能卡密码！");
		    return false;
	  }
	  if(pin_new1 != pin_new2){
		    alert("确认密码与新密码不一致");
		    return false;
    }

	  try {
		    if(SafeCtrl.USB_OpenDevice(0)) {
			      SafeCtrl.USB_CloseDevice();
			      alert("未检测到智能卡！");
			      return false;
		    }
		    if(SafeCtrl.USB_ChangePin(1,pin_old,pin_new1)) {
			      SafeCtrl.USB_CloseDevice();
			      var errmsg = SafeCtrl.USB_GetLastError();
			      alert("修改智能卡密码失败，原因为："+errmsg);
			      return false;
		    }
		    SafeCtrl.USB_CloseDevice();
		    document.all.SAFE_PLUGIN_CHANGEPASSWORD_OLD.value = "";
		    document.all.SAFE_PLUGIN_CHANGEPASSWORD_NEW1.value = "";
		    document.all.SAFE_PLUGIN_CHANGEPASSWORD_NEW2.value = "";
		    alert("修改智能卡密码成功！");
		    self.close();
		   return true;
	  } catch(e) {
		    alert("客户端没有正确安装，请下载安装后再试！");
		    return false;
	  }
}
/*
 * Title  -> JavaScript Base64 Encoder Decoder
 * Author -> Paul Gration
 * URL    -> http://www.i-labs.org
 * Email  -> pmgration(at)i-labs.org
 */

function JavaScriptBase64()
{
    var string;
    var base64;

    this.JavaScriptBase64 = function(string)
    {
        this.string = new String(string);
        this.base64 = new Array('A','B','C','D','E','F','G','H',
                                'I','J','K','L','M','N','O','P',
                                'Q','R','S','T','U','V','W','X',
                                'Y','Z','a','b','c','d','e','f',
                                'g','h','i','j','k','l','m','n',
                                'o','p','q','r','s','t','u','v',
                                'w','x','y','z','0','1','2','3',
                                '4','5','6','7','8','9','*','/');
    }
    
    this.encode = function()
    {
        var binary = new String();
        var result = new String();
        for(i = 0; i < this.string.length; i++)
        {
            binary += String("00000000" + this.string.charCodeAt(i).toString(2)).substring(this.string.charCodeAt(i).toString(2).length);
        }
        for(i = 0; i < binary.length; i+=6)
        {
            var number = new Number();
            var counter = new Number();
            for(j = 0; j < binary.substring(i, i+6).length; j++)
            {
                for(k = 32; k >= 1; k-=(k/2))
                {
                    if(binary.substring(i, i+6).charAt(counter++) == "1")
                    {
                        number += k;
                    }
                }
            }
            result += this.base64[number];
        }
        return result;
    }

    this.decode = function()
    {
        var binary = new String();
        var result = new String();
        for(i = 0; i < this.string.length; i++)
        {
            for(j = 0; j < this.base64.length; j++)
            {
                if(this.string.charAt(i) == this.base64[j])
                {
                    binary += String("000000" + j.toString(2)).substring(j.toString(2).length);
                }
            }
        }
        for(i = 0; i < binary.length; i+=8)
        {
            var number = new Number();
            var counter = new Number();
            for(j = 0; j < binary.substring(i, i+8).length; j++)
            {
                for(k = 128; k >= 1; k-=(k/2))
                {
                    if(binary.substring(i, i+8).charAt(counter++) == "1")
                    {
                        number += k;
                    }
                }
            }
            result += String.fromCharCode(number);
        }
        return result;
    }
}
 