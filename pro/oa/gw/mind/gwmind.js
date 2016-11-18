GLOBAL.namespace("rh.gw");
rh.gw.mind = function(options) {
  var defaults = {
    "id":"",
    "pCon":null,
    "wfCard":null,
    "servId":null,
    "dataId":null
  };
  this._opts = jQuery.extend(defaults,options);	
  this._data = {};
  this._servId = this._opts.servId;
  this._dataId = this._opts.dataId;
  this._pCon = this._opts.pCon;
  this._wfCard = this._opts.wfCard;
};
/**
 *
 */
rh.gw.mind.prototype.render = function() {
var wfCard = this._wfCard;
var authBean = wfCard.getAuthBean();
if (wfCard != null && authBean.userDoInWf) {
  this._initText();
  }
  this._initData();
  this._initClos();
  this._initTable();
};
rh.gw.mind.prototype._initText = function() {
  var _self = this;
  //get work flow data
  var wfCard = this._wfCard;
  var authBean = wfCard.getAuthBean();
  
  //current mind code name
  var currMindCodeName = this._wfCard.getMindCodeBean().CODE_NAME;
  var currNiId = this._wfCard.getNodeInstBean().NI_ID;
  var currMindCode = this._wfCard.getMindCodeBean().CODE_ID;
  var currMindDisRule = this._wfCard.getMindCodeBean().MIND_DIS_RULE;
  var currServId = "GW_FW_GSFW";
  var currDataId = this._dataId;
  //意见类型，1：普通意见,2:手写意见,3:附件
  var currMindType = 1;
    
  var div = jQuery("<div></div>").appendTo(this._pCon);
  jQuery("<span>请填写" + currMindCodeName +"意见&nbsp;&nbsp;</span>").appendTo(div);
  var favoBtn = jQuery("<a href='#'>常用批语</a>").appendTo(div);
  favoBtn.bind("click",function() {
    var inputName ="MIND_TEXT";
    var configStr = "SY_COMM_MIND_USUAL,{'TARGET':'"+inputName+"','SOURCE':'ACT_CODE','EXTWHERE':' and S_USER=^"+"@USER_CODE@"+"^','TYPE':'single'}";
    var options = {"itemCode":inputName,"config" :configStr,"rebackCodes":inputName,"parHandler":this,"formHandler":this};		
    var queryView = new rh.vi.rhSelectListView(options);
    queryView.show();
  });
  
  //Stylus pen
  if (authBean.canHandWrite){
   jQuery("<span>&nbsp;&nbsp;</span>").appendTo(div);
   var  stylusBtn = jQuery("<a href='#'>手写笔</a>").appendTo(div);
  stylusBtn.bind("click",function() {
    var obj = window.open("/gw/tuya/tuya.html","name",""); 
  });
  }
  

  _self.text = jQuery("<textarea></textarea>").attr("cols","77").attr("rows","5").attr("id","MIND_TEXT").appendTo(this._pCon);
  _self.textDataId = "";
 
  var btn = jQuery("<a href='#'>修改" + currMindCodeName + "意见</a>").appendTo(this._pCon);
  btn.bind("click",function() {
    var data = {};
    if (null != _self.textDataId) {
    data["_PK_"] = _self.textDataId;
    }
    data["MIND_CONTENT"] = _self.text.val();
    data["MIND_CODE"] = currMindCode;
    data["SERV_ID"] = currServId;
    data["DATA_ID"] = currDataId;
    data["MIND_TYPE"] = currMindType;
    data["WF_NI_ID"]= currNiId;
    data["MIND_DIS_RULE"]= "";
    data["BD_USER"]= "";
    data["DB_UNAME"]="";
    var resultData = FireFly.doAct("SY_COMM_MIND","save",data);
    if (resultData[UIConst.RTN_MSG] && resultData[UIConst.RTN_MSG].indexOf(UIConst.RTN_OK) == 0) {
      _self.refresh();
    } else {
      showBarTip("返回错误，请检查！" + JsonToStr(resultData), true);
    }
  });

};
/*
 * 初始化数据
 * @param datas 菜单数据
 * @param topFlag 顶级菜单标识
 */
rh.gw.mind.prototype._initData = function() {
  var _self = this;
  var data = {};
  data["_NOPAGE_"] = "YES";
  data["_searchWhere"] = " and GW_ID='" + "ss" + "'";
  var res = FireFly.doAct("SY_COMM_MIND","getMindGroupByType",data);
  this._data = res._DATA_;
};
rh.gw.mind.prototype._initClos = function() {
  this.table = jQuery("<table border=0 cellspacing=0 cellpadding=0 aglign=center valign=top></table>");
  this.table.appendTo(this._pCon);
};
rh.gw.mind.prototype._initTable = function() {
  var _self =  this;
  var wfCard = this._wfCard;
  var authBean = wfCard.getAuthBean();
  this.table.empty();
 
  jQuery.each(this._data,function(i, typeArray) {
    var typeTr = jQuery("<tr></tr>");
    jQuery("<td></td>").append("<div><img src='/gw/images/mind.gif'/> " + typeArray._PK_ +"</div>").appendTo(typeTr);
    jQuery("<td></td>").appendTo(typeTr);
    jQuery("<td></td>").appendTo(typeTr);
    jQuery("<td></td>").appendTo(typeTr);
    typeTr.appendTo(_self.table);

    jQuery.each(typeArray._DATA_,function(index, n) {
      var tr = jQuery("<tr></tr>");
      //      jQuery.each(n,function(j,m) {
      //      	  var td = jQuery("<td></td>");
      //      	  td.append(m).appendTo(tr);
      //      });
      jQuery("<td></td>").append("&nbsp;&nbsp;&nbsp;&nbsp;").append(n.S_UNAME ).appendTo(tr);
      jQuery("<td></td>").append("&nbsp;&nbsp;&nbsp;&nbsp;").append(n.MIND_CONTENT).appendTo(tr);
      jQuery("<td></td>").append("&nbsp;&nbsp;&nbsp;&nbsp;").append(n.S_MTIME.substring(0,19)).appendTo(tr);

     //if do in workflow and the currentUser mind's owner, we show control button
      if (wfCard != null && authBean.userDoInWf && System.getUser("USER_CODE") == n.S_USER) {      
      //current mind set into text
      _self.text.attr("value",n.MIND_CONTENT);
      _self.textDataId = n.MIND_ID;
      
      var btn = jQuery("<a href='#'>删除</a>");
      btn.bind("click",function() {
        var data = {};
        data["_PK_"] = n.MIND_ID;
        var resultData = FireFly.doAct("SY_COMM_MIND","delete",data);
        if (resultData[UIConst.RTN_MSG] && resultData[UIConst.RTN_MSG].indexOf(UIConst.RTN_OK) == 0) {
          _self.refresh();
        } else {
          showBarTip("返回错误，请检查！" + JsonToStr(resultData), true);
        }
      });
      jQuery("<td></td>").append("&nbsp;&nbsp;&nbsp;&nbsp;").append(btn).appendTo(tr);
      }
      tr.appendTo(_self.table);    	
    });
    jQuery("</br>").appendTo(this._pCon);
    jQuery("</br>").appendTo(this._pCon);
  });
};
rh.gw.mind.prototype.refresh = function() {
  this._initData();
  this._initTable();
};


















