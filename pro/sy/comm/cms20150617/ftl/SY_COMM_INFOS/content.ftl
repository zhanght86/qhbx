<HTML>
<HEAD>
<TITLE>${content.title}</TITLE>
<Meta http-equiv="Content-Type" content="text/html; charset=gb2312"/>
<Meta http-equiv="Content-Language" charset="gb2312"/>
<Meta name="keywords" content="中华保险办公自动化系统">
<Meta name="Description" content="中华保险">
<Meta name="Robots" content="All|None|Index|Noindex|Follow|Nofollow">
<Meta name="Author" content="东华合创">
<Meta name="Copyright" content="中华保险办公自动化系统">
<link href="/portal/render/cicoa/css/css.css" rel="stylesheet" type="text/css">
</HEAD>

<BODY>
<#include "skinpath.ftl">
<SCRIPT language=javascript src="/cms/javascript/common.js"></SCRIPT>
<script type='text/javascript' src='/dwr/engine.js'></script>
<script type='text/javascript' src='/dwr/util.js'></script>
<script type="text/javascript" src="/dwr/interface/pageViewActon.js"></script>
<script type="text/javascript" src="/common/js/showdate.js"></script>
<OBJECT ID="wb" WIDTH=0 HEIGHT=0 CLASSID="CLSID:8856F961-340A-11D0-A96B-00C04FD705A2"></OBJECT>
<div align="center">
<table   cellspacing="0" cellpadding="0" class="laout">
  <tr>
    <td>
    <table cellspacing="0" cellpadding="0" class="top">
        <tr>
            <td><img src="/portal/render/cicoa/images/top.jpg"/>
            </td>
        </tr>
    </table>
    <table   border="0" cellspacing="0" cellpadding="0" class="meun">
      <tr>
        <td class="meunbg">&nbsp;</td>
      </tr>
    </table>
    <table  cellspacing="0" cellpadding="0"  width="100%" border="0">
          <tr  class="rightsidernav03">
            <td width="100%" class="black" valign="middle"><div id="pv" style=" font:bold 14px 宋体" align="right"></div></td>
            <SCRIPT language=javascript src="/cms/javascript/pageview.js"></SCRIPT>
          </tr>
          <tr align="left" valign="top">
            <td colspan="2" class="rightsiderbg">
              <table  class="newst1" cellspacing="0" cellpadding="0">
                <tr>
                  <td width="67%"  class="Cameblack">
                  <a href="javascript:ChangeFontSize(18)" class="bluebg">大</a>&nbsp;&nbsp;
                  <a href="javascript:ChangeFontSize(14)" class="redbg">中</a>&nbsp;&nbsp;
                  <a href="javascript:ChangeFontSize(12)" class="greenbg">小</a>&nbsp;&nbsp;
                  </td>
                  <!--
                  <td width="11%"  class="Cameblack"><a href="javascript:printpreview()" >
                  <img src="/portal/render/cicoa/images/cicprintt.gif" width="80" height="20" border="0"/></a>
                  </td>
                  -->
                  <td width="11%"  class="Cameblack"><a href="javascript:doPrint()" ><img src="/portal/render/cicoa/images/cicprint.gif" width="80" height="20" border="0"/></a>
                  </td>
                  <td width="11%"  class="Cameblack"><a href="javascript:window.close()" ><img src="/portal/render/cicoa/images/ciclose.gif" width="80" height="20" border="0"/></a>
                  </td>
                </tr>
              </table>
              <table   class="newst" cellspacing="0" cellpadding="0">
              <tr>
                <td class="newsbold">${content.title}</td>
              </tr>
              <tr>
                <td><hr class="hr0"/></td>
              </tr>
              <tr>
                <td class="textnews"><table class="newstexttable" cellspacing="0" cellpadding="0">


                  <tr>
                    <td colspan="2" class="newsblack"><br>
                    <FONT id=ByZoom>${content.content}</FONT></td>
                  </tr>

              <tr>
                <td colspan="2"  class="tableblank"></td>
              </tr>


                </table></td>
              </tr>
            </table>
              <table  class="newst1" cellspacing="0" cellpadding="0">
                <tr>
                  <td width="67%"  class="Cameblack">
                  <a href="javascript:ChangeFontSize(18)" class="bluebg">大</a>&nbsp;&nbsp;
                  <a href="javascript:ChangeFontSize(14)" class="redbg">中</a>&nbsp;&nbsp;
                  <a href="javascript:ChangeFontSize(12)" class="greenbg">小</a>&nbsp;&nbsp;
                  </td>
                  <!--
                  <td width="11%"  class="Cameblack"><a href="javascript:printpreview()" >
                  <img src="/portal/render/cicoa/images/cicprintt.gif" width="80" height="20" border="0"/></a>
                  </td>
                  -->
                  <td width="11%"  class="Cameblack"><a href="javascript:doPrint()" >
                  <img src="/portal/render/cicoa/images/cicprint.gif" width="80" height="20" border="0"/></a>
                  </td>
                  <td width="11%"  class="Cameblack"><a href="javascript:window.close()" ><img src="/portal/render/cicoa/images/ciclose.gif" width="80" height="20" border="0"/></a>
                  </td>
                </tr>
              </table></td>
          </tr>
          <TR>
                <TD>
                <table  class="newst1" cellspacing="0" cellpadding="0" align="center">
                <tr>
                  <td >
                    <fieldset style="padding: 2">
                    <legend><b>附件列表</b></legend>
                        <TABLE  cellSpacing=0 cellPadding=0 align=center border=0>
                            <TBODY>
                              <TR>
                                    <TD  width="70%" align="center">
                                    附件名称</TD>
                                    <TD  width="15%" align="center">
                                    创建日期</TD>
                                    <TD  width="15%" align="center">
                                    附件类型</TD>
                              </TR>
                              <#list attachfile as being>
                                   <TR>
                                        <TD class="tablebar" height="25"><a href="/cms/simpleDownload?fileId=${being.fileId}"  class="black">${being.fileName}</a></TD>
                                        <TD class="tablebar" nowrap>${being.createdate}</TD>
                                        <TD class="tablebar" nowrap>${being.mimetype}</TD>
                                    </TR>
                              </#list>
                            </TBODY>
                        </TABLE>
                    </fieldset>
                    </TD>
            </TR>
        </table>
                </TD>
            </TR>
        </table>

         <table cellspacing="0" cellpadding="0" class="tableblank">
          <tr>
            <td></td>
          </tr>
        </table>
        <table cellspacing="0" cellpadding="0"  class="footerbg">
          <tr>
            <td align="center" valign="middle" class="gray">Copyright &copy;2008 China Insurance . All Rights Reserved
                     <br>
                    中华保险 版权所有 </td>
          </tr>
        </table>
         </td>
      </tr>
    </table>
</div>
</BODY>
</HTML>
