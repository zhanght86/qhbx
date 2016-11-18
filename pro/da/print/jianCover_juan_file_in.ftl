	<#if DATALIST?size == 0>
		<div style="text-align:center;font-size:20px;font-family:仿宋_gb2312;font-weight:600;">没有数据！</div>
	</#if>
	<#if DATALIST?size != 0>
        <#list DATALIST as DATA>
<div style="height:35px;"></div>
<div style="border:2px solid black;width:600px;padding:2px;float:center;margin-left: auto;margin-right: auto;" >
<table cellspacing="0" cellpadding="0" style="border:1px solid black;border-collapse:collapse;width:600px;">
			<tbody>

				<tr>
					<td style="padding-top:100px;padding-left:380px;">
						<table>
							<tr>
								<td style="border:1px solid black;border-collapse:collapse;width:100px;height:20px;font-size:12px;font-family:仿宋_gb2312;text-align:center;">&nbsp;</td>
								<td style="border:1px solid black;border-collapse:collapse;width:40px;height:20px;font-size:12px;font-family:仿宋_gb2312;text-align:center;">&nbsp;${DATA.DA_YEAR?c}&nbsp;</td>
								<td style="border:1px solid black;border-collapse:collapse;width:50px;height:20px;font-size:12px;font-family:仿宋_gb2312;text-align:center;">&nbsp;${DATA.DA_NUM?c}&nbsp;</td>
							</tr>
							<tr>
								<td style="border:1px solid black;border-collapse:collapse;width:100px;height:20px;font-size:12px;font-family:仿宋_gb2312;text-align:center;">&nbsp;${DATA.FONDS_NAME}&nbsp;</td>
								<td style="border:1px solid black;border-collapse:collapse;width:40px;height:20px;font-size:12px;font-family:仿宋_gb2312;text-align:center;">&nbsp;${DATA.DA_TERM}&nbsp;</td>
								<td style="border:1px solid black;border-collapse:collapse;width:50px;height:20px;font-size:12px;font-family:仿宋_gb2312;text-align:center;">&nbsp;</td>
							</tr>
						</table>
					</td>
				</tr>
				<tr>
					<td style="font-size:16px;font-family:仿宋_gb2312;font-weight:600;text-align:center;padding-top:100px;height:299px;vertical-align: top;">${DATA.TITLE}
					</td>
				</tr>
				<tr>
					<td style="text-align:center;padding-bottom:60px;font-size:12px;font-family:仿宋_gb2312;">
					                <#if DATA.GW_CODE??>
${DATA.GW_CODE}
</#if>
					</td>
				</tr>
				<tr>
					<td style="text-align:center;padding-bottom:60px;font-size:12px;font-family:仿宋_gb2312;">
					${DATA.DUTY}
					</td>
				</tr>
				<tr>
					<td style="text-align:center;padding-bottom:240px;font-size:12px;font-family:仿宋_gb2312;">
						${DATA.GD_DATE}
					</td>
				</tr>
			</tbody>
</table>
</div>
<div style='page-break-after: always;'></div>
  </#list>
		</#if>
