/*
 * 高级查询组件
 */
GLOBAL.namespace("rh.ui");
rh.ui.search = function(options) {
	var defaults = {
		"id":"",
		"msg":"执行中，请稍后..",
		"data":null,
		"treeLink":false,
		"pCon":null,//外层容器
		"col":1,//显示列数，默认值是1列
		"parHandler":null,
		"data":{}
	};
	this.opts = jQuery.extend(defaults,options);
	this._msg = this.opts.msg;
	this._id = "rh-advanceSearch-" + this.opts.id;
	this._treeLink = this.opts.treeLink;
	this._items = this.opts.data.ITEMS;
	this._dicts = this.opts.data.DICTS;
	this._parHandler = this.opts.parHandler;
	this._pCon = this.opts.pCon;
	this._col = this.opts.col;//显示列数
	this._numberInputTipMsg = "请输入数字";
	this.isMobile = Browser.isMobileOS();
};
rh.ui.search.prototype.show = function() {
	if (this._pCon == null) {
		this._layout();
	} else {
		this._bldContent();
	}
};
/*
 * 构建弹出框页面布局
 */
rh.ui.search.prototype._layout = function() {
	var _self = this;
	
	var hei = GLOBAL.getDefaultFrameHei() - 100;
	var scroll = RHWindow.getScroll(window.top);
    var viewport = RHWindow.getViewPort(window.top);
    
    // 可视高度，如果本卡片的高度大于可视区域的高度则取可视区域的高度，否则取卡片的高度减去卷去的高度
    var cardHeight;
    if (window.top.top != parent.window) { // 三级iframe
		cardHeight = jQuery(document).height();
    } else {
    		cardHeight = jQuery(".cardDialog", document).first().height();
    }
    var viewportHeight = cardHeight - (scroll.top > 45 ? scroll.top - 45 : 0);
    
    // 如果可视区域高度小于等于dialog高度则改变dialog高度
	if (viewportHeight < hei) {
		hei = viewportHeight;
	}
	// 高度不能小于300
	if (hei <= 400) {
		hei = 400;
	}
	// 高度不能大于450
	if (hei > 450) {
		hei = 450;
	}
	// 如果可视区域放下dialog之后
	if (viewportHeight - hei < 100) {
		// 计算出差值
		var scrollTop = scroll.top - (100 - (viewportHeight - hei));
		if (scrollTop > 0) {
			// 滚动到正确的位置
			jQuery(window.top.document).scrollTop(scrollTop); 
			scroll = RHWindow.getScroll(window.top);
		}
	}
	var wid = 620;
	
	// 居中显示，对于关联服务为iframe的tab，由于它的父有45像素的头，所以弹出框往上移动45像素
    var top = scroll.top;
    if (window.top.top != parent.window) { // 三级iframe
    		top -= 45;
    		if (top < 0) {
        		top = 0;
        }
    } else {
    		// 保持和iframe形式的关联服务一致
    		if (top == 0) {
			top += 45;
		}
    }
    
    var left = scroll.left + viewport.width / 2 - wid / 2;
    // 排除左侧菜单的宽度之后居中
    if (!$("#left-homeMenu", window.top.document).is(":hidden")) {
		var menuWidth = $("#left-homeMenu", window.top.document).width();
		left -= menuWidth / 2;
		if (window.top.top != parent.window) {
			left -= 20;
		}
    }
    
	var posArray = [left,top];
	
	if (jQuery("#" + this._id).size() == 1) {
		jQuery("#" + this._id).dialog({position:posArray});
		jQuery("#" + this._id).dialog("open");
		this._btnBind();
	} else {
		//1.构造dialog
		this.winDialog = jQuery("<div></div>").addClass("rh-searchDialog").attr("id",this._id).attr("title","高级查询");
		this.winDialog.appendTo(jQuery("body").first());

		jQuery("#" + this._id).dialog({
			autoOpen: false,
			height: hei,
			width: wid,
			modal: true,
			resizable:false,
			position:posArray
		});
		_self._bldContent();
		var dialogObj = jQuery("#" + this._id);
		dialogObj.dialog("open");
		dialogObj.parent().addClass("rh-bottom-right-radius rh-advSearch");
		jQuery(".ui-dialog-titlebar").last().css("display","block");//设置标题显示
	}
};
/*
 * 构建弹出框内字段显示
 */
rh.ui.search.prototype._bldContent = function() {
	var _self = this;
	var widCon = 264;
	var sumCols = 4;//2大列显示是table共需4列
	if (!this.isMobile) { // 移动版两列
		if (this._col == 3) {
			widCon = 150;
			sumCols = 6;
		} else if (this._col == 1) {
			widCon = 250;
			sumCols = 2;		
		}
	}
	//元素列表
	this.fieldSet = jQuery("<fieldset><legend style='cursor:pointer'>请输入或选择查询条件<span class='rh-advSearch__iconC icon-card-close'></span></legend></fieldset>").addClass("rh-advSearch-fieldSet");
	var legend = this.fieldSet.find("legend");
	//增加legend展开折叠处理
	legend.mousedown(function() {
		var close = jQuery(this).find(".rh-advSearch__iconC");
		if (close.hasClass("icon-card-close")) {
			close.removeClass("icon-card-close").addClass("icon-card-open");
			_self.container.fadeOut(0);
			_self._parHandler._resetHeiWid();
		} else {
			close.removeClass("icon-card-open").addClass("icon-card-close");
			_self.container.fadeIn(0);
			_self._parHandler._resetHeiWid();
		}
	});
	this.container = jQuery("<div></div").appendTo(this.fieldSet);
	this.table = jQuery("<table width='100%'></table>").addClass("rh-advSearch-table");
	this.tr = jQuery();
	var colspanCount = 0;
	jQuery.each(this._items,function(i,n) {
		if (n.ITEM_TYPE == UIConst.FORM_FIELD_TYPE_SELF) {//过滤自定义字段
			return true;
		}
		if (n.ITEM_SEARCH_FLAG == UIConst.STR_YES) {//启用高级查询的情况
			var tempColspan = 2;//label+字段列=2列
			if ((colspanCount == 0) || (colspanCount >= sumCols)) {//起始换行
				colspanCount = 0;//重置计数
				_self.tr = jQuery("<tr height='35px'></tr>");
				_self.tr.appendTo(_self.table);
			}
			var tdLab = jQuery("<td></td>").addClass("rh-advSearch-lab-td").appendTo(_self.tr);
			var tdInp = jQuery("<td icode = '" + n.ITEM_CODE + "'></td>").addClass("rh-advSearch-inp-td").appendTo(_self.tr);
			//标签部分
			jQuery("<label value='" + n.ITEM_CODE + "'>" + n.ITEM_NAME + "</label>").addClass("rh-advSearch-lab").appendTo(tdLab);
			//输入框部分
			var code = n.ITEM_CODE;
			var type = n.ITEM_INPUT_TYPE;//输入类型
			var mode = n.ITEM_INPUT_MODE;//输入模式
			var conf = n.ITEM_INPUT_CONFIG;
			if (conf.indexOf("'SEARCHHIDE':'true'") > 0) {
				return true;
			}
			var array = conf.split(",");
			conf = jQuery.trim(n.ITEM_INPUT_CONFIG);
			var confJson = {};
			if(conf){
				//以"{"开头，
				if(conf.indexOf("{")==0){
					confJson = StrToJson(conf);
				} else if (array.length > 1) {//有配置附加参数的
					//不是以"{"开头，则可能是字典或其他配置
					confJson = StrToJson(array.slice(1).join(","));
				}
			}
			var dictCode = array[0];
			if ((type == UIConst.FITEM_ELEMENT_SELECT) && (conf.indexOf("'SEARCHTYPE':'multi'") < 0)) {
				if (dictCode == "") {//没有配置相应字典，则不予显示
					return true;
				}
				var temp = _self._dicts[dictCode] || "";
				if (temp == undefined) {//页面没有去后台取
					temp = FireFly.getDict(dictCode);
					if(temp && temp.length > 0){
						//默认取第一个
						temp = temp[0].CHILD;
					}
				}
				var sel = jQuery("<select></select>").addClass("rh-advSearch-val").width(widCon + 5).height("25").css("border","1px #bebebe solid");
				sel.data("itemcode",code);
				sel.data("itemtype",UIConst.FITEM_ELEMENT_SELECT);
				jQuery("<option value=''>------</option>").appendTo(sel);
				jQuery.each(temp,function(i,n) {
					var id = n.ID;
					var name = n.NAME;
					jQuery("<option value='" + id + "'>" + name + "</option>").appendTo(sel);
				});
				sel.data("symbol", confJson.SEARCHONE);
				sel.appendTo(tdInp);	
			} else if ((type == UIConst.FITEM_ELEMENT_SELECT) && (conf.indexOf("'SEARCHTYPE':'multi'") > 0)
					|| (type == UIConst.FITEM_ELEMENT_RADIO) 
					|| (type == UIConst.FITEM_ELEMENT_CHECKBOX)) {
				var temp = _self._dicts[dictCode];
				if (temp == undefined) {//页面没有去后台取
					temp = FireFly.getDict(dictCode);
					if(temp && temp.length > 0){
						//默认取第一个
						temp = temp[0].CHILD;
					}
				}
				var sel = jQuery("<fieldset></fieldset>").addClass("rh-advSearch-val").width((widCon -3)+ "px").css({'padding':'0px 0px 0px 5px',"border":"1px #bebebe solid"});
				sel.data("itemcode",code);
				sel.data("itemtype",UIConst.FITEM_ELEMENT_CHECKBOX)
				jQuery.each(temp,function(i,n) {
					var id = n.ID;
					var name = n.NAME;
					var uid = code + "-" + id;
					var check = jQuery("<input type='checkbox' />");
					check.attr("id",uid);
					check.attr("name",code);
					check.attr("value",id);
					check.addClass("rh-advSearch-check").appendTo(sel);	
					jQuery("<label for='" + uid + "'></label>").text(name).addClass("rh-advSearch-checkLable").appendTo(sel);
				});
				sel.appendTo(tdInp);				
			} else {//其它输入类型
				var inp = jQuery("<input type='text' size=45/>").data("itemcode",code).addClass("rh-advSearch-val").width(widCon);
				var inpName = jQuery("<input type='text' size=45/>").addClass("rh-advSearch-inp").width(widCon);
				//var inpId = _self._id + code;
				var inpId = _self._createItemId(code);
				inp.attr("id",inpId);
				inpName.attr("id",inpId + "__NAME");
				
				if (mode == UIConst.FITEM_INPUT_QUERY) {//查询选择
					inp.data("itemmode",UIConst.FITEM_INPUT_QUERY).appendTo(tdInp);
					var querySel = jQuery("<a>选择</a>").addClass("rh-advSearch-sel").appendTo(tdInp);
					querySel.bind("click",function(event) {
						var options = {
								"itemCode" : inpId,
								"config" : conf,
								"parHandler" : _self,
								"searchFlag" : true,
								"rebackCodes":inpId
							};
							var queryView = new rh.vi.rhSelectListView(options);
							queryView.show(event);	
					});
					var queryCan = jQuery("<a>取消</a>").addClass("rh-advSearch-cancle").appendTo(tdInp);
					queryCan.bind("click",function() {
						inp.val("");
					});
				} else if(mode == UIConst.FITEM_INPUT_DICT) {//字典
					inp = jQuery("<input type='hidden' size=45/>").data("itemcode",code).data("itemmode",UIConst.FITEM_INPUT_DICT)
					       .addClass("rh-advSearch-val").width(widCon);
					inp.attr("id",inpId);
					inp.appendTo(tdInp);
					inpName.appendTo(tdInp);
					var dictSel = jQuery("<a>选择</a>").addClass("rh-advSearch-sel").appendTo(tdInp);
					dictSel.bind("click",function(event) {
						var options = {
								"itemCode" : inpId,
								"config" : conf,
								"parHandler" : _self,
								"hide" : "explode",
								"show" : "blind",
								"searchFlag" : true,
								"rebackCodes":inpId
						};
						var dictView = new rh.vi.rhDictTreeView(options);
						dictView.show(event);
						dictView.tree.selectNodes(inp.val().split(","));
					});
					var queryCan = jQuery("<a>取消</a>").addClass("rh-advSearch-cancle").appendTo(tdInp);
					queryCan.bind("click",function() {
						inp.val("");
						inpName.val("");
					});
				} else if (mode == UIConst.FITEM_INPUT_DATE) {//日期
					var type = "<input type='text' tabindex='-1' size=19/>";
					conf = "DATE";
					var html5DP = false;
					if (Browser.supportHTML5()) {
						html5DP = true;
					}
					var mobisrollDP = !html5DP;
					if (_self.isMobile) {
						if (html5DP) {
							type = "<input type='date' tabindex='-1' size=19/>";
						} else if (mobisrollDP) {
							type = "<input type='text' tabindex='-1' size=19 class='rhMobiScrollDatePicker' id='rhMobiScrollDatePicker_" + this.ITEM_CODE + "'/>";											
						}						
					}
					var width = widCon;
					var symbolSelect = jQuery("<SELECT><option value='>='>>=</option><option value='<='><=</option><option value='>'>></option><option value='<'><</option><option value='=' selected='selected'>=</option></SELECT>")
						.css("border","1px #bebebe solid").height("25");
					if(confJson.SEARCHONE && confJson.SEARCHTWO){
						//如果配置了操作符,则设定为默认值
						symbolSelect.val(confJson.SEARCHONE);
						symbolSelect.appendTo(tdInp);
						tdInp.append("&nbsp;");
						width = widCon-40;
						var inp1 = jQuery(type).data("itemcode",code)
				           .data("itemmode",UIConst.FITEM_INPUT_DATE).addClass("rh-advSearch-val").width(width);
						inp1.appendTo(tdInp);
						if (_self.isMobile && mobisrollDP) {
							_self.doMobiScroll(inp1);
						}
						if (!_self.isMobile) {
							inp1.bind("focus",function() {
								datePicker(conf);
							});
						}
						tdInp.append("<br>");
						var symbolSelect2= symbolSelect.clone();
						//如果配置了操作符,则设定为默认值
						symbolSelect2.val(confJson.SEARCHTWO);
						symbolSelect2.appendTo(tdInp);
						tdInp.append("&nbsp;");
						var inp2 = jQuery(type).data("itemcode",code)
							.addClass("rh-advSearch-inp").width(width);
						inp1.data("nextInp",inp2);
						inp1.data("symbolSelect",symbolSelect);
						inp2.appendTo(tdInp);
						if (_self.isMobile && mobisrollDP) {
							_self.doMobiScroll(inp2);
						}
						inp2.data("symbolSelect",symbolSelect2);
						if (!_self.isMobile) {
							inp2.bind("focus",function() {
								datePicker(conf);
							});
						}
					}else if(confJson.SEARCHONE || confJson.SEARCHTWO){
						width = widCon-40;
						//如果配置了操作符,则设定为默认值
						symbolSelect.val(confJson.SEARCHONE ? confJson.SEARCHONE : confJson.SEARCHTWO);
						symbolSelect.appendTo(tdInp);
						tdInp.append("&nbsp;");
						//运算符号，默认为"="
						inp = jQuery(type).data("itemcode",code)
				           .data("itemmode",UIConst.FITEM_INPUT_DATE).addClass("rh-advSearch-val").width(width);
						inp.appendTo(tdInp);
						if (_self.isMobile && mobisrollDP) {
							_self.doMobiScroll(inp);
						}
						inp.data("symbolSelect",symbolSelect);
						if (!_self.isMobile) {
							inp.bind("focus",function() {
								datePicker(conf);
							});
						}
					} else {
						//日期默认为区间查询
						//tdInp.attr("colspan","3");
						var wid = 90;
						if (_self.isMobile) {
							wid = 119;
						}
						var inp1 = jQuery(type).data("itemcode",code)
				           .data("itemmode",UIConst.FITEM_INPUT_DATE).addClass("rh-advSearch-val").width(wid);
						inp1.appendTo(tdInp);
						if (_self.isMobile && mobisrollDP) {
							_self.doMobiScroll(inp1);
						}
						if (!_self.isMobile) {
							inp1.bind("focus",function() {
								datePicker(conf);
							});
						}
						jQuery("<span>-</span>").addClass("rh-advSearch-dao").appendTo(tdInp);
						var inp2 = jQuery(type).addClass("rh-advSearch-inp").width(wid).data("itemcode",code);
						inp1.data("nextInp",inp2);
						if (!_self.isMobile) {
							inp2.bind("focus",function() {
								datePicker(conf);
							});
						}
						inp2.appendTo(tdInp);
						if (_self.isMobile && mobisrollDP) {
							_self.doMobiScroll(inp2);
						}
						//计算时间并返回相应条件
						if (confJson["SEARCHDAYS"] && confJson["SEARCHDAYS"].length > 0) {
							inp1.val(_self._time(confJson["SEARCHDAYS"]));
							inp2.val(System.getVar("@DATE@"));
							_self.defaultSetFlag = true;
						}
						//tempColspan = 4;
					}
				} else {
					//输入类型为“文本框”&&字段类型为“数字”
					if(type == UIConst.FITEM_ELEMENT_INPUT && n.ITEM_FIELD_TYPE==UIConst.DATA_TYPE_NUM){
						var width = widCon;
						var symbolSelect = jQuery("<SELECT><option value='>='>>=</option><option value='<='><=</option><option value='>'>></option><option value='<'><</option><option value='=' selected='selected'>=</option></SELECT>")
						.css("border","1px #bebebe solid").height("25");
						if(confJson.SEARCHONE && confJson.SEARCHTWO){
							//如果配置了操作符,则设定为默认值
							symbolSelect.val(confJson.SEARCHONE);
							symbolSelect.appendTo(tdInp);
							tdInp.append("&nbsp;");
							width = widCon-40;
							var inp1 = jQuery("<input type='text' size=19/>").data("itemcode",code)
					           .data("datatype",UIConst.DATA_TYPE_NUM).addClass("rh-advSearch-val").width(width);
							inp1.appendTo(tdInp);
							tdInp.append("<br>");
							var symbolSelect2= symbolSelect.clone();
							//如果配置了操作符,则设定为默认值
							symbolSelect2.val(confJson.SEARCHTWO);
							symbolSelect2.appendTo(tdInp);
							tdInp.append("&nbsp;");
							var inp2 = jQuery("<input type='text' size=19/>").data("itemcode",code)
								.addClass("rh-advSearch-inp").width(width);
							inp1.data("nextInp",inp2);
							inp1.data("symbolSelect",symbolSelect);
							inp2.appendTo(tdInp);
							inp2.data("symbolSelect",symbolSelect2);
							//输入框增加回车查询事件
							_self.addEnterEventObj(inp1);
							_self.addEnterEventObj(inp2);
							//数字输入框事件注册
							_self.addNumberInputEventObj(inp1);
							_self.addNumberInputEventObj(inp2);
						} else if(confJson.SEARCHONE || confJson.SEARCHTWO){
							width = widCon-40;
							//如果配置了操作符,则设定为默认值
							symbolSelect.val(confJson.SEARCHONE ? confJson.SEARCHONE : confJson.SEARCHTWO);
							symbolSelect.appendTo(tdInp);
							tdInp.append("&nbsp;");
							//运算符号，默认为"="
							//var symbol = confJson.SEARCHONE ? confJson.SEARCHONE : (confJson.SEARCHTWO ? confJson.SEARCHTWO : "=");
							inp = jQuery("<input type='text' size=19/>").data("itemcode",code)
					           .data("datatype",UIConst.DATA_TYPE_NUM).addClass("rh-advSearch-val").width(width);
							inp.appendTo(tdInp);
							inp.data("symbolSelect",symbolSelect);
							//输入框增加回车查询事件
							_self.addEnterEventObj(inp);
							//数字输入框事件注册
							_self.addNumberInputEventObj(inp);
						} else {
							//默认
							inp = jQuery("<input type='text' size=19/>").data("itemcode",code)
					           .data("datatype",UIConst.DATA_TYPE_NUM).addClass("rh-advSearch-val").width(widCon);
							inp.appendTo(tdInp);
							//输入框增加回车查询事件
							_self.addEnterEventObj(inp);
							//数字输入框事件注册
							_self.addNumberInputEventObj(inp);
						}
					} else{
						var symbolSelect = jQuery("<SELECT><option value='like' selected='selected'>包含</option><option value='not like'>不包含</option><option value='='>等于</option></SELECT>")
							.css("border","1px #bebebe solid").height("25");
						if(confJson.SEARCHONE){
							//如果配置了操作符,则设定为默认值
							symbolSelect.val(confJson.SEARCHONE);
							symbolSelect.appendTo(tdInp);
							tdInp.append("&nbsp;");
							inp = jQuery("<input type='text' size=19/>").data("itemcode",code)
					           .data("datatype",UIConst.DATA_TYPE_STR).addClass("rh-advSearch-val").width(widCon-66);
							inp.appendTo(tdInp);
							inp.data("symbolSelect",symbolSelect);
						} else{
							//默认
							inp.appendTo(tdInp);
						}
						//输入框增加回车查询事件
						_self.addEnterEventObj(inp);
					}
				}
			}
			colspanCount += tempColspan;
		}
	});
	this.table.appendTo(this.container);
	if (this._pCon == null) {
		this.fieldSet.appendTo(this.winDialog);
	} else {
		this.fieldSet.appendTo(this._pCon);
	}

	//查询按钮
	this.btnDiv = jQuery("<div></div>").addClass("rh-advSearch-div");
	this.advBtn = jQuery("<input type='button' id='advSearch-btn' value='开始查询'/>").addClass("rh-advSearch-btn").appendTo(this.btnDiv);

	//设置关联树条件
    if (this._treeLink == true) {
    	var treeLink =  jQuery("<input type=checkbox id='rh_search_treeLink' checked=true/>").addClass("rh-advSearch-check").appendTo(this.btnDiv);
    	jQuery("<label for='rh_search_treeLink'>关联导航树查询</label>").appendTo(this.btnDiv);
    }
    this.btnDiv.appendTo(this.container);
    this._btnBind();
};
/*
 * 输入框回车事件注册
 */
rh.ui.search.prototype.addEnterEventObj = function(obj){
	var _self = this;
	//绑定回车事件
	obj.keypress(function(event) {
        if (event.keyCode == '13') {
            _self.advBtn.click();
            return false;
        }
    });
};
/*
 * 数字输入框事件注册
 */
rh.ui.search.prototype.addNumberInputEventObj = function(obj){
	var _self = this;
	//初始合法值定义为""
	obj.data("preValidVal","");
	obj.bind("keyup",function(){
		var flag = new RegExp("^(0|[-+]?[0-9]*([\.])?([0-9])*)$").test(obj.val());
		if(flag){
			//输入合法，设定值为上次合法值
			obj.data("preValidVal",obj.val());
		}else{
			//输入不合法，将值恢复至上次合法值
			obj.val(obj.data("preValidVal"));
		}
	});
	//初始化值为提示信息"请输入数字"
	obj.val(_self._numberInputTipMsg);
	obj.css("color","#999");
	obj.bind("focus",function(){
        var txt_value = obj.val();
        if(txt_value == _self._numberInputTipMsg){
        	obj.css("color","");
        	obj.val("");
        }
    });
	obj.bind("blur",function(){
       var txt_value = obj.val();
       if(txt_value == ""){
    	   obj.val(_self._numberInputTipMsg);
    	   obj.css("color","#999");
       }
   });
};
/*
 * 获取高级查询条件
 */
rh.ui.search.prototype.getWhere = function() {
	var _self = this;
	var res = jQuery(".rh-advSearch-val",this.table);
	var temp = "";
	jQuery.each(res,function(i,n) {
		var obj = jQuery(n);
		var va = "";
		if (obj.data("itemtype") == UIConst.FITEM_ELEMENT_CHECKBOX) {
			var checks = obj.find("input:checked");
			var array = [];
			jQuery.each(checks, function(i,n) {
				array.push(jQuery(n).attr("value"));
			});
			va = array.join(",");
		} else {
			va = jQuery.trim(obj.val());
		}
		if (obj.data("itemmode") && obj.data("itemmode") == UIConst.FITEM_INPUT_DATE) {//日期特殊处理
			if (this.isMobile && va && va.length > 0) { // 日期需要转换一下
				va = va.substr(0, 10);
				year = parseInt(va.substr(0, 4));
				month = parseInt(va.substr(5, 2));
				date = parseInt(va.substr(8, 2));
				va = new Date(year, month, date).format("isoDate");
			}
			var symbolSelect = obj.data("symbolSelect");
			//日期为区间查询，如果没有选择过滤符号，第一个日期框默认为">="
			var symbol = symbolSelect ? symbolSelect.val() : ">=";
			if(va && va.length > 0){
				temp += " and ";
				temp += obj.data("itemcode");
				temp += (" " + symbol + " '");
				temp += va;
				temp += "'";
			}
			//判断是否是区间查询
			var nextInp = obj.data("nextInp");
			var nextVal = nextInp ? nextInp.val() : null;
			if (nextVal && nextVal.length > 0) {
				symbolSelect = nextInp.data("symbolSelect");
				//日期为区间查询，如果没有选择过滤符号，第二个日期框默认为"<="
				symbol = symbolSelect ? symbolSelect.val() : "<=";
				temp += " and ";
				temp += obj.data("itemcode");
				temp += (" " + symbol + " '");
				if(nextVal.length == 10){ //只有年月日，则加上时分秒
					temp += nextVal + " 24:00:00" ;
				}else if(nextVal.length == 16){ //有年月日时分，则增加秒
					temp += nextVal + ":60" ;
				}else {
					temp += nextVal;
				}
				temp += "'";
			}
		} else if ((obj.data("itemmode") == UIConst.FITEM_INPUT_DICT) //字典
				     || (obj.data("itemmode") == UIConst.FITEM_INPUT_QUERY) //查询选择
				     || (obj.data("itemtype") == UIConst.FITEM_ELEMENT_SELECT) //下拉框
				     || (obj.data("itemtype") == UIConst.FITEM_ELEMENT_CHECKBOX)) { //多选框
			//如果没有输入查询项，则返回
			if(!va || va.length <= 0){
				return true;
			}
			var array = va.split(",");
			if (array.length > 1) {
				var codesArray = [];
				for (var i = 0; i < array.length;i++) {
					var codes = "";
					codes += "'";
					codes += array[i];
					codes += "'";
					codesArray.push(codes);
				}
				temp += " and ";
				temp += jQuery(n).data("itemcode");
				temp += " in (";
				temp += codesArray.join(",");
				temp += ")";
			} else {
				var symbol = obj.data("symbol") ? obj.data("symbol") : "=";
				//如果没有选择过滤符号，默认为"="，但是下拉列表可能会通过值的like查询
				if(symbol == '='){
					temp += " and ";
					temp += jQuery(n).data("itemcode");
					temp += " "  + symbol + " '";
					temp += va;
					temp += "'";
				}else{
					temp += " and ";
					temp += jQuery(n).data("itemcode");
					temp += " "+symbol+" '%";
					temp += va;
					temp += "%'";
				}
			}
		} else if(obj.data("datatype") == UIConst.DATA_TYPE_NUM){
			var symbolSelect = obj.data("symbolSelect");
			//如果没有选择过滤符号，默认为"="
			var symbol = symbolSelect ? symbolSelect.val() : "=";
			if(va && va.length > 0 && va != _self._numberInputTipMsg){
				temp += " and ";
				temp += obj.data("itemcode");
				temp += (" " + symbol + " '");
				temp += va;
				temp += "'";
			}
			//判断是否是区间查询
			var nextInp = obj.data("nextInp");
			var nextVal = nextInp ? nextInp.val() : null;
			if (nextVal && nextVal.length > 0 && nextVal != _self._numberInputTipMsg) {
				symbolSelect = nextInp.data("symbolSelect");
				symbol = symbolSelect ? symbolSelect.val() : "=";
				temp += " and ";
				temp += obj.data("itemcode");
				temp += (" " + symbol + " '");
				temp += nextVal;
				temp += "'";
			}
		} else {
			//如果没有输入查询项，则返回
			if(!va || va.length <= 0){
				return true;
			}
			var symbolSelect = obj.data("symbolSelect");
			//如果没有选择过滤符号，默认为"like"
			var symbol = symbolSelect ? symbolSelect.val() : "like";
			if(symbol == '='){
				temp += " and ";
				temp += jQuery(n).data("itemcode");
				temp += " "+symbol+" '";
				temp += va;
				temp += "'";
				temp = temp;
			}else{
				temp += " and ";
				temp += jQuery(n).data("itemcode");
				temp += " "+symbol+" '%";
				temp += va;
				temp += "%'";
				temp = temp;
			}
		}
	});
	return temp;
};
/*
 * 获取高级查询条件,Json格式
 */
rh.ui.search.prototype.getWhereJson = function() {
	var _whereJson = {};
	var _self = this;
	var res = jQuery(".rh-advSearch-val",this.table);
	var temp = "";
	jQuery.each(res,function(i,n) {
		var obj = jQuery(n);
		var va = "";
		if (obj.data("itemtype") == UIConst.FITEM_ELEMENT_CHECKBOX) {
			var checks = obj.find("input:checked");
			var array = [];
			jQuery.each(checks, function(i,n) {
				array.push(jQuery(n).attr("value"));
			});
			va = array.join(",");
		} else {
			va = jQuery.trim(obj.val());
		}
		if (obj.data("itemmode") && obj.data("itemmode") == UIConst.FITEM_INPUT_DATE) {//日期特殊处理
			if (va && va.length > 0) { // 日期需要转换一下
				va = va.substr(0, 10);
				_whereJson["START_"+obj.data("itemcode")] = va;
			}
			//判断是否是区间查询
			var nextInp = obj.data("nextInp");
			var nextVal = nextInp ? nextInp.val() : null;
			if (nextVal && nextVal.length > 0) {
				_whereJson["END_"+obj.data("itemcode")] = nextVal.substr(0,10);
			}
		} else {
			if(!va || va.length <= 0 || va == _self._numberInputTipMsg){
				return true;
			}
			var na = obj.data("itemcode");
			_whereJson[na] = va;
		}
	});
		
	return _whereJson;
};

/**
 * 过滤掉不必要或者匹配不同的条件
 * @param {Object} temp
 */
rh.ui.search.prototype.resetWhere = function(where){
	
};

rh.ui.search.prototype.beforeSearch = function(){
	return true;
};

/*
 * 关闭当前弹出框
 */
rh.ui.search.prototype._btnBind = function() {
	var _self = this;
	var winObj = jQuery("#" + this._id);
	if (this._pCon) {
		winObj = this._pCon;
	}
	jQuery(".rh-advSearch-btn",this._pCon).unbind("click").bind("click",function() {
		if (_self._parHandler) {
			var result = _self.beforeSearch();
			if(!result){
				return;
			}
			_self._parHandler.listBarTipLoad("加载中...");
			setTimeout(function() {
				var where = _self.getWhere();
				var resetWhereStr = _self.resetWhere(where) || "";
				if ("" != resetWhereStr) {
					where = resetWhereStr;
				}
				_self._parHandler._clearSearchValue();//清空普通查询
				var treeLink = false;
				var check = jQuery(".rh-advSearch-div",winObj).find("input:checked");
				if ((_self._treeLink == true) && (check.prop("checked") == true)) {
					check = true;
				}
				//if (where.length > 0) {
				_self._parHandler.setSearchWhereAndRefresh(where,check);//开始查询
				//}
				jQuery("#" + _self._id).dialog("close");
			},0);
		}
	});
};
/*
 * 关闭当前弹出框
 */
rh.ui.search.prototype.del = function() {
	jQuery("#" + this._id).dialog("close");
};
/*
 * 彻底销毁弹出框
 */
rh.ui.search.prototype.des = function() {
	if (jQuery("#" + this._id).size() == 1) {
		jQuery("#" + this._id).dialog("destroy");
		jQuery("#" + this._id).empty();
		jQuery("#" + this._id).remove()
	}
};
/*
 * 清空各项值
 */
rh.ui.search.prototype.clearAll = function() {
	jQuery(".rh-advSearch-inp").val("");
};
/*
 * 根据参数返回时间
 */
rh.ui.search.prototype._time = function(time) {
	var newDay = rhDate.nextDate(System.getVar("@DATE@"),time);
	return newDay;
};

/**
 * 
 */
rh.ui.search.prototype._createItemId = function(itemCode){
	return this._id + itemCode;
};

/**
 * 取得Container
 */
rh.ui.search.prototype.getItemVal = function(code){
	var id = this._createItemId(code);
	return jQuery("#" + id ,this.table).val();
};

/**
 * 获取当前字段对象
 * @param {Object} itemCode
 */
rh.ui.search.prototype.getItem = function(itemCode){
	return this.table.find("td[icode='" + itemCode + "']");
};

rh.ui.search.prototype.doMobiScroll = function(inp){
	var _self = this;
	if (!_self.isMobile) {
		return;
	} 
	var dpId = StringUtils.randomNum();
	var dpOpt = {
		"preset": "date",
		"dateFormat": 'yy-mm-dd', // 日期输出格式
		"dateOrder": 'yymmdd', //面板中日期排列格式
		"theme": "default",
		"mode": "mixed",
		"display": "top",
		"lang": "zh",
		"startYear": "1990",
		"endYear": "2030",
		"showOnFocus": false,
		"button3Text": "清除",
		"button3": function(){
			inp.val("");
			jQuery("#rhMobiScrollDatePicker_" + inp.parent().attr("icode") + "_" + dpId).mobiscroll("cancel");
		}
	};
	inp.attr("id", inp.attr("id") + "_" + dpId);
	inp.mobiscroll("destroy").mobiscroll(dpOpt);
};