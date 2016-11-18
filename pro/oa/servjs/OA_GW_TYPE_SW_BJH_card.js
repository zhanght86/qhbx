
//如果来文类别为剪报类型 则显示剪报类别
var  SW_TYPE=document.getElementById("OA_GW_TYPE_SW_BJH-GW_SW_TYPE").value;
var jianBao = "简报";
if(SW_TYPE==jianBao){
	jQuery("#LW_JB_TYPE").css({"display":""});
}
