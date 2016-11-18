var _viewer = this;

var URL_WHERE = _viewer.params._WHERE_ ;
var CHNL_ID = URL_WHERE.substring( URL_WHERE.indexOf("'")+1 , URL_WHERE.lastIndexOf("'"));


jQuery("div[itemid="+CHNL_ID+"]").parent().parent().children().hide();
jQuery("div[itemid="+CHNL_ID+"]").parent().show();

