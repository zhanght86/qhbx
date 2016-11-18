<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%
	String url = (String)request.getAttribute("redirTo");
%>
<a href="<%=url%>" id="redirTo">请稍候……</href>
<script>
	var hrefObj = document.getElementById("redirTo");
	if(hrefObj) {
		hrefObj.click();
	}
</script>