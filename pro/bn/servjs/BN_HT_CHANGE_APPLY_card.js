
var _viewer = this;
/*
 * 相关合同列表中添加 编辑  --------start
 */

//获取流程节点
if (this.wfCard) {
	//获取流程变量 canEditRelateFile  是否可以编辑相关文件
	var canEditRelateFile = this.wfCard.getCustomVarContent("canEditRelateFile");
	if(canEditRelateFile && "true"==canEditRelateFile){
		//在删除td之前添加编辑的td
		jQuery(".ui-linkSelect-default-td.ui-linkselect-default-delete").before("<td class='ui-linkSelect-default-td ui-linkselect-default-edit' align='right'  style='border:0px !important;min-width:30px !important;'><a href='javascript:void(0);'>编辑</a></td>");
		//获取编辑按钮
		var editTd = jQuery("div[class='fl wp']").find("td[class='ui-linkSelect-default-td ui-linkselect-default-edit']");
		//编辑按钮绑定事件
		editTd.bind("click",function(){
			//获取当前td的父tr 再在当前tr中找到相应的 dataID和dataTitle
			//为了避免有多个相关记录时只找到第一个tr中的数据
			var trObj = jQuery(this).parent();
			var dataID = trObj.find("td[class='dataPk']").text();
			var dataTitle = trObj.children("td[class='dataTitle']").text();
			var opts = {"tTitle":dataTitle,"url":"LW_CT_CONTRACT_EIDT"+".card.do?pkCode="+dataID+"&readOnly=false","menuFlag":3};
			Tab.open(opts);
		});
	}
}
/*
 * 相关合同列表中添加 编辑  --------end
 */
/*
 * 除起草节点外不允许上传相关文件  -------start
 */
//获取当前流程节点
var nodeCodeBean = FireFly.doAct(_viewer.servId,"getNodeCode",{"S_WF_INST":_viewer.getItem("S_WF_INST").getValue(),"S_WF_NODE":_viewer.getItem("S_WF_NODE").getValue()});
//判断是都是起草节点
if(nodeCodeBean.NODE_CODE!="2"){
	//_viewer.getItem("GW_CONTACT_PHONE").obj.attr("readonly","true");
	//获取上传按钮
	var uplodaRalate = jQuery("div[class='fl wp']").find("a[class='ui-linkSelect-default rh-icon']");
	uplodaRalate.hide();  
}



/*
 * 除起草节点外不允许上传相关文件  -------end
 */


