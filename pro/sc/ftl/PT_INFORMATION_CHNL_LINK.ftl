

<style type="text/css">
.com_banner {width:100%;background-color:#64a6c9;float:left;}
.com_banner li {height:auto;line-height:auto;float:left;margin:0px 20px;font-weight:bold;}
.com_banner li a {text-decoration: none;color:white;}
.com_banner li a:hover {text-decoration: underline;} 
.bannercss {color:black}
.bannercss a:hover{color:#e7340c}
</style>



	
<table border="0" cellSpacing="0" cellPadding="4" width="100%">
					
					<tbody>
					<tr style="border-bottom-width: 5px;height: 7px;"></tr>
						<tr>
							<td style="border-bottom-width: 0px;">
							<div id="forlikpicz" style="padding-left:20px;"></div>
							</td>
						</tr>
			  		</tbody></table>
					
<script type="text/javascript">
/*初始化页面*/
jQuery(document).ready(function(){
       ptManaglinkz.initLinkz();		
			});
/*初始化方法*/
	 var ptManaglinkz = {
	 /*初始化链接*/
	 initLinkz:function(){
	 /*加载链接信息*/
	 var odeptCode = System.getUser("ODEPT_CODE");
	     FireFly.doAct("SY_COMM_INFOS_CHNL_VIEW","query",{"_ROWNUM_":1000,"_WHERE_":" AND PORTAL_DISPLAY = 1"},null,false,function(returnData){
		 	
		 /*处理链接信息迭代生成图片链接*/

		 ptManaglinkz.createLinksz(returnData);	    		      									
	     });  
	 },
	     /*生成链接方法*/
	    createLinksz:function(SYArray){
		var num = 0;
			jQuery.each(SYArray._DATA_,function(n,syslink) {
			num=n;
				/*目标链接*/
				var chnlId = syslink.CHNL_ID;
				var chnlCode = syslink.CHNL_CODE;
				var chnlName = syslink.CHNL_NAME;
			    var newdiv = "<div style='float:left;width:65px;height:20px;line-height: 25px;color:#D1D6D2;text-align:center;vertical-align:middle;display:table-cell;'></div>";	
                if ((n+1)%4>0){				
				newdiv = "<div style='float:left;width:10px;height:10px;line-height: 10px;color:#D1D6D2;text-align:center;vertical-align:middle;display:table-cell;'>丨</div>";
				}else if((n+1)%12==0){
				newdiv = "<div style='float:left;width:100%;'></div>";
				}else{
				}
				/*拼接链接文字<img src='BN_GSMH_banner.png'/>*/
				var aaa ="<div class='bannercss' title="+chnlName+" style='float:left;padding:0px 3px; font-size:12px;width:55px;height:12px;text-align:center;'><a id="+chnlId+" style='cursor:pointer;'>"+chnlName+"</a></div>";
				aaa+=newdiv;
				
			
				$("#forlikpicz").append(aaa);
				/*将链接文字绑定触发事件*/
				ptManaglinkz.createhrefz(chnlId,chnlCode,chnlName);
			});
			if(System.getVar("@ODEPT_CODE@")=="0001B210000000000BU3"){
				$("#forlikpicz").append("<div class='bannercss' title='部门门户' style='float:left;padding:0px 3px; font-size:12px;width:55px;height:12px;text-align:center;'><a id='bumenmenhu' style='cursor:pointer;'>部门门户</a></div>");
				ptManaglinkz.createhrefz('bumenmenhu','BUMENMENHU',"");
			}
			$("#forlikpicz").append("<div style='float:left;width:10px;height:10px;line-height: 10px;color:#D1D6D2;text-align:center;vertical-align:middle;display:table-cell;'>丨</div>");
			$("#forlikpicz").append("<div class='bannercss' title='知识库' style='float:left;padding:0px 3px; font-size:12px;width:55px;height:12px;text-align:center;'><a id='zhishiku' style='cursor:pointer;'>知识库</a></div>");
				ptManaglinkz.createhrefz('zhishiku','ZHISHIKU',"");
			/*FireFly.doAct("SY_COMM_LINK","getComLinks",{"_ROWNUM_":100,"LINK_TYPE":3,"_WHERE_":" AND 1=1"},null,false,function(returnData){
			var homeHref = "";
				jQuery.each(returnData._DATA_,function(n,link) {
					var links = link.LINK_ADDRESS;
					var linkName = link.LINK_NAME;
					var linkType = link.LINK_TYPE;
					
					var link= jQuery("<a style='cursor:pointer;text-decoration:none;font-family:微软雅黑 '>"+linkName+"</a>");
					link.click(function(){
						window.open(links);
					});
					if(linkType==3){
						if(n%2==1){
							homeHref = "<div class='bannercss myclicka"+n+"' title='"+linkName+"' style='float:left;padding:5px;font-size:12px;width:55px;height:10px;text-align:center;'></div>";
						}else{
							homeHref = "<div class='bannercss myclicka"+n+"' title='"+linkName+"' style='float:left;padding:5px;font-size:12px;width:55px;height:10px;text-align:center;'></div><div style='float:left;width:10px;height:10px;line-height: 10px;color:#D1D6D2;text-align:center;vertical-align:middle;display:table-cell;'>丨</div>";	
					}
					$("#forlikpicz").append(homeHref);
					$("#forlikpicz").find(".myclicka"+n).append(link);
					n+=1;
					}
					
				});
				$("#forlikpicz").append("<div class='bannercss myclicka"+n+"' title='部门门户' style='float:left;padding:5px;font-size:12px;width:55px;height:10px;text-align:center;'><a style='cursor:pointer;text-decoration:none;font-family:微软雅黑 '>部门门户</a>
				</div>");
			});*/
			
			
	    },
		createhrefz:function(chnlId,chnlCode,chnlName){
		
			var q = "#"+chnlId;
			$(q).on("click",function(){
				var params = {};
				params["flag"] = true;
				params["ZT"]="1";
				
				var where = " and CHNL_ID = '" +chnlId+"'";
				var options = {"url":"SY_COMM_INFOS_VIEW.list.do","tTitle":"信息浏览","params":params,"menuFlag":3};
				var tabOpt = {"url":"BN_CHNL_"+chnlCode.toUpperCase()+".list.do","params":params,"menuFlag":3,"tTitle":chnlName,'nohex':'true'};
				
				var tabP = jQuery.toJSON(tabOpt);
				tabP = tabP.replace(/\"/g,"'");
				if(chnlCode=="guanyubainian"){
					/*window.open("/cms/SY_COMM_INFOS/0E5qkgKZVdnUXWRoTP8pMK.html?CHNL_PID=20g4FFN75aoV3Eji90L4fj&CHNL_ID=0edvKdXaNdB8sHGbwTXllv");*/
					window.open("/SY_COMM_TEMPL.show.do?pkCode=2W6rkt96x4GqbRKnO9Zbds");
				}else if(chnlCode=="BUMENMENHU"){
					var odept="0001B210000000000BU3";
					window.open("http://"+window.location.host+"/sy/comm/page/portal.jsp?sysSub=ab&ODEPT_CODE="+odept+"&DEPT_CODE="+System.getVar("@TDEPT_CODE@")+"&ODEPT_LEVEL=2");
					
				}else if(chnlCode=="ZHISHIKU"){
					/*window.open("http://"+window.location.host+"/sy/base/view/stdListView.jsp?sId=BN_FILE_VIEW");*/
					var tabZ = jQuery.toJSON({"url":"BN_FILE_VIEW.list.do","tTitle":"文档查看","menuFlag":3});
					tabZ = tabZ.replace(/\"/g,"'");
					window.open("/sy/comm/page/page.jsp?openTab="+tabZ);
				}else if(chnlCode=="BAINIANJIAYUAN"){
				window.open("/SY_COMM_TEMPL.show.do?pkCode=EMPLOYEE_ACTIVITY2");
				}else if(chnlCode=="BAINIANJIANGTAN"){
					window.open("/bn/link/bnjt/bnjt.html","_blank");
				}else{
					window.open("/sy/comm/page/page.jsp?openTab="+(tabP)+"&where="+encodeURIComponent(where));
				}
				
			});
		}	
	 };
	
</script>