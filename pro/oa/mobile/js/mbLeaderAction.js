/** 领导日程 */
GLOBAL.namespace("mb.vi");
/*待解决问题：
 * 
 */
mb.vi.ldrAction = function(options) {
	var defaults = {
		"sId":""//服务ID
	};
	this.opts = $.extend(defaults,options);
	this.servId = this.opts.sId;
	this.model = 3;
};
/*
 * 渲染列表主方法
 */
mb.vi.ldrAction.prototype.show = function() {
	var _self = this;
    $.mobile.loading( "show", {
        text: "加载中……",
        textVisible: true,
        textonly: false 
    });
    _self._initMainData().then(function(){
        _self._layout();
        _self._render();
        _self._afterLoad();
    }).catch(function(err){
//        console.log(err);
    }).finally(function(){
        $.mobile.loading( "hide" );
    }); 
};

mb.vi.ldrAction.prototype._initMainData = function() {
	 var _self = this;
	 return _self._initRoleData();
};


/**
 * 初始化可查看角色数据
 */
mb.vi.ldrAction.prototype._initRoleData = function() {
	var _self = this; 
    //将角色数据进行缓存
    var cachedData = $.mobile.window.data("ROLE_");
    return Q(cachedData).then(function(data){
        if(data) {
            _self._roleData = data;
            return;
        } else {
       		//获取登录用户的user code
            var userCode = System.getUser("USER_CODE");
            //获取用户数据
            var param ={
                    "_SELECT_" : "*",
                    "_WHERE_" : " AND QUERY_ID in (select QUERY_ID  from BN_LDR_QUERY_USER where ROLE_CODE in(select ROLE_CODE from SY_ORG_ROLE_USER where USER_CODE like '%"+userCode+"%'))"
            };
            return FireFly.doAct(_self.servId, "finds", param).then(function(result){
                console.log("role result",result);
                if(result["_DATA_"].length > 0){
                    _self._roleData = result["_DATA_"];
                    $.mobile.window.data("ROLE_" , result["_DATA_"]);
                }  
            });
        }
    });
};


/*
 * 构建列表页面布局
 */
mb.vi.ldrAction.prototype._layout = function() {
	var _self = this;
	this.headerWrp = $("#ldrAction_header");
    this.contentWrp = $("#ldrAction_content");
    this.ldrActionGroupWrp = $("#ldrActionGroup");     //该角色的全部分组
};

/*
 * 绑定数据
 */
mb.vi.ldrAction.prototype._render = function() {
	var _self = this;
	this.headerWrp.find("h1").html("领导日程");
	//角色
    _self._renderRole();
};

/*
 * 绑定角色数据
 */
mb.vi.ldrAction.prototype._renderRole = function() {
	var _self = this;
	this.ldrActionGroupWrp.empty();     //该角色的人员分组
	//人员
    if (_self._roleData && _self._roleData.length > 0) {
    	//按角色排序
    	var queryRoleData = _self._roleData.slice(0);
    	var group={},lastGroup;
    	$.each( queryRoleData , function(i , obj) {
    		//添加分组
    		if (!group[obj["ROLE_CODE"]]) {
    			
    			if (lastGroup && lastGroup != obj["ROLE_CODE"]) {
    				$("#ldrActionGroup-" + lastGroup).data("list",group[lastGroup]);
    			}
    			lastGroup = obj["ROLE_CODE"];
    			group[obj["ROLE_CODE"]] = [];//obj["ROLE_NAME"];
    			//渲染collapsible
    			var tempDom = "<div data-role='collapsible' id='ldrActionGroup-" + obj["ROLE_CODE"] + "' ><h2>" + obj["ROLE_NAME"] + "</h2>" + 
    						 	"<ul data-role='listview' id='listview-" + obj["ROLE_CODE"] + "' data-icon='false' data-swipable='true' style='position:relative;'></ul>" + 
    						 "</div>";
    			_self.ldrActionGroupWrp.append(tempDom);
    			var roledept = obj["DEPT_CODE"];
    			var deptArray = new Array();
    			deptArray=roledept.split(",");
    			if(roledept==""){
    				//角色下选择部门——给当前角色获得人员数据
    				_self._initPersonData(obj["ROLE_CODE"],obj["DEPT_CODE"]);
    			}else{
    				//角色下没有部门——给当前角色获得人员人员
    				for(var i = 0; i<deptArray.length;i++){
    					_self._initPersonData(obj["ROLE_CODE"],deptArray[i]);
    				}
    			}
    			
    		} 
    		group[obj["ROLE_CODE"]].push(obj);
    	});
    	$("#ldrActionGroup-" + lastGroup).data("list",group[lastGroup]);
    } else {//TODO 数据错误处理
							avigator.notification.alert(
    								'您没有可查看领导日程权限！',  		// message
    								function(){
    									navigator.app.exitApp();
    								},'提示', '确定'                   
    						);
    }
};

/*
 * 获取，绑定人员数据
 */
mb.vi.ldrAction.prototype._initPersonData = function(roleCode,deptCode) {
	var _self = this; 
	var cachedData = $.mobile.window.data("LEADER_");
	return Q(cachedData).then(function(data){
    	if(data) {
			_self._leaderData = data;
			return;
        } else {
			var param={};
		    //将角色数据进行缓存
		    //该角色下是否选择可查看部门
			if(deptCode!=""){
				param ={
		        	"_SELECT_" : "USER_CODE,USER_NAME,USER_SORT,DEPT_CODE,DEPT_NAME,USER_LOGIN_NAME",
		            "_WHERE_" : " and USER_CODE in ( select user_code from  SY_ORG_ROLE_USER where  CMPY_CODE='zhbx' and ROLE_CODE ='"+ roleCode+ "' and DEPT_CODE ='"+deptCode+"')",
					"_ORDER_" : "DEPT_LEVEL, DEPT_SORT, USER_SORT"
		        	};
			}else{
			    param ={
			        "_SELECT_" : "USER_CODE,USER_NAME,USER_SORT,DEPT_CODE,DEPT_NAME,USER_LOGIN_NAME",
		            "_WHERE_" : " and USER_CODE in ( select user_code from  SY_ORG_ROLE_USER"
								+ " where  CMPY_CODE='zhbx' and ROLE_CODE ='"+ roleCode+ "')",
					"_ORDER_" : "DEPT_LEVEL, DEPT_SORT, USER_SORT"
		        };
			}
            //获取用户数据
      		return FireFly.doAct("SY_ORG_USER", "finds", param).then(function(result){
                console.log("leader result",result);
                if(result["_DATA_"].length > 0){
                    _self._leaderData = result["_DATA_"];
                    $.mobile.window.data("PERSON_" , result["_DATA_"]);
                    var resultDate = result["_DATA_"];
                    _self._renderPerson(roleCode,resultDate);
                }
            });
        }
	});
};


/*
 * 绑定人员数据
 */
mb.vi.ldrAction.prototype._renderPerson = function(roleCode,resultDate) {
	var _self = this; 
	var roleID = "#listview-"+roleCode;
	//按名称排序
    var leaderAllData = resultDate;//_self._leaderData.slice(0).sort(_self._orderBy("USER_SORT" , false));
    //每页20条数据
    var pageSize = 20;
    var totleSize = leaderAllData.length;
    var totlePage = parseInt(totleSize / pageSize,10) + 1;
    	
    //总页数
    this.ldrActionGroupWrp.data("totlepage" , totlePage);
    	
    //当前页数
    this.ldrActionGroupWrp.data("currpage" , 0);
    	
    var pager={};
    	
    $.each( leaderAllData , function(i , obj) {
    	//处于第几页
    	var pageIndex = parseInt(i/pageSize, 10) ;
    		if(!pager[pageIndex]){
    			pager[pageIndex] = [];
    		}
    		pager[pageIndex].push(obj);
    		if( i < 20 ){
	    		var $li = $("<li data-filtertext='"+ obj["USER_LOGIN_NAME"] +"'  id='li-" + obj["USER_CODE"] + "'></li>");
	    			$li.data("detail" , obj);
		   		var $a = $("<a></a>").attr("href","#").attr("class","ui-btn").appendTo($li);
		   		$("<h1></h1>").html(obj["USER_NAME"]+"("+obj["DEPT_NAME"]+")").appendTo($a);
	    		jQuery(roleID).append($li);
    		}
    	});
    	_self.ldrActionGroupWrp.data("ldrActionPager",pager);	
};



/**
* 排序方法
* @method orderBy   
* @param {String} type 根据指定属性排序
* @param {Boolean} desc 是否降序 true:降序，false:升序
* @return{Function} 匿名的compare排序方法，供Array.sort调用
*/
mb.vi.ldrAction.prototype._orderBy = function(type,desc){
	return function(v1,v2){
		if(desc){
			return v2[type] > v1[type] ? 1 : -1;
		}else{
			return v2[type] < v1[type] ? 1 : -1;
		}
	};
}


/*
 * 加载后执行
 */
mb.vi.ldrAction.prototype._afterLoad = function() {
	$.mobile.pageContainer.pagecontainer( "change","#ldrAction",{transition:"slide"});
};


 