
    <tbody>
		<#if DATALIST?size == 0>
		<div style="text-align:center;font-size:20px;font-family:仿宋_gb2312;font-weight:600;">没有数据！</div>
		</#if>
		<#if DATALIST?size != 0>
        <#list DATALIST as DATA>
		
        <#if DATA_index%10 == 0 >

		<p style="text-align:center;">
			<span style="font-family:仿宋_gb2312;font-size:26px;font-weight:600;">归&nbsp;&nbsp;档&nbsp;&nbsp;目&nbsp;&nbsp;录</span>
		</p>
		<br>
		<table style="border:2px solid black;border-collapse:collapse;width:610px;margin-left:auto;margin-right:auto;text-align:center;" >		
        <tr>
            <td style="border:1px solid black;border-collapse:collapse;padding:0px 7px;word-break:break-all;width:8%;height:40px;" >
                    <span style="font-family:仿宋_gb2312;font-size:16px;font-weight:600;">件号</span>
			</td>
            <td style="padding:0px 7px;border:1px solid black;border-collapse:collapse;word-break:break-all;width:11%;height:40px;">
                    <span style="font-family:仿宋_gb2312;font-size:16px;font-weight:600;">文号</span>
            </td>
            <td style="padding:0px 7px;border:1px solid black;border-collapse:collapse;word-break:break-all;width:11%;height:40px;">
                    <span style="font-family:仿宋_gb2312;font-size:16px;font-weight:600;">责任者</span>
            </td>
            <td style="padding:0px 7px;border:1px solid black;border-collapse:collapse;word-break:break-all;width:39%;height:40px;">
                    <span style="font-family:仿宋_gb2312;font-size:16px;font-weight:600;">文件名</span>
            </td>
            <td style="padding:0px 7px;border:1px solid black;border-collapse:collapse;word-break:break-all;width:8%;height:40px;">
                    <span style="font-family:仿宋_gb2312;font-size:16px;font-weight:600;">年度</span>
            </td>
            <td style="padding:0px 7px;border:1px solid black;border-collapse:collapse;word-break:break-all;width:8%;height:40px;">
                    <span style="font-family:仿宋_gb2312;font-size:16px;font-weight:600;">保管期限</span>
            </td>
            <td style="padding:0px 7px;border:1px solid black;border-collapse:collapse;word-break:break-all;width:14%;height:40px;">
                    <span style="font-family:仿宋_gb2312;font-size:16px;font-weight:600;">备注</span>
            </td>
        </tr>
		</#if>		
		
        <tr>
            <td style="padding:0px 7px;border:1px solid black;border-collapse:collapse;word-break:break-all;height:75px;">			    
                 ${DATA.DA_NUM}
            </td>
            <td style="padding:0px 7px;border:1px solid black;border-collapse:collapse;word-break:break-all;height:75px;">
                <#if DATA.GW_CODE??>
${DATA.GW_CODE}
</#if>
				
            </td>
            <td style="padding:0px 7px;border:1px solid black;border-collapse:collapse;word-break:break-all;height:75px;">
                ${DATA.DUTY}
            </td>
            <td style="padding:0px 7px;border:1px solid black;border-collapse:collapse;word-break:break-all;height:75px;">
                ${DATA.TITLE}
            </td>
            <td name="daYearFormat" style="padding:0px 7px;border:1px solid black;border-collapse:collapse;word-break:break-all;height:75px;">
              ${DATA.DA_YEAR}
            </td>
            <td style="padding:0px 7px;border:1px solid black;border-collapse:collapse;word-break:break-all;height:75px;">
                ${DATA.DA_TERM}
            </td>
            <td style="padding:0px 7px;border:1px solid black;border-collapse:collapse;word-break:break-all;height:75px;">
               ${DATA.REMARK}
            </td>
        </tr>
		
        <#if DATA_index%10 == 9 >
		</table>
		<div style='page-break-after: always;'></div>
		</#if>
		
        </#list>
		</#if>
    </tbody>
     <script type="text/javascript">
    	jQuery(document).ready(function(){
    		var daYearFormatObj = jQuery("td[name='daYearFormat']");
    		daYearFormatObj.each(function(){
    			var _self = jQuery(this);
	    		_self.html(_self.html().replace(",", ""));
    		});
    	});
    </script>


