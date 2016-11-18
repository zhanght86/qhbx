
<div  class='portal-box pt-gsyw-wrapper' id='${id}__box'>
<div class='portal-box-title ' style='position:relative;'>
	<span class='portal-box-title-top'></span>
	<span class="portal-box-title-label">公司部门</span>
	<div class="portal-box-title-bottom-line"></div>
</div>
<div class='portal-box-con info-column' style='height:261px;overflow-y:auto;overflow-x:hidden;'>
<table id='showContent'width="100%" style="table-layout:fixed;margin-left:8px;">

</table>
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
		
		}	FireFly.doAct("SY_COMM_INFOS_CHNL_VIEW","getDeptAll",{"_ROWNUM_":100,"ODEPT_CODE":odeptCode},null,false,function(returnData){
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
</script>