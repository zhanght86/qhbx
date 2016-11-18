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
<div id='SC_TODO_TAB' class='portal-box ${boxTheme}'>
	<div id="SC_TODO_TAB_CON" class='portal-box-con portal-box-tab'>
		<ul class='portal-box-title'>
			<li ><a href="#SC_TODO_TAB_DOTO">待办(<span id ='todos'>0</span>)</a></li>
			<li ><a href="#SC_TODO_TAB_READ">待阅(<span id ='reads'>0</span>)</a></li>
			<li ><a href="#SC_TODO_TAB_AGENCY">委托(<span id ='agencys'>0</span>)</a></li>
		</ul>
		<div style='margin-left:5px'>
			<div id='SC_TODO_TAB_DOTO' class='portal-box padding:0' style='min-height:100px;max-height:100px;OVERFLOW: auto;'>
				<#if _DATA_0._DATA_?size != 0>
				<script>
					$("#todos").text(${_DATA_0._DATA_?size});
				</script>
				<div id = 'todo-content-datas' style='width:100%;'>
				<#list _DATA_0._DATA_ as todo>
				<ul style='margin-top:5px'>
	                <li style='width:5%' class="WORK-CENTER-TODO-CONTENT-li"><span class="WORK-CENTER-TODO-CONTENT-span" style='margin-left:15%;'>#${todo_index+1}</span></li>
					<li style='width:10%' class="WORK-CENTER-TODO-CONTENT-li"><span class="WORK-CENTER-TODO-CONTENT-span">[${todo.sysName}]</span></li>
	                <li style='width:20%' class="WORK-CENTER-TODO-CONTENT-li"><span class="WORK-CENTER-TODO-CONTENT-span"><a href='#' title=${todo.title} style='color:#0000ff;text-decoration:none' onclick='openUrlPage("${todo.url}")'>${todo.shortTitle}</a></span></li>
	                <li style='width:15%' class="WORK-CENTER-TODO-CONTENT-li"><span class="WORK-CENTER-TODO-CONTENT-span">${todo.codeAlias}<span/></li>
	                <li style='width:25%' class="WORK-CENTER-TODO-CONTENT-li"><span class="WORK-CENTER-TODO-CONTENT-span">发送人：${todo.sendUserName}<span/></li>
	                <li style='width:25%' class="WORK-CENTER-TODO-CONTENT-li"><span class="WORK-CENTER-TODO-CONTENT-span">${todo.time}<span/></li>
	            </ul>
				</#list>
				</div>
				<#else>
				<ul>
					<span>没有待办需要处理！</span>
				</ul>
				</#if>
		    </div>
		</div>
		<div style='margin-left:5px'>
			<div id='SC_TODO_TAB_READ' class='portal-box padding:0' style='min-height:150px;max-height:150px'>
			    <div id='READ-CONTENER'>
		            <ul style='margin-top:15px'>
		                <li style='width:5%' class="WORK-CENTER-TODO-title-li"><span class="WORK-CENTER-TODO-title-span" style='margin-left:15%;'>#</span></li>
		                <li style='width:40%' class="WORK-CENTER-TODO-title-li"><span class="WORK-CENTER-TODO-title-span">标 题<span></li>
		                <li style='width:13%' class="WORK-CENTER-TODO-title-li"><span class="WORK-CENTER-TODO-title-span">待阅服务<span></li>
		                <li style='width:20%' class="WORK-CENTER-TODO-title-li"><span class="WORK-CENTER-TODO-title-span">发送人<span></li>
		                <li style='width:22%' class="WORK-CENTER-TODO-title-li"><span class="WORK-CENTER-TODO-title-span">发送时间<span></li>
		            </ul>
					<#if _DATA_1._DATA_?size != 0>
									<script>
					$("#reads").text(${_DATA_1._DATA_?size});
				</script>
					<div id = 'agency-content-datas' style='width:100%;height:120px;OVERFLOW: auto;'>
					<#list _DATA_1._DATA_ as read>
					<ul style='margin-top:15px'>
						<li style='width:5%' class="WORK-CENTER-TODO-CONTENT-li"><span class="WORK-CENTER-TODO-CONTENT-span" style='margin-left:15%;'>#${read_index+1}</span></li>
						<li style='width:10%' class="WORK-CENTER-TODO-CONTENT-li"><span class="WORK-CENTER-TODO-CONTENT-span">[${read.sysName}]</span></li>
		                <li style='width:20%' class="WORK-CENTER-TODO-CONTENT-li"><span class="WORK-CENTER-TODO-CONTENT-span"><a href='#' title=${read.title} style='color:#0000ff;text-decoration:none' onclick='openUrlPage("${read.url}")'>${read.shortTitle}</a></span></li>
		                <li style='width:15%' class="WORK-CENTER-TODO-CONTENT-li"><span class="WORK-CENTER-TODO-CONTENT-span">${read.codeAlias}<span/></li>
		                <li style='width:25%' class="WORK-CENTER-TODO-CONTENT-li"><span class="WORK-CENTER-TODO-CONTENT-span">发送人：${read.sendUserName}<span/></li>
		                <li style='width:25%' class="WORK-CENTER-TODO-CONTENT-li"><span class="WORK-CENTER-TODO-CONTENT-span">${read.time}<span/></li>
			        </ul>
					</#list>
					</div>
					<#else>
					<ul>
						<span>没有待办需要处理！</span>
					</ul>
					</#if>
		        </div>
		    </div>
		</div>
		<div style='margin-left:5px'>
			<div id='SC_TODO_TAB_AGENCY' class='portal-box padding:0' style='min-height:150px;max-height:150px;OVERFLOW: auto;'>

				<#if _DATA_2._DATA_?size != 0>
				<script>
					$("#agencys").text(${_DATA_2._DATA_?size});
				</script>
				<div id = 'agency-content-datas' style='width:100%;height:120px;OVERFLOW: auto;'>
				<#list _DATA_2._DATA_ as agency>
				<ul style='margin-top:5px'>
	                <li style='width:5%' class="WORK-CENTER-TODO-CONTENT-li"><span class="WORK-CENTER-TODO-CONTENT-span" style='margin-left:15%;'>#${agency_index+1}</span></li>
					<li style='width:10%' class="WORK-CENTER-TODO-CONTENT-li"><span class="WORK-CENTER-TODO-CONTENT-span">[${agency.sysName}]</span></li>
	                <li style='width:20%' class="WORK-CENTER-TODO-CONTENT-li"><span class="WORK-CENTER-TODO-CONTENT-span"><a href='#' title=${agency.title} style='color:#0000ff;text-decoration:none' onclick='openUrlPage("${agency.url}")'>${agency.shortTitle}</a></span></li>
	                <li style='width:15%' class="WORK-CENTER-TODO-CONTENT-li"><span class="WORK-CENTER-TODO-CONTENT-span">${agency.codeAlias}<span/></li>
	                <li style='width:25%' class="WORK-CENTER-TODO-CONTENT-li"><span class="WORK-CENTER-TODO-CONTENT-span">发送人：${agency.sendUserName}<span/></li>
	                <li style='width:25%' class="WORK-CENTER-TODO-CONTENT-li"><span class="WORK-CENTER-TODO-CONTENT-span">${agency.time}<span/></li>
	            </ul>
				</#list>
				</div>
				<#else>
				<ul>
					<span>没有需要处理的委托！</span>
				</ul>
				</#if>
		    </div>
		</div>
	</div>
</div>
<script type="text/javascript">
	
(function() {
    jQuery(document).ready(function(){
	    setTimeout(function() {
	      jQuery("#SC_TODO_TAB_CON").tab({});
	    },0);
    });
})();
	function openUrlPage(url){

		window.open("http://localhost:8082/servlet/com.zotn.screens.util.ShowSecondPageServlet?defaultUrl="+encodeURIComponent(url));
    }
</script>

