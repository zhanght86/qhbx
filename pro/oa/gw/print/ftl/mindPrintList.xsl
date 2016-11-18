<?xml version='1.0' encoding='gb2312'?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
<xsl:output method="html"/>
<xsl:variable name="check" select="MindContent/flag"/>
<xsl:template match="/MindContent">
<html>
	<head>
	<title>打印意见列表</title>
	<style type="text/css">
		.head {
			font-family: "华文中宋";
			font-size: 28px;
			font-weight: bold;
			letter-spacing:5px;
			color: #FF0000;
		}
		.border1 {
			border: 2px solid #ff0000;
		}
		.border2 {
			border-bottom: 1px solid #ff0000;
		}
		.border3 {
			border-left: 1px solid #ff0000;
			border-bottom: 1px solid #ff0000;
		}
		.border4 {
			border-left: 1px solid #ff0000;
		}
		td {
			font-size: 14px;
			color: #000000;
			WORD-BREAK: break-all; WORD-WRAP: break-word;
		}
		table {
		    TABLE-LAYOUT: fixed; WORD-BREAK: break-all;
		}
		.fontcolor {
			font-size: 14px;
			color: #FF0000;
		}
		.title {
			font-family: "楷体_GB2312";	
			font-size: 20px;
			font-weight: bold;
			color: #000000;
		}
	</style>
	</head>
	<body marginheight="0" marginwidth="0" topmargin="0" leftmargin="3" text="#000000" bgcolor="#FFFFFF">
		<center>
			<table cellSpacing="0" cellPadding="0" width="600" border="0">
				<tr>
					<td class="head" align="center" height="120">
						<br/>文件意见清单 
					</td></tr>
				<tr>
					<td class="border1" vAlign="top" align="left">
						<table cellSpacing="0" cellPadding="0" width="100%" border="0">
							<tr>
								<td class="border2" align="center" width="18%" height="80">
									<font color="#ff0000">文件标题</font></td>
								<td class="border3" align="left" width="82%">
									<font class="title">
										<br/>
										<xsl:text>&#160;</xsl:text>
										<xsl:value-of select="DataBean/DataTitle"/>
									</font></td>
							</tr>
							<tr>
								<td class="border2" vAlign="middle" align="center" height="100">
									<font color="#ff0000">签发意见</font>
								</td>
								<xsl:call-template name="mindForm">
									<xsl:with-param name="type" select="'QF'"/>
								</xsl:call-template>
							</tr>
							<tr>
								<td class="border2" align="center" height="50">
									<font color="#ff0000">办 
										<xsl:text>&#160;</xsl:text>公 
										<xsl:text>&#160;</xsl:text>室 
										<br/>意 
										<xsl:text>&#160;</xsl:text>
										<xsl:text>&#160;</xsl:text>
										<xsl:text>&#160;</xsl:text>
										<xsl:text>&#160;</xsl:text>见 
									</font>
								</td>
								<xsl:call-template name="mindForm">
									<xsl:with-param name="type" select="'HP'"/>
								</xsl:call-template>
							</tr>
							<tr>
								<td class="border2" align="center" height="50">
									<font color="#ff0000">会签意见</font>
								</td>
								<xsl:call-template name="mindForm">
									<xsl:with-param name="type" select="'HQ'"/>
								</xsl:call-template>
							</tr>
							<tr>
								<td class="border2" align="center">
									<font color="#ff0000">文印意见</font>
								</td>
								<xsl:call-template name="mindForm">
									<xsl:with-param name="type" select="'WY'"/>
								</xsl:call-template>
							</tr>										
							<tr>
								<td class="border2" align="center">
									<font color="#ff0000">部门领导 
										<br/>审定意见 
									</font>
								</td>
								<xsl:call-template name="mindForm">
									<xsl:with-param name="type" select="'SD'"/>
								</xsl:call-template>
							</tr>
						</table>
					</td>
				</tr>
			</table>
		</center>
	</body>
</html>
</xsl:template>
<xsl:template name="mindForm">
	<xsl:param name="type"/>
	<td class="border3" vAlign="middle">
		<xsl:for-each select="MindList/Mind" xml:space="default">
			<xsl:if test="contains(TypeCode,$type)">
				<table cellSpacing="0" cellPadding="2" width="100%" border="0">
					<xsl:if test="$type = 'HQ'">
						<tr > 
							<td height="25" colspan="2">
								<font color="#000000">·<b>
									<xsl:value-of select="DeptName"/></b></font>
							</td>
						</tr>
					</xsl:if>
					<tr id="ID">
						<xsl:if test="$check = 'showCheck'">
							<td width="1%" align="center" id="chkbox6">
								<input type="checkbox" name="checkbox6" value='6' checked="checked"/>
							</td>
						</xsl:if>
						<td class="fontcolor" width="99%">
							<table cellSpacing="0" cellPadding="4" width="100%" border="0">
								<tr>
									<td class="fontcolor" vAlign="middle" align="left" width="15%">
										<font color="#000000">
											<xsl:value-of select="UserName"/></font>
									</td>
									<td class="fontcolor" align="left" width="55%">
										<font color="#000000">
											<xsl:value-of select="Content"/></font>
									</td>
									<td class="fontcolor" align="center">
										<font color="#000000">
											<xsl:value-of select="Time"/></font>
									</td>
								</tr>
							</table>
						</td>
					</tr>
				</table>
			</xsl:if>
		</xsl:for-each>
		<xsl:text>&#160;</xsl:text>
	</td>
</xsl:template>
</xsl:stylesheet>
<!-- Stylus Studio meta-information - (c) 2004-2005. Progress Software Corporation. All rights reserved.
<metaInformation>
<scenarios ><scenario default="yes" name="Scenario1" userelativepaths="yes" externalpreview="no" url="MindPrintXML.xml" htmlbaseurl="" outputurl="test.html" processortype="internal" profilemode="0" profiledepth="" profilelength="" urlprofilexml="" commandline="" additionalpath="" additionalclasspath="" postprocessortype="none" postprocesscommandline="" postprocessadditionalpath="" postprocessgeneratedext=""/></scenarios><MapperMetaTag><MapperInfo srcSchemaPathIsRelative="yes" srcSchemaInterpretAsXML="no" destSchemaPath="" destSchemaRoot="" destSchemaPathIsRelative="yes" destSchemaInterpretAsXML="no"/><MapperBlockPosition><template match="/"><block path="html/body/center/table/tbody/tr[1]/td/table/tbody/tr[1]/td[1]/xsl:for&#x2D;each" x="245" y="109"/><block path="html/body/center/table/tbody/tr[1]/td/table/tbody/tr[1]/td[1]/xsl:for&#x2D;each/xsl:if/=[0]" x="240" y="172"/><block path="html/body/center/table/tbody/tr[1]/td/table/tbody/tr[1]/td[1]/xsl:for&#x2D;each/xsl:if" x="286" y="174"/></template><template match="@Type=QF"></template><template match="@Type"></template></MapperBlockPosition></MapperMetaTag>
</metaInformation>
-->