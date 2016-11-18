	GLOBAL.namespace("rh.oa");
	var _viewer = this;
	
	jQuery(".rhGrid-thead-box").hide(); // 隐藏列表头部复选框
	jQuery(".checkTD").hide(); // 隐藏复选框
	jQuery(".tBody-tr").css({
		"height" : "40px"
	}); // 设置列表行宽度为40px
	
	var onRemark = false; // 放在备注列表展现
	var onDialog = false; // 对话框得到焦点
	
	// 给【名称】列绑定鼠标事件
	jQuery("td[icode='NAME']").bind("mouseover", function(event) {// 给【名称】列绑定鼠标over事件
	
		// 设置名称字段显示文字颜色为蓝色
		jQuery(this).parent().find("[icode='NAME']").css({
			"color" : "blue"
		});
	
		var floorId = jQuery(this).parent().find("[icode='FLOOR_ID']").html(); // 获得当前行中数据楼层ID，由于服务调用传递参数
		var imgPath = jQuery(this).parent().find("[icode='IMAGE']").html(); // 获得楼层的图片路径，由于显示图片信心
	
		imgPath = "/file/" + imgPath; // 获得截取过后图片路径
	
		jQuery("#MEET_DIALOG").remove(); // 每次加载Dialog时，要把之前的删除掉
	
		getDialog(event, floorId, imgPath); // 调用打开Dialog方法
		//jQuery(".ui-dialog").animate({left:jQuery("body").width()/2,top:event.clientY  50},1000);
	
		// 隐藏dialog中列表的查询框
		jQuery("#MEET_DIALOG").find("[class='rhGrid-btnBar']").hide();
	
		// 列表高度保持一致
		jQuery(".content").height(jQuery("#MEET_DIALOG_dialogDiv").height());
	
		// 解绑dialog中点击列表头部排序
		jQuery("#MEET_DIALOG").find("[class='rhGrid-thead-th']").unbind("click");
	
		onDialog = false;
		onRemark = false;
	
	}).bind("mouseleave", function(event) { // 为【名称】列添加鼠标leave事件
		jQuery(this).parent().find("[icode='NAME']").css({
			"color" : ""
		}); // 重置【名称】列文字显示颜色样式
	}).bind("click", function(event) {
		var floorId = jQuery(this).parent().find("[icode='FLOOR_ID']").html();
		var floorName = jQuery(this).parent().find("[icode='NAME']").html();
		jQuery("#MEET_DIALOG").remove();
		var newDate = System.getVar("@DATE@");
		
		//modified by wangchen start
		//判断是否是OA_MT_MEETING服务调用的
		if(_viewer.links.callFlag != null && _viewer.links.callFlag == "OA_MT_MEETING"){
			Tab.open({
				"url" : "OA_MT_MEETINGROOM_OCCUPY.list.do",
				"params" : {
					"links":{"callFlag":"OA_MT_MEETING"},//需要传到后台的参数放到里面
					"GET_FLOOR_ID" : floorId,
					"NEW_DATE" : newDate,
					"FLOOR_NAME" : floorName,
					"OA_MT_MEETING_BEGIN_TIME" : _viewer.params.mtData.BEGIN_TIME,//OA_MT_MEETINGROOM服务传递的会议开始时间 add by wangchen
					"OA_MT_MEETING_END_TIME" : _viewer.params.mtData.END_TIME,//OA_MT_MEETINGROOM服务传递的会议结束时间 add by wangchen
					"OA_MT_MEETING_pSid" : _viewer.params.mtData.pSid,//OA_MT_MEETING服务传递的传给本服务的服务ID add by wangchen
					"handler" : {
								"handler_1":_viewer.params.handler,//OA_MT_MEETING文档句柄供本页面回调用 add by wangchen
								"handler_2":_viewer//OA_MT_MEETINGROOM_V服务（本服务）的句柄 add by wangchen
								},
					"_extWhere" : " and FLOOR_ID ='" + floorId + "'"
				},
				"tTitle" : floorName + "会议室占用情况",
				"menuFlag" : 4
			});
		}else{
			Tab.open({
				"url" : "OA_MT_MEETINGROOM_OCCUPY.list.do",
				"params" : {
					"GET_FLOOR_ID" : floorId,
					"NEW_DATE" : newDate,
					"FLOOR_NAME" : floorName,
					"_extWhere" : " and FLOOR_ID ='" + floorId + "'"
				},
				"tTitle" : floorName + "会议室占用情况",
				"menuFlag" : 4
			});
		}
		//modified by wangchen end
		
	});
	
	// 给备注类添加鼠标事件
	jQuery("td[icode='MT_MEETINGROOM_REMARK']").bind("mouseover", function(event) {
		onRemark = true;
		var times = 0; // 初始化计时次数
	
		// 定时器
		var timeInterVal = setInterval(function() {
			times++;
			if (times == 5) {
				if (!onDialog && onRemark) {
					hideDialog();
				}
				times = 0;
				clearInterval(timeInterVal);
			}
		}, 1000);
	});
	
	// 页面加载完毕执行事件-------------------------------------------ready------------------------
	jQuery(document).ready(function() {
	
		// 给页面列表添加鼠标事件
		jQuery(".content-mainCont").bind("click", function(event) {
			onDialog = false;
			onRemark = false;
			jQuery("#MEET_DIALOG").remove();
		});
	});
	
	/*
	 * 构建弹出框页面布局
	 */
	function getDialog(event, floorId, imgPath) {
		var _self = this;
		jQuery("#dialogImg").remove(); // 先将图片移除
		// 构造dialog
		var dialogId = "MEET_DIALOG"; // 设置Dialog的id
		var winDialog = jQuery("<div></div>").attr("id", dialogId).attr("title",
				"会议室楼层分布图及简单介绍");
		winDialog.appendTo(jQuery("body"));
		var bodyWid = jQuery("body").width() / 2;
		var hei = GLOBAL.getDefaultFrameHei() - 100;
		var wid = bodyWid;
		var posArray = [ 30, 30 ];
		if (event) {
			var cx = jQuery("body").width();
			cx = cx / 3;
			var cy = event.clientY;
			posArray[0] = cx;
			posArray[1] = cy - 300;
			/*posArray[0] = event.clientX;
			posArray[1] = event.clientY;*/
		}
		jQuery("#" + dialogId).dialog({
			autoOpen : false,
			height : hei,
			width : wid,
			modal : false,
			show:"blud",
			hide:"blue",
			resizable : false,
			position : posArray,
			open : function() {
	
			},
			close : function() {
				hideDialog();
			}
		});
	
		// 打开dialog
		var dialogObj = jQuery("#" + dialogId);
		dialogObj.dialog("open");
	
		dialogObj.focus();
		jQuery(".ui-dialog-titlebar").last().css("display", "block");// 设置标题显示
		dialogObj.parent()
				.addClass("rh-bottom-right-radius rhSelectWidget-content");
		Tip.showLoad("努力加载中...", null, jQuery(".ui-dialog-title", winDialog)
				.last());
	
		// 追加dialog框上半部图片div
		var divImg = jQuery("<div></div>").css({
			"text-align" : "center",
			"height" : (hei / 5) * 3,
			"padding-top" : "5px"
		}).attr("id", "imgDiv").appendTo(dialogObj);
	
		// 追加列表div
		var divMt = jQuery("<div></div>").attr("id", "MEET_DIALOG_dialogDiv").css({
			"height" : (hei / 5 * 2 - 15),
			"overflow-y" : "auto"
		}).appendTo(dialogObj);
	
		if ("/file/" == imgPath) {
	
			// 没有图片时显示信息
			jQuery("<p>图片未上传</p>").css({
				"font-size" : "16px"
			}).appendTo(divImg);
	
		} else {
	
			// 追加图片img
			var img = jQuery("<img id = 'dialogImg'/>").attr("src", imgPath).attr(
					"height", (hei / 5 * 3) - 25);
			img.appendTo(divImg);
		}
	
		// 让列表父标签适应 divMt 这个div对象
		jQuery(".content").css({
			"height" : (hei / 5) * 2
		});
	
		// 给dialog赋鼠标事件
		jQuery(".ui-dialog").bind("mouseover", function(event) {
			onDialog = true;
		}).bind("mouseleave", function(event) {
			onDialog = false;
		}).bind("click", function(event) {
			onDialog = true;
			jQuery(".ui-dialog").unbind("mouseover").unbind("mouseleave");
		});
	
		// 给列表赋鼠标事件
		jQuery("#MEET_DIALOG_dialogDiv").bind("click", function(event) {
			onDialog = true;
			jQuery(".ui-dialog").unbind("mouseover").unbind("mouseleave");
		});
	
		// 查询列表项
		var invokServ = {
			"sId" : "OA_MT_MEETINGROOM_DIALOG",
			"pCon" : divMt,
			"showTitleBarFlag" : "false",
			"extWhere" : " and FLOOR_ID ='" + floorId + "'",
			"_SELECT_" : "NAME,SEAT_NUM,MAX_CAPACITY,BUILDING_AREA,MR_TYPE,STATUS,PROPERTIES",
			"selectView" : true,
			"_HIDE_":""
		};
	
		// 展示查询列表
		this.listView = new rh.vi.listView(invokServ);
		this.listView.show();
	};
	
	/*
	 * 去除Dialog对话框
	 */
	function hideDialog() {
		jQuery(".ui-dialog").delay(1000).fadeOut("slow");
	}
	
	/*
	 * 展现Dialog对话框
	 */
	function showDialog() {
		jQuery(".ui-dialog").fadeIn("slow");
	}
