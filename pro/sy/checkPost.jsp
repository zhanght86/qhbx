<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
	request.setCharacterEncoding("UTF-8");
	String urlPath = request.getContextPath();
	String postData = "";
	if(request.getParameter("postData")!=null){
		postData = request.getParameter("postData");
		out.println(postData);
	}else{
%>
<html>
<head>
<script type="text/javascript" src="<%=urlPath %>/sy/jquery-1.8.2.js"></script> 
<script type="text/javascript">
	var FireFlyContextPath = "<%=urlPath%>";
	jQuery(document).ready(function(){
		jQuery("#commitData").unbind("click").bind("click",function(){
		alert("待提交的数据："+jQuery("#postData").val());
		jQuery.ajax({
        type:"post",
        data:{"postData":jQuery("#postData").val()},
        url:encodeURI("<%=urlPath%>/sy/checkPost.jsp"),
        dataType:"json",
        cache:false,
        async:false,
        timeout:60000,
        success:function(data) {
            alert(data);
        },
        error:function(err) {
			alert(err.responseText);
        }
		});
	});
	jQuery("#commitData2").unbind("click").bind("click",function(){
		alert("待提交的数据："+jQuery("#postData").val());
		jQuery.ajax({
        type:"post",
        data:jQuery.param({"postData":jQuery("#postData").val()}),
        url:encodeURI("<%=urlPath%>/sy/checkPost.jsp"),
        dataType:"json",
        cache:false,
        async:false,
        timeout:60000,
        success:function(data) {
            alert(data);
        },
        error:function(err) {
			alert(err.responseText);
        }
		});
	});
	});
</script>
</head>
<body>
<input type="text" id="postData" value=""/><input type="button" name="commitData" value="提交数据" id="commitData"/><input type="button" name="commitData" value="提交数据2" id="commitData2"/>
</body>
</html>
<%}%>
