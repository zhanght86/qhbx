<style>
	html { height:0px; background-color:white; }
	#week-action-button-bar {
		float:right;
		margin-top:-30px;
		margin-right:10px;
	}
	#LDR-LIST {
		float:left;
		width:15%;
		margin-top:0px;
		border:0px;
	}
	#note{ line-height:25px; }
	#fenye{ display:none; float:right; }
	.action-content {
		width:350px;
		height:50px;
		background:#f1f7fc;
		position:absolute;
		display:none;
		border:1px solid silver;
		height:auto;
		width:auto;
		padding:1px 3px 1px 3px;
		line-height:18px;
	}
	.bbit-tree-node .bbit-tree-node-over{background-color:#F7EBC0;}
	#fenye a {padding:4px 10px 4px 10px;display:inline-block;border:#8db5d7 1px solid;color:#000;margin:0 2px 0 2px;text-decoration:none;}
	.current {padding:4px 10px 4px 10px;border:#8db5d7 1px solid;color:white;margin:0 2px 0 2px;cursor:pointer;background-color:#f0f4fd}
	span {display:inline-block;}
	span.disabled {padding:4px 10px 4px 10px;border:#8db5d7 1px solid;color:white;margin:0 2px 0 2px;background-color:#0080FF;}
	span.current {color:white;padding:4px 10px 4px 10px;border:#ccc 1px solid;color:#ccc;margin:0 2px 0 2px;}
</style>
<#--引用外部工具函数-->
<script type="text/javascript" src='/sy/comm/ldr/js/calDay.js'></script>
<script type="text/javascript" src='/sy/base/frame/tools.js'></script>
<script type="text/javascript" src="/sy/base/frame/plugs/jquery-ui/a.jquery.ui.core.js"></script>
<script type="text/javascript" src='/bn/base/frame/engines/page.js'></script>
	<#--HTML-->
	<div >
		<!--左：角色列表-->
		<div id='LDR-LIST' style='height:617px;overflow:hidden;background:#FBFBFB'>
			<!--角色列表:头-->
			<div style='margin-top:10px;margin-left:8px;margin-bottom:5px'>
				<table id="LDR-LIST-tabHead">
					<thead>
						<tr>
							<td width=80% style='font-size:14px;font-weight:bold;'>可查看角色</td>
						</tr>
					</thead>
				</table>
			</div>
			<!--角色列表:体-->
			<div id='ROLE_TREE'></div>
		</div>
	

	<!--右：活动列表-->
		<div id='LDR-ACTION' style='float:right;width:85%;'>
			<div id='ACTION-TAB' >
				<!--活动列表：盒子-->
				<div id='WEEK-CON' class='portal-box' style="border-top:0px solid black;border:0px;">
					<!--标题-->
					<div id='week-con-title'>
						<ul style='text-align:center;'>
							<li style='font-size:30px;color:red;line-height:30px;margin-top:25px;'>
								<!--${orgVar.@ODEPT_NAME@}领导活动安排表-->
								领导活动安排表
							</li>
							<li  style="font-size:14px;font-weight:bold;margin-top:5px;margin-bottom:5px;">
								<span style='line-height:35px' id='b-time'></span>
								<span style='line-height:35px'>——</span>
								<span style='line-height:35px' id='e-time'></span>
							</li>
						</ul>
					</div>
					<!--按钮-->
					<div id="week-action-button-bar" style="margin-bottom:8px;margin-right:18px;">
						<button id='lastWeek'>上周</button>
						<button id='thisWeek'>本周</button>
						<button id='nextWeek'>下周</button>
						<button id='exp'>导出excel</button>
					</div>
					<!--表格-->
					<div id='week-action-con' style='height:auto;width:100%;OVERFLOW:auto;margin-left:0px;'>
						<table width="915px" border="1" style='border:1px #CCC solid;width:100%;'>
							<!--表格标题-->
							<thead style='line-height:35px;font-weight:bold;font-size:14px;background:#F7EBC0;color:#C4A950;'>
								<tr align='center'>
									<td width='100px'>领导</td>
									<td width='141px'>部门</td>
									<td >星期一</td>
									<td >星期二</td>
									<td >星期三</td>
									<td >星期四</td>
									<td >星期五</td>
									<td >星期六</td>
									<td >星期日</td>
								</tr>
							</thead>
							<!--表格内容-->
							<div id='actions'>
								<tbody id='actions-t' style='line-height:30px'>			
								</tbody>
							</div>
						</table>
					</div>
					<!--分页div start-->
					<div id='fenye' style='margin-top:10px;margin-right:15px;'></div>
					<input type='hidden' id='nowPage' value='1'/>
					<!--分页div end-->
					<!--下方注释 start-->
					<div>
						<table width="50%" border="0" id="note" style="margin-top:33px">
						  <tr>
						    <td width="20%" align="center"><font  style='font-weight:bold'>图例说明</font></td>
						    <td> </td>
						  </tr>
						  <tr>
						    <td bgcolor='#0000fe'></td>
						    <td> &nbsp;&nbsp;蓝色：表示为"会议"类型活动</td>
						  </tr>
						  <tr>
						    <td bgcolor='#ff9900'>&nbsp;</td>
						    <td> &nbsp;&nbsp;橙色：表示为"活动"类型活动</td>
						  </tr>
						  <tr>
						    <td bgcolor='#fcff00'>&nbsp;</td>
						    <td> &nbsp;&nbsp;黄色：表示为"出差"类型活动</td>
						  </tr>
						  <tr>
						    <td bgcolor='#fe0000'>&nbsp;</td>
						    <td> &nbsp;&nbsp;红色：表示为"休假"类型活动</td>
						  </tr>
						</table>
					</div>
					<!--下方注释 end-->
					<!--鼠标悬浮div start </div>  <div id="action-content2" class='action-content'></div> 
					<div id="action-content3" class='action-content'></div>  <div id="action-content4" class='action-content'></div>  
					<div id="action-content5" class='action-content'></div>  <div id="action-content6" class='action-content'></div> 
					<div id="action-content7" class='action-content'></div>  <div id="action-content8" class='action-content'></div> 
					<div id="action-content9" class='action-content'></div>  <div id="action-content10" class='action-content'></div>
				    <div id="action-content11" class='action-content'></div> <div id="action-content12" class='action-content'></div> 
					<div id="action-content13" class='action-content'></div> <div id="action-content14" class='action-content'></div> -->
					<div id="action-content"></div>
					<!--鼠标悬浮div end-->
				</div>
			</div>
		</div>
	</div>
<#--内部脚本-->
<script type='text/javascript'>
	var chooseRoleCode ="";
	function openLdrAction(element){
		var url = "OA_LDR_ACTION_NSH.card.do?pkCode="+$(element).attr("data-id")+"&readOnly=true";
		var opts = {	
				"url":url,
				"tTitle":"领导活动",
				"menuFlag":3
		};
		Tab.open(opts);
	}
	function replaceId (str) {
		return str.replace(/[^\w]/gi, "_");
	}
	<#-- 获取可查看角色列表  start-->
	function getRoleList(){
		var _self = this;
		var dictId = "BN_LDR_QUERY_RELAT";
		var userCode = System.getVar('@USER_CODE@');
		var exwhere = " and  QUERY_ID in (select QUERY_ID  from BN_LDR_QUERY_USER where ROLE_CODE in(select ROLE_CODE from SY_ORG_ROLE_USER where USER_CODE like '%"+userCode+"%'))";
		
		var tempData = FireFly.getDict(dictId,"",exwhere,"","","");
		var orgSetting = {
				showcheck: false,  
				url: "SY_COMM_INFO.dict.do", 
				theme: "bbit-tree-no-lines", 
				rhexpand: false,
			 	cascadecheck: true,
				checkParent: false,
				childOnly:false,
				onnodeclick: function(item,id){
					var nodeObj = jQuery("#" + id + "_" + replaceId(item.ID),_self.navTree);
					chooseRoleCode = item.ID ;
					getLeaderList(item.ID);
					if (nodeObj.hasClass("rh-bbit-tree-selected")) {
						nodeObj.removeClass("bbit-tree-selected");
						nodeObj.removeClass("rh-bbit-tree-selected");
					}
				}
		};
		if(tempData[0]['CHILD']!=undefined){
			orgSetting.data = tempData[0]['CHILD']; 
			var child = orgSetting.data[0];
			if (child.length == 1) {
				orgSetting.data = child;
			}
			orgSetting.dictId = dictId;
			var orgTree = new rh.ui.Tree(orgSetting);
			jQuery("#ROLE_TREE").append(orgTree.obj);
		}	
	}
	getRoleList();
	<#-- 获取可查看角色列表  end-->	
	<#-- 设置角色列表字体  start-->
	
	<#-- 设置角色列表字体  end-->	
		
	<#-- 为上周 本周 下周 添加事件  start-->	
	jQuery("#b-time").text(showWeekFirstDay());
	jQuery("#e-time").text(showWeekLastDay());
	jQuery("#lastWeek").unbind("click").bind("click",function(){
		jQuery("#b-time").text(getNextDate(jQuery("#b-time").text(),-7));
		jQuery("#e-time").text(getNextDate(jQuery("#e-time").text(),-7));
		jQuery("#ROLE_TREE").empty();
		jQuery("#actions-t").empty();
		getRoleList();
		getLeaderList(chooseRoleCode);
	});
	jQuery("#thisWeek").unbind("click").bind("click",function(){
		jQuery("#b-time").text(showWeekFirstDay());
		jQuery("#e-time").text(showWeekLastDay());
		jQuery("#ROLE_TREE").empty();
		jQuery("#actions-t").empty();
		getRoleList();
		getLeaderList(chooseRoleCode);
	});	
	jQuery("#nextWeek").unbind("click").bind("click",function(){
		jQuery("#b-time").text(getNextDate(jQuery("#b-time").text(),7));
		jQuery("#e-time").text(getNextDate(jQuery("#e-time").text(),7));
		jQuery("#ROLE_TREE").empty();
		getRoleList();
		getLeaderList(chooseRoleCode);
	});
	<#-- 为上周 本周 下周 添加事件  end-->	
	
	<#-- 选择角色从后台获得所在机构领导数据 start-->
	function getLeaderList(roleCode){
    	 FireFly.doAct("BN_LDR_QUERY_USER", "getLeaderList", {"ROLE_CODE":roleCode,"SHOWNUM":"10","NOWPAGE":"1"},false,false,function(result){
   			if(result._DATA_.length==0){
   				jQuery("#actions").empty();
   				jQuery("#actions-t").empty();
   				jQuery("#actions").append("<tr><td align='center' style='width:76%;position: relative;'>该角色下无人员可查看！</td></tr>");
   			}else{
   				jQuery("#actions").empty();
				jQuery("#actions-t").empty();
				var beginDate = jQuery("#b-time").text();
				var endDate   = jQuery("#e-time").text();
				
				<#-- 点击角色时 nowPage div 初始化值为1 -->
				jQuery("#nowPage").val(1);
   				<#-- 执行渲染action的方法  -->
   				actions(result,beginDate,endDate);
   				
   				<#-- 需要翻页div -->
				if(Number(result._PAGE_.ALLNUM)>Number(result._PAGE_.SHOWNUM)){
					jQuery("#fenye").css("display","inline-block");
					jQuery("#fenye").empty();
					
					<#--如果当前显示页为首页 -->
					jQuery("<span id='up' class='current'>上一页</span>").appendTo(jQuery("#fenye")).click(function(){
						var nowPage = parseInt(jQuery("#nowPage").val());
						FireFly.doAct("BN_LDR_QUERY_USER", "getLeaderList", {"ROLE_CODE":roleCode,"SHOWNUM":"10","NOWPAGE":nowPage-1},false,false,function(result){
   							<#-- 执行渲染action的方法 -->
   							jQuery("#nowPage").val(result._PAGE_.NOWPAGE);
   							actions(result,beginDate,endDate);
						});
					});
					
					if(Number(result._PAGE_.NOWPAGE==1)){
						jQuery("#up").removeClass("current");
						jQuery("#up").addClass("disabled");
						jQuery("#up").hide();
					}
					<#--循环显示全部页码数-->
					for(var n=1;n<=Number(result._PAGE_.PAGES);n++){
						<#--如果当前显示页为第n页-->
			   			jQuery("<span id='num"+n+"' class='current'>"+n+"</span>").attr("data",n).appendTo(jQuery("#fenye")).click(function(){
							var page = parseInt(jQuery(this).attr("data"));
							FireFly.doAct("BN_LDR_QUERY_USER", "getLeaderList", {"ROLE_CODE":roleCode,"SHOWNUM":"10","NOWPAGE":page},false,false,function(result){
   								<#-- 执行渲染action的方法 -->
   								jQuery("#nowPage").val(page);
   								actions(result,beginDate,endDate);
							});
						});
						<#--修改第一页页码div -->
						jQuery("#num1").removeClass("current");
						jQuery("#num1").addClass("disabled");
					}
					
				   	<#--如果当前显示页为最后一页 -->
					jQuery("<span id='down' class='current'>下一页</span>").appendTo(jQuery("#fenye")).click(function(){
						var nowPage = parseInt(jQuery("#nowPage").val());
						FireFly.doAct("BN_LDR_QUERY_USER", "getLeaderList", {"ROLE_CODE":roleCode,"SHOWNUM":"10","NOWPAGE":nowPage+1},false,false,function(result){
   							<#-- 执行渲染action的方法 -->
   							jQuery("#nowPage").val(result._PAGE_.NOWPAGE);
   							actions(result,beginDate,endDate);
						});
					});
				}else{<#-- 数据量少不需要设置翻页 -->
					jQuery("#fenye").css("display","none");
				}
	   			
   			}
   		});
	}
	<#-- 选择角色从后台获得所在机构领导数据 end-->
	<#-- 鼠标滑过悬浮事件 start-->
	function show(obj,id) { 
		var objDiv = jQuery("#"+id+""); 
		jQuery(objDiv).css({"display":"block","z-index":"20"}); 
		jQuery(objDiv).css("left", event.clientX); 
		jQuery(objDiv).css("top", event.clientY-20); 
	}
	function hide(obj,id) { 
		var objDiv = jQuery("#"+id+""); 
		jQuery(objDiv).css("display","none"); 
	} 
	<#-- 鼠标滑过悬浮事件 end--> 
	
	<#-- 渲染action start--> 
	function actions(result,beginDate,endDate){
		jQuery("#actions-t").empty();
		<#-- 控制页码 start -->
		var nowPage = jQuery("#nowPage").val();
		<#-- 当前页码是首页-->
		if(nowPage==1){
			jQuery("#up").removeClass("current");
			jQuery("#up").addClass("disabled");
			jQuery("#up").hide();
		}else{
			jQuery("#up").show();
			jQuery("#up").addClass("current");
			jQuery("#up").removeClass("disabled");
			jQuery("#num1").removeClass("disabled");
			jQuery("#num1").addClass("current");
		}
		
		<#-- 当前页码是选中页 -->
		for(var n=1;n<=Number(result._PAGE_.PAGES);n++){
			var num = "num"+n;
			if(n!=nowPage){
				jQuery("#"+num+"").removeClass("disabled");
				jQuery("#"+num+"").addClass("current");
			}
		}	
		jQuery("#num"+nowPage+"").removeClass("current");
		jQuery("#num"+nowPage+"").addClass("disabled");
		
		<#-- 当前页码是最后一页 -->
		if(nowPage == result._PAGE_.PAGES){
			jQuery("#down").hide();
			jQuery("#down").removeClass("current");
			jQuery("#down").addClass("disabled");
		}else{
			jQuery("#down").show();
			jQuery("#down").addClass("current");
			jQuery("#down").removeClass("disabled");
		}
		<#-- 控制页码 end-->
		
		for(var i=0;i<result._DATA_.length;i++){
			var leaderList = result._DATA_[i];
			<#-- 获取当前领导的全部日程 -->
			param = {
				"LEADER_CODE":leaderList.USER_CODE,
				"BEGIN_DATE":beginDate,
				"END_DATE":endDate,
				"_PAGE_":{"SHOWNUM":"1","NOWPAGE":"1"}
			};
			var actionLists = FireFly.doAct("BN_LDR_QUERY_USER","getLeaderActons",param);
			<#-- 渲染领导人日程tr -->
			if(leaderList.ODEPT_CODE==System.getVar("@C_BN_ORG_ZODEPT_CODE@")){
				jQuery("#actions-t").append("<tr  id='tr"+leaderList.USER_CODE+"'><td  id='leaderName' align='center'><font  style='font-weight:bold'>"+leaderList.USER_NAME+"</font></td>
					<td align='center'>"+leaderList.DEPT_NAME+"</td><td width='672px' colspan='7'><div id='actionTd'  style='position:relative;width:672px;height:30px'></div></td></tr>");
			}else{
				jQuery("#actions-t").append("<tr  id='tr"+leaderList.USER_CODE+"'><td  id='leaderName' align='center'><font  style='font-weight:bold'>"+leaderList.USER_NAME+"</font></td>
					<td align='center'>"+leaderList.ODEPT_NAME+"</td><td width='672px' colspan='7'><div id='actionTd'  style='position:relative;width:672px;height:30px'></div></td></tr>");
			}
			
					
					
					
					
					
				
			if(actionLists._DATA_.length>0){
				for(var j=0;j<actionLists._DATA_.length;j++){
					var actionData = actionLists._DATA_[j];
					<#-- 设定填充颜色  -->
					var tdcolor = "";
					if(actionData.ACT_TYPE=="会议"){
						tdcolor = "#0000fe";
					}else if(actionData.ACT_TYPE=="活动"){
						tdcolor = "#ff9900";
					}else if(actionData.ACT_TYPE=="出差"){
						tdcolor = "#fcff00";
					}else if(actionData.ACT_TYPE=="休假"){
						tdcolor = "#fe0000";
					}else{
						tdcolor = "#ffffff";
					}
					
					var divW = 2*(parseInt(actionData.END_BLOCK)-parseInt(actionData.BEGIN_BLOCK)+1);
					
				
					
					var floatLeft = 2*(parseInt(actionData.BEGIN_BLOCK));
					
					
					
					
					var dataTd = jQuery("#tr"+leaderList.USER_CODE+" #actionTd");
					
					jQuery(dataTd).append("<div  id='div"+j+"' onclick='openLdrAction(this);' data-id='"+actionData.ACT_ID+"' style='background-color:"+tdcolor+";width:"+divW+"px;height:30px;overflow:hidden;left:"+floatLeft+"px;z-index:15;position:absolute;float:left'></div>");
					<#-- 渲染鼠标悬浮div  + "开始格子"+actionData.BEGIN_BLOCK+"结束格子"+actionData.END_BLOCK
						+"宽度"+divW+"像素，左飘"+floatLeft-->
					var content = "时间：" + actionData.BEGIN_TIME + " —— " + actionData.END_TIME;
					content += "</br>标题：" + actionData.TITLE;
					jQuery("#action-content").append("<div id='action-"+leaderList.USER_CODE+"-"+j+"' class='action-content'></div>");
					var contentId = "action-"+leaderList.USER_CODE+"-"+j;
					var actionDiv= jQuery("#tr"+leaderList.USER_CODE+" #actionTd #div"+j+"");
					jQuery("#action-"+leaderList.USER_CODE+"-"+j+"").html(content);
					jQuery(actionDiv).attr({"onMouseOver":"javascript:show(this,'"+contentId+"')","onMouseOut":"hide(this,'"+contentId+"')"});
				}
			}
   		}    
   	}
	<#-- 渲染action end--> 
	
	<#-- 导出excel start-->
	jQuery("#exp").unbind("click").bind("click",function(){
		var begin = jQuery("#b-time").text();
		var end   = jQuery("#e-time").text();
		if(chooseRoleCode){
		    var data = {"_PK_":""};
		    serchWhere = {
		    	"WEEK_BEGIN":begin,
		    	"WEEK_END":end,
		    	"LEADER_ROLE":chooseRoleCode
		    };
		    data = jQuery.extend(data,serchWhere);
			window.open(FireFly.getContextPath() + '/OA_LDR_ACTION_NSH_COPY.exp.do?data=' + 
		    		encodeURIComponent(jQuery.toJSON(data)));
			<#--
			expParam = {
				"LEADER_ROLE_CODE":chooseRoleCode,
				"BEGIN_DATE":begin,
				"END_DATE":end
			};var aa=FireFly.doAct("OA_LDR_ACTION_NSH_COPY","exp",expParam);-->
		}else{
			alert("请先选择领导角色！");
		}
	});
	<#-- 导出excel end-->
</script>
</html>	