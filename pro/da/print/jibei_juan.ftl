<#if DATALIST?size == 0>
		<div style="text-align:center;font-size:20px;font-family:仿宋_gb2312;font-weight:600;">没有数据！</div>
</#if>
<table align="center"><tr>
	<#if DATALIST?size != 0>
        <#list DATALIST as DATA>


<td>




<table cellspacing="0" cellpadding="0" align="left" width="152px" height="750px" style=" border:1px solid black;border-collapse:collapse;">
    <tbody>
        <tr>
			<td align="center" width="20%" height="30%" style=" border-bottom:1px solid black;border-collapse:collapse;">
				<table height="100%" align="center" >
					<tr><td width="20%" height="45%" valign="bottom" align="center" style="font-size:20px;font-family:仿宋_gb2312;" ><span>全宗号</span></td>
					<tr><td style="height:10%;"></td></tr>
					<tr><td width="20%" height="45%" valign="top" align="center"  style="font-size:20px;font-family:仿宋_gb2312;"><span >${DATA.FONDS_NAME}</span></td>
					</tr>
				</table>
			</td>
		</tr>

		<tr>
			<td align="center" width="20%" height="25%" style=" border-bottom:1px solid black;border-collapse:collapse;">
				<table height="100%" align="center" >
					<tr><td width="20%" height="45%" valign="bottom" align="center" style="font-size:20px;font-family:仿宋_gb2312;" ><span >年度</span></td>
					<tr><td style="height:10%;"></td></tr>
					<tr><td width="20%" height="45%" valign="top" align="center" style="font-size:20px;font-family:仿宋_gb2312;" ><span >${DATA.DA_YEAR}</span></td>
					</tr>
				</table>
			</td>
		</tr>
		<tr>
			<td align="center" width="20%" height="25%" style=" border-bottom:1px solid black;border-collapse:collapse;">
				<table height="100%" align="center" >
					<tr><td width="20%" height="45%" valign="bottom" align="center"  style="font-size:20px;font-family:仿宋_gb2312;"><span >保管期限</span></td>
					<tr><td style="height:10%;"></td></tr>
					<tr><td width="20%" height="45%" valign="top" align="center" style="font-size:20px;font-family:仿宋_gb2312;" ><span >${DATA.DA_TERM}</span></td>
					</tr>
				</table>
			</td>
		</tr>
		<tr>
			<td align="center" width="20%" height="10%" style="font-size:16px;font-family:仿宋_gb2312; border-bottom:1px solid black;border-collapse:collapse;">
				<table width="100%" height="100%">
					<tr>
						<td style="border-right:1px solid black;width:50%;height:100%;">
							<table style="width:100%;height:100%;">
							<tr>
								<td style="width:45%;height:100%;"></td>
								<td  style="width:10%;height:100%;">起<br><br>止<br><br>件<br><br>号</td>
								<td style="width:45%;height:100%;"></td>
							</tr>
							</table>
						</td>
						<td width="50%" height="100%" >
							<table width="100%" height="100%">
								<tr><td width="100%" height="100%" align="center">共${DATA.COUNT_DA?c}件</td></tr>
								<tr>
									<td width="100%" height="100%" align="center">
										<#if  DATA.MIN_DA_NUM == DATA.MAX_DA_NUM>
										${DATA.MIN_DA_NUM}#
										</#if>
										<#if  DATA.MIN_DA_NUM != DATA.MAX_DA_NUM>
										${DATA.MIN_DA_NUM}#-${DATA.MAX_DA_NUM}#
										</#if>
									<td>
								</tr>							
							</table>
						</td>
					</tr>
					
				</table>
			</td>
		</tr>
		<tr>
			<td align="center" width="20%" height="30%" style="font-size:20px;font-family:仿宋_gb2312; border-bottom:border-collapse:collapse;">
				<table style="width:100%;height:100%" >
					<tr><td width="50%" height="35%"  align="center">盒号${DATA.BOX_NUM?c}</td></tr>
					<tr><td width="50%" height="65%"  valign="top" align="center">共（${DATA.COUNT_BOX?c}）盒</td></tr>
					<#--<tr><td width="50%" height="45%"  valign="top" align="center">共（${DATA.COUNT_BOX?c}）盒</td></tr>
					<tr><td width="50%" height="20%"  valign="top" align="center">${DATA.CAT_NAME}</td></tr>-->
				</table>
			</td>
		</tr>

    </tbody>
</table>

<table align="left"  width="10px" height="750px" ></table>
<#if  DATA_index % 4==3>

</td>
</tr></table><br><div style='page-break-after: always;'></div><table align="center"><tr>
</#if>
  </#list>
		</#if>


