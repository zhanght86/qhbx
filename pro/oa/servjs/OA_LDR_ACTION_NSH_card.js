var _viewer = this;

/**
 * 开始时间    结束时间   可选择小时和分钟  start
 */

//var start = jQuery("#OA_LDR_ACTION_NSH-BEGIN_TIME");
jQuery("#OA_LDR_ACTION_NSH-BEGIN_TIME_Z").css("width","200px")
.after("<select id='OA_LDR_ACTION_NSH-BEGIN_TIME-HH' name='OA_LDR_ACTION_NSH-BEGIN_TIME-HH' class='ui-select-default' style='width:50px' ><option value='' selected=''></option>" +
		"<option value='00' selected ='selected'>00</option><option value='01'>01</option>" +
		"<option value='02'>02</option><option value='03'>03</option>" +
		"<option value='04'>04</option><option value='05'>05</option>" +
		"<option value='06'>06</option><option value='07'>07</option>" +
		"<option value='08'>08</option><option value='09'>09</option>" +
		"<option value='10'>10</option><option value='11'>11</option>" +
		"<option value='12'>12</option><option value='13'>13</option>" +
		"<option value='14'>14</option><option value='15'>15</option>" +
		"<option value='16'>16</option><option value='17'>17</option>" +
		"<option value='18'>18</option><option value='19'>19</option>" +
		"<option value='20'>20</option><option value='21'>21</option>" +
		"<option value='22'>22</option><option value='23'>23</option></select>：" +
		"<select id='OA_LDR_ACTION_NSH-BEGIN_TIME-MM' name='OA_LDR_ACTION_NSH-BEGIN_TIME-MM' class='ui-select-default' style='width:50px'><option value='' selected=''></option>" +
		"<option value='00' selected ='selected'>00</option><option value='30'>30</option></select>");


jQuery("#OA_LDR_ACTION_NSH-END_TIME_Z").css("width","200px")
.after("<select id='OA_LDR_ACTION_NSH-END_TIME-HH' name='OA_LDR_ACTION_NSH-END_TIME-HH' class='ui-select-default' style='width:50px' ><option value='' selected=''></option>" +
		"<option value='00' selected ='selected'>00</option><option value='01'>01</option>" +
		"<option value='02'>02</option><option value='03'>03</option>" +
		"<option value='04'>04</option><option value='05'>05</option>" +
		"<option value='06'>06</option><option value='07'>07</option>" +
		"<option value='08'>08</option><option value='09'>09</option>" +
		"<option value='10'>10</option><option value='11'>11</option>" +
		"<option value='12'>12</option><option value='13'>13</option>" +
		"<option value='14'>14</option><option value='15'>15</option>" +
		"<option value='16'>16</option><option value='17'>17</option>" +
		"<option value='18'>18</option><option value='19'>19</option>" +
		"<option value='20'>20</option><option value='21'>21</option>" +
		"<option value='22'>22</option><option value='23'>23</option></select>：" +
		"<select id='OA_LDR_ACTION_NSH-END_TIME-MM' name='OA_LDR_ACTION_NSH-END_TIME-MM' class='ui-select-default' style='width:50px'><option value='' selected=''></option>" +
		"<option value='00' selected ='selected'>00</option><option value='30'>30</option></select>");
/**
 * 开始时间    结束时间   可选择小时和分钟  end
 */
var start1 = _viewer.getItem("BEGIN_TIME").getValue();
var end1 = _viewer.getItem("END_TIME").getValue();
if(start1!=""){
	var s1=start1.substring(0,10);
	jQuery("#OA_LDR_ACTION_NSH-BEGIN_TIME_Z").val(s1);
	var s2=start1.substring(11,13);
	jQuery("#OA_LDR_ACTION_NSH-BEGIN_TIME-HH").val(s2);
	var s3=start1.substring(14,16);
	jQuery("#OA_LDR_ACTION_NSH-BEGIN_TIME-MM").val(s3);
}

if(end1!=""){
	var e1=end1.substring(0,10);
	jQuery("#OA_LDR_ACTION_NSH-END_TIME_Z").val(e1);
	var e2=end1.substring(11,13);
	jQuery("#OA_LDR_ACTION_NSH-END_TIME-HH").val(e2);
	var e3=end1.substring(14,16);
	jQuery("#OA_LDR_ACTION_NSH-END_TIME-MM").val(e3);
}




/**
 * 保存之前
 */
_viewer.beforeSave = function() {
	var sDate = jQuery("#OA_LDR_ACTION_NSH-BEGIN_TIME_Z").val();
	var sHour = jQuery("#OA_LDR_ACTION_NSH-BEGIN_TIME-HH").val();
	var sMin = jQuery("#OA_LDR_ACTION_NSH-BEGIN_TIME-MM").val();
	var eDate = jQuery("#OA_LDR_ACTION_NSH-END_TIME_Z").val();
	var eHour = jQuery("#OA_LDR_ACTION_NSH-END_TIME-HH").val();
	var eMin = jQuery("#OA_LDR_ACTION_NSH-END_TIME-MM").val();
	
	//开始时间、结束时间不能为空
	if(sDate==""||sHour==""||sMin==""||eDate==""||eHour==""||eMin==""){
		alert("活动开始时间、结束时间不符合规则！");
		return false;
	}
	
	//保存开始时间
	var beginTime = sDate+" "+sHour+":"+sMin;
	_viewer.getItem("BEGIN_TIME").setValue(beginTime);
	
	//保存结束时间
	var endTime = eDate+" "+eHour+":"+eMin;
	_viewer.getItem("END_TIME").setValue(endTime);
	
	//结束时间不能小于开始时间
	if(_viewer.getItem("END_TIME").getValue() <= _viewer.getItem("BEGIN_TIME").getValue()){
		alert("活动结束时间需大于开始时间");
		return false;
	}
	
	//活动时间不能冲突
	var leaderCode = _viewer.getItem("LDR_ID").getValue();
	if(leaderCode!=""){
		var leaderCodes = new Array();
		leaderCodes = leaderCode.split(",");
		for(var i=0;i<leaderCodes.length;i++){
			var serchWhere = " and instr(','||ldr_id||',',',"+leaderCodes[i]+",')>0";
			var result = FireFly.doAct(_viewer.servId,"query",{"_WHERE_":serchWhere,"_SELECT_":"LDR_ID,BEGIN_TIME,END_TIME,ACT_ID"});
			var data = result._DATA_;
			if(data.length>0){
				for(var i=0;i<data.length;i++){
					var idTrue = data[i].ACT_ID==_viewer.getItem("ACT_ID").getValue();
					if(data[i].BEGIN_TIME>beginTime && data[i].BEGIN_TIME<endTime){
						if(!idTrue){
							alert("活动时间冲突！"+$.toJSON(data[i]));
							//Tip.showError("活动时间冲突！",true);
							return false;
						}
					}else if(data[i].END_TIME>beginTime && data[i].END_TIME<endTime){
						if(!idTrue){
							alert("活动时间冲突！"+$.toJSON(data[i]));
							//_self.cardBarTipError("校验未通过！");
							return  false;
						}
					}
				}
			}
		}
	}
};

/**
 * 发布
 */
_viewer.getBtn('publish-card').unbind("click").bind("click",function() {
	if(!_viewer.form.validate()) {
		_viewer.cardBarTipError("校验未通过");
    	return false;
    }
	if(_viewer.getPKCode()){
		var chkState = _viewer.itemValue("CHK_STATE");
		var actState = _viewer.itemValue("ACT_STATE");
		if(chkState == 20 && actState == 2){ //不审核，且未发布的信息可以启用
			FireFly.doAct(_viewer.servId, "publish", {"ids":_viewer.getPKCode()}, true);
			_viewer.refresh();
		}else{
			alert("状态为‘未发布’的记录才可以直接发布");
			return false;
		}
	}else{
		alert("请先保存");
		return false;
	}
});

