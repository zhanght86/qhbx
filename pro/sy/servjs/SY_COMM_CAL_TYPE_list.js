var _viewer = this;
jQuery("td[icode='TYPE_COLOR']",_viewer.grid.getTable()).each(function(n){
	jQuery(this).css({"background-color":"#" + jQuery(this).text(), color:"#" + jQuery(this).text()});
});