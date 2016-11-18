<#if DATALIST?size == 0>
		<div style="text-align:center;font-size:20px;font-family:仿宋_gb2312;font-weight:600;">没有数据！</div>
</#if>
	<#if DATALIST?size != 0>
        <#list DATALIST as DATA>

<table cellspacing="0" cellpadding="0" align="left" width="200" height="700" style="border-top:2px solid black;border-bottom:2px solid black;border-collapse:collapse;">
    <tbody>
        <tr>
			<td align="center" width="20%" height="65%" style=" border-bottom:1px solid black;border-collapse:collapse;">
				<table height="100%" align="center" style="font-size:30px;font-family:仿宋_gb2312;">
					<tr><td width="40%" height="100%"></td><td width="20%" height="100%" valign="center" align="center"  ><span style="writing-mode:tb-rl;">中华联合保险有限公司</span></td>
					<td width="40%" height="100%"></td>
					</tr>
				</table>
			</td>
		</tr>

		<tr>
			<td align="center" width="20%" height="20%" style=" border-bottom:1px solid black;border-collapse:collapse;">
				<table width="100%" height="100%">
					<tr>
						<td width="50%" height="100%" style="border-right:1px solid black;font-size:16px;font-family:仿宋_gb2312;">
							<table style='width:100%;height:100%;' >
								<tr><td style="font-size:16px;font-family:仿宋_gb2312;">件号</td></tr>
								<tr><td style="font-size:14px;font-family:仿宋_gb2312;">${DATA.DA_NUM?c}</td></tr>
							</table>
							<span style="writing-mode:tb-rl;"></span>
						</td>
						<td width="50%" height="100%" >
							<table width="100%" height="100%">
								<tr><td width="100%" height="100%" valign="top"><div style="padding:30px 0px 0px 0px;font-size:16px;">室</div></td></tr>
								<tr><td width="100%" height="100%" valign="top"><div style="padding:30px 0px 0px 0px;font-size:16px;">馆</div></td></tr>
							</table>
						</td>
					</tr>
					
				</table>
			</td>
		</tr>

		<tr>
			<td align="center" width="20%" height="15%" style=" border-bottom:1px solid black;border-collapse:collapse;">
				<table width="100%">
					<tr><td width="50%" height="50" style="font-size:16px;font-family:仿宋_gb2312;">年度</td></tr>
					<tr><td width="50%" height="50" style="font-size:14px;font-family:仿宋_gb2312;" >${DATA.DA_YEAR?c}</td></tr>
				</table>
			</td>

		</tr>

    </tbody>
</table>
<table align="left"  width="10" height="700" ></table>
<#if  DATA_index % 3==2>
<div style='page-break-after: always;'></div>
</#if>
  </#list>
		</#if>
