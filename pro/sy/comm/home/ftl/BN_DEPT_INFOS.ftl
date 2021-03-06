<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title>文档内容</title>
<#include "global.ftl"/>
<script type="text/javascript" src="/sy/base/frame/coms/tree/jquery.tree.js"></script>
<script type="text/javascript" src="/sy/base/frame/coms/tree/ui.tree.js"></script>
<script type="text/javascript" src="/sc/js/sc_projectJs.js"></script>
<meta http-equiv="X-UA-Compatible" content="IE=Edge">
<meta http-equiv="content-type" content="text/html; charset=UTF-8">
</head>
<body class="portalBody">
<link rel="stylesheet" type="text/css" href="/sc/css/system_reset.css"/>
<div id="dept_code" style="display:none">${DEPT_CODE}</div>
<div class="portal-target wp20 floatLeft">
	<div class='portal-box-title ' style='position:relative;'>
		<span class='portal-box-title-top'></span>
		<span class="portal-box-title-label" style="margin-left: 10px;">公司部门</span>
		<div class="portal-box-title-bottom-line"></div>
	</div>

	<div class='portal-box-con info-column' style='height:auto;'>
		<table id='showContent' width="100%" style="table-layout:fixed;margin-left:8px;">
			
		</table>
	</div>
</div>
<div class="portal-target wp80 floatLeft">
	<link rel="stylesheet" type="text/css" href="/sc/css/system_reset.css"/>
	<div  class='portal-box pt-gsyw-wrapper' id=''>
		<div class='portal-box-title' style='position:relative;'>
			<span class='portal-box-title-top'></span>
			<span class="portal-box-title-label" style="margin-left: 10px;">部门新闻</span>
			<div class="portal-box-title-bottom-line"></div>
		</div>

		<div class='portal-box-con info-column' style='height:174px;'>
			<table id='showNews' width="100%" style="table-layout:fixed;margin-left:8px;">

			</table>
		</div>
		<div  class='portal-box pt-gsyw-wrapper' id=''>

			<div class='portal-box-title ' style='position:relative;'>
				<span class='portal-box-title-top'></span>
				<span class="portal-box-title-label" style="margin-left: 10px;">部门文件</span>
				<div class="portal-box-title-bottom-line"></div>
			</div>

			<div class='portal-box-con info-column' style='height:auto;'>
				<table width="100%" style="table-layout:fixed;margin-left:8px;">
					<div class ="news-list-left" style="float:left;width:15%;" >
						<div id="ORG_TREE"></div>
					</div>  
					<div class = "news-list-right" style="float:left;width:85%;" >
						<ul class = "news-list-right-85">
					
						</ul>
					</div>
				</table>
			</div>
		</div>
	</div>
</div>
</body>
<script type ="text/javascript" >
	jQuery(document).ready(function(jQuery) {
		
		var boxHtml = jQuery("#BN_NEWS_FILE_DQ__temp #BN_NEWS_FILE_DQ__box").html();
		jQuery("#BN_NEWS_FILE_DQ__temp").html("<div class='portal-box pt-gsyw-wrapper' id='BN_NEWS_FILE_DQ__box'>" + boxHtml + "</div>");
		var odeptCode = System.getUser("ODEPT_CODE");
		FireFly.doAct("SY_COMM_INFOS_CHNL_VIEW","getDeptAll",{"_ROWNUM_":100,"ODEPT_CODE":odeptCode},null,false,function(returnData){
			 refreshRight(returnData);
		});
	});
	
/*左侧导航树的刷新方法*/
	function showContent(newsData){ 
	
		for(var i=0;i<newsData._DATA_.length;i++){
			var listBean = newsData._DATA_[i];
			jQuery("#showContent").append("<tr><td class='icon'><td style='width:56%;position: relative;'><a id = '"+listBean.DEPT_CODE+"' title='"+listBean.DEPT_NAME+"' style='width:100%;margin-left:3px;display: block;height:28px;' href='/cms/SY_COMM_CMS_TMPL/${CHNL_PID}/index_1.html?CHNL_PID=${CHNL_PID}&DEPT_CODE="+listBean.DEPT_CODE+"'><span class='elipd'>"+listBean.DEPT_NAME+"</span></a></td><td style='width:20%;'><span style='float:right;margin-right:6px;color:#999999;'>......</span></td></tr>");
		}
	}
	
	/*右侧刷新*/
	function refreshRight(deptCode){
	
		showContent(deptCode);
		showDeptInfos(deptCode);
		showDept(deptCode);
		showFile(deptCode);
		
	}
	/*右侧上方新闻模块的刷新方法*/
	function showDeptInfos(deptCode){
		var newsData = FireFly.doAct("BN_DEPT_INFOS","getDeptInfos",{"where":"   and chnl_id in (select CHNL_ID from BN_DEPT_CHNL where DEPT_CODE='${DEPT_CODE}')"});
		var listBeans = newsData.listBeans;
		if(listBeans.length==0){
			jQuery("#showNews").append("<tr><td align='center' style='width:76%;position: relative;'><a id = '' style='width:100%;margin-left:3px;display: block;height:28px;' href='javascript:void(0);' ><span class='elipd'>该部门暂无新闻</span></a></td><td style='width:20%;'><span style='float:right;margin-right:6px;color:#999999;'></span></td></tr>");
		}else{
			for(var i=0;i<listBeans.length;i++){
				var listBean = listBeans[i];
				jQuery("#showNews").append("<tr><td class='icon'></td><td style='width:76%;position: relative;'><a id = '"+listBean.NEWS_ID+"' title='"+listBean.NEWS_SUBJECT+"' style='width:100%;margin-left:3px;display: block;height:28px;' href='javascript:void(0);' onclick='newsView(\""+listBean.NEWS_ID+"\");'><span class='elipd'>"+listBean.NEWS_SUBJECT+"</span></a></td><td style='width:20%;'><span style='float:right;margin-right:6px;color:#999999;'>"+listBean.NEWS_TIME.substring(5,10)+"</span></td></tr>");
			}
		}
	}
	/*右侧文件区域的刷新方法*/
	function showFile(deptCode){
		var newsData = FireFly.doAct("BN_DEPT_INFOS","getChnlFile",{"DEPT_CODE":'${DEPT_CODE}'});
		var listBeans = newsData.listBeans;
		for(var i=0;i<listBeans.length;i++){
			var listBean = listBeans[i];
			jQuery(".news-list-right-85").append("<tr><td class='icon'></td><td style='width:76%;position: relative;'><a id = '"+listBean.DATA_ID+"' title='"+listBean.DIS_NAME+"' style='width:100%;margin-left:3px;display: block;height:28px;' href='javascript:void(0);' onclick='checkFile(\""+listBean.FILE_ID+"\",\""+listBean.DIS_NAME+"\")'><span class='elipd'>"+listBean.DIS_NAME+"</span></a></td><td style='width:20%;'><span style='float:right;margin-right:6px;color:#999999;'>"+listBean.S_MTIME.substring(5,10)+"</span></td></tr>");
		}
	}
	function checkFile(fileId,fileName){
		RHFile.read(fileId, fileName);
	}
	/*左侧下方部门树刷新方法*/
	function showDept(DeptCode){
		var _self = this;
	function replaceId (str) {
		return str.replace(/[^\w]/gi, "_");
	};
	/**
	 * 重置protal 高度
	 */
	function resetPortalHeight(){
		setTimeout(function(){
			Tab.setFrameHei($(".portal-target").height()+30);
		},100);
	}
	var odept = System.getVar('@ODEPT_CODE@');
	var dictId = "SY_COMM_INFOS_CHNL_MANAGE";
	var exwhere = "and chnl_id in ( select CHNL_ID from BN_DEPT_CHNL where DEPT_CODE='${DEPT_CODE}')";
	
	
	
	var tempData = FireFly.doAct('SY_COMM_ADDRESS_LIST_SC','createTree',{'dictId':dictId,'extWhere':exwhere});
    var orgSetting = {
           	showcheck: false,  
           	url: "SY_COMM_INFO.dict.do", 
            theme: "bbit-tree-no-lines", 
            rhexpand: false,
            cascadecheck: true,
            checkParent: false,
            childOnly:false,
            rhBeforeOnNodeClick: function (item,id) {
	        	var nodeObj = jQuery("#" + id + "_" + replaceId(item.ID),_self.navTree);
	        	if (nodeObj.hasClass("bbit-tree-selected")) {
	        		nodeObj.addClass("rh-bbit-tree-selected");
	        	}
	        },
            onnodeclick: function(item,id){
            	var nodeObj = jQuery("#" + id + "_" + replaceId(item.ID),_self.navTree);
	        	if (nodeObj.hasClass("rh-bbit-tree-selected")) {
	        		nodeObj.removeClass("bbit-tree-selected");
	        		nodeObj.removeClass("rh-bbit-tree-selected");
	        		
	        	}else{
	        	
	        	}
            },
            afterExpand: function(){
            	resetPortalHeight();
            },
            afterCollapsed: function(){
            	resetPortalHeight();
            }
        };
		if(tempData['CHILD']!="undefined"){
		orgSetting.data = tempData['CHILD']; 
		var child = orgSetting.data[0].CHILD;
		if (child.length == 1) {
			orgSetting.data = child;
		}
		orgSetting.dictId = dictId;
		var orgTree = new rh.ui.Tree(orgSetting);
		$("#ORG_TREE").append(orgTree.obj);
			}
    
	
	}
	
</script>
</html>	