<#assign mindTypeList = userMind.getMindTypeList(odeptCode) >
	<#list mindTypeList as mindType>
	<#assign mindList = userMind.getMindListByType(mindType.ID, odeptCode) > 
		<#if mindList?size != 0>
		<tr>
			<td class="mindTypeTitle"><span>${mindType.NAME}</span></td>
		</tr>
		<tr>
			<td>
				<table class='mindShowList' border="0" cellspacing="0" cellpadding="0">
				<#list mindList as mind>
					<tr class="mind-content-tr">
					<td>
						<div class="mind-content-div">
							<#if mind.MIND_CONTENT??>
								<span class="MIND_CONTENT"><p>${mind.MIND_CONTENT?replace("\n","<br>")}</p></span>
								</#if>
								<#if mind._MIND_FILE_LIST?? && mind._MIND_FILE_LIST?size gt 0>
								<#list mind._MIND_FILE_LIST as fileBean>
								<div>${fileBean_index + 1}.&nbsp;<a href="javascript:void(0)" onclick="RHFile.read('${fileBean.FILE_ID}','${fileBean.FILE_NAME}')" fileID="${fileBean.FILE_ID}" target="_blank" class="MIND_FILE"><span class="icon-image iconC" style="border:0"></span>${fileBean.FILE_NAME}</a></div>
								</#list>
							</#if>
						</div>
						<div class="mind-content-div">
							<#if userDoInWf?? && userDoInWf=true>
								<#if canCopy?? && canCopy=true>
								[<a href="javascript:void(0)" class="COPY_MIND">复制</a>]
								</#if>
								<#if NI_ID?? && NI_ID= mind.WF_NI_ID && mind.S_USER = userBean.USER_CODE>
									&nbsp;[<a href="javascript:void(0)" MIND_ID="${mind.MIND_ID}" class="DELETE_MIND">删除</a>]
								<#elseif NI_ID?? && DEL_MIND?? && DEL_MIND = "true" && mind.S_USER = userBean.USER_CODE>
									&nbsp;[<a href="javascript:void(0)" MIND_ID="${mind.MIND_ID}" class="DELETE_MIND">删除</a>]
								<#elseif NI_ID?? && DEL_SELF_MIND?? && DEL_SELF_MIND && mind.S_USER = userBean.USER_CODE>
								    &nbsp;[<a href="javascript:void(0)" MIND_ID="${mind.MIND_ID}" class="DELETE_MIND">删除</a>]		
								</#if>
							</#if>
						</div>
						<div class="mind-content-div">
							<!--<span>${mind.S_TNAME}&nbsp;&nbsp;&nbsp;&nbsp; </span>-->
							<#if mind.IS_BD?? && mind.IS_BD = 1 && mind.BD_UNAME?length gt 0>
						    	<span>${mind.S_UNAME}(${mind.BD_UNAME}补登)</span>
						    <#elseif mind.BD_UNAME?length gt 0>
						    	<span>${mind.BD_UNAME}(${mind.S_UNAME}授权)</span>
						    <#else>
						    	<span>${mind.S_UNAME}<span>
						    </#if>
						    <span class='mindList_time'>
							    <#if mind.MIND_TIME??>
							        <#if mind.MIND_TIME?length gt 16>    
							            ${mind.MIND_TIME?substring(5,16)}
							        <#else>
							            ${mind.MIND_TIME}
							        </#if>
							    </#if>    
						    </span>
						</div>
					 </td>
					</tr>
				</#list>
				</table>
			</td>
		</tr>
		</#if>
	</#list>