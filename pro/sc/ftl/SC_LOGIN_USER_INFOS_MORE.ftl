<style>
.user-profile{position:relative;float:left;width:100px;height:100px;padding: 1px;vertical-align:middle;text-align:center;background-color: #FFF;}
.user-profile img{position:absolute;top:50%;left:50%;}
.user-detail{width:170px;margin-left:110px;color: #000;font-size:12px;font-weight:normal;line-height: 22px;}
.user-detail .user-name{font-size:18px;font-weight: bold; line-height:32px; color:#000}
.user-detail .user-sys-opt a:link{color:#7f7f7f;text-decoration:none; }
.user-detail .user-sys-opt a:hover,
.user-detail .user-sys-opt a:focus { text-decoration:none; color:#000; }
.userActivity{position:relative;height:60px;margin-top:10px; z-index:1;}

.userActivity table{width:280px;height:60px}
.userActivity table td{width: 68px;height: 60px;background-color: #FAFAFA;border-top: 1px solid #E5E5E5;border-left: 1px solid #E5E5E5;text-align:center;}
.userActivity a:link{background-color:#fbfbfb;}
.userActivity a:link,
.userActivity a:visited { display:block; width:100%; height:60px; color:#E5E5E5; font-size:12px;line-height: 12px; text-decoration:none; }
.userActivity a:hover,
.userActivity a:focus { background-color: #FDFDFD;text-decoration:none; color:#000; }
.userActivity a>span{display:block;color:#000;font-size:14px;font-weight: normal;}
.userActivity span.num{height:40px;font-size:22px;font-weight: bold; line-height:40px; color:#2a8dd4}
</style>
<#assign csFlag = csFlag!"false">
<div class='portal-box' style='position: relative;padding:0;width:100%;height:170px;min-height:170px;background:#FFF;border: 1px solid #E5E5E5;font-family:宋体'>
	<div class='portal-box-title' style='padding: 0;height: 100%;background:none;border:none'>
		<div class="user-profile">
		    <image id="SC_USER_IMG" src="/sc/img/61620.jpg" width="100" height="100" border="0" style="cursor:pointer;"/>
		</div>
		<div class="user-detail">
		    <div id="SC_USER_NAME" class="user-name"></div>
		    <div><span>部门：</span><span id="SC_USER_DEPT" class="user-dept"></span></div>
		    <div><span>职务：</span> <span class="user-role">系统管理员</span></div>
		    <div class="user-sys-opt">
		    	<a href="javascript:void(0)" id="js-showmore">[&nbsp;资料&nbsp;]</a>
		    	<a href="javascript:void(0)" onclick='scLoginOut()'>[&nbsp;退出&nbsp;]</a>
		    </div>
		</div>
		<div class="userActivity">
			<table border="0" cellpadding="0" cellspacing="0">
				<tr>
					<td style="border-left:none;">
						<a href="javascript:void(0);" class="js-trigger" data-mark="questions" data-active="js-mark-questions"> 
				      		<span id="js-question" class="num">0</span> 
				            <span>提问</span> 
				        </a>
					</td>
					<td>
						<a href="javascript:void(0);" class="js-trigger" data-mark="answers" data-active="js-mark-answers"> 
				        	<span id="js-answer" class="num">0</span> 
				            <span>回答</span> 
				        </a>
					</td>
					<td>
						<a href="javascript:void(0);" class="js-trigger" data-mark="follows"> 
				        	<span id="js-follow" class="num">0</span> 
				            <span>关注</span> 
				        </a> 
					</td>
					<#if csFlag=='true'>
					<#if msg == "OK">
					<#if userMailExist != "no" >
					<td>
						<a href="javascript:void(0);" onclick="coreMailLogin('${sessionId}');"> 
				        	<span class="num">${noReadCount}</span> 
				            <span>邮件</span> 
				        </a> 
					</td>
					</#if>
					</#if>
					</#if>
				</tr>
			</table>
		 </div>
	</div>
</div>
<script type="text/javascript">
    $(document).ready(function(){
		$("#SC_USER_NAME").html(System.getVar("@USER_NAME@"));
		$("#SC_USER_DEPT").text(System.getVar("@DEPT_NAME@"))
						  .attr("title",System.getVar("@DEPT_NAME@"));
		
		var imgUrl = System.getVar("@USER_IMG@");
			imgUrl = imgUrl.split("?")[0];
		imgUrl= imgUrl+"?size=100x100&t="+(new Date()).getTime();
	 	 
		
		$("#SC_USER_IMG").attr("src",imgUrl).on("load",function(){
			var src = $(this).attr("src"),
				param = ImgUtils.getConstrainedSize(src,{width:100,height:100});
				param["marginTop"]= - param["height"]/2;
				param["marginLeft"]= - param["width"]/2;
			$(this).css(param);
		}).bind("click", function(event) {
			var options = {"url":"SY_ORG_USER_CENTER.show.do","menuFlag":1,"menuId":"SY_USER_INFO__" + System.getVar("@CMPY_CODE@"),"tTitle":"基本信息"};
			parent.Tab.open(options);
		});
		
		$("#js-showmore").on("click",function(event){
			$("#SC_USER_IMG").trigger("click");
		});
		
		
		var result =  FireFly.doAct("SC_COREMAIL_SERV","getUserActivity",{},false,false);
		$("#js-question").html(result["questionCount"]);
		$("#js-answer").html(result["answerCount"]);
		$("#js-follow").html(result["followCount"]);
		
		$(".js-trigger").on("click",function(event){
			var mark = $(this).attr("data-mark"),
				active = $(this).attr("data-active"),
				url = "/cms/SY_COMM_CMS_TMPL/0i0y5gDfN3yH3IMt0hpErY.html?who="+System.getVar("@USER_CODE@")+"&mark="+mark;
			
			if(active){
				url += "&active="+active;
			}
			var options={'url':url,'tTitle':System.getVar("@USER_NAME@")+'的知道首页','menuFlag':3,'scrollFlag':false};
			parent.Tab.open(options);
		});
		
	});
	function scLoginOut(){
		var resultData = parent.FireFly.logout();
	       if (resultData[parent.UIConst.RTN_MSG].indexOf(parent.UIConst.RTN_OK) == 0) {
	    	   if (parent.opts.rhClient == "true" || parent.opts.rhClient == true) {
	    		   parent.document.title = "RhClientAction_Close";
	    	   } else {
	    		   parent.Tools.redirect(FireFly.getContextPath() + "/logout.jsp"); 
	    	   }
	       }
	       return false;
	};
	function coreMailLogin(sid){
		window.open("http://mail.capitalwater.cn/coremail/XJS/index.jsp?sid="+sid+"&firstShowPage=mbox/list.jsp?sid="+sid);
	}
</script>
