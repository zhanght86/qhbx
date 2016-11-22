/**
 * 单条信息展示
 * @param id
 */
function newsView(id){
	var url = "/cms/SY_COMM_INFOS/" + id + ".html";
	if(!checkeKey()){ //如果使用Key登录则进行检查判断
		parent.jQuery("#loginOut").trigger("click");
		return false;
	}
	window.open(url);
};
/**
 * 信息的更多做分页处理[带时间]
 * @param chnl_id
 * @param chnl_name
 */
function openDateListMoreByChnl(chnl_id,chnl_name) {
	var year = new Date().getFullYear();
	
	var params = {};
	params["flag"] = true;
	params["ZT"]="1";
	params["CUR_DATE"] = year;
	var where = " and CHNL_ID = '" +chnl_id+"'";
	var tabOpt = {"url":"SY_COMM_INFOS.getDateMoreInfos.do","params":params,"menuFlag":3,"tTitle":"前海再保险",'nohex':'true'};
	var opts = {"tTitle":chnl_name,"url":"SY_COMM_INFOS.getDateMoreInfos.do?CHNL_ID="+chnl_id+"&CHNL_NAME="+chnl_name+"&CUR_DATE="+year,"menuFlag":3};
	var tabP = jQuery.toJSON(opts);
	tabP = tabP.replace(/\"/g,"'");
	window.open("/sy/comm/page/page.jsp?openTab="+(encodeURIComponent(tabP)));
	
	
	/*var year = new Date().getFullYear();
	var opts = {"tTitle":chnl_name,"url":"SY_COMM_INFOS.getDateMoreInfos.do?CHNL_ID="+chnl_id+"&CHNL_NAME="+encodeURIComponent(chnl_name)+"&CUR_DATE="+year,"menuFlag":3};
	Tab.open(opts);*/
};
function openListMoreByChnlNomal(chnl_id,chnl_name){

	if(chnl_id==undefined||chnl_id==""){
		alert("此栏目下没有信息！")
	}else{
//		var params = {};
//		params["_WHERE_"] = "AND CHNL_ID = '"+chnl_id+"'";
//		var opts = {"tTitle":chnl_name,"url":"SY_COMM_INFOS.list.do","params":params,"menuFlag":3};
//		Tab.open(opts);
		var params = {};
		params["flag"] = true;
		params["_extWhere"] = " and CHNL_ID IN (SELECT CHNL_ID FROM SY_COMM_CMS_CHNL WHERE CHNL_PATH LIKE '%^"+chnl_id+"^%')";
		var tabOpt = {"url":"SY_COMM_INFOS_VIEW.list.do","params":params,"menuFlag":3,"tTitle":chnl_name,"nohex":"true"};
		var tabP = jQuery.toJSON(tabOpt);
		tabP = tabP.replace(/\"/g,"'");
		window.open("/sy/comm/page/page.jsp?openTab="+(tabP)+"&where="+encodeURIComponent(where));
		
	}
}
/**
 * 信息的更多做分页处理[不带时间]
 * @param chnl_id
 * @param chnl_name
 */
function openListMoreByChnl(chnl_id,chnl_name) {

	var opts = {"tTitle":chnl_name,"url":"SY_COMM_INFOS.getMoreInfos.do?CHNL_ID="+chnl_id+"&CHNL_NAME="+chnl_name,"menuFlag":3};
	var tabP = jQuery.toJSON(opts);
	tabP = tabP.replace(/\"/g,"'");
	window.open("/sy/comm/page/page.jsp?openTab="+encodeURIComponent(tabP));
	
};

/**
 * 党群信息的更多做分页处理[不带时间]
 * @param chnl_id
 * @param chnl_name
 */
function openDqListMoreByChnl(chnl_id,chnl_name) {
	var $currentTab = parent.jQuery(".ui-tabs-selected"); 
	
	var portalCode = $currentTab.attr("data-portalcode"),
		portalType = $currentTab.attr("data-portaltype");
	
	var url = "SY_COMM_INFOS.getMoreInfos.do?CHNL_ID="+chnl_id+"&CHNL_NAME="+encodeURIComponent(chnl_name)
	
	url+="&from=iframe&portalCode="+portalCode+"&portalType="+portalType;
	
	var opts = {"tTitle":chnl_name,"url":url,"menuFlag":3};
	Tab.open(opts);
};

//打开"文库"的方法
function openWenkuTab(){
	var menuItemWenku = {"ID":"SY_DOC_CENTER__ruaho","NAME":"文档中心","INFO":"cms/SY_COMM_CMS_CHNL/WENKU_3hWpJcmkZcNHarHMgIvMm2/index.html","MENU":"3"};
	Menu.linkNodeClick(menuItemWenku);
}

//打开"知道"的方法
function openZhidaoTab(){
//	var menuItemZhidao = {"ID":"SY_COMM_ZHIDAO_INDEX__ruaho","NAME":"知道","INFO":"cms/SY_COMM_CMS_TMPL/1oRlDxWJN35Ug4r6wwDglu.html","MENU":"3"};
//	Menu.linkNodeClick(menuItemZhidao);
	var tabLis = parent.jQuery("#homeTabs").find(".ui-state-default");
	//模拟点击事件
	jQuery(tabLis[2]).children("a").children("span").trigger("click");
}

//"知道"的违禁词管理
function zhidaoKeyword(){
	var keywords=FireFly.getListData("SY_COMM_KEYWORD",{"_NOPAGE_":true})._DATA_;
	if(keywords){
		if(keywords.length>0) {
			var keywordStr="";
			for(var i=0;i<keywords.length;i++) {
				keywordStr	+= " "+keywords[i]["KEYWORD_NAME"];
			}
			keywordStr = encodeURIComponent(keywordStr);
			var param={"KEYWORDS":keywordStr,"FILTER":[{"id":"service","data":"SY_COMM_ZHIDAO_QUESTION"}]};
			var opt={"sId":"SEARCH-RES","tTitle":"搜索","url":"/SY_PLUG_SEARCH.query.do?data="+jQuery.toJSON(param)+"&ie=UTF-8","menuFlag":3,"scrollFlag":true};
			Tab.open(opt)
		}
	}
}

//"文库"的违禁词管理
function wenkuKeyword(){
	var keywords=FireFly.getListData("SY_COMM_KEYWORD",{"_NOPAGE_":true})._DATA_;
	if(keywords){
		if(keywords.length>0){
			var value="";
			for(var i=0;i<keywords.length;i++){
				value+=" "+keywords[i]["KEYWORD_NAME"];
			}
			value = encodeURIComponent(value);
			var param={"KEYWORDS":value,"FILTER":[{"id":"service","data":"SY_COMM_WENKU_DOCUMENT"}]};
			var opts={"sId":"SEARCH-RES","tTitle":"搜索","url":"/SY_PLUG_SEARCH.query.do?data="+jQuery.toJSON(param)+"&ie=UTF-8","menuFlag":3,"scrollFlag":true};
			Tab.open(opts);
		}
	}
}


/**
 * 发送邮件(SC CoreMail)
 * @param coreMail
 */
function sendEmail(coreMail){
	/*var data = {};
	var emailInfo = FireFly.doAct('SC_COREMAIL_SERV','getMailInfos',data);
	if(emailInfo.userMailExist=="yes"){
		window.open("http://mail.capitalwater.cn/coremail/XJS/compose/main.jsp?sid="+emailInfo.sessionId+"&to="+coreMail);
	}else{
		alert("该用户邮箱信息有误，暂不能发送邮件！");
	}*/
}

/**
 * 发送手机信息
 * @param phone 手机号
 * @param userLoginName 登录名
 * @param userName 用户名
 */
function sendSmsMsg(userId,userName,userMobile){
	var p1 = /^1[3458][0-9]\d{8}$/;
	if (p1.test(userMobile)){
		var params={"SMS_SENDTOID":userId,"SMS_SENDTONAME":userName};
//		Tab.open({"url":"SC_SMSCENTER.card.do","tTitle":"短信中心","menuFlag":2,"menuId":"0E5CSqu8RN5tbC1poPCbQjd","params":params});
		Tab.open({"url":"SC_SMSCENTER.card.do","tTitle":"短信中心","menuFlag":3,"params":params});
	}else{
		alert("此号码有误，不能发送短信！");
	}
}

/**
 * 判断是否插入U-KEY
 */
function checkUKey() {
	try{
		if (SafeCtrl.USB_OpenDevice(0)) {
			SafeCtrl.USB_CloseDevice();
			//alert("未检测到智能卡！");
			return false;
		} else {
			return true;
		}
	} catch (e) {
		try{
			if (parent.SafeCtrl.USB_OpenDevice(0)) {
				parent.SafeCtrl.USB_CloseDevice();
				//alert("未检测到智能卡！");
				return false;
			} else {
				return true;
			}
		} catch (e) {
			//alert("认证控件没有正确安装，请下载安装后再试！");
			return false;
		}
	}
};

function checkeKey() {
	// 暂时添加一个系统参数，来屏蔽掉UKey的验证
	try {
		if (System.getVar("@C_SC_NO_CHECK_KEY_USERS@").indexOf("ALL") >= 0
				|| System.getVar("@C_SC_NO_CHECK_KEY_USERS@").indexOf(
						System.getVar("@USER_CODE@")) >= 0) {
			return true;
		}
	} catch (e) {
		try {
			if (parent.System.getVar("@C_SC_NO_CHECK_KEY_USERS@")
					.indexOf("ALL") >= 0
					|| parent.System.getVar("@C_SC_NO_CHECK_KEY_USERS@")
							.indexOf(parent.System.getVar("@USER_CODE@")) >= 0) {
				return true;
			}
		} catch (e) {

		}
	}
	var userPin = System.getVar("@USER_PIN@");
	var certDir = System.getVar("@USER_CERTDIR@");
	if (userPin != "") {
		if (certDir != "") {
			return true;
		}
		var baseCode = new JavaScriptBase64;
		baseCode.JavaScriptBase64("");
		baseCode.string = userPin;
		plainUserPIN = baseCode.decode();

		try {
			if (SafeCtrl.USB_OpenDevice(0)) {
				SafeCtrl.USB_CloseDevice();
				alert("UKey已被拔出！系统将返回登录页面，请您插入UKey后重新登录！！");
				return false;
			}
			if (SafeCtrl.USB_VerifyPin(1, plainUserPIN) != 0) {
				SafeCtrl.USB_CloseDevice();
				alert("错误的密钥！");
				return false;
			}
			SafeCtrl.USB_CloseDevice();
		} catch (e) {
			try {
				if (parent.SafeCtrl.USB_OpenDevice(0)) {
					parent.SafeCtrl.USB_CloseDevice();
					alert("UKey已被拔出！系统将返回登录页面，请您插入UKey后重新登录！！");
					return false;
				}
				if (parent.SafeCtrl.USB_VerifyPin(1, plainUserPIN) != 0) {
					parent.SafeCtrl.USB_CloseDevice();
					alert("错误的密钥！");
					return false;
				}
				parent.SafeCtrl.USB_CloseDevice();
			} catch (e) {
				alert("客户端没有正确安装，请下载安装后再试！");
				return false;
			}
		}
	}

	return true;
}

/**
 * 积分变化提示信息方法
 * @param msg - 提示信息
 */
function scoreMsg (msg) {
//	var msg = "发布主题&nbsp;&nbsp;积分+10";
	if (msg != 'isAdmin') {
		var $scoreMask = parent.$("#score-mask");
		var scrollTop = parent.Mouse.getScrollTop();
		var posiTop = parseInt(scrollTop) + 235;
		$scoreMask.css("top",posiTop+"px");
		$scoreMask.fadeIn(300).html(msg).fadeOut(5000);
	}
}
/**
 * 论坛说明
 */
function bbsExplain () {
	var userCode = System.getVar('@USER_CODE@');
	// 去查询数据库有无此人记录
	var param = {};
	param['_WHERE_'] = ' and USER_ID = ' + userCode;
	var okCount = FireFly.doAct('SC_BBS_EXPLAIN','finds',param)._OKCOUNT_;
	
	if (okCount == 0) { // TODO 如果没人
		// 显示论坛须知
		if (top.$('#bbs-shadow') || top.$('#bbs-content')) {
			top.$('#bbs-shadow').remove();
			top.$('#bbs-content').remove();
		}
		var $scoreMask = top.$('#score-mask');
		var innerHtml = '<div id="bbs-shadow"></div><div id="bbs-content"><div id="bbs-top"><span class="title">员工园地论坛须知</span></div><div id="bbs-middle"><div class="subject">';
		innerHtml += '<p style="line-height: 1.5em; text-indent: 2em;"><span style="color: rgb(0, 0, 0); font-size: 12px;">“员工园地”为公司员工提供了一个谏言献策、思想交流、信息共享的平台，公司所有有权限的员工均可通过“员工园地”发布和分享工作、生活经验与知识，进行思想交流和信息共享，提出合理化建议或意见，谏言献策等。员工使用“员工园地”栏目应遵守以下服务条款：</span></p><p style="line-height: 1.5em; text-indent: 2em;"><span style="color: rgb(0, 0, 0); font-size: 12px;">1、公司拥有对“员工园地”一切活动的监督、审核、提示、检查、纠正及处罚等权利。员工阅读本服务条款并点击"同意"按钮，即表示员工自愿接受本服务条款的所有内容。如果员工不同意本服务条款，则不能获得使用“员工园地”服务的权利。</span></p><p style="line-height: 1.5em; text-indent: 2em;"><span style="color: rgb(0, 0, 0); font-size: 12px;">2、员工承诺发表言论要:爱国、守法、自律、真实、文明。不得发布、转载、传送任何非法的、骚扰性的、中伤他人的、辱骂性的、恐吓性的、伤害性的、庸俗的、淫秽的、危害国家安全的、泄露国家或公司机密的、破坏国家宗教政策和民族团结的、违反公司规章制度的、其它违反法律法规及政策的内容。</span></p><p style="line-height: 1.5em; text-indent: 2em;"><span style="color: rgb(0, 0, 0); font-size: 12px;">3、禁止利用“员工园地”恶意散布虚假信息，禁止发布广告等以商业盈利为目的信息。</span></p><p style="line-height: 1.5em; text-indent: 2em;"><span style="color: rgb(0, 0, 0); font-size: 12px;">4、不得利用“员工园地”进行任何不利于公司的行为。</span></p><p style="line-height: 1.5em; text-indent: 2em;"><span style="color: rgb(0, 0, 0); font-size: 12px;">5、不得盗取他人帐号或以他人名义发布言论。</span></p><p style="line-height: 1.5em; text-indent: 2em;"><span style="color: rgb(0, 0, 0); font-size: 12px;">6、员工应保证发表或者上传于“员工园地”的所有信息（文字、图片、图形、音频和/&nbsp;或视频等）不侵犯第三人的合法权益。员工对其所提供信息因形式、内容及授权的不完善、不合法所造成的一切后果承担完全责任。</span></p><p style="line-height: 1.5em; text-indent: 2em;"><span style="color: rgb(0, 0, 0); font-size: 12px;">7、发帖时应针对不同的帖子内容选择适当的板块，请不要选择与帖子内容不符的板块，如果所选择板块与内容不符，论坛管理员有权进行修改、删除等操作。</span></p><p style="line-height: 1.5em; text-indent: 2em;"><span style="color: rgb(0, 0, 0); font-size: 12px;">8、禁止一贴多发，一帖多发包括在不同板块发布内容相似的帖子及在同一版块发布内容相似的帖子两种情况；禁止在同一帖内反复拷贝重复内容；禁止短时间内同一人发多帖刷屏，此类情况由论坛管理员视具体情况进行相应处理。</span></p><p style="line-height: 1.5em; text-indent: 2em;"><span style="color: rgb(0, 0, 0); font-size: 12px;">9、员工从“员工园地”中获得的涉及公司利益信息，未经许可，不得任意对外传播、复制或转载。</span></p><p style="line-height: 1.5em; text-indent: 2em;"><span style="color: rgb(0, 0, 0); font-size: 12px;">10、员工对其发表、上传或传送的内容负责，对于违法使用“员工园地”而引起的一切责任，由员工负全部责任。</span></p><p style="line-height: 1.5em; text-indent: 2em;"><span style="color: rgb(0, 0, 0); font-size: 12px;">11、员工如有违反上述任何规定，公司有权要求员工改正或直接采取一切必要的措施(包括但不限于删除有关内容、暂停或终止员工使用“员工园地”的权利)以减轻员工不当行为而造成的影响，并保留追究其责任的权利。</span></p>';
		innerHtml += '</div></div><div id="bbs-bottom"><span class="btn-group"><span class="btn bbs-yes-btn">同意</span><span class="btn bbs-no-btn">不同意</span></span></div></div>';
		$scoreMask.after(innerHtml);
		
		// 去掉滚动条
		top.$('html,body').animate({scrollTop:0},0);
		top.$("body").css("overflow-y","hidden");
		
		// 绑定点击事件
		var yesBtn = top.$('#bbs-content').find('.bbs-yes-btn');
		var noBtn = top.$('#bbs-content').find('.bbs-no-btn');
		$(yesBtn).off('click').on('click',function () {
			top.$('body').css('overflow-y','auto');
			// 移除所有阴影
			top.$('#bbs-shadow').remove();
			top.$('#bbs-content').remove();
			// 向后台添加数据
			FireFly.doAct('SC_BBS_EXPLAIN','save',{'USER_ID':userCode});
		});
		$(noBtn).off('click').on('click',function () {
			top.$('body').css('overflow-y','auto');
			// 跳回首页
			$($(top.$('#homeTabs').find('.tabUL').find('li')[0]).find('a')).trigger('click');
			// 移除所有阴影
			top.$('#bbs-shadow').remove();
			top.$('#bbs-content').remove();
		});
	}
};
