var _viewer = this;

/**
 * *如果是查询选择***
 * 该查询选择用于baiduwenku_create_doclist.ftl
 * 进入查询选择框，默认显示defaultChnl的数据
 * 采用触发该节点的click事件实现
 * 如果是三级节点，则通过css展开,并改变父节点图标展开闭合样式
 * HardCoding
 * */
if(_viewer.selectViewFlag){
	var defaultChnl=window.chnlName;
	if(defaultChnl){
		//仅在第一次进入时使用window.chnlName
		//故此处需设置为undefined,因为每次click事件后
		//都会刷新页面，会陷入死循环
		window.chnlName= undefined;
		jQuery("div[title="+defaultChnl+"]").trigger("click");
		//被选中的节点
		var selectedNode=jQuery("div[title="+defaultChnl+"]");
		//如果是叶子节点
		if(selectedNode.hasClass("bbit-tree-node-leaf")){
			if(selectedNode.closest('ul').is(":hidden")){
				var $ul=selectedNode.closest('ul').show("fast");
					if($ul.siblings().length > 0 ){
						var $div=$ul.prev();//父节点dom
						//节点图标由闭合(+)变为展开(-)
						$div.find(".bbit-tree-elbow-end-plus")
							.removeClass(".bbit-tree-elbow-end-plus")
							.addClass("bbit-tree-elbow-end-minus");
																
					}
			}
		}
	}
}
