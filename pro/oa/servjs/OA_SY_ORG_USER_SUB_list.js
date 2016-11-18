var _viewer = this;
_viewer.getBtn("upUserPassword").unbind("click").bind("click", function(event) {
	
	  var pkArray = _viewer.grid.getSelectPKCodes();
  	if (jQuery.isEmptyObject(pkArray)) {
  		_viewer.listBarTipError("请选择相应记录！");
  	} else {
  		 var res = confirm("您确定要重置密码吗？");
  		 if (res == true) {
  			 debugger;
  			var temp = new rh.ui.popPrompt({
  				title:"请输入初始密码",
  				tip:"密码格式为：数字，字母",
  				okFunc:function() {
  					var pwd = temp.obj.val();
  					if (pwd == "" || pwd == null) {
  						alert("初始密码为空！");
  					} else {
  						var strs = pkArray.join(",");
  			    		var param = {};
  			    		param['userIds']=strs;
  			    		param['pwd'] = pwd;
  			    		var resultData = FireFly.doAct(_viewer.servId, "upUserPwd", param, true);
  			    		var tip = resultData[UIConst.RTN_MSG];
  				        if (tip.indexOf(UIConst.RTN_OK) == 0) {//成功则刷新
  				            temp.closePrompt();
  							_viewer.refresh();	
  					    }
  					}
  				}
  			});
  			temp.render(event);
  		 } else {
  		 	return false;
  		 }
  	}
});