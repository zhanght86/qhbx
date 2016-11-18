<!-- FAWEN_QICAO_zgrs.html-->
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<title>发文审批单</title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<style type="text/css">
<!--
.head {
	font-family: "华文中宋";
	font-size: 28px;
	font-weight: bold;
	color: #FF0000;
	letter-spacing:5px;
}
.fontcolor{
	font-family: "楷体_GB2312";
	font-size: 16px;
	color: #ff0000;
}
.fontcolor1{
	font-size: 16px;
	font-weight: bold;
	color: #000000;
}
.qianfa{
	font-size:16px;
	color: #000000;
	line-height:130%;
	
}
.huiqian{
	line-height:130%;
}
.zhusong{
	font-size:16px;
	color: #000000;
}
.border1 {
	border: 2px solid #ff0000;
}
.border2 {
	border-top: 1px solid #ff0000;
	border-right: 1px solid #ff0000;
}
.border3 {
    border-top: 1px solid #ff0000;
	
}
.border4 {
    border-left: 1px solid #ff0000;
}
.border5 {
    border-right: 1px solid #ff0000;
}
.border6 {
    border-top: 1px solid #ff0000;
	border-bottom: 1px solid #ff0000;
}
.border7 {
    border-top: 1px solid #ff0000;
    	border-bottom: 2px solid #ff0000;
}
.border8 {
	border-top: 2px solid #ff0000;
	border-right: 1px solid #ff0000;
}
.border9 {
    border-top: 2px solid #ff0000;
	
}
td {
	font-size: 14px;
	color: #000000;

	WORD-BREAK: break-all; WORD-WRAP: break-word;
}

table {
TABLE-LAYOUT: fixed; WORD-BREAK: break-all;
}

.title {
	font-family: "华文中宋";		
	font-size: 20px;
	font-weight: bold;
	color: #000000;
}
-->
</style>
</head>

<body leftmargin="0" topmargin="0" marginwidth="0" marginheight="0">
<center>
  <table width="650" border="0" cellspacing="0" cellpadding="0">
    <tr> 
      <td height="80" align="center" class="head">中唐网数码科技有限公司发文ee审批单</td>
    </tr>
    
    <tr>
      <td align="left" valign="top" ><table width="100%" border="0" cellspacing="0" cellpadding="0">
          <tr>
            <td width="50%" height="40"><table width="50%" border="0" cellspacing="0" cellpadding="0">
                <tr>
                  <td width="45%" align="left" class="fontcolor">&nbsp;密级（</td>
                  <td width="40%" align="left" class="fontcolor1">${gwBean.GW_SECRET}</td>
                  <td width="15%" align="left" class="fontcolor">）</td>
                </tr>
              </table></td>
            <td width="50%" align="right"><table width="50%" border="0" cellspacing="0" cellpadding="0">
                <tr> 
                  <td width="51%" align="right" class="fontcolor">&nbsp;缓急（</td>
                  <td width="39%" align="center"  class="fontcolor1">${gwBean.GW_EMERGENCY}</td>
                  <td width="10%" align="left" class="fontcolor">）</td>
                </tr>
              </table></td>
          </tr>
          <tr>
            <td width="50%" align="left" valign="top" class="border8">
				<table width="100%" height="110" border="0" cellpadding="0" cellspacing="0">
					<tr>
						<td height="10"></td>
					</tr>
					<tr> 
						<td width="100%" align="left" valign="top" class="fontcolor">&nbsp;签发：</td>
					</tr>
					<tr>  
						<td width="100%" align="left" valign="top">
						<br>
						<!--#mindlist1(col[</td><td width="20%" class="qianfa"><b>+space1+qianyijianren+</b></td><td width="80%">+yijianneirong+（+qianyijianshijian(1|10)+）],filter[QF_1020],order[d_u_priority asc])=style(checkbox)#-->
						 
						</td>
					</tr>
				</table>
			</td>
            <td align="left" width="50%"  valign="top" class="border9">
				<table width="100%" height="110" border="0" cellpadding="0" cellspacing="0">
					<tr> 
					    <td height="10"></td>
					</tr>
					<tr> 
					    <td width="100%" align="left" valign="top" class="fontcolor">&nbsp;会签：</td>
					</tr>
					<tr>
					  <td width="100%" align="left" valign="top" >
					  <br>
					  <!--#mindlist2(col[<span class=huiqian>+<b>+space1+qianyijianzhubumen+</b>+:+space2+qianyijianren+space2+yijianneirong+（+qianyijianshijian(1|10)+）+</span>],filter[HQ_HQ_1042])=style(checkbox)#--> 
						<#if hqMindList?size != 0>
							<#list hqMindList as mind>  
								<span class=huiqian><b>${mind.S_DNAME}</b>:${mind.S_UNAME} ${mind.MIND_CONTENT} (${mind.MIND_TIME})</span><br>
							</#list>
						</#if>
					  </td>
					</tr>
				</table> 
			</td>
            </tr>
        </table>
		
		<table width="100%" border="0" cellspacing="0" cellpadding="0">
          <tr> 
            <td width="36%" class="border2">
				<table width="100%" border="0" cellpadding="0" cellspacing="0">
					<tr> 
					    <td width="40%" class="fontcolor">&nbsp;办公室主任<br>
						<br>
						&nbsp;核&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;批
					    </td>
					    <td width="60%">&nbsp;
						<!--#mindlist3(col[qianyijianren+space1+(+qianyijianshijian(1|10)+)],filter[HP_1040])=style(list)#-->
						<#if hpMindList?size != 0>
							<#list hpMindList as mind>  
								${mind.S_UNAME} (${mind.MIND_TIME?substring(0,10)}) <br>
							</#list>
						</#if>
					    </td>
					</tr>
				</table>
			</td>
            <td width="32%" class="border2">
			    <table width="100%" border="0" cellpadding="0" cellspacing="0">
                <tr> 
                    <td width="40%" class="fontcolor"> &nbsp;办公室<br>
                    <br>
                    &nbsp;核&nbsp;&nbsp;稿
				    </td>
                    <td width="60%">&nbsp;
                    <!--#mindlist4(col[qianyijianren+space1+<br>+(+qianyijianshijian(1|10)+)],filter[HG_1041],order[d_u_priority asc])=style(checkbox)#-->
					<#if hgMindList?size != 0>
						<#list hgMindList as mind>  
							<span>${mind.S_UNAME} (${mind.MIND_TIME?substring(0,10)})</span>  <br>
						</#list>
					</#if>
                    </td>
                </tr>
                </table>
			</td>
            <td width="32%" class="border3">
			    <table width="100%" border="0" cellpadding="0" cellspacing="0">
                <tr> 
                    <td width="40%" class="fontcolor"  align="left"> &nbsp;拟稿部门<br>
                    <br>
                    &nbsp;领导审签
				    </td>
                    <td width="60%" align="left">
                    <!--#mindlist4(col[qianyijianren+space1+<br>+(+qianyijianshijian(1|10)+)],filter[SD_1043],order[d_u_priority asc])=style(checkbox)#-->
                    </td>
                </tr>
                </table>
			</td>
            </tr>
          <tr height="60"> 
            <td colspan="3" align="left" valign="top" class="border3">
			    <table width="100%" border="0" cellspacing="0" cellpadding="0">
                <tr height="60"> 
                    <td class="border5" width="36%" align="left" > 
				    <table width="100%" border="0" cellspacing="0" cellpadding="0">
                        <tr> 
                        <td width="40%" class="fontcolor" align="left" >&nbsp;拟稿处室</td>
                        <td width="55%">
                          <!--#mindlist6(col[qianyijianbumen],filter[SD_1043],row[end])=style(onlyline)#-->
                        </td>
                        </tr>
                    </table>
				    </td>
                    <td class="border5" width="32%">
				    <table width="100%" border="0" cellspacing="0" cellpadding="0">
                        <tr> 
							<td width="35%" class="fontcolor" align="left">&nbsp;拟稿人</td>
							<td width="65%" align="left">
							  ${gwBean.GW_CONTACT}(${gwBean.GW_BEGIN_TIME})
							</td>
                        </tr>
                    </table></td>
                    <td class="border0" width="32%"> 
				    <table width="100%" border="0" cellspacing="0" cellpadding="0">
                      <tr> 
                        <td width="40%" class="fontcolor" align="left">&nbsp;电&nbsp;&nbsp;&nbsp;&nbsp;话</td>
                        <td width="60%">
                          ${gwBean.GW_CONTACT_PHONE}
                        </td>
                      </tr>
                    </table>
				    </td>
                </tr>
              </table>
			 </td>
          </tr>
          <tr>
            <td colspan="3" align="left" valign="top" class="border3"><table width="100%" height="90" border="0" cellpadding="0" cellspacing="0">
                <tr> 
                  <td width="15%" height="10"></td>
				  <td width="85%" height="10"></td>
                </tr>
                <tr height="50"> 
                  <td  align="left" valign="top" class="fontcolor">&nbsp;发文标题：</td>
                  <td  align="left" valign="top" class="title">
                    ${dataBean.GW_TITLE}
                  </td>
                </tr>
              </table></td>
          </tr>
        </table>
        <table width="100%" border="0" cellspacing="0" cellpadding="0">
          <tr>
            <td align="left" valign="top" class="border3"><table width="100%" height="70" border="0" cellpadding="0" cellspacing="0">
                <tr> 
                  <td width="15%" height="10"></td>
				  <td width="85%" height="10"></td>
                </tr>
                <tr> 
                  <td width="15%" align="left" valign="top" class="fontcolor">&nbsp;附件标题：</td>
                  <td width="85%" align="left" valign="top"><!--#attachmentlist(col[fujianmingcheng])=style(list)#--></td>
                </tr>
              </table></td>
          </tr>
        </table>
        <table width="100%" border="0" cellspacing="0" cellpadding="0">
          <tr>
            <td align="left" valign="top" class="border3"><table width="100%" height="70" border="0" cellpadding="0" cellspacing="0">
                <tr> 
                  <td width="15%" height="10"></td>
				  <td width="85%" height="10"></td>
                </tr>
                <tr> 
                  <td width="8%" align="left" valign="top" class="fontcolor">&nbsp;主&nbsp;&nbsp;&nbsp;&nbsp;送：</td>
                  <td width="92%" align="left" valign="top" class="zhusong">&nbsp;${gwBean.GW_MAIN_TO}</td>
                </tr>
              </table></td>
          </tr>
        </table>
		<table width="100%" border="0" cellspacing="0" cellpadding="0">
          <tr>
            <td align="left" valign="top" class="border3"><table width="100%" height="70" border="0" cellpadding="0" cellspacing="0">
                <tr> 
                  <td width="15%" height="10"></td>
				  <td width="85%" height="10"></td>
                </tr>
                <tr> 
                  <td width="8%" align="left" valign="top" class="fontcolor">&nbsp;抄&nbsp;&nbsp;&nbsp;&nbsp;送：</td>
                  <td width="92%" align="left" valign="top" class="zhusong">&nbsp;${gwBean.GW_COPY_TO}</td>
                </tr>
              </table></td>
          </tr>
        </table>
		<table width="100%" border="0" cellspacing="0" cellpadding="0">
          <tr>
            <td align="left" valign="top" class="border3"><table width="100%" height="50" border="0" cellpadding="0" cellspacing="0">
                <tr> 
                  <td width="15%" height="10"></td>
				  <td width="85%" height="10"></td>
                </tr>
                <tr> 
                  <td width="8%" align="left" valign="top" class="fontcolor">&nbsp;主&nbsp;题&nbsp;词：</td>
                  <td width="92%" align="left" valign="top" class="zhusong">&nbsp;${gwBean.GW_TOPIC}</td>
                </tr>
              </table></td>
          </tr>
        </table>
        
		<table width="100%" border="0" cellspacing="0" cellpadding="0">
          <tr>
            <td height="50" class="border3"><table width="100%" border="0" cellspacing="0" cellpadding="0">
                <tr>
                  
                  <td width="23%" aligh="right" class="fontcolor"> 拟稿人终校：</td>
                  <td width="22%">&nbsp;<!--#mindlist8(col[qianyijianren],filter[JD_1044],row[end])=style(onlyline)#--></td>
				  <td width="13%" class="fontcolor">&nbsp;</td>
                  <td width="47%">&nbsp;</td>
                </tr>
              </table></td>
          </tr>
        </table>
        
        <table width="100%" border="0" cellspacing="0" cellpadding="0">
          <tr> 
            <td height="50" class="border7"><table width="100%" border="0" cellspacing="0" cellpadding="0">
                <tr> 
                  <td width="28%"><table width="100%" border="0" cellspacing="0" cellpadding="0">
                      <tr> 
                        <td width="53%" class="fontcolor">&nbsp;发文编号：</td>
                        <td width="47%" align="left"><!--#bianhao#--></td>
                      </tr>
                    </table></td>
                  <td width="25%" align="left"><table width="100%" border="0" cellspacing="0" cellpadding="0">
                      <tr> 
                        <td width="5%"  align="center" class="fontcolor">〔</td>
                        <td width="20%" align="center"><!--#niandu#--></td>
                        <td width="5%" align="center" class="fontcolor">〕</td>
                        <td width="15%" align="center"><!--#hao#--></td>
                        <td width="55%" align="left" class="fontcolor">号</td>
                      </tr>
                    </table></td>
                  <td width="47%" align="right"><table width="80%" border="0" cellspacing="0" cellpadding="0">
                      <tr> 
                        <td width="45%" align="right"><font class="fontcolor">印发日期：</font><!--#signdate=data(1|4)#--></td>
                        <td width="8%" align="center" class="fontcolor">年</td>
                        <td width="8%" align="center"><!--#signdate=data(6|2)#--></td>
                        <td width="8%" align="center" class="fontcolor">月</td>
                        <td width="8%" align="center"><!--#signdate=data(9|2)#--></td>
                        <td width="23%" align="left" class="fontcolor">日封发</td>
                      </tr>
                    </table></td>
                </tr>
              </table></td>
          </tr>
	</table>
	</td>
		         
    </tr>
  </table>
</center>

</body>
</html>

