<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title>文档内容</title>
<script type="text/javascript" src="/sy/base/frame/coms/tree/jquery.tree.js"></script>
<script type="text/javascript" src="/sy/base/frame/coms/tree/ui.tree.js"></script>
<script type="text/javascript" src="/sc/js/sc_projectJs.js"></script>
<meta http-equiv="X-UA-Compatible" content="IE=Edge">
<meta http-equiv="content-type" content="text/html; charset=UTF-8">
</head>
<body class="portalBody">
<link rel="stylesheet" type="text/css" href="/sc/css/system_reset.css"/>
<style>
.portal-box-con table tbody tr td.icon1{width:3px;height:9px;margin-top:10px;background:url('/sc/img/pt_style/default/front_bg.png') no-repeat 0 -689px}
.portal-box-con table tbody tr:hover td.icon1{background-position:0 -709px}
</style>
<div id="dept_code" style="display:none">${DEPT_CODE}</div>
<div class="portal-target wp20 floatLeft">
	<div class='portal-box pt-gsyw-wrapper '>
	<div class='portal-box-title ' style='position:relative;'>
		<span class='portal-box-title-top'></span>
		<span class="portal-box-title-label" style="">公司部门</span>
		<div class="portal-box-title-bottom-line"></div>
	</div>
	</div>
	<div class='portal-box-con info-column' style='height:527px;overflow-x:hidden;'>
	<div id="ORG_DEPT_TREE"></div>
		<table id='showContent' width="100%" style="table-layout:fixed;margin-left:8px;">
			
		</table>
	</div>
	
</div>
<div class="portal-target wp80 floatLeft">
	
	<div  class='portal-box pt-gsyw-wrapper' id=''>
		<div class='portal-box-title' style='position:relative;'>
			<span class='portal-box-title-top'></span>
			<span class="portal-box-title-label" style="margin-left: 0px;">部门新闻</span>
			<div class="portal-box-title-bottom-line"></div>
		</div>

		<div class='portal-box-con info-column' style='height:174px;'>
		
			<table id='showNews' width="100%" style="table-layout:fixed;margin-left:8px;">

			</table>
		</div>
		<div  class='portal-box pt-gsyw-wrapper' id=''>

			<div class='portal-box-title ' style='position:relative;'>
				<span class='portal-box-title-top'></span>
				<span class="portal-box-title-label" style="margin-left: 0px;">部门文件</span>
				<div class="portal-box-title-bottom-line"></div>
			</div>

			<div class='portal-box-con' style='height:300px;'>
					<div class ="news-list-left" style="border-right:1px solid #C0C0C0;float:left;width:14%;height:300px;" >
						<div id="ORG_TREE"></div>
					</div>  
					<div class = "news-list-right" style="float:left;width:84%;height:300px;" >
						<table class = "news-list-right-85" id="showFile">
							<tr>
								<td style='width:15px;'></td>
								<td style='width:40%;position: relative;' align='center'>
									文件名称
								</td>
								<td style='width:18%;position:relative;color:#999999;' align='center'>文件大小</td>
								<td style='width:18%;position:relative;color:#999999;' align='center'>上传人</td>
								<td style='width:20%;'>
									<span style='float:right;margin-right:6px;color:#999999;'>上传时间</span>
								</td>
							</tr>
						</table>
					</div>
				
			</div>
		</div>
	</div>
</div>
</body>
<script type ="text/javascript" >
	jQuery(document).ready(function(jQuery) {
		/*
		var boxHtml = jQuery("#BN_NEWS_FILE_DQ__temp #BN_NEWS_FILE_DQ__box").html();
		jQuery("#BN_NEWS_FILE_DQ__temp").html("<div class='portal-box pt-gsyw-wrapper' id='BN_NEWS_FILE_DQ__box'>" + boxHtml + "</div>");
		*/
		var odeptCode = "0001B210000000000BU3";
		FireFly.doAct("SY_COMM_INFOS_CHNL_VIEW","getDeptAll",{"_ROWNUM_":100,"ODEPT_CODE":odeptCode},null,false,function(returnData){
			showContent(returnData);
			refreshRight(System.getVar('@TDEPT_CODE@'));
		});
	});
	/*打开新闻*/
	function newsView(element){
		var url = "/cms/SY_COMM_INFOS/" + $(element).attr("data_id") + ".html";			
		window.open(url);
	}
/*左侧导航树的刷新方法*/
	function showContent(newsData){ 
	var _self = this;
		function replaceId (str) {
			return str.replace(/[^\w]/gi, "_");
		};
		
		function resetPortalHeight(){
			setTimeout(function(){
				Tab.setFrameHei($(".portal-target").height()+30);
			},100);
		}
		var odept = System.getVar('@ODEPT_CODE@');
		var dictId = "BN_DEPT";
		var exwhere = "and ODEPT_CODE='${ODEPT_CODE}' and DEPT_LEVEL in(2,3)";
		
		
		
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
					
					window.location.href = "http://"+window.location.host+"/cms/SY_COMM_CMS_TMPL/${CHNL_PID}/index_1.html?DEPT_LEVEL=2&CHNL_ID=${CHNL_ID}&DEPT_FLAG=1&CHNL_PID=${CHNL_PID}&DEPT_CODE="+item.ID+"&ODEPT_CODE="+System.getVar("@ODEPT_CODE@")+"";
					
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
			if(tempData['CHILD']!=undefined){
			
				orgSetting.data = tempData['CHILD']; 
				var child = orgSetting.data[0].CHILD;
				if (child.length == 1) {
					orgSetting.data = child;
				}
				orgSetting.dictId = dictId;
				var orgTree = new rh.ui.Tree(orgSetting);
				$("#ORG_DEPT_TREE").append(orgTree.obj);
			}
		/*for(var i=0;i<newsData._DATA_.length;i++){
var odeptCode = "0001B210000000000BU3";
			var listBean = newsData._DATA_[i];
			jQuery("#showContent").append("<tr style='line-height:20px;height:20px;'><td class='icon1'><td style='width:66%;position: relative;'><a id = '"+listBean.DEPT_CODE+"' title='"+listBean.DEPT_NAME+"' style='width:100%;margin-left:3px;display: block;height:19px;'  href='/cms/SY_COMM_CMS_TMPL/${CHNL_PID}/index_1.html?DEPT_LEVEL=2&CHNL_ID=${CHNL_ID}&DEPT_FLAG=1&CHNL_PID=${CHNL_PID}&DEPT_CODE="+listBean.DEPT_CODE+"&ODEPT_CODE="+odeptCode+"'><span class='elipd'>"+listBean.DEPT_NAME+"</span></a></td><td style='width:20%;'><span style='float:right;margin-right:6px;color:#999999;'></span></td></tr>");
		}*/
	}
	
	/*右侧刷新*/
	function refreshRight(deptCode){
		showDeptInfos(deptCode);
		showDept(deptCode);
		showFile(deptCode);
	}
	/*右侧上方新闻模块的刷新方法*/
	function showDeptInfos(deptCode){
		FireFly.doAct("BN_DEPT_INFOS","getDeptInfos",{"S_TDEPT":"","where":" and chnl_id in (select CHNL_ID from BN_DEPT_CHNL where DEPT_CODE='${DEPT_CODE}')"},false,false,function(newsData){
			var listBeans = newsData.listBeans;
			jQuery("#showNews").html("");
			if(listBeans.length==0){
				jQuery(".news-list-right-85").append("<tr><td style='width:15px;'></td><td align='center' style='width:36%;position: relative;'>该部门没有文件可供查看</td><td></td><td></td><td></td></tr>");
			}else{
				for(var i=0;i<listBeans.length;i++){
					var listBean = listBeans[i];
					jQuery("#showNews").append("<tr><td class='icon'></td><td style='width:76%;position: relative;'><a data_id = '"+listBean.NEWS_ID+"' title='"+listBean.NEWS_SUBJECT+"' style='width:100%;margin-left:3px;display: block;height:28px;' href='javascript:void(0);' onclick='newsView(this);'><span class='elipd'>"+listBean.NEWS_SUBJECT+"</span></a></td><td style='width:20%;'><span style='float:right;margin-right:30px;color:#999999;'>"+listBean.NEWS_TIME.substring(0,10)+"</span></td></tr>");
				}
			}
		});
		
	}
	/*右侧文件区域的刷新方法*/
	function showFile(deptCode){
		FireFly.doAct("BN_DEPT_INFOS","getChnlFile",{"DEPT_CODE":deptCode},false,false,function(newsData){
			var listBeans = newsData.listBeans;
			jQuery("#showFile").html("");
			jQuery(".news-list-right-85").append("<tr><td style='width:15px;'></td><td style='width:40%;position: relative;' align='center'>文件名称</td><td style='width:18%;position:relative;color:#999999;' align='center'>文件大小</td><td style='width:18%;position:relative;color:#999999;' align='center'>上传人</td><td style='width:20%;'><span style='float:right;margin-right:6px;color:#999999;'>上传时间</span></td></tr>");
			if(listBeans.length==0){
				jQuery(".news-list-right-85").append("<tr><td style='width:15px;'></td><td align='center' style='width:36%;position: relative;'>该部门没有文件可供查看</td><td></td><td></td><td></td></tr>");
			}else{
				for(var i=0;i<listBeans.length;i++){
					var listBean = listBeans[i];
					jQuery(".news-list-right-85").append("<tr ><td class='icon' style='width:15px;'></td><td style='width:40%;position: relative;'><a id = '"+listBean.DATA_ID+"' data-id='"+listBean.FILE_ID+"' title='"+listBean.DIS_NAME+"' style='width:100%;margin-left:8px;display: block;height:28px;' href='javascript:void(0);' onclick='checkFile(this)'><span class='elipd'>"+listBean.DIS_NAME+"</span></a></td><td style='width:18%;' align='center'><span style='margin-right:6px;color:#999999;'>"+listBean.FILE_SIZE+"</span></td><td style='width:18%;' align='center'><span style='margin-right:6px;color:#999999;'>"+listBean.S_UNAME+"</span></td><td style='width:18%;' align='center'><span style='float:right;margin-right:6px;color:#999999;'>"+listBean.S_MTIME.substring(5,10)+"</span></td></tr>");
				}
			}
		});
		
		
	}
	/*右侧文件区域通过部门栏目的刷新方法*/
	function showFileByChnl(deptCode,chnlId){
		FireFly.doAct("BN_DEPT_INFOS","getChnlFile",{"DEPT_CODE":deptCode,"CHNL_ID":chnlId},false,false,function(newsData){
			var listBeans = newsData.listBeans;
			jQuery("#showFile").html("");
			jQuery(".news-list-right-85").append("<tr><td style='width:15px;'></td><td style='width:40%;position: relative;' align='center'>文件名称</td><td style='width:18%;position:relative' align='center'>文件大小</td><td style='width:18%;position:relative' align='center'>上传人</td><td style='width:20%;'><span style='float:right;margin-right:6px;color:#999999;'>上传时间</span></td></tr>");
			if(listBeans.length==0){
				jQuery(".news-list-right-85").append("<tr><td style='width:15px;'></td><td align='center' style='width:36%;position: relative;'>该部门没有文件可供查看</td><td></td><td></td><td></td></tr>");
			}else{
				for(var i=0;i<listBeans.length;i++){
					var listBean = listBeans[i];
					jQuery(".news-list-right-85").append("<tr ><td class='icon' style='width:15px;'></td><td style='width:40%;position: relative;'><a id = '"+listBean.DATA_ID+"' data-id='"+listBean.FILE_ID+"' title='"+listBean.DIS_NAME+"' style='width:100%;margin-left:8px;display: block;height:28px;' href='javascript:void(0);' onclick='checkFile(this)'><span class='elipd'>"+listBean.DIS_NAME+"</span></a></td><td style='width:18%;' align='center'><span style='margin-right:6px;color:#999999;'>"+listBean.FILE_SIZE+"</span></td><td style='width:18%;' align='center'><span style='margin-right:6px;color:#999999;'>"+listBean.S_UNAME+"</span></td><td style='width:18%;' align='center'><span style='float:right;margin-right:6px;color:#999999;'>"+listBean.S_MTIME.substring(5,10)+"</span></td></tr>");
				}
			}
		});
		
	}
	function checkFile(element){
	var fileId = $(element).attr("data-id");
	
	var fileName = $(element).attr("title");
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
		var odept = "0001B210000000000BU3";
		var dictId = "SY_COMM_INFOS_CHNL_MANAGE";
		var exwhere = "and DEPT_CODE = '"+System.getVar("@TDEPT_CODE@")+"'";
		
		
		
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
					
					showFileByChnl(System.getVar("@TDEPT_CODE@"),item.ID);
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
			if(tempData['CHILD']!=undefined){
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