<link rel="stylesheet" type="text/css" href="/sc/comm/todo/css/todo.css"/>

<script src="/sc/js/jquery.tmpl.min.js"></script>
<style>
.portal-box-more {border:0px red solid;height:24px;padding:0px 11px 0px 15px;float:right;margin-top:0px;margin-right:10px;*margin-top:-27px;background:url("/sy/theme/default/images/common/more.png") no-repeat 0px -26px;}

.pt-todo-wrapper .tabControl .tab-item a {color:#136098; font-family:Microsoft Yahei;}
</style>
<#noparse>
<script id="scTodoTemplate" type="text/x-jquery-tmpl">
<div class="pt-todo-item clearfix">
	<a href="javascript:void(0);" title="${TODO_TITLE}" onclick="openUrlPage('${TODO_URL}','${SYS_CODE}','${TODO_CODE_NAME}','${TODO_ID}','${SERV_ID}','${TODO_OBJECT_ID1}','${TODO_CATALOG}','${OWNER_CODE}','${TODO_CONTENT}')">
		<div class="fl w60">
			<div class="pt-todo-icon fl icon-col"></div>
			<div class="fl sys-col">[<span class="pt-todo-system">${SYS_NAME}</span>]</div>
			<div class="fl title-col">
				<span class="elipd" >${SHORT_TITLE}</span>
			</div>
		</div>
		<div class="fr w40">
			<div class="fl tl">发送人：${SEND_USER_NAME}</div>
			<div class="fr">
				 ${rhDate.pattern("yyyy-MM-dd",TODO_SEND_TIME)}
			</div>
		</div>
	</a>
</div>
</script>
<script id="scBlankTemplate" type="text/x-jquery-tmpl">
	<div class="pt-todo-blank">恭喜,所有的${title}均已处理完毕！</div>
</script>
</#noparse>


<div class="portal-box pt-todo-wrapper" style="width:98%">
	<div class="portal-box-title pt-todo-header">
        <div class="pt-todo-tab"> 
          <ul class="tabControl clearfix" id="tabControl">
                <li class="tab-item current">
                    <a href="#js-todo" title="待办"> 待办</a>
                </li>
                <!--
				<li class="line">
                    |
                </li>
				-->
                <li class="tab-item">
                    <a href="#js-toRead" title="待阅">待阅</a>
                </li>
                <!--
				<li class="line">
                    |
                </li>
				-->
                <li class="tab-item">
                    <a href="#js-delegate" title="委托"> 委托</a>
                </li>
                <li class="slide">
                    <div class="icon">
                    </div>
                </li>
                <li class="hover" id="hover" style="left:18px;">
                </li>
				<span class="portal-box-more-sc" style="margin-right:0px;"><a href="javascript:void(0);" onclick="openMoreTodo()">更多</a></span>
				<span id='todoMore'><a onclick='handRefresh()'>刷新</a></span>
            </ul>
        </div>
      </div>
  <div class="portal-box-con pt-todo-container">
      <!------------------end of pt-todo-content-header----------------------------->
       <div id="js-todo" class="pt-todo-grid pt-beautify pt-todo-loading" style="height:${height};max-height:${height};">
       	 
        </div>
      <!---------------end of js-latest-qa----------------------------> 
       	<div id="js-toRead" class="pt-todo-grid pt-beautify pt-todo-loading" style="display:none;height:${height};max-height:${height};">
			 
        </div>
        
      <!---------------end of js-leader-qa----------------------------> 
       	<div id="js-delegate" class="pt-todo-grid pt-beautify pt-todo-loading" style="display:none;height:${height};max-height:${height};">
			 
        </div>
      <!---------------end of js-dept-qa----------------------------> 
    </div>
  <!---------------end of content---------------------------->
</div>
<script type="text/javascript">
/*打开更多待办待阅*/
function openMoreTodo(){
	var $element = jQuery("#tabControl .tab-item.current").find("a");
	var openUrl = "";
	var tTitle = "";
	if($element.attr("title")=="待办"){
	openUrl = "BN_COMM_TODO";
	}else if($element.attr("title")=="待阅"){
	openUrl = "BN_COMM_TODO_READ";
	}else if($element.attr("title")=="委托"){
	openUrl = "SY_ORG_USER_TYPE_AGENT_TO";
	}
	var options = {"url": openUrl + ".list.do", "tTitle":$element.attr("title"), "menuFlag":"3"};
	var tabP = jQuery.toJSON(options);
	tabP = tabP.replace(/\"/g,"'");
	window.open("/sy/comm/page/page.jsp?openTab="+tabP);
}

function asyncRender(target,act){
    FireFly.doAct("SC_TODO_COLLECTIONS",act,{},false,true,function(result){
		var data = result["_DATA_"],
		    len = len = data ? result["_DATA_"].length : 0,
		    $tabItem = $(".tab-item").find("a[href=" + target + "]"),
		    title = $tabItem.attr("title");
		
		if (len) {
			$tabItem.html(title+"(<span>" + len + "</span>)");
			$(target).find(".mCSB_container")
						 .css("marginRight","-16px")
						 .html($("#scTodoTemplate").tmpl(data))
						 .css("marginRight",0);
		} else {
			data = {title:$tabItem.attr("title")};
			$tabItem.html(title+"(<span>0</span>)");
			$(target).html($("#scBlankTemplate").tmpl(data));
		}
		$(target).removeClass("pt-todo-loading");
	});
}


/*
function handRefresh(){
	this.portalView.refreshBlock("SC_TODOS_COLLECTION");
}*/

/**手动刷新待办，待阅，委托**/
function handRefresh(){
	var type ="";
	var currentTab = jQuery(".current").find("a").attr("href");
	if(currentTab == "#js-todo"){
		type = "todo";
	}else if(currentTab == "#js-toRead"){
		type = "read";
	}else if(currentTab == "#js-delegate"){
		type = "wt";
	}
	if(type=="todo"){
		asyncRender("#js-todo" , "getTodos");
	}else if(type == "read"){
		asyncRender("#js-toRead" , "getReads");
	}else if(type=="wt"){
		asyncRender("#js-delegate" , "getWts");
	}
}

function openUrlPage(url,sysCode,title,todoId,servId,object1,todoCatalog,owner,con){
	var options;
	if　(servId == 'SY_COMM_ZHIDAO_QUESTION') {
		FireFly.doAct("SY_COMM_TODO","endReadCon",{"_PK_":todoId});
	}
	if("PT"==sysCode){
		if (url == "") {
			showRHDialog(title,con,function(){
				var data = {};
				data[UIConst.PK_KEY] = todoId;
				data["TODO_ID"] = todoId;
				FireFly.doAct("SY_COMM_TODO","endReadCon",data,false);
	        }, this, "", window.event, "SC_TODOS_COLLECTION", this.portalView);
		} else {
			
			var menuId = "2CM_TODO";
			if(url.indexOf(".byid.do") > 0){
				if(todoCatalog==2){
					menuId = "SC_TODO_READ__2";
				}
				var params = {"portalHandler":this.portalView};
				options = {"url":servId + ".card.do?pkCode=" + object1, "tTitle":title, "menuFlag":3,"replaceUrl":url,"areaId":"SC_TODOS_COLLECTION"};
			}else if(url.indexOf(".show.do") > 0){
				var params = {"replaceUrl":url, "from":"todo"};
				if(url.indexOf("QA_WJDC_TEST_PAPER.show.do")>=0){
					params["callBackHandler"]=this.portalView;
					params["closeCallBackFunc"] = function(){
						handRefresh();
					};
				}
				options = {"url":url, "tTitle":title,"menuFlag":3, "areaId":"SC_TODOS_COLLECTION","params":params};
			}else{
				var params = {"replaceUrl":url, "from":"todo","portalHandler":this.portalView};
				options = {"url":url, "tTitle":title,"params":params,"areaId":"SC_TODOS_COLLECTION","menuFlag":3};
			}
				
			var options = jQuery.toJSON(options);
			options = options.replace(/\"/g,"'");
			window.open("/sy/comm/page/page.jsp?openTab="+options);
		}
		
	}else if("OA"==sysCode||"OAWT"==sysCode){
		if(!checkeKey()){
			parent.jQuery("#loginOut").trigger("click");
			return false;
		}
		var userCode = System.getVar("@USER_CODE@");
		url = url+"&fromPt=1&userCode=" + userCode;
		
		window.open(url);
	}else if("OLD_OA"==sysCode){
		if(!checkeKey()){
			parent.jQuery("#loginOut").trigger("click");
			return false;
		}
		var oaAddr = jQuery.trim(System.getVar("@C_SC_OA_SERVICE_URL@"));
		var userCode = System.getVar("@USER_CODE@");
		window.open(oaAddr+"/servlet/com.zotn.screens.util.ShowSecondPageServlet?defaultUrl="+encodeURIComponent(url)+"&fromPt=1&userCode="+userCode);
	}
}


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
			
			var t  = $(this).position().left,
			    ow = $(this).outerWidth(true);
				
			/*	
			$(this).siblings(".slide").css({left:t,width:ow});
			$(this).siblings(".hover").css({left:t,width:ow});
			 */
			 if("0" == $(this).index()){
				$(this).siblings(".slide").css({left:t,width:ow});
				$(this).siblings(".hover").css("left","18px");
			 }
			 if("1" == $(this).index()){
				$(this).siblings(".slide").css({left:t,width:ow});
				$(this).siblings(".hover").css("left","117px");
			 }
			  if("2" == $(this).index()){
				$(this).siblings(".slide").css({left:t,width:ow});
				$(this).siblings(".hover").css("left","215px");
			 }
			 
			 
	});
	
	asyncRender("#js-todo" , "getTodos");
 	asyncRender("#js-toRead" , "getReads");
 	asyncRender("#js-delegate" , "getWts");
	/**定时刷新待办组件**/
	window.setInterval(function(){
		handRefresh();
	},30000);
});
</script>