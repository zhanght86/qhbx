
	var odeptCode = System.getUser("ODEPT_CODE");
	FireFly.doAct("SY_COMM_INFOS_CHNL_VIEW","getDeptAll",{"_ROWNUM_":100,"ODEPT_CODE":odeptCode},null,false,function(returnData){

			 refreshRight(returnData);
	});
/*��ർ������ˢ�·���*/
	function showContent(newsData){ 
	
		for(var i=0;i<newsData._DATA_.length;i++){
			var listBean = newsData._DATA_[i];
			jQuery("#showContent").append("<tr><td class='icon'></td><td style='width:56%;position: relative;'><a id = '"+listBean.DEPT_CODE+"' title='"+listBean.DEPT_NAME+"' style='width:100%;margin-left:3px;display: block;height:28px;' href='#' onclick='refreshRight("+listBean.DEPT_CODE+")'><span class='elipd'>"+listBean.DEPT_NAME+"</span></a></td><td style='width:20%;'><span style='float:right;margin-right:6px;color:#999999;'>......</span></td></tr>");
		}
	}
	/*�Ҳ�ˢ��*/
	function refreshRight(deptCode){
	
		showContent(deptCode);
		showDeptInfos(deptCode);
		showFile(deptCode);
		
	}
	/*�Ҳ��Ϸ�����ģ���ˢ�·���*/
	function showDeptInfos(deptCode){
		var newsData = FireFly.doAct("BN_DEPT_INFOS","getDeptInfos",{"where":" AND S_ODEPT = '"+System.getUser("ODEPT_CODE")+"'"});
		var listBeans = newsData.listBeans;
		for(var i=0;i<listBeans.length;i++){
		var listBean = listBeans[i];
		jQuery("#showNews").append("<tr><td class='icon'></td><td style='width:76%;position: relative;'><a id = '"+listBean.NEWS_ID+"' title='"+listBean.NEWS_SUBJECT+"' style='width:100%;margin-left:3px;display: block;height:28px;' href='#' onclick='newsView("+listBean.NEWS_ID+")'><span class='elipd'>"+listBean.NEWS_SUBJECT+"</span></a></td><td style='width:20%;'><span style='float:right;margin-right:6px;color:#999999;'>"+listBean.NEWS_TIME.substring(5,10)+"</span></td></tr>");
		}
	}
	/*�Ҳ��ļ������ˢ�·���*/
	function showFile(deptCode){
		
	}
	/*����·�������ˢ�·���*/
	function showDept(DeptCode){
	
	}