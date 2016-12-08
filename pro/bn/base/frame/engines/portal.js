function mouseAbove(){
	$("#tableChnl").css('display','block');
}

function mouseAbove2(){
	a=false;
}
function mouseLeave2(){
a=true;
mouseLeave();
}
var a=true;
function mouseLeave(){
	setTimeout(
		function(){
			if(a){
				$("#tableChnl").css('display','none');
			}
		},50);

}
function showSubCmpy(){
	var tabDiv = jQuery(".tabDiv");
		/* var hoverStr="<div class='banner-color' id='tableChnl' style='display:none'><table border='0' cellSpacing='0' cellPadding='4' width='100%'><div id='forlikpic' style='margin:5px 70px;'  onmouseover='mouseAbove2();' onmouseleave='mouseLeave2();'></div></table></div>";
		var hoverStr = "<div style='height:4px;width:100%;background-color:#D50310'></div>";
		$(hoverStr).appendTo(tabDiv); */
		//ptManaglink.initLink();
		//ֻ���ܹ�˾չʾ��Ŀ
		//ptManaglink.showChnl();
}
var ptManaglink = {
	initLink:function(){
		var odeptCode = System.getUser("ODEPT_CODE");
		FireFly.doAct("SY_COMM_INFOS_CHNL_VIEW","getDeptSF",{"_ROWNUM_":1000},null,false,function(returnData){
			 ptManaglink.createLinks(returnData);
		});
	},
	createLinks:function(SYArray){debugger;
		jQuery.each(SYArray._DATA_,function(n,syslink) {
			var code = syslink.DEPT_CODE;
			var name = syslink.DEPT_NAME;
			var aaa ="<div class='bannercss' title="+name+" style='float:left;padding:5px; font-size:12px;width:80px;height:20px;text-align:center;'><a id="+code+" style='cursor:pointer;'>"+name+"</a></div>";
			$("#forlikpic").append(aaa);
			/*�󶨴����¼�*/
			ptManaglink.createhref(code);
		});
	},
	createhref:function(code){
		$("#"+code).unbind("click").bind("click",function(){
			var params = {};
			var where = " and CHNL_ID = '" +code+"'";
			/*var options = {"url":"SY_COMM_TEMPL.show.do?pkCode=SY_HOME_OA_SUB","tTitle":"�ֹ�˾�Ż�","params":params,"menuFlag":1};
			var tabP = jQuery.toJSON(options);
			tabP = tabP.replace(/\"/g,"'");
			href='javascript:top.Tab.open({'scrollFlag':true , 'url':'http://www.taikang.com','tTitle':'̩������'});'
			Tab.open("/sy/comm/page/page.jsp?openTab="+(tabP)+"&where="+encodeURIComponent(where));
			var pageView = new rh.vi.portalView(options);
			pageView.show();*/
			window.open("/sy/comm/page/portal.jsp?sysSub=aa");
		});
	},
	showChnl:function(){
		var tab = jQuery(".tabDiv .ui-tabs-selected");
		if(tab=="system"){
			$("#tableChnl").css('display','block');
		}
	}
};
				

