<#if DATALIST?size == 0>
		<div style="text-align:center;font-size:20px;font-family:仿宋_gb2312;font-weight:600;">没有数据！</div>
</#if>	
	<#if DATALIST?size != 0>
        <#list DATALIST as DATA>

<table style="border:2px solid black;border-collapse:collapse;width:610px;margin-left:auto;margin-right:auto;">
    <tbody>
		<tr>
			<td style="border:1px solid black;border-collapse:collapse;height:100px;"  colspan="3">
				
			</td>
		</tr>
		<tr>
			<td  style="border:1px solid black;border-collapse:collapse;height:100px;text-align:center;"  colspan="3">
				<span style="font-size:28px;font-family:仿宋_gb2312;">${DATA.FONDS_NAME}</span>
			</td>
		</tr>
		<tr>
			<td style="height:350px;text-align:center"  valign="top"  colspan="3">
			<div style="font-size:22px;font-famliy:仿宋_gb2312;padding-top: 50px;">${DATA.TITLE}
			</div>
			</td>
		</tr>
		<tr>
			<td style="width:45%;height:20px;text-align:right;" ><span style="font-size:18px;font-famliy:仿宋_gb2312;">保管期限&nbsp;&nbsp;</span></td>
			<td style="border-bottom:1px solid black;width:15%;height:20px;text-align:center;"> <span style="font-size:14px;font-famliy:仿宋_gb2312;">${DATA.DA_TERM}</span></td>
			<td style="width:40%;"></td>
		</tr>
		<tr>
		<td style="height:20px;" colspan="3"></td>
		</tr>
		<tr>
			<td style="width:45%;height:20px;text-align:right;"><span style="font-size:18px;font-famliy:仿宋_gb2312;">密&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;集&nbsp;&nbsp;</span></td>
			<td style="border-bottom:1px solid black;width:15%;height:20px;text-align:center;"> <span style="font-size:14px;font-famliy:仿宋_gb2312;">${DATA.SECRET}</span></td>
			<td style="width:40%;"></td>
		</tr>
		
		<tr>
		<td style="height:20px;" colspan="3"></td>
		</tr>
		<tr>
			<td style="width:45%;height:20px;text-align:right;"><span style="font-size:18px;font-famliy:仿宋_gb2312;">年&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;度&nbsp;&nbsp;</span></td>
			<td style="border-bottom:1px solid black;width:15%;height:20px;text-align:center;"> <span style="font-size:14px;font-famliy:仿宋_gb2312;">${DATA.DA_YEAR}</span></td>
			<td style="width:40%;"></td>
		</tr>
		
		<tr>
		<td style="height:220px;" colspan="3"></td>
		</tr>
		
    </tbody>
</table>
</br>
<div style='page-break-after: always;'></div>
  </#list>
		</#if>
