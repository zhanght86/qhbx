var _viewer = this;


//将未编正式文号的文号暂时清空
jQuery(_viewer.grid.getTable()).find("td[icode='GW_CODE']").each(
	function() {
		var code = $(this).html();
		if(code.length<=8){
			$(this).html(" ");
		}
	}
);