<%@ page language="java" contentType="text/html;charset=GBK"%>
<script type="text/javascript" src="<%=request.getContextPath()%>/bn/cms/global/js/jquery-1.3.2.min.js"></script>
<div class="picAll">
	<div class="picLeft">
		<img id="picDet" src="pic1.jpg"/>
	</div>
	<div class="picRight">
		<a class="picSmlA" id="pic1" href="#"><img class="picImg" src="spic1.jpg"/></a>
		<a class="picSmlA" id="pic2" href="#"><img class="picImg" src="spic2.jpg"/></a>
		<a class="picSmlA" id="pic3" href="#"><img class="picImg" src="spic3.jpg"/></a>
		<a class="picSmlA" id="pic4" href="#"><img class="picImg" src="spic4.jpg"/></a>
		<a class="picSmlA" id="pic5" href="#"><img class="picImg" src="spic5.jpg"/></a>
	</div>
</div>

<script type="text/javascript">
var onMouseOutOpacity = 0.5;
var slidint;
var selectedId="pic1";

$('.picRight a').css('opacity', onMouseOutOpacity)
.hover(
	//li获得焦点动作
	function () {
		selectedId=$(this).attr("id");
		initPic();
		clearTimeout(slidint);
	}, 
	//li失去焦点动作
	function () {
		slidint = setTimeout(playNext,3000);
	}
);
function initPic(){
	$("#pic1").removeClass("picSmlASelected");
	$("#pic2").removeClass("picSmlASelected");
	$("#pic3").removeClass("picSmlASelected");
	$("#pic4").removeClass("picSmlASelected");
	$("#pic5").removeClass("picSmlASelected");
	$("#pic1").addClass("picSmlA");
	$("#pic2").addClass("picSmlA");
	$("#pic3").addClass("picSmlA");
	$("#pic4").addClass("picSmlA");
	$("#pic5").addClass("picSmlA");
	$("#pic1").fadeTo('fast', onMouseOutOpacity);
	$("#pic2").fadeTo('fast', onMouseOutOpacity);
	$("#pic3").fadeTo('fast', onMouseOutOpacity);
	$("#pic4").fadeTo('fast', onMouseOutOpacity);
	$("#pic5").fadeTo('fast', onMouseOutOpacity);
	$("#"+selectedId).fadeTo('fast', 1.0);
	$("#"+selectedId).removeClass("picSmlA");
	$("#"+selectedId).addClass("picSmlASelected");
	
	var str = selectedId+".jpg";
	$("#picDet").fadeTo(0, 0);
	$("#picDet").attr("src", str);
	$("#picDet").fadeTo(500, 1);
}

function playNext(){
	if(selectedId=="pic1"){
		selectedId="pic2";
	}else if(selectedId=="pic2"){
		selectedId="pic3";
	}else if(selectedId=="pic3"){
		selectedId="pic4";
	}else if(selectedId=="pic4"){
		selectedId="pic5";
	}else if(selectedId=="pic5"){
		selectedId="pic1";
	}
	initPic();
	slidint = setTimeout(playNext,3000);
}

$("#pic1").removeClass("picSmlA");
$("#pic1").addClass("picSmlASelected");
$("#pic1").fadeTo('fast', 1.0);

slidint = setTimeout(playNext,3000);
</script>