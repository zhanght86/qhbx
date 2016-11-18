<script>
function openListMore() {
	var opts = {"tTitle":"备忘录","url":"SY_COMM_REMIND.list.do","params":'',"menuFlag":3};
	Tab.open(opts);
};
function view(id,title) {
	var opts = {"url":"SY_COMM_REMIND.card.do?pkCode="+id,"tTitle":title,"params":null,"menuFlag":3};
	Tab.open(opts);
};
</script>
<div class='portal-box'>
	<div class='portal-box-title'>
		<span class="portal-box-title-pre"></span>
		<span class="portal-box-title-label">备忘录</span>
		<span class='portal-box-title-fix'></span>
		<span class="portal-box-more-sc"><a href="javascript:openListMore();">更多</a></span>
	</div>
	<div class='portal-box-con info-column' style='height:${height}'>
		<table width="100%" style="table-layout:fixed; margin-left:8px;"><tbody>
		<#if (_DATA_?size == 0)>
			<tr>
				<td align="center">该栏目下没有信息！</td>
			</tr>
		</#if>
		<#list _DATA_ as obj>
			<tr>
				<td class="icon"></td>
				<td style="width:65%;">
					<a href="javascript:view('${obj.REM_ID}','${obj.REM_TITLE}');" title="${obj.REM_TITLE}" style="margin-left:3px;height:28px;display:block">
					<span class="elipd">${obj.REM_TITLE}</span>
					</a>
				</td>
				<td  style="width:30%;">
					<span style="float:right;margin-right:6px;color:#999999;">
						<#if (obj.S_ATIME?length >10)>
							${obj.S_ATIME?substring(0,10)}
						<#else>
							${obj.S_ATIME}
						</#if>
					</span>
				</td>
			</tr>
		</#list>
		</tbody>
	  </table>
	</div>
</div>