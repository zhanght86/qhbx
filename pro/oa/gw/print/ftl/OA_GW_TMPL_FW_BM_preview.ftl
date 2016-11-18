<html>
<head>
    <title>打印 </title>    
</head>	

<body bgcolor="#FFFFFF" text="#000000">
<object ID="WebBrowser1" WIDTH="0" HEIGHT="0" CLASSID="CLSID:8856F961-340A-11D0-A96B-00C04FD705A2" VIEWASTEXT>
</object>

<script type="text/javascript" src="/core/frame/jquery-1.6.4.min.js"></script>
<script type="text/javascript" src="/core/frame/platform.js"></script>
<script type="text/javascript" src="/core/frame/tools.js"></script>
<script type="text/javascript" src="/core/frame/constant.js"></script>

<SCRIPT LANGUAGE="JavaScript">
function PageSetup(){
    document.styleSheets[0].addRule( "Input.Btn" , "display:none" , 0 );
    WebBrowser1.ExecWB(8,1,0,2);
    document.styleSheets[0].removeRule(0);
}

function PageSave(){
    document.styleSheets[0].addRule( "Input.Btn" , "display:none" , 0 );
    WebBrowser1.ExecWB(4,1);
    document.styleSheets[0].removeRule(0);
}


function printPage(){
    //打印前去掉区域
    document.all("idControls1").style.display="none";
    document.all("idControls2").style.display="none";
    var i,j;
    var paramName;
    var idName;
    try {
        var elem = document.all;
        for ( i=0; i<elem.length; i++){ 
            //打印前去掉不打印文字或线条
            paramName = elem[i]; 
            idName = paramName.id
            
            if (paramName.gwText != undefined && idName != "") {
                colValue = printdiv.all.item(idName).innerHTML;
                colLength = ComputeStringLength(colValue);
                printdiv.all.item(idName).gwText = colValue;
                colValue = "";
                for (j=0 ;j < colLength ; j++)
                {
                    colValue =colValue +" "; 
                }
                printdiv.all.item(idName).innerHTML=colValue;
            }
            
            if (paramName.gwBord != undefined) {
                myBoder = printdiv.all(idName).className;
                printdiv.all(idName).className = paramName.gwBord;
                paramName.gwBord = myBoder;
            }
        }
        
        //打印
        WebBrowser1.ExecWB(6,6);
            
        //打印后恢复去掉文字或线条
        for ( i=0; i<elem.length; i++) { 
            paramName = elem[i]; 
            idName = paramName.id
            if (paramName.gwText != undefined  && idName != "") {
                printdiv.all.item(idName).innerHTML = printdiv.all.item(idName).gwText;
                printdiv.all.item(idName).gwText = 1;
            }
            
            if (paramName.gwBord != undefined) {
                myBoder = paramName.gwBord;
                paramName.gwBord = printdiv.all(idName).className;
                printdiv.all(idName).className = myBoder;
            }
        } 
        //alert('打印成功！');
    } catch(e) {
        //打印后恢复去掉区域
        alert(idName);
        alert("打印失败！模板定义有误,请重新定义后再打印！");
    } finally {
        //打印后恢复去掉区域
        document.all("idControls1").style.display="";
        document.all("idControls2").style.display="";
    }
}

/**
 * 新加的调用window.print进行打印
 */
function printNew() {
    //将不打印的区域隐藏
    document.getElementById("idControls1").style.display="none";
    document.getElementById("idControls2").style.display="none";
    window.print();
    document.getElementById("idControls1").style.display="";
    document.getElementById("idControls2").style.display="";		
}

function printList() {    
	var mindReqObj = {};
	mindReqObj.SERV_ID = "${SERV_ID}";
	mindReqObj.DATA_ID = "${DATA_ID}";
	
	OpenWindow = window.open("${SERV_ID}.mindPrint.do?data=" + JsonToStr(mindReqObj)); 
}     

//不套打
function printPage1() {    
    //打印前去掉区域
    document.all("idControls1").style.display="none";
    document.all("idControls2").style.display="none";
    //打印
    WebBrowser1.ExecWB(6,6); 
    //打印后恢复去掉区域
    document.all("idControls1").style.display="";
    document.all("idControls2").style.display="";
}     

</SCRIPT>

<div id=idControls1 class="noprint" width="100%">
    <TABLE cellSpacing=0 cellPadding=0 border=0 width=100%>
        <tr>
			<td  align=center>
			<!--&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;-->

				<!--input type=button value="确认并进入打印预览" onclick="javascript:doPrint()"-->

				<input class=Btn id=set type=button value="页面设置" onclick="PageSetup();">
				<input class=Btn id=print type=button value="打印" onclick="printNew()">    

				<input class=Btn id=back type="button" value="关闭" ONCLICK="window.close()">

				<input class=Btn id=printList type="button" value="打印意见" onclick="printList()">
			</td>
		</tr>
        <tr><td>&nbsp;</td></tr>
        <tr>
			<td align=center>
				<font FONT-SIZE=12px COLOR=#000000>请先在“页面设置”中将页边距设置成：左17，右19，上17，下5。<br>如果发现有整体向上偏，则可以在“页面设置”中将页“上”边距加“1”或者将页“下”边距减“1”，<br>反之，则在“页面设置”中将页“上”边距减“1”或者将页“下”边距加“1”。</font>
			</td>
		</tr>
    </table>
</div>


<center>
    <!--<table width="640" border="0" cellspacing="0" cellpadding="3" height="100" >-->
    <form name="printForm" method="post" action="">
        <tr>
            <td>
                <div id=printdiv>
                ${printBody}
                </div>
            </td>
        </tr>
    </form>
    <!--</table>-->
</center>	

<div id=idControls2 class="noprint" align="center">
	<!--input type=button value="确认并进入打印预览" onclick="javascript:doPrint()"-->

	<input class=Btn id=set type=button value="页面设置" onclick="PageSetup();">
	<input class=Btn id=print type=button value="打印" onclick="printNew()"> 
	<!-- <input class=Btn id=print type=button value="不套打" onclick="printPage1()"> -->

	<input class=Btn id=back type="button" value="关闭" ONCLICK="window.close()">

	<input class=Btn id=printList type="button" value="打印意见" onclick="printList()">

</div>

</body>
</html>