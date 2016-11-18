GLOBAL.namespace("rh.da");
rh.da.rhGroupAct = function(options) {
	jQuery(document).data("existBtnGroup","true");
	this._viewer = options.parHandler || null;
}

/**
 * 构建按钮组
 * name 组按钮名称
 * code 组按钮编码
 * 
 */
rh.da.rhGroupAct.prototype.initBtnGroup = function(name, code) {
	var _self = this;
	//构建按钮容器a标签
	var temp = jQuery("<a></a>").addClass("rh-icon").addClass("rhGrid-btnBar-a");
	temp.attr("id",  _self._viewer.opts.sId+"-"+code);
	temp.attr("actcode", code);
	//构建按钮文字span标签
	var labelName = jQuery("<span></span>").addClass("rh-icon-inner").text(name);
	temp.append(labelName);
	//构建按钮图标span标签
	var icon = jQuery("<span></span>").addClass("rh-icon-img").addClass("btn-budeng");
	temp.append(icon);
	//更多下拉图标
	var moreIcon = jQuery("<span></span>").addClass("rh-icon-img-more");
	labelName.append(moreIcon);
	var btnGroupCon = jQuery("<div class='rh-icon-groupCon'></div>");
	btnGroupCon.appendTo(temp);	
	temp.appendTo(jQuery(".rhGrid-btnBar"));	
	//绑定事件
	temp.bind("click",function(event){
		var btnGroupObj = jQuery(this).find(".rh-icon-groupCon");
		if(btnGroupObj.css("display") == "none"){
			_self._showBtnGroup(btnGroupObj);
		} else {
			_self._hideBtnGroup(btnGroupObj);
		}
		event.stopPropagation();
	});	
	return btnGroupCon;
};

/**
 * 插入组方式渲染按钮
 * btnDef 按钮定义
 * btnsCon 按钮容器
 */
rh.da.rhGroupAct.prototype._bldGroupBtn = function(btnID , GroupID) {	
	var addObj = jQuery("#"+btnID);
	var groupObj = jQuery("#"+GroupID+" .rh-icon-groupCon");
	addObj.appendTo(groupObj);
};

/**
 * 显示按钮组
 * 组对象
 */
rh.da.rhGroupAct.prototype._showBtnGroup = function(groupObj) {
	this._hideAllBtnGroup();
	groupObj.show();
};

/**
 * 隐藏按钮组
 * 组对象
 */
rh.da.rhGroupAct.prototype._hideBtnGroup = function(groupObj) {
	groupObj.hide();
};


rh.da.rhGroupAct.prototype._hideAllBtnGroup = function() {
	jQuery(".rh-icon-groupCon").hide();
};

/**
 * 根据按钮所属的组，添加组名.并将按钮添加至组中。
 */
rh.da.rhGroupAct.prototype.addGroupsName = function(){
	var _self = this;
	var groupNames = [];
	//循环服务定义中的按钮
	jQuery.each(_self._viewer._data.BTNS, function(i,n){
		//服务定义中“组”的值。n.ACT_TIP：按钮定义中“说明”一项。将来按钮定义中添加“所属组”属性后，替换ACT_TIP即可 
		var groupName = n.ACT_TIP || "";
		//gropNameArr:将值按照“,”拆分。gropNameArr[0]:code    gropNameArr[1]：组名称
		var gropNameArr = "";
		if(groupName != ""){
			 gropNameArr = groupName.split(",");
		}
		//如果“所属组”中定义了组信息。
		if(groupName != ""){
			//如果数组中取不到组名，则将组名放到一个数组中并在前台添加组。判断是否有指定名称的组名是为了出去重复的组
			if(!groupNames[gropNameArr[1]]){
				//将组名称放入数组
				groupNames[gropNameArr[1]] = gropNameArr[1];
				//初始化组名
				_self.initBtnGroup(gropNameArr[1],gropNameArr[0]);
			}
			//将取到的按钮添加到组中。
			_self._bldGroupBtn(_self._viewer.opts.sId+"-"+ i , _self._viewer.opts.sId+"-"+gropNameArr[0]);
		}
	});
};