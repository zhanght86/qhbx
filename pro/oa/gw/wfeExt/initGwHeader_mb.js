GLOBAL.namespace("rh.vi");

rh.vi.gwHeader = function(options) {
	this.servId = options.servId || null;
	this.parHandler = options.parHandler || null;
	this._viewer = this.parHandler;
	//公文模板定义
	this.tmpl = this.parHandler.byIdData.tmpl;
}

/**
 * 是否能装载机关代字的设置
 */
rh.vi.gwHeader.prototype.canLoadGwCode = function(){
	if(this._viewer.wfCard.isWorkflow() && this._viewer.wfCard.getCustomVarContent("gwCode")){
		return true;
	}
	
	return false;
}

/**
 * 公文审批单初始化方法
 */
rh.vi.gwHeader.prototype.init = function() {
	var _viewer = this.parHandler;
	this.createHeader();
	this.createYearCodeItem();
}

/**
 * 创建公文表单头信息
 */
rh.vi.gwHeader.prototype.createHeader = function() {
	var _viewer = this.parHandler;
	if (this.tmpl) {
		//设置公文标题
		_viewer.form.obj.find("[code=TMPL_TITLE]").html(this.tmpl["TMPL_TITLE"]);//设置公文标题
		if (this.tmpl["DISPLAY_MEMO"] == UIConst.STR_YES && this.tmpl["MEMO"] != "") {
			var memoTd = _viewer.form.obj.find("[code=TMPL_MEMO]");
			if(memoTd.length > 0){
				memoTd.html(this.tmpl["MEMO"]);
				memoTd.css("display","");
				memoTd.parent().css("display","");
			}
		}
	}	
}

/**
 * 初始化机关代字编码配置项
 */
rh.vi.gwHeader.prototype.initYearCodeOpts = function(userOpts){
	var _viewer = this.parHandler;
	var opts = {
			_viewer : _viewer,
			autoMax : false,   //是否自动获取编号最大值
			showMaxBtn : false, //是否显示获取最大编号值按钮
			canEditCode : false,    //是否能编辑
			canEditYear : false,	//是否可以编辑年度
			canEditNumber : false,  //是否能编辑编号
			showCodeInput : true,   //是否显示代字输入选择框
			defValue : ""      //代字的默认值
			
		};
	jQuery.extend(opts,userOpts);	
	
	var gwCode = "";
	if (_viewer.wfCard && _viewer.wfCard.isWorkflow()) {
		//从流程节点获取定义的机关代字控制的变量gwCode
		gwCode = _viewer.wfCard.getCustomVarContent("gwCode");
		if (!gwCode) {
			gwCode = "0";
		}		
	}
	
	//gwCode==0，表示启动了工作流，但是工作流没有设置gwCode的值，采用默认值。
	//gwCode==1，则只能修改代字和年度，编号不可编辑且不自动获取编号最大值
	//gwCode==2，则代字和年度、编号均可修改，并自动根据年度和代字变化取得编号最大值，不显示获取最大编码的按钮
	//gwCode==3，代字、年度不可修改，可以修改编号，以及显示获取最大编号按钮。
	//gwCode==4, 代字、年度、编号都可以修改，不显示最大编号按钮。
	//gwCode==5, 代字、年度、编号都可以修改，显示最大编号按钮。
	if(gwCode == "0"){
		opts.canEditCode = false;
		opts.canEditYear = false;
		opts.canEditNumber = false;
	}else if(gwCode == "1") {
		opts.canEditCode = true;
		opts.canEditYear = true;
	}else if(gwCode == "2") {
		opts.canEditCode = true;
		opts.canEditYear = true;
		opts.canEditNumber = true;
		opts.autoMax = true;
	}else if(gwCode == "3") {
		opts.canEditCode = false;
		opts.canEditYear = false;
		opts.canEditNumber = true;
		opts.showMaxBtn = true;
	}else if(gwCode == "4"){
		opts.canEditCode = true;
		opts.canEditYear = true;
		opts.canEditNumber = true;
		opts.autoMax = false;
	}else if(gwCode == "5"){
		opts.canEditCode = true;
		opts.canEditYear = true;
		opts.canEditNumber = true;
		opts.autoMax = true;
		opts.showMaxBtn = true;
	}
	
	return opts;
}

//封装的获取机关代字的对象
rh.vi.gwHeader.prototype.createYearCodeItem = function() {
	var _viewer = this.parHandler;
	var userOpts = {};
	if(_viewer.byIdData["_ADD_"] == "true" && this.tmpl["CODE_DISPLAY_TYPE"].length > 0){
		userOpts = StrToJson(this.tmpl["CODE_DISPLAY_TYPE"]);
	}
	
	var opts = this.initYearCodeOpts(userOpts);

	// 设置机关代字
	var yearCode = opts._viewer.form.getItem("GW_YEAR_CODE");

	if (!yearCode) { //如果没有代字输入框则
		return;
	}

	// 若设置了机关代字文本框是否显示，
	if (opts.showCodeInput) {
		yearCode.obj.show();
	} else {
		yearCode.obj.hide();
	}
	
	//设置是否可以编辑
	if (opts.canEditCode) {
		yearCode.enabled();
	}else{
		yearCode.disabled();
	}	
	
	// 设置保存的机关代字
	if(yearCode.type.toLowerCase() == "select"){ //代字输入框为Select
		if(this.canLoadGwCode() || (opts.showCodeInput && opts.canEditCode)){ //是否需要装载机关代字
			//取得主办部门
			var params = {"tmplId":this.servId,"ZB_TDEPT": _viewer.getByIdData("GW_ZB_TDEPT")};
			//流程中需要取机关代字
			this.gwYearCodes = FireFly.doAct("OA_GW_TMPL", "gwYearCodes",params , false);
			//增加选项
			yearCode.addOptions(this.gwYearCodes["GW_YEAR_CODES"]);
			
			//如果是起草， 如果只有一条，默认选上
			if (this._viewer._actVar == UIConst.ACT_CARD_ADD) { //添加模式
				if (this.gwYearCodes["GW_YEAR_CODES"].length == 1) { //只有一个值
					yearCode.setValue(this.gwYearCodes["GW_YEAR_CODES"][0].ID); //设置默认值
				}
			}
		}
		
		if (opts._viewer.getByIdData("GW_YEAR_CODE") != "") { //表单中已将有值
			var selectedVal = _viewer.getByIdData("GW_YEAR_CODE");
//			this.selectYearCodeOption(yearCode,selectedVal);
			yearCode.setValue(selectedVal,selectedVal);
		} else if((opts.defValue && opts.defValue.length > 0) ){ //设置默认值
//			this.selectYearCodeOption(yearCode,opts.defValue);
			yearCode.setValue(selectedVal,selectedVal);
		} 
	} else if(yearCode.type.toLowerCase() == "text"){
		if(opts.defValue && opts.defValue.length > 0){
			yearCode.value(opts.defValue);
		}
	}
	


	// 年度设置了只读，则机关代字、年度、编号均不能修改
	if (!opts.canEditYear) {
		opts._viewer.form.getItem("GW_YEAR").disabled();
	}

	var yearNumber =opts._viewer.form.getItem("GW_YEAR_NUMBER");
	
	if(yearNumber){
		var gwYearNum = yearNumber.getValue();
		if(gwYearNum == "0"){
			yearNumber.setValue(""); 
		}
	}
	if(yearNumber && opts.canEditNumber){
		yearNumber.enabled();
	}else{
		yearNumber.disabled();
	}
	
	// 自动生成最大码
	if (opts.autoMax) {
		getMaxCode(opts._viewer, yearCode,true);
		yearCode.obj.bind("change", function() { // 机关代字选择事件
			getMaxCode(opts._viewer, yearCode, false);
		});
		opts._viewer.form.getItem("GW_YEAR").obj.bind("change", function() { // 年度修改事件
			getMaxCode(opts._viewer, yearCode, false);
		});
		opts._viewer.form.getItem("GW_YEAR_NUMBER").enabled();
	}
	// 显示自动获取最大编码的按钮
	if (opts.showMaxBtn 
			&& (this._viewer.wfCard && !this._viewer.wfCard.isLocked())) {
		//var maxButton = jQuery("<a class='rh-icon rhGrid-btnBar-a' style='padding-left: 2px;'><span class='rh-icon-inner'>取最大编号</span><span class='rh-icon-img btn-fenfa'></span></a>");
		
		//maxButton.appendTo(codeDiv);
		//maxButton.bind("click", function() {
		var yearNumVal = yearNumber.getValue();
		if(yearNumVal == ""){
			getMaxCode(opts._viewer, yearCode, false);
		}
		//});
		yearNumber.enabled();
	}
	
};

/*
 * 根据机关代字和年度获取公文最大编号
 * @param _viewer 表单对象
 * @param yearCode 公文年度
 * @param compareFlag 参数是否与表单值进行对比
 */
function getMaxCode(_viewer, yearCode, compareFlag) { // 获取机关代字最大值
	var param = {};
	param["GW_YEAR_CODE"] = yearCode.getValue();
	param[UIConst.PK_KEY]  = _viewer.getPKCode();
	param["GW_YEAR"] = _viewer.form.getItem("GW_YEAR").getValue();
	param["TMPL_TYPE_CODE"] = _viewer.getByIdData("TMPL_TYPE_CODE");
	var yearNumber;
	if (compareFlag
			&& (param["GW_YEAR_CODE"] == _viewer.getByIdData("GW_YEAR_CODE"))
			&& (param["GW_YEAR"] == _viewer.getByIdData("GW_YEAR"))) { // 机关代字修改时，选中缺省的代字和年度，保留存储的编号
		yearNumber = _viewer.getByIdData("GW_YEAR_NUMBER");
	} else {
		var maxYearNumber = FireFly.doAct(_viewer.servId, "getMaxCode", param, false);
		yearNumber = maxYearNumber["GW_YEAR_NUMBER"];
	}

	_viewer.form.getItem("GW_YEAR_NUMBER").setValue(yearNumber);
}


