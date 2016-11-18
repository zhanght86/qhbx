var _viewer = this;

//获取所有的checkbox
var chkboxs = _viewer.grid.getCheckBox();
//是否是任务管理员
var isZ_RWGL = (System.getVar("@ROLE_CODES@").indexOf("Z_RWGL") >= 0) ;

jQuery(chkboxs).each(function(){
	var chkboxTd = jQuery(this);
	var sPublic = _viewer.grid.getRowItemValueByElement(chkboxTd,"S_PUBLIC");
	if(sPublic == 2){ //如果是非公共数据，表示是自己的数据
		return;
	}
	if(!isZ_RWGL){ //对于非公共数据，如果不是任务管理员则不能查看和修改
		chkboxTd.hide();
		_viewer.grid.getRowByElement(this).unbind("dblclick").bind("dblclick",function(){
			Tip.showAttention("没有权限！",true);
		});
	}
});