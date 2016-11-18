<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ page import="com.rh.core.comm.file.Uploader" %>


<%
    request.setCharacterEncoding("utf-8");
    response.setCharacterEncoding("utf-8");

    Uploader up = new Uploader(request);
    up.setSavePath("upload"); //保存路径
    
  	//允许的文件类型
    String[] fileType = {".jpg", ".jpeg",".png",".gif",".doc",".docx",".wps",".xls",".xlsx",".ppt",".pptx",".rar",".zip",".7z",".gz",".tar",".txt",".chm",".pdf;",".swf", ".wmv"};
    up.setAllowFiles(fileType);
    up.setMaxSize(100 * 1024);        //允许的文件最大尺寸，单位KB
    up.upload();
    response.getWriter().print("{'url':'"+up.getUrl()+"','fileType':'"+up.getType()+"','state':'"+up.getState()+"','original':'"+up.getOriginalName()+"'}");

%>
