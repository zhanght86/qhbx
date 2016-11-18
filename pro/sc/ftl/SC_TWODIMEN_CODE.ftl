

<style type="text/css">
.mb-link {text-align:center;}

</style>


<div class="mb-link">
	<img id="mb-link-img" src='' width='150px' height='150px'></img>
	<div class="mb-link-title">扫描二维码进入手机登录</div>
</div>


<script type="text/javascript">
	$(function() {
	
		 var imgLink = FireFly.getHttpHost() + FireFly.contextPath + "/file?act=qrCode&value=" + FireFly.getHttpHost() + FireFly.contextPath + "&size=" +${size};
		jQuery("#mb-link-img").attr("src",imgLink);
	});

</script>