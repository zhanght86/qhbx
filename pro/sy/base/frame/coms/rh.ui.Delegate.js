/**
 * 授权页面
 * @param {} options
 */
rh.ui.Delegate = function(options) {
	var defaults = {
		"servId": "",
		"dataId": "",
		"aclType": "",
		"hide" : "",
		"show" : "",
		"resizable" : true,
		"draggable" : true,
		"title" : "标题"
	};
	
	// 合并默认配置参数和传入的参数
	this._opts = jQuery.extend(defaults, options);

	// 默认授角色和部门用户
	if (this._opts.deleteTree) {
		this._delegateTree = this._opts.deleteTree;
	} else {
		this._delegateTree = {"ROLE":{"id":"SY_ORG_ROLE","showcheck":true},"DEPT_USER":{"id":"SY_ORG_DEPT_USER","showcheck":true}};
	}
	
	// 保存当前页面里的树，分别为以字典ID为key
	this._tree = {};
	
	this.dialogID = GLOBAL.getUnId("dialog-delegate", this._opts.id);
	
	this.tabsID = GLOBAL.getUnId("tabs-delegate", this._opts.id);
	
	// 数据权限
	this.aclData = {};
	
	// 数据权限类型
	this.aclType = defaults.aclType;
	
	// 一开始的角色
	this.preRoleAcl = [];
	
	// 一开始的部门和用户
	this.preDeptUserAcl = [];
	
	// 权限类型按钮
	this.aclTypeBtn = {};
};

/**
 * 打开授权页面
 */
rh.ui.Delegate.prototype.open = function() {
	var _self = this;
	jQuery("#" + this.dialogID).dialog("destroy");
	
	// 构造Dialog
	_self.dialog = jQuery("<div></div>").addClass("cardDialog").attr("id", this.dialogID).attr("title",this._opts.title);
	
	_self.dialog.appendTo(jQuery("body"));
	
	var height = 400;
    var width = 400;
    
    var scroll = RHWindow.getScroll(parent.window);
    var viewport = RHWindow.getViewPort(parent.window);
    var top = scroll.top + viewport.height / 2 - height / 2 - 88;
	
	var posArray = ["", top];
    
	jQuery("#" + _self.dialogID).dialog({
		autoOpen: false,
		height: height,
		width: width,
		modal: true,
		hide: _self._opts.hide,
		show: _self._opts.show,
		resizable: _self._opts.resizable,
		draggable: _self._opts.draggable,
		position: posArray,
		buttons: {
//			"关闭": function() {
//				_self.dialog.remove();
//			}
		},
		open: function() { 
		},
		close: function() {
			_self.dialog.remove();
		}
	});
    jQuery("#" + this.dialogID).dialog("open");
    jQuery(".ui-dialog-titlebar").last().css("display","block");//设置标题显示
    
    this.init();
};

rh.ui.Delegate.prototype.init = function() {
	var _self = this;
	
	this.tabs = jQuery("<div></div>");
	// <span class='rh-icon rhGrid-btnBar-a'><span class='rh-icon-inner'>保存<span class='rh-icon-img btn-save'></span></span></span></a>
	var aclBtn = jQuery("<div class='rhCard-btnBar' style='background-color:#F1EFEE;'></div>")
		.append(jQuery("<a class='rh-icon rhGrid-btnBar-a' id='" + this.tabsID + "' actcode='save'><span class='rh-icon-inner'>保 存<span class='rh-icon-img btn-save'/></span><span class='rh-icon-img btn-save'/></a>").click(function(){
			 
			_self._save();
			_self.dialog.remove();
		})
	).appendTo(this.tabs);

	_self.dialog.append(this.tabs);
	
	_self._buildTree();
	
	_self.loadData();
};

rh.ui.Delegate.prototype.loadData = function() {
	var _self = this;
	$.ajax({
        type: "POST",
        url: "SY_SERV_DACL_ITEM.show.do",
        data: {"SERV_ID":_self._opts.servId,"DATA_ID":_self._opts.dataId,"ACL_TYPE":_self.aclType},
        async: true,
        success: function(data){
        	_self.aclData = StrToJson(data);
        	_self._select(_self.aclData[_self.aclType]);
        	
        	_self._expand();
        },
        error: function(e) {alert("error occur!");}
	});
};

/**
 * 构造树
 */
rh.ui.Delegate.prototype._buildTree = function() {
	var _self = this;
	var setting = {
    	showcheck: false,  
    	url: "SY_COMM_INFO.dict.do",
        theme: "bbit-tree-no-lines", //bbit-tree-lines ,bbit-tree-no-lines,bbit-tree-arrows
        rhexpand: false,
        cascadecheck: false,
        checkParent: false,
        root_no_check: true,
        onnodeclick: function(item, id) {
        	jQuery.each(_self._tree, function(key, tree){
        		if(tree.getId() != id) {// 收起其它树,并取消其它树的选中
        			tree.unselected();
        		}
        	});
        },
        oncheckboxclick: function(item, s, id) {
        	jQuery.each(_self._tree, function(key, tree){
        		if(tree.getId() != id) {// 收起其它树,并取消其它树的选中
        			tree.unselected();
        		}
        	});
        }
    };

    jQuery.each(_self._delegateTree, function(key, dict){
    	setting["showcheck"] = dict.showcheck;
	    var dictData = FireFly.getDict(dict.id);
	    setting.dictId = dict.id;
	    setting.data = [{"NAME":dictData[0]["NAME"],"isexpand":true,"ID":"root","CHILD":dictData[0]["CHILD"]}];
	    var tree = new rh.ui.Tree(setting);
	    // 保存起来
	    _self._tree[key] = tree;
		_self.tabs.append(tree.obj);
    });
};

/**
 * 指定选中树的节点
 * @todo 添加其它支持改这
 */
rh.ui.Delegate.prototype._select = function(data) {
	var _self = this;
	_self.preRoleAcl = [];
	_self.preDeptUserAcl = [];
	_self._clean();
	jQuery.each(data, function(index, node){
		var owner = node["ACL_OWNER"];
		if(_self._tree["ROLE"] && owner.indexOf("R_") == 0) {// 角色
			_self.preRoleAcl.push(node);
			_self._tree["ROLE"].checkNode(owner.substr(2, owner.length));
		} else if(_self._tree["DEPT_USER"] && owner.indexOf("D_") == 0 || owner.indexOf("U_") == 0) {// 部门或用户
			_self.preDeptUserAcl.push(node);
			_self._tree["DEPT_USER"].checkNode(owner.substr(2, owner.length));
		}
	});
};

/**
 * 清空树
 */
rh.ui.Delegate.prototype._clean = function() {
	var _self = this;
	jQuery.each(_self._tree, function(key, tree){
		tree.clean();
	});
};

/**
 * 展开树
 */
rh.ui.Delegate.prototype._expand = function() {
	var _self = this;
	jQuery.each(_self._tree, function(key, tree){
		// 展开有子节点被选中的节点
		tree.expandParent();
	});
};

/**
 * 取得添加的和删除的权限实体
 * @todo 添加其它支持改这
 */
rh.ui.Delegate.prototype._save = function() {
	var _self = this;
	
	// 需要删除的IDs
	var deleteIds = [];
	
	// 需要添加的节点
	var addedNodes = [];
	
	// 处理角色的增加和删除
	if (this._tree["ROLE"]) {
		var roleAcl = this._tree["ROLE"].getCheckedNodes();
		// 获取添加删除的角色
		var roleAclAdd = this._getRoleDiffNodes(roleAcl, this.preRoleAcl, true);
		var roleAclDelete = this._getRoleDiffNodes(this.preRoleAcl, roleAcl, false);
		
		jQuery.each(roleAclDelete, function(index, item) {
			deleteIds.push(item["ACL_ID"]);
		});
		
		jQuery.each(roleAclAdd, function(index, item){
			var node = {};
			node["SERV_ID"] = _self._opts["servId"];
			node["DATA_ID"] = _self._opts["dataId"];
			node["ACL_TYPE"] = _self.aclType;
			node["ACL_OWNER"] = "R_" + item["ID"];
			addedNodes.push(node);
		});
	}
	
	// 处理部门和用户的增加和删除
	if (this._tree["DEPT_USER"]) {
		var deptUserAcl = this._tree["DEPT_USER"].getCheckedNodes();
		
		// 获取添加删除的部门用户
		var deptUserAdd = this._getDeptUserDiffNodes(deptUserAcl, this.preDeptUserAcl, true);
		var deptUserDelete = this._getDeptUserDiffNodes(this.preDeptUserAcl, deptUserAcl, false);
		
		jQuery.each(deptUserDelete, function(index, item){
			deleteIds.push(item["ACL_ID"]);
		});
		
		jQuery.each(deptUserAdd, function(index, item){
			var node = {};
			node["SERV_ID"] = _self._opts["servId"];
			node["DATA_ID"] = _self._opts["dataId"];
			node["ACL_TYPE"] = _self.aclType;
			if(item["OTYPE"] == 2) {
				node["ACL_OWNER"] = "D_" + item["ID"];
			} else if(item["OTYPE"] == 3) {
				node["ACL_OWNER"] = "U_" + item["ID"];
			}
			addedNodes.push(node);
		});
	}
	
	if(deleteIds.length > 0 && addedNodes.length > 0) {
		FireFly.batchSave("SY_SERV_DACL_ITEM",{"BATCHDELS":deleteIds.join(","),"BATCHDATAS":addedNodes}, function(){_self.loadData();});
	} else if(deleteIds.length > 0) {// 只有删除
		FireFly.batchSave("SY_SERV_DACL_ITEM",{"BATCHDELS":deleteIds.join(",")}, function(){_self.loadData();});
	} else if(addedNodes.length > 0) {// 只有添加
		FireFly.batchSave("SY_SERV_DACL_ITEM",{"BATCHDATAS":addedNodes}, function(){_self.loadData();});
	} else {// 没有变化
		Tip.showError("权限没有任何改变！", true);
	}              
};

/**
 * 过滤出左边比右边多的角色
 * @param {} arr1
 * @param {} arr2
 * @param {} isAdd 是否获取添加的
 * @return {}
 * @todo 添加其它支持得添加一个类似的方法
 */
rh.ui.Delegate.prototype._getRoleDiffNodes = function(arr1, arr2, isAdd) {
	return jQuery.grep(arr1, function(item1) {
		var bool = true;
		jQuery.each(arr2, function(index2, item2){
			if(isAdd) {
				if("R_" + item1["ID"] == item2["ACL_OWNER"]) {// 过滤掉
					bool = false;
					return;
				}
			} else {
				if(item1["ACL_OWNER"] == "R_" + item2["ID"]) {// 过滤掉
					bool = false;
					return;
				}
			}
		});
		return bool;
	});
};

/**
 * 过滤出左边比右边多的部门和用户
 * @param {} arr1
 * @param {} arr2
 * @param {} isAdd
 */
rh.ui.Delegate.prototype._getDeptUserDiffNodes = function(arr1, arr2, isAdd) {
	return jQuery.grep(arr1, function(item1) {
		var bool = true;
		jQuery.each(arr2, function(index2, item2){
			if(isAdd) {
				if(item1["OTYPE"] == 2) {// 部门
					if("D_" + item1["ID"] == item2["ACL_OWNER"]) {// 过滤掉
						bool = false;
						return;
					}
				} else if(item1["OTYPE"] == 3) {// 用户
					if("U_" + item1["ID"] == item2["ACL_OWNER"]) {// 过滤掉
						bool = false;
						return;
					}
				}
			} else {
				if(item2["OTYPE"] == 2) {// 部门
					if(item1["ACL_OWNER"] == "D_" + item2["ID"]) {// 过滤掉
						bool = false;
						return;
					}
				} else if(item2["OTYPE"] == 3) {// 用户
					if(item1["ACL_OWNER"] == "U_" + item2["ID"]) {// 过滤掉
						bool = false;
						return;
					}
				}
			}
		});
		return bool;
	});
};