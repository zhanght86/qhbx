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
<style type="text/css">
.backhome {float:right;}
.backhome a{color:red;text-decoration:none;}
.portal-box-con table tbody tr td.icon2{width:3px;height:9px;margin-top:10px;background:url('/sc/img/pt_style/default/front_bg.png') no-repeat 0 -689px}
.portal-box-con table tbody tr:hover td.icon2{background-position:0 -709px}
</style>
</head>
<body class="portalBody">
<link rel="stylesheet" type="text/css" href="/sc/css/system_reset.css"/>
<div id="dept_code" style="display:none">${DEPT_CODE}</div>
<div class="portal-target wp20 floatLeft">
<div class='portal-box pt-gsyw-wrapper '>
	<div class='portal-box-title ' style='position:relative;background-color:#F5F5F5;'>
		<span class='portal-box-title-top'></span>
		<span class="portal-box-title-label" style="margin-left: 0px;">公司部门</span>
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
	<link rel="stylesheet" type="text/css" href="/sc/css/system_reset.css"/>
	<div  class='portal-box pt-gsyw-wrapper' id='' style="width:100%;">
		<div class='portal-box-title' style='position:relative;background-image:none;background-color:#F9F9F9;'>
			<span class='portal-box-title-top'></span>
			<span class="portal-box-title-label" style="margin-left: 0px;">部门新闻</span>
			<div class="portal-box-title-bottom-line" style="height:0px;"></div>
			<#if (DEPT_FLAG=="1")>
		
		<#else>
		<span class='portal-box-title-label backhome' style="float:right;">
		<a href="javascript:void(0)" style='color:red;' onclick="backhome()">返回分公司首页</a>
		</span>
		</#if>
		</div>

		<div class='portal-box-con info-column' style='height:174px;'>
			<table id='showNews' width="100%" style="table-layout:fixed;margin-left:8px;">

			</table>
		</div>
		<div  class='portal-box pt-gsyw-wrapper' style='width:100%;margin-left:-1px;border-bottom-width:0px;' id='DEPT_NEWS'>

			<div class='portal-box-title ' style='position:relative;background-image:none;background-color:#F9F9F9;'>
				<span class='portal-box-title-top'></span>
				<span class="portal-box-title-label" style="margin-left: 0px;">部门文件</span>
				<div class="portal-box-title-bottom-line" style="height:0px;"></div>
			</div>

			<div class='portal-box-con' style='height:300px;'>
					<div class ="news-list-left" style="border-right:1px solid #C0C0C0;float:left;width:20%;height:307px;" >
						<div id="ORG_TREE"></div>
					</div>  
					<div class = "news-list-right" style="float:left;width:78%;height:300px;" >
						<table class = "news-list-right-85" id="showFile">
							<tr>
								<td style='width:15px;'></td>
								<td style='width:40%;position: relative;' align='center'>
									文件名称
								</td>
								<td style='width:18%;position:relative;' align='center'>文件大小</td>
								<td style='width:18%;position:relative;' align='center'>上传人</td>
								<td style='width:20%;'>
									<span style='float:right;margin-right:6px;'>上传时间</span>
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
		
		
		var odeptCode = "0001B210000000000BU3";
		FireFly.doAct("SY_COMM_INFOS_CHNL_VIEW","getDeptAll",{"_ROWNUM_":100,"ODEPT_CODE":"${ODEPT_CODE}"},null,false,function(returnData){
			 showContent(returnData);
			 refreshRight('${DEPT_CODE}');
			
			 jQuery("div[itemid='${DEPT_CODE}']").addCss("bbit-tree-selected");
		});
	});
	function backhome(){
	window.location.href="http://"+window.location.host+"/SY_COMM_TEMPL.show.do?pkCode=SY_HOME_OA_SUB&ODEPT_CODE=${ODEPT_CODE}";
	/*var options = {"url":"SY_COMM_TEMPL.show.do?pkCode=SY_HOME_OA_SUB","tTitle":"test","params":{},"menuFlag":3};
		window.open("SY_COMM_TEMPL.show.do?pkCode=SY_HOME_OA_SUB");*/
	}
/*左侧导航树的刷新方法*/
	function showContent(newsData){ 
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
		var dictId = "BN_DEPT";
		var exwhere = "and ODEPT_CODE='${ODEPT_CODE}' and DEPT_LEVEL in(${ODEPT_LEVEL},${ODEPT_LEVEL}+1) order by DEPT_SORT asc";
		
		
		
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
					jQuery("div[itemid='"+GetRequest("DEPT_CODE")+"']").removeClass("bbit-tree-selected");
					jQuery("div[itemid='"+GetRequest("DEPT_CODE")+"']").removeClass("rh-bbit-tree-selected");
					var nodeObj = jQuery("#" + id + "_" + replaceId(item.ID),_self.navTree);
					if (nodeObj.hasClass("bbit-tree-selected")) {
						nodeObj.addClass("rh-bbit-tree-selected");
						
					}
				},
				onnodeclick: function(item,id){
					var nodeObj = jQuery("#" + id + "_" + replaceId(item.ID),_self.navTree);
					
					refreshRight(item.ID);
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
			jQuery("div[itemid='"+GetRequest("DEPT_CODE")+"']").addClass("bbit-tree-selected");
			jQuery("div[itemid='"+GetRequest("DEPT_CODE")+"']").addClass("rh-bbit-tree-selected");
		/*for(var i=0;i<newsData._DATA_.length;i++){
			var listBean = newsData._DATA_[i];
			jQuery("#showContent").append("<tr style='height: 20px; line-height: 20px;'><td class='icon2'><td style='width:66%;position: relative;'><a id = '"+listBean.DEPT_CODE+"' title='"+listBean.DEPT_NAME+"' style='width:100%;margin-left:3px;display: block;height:20px;' href='/cms/SY_COMM_CMS_TMPL/${CHNL_PID}/index_1.html?CHNL_ID=${CHNL_ID}&DEPT_FLAG=${DEPT_FLAG}&CHNL_PID=${CHNL_PID}&DEPT_CODE="+listBean.DEPT_CODE+"&ODEPT_CODE=${ODEPT_CODE}'><span class='elipd'>"+listBean.DEPT_NAME+"</span></a></td><td style='width:20%;'><span style='float:right;margin-right:6px;'></span></td></tr>");
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
	
		var newsData = FireFly.doAct("BN_DEPT_INFOS","getDeptInfos",{"where":" and CHNL_ID = '${CHNL_ID}'","TDEPT_CODE":deptCode});
		var listBeans = newsData.listBeans;
		jQuery("#showNews").html("");
		if(listBeans.length==0){
			jQuery("#showNews").append("<tr><td align='center' style='width:76%;position: relative;'>该部门暂无新闻!!</td></tr>");
		}else{
			for(var i=0;i<listBeans.length;i++){
				var listBean = listBeans[i];
				jQuery("#showNews").append("<tr><td style='width:10px;'></td><td class='icon' style='5px;'></td><td style='width:76%;position: relative;'><a id = '"+listBean.NEWS_ID+"' title='"+listBean.NEWS_SUBJECT+"' style='width:100%;margin-left:3px;display: block;height:28px;' href='javascript:void(0);' onclick='newsView(\""+listBean.NEWS_ID+"\");'><span class='elipd'>"+listBean.NEWS_SUBJECT+"</span></a></td><td style='width:20%;'><span style='float:right;margin-right:6px;'>["+listBean.NEWS_TIME.substring(5,10)+"]</span></td></tr>");
			}
		}
	}
	/*右侧文件区域的刷新方法*/
	function showFile(deptCode){

		jQuery(".news-list-right-85 tr").remove();
		
		var newsData = FireFly.doAct("BN_DEPT_INFOS","getDeptFile",{"DEPT_CODE":deptCode});
		var listBeans = newsData.listBeans;
			jQuery("#showFile").html("");
			jQuery(".news-list-right-85").append("<tr><td style='width:10px;'></td><td style='width:5px;'></td><td style='width:40%;position: relative;' align='center'>文件名称</td><td style='width:18%;position:relative;' align='center'>文件大小</td><td style='width:18%;position:relative;' align='center'>上传人</td><td style='width:20%;'><span style='float:right;margin-right:6px;'>上传时间</span></td></tr>");
		if(listBeans.length==0){
			jQuery(".news-list-right-85").append("<tr><td style='width:10px;'></td><td style='width:5px;'></td><td align='center' style='width:36%;position: relative;'>该部门没有文件可供查看</td><td></td><td></td><td></td></tr>");
		}else{
			for(var i=0;i<listBeans.length;i++){
			/**
				var listBean = listBeans[i];
				jQuery(".news-list-right-85").append("<tr ><td style='width:10px;'></td><td class='icon' style='width:5px;'></td><td style='width:40%;position: relative;'><a id = '"+listBean.DATA_ID+"' title='"+listBean.DIS_NAME+"' style='width:100%;margin-left:8px;display: block;height:28px;' href='javascript:void(0);' onclick='checkFile(\""+listBean.FILE_ID+"\",\""+listBean.DIS_NAME+"\")'><span class='elipd'>"+listBean.DIS_NAME+"</span></a></td><td style='width:18%;' align='center'><span style='margin-right:6px;'>"+listBean.FILE_SIZE+"</span></td><td style='width:18%;' align='center'><span style='margin-right:6px;'>"+listBean.S_UNAME+"</span></td><td style='width:18%;' align='center'><span style='float:right;margin-right:6px;'>"+listBean.S_MTIME.substring(5,10)+"</span></td></tr>");
			*/	
				var listBean = listBeans[i];
				jQuery(".news-list-right-85").append("<tr ><td style='width:10px;'></td><td class='icon' style='width:5px;'></td><td style='width:40%;position: relative;'><a id = '"+listBean._ID+"' title='"+listBean.FILE_NAME+"' style='width:100%;margin-left:8px;display: block;height:28px;' href='javascript:void(0);' onclick='checkFile(\""+listBean.FILE_ID+"\",\""+listBean.FILE_NAME+"\")'><span class='elipd'>"+listBean.FILE_NAME+"</span></a></td><td style='width:18%;' align='center'><span style='margin-right:6px;'>"+listBean.FILE_SIZE+"</span></td><td style='width:18%;' align='center'><span style='margin-right:6px;'>"+listBean.S_USER+"</span></td><td style='width:18%;' align='center'><span style='float:right;margin-right:6px;'>"+listBean.S_MTIME.substring(5,10)+"</span></td></tr>");
				
				
			}
		}
		
	}
	function checkFile(fileId,fileName){
		RHFile.read(fileId, fileName);
	}
	/*右侧文件区域通过部门栏目的刷新方法*/
	function showFileByChnl(deptCode,chnlId){
		jQuery(".news-list-right-85 tr").remove();
		var newsData = FireFly.doAct("BN_DEPT_INFOS","getDeptFile",{"DEPT_CODE":deptCode,"CHNL_ID":chnlId});
		var listBeans = newsData.listBeans;
			jQuery("#showFile").html("");
			/**
			jQuery(".news-list-right-85").append("<tr><td style='width:15px;'></td><td style='width:40%;position: relative;' align='center'>文件名称</td><td style='width:18%;position:relative' align='center'>文件大小</td><td style='width:18%;position:relative' align='center'>上传人</td><td style='width:20%;'><span style='float:right;margin-right:6px;'>上传时间</span></td></tr>");
			*/
			
			jQuery(".news-list-right-85").append("<tr><td style='width:10px;'></td><td style='width:5px;'></td><td style='width:40%;position: relative;' align='center'>文件名称</td><td style='width:18%;position:relative;' align='center'>文件大小</td><td style='width:18%;position:relative;' align='center'>上传人</td><td style='width:20%;'><span style='float:right;margin-right:6px;'>上传时间</span></td></tr>");
			
		if(listBeans.length==0){
		/**
			jQuery(".news-list-right-85").append("<tr><td style='width:15px;'></td><td align='center' style='width:36%;position: relative;'>该部门没有文件可供查看</td><td></td><td></td><td></td><td></td></tr>");
		*/
		jQuery(".news-list-right-85").append("<tr><td style='width:10px;'></td><td style='width:5px;'></td><td style='width:40%;position: relative;' align='center'>该部门没有文件可供查看</td><td style='width:18%;position:relative;' align='center'></td><td style='width:18%;position:relative;' align='center'></td><td style='width:20%;'><span style='float:right;margin-right:6px;'></span></td></tr>");
		
		}else{
			for(var i=0;i<listBeans.length;i++){
			/**
				var listBean = listBeans[i];
				jQuery(".news-list-right-85").append("<tr ><td class='icon' style='width:15px;'></td><td style='width:40%;position: relative;'><a id = '"+listBean.DATA_ID+"' title='"+listBean.DIS_NAME+"' style='width:100%;margin-left:8px;display: block;height:28px;' href='javascript:void(0);' onclick='checkFile(\""+listBean.FILE_ID+"\",\""+listBean.DIS_NAME+"\")'><span class='elipd'>"+listBean.DIS_NAME+"</span></a></td><td style='width:18%;' align='center'><span style='margin-right:6px;'>"+listBean.FILE_SIZE+"</span></td><td style='width:18%;' align='center'><span style='margin-right:6px;'>"+listBean.S_UNAME+"</span></td><td style='width:18%;' align='center'><span style='float:right;margin-right:6px;'>"+listBean.S_MTIME.substring(5,10)+"</span></td></tr>");
			*/
			
				var listBean = listBeans[i];
				jQuery(".news-list-right-85").append("<tr ><td style='width:10px;'></td><td class='icon' style='width:5px;'></td><td style='width:40%;position: relative;'><a id = '"+listBean._ID+"' title='"+listBean.FILE_NAME+"' style='width:100%;margin-left:8px;display: block;height:28px;' href='javascript:void(0);' onclick='checkFile(\""+listBean.FILE_ID+"\",\""+listBean.FILE_NAME+"\")'><span class='elipd'>"+listBean.FILE_NAME+"</span></a></td><td style='width:18%;' align='center'><span style='margin-right:6px;'>"+listBean.FILE_SIZE+"</span></td><td style='width:18%;' align='center'><span style='margin-right:6px;'>"+listBean.S_USER+"</span></td><td style='width:18%;' align='center'><span style='float:right;margin-right:6px;'>"+listBean.S_MTIME.substring(5,10)+"</span></td></tr>");
			
			}
		}
		
	}
	/*左侧下方部门树刷新方法*/
	function showDept(DeptCode){
	    var deptCode = DeptCode;
		jQuery("#ORG_TREE").html("");
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
		var dictId = "BN_FILE_CHNL_VIEW";
		var exwhere = "and DEPT_CODE='"+DeptCode+"'";
		
		var tempData = FireFly.doAct('SY_COMM_ADDRESS_LIST_SC','createTree',{'dictId':dictId,'extWhere':exwhere});
		var orgSetting = {
				showcheck: false,  
				url: "SY_COMM_INFO.dict.do", 
				theme: "bbit-tree-no-lines", 
				rhexpand: false,
				cascadecheck: true,
				checkParent: false,
				childOnly:true,
				rhBeforeOnNodeClick: function (item,id) {
					var nodeObj = jQuery("#" + id + "_" + replaceId(item.ID),_self.navTree);
					if (nodeObj.hasClass("bbit-tree-selected")) {
						nodeObj.addClass("rh-bbit-tree-selected");
					}
				},
				onnodeclick: function(item,id){
				debugger;
					if(item.LEAF==1){
						showFileByChnl('${DEPT_CODE}',item.ID);
					}
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
			if(tempData['CHILD']!=undefined){
				orgSetting.data = tempData['CHILD']; 
				if( orgSetting.data.length > 0 ){
					var child = orgSetting.data[0].CHILD;
				
					orgSetting.dictId = dictId;
					var orgTree = new rh.ui.Tree(orgSetting);
					$("#ORG_TREE").append(orgTree.obj);
				}
				
			}

	}
function GetRequest(param) {
  var url = location.search; 
   if (url.indexOf("?") != -1) {
      var str = url.substr(1);
	  
      strs = str.split("&");
	  
      for(var i = 0; i < strs.length; i ++) {
			if(param==strs[i].split("=")[0]){
				return strs[i].split("=")[1];
			}
      }
	return "";
   }
   
}	
</script>
</html>	