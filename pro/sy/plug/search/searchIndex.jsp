<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<!--searchIndex.jsp 搜索入口页面-->
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>软虹-云智能搜索</title>
<%@ page import="com.rh.core.plug.search.client.*"%>
<%@ include file="/sy/base/view/inHeader.jsp"%>
<style type="text/css">
#suggest {
	position: absolute;
	background-color: #FFFFFF;
	border: 1px solid #CCCCFF;
	width: 404;
}

#suggest div {
	padding: 5px;
	display: block;
	width: 404;
	FONT: 15px arial;
	overflow: hidden;
	white-space: nowrap;
}

#suggest div.select {
	color: #FFFFFF;
	background-color: #3366FF;
}

#suggest div.over {
	background-color: #99CCFF;
}

.searchDivStyle {
	background-color:white;
	margin: 50px 20px 0px 20px;
	padding:0px 0px 50px 0px;
	border:1px #d5e1ed solid;
}
</style>
</head>
<body style="background-color:#f6f6f6;">
	<div class="searchDivStyle ui-corner-5">
	<div class="rh-allSearch-door">
		<table width="400" border="0" class="rh-allSearch-bar">
			<tr>
				<td colspan="2" class="form_title">全文检索</td>
			</tr>
			<tr>
				<td style='width: 100px'>数据类别：</td>
				<td><input type="hidden" id="rh-select-serv-id" /> <input
					type="text" class="rh-select-serv" id="rh-select-serv-id__NAME"
					value='----类别选择----' onclick="getServ()" /></td>
			</tr>
			<tr>
				<td style='width: 120px'>搜索关键字：</td>
				<td colspan="2"><input type="text" class="keywords"
					id="keywords"></input></td>
			</tr>
			<tr>
				<td colspan="2"><a class="rh-icon rh-allSearch-btn"><span
						class="rh-icon-inner">开始搜索</span><span
						class="rh-icon-img btn-search"></span></a></td>
			</tr>
		</table>
	</div>
	<input type="hidden" id="rh-select-cmpy-id" />
	<input type="hidden"
		style="-moz-box-shadow: 1px 1px 1px #BDBEBB inset; -webkit-box-shadow: 1px 1px 1px #BDBEBB inset; box-shadow: 1px 1px 1px #BDBEBB inset;"
		class="" id="rh-select-cmpy" value='----单位选择----' onclick="getOrg()" />
	<div id="suggest"
		style="z-index: 10; display: none;height:270px"></div>
	</div>
</body>

<script src="/sy/plug/search/search.js" type=text/javascript></script>
<script src="/sy/plug/search/suggest.js" type=text/javascript></script>
<%
    String suggestServer = RhSuggestionClient.getInstance()
					.getServerUri();
%>
<script type="text/javascript">
//suggestion
function startSuggest(){
	 var server = "<%=suggestServer%>";
		var list = [ "" ];
		new Suggest.Local("keywords", // input element id.
		"suggest", // suggestion area id.
		server + "?rtn=js&w=", "query()", list, {
			dispMax : 10,
			interval : 10
	}); // options
}

jQuery(document).ready(function(){
	startSuggest();
});

	jQuery(document).ready(function() {
		Tab.setFrameHei(GLOBAL.getDefaultFrameHei());
		jQuery(".rh-allSearch-btn").bind("click", function() {
			query();
		});

		jQuery(".rh-allSearch-bar").bind('keydown', function(e) {
			var key = e.which;
			if (key == 13) {
				query();
			}
		});
	});

	query = function() {
		var keywords = jQuery("#keywords").val();
		keywords = $.trim(keywords);
		if(keywords.length == 0){
			alert("请输入查询关键字！");
			return;
		}
		var filterCache = [];
		var filterStr = "[";
		var servIds = jQuery("#rh-select-serv-id").val();

		var servArray = servIds.split(",");
		jQuery.each(servArray, function(i, n) {
			if (n != "") {
				var filter = {};
				filter["id"] = "service";
				filter["data"] = n;
				filterCache.push(filter);
				//filterStr += "{\'id\':\'service\', \'data\':\'"+ n +"\'},";
				filterStr += parseParam(filter) + ",";
				//"[{\'aa\':\'bb\'},{\'cc\':\'99\'}]"
			}
		});

		var cmpyIds = jQuery("#rh-select-cmpy-id").val();
		var cmpyArray = cmpyIds.split(",");
		jQuery.each(cmpyArray, function(i, n) {
			if (n != "") {
				var filter = {};
				filter["id"] = "company";
				filter["data"] = n;
				filterCache.push(filter);
				filterStr += parseParam(filter) + ",";
			}
		});
		filterStr += "]";
		keywords = encodeURIComponent(keywords);
		var opts = {
			"sId" : "SEARCH-RES",
			"tTitle" : "搜索",
			"url" : "/SY_PLUG_SEARCH.query.do?data={" + "'KEYWORDS':'"
					+ keywords + "','FILTER':" + filterStr
					+ ",'_PAGE_':{SHOWNUM:10}" + "}",
			"menuFlag" : 3,
			"scrollFlag" : true
		};

		Tab.open(opts);
		Tab.close();
	};

	//选择单位触发方法
	getOrg = function(event) {
		jQuery("#rh-select-cmpy").empty()
		var options = {
			"itemCode" : "rh-select-cmpy",
			"config" : "SY_ORG_CMPY,{'extendDicSetting':{'rhexpand':false,'cascadecheck':true},'TYPE':'multi','rtnNullFlag':true}",
			"parHandler" : null,
			"hide" : "explode",
			"show" : "blind",
			replaceCallBack : function(id, value) {
				jQuery("#rh-select-cmpy-id").val(id);
				jQuery("#rh-select-cmpy").val(value);
				//search...
			}
		};
		var dictView = new rh.vi.rhDictTreeView(options);
		dictView.show(event, [ 170, 150 ]);
		var id = jQuery("#rh-select-cmpy-id").val();
		jQuery(".ui-dialog-title").text("请选择单位");

		var array = id.split(",");
		jQuery.each(array, function(index, n) {
			dictView.tree.checkNode(n);
			dictView.tree.expandParent(n);
		});

		return false;
	};

	//选择服务(类别)触发方法
	getServ = function(event) {
		jQuery("#rh-select-serv").empty()
		var options = {
			"itemCode" : "rh-select-serv-id",
			"config" : "SY_SERV_SEARCH,{'extendDicSetting':{'rhexpand':true},'TYPE':'multi','extendWhere':' AND SERV_SEARCH_FLAG=1','rtnNullFlag':true}",
			"parHandler" : null,
			"hide" : "explode",
			"show" : "blind",
			replaceCallBack : function(id, value) {
				jQuery("#rh-select-serv-id").val(id);
				jQuery("#rh-select-serv-id__NAME").val(value);
			}
		};
		var dictView = new rh.vi.rhDictTreeView(options);
		dictView.show(event);
		var id = jQuery("#rh-select-serv-id").val();
		jQuery(".ui-dialog-title").text("请选择类别");

		var array = id.split(",");
		jQuery.each(array, function(index, n) {
			dictView.tree.checkNode(n);
			dictView.tree.expandParent(n);
		});

		return false;
	};
</script>
</html>