<style type="text/css">
	.type tr td{
	 padding-left:10px;
	 border:1px solid #91BDEA;
    }
</style>
<div id="OA_ZH_NYCBYW_CB-TYPE-BXXS"> 
<table  class="ui-checkbox-default type" style="display:inline-table;width:100%;">
<tbody>
<tr style="text-align:center;"><td><span>业务类型*</span></td><td><span>表现形式*</span></td></tr>
<#list _YWLX_ as fywlx>
     <tr><td code="${_ywlxServ_}"><input type="checkbox" value="${fywlx.ITEM_CODE}"  name="${_ywlxServ_}" /><label>${fywlx.NAME}</label></td>
  <td code="${_bxxsServ_}">
   <#list _BXXS_ as zbxxs>
       <#if (zbxxs.ITEM_CODE?index_of(fywlx.ITEM_CODE)==0)>
         <input type="checkbox" value="${zbxxs.ITEM_CODE}"  name="${_bxxsServ_}" /><label>${zbxxs.NAME}</label> 
       </#if>
    </#list>
  </td></tr>
</#list>
</tbody>
</table>
</div>