/**
 * 页面加载执行的方法
 * @param {Object} 
 */
$(document).ready(function(){
	/**
	 * 组织机构面板
	 */
	var _self = this;
	function replaceId (str) {
		return str.replace(/[^\w]/gi, "_");
	};
	//var dictId = "SY_ORG_DEPT_EXT_SCHR";
	var odept = System.getVar('@ODEPT_CODE@');
//	var dictId = "SC_PT_ORG_ALL"; -- zjx废除原有字典,使用统一的组织机构服务
	var dictId = "SY_ORG_DEPT_SUB";
	var exwhere = "";
	
	if(odept=="24"){
		exwhere = " and 1=1";
		 $("#leftOrg").attr("value","0001B210000000000BU3^");
	}else{
		exwhere = " and ODEPT_CODE='" +odept +"'";
		 $("#leftOrg").attr("value",odept+"^");
	}
	
	//设置树的初始化数据
    var tempData = FireFly.getDict(dictId,""," and 1=1","","",""); //zjx -- //放弃从字典中查询，使用后台方法
	//var tempData = FireFly.doAct('SY_COMM_ADDRESS_LIST_SC','createTree',{'dictId':dictId,'extWhere':exwhere});

    var orgSetting = {// 机构树的配置
           	showcheck: false,  
           	url: "SY_COMM_INFO.dict.do", 
            theme: "bbit-tree-no-lines", //bbit-tree-lines ,bbit-tree-no-lines,bbit-tree-arrows
            rhexpand: false,
            cascadecheck: true,
            checkParent: false,
            childOnly:false,
            rhBeforeOnNodeClick: function (item,id) {//点击添加选中状态之前
	        	var nodeObj = jQuery("#" + id + "_" + replaceId(item.ID),_self.navTree);
	        	if (nodeObj.hasClass("bbit-tree-selected")) {//节点取消选中
	        		nodeObj.addClass("rh-bbit-tree-selected");
	        	}
	        },
            onnodeclick: function(item,id){
            	var nodeObj = jQuery("#" + id + "_" + replaceId(item.ID),_self.navTree);
	        	if (nodeObj.hasClass("rh-bbit-tree-selected")) {//节点取消选中
	        		nodeObj.removeClass("bbit-tree-selected");
	        		nodeObj.removeClass("rh-bbit-tree-selected");
	        		orgUserSearch(null,odept);
	        	}else{
	        		orgUserSearch(item,odept);
	        	}
            },
            afterExpand: function(){//在节点展开后
            	resetPortalHeight();
            },
            afterCollapsed: function(){//在节点折叠后
            	resetPortalHeight();
            }
        };
		
//  设置树的初始化数据
//    orgSetting.data = tempData[0]["CHILD"];
    orgSetting.data = tempData[0]['CHILD']; // zjx -- 上面的取不到值
    var child = orgSetting.data[0].CHILD;
    if (child.length == 1) {
    	orgSetting.data = child;
    }
    // 异步加载条件
    orgSetting.dictId = dictId;
    var orgTree = new rh.ui.Tree(orgSetting);
	var treeId = orgTree.obj.find(".bbit-tree-root > li > div").attr("id");
	jQuery("#"+treeId,orgTree.obj).click();	

    $("#ORG_TREE").append(orgTree.obj);
	     	
    distroyScorllLoad();
    initScrollLoad();
    $("#txl-search").unbind().bind("keypress",function(event){
	
    	if(event.which==13){
    		userZhQuery($("#txl-search")[0].value);
    	};
    });
    
    $(".txl-conHeader-search").unbind().bind("click",function(){
    	userZhQuery($("#txl-search")[0].value);
    });
//    setHei(parseInt($("#dataCount").attr("value")),1);

	// zjx - 分公司显示本部门
	if (odept != '0001B210000000000BU3') {
    	var dept = System.getVar('@DEPT_CODE@');
    	$("#txl-users").children().remove();
    	loadNextData(null,1," and DEPT_CODE = '" + dept + "'");
    }
    resetPortalHeight();
    placePhoto();
    $(".sc-portal-bottom-txl").css({
    	width:1200,
    	marginLeft:0
    })

});
/**
 * 重置protal 高度
 */
function resetPortalHeight(){
	setTimeout(function(){
		Tab.setFrameHei($(".portal-target").height()+30);
	},100);
}
function placePhoto(){
	//放置用户人像
	jQuery(".photo").each(function(i,n) {		
		var id = jQuery(n).attr("id");
		var imgSrc = "";
		if ( id.length > 6) {
			var imgSub = id.substring(6,id.length);
			if (imgSub.indexOf(",") > 0) {
				imgSrc = FireFly.getContextPath() + "/file/" + imgSub.substring(0, imgSub.indexOf(","))+"?size=50x50";
			} else {
				imgSrc = FireFly.getContextPath() + "/file/" + imgSub+"?size=50x50";
			}
		}else{
			 imgSrc = "/sc/comm/addressbook/img/defaultPicture.gif"; 
		}
		jQuery(n).attr("src",imgSrc);
	});
}

/**
 * 通过组织机构查询
 * @param item 机构信息
 */
function orgUserSearch(item,odept){
	var codePath="";
	var where = "";
	if(item){
		codePath=getCodePath(item);
		// zjx -- 替换掉虚拟节点的codePath
		//codePath = codePath.replace('12^','');
		$("#leftOrg").attr("value",codePath);
		if(System.getVar("@ODEPT_CODE@").indexOf("0001B210000000000BU3") < 0) {
			var tempCodePath = codePath.replace('12^','');
			where = " and DEPT_CODE in (select DEPT_CODE from SY_ORG_DEPT where 1=1  and (CMPY_CODE='"+System.getVar("@CMPY_CODE@")+"' )  and CODE_PATH like '3rin6giCR9vUIv6kHIO3ex^"+tempCodePath+"%')";
		} else {
			where = " and DEPT_CODE in (select DEPT_CODE from SY_ORG_DEPT where 1=1  and (CMPY_CODE='"+System.getVar("@CMPY_CODE@")+"' )  and CODE_PATH like '"+codePath+"%')";
		}
//		where = " and DEPT_CODE in (select DEPT_CODE from SY_ORG_DEPT_EXT where 1=1  and (CMPY_CODE='"+System.getVar("@CMPY_CODE@")+"' )  and CODE_PATH like '"+codePath+"%')";
		//where = " and DEPT_CODE in (select DEPT_CODE from SY_ORG_DEPT where 1=1  and (CMPY_CODE='"+System.getVar("@CMPY_CODE@")+"' )  and CODE_PATH like '"+codePath+"%')";
	}else{
		if(odept=='0001B210000000000BU3'){
			$("#leftOrg").attr("value","");
		}else{
			$("#leftOrg").attr("value","0001B210000000000BU3^"+odept+"^");
		}
		
		//$("#leftOrg").attr("value","0001B210000000000BU3^12^");
	}
	
	var searchText = $("#txl-search")[0].value;
	var preZm = $("#preZm").attr("value");
	if($.trim(preZm).length>0){
		where = where+" and (USER_SPELLING like '"+preZm+"%' or USER_SPELLING like '"+preZm.toLowerCase()+"%')";
	}
	if($.trim(searchText).length>0){
		where = where+" and (USER_SPELLING like '%" + searchText + "%' or USER_NAME like '%" + searchText + "%')";
	};
	$("#txl-users").children().remove();
	loadNextData(null,1,where);
	distroyScorllLoad();
	initScrollLoad();
}

/**
 * 递归获取部门的codePath
 * @param item
 * @returns {String}
 */
function getCodePath(item){
	if(item.parent && $.trim(item.PID).length>0){
		return getCodePath(item.parent)+item.ID+"^";
	}else{
		if(item.PID.length>0){
			return item.PID+"^"+item.ID+"^";
		}else{
			return item.ID+"^";
		}
		
	}
}

/**
 * 字母查询
 * @param preZm 首字母
 */
function zmSearch(preZmObj){
	var preZm = preZmObj.innerHTML;
	$(".txl-zm-div").removeClass("txl-zmsearch-selected");
	$("#zmAll").removeClass("txl-zmsearch-all-selected");
	$(preZmObj).parent().addClass("txl-zmsearch-selected");
	$("#preZm").attr("value",preZm);
	var leftOrg = $("#leftOrg").attr("value");
	var searchText = $("#txl-search")[0].value;
	var where = " and (USER_SPELLING like '"+preZm+"%' or USER_SPELLING like '"+preZm.toLowerCase()+"%')";
	if($.trim(leftOrg).length>0){
		if(leftOrg.indexOf('12^') >= 0) {
			var tempLeftOrg = leftOrg.replace('12^','');
			where = where+" and DEPT_CODE in (select DEPT_CODE from SY_ORG_DEPT where 1=1  and (CMPY_CODE='"+System.getVar("@CMPY_CODE@")+"' )  and CODE_PATH like '"+tempLeftOrg+"%' and ODEPT_CODE = '0001B210000000000BU3')";
		} else {
		where = where+" and DEPT_CODE in (select DEPT_CODE from SY_ORG_DEPT where 1=1  and (CMPY_CODE='"+System.getVar("@CMPY_CODE@")+"' )  and CODE_PATH like '"+leftOrg+"%')";
		}
//		where = where+" and DEPT_CODE in (select DEPT_CODE from SY_ORG_DEPT_EXT where 1=1  and (CMPY_CODE='"+System.getVar("@CMPY_CODE@")+"' )  and CODE_PATH like '"+leftOrg+"%')";
	}
	if($.trim(searchText).length>0){
		where = where+" and (USER_SPELLING like '%" + searchText + "%' or USER_NAME like '%" + searchText + "%')";
	}
	$("#txl-users").children().remove();
	loadNextData(null,1,where);
	distroyScorllLoad();
	initScrollLoad();
}


/**
 * 清除字母查询条件
 */
function zmSearchClear(){
	$(".txl-zm-div").removeClass("txl-zmsearch-selected");
	$("#zmAll").removeClass("txl-zmsearch-all-selected");
	$("#zmAll").addClass("txl-zmsearch-all-selected");
	$("#preZm").attr("value","");
	var leftOrg = $("#leftOrg").attr("value");
	var searchText = $("#txl-search")[0].value;
	var where = "";
	if($.trim(leftOrg).length>0){
		if(leftOrg.indexOf('12^') >= 0) {
			var tempLeftOrg = leftOrg.replace('12^','');
			where = where+" and DEPT_CODE in (select DEPT_CODE from SY_ORG_DEPT where 1=1  and (CMPY_CODE='"+System.getVar("@CMPY_CODE@")+"' )  and CODE_PATH like '"+tempLeftOrg+"%' and ODEPT_CODE = '0001B210000000000BU3')";
		} else {
			where = where+" and DEPT_CODE in (select DEPT_CODE from SY_ORG_DEPT where 1=1  and (CMPY_CODE='"+System.getVar("@CMPY_CODE@")+"' )  and CODE_PATH like '"+leftOrg+"%')";		
		}
//		where = where+" and DEPT_CODE in (select DEPT_CODE from SY_ORG_DEPT_EXT where 1=1  and (CMPY_CODE='"+System.getVar("@CMPY_CODE@")+"' )  and CODE_PATH like '"+leftOrg+"%')";
		//where = where+" and DEPT_CODE in (select DEPT_CODE from SY_ORG_DEPT where 1=1  and (CMPY_CODE='"+System.getVar("@CMPY_CODE@")+"' )  and CODE_PATH like '"+leftOrg+"%')";
	}
	if($.trim(searchText).length>0){
		where = where+" and (USER_SPELLING like '%" + searchText + "%' or USER_NAME like '%" + searchText + "%')";
	}
	$("#txl-users").children().remove();
	loadNextData(null,1,where);
	distroyScorllLoad();
	initScrollLoad();
}

/**
 * 姓名综合模糊查询，查询为登录名及用户姓名的模糊查询
 * @param inputValue
 */
function userZhQuery(inputValue){
	var preZm = $("#preZm").attr("value");
	var leftOrg = $("#leftOrg").attr("value");
	var where = "";
	var preZm = $("#preZm").attr("value");
	if($.trim(preZm).length>0){
		where = where+" and (USER_SPELLING like '"+preZm+"%' or USER_SPELLING like '"+preZm.toLowerCase()+"%')";
	}
	if($.trim(leftOrg).length>0){
		if(leftOrg.indexOf('12^') >= 0) {
			var tempLeftOrg = leftOrg.replace('12^','');
			where = where+" and DEPT_CODE in (select DEPT_CODE from SY_ORG_DEPT where 1=1  and (CMPY_CODE='"+System.getVar("@CMPY_CODE@")+"' )  and CODE_PATH like '"+tempLeftOrg+"%' and ODEPT_CODE = '0001B210000000000BU3')";
		} else {
			where = where+" and DEPT_CODE in (select DEPT_CODE from SY_ORG_DEPT where 1=1  and (CMPY_CODE='"+System.getVar("@CMPY_CODE@")+"' )  and CODE_PATH like '"+leftOrg+"%')";		
		}
//		where = where+" and DEPT_CODE in (select DEPT_CODE from SY_ORG_DEPT_EXT where 1=1  and (CMPY_CODE='"+System.getVar("@CMPY_CODE@")+"' )  and CODE_PATH like '"+leftOrg+"%')";
		//where = where+" and DEPT_CODE in (select DEPT_CODE from SY_ORG_DEPT where 1=1  and (CMPY_CODE='"+System.getVar("@CMPY_CODE@")+"' )  and CODE_PATH like '"+leftOrg+"%')";
	}
	if($.trim(inputValue).length>0){
		where = where+" and (USER_SPELLING like '%" + inputValue + "%' or USER_NAME like '%" + inputValue + "%')";
	}
	$("#txl-users").children().remove();
	loadNextData(null,1,where);
	distroyScorllLoad();
	initScrollLoad();
};
 
/**
 * 显示详细信息
 * @param id 用户CODE
 */
function showUserInfos(id,odeptCode){
	var $dsp = jQuery("#"+id+"-div").find("#"+id+"dsp"),
		dspLen = $dsp.length;
	if(!dspLen){
		var user = FireFly.byId("SY_COMM_ADDRESS_LIST", id);
		var userCode = user.USER_CODE;
		var loginName= user.USER_LOGIN_NAME;
		var userName = user.USER_NAME;
		var userPost = user.USER_POST;
		var odeptName = user.ODEPT_CODE__NAME;
		var deptName = user.DEPT_CODE__NAME;
		var email = user.USER_EMAIL;
		var vpmn = user.USER_VPMN;
		if(deptName.length>odeptName.length){
			deptName = deptName.substring(odeptName.length+1,deptName.length)
		};
		var imgSub = user.USER_IMG_SRC;
        if (imgSub.length > 0) {
            if (imgSub.indexOf(",") > 0) {
				imgSrc = FireFly.getContextPath() + "/file/" + imgSub.substring(0, imgSub.indexOf(","))+"?size=50x50";
			} else {
				imgSrc = FireFly.getContextPath() + "/file/" + imgSub+"?size=50x50";
			}
		}else{
			 imgSrc = "/sc/comm/addressbook/img/defaultPicture.gif"; 
		}
		var phone = user.USER_OFFICE_PHONE;
		var mobile = user.USER_MOBILE;
		var txlUserDsp = "<div id = '"+userCode+"dsp' class='txl-user-div-dsp'>"
			+"<div class='dsp-box'><div class='dsp-cont'>"
			+"<div class='dsp-cont-up'>"
			+"<table>"
			+"<tr><td style='width:auto;height:50px;display:inline;' rowspan='2'><img style='width:auto;height:50px;' src='"+imgSrc+"'/></td><td width='108' style='height:50px;line-height:25px;'><span style='display:block;'>"+userName+"</span><span style='display:block;'>"+userPost+"</span></td></tr>"
			//+"<tr><td height='25'>"+userPost+"</td></tr>"
			+"</table>"
			+"</div>"
			+"<div class='dsp-cont-down'>"
			+"<table>"
			+"<tr height='23' valign='top' style='line-height:23px'><td width='40' align='right'>单位：</td><td width='145'>"+odeptName+"</td></tr>"
			+"<tr height='23' valign='top' style='line-height:23px'><td width='40' align='right'>部门：</td><td width='145'>"+deptName+"</td></tr>"
			+"<tr height='23' style='line-height:23px'><td width='40' align='right'>电话：</td><td width='145'>"+phone+"</td></tr>"
			+"<tr height='23' style='line-height:23px'><td width='40' align='right'>手机：</td><td width='145'>"+mobile+"</td></tr>"
			//+"<tr height='23' style='line-height:23px'><td width='40' align='right'>集团号</td><td width='145'>："+vpmn+"</td></tr>"
			+"<tr><td valign='bottom' colspan='2'><div class='dsp-cont-down-buttons'>"
			+"<div class='dsp-cont-rtx'><img src='/sc/comm/contacts/images/blank.gif' data-name='"+loginName+"' class='RTX' width='20' height='20' /></div>"
			+"<a class='dsp-cont-down-email' href='mailto:"+email+"'></a>";
		if(odeptCode=='0001B210000000000BU3'&&false){
			txlUserDsp = txlUserDsp +"<div class='dsp-cont-down-msg' onclick=sendPhoneMsg('"+mobile+"','"+userCode+"','"+userName+"')></div>";
		}
			
		txlUserDsp = txlUserDsp+"</div></td></tr>"+"</table></div></div></div></div>";
		txlUserDsp =$(txlUserDsp);
		
		/**RTX**/
		if(userCode && userCode == System.getVar('@USER_CODE@')){
			txlUserDsp.find("img.RTX").hide();
		}else{
			txlUserDsp.find("img.RTX").on("load",function(event){
				event = event || window.event;
				RAP($(this).attr("data-name"),event);
			});
		}
		
		txlUserDsp.appendTo(jQuery("#"+id+"-div"));
		
	}
	$(".txl-user-div-dsp").hide();
	
	$dsp = jQuery("#txl-users").find("#"+id+"dsp");
	$dsp.mouseleave(function(){
		$(this).hide();
	});
//	showTimeoutId =  setTimeout(function(){
		$dsp.show();
//	},100);
};
var page = {};
var datas = {};
datas["_SELECT_"]="USER_CODE,USER_NAME,USER_LOGIN_NAME,USER_POST,ODEPT_CODE,DEPT_CODE,DEPT_LEVEL,USER_OFFICE_PHONE,USER_MOBILE,USER_IMG_SRC,USER_EMAIL";

/**
 * 根据总页数及当前页获取下页数据
 * @param pages 总页数
 * @param cur 当前页
 * @param where 查询条件
 */
function loadNextData(pages,cur,where){
	if(pages==null){

		page={};
		datas["_PAGE_"]=page;
    	if($.trim(where).length==0){
    		where = " and ODEPT_CODE='"+System.getVar("@ODEPT_CODE@")+"'";
    	}/*else{
    		datas["_searchWhere"] = " and ODEPT_CODE='"+System.getVar("@ODEPT_CODE@")+"' " + where;
    	}*/
	
		/*datas["_searchWhere"] =  where + " and ODEPT_CODE IN " + System.getVar("@C_HAS_PT_CMPY@");*/
		datas["_searchWhere"] = where;
        users = FireFly.getListData("SY_COMM_ADDRESS_LIST", datas);
        appendUserData(users);
	}else if(cur<=pages){
        page["NOWPAGE"]=cur;
        datas["_PAGE_"]=page;
    	if($.trim(where).length==0){
    		where = " and ODEPT_CODE='"+System.getVar("@ODEPT_CODE@")+"'";
    	}/*else{
    		datas["_searchWhere"] = " and ODEPT_CODE='"+System.getVar("@ODEPT_CODE@")+"' " + where;
    	}*/
		datas["_searchWhere"] =  where + " and S_FLAG='1' and ODEPT_CODE IN " + System.getVar("@C_HAS_PT_CMPY@");
        users = FireFly.getListData("SY_COMM_ADDRESS_LIST", datas);	
        appendUserData(users);
    }else{
    	distroyScorllLoad();
    }
};
/**
 * 滚动条触底加载数据
 */
function initScrollLoad(){

		$(parent.window).scroll(function(){
			var li = parent.jQuery("li.ui-tabs-selected");
			//如果是当前"通讯录"被打开时
			if (li.attr("pretabid") == 'SY_COMM_TEMPL-show-domodelviewpkCodeSC_TXL-tabDiv') {
				var a = parseInt($(parent.document).scrollTop() + $(parent.window).height());
				var b = parseInt($(parent.document).height());
				if(a == b) {
					var cur = parseInt($("#currentPage").attr("value"))+1;
					var pages = parseInt($("#pages").attr("value"));
					var preZm = $("#preZm").attr("value");
					var leftOrg = $("#leftOrg").attr("value");
					var searchText = $("#txl-search")[0].value;
					var where = "";
					if($.trim(preZm).length>0){
						where = where+" and (USER_SPELLING like '"+preZm+"%' or USER_SPELLING like '"+preZm.toLowerCase()+"%')";
					}
					if($.trim(leftOrg).length>0){
						if(leftOrg.indexOf('12^') >= 0) {
							var tempLeftOrg = leftOrg.replace('12^','');
							where = where+" and DEPT_CODE in (select DEPT_CODE from SY_ORG_DEPT where 1=1  and (CMPY_CODE='"+System.getVar("@CMPY_CODE@")+"' )  and CODE_PATH like '"+tempLeftOrg+"%' and ODEPT_CODE = '0001B210000000000BU3')";
						} else {
							where = where+" and DEPT_CODE in (select DEPT_CODE from SY_ORG_DEPT where 1=1  and (CMPY_CODE='"+System.getVar("@CMPY_CODE@")+"' )  and CODE_PATH like '"+leftOrg+"%')";		
						}
//						where = where+" and DEPT_CODE in (select DEPT_CODE from SY_ORG_DEPT_EXT where 1=1  and (CMPY_CODE='"+System.getVar("@CMPY_CODE@")+"' )  and CODE_PATH like '"+leftOrg+"%')";
						//where = where+" and DEPT_CODE in (select DEPT_CODE from SY_ORG_DEPT where 1=1  and (CMPY_CODE='"+System.getVar("@CMPY_CODE@")+"' )  and CODE_PATH like '"+leftOrg+"%')";
					}
					if($.trim(searchText).length>0){
						where = where+" and (USER_SPELLING like '%" + searchText + "%' or USER_NAME like '%" + searchText + "%')";
					}
					setTimeout(function(){
						loadNextData(pages,cur,where);
						$("#currentPage").attr("value",cur);
					},0);
				}
			}
		});
}

/**
 * 数据加载完后销毁滚动加载
 */
function distroyScorllLoad(){
	$(parent.window).unbind();
}


/**
 * 动态追加用户信息数据
 * @param userObjs 用户列表
 */
function appendUserData(userObjs){
    $("#currentPage").attr("value",users._PAGE_.NOWPAGE);
    $("#pages").attr("value",users._PAGE_.PAGES);
	var txlUserDiv = $("#txl-users");//通讯录容器
	var txlUserView = "";//可见用户信息DIV
	var txlUserDsp = "";//隐藏用户信息DIV
	$(userObjs._DATA_).each(function(){
		var userCode = this.USER_CODE;
		var loginName= this.USER_LOGIN_NAME;
		var userName = this.USER_NAME;
		var userPost = this.USER_POST;
		var odeptCode = this.ODEPT_CODE;
		var odeptName = this.ODEPT_CODE__NAME;
		var deptName = this.DEPT_CODE__NAME;
		var email = this.USER_EMAIL;
		var userImg = this.USER_IMG_SRC;
		if(deptName.length>odeptName.length){
			deptName = deptName.substring(odeptName.length+1,deptName.length)
		};
		var phone = this.USER_OFFICE_PHONE;
		var mobile = this.USER_MOBILE;
		var txlUser = $("<div id = '"+userCode+"-div' class='txl-user-div'></div>");
		txlUserView = "<div class='txl-user-container'>"
				+"<div id='"+userCode+"'>"
				+"<img id='photo_"+userImg+"' class='photo' style='width:auto;height:50px;max-width:50px;' src=''/>"
				+"</div>"
				+"<div class='txt-user-name'>"+userName+"</div>"
				+"<div class='txt-user-post'>"+userPost+"</div>"
				+"</div>";
		$(txlUserView).find("#"+userCode).on("mouseenter",function(){
			showUserInfos(userCode,odeptCode);
		}).end()
		  .appendTo(txlUser);
		
		txlUser.appendTo(txlUserDiv);
	});
	placePhoto();
	//计算User内容DIV高度
//	setHei(userObjs._DATA_.length,userObjs._PAGE_.NOWPAGE);
	resetPortalHeight();
};

/**
 * 计算User内容DIV高度
 * @param dataCount 数据量
 * @param nowPage 当前页
 */
function setHei(dataCount,nowPage){
	var hei = (Math.ceil(dataCount/5)+(parseInt(nowPage)-1)*10)*230+50;
	if(hei>970){
		$(parent.document).find("#SY_COMM_TEMPL-show-domodelviewpkCodeSC_TXL-tabFrame").attr("style","position: relative;max-height:"+(hei+120)+"px;height:"+(hei+120)+"px");
	}else{
		$(parent.document).find("#SY_COMM_TEMPL-show-domodelviewpkCodeSC_TXL-tabFrame").attr("style","position: relative;height:1090px");
	}
}

/**
 * 打开导出通讯录页
 */
function openTxl(){
	var params = {};
	var options = {"url":"OA_COMM_ADDRESS_LIST.list.do","tTitle":"公司通讯录","params":params,"menuFlag":3};
	Tab.open(options);
};


/**
 * 发送邮件
 * @param email
 */
function sendEmail(email){
	var data = {};
	var emailInfo = FireFly.doAct('SC_COREMAIL_SERV','getMailInfos',data);
	if(emailInfo.userMailExist=="yes"){
		window.open("http://mail.capitalwater.cn/coremail/XJS/compose/main.jsp?sid="+emailInfo.sessionId+"&to="+email);
	}else{
		alert("您还未完善您的用户邮箱，暂不能发送邮件！");
	}
}

/**
 * 发送手机信息
 * @param phone 手机号
 * @param userLoginName 登录名
 * @param userName 用户名
 */
function sendPhoneMsg(phone,userLoginName,userName){
	var p1 = /^1[3458][0-9]\d{8}$/;
	if (p1.test(phone)){
		var params={"SMS_SENDTOID":userLoginName,"SMS_SENDTONAME":userName};
//		Tab.open({"url":"SC_SMSCENTER.card.do","tTitle":"短信中心","menuFlag":2,"menuId":"0E5CSqu8RN5tbC1poPCbQjd","params":params});
		Tab.open({"url":"SC_SMSCENTER.card.do","tTitle":"短信中心","menuFlag":3,"params":params});
	}else{
		alert("此号码有误，不能发送短信！");
	}
}

