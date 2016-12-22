<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<head>

<title>修改智能卡密码</title>
<OBJECT CLASSID="CLSID:F83A15A2-BAD8-465E-85C4-74ACB165924C" ID="SafeCtrl"></OBJECT>
<script language="JavaScript" src="method.js"></script>
</head>
<body style="margin:0px">
<form name="form">
<table style="background:url('images/login_main_bg.png');width:300px;height:200px;font-size:13px">
  <tr>
    <td height="23" colspan="2"><div align="center"><B>修改智能卡密码</B></div></td>
  </tr>
  <tr>
    <td width="90" height="30" align="right">旧 密 码：</td>
    <td width="100" height="30"><input name="SAFE_PLUGIN_CHANGEPASSWORD_OLD" type="password" class="input1" size="20" ></td>
  </tr>
  <tr>
    <td width="90" height="30" align="right">新 密 码：</td>
    <td width="100" height="30"><input name="SAFE_PLUGIN_CHANGEPASSWORD_NEW1" type="password" class="input1" size="20" ></td>
  </tr>
  <tr>
    <td width="90" height="30" align="right">确认密码：</td>
    <td width="100" height="30"><input name="SAFE_PLUGIN_CHANGEPASSWORD_NEW2" type="password" class="input1" size="20" ></td>
  </tr>
  <tr>
    <td height="37" colspan="2"><div align="center">
      <input name="Submit" style="height:22px;width:80px;" type="button" value="确定" onClick="return changeKeyPIN();"> 
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