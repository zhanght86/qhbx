/**	意见	*/ 
GLOBAL.namespace("mb.vi");
mb.vi.mind = function (options) {
	var defaults = {
    	"parHandler":null,
    	"wfCard":null,
    	"servId":null,
    	"dataId":null,
    	"pCon":null
  	};
	this.opts = jQuery.extend(defaults,options);	
  	this._mind = this.opts.mind;
  	this._servId = this.opts.servId;
  	this._dataId = this.opts.dataId;
  	this._parHandler = this.opts.parHandler;
  	this._wfCard = this.opts.wfCard;
  	this._pCon = this.opts.pCon;
  	this._mindInput = "";
  	this._mindList = "";
  	this.oldMindText = "";
  	/**
	 * 意见是否已修改，包括意见内容
	 */
  	this.isModify = function() {
  		return this._textIsModify();
  	};
  	/**
	 * 意见内容是否已修改
	 */
  	this._textIsModify = function() {
  		var newVal = $("#mind-content").val() || "";
  		if (newVal == '' || newVal == this.oldMindText) {
  			return false
  		}
  		return true;
  	};
  	/**
     *是否为修改意见
     */
    this._isModifyMind = function(){
        if(this.oldMindText == ""){
            return false;
        }
        return true;
    }
	
  	this._generalMind = "";
	
	// 是否已提交意见，防止二次提交，生成多条相同的数据
    this._hasCommited = false;
};
/**
 * 显示卡片页面，主方法
 */
mb.vi.mind.prototype.show = function() {
   this._initMainData();
   this._layout();
   this._bindEvent();
   this._afterLoad();
};

//从后台获取意见列表；显示意见列表
mb.vi.mind.prototype._initMainData = function () {
	var _self = this;
	this._mindInput = this._mind["mindInput"];
	this._mindList = this._mind["mindDatas"]["mindList"];
	this._currMind = this._mind["mindDatas"]["_DATA_"];
	if(this._currMind.length){
		$.each(this._currMind, function(index, item) {
			// 如果用户相同，证明是当前用户填写的意见，防止并发同部门的情况下，意见可以互相删除
			if (item["S_USER"] == System.getUser("USER_CODE")) {
				_self.pkCode = item["MIND_ID"];
				_self.mindContent = item["MIND_CONTENT"];
				_self.oldMindText = item["MIND_CONTENT"] || "";
			}
		});
	}
	
	//意见列表
	var mindListHtml='',
		mindList = this._mindList,
		mindListLen = mindList.length;
	if (mindListLen > 0) {
		for (var i=mindListLen-1; i>= 0; i--) {
			mindListHtml += _self._renderMindBean(mindList[i]);
		}
	}
	if(mindListHtml){
		//判断是否存在意见列表
		if (jQuery(_self._pCon).children("li").length <= 0) {
			_self._pCon.append(mindListHtml);
		} else {
			_self._pCon.empty();
			_self._pCon.append(mindListHtml);
		}
	}
};

//显示意见输入框
mb.vi.mind.prototype._layout = function () {
	var _self = this;
	
	var generalMind = _self._mindInput.generalMind;
	/*
	 * 绘制分组框
	 */
	if (generalMind.CODE_ID) {
		// textarea绘制
		_self._pCon.append("<li class='js-mind-input-contain'><label for='mind-content'>办理意见：</label><textarea cols='40' rows='8' id='mind-content' style='resize:none;'>" +(_self.mindContent ? _self.mindContent : "") + "</textarea></li>");
		// 绘制签名选择
		var signList = _self._mindInput.signList,
			signListLen = signList.length;
		if (signListLen > 0) {
			var signArr = [];
			signArr.push("<li class='ui-field-contain sc-li-mind'>");
			signArr.push("<label class='ui-input-text sc-static-label'>请选择</label>");
			signArr.push("<div class='ui-input-text ui-grid-a ui-input-text' style='margin-top:1em;border-width:0;'>");
			for (var i=0; i < signListLen; i++) {
				var signBean = signList[i];
				signArr.push("<div class='ui-block-b' data-corners='true'>");
				signArr.push("<a href='#' data-role='button' data-corners='false' data-sign-id='",signBean.SIGN_ID,"' data-shadow='false' class='sc-sign-block"+ (i==0 ? " sc-sign-selected":"") + "'>");
				signArr.push("<img style='width:100%;' src='",FireFlyContextPath + signBean.imgSrc , "'/>");
				signArr.push("</a>");
				signArr.push("</div>");
			}
			signArr.push("</div>");
			signArr.push("</li>");
			_self._pCon.append(signArr.join(""));
		}
		//获取常用批语
		FireFly.doAct("SY_COMM_USUAL", "query", {}).then(function(result){
		 	if (result && result["_DATA_"] && result["_DATA_"].length) {
		 		_self._generalMind = result["_DATA_"];
		 	}
		 	// 保存意见按钮
		 	_self._initMindBtn();
		});
	}
};
/**
 * 给元素绑定事件
 */
mb.vi.mind.prototype._renderMindBean = function (mindBean,isNew) {
	//意见列表
	var mindHtml = "";
	if(mindBean){
		var canDelete = false;
		if(isNew || this.pkCode == mindBean["MIND_ID"]){
			canDelete = true;
		}
		var dateTime = mindBean["MIND_TIME"];
		time = dateTime.toString().substring(0,10);
		var d = new Date();
		var t = "";
		//同年
		if(time.substring(0,4) == d.getFullYear()){
		/*	if(parseInt(time.substring(5,7)) == parseInt(d.getMonth()+1)){
				if(parseInt(time.substring(8,10)) == parseInt(d.getDate())){
					t =  dateTime.toString().substring(11,16);
				}else if(parseInt(d.getDate()) - parseInt(time.substring(8,10)) == 1){
					t = "昨天";
				}else{
					t = time.substring(5,10);
				}
			}else{
				t = time.substring(5,10);
			}*/
			
			t = dateTime.substring(5,16);
		}else{
			t = time;
		}
		mindHtml += "<li class='ui-field-contain"+ (canDelete ? " sc-mind-temp" : " sc-mind-temp1") +"' id='"+ mindBean["MIND_ID"] +"'>" +
		"<label class='ui-input-text sc-mind-label'>" + mindBean["MIND_CODE_NAME"] + "</label>" +
		(canDelete ? "<span>删除</span>" : "<span>&nbsp;</span>")+
		"<div class='sc-mind-text' style='overflow:;'>" + mindBean["MIND_CONTENT"] + "</div>"+		//style='text-align:left;'
		"<div class='ui-input-text ui-grid-a' style='border-width:0;margin-top:0px; display:block; margin:3px 2px 2px 0px;'>" +
		//"<span class='sc-mind-user'>"+mindBean["S_TNAME"]+"</span>"+
		"<span class='sc-mind-user' style='margin:0 4px;'>" + mindBean["ODEPT_NAME"]+"</span>"+
		"</div>"+
		"<div style='width:100%;opacity:.5;'><span style='float:right;'>" + t + "</span><span style='float:right;margin-right:7px;'>" + mindBean["S_UNAME"]+"</span></div>";
		
		mindHtml += "</li>";
		/*mindHtml += "<li class='ui-field-contain"+ (canDelete ? " sc-mind-temp" : " sc-mind-temp1") +"' id='"+ mindBean["MIND_ID"] +"'>" +
						"<label class='ui-input-text sc-mind-label'>" + mindBean["MIND_CODE_NAME"] + "</label>" +
						(canDelete ? "<span>删除</span>" : "<span>&nbsp;</span>")+
						"<div class='ui-input-text ui-grid-a' style='margin-top:2em;border-width:0'>" +
						"<span class='sc-mind-user'>" + mindBean["S_UNAME"] + ": </span>" +
						"<div class='sc-mind-text'>" + mindBean["MIND_CONTENT"] + "</div>"+
						"<span style='text-align:right;float:right'>" + mindBean["MIND_TIME"] + "</span>";
		mindHtml += "</div></li>";*/
	}
	return mindHtml;
};
/**
 * 给元素绑定事件
 */
mb.vi.mind.prototype._bindEvent = function () {
	var _self = this;
	$("#" + _self._dataId + "_MIND").on("vclick", ".sc-mind-temp>span", function (event) {
		event.preventDefault();
		event.stopImmediatePropagation();
		var $that = $(this).parent();
		navigator.notification.confirm('是否删除?', function(index){
        	if(index==2){
	    		 var id = $that.attr("id");
	      	  	 FireFly.doAct("SY_COMM_MIND", "delete", {"_PK_": id}).done(function(result){
		  		 	var rtnMsg = result[UIConst.RTN_MSG];
	 				if (StringUtils.startWith(rtnMsg, UIConst.RTN_OK)) {
			  			_self.pkCode = null;
			  			$("#mind-content").val("");
			  			_self.oldMindText = "";
			  			$that.fadeOut(500,function(){
			  				$that.remove();
			  			});
						_self._hasCommited=false;
			  		 }
		  		 });
        	} 
	    }, '提示', ['取消','确定']);
	}).on("vclick","#save-btn",function (event) {// 给保存按钮绑定保存事件
		var currMind = _self._wfCard.getMindCodeBean();
		if (!currMind.CODE_ID){ //没有意见输入框，则返回不保存
			return false; 
		}
		// 如果已提交，则不允许再次提交
        if(_self._hasCommited && _self._hasCommited ==true){
            navigator.notification.alert("正在提交中，请稍后!",null,'提示', '确定');
            return false;
        }
        // 新增意见，点击保存后，置为已提交，防止二次提交
        if(_self._isModifyMind() == false){
            _self._hasCommited = true;
        }
		var mindContent = $("#mind-content").val();
		if (mindContent == undefined || mindContent.length <= 0) {
			navigator.notification.alert("意见不能为空!",null,'提示', '确定');
			return false;
		}
		_self._saveMind();

	});
	
	//固定意见输入框的大小
	$("#mind-content").on("change",this,function(event) {
		event.preventDefault();
		event.stopImmediatePropagation();
	}).on("input",this,function(event) {
		event.preventDefault();
		event.stopImmediatePropagation();
	}).on("keyup",this,function(event) {
		event.preventDefault();
		event.stopImmediatePropagation();
	});
};
/**
 * 给元素绑定事件
 */
mb.vi.mind.prototype._afterLoad = function () {
	// 如果没有意见输入框也没有意见列表，隐藏掉意见分组框
	this._hideMindGroup();
};
// 隐藏意见分组框  TODO 由流程控制
mb.vi.mind.prototype._hideMindGroup = function () {
	var _self = this;
	if (jQuery(_self._pCon).children("li").length <= 0) { // 如果既没有输入框也没有列表
		jQuery("#" + _self._dataId + "_MIND").hide(); // 隐藏掉意见分组框
	}
};

/**  原来的意见
mb.vi.mind.prototype._initMindBtn = function () {
	var _self = this;
	if (_self._generalMind) {
		_self._pCon.append("<li class='ui-field-contain ui-body ui-body-a'><div class='ui-grid-a'><div class='ui-block-a'><a href='#' id='save-btn' class='js-btn-save-contain ui-btn ui-corner-all'>保存意见</a></div><div class='ui-block-b'><a href='#' id='general-mind' class='js-general-mind-contain ui-btn ui-corner-all'>常用批语</a></div></div></li>");
	} else {
		_self._pCon.append("<li class='ui-field-contain ui-body ui-body-a'><div class='ui-grid-solo'><div class='ui-block-a'><a href='#' id='save-btn' class='js-btn-save-contain ui-btn ui-corner-all'>保存意见</a></div></div></li>");
	}
	
	//绑定常用批语弹出事件
	$("#general-mind").on("vclick",_self,function(event) {
		event.preventDefault();
		event.stopImmediatePropagation();
		var popupWrp = $("<div data-role='popup' id='generalMind_popup' data-dismissible='false'> <a href='#' class='sc-btn-close ui-btn ui-corner-all ui-shadow ui-btn-a ui-icon-delete ui-btn-icon-notext ui-btn-right'>Close</a></div>");
		var popupListView = $("<ul data-role='listview' data-inset='true' style='min-width:210px;'><li data-role='divider'>常用批语</li></ul>").appendTo(popupWrp);
		if (_self._generalMind) {
			$.each(_self._generalMind, function(index, item) {
				if (index>=5) {
					return false;
				}
				var $liWrapper = $("<li></li>");
				var mindItem = $("<a href='#'></a>").appendTo($liWrapper);
				mindItem.text(item.TITLE);
				$liWrapper.appendTo(popupListView);
				mindItem.bind("vclick",function() {
					$("#mind-content").val(mindItem.text());
				});
			});
		}
		popupWrp.on("vclick","a.sc-btn-close",function(){
			popupWrp.popup("close");
		})
		popupWrp.on( "popupafterclose", function() {
			$(this).remove();
		});
		$.mobile.activePage.append(popupWrp);//.trigger("create");
		popupWrp.popup().enhanceWithin();
		popupWrp.popup("open");
	});
}
*/



mb.vi.mind.prototype._initMindBtn = function () {
	var _self = this;
	if (_self._generalMind) {
		_self._pCon.append("<li class='ui-field-contain ui-body ui-body-a'><div class='ui-grid-a'><div class='ui-block-a'><a href='#' id='save-btn' class='js-btn-save-contain ui-btn ui-corner-all'>保存意见</a></div><div class='ui-block-b'><a href='#' id='general-mind' class='js-general-mind-contain ui-btn ui-corner-all'>常用批语</a></div></div></li>");
	} else {
		_self._pCon.append("<li class='ui-field-contain ui-body ui-body-a'><div class='ui-grid-solo'><div class='ui-block-a'><a href='#' id='save-btn' class='js-btn-save-contain ui-btn ui-corner-all'>保存意见</a></div></div></li>");
	}
	
	//绑定常用批语弹出事件
	$("#general-mind").on("vclick",_self,function(event) {
		event.preventDefault();
		event.stopImmediatePropagation();
		_self.popupWrp = $("<div data-role='popup' id='generalMind_popup' data-dismissible='false'> <a href='#' class='sc-btn-close ui-btn ui-corner-all ui-shadow ui-btn-a ui-icon-delete ui-btn-icon-notext ui-btn-right'>Close</a></div>");
                          _self.popupListView = $("<ul style='margin:0;padding: 10px;' data-inset='true' style='min-width:210px;'></ul>").appendTo(_self.popupWrp);
                          _self.popupWrp.append("<span style='min-width:210px;'><a id='prepage' style='color:#000;font-weight:normal;display:none;'>上一页</a>&nbsp;&nbsp;<a id='nextpage' style='color:#000;font-weight:normal;display:none;'>下一页</a></span>");
        if (_self._generalMind) {
            _self.currpage = 0;
            _self.totalpage = _self._generalMind.length%5>0?parseInt((_self._generalMind.length/5))+1:_self._generalMind.length/5;
            $(_self.popupWrp).find("#prepage").bind("vclick",function(event){
                                                    event.preventDefault();
                                                    event.stopImmediatePropagation();
                _self.currpage = _self.currpage-1;
                $("li[id=usualMindLi]").remove();
                _self.showUsualList();
            });
            $(_self.popupWrp).find("#nextpage").bind("vclick",function(event){
                                                     event.preventDefault();
                                                     event.stopImmediatePropagation();
                _self.currpage = _self.currpage+1;
                $("li[id=usualMindLi]").remove();
                _self.showUsualList();
            });
            _self.showUsualList();
			
		}
		_self.popupWrp.on("vclick","a.sc-btn-close",function(){
			_self.popupWrp.popup("close");
        });
		_self.popupWrp.on( "popupafterclose", function() {
			$(this).remove();
		});
		$.mobile.activePage.append(_self.popupWrp);//.trigger("create");
		_self.popupWrp.popup().enhanceWithin();
		_self.popupWrp.popup("open");
	});
}

mb.vi.mind.prototype.showUsualList = function(){
    var _self = this;
    if(_self.currpage<(_self.totalpage-1)){
        $(_self.popupWrp).find("#nextpage").show();
    }else{
        $(_self.popupWrp).find("#nextpage").hide();
    }
    if(_self.currpage>0){
        $(_self.popupWrp).find("#prepage").show();
    }else{
        $(_self.popupWrp).find("#prepage").hide();
    }
    $.each(_self._generalMind, function(index, item) {
           if (index<(_self.currpage+1)*5&&index>=(_self.currpage)*5) {
               var $liWrapper = $("<li id='usualMindLi' style='line-height:35px;list-style:none;text-align:left;'></li>");
               var mindItem = $("<a style='text-decoration:none;color:#000;font-weight:normal;' href='#'></a>").appendTo($liWrapper);
               mindItem.text(item.TITLE);
               $liWrapper.appendTo(_self.popupListView);
               mindItem.bind("vclick",function() {
                    $("#mind-content").val(mindItem.text());
               });
           }
    });
}





//TODO 此处需要支持回调函数
mb.vi.mind.prototype._saveMind = function () {
	var _self = this;
	
	var currMind = _self._wfCard.getMindCodeBean();

	var data = {};
	data["MIND_CODE"] = currMind.CODE_ID;
	data["MIND_CONTENT"] = $("#mind-content").val();
	data["MIND_CODE_NAME"] = currMind.CODE_NAME;
	data["SERV_ID"] = _self._servId;
	data["DATA_ID"] = _self._dataId;
	data["WF_NI_ID"] = _self._wfCard.getNodeInstBean().NI_ID;
	data["WF_NI_NAME"] = _self._wfCard.getNodeInstBean().NODE_NAME;
	data["MIND_DIS_RULE"] = currMind.MIND_DIS_RULE; //显示规则
	data["MIND_TYPE"] = "1"; //1:文字意见;2:手写意见
	data["S_FLAG"] = 2; //启用标志
	data["MOBILE_FLAG"] = "1"; // 是手机端填写
		
	if(_self.pkCode) {//如果存在则更新，否则保存
		data["_PK_"] = _self.pkCode;
	}
	
	FireFly.doAct("SY_COMM_MIND", "save", data).done(function(result){
		var rtnMsg = result[UIConst.RTN_MSG];
		if (StringUtils.startWith(rtnMsg, UIConst.RTN_OK) || StringUtils.startWith(rtnMsg, UIConst.RTN_WARN)) {
			_self.pkCode = result["MIND_ID"];
			_self.oldMindText = result["MIND_CONTENT"] || "";
			if($("#" + _self.pkCode ).length) {
				var $mindText = $("#" + _self.pkCode ).find(".sc-mind-text");
				$mindText.html(result["MIND_CONTENT"]);
			} else {
			//var newMind = _self._renderMindBean(result,true);
			//$(".js-mind-input-contain").before(newMind);
			//$(".js-btn-save-contain").parent().listview("refresh");
				_self._wfCard._parHandler._refresh();
			}
			// zjx -- 重新判断是否为必填意见
			_self._wfCard._mindMust();
		} else {
			navigator.notification.alert("意见保存失败，请检查!",null,'提示', '确定');
		}
	});	
}