/** 网页实时通讯页面渲染引擎 */
GLOBAL.namespace("rh.vi");
var rhImFunc = {
	jid : "", // 登陆用户
	token : "", // 登陆用户身份标识
	currentChat : "", // 当前目标聊天用户
	domain : "", // 通讯服务器domain
	friendArray: null,//好友列表cache
	enterCookie: "rh_enter_" + System.getUser("USER_CODE"),//记录发送消息的回车或者ctrl+enter
	flickerUser: {},//新消息用户
	connection : null,
	offline : "wbim_status_offline",
	online : "wbim_status_online",
	jid_to_id : function(jid) {
		return Strophe.getBareJidFromJid(jid).replace("@", "-").replace(".",
				"-");
	},
	showChatArea: function(data) {
		data["id"] = data["id"] + "@rhim.server";
		data["jid"] = data["id"] + "-rhim-server";
		jQuery(document).data("imObj")._showChatArea(data);
	},
	/**
	 * 消息服务器断开，清空用户列表
	 */
	wbimDisconnect : function() {
		if (rhImFunc.connection == null) {
			return;
		}
		jQuery(".wbim_tit_lf .tit .rh_wbim_line_status").addClass("wbim_status_offline");
		jQuery(".wbim_tit_lf .tit .txt").text("离线");
		jQuery("#rh_list_group_myfriend").empty().parent().find(".friendCount").text(0);
		jQuery("#rh_list_group_alluser").empty().parent().find(".friendCount").text(0);
		jQuery(".wbim_min_friend .statusbox span").removeClass("wbim_status_online").addClass("wbim_status_offline");
		jQuery(".wbim_min_friend .wbim_online_count").text(0);
		jQuery(document).data("imObj")._disconnected();
	},

	/**
	 * 获取好友列表
	 */
	getFriends : function() {
		var iq = $iq({
			type : 'get'
		}).c('query', {
			xmlns : 'jabber:iq:roster'
		});
		rhImFunc.connection.sendIQ(iq, rhImFunc.on_friend);// 获取列表
	},

	/**
	 * 好友列表callback
	 */
	on_friend : function(iq) {
		$(iq).find('item').each(function() {
			var jid = $(this).attr('jid');
			var name = $(this).attr('name') || jid;
			var jid_id = rhImFunc.jid_to_id(jid);
			var contact = {
				"id" : jid_id,
				"name" : name,
				"jid" : jid
			};
			rhImFunc.insert_friend(contact);
		});

	},

	/**
	 * 获取最近联系人
	 */
	getRcontacts : function() {
		rhImFunc.connection.recentcontact.listContacts("",
				rhImFunc.on_Rcontacts);
	},

	/**
	 * 最近联系人callback
	 * 
	 */
	on_Rcontacts : function(collections, responseRsm) {
		// get recent contacts
		for ( var i = 0; i < collections.length; i++) {
			var item = collections[i];
			var jid = item.jid;
			var name = item.name;
			var jid_id = rhImFunc.jid_to_id(jid);
			
			var contact = {
				"id" : jid_id,
				"name" : name,
				"jid" : jid
			};
			rhImFunc.insert_contact(contact);
		}	
	},

	/**
	 * 获取聊天记录
	 */
	getHisMsg : function(jid, callback, beforeId) {
		var tempBeforeId = 0;
		if (beforeId) {
			tempBeforeId = beforeId;
		}
		if (rhImFunc.connection && rhImFunc.connection.hismsg) {
			rhImFunc.connection.hismsg.listHisMsgs(jid, "", callback, tempBeforeId);
		}
	},
	/**
	 * 聊天记录callback
	 */
	on_hisMsgs : function(collections, responseRsm) {
		// get last history message
		for ( var i = 0; i < collections.length; i++) {
			var msg = collections[i];
			var timestamp = msg.timestamp;
			var from = msg.from;
			var to = msg.to;
			var body = msg.body;
		}
	},

	/**
	 * 发送信息
	 */
	sendMsg : function(to, body) {
		var message = $msg({
			to : to,
			"type" : "chat"
		}).c('body').t(body).up().c('active', {
			xmlns : "http://jabber.org/protocol/chatstates"
		});
		rhImFunc.connection.send(message);
		// write into message history
		rhImFunc.insert_hismsg(rhImFunc.jid, to, body, '');
		rhImFunc.scroll_chat();
	},
	startComposing : function(jid) {
		if (rhImFunc.connection.send) {
		} else {
			jQuery(document).data("imObj")._connect();
			if (rhImFunc.connection == null) {
				alert("startComposing：消息服务器连接不上！");
			}
		}
		var notify = $msg({to: jid, "type": "chat"}).c('composing', {xmlns: "http://jabber.org/protocol/chatstates"});
		rhImFunc.connection.send(notify);
	},
	endComposing : function(jid) {
		//alert("end==" + rhImFunc.connection.send);
		if (rhImFunc.connection.send) {
		} else {
			jQuery(document).data("imObj")._connect();
			if (rhImFunc.connection == null) {
				alert("startComposing：消息服务器连接不上！");
			}
		}
       var notify = $msg({to: jid, "type": "chat"}).c('paused', {xmlns: "http://jabber.org/protocol/chatstates"});
       rhImFunc.connection.send(notify);
	},
	pending_subscriber : null,
	on_presence : function(presence) {
		var ptype = $(presence).attr('type');
		var from = $(presence).attr('from');
		var jid = Strophe.getBareJidFromJid(from);
		var jid_id = rhImFunc.jid_to_id(jid);
		if (ptype === 'subscribe') {
			// populate pending_subscriber, the approve-jid span, and
			// open the dialog
			rhImFunc.pending_subscriber = from;
			$('#approve-jid').text(Strophe.getBareJidFromJid(from));
		} else if (ptype !== 'error') {
			var state = "offline";
			if (ptype === 'unavailable') {
				state = "offline";
			} else if (!ptype) {
				state = "online";
			} else {
				var show = $(presence).find("show").text();
				if (show === "" || show === "chat") {
					state = "online";
				} else {
					state = "away";
				}
			}
			// alert("res:" + from + "\t ptype:" + ptype + "\t state:" + state);
			//更新最近联系人状态
				var userState = {
					'id' : jid_id,
					'jid' : jid,
					'res' : from,
					'state' : state
				};// 状态参数
				//更新最近联系人状态
				rhImFunc.update_contactStatus(userState);
				//更新好友状态
				rhImFunc.update_friendStatus(userState);
		}

		// reset addressing for user since their presence changed
		var jid_id = rhImFunc.jid_to_id(from);
		$('#chat-' + jid_id).data('jid', Strophe.getBareJidFromJid(from));
		return true;
	},
	on_friend_changed : function(iq) {
		$(iq).find('item').each(function() {
			// alert("on_friend_changed");

			// var sub = $(this).attr('subscription');
			// var jid = $(this).attr('jid');
			// var name = $(this).attr('name') || jid;
			// var jid_id = rhImFunc.jid_to_id(jid);
			//
			// if (sub === 'remove') {
			// // contact is being removed
			// $('#' + jid_id).remove();
			// } else {
			// // contact is being added or modified
			// var contact_html = "<li id='" + jid_id + "'>" +
			// "<div class='" +
			// ($('#' + jid_id).attr('class') || "roster-contact offline") +
			// "'>" +
			// "<div class='roster-name'>" +
			// name +
			// "</div><div class='roster-jid'>" +
			// jid +
			// "</div></div></li>";
			// if ($('#' + jid_id).length > 0) {
			// $('#' + jid_id).replaceWith(contact_html);
			// } else {
			// // rhImFunc.insert_contact(contact_html);
			// }
			// }
		});
		return true;
	},
	on_message : function(message) {
		var full_jid = $(message).attr('from');
		var toId = $(message).attr('to');
		var jid = Strophe.getBareJidFromJid(full_jid);
		var jid_id = rhImFunc.jid_to_id(jid);

		var composing = $(message).find('composing');
		if (composing.length > 0) {
			jQuery(".wbim_single_user .bringin").show();
			return true;
		}
		var paused = $(message).find('paused');
		if (paused.length > 0) {
			jQuery(".wbim_single_user .bringin").hide();
			return true;
		}
		// 用户是否存在
		if (!rhImFunc.exits_contact(jid)) {
			var userName = rhImFunc.getUserNameFromServer(jid);
			// 添加用户 
			rhImFunc.new_contact(jid, userName);
		}
		var user = jQuery(".rh_wbim_friend_list[nodeid='" + jid_id + "']");
		var name = user.find(".wbim_username").html();
		// 获取消息闪烁
		var tempState = user.find(".wbim_status").data("state");
		var temp = {
				"id" : jid_id,
				"jid" : jid,
				"name" : name,
				"state" : tempState
		};
		rhImFunc.msg_flicker(temp);
		//获取消息内容
		var body = $(message).find('html > body');
        if (body.length === 0) {
            body = $(message).find('body');
            if (body.length > 0) {
                body = body.text()
            } else {
                body = null;
            }
        } else {
            body = body.contents();
        }
        if (body) {
			var timestamp = '';
			var from = jid;
			var to = toId;
			var body = body;
			rhImFunc.insert_hismsg(from, to, body, timestamp,from);
			rhImFunc.scroll_chat();
        }
		return true;
	},
	msg_flicker : function(data) {
		var id = data.id;
		var jid = data.jid;
		rhImFunc.flickerUser[jid] = "true";//存储新消息用户
		var obj = jQuery(document).data("imObj");
		if (obj.imChatBox.css("display") == "block") {//当前为最大化显示
			//obj._cancelMinFlicker();
			if (jid == rhImFunc.currentChat) {
				delete rhImFunc.flickerUser[jid];
			} else {
				jQuery(".rh_chatLeftLi[userid='" + id + "']").addClass("wbim_highlight");
			}
		} else if (obj.imChatBox.css("display") == "none") {
			jQuery(".wbim_min_chat").addClass("wbim_min_chat_msg");
			obj._showMinChatArea(data);
		}
	},
	scroll_chat : function() {
		document.getElementById('wbim_chat_list').scrollTop = document.getElementById('wbim_chat_list').scrollHeight;
	},
	presence_value : function(elem) {
		if (elem.hasClass('online')) {
			return 2;
		} else if (elem.hasClass('away')) {
			return 1;
		}
		return 0;
	},

	/**
	 * 日期格式化
	 * 
	 * @param dateobje -
	 *            Date
	 * @param fmt -
	 *            format expresstion
	 * 
	 * example: ("yyyy-MM-dd hh:mm:ss.S") ==> 2006-07-02 08:09:04.423 ("yyyy-M-d
	 * h:m:s.S") ==> 2006-7-2 8:9:4.18
	 * 
	 * 
	 */
	dateFormat : function(dateobj, fmt) { // author: meizz
		var o = {
			"M+" : dateobj.getMonth() + 1, // 月份
			"d+" : dateobj.getDate(), // 日
			"h+" : dateobj.getHours(), // 小时
			"m+" : dateobj.getMinutes(), // 分
			"s+" : dateobj.getSeconds(), // 秒
			"q+" : Math.floor((dateobj.getMonth() + 3) / 3), // 季度
			"S" : dateobj.getMilliseconds()
		// 毫秒
		};
		if (/(y+)/.test(fmt))
			fmt = fmt.replace(RegExp.$1, (dateobj.getFullYear() + "")
					.substr(4 - RegExp.$1.length));
		for ( var k in o)
			if (new RegExp("(" + k + ")").test(fmt))
				fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k])
						: (("00" + o[k]).substr(("" + o[k]).length)));
		return fmt;
	},
	
	getUserNameFromServer : function(jid) {
		var userName = "";
		var userCode = rhImFunc.getUsercode(jid);
		var param ={};
		param["_PK_"] =  userCode;
		var userBean = FireFly.doAct("SY_ORG_USER", "byid", param);
		if (userBean &&  userBean.USER_NAME) {
		userName = userBean.USER_NAME;
		}
		return userName;
	},
	getUsercode : function(jid) { 
		var index = jid.indexOf("@");
		if (-1 < index) {
			return jid.substring(0,index);
		}
		return jid;
	},
	//from:来自，to：发向，body：内容，teimestamp:时间，aimUser:对面聊天人，msgId:消息主键，beforeId:某条消息之前
	insert_hismsg : function(from, to, body, timestamp, aimUser, msgId, beforeId) {
		var toId = rhImFunc.currentChat;
		if (aimUser) {
			toId = aimUser;
		}
		var obj = jQuery(".rh_chat_dl[touser='" + toId + "']");
		if (obj.length == 0) {
			jQuery(".rh_chat_dl").hide();
			//+ "<div id='wbim_chat_list' class='wbim_chat_list' style='height: 198px; top: 24px;'></div>"
			obj = jQuery("<dl class='rh_chat_dl' style='display: block;'></dl>").attr("touser",toId).appendTo(jQuery("#wbim_chat_list"));
		} else if (jQuery(".rh_chat_dl[touser='" + toId + "']:hidden").length == 1) {
//			jQuery(".rh_chat_dl").hide();
//			obj.show();
		}
		//timestamp build
		if (timestamp) {
			var newDate = new Date();
			newDate.setTime(timestamp);
			timestamp = rhImFunc.dateFormat(newDate, "MM-dd hh:mm");
		} else {
			var newDate = new Date();
			timestamp = rhImFunc.dateFormat(newDate, "MM-dd hh:mm");
		}
		// file message?
		if (0 == body.indexOf('${rh.file}')) {
			var fileUrl = body.substring(10);
			var fileName = body.substring(body.indexOf("?fileName=") + 10);
			body = "<a href='" + fileUrl + "'>" + fileName + "</a>";
		}
		//chat record append
		var hismsgHtml = "";
		if (to == rhImFunc.jid) {
			hismsgHtml += "		<dd class='rh_wbim_msg_dd wbim_msgl'>"
					+ "			<div class='wbim_msgpos'>"
					+ "				<div class='msg_time'>" + timestamp + "</div>"
					+ "				<div class='msg_box'>" + "					<p class='txt'>"
					+ body + "</p>" + "				</div>"
					+ "				<div class='msg_arr'></div>" + "			</div>"
					+ "		</dd>";
		} else {
			hismsgHtml += "		<dd class='rh_wbim_msg_dd wbim_msgr'>"
					+ "			<div class='wbim_msgpos'>"
					+ "				<div class='msg_time'>" + timestamp + "</div>"
					+ "				<div class='msg_box'>" + "					<p class='txt'>"
					+ body + "</p>" + "				</div>"
					+ "				<div class='msg_arr'></div>" + "			</div>"
					+ "		</dd>";
		}
		// 聊天记录展示
		if (beforeId) {
			jQuery(hismsgHtml).attr("msgid",msgId).insertBefore(jQuery(".rh_wbim_msg_dd[msgid='" + beforeId + "']"));
		} else {
			if (msgId) {
				obj.append(jQuery(hismsgHtml).attr("msgid",msgId));
			} else {
				obj.append(jQuery(hismsgHtml));
			}
		}
	},
	clean_hismsg : function(from, to, body, timestamp) {
//		var hm = jQuery("#hismsg");
//		hm.html("");
	},
	add_hismsg_line : function(obj) {
		var lineHtml = jQuery("<dd class='line'></dd>");
		if (obj) {
			lineHtml.insertBefore(obj);
		} else {
			jQuery("#wbim_chat_list .rh_chat_dl:visible").append(lineHtml);
		}
	},

	/**
	 * 新增联系人，并与其进行聊天， (如果该联系人目前存在则忽略)
	 */
	new_contact : function(jid, name) {
		// 检查目标联系人是否存在,如果存在则返回
		if (rhImFunc.exits_contact(jid)) {
			return;
		}
		// 插入用户
		var jid_id = rhImFunc.jid_to_id(jid);
		var contact = {
			"id" : jid_id,
			"name" : name,
			"jid" : jid
		};
		rhImFunc.insert_contact(contact);
		
		//更新联系人状态
		var friend = rhImFunc.get_local_friend(jid);
		if (friend) {
			rhImFunc.update_contactStatus(friend);
		}
		rhImFunc.fillOnlineCount();
		// 关注该用户
		var data = {
			jid : jid,
			name : ''
		};
		rhImFunc.subscribe(data);
	},

	/**
	 * 关注联系人、关注后将及时得到该联系人的上线、下线等状态
	 */
	subscribe : function(data) {
		var iq = $iq({
			type : "set"
		}).c("query", {
			xmlns : "jabber:iq:roster"
		}).c("item", data);
		rhImFunc.connection.sendIQ(iq);

		var subscribe = $pres({
			to : data.jid,
			"type" : "subscribe"
		}).c("priority", "0");
		rhImFunc.connection.send(subscribe);
	},

	/**
	 * 目标联系人是否存在
	 * 
	 */
	exits_contact : function(jid) {
		var jid_id = rhImFunc.jid_to_id(jid);
		// 检查目标联系人是否存在,如果存在则返回
		var user = jQuery(".rh_wbim_friend_list[nodeid='" + jid_id + "']");
		if (user.attr("jid")) {
			return true;
		} else {
			return false;
		}
	},
	insert_contact : function(elem) {
		var id = elem.id || '';
		var jid = elem.jid || '';
		var name = elem.name || '';
		var state = elem.state || '';
		var res = elem.res || '';
		var pres = "";
		
		if (rhImFunc.exits_contact(jid)) {
			alert("contact is exits, can not new one!");
			return;
		}
		
		if (!name) {
			name = rhImFunc.getUserNameFromServer(jid);
		}
		
			// 插入好友(无状态)
			var obj = jQuery("<li></li>").addClass("rh_wbim_friend_list").attr(
					"nodeid", id).attr("jid", jid).attr("res", "");
			var userHead = jQuery("<div class='wbim_userhead'></div>")
					.appendTo(obj);
			jQuery("<img src='http://tp2.sinaimg.cn/1997392765/30/0/1'>")
					.appendTo(userHead);
			jQuery(
					"<span style='display:none;' class='wbim_icon_msg_s'></span>")
					.appendTo(userHead);
			jQuery("<span class='wbim_status'></span>").addClass(
					rhImFunc.offline).data("state", "offline").appendTo(
					userHead);
			var userName = jQuery("<div class='wbim_username'></div>").text(
					name).appendTo(obj);
			// 选择好友进行聊天
			obj.bind("click",
					function(event) {
						var obj = jQuery(document).data("imObj");
						var tempState = jQuery(this).find(".wbim_status").data(
								"state");
						var temp = {
							"id" : id,
							"jid" : jid,
							"name" : name,
							"state" : tempState
						};
						obj._showChatArea(temp);
					});
			var friendObj = jQuery("#rh_list_group_myfriend");
			friendObj.append(obj);
			var count = friendObj.find(".rh_wbim_friend_list").length;
			friendObj.parent().find(".friendCount").text(count);
	},
	update_contactStatus : function(elem) {
		var id = elem.id || '';
		var jid = elem.jid || '';
		var name = elem.name || '';
		var state = elem.state || '';
		var res = elem.res || '';
		var pres = "";
		
		//更新用户名
		if (name) {
			user.find(".wbim_username").val(name);
		}
		
		if (!state) {
		//	alert("state can not be null!");
			return;
		}

		if (!rhImFunc.exits_contact(jid)) {
		//	alert("contact is null, can not update state");
			return;
		}
		
		var temp = "wbim_status_";
		var user = jQuery(".rh_wbim_friend_list[nodeid='" + id + "']");
		if (state == "offline") {// 离线
			// 如果客户端列表为空,状态才视为离线。
			var resources = "";
			if (user.attr("res")) {
				resources = user.attr("res");
			}
			var resArray = resources.split(",");
			var newResArray = [];
			var newResStr = "";
			// 从客户端列表中移除已下线的客户端
			for ( var i = 0; i < resArray.length; i++) {
				if (resArray[i] && res != resArray[i]) {
					newResArray.push(resArray[i]);
					newResStr += resArray[i] + ",";
				}
			}
			user.attr("res", newResStr);
			if (0 < newResArray.length) {
				return;
			}
			temp += "offline";
			user.find(".wbim_status").removeClass(rhImFunc.online).data(
					"state", state).addClass(temp);
		} else if (state == "away") {// 离开
			temp += "offline";
			user.find(".wbim_status").removeClass(rhImFunc.online).data(
					"state", state).addClass(temp);
		} else if (state == "online") {// 在线
			temp += "online";
			user.find(".wbim_status").removeClass(rhImFunc.offline).data(
					"state", state).addClass(temp);
			// 更新客户端列表
			var resources = user.attr("res") + "," + res;
			user.attr("res", resources);
			rhImFunc.fillOnlineCount();
		}
	},
	fillOnlineCount:function() {
		var obj = jQuery(document).data("imObj");
		obj._fillOnlineUserCount();
	},
	insert_friend : function(elem) {
		var id = elem.id || '';
		var jid = elem.jid || '';
		var name = elem.name || '';
		var state = elem.state || '';
		var res = elem.res || '';
		var pres = "";
		
		var friend = {
				"id" : id,
				"jid" : jid,
				"name" : name,
				"state" : state
			};
		rhImFunc.friendArray.push(friend);
	},
	
	get_local_friend : function(jid) {
		for ( var i = 0; i < rhImFunc.friendArray.length; i++) {
			var friend = rhImFunc.friendArray[i];
			if (friend.jid == jid) {
				return friend;
			}
		}
		
		return null;
	},

	update_friendStatus : function(elem) {
		var id = elem.id || '';
		var jid = elem.jid || '';
		var name = elem.name || '';
		var state = elem.state || '';
		var res = elem.res || '';
		var pres = "";

		//如果该好友存在，进行更新
		for ( var i = 0; i < rhImFunc.friendArray.length; i++) {
			var friend = rhImFunc.friendArray[i];
			if (friend.jid == jid) {
				if (state) {
				friend.state = state;
				}
				if (name) {
				friend.name = name;
				}
				return;
			}
		}
		
		//如果该好友不存在，新建
		rhImFunc.insert_friend(elem);
	}

};
/*
 * 待解决问题：
 * 
 */
rh.vi.wbimView = function(options) {
	var defaults = {
		"id" : options.id + "-wbimView",
		"positon:" : "bottom",
		"jid" : options.jid,
		"token" : options.token,
		"domain" : options.domain
	};
	rhImFunc.jid = options.jid;
	rhImFunc.token = options.token;
	rhImFunc.domain = options.domain;
	rhImFunc.friendArray = [];

	this.opts = jQuery.extend(defaults, options);
	this._pkCode = this.opts.pkCode;
};
/*
 * 渲染主方法
 */
rh.vi.wbimView.prototype.show = function() {
	var _self = this;
	this._initMainData();// 初始化数据
	this._layout(); // 构建布局
	this._afterLoad();
	// 查找用户demo
	var searchObj = jQuery("#searchUser");

};

/*
 * 准备数据
 */
rh.vi.wbimView.prototype._initMainData = function() {
	var _self = this;
};
/*
 * 构建列表页面布局
 */
rh.vi.wbimView.prototype._layout = function() {
	var _self = this;
	// 最外层容器
	var imBox = jQuery("<div id='wbim_box' class='wbim_box' style='position: fixed; bottom: 0px; right: 20px;'></div>");
	// swfbox选择文件
	this.imSwfBox = jQuery("<div id='webim_swf_box' style='position: absolute; bottom: 2px; right: 2px; z-index: 1001; width: 1px; height: 1px;'></div>");
	this.imSwfBox.appendTo(imBox);
	// swfbox选择文件？
	this.imSwfBox2 = jQuery("<div style='position: absolute; z-index: 1500; overflow: hidden; width: 95px; height: 18px; bottom: 119px; right: 360px;'></div>");
	this.imSwfBox2.appendTo(imBox);
	// 用户列表容器 (登陆后，默认最小化)
	this.imListExpand = jQuery("<div class='wbim_list_expand' style='z-index: 1001; right: 0px; bottom: 0px;  display: none;'></div>");
	this._initListExpand().appendTo(this.imListExpand);
	this.imListExpand.appendTo(imBox);
	// 聊天外层容器 (登陆后，默认隐藏)
	this.imChatBox = jQuery("<div style='position: absolute; right: 215px; bottom: 0px; z-index: 1001; display: none;' class='wbim_chat_box wbim_chat_box_s'></div>");
	this._initChatBox().appendTo(this.imChatBox);
	this.imChatBox.appendTo(imBox);
	// 最小化的操作条容器(登陆后，默认隐藏)
	this.imMinBoxCol3 = jQuery("<div class='wbim_min_box_col2' style='position: absolute; bottom: 0px; right: 0px;'></div>");
	this._initMinBoxCol3().appendTo(this.imMinBoxCol3);
	this.imMinBoxCol3.appendTo(imBox);
	imBox.appendTo(jQuery("body"));
	// 好友列表对象
	this.myFriendUl = jQuery("#rh_list_group_myfriend");

	// 发送信息
	var sendObj = jQuery(".wbim_btn_publish");
	sendObj.bind("click", function(event) {
		var body = jQuery("#input").val();
		var countLim = jQuery(".rh_wbim_tips_char_count").hasClass("spetxt");
		if ((jQuery.trim(body).length > 0) && countLim === false) {
			// send message
			rhImFunc.sendMsg(rhImFunc.currentChat, body);
			// clean message input
			jQuery("#input").val("");
			//scroll to bottom
			rhImFunc.scroll_chat();
		} else {
			jQuery("#input").css("background-color","#eee");
		    setTimeout(function() {
		    	jQuery("#input").css("background-color","");
	     	},500);
		    setTimeout(function() {
		    	jQuery("#input").css("background-color","#eee");
	     	},1000);
		    setTimeout(function() {
		    	jQuery("#input").css("background-color","");
	     	},1500);
		}
	});

	// TODO 发送文件
	var fileObj = jQuery("#webim_file");
	var config = {"FILE_CAT":"SHARE_FILE","TYPES":"*.jpg;*.jpeg;*.png;*.gif;*.doc;*.docx;*.wps;*.xls;*.xlsx;*.ppt;*.pptx;*.txt;","zotn":false};
	var rhfile = new rh.ui.File({"config":config});
	// send file callback
	rhfile.fillData = function(val) {
		for ( var i = 0; i < val.length; i++) {
			var file = val[i];

			var fileId = file["FILE_ID"];
			var fileName = file["FILE_NAME"];
			var fileUrl = FireFly.getContextPath() + "/file/" + fileId

			// send message
			var body = "${rh.file}" + fileUrl + "?fileName=" + fileName;
			rhImFunc.sendMsg(rhImFunc.currentChat, body);
		}
	};

	fileObj.append("<i class='wbim_icon_doc' style='float:left;'></i>");
//	rhfile.obj.css({"float":"left"});
	rhfile.getObj().find(".rh-icon").removeClass("rh-icon");
	rhfile.getObj().find(".rh-icon-inner").removeClass("rh-icon-inner");
	fileObj.append(rhfile.obj);

	rhfile.initUpload();
};

/*
 * 用户列表容器初始化
 */
rh.vi.wbimView.prototype._initListExpand = function() {
	var _self = this;
	var listCon = jQuery("<div class='wbim_list_con'></div>");
	// 1.头部
	var listTit = jQuery("<div class='wbim_tit'>"
			+ "<div class='wbim_titin'>"
			+ "	<div class='wbim_tit_lf'>"
			+ "		<div node-type='status_manager'>"
			+ "			<div class='tit'>"
			+ "				<span class='rh_wbim_line_status wbim_status_online'></span><span class='txt'>在线</span><span class='icon'></span>"
			+ "			</div>"
			//+ "			<div class='linert'></div>"
			+ "			<ul class='rh_wbim_line_list' style='display: none; visibility: hidden;'>"
			+ "				<li class='lingtop'></li>"
			+ "				<li class='rh_wbim_line_li'><a href='javascript:void(0)'><span class='wbim_status_online'></span>在线</a></li>"
			//+ "				<li class='rh_wbim_line_li'><a href='javascript:void(0)'><span class='wbim_status_busy'></span>忙碌</a></li>"
			//+ "				<li class='rh_wbim_line_li'><a href='javascript:void(0)'><span class='wbim_status_away'></span>离开</a></li>"
			+ "				<li class='rh_wbim_line_li'><a href='javascript:void(0)'><span class='wbim_status_offline'></span>离线</a></li>"
			+ "			</ul>"
			+ "		</div>"
			+ "	</div>"
			+ "	<div class='wbim_tit_rt'>"
			+ "		<a href='#' title='设置' target='_blank' class='wbim_icon_setup'></a>" 
			//+ "		<a class='wbim_icon_fk' title='意见反馈' href='#' target='_blank'></a>"
			+		"<a hidefocus='true' title='最小化' class='wbim_icon_mini' href='javascript:;'></a>"
			+ "	</div>" + "</div>" + "</div>");
	listTit.find("a.wbim_icon_mini").bind("click", function() {
		_self._minListExpand();
	});
	listTit.find(".tit").bind("click", function() {
		listTit.find(".rh_wbim_line_list").show().css("visibility","visible");
	});
	listTit.find(".rh_wbim_line_list").bind("mouseleave",function(event) {
		jQuery(this).hide().css("visibility","hide");
	});
	listTit.find(".rh_wbim_line_list .rh_wbim_line_li").bind("click",function(event) {
		var clas = jQuery(this).find("span").attr("class");
		var txt = jQuery(this).find("a").text();
		listTit.find(".rh_wbim_line_status").removeClass("wbim_status_online wbim_status_busy wbim_status_away wbim_status_offline").addClass(clas);
		listTit.find(".wbim_tit_lf .txt").text(txt);
		jQuery(this).parent().hide().css("visibility","hide");
		if (clas == "wbim_status_online") {
			_self._connect();
		} else if (clas == "wbim_status_offline") {
			rhImFunc.wbimDisconnect();
		}
	});
	listTit.appendTo(this.imListExpand);
	// 2.分割线
	//var line = jQuery("<div class='wbim_line'></div>").appendTo(listCon);
	// 3.查找
	var listUp = jQuery("<div class='wbim_list_up'>"
			+ "	<div class='wbim_list_srch'>"
			+ "		<div class='wbim_list_srchin'>"
			+ "			<a href='javascript:void(0)' hidefocus='true' style='display: none;' class='wbim_icon_close_s'></a><input id='searchUser' type='text' value='查找好友'>"
			+ "		</div>" + "	</div>" + "</div>")
	listUp.appendTo(listCon);
	// 4.tab分组
	var listTab = jQuery("<div class='wbim_list_tab'>"
			+ "<ul>"
			+ "	<li class='rh_wbim_userTab rh_wbim_user_latest curr'><strong class='user'><a title='最近联系人' href='javascript:;'><span><em class='wbim_tab_near'></em></span></a></strong></li>"
			+
			// " <li ><strong class='group'><a title='群聊'
			// href='javascript:;'><span><em
			// class='wbim_tab_group'></em></span></a></strong></li>" +
			"	<li class='rh_wbim_userTab rh_wbim_user_all'><strong class='near'><a title='全部联系人' href='javascript:;'><span><em class='wbim_tab_user'></em></span></a></strong></li>"
			+ "</ul>" + "</div>")
	listTab.find("li.rh_wbim_user_latest").bind("click", function(event) {
        jQuery(".wbim_list_friend").hide();
        jQuery("#rh_list_latest").show();
        jQuery(".rh_wbim_userTab").removeClass("curr");
        jQuery(this).addClass("curr");
	});
	listTab.find("li.rh_wbim_user_all").bind("click", function(event) {
		if (jQuery("#rh_list_alluser .bbit-tree").length == 0) {
			_self._bldAllUser();
			jQuery("#rh_list_alluser .friendCount").text(jQuery("#rh_list_alluser .bbit-tree-node").length);
		}
        jQuery(".wbim_list_friend").hide();
        jQuery("#rh_list_alluser").show();
        jQuery(".rh_wbim_userTab").removeClass("curr");
        jQuery(this).addClass("curr");
		//alert(99);
	});
	listTab.appendTo(listCon);
	// 5.分组头
	var groupTit = jQuery("<div style='position: absolute; top: 74px; left: 2px; width: 180px; height: 31px; background-color: white; z-index: 1; display: none; overflow: hidden;'"
			+ "class='wbim_float_group_tit'></div>");
	groupTit.appendTo(listCon);
	// 6.列表外层容器
	var listBox = jQuery("<div></div>");
	this._initListGroup().appendTo(listBox);
	listBox.appendTo(listCon);
	// 7.底部条
	var listPos = jQuery("<div class='wbim_list_pos'>"
			+ "<a href='javascript:;' hidefocus='true' class='wbim_clicknone'><span class='wbim_icon_arrd'></span></a>"
			+ "<div title='查看我的私信' class='wbim_send_con'>"
			+ "	<a href='#' target='_blank' hidefocus='true' class='wbim_send_pmsg'><span class='wbim_btn_smesg'></span></a>"
			+ "</div>" + "</div>");
	listPos.find("a.wbim_clicknone").bind("click", function(event) {
		_self._minListExpand();
	});
	listPos.appendTo(listCon);
	return listCon;
};
/*
 * 构造联系人列表
 */
rh.vi.wbimView.prototype._initListGroup = function() {
	var listBox = jQuery("<div class='wbim_list_box'></div>");
	// 1.最近联系人
	var listFriendLatest = jQuery("<div class='wbim_list_friend' id='rh_list_latest' style=''>"
			+ "<div class='wbim_list_group'>"
			+ "	<div class='wbim_list_group_tit wbim_open' title='最近联系人'>最近联系人 [ <span title='在线联系人' class='friendCount'>0</span> ]</div>"
//			+ "	<div class='wbim_chat_tips'>"
//			+ "		删除<a href='http://weibo.com/messages' target='_blank'>私信</a>后可移除最近联系人<a hidefocus='true' href='javascript:;' class='wbim_icon_close_s'></a>"
//			+ "	</div>"
			+ "	<ul class='rh_list_group_myfriend' id='rh_list_group_myfriend'>"
//			+ "		<li uid='1697081282' title='中国金融博物馆书院' class='wbim_offline'><div"
//			+ "				class='wbim_userhead'>"
//			+ "				<img src='http://tp3.sinaimg.cn/1697081282/30/1288167261/2'><span"
//			+ "					style='display: none;' class='wbim_icon_msg_s'></span><span class='wbim_status_offline'></span>"
//			+ "			</div>"
//			+ "			<div class='wbim_username'>"
//			+ "				中国金融博物馆书院 <span title='微博会员' node-type='wbim_ico_member'></span>"
//			+ "			</div></li>" 
			+ "	</ul>" + "</div>" + "</div>");
	listFriendLatest.appendTo(listBox);
	// 2.用户
	var listFriend = jQuery("<div class='wbim_list_friend' id='rh_list_alluser' style='display: none;'>"
			// + "<div class='wbim_list_group'>" +
			// " <div class='wbim_list_group_tit wbim_close' title='在线好友'>在线好友 [
			// <span
			// title='在线好友'>0</span> ]</div>" +
			// " <ul style=''></ul>" +
			// "</div>"
			+ "<div class='wbim_list_group'>"
			+ "	<div class='wbim_list_group_tit wbim_open' title='所有联系人'>所有联系人 [ <span title='在线联系人' class='friendCount'>0</span> ]</div>"
			+ "	<ul class='rh_list_group_alluser' id='rh_list_group_alluser'></ul>"
			+ "</div>" + "</div>");
	listFriend.appendTo(listBox);
	// 3.群组
	var listFriendGroup = jQuery("<div class='wbim_list_friend' style='display: none;'>"
			+ "<div class='wbim_list_group'>"
			+ "	<div class='wbim_chat_tips' style='display: none;'>"
			+ "		仅达人<a href='http://q.weibo.com/create_private?source=webim'"
			+ "			target='_blank'>创建</a>的私密群可群聊<a hidefocus='true'"
			+ "			href='javascript:;' class='wbim_icon_close_s'></a>"
			+ "	</div>" + "</div>" + "</div>");
	listFriendGroup.appendTo(listBox);
	listBox.find(".wbim_list_group_tit").bind("click",function(event) {
		if (jQuery(this).next("ul:visible").length == 1) {
			jQuery(this).next("ul:visible").hide();
			jQuery(this).addClass("wbim_close");
		} else {
			jQuery(this).next("ul").show();
			jQuery(this).removeClass("wbim_close");
		}
	});
	return listBox;
};
/*
 * 聊天外层容器
 */
rh.vi.wbimView.prototype._initChatBox = function() {
	var _self = this;
	// 1.头和聊天记录
	var chatCon = jQuery("<div class='chat_con radius8' style='float:left;'></div>");
	// 1.1 头部
	var titleContainer = jQuery("<div class='wbim_tit'></div>").appendTo(this.imChatBox);
	var wbimTtitin = jQuery("<div class='wbim_titin'></div>").appendTo(titleContainer);
	var wbim_tit_lf = jQuery("<div class='wbim_tit_lf rh_wbim_tit_lf'></div>").appendTo(wbimTtitin);// 头部左侧的聊天人状态
	jQuery("<p class='wbim_single_user'><span node-type='wbim_status' class='wbim_status wbim_status_offline'></span>"
					+ "<span class='txt'><a target='_blank' title='' href=''>test</a></span>"
					+ "<span class='bringin' style='display: none'>正在输入</span></p>")
			.appendTo(wbim_tit_lf);
	jQuery(
			"<p style='display: none;'><span class='wbim_icon_group_tit'></span><span class='txt'><a target='_blank'></a>"
					+ "<em class='txtg'>在线成员(<a target='_blank' title='查看在线成员'></a>)</em></span></p>")
			.appendTo(wbim_tit_lf);

	var wbim_tit_rt = jQuery("<div class='wbim_tit_rt'></div>").appendTo(
			wbimTtitin);// 头部右侧的窗口操作
	var minObj = jQuery(
			"<a node-type='wbim_icon_mini' title='最小化' hidefocus='true' class='wbim_icon_mini' href='javascript:;'></a>")
			.appendTo(wbim_tit_rt);
	minObj.bind("click", function(event) {
		_self._minChatArea();
	});
	var closeObj = jQuery(
			"<a title='关闭' hidefocus='true' class='wbim_icon_close' id='rh_wbim_chat_close' href='javascript:;'></a>")
			.appendTo(wbim_tit_rt);
	closeObj.bind("click", function(event) {
		_self._closeChatArea();
	});
	//个人大头像的展示
	var selfImg = jQuery("<img class='rh_wbim_selfImg' src='http://tp2.sinaimg.cn/1997392765/30/0/1'></img>").appendTo(chatCon);
	
	// 1.2 确认提示，默认隐藏
	this._initWbimMask().appendTo(chatCon);
	this._initChatLeft().appendTo(chatCon);
	// 1.3 聊天区+操作条+输入区
	this._initChartRight().appendTo(chatCon);

	return chatCon;
};

/*
 * 屏蔽区域
 */
rh.vi.wbimView.prototype._initWbimMask = function() {
	var imMask = jQuery("<div style='display: none; position: absolute; top: 24px; right: 316px; z-index: 1002;' node-type='wbim_mask'>"
			+ "<div class='wbim_box_pop' style='width: 316px; height: 365px;'></div>"
			+ "<div style='top: 145px; left: 50px;' class='wbim_confirm_box'>"
			+ "	<div class='wbim_confirm_info'>"
			+ "		<p class='wbim_confirm_p'><span class='wbim_icon_tips'></span><span node-type='txt' class='txt'></span></p>"
			+ "		<p class='wbim_confirm_btn'><a hidefocus='true' href='javascript:;' node-type='wbim_btn_c' class='wbim_btn_c'><em>确定</em></a></p>"
			+ "	</div>" + "</div>" + "</div>");
	return imMask;
};
/*
 * 左侧的多人提示
 */
rh.vi.wbimView.prototype._initChatLeft = function() {
	var chatLeft = jQuery("<div class='wbim_chat_lf' style='display:none;'>"
			+ "<a onclick='return false;' href='javascript:;' hidefocus='true' class='wbim_scrolltop_n'></a>"
			+ "<div class='wbim_chat_friend_box'>"
			+ "	<ul class='wbim_chat_friend_list'>"
			+ "	</ul>"
			+ "</div>"
			+ "<a onclick='return false;' href='javascript:;' hidefocus='true' node-type='wbim_scrollbtm' class='wbim_scrollbtm_n'></a>"
			+ "</div>");
	return chatLeft;
};
/*
 * 增加一人都左侧列表
 */
rh.vi.wbimView.prototype._addUserToLeft = function(data) {
	var _self = this;
	var id = data.id;
	var name = data.name;
	var state = data.state;
	var stateLiStr = "wbim_" + state
	var stateStr = "wbim_status_" + state
	//宽度修改
	if (jQuery(".rh_chatLeftLi").length == 1 && (jQuery(".wbim_chat_lf:visible").length == 0)) {
		if (jQuery(".rh_chatLeftLi[userid='" + id + "']").length == 0) {
			jQuery(".wbim_chat_box_s").removeClass("wbim_chat_box_s");
			jQuery(".wbim_chat_lf").show();
		}
	}
	if (jQuery(".rh_chatLeftLi[userid='" + id + "']").length == 0) {
		var chatLeft = jQuery("<li title='" + name + "' userid='" + id + "' class='rh_chatLeftLi " + stateLiStr + "'>"
				+ "			<div class='wbim_userhead'>"
				+ "				<img src='http://tp2.sinaimg.cn/1997392765/30/0/1'><span class=' " + stateStr + " '></span>"
				+ "			</div>"
				+ "			<div class='wbim_username'>" + name + "</div>"
				+ "			<a class='wbim_icon_close_s' href='javascript:;' hidefocus='true'></a>"
				+ "</li>");
		chatLeft.bind("click",function(event) {
			_self._cancelLeftUserFlicker(id);
			_self._showChatArea(data);
		});
		chatLeft.find(".wbim_icon_close_s").bind("click", function(event) {
			var obj = jQuery(this).parent();
			if (obj.next().length > 0) {
				obj.next().first().click();
			} else if (obj.prev().length > 0) {
				obj.prev().last().click();
			} else if (obj.prev().length == 0) {
				_self.imChatBox.addClass("wbim_chat_box_s");
				jQuery(".wbim_chat_lf").hide();
				jQuery("#rh_wbim_chat_close").click();
			}
			obj.remove();
		});
		chatLeft.appendTo(jQuery(".wbim_chat_friend_list"));
	}
	jQuery(".rh_chatLeftLi").removeClass("wbim_active");
	jQuery(".rh_chatLeftLi[userid='" + id + "']").addClass("wbim_active");
};
/*
 * 聊天去+操作条+输入区
 */
rh.vi.wbimView.prototype._initChartRight = function() {
	var _self = this;
	var charRt = jQuery("<div class='wbim_chat_rt'></div>");
	// 1.聊天历史区
	var chatUp = jQuery("<div class='wbim_chat_up'>"
			+ "<div class='wbim_chat_tips' style='display: block;'>"
			+ "	<span class='wbim_icon_tips'></span><span>对方当前不在线或隐身，可能无法立即回复。</span>"
			+ "	<a class='wbim_icon_close_s' node-type='wbim_icon_close_s' href='javascript:;' hidefocus='true'></a>"
			+ "</div>"
			+ "<div id='wbim_chat_list' class='wbim_chat_list' style='height: 198px; top: 24px;'>"
//			+ "	<dl id='hismsg' style='display: block;'>"
//			+ "	</dl>"
			+ "</div>"
			+ "</div>");
	chatUp.find("div.wbim_chat_tips").bind("click", function(event) {
		_self._closeTip();
	});
	chatUp.appendTo(charRt);
	// 2.操作条区
	var chatToolbar = jQuery("<div node-type='wbim_chat_toolbar' class='wbim_chat_toolbar'>"
			+ "<div node-type='wbim_chat_toolbarin' class='wbim_chat_toolbarin'>"
//			+ "	<div node-type='wbim_face' class='wbim_face'>"
//			+ "		<a href='javascript:;' hidefocus='true' title='普通表情' class='wbim_tool_ctrl'><i class='wbim_icon_face'></i>表情</a>"
//			+ "		<div node-type='wbim_face_box' style='display: none;' class='wbim_face_box'>"
//			+ "			<div class='wbim_face_tit'>"
//			+ "				<div class='wbim_face_arr'></div>"
//			+ "				<div class='wbim_face_tit_lf'>普通表情</div>"
//			+ "				<a  href='javascript:;' class='wbim_icon_close'></a>"
//			+ "			</div>"
//			+ "			<div class='wbim_face_con'>"
//			+ "				<ul node-type='wbim_face_list' class='wbim_face_list'>"
//			+ "					<li><a href='javascript:;' action-type='wbim_face_a'"
//			+ "						title='[织]'><img src='http://img.t.sinajs.cn/t35/style/images/common/face/ext/normal/41/zz2_org.gif'></a></li>"
//			+ "					<li><a href='javascript:;' action-type='wbim_face_a'"
//			+ "						title='[神马]'><img src='http://img.t.sinajs.cn/t35/style/images/common/face/ext/normal/60/horse2_org.gif'></a></li>"
//			+ "				</ul>"
//			+ "				<div class='tsina_loading cter'>"
//			+ "					<span class='tsina_ico_ldg'></span>"
//			+ "				</div>"
//			+ "			</div>"
//			+ "		</div>"
//			+ "	</div>"
//			+ "	<div title='上传图片' class='wbim_face'>"
//			+ "		<a class='wbim_tool_ctrl' hidefocus='true' href='javascript:;'><i class='wbim_icon_img'></i>图片</a>"
//			+ "	</div>"
			+ "	<div id='webim_file' title='选择文件' class='wbim_face'>"
			+
			// " <a class='wbim_tool_ctrl' hidefocus='true'
			// href='javascript:;'><i
			// class='wbim_icon_doc'></i>文件</a>" +
			"	</div>"
//			+ "	<a target='_blank' title='私信记录' node-type='wbim_history_user' style='' class='wbim_history'"
//			+ "		href='http://weibo.com/message/talklist.php?source=webim&amp;uid=1649351907'><span class='wbim_icon_chatdoc'></span>私信记录</a>"
			+ "</div>" + "</div>");
	chatToolbar.appendTo(charRt);

	// 3.输入文字区
	var chatInput = jQuery("<div style='margin-top: 1px;' node-type='wbim_chat_input' class='wbim_chat_input'>"
			+ "<div class='wbim_chat_input_tips' style='display: none;'>"
			+ "	<div style='display: none' node-type='wbim_tips_pic' class='wbim_tips_pos_n'>"
			+ "		<div class='wbim_tips_pic_n'>"
			+ "			<a href='javascript:;'><img style='cursor: pointer' alt='' node-type='img_preview'></a>"
			+ "		</div>"
			+ "		<span class='wbim_p_arr'></span>"
			+ "	</div>"
			+ "	<div node-type='fl' class='fl'></div>"
			+ "	<div node-type='fr' class='fr'></div>"
			+ "</div>"
			+ "<textarea id='input' node-type='wbim_chat_input_ta'></textarea>"
			+ "</div>");
	
	chatInput.find("#input").focus(function(event) {//增加回车输入内容
		rhImFunc.startComposing(rhImFunc.currentChat);
	}).blur(function(event) {
		rhImFunc.endComposing(rhImFunc.currentChat);
	});
	chatInput.find("#input").keypress(function(event) {//增加回车输入内容
		var str = Cookie.get(rhImFunc.enterCookie);
		if (str && str.length > 0) {
			if ((str == "enter") && event.keyCode == '13') {
				jQuery(".wbim_btn_publish").click();
	            return false;
			} else if ((str == "ctrlenter") && event.keyCode == '10') {
	            jQuery(".wbim_btn_publish").click();
	            return false;				
			}
		} else if (event.keyCode == '13') {
            jQuery(".wbim_btn_publish").click();
            return false;
        }
    }).keyup(function(event) {//允许输入字数控制
		 var len = jQuery(this).val().length;
		 var res = 200 - len;
		 jQuery(".rh_wbim_tips_char_count").text(res);
		 if (res < 0) {
			 jQuery(".rh_wbim_tips_char_count").addClass("spetxt");
		 } else {
			 jQuery(".rh_wbim_tips_char_count").removeClass("spetxt");
		 }
	});
	chatInput.appendTo(charRt);
	// 4.底部区域
	var chatBtm = jQuery("<div node-type='wbim_chat_btm' class='wbim_chat_btm'>"
			+ "<div class='wbim_chat_btmin'>"
			+ "	<div class='wbim_chat_btm_lf'>"
			+ "		"
			+ "	</div>"
			+ "	<div class='wbim_chat_btm_rt'>"
			+ "		<p class='wbim_tips_char'>"
			+ "			<span class='rh_wbim_tips_char_count'>200</span><span class='spetxt'></span>"
			+ "		</p>"
			+ "		<div node-type='wbim_btn_send' class='wbim_btn_send'>"
			+ "			<a title='发送' href='javascript:;' class='wbim_btn_publish' hidefocus='true'>发送</a>"
			+ "			<div class='wbim_btn_choose'>"
			+ "				<a class='wbim_btn_choose_a' node-type='wbim_btn_choose_a'>选择</a>"
			+ "				<ul class='wbim_btn_choose_ext'>"
			+ "					<li class='rh_wbim_enterbtn curr' nodetext='enter'><span></span><em><a href='javascript:;'>按Enter键发送</a></em></li>"
			+ "					<li class='line'><span></span><em></em></li>"
			+ "					<li class='rh_wbim_enterbtn' nodetext='ctrlenter'><span></span><em><a href='javascript:;'>按Ctrl+Enter键发送</a></em></li>"
			+ "				</ul>"
			+ "			</div>"
			+ "		</div>"
			+ "	</div>"
			+ "</div>"
			+ "</div>");
	chatBtm.find(".wbim_btn_choose_a").bind("click", function(event) {//弹出选择输入方式框
		if (jQuery(".wbim_btn_choose_ext:visible").length == 0) {
			jQuery(".wbim_btn_choose_ext").show();
		} else {
			jQuery(".wbim_btn_choose_ext").hide();
		}
		var str = Cookie.get(rhImFunc.enterCookie);
		if (str == "ctrlenter") {
			jQuery(".rh_wbim_enterbtn").removeClass("curr");
			jQuery(".rh_wbim_enterbtn[nodetext='" + str + "']").addClass("curr");
		}
	});
	chatBtm.find(".rh_wbim_enterbtn").bind("click", function(event) {//输入方式列表的绑定
		jQuery(".rh_wbim_enterbtn").removeClass("curr");
		jQuery(this).addClass("curr");
		//将设定写入cookie
		var str = jQuery(this).attr("nodetext");
		Cookie.set(rhImFunc.enterCookie, str, "365");
		jQuery(".wbim_btn_choose_ext").hide();
	});
	chatBtm.appendTo(charRt);
	return charRt;
};

/*
 * 最小化的操作条容器内容填充
 */
rh.vi.wbimView.prototype._initMinBoxCol3 = function() {
	var _self = this;
	var imMinBox = jQuery("<div class='wbim_min_box'></div>");
	// 联系人最小区块
	this.minFriend = jQuery("<div class='wbim_min_friend'></div>").appendTo(
			imMinBox);
	jQuery("<p class='statusbox'><span class='wbim_status_online'></span></p>")
			.appendTo(this.minFriend);
	jQuery("私信聊天 [ <span class='wbim_online_count' title='在线联系人'>0</span> ]")
			.appendTo(this.minFriend);
	// jQuery("<span class='webim_rec_count' style='display: none; color: gray;
	// margin-left: 80px;'></span>").appendTo(minFriend);
	this.minFriend.bind("click", function() {
		_self._showListExpand();
	});
	// 分割线
	var minLine = jQuery("<div class='wbim_min_line wbim_min_linefor3'></div>")
			.appendTo(imMinBox);
	// 和谁聊天区块
	this.minChat = jQuery("<div class='wbim_min_chat'></div>").appendTo(imMinBox);//wbim_min_chat_msg
	jQuery("<span class='wbim_icon_msg'></span><span class='wbim_min_text_pre' style='display: inline;'>正与 </span>")
			.appendTo(this.minChat);
	jQuery("<span class='wbim_min_nick'></span>").appendTo(this.minChat);
	jQuery("<span class='line'></span><span class='wbim_min_text'>聊天中</span>").appendTo(this.minChat);
	this.minChat.bind("click", function() {
		_self._maxChatArea();
	});
	return imMinBox;
};
/*
 * act方法
 */
rh.vi.wbimView.prototype._act = function(aId, aObj) {
	var _self = this;
	var taObj = aObj;
	switch (aId) {
	case "model":// 选择模版
		taObj.bind("click", function() {
			// alert(99);
			_self._openModelView();
		});
		break;
	case "coms":// 选择组件
		taObj.bind("click", function() {
			// alert(99);
			_self._openComsView();
		});
		break;
	case "view":// 预览
		taObj.bind("click", function() {
			Tab.open({
				'url' : 'SY_COMM_TEMPL.show.do?model=view&pkCode='
						+ _self._pkCode,
				'tTitle' : '模版预览',
				'menuFlag' : 3
			});
		});
		break;
	case "save":// 保存
		taObj.bind("click", function() {
			_self._saveTempl();
		});
		break;
	case "refresh":// 刷新
		taObj.bind("click", function() {
			window.location.href = window.location.href;
		});
		break;
	}
};

/*
 * 页面渲染后
 */
rh.vi.wbimView.prototype._afterLoad = function() {
	var _self = this;
	//连接消息服务器
	this._connect();
};

/*
 * 区块的统一处理
 */
rh.vi.wbimView.prototype._initArea = function() {

};
/*
 * 聊天区最小化
 */
rh.vi.wbimView.prototype._minChatArea = function() {
	this.imChatBox.css("display", "none");
	this.imMinBoxCol3.removeClass("wbim_min_box_col2").addClass("wbim_min_box_col3");
	jQuery(".wbim_min_nick").text(jQuery(".wbim_active").attr("title"));
	if (jQuery(".wbim_highlight").length > 0) {
		this._minFlicker();
	}
};

/*
 * 聊天区最大化
 */
rh.vi.wbimView.prototype._maxChatArea = function() {
	this.imChatBox.css("display", "block");
	rhImFunc.scroll_chat();
	var nowId = rhImFunc.currentChat;
	if (rhImFunc.flickerUser[nowId] == "true") {//当前聊天区正是新消息用户
		this._cancelMinFlicker();
		delete rhImFunc.flickerUser[nowId];
	}
};
/*
 * 聊天区最大化
 */
rh.vi.wbimView.prototype._updateChatArea = function(data) {
	if (!data) {
		return;
	}
	rhImFunc.currentChat = data.jid;
    var _self = this;
	if (data && data.id) {
		var name = data.name;
		var state = data.state;
		var obj = jQuery(".wbim_single_user");
		obj.find(".txt").html("<a title='" + name + "' href='#' target='_blank'>" + name + "</a>");
		if (state == "offline") {
			obj.find(".wbim_status").removeClass(rhImFunc.online).addClass(rhImFunc.offline);
			_self._showTip();
		} else if (state == "online") {
			obj.find(".wbim_status").removeClass(rhImFunc.offline).addClass(rhImFunc.online);
			_self._closeTip();
		}
        //如果当前聊天区为显示状态
		var chartObj = jQuery(".rh_chat_dl[touser='" + rhImFunc.currentChat + "']");
		if (chartObj.length == 0) {
			// 加载聊天记录
			this._getRecords(data.jid);
			rhImFunc.scroll_chat();
		} else if (jQuery(".rh_chat_dl[touser='" + rhImFunc.currentChat + "']:visible").length == 0) {
			jQuery(".rh_chat_dl").hide();
			chartObj.show();
			setTimeout(function() {
				rhImFunc.scroll_chat();
			},0);
		}
		//加载左侧用户列表
		_self._addUserToLeft(data);
	}
};
/*
 * 加载聊天记录，最近10条或更多
 */
rh.vi.wbimView.prototype._getRecords = function(jid,beforeId) {
	var _self = this;
	rhImFunc.getHisMsg(rhImFunc.currentChat, function(collections,responseRsm) {
		var hismsgHtml = "";
		var tempMsgId = 0;
		// get last history message
		for ( var i = collections.length - 1; i > -1; i--) {
			var msg = collections[i];
			var timestamp = msg.timestamp;
			var from = msg.from;
			var to = msg.to;
			var body = msg.body;
			var msgId = msg.id;
			rhImFunc.insert_hismsg(from, to, body, timestamp, "",msgId,beforeId);
			if ((collections.length-1) == i) {
				tempMsgId = msgId;
			}
			if ((collections.length == 10) && (i == 0)) {
				var dlObj = jQuery(".rh_chat_dl[touser='" + jid + "']");
				dlObj.find(".rh_wbim_more").remove();
				var more = jQuery("<div class='rh_wbim_more'>点击查看更多消息</div>").prependTo(dlObj);
				more.bind("click",function() {
					_self._getRecords(jid,tempMsgId);
				});
			}
		}
		if (beforeId) {//更多的点击
			var msgdd = jQuery(".rh_wbim_msg_dd[msgid='" + msgId + "']");
			rhImFunc.add_hismsg_line(msgdd);
			if (collections.length < 10) {
				var dlObj = jQuery(".rh_chat_dl[touser='" + jid + "']");
				dlObj.find(".rh_wbim_more").remove();
				jQuery("<div class='rh_wbim_more'>无更多消息</div>").prependTo(dlObj);
			}
		} else {
			// 增加聊天历史分割线
			rhImFunc.add_hismsg_line();
			rhImFunc.scroll_chat();
		}
	},beforeId);
};
/*
 * 显示最小化聊天区块
 */
rh.vi.wbimView.prototype._showMinChatArea = function(data) {
	//最小化的条件
	//if () {
		this._updateChatArea(data);
		this._minChatArea();
//	}
};
/*
 * 聊天区显示
 */
rh.vi.wbimView.prototype._showChatArea = function(data) {
    this._updateChatArea(data);
	this.imChatBox.css("display", "block");
};
/*
 * 聊天区关闭
 */
rh.vi.wbimView.prototype._closeChatArea = function() {
	//左侧人员列表处理
	jQuery(".wbim_chat_friend_list").empty();
	this.imChatBox.addClass("wbim_chat_box_s");
	jQuery(".wbim_chat_lf").hide();
	//box处理
	this.imChatBox.css("display", "none");
	this.imMinBoxCol3.removeClass("wbim_min_box_col3").addClass("wbim_min_box_col2");
};
/*
 * 人员列表区最小化
 */
rh.vi.wbimView.prototype._minListExpand = function() {
	this._sycState();
	this.imListExpand.css("display", "none");
	this._fillOnlineUserCount();
};
/*
 * 同步当前状态和最小化的时候
 */
rh.vi.wbimView.prototype._sycState = function() {
	var clas = jQuery(".rh_wbim_line_status").attr("class");
	clas = clas.split(" ");
	clas = clas.splice(1,1);
	var str = clas.join(" ");
	if (str.length > 2) {
		jQuery(".wbim_min_friend .statusbox span").attr("class","").addClass(str);
	}
};
/*
 * 更新在线人员数量
 */
rh.vi.wbimView.prototype._fillOnlineUserCount = function() {
	jQuery(".wbim_online_count").text(this.myFriendUl.find("." + rhImFunc.online).length + "个最近联系人在线");
};
/*
 * 人员列表区显示
 */
rh.vi.wbimView.prototype._showListExpand = function() {
	this.imListExpand.css("display", "block");
};
/*
 * 关闭不在线的提示："对方当前不在线或隐身，可能无法立即回复"条
 */
rh.vi.wbimView.prototype._closeTip = function() {
	jQuery("div.wbim_chat_tips").css("display", "none");
	jQuery(".wbim_chat_list").css({
		"height" : "222",
		"top" : "0"
	});
};
rh.vi.wbimView.prototype._showTip = function() {
	jQuery("div.wbim_chat_tips").css("display", "");
	jQuery(".wbim_chat_list").css({
		"height" : "198",
		"top" : "24px"
	});
};
/*
 * 连接消息服务器
 */
rh.vi.wbimView.prototype._connect = function() {
	var _self = this;
	var fullJid = rhImFunc.jid + "/" + "rhwebim";
    var data = {jid : fullJid,password : rhImFunc.token};
	//var conn = new Strophe.Connection('http://192.168.1.86:7070/http-bind/');
	//var conn = new Strophe.Connection('http://172.16.0.4:7070/http-bind/');
  var imserver = System.getVar("@C_SY_PLUG_WEBIM_SERVER@");                 
	var conn = new Strophe.Connection(imserver);

	conn.connect(data.jid, data.password, function(status) {
		if (status === Strophe.Status.CONNECTED) {
			_self._connected();
		} else if (status === Strophe.Status.DISCONNECTED) {
			_self._disconnected();
		} else if (status === Strophe.Status.ERROR) {
			alert('error.client.im');
		} else if (status === Strophe.Status.CONNECTING ) {
			//  alert('connecting.client.im');
		} else if (status === Strophe.Status.CONNFAIL ) {
			alert('connfail.client.im');
		} else if (status === Strophe.Status.AUTHENTICATING ) {
			//alert('authenticating.client.im');
		} else if (status === Strophe.Status.AUTHFAIL ) {
			//alert('authfail.client.im');
		} else if (status === Strophe.Status.DISCONNECTING ) {
			//alert('diconnecting.client.im');
		} else if (status === Strophe.Status.ATTACHED ) {
			alert('attached.client.im');
		} 
	});
	rhImFunc.connection = conn;
};

/*
 * 连接消息服务器后
 */
rh.vi.wbimView.prototype._connected = function() {
	// rhImFunc.getFriends();//获取好友列表
	rhImFunc.getRcontacts();// 获取最近联系人列表

	// 注册联系人上下线通知 set up presence handler and send initial presence
	rhImFunc.connection.addHandler(rhImFunc.on_presence, null, "presence");
	rhImFunc.connection.send($pres());

	rhImFunc.connection.addHandler(rhImFunc.on_friend_changed,"jabber:iq:roster", "iq", "set");// 好友发生变化时修改列表
	rhImFunc.connection.addHandler(rhImFunc.on_message, null, "message", "chat");// 消息处理
	// rhImFunc.connection.addHandler(on_subscription_request, null, "presence",
	// "subscribe");
};

/*
 * 中断消息服务器连接
 */
rh.vi.wbimView.prototype._disconnected = function() {
	rhImFunc.connection.disconnect("just disconnect");
	rhImFunc.connection = null;
	rhImFunc.pending_subscriber = null;
};
/*
 * 获取所有联系人
 */
rh.vi.wbimView.prototype._bldAllUser = function() {
	var options = {
		"itemCode" : "rh-select-serv",
		"config" : "SY_ORG_USER,{'extendDicSetting':{'rhexpand':true},'TYPE':'single','extendWhere':' AND SERV_SEARCH_FLAG=1','rtnNullFlag':true}",
		"parHandler" : null,
		"hide" : "explode",
		"show" : "blind",
		"pCon" : jQuery("#rh_list_group_alluser"),
		replaceCallBack : function(id, value) {
			var jid = id + rhImFunc.domain;
			var jid_id = rhImFunc.jid_to_id(jid);
			var name = value.toString();
			// 添加用户
			rhImFunc.new_contact(jid, name);
			// 与该用户聊天
			var obj = jQuery(document).data("imObj");
			var tempState = jQuery(this).find(
					".wbim_status").data("state");
			var temp = {
				"id" : jid_id,
				"jid" : jid,
				"name" : name,
				"state" : tempState
			};
			obj._showChatArea(temp);
		}
	};
	var dictView = new rh.vi.rhDictTreeView(options);
	dictView.show();
};
/*
 * 取消最小化聊天区的提示
 */
rh.vi.wbimView.prototype._cancelMinFlicker = function() {
	jQuery(".wbim_min_chat").removeClass("wbim_min_chat_msg");
};
/*
 * 取消最小化聊天区的提示
 */
rh.vi.wbimView.prototype._minFlicker = function() {
	jQuery(".wbim_min_chat").addClass("wbim_min_chat_msg");
};
/*
 * 取消聊天区人员列表区的提示
 */
rh.vi.wbimView.prototype._cancelLeftUserFlicker = function(id) {
	jQuery(".rh_chatLeftLi[userid='" + id + "']").removeClass("wbim_highlight");
	delete rhImFunc.flickerUser[id];
};
