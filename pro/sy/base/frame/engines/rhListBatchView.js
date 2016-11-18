/** 嵌入服务式列表渲染引擎+组件 */
GLOBAL.namespace("rh.vi");
/*待解决问题：
 * 
 * */

/*
 * 构造函数
 */
rh.vi.listViewBatch = function(options) {
	var defaults = {
		"id":options.sId + "-viListViewBatch",
		"sId":"",//服务ID
		"aId":"", //操作ID
		"pCon":null,
		"pId":null,
		"linkWhere":"",
		//关联服务配置数据
		"linkServData":{}
	};
	this.opts = jQuery.extend(defaults,options);
	this.id = this.opts.id;
	//表单类型
	this.type = "DataService";
	//所属卡片
	this.cardObj = this.opts.cardObj;
	//只读标识
	this.isReadOnly = this.opts.isReadOnly;
	//隐藏标识
	this.isHidden = this.opts.isHidden;
	//单独服务标识
	this.singleServFlag = this.opts.singleServFlag;
	//批量编辑标识
	this.batchFlag = this.isReadOnly ? false : true;
	//标识是否全部列为修改
	this.allTDModify = this.isReadOnly ? false : true;
	if (!this.isReadOnly && this.singleServFlag) {
		this.allTDModify = false;
	}
	//所在form表单数据，修改时使用
	this._formData = {};
	this._parHandler = this.opts.parHandler;
	this._data = null;	
	this._originalWhere = "";//服务定义条件
	this._extendWhere = "";//扩展条件
	this._linkWhere = this.opts.linkWhere;//关联功能过滤条件
	this.links = this.opts.links || {};//关联功能过滤条件
	this._height = "";
	this._width = this.opts.width;
	//关联服务配置数据
	this._linkServData = this.opts.linkServData;
	//关联明细定义-过滤关联字段,提交数据时使用提供默认值使用
	this._linkWhereItemArray = [];
	//删除的主键缓存对象,例：{‘11’:'11','22':'22'}，保持key和value都是主键值，方便删除
	this._deleteDataPks = {};
	
	// 保存之前
	this.beforeSave = function() {};
	// 保存之后
	this.afterSave = function() {};
};

/*
 * 渲染引擎：渲染列表主方法
 */
rh.vi.listViewBatch.prototype.render = function() {
	this._initMainData();
	this._layout();
	this._bldBtnBar();
	this._bldGrid();
	this._afterLoad();
};

/*
 * 渲染引擎：初始化服务主数据，包括服务定义、字段、按钮等
 */
rh.vi.listViewBatch.prototype._initMainData = function() {
	this._data = FireFly.getServMainData(this.opts.sId);
};

/*
 * 渲染引擎：构建列表页面布局
 */
rh.vi.listViewBatch.prototype._layout = function() {
	//默认布局
	var pCon = this.opts.pCon;
	this.content = jQuery("<div id='"+this.id+"'></div>").addClass("content-mainCont").width(this._width);
	this.content.appendTo(pCon);
};

/*
 * 渲染引擎：构建按钮条
 */
rh.vi.listViewBatch.prototype._bldBtnBar = function() {
	var _self = this;
	var tempData;
	this.btns = {};
	var _btnBar = jQuery("<div></div>").addClass("rhGrid-btnBar");
	if (this.singleServFlag) {
		tempData = this._data.BTNS;
		_self._rowBtns = []; //列表行按钮数组
		var oneVar = UIConst.STR_YES;
		jQuery.each(tempData,function(i,n) {
			if ((n.ACT_CODE == UIConst.ACT_BYID) && (n.ACT_TYPE == UIConst.ACT_TYPE_NOBTN)) {//非按钮&&查看
				_self._byIdFlag = true;
			}
			var showFlag = true;//按钮的只读开关
	        if ((_self._readOnly === true || _self._readOnly === "true") && (n.ACT_GROUP == oneVar)) {//页面只读&&非编辑组
	        	showFlag = false;
	        }     
			if ((n.ACT_TYPE == UIConst.ACT_TYPE_LIST) && showFlag) {
				var temp = jQuery("<a></a>").addClass("rh-icon").addClass("rhGrid-btnBar-a");
				temp.attr("id",_self._getUnId(n.ACT_CODE));
				temp.attr("actcode",n.ACT_CODE);
				temp.attr("title",n.ACT_TIP);
				_self._act(n.ACT_CODE,temp);
				var labelName = jQuery("<span></span>").addClass("rh-icon-inner").text(n.ACT_NAME);
				temp.append(labelName);
				var icon = jQuery("<span></span>").addClass("rh-icon-img").addClass("btn-" + n.ACT_CSS);
				temp.append(icon);
				if (n.ACT_MEMO.length > 0) {
					temp.bind("click",function() {
						var _funcExt = new Function(n.ACT_MEMO);
	                    _funcExt.apply(_self);
					});
				}
				_self.btns[n.ACT_CODE] = temp;
				if (n.ACT_CODE == UIConst.ACT_BATCH_SAVE) {//批量编辑的标志
					_self._transBatchFlag = true;
				} 
				if ((n.ACT_EXPRESSION.length > 0) && _self._excuteActExp(n.ACT_EXPRESSION) == false) {//判断操作表达式
					temp.hide();
					_btnBar.append(temp);
				} else {
					_btnBar.append(temp);
				}
				if (n.ACT_CODE == UIConst.ACT_ADD) {//没有添加按钮则只读卡片
					_self._cardRead = false;
				}
			} else if ((n.ACT_TYPE == UIConst.ACT_TYPE_LISTROW) && showFlag) {//列表行按钮
				_self._rowBtns.push(n);
			}
			if (_btnBar.children().length == 0) {
				//将原来的按钮条替换为间隔条
				_btnBar.removeClass("rhGrid-btnBar").addClass("rhGrid-spacer");
			}
		});
	} else {
		tempData = [{"ACT_NAME":" 添 加 ","ACT_CODE":"addBatch","ACT_CSS":"add"}];//this._data.BTNS;
		//根据只读标识判断是否显示按钮
		if(!_self.isReadOnly){
			jQuery.each(tempData,function(i,n) {
				var temp = jQuery("<a></a>").addClass("rh-icon").addClass("rhGrid-btnBar-a");
				temp.attr("id",_self._getUnId(n.ACT_CODE));
				temp.attr("actcode",n.ACT_CODE);
				_self._act(n.ACT_CODE,temp);
				var labelName = jQuery("<span></span>").addClass("rh-icon-inner").text(n.ACT_NAME);
				temp.append(labelName);
				var icon = jQuery("<span></span>").addClass("rh-icon-img").addClass("btn-" + n.ACT_CSS);
				temp.append(icon);
				_btnBar.append(temp);
			});
		}else{
			//将原来的按钮条替换为间隔条
			_btnBar.removeClass("rhGrid-btnBar").addClass("rhGrid-spacer");
		}
	}
	this._btnBar = _btnBar;
	this._btnBar.appendTo(this.content);
	return this._btnBar;
};

/*
 * 渲染引擎：构建列表(rh.ui.grid)，包括按钮、数据表格、分页条
 */
rh.vi.listViewBatch.prototype._bldGrid = function() {
	//判断如果_listData为null，则请求数据。
	if(!this._listData || jQuery.isEmptyObject(this._listData)){
		var options = {};
		this._linkWhere  =  this._linkWhere ? this._linkWhere : " and 1=2";
		options[UIConst.LINK_WHERE] = this._linkWhere;
		//设定不分页，取所有记录
		options["_NOPAGE_"] = "true";
		this._listData =  FireFly.getPageData(this.opts.sId,options) || {};	
	}
	//列表行按钮，只读时不生成按钮
	var rowBtns;
	if (!this.singleServFlag) {
		rowBtns = this.isReadOnly ? [] :  [{"ACT_NAME":"删除","ACT_CODE":"markDelete","ACT_CSS":"delete"}];
	} else {
		rowBtns = this._rowBtns;
	}
	var temp = {"id":this.opts.id,"pid":this.opts.pId,"mainData":this._data,
	            "parHandler":this,"pCon":this.content,"rowBtns":rowBtns,"allTDModify":this.allTDModify,
	            "batchFlag":this.batchFlag,"sortGridFlag":"true","buildPageFlag":"false"};
	temp["listData"] = this._listData;
	this.grid = UIFactory.create(rh.ui.grid,temp);
	this.grid.render();	
};

/*
 * 渲染引擎：渲染后执行方法
 */
rh.vi.listViewBatch.prototype._afterLoad = function() {
	//绑定事件
	this._bindEvent();
	//组装关联明细定义的过滤关联字段
	this._setLinkWhereItem();
	//没有按钮则自动隐藏多选框
	this.hideCheckBox();
};

/*
 * 渲染引擎：根据动作绑定相应的方法
 * @param aId 动作ID
 */
rh.vi.listViewBatch.prototype._act = function(aId,aObj) {
	var _self = this;
	var taObj = aObj;
	switch(aId) {
		case UIConst.ACT_BATCH_ADD://添加(默认版)
			taObj.bind("click",function() {
               _self._addNewTr();
               return false;
			});	
			break;
		case UIConst.ACT_ADD://添加(单独嵌入版)
			taObj.bind("click",function(event) {
				alert("暂时不支持添加，需要给rh.ui.card中构造列表的地方加入关联服务的传值");
				return false;
               _self._openCardView(UIConst.ACT_CARD_ADD);
               event.stopPropagation();
               return false;
			});	
			break;
		case UIConst.ACT_BATCH_SAVE://保存
		    taObj.bind("click",function() {
		    	  var datas = _self.grid.getModifyTrDatas();     
	              if (datas == null) {
	              	   _self.listBarTipError("请选择相应记录！");
	              } else {
	            	  //判断列表如果需要校验，对列表进行校验
	            	  if(_self.grid.needValidate() && !_self.grid.validate()) {
		          	       _self.listBarTipError("校验未通过");
		          	       return false;
		          	   }
	              	   _self.listBarTipLoad("提交中...");
	              	   setTimeout(function() {
	              		   // 保存之前
	              		   _self.beforeSave.call(_self, datas);
	              		   
		              	   var batchData = {};
		              	   batchData["BATCHDATAS"] = datas;
						   var resultData = FireFly.batchSave(_self.opts.sId,batchData,null,_self.getNowDom());
						   
						   // 保存之后
						   _self.afterSave.call(_self, datas);
						   
					       _self.refreshGrid();
	              	   },0);
	              }
		    });  
		    break;
	    case UIConst.ACT_DELETE://删除
		    taObj.bind("click",function() {
                var pkArray = _self.grid.getSelectPKCodes();
		    	if (jQuery.isEmptyObject(pkArray)) {
		    		 _self.listBarTipError("请选择相应记录！");
		    	} else {
		    		 var res = confirm("您确定要删除该数据么？");
		    		 if (res == true) {
			    		_self.listBarTipLoad("提交中...");
			    		setTimeout(function() {
			    			if(!_self.beforeDelete(pkArray)){
			    				return false;
			    			}
				    		var strs = pkArray.join(",");
				    		var temp = {};
				    		temp[UIConst.PK_KEY]=strs;
				    		var resultData = FireFly.listDelete(_self.opts.sId,temp,_self.getNowDom());
					        _self.refreshGrid();
					        _self.afterDelete();
			    		},0);	
		    		 } else {
		    		 	return false;
		    		 }
		    	}
		    }); 
		    break;
	}
};

/*
 * 渲染引擎：绑定事件
 */
rh.vi.listViewBatch.prototype._bindEvent = function(){
	var _self = this;
	if (_self.singleServFlag) {
		return;
	}
	//绑定列表行按钮点击事件
	this.grid.getBtn("markDelete").unbind("click",this._markDeleteClickEvent).bind("click", {"view":this},this._markDeleteClickEvent);

};

/*
 * 工具：获取按钮对象
 */
rh.vi.listViewBatch.prototype.getBtn = function(actCode) {
    var _self = this;
    if (this.btns[actCode]) {
    	return this.btns[actCode];
    } else {
    	return jQuery();
    }
};

/*
 * 工具：错误的提示信息
 * @param msg 提示内容
 */
rh.vi.listViewBatch.prototype.listBarTipError = function(msg,areaFlag) {
	var flag = true;
 	if (areaFlag) {
 		flag = areaFlag;
 	}
	Tip.showError(msg,flag);
};

/*
 * 工具：加载的提示信息
 * @param msg 消息
 */
rh.vi.listViewBatch.prototype.listBarTipLoad = function(msg,areaFlag) {
	var flag = true;
 	if (areaFlag) {
 		flag = areaFlag;
 	}
	Tip.showLoad(msg,flag);
};

/*
 * 工具：区分列表还是卡片
 */
rh.vi.listViewBatch.prototype.getNowDom = function() {
	var _self = this;
	if (this.opts.parHandler && _self.opts.bottomTabFlag && (_self.opts.bottomTabFlag == true)) {
	    return "listBottom";
	} else if (this.opts.parHandler) {
		return null;
	} else {
		return "list";
	}
};

/*
 * 工具：对外刷新列表数据方法
 */
rh.vi.listViewBatch.prototype.refreshGrid = function(options) {
	this._refreshGridBody(options);
};

/*
 * 工具：刷新组件内的grid
 */
rh.vi.listViewBatch.prototype._refreshGridBody = function(options) {
	var data = options || {};
	if (!data[UIConst.LINK_WHERE]) {
		data[UIConst.LINK_WHERE] = this._linkWhere;
	}
	this._listData =  FireFly.getPageData(this.opts.sId,data) || {};
	this.grid.refresh(this._listData);
	//绑定组件事件
	this._bindEvent();
	//没有按钮则自动隐藏多选框
	this.hideCheckBox();
	//刷新后执行
	if (this.afterRefresh) {
		this.afterRefresh();
	}
	//重算高度
	this._resetHeiWid();
};

/*
 * 工具：刷新组件
 */
rh.vi.listViewBatch.prototype.refresh = function(options) {
	options = options ? options : {};
    this._refreshGridBody(options);
};

/*
 * 工具：添加新的一行表格，不含数据
 */
rh.vi.listViewBatch.prototype._addNewTr = function() {
	var _self = this;
	var noDataTd = jQuery(this.grid._table).find(".rhGrid-showNO");
	if(noDataTd){
		//移除“无相关记录”行
		noDataTd.parent().remove();
	}
	var newTr = this.grid.addNewTrs();
	//隐藏新增列的删除按钮，保持列表中始终有一空列
	//jQuery(".rhGrid-td-rowBtn[icode='markDelete']",newTr).find(".rhGrid-td-rowBtnObj").hide();
	//重新绑定列表行事件
	this.grid._bindTrEvent();
	//绑定组件事件
	this._bindEvent();
	//重新计算高宽
	this._resetHeiWid();
};

/*
 * 工具：打开卡片页面
 * @param act 动作act
 * @param paramData 自定义传递的参数，供卡片获取
 */
rh.vi.listViewBatch.prototype._openCardView = function(act,pkCode,servId,readOnly,paramData) {
	var _act = UIConst.ACT_CARD_MODIFY;
	if (act && act == UIConst.ACT_CARD_ADD) {
	    _act = act;	
	}
	var sId = this.opts.sId;
	if (servId) {
		sId = servId;
    }
    var readFlag = this._cardRead;
    if ((this._readOnly === true) || (this._readOnly === "true")) {//只读关联功能
  	    readFlag = true;
    }
    if (readOnly) {
    	readFlag = readOnly;
    }
    backBtn = this.opts.cardBackBtn;
    //打开小卡片条件
    var widHeiArray = null;
    if (window.self == window.top) {//最外层的打开显示
        var miniCardWid = jQuery("body").width() - 300;
        widHeiArray = [miniCardWid,500];
    	backBtn = true;
    }
    this.cardView = null;
    var temp = {"act":_act,"sId":sId,"parHandler":this,"transferData":this._transferData,"readOnly":readFlag,"title":this._title,
    		    "paramData":paramData,"links":this.links,"pCon":this.opts.cardCon,"reset":this.opts.cardReset,
    		    "backBtn":backBtn,"widHeiArray":widHeiArray};
    temp[UIConst.PK_KEY] = pkCode;
    this.cardView = new rh.vi.cardView(temp);
    this.cardView.show();
    //RHFile.bldDestroyBase(this.cardView);
};

/*
 * 工具：获取当前页面的父句柄
 */
rh.vi.listViewBatch.prototype.getParHandler = function() {
	return this.opts.parHandler;
};

/*
 * 工具：解析操作表达式
 */
rh.vi.listViewBatch.prototype._excuteActExp = function(tmpExp) {
	var _self = this;
    //替换系统变量
	if (tmpExp.indexOf(";") > 0) {//如果有显示控制和后台操作表达式一起控制
		var expArray = tmpExp.split(";");
		tmpExp = expArray[0];
	}  
    tmpExp = Tools.systemVarReplace(tmpExp);
	tmpExp = Tools.itemVarReplace(tmpExp,_self.links);//关联定义的变量替换
	if (typeof this._queryId == "string") {//常用查询的替换变量 #系统#
		var normalData = {};
		normalData[UIConst.QUERY_ID] = _self._queryId;
		tmpExp = Tools.itemVarReplace(tmpExp,normalData);
	}
    //表达式是否为true
    var actExp = eval(tmpExp);
    return actExp;
};

/*
 * 工具：获取服务和id的合并值(服务-ID)
 * @param id ID值
 */
rh.vi.listViewBatch.prototype._getUnId = function(id) {
    return this.id + "-" + id;
};

/*
 * 工具：删除前方法执行
 */
rh.vi.listViewBatch.prototype.beforeDelete= function(pkArray) {
	return true;
};

/*
 * 工具：删除后方法执行
 */
rh.vi.listViewBatch.prototype.afterDelete = function() {
};

/*
 * 工具：隐藏多选框列
 */
rh.vi.listViewBatch.prototype.hideCheckBox = function() {
	var _self = this;
	if (_self.grid) {//非查询选择
		if (this._isHaveShowBtn() === false) {
			this.grid.hideCheckBoxColum();
		}
	}
};

/*
 * 工具：获取列表是否含有显示按钮
 */
rh.vi.listViewBatch.prototype._isHaveShowBtn = function() {
	var _self = this;
	var btns =  this._btnBar.find(".rhGrid-btnBar-a");
	if (btns.length == 0) {
		return false;
	} else {
		return true;
	}
};

/*
 * 工具：列表行删除按钮点击方法
 */
rh.vi.listViewBatch.prototype._markDeleteClickEvent = function(event) {
	var _self = event.data.view;
	var pTr = jQuery(this).parent().parent(".tBody-tr");
	var checkstate = jQuery(".rowIndex",pTr).attr("checked");
	if(pTr.hasClass("newTr")){
		pTr.remove();
		//重新计算高宽
		_self._resetHeiWid();
	}else{
		var pk = jQuery(this).attr("rowpk");
		if(pTr.hasClass("markDelete-tr")){
			pTr.removeClass("markDelete-tr");
			pTr.css('background-color','');
			//从删除主键缓存对象中移除
			if(pk){
				delete _self._deleteDataPks[pk];
			}
			//设定行“取消删除”按钮变为“删除”
	    	jQuery(this).find(".rh-icon-inner").text("删除");
	    }
	    else{
	    	pTr.addClass("markDelete-tr");
	    	//DimGray
	    	pTr.css('background-color','red');
	    	//加入到删除主键缓存对象中
	    	if(pk){
	    		_self._deleteDataPks[pk] = pk;
			}
	    	//设定行“删除”按钮变为“取消删除”
			jQuery(this).find(".rh-icon-inner").text("取消");
	    }
	}
	event.stopPropagation();
	return false;
};

/*
 * 工具：重置当前页面的高度，初始化时、从卡片返回列表时
 */
rh.vi.listViewBatch.prototype._resetHeiWid = function() {
	if(this.cardObj){
		this.cardObj._resetHeiWid();
	}
};

/*
 * 工具：重新渲染组件
 */
rh.vi.listViewBatch.prototype._reRender = function() {
	this.content.remove();
	this._layout();
	this._bldBtnBar();
	this._bldGrid();
	this._afterLoad();
};

/*
 * 工具：获取组件ChangeData
 */
rh.vi.listViewBatch.prototype.getChangeData = function(){
	var _self = this;
	var changeData = {};
	//获取默认数据（子单和主单关联的那部分数据，如外键的值）
	var defaultData = {};
	jQuery.each(_self._linkWhereItemArray,function(i,n){
		var value = null;
		if (n.LINK_VALUE_FLAG == 2) {//主单常量值
  		    value = n.ITEM_CODE;
  		}else{
  			value = _self.getMainItemValue(n.ITEM_CODE);
  		}
		defaultData[n.LINK_ITEM_CODE] = value;
	});
	//获取删除数据主键串
	var deletePks = "";
	jQuery.each(_self._deleteDataPks,function(i,n){
		deletePks +=n+",";
	})
	if(deletePks){
		changeData[this.opts.sId+'__DELS'] = deletePks.substring(0, deletePks.lastIndexOf(","));
	}
	//获取新增数据
	var newData = [];
	if(_self.grid.getNewTrDatas()){
		newData = _self.grid.getNewTrDatas();
	}
	//获取修改数据
	var modifyData = [];
	if(_self.grid.getModifyTrDatas()){
		modifyData=_self.grid.getModifyTrDatas()
	}
	//合并新增和修改数据
	var datas = [];
	datas = jQuery.merge(newData,modifyData);
	//将子单的默认数据填充
	jQuery.each(datas,function(i,n){
		n = jQuery.extend(n,defaultData); 
	});
	//将新增和修改数据放入changeData
	if(datas.length > 0){
		changeData[_self.opts.sId] = datas;
	}
	if(jQuery.isEmptyObject(changeData)){
		//返回""，标识没有数据修改
		return "";
	}else{
		//$.toJSON(changeData)
	    return changeData;
	}
};

/*
 * 工具：根据itemCode获取主单数据项的值
 */
rh.vi.listViewBatch.prototype.getMainItemValue = function(itemCode){
	var value = null;
	if(this.cardObj){
		value = this.cardObj.itemValue(itemCode);
	}
	return value;
};

/*
 * 工具：组装关联明细定义的过滤关联字段
 */
rh.vi.listViewBatch.prototype._setLinkWhereItem = function() {
	  var _self = this;
	  //关联功能构造
	  var linkItem = this._linkServData.SY_SERV_LINK_ITEM || {};
	  jQuery.each(linkItem, function(i,n) {
	  	if (n.LINK_WHERE_FLAG == 1) {
	  		_self._linkWhereItemArray.push(n);
	  	}
	  });
};
/*
 * 工具：组装where条件
 * @param data form表单数据
 */
rh.vi.listViewBatch.prototype._assembleWhere = function(data) {
	  //关联功能构造
	  var linkItem = this._linkServData.SY_SERV_LINK_ITEM || {};
	  var linkWhere = [];
	  var links = {};
	  var parVal = {};//关联字段值转换成系统变量,供子调用
	  jQuery.each(linkItem, function(i,n) {//生成子功能过滤条件
	  	if (n.LINK_WHERE_FLAG == 1) {
	  		linkWhere.push(" and ");
	  		linkWhere.push(n.LINK_ITEM_CODE);
	  		linkWhere.push("='");
	  		var value = data[n.ITEM_CODE];
	  		if (n.LINK_VALUE_FLAG == 2) {//主单常量值
	  		    value = n.ITEM_CODE;
	  		}
	  		linkWhere.push(value);
	  		linkWhere.push("' ");
	  	}
	  });
	  //关联服务定义里的过滤条件处理
	  var itemLinkWhere = Tools.itemVarReplace(this._linkServData.LINK_WHERE,data);
	  return linkWhere.join("")+ itemLinkWhere;
};

/*
 * 工具：获取Grid组件
 */
rh.vi.listViewBatch.prototype.getGrid = function(val) {
	return this.grid;
};

/*
 * 工具：根据code获取列Items
 */
rh.vi.listViewBatch.prototype.getColsItems = function(code) {
	return jQuery("#"+this.id+" .batchModify[icode='"+code+"']");
};

/*
 * 工具：设定组件只读属性
 */
rh.vi.listViewBatch.prototype._setReadOnly = function(val) {
	this.isReadOnly = val;
//	//批量编辑标识
//	this.batchFlag = val ? false : true;
//	//标识是否全部列为修改
//	this.allTDModify = val ? false : true;
	this.allTDModify = this.isReadOnly ? false : true;
	if (!this.isReadOnly && this.singleServFlag) {
		this.allTDModify = false;
	}
};

/*************************嵌入列表组件部分*********************************/
/**
 * 清空
 */
rh.vi.listViewBatch.prototype.clear = function() {
	/**此组件的值通过关联服务定义查询出来，不能清除，此处提供一个空实现*/
};
/**
 * 获取Label
 * @return {} 返回Label对象
 */
rh.vi.listViewBatch.prototype.getLabel = function() {
	var _self = this;
	return _self.obj.parent().parent().find("#" + _self._id + "_label");
};
/**
 * 获取容器
 */
rh.vi.listViewBatch.prototype.getContainer = function() {
	return this.obj.parent().parent();
};
/**
 * 校验是否为空
 */
rh.vi.listViewBatch.prototype.isNull = function() {
	var nullFlag = true;
	/**查询数据列*/
	var dataRows = jQuery(this.grid._table).find(".tBody-tr");
	if(dataRows && dataRows.length > 0){
		nullFlag = false;
	}
	return nullFlag;
};
/**
 * 返回jquery对象的html，便于调试
 */
rh.vi.listViewBatch.prototype.toString = function() {
	return jQuery('<div></div>').append(this.obj).clone().remove().html();
};
/**
 * 填充默认数据
 */
rh.vi.listViewBatch.prototype.fillDefault = function() {
	/**Form表单项方法，此处提供一个空实现*/
};
/**
 * 填充数据
 * @param val  组件值(此组件的数据通过关联服务定义的定义信息查询出来，val暂时无效，此处保留和form其他组件保持一致)
 * @param data form表单数据
 */
rh.vi.listViewBatch.prototype.fillData = function(val,data) {
	this._formData = data;
	/**组装查询条件*/
	this._linkWhere = this._assembleWhere(data);
	var options = {};
	options[UIConst.LINK_WHERE] = this._linkWhere;
	/**设定不分页，取所有记录*/
	options["_NOPAGE_"] = "true";
	/**刷新列表*/
	this._refreshGridBody(options);
};
/**
 * 重新设置数据
 */
rh.vi.listViewBatch.prototype.setValue = function(val) {
	/**本组件的数据通过关联服务定义的定义信息查询出来，不能设定值，此处提供一个空实现。*/
};
/**
 * 获取组件的值
 */
rh.vi.listViewBatch.prototype.getValue = function() {
	return this.getChangeData();
};
/**
 * 使组件无效
 */
rh.vi.listViewBatch.prototype.disabled = function() {
	this._setReadOnly(true);
	/**重新渲染组件*/
	this._reRender();
};
/**
 * 使组件有效
 */
rh.vi.listViewBatch.prototype.enabled = function() {
	this._setReadOnly(false);
	/**重新渲染组件*/
	this._reRender();
};
/**
 * 使组件无效（实现方式之一，已停用）
 */
rh.vi.listViewBatch.prototype._disabled = function() {
	if (this.singleServFlag) {
		var listJobj = this.obj;
		var shieldDiv = jQuery("<div id='" + this.opts.sId + "-shieldDiv'></div>");
		shieldDiv.css({"position":"absolute","width":"100%","height":"100%","background-color":"gray","opacity":"0.2","z-index":"1"});
		shieldDiv.appendTo(listJobj.parent());
		listJobj.parent().css({"position":"relative"});
	} else {
		this._setReadOnly(true);
		/**重新渲染组件*/
		this._reRender();
	}
};
/**
 * 使组件有效（实现方式之一，已停用）
 */
rh.vi.listViewBatch.prototype._enabled = function() {
	if (this.singleServFlag) {
		var listJobj = this.obj;
		jQuery("#" + this.opts.sId + "-shieldDiv", listJobj.parent()).remove();
		
	} else {
		this._setReadOnly(false);
		/**重新渲染组件*/
		this._reRender();
	}
};
/**
 * 隐藏该字段
 */
rh.vi.listViewBatch.prototype.hide = function() {
	this.isHidden = true;
	this.obj.parent().parent().hide();
};
/**
 * 显示该字段
 */
rh.vi.listViewBatch.prototype.show = function() {
	this.isHidden = false;
	this.obj.parent().parent().show();
};
/**
 * 设置组件为必须输入项
 */
rh.vi.listViewBatch.prototype.setNotNull = function(bool) {
	this.opts.isNotNull = bool;
};

/**
 * 获取校验对象
 */
rh.vi.listViewBatch.prototype._getValidateObj = function() {
	return this.opts.pCon;
};