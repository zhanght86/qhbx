<#--样式-->
<style>
	html {
		height:0px;
		background-color:white;
	}
	.first-td{
		text-align:center;
	}
	.ldr-tr{height:68px;width:100%;border-bottom:1px #cccccc solid;margin-top:2px}
	.ldr-tr:hover{
		background:#fadffa;
	}
	.ldr-td-checkbox-border{
		float:right;height:70px;margin-top:-2px;
	}
	.ldr-td-uname{font-size:15px;font-weight:bold;line-height:25px;}
	.ldr-td-upost{font-size:13px;font-weight:bold;color:#999999}
	.button-left-icon{
		background:url(/sy/theme/default/images/icons/photo-bk.png) no-repeat scroll -8px -114px transparent;
		padding-right: 15px;
		padding-top: 11px;
	}
	.ldr-td-img{width:50px;height:60px;margin-top:2px}
	.button-right-icon{
		background:url(/sy/theme/default/images/icons/photo-bk.png) no-repeat scroll -11px -64px transparent;
		padding-left: 15px;
		padding-top: 22px;
	}
	.week-end{background-color:#f1f7fc}
	td{border:1px #CCC solid;}
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
	#LDR-LIST-tabHead {
		height:27px;
		line-height:30px;
		width:100%;
		background-color:#e2e2e2;
	}
	#LDR-LIST-tabBody {
		border:1px solid #cccccc;
		width:100%;
		table-layout:fixed;
		word-break:break-all;
		word-wrap:break-word;
	}
</style>

<#--引用外部工具函数-->
<script type="text/javascript" src='/sy/comm/ldr/js/calDay.js'></script>

<#--HTML-->
<div>
	<!--左：领导列表-->
	<div id='LDR-LIST'>
		<!--领导列表:头-->
		<div>
			<table id="LDR-LIST-tabHead">
				<thead>
					<tr>
						<td align='center' valign='middle' width=20%>
							<div style='float:right;height:27px;'></div>
							<input type='checkbox' id='all' style='margin-top:5px;'/>
						</td>
						<td width=80% style='font-size:16px;'>&nbsp;&nbsp;领导列表</td>
					</tr>
				</thead>
			</table>
		</div>
		<!--领导列表:体-->
		<div id='leader-contener' style='height:auto;width:100%;OVERFLOW-y:auto;'>
			<div id='leader-info' style='width:100%'>
			</div>
		</div>
	</div>
	<!--右：活动列表-->
	<div id='LDR-ACTION' style='float:left;width:85%'>
		<div id='ACTION-TAB'>
			<!--活动列表：标签bar-->
			<div>
				<ul class='portal-box-title'>
					<li>
						<a href="#WEEK-CON">周安排 </a>
					</li>
				</ul>
			</div>
			<!--活动列表：盒子-->
			<div id='WEEK-CON' class='portal-box' style="border-top:0px solid black;border:0px;">
				<!--标题-->
				<div id='week-con-title' style=''>
					<ul style='text-align:center;'>
						<li style='font-size:16px;color:red;line-height:25px'>
							${orgVar.@ODEPT_NAME@}领导活动安排表
						</li>
						<li>
							<span style='line-height:25px' id='b-time'></span>
							<span style='line-height:25px'>——</span>
							<span style='line-height:25px' id='e-time'></span>
						</li>
					</ul>
				</div>
				<!--按钮-->
				<div id="week-action-button-bar">
					<button id='up'>上周</button>
					<button id='thisweek'>本周</button>
					<button id='next'>下周</button>
				</div>
				<!--表格-->
				<div id='week-action-con' style='height:auto;width:100%;OVERFLOW:auto;'>
					<table width="100%" border="1" style='border:1px #CCC solid;'>
						<!--表格标题-->
						<thead style='line-height:30px;font-weight:bold;background:#f1f7fc'>
							<tr align='center'>
								<td width=15%>日期</td>
								<td width=15%>时间</td>
								<td width=15%>出席领导</td>
								<td width=20%>地点</td>
								<td width=20%>事项</td>
								<td width=15%>参加人员</td>
							</tr>
						</thead>
						<!--表格内容-->
						<div id='actions'>
							<tbody id='actions-t' style='line-height:30px'>			
							</tbody>
						</div>
					</table>
				</div>
			</div>
		</div>
	</div>
</div>

<#--内部脚本-->
<script type='text/javascript'>
	<#--文档加载完执行-->
	$(document).ready(function(){
		setTimeout(function(){
			<#--初始化活动标签-->
            $("#ACTION-TAB").tabs({});				
        
		$("#b-time").text(showWeekFirstDay());
		$("#e-time").text(showWeekLastDay());
		<#--渲染-->
		getOdeptLeader();
		createTabCell(showWeekFirstDay(),"",true);
		<#--绑定事件-->
		$("#up").unbind("click").bind("click",function(){
			createTabCell(getNextDate($("#b-time").text(),-7),getLdrCondition());
			$("#b-time").text(getNextDate($("#b-time").text(),-7));
			$("#e-time").text(getNextDate($("#e-time").text(),-7));
		});
		$("#next").unbind("click").bind("click",function(){
			createTabCell(getNextDate($("#b-time").text(),7),getLdrCondition());
			$("#b-time").text(getNextDate($("#b-time").text(),7));
			$("#e-time").text(getNextDate($("#e-time").text(),7));
		});
		$("#thisweek").unbind("click").bind("click",function(){
			createTabCell(showWeekFirstDay(),getLdrCondition());
			$("#b-time").text(showWeekFirstDay());
			$("#e-time").text(showWeekLastDay());
		});	
		$(".ldr-tr").unbind("click").bind("click",function(event){
			var uid = (this.id).substr(3);
			var trflag = $("#"+uid).attr("checked");
			if(event.target.nodeName == "INPUT"){
				trflag = !trflag;
			}
			if(trflag){
				$("#"+uid).attr("checked",false);
			}else{
				$("#"+uid).attr("checked",true);
			}
			createTabCell(getNextDate($("#b-time").text(),0),getLdrCondition());	
		});
		var flag = $("#all").attr("checked");
		$("#all").unbind("click").bind("click",function(){
			if(!flag){
				$("td input[type='checkbox']").attr("checked", true);
				createTabCell(getNextDate($("#b-time").text(),0),"");
				flag=true;
			}else{
				$("td input[type='checkbox']").attr("checked", false);
				createTabCell(getNextDate($("#b-time").text(),0),"false");
				flag=false;
			}
		})
		}, 0);
	});
	
	<#--构造领导SQL条件-->
	function getLdrCondition(){
		var sqlCondition=" and (";
		$(":checkbox[checked]").each(function(){
			if("all"!=this.id){
				sqlCondition +=" LDR_ID like '%"+jQuery(this).attr('uCode')+"%' or";
			}
		});
		if(" and ("==sqlCondition){
			return "false";
		}
		return sqlCondition.substr(0,sqlCondition.length-3)+")";
	}
	
	<#--函数定义-->		
	<#--从后台获得所在机构领导数据-->
	function getOdeptLeader(){
        var result = FireFly.doAct("OA_LDR_ACTION", "getLeaderList", {}, true, false);
        if (result[UIConst.RTN_MSG].indexOf(UIConst.RTN_OK) >= 0) {
        	if(result._DATA_.length > 0){
        		createLeaderList(result);
        	} else {
        		alert("领导数量为空,请联系管理员检查公司领导角色");
        	}		
		} else {
			alert("领导列表加载失败 " + result[UIConst.RTN_MSG]);
		}		
	};
	<#--渲染领导列表-->
	function createLeaderList(users){
		var leaderLable = $("#leader-info");
		leaderLable.empty();
		var table = $("<table id='LDR-LIST-tabBody'></table>");
		var i = 0;
		$(users._DATA_).each(function(){
			i=i+1;
			var userName = this.USER_NAME;
			var userCode = this.USER_CODE;
			var userLoginName = this.USER_LOGIN_NAME;
			var imgSub = this.USER_IMG_SRC;
			var userPost = this.USER_POST;
			var imgSrc = "";
			if (imgSub.length <= 0) {
	            if (this.USER_SEX == "1") {
	                imgSrc = FireFly.getContextPath() + "/sy/theme/default/images/common/rh-lady-icon.png";
	            } else {
	                imgSrc = FireFly.getContextPath() + "/sy/theme/default/images/common/rh-male-icon.png";
	            }
	        }else {
	            imgSrc = FireFly.getContextPath() + "/file/" + imgSub.substring(0, imgSub.indexOf(","));
	        }
			var tr = $("<tr width='100%' height='64px' id='tr_"+userLoginName+"'class='ldr-tr'></tr>");

			var img = $("<div style='height:62px;width:52px;border:1px #ccc solid;'><img class='ldr-td-img' src="+imgSrc+"></img></div>");
			$("<td align='center' valign='middle' width='20%'><div class='ldr-td-checkbox-border'></div><input style='margin-top:25px;'type='checkbox' id='"+userLoginName+"' uCode='"+userCode+"'/></div></td>").appendTo(tr);
			var imgTd=$("<td width='40%' align='center' valign='middle' ></td>");
			img.appendTo(imgTd);
			imgTd.appendTo(tr);
			$("<td width=40% align='center' valign='middle'><span class='ldr-td-uname'>"+userName+"</span><br><span class='ldr-td-upost'>"+userPost+"</span></td>").appendTo(tr);
			var trDiv = $("<div id='tr_"+userLoginName+"'class='ldr-tr'></div>");
			tr.appendTo(table);
		});
		table.appendTo(leaderLable)
	}
	
	<#--获得领导活动记录后装配list-->
	function getLdrActionList(list,dayArr){
		var arrAct = new Array();		
		for(var i = 0 ; i<list.length;i++){		
			var b_time = getMyDate(list[i].BEGIN_TIME);
			var e_time = getMyDate(list[i].END_TIME);
			if(b_time==e_time){
				var actObj = {};
				actObj["ACT_DAY"]=b_time;
				actObj["LDR_ID__NAME"]=list[i].LDR_ID__NAME;
				actObj["ADDRESS"]=list[i].ADDRESS;
				actObj["TITLE"]=list[i].TITLE;
				actObj["OTHER_USER__NAME"]=list[i].OTHER_USER__NAME;
				actObj["BEGIN_TIME"]=(list[i].BEGIN_TIME).substr(11,5);
				actObj["END_TIME"]=(list[i].END_TIME).substr(11,5);
				arrAct.push(actObj);
			}else{
				for(var b = b_time;b<=e_time;b=getNextDate(b,1)){
					if(b>dayArr[6]) break;
					if(b<dayArr[0]) continue;
					for(var j = 0 ;j<dayArr.length;j++){	
						if(b==dayArr[j]){
							var actObj = {};
							actObj["ACT_DAY"]=b;
							actObj["LDR_ID__NAME"]=list[i].LDR_ID__NAME;
							actObj["ADDRESS"]=list[i].ADDRESS;
							actObj["TITLE"]=list[i].TITLE;
							actObj["OTHER_USER__NAME"]=list[i].OTHER_USER__NAME;
							if(b==b_time){
								actObj["BEGIN_TIME"]=(list[i].BEGIN_TIME).substr(11,5);
								actObj["END_TIME"]="18:00";
							}else if(b==e_time){
								actObj["BEGIN_TIME"]="09:00";
								actObj["END_TIME"]=(list[i].END_TIME).substr(11,5);
							}else{
								actObj["BEGIN_TIME"]="09:00";
								actObj["END_TIME"]="18:00";
							}
							arrAct.push(actObj);
						}
					}
				}
			}
		}
		return arrAct;
	}
	<#--从后台获取数据并渲染活动表格-->
	function createTabCell(firstDay,ldr,firstFlag){
	
		<#--如果需要初始化时数据也为空再取消此赋值即可-->
		firstFlag = false;
	
		var actions ={};
		<#--获得领导活动记录-->
		var tb = $("#actions-t");
		tb.empty();
		var dayArr = [firstDay,getNextDate(firstDay,1),getNextDate(firstDay,2),getNextDate(firstDay,3),getNextDate(firstDay,4),getNextDate(firstDay,5),getNextDate(firstDay,6)];
		var data={};
		data["_NOPAGE_"] = true;
		data["_SELECT_"]="*";
	    var strWhere = " and (substr(BEGIN_TIME,1,10) between '" 
			+ firstDay + "' and '" + getNextDate(firstDay, 6) + "' or '" 
			+ firstDay + "' between substr(BEGIN_TIME, 1, 10) and substr(END_TIME, 1, 10) or '" 
			+ getNextDate(firstDay, 6) + "' between substr(BEGIN_TIME, 1, 10) and substr(END_TIME, 1, 10))";
		if (ldr == "") {
			if(firstFlag){
				actions._DATA_ = [];
			}else{
				data["_PAGE_"] = {"NOWPAGE":1,"SHOWNUM":0};
				data["_searchWhere"] = " and S_ODEPT = '" + System.getVar('@ODEPT_CODE@') + "'"+strWhere+" and CHK_STATE in(10,15,20)";
				data["_ORDER_"]="BEGIN_TIME asc";
				actions = FireFly.getListData("OA_LDR_ACTION_QUERY", data);
			}
		}else if (ldr == 'false' || !ldr) {
			actions._DATA_ = [];
		}else{
			if(firstFlag){
				actions._DATA_ = [];
			}else{
				data["_PAGE_"] = {"NOWPAGE":1,"SHOWNUM":0};
				data["_searchWhere"] = " and S_ODEPT = '"+System.getVar('@ODEPT_CODE@')+"'"+strWhere+ldr+" and CHK_STATE in(10,15,20)";
				data["_ORDER_"]="BEGIN_TIME asc";
				actions = FireFly.getListData("OA_LDR_ACTION_QUERY", data);
			}
		}
	
		<#--渲染-->
		var list=[];
		if(actions){
			list = getLdrActionList(actions._DATA_,dayArr);	
		}
		var star = 0;
		for(var a =0;a<dayArr.length;a++){
			var count = 0;
			var td = $("<td class='first-td'>"+getWeekCN(a)+"<br>"+dayArr[a]+"</td>").attr("rowspan",1);
			for(var i = star ;i<list.length;i++){
				if(list[i].ACT_DAY==dayArr[a]){
					if(count!=0){
						var tr = $("<tr></tr>");
						if(a>4){
							tr.addClass("week-end");
						}
						count=count+1;
						$("<td align='center'></td>").text(list[i].BEGIN_TIME+"至"+list[i].END_TIME).appendTo(tr);
						$("<td align='center'></td>").text(list[i].LDR_ID__NAME).appendTo(tr);
						$("<td align='center'></tb>").text(list[i].ADDRESS).appendTo(tr);
						$("<td></td>").text(list[i].TITLE).appendTo(tr);
						$("<td align='center'></td>").text(list[i].OTHER_USER__NAME).appendTo(tr);
						tr.appendTo(tb);

					}else{
						var tr = $("<tr></tr>");
						if(a>4){
							tr.addClass("week-end");
						}
						count =count+1;
						td.appendTo(tr);
						$("<td align='center'></td>").text(list[i].BEGIN_TIME+"至"+list[i].END_TIME).appendTo(tr);
						$("<td align='center'></td>").text(list[i].LDR_ID__NAME).appendTo(tr);
						$("<td align='center'></tb>").text(list[i].ADDRESS).appendTo(tr);
						$("<td></td>").text(list[i].TITLE).appendTo(tr);
						$("<td align='center'></td>").text(list[i].OTHER_USER__NAME).appendTo(tr);
						tr.appendTo(tb);
					}
				}
			}
			if(count==0){
				var tr = $("<tr></tr>");
				if(a>4){
					tr.addClass("week-end");
				}
				td.appendTo(tr);
				$("<td align='center'></td>").appendTo(tr);
				$("<td align='center'></td>").appendTo(tr);
				$("<td align='center'></td>").appendTo(tr);
				$("<td></td>").appendTo(tr);
				$("<td align='center'></td>").appendTo(tr);
				tr.appendTo(tb);
			}else{
				td.attr("rowspan",count);
				star = star+count-1;
			}

		}
		portalView._setPortalHei(jQuery(".portal-target").height());
	};
</script>