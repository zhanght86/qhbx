/**
 * poll view 
 */

/** 投票展示页面渲染引擎 */
GLOBAL.namespace("rh.vi");

rh.vi.poll = function(options) {
	var defaults = {
		"id" : options.sId + "-rhPollView",
		"sId" : "",// 服务ID
		"dId" : "", // 操作ID
		"pollId":"",
		"pCon" : null,
		"pId" : null,
		"linkWhere" : "",
		"extWhere" : ""
	};
	this.opts = jQuery.extend(defaults, options);
	this._id = this.opts.id;
	this._pollId = this.opts.pollId;
	this.servId = this.opts.sId;
	this._pCon = this.opts.pCon;
	this._data = this.opts.dId;
	this._searchWhere = "";// 查询条件
	this._originalWhere = "";// 服务定义条件
	this._extendWhere = "";// 扩展条件
	this._linkWhere = this.opts.linkWhere;// 关联功能过滤条件

	this.nowPage = 1;
	this.keyStr = "";

};

/*
 * 渲染列表主方法
 */
rh.vi.poll.prototype.show = function() {
	var _self = this;
	// this._initMainData();
	this._layout();
	//取投票信息
	var pollParm = {};
	pollParm["_PK_"] = this._pollId;
	var poll = parent.FireFly.doAct("SY_COMM_POLL", "show", pollParm);
	this._data = poll;
	var options = poll.OPTIONS;
	
	//参与人数
	var allVotes = 0;
	 jQuery.each(options, function(index, node){
		 allVotes += Number(node.OPTION_COUNTER);
	 });

	// 标题
	var titleDiv = jQuery("<div><div class='join_nm'><div class='text'>参与人数</div><div class='count'>"+allVotes+"</div></div></div>").addClass("rh-cusearch-titleDiv")
			.appendTo(_self.pollContainer);
	
	//div(rh-set-title+rh-set-info)
	var titleRight = jQuery("<div style='height:56px;'></div>").addClass("").appendTo(titleDiv);
	jQuery("<span>" + poll.POLL_TITLE + "</span>").addClass("rh-set-title")
			.appendTo(titleRight);
	
	
	//最多选几项
	var optionDesc = "单选";
	if (1 < poll.POLL_MAX_OPTION) {
		optionDesc = "最多选择" + poll.POLL_MAX_OPTION + "项"; 
	}
	
	
	var infoDiv = jQuery("<div></div>").addClass("rh-set-info").appendTo(titleRight);
	//结束时间 
	var endDesc = "";
	if (poll.POLL_END_TIME) {
		endDesc = "结束时间:" + poll.POLL_END_TIME + " | ";
	}
	jQuery("<span>" + endDesc + optionDesc + "</span>").addClass("rh-set-time").appendTo(infoDiv);
	
	

	// 说明
	var descDiv = jQuery("<div></div>").addClass("rh-set-descDiv").appendTo(
			_self.pollContainer);
	descDiv.text(poll.POLL_DESCRIPTION);

	
	//投票选项
	_self._table = jQuery("<table cellspacing='0' cellpadding='4'></table>").attr("id", _self._id).addClass("rh-set-table").appendTo(_self.pollContainer);
	 jQuery.each(options, function(index, item){
		 var vote = item.OPTION_COUNTER;
		 var rate = 0.00;
		 if (0 < allVotes) {
			rate =  vote / allVotes * 100.00;
		 }
		 rate = rate.toFixed(2);
		 var showResult = false;
		 if (1==poll.SHOW_RESULT) {
			 showResult = true;
		 }
		 
		 var tr = jQuery("<tr></tr>").appendTo(_self._table);
		 var optTd = jQuery("<td style='width:40%;text-align:left;'></td>").appendTo(tr);
		 //选项主题
		 if (1 < poll.POLL_MAX_OPTION) { 
			 jQuery("<label><input type='checkbox' name='optionItem' class='optionItem' value='" + item.OPTION_ID + "'><span class='option'>" + item.OPTION_TEXT +"</span></label>").appendTo(optTd);
		 } else {
			 jQuery("<label><input type='radio' name='optionItem' class='optionItem' value='" + item.OPTION_ID + "'><span class='option'>" + item.OPTION_TEXT +"</span></label>").appendTo(optTd);
		 }
		 if (showResult) {
	  	 //选项结果图
		 jQuery("<td width='40%'><div class='process' style='display: block;'><div class='style"+(index%9)+"' style='width: "+(1.7*rate)+"px;'></div></div></td>").appendTo(tr);
		 //选项结果
		 var optRstTd = jQuery("<td width='20%' class='black' id='result'></td>").appendTo(tr);
		 jQuery("<nobr>" +vote +" (" + rate + "%)</nobr>").appendTo(optRstTd);
		 }
		 
	 });
	 
	 //提交
	 //已经投票的用户，我们将不显示投票按钮
	 if (2 == poll.VOTED) {
	 	commitBtn = jQuery("<div><input id='poll' type='button' value='投票'></div>").addClass("btn").appendTo(_self.pollContainer);
	 	commitBtn.bind("click", function() {
    	_self.submit();
        });
	 } else {
		 jQuery("<div>已投票</div>").addClass("done").appendTo(_self.pollContainer);
	 }
	
	 return allVotes;
	// _self._bldTrs(data);
	// _self._bldMore(node);
	// _self._afterLoad();
};
rh.vi.poll.prototype._initMainData = function() {
};

/**
 * submit
 */
rh.vi.poll.prototype.submit = function() {
	var _self = this;
	var poll = this._data;
	var pollId = poll.POLL_ID;
	var options = jQuery("input:checked");
	if ( poll.POLL_MAX_OPTION < options.length) {
		alert("本次投票最多可以选" + poll.POLL_MAX_OPTION + "项." );
	}
	var optIdStr = "";
	jQuery.each(options,function(i,n) {
		var optId = jQuery(n).val();
		optIdStr += optId + ",";
	});
	
	
	var data = {};
	data["OPTION_ID"] = optIdStr;
	data["POLL_ID"] = pollId;
	var resultData = parent.FireFly.doAct("SY_COMM_POLL", "vote", data);
	if (resultData[UIConst.RTN_MSG] && resultData[UIConst.RTN_MSG].indexOf(UIConst.RTN_OK) == 0) {
  		Tip.show("保存成功!");
  		//刷新页面
  		_self.refresh();
	} else {
  		Tip.show("保存失败！" + JsonToStr(resultData), true);
	}
	
};

/*
 * 刷新
 */
rh.vi.poll.prototype.refresh = function() {
	var _self = this;
	_self._pCon.empty();
	_self.show();
};
/*
 * 构建列表页面布局
 */
rh.vi.poll.prototype._layout = function() {
	var _self = this;
	this.searchBar = jQuery("<div></div>").addClass("mbTopBar").appendTo(
			this._pCon);
	this.pollContainer = jQuery("<div></div>").addClass("mbSearch-container")
			.appendTo(this._pCon);// 列表外容器
};

