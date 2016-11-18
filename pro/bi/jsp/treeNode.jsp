<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.aeon.bi.bean.BiAppTreeView" %>
<%@ page import="com.aeon.bi.business.Bi" %>
<%@ page import="com.aeon.bi.util.Util" %>
<%@ page import="com.aeon.bi.util.Const" %>

<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.Map" %>
<%@ page import="java.util.HashMap" %>
<%
	String actionUrl = "/bi/jsp/biUrl.jsp";
	String parentId = (String)request.getAttribute("parentId");
	Map<String, ArrayList<BiAppTreeView>> map = (Map<String, ArrayList<BiAppTreeView>>)request.getAttribute("map");
	ArrayList<BiAppTreeView> list = map.get(parentId);
	for (int i = 0; i < list.size(); i++) {
		BiAppTreeView node = list.get(i);
		boolean isLeaf = !map.containsKey(String.valueOf(node.getId()));
		String cssClass = "";
		String image = "";
		String style="";
		if (isLeaf) {
			cssClass = "tree-item";
			image = "/bi/images/spacer.png";
			style = "width:18px;height:18px;background-color:transparent;cursor:pointer;";
		} else {
			cssClass = "tree-item has-children node-open";
			image = "/bi/images/minus.png";
			style = "width:18px;height:18px;background-color:transparent;";
		}
%>

		<li class="<%=cssClass%>" style="list-style:none;">
			<%if(!isLeaf){ %>
			<input type="image" class="expand-image no-png-fix"
				style="<%= style %>" src="<%=image%>" />
				<%} %>
			<%if(Util.isNull(node.getUrl())){%>
					<input type="submit" value="" disabled="disabled" class="expand-image no-png-fix"
						style="width:16px;height:16px;background-color:transparent;
						background-image: url(/bi/images/page.png);border:none;cursor:pointer;" />
					<a style="padding-right:2px; cursor:pointer;">
						<span><%= node.getName() %></span>
					</a>
			<%} else {%>
					<input type="submit" value="" disabled="disabled" class="expand-image no-png-fix"
						style="width:16px;height:16px;background-color:transparent;
						background-image: url(/bi/images/page.png);border:none;cursor:pointer;" />
					<a style="padding-right:2px;cursor:pointer;"
						onmouseover="mouseOverTreeNode(this)" onmouseout="mouseOutTreeNode(this)"
						onclick="biSubmitAction('<%= actionUrl %>?appId=<%=String.valueOf(node.getId()) %>');styleTreeNode(this);">
						<span><%= node.getName() %></span>
					</a>
			<%} %>
			<%if(!isLeaf){ %>
					<ul class="has-children node-open" style="margin-top:0">
					<%
						request.setAttribute("parentId", String.valueOf(node.getId()));
						request.setAttribute("map", map);
						pageContext.include("treeNode.jsp");
					%>
					</ul>
			<%} %>
		</li>
<%
	}
%>