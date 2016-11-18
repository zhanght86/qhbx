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
	<div id="ORG_DEPT_TREE" style="height:527px;overflow:scroll;"></div>
		
	</div>
	
</div>
<div class="portal-target wp80 floatLeft">
	
	<div  class='portal-box pt-gsyw-wrapper' style='width:100%' id='DEPT_NEWS'>
		<div class='portal-box-title' style='position:relative;background-image:none;background-color:#F9F9F9;'>
			<span class='portal-box-title-top'></span>
			<span class="portal-box-title-label" style="margin-left: 0px;" id="DEPT_NAME">部门新闻</span>
			<div class="portal-box-title-bottom-line"></div>
		</div>

		<div class='portal-box-con info-column' style='height:174px;'>
		
			<table id='showNews' width="100%" style="table-layout:fixed;margin-left:8px;">

			</table>
		</div>
		<div  class='portal-box pt-gsyw-wrapper' id='DEPT_NEWS' style='width:100%;margin-left:-1px;border-bottom-width:0px;'>

			<div class='portal-box-title ' style='position:relative;background-image:none;background-color:#F9F9F9;'>
				<span class='portal-box-title-top'></span>
				<span class="portal-box-title-label" style="margin-left: 0px;">部门文件</span>
				<div class="portal-box-title-bottom-line"></div>
			</div>

			<div class='portal-box-con' style='height:307px;'>
					<div class ="news-list-left" style="border-right:1px solid #C0C0C0;float:left;width:20%;height:300px;" >
						<div id="ORG_TREE"></div>
					</div>  
					<div class = "news-list-right" style="float:left;width:78%;height:300px;" >
						<table class = "news-list-right-85" id="showFile">
							<tr>
								<td style='width:10px;'></td>
								<td style='width:5px;'></td>
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
	/*  查询出来的数据   查询出来数据的数量    每页显示的条数 */
	var listBeansContainer ,totalFileSize , showNum =6 ;
	
	jQuery(document).ready(function(jQuery) {
		jQuery("#DEPT_NAME").text(System.getVar("@TDEPT_NAME@"));
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
		var odept = GetRequest("ODEPT_CODE");
		
		var odeptLevel = parseInt(GetRequest("ODEPT_LEVEL"));
		var dictId = "BN_DEPT";
		var exwhere = "and ODEPT_CODE='"+odept+"' and DEPT_LEVEL in("+odeptLevel+","+(odeptLevel+1)+") order by DEPT_SORT asc";
		
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
					
					window.location.href = "http://"+window.location.host+"/cms/SY_COMM_CMS_TMPL/${CHNL_PID}/index_1.html?ODEPT_LEVEL="+odeptLevel+"&CHNL_ID=${CHNL_ID}&DEPT_FLAG=1&CHNL_PID=${CHNL_PID}&DEPT_CODE="+item.ID+"&ODEPT_CODE="+odept;
					
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
var odeptCode = "0001B210000000000BU3";
			var listBean = newsData._DATA_[i];
			jQuery("#showContent").append("<tr style='line-height:20px;height:20px;'><td class='icon1'><td style='width:66%;position: relative;'><a id = '"+listBean.DEPT_CODE+"' title='"+listBean.DEPT_NAME+"' style='width:100%;margin-left:3px;display: block;height:19px;'  href='/cms/SY_COMM_CMS_TMPL/${CHNL_PID}/index_1.html?DEPT_LEVEL=2&CHNL_ID=${CHNL_ID}&DEPT_FLAG=1&CHNL_PID=${CHNL_PID}&DEPT_CODE="+listBean.DEPT_CODE+"&ODEPT_CODE="+odeptCode+"'><span class='elipd'>"+listBean.DEPT_NAME+"</span></a></td><td style='width:20%;'><span style='float:right;margin-right:6px;'></span></td></tr>");
		}*/
	}
	
	/*右侧刷新*/
	function refreshRight(deptCode){
	var deptCode1 = GetRequest("DEPT_CODE");
	
	if(deptCode1 == ""){
		deptCode1 = System.getVar('@TDEPT_CODE@');
	}
	jQuery("#DEPT_NAME").text(jQuery("div[itemid='"+deptCode1+"']").attr("title"));
		showDeptInfos(deptCode1);
		showDept(deptCode1);
		showFile(deptCode1);
	}
	/*右侧上方新闻模块的刷新方法*/
	function showDeptInfos(deptCode){
	var TDEPT_CODE = System.getVar('@TDEPT_CODE@');
		FireFly.doAct("BN_DEPT_INFOS","getDeptInfos",{"TDEPT_CODE":TDEPT_CODE,"where":" and CHNL_ID = '${CHNL_ID}'"},false,false,function(newsData){
			var listBeans = newsData.listBeans;
			jQuery("#showNews").html("");
			if(listBeans.length==0){
				jQuery("#showNews").append("<tr><td align='center' style='width:76%;position: relative;'>该部门暂无新闻!!</td></tr>");
			}else{
				for(var i=0;i<listBeans.length;i++){
					var listBean = listBeans[i];
					jQuery("#showNews").append("<tr><td style='width:10px;'></td><td class='icon'></td><td style='width:76%;position: relative;'><a data_id = '"+listBean.NEWS_ID+"' title='"+listBean.NEWS_SUBJECT+"' style='width:100%;margin-left:3px;display: block;height:28px;' href='javascript:void(0);' onclick='newsView(this);'><span class='elipd' style='color:#666;'>"+listBean.NEWS_SUBJECT+"</span></a></td><td style='width:20%;'><span style='float:right;margin-right:30px;'>["+listBean.NEWS_TIME.substring(0,10)+"]</span></td></tr>");
				}
			}
		});
		
	}
	/*右侧文件区域的刷新方法*/
	function showFile(deptCode){
		FireFly.doAct("BN_DEPT_INFOS","getDeptFile",{"DEPT_CODE":deptCode},false,false,function(newsData){
			var listBeans = newsData.listBeans;
			/* 数据保存到变量中，用于分页  */
			listBeansContainer = listBeans;
			totalFileSize = listBeans.length;
			jQuery("#showFile").html("");
			
			if(listBeans.length==0){
			/*
			jQuery(".news-list-right-85").append("<tr><td style='width:10px;'></td><td style='width:5px;'></td><td style='width:40%;position: relative;' align='center'>文件名称</td><td style='width:18%;position:relative;' align='center'>文件大小</td><td style='width:18%;position:relative;' align='center'>上传人</td><td style='width:20%;'><span style='float:right;margin-right:6px;color:#	;'>上传时间</span></td></tr>");
			
			jQuery(".news-list-right-85").append("<tr><td style='width:10px;'></td><td style='width:5px;'></td><td style='width:40%;position: relative;' align='center'>该部门没有文件可供查看</td><td style='width:18%;position:relative;' align='center'></td><td style='width:18%;position:relative;' align='center'></td><td style='width:20%;'><span style='float:right;margin-right:6px;'></span></td></tr>");
			*/
			
			buildPage(listBeans);
			
			}else if(listBeans.length<= showNum ){
			
			/**
				for(var i=0;i<listBeans.length;i++){
					var listBean = listBeans[i];
					jQuery(".news-list-right-85").append("<tr ><td style='width:10px;'></td><td class='icon' style='width:5px;'></td><td style='width:40%;position: relative;'><a id = '"+listBean.DATA_ID+"' data-id='"+listBean.FILE_ID+"' title='"+listBean.FILE_NAME+"' style='width:100%;margin-left:8px;display: block;height:28px;' href='javascript:void(0);' onclick='checkFile(this)'><span class='elipd'>"+listBean.FILE_NAME+"</span></a></td><td style='width:18%;' align='center'><span style='margin-right:6px;'>"+listBean.FILE_SIZE+"</span></td><td style='width:18%;' align='center'><span style='margin-right:6px;'>"+listBean.S_USER+"</span></td><td style='width:18%;' align='center'><span style='float:right;margin-right:6px;'>"+listBean.S_MTIME.substring(5,10)+"</span></td></tr>");
				}
			*/	
				
				buildPage(listBeans);
			}else{
				var firstShow = listBeans.slice(0,showNum);
				buildPage(firstShow);
			}
		});
	}
	/*右侧文件区域通过部门栏目的刷新方法*/
	function showFileByChnl(deptCode,chnlId){
		
		FireFly.doAct("BN_DEPT_INFOS","getDeptFile",{"DEPT_CODE":deptCode,"CHNL_ID":chnlId},false,false,function(newsData){
		
			var listBeans = newsData.listBeans;
			listBeansContainer = listBeans;
			totalFileSize = listBeans.length;
			jQuery("#showFile").html("");
			
			if(listBeans.length==0){
				buildPage(listBeans);
				
			}else if( listBeans.length <= showNum ){
				buildPage(listBeans);
			}else{
				var firstShow = listBeans.slice(0,showNum);
				buildPage(firstShow);
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
		var dictId = "BN_FILE_CHNL_VIEW";
		var exwhere = "and DEPT_CODE = '"+DeptCode+"'";
		

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
					if (nodeObj.hasClass("bbit-tree-selected n")) {
						nodeObj.addClass("rh-bbit-tree-selected n");
						
					}
				},
				onnodeclick: function(item,id){
					var nodeObj = jQuery("#" + id + "_" + replaceId(item.ID),_self.navTree);
					
					showFileByChnl(System.getVar("@TDEPT_CODE@"),item.ID);
					if (nodeObj.hasClass("rh-bbit-tree-selected n")) {
						nodeObj.removeClass("bbit-tree-selected n");
						nodeObj.removeClass("rh-bbit-tree-selected n");
						
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
			
			if(tempData['CHILD']!=undefined && tempData['CHILD'].length>0){
				orgSetting.data = tempData['CHILD']; 
				var child = orgSetting.data[0].CHILD;
				
				orgSetting.dictId = dictId;
				var orgTree = new rh.ui.Tree(orgSetting);
				$("#ORG_TREE").append(orgTree.obj);
			}

	
	}
	/*获取url中的参数*/
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

/* 分页 构建列表数据 */
function buildPage(listBeans){
	var dataList = {
		
		"_PAGE_":{"ALLNUM":1, "SHOWNUM":50, "NOWPAGE":1, "PAGES":1}, 	
		"_DATA_":[],
		"_COLS_":{
			"FILE_ID":{"ITEM_NAME":"文件ID","ITEM_CODE":"FILE_ID","ITEM_LIST_FLAG":2},
			"FILE_NAME":{"ITEM_NAME":"文件名称","ITEM_CODE":"FILE_NAME","ITEM_LIST_FLAG":1},
			"FILE_SIZE":{"ITEM_NAME":"文件大小(B)","ITEM_CODE":"FILE_SIZE","ITEM_LIST_FLAG":1},
			"FILE_USER":{"ITEM_NAME":"上传人","ITEM_CODE":"FILE_USER","ITEM_LIST_FLAG":1},
			"FILE_MTIME":{"ITEM_NAME":"上传时间","ITEM_CODE":"FILE_MTIME","ITEM_LIST_FLAG":1}
		}
	}
	
	dataList._PAGE_.ALLNUM = totalFileSize;
	
	jQuery.each(listBeans,function(j,m) {
		var bean = {"_PK_":m.FILE_ID,"FILE_NAME":m.FILE_NAME,"FILE_SIZE":m.FILE_SIZE,"FILE_USER":m.S_USER,"ROWNUM_":m._ROWNUM_+1,"_ROWNUM_":m._ROWNUM_,"FILE_ID":m.FILE_ID,"FILE_MTIME":m.S_MTIME.substring(5,10)};
		
		dataList._DATA_.push(bean);
	});
	
	jQuery(".portal-box-con .news-list-right").find("#FILE_LIST_CONTAINER").remove();
	var fileListCon = jQuery("<div id='FILE_LIST_CONTAINER'></div>").appendTo(jQuery(".portal-box-con .news-list-right"));

	var param = {
		"id":"BN_FILE_UPLOAD",
		"pid":"",
		"mainData":FireFly.getCache("BN_FILE_UPLOAD",FireFly.servMainData),
		"rowBtns":new Array(),
		"type":null,
		"pkHide":false,
		"pCon":fileListCon,
		"byIdFlag":"false",
		"batchFlag":"false",
		"sortGridFlag":"false",
		"buildPageFlag":"true",
		"listData":dataList
	};
	var grid = new rh.ui.grid(param);
	grid.render();
}

/* 下一页  */
function nextPage(listBeans){
	var nowPage = jQuery(jQuery(".portal-box-con .news-list-right .rhGrid-page .current")[0]).text();
	var begin =  nowPage * showNum;
	
	if(listBeans.length <= nowPage* showNum ){
		alert("已经是最后一页");
	}else{	
		 /* 剩余数据不满足5条  */
		if( listBeans.length < (nowPage+1)* showNum ){
			var showNextPartBeans = listBeans.slice(begin , listBeans.length );
			buildPage(showNextPartBeans);
			setNextShowNum(nowPage);
			
		}else{
			/*  展示5条数据 */
			var showNextFullBeans = listBeans.slice(begin , begin+ showNum );
			buildPage(showNextFullBeans);
			setNextShowNum(nowPage);
		}
		nowPage++;
		jQuery(jQuery(".portal-box-con .news-list-right .rhGrid-page .current")[0]).text(nowPage);
	}	
}

/*   上一条  */
function forwardPage(listBeans){
	var nowPage = jQuery(jQuery(".portal-box-con .news-list-right .rhGrid-page .current")[0]).text();
	
	if( nowPage==1 ){
		alert("已经是第一页");
	}else if(nowPage > 1 ){
		var begin =  (nowPage-2) * showNum;
		var showForwardBean = listBeans.slice(begin , begin+showNum );
		buildPage(showForwardBean);
		setForwardShowNum(nowPage);
		nowPage --;
		jQuery(jQuery(".portal-box-con .news-list-right .rhGrid-page .current")[0]).text(nowPage);
	}
}

/*  重设列表前面的序号 如第二页 6.7...   */
function setNextShowNum (nowPage){
	var trs = jQuery(".portal-box-con .news-list-right table:last tbody tr");
	var startNum = nowPage * showNum +1;
	$.each(trs,function(n,value){
		jQuery(value).find(".indexTD").text(startNum);
		startNum++;
	});
}

function setForwardShowNum (nowPage){
	var trs = jQuery(".portal-box-con .news-list-right table:last tbody tr");
	var startNum = (nowPage-2) * showNum +1;
	if(nowPage>1){
		$.each(trs,function(n,value){
			jQuery(value).find(".indexTD").text(startNum);
			startNum++;
		});
	}
}

/*   给分页按钮添加事件  */
jQuery(document).ready(function(jQuery){
	jQuery(".portal-box-con .news-list-right .rhGrid-page .disabled:first").live('click',function(){
		forwardPage(listBeansContainer);
	});
	jQuery(".portal-box-con .news-list-right .rhGrid-page .disabled:last").live('click',function(){
		nextPage(listBeansContainer);
	});

});

</script>
</html>	