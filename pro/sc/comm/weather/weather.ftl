<div class='portal-box'>
	<div class='portal-box-title' style='position:relative;'>
    	<span class='portal-box-title-pre'></span>
        <span class='portal-box-title-label'>天气预报</span>
		<span class='portal-box-title-fix'></span>
    </div>
    <div id="weatherContainer" style='height:${height};text-align: center;'></div>
</div>
 <script>
 	var disabled = "${disabled}";
    window.loadGadget = function () {
	  		var codeUrl = 'http://ext.weather.com.cn/capitalwater.cn/44148.js';
	  		var containerID = 'weatherContainer';
	  		var s = document.createElement('script');
	  		s.type = 'text/javascript';
	  		s.src = codeUrl + '?target=' + containerID;
	  		if (!disabled) {
		  		document.body.appendChild(s);
	  		}

			var s2 = document.createElement('script');
	  		s2.type = 'text/javascript';
	  		s2.id = 'g44148';
	  		if (!disabled) {
	  			document.body.appendChild(s2);
	  		}
	  };
	  window.onload = window.onload ? function () {
		window.onload();
		window.loadGadget();
	  } : window.loadGadget;
</script>