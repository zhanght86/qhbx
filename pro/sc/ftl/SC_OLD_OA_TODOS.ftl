<style type="text/css" >
    .WORK-CENTER-TODO-title-li {
        border-bottom: 1px #cccccc solid;
        float: left;
    } 
	.WORK-CENTER-TODO-title-span {
        font-family: 微软雅黑;
        font-size: 13px;
        font-weight: normal;
        font-style: normal;
        text-decoration: none;
        color: #990000;
    }
	.WORK-CENTER-TODO-CONTENT-li{
		float: left;
		margin-top:5px;
	}
    .WORK-CENTER-TODO-CONTENT-span {
        font-family: 宋体;
        font-size: 13px;
        font-weight: normal;
        font-style: normal;
        text-decoration: none;
        color: #333333;
    }
</style>
<div style='margin-left:5px'>
	<div id='WORK-CENTER-TODO' class='portal-box padding:0' style='min-height:250px;max-height:250px'>
	    <div class='portal-box-title'>
	    	<span class="portal-box-title-pre"></span>
	        <span class="portal-box-title-label">待办事项</span>
			<span class='portal-box-title-fix'></span>
	        <span class="portal-box-more"><a href="#" onclick="alert(1)"></a></span>
	    </div>
	    <div id='TODO-CONTENER'>
	        <div style="width:100%">
	            <ul style='margin-top:15px'>
	                <li style='width:5%' class="WORK-CENTER-TODO-title-li"><span class="WORK-CENTER-TODO-title-span" style='margin-left:15%;'>#</span></li>
	                <li style='width:40%' class="WORK-CENTER-TODO-title-li"><span class="WORK-CENTER-TODO-title-span">标 题<span></li>
	                <li style='width:13%' class="WORK-CENTER-TODO-title-li"><span class="WORK-CENTER-TODO-title-span">待办服务<span></li>
	                <li style='width:20%' class="WORK-CENTER-TODO-title-li"><span class="WORK-CENTER-TODO-title-span">发送人<span></li>
	                <li style='width:22%' class="WORK-CENTER-TODO-title-li"><span class="WORK-CENTER-TODO-title-span">发送时间<span></li>
	            </ul>
				<div id = 'todo-content-datas'></div>
	        </div>
	    </div>
	</div>
	
	<div id='WORK-CENTER-TOREAD' class='portal-box padding:0' style='min-height:250px;max-height:250px;'>
	    <div class='portal-box-title'>
	        <span class="portal-box-title-label">待阅事项(</span><span id='todo-count' style='color:#FF0000'>1</span><span>)</span>
	        <span class="portal-box-more"><a href="#" onclick="alert(1)"></a></span>
	    </div>
	    <div id='TOREAD-CONTENER'>
	        <div style="width:100%">
	            <ul style='margin-top:15px'>
	                <li style='width:5%' class="WORK-CENTER-TODO-title-li"><span class="WORK-CENTER-TODO-title-span" style='margin-left:15%;'>#</span></li>
	                <li style='width:40%' class="WORK-CENTER-TODO-title-li"><span class="WORK-CENTER-TODO-title-span">标 题<span></li>
	                <li style='width:13%' class="WORK-CENTER-TODO-title-li"><span class="WORK-CENTER-TODO-title-span">待阅服务<span></li>
	                <li style='width:20%' class="WORK-CENTER-TODO-title-li"><span class="WORK-CENTER-TODO-title-span">发送人<span></li>
	                <li style='width:22%' class="WORK-CENTER-TODO-title-li"><span class="WORK-CENTER-TODO-title-span">发送时间<span></li>
	            </ul>
				<div id = 'toread-content-datas'></div>
	        </div>
	    </div>
	</div>
	
	<div id='WORK-CENTER-TODO-PART' class='portal-box padding:0' style='min-height:250px;max-height:250px;'>
	    <div class='portal-box-title'>
	        <span class="portal-box-title-label">兼职待办事项(</span><span id='todo-count' style='color:#FF0000'>1</span><span>)</span>
	        <span class="portal-box-more"><a href="#" onclick="alert(1)"></a></span>
	    </div>
	    <div id='TODO-PART-CONTENER'>
	        <div style="width:100%">
	            <ul style='margin-top:15px'>
	                <li style='width:5%' class="WORK-CENTER-TODO-title-li"><span class="WORK-CENTER-TODO-title-span" style='margin-left:15%;'>#</span></li>
	                <li style='width:40%' class="WORK-CENTER-TODO-title-li"><span class="WORK-CENTER-TODO-title-span">标 题<span></li>
	                <li style='width:13%' class="WORK-CENTER-TODO-title-li"><span class="WORK-CENTER-TODO-title-span">待办服务<span></li>
	                <li style='width:20%' class="WORK-CENTER-TODO-title-li"><span class="WORK-CENTER-TODO-title-span">发送人<span></li>
	                <li style='width:22%' class="WORK-CENTER-TODO-title-li"><span class="WORK-CENTER-TODO-title-span">发送时间<span></li>
	            </ul>
				<div id = 'todoparttime-content-datas'></div>
	        </div>
	    </div>
	</div>
</div>

<script type="text/javascript">
    (function(){
        $(document).ready(function(){
			readMyTodoDatas("todo-content-datas",0);
			
			readMyTodoDatas("todoparttime-content-datas","parttime");
    	})
	})();
	
	function readMyTodoDatas(divId,catalog){
		var i = 0;
		var data={};
		var dtTodos={};
		if(catalog==0){
			dtTodos = FireFly.doAct("SC_OLD_OA_SERV","getTodos",data);
		}else if(catalog==1){
			dtTodos = FireFly.doAct("SC_OLD_OA_SERV","getToReads",data);
		}else if(catalog=="parttime"){
			dtTodos = FireFly.doAct("SC_OLD_OA_SERV","getPartTimeToDos",data);
		}
		var ulId = "";
		var user={};
		var divObj = $("#"+divId);
		divObj.empty();
        $(dtTodos._DATA_).each(function(){
			user = FireFly.byId("SY_ORG_USER",this.sendUser);
			ulId=divId+"-"+i;
			$("<ul></ul>").attr("id",ulId).appendTo(divObj);
			$("<li style='width:5%' class='WORK-CENTER-TODO-CONTENT-li'><span class='WORK-CENTER-TODO-CONTENT-span' style='margin-left:15%;'>"+(i+1)+"</span></li>").appendTo($("#" + ulId));
			$("<li style='width:40%' class='WORK-CENTER-TODO-CONTENT-li'><span class='WORK-CENTER-TODO-CONTENT-span'><a href='#' onclick='openUrlPage(\""+this.url+"\")'>"+this.title+"</a></span></li>").appendTo($("#" + ulId));
			$("<li style='width:13%' class='WORK-CENTER-TODO-CONTENT-li'><span class='WORK-CENTER-TODO-CONTENT-span'>"+this.codeAlias+"<s/pan></li>").appendTo($("#" + ulId));
			$("<li style='width:20%' class='WORK-CENTER-TODO-CONTENT-li'><span class='WORK-CENTER-TODO-CONTENT-span'>[<a style='color:#0000ff'>"+user.DEPT_NAME+"</a>]"+user.USER_NAME+"</span></li>").appendTo($("#" + ulId));
			$("<li style='width:22%' class='WORK-CENTER-TODO-CONTENT-li'><span class='WORK-CENTER-TODO-CONTENT-span'>"+this.time+"</span></li>").appendTo($("#" + ulId));
			i=i+1;
		});
	};
	function openUrlPage(url){
		window.open("http://localhost:8082/servlet/com.zotn.screens.util.ShowSecondPageServlet?defaultUrl="+encodeURIComponent(url));
    }
</script>

