//定义一个方法，将所有的userImg统一格式
var formatUserImg = function(userCode,url,id,clazz,width,height,others){
	
	var imgStr = "";
	imgStr += "<img";
	imgStr += ' onmouseover="zhidaoUserInfo(event,\''+userCode+'\');"';
	//imgStr += "userCode = '"+userCode+"'";
	if(id != null && id != ''){
		imgStr += "id='"+id+"'";
	}
	if(clazz != null && clazz != ''){
		var clazzes = clazz.split(" ");
		imgStr += "class='";
		jQuery(clazzes).each(function(index,item){
			imgStr += item+" ";
		});
		imgStr += "'";
	}
	if(width != null && width != ''){
		//imgStr += "width='"+width+"'";
	}
	if(height != null && height != ''){
		//imgStr += "height='"+height+"'";
	}
	if(others != null && others != ''){
		imgStr += " "+others+" ";
	}
	if (url != undefined && url.indexOf("?") > 0){
		url = url.split("?")[0];
		url += "?size="+width.replace("px","")+"x"+height.replace("px","")+"&t="+(new Date()).getTime();
	} else {
		url = "/sy/theme/default/images/common/rh-male-icon.png";
		imgStr += "style='width:auto;height:"+height+";'";
	}
	imgStr += "src='"+url+"' />";
	
	//alert(imgStr);
	return imgStr;
}


var formatImgHieWid = function(obj) {
	var oldWid = $(obj).attr("width");
	var oldHei = $(obj).attr("height");
	var src = "";
	if (oldWid != undefined && oldWid != '' && oldHei != undefined && oldHei != '') {
		oldWid = oldWid.replace("px","");
		oldHei = oldHei.replace("px","");
		src = oldSrc.split("?")[0] + "?size=" + oldWid + "x" + oldHei + "&t=" + (new Date()).getTime();
	} else {
		src = oldSrc.split("?")[0] + "?t=" + (new Date()).getTime();
		oldWid = "100";
		oldHei = "100";
	}
	var param = ImgUtils.getConstrainedSize(obj,{width:oldWid,height:oldHei});
	$(who).attr(param);
}


/**
 * "知道"的rh.vi.userInfo方法
 */
var zhidaoUserInfo = function(event,userCode){
	
//	var loginUser = parent.System.getVar("@USER_CODE@");
//	var btns = "";
//	
//	//如果名片是当前用户本人，不显示求助和关注
//	if(loginUser == userCode){
//		btns = "";
//	}else{
//		var flag = parent.FireFly.doAct("SY_COMM_ZHIDAO","isMyFollowPerson",{"USER_CODE":userCode},false)["flag"];
//		//已关注此人
//		if(flag == 'true'){
//			btns = [{"id":userCode+"-help","name":"向Ta求助","icon":"rh-icon-img btn-msg","func":
//				function(){
//					var userName = parent.jQuery("#user-info-name").find("span").text();
//					loadTiWen(userName,userCode);
//					jQuery("html, body").animate({scrollTop:0},300);
//				}
//			},{"id":userCode+"-follow","name":"取消关注","icon":"rh-icon-img btn-msg zd-btn-unfollow","func":
//				function(){
//					if($(this).hasClass("zd-btn-unfollow")){
//						jQuery(this).attr("title","关注");
//						$(this).removeClass("zd-btn-unfollow").find("font").text("关注");
//						//取消关注
//						var data={
//								"DATA_ID":userCode
//						};
//						parent.FireFly.doAct("SY_COMM_ZHIDAO", "deletePersonFollow", data,false,false);
//					}else{
//						jQuery(this).attr("title","取消关注");
//						$(this).addClass("zd-btn-unfollow").find("font").text("取消关注");
//						//关注
//						var data= {
//								"DATA_ID":userCode
//						};
//						parent.FireFly.doAct("SY_COMM_ZHIDAO", "addPersonFollow", data,false,false);
//					}
//				}
//			}];
//		}else{ //未关注此人
//			btns = [{"id":userCode+"-help","name":"向Ta求助","icon":"rh-icon-img btn-msg","func":
//				function(){
//					var userName = parent.jQuery("#user-info-name").find("span").text();
//					loadTiWen(userName,userCode);
//					jQuery("html, body").animate({scrollTop:0},300);
//				}
//			},{"id":userCode+"-follow","name":"关注","icon":"rh-icon-img btn-msg","func":
//				function(){
//					if($(this).hasClass("zd-btn-unfollow")){
//						jQuery(this).attr("title","关注");
//						$(this).removeClass("zd-btn-unfollow").find("font").text("关注");
//						//取消关注
//						var data={
//								"DATA_ID":userCode
//						};
//						parent.FireFly.doAct("SY_COMM_ZHIDAO", "deletePersonFollow", data,false,false);
//					}else{
//						jQuery(this).attr("title","取消关注");
//						$(this).addClass("zd-btn-unfollow").find("font").text("取消关注");
//						//关注
//						var data= {
//								"DATA_ID":userCode
//						};
//						parent.FireFly.doAct("SY_COMM_ZHIDAO", "addPersonFollow", data,false,false);
//					}
//				}
//			}];
//		}
//	}
//	
//	new parent.rh.vi.userInfo(event,userCode,{"btns":btns});
}