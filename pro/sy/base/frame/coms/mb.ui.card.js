/** 手机卡片页面form组件 */
GLOBAL.namespace("mb.ui");
mb.ui.card = function(options) {
   var defaults = {
		"id":options.pId + "-card",
		"act":"",
		"sId":"",
		"pCon":null,
		"pId":"",
		"parHandler":null,
		"readOnly":false,
		"data":null
   };
   this.opts = jQuery.extend(defaults,options);
   this._id = this.opts.id;
   this._pCon = this.opts.pCon;
   this._pkCode = options[UIConst.PK_KEY] || "";
   this.servId = this.opts.sId; 
   this._data = this.opts.data;//服务定义信息
   this._items = this._data.ITEMS; //字段定义信息
   this.servSrcId = this.opts.data.SERV_SRC_ID;	// 服务src
   this._readOnly = this.opts.readOnly || false;//form只读
   this.redTitle = this.opts.data.SERV_RED_HEAD;	// 红头标题
   if (this.redTitle.length > 0) {
       var redHeadObj = jQuery.parseJSON(this.redTitle);
       this.redTitle = redHeadObj.mbTitle;
   }
   
   this.dicts = this.opts.data.DICTS;
   this.origData = null;
   
   this.lTitle = UIConst.ITEM_MOBILE_LTITLE; 					/* 移动列表标题 */
   this.lItem = UIConst.ITEM_MOBILE_LITEM;    					/* 移动列表项 */
   this.cItem = UIConst.ITEM_MOBILE_CITEM;					    /* 移动卡片项 */
   this.cHidden = UIConst.ITEM_MOBILE_CHIDDEN;    				/* 移动卡隐藏项 */
   this.lTime = UIConst.ITEM_MOBILE_LTIME;					    /* 列表时间项 */
   
   this._uItems = {};
   
   this._validations = {}; // 校验信息
   
   this._groups = {}; // 保存分组框信息
};


/*
 * 显示卡片页面，主方法
 */
mb.ui.card.prototype.render = function() {
   this._bldLayout();
};
mb.ui.card.prototype.afterRender = function() {
   this._afterLoad();
};
mb.ui.card.prototype.fillData = function(data) {
    this.origData = data;
   jQuery.each(this._uItems,function(i,n) {
	   var value = data[i];
	   if (n.setValue) {
		   if (data[i + "__NAME"]) {
			   n.setValue(value,data[i + "__NAME"]);
		   } else if (n.type == UIConst.FITEM_ELEMENT_ATTACH) {
			   n.setValue(data);
		   } else {
			   n.setValue(value);
		   }
	   }
   });
};

mb.ui.card.prototype._bldLayout = function() {
   this.mainContainer = jQuery("<div></div>").addClass("mbCard-form");
   this._bldForm();
   this.mainContainer.appendTo(this._pCon);
};
/**
 * 启动校验
 * 只实现正则校验，其它的都转到正则校验，例如：数字、非空等...
 */
mb.ui.card.prototype.validate = function() {
	var _self = this;
	var succuss = true;
	function _valid(item, itemCode, errorMsg) { // 校验处理函数
		if (item && item.obj) {
			var validate = jQuery("#" + itemCode + "-validate", item.obj.parent());
			if (validate.length == 0) {
				item.obj.addClass("error");
				validate = jQuery("<span id='" + itemCode + "-validate' class='errorMsg'></span>");
				item.obj.parent().prepend(validate);
			}
			//(function(msg){
				validate.unbind("click").click(function(){
					_self.opts.parHandler.showTipError(errorMsg);
				});
			//})(errorMsg);
		}
		succuss = false;
	}
	jQuery.each(_self._validations, function(itemCode, rules){
		var item = _self.getItem(itemCode);
		if (item && item.getValue) {
			var value = item.getValue();
			var len = rules.length;
			for (var i = 0; i < len; i++) { // 挨个校验直到第一个校验失败为止
				var regular = rules[i]["regular"];
				var errorMsg = rules[i]["errorMsg"];
				if (regular == "^[\s]{0,}$") { // 非空校验
					if (RegExp(regular).test(value)) { // 校验成功则表示为空
						_valid(item, itemCode, errorMsg);
						return;
					} else {
						item.obj.removeClass("error");
						jQuery("#" + itemCode + "-validate", item.obj.parent()).remove();
					}
				} else { // 其它校验
					if (!RegExp(regular).test(value)) {
						_valid(item, itemCode, errorMsg);
						return;
					} else {
						item.obj.removeClass("error");
						jQuery("#" + itemCode + "-validate", item.obj.parent()).remove();
					}
				}
			}
		}
	});
	return succuss;
};
/**
 * 初始化校验
 * 收集校验规则：{itemCode:[{"regular" : "^\S+$", "message" : "该项必须输入！"}]}
 */
mb.ui.card.prototype._initValidate = function(data) {
	var _self = this;
	/*
	 * 取出必填及校验规则
	 */
	if (data.ITEM_HIDDEN == UIConst.NO) { // 没有隐藏的字段才做校验
		var rules = []; // 校验规则集，可能存在多重校验
		if (data.ITEM_NOTNULL == UIConst.YES) { // 非空
			rules.push({"regular" : "^[\s]{0,}$", "errorMsg" : "该项必须输入！"});
		}
		
		var type = data.ITEM_INPUT_TYPE;
		var fieldType = data.ITEM_FIELD_TYPE;
		var length = data.ITEM_FIELD_LENGTH;
		var inputMode = data.ITEM_INPUT_MODE;
		// 用户手动输入的才需要做校验
		if ((type == UIConst.FITEM_ELEMENT_INPUT || type == UIConst.FITEM_ELEMENT_TEXTAREA) 
				&& inputMode == UIConst.FITEM_INPUT_AUTO) {
			if (fieldType == UIConst.DATA_TYPE_NUM) { // 数字
				var rule = {};
				if (length.indexOf(",") > 0) {// 小数
					var intLength = length.substring(0, length.indexOf(","));
					var decLength = length.substring(length.indexOf(",") + 1);
					rule["regular"] = "^(0|[-+]?[0-9]{1," + (intLength - parseInt(decLength)) + "}([\.][0-9]{0," + decLength + "})?)$";
					rule["errorMsg"] = "请输入整数长度不超过" + (intLength - parseInt(decLength)) + "位，小数长度不超过" + decLength + "位的有效数字！";
				} else {
					rule["regular"] = "^(0|[-+]?[0-9]{0," + length + "})$";
					rule["errorMsg"] = "请输入长度不超过" + length + "位有效数字！";
				}
				rules.push(rule);
			} else if (fieldType == UIConst.DATA_TYPE_STR 		// 字符串
					|| fieldType == UIConst.DATA_TYPE_BIGTEXT) { // 大文本
				var regular = "^([\\S\\s]{0," + length + "})$";
				var message = "长度不能超过" + length + "位！";
				rules.push({"regular" : regular, "errorMsg" : message});
			}
			
			// 正则表达式
			var regular = data.ITEM_VALIDATE;
			// 正则校验失败提示语
			var hint = data.ITEM_VALIDATE_HINT; 
			if (regular && jQuery.trim(regular) != "") { // 正则校验
				rules.push({"regular" : regular, "errorMsg" : (hint ? hint : "")});
			}
		}
		
		if (!jQuery.isEmptyObject(rules)) {
			_self._validations[data.ITEM_CODE] = rules;
		}
	}
};
mb.ui.card.prototype._bindHrClick = function(hrObj) {
  var _self = this;
  if (hrObj.hasClass("mbCard-hr-closeFlag")) {
	  hrObj.next().fadeIn("slow");
	  hrObj.removeClass("mbCard-hr-closeFlag");
	  hrObj.find(".mbCard-hr-close").html(UIConst.FONT_STROKE_close);
  } else {
	  hrObj.next().fadeOut("slow");
	  hrObj.addClass("mbCard-hr-closeFlag");
	  hrObj.find(".mbCard-hr-close").html(UIConst.FONT_STROKE_expand);
  }
  
  
};
mb.ui.card.prototype._bldForm = function() {
    var _self = this;
    //主标题显示
    if (this.redTitle && this.redTitle.length > 0) {
    		jQuery("<div class='mbCard-form-redTitle'>" + this.redTitle + "</div>").appendTo(_self.mainContainer);
    }
    //form体展示
    var closeHrStr = "";//"<span class='mbCard-hr-close fontomasRegular'>" + UIConst.FONT_STROKE_close + "</span>";
    this.set = jQuery("<div></div>").addClass("mbCard-form-set").appendTo(_self.mainContainer);
    this.table = jQuery("<table></table>").addClass("mbCard-form-table mb-shadow-3 mb-radius-9").appendTo(this.set);
    this.hr = jQuery("<div></div>").html("基本信息" + closeHrStr).addClass("mbCard-form-hr");
    this.set.prepend(this.hr);
//	_self.hr.bind("click",function() {
//		_self._bindHrClick(jQuery(this));
//	});
    var count = 0;
    jQuery.each(this._items,function(i,n) {
    	_self._initValidate(n);
    	var type = n.ITEM_INPUT_TYPE;
    	var itemName = n.ITEM_NAME;
    	var itemCode = n.ITEM_CODE;
    	var mbType = n.ITEM_MOBILE_TYPE;
    	var isHidden = n.ITEM_HIDDEN;
    	if (mbType && mbType.length > 0 && mbType < _self.cHidden) {//卡片显示：包括卡片项和列表的展示项
    		if ((count == 0) && (type == UIConst.FITEM_ELEMENT_HR)) {//第一个分组框
    			_self.hr.remove();
    			_self.hr = jQuery("<div id='" + itemCode + "'></div>").html(itemName + closeHrStr).addClass("mbCard-form-hr");
    			_self.set.prepend(_self.hr);
//    			_self.hr.bind("click",function() {
//    				_self._bindHrClick(jQuery(this));
//    			});
    			count++;
    			_self._groups[itemCode] = type;
    			if (isHidden == 1) {
    				_self.set.hide();
    			}
    			return true;    		
    		}
    		if ((count > 0) && (type == UIConst.FITEM_ELEMENT_HR)) {//分组框
    			_self.set = jQuery("<div id='" + itemCode + "'></div>").addClass("mbCard-form-set").appendTo(_self.mainContainer);
    			_self.table = jQuery("<table></table>").addClass("mbCard-form-table mb-shadow-3 mb-radius-9").appendTo(_self.set);
    			_self.hr = jQuery("<div></div>").html(itemName + closeHrStr).addClass("mbCard-form-hr");
    			_self.set.prepend(_self.hr);
//    			_self.hr.bind("click",function() {
//    				_self._bindHrClick(jQuery(this));
//    			});
    			count++;
    			_self._groups[itemCode] = type;
    			if (isHidden == 1) {
    				_self.set.hide();
    			}
    			return true;
    		}
    		if (type == UIConst.FITEM_ELEMENT_MIND) {//意见分组框
    			_self.set = jQuery("<div id='" + itemCode + "'></div>").addClass("mbCard-form-set").appendTo(_self.mainContainer);
    			_self.table = jQuery("<div></div>").addClass("mbCard-form-table mb-shadow-3 mb-radius-9").appendTo(_self.set);
    			_self.hr = jQuery("<div></div>").html(itemName + closeHrStr).addClass("mbCard-form-hr");
    			_self.set.prepend(_self.hr);
    			_self._groups[itemCode] = type;
    			if (isHidden == 1) {
    				_self.set.hide();
    			}
    			return true;
    		}
    		var rtnTr = _self._bldTr(n);
    		if (rtnTr) {
    			if (isHidden == 1) {//隐藏字段自动隐藏
    				rtnTr.hide();
    			}
    			rtnTr.appendTo(_self.table);
    		}
    		count++;
    	}
    });
};
//获取分组框方法，mind.js里会用到，暂时无用
mb.ui.card.prototype.getGroup = function(itemCode, inputType) {
	if (!inputType) {
		return jQuery("#" + itemCode).last();
	} else {
		// 查找意见分组框
		for (var itemCode in this._groups) {
			var type = this._groups[itemCode];
			if (type == UIConst.FITEM_ELEMENT_MIND) {
				return this.getGroup(itemCode);
			}
		}
	}
	return null;
};
mb.ui.card.prototype._bldTr = function(item) {
	var _self = this;
    var tr = jQuery("<tr></tr>").addClass("mbCard-form-leftTr");
    var leftTd = jQuery("<td></td>").addClass("mbCard-form-leftTd").appendTo(tr);
    var rightTd = jQuery("<td></td>").addClass("mbCard-form-rightTd").appendTo(tr);
   
	// 输入框类型
	var type = item.ITEM_INPUT_TYPE;
	// 输入模式
	var inputMode = item.ITEM_INPUT_MODE;
	// 必填
	var notNull = item.ITEM_NOTNULL;
	var itemCode = item.ITEM_CODE;
	var itemName = item.ITEM_NAME;
	//构造lable
	_self._label({"id":"","item":item}).appendTo(leftTd);
	//构造输入框
	var ui;
	switch (type) {
	case UIConst.FITEM_ELEMENT_INPUT ://输入框
		if (inputMode == UIConst.FITEM_INPUT_AUTO) {//输入框
			var opts = {
					"id":_self._id + "-" + itemCode,
					"item":item
				}
				ui = new mb.ui.input(opts);
				_self._uItems[itemCode] = ui;
				ui.obj.appendTo(rightTd);
		} else if (inputMode == UIConst.FITEM_INPUT_DICT) {//字典
			var opts = {
					"id":_self._id + "-" + itemCode,
					"item":item,
					"data":_self.dicts[item.DICT_ID],
					"dictId":item.DICT_ID
				}
			ui = new mb.ui.dict(opts);
			_self._uItems[itemCode] = ui;
			ui.obj.appendTo(rightTd);
		} else if (inputMode == UIConst.FITEM_INPUT_DATE) {//日期选择
			var opts = {
					"id":_self._id + "-" + itemCode,
					"item":item
				}
			ui = new mb.ui.DateInput(opts);
			_self._uItems[itemCode] = ui;
			ui.obj.appendTo(rightTd);
		}		break;
	case UIConst.FITEM_ELEMENT_RADIO ://单选
		var opts = {
			"id":_self._id + "-" + itemCode,
			"item":item,
			"data":_self.dicts[item.DICT_ID]
		}
		ui = new mb.ui.radioBoxGroup(opts);
		_self._uItems[itemCode] = ui;
		ui.obj.appendTo(rightTd);
		break;
	case UIConst.FITEM_ELEMENT_CHECKBOX ://多选
		var opts = {
			"id":_self._id + "-" + itemCode,
			"item":item,
			"data":_self.dicts[item.DICT_ID]
		}
		ui = new mb.ui.checkBoxGroup(opts);
		_self._uItems[itemCode] = ui;
		ui.obj.appendTo(rightTd);
		break;
	case UIConst.FITEM_ELEMENT_SELECT ://下拉框
		var opts = {
			"id":_self._id + "-" + itemCode,
			"item":item,
			"data":_self.dicts[item.DICT_ID]
		}
		ui = new mb.ui.select(opts);
		_self._uItems[itemCode] = ui;
		ui.obj.appendTo(rightTd);
		break;
	case UIConst.FITEM_ELEMENT_TEXTAREA ://大文本
		var opts = {
			"id":_self._id + "-" + itemCode,
			"item":item
		}
		ui = new mb.ui.textArea(opts);
		_self._uItems[itemCode] = ui;
		ui.obj.appendTo(rightTd);	
		break;
	case UIConst.FITEM_ELEMENT_FILE ://文件
		var opts = {
			"id":_self._id + "-" + itemCode,
			"item":item
		}
		ui = new mb.ui.file(opts);
		_self._uItems[itemCode] = ui;
		ui.obj.appendTo(rightTd);	
		break;
	case UIConst.FITEM_ELEMENT_ATTACH ://上传文件
		var opts = {
			"id":_self._id + "-" + itemCode,
			"item":item,
			"servId":_self.servId,
			"servSrcId":_self.servSrcId
		}
		ui = new mb.ui.attach(opts);
		ui.type = UIConst.FITEM_ELEMENT_ATTACH;
		_self._uItems[itemCode] = ui;
		ui.obj.appendTo(rightTd);
		break;
	case UIConst.FITEM_ELEMENT_IMAGE ://图片
		var opts = {
			"id":_self._id + "-" + itemCode,
			"item":item
		}
		ui = new mb.ui.img(opts);
		_self._uItems[itemCode] = ui;
		ui.obj.appendTo(rightTd);
		break;
	case UIConst.FITEM_ELEMENT_PSW ://密码框
		var opts = {
			"id":_self._id + "-" + itemCode,
			"item":item
		}
		ui = new mb.ui.pwd(opts);
		_self._uItems[itemCode] = ui;
		ui.obj.appendTo(rightTd);	
		break;
	case UIConst.FITEM_ELEMENT_STATICTEXT://静态显示文本区 
		var opts = {
			"id":_self._id + "-" + itemCode,
			"item":item
		}
		ui = new mb.ui.staticText(opts);
		_self._uItems[itemCode] = ui;
		ui.obj.appendTo(rightTd);	
		break;
	default :
    }
	//必填项的支持
	if (notNull == UIConst.STR_YES) {
		jQuery("<span>*</span>").addClass("mbCard-form-notNull mb-icon-notNull").appendTo(leftTd);
	}
	return tr;
};
mb.ui.card.prototype._label = function(options) {
    var defaults = {
			"id":"",
			"item":null			
	};
    var opts = jQuery.extend(defaults,options);
    var itemName = opts.item.ITEM_NAME || "";
    itemName = itemName.replace(/\&nbsp;/g, "").replace(/\&nbsp/g, "");
	var label = jQuery("<label></label>").addClass("mbCard-form-label").text(itemName); 
	return label;
};
mb.ui.card.prototype._afterLoad = function() {
	//只读控制
	if (this._readOnly === true || this._readOnly === "true") {
		var len = this._uItems.length;
		jQuery.each(this._uItems, function(i,item) {
			item.disabled();
		});
	}
	//绑定事件
    this._clickDo();
};
/**
 * 所有都只读
 */
mb.ui.card.prototype.disabledAll = function() {
	jQuery.each(this._uItems, function(i,item) {
		item.disabled();
	});
};
/**
 * 设置某个字段必填
 * @parem itemCode 字段ID
 * @param bool 是否必填
 */
mb.ui.card.prototype.setNotNull = function(itemCode, bool) {
	var rules = this._validations[itemCode];
	var item = this.getItem(itemCode);
	if (bool) {
		if (jQuery.isEmptyObject(rules)) { // 该字段不存在任何校验
			rules = [];
			rules.push({"regular" : "^[\s]{0,}$", "errorMsg" : "该项必须输入！"});
			this._validations[itemCode] = rules;
		} else {
			var len = rules.length;
			var must = false;
			for (var i = 0; i < len; i++) { // 遍历查找是否已经存在必须输入校验
				var rule = rules[i];
				if (rule["regular"] == "^[\s]{0,}$") {
					must = true;
				}
			}
			if (!must) {
				rules.push({"regular" : "^[\s]{0,}$", "errorMsg" : "该项必须输入！"});
			}
		}
		
		if (item) { // 添加必填星号
			var leftTd = item.obj.parent().prev();
			if (leftTd && leftTd.find(".mbCard-form-notNull").length == 0) {
				jQuery("<span>*</span>").addClass("mbCard-form-notNull mb-icon-notNull").appendTo(leftTd);
			}
		}
	} else {
		if (!jQuery.isEmptyObject(rules)) {
			var len = rules.length;
			var rule;
			var tmpRules = [];
			for (var i = 0; i < len; i++) { // 遍历查找是否已经存在必须输入校验，存在则删除
				rule = rules[i];
				if (rule["regular"] == "^[\s]{0,}$") {
					continue;
				}
				tmpRules.push(rule);
			}
			this._validations[itemCode] = tmpRules;
		}
		
		if (item) { // 移除必填星号
			var leftTd = item.obj.parent().prev();
			if (leftTd) {
				leftTd.find(".mbCard-form-notNull").remove();
			}
		}
	}
};
mb.ui.card.prototype._clickDo = function() {
   this.mainContainer.bind("click",function(event) {
	   var tar = event.target;
	   if (jQuery(tar).hasClass("mb-select-a")) {
		   
	   } else {
		   jQuery(".mb-select-active").removeClass("mb-select-active");
		   
	   }
   });
};
mb.ui.card.prototype.getModifyData = function() {
	var _self = this;
	var modiData = {};
	if (_self.origData) {//有初始数据
		var origData = _self.origData;
		jQuery.each(_self._uItems,function(i,n) {
			var origValue = origData[i];
			if (n.getValue() == origValue) {
				
			} else {
				modiData[i] = n.getValue();
			}
		});
		
	} else {
		jQuery.each(this._uItems,function(i,n) {
			modiData[i] = n.getVlaue();
		});		
	}
    return modiData;
};
mb.ui.card.prototype.getItem = function(itemCode) {
	var _self = this;
    var ui = _self._uItems[itemCode];
    return ui;
};
mb.ui.card.prototype.itemValue = function(itemCode) {
	var _self = this;
    var ui = _self.getItem(itemCode);  
    if (ui) {
    	return ui.getValue();
    } else if(this.origData[itemCode]){ 
    	//如果没有找到对应的Item则从byid中找相应的值
    	return this.origData[itemCode];
    } else {
    	return "";
    }
};
mb.ui.card.prototype.getAllItems = function() {
	var _self = this;
	return _self._uItems;
};
mb.ui.card.prototype.getAllItemsValue = function() {
	var _self = this;
	var jsonObj = {};
	var items = this.getAllItems();
	for (var i in items) {//遍历所有表单域
		var item = items[i];
		var curVal = item.getValue();//当前值
		if (!curVal) { //如果curVal为空或undefined
			curVal = "";
		}
		jsonObj[i] = curVal;
	}
	return jsonObj;
};
mb.ui.card.prototype.hideAll = function() {
	var _self = this;
	var items = this.getAllItems();
	for (var i = 0; i < items.length; i++) {
		items[i].obj.hide();
	}
};


/**
 * 获取修改了的item，拼接成json对象
 */
mb.ui.card.prototype.getChangedItems = function() {
	var jsonObj = {};
	var tmpData = this.origData;
	var items = this.getAllItems();
	for (var key in items) {//遍历所有表单域
		var item = items[key];
		if (item.type == "File" || item.type == "StaticText") {// Attach类型附件或者静态文本不通过Form提交
			continue;
		}
		var theVal = tmpData[key];// 原始值
		var curVal = item.getValue();// 当前值
		if (theVal == undefined) {
			if (curVal) {
				//如果item返回对象，直接放入jsonObj中
				if (typeof(curVal) == "object") {
					jsonObj = jQuery.extend(jsonObj,curVal); 
					continue;
				}
				jsonObj[key] = curVal;
			}
		} else if (theVal != curVal) {//如果表单有变化，且不是从undefined变化而来的
			if (!curVal) { //如果curVal为空或undefined
				curVal = "";
			}
			jsonObj[key] = curVal;
		}
	}
	return jsonObj;
};

/**
 * 获取UI对象容器
 */
mb.ui.card.prototype.getItems = function() {
	return this._items;
};
//==================分组框======================
/*
 * Demo:
 * var group = new mb.ui.group({"id":"test","name":"test","pCon":_viewer.form.mainContainer});
 * group.render();
 * var test = jQuery("<div>test11</div>");
 * group.append(test);
 */
mb.ui.group = function(options) {
	var _self = this;
    var defaults = {
			"id":"mb-group",
			"name":"分组框", //显示的标题
			"type":"div",//talbe:表示内次为talbe构造
			"pCon":null
	};
    var opts = jQuery.extend(defaults,options);
    this.pCon = opts.pCon;
    var type = opts.type;
    var item = opts.item;
    var itemCode = opts.id;
    var itemName = opts.name;
    var set = jQuery("<div id='" + itemCode + "'></div>").addClass("mbCard-form-set");
    var table = jQuery("<table></table>");
    if (type == "div") {
    	talbe = jQuery("<div></div>");
    }
    table.addClass("mbCard-form-table mb-shadow-3 mb-radius-9").appendTo(set);
    var hr = jQuery("<div></div>").html(itemName).addClass("mbCard-form-hr");
    set.prepend(hr);
	this.obj = set; 
};
mb.ui.group.prototype.render = function() {
	this.obj.appendTo(this.pCon);
}
mb.ui.group.prototype.getObj = function() {
	return this.obj;
}
mb.ui.group.prototype.getSubObj = function() {
	return this.obj.find(".mbCard-form-table");
};
mb.ui.group.prototype.append = function(con) {
	this.obj.find(".mbCard-form-table").append(con);
};
//==================普通输入框======================
mb.ui.input = function(options) {
    var defaults = {
			"id":"",
			"type":"text"
	};
    var opts = jQuery.extend(defaults,options);
    var item = opts.item;
	//this.obj = jQuery("<input type='" + opts.type + "' value=''></input>").attr("id",opts.id).addClass("mbCard-form-input"); 
	this.obj = jQuery("<textarea></textarea>").attr("id",opts.id).addClass("mbCard-form-input"); 

};
mb.ui.input.prototype.getContainer = function() {
	return this.obj.parent().parent();
};
mb.ui.input.prototype.getLen = function (str){
	if (!str) {
		return 0;
	}
	return str.replace(/[^\x00-\xff]/gi,'aa').length;
};
mb.ui.input.prototype.setValue = function(value) {
	this.obj.val(value);
	var widthStr = this.obj.css("width");
	var width = Format.substr(0,widthStr.length-2,widthStr);
	width = width - 2; //textarea的宽度
	var count = 1;//默认行数
	var characters = this.getLen(value);//转换后的字符个数
	var words = 18*characters/2; //文字的总像素值
	if(width > 0){
		count = Math.ceil(words/width);
		if (count == 0) {
			count = 1;
		}
	}
	var heiAll = count*30;
	this.obj.css("height",heiAll+"px");
	this.obj.css("line-height","30px");
};
mb.ui.input.prototype.getValue = function() {
	return this.obj.val();
};
mb.ui.input.prototype.disabled = function() {
	return this.obj.prop("disabled",true).addClass("mbCard-item-disabled");
};
mb.ui.input.prototype.enabled = function() {
	this.obj.prop("disabled",false).removeClass("mbCard-item-disabled");
	return this.obj;
};
//==================大文本框======================
mb.ui.textArea = function(options) {
    var defaults = {
			"id":""
	};
    var opts = jQuery.extend(defaults,options);
    var item = opts.item;
	this.obj = jQuery("<textarea></textarea>").attr("id",opts.id).addClass("mbCard-form-textArea"); //mb-box-shadow-inset mb-radius-9

};
mb.ui.textArea.prototype.getContainer = function() {
	return this.obj.parent().parent();
};
mb.ui.textArea.prototype.setValue = function(value) {
	this.obj.val(value);
};
mb.ui.textArea.prototype.getValue = function() {
	return this.obj.val();
};
mb.ui.textArea.prototype.disabled = function() {
	this.obj.prop("disabled",true).addClass("mbCard-item-disabled");
	return this.obj;
};
mb.ui.textArea.prototype.enabled = function() {
	return this.obj.prop("disabled",false).removeClass("mbCard-item-disabled");
};
//==================日期输入框======================
mb.ui.DateInput = function(options) {
    var defaults = {
			"id":"",
			"type":"date"
	};
    var opts = jQuery.extend(defaults,options);
    var type = opts.type;
    var item = opts.item;
    if (type == "DATETIME") {
    	type = "datetime-local";
    } else if (type == "MONTH") {
    	type = "datetime-local";
    } else if (type == "DATEHM") {
    	type = "time";
    }
	this.obj = jQuery("<input type='" + type + "' value=''></input>").attr("id",opts.id).addClass("mbCard-form-input"); // mb-box-shadow-inset mb-radius-9
	this.obj.bind("click",function() {
		
	});

};
mb.ui.DateInput.prototype.getContainer = function() {
	return this.obj.parent().parent();
};
mb.ui.DateInput.prototype.setValue = function(value) {
	this.obj.val(value);
};
mb.ui.DateInput.prototype.getValue = function() {
	return this.obj.val();
};
mb.ui.DateInput.prototype.disabled = function() {
	this.obj.prop("disabled",true).addClass("mbCard-item-disabled");
	return this.obj;
};
mb.ui.DateInput.prototype.enabled = function() {
	return this.obj.prop("disabled",false).removeClass("mbCard-item-disabled");
};
//==================单选输入框======================
mb.ui.radioBoxGroup = function(options) {
	var _self = this;
    var defaults = {
			"id":"",
			"data":null//字典数据
	};
    var opts = jQuery.extend(defaults,options);
    var item = opts.item;
    var data = opts.data;
    this.id = opts.id;
    
    var len = data.length - 1;
    var group = jQuery("<div></div>").addClass("mb-radioGroup");

    jQuery.each(data,function(i,n) {
    	var id = n.ID;
    	var unitId = _self.id + "-" + id;
    	var labelId = unitId + "-label";
    	
    	var radioDiv = jQuery("<div></div>").addClass("mb-radio").appendTo(group);
    	var radioInput = jQuery("<input type='radio'/>").attr({"value":id,"name":unitId,"id":unitId}).addClass("mb-radio-input").appendTo(radioDiv);
    	var radioLabel = jQuery("<label type='radio'></label>").attr({"for":unitId,"id":labelId}).addClass("mb-radio-label").appendTo(radioDiv);
    	if (i == 0) {
    		radioLabel.addClass("mb-radius-left6");
    	} else if (i == len) {
    		radioLabel.addClass("mb-radius-right6");
    	}
    	var radioBtnInner = jQuery("<span></span>").addClass("mb-btn-inner").appendTo(radioLabel);
    	var radioBtnText = jQuery("<span></span>").addClass("mb-btn-text").text(n.NAME).appendTo(radioBtnInner);

    	radioLabel.bind("click", function(event) {
    		_self.setValue(id);
    	});
    });
	this.obj = group; 
};
mb.ui.radioBoxGroup.prototype.getContainer = function() {
	return this.obj.parent().parent();
};
mb.ui.radioBoxGroup.prototype.setValue = function(value) {
	var unitId = this.id + "-" + value;
	var labelId = unitId + "-label";
	this.obj.find(".mb-radio-labelActive").removeClass("mb-radio-labelActive");
	this.obj.find("#" + labelId).addClass("mb-radio-labelActive");
	this.obj.find(".mb-radio-input").removeAttr("CHECKED");
	this.obj.find("#" + unitId).attr("CHECKED","checked");
};
mb.ui.radioBoxGroup.prototype.getValue = function() {
	var res = this.obj.find(".mb-radio-input[CHECKED=checked]").attr("value");
	return res || '';
};
mb.ui.radioBoxGroup.prototype.disabled = function() {
	this.obj.addClass("mbCard-item-disabled").find(".mb-radio-label").unbind("click");
};
mb.ui.radioBoxGroup.prototype.enabled = function() {
	this.obj.removeClass("mbCard-item-disabled");
};
//==================多选输入框======================
mb.ui.checkBoxGroup = function(options) {
	var _self = this;
    var defaults = {
			"id":"",
			"data":null//字典数据
	};
    var opts = jQuery.extend(defaults,options);
    var item = opts.item;
    var data = opts.data;
    this.id = opts.id;
    
    var len = data.length - 1;
    var group = jQuery("<div></div>").addClass("mb-radioGroup");
//    group.bind("click",function() {
//    	_self.getValue();
//    });
    jQuery.each(data,function(i,n) {
    	var id = n.ID;
    	var unitId = _self.id + "-" + id;
    	var labelId = unitId + "-label";
    	
    	var radioDiv = jQuery("<div></div>").addClass("mb-radio").appendTo(group);
    	var radioInput = jQuery("<input type='radio'/>").attr({"value":id,"name":unitId,"id":unitId}).addClass("mb-radio-input").appendTo(radioDiv);
    	var radioLabel = jQuery("<label type='radio'></label>").attr({"for":unitId,"id":labelId}).addClass("mb-radio-label").appendTo(radioDiv);
    	if (i == 0) {
    		radioLabel.addClass("mb-radius-left6");
    	} else if (i == len) {
    		radioLabel.addClass("mb-radius-right6");
    	}
    	var radioBtnInner = jQuery("<span></span>").addClass("mb-btn-inner").appendTo(radioLabel);
    	var radioBtnText = jQuery("<span></span>").addClass("mb-btn-text").text(n.NAME).appendTo(radioBtnInner);

    	radioLabel.bind("click", function(event) {
    		if (jQuery(this).hasClass("mb-radio-labelActive")) {
    			_self.dsetValue(id);
    		} else {
    			_self.setValue(id);
    		}
    		
    	});
    });
	this.obj = group;
};
mb.ui.checkBoxGroup.prototype.getContainer = function() {
	return this.obj.parent().parent();
};
mb.ui.checkBoxGroup.prototype.setValue = function(value) {
	var unitId = this.id + "-" + value;
	var labelId = unitId + "-label";
	this.obj.find("#" + labelId).addClass("mb-radio-labelActive");
	this.obj.find("#" + unitId).attr("CHECKED","checked");
};
mb.ui.checkBoxGroup.prototype.dsetValue = function(value) {
	var unitId = this.id + "-" + value;
	var labelId = unitId + "-label";
	this.obj.find("#" + labelId).removeClass("mb-radio-labelActive");
	this.obj.find("#" + unitId).removeAttr("CHECKED");
};
mb.ui.checkBoxGroup.prototype.getValue = function() {
	var res = this.obj.find(".mb-radio-input[CHECKED=checked]");
	var resArray = [];
	jQuery.each(res,function(i,n) {
		
		resArray.push(jQuery(n).attr("value"));
	});
	return resArray.join(",");
};
mb.ui.checkBoxGroup.prototype.disabled = function() {
	this.obj.addClass("mbCard-item-disabled").find(".mb-radio-label").unbind("click");
};
mb.ui.checkBoxGroup.prototype.enabled = function() {
	this.obj.removeClass("mbCard-item-disabled");
};
//==================下拉选择框======================
mb.ui.select = function(options) {
	var _self = this;
    var defaults = {
			"id":"",
			"data":null//字典数据
	};
    var opts = jQuery.extend(defaults,options);
    var item = opts.item;
    this._data = opts.data;
    this.id = opts.id;
    
    this.name = item.ITEM_NAME;
    var group = jQuery("<div></div>").addClass("mb-select");
    this.a = jQuery("<a></a>").addClass("mb-select-a").appendTo(group);
    var inner = jQuery("<span></span>").addClass("mb-select-inner").appendTo(this.a);
    this.input = jQuery("<input type='hidden'></input>").addClass("mb-select-input").appendTo(inner);
    this.text = jQuery("<span></span>").addClass("mb-select-text").appendTo(inner);
    var icon = jQuery("<span></span>").addClass("mb-select-icon mb-down-nav mb-radius-18").appendTo(inner);
	this.obj = group;
	this.bindClick();
};
mb.ui.select.prototype.bindClick = function() {
	var _self = this;
    this.obj.unbind("click").bind("click",function(event) {
		jQuery(".mb-select-active").removeClass("mb-select-active");
		_self.a.addClass("mb-select-active");
		var opts = {
				"id":_self.id,
				"data":_self._data,
				"title":_self.name,
				"parHandler":_self
			}
		var ui = new mb.ui.dialog(opts);
		ui.render();
    });
};
mb.ui.select.prototype.getContainer = function() {
	return this.obj.parent().parent();
};
mb.ui.select.prototype.reSetDict = function(dictId){ //替换字典数据
	var _self = this;
	_self._data = FireFly.getDict(dictId)[0].CHILD;
};
mb.ui.select.prototype.afterSetValue = function(){}; //change事件
mb.ui.select.prototype.setValue = function(value,text,neglectAfterSetValue) {
	var _self = this;
	var unitId = this.id + "-" + value;
	var labelId = unitId + "-label";
	
	
	if (text != "undefined") { //如果传入值是 ""，那么这里就不会将""传入进去，故修改  hdy 2013年9月22日 09:28
		_self.input.val(value);
		_self.text.text(text);
	} else {
		var len = 0;
		if(this._data){
			len = this._data.length;
		}
		for (var i;i < len;i++) {
			var obj = _self._data[i];
			if (obj.ID == value) {
				_self.input.val(value);
				_self.text.text(obj.NAME);
				break;
			}
		}
	}
	if (!neglectAfterSetValue) { //是否忽略 调用保存后方法
		this.afterSetValue();
	}
};
mb.ui.select.prototype.getValue = function() {
	var _self = this;
    return _self.input.val();
};
mb.ui.select.prototype.getText = function() {
	var _self = this;
	return _self.text.val();
};
mb.ui.select.prototype.setActive = function() {
	var _self = this;
};
mb.ui.select.prototype.disabled = function() {
	this.obj.addClass("mbCard-item-disabled").unbind("click");
};
mb.ui.select.prototype.enabled = function() {
	this.obj.removeClass("mbCard-item-disabled");
	this.bindClick();
};
//==================文件查看======================
mb.ui.file = function(options) {
	var _self = this;
    var defaults = {
			"id":"",
			"data":null//字典数据
	};
    var opts = jQuery.extend(defaults,options);
    var item = opts.item;
    this._data = opts.data;
    this.id = opts.id;
    
    var name = item.ITEM_NAME;
    var group = jQuery("<div></div>").addClass("mb-file");
    var icon = jQuery("<span></span>").addClass("mb-file-icon").appendTo(group);
    this.a = jQuery("<a href='#' target='_blank'></a>").addClass("mb-file-a").appendTo(group);
	this.obj = group;
};
mb.ui.file.prototype.getContainer = function() {
	return this.obj.parent().parent();
};
mb.ui.file.prototype.setValue = function(value) {
	var _self = this;
	if (value) {
		var array = value.split(";");
		for (i = 0; i< array.length;i++) {
			if (array[i].length > 0) {
				var fileValue = array[i].split(",");
				var href = "/file/" + fileValue[0];
				_self.a.attr("href",href);
				_self.a.text(fileValue[1]);
			}
		}
	}
};
mb.ui.file.prototype.getValue = function() {
	var _self = this;
    return _self.a.text();
};
mb.ui.file.prototype.disabled = function() {
};
mb.ui.file.prototype.enabled = function() {
};
//==================自定义文件查看======================
mb.ui.attach = function(options) {
	var _self = this;
    var defaults = {
			"id":"",
			"data":null,//字典数据
			"servId":""
	};
    this.opts = jQuery.extend(defaults,options);
    var item = this.opts.item;
    this._data = this.opts.data;
    this.id = this.opts.id;
    this.itemCode = item.ITEM_CODE;
    this.servId = this.opts.servId;
    this.servSrcId = this.opts.servSrcId;
    
    var name = item.ITEM_NAME;
    var group = jQuery("<div></div>").addClass("mb-file");
    var icon = jQuery("<span></span>").addClass("mb-file-icon").appendTo(group);
    //this.a = jQuery("<a href='#' target='_blank'></a>").addClass("mb-file-a").appendTo(group);
	this.obj = group;
	
	this._fileData = {}; // 文件数据
	this._fileIds = ""; // 文件ID集合
};
mb.ui.attach.prototype.getContainer = function() {
	return this.obj.parent().parent();
};
mb.ui.attach.prototype.setValue = function(byIdData) {
	var _self = this;
	// 载入附件
	var servDataId = byIdData._PK_;
	var servSrcId = this.servSrcId;
	var fileData = FireFly.getCardFile(servSrcId, servDataId, this.itemCode);
	var fileIds = [];
	for (i = 0; i< fileData.length;i++) {
		var item = fileData[i];
		var href = "/file/" + item.FILE_ID;
		fileIds.push(item.FILE_ID); 
		this._fileData[item.FILE_ID] = item;
		var aItem = jQuery("<div style='padding:6px 0 6px 0;'><span class='mb-file-sort'>" + (i + 1) + "、</span><a href='" + href + "' class='mb-file-a'>" + item.FILE_NAME + "</a></div>");
		aItem.appendTo(this.obj);
	}
	if (this._fileIds.length > 0) {
		this._fileIds += "," + fileIds.join(",");
	} else {
		this._fileIds = fileIds.join(",");
	}
};
mb.ui.attach.prototype.getValue = function() {
	var _self = this;
    return _self._fileIds;
};
mb.ui.attach.prototype.disabled = function() {
};
mb.ui.attach.prototype.enabled = function() {
};
//==================图片显示======================
mb.ui.img = function(options) {
	var _self = this;
    var defaults = {
			"id":"",
			"data":null//字典数据
	};
    var opts = jQuery.extend(defaults,options);
    var item = opts.item;
    this._data = opts.data;
    this.id = opts.id;
    
    var name = item.ITEM_NAME;
    var group = jQuery("<div></div>").addClass("mb-img-div");
    this.img = jQuery("<img></img>").addClass("mb-img").appendTo(group);
    this.img.attr({width:"80px",height:"80px"});
	this.obj = group;
};
mb.ui.img.prototype.getContainer = function() {
	return this.obj.parent().parent();
};
mb.ui.img.prototype.setValue = function(value) {
	var array = value.split(",");
	var src = "/file/" + array[0];
	this.img.attr("src",src);
};
mb.ui.img.prototype.getValue = function() {
	var _self = this;
    return this.img.attr("src");
};
mb.ui.img.prototype.disabled = function() {
	this.obj.addClass("mbCard-item-disabled");
};
mb.ui.img.prototype.enabled = function() {
	this.obj.removeClass("mbCard-item-disabled");
};
//==================密码框======================
mb.ui.pwd = function(options) {
	var _self = this;
    var defaults = {
			"id":"",
			"data":null//字典数据
	};
    var opts = jQuery.extend(defaults,options);
    var item = opts.item;
    this._data = opts.data;
    this.id = opts.id;
    
    var name = item.ITEM_NAME;
	this.obj = jQuery("<input type='password' value=''></input>").attr("id",opts.id).addClass("mbCard-form-input mb-box-shadow-inset mb-radius-9"); 
};
mb.ui.pwd.prototype.getContainer = function() {
	return this.obj.parent().parent();
};
mb.ui.pwd.prototype.setValue = function(value) {
	this.obj.val(value);
};
mb.ui.pwd.prototype.getValue = function() {
	var _self = this;
    return this.obj.val();
};
mb.ui.pwd.prototype.disabled = function() {
	this.obj.attr("readonly","true").addClass("mbCard-item-disabled");
};
mb.ui.pwd.prototype.enabled = function() {
	this.obj.attr("readonly","").removeClass("mbCard-item-disabled");
};
//==================字典选择======================TODO:异步加载的情况
mb.ui.dict = function(options) {
	var _self = this;
    var defaults = {
			"id":"",
			"dictId":"",
			"data":null//字典数据
	};
    var opts = jQuery.extend(defaults,options);
    var item = opts.item;
    this._data = opts.data;
    this.id = opts.id;
    this.dictId = opts.dictId;
    var config = item.ITEM_INPUT_CONFIG;
    var name = item.ITEM_NAME;
    var group = jQuery("<div></div>").addClass("mb-select");
    this.a = jQuery("<a></a>").addClass("mb-select-a").appendTo(group);
    var inner = jQuery("<span></span>").addClass("mb-select-inner").appendTo(this.a);
    this.input = jQuery("<input type='hidden'></input>").addClass("mb-select-input").appendTo(inner);
    this.text = jQuery("<span></span>").addClass("mb-select-text").appendTo(inner);
    var icon = jQuery("<span></span>").addClass("mb-select-icon mb-down-nav mb-radius-18").appendTo(inner);
    group.bind("click",function(event) {
    	jQuery(".mb-select-active").removeClass("mb-select-active");
    	_self.a.addClass("mb-select-active");
    	
    	var temp = {"dictId":_self.dictId,"pCon":jQuery("body"),"parHandler":_self,"config":config};
	    var selectView = new mb.vi.selectList(temp);
	    selectView._bldWin(event);
	    selectView.show();
//		var selectListUrl = "/sy/base/view/mbSelectListView.jsp?dictId=" + _self.dictId;
//    	alert(selectListUrl);
//		window.location.href = selectListUrl;
    });

	this.obj = group;
};
mb.ui.dict.prototype.getContainer = function() {
	return this.obj.parent().parent();
};
mb.ui.dict.prototype.setValue = function(value,text) {
	var _self = this;
	var unitId = this.id + "-" + value;
	var labelId = unitId + "-label";
	var i = 0;
	if (text || text == "") {
		_self.input.val(value);
		_self.text.text(text);
	} else {
		if (_self._data) {
			_self.pickNode(value,_self._data);
		}
	}
};
mb.ui.dict.prototype.pickNode = function(id,data) {
	var _self = this;
	var len = 0;
	if (data) {
		len = data.length;
	}
	for (var i = 0; i < len; i++) { // 遍历查找NAME
		if (id == data[i].ID) {
			_self.input.val(id);
			_self.text.text(data[i].NAME);
			return true; // 找到了
		} else if (data[i].CHILD) {
			var ret = _self.pickNode(id,data[i].CHILD);
			if (ret) { // 找到了则直接返回，不进行余下的循环
				return true;
			}
		}
	}
	return false; // 没有找到
};
mb.ui.dict.prototype.getValue = function() {
	var _self = this;
    return _self.input.val();
};
mb.ui.dict.prototype.getText = function() {
	var _self = this;
	return _self.text.val();
};
mb.ui.dict.prototype.setActive = function() {
	var _self = this;
};
mb.ui.dict.prototype.disabled = function() {
	this.obj.addClass("mbCard-item-disabled").unbind("click");
};
mb.ui.dict.prototype.enabled = function() {
	this.obj.removeClass("mbCard-item-disabled");
};
//==================静态文本======================
mb.ui.staticText = function(options) {
	var _self = this;
    var defaults = {
			"id":""
	};
    var opts = jQuery.extend(defaults,options);
    this.item = opts.item;
    this.id = opts.id;
    var name = this.item.ITEM_NAME;
	this.obj = jQuery("<span></span>").attr("id",opts.id); 
};
mb.ui.staticText.prototype.getContainer = function() {
	return this.obj.parent().parent();
};
mb.ui.staticText.prototype.setValue = function(value) {
	//this.obj.html(this.item.ITEM_INPUT_CONFIG);
	if (value && (value != "config")) {
		this.obj.html(value);
	} else if (this.item.ITEM_INPUT_CONFIG) {
		this.obj.html(this.item.ITEM_INPUT_CONFIG);
	}
};
mb.ui.staticText.prototype.getValue = function() {
	var _self = this;
    return this.obj.html();
};
mb.ui.staticText.prototype.disabled = function() {
};
mb.ui.staticText.prototype.enabled = function() {
};
