GLOBAL.namespace("rh.gw");
rh.gw.file = function(options) {
  var defaults = {
    "id":"",
    "pCon":null,
    "GW_ID":""
  };
  this._opts = jQuery.extend(defaults,options);	
  this._data = {};
  this._pCon = this._opts.pCon;
};
/*
 * 渲染公文文件主方法
 */
rh.gw.file.prototype.render = function() {
  this._initZhengwen();
  this._setZhengwen();
  this._initReferFile();
  this._setReferFile();

};
rh.gw.file.prototype._initZhengwen = function() {
  jQuery("<div><img src='/gw/images/mind.gif'/>正文</div><br>").appendTo(this._pCon);
  this.zwTabl = jQuery("<table border=0 cellspacing=0 cellpadding=0 aglign=center valign=top></table>");
  this.zwTabl.appendTo(this._pCon);
};

rh.gw.file.prototype._setZhengwen = function() {
  var _self = this;
  this.zwTabl.empty();
  var data = {};
  data["_NOPAGE_"] = "YES";
  data["_searchWhere"] = " and GW_ID='" + "ss" + "'";
  var res = FireFly.getListData("GW_FILE",data)._DATA_;

  //set table data
  jQuery.each(res,function(i, n) {
    var tr = jQuery("<tr></tr>");
    jQuery("<td></td>").append("&nbsp;&nbsp;&nbsp;&nbsp;" +  n.DISPLAY_NAME).appendTo(tr);
    jQuery("<td></td>").append("&nbsp;&nbsp;&nbsp;&nbsp;" + n.S_USER).appendTo(tr);
    var btn = jQuery("<a href='#'>查看</a>");
    btn.bind("click",function() {
      //	  data["_PK_"] = n.MIND_ID;
      readOfficeFile('test.doc','/gw/ms_office/demo_file.doc','read');		
    });
    jQuery("<td></td>").append("&nbsp;&nbsp;&nbsp;&nbsp;").append(btn).appendTo(tr);
    tr.appendTo(_self.zwTabl);    	
  });   
};

rh.gw.file.prototype._initReferFile = function() {
  jQuery("<div><img src='/gw/images/mind.gif'/>相关文件</div><br>").appendTo(this._pCon);
  this.rfTabl = jQuery("<table border=0 cellspacing=0 cellpadding=0 aglign=center valign=top></table>");
  this.rfTabl.appendTo(this._pCon);
};
rh.gw.file.prototype._setReferFile = function() {
  var _self =  this;
  this.rfTabl.empty();
  //get data 
  var _self = this;
  var data = {};
  data["_NOPAGE_"] = "YES";
  data["_searchWhere"] = " and GW_ID='" + "ss" + "'";
  var res = FireFly.getListData("GW_FILE",data)._DATA_;
  //render table
  jQuery.each(res,function(i, n) {
    var tr = jQuery("<tr></tr>");
    jQuery("<td></td>").append("&nbsp;&nbsp;&nbsp;&nbsp;").append(n.DISPLAY_NAME).appendTo(tr);
    jQuery("<td></td>").append("&nbsp;&nbsp;&nbsp;&nbsp;").append(n.S_MTIME).appendTo(tr);
    var btn = jQuery("<a href='#'>删除</a>");
    btn.bind("click",function() {
      var data = {};
      data["_PK_"] = n.GW_FILE_ID;
    });
     jQuery("<td></td>").append("&nbsp;&nbsp;&nbsp;&nbsp;").append(btn).appendTo(tr);
    tr.appendTo(_self.rfTabl);    	
  });
};


/** 附件 **/
rh.gw.file.prototype._initAttachment = function() {
  jQuery("<div><img src='/gw/images/mind.gif'/>附件</div><br>").appendTo(this._pCon);
  this.attTabl = jQuery("<table border=0 cellspacing=0 cellpadding=0 aglign=center valign=top></table>");
  this.attTabl.appendTo(this._pCon);
};

rh.gw.file.prototype._setAttachment = function() {
  var _self =  this;
  this.attTabl.empty();
  //get data 
  var _self = this;
  var data = {};
  data["_NOPAGE_"] = "YES";
  data["_searchWhere"] = " and GW_ID='" + "ss" + "'";
  var res = FireFly.getListData("GW_FILE",data)._DATA_;
  //render table
  jQuery.each(res,function(i, n) {
    var tr = jQuery("<tr></tr>");
    jQuery("<td></td>").append("&nbsp;&nbsp;&nbsp;&nbsp;").append(n.DISPLAY_NAME).appendTo(tr);
    jQuery("<td></td>").append("&nbsp;&nbsp;&nbsp;&nbsp;").append(n.S_MTIME).appendTo(tr);
    var btn = jQuery("<a href='#'>删除</a>");
    btn.bind("click",function() {
      var data = {};
      data["_PK_"] = n.GW_FILE_ID;
    });
     jQuery("<td></td>").append("&nbsp;&nbsp;&nbsp;&nbsp;").append(btn).appendTo(tr);
    tr.appendTo(_self.attTabl);    	
  });
};


rh.gw.file.prototype.refresh = function() {
};


















