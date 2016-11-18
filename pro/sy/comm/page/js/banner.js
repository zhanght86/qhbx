$(function(){
	//banner部分动画效果
	var bno = 0;
	banner_change(0);
	
	$(".gz_banner").mouseover(function(){clearInterval(times);});
	$(".gz_banner").mouseout(function(){times = setInterval(banner_right,5000);});
	
	var times = setInterval(banner_right,5000);
	
	$(".gz_banner_but li").click(function(){
		bno = $(".gz_banner_but li").index($(this));
		banner_change(bno);
	});
	
	function banner_right(){
		bno = bno + 1;
		if(bno > 4){bno = 0}
		banner_change(bno);
	}
	
	function banner_change(bno){
		$(".gz_banner_pic li").eq(bno).fadeIn(500);
		$(".gz_banner_but li").eq(bno).addClass("curr");
		$(".gz_banner_pic li").eq(bno).siblings().css("display", "none");
		$(".gz_banner_but li").eq(bno).siblings().removeClass("curr");
	}
});