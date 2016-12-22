<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<head>
<title>修改PFX证书密码</title>
<OBJECT CLASSID="CLSID:F83A15A2-BAD8-465E-85C4-74ACB165924C" ID="SafeCtrl"></OBJECT>
<script language="JavaScript" src="method.js"></script>

<script language="JavaScript">
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
</script>

</head>
<body style="margin:0px">
<form name="form">
<table style="background:url('images/login_main_bg.png');width:350px;height:250px;font-size:13px">
  <tr>
    <td height="23" colspan="2"><div align="center"><B>修改PFX证书密码</B></div></td>
  </tr>
  <tr>
    <td width="100" height="30" align="right">选择证书文件：</td>
    <td width="100" height="30"><input type="file" name="p12certfile" class="input1" size="20"></td>
  </tr>
  <tr>
    <td width="100" height="30" align="right">旧 密 码：</td>
    <td width="100" height="30"><input type="password" name="oldpass"" class="input1" size="20"></td>
  </tr>
  <tr>
    <td width="100" height="30" align="right">新 密 码：</td>
    <td width="100" height="30"><input type="password" name="newpass" class="input1" size="20"></td>
  </tr>
  <tr>
    <td width="100" height="30" align="right">确认密码：</td>
    <td width="100" height="30"><input type="password" name="newpass2" class="input1" size="20"></td>
  </tr>
  <tr>
    <td height="37" colspan="2"><div align="center">
      <input name="Submit" style="height:22px;width:80px;" type="button" value="确定" onClick="return modifyPass();"> 
      <input name="Submit2" style="height:22px;width:80px;" type="button" value="重置" onclick="return restart();">
    </div></td>
  </tr>
  <tr>
    <td height="23" colspan="2">    <div align="center"><span class="text">注意：密码只能是4－8位数字!</span></div></td>
  </tr>
</table>
</form>
</body>
</html>