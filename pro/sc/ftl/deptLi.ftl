
<div  class='portal-box pt-gsyw-wrapper' id='${id}__box'>
<div class='portal-box-title ' style='position:relative;'>
	<span class='portal-box-title-top'></span>
	<span class="portal-box-title-label">公司部门</span>
	<div class="portal-box-title-bottom-line"></div>
</div>
<div class='portal-box-con info-column' id="DEPT_TREE" style='height:261px;overflow-y:auto;overflow-x:hidden;'>

</div>
</div>
<script>
jQuery(document).ready(function(jQuery){
		var boxHtml = jQuery("#${id}__temp #${id}__box").html();
		jQuery("#${id}__temp").html("<div id='${id}__box' class='portal-box pt-gsyw-wrapper'>" + boxHtml + "</div>");
		var odeptCode = System.getUser("ODEPT_CODE");
		var hrefStr = location.search;
		
		if(hrefStr.indexOf("ODEPT_CODE")>=0){
			odeptCode = GetRequest("ODEPT_CODE");
		}else if(odeptCode=="0001B210000000000BU3"){
			odeptCode = System.getVar("@C_BN_DEFAULT_CMPY_SUB@");	
		}else{
		
		}	
			createTree(odeptCode);
	FireFly.doAct("SY_COMM_INFOS_CHNL_VIEW","getDeptAll",{"_ROWNUM_":100,"ODEPT_CODE":odeptCode},null,false,function(returnData){
		 		 showContent(returnData,odeptCode);
		});
	});
function showContent(newsData,odeptCode){ 
		for(var i=0;i<newsData._DATA_.length;i++){
			var listBean = newsData._DATA_[i];
			jQuery("#showContent").append("<tr><td class='icon'></td><td style='width:67%;position: relative;'><a id = '"+listBean.DEPT_CODE+"' title='"+listBean.DEPT_NAME+"' style='width:100%;margin-left:3px;display: block;height:28px;' href='/cms/SY_COMM_CMS_TMPL/${CHNL_PID}/index_1.html?DEPT_LEVEL=3&CHNL_ID=${CHNL_ID}&DEPT_FLAG=2&CHNL_PID=${CHNL_PID}&DEPT_CODE="+listBean.DEPT_CODE+"&ODEPT_CODE="+odeptCode+"'><span class='elipd'>"+listBean.DEPT_NAME+"</span></a></td><td style='width:20%;'><span style='float:right;margin-right:6px;color:#999999;'>......</span></td></tr>");
		}
	}
function openDeptInfos(){
	var menuItemWenku = {"ID":"SY_DOC_CENTER__ruaho","NAME":"部门信息","INFO":"cms/SY_COMM_CMS_TMPL/${CHNL_PID}/index_1.html","MENU":"3"};
	Menu.linkNodeClick(menuItemWenku);
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
   }
   
}
function createTree(odeptCode){
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
	var odept = odeptCode;
	var dictId = "BN_DEPT";
	var exwhere = "and ODEPT_CODE='"+odept+"' and DEPT_LEVEL in(3,4) order by DEPT_SORT asc";
	
	
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
				window.open("http://"+window.location.host+"/sy/comm/page/portal.jsp?sysSub=ab&ODEPT_CODE="+odept+"&ODEPT_LEVEL=3&DEPT_CODE="+item.ID);
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
			$("#DEPT_TREE").append(orgTree.obj);
		}

	
}
</script>