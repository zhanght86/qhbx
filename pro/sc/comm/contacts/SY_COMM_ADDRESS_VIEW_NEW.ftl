<link rel="stylesheet" type="text/css" href="/sc/comm/contacts/css/contacts.css">
<script type="text/javascript" src="/sc/comm/contacts/js/browinfo.js"></script>
<script type="text/javascript" src="/sc/comm/contacts/js/rtxint-1.js"></script>
<script type="text/javascript" src="/sc/comm/contacts/js/contacts.js"></script>
<div id='txl-out'>
<div id = 'txl' class='txl_bck_img'>
	<div id='USER_TXL' class='USER_TXL_back_img'>
	    <div id='USER_SERCH_TAB'  style='height:100%;width:43%;float:left;margin-left:30px;margin-top:7px'>
	        <div id="USER_SERCH_TAB_CON" style='min-height:100%;'>
	            <ul class='portal-box-title' style = 'margin-top:30px'>
	                <li style='margin-left:30px;border-top-left-radius:5px;border-right:0.5px #9ea0a9 solid'>
	                    <a href="#USER_SERCH_TAB_ORG_TREE">机 构 树</a>
	                </li>
	                <li style='margin-left:-1.5px;'>
	                    
						<a href="#USER_SERCH_TAB_PINYIN">拼音结构 </a>
	                </li>
					<li style='margin-left:-1.5px;border-top-right-radius:5px'>
	                    <a  onclick='openTxl()'>导出通讯录</a>
	                </li>
	            </ul>
	            <div id="USER_SERCH_TAB_PINYIN">
	            	<div id='PY_ZM' style='width:8%;float:left'>
	            		<ul id ='PY_ZM_LI'></ul>
	            	</div>
					<div style='width: 92%;float:left'>
						<div id="user-search" style="height:35px;">
			            	<input type="text" id="SY_ALL_SEARCH_INPUT" class='search-input' onkeyup="getSearchDatas(this.value)"></input>
			            	<div id="SY_ALL_SEARCH_BTN" class="btn-search" href="#" style="width:16px;height:16px;float:left;margin-top: 13px;"></div>
			        	</div>
						<div style='height:425px;width:100%;border:1px #cccccc solid;OVERFLOW: auto;' >
							<div id='user-contener'>
								<ul id = 'PY_ZM_LAB'></ul>
							</div>
						</div>
					</div>
	            </div>
				<#--构建组织机构树-->
	            <div id="USER_SERCH_TAB_ORG_TREE" style='height:450px;width:100%;OVERFLOW: auto;'></div>
				<#--
				<div id="SERCH_TAB_ORG_TREE"></div>-->
	        </div>
	    </div>
	
		<div id='USER_SELF' style='height:480px;width:43%;float:left;margin-left:8%'>
	        <div id='user_all_info' style='width:93%;height:100%;margin-top: 50px;'>
				<div style="height:35%">
					<div id='user_img' style="width:30%;float:left;margin-top:8px;">
						<img id='user_img_view' width='100' height='100' style='border-radius:5px'></img>
					</div>
					<div style="width:70%;float:left">
						<ul>
							<li class='user-all-info-lab-li'><span>姓名:</span></li>
							<li class='user-all-info-lab-li'><span>职务:</span></li>
							<li class='user-all-info-lab-li'><span>单位:</span></li>
							<li class='user-all-info-lab-li'><span>部门:</span></li>
							<li class='user-all-info-lab-li'><span>办公电话:</span></li>
							<li class='user-all-info-lab-li'><span>移动电话:</span></li>
						</ul>
					</div>

					<div style='position: absolute;margin-top: 125px;margin-left: 300px;'>
						<a class="rh-icon rhGrid-btnBar-a" id="send-mobile-apply">
							<span class="rh-icon-inner">申请</span>
							<span class="rh-icon-img btn-right"></span>
						</a>
					</div>
				</div>
				<div style='position:absolute;width:200px;margin-top: -167px;margin-left: 144px;'>
					<ul style='margin-left:3px'>
						<li class='user-all-info-result-li'><span id='user_name' >&nbsp;</span></li>
						<li class='user-all-info-result-li'><span id='user_post' >&nbsp;</span></li>
						<li class='user-all-info-result-li'><span id='user_cmpy' >&nbsp;</span></li>
						<li class='user-all-info-result-li'><span id='user_dept' >&nbsp;</span></li>
						<li class='user-all-info-result-li'><span id='user_offer_tel' style='margin-left:20px'>&nbsp;</span></li>
						<li class='user-all-info-result-li'><span id='user_mobile' style='margin-left:20px'>&nbsp;</span></li>
					</ul>
				</div>
				<div style="height:8%;background:#f6f6f6">
					<ul>
						<!-- <li style="float:left;width:33%;margin-top:5px;margin-left:5px">
							<a class="rh-icon rhGrid-btnBar-a" id="instant-messaging">
								<span class="rh-icon-inner"> 即时通讯 </span>
								<span class="rh-icon-img btn-chat"></span>
							</a>
							<img src="/sc/comm/contacts/images/blank.gif" id="RTX" align="absbottom" width=32 height=32 />
						</li> -->
						<li style="float:left;width:33%;margin-top:5px">
							<a class="rh-icon rhGrid-btnBar-a" id="send-phone-msg">
								<span class="rh-icon-inner">发送短信</span>
								<span class="rh-icon-img btn-phone"></span>
							</a>
						</li>
						<li style="float:left;width:30%;margin-top:5px">
							<a class="rh-icon rhGrid-btnBar-a" id="send-email">
								<span class="rh-icon-inner">发送邮件</span>
								<span class="rh-icon-img btn-msg"></span>
							</a>
						</li>
						<li style="float:left;width:33%;margin-top:5px;margin-left:5px">
							<!--  <a class="rh-icon rhGrid-btnBar-a RTX" id="instant-messaging1">
								<span class="rh-icon-inner"> RTX </span>
								<span class="rh-icon-img btn-chat"></span> 
							</a> -->
							<img src="/sc/comm/contacts/images/blank.gif" class="RTX" align="absbottom" width=20 height=20 />
						</li>
					</ul>
				</div>
				<div style="height:60%;">
					<div style="height:10%;margin-top:4px">
						<div style='float:left;width:82%'>
							<span class='user-all-info-lab-li'>简历:</span>
						</div>
						<div style='float:left;width:17%;'>
							<a class="rh-icon rhGrid-btnBar-a" id="resume-apply">
								<span class="rh-icon-inner">申请</span>
								<span class="rh-icon-img btn-right"></span>
							</a>
						</div>
					</div>
					<div id='self-resume-tabs' style='min-height:245px;max-height:245px;border:1px #cccccc solid;'>
						 <ul class='portal-box-title' style='background:none '>
			                <li style='border-top-left-radius:5px;border-right:o.5px #9ea0a9 solid;'>
			                    <a href="#base-info">基本信息 </a>
			                </li>
			                <li style='margin-left:-1px;border-right:0.5px #9ea0a9 solid'>
			                    <a href="#work-info">工作经历</a>
			                </li>
							<li style='margin-left:-1px;border-right:0.5px #9ea0a9 solid'>
			                    <a href="#education-info">教育经历</a>
			                </li>
							<li style='margin-left:-1px;border-top-right-radius:5px;'>
			                    <a href="#jc-info">奖惩情况</a>
			                </li>
			            </ul>
						<div id='base-info' style='width:100%;height:100%' >
							<div style='height:70%'>
								<ul style='width:17%;float:left'>
									<li class='user-base-info-lab-li' style='text-align:right'>姓名:</li>
									<li class='user-base-info-lab-li' style='text-align:right'>民族:</li>
									<li class='user-base-info-lab-li' style='text-align:right'>籍贯:</li>
									<li class='user-base-info-lab-li' style='text-align:right'>身高:</li>
									<li class='user-base-info-lab-li' style='text-align:right'>学历:</li>
									<li class='user-base-info-lab-li' style='text-align:right'>职称:</li>
								</ul>
								<ul style='width:25%;float:left'>
									<li id='base-info-user-name' class='user-base-info-result-li' style='margin-left:3px'>&nbsp</li>
									<li id='base-info-user-nation' class='user-base-info-result-li' style='margin-left:3px'>&nbsp</li>
									<li id='base-info-user-home-land' class='user-base-info-result-li' style='margin-left:3px'>&nbsp</li>
									<li id='base-info-user-height' class='user-base-info-result-li' style='margin-left:3px'>&nbsp</li>
									<li id='base-info-user-edu-level' class='user-base-info-result-li' style='margin-left:3px'>&nbsp</li>
									<li id='base-info-user-title' class='user-base-info-result-li' style='margin-left:3px'>&nbsp</li>	
								</ul>
								<ul style='width:25%;float:left'>
									<li class='user-base-info-lab-li' style='text-align:right'>出生日期:</li>
									<li class='user-base-info-lab-li' style='text-align:right'>婚姻状况:</li>
									<li class='user-base-info-lab-li' style='text-align:right'>政治面貌:</li>
									<li class='user-base-info-lab-li' style='text-align:right'>参加工作日期:</li>
									<li class='user-base-info-lab-li' style='text-align:right'>入职日期:</li>
									<li class='user-base-info-lab-li' style='text-align:right'>工号:</li>
								</ul>
								<ul style='width:25%;float:left'>
									<li id='base-info-user-birthday'class='user-base-info-result-li' style='margin-left:3px'>&nbsp</li>
									<li id='base-info-user-marriage' class='user-base-info-result-li' style='margin-left:3px'>&nbsp</li>
									<li id='base-info-user-politics' class='user-base-info-result-li' style='margin-left:3px'>&nbsp</li>
									<li id='base-info-user-work-date' class='user-base-info-result-li' style='margin-left:3px'>&nbsp</li>
									<li id='base-info-user-cmpy-date' class='user-base-info-result-li' style='margin-left:3px'>&nbsp</li>
									<li id='base-info-user-work-loc' class='user-base-info-result-li' style='margin-left:3px'>&nbsp</li>
								</ul>
							</div>
							<div style='height:30%'>
									<ul style='width:17%;float:left'>
									<li class='user-base-info-lab-li' >身份证号:</li>
									<li class='user-base-info-lab-li' style='text-align:right'>学校:</li>
									<li class='user-base-info-lab-li' style='text-align:right'>专业:</li>
	
								</ul>
								<ul style='width:75%;float:left'>
									<li id='base-info-user-idcard' class='user-base-info-result-li' style='margin-left:3px'>&nbsp</li>
									<li id='base-info-user-edu-school' class='user-base-info-result-li' style='margin-left:3px'>&nbsp</li>
									<li id='base-info-user-edu-major' class='user-base-info-result-li' style='margin-left:3px'>&nbsp</li>
								</ul>
							</div>
						</div>
						<div id='work-info' ></div>
						<div id='education-info'></div>
						<div id='jc-info' ></div>
					</div>
				</div>
			</div>
	    </div>
	</div>
</div><div class='txl_bck_img_bt'></div>
</div>

