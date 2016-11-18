<link rel="stylesheet" type="text/css" href="/sc/comm/addressbook/css/addressbook.css">
<script type="text/javascript" src="/sc/comm/contacts/js/browinfo.js"></script>
<script type="text/javascript" src="/sc/comm/contacts/js/rtxint-2.js"></script>
<script type="text/javascript" src="/sc/comm/addressbook/js/addressbook.js"></script>
<div class="txl">
	<div class="txl-conHeader">
		<div class="txl-conHeader-left"></div>
		<div class="txl-conHeader-right">
			<div class="txl-conHeader-line"></div>
			<div class="txl-conHeader-button">
				<span onclick="openTxl()">导出通讯录</span>
			</div>
			<div class="txl-conHeader-search">
				<input type="text" id="txl-search"></input>
			</div>
		</div>
	</div>
	<div>
		<div class="txl-content-left">
			<div id = "ORG_TREE"></div>
		</div>
		<div class="txl-content-right">
			<div class="txl-zmsearch-cont">
				<div class="txl-zmsearch">
					<div class="txl-zmsearch-back-left"></div>
					<div class="txl-zmsearch-back-center"></div>
					<div class="txl-zmsearch-back-right"></div>
					<div class="txl-zm-lable">
						<div id='zmAll' class="txl-zmsearch-all txl-zmsearch-all-selected"><span style='margin-left:5px' onclick="zmSearchClear()">全部</span></div>
						<div class="txl-zm-div"><span onclick="zmSearch(this)">A</span></div>
						<div class="txl-zm-div"><span onclick="zmSearch(this)">B</span></div>
						<div class="txl-zm-div"><span onclick="zmSearch(this)">C</span></div>
						<div class="txl-zm-div"><span onclick="zmSearch(this)">D</span></div>
						<div class="txl-zm-div"><span onclick="zmSearch(this)">E</span></div>
						<div class="txl-zm-div"><span onclick="zmSearch(this)">F</span></div>
						<div class="txl-zm-div"><span onclick="zmSearch(this)">G</span></div>
						<div class="txl-zm-div"><span onclick="zmSearch(this)">H</span></div>
						<div class="txl-zm-div"><span onclick="zmSearch(this)">I</span></div>
						<div class="txl-zm-div"><span onclick="zmSearch(this)">J</span></div>
						<div class="txl-zm-div"><span onclick="zmSearch(this)">K</span></div>
						<div class="txl-zm-div"><span onclick="zmSearch(this)">L</span></div>
						<div class="txl-zm-div"><span onclick="zmSearch(this)">M</span></div>
						<div class="txl-zm-div"><span onclick="zmSearch(this)">N</span></div>
						<div class="txl-zm-div"><span onclick="zmSearch(this)">O</span></div>
						<div class="txl-zm-div"><span onclick="zmSearch(this)">P</span></div>
						<div class="txl-zm-div"><span onclick="zmSearch(this)">Q</span></div>
						<div class="txl-zm-div"><span onclick="zmSearch(this)">R</span></div>
						<div class="txl-zm-div"><span onclick="zmSearch(this)">S</span></div>
						<div class="txl-zm-div"><span onclick="zmSearch(this)">T</span></div>
						<div class="txl-zm-div"><span onclick="zmSearch(this)">U</span></div>
						<div class="txl-zm-div"><span onclick="zmSearch(this)">V</span></div>
						<div class="txl-zm-div"><span onclick="zmSearch(this)">W</span></div>
						<div class="txl-zm-div"><span onclick="zmSearch(this)">X</span></div>
						<div class="txl-zm-div"><span onclick="zmSearch(this)">Y</span></div>
						<div class="txl-zm-div"><span onclick="zmSearch(this)">Z</span></div>
					</div>
				</div>
			</div>
			<div class="txl-users">
			  <div id="leftOrg" style="display:none" value="3rin6giCR9vUIv6kHIO3ex^0001B210000000000BU3^"></div>
			  <div id="preZm" style="display:none" value=""></div>
			  <div id='pages' style="display:none" value='${_PAGE_.PAGES}'></div>
			  <div id='dataCount' style="display:none" value='${_DATA_?size}'></div>
			  <div id='currentPage' style="display:none" value='${_PAGE_.NOWPAGE}'></div>
			  <div id = "txl-users" style="width:100%;">
			  <#list _DATA_ as user>
				  	<div id='${user.USER_CODE}-div' class="txl-user-div" >
						<div class="txl-user-container">
							<div  id="${user.USER_CODE}" onmouseenter="showUserInfos('${user.USER_CODE}','${user.ODEPT_CODE}')">
								<img id='${"photo_"+user.USER_IMG_SRC}'  class="photo" style='width:auto;height:50px;max-width:30px;' src= ""/>
							</div>
							<div class="txt-user-name">${user.USER_NAME}</div>
							<div class="txt-user-post">${user.USER_POST}</div>
						</div>
				 	</div>
				</#list>
				</div>
			</div>
		</div>
	</div>
</div>
