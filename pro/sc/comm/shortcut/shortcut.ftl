<link rel="stylesheet" type="text/css" href="/sc/comm/shortcut/css/shortcut.css"/>
<script type="text/javascript" src="/sc/comm/shortcut/js/shortcut.js"></script>
<script type="text/javascript" src="/sc/js/jquery.carouFredSel-6.2.1-packed.js"></script>



<div class="portal-box">
	<div class='portal-box-title'>
    	<span class="portal-box-title-pre"></span>
        <span class="portal-box-title-label">快捷菜单</span>
		<span class='portal-box-title-fix'></span>
	</div>
	<div class="shortcut-wrapper" style="height:${height}" data-size="${visible!6}">
		<#--
			为了在data-size = 3  和  data-size = 6 的情况下 复用此flt，设置相应高度 和marginTop
		-->
		<#if visible == "6">
			<#assign 
				tempHeight=186
				tempMarginTop = -93
			>
		<#else>
			<#assign 
				tempHeight= 93
				tempMarginTop = -46.5
			>
		</#if>
		<div class="shortcut-carousel" style="width:244px;height:${tempHeight}px;margin-top:${tempMarginTop}px">
			<div class="shortcut-foo"></div>
			<div class="clearfix"></div>
			<a class="prev" id="shortcut-prev" href="#"></a>
			<a class="next" id="shortcut-next" href="#"></a>
		</div>
		<div class="shortcut-dialog">
			<div class="dialog-front" draggable="true">
				<span class="tiparrow"></span>
			    <div class="dialog-header">
			        <span class="dialog-title">常用菜单</span>
			        <a href="javascript:void(0);" class="js-dialog-setting setting" data-action="setup">
			        	<i></i><span>设置</span>
		       		</a>
				</div>
			    <div class="menu-left">
			        <div id="submenu-in-use" class="menu-panel clearfix"></div>
			    </div>
		    </div>
		    <div class="dialog-back">
		   		<div class="dialog-header">
		        	<i></i>
		            <span class="dialog-title">系统菜单库</span>
		            <span class="nomal-tip">单击图标增加或删除常用菜单</span>
				</div>
		       	<div class="menu-right">
		         <div id="submenu-no-use" class="menu-panel clearfix"></div> 
		        </div>
		     </div>
		</div>
		
	</div>
	
</div>