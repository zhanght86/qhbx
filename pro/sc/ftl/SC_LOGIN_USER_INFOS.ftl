<#assign csFlag = csFlag!"false">
<div class='portal-box' style='position: relative;padding:0;width:100%;min-width:250px;max-width:330px;height: 61px;min-height: 61px;background:#f7f7f7;border:1px #f2f2f2 solid;'>
<div class='portal-box-title' style='padding: 0;min-height: 57px;min-width:250px;width:100%;max-width:330px;height: 57px;background:none;border:none'>
	<div class="clearfix">
		<img id='SC_USER_IMG' style='float:left;padding: 0px 10px;cursor:pointer;'src="" title="进入个人基本信息">
		<div style='float:left;padding:10px 0;color:#000;font-size:13px;line-height:19px;font-weight:normal '>
			<span id='SC_USER_NAME' style='display: none;'>
				<div id="jiangang" title="查看兼岗情况" class="left-topBar-jian-text" style="display:inline-block;margin:0px;">
					<a href="javascript:void(0);" class="left-topBar-jian-text-con"></a>
				</div>
			</span>
			<div>
				 <span id='SC_USER_DEPT' style='display: inline-block;float: left;width: 100px;overflow: hidden;text-overflow: ellipsis; white-space: nowrap;'></span>&nbsp;
				 <a href="javascript:void(0)"style='display: inline-block;' onclick='scLoginOut()'>[退出]</a>
		    </div>
		</div>
	</div>
</div>
<#if csFlag=='true'>
<#if msg == "OK">
<#if userMailExist == "no" >
	<div class='mail'>
		<span></span>
	</div>
<#else>
	<div class='sc-mail' style='cursor:pointer;' onclick="coreMailLogin('${sessionId}')">
		<span>${noReadCount}</span>
	</div>
</#if>
</#if>
</#if>
</div>
<!---------------兼岗----------------------->
<script>
	jQuery(document).ready(function(){
		if (System.getVar("@JIAN_CODES@").length > 0) {
			var jianGang = jQuery("#jiangang");
			
			<#--兼岗图标更换-->
			var data = FireFly.doAct("SY_ORG_LOGIN","getJianUsers",null,false);
			var hasTodoFlag = false;
			jQuery.each(data._DATA_,function(i,n) {
				if (n.TODO_COUNT > 0) { <#--如果有待办-->
					hasTodoFlag = true;
				}
			});
			if (hasTodoFlag) { <#--有待办-->
				jianGang.addClass("left-topBar-jian-text");
			} else { <#--无待办-->
				jianGang.addClass("left-topBar-jian-text").addClass("no-todo");
			}
			
			jianGang.bind("click", function() {
				var obj = jQuery(".left-topBar-jian-list");
				if (obj.length == 1) {
					if (jQuery(".left-topBar-jian-list:visible").length == 1) {
						jQuery(".left-topBar-jian-list:visible").hide("normal");
					} else {
						jQuery(".left-topBar-jian-list").show("normal");
					}
				} else {
					var jianListAbsoluteClass = "rh-head-per-left";
					var jianList1 = jQuery("<ul style='top:0;left:0;width:400px;'></ul>").addClass("left-topBar-jian-list").addClass(jianListAbsoluteClass);
					jQuery("<span class='left-top-jian-list--title'>兼岗</span>").appendTo(jianList1);
					var data = FireFly.doAct("SY_ORG_LOGIN","getJianUsers",null,false);
					jQuery.each(data._DATA_,function(i,n) {
						var content1 = jQuery("<a href='#' class='left-top-jian-list--li--a' title='" + n.TDEPT_NAME + "(" + n.USER_NAME + ")'>" + n.ODEPT_NAME + "/" + n.TDEPT_NAME + "/" + n.USER_NAME + "  (<span style='color:red;'>" + n.TODO_COUNT + "条</span>)" + "</a>");
						content1.bind("click",function(event) {
							var result = FireFly.doAct("SY_ORG_LOGIN","changeUser",{"TO_USER_CODE":n.USER_CODE},false);
							if (result[UIConst.RTN_MSG].indexOf(UIConst.RTN_OK) == 0) {
								var res = confirm("当前页面将刷新，确定继续吗？");
								if (res === true) {
									parent.window.location.href = FireFly.getContextPath() + "/sy/comm/page/page.jsp";
								}
							} 
							event.stopPropagation();
						});
						var li1 = jQuery("<li class='left-top-jian-list--li'></li>");
						var aCon1 = jQuery("<div class='left-top-jian-list--li--aCon'></div>");
						aCon1.append(content1);
						li1.append(aCon1);
						li1.appendTo(jianList1);
					});
					jianList1.appendTo(jQuery("#jiangang"));
					jianList1.show("normal");
					jianList1.bind("mouseleave",function(event) {
						jianList1.hide("slow");
					});
				}
			});
		} else {
			jQuery("#jiangang").hide();
		}
	});
</script>
<script type="text/javascript">
    $(document).ready(function(){
		$("#SC_USER_DEPT").text(System.getVar("@DEPT_NAME@"))
						  .attr("title",System.getVar("@DEPT_NAME@"));
		$("#SC_USER_NAME").prepend(System.getVar("@USER_NAME@"));
		var imgUrl = System.getVar("@USER_IMG@");
			imgUrl = imgUrl.split("?")[0];
		imgUrl= imgUrl+"?size=55x55&t="+(new Date()).getTime();
		$("#SC_USER_IMG").attr("src",imgUrl).on("load",function(){
			var src = $(this).attr("src"),
				param = ImgUtils.getConstrainedSize(src,{width:55,height:55});
			$(this).attr(param);
		}).bind("click", function(event) {
			<#--var options = {"url":"SY_ORG_USER_CENTER.show.do","menuFlag":1,"menuId":"SY_USER_INFO__" + System.getVar("@CMPY_CODE@"),"tTitle":"基本信息"};-->
			var options = {"url":"SY_ORG_USER_CENTER.show.do","menuFlag":3,"menuId":"SY_USER_INFO__" + System.getVar("@CMPY_CODE@"),"tTitle":"基本信息"};
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
