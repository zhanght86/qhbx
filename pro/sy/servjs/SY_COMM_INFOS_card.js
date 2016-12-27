var _viewer = this;
//保存之前获取对应栏目的权限
//动态替换掉外部的extwhere
//增加为null判断，防止tab.open()打开报错
var parHandler = _viewer.getParHandler();
var extwhere = "";
if(parHandler){
  extwhere = parHandler.slimParams()["@com.rh.core.comm.news.InfosChnlDict__EXTWHERE"];
} 
if(extwhere){
 _viewer.getItem("CHNL_ID")._getConfigVal = function() {
	var config ='@com.rh.core.comm.news.InfosChnlDict,{"params":{"REP_DICT_ID":"SY_COMM_INFOS_CHNL_POST","_extWhere":"'+extwhere+'"}}';
	return config;
};
}
if(extwhere){
_viewer.getItem("COPY_TO_NAME")._getConfigVal = function() {
	var config ='@com.rh.core.comm.news.InfosChnlDict,{"params":{"REP_DICT_ID":"SY_COMM_INFOS_CHNL_POST","_extWhere":"'+extwhere+'"}}';
	return config;
};
}
_viewer.beforeSave= function (){
	var news_check = _viewer.getItem("NEWS_CHECK").getValue();
	if( news_check != 3){
	var servid=_viewer.servId;
	var chnlid=_viewer.getItem("CHNL_ID").getValue();
	if(chnlid !=""){
	var data = {};
	data["CHNL_ID"]=chnlid;
	var outbean = FireFly.doAct(servid,"queryChnl",data);
    var chnl_check=outbean['CHNL_CHECK'];
    var checker = outbean['CHNL_CHECKER']; //审核人
   _viewer.getItem("NEWS_CHECK").setValue(chnl_check);
   _viewer.getItem("NEWS_CHECKER").setValue(checker);
	}
  }
}
_viewer.afterSave= function (){
//点击发布 同时发布到某个栏目下
	//获取同时发布大的栏目
	var serid = _viewer.servId;
	var copychnlname = _viewer.getItem("COPY_TO_NAME").getValue();
	var pkdata =_viewer.getPKCode();
	var oldData =FireFly.byId(serid,pkdata);
    var oldChnl =oldData["CHNL_ID"];
	var tupians={};
	 tupians["DATA_ID"]=pkdata;
	 tupians["FILE_CAT"]="TUPIANJI";
	var tupianData = FireFly.doAct("SY_COMM_FILE","finds",tupians);
	var tupianBean=tupianData["_DATA_"];
	
	var fujians={};
	fujians["DATA_ID"]=pkdata;
	fujians["FILE_CAT"]="FUJIAN";
	var fujianData = FireFly.doAct("SY_COMM_FILE","finds",fujians);
	var fujianBean=fujianData["_DATA_"];
	
	if(copychnlname !=""){
	 var targets = copychnlname.split(",");
     for(var i=0;i<targets.length;i++){
      if(targets[i] != oldChnl){
     
      var chnl ={};
      chnl["CHNL_ID"]=targets[i];
      var outbeans = FireFly.doAct(serid,"queryChnl",chnl);
      var check=outbeans['CHNL_CHECK'];
      var checker = outbeans['CHNL_CHECKER'];
      oldData["_PK_"]="";
      oldData["CHNL_ID"]=targets[i];
      oldData["NEWS_ID"]="";
      oldData["NEWS_CHECK"]=check;
      oldData["NEWS_CHECKER"]=checker;
      oldData["COPY_TO_NAME"] ="";
	  // 同步到其他栏目标识
	  oldData["IS_SYNC"] = "true";
      var copydata= FireFly.doAct(serid,"save",oldData);
     
      //同时复制图片集
      if(tupianBean.length>0){
       for (var j=0;j<tupianBean.length;j++){
       tupianBean[j]["_PK_"]="";
       tupianBean[j]["FILE_ID"]="";
       tupianBean[j]["DATA_ID"]=copydata["_PK_"];
       FireFly.doAct("SY_COMM_FILE","save",tupianBean[j]);
       }
      }
      //同时复制附件信息
      if(fujianBean.length>0){
       for(var n=0;n<fujianBean.length;n++){
        fujianBean[n]["_PK_"]="";
        fujianBean[n]["FILE_ID"]="";
        fujianBean[n]["DATA_ID"]=copydata["_PK_"];
        FireFly.doAct("SY_COMM_FILE","save",fujianBean[n]);
       } 	
      }
      //同时保存两信息的关联关系
      var links={};
      links["NN_MAIN_ID"] =pkdata;
      links["NN_COPY_ID"] =copydata["_PK_"];
      FireFly.doAct("SY_COMM_INFOS_LINKS","save",links);
      
      } 
     }
	 
	}
}
_viewer.getBtn("commit").unbind("click").bind("click", function() {
	var data = {};
	var serid = _viewer.servId;
	var chnlid=_viewer.getItem("CHNL_ID").getValue();
	data["CHNL_ID"]=chnlid;
     var outbean = FireFly.doAct(serid,"queryChnl",data);
   var check=outbean['CHNL_CHECK'];
   var checker = outbean['CHNL_CHECKER'];
   _viewer.getItem("NEWS_CHECK").setValue(check);
   _viewer.getItem("NEWS_CHECKER").setValue(checker);
	if (check == '2' || check =='') {
		_viewer.getItem("NEWS_CHECKED").setValue(6);
	} else {
		_viewer.getItem("NEWS_CHECKED").setValue(3);
	}
	_viewer.saveForm();
	//发起一条待办
	 var newstitle = _viewer.getItem("NEWS_SUBJECT").getValue(); // 信息标题
    //var TODO_CODE =  TODO_OBJECT_ID1 TODO_CATALOG TODO_URL
    var newsid =_viewer.getItem("NEWS_ID").getValue();
    var senduser =_viewer.getItem("NEWS_USER").getValue();
   if(check == '1'){
   	var data={};
   	 data["OWNER_CODE"] = checker; //代办用户
   	 data["SEND_USER_CODE"] = senduser;
   	 data["TODO_TITLE"] = newstitle;
   	 data["TODO_CODE"] = serid;
   	 data["TODO_OBJECT_ID1"] = newsid;
   	 data["SERV_ID"] = "SY_COMM_INFOS_DEAL";	 
   	 data["TODO_CATALOG"] = 1;
   	 data["TODO_URL"] ="SY_COMM_INFOS_DEAL.byid.do?data={_PK_:"+newsid+"}";
     FireFly.doAct("SY_COMM_TODO","addToDo",data);
   }
});
_viewer.getBtn("postCard").unbind("click").bind("click", function() {
	var data = {};

	var serid = _viewer.servId;
	if(_viewer.getPKCode()==""){
		alert("请先保存后，再发布。");
		return;
	}
	var chnlid=_viewer.getItem("CHNL_ID").getValue();
	data["CHNL_ID"]=chnlid;
     var outbean = FireFly.doAct(serid,"queryChnl",data);
   var check=outbean['CHNL_CHECK'];
   var checker = outbean['CHNL_CHECKER'];
   _viewer.getItem("NEWS_CHECK").setValue(check);
   _viewer.getItem("NEWS_CHECKER").setValue(checker);
   var nowDate = rhDate.getTime();
   _viewer.getItem("NEWS_TIME").setValue(nowDate);
	if (check == '2' || check =='') {
		_viewer.getItem("NEWS_CHECKED").setValue(6);
	} else {
		_viewer.getItem("NEWS_CHECKED").setValue(3);
	}
	
	_viewer.saveForm();
	
});