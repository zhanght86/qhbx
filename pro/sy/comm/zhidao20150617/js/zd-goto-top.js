/**
 * 找到id为zd-goto-top的div,追加返回顶部按钮和事件
 */
jQuery(document).ready(function(){
	jQuery("#zd-goto-top").addClass("zd-goto-top");
	$(window).scroll(function(){
		var offsetToTop=$(this).scrollTop();
		if(offsetToTop>100){
			$('#zd-goto-top').fadeIn(500);
		}else{
			$('#zd-goto-top').fadeOut(500);
		}
	});
	$('#zd-goto-top').click(function(event){
		$('html,body').animate({scrollTop:0},500);
	});
	
	//给按钮绑定hover事件
	jQuery("#zd-goto-top").live("mouseover",function(){
		jQuery(this).addClass("zd-goto-top-hover");
	});
	jQuery("#zd-goto-top").live("mouseout",function(){
		jQuery(this).removeClass("zd-goto-top-hover");
	});
});