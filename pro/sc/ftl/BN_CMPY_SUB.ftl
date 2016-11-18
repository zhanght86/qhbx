

<style type="text/css">
.com_banner {width:100%;background-color:#64a6c9;float:left;}
.com_banner li {height:auto;line-height:auto;float:left;margin:0px 20px;font-weight:bold;}
.com_banner li a {text-decoration: none;color:white;}
.com_banner li a:hover {text-decoration: underline;} 
.bannercss {color:black}
.bannercss a:hover{color:#e7340c}
</style>



	
<table id = "CMPY_BANNER" border="0" cellSpacing="0" cellPadding="4" width="100%">
					
					<tbody>
					
						<tr>
							<td style="border-bottom-width: 0px;">
							<div id="forlikpic" ></div>
							</td>
						</tr>
			  		</tbody></table>
					
<script type="text/javascript">
/*初始化页面*/
jQuery(document).ready(function(){
		
       ptManaglink.initLink();		
			});
/*初始化方法*/
	 var ptManaglink = {
	 
	 /*初始化分公司*/
	 initLink:function(){
	 /*加载分公司信息*/
	 var odeptCode = System.getUser("ODEPT_CODE"); 

	 if(odeptCode == "0001B210000000000BU3"){
		$("#CMPY_BANNER").show();
	 }else{
		$("#CMPY_BANNER").hide();
	 }
	 var hrefStr = location.search;
		if(hrefStr.indexOf("ODEPT_CODE")>=0){
		odeptCode = GetRequest("ODEPT_CODE");
		}else{
		
		}
	 $(".bannercss a").css({color:"#000000"});
	 $("#"+odeptCode).css({color:"#E7340C"});
	 FireFly.doAct("SY_COMM_INFOS_CHNL_VIEW","getDeptSF",{"_ROWNUM_":1000,"ODEPT_CODE":odeptCode},null,false,function(returnData){
			 ptManaglink.createLinks(returnData);
		});
	 $(".bannercss a").css({color:"#000000"});
	 $("#"+odeptCode).css({color:"#E7340C"});
	 },
	     /*生成分公司方法*/
	    createLinks:function(SYArray){
			jQuery.each(SYArray._DATA_,function(n,syslink) {
				/*目标分公司*/
				var oDeptCode = syslink.ODEPT_CODE;
				var code = syslink.DEPT_CODE;
				var name = syslink.DEPT_NAME;
			
				if(code=='0001B210000000000ET6'){
				var newdiv = "<div style='float:left;width:10px;height:25px;line-height: 25px;color:#D1D6D2;text-align:center;vertical-align:middle;display:table-cell;'>丨</div>";	
                if (n%2==1){				
				
				}else {
				newdiv = "";
				}
				
				var aaa ="<div class='bannercss' title="+name+" style='float:left;padding:5px; font-size:12px;width:134px;height:25px;line-height:25px;text-align:center;'><a id="+code+" style='cursor:pointer;'>分公司门户 >"+name+" </a></div>";
				aaa+=newdiv;
				
			
				$("#forlikpic").append(aaa);
				
				/*将分公司文字绑定触发事件*/
				ptManaglink.createhref(code,oDeptCode);
				}
			    
			});

	    },
				createhref:function(code,oDeptCode){
				$("#"+code).unbind("click").bind("click",function(){
				$(".bannercss a").css({color:"#000000"});
				window.open("http://"+window.location.host+"/sy/comm/page/portal.jsp?sysSub=aa&ODEPT_CODE="+code);
				$("#"+code).css({color:"#E7340C"});
		});
				
				
				
		        }
		
	 };
/*获取url中的参数*/
function GetRequest(param) {
  var url = location.search; 
   if (url.indexOf("?") != -1) {
      var str = url.substr(1);
      strs = str.split("&");
      for(var i = 0; i < strs.length; i ++) {
		  if(param==strs[i].split("=")[1]){
			return strs[i].split("=")[1];
		  }
			 
      }
   }
   
}
</script>