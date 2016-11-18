// JavaScript Document
$(document).ready(function(e) {
    //文档贡献---文辑贡献   切换
	$(".tab-item").on({
		mouseover:function(){
			var left=0;
			if($(this).index()==2){
				left=97;
			}
			$(this).siblings(".hover").css("left",left);
		},
		mouseleave:function(){
			if(!$(this).hasClass("current")){
				var left=97;
				if($(this).index()==2){
					left=0;
				}
				$(this).siblings(".hover").css("left",left);
			}
		},
		click:function(event){
			event.preventDefault();
			$(this).siblings(".current").removeClass("current");
			$(this).addClass("current");
			var left=0,
				id=$(this).find("a").attr("href"),
				hid=$(this).siblings(".tab-item").find("a").attr("href");
			$(hid).hide();
			$(id).show();
			if($(this).index()==2){
				left=97;
			}
			$(this).siblings(".slide").css("left",left);
		}
	});
	
	var type=window.location.href.split("&t=")[1];
	if(type=="1"){
		var t=$(".tab-item").eq(1).trigger("mouseover").trigger("click");;
	} 
	//调整图片大小，如果超过最大宽度，等比例缩放
	function adjustImgSize($img,maxWidth,minWidth){
		//原始图像大小
		var width,
			height,
			originWidth=$img.width(),
			originHeight=$img.height();
		if(originWidth > maxWidth){ 
			//如果原始图像大小  大于 maxWidth
		    width=maxWidth;
		    height=( originHeight * maxWidth ) / originWidth;
		  
		} 
		if(originWidth < minWidth){
			width=minWidth;
			height=( originHeight * minWidth ) / originWidth;
		}
		$img.css({"width":width,"height":height});
	}//---- end of adjustImgSize
	$(".wk-right-side").find("img").each(function(){
		   adjustImgSize(jQuery(this),110,64);
	});
});