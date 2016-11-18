		
//*************************************************
//
//	Author: Yu Zhang
//	Created Date: 2004-03-08
//	Version: 1.0.0.2 (the same with RTXName.dll)
//
//*************************************************
var RTX_STATUS_UNKNOWN = -1;
var RTX_STATUS_OFFLINE = 0;
var RTX_STATUS_ONLINE = 1;
var RTX_STATUS_AWAY = 2;

var RTX_DOWNLOAD_TIMEOUT_TRYCOUNT = 300; // 30s = 300 * 100
var rtx_downloadTryCount = 0;

var rtx_oldWinOnload = null;
var rtx_isFirstLoad = false;
var rtx_oldWinScroll = null;
var rtx_isInWinScroll = false;

var rtx_needDownload = false;
var rtx_codebase = null;

if(rtx_codebase == null)
{
	RDL("cab");
}

var rtx_nameControlID = "RTXNameControlID";
var rtx_nameControlHTMLFormat = "<object classid='clsid:8F8086BE-0925-481D-B3C1-06BCB4121A5E' codebase='%CODEBASE%/RTXName.dll#version=2,0,1,0' id='" + rtx_nameControlID + "'  style='display:none;'></object>";
var rtx_namePrefix = "rtxname";
var rtx_nameIndex = 0;
var rtx_nameControl = null;
var rtx_isNameControlInited = false;
var rtx_isNameControlLoadError = false;
var rtx_idDictionary = null;
var rtx_stateDictionary = null;
var rtx_nickDictionary = null;
var rtx_showOfflineDictionary = null;
var rtx_groupidDictionary = null;
var rtx_pageGroupNicks = "";

var m_isLogout = false;
var rtx_updateAllStatusCallInfo = null;
var rtx_frameRandom = 0;

//wwjs 2004.7.21 =============================
var rtx_deployUrlDictionary = null;
var rtx_helpUrlDictionary = null;
var rtx_userRTXClientDictionary = null;
var rtx_getStatusUrlDictionary = null;

var rtx_deployUrlDictionarys = null;
var rtx_helpUrlDictionarys = null;
var rtx_userRTXClientDictionarys = null;
var rtx_getStatusUrlDictionarys = null;

var RTX_USERTX = 1;
var RTX_DEF_DEPLOYURL = ":8012/quickdeploy";
var RTX_DEF_HELPURL = ":8012/help/start.htm";
//var RTX_DEF_GETSTATUSURL = ":8012/getstatus.php";
var RTX_DEF_GETSTATUSURL = ":8012/getimageex.cgi";
var rtxEvent;

function rtx_getStatusHost(url)
{
  var pos = 0;
  var re = new RegExp("[^/]/[^/]")
  var host = "";
 // alert(url);
  pos = url.search(re);
 // alert(pos);
  if(pos > 0)
  {
	 host = url.substr(0, pos+1);
  }
//  alert(host);
  return host;
}

function rtx_addUrl(src, id)
{
   // alert("hello");
    var hostUrl = "";
    if(document.location && document.location.host)
	{
		hostUrl = "http://" + document.location.host;
	}
	//alert("hostUrl");

	if(typeof(src.statusUrl) != "undefined")
	{
		rtx_getStatusUrlDictionary[id] = src.statusUrl;
		hostUrl = rtx_getStatusHost(src.statusUrl);		
	}
	else
	{
		rtx_getStatusUrlDictionary[id] = hostUrl + RTX_DEF_GETSTATUSURL;
	}
		
	if(typeof(src.deployUrl) != "undefined")
	{
		rtx_deployUrlDictionary[id] = src.deployUrl;
	}
	else
	{
		rtx_deployUrlDictionary[id] = hostUrl + RTX_DEF_DEPLOYURL;
	}
				
	if(typeof(src.helpUrl) != "undefined")
	{
		rtx_helpUrlDictionary[id] = src.helpUrl;
	}
	else
	{
		rtx_helpUrlDictionary[id] = hostUrl + RTX_DEF_HELPURL;
	}
	

		
	if(typeof(src.noRTX) != "undefined")
	{
		rtx_userRTXClientDictionary[id] = 0;
	}
	else
	{
		rtx_userRTXClientDictionary[id] = 1;
	}
}
function rtx_addGroupUrl(src, id)
{
    var hostUrl = "";
    if(document.location && document.location.host)
	{
		hostUrl = document.location.host;
	}

	if(typeof(src.statusUrl) != "undefined")
	{
		rtx_getStatusUrlDictionarys[id] = src.statusUrl;
		hostUrl = rtx_getStatusHost(src.statusUrl);		
	}
	else
	{
		rtx_getStatusUrlDictionarys[id] = hostUrl + RTX_DEF_GETSTATUSURL;
	}
			
	if(typeof(src.deployUrl) != "undefined")
	{
		rtx_deployUrlDictionarys[id] = src.deployUrl;
		
	}
	else
	{
		rtx_deployUrlDictionarys[id] = hostUrl + RTX_DEF_DEPLOYURL;
	}
				
	if(typeof(src.helpUrl) != "undefined")
	{
		rtx_helpUrlDictionarys[id] = src.helpUrl;
	}
	else
	{
		rtx_helpUrlDictionarys[id] = hostUrl + RTX_DEF_HELPURL;
	}

	
		
		
	if(typeof(src.noRTX) != "undefined")
	{
		rtx_userRTXClientDictionarys[id] = 0;
	}
	else
	{
		rtx_userRTXClientDictionarys[id] = 1;
	}
}

function rtx_setUrl(deployUrl, helpUrl, useRTX, statusUrl)
{
//	alert(deployUrl);
//	alert(helpUrl);
//	alert(useRTX);
	
	rtx_nameControl.DeployUrl = deployUrl;
	rtx_nameControl.HelpUrl = helpUrl;
	rtx_nameControl.UseRTXClient = useRTX;
	rtx_nameControl.GetStatusUrl = statusUrl;
}
function rtx_setUrlFromID(id)
{
//	alert(rtx_idDictionary[id]);
//	alert(id);
//	alert(rtx_nickDictionary[id]);
//	alert(rtx_userRTXClientDictionary[id]);

	rtx_setUrl(rtx_deployUrlDictionary[id], 
			   rtx_helpUrlDictionary[id], 
			   rtx_userRTXClientDictionary[id],
			   rtx_getStatusUrlDictionary[id]);
}
function rtx_setUrlFromIDs(id)
{
	rtx_setUrl(rtx_deployUrlDictionarys[id], 
	rtx_helpUrlDictionarys[id], 
	rtx_userRTXClientDictionarys[id],
	rtx_getStatusUrlDictionarys[id]);
}
///////////////////////////////////////////////////////




function RAGP(nicks, isPageGroup)
{
	rtx_addGroupPresence(nicks, isPageGroup);
}

function rtx_addGroupPresence(nicks, isPageGroup)
{
    var src = rtxEvent.srcElement;
	var id;
	
	rtx_register_winOnload();
	
	if(!rtx_groupidDictionary)
	{
		rtx_groupidDictionary = new Object();

		rtx_deployUrlDictionarys = new Object();
		rtx_helpUrlDictionarys = new Object();
		rtx_userRTXClientDictionarys = new Object();
		rtx_getStatusUrlDictionarys = new Object();
	
		

	}
	
	if(rtx_groupidDictionary && src && typeof(nicks) == "string")
	{
		id = rtx_namePrefix + (rtx_nameIndex++);
		src.id = id;
		src.isGroup = true;
		
		if(isPageGroup)
		{
			src.isPageGroup = true;
		}

		rtx_groupidDictionary[id] = nicks;
		rtx_addGroupUrl(src, id);

		src.onload = null;
	}
}

function rtx_updateGroupImages()
{
	var id;
	var obj;
	var img;
	
	for(id in rtx_groupidDictionary)
	{
		obj = document.images(id);
		
		if(obj && obj.isGroup)
		{
//			obj.onmouseover = rtx_showUIMouse;
//			obj.onmouseout = rtx_hideUI;

			img = "rtxgrp.gif";
			window.setTimeout("rtx_updateImage('" + id + "', '" + img + "');", 1);
		}
	}
}

function RAP(nick,event)
{
	
	if(rtx_needDownload)
	{
		rtx_downloadNameControl();
		rtx_needDownload=false;
	}

	rtx_addPresence(nick,event);
}

function rtx_addPresence(nick,event)
{
//	debugger;
    var src = event.srcElement;// obj;//event.srcElement;
	var id;
 
	
	if(!rtx_idDictionary)
	{
		rtx_idDictionary = new Object();
		rtx_stateDictionary = new Object();
		rtx_nickDictionary = new Object();
		rtx_showOfflineDictionary = new Object();
		
		rtx_deployUrlDictionary = new Object();
		rtx_helpUrlDictionary = new Object();
		rtx_userRTXClientDictionary = new Object();
		rtx_getStatusUrlDictionary = new Object();
		
	}
	
	
	if(rtx_idDictionary && src)
	{

		id = rtx_namePrefix + (rtx_nameIndex++);
		src.id = id;
		rtx_idDictionary[id] = nick;
		
		rtx_stateDictionary[id] = RTX_STATUS_UNKNOWN;
		
		if(typeof(rtx_nickDictionary[nick]) == "undefined")
		{
			rtx_nickDictionary[nick] = new Array();
			rtx_pageGroupNicks += nick + '\0';
		}

		var idArray = rtx_nickDictionary[nick];
		
		if(typeof(idArray) == "object")
		{
			idArray[idArray.length] = id;
		}

		if(typeof(src.showOffline) != "undefined")
		{
			rtx_showOfflineDictionary[id] = true;
		}

		rtx_addUrl(src, id);
	
		rtx_createNameControl();
		
 
		src.onmouseover = function(e){
			var e = e || window.event;
			log("e",e);
			var position = {
					uiX : e.screenX ,
					uiY : e.screenY 
			};
			rtx_showUIMouse(position,event);
		} 
        src.onmouseout = rtx_hideUI;

	}
}

function rtx_showUIMouse(position,event)
{
    rtx_showUI(0,position,event);
}

function rtx_showUI(inputType,position,event)
{
//	debugger;
	var obj = event.srcElement;
 
	if(obj && rtx_nameControl)
	{
		var uiLocation = position;// rtx_getUILocation(obj); 
		
		//wwjs 2004.7.21
		var deployUrl = null;
		var helpUrl = null;
		var useRTX = null;

		if(obj.isGroup)
		{
			var nicks = null;
			
			if(obj.isPageGroup)
			{
				nicks = rtx_pageGroupNicks;
			}
			else if(rtx_groupidDictionary)
			{
				nicks = rtx_groupidDictionary[obj.id];
	
			}

			if(typeof(nicks) == "string" && nicks.length > 0)
			{
				//wwjs 2004.7.21
				rtx_setUrlFromIDs(obj.id);
				rtx_nameControl.ShowGroupUI(nicks, inputType, uiLocation.uiX, uiLocation.uiY);
			}
		}
		else
		{
			if(rtx_idDictionary)
			{
				var nick = rtx_idDictionary[obj.id];
				//wwjs 2004.7.21
				rtx_setUrlFromID(obj.id);
				rtx_nameControl.ShowOOUI(nick, inputType, uiLocation.uiX, uiLocation.uiY);
			}
		}
	}
}
function log(msg , obj){
//	console.log("--------"+msg+" start----------");
//	console.debug(obj);
}

function rtx_getUILocation2(objSrc){
	
	var w =window;
	var outterWidth = $(window.top).width();
	var outterLeft  = (outterWidth - $(document).width())/2 +3;
	var outterTop   = 99 +33;
	
	
	log("position",$(objSrc).position());
	log("offset",$(objSrc).offset());
	log("window.scollTop",$(window).scrollTop());
	log("window.top.scollTop",$(window.top).scrollTop());
	log("window.width",$(window).width());
	log("window.top.width",$(window.top).width());
	log("document",$(document).width());
	
	
	var left = $(objSrc).offset().left,
		top  = $(objSrc).offset().top - $(window.top).scrollTop();
	
	log("left",left + outterLeft);
	log("top",top + outterTop);
	log("atop",top + outterTop - $(window.top).scrollTop());
	
	var atop  = top + outterTop - $(window.top).scrollTop(),
		aleft = left + outterLeft;
	
	return {
		uiX : aleft,
		uiY : atop
	};
}


function rtx_getUILocation(objSrc)
{
//	debugger;
	var obj = objSrc;
	var uiLocation = new Object();
	var uiX = 0;
	var uiY = 0
	var objDX = 0;
	var fRtl = (document.dir == "rtl");
	var parentWindow = window;
	
	if(rtx_frameRandom == 0)
	{
		var dt = new Date();

		rtx_frameRandom = dt.getSeconds() * 1000 + dt.getMilliseconds();
	}

	rtx_frameRandom++;

	while(obj && obj.rtxframeRandom != rtx_frameRandom)
	{
		obj.rtxframeRandom = rtx_frameRandom;

		if(fRtl)
		{
			if(obj.scrollWidth >= obj.clientWidth + obj.scrollLeft)        
			{
				objDX = obj.scrollWidth - obj.clientWidth - obj.scrollLeft;
			}
			else
			{
				objDX = obj.clientWidth + obj.scrollLeft - obj.scrollWidth;
			}

			uiX += obj.offsetLeft + objDX;
		}
		else
		{
			uiX += obj.offsetLeft - obj.scrollLeft;
		}

		uiY += obj.offsetTop - obj.scrollTop;
		obj = obj.offsetParent;            

		if(!obj)
		{
			if(parentWindow.frameElement)
			{
				obj = parentWindow.frameElement;

				parentWindow = parentWindow.parent;
			}
		}
	}

	if(parentWindow)
	{
		uiX += parentWindow.screenLeft;
		uiY += parentWindow.screenTop;
	}

	uiLocation.uiX = 300;//uiX;
	uiLocation.uiY = 300;//uiY;

	if(fRtl)
	{
		uiLocation.uiX += objSrc.offsetWidth;
	}

	return uiLocation;
}

function rtx_hideUI()
{
	  
	if(rtx_nameControl)
	{
		rtx_nameControl.HideOOUI();
		return false;
	}

	return true; 
}

function rtx_register_winScroll()
{
	rtx_oldWinScroll = window.onscroll;
	window.onscroll = rtx_winScroll;
}

function rtx_winScroll()
{
	if(!rtx_isInWinScroll)
	{
		rtx_isInWinScroll = true;
		rtx_hideUI();
	}

	rtx_isInWinScroll = false;

	return typeof(rtx_oldWinScroll) == "function" ? rtx_oldWinScroll() : true;   
}

function RDL(codebase)
{
	rtx_download(codebase);
}

function rtx_download(codebase)
{
	if(typeof(codebase) == "string")
	{
		rtx_needDownload = true;
		rtx_codebase = codebase;
	}
}

function rtx_downloadNameControl()
{
	var strHTML = rtx_nameControlHTMLFormat.replace(/%CODEBASE%/g, rtx_codebase);

	document.body.insertAdjacentHTML("beforeEnd", strHTML);
	
	//rtx_nameControlLoadTimer();
}

function rtx_createNameControl()
{
//	debugger;
//	if(rtx_needDownload)
//	{
//		rtx_downloadNameControl();
//		rtx_needDownload=false;
//	}
//	else
//	{
		rtx_ensureNameControl();
//	}
}

function rtx_nameControlOnload()
{
	rtx_ensureNameControl();
}

function rtx_nameControlLoadTimer()
{
//	debugger;
	if(!rtx_isNameControlLoadError)
	{
		var obj = document.all(rtx_nameControlID);

		if(obj && typeof(obj.PresenceEnabled) != "undefined")
		{
			if(obj.PresenceEnabled)
			{
				rtx_nameControlOnload();
			}
		}
		/*else
		{
			if(rtx_downloadTryCount < RTX_DOWNLOAD_TIMEOUT_TRYCOUNT)
			{
				rtx_downloadTryCount++;
				window.setTimeout("rtx_nameControlLoadTimer();", 100);
			}
			else
			{
				rtx_ensureNameControl(); // Advanced try.
			}
		}*/
	}
	else
	{
		rtx_ensureNameControl(); // Advanced try.
	}
}

function rtx_nameControlOnError()
{
//	debugger;
	rtx_isNameControlLoadError = true;
}

function rtx_ensureNameControl()
{
//    if(!rtx_isNameControlInited)
//    {
		if(rtx_browserInfo.ie5up && rtx_browserInfo.win32)
		{
//@cc_on
//@if(@_jscript_version >= 5)
//@			try
//@			{
//@				rtx_nameControl = new ActiveXObject("RTXName.NameCtrl");
//@			}
//@			catch(e)
//@			{
//@			};
//@else
//@end
		}
        
		rtx_isNameControlInited = true;

		if(rtx_nameControl)
		{
			if(rtx_nameControl.PresenceEnabled)
			{
				rtx_register_winScroll();
//				
//				if(rtx_nameControl.SupportGroup)
//				{
//					rtx_updateGroupImages();
//				}
				
				rtx_nameControl.OnStatusChange = rtx_OnStatusChange;
				rtx_updateAllStatus(1);
			}
			else
			{
				rtx_nameControl = null;
			}
		}
//	} 
	
	return rtx_nameControl;
}

function rtx_OnStatusChange(nick, state, file, rtxNum)
{
 
	
    if(rtx_idDictionary)
    {
    	
//    console.log(rtx_idDictionary);
		if(state >= 0)
		{
			var idArray = rtx_getIDArrayFromNick(nick);
			var index;

			if(idArray)
			{
				for(index in idArray)
				{
					rtx_updateImageOfID(idArray[index], state, file);
				}
			}

			rtx_onlogin();
        }
        else
        {
			switch(state)
			{
			case -1:
				m_isLogout = true;
			
				window.setTimeout("rtx_allOffline();", 1000);
				break;
			case -2:
				rtx_onlogin();
				break;
			default:
				break;
			}
        }
    }
}

function rtx_winOnload()
{
//    if(rtx_oldWinOnload != null)
//    {
//	    if(typeof(rtx_oldWinOnload) == "function" || typeof(rtx_oldWinOnload) == "object")
//	    {
//		    rtx_oldWinOnload();
//	    }
//	}
	
	rtx_createNameControl();
}

function rtx_register_winOnload()
{
//	debugger;
//	if(!rtx_isFirstLoad)
//	{
//		rtx_isFirstLoad = true;
		
//		rtx_oldWinOnload = window.onload;
//		window.onload = rtx_winOnload;
		var t =setTimeout(function(){
			 rtx_winOnload();
			 clearTimeout(t);
		},100);
	 
			  
			 
//	}
}

function rtx_getIDArrayFromNick(nick)
{
    var idArray = null;

    if(typeof(nick) == "string" && typeof(rtx_nickDictionary[nick]) == "object")
    {
        idArray = rtx_nickDictionary[nick];
    }

    return idArray;
}

function rtx_allOffline()
{
	var state = RTX_STATUS_OFFLINE;
	var nick;
	var idArray;
	var index;
	
	if(rtx_nickDictionary)
	{
		for(nick in rtx_nickDictionary)
		{
			idArray = rtx_getIDArrayFromNick(nick);

			if(idArray)
			{
				for(index in idArray)
				{
					rtx_updateImageOfID(idArray[index], state, "");
				}
			}
		}
	}

	m_retryCount = 0;
}

function rtx_isOnlineState(state)
{
	return state == RTX_STATUS_ONLINE || state == RTX_STATUS_AWAY;
}

function rtx_onlogin()
{
//	debugger;
	if(m_isLogout)
	{
		m_isLogout = false;

		if(!rtx_updateAllStatusCallInfo)
		{
			window.setTimeout("rtx_updateAllStatus(5);",6000);
		}
	}
}

function rtx_updateAllStatus(retryCount)
{
	//debugger;
	var updateCount;
	var state = 0;
	var stateImage;
	var nick;
	var id;
	var dict;
	var i;
	var idArray;
	var maxLoopCount = 10;
	
	if(!rtx_updateAllStatusCallInfo)
	{
		rtx_updateAllStatusCallInfo = new Object();
		
		updateCount = 0;
		
		dict = new Object();
		rtx_updateAllStatusCallInfo.dict = dict;

		i = 0;
		idArray = new Array();
		for(id in rtx_idDictionary)
		{
			idArray[i++] = id;
		}
		rtx_updateAllStatusCallInfo.idArray = idArray;

		rtx_updateAllStatusCallInfo.index = 0;
	}
	else
	{
		updateCount = rtx_updateAllStatusCallInfo.updateCount;
		dict = rtx_updateAllStatusCallInfo.dict;
		idArray = rtx_updateAllStatusCallInfo.idArray;
	}

	if(rtx_idDictionary && rtx_nameControl && dict && idArray)
	{
		var loopCount = idArray.length < (rtx_updateAllStatusCallInfo.index + maxLoopCount) ? idArray.length : (rtx_updateAllStatusCallInfo.index + maxLoopCount); 

		for(i = rtx_updateAllStatusCallInfo.index; i < loopCount; i++)
		{
			id = idArray[i];
			nick = rtx_idDictionary[id];

			if(dict[nick])
			{
				stateImage = dict[nick].stateImage;
				state = dict[nick].state;
			}
			else
			{
				rtx_setUrlFromID(id);
				stateImage = rtx_nameControl.GetStatusImage(nick, id);
				state = rtx_getStatusFromImage(stateImage);
				
				dict[nick] = new Object();
				dict[nick].stateImage = stateImage;
				dict[nick].state = state;

				if(rtx_isOnlineState(state))
				{
					updateCount++;
				}
			}
			if(rtx_updateImageOfID(id, state, stateImage))
			{
			}
		}
		
		if(!m_isLogout)
		{
			if(i < idArray.length)
			{
				rtx_updateAllStatusCallInfo.index = i;
				rtx_updateAllStatusCallInfo.updateCount = updateCount;
				window.setTimeout("rtx_updateAllStatus(" + retryCount + ");", 10);
			}
			else
			{
				rtx_updateAllStatusCallInfo = null;
				
				if(updateCount < 2)
				{
					retryCount--;
					
					if(retryCount > 0)
					{
						window.setTimeout("rtx_updateAllStatus(" + retryCount + ");", 6000);
					}
				}
			}
		}
		else
		{
			rtx_updateAllStatusCallInfo = null;
		}
	}
}

function rtx_getStatusImage(state, stateImage, showOffline)
{
	var img = "blank.gif";

	if(stateImage == "")
	{
		switch(state)
		{
		case RTX_STATUS_OFFLINE:
			if(showOffline)
			{
				img = "rtxoff.gif";
			}
			else
			{
				img = "blank.gif";
			}
			break;      
		case RTX_STATUS_ONLINE:
			img = "rtxon.gif";
			break;  
		case RTX_STATUS_AWAY:
			img = "rtxaway.gif";
			break;
		default:
			break;
		}
	}
	else
	{
		if(state == RTX_STATUS_UNKNOWN || (state == RTX_STATUS_OFFLINE && !showOffline))
		{
			img = "blank.gif";
		}
		else
		{
			//img = stateImage;
			img="on.png";
		}
	}
 
	return img;
}
function rtx_getStatusFromImage(stateImage)
{
//	debugger;
	var state = RTX_STATUS_UNKNOWN;
	var stateFromImage;
	var ext = "";

	if(typeof(stateImage) == "string" && stateImage.length >= 5)
	{
		ext = stateImage.substring(stateImage.length - 4, stateImage.length);

		if(ext == ".bmp")
		{
			stateFromImage = stateImage.substring(stateImage.length - 5, stateImage.length - 4);
			
			stateFromImage = parseInt(stateFromImage, 10);
			
			if(isNaN(stateFromImage))
			{
				stateFromImage = 0;
			}
			
			switch(stateFromImage)
			{
			case 1:
				state = RTX_STATUS_ONLINE;
				break;
			case 2:
				state = RTX_STATUS_OFFLINE;
				break;
			case 3:
				state = RTX_STATUS_AWAY;
				break;
			default:
				state = RTX_STATUS_UNKNOWN;
				break;
			}
		}
	}

	return state;
}

function rtx_updateImageOfID(id, state, stateImage)
{
	var isUpdate = false;

	if(rtx_idDictionary && id != null)
	{
		var img = rtx_getStatusImage(state, stateImage, rtx_showOfflineDictionary[id]);

		if(rtx_stateDictionary[id] != state)
		{
			img = img.replace(/\\/g, "\\\\");
			window.setTimeout("rtx_updateImage('" + id + "', '" + img + "');rtx_stateDictionary['" + id + "'] = " + state + ";", 1);

			isUpdate = true;
		}
	}

	return isUpdate;
}

function rtx_updateImage(id, img)
{
//	debugger;
	var obj = document.images(id);

	if(obj && typeof(img) == "string")
	{
		var oldImg = obj.src;
		var index;
		var newImg;

		if(!obj.imageBaseURL)
		{
			index = oldImg.lastIndexOf("/");
			obj.imageBaseURL = oldImg.slice(0, index + 1);
		}

		index = img.lastIndexOf("\\");

		if(index == -1)
		{
			newImg = obj.imageBaseURL + img;
		}
		else
		{
			newImg = img;
		}

		if(oldImg != newImg)
		{
			//obj.style.filter = "Chroma(Color = '#008080', Enable = true)";
			obj.src = newImg;
		}
	}
}
/*$(function () {
   $(".RTX").mouseenter(function(event){
    	var uid = $(".RTX").attr("data-id");
		RAP(uid);
   }).load(function(event){
		   	rtxEvent  = event || window.event ;
	   		$.data(document.body,"target",rtxEvent.srcElement);
	    	var uid = $(".RTX").attr("data-id");
			RAP(uid);
			rtx_isFirstLoad=true;
   });
});		*/