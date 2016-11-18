if (!_Ajax) {
	var _Ajax = {};
}

_Ajax.sendRequest = function(text, url, obj) {
	jQuery.getScript(url + encodeURIComponent(text), function() {
		var list = response.wlist;
		obj.candidateList = list;
		var resultList = obj._initsuggest();
		if (resultList.length == 0) {
			console.log("hideSuggestArea");
			obj.hideSuggestArea();
		} else {
			obj.createSuggestArea(resultList);
		}

	});
}

/*
 * -------------------------------------------------------- suggest.js - Input
 * Suggest Version 2.2 (Update 2010/09/14)
 * 
 * Copyright (c) 2006-2010 onozaty (http://www.enjoyxstudy.com)
 * 
 * Released under an MIT-style license.
 * 
 * For details, see the web site: http://www.enjoyxstudy.com/javascript/suggest/
 * 
 * --------------------------------------------------------
 */

if (!Suggest) {
	var Suggest = {};
}
/*-- KeyCodes -----------------------------------------*/
Suggest.Key = {
	TAB : 9,
	RETURN : 13,
	ESC : 27,
	UP : 38,
	DOWN : 40
};

/*-- Utils --------------------------------------------*/
Suggest.copyProperties = function(dest, src) {
	for ( var property in src) {
		dest[property] = src[property];
	}
	return dest;
};

/*-- Suggest.Local ------------------------------------*/
Suggest.Local = function() {
	this.initialize.apply(this, arguments);
};
Suggest.Local.prototype = {
	initialize : function(input, suggestArea, url, callback, candidateList) {
		this.input = this._getElement(input);
		this.suggestArea = this._getElement(suggestArea);
		this.candidateList = candidateList;
		this.oldText = this.getInputText();
		this.url = url;
		this.callback = callback;

		if (arguments[3])
			this.setOptions(arguments[3]);

		var self = this;
		var inputObj = jQuery(this.input);
		// 输入框失去焦点
		inputObj.unbind().focus().val(inputObj.val()).blur(function() {
			self.changeUnactive();
			self.oldText = self.getInputText();
			setTimeout(function() {
				jQuery(self.suggestArea).hide();
			}, 120);
		}).focus(function() { // 输入框获取焦点
			self._displaySuggestArea();
		}).keydown(
				function() { // 输入文字
					if (self.dispAllKey && event.ctrlKey
							&& self.getInputText() == '' && !self.suggestList
							&& event.keyCode == Suggest.Key.DOWN) {
						self._stopEvent(event);
						self.keyEventDispAll();
					} else if (event.keyCode == Suggest.Key.UP
							|| event.keyCode == Suggest.Key.DOWN) { // 输入向上、向下按钮
						if (self.suggestList && self.suggestList.length != 0) {
							self._stopEvent(event);
							self.keyEventMove(event.keyCode);
						}
					} else if (event.keyCode == Suggest.Key.RETURN) { // 输入回车按钮
						self.doCallback(); //执行回调
					} else if (event.keyCode == Suggest.Key.ESC) { // 输入取消按钮
						if (self.suggestList && self.suggestList.length != 0) {
							self._stopEvent(event);
							self.keyEventEsc();
						}
					}
				}).keyup(function() {
			var text = self.getInputText();
			console.log(text);
			if (text != self.oldText) {
				self.oldText = text;
				self.search();
			}
		});

		this.clearSuggestArea();
	},

	// options
	dispMax : 20,
	listTagName : 'div',
	prefix : false,
	ignoreCase : true,
	highlight : false,
	dispAllKey : false,
	classMouseOver : 'over',
	classSelect : 'select',
	hookBeforeSearch : function(text) {
		_Ajax.sendRequest(text, this.url, this);
	},

	setOptions : function(options) {
		Suggest.copyProperties(this, options);
	},
	/**
	 * 向服务器查询关键字请求
	 */
	search : function() {
		// init
		this.clearSuggestArea();

		var text = this.getInputText();

		if (text == '' || text == null) {
			this.hideSuggestArea();
			return;
		}

		this.hookBeforeSearch(text);
	},

	_initsuggest : function() {
		var resultList = [];
		var temp;
		this.suggestIndexList = [];

		for ( var i = 0, length = this.candidateList.length; i < length; i++) {
			var temp = this.candidateList[i];
			resultList.push(temp);
			this.suggestIndexList.push(i);

			if (this.dispMax != 0 && resultList.length >= this.dispMax)
				break;
		}
		return resultList;
	},

	/**
	 * 隐藏关键字列表
	 */
	hideSuggestArea : function() {
		jQuery(this.suggestArea).hide();
	},

	/**
	 * 显示列表
	 */
	_displaySuggestArea : function() {
		if (!this.suggestList || this.suggestList.length == 0) {
			return;
		}

		var sugObj = jQuery(self.suggestArea);

		var pos = this._getPos(this.input);
		var top = pos.top + jQuery(this.input).height();
		var left = pos.left;
		console.log("left:" + left);
		// 显示DIV
		this.suggestArea.scrollTop = 0;
		this.suggestArea.style.display = "";
		this.suggestArea.style.left = left + "px";
		this.suggestArea.style.top = top + "px";
		this.suggestArea.style.width = this.input.scrollWidth + "px";
	},

	_getPos : function(el) {
		var lx = 0, ly = 0;
		while (el != null) {
			lx += el.offsetLeft, ly += el.offsetTop, el = el.offsetParent
		}
		return {
			left : lx,
			top : ly
		};
	},

	/**
	 * 清除提示框的数据
	 */
	clearSuggestArea : function() {
		this.suggestArea.innerHTML = '';
		this.suggestList = null;
		this.suggestIndexList = null;
		this.activePosition = null;
	},
	/**
	 * @param resultList
	 *            服务器端反馈的提示信息
	 */
	createSuggestArea : function(resultList) {
		var self = this;
		this.suggestList = [];
		this.inputValueBackup = self.input.value;
		for ( var i = 0, length = resultList.length; i < length; i++) {
			var element = document.createElement(self.listTagName);
			element.innerHTML = resultList[i];
			element.style.width = "96%";
//			jQuery(element).css({"border":"solid 1px #FF0000"});
			self.suggestArea.appendChild(element);
			self.suggestList.push(element);
			//点击关键字，使用mousedown事件，增加灵敏度，避免事件被input的失去焦点先捕获
			jQuery(element).unbind().mousedown(function() { // 点击关键字
				var val = jQuery(this).text();
				jQuery(self.input).focus().val(val);
				self.doCallback(); //执行回调
			}).hover(function() { // 鼠标放上去
				jQuery(this).addClass(self.classMouseOver);
			}, function() { // 鼠标移开
				jQuery(this).removeClass(self.classMouseOver);
			});
		}
//		jQuery(self.suggestArea).children()
		if (self.suggestList.length == 0) {
			jQuery(self.suggestArea).hide();
		}
		this._displaySuggestArea();
	},
	/**
	 * 执行回调函数
	 */
	doCallback : function(){
		var self = this;
		if (null != self.callback) {
			self.hideSuggestArea();
			eval(self.callback);
			self._stopEvent(event);
		}		
	},
	/**
	 * @return 返回输入框的值
	 */
	getInputText : function() {
		return this.input.value;
	},
	keyEventDispAll : function() {
		// init
		this.clearSuggestArea();

		this.oldText = this.getInputText();

		this.suggestIndexList = [];
		for ( var i = 0, length = this.candidateList.length; i < length; i++) {
			this.suggestIndexList.push(i);
		}

		this.createSuggestArea(this.candidateList);
	},
	/**
	 * 向上、向下箭头
	 */
	keyEventMove : function(keyCode) {
		this.changeUnactive();

		if (keyCode == Suggest.Key.UP) { // 向上按钮
			if (this.activePosition == null) {
				this.activePosition = this.suggestList.length - 1;
			} else {
				this.activePosition--;
				if (this.activePosition < 0) {
					this.activePosition = null;
					this.input.value = this.inputValueBackup;
					this.suggestArea.scrollTop = 0;
					return;
				}
			}
		} else { // 向下按钮
			if (this.activePosition == null) {
				this.activePosition = 0;
			} else {
				this.activePosition++;
			}

			if (this.activePosition >= this.suggestList.length) {
				this.activePosition = null;
				this.input.value = this.inputValueBackup;
				this.suggestArea.scrollTop = 0;
				return;
			}
		}

		this.changeActive(this.activePosition);
	},

	keyEventEsc : function() { // 输入esc按钮
		this.clearSuggestArea();
		this.input.value = this.inputValueBackup;
		this.oldText = this.getInputText();

		if (window.opera)
			setTimeout(this._bind(this.moveEnd), 5);
	},

	changeActive : function(index) {
		var element = this.suggestList[index];
		jQuery(element).addClass(this.classSelect);

		// auto scroll
		var offset = element.offsetTop;
		var offsetWithHeight = offset + element.clientHeight;

		if (this.suggestArea.scrollTop > offset) {
			this.suggestArea.scrollTop = offset
		} else if (this.suggestArea.scrollTop + this.suggestArea.clientHeight < offsetWithHeight) {
			this.suggestArea.scrollTop = offsetWithHeight
					- this.suggestArea.clientHeight;
		}

		// 把选中的值放到输入框中
		var val = jQuery(element).text();
		jQuery(this.input).focus();
		jQuery(this.input).val(val);
		this.oldText = val;
	},

	changeUnactive : function() {
		// 移除所有的选中的css
		jQuery(this.listTagName, this.suggestArea)
				.removeClass(this.classSelect);
	},

	moveEnd : function() {
		if (this.input.createTextRange) {
			this.input.focus(); // Opera
			var range = this.input.createTextRange();
			range.move('character', this.input.value.length);
			range.select();
		} else if (this.input.setSelectionRange) {
			this.input.setSelectionRange(this.input.value.length,
					this.input.value.length);
		}
	},

	// Utils
	_getElement : function(element) {
		return (typeof element == 'string') ? document.getElementById(element)
				: element;
	},
	_stopEvent : function(event) { // 阻止时间冒泡
		if (event.preventDefault) {
			event.preventDefault();
			event.stopPropagation();
		} else {
			event.returnValue = false;
			event.cancelBubble = true;
		}
	},
	_bind : function(func) {
		var self = this;
		var args = Array.prototype.slice.call(arguments, 1);
		return function() {
			func.apply(self, args);
		};
	}
};