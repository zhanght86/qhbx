<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="org.apache.commons.lang.RandomStringUtils"%>
<%@ page import="com.rh.core.serv.dict.DictMgr"%>
<%@ page import="com.rh.core.comm.CacheMgr"%>
<%@ page import="java.util.List"%>
<%@ page import="com.rh.core.util.threadpool.RhThreadPool"%>
<%@ page import="com.rh.core.plug.search.client.BatchIndexTask"%>
<%@ page import="com.rh.core.plug.search.client.IndexTaskStatus"%>
<%@ page import="com.rh.core.util.DateUtils"%>
<%@ page import="com.rh.core.plug.search.client.RhSearchClient"%>

<%
	request.setCharacterEncoding("UTF-8");
    String searchId = request.getParameter("searchId");
    if (searchId != null) {
        if (!BatchIndexTask.isRunning()) {
            if (request.getParameter("run") != null) {
                BatchIndexTask task = BatchIndexTask.createInstance(searchId);
                RhThreadPool.getDefaultPool().execute(task);
                Thread.sleep(500);
            }
        }
        
        if (request.getParameter("del") != null) {
            String query = "service:" + searchId;
            RhSearchClient rsc = new RhSearchClient();
            rsc.delIndexByQuery(query);
        }
        
        if (request.getParameter("endtime") != null) {
            String[] sIds = searchId.split(",");
            for (String sId : sIds) {
                IndexTaskStatus.setEndTime(sId,
                        request.getParameter("endtime"));
            }
        }
        
    }
%>
<div class="table_msg">当前时间：<%=DateUtils.getDatetime()%></div>
<table class="bordered">
	<thead>
		<tr>
			<td>名称</td>
			<td>是否运行？</td>
			<td>完成索引数</td>
			<td>实际结束时间</td>
			<td>计划结束时间</td>
		</tr>
	</thead>
	<tbody>	
<%    
    Object[] tasks = IndexTaskStatus.getTaskNames();
    for (Object taskName : tasks) {
        String name = (String) taskName;
%>
		<tr>
			<td><%=taskName%></td>
			<td><%=IndexTaskStatus.isRunning(name)%></td>
			<td><%=IndexTaskStatus.getCountVal(name)%></td>
			<td><%=IndexTaskStatus.getFinishTime(name)%></td>
			<td><%=IndexTaskStatus.getEndTime(name)%></td>
		</tr>
<%
    }
%>
	</tbody>
</table>

<br />
<style>

table {
	*border-collapse: collapse; /* IE7 and lower */
	border-spacing: 0;
	width: 90%;
}

.table_msg{
	margin-top:20px;
	margin-bottom:20px;
	font-weight:bold;
}

.bordered {
	border: solid #ccc 1px;
	-webkit-box-shadow: 0 1px 1px #ccc;
	-moz-box-shadow: 0 1px 1px #ccc;
	box-shadow: 0 1px 1px #ccc;
}

.bordered td,.bordered thead {
	border-left: 1px solid #ccc;
	border-top: 1px solid #ccc;
	padding: 10px;
	text-align: left;
}

.bordered thead {
	background-color: #dce9f9;
	background-image: -webkit-gradient(linear, left top, left bottom, from(#ebf3fc),
		to(#dce9f9) );
	background-image: -webkit-linear-gradient(top, #ebf3fc, #dce9f9);
	background-image: -moz-linear-gradient(top, #ebf3fc, #dce9f9);
	background-image: -ms-linear-gradient(top, #ebf3fc, #dce9f9);
	background-image: -o-linear-gradient(top, #ebf3fc, #dce9f9);
	background-image: linear-gradient(top, #ebf3fc, #dce9f9);
	-webkit-box-shadow: 0 1px 0 rgba(255, 255, 255, .8) inset;
	-moz-box-shadow: 0 1px 0 rgba(255, 255, 255, .8) inset;
	box-shadow: 0 1px 0 rgba(255, 255, 255, .8) inset;
	border-top: none;
	text-shadow: 0 1px 0 rgba(255, 255, 255, .5);
}
.bordered thead tr td {
	text-align:center;
	font-weight:bold;
}
</style>