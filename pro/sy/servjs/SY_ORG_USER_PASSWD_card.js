var _viewer = this;
//获取新密码文本框的对象
var newPwdObj = _viewer.getItem("USER_PASSWORD").obj;
_viewer.getItem("USER_PASSWORD").obj.parent().css({"float":"left"});
jQuery("<ul class='sy-org-user-passwd-level'><li id='little'>弱</li><li id='middle'>中</li>" +
	"<li id='big'>强</li></ul>").insertAfter(newPwdObj.parent());

var complexRate = 0;
var matchTimesVal = 0;
//密码验证次数判断
function pwdComplexRate(newPswd){
	//1只是数字验证
	var regNum = /\d/;
    var resultNum = regNum.test(newPswd);
	//1只是小写字母验证
	var regLWord = /[a-z]/;
	var resultLWord = regLWord.test(newPswd);
	//1只是大写字母验证
	var regHWord = /[A-Z]/;
	var resultHWord = regHWord.test(newPswd);
	//1只是特殊字符验证
	var regChar = /[-`=\\\[\];',./~!@#$%^&*()_+|{}:"<>?]/;
	var resultChar = regChar.test(newPswd);
	
	var matchTimes = 0;
	if(resultNum){
		matchTimes++;
	}
	if(resultLWord){
		matchTimes++;
	}
	if(resultHWord){
		matchTimes++;
	}
	if(resultChar){
		matchTimes++;
	}
	
	return matchTimes;
}

newPwdObj.keyup(function(){
	var newPswd = _viewer.itemValue("USER_PASSWORD"); //新密码
	var matchTimes = pwdComplexRate(newPswd);

	complexRate = 0;
	
	var passwdLen = newPswd.length;
	
	//如果密码长度大于等于8 且 有3中及其以上的字符则是复杂级别
	if(passwdLen >= 8 && matchTimes >= 3){
		complexRate = 3;
	}else if(passwdLen >= 12 && matchTimes >= 2){
		complexRate = 3;
	} else if(passwdLen >= 6 && matchTimes >= 2){
		//如果密码长度大于等于6 ，且有2中及其以上字符
		complexRate = 2;
	}else{
		complexRate = 1;
	}
	
    jQuery("#little,#middle,#big").css({
        "height": "1px",
        "color": "#CCC",
        "background-color": "white"
    })	
		
	//使用一种字符
    if (complexRate == 1) {
        jQuery("#little").css({
            "height": "1px",
            "color": "black",
            "background-color": "red"
        });
    } else if (complexRate == 2) {
		//使用了2种字符
        jQuery("#little,#middle").css({
            "height": "1px",
            "background-color": "orange"
        });
        jQuery("#middle").css({
            "color": "black"
        });
     } else if(complexRate == 3) {
	 	//使用了多种字符
        jQuery("#little,#middle,#big").css({
            "height": "1px",
            "color": "#CCC",
            "background-color": "green"
        })
        jQuery("#big").css({
            "color": "black"
        })
     }
});

//确认密码框修改时触发事件
var conFlag=0;
var conPwdObj = _viewer.getItem("CONFRIM_PASSWORD").obj;
//获取密码框的容器对象 --@author hdy 2013-4-18 15:32:51
var pwdContainer = _viewer.getItem("CONFRIM_PASSWORD").getContainer();
//---
conPwdObj.keyup(function(){
	var newPswd = _viewer.itemValue("USER_PASSWORD"); //新密码
	var conPswd = _viewer.itemValue("CONFRIM_PASSWORD"); //确认密码
	//判断新密码是否等于确认密码
	if(newPswd != conPswd){
		if(conFlag==0){
			jQuery("#conPswd").remove();
			jQuery("<div id='conPswd' style='float:left;position:relative' class='rh-icon-img btn-delete'></div>").appendTo(pwdContainer.find(".right"));
			jQuery("#conPswd").css({
				"margin": "5px 5px"
			})
		}
		conFlag=1;
	}else {
		jQuery("#conPswd").remove();
		jQuery("<div id='conPswd' style='float:left;position:relative' class='rh-icon-img btn-ok'></div>").appendTo(pwdContainer.find(".right"));
		jQuery("#conPswd").css({
			"margin": "5px 5px"
		})
		conFlag=0;
    }
});

//密码验证次数判断
function pwdComplexRateTimes(){
	var newPswdVal = _viewer.itemValue("USER_PASSWORD");
	//1只是数字验证
	var regNumVal = /\d/;
    var resultNumVal = regNumVal.test(newPswdVal);
	//1只是小写字母验证
	var regLWordVal = /[a-z]/;
	var resultLWordVal = regLWordVal.test(newPswdVal);
	//1只是大写字母验证
	var regHWordVal = /[A-Z]/;
	var resultHWordVal = regHWordVal.test(newPswdVal);
	//1只是特殊字符验证
	var regCharVal = /[-`=\\\[\];',./~!@#$%^&*()_+|{}:"<>?]/;
	
	var resultCharVal = regCharVal.test(newPswdVal);
	matchTimesVal = 0;
	if(resultNumVal){
		matchTimesVal += 1;
	}
	if(resultLWordVal){
		matchTimesVal += 1;
	}
	if(resultHWordVal){
		matchTimesVal += 1;
	}
	if(resultCharVal){
		matchTimesVal += 1;
	}
	return newPswdVal.length;
}
//单击保存按钮触发事件
_viewer.getBtn("saveInfo").bind("click", function(){
    var oldPswd = _viewer.itemValue("OLD_PASSWORD"); //旧密码
	var newPswd = _viewer.itemValue("USER_PASSWORD"); //新密码
	var conPswd = _viewer.itemValue("CONFRIM_PASSWORD"); //确认密码	
    if (jQuery.isEmptyObject(_viewer.getChangeData())) {
        _viewer.cardBarTipError("没有修改数据，未做提交！");
        return false;
    }else { // 有修改才做校验
        if (!_viewer.form.validate()) {
            _viewer.cardBarTipError("校验未通过");
            return false;
        }
    }
	//如果修改了密码则验证确认密码  如果没有修改密码则也能保存其他修改项
    if (newPswd != "" && conPswd != "") {
		if(oldPswd == ""){
			_viewer.cardBarTipError("请填写旧密码！");
			return;
		}
		
        if (newPswd != conPswd) {
            _viewer.cardBarTipError("密码输入不一致！");
			return;
        }
    }else if(newPswd != "" || conPswd != ""){
		_viewer.cardBarTipError("密码输入不一致！");
		return;
	}else if(oldPswd != ""){
		_viewer.cardBarTipError("请填写新密码！");
		return;
	}
	
	//如果密码默认级别
	var passwordComplexRate = System.getVar("@C_SY_ORG_USER_PASSWORD_COMPLEXRATE@") || "0";
	var passwordComplexRateVal = parseInt(passwordComplexRate);
	var fullCharactersLength = (_viewer.itemValue("USER_PASSWORD").match(/[^\x00-\xff]/ig) || "" ).length; //全角字符
	//如果存在全角字符，则不让修改
	if (fullCharactersLength > 0) {
		_viewer.cardBarTipError("请不要输入全角字符！");
		return;
	//密码返回值大于0，即满足级别要求
	} else if (passwordComplexRateVal == 1) {
		if (pwdComplexRateTimes() < 6) {
			_viewer.cardBarTipError("密码长度不小于6，且密码中必须包括数字，小写字母，大写字母和特殊字符中的任意两种");
			return;
		} else {
			if (matchTimesVal < 2) {
				_viewer.cardBarTipError("密码中必须包括数字，小写字母，大写字母和特殊字符中的任意两种");
				return;
			}
		}
	} else if (passwordComplexRateVal == 2) {
		if (pwdComplexRateTimes() < 8) {
			_viewer.cardBarTipError("密码长度不小于8，且密码中必须包括数字，小写字母，大写字母和特殊字符中的任意三种");
			return;
		} else {
			if (matchTimesVal < 3) {
				_viewer.cardBarTipError("密码中必须包括数字，小写字母，大写字母和特殊字符中的任意三种");
				return;
			}
		}
	} else  if (passwordComplexRateVal == 3) {
		if (pwdComplexRateTimes() < 10) {
			_viewer.cardBarTipError("密码长度不小于10，且密码中必须包括数字，小写字母，大写字母和特殊字符");
			return;
		} else {
			if (matchTimesVal < 4) {
				_viewer.cardBarTipError("密码中必须包括数字，小写字母，大写字母和特殊字符");
				return;
			}
		}
	} else  if (passwordComplexRateVal > 3) {
		_viewer.cardBarTipError("密码级别设置超过最大级别");
		return;
	}
	var param = jQuery.extend({}, _viewer.getChangeDataAndPK());
	FireFly.doAct(_viewer.servId, "saveInfo", param, true);	
});

//function passWord(oldPassWord){
	FireFly.doAct(_viewer.servId, "getUserPassword","{}",false,false,function(data){
		var pwdStr = data.password;
		if("yes"==pwdStr){
			_viewer.getItem("OLD_PASSWORD").setValue("*********");
		}
	});	
//	return oldPassWord;
//}

