<link rel="stylesheet" type="text/css" href="/sc/comm/todo/css/todo.css"/>
<div class="portal-box pt-todo-wrapper clearfix">
  <div class="pt-todo-container">
      <div class="pt-todo-header">
        <div class="pt-todo-tab"> 
          <ul class="portal-box-title tabControl clearfix" id="tabControl">
                <li class="tab-item current">
                    <a href="#js-todo" title="待办"> 待办(<span id ='todos'>0</span>)</a>
                </li>
                <li class="line">
                    |
                </li>
                <li class="tab-item">
                    <a href="#js-toRead" title="待阅">待阅(<span id ='reads'>0</span>)</a>
                </li>
                 <li class="line">
                    |
                </li>
                <li class="tab-item">
                    <a href="#js-delegate" title="委托"> 委托(<span id ='agencys'>0</span>)</a>
                </li>
                <li class="slide">
                    <div class="icon">
                    </div>
                </li>
                <li class="hover" id="hover">
                </li>
            </ul>
        </div>
      </div>
      <!------------------end of pt-todo-content-header----------------------------->
       <div id="js-todo" class="pt-todo-grid pt-beautify" style="height:${height};max-height:${height};">
       	<#if _DATA_0._DATA_?size != 0>
			<script>
				$("#todos").text(${_DATA_0._DATA_?size});
			</script>
			<#list _DATA_0._DATA_ as todo>
       		<div class="pt-todo-item clearfix">
       			<div class="fl w60">
       				<div class="pt-todo-icon fl icon-col"></div>
	       			<div class="fl sys-col">[<span class="pt-todo-system">${todo.SYS_NAME}</span>]</div>
	       			<div class="fl title-col">
	       				<a class="elipd" href='javascript:void(0);' title=${todo.TODO_TITLE} style='text-decoration:none' onclick='openUrlPage("${todo.TODO_URL}","${todo.SYS_CODE}","${todo.TODO_CODE_NAME}","${todo.TODO_ID}","${todo.SERV_ID}","${todo.TODO_OBJECT_ID1}","${todo.TODO_CATALOG}","${todo.OWNER_CODE}","${todo.TODO_CONTENT}")'>${todo.SHORT_TITLE}</a>
	       			</div>
       			</div>
       			<div class="fr w40">
	       			<div class="fl tl">发送人：${todo.SEND_USER_NAME}</div>
	       			<div class="fr">
	       				<#if (todo.TODO_SEND_TIME?length >10)>
							${todo.TODO_SEND_TIME?substring(0,10)}
						<#else>
							${todo.TODO_SEND_TIME}
						</#if>
	       			</div>
       			</div>
       		</div>
			</#list>
			<#else>
			<div class="pt-todo-item">
       			<div class="pt-todo-blank">没有待办需要处理！</div>
       		</div>
		</#if>
        </div>
      <!---------------end of js-latest-qa----------------------------> 
       	<div id="js-toRead" class="pt-todo-grid pt-beautify" style="display:none;height:${height};max-height:${height};">
			<#if _DATA_1._DATA_?size != 0>
			<script>
				$("#reads").text(${_DATA_1._DATA_?size});
			</script>
				<#list _DATA_1._DATA_ as read>
				<div class="pt-todo-item clearfix">
	       			<div class="fl w60">
	       				<div class="pt-todo-icon fl icon-col"></div>
		       			<div class="fl sys-col">[<span class="pt-todo-system">${read.SYS_NAME}</span>]</div>
		       			<div class="fl title-col">
		       				<a class="elipd" href='#' title=${read.TODO_TITLE} style='text-decoration:none' onclick='openUrlPage("${read.TODO_URL}","${read.SYS_CODE}","${read.TODO_CODE_NAME}","${read.TODO_ID}","${read.SERV_ID}","${read.TODO_OBJECT_ID1}","${read.TODO_CATALOG}","${read.OWNER_CODE}","${read.TODO_CONTENT}")'>${read.SHORT_TITLE}</a>
		       			</div>
	       			</div>
	       			<div class="fr w40">
		       			<div class="fl tl">发送人：${read.SEND_USER_NAME}</div>
		       			<div class="fr">
		       				<#if (read.TODO_SEND_TIME?length >10)>
								${read.TODO_SEND_TIME?substring(0,10)}
							<#else>
								${read.TODO_SEND_TIME}
							</#if>
		       			</div>
	       			</div>
	       		</div>
				</#list>
			<#else>
				<div class="pt-todo-item">
       				<div class="pt-todo-blank">没有待阅需要处理！</div>
       			</div>
			</#if>
        </div>
        
      <!---------------end of js-leader-qa----------------------------> 
       	<div id="js-delegate" class="pt-todo-grid pt-beautify" style="display:none;height:${height};max-height:${height};">
			<#if _DATA_2._DATA_?size != 0>
			<script>
				$("#agencys").text(${_DATA_2._DATA_?size});
			</script>
				<#list _DATA_2._DATA_ as agency>
				<div class="pt-todo-item clearfix">
	       			<div class="fl w60">
	       				<div class="pt-todo-icon fl"></div>
		       			<div class="fl">[<span class="pt-todo-system">${agency.SYS_NAME}</span>]</div>
		       			<div class="fl">
		       				<a href='#' title=${agency.TODO_TITLE} style='text-decoration:none' onclick='openUrlPage("${agency.TODO_URL}","${agency.SYS_CODE}","${agency.TODO_CODE_NAME}","${agency.TODO_ID}","${agency.SERV_ID}","${agency.TODO_OBJECT_ID1}","${agency.TODO_CATALOG}")'>${agency.SHORT_TITLE}</a>
		       			</div>
	       			</div>
	       			<div class="fr w40">
		       			<div class="fl tl">发送人：${agency.SEND_USER_NAME}</div>
		       			<div class="fr">
		       				<#if (agency.TODO_SEND_TIME?length >10)>
								${agency.TODO_SEND_TIME?substring(0,10)}
							<#else>
								${agency.TODO_SEND_TIME}
							</#if>
		       			</div>
	       			</div>
	       		</div>
				</#list>
			<#else>
				<div class="pt-todo-item">
       				<div class="pt-todo-blank">没有需要处理的委托！</div>
       			</div>
			</#if>
        </div>
      <!---------------end of js-dept-qa----------------------------> 
    </div>
  <!---------------end of content---------------------------->
</div>
<script type="text/javascript">
jQuery(document).ready(function(e) {
	$(".pt-beautify").mCustomScrollbar({
    	  theme:"dark-thin",
    	  scrollInertia:200,
    	  advanced:{
				updateOnContentResize:true
		  }
      });
	var offleft=$("ul#tabControl").offset().left;
	$(".tab-item").on("click",function(event){
			event.preventDefault();
			if($(this).hasClass("current")){
				return false;
			}
			var id=$(this).find("a").attr("href");
			var hid=$(this).siblings(".current").find("a").attr("href");
			$(this).siblings(".current").removeClass("current");
			$(this).addClass("current");
			 
			$(hid).hide();
			$(id).show();
			var t  = $(this).offset().left,
				w  = $(this).width(),
			    ow = $(this).outerWidth(true);
			$(this).siblings(".slide").css({left:t-offleft-24,width:ow});
			$(this).siblings(".hover").css({left:t-offleft-10,width:ow});
	});
});

function openUrlPage(url,sysCode,title,todoId,servId,object1,todoCatalog,owner,con){
	if　(servId == 'SY_COMM_ZHIDAO_QUESTION') {
		FireFly.doAct("SY_COMM_TODO","endReadCon",{"_PK_":todoId});
	}
	if("PT"==sysCode){
		if (url == "") {
			showRHDialog(title,con,function exeToDo(){
				var data = {};
				data[UIConst.PK_KEY] = todoId;
				data["TODO_ID"] = todoId;
				var res = FireFly.doAct("SY_COMM_TODO","endReadCon",data,false);
				if (res[UIConst.RTN_MSG].indexOf(UIConst.RTN_OK) == 0) {
				}
	        }, this, "", null, null, null);
		} else {
			var options;
			var menuId = "2CM_TODO";
			if(url.indexOf(".byid.do") > 0){
				if(todoCatalog==2){
					menuId = "SC_TODO_READ__2";
				}
				options = {"url":servId + ".card.do?pkCode=" + object1, 
				"tTitle":title, "menuFlag":2, "menuId":menuId,"replaceUrl":url,"params":null,"areaId":null};
			}else if(url.indexOf(".show.do") > 0){
				var params = {"replaceUrl":url, "areaId":"","from":"todo"};
				options = {"url":url, "tTitle":title,"menuFlag":3, "params":params};
			}else{
				var params = {"replaceUrl":url, "areaId":"","from":"todo","portalHandler":null};
				options = {"url":url, "tTitle":title,"params":params,"areaId":null,"menuFlag":3};
			}
			Tab.open(options);
		}
		
	}else if("OA"==sysCode||"OAWT"==sysCode){

		var oaAddr = System.getVar("@C_OA_HOST_ADDR@").trim();
		window.open(oaAddr+"/sy/comm/page/page.jsp?fromPt=1&openTab="+encodeURIComponent(url));
	}
}
</script>