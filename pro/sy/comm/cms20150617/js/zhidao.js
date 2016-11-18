	/**
	 * 公告查看
	 **/
    function openNotice(tmplId, noticeId) {
    	var url = "/cms/SY_COMM_CMS_TMPL/" + tmplId + ".html?NOTICE_ID=" + noticeId;
    	  var opts={'scrollFlag':true , 'url':url,'tTitle':"知道公告",'menuFlag':3};
          parent.Tab.open(opts);
    }
    
    
    /**
     *显示、收起 知道提问评论 
     */
    function bindAskComment(dataId, btnId, commentDivId) {
    	var commentServ = "SY_COMM_ZHIDAO_QUESTION_COMMENT";
    	var servId = "SY_COMM_ZHIDAO_QUESTION";
    	_bindComment(commentServ, servId, dataId, btnId, commentDivId);
    }

    /**
     *显示、收起 知道回答评论 
     */
    function bindAnswerComment(dataId, btnId, commentDivId) {
    	var commentServ = "SY_COMM_ZHIDAO_ANSWER_COMMENT";
    	var servId = "SY_COMM_ZHIDAO_ANSWER";
    	_bindComment(commentServ, servId, dataId, btnId, commentDivId);
    }
    
    /**
     * 显示、收起 评论框
     */
    function _bindComment(commentServ,servId, dataId, btnId, commentDivId) {
		var cmtBtn = jQuery("#" + btnId);
		var commentDiv = jQuery("#" + commentDivId);
		commentDiv.attr("style","display: none;"); 
		var opts = {
				"COMMENT_SERV" : commentServ,
				"DATA_ID" : dataId,
				"SERV_ID" : servId,
				"SHOWNUM" : 10,
				"NOWPAGE" : 1,
				"pCon" : commentDiv
			};
		var listView = new rh.vi.zhidao_comment(opts);
		cmtBtn.unbind("click").bind("click", function() {
			if (false == listView.isShow()) {
				commentDiv.attr("style","display: block;"); 
			    //布局初始化
			    listView.layout();
			    //展示数据
				listView.show();
			} else {
				commentDiv.attr("style","display: none;"); 
				listView.remove();
			}
		}); 
    }
	
    /** 
	 * 查看知道
	 **/
    function view(id, title){
		parent.FireFly.doAct("SY_COMM_ZHIDAO_QUESTION","increaseReadCounter",{"Q_ID":id});
      //  var url = "/cms/tmpl/26b1CEGLB23FJNpZaDFFHW.html?id=" + id ;
		var url = "/cms/SY_COMM_ZHIDAO_QUESTION/" + id + ".html" ;
        var opts={'scrollFlag':true , 'url':url,'tTitle':title,'menuFlag':3};
        parent.Tab.open(opts);
      }
    /**
     * 针对回答：阅读全部内容
     * @param id ： answer id
     * @param title
     * @param hash 锚点
     */
	function viewAnswer(id, title, aId){
		//此处hash=id
		var url = "/cms/SY_COMM_ZHIDAO_QUESTION/" + id + ".html#"+aId ;
        var opts={'scrollFlag':true , 'url':url,'tTitle':title,'menuFlag':3};
        parent.Tab.open(opts);
	}
    /** 
	 * 知道导航
	 **/
    function zhidaoNavigation(urlId,tTitle){
        var url = "/cms/tmpl/"+ urlId +".html" ;
        var opts={'scrollFlag':true ,'tTitle': tTitle,'url':url,'menuFlag':4};
        parent.Tab.open(opts);
      } 
    /** 
	 * 知道提问
	 * targetUser - 指定回答用户
	 **/
    function askQuestion(targetUser){
        var url = "/cms/SY_COMM_CMS_TMPL/3O0F5DwFV5yoWFL5CwR_yb.html" ;
        if (targetUser) {
        	url += "?spec=" + targetUser;
        }
        var opts={'scrollFlag':true ,'tTitle':'知道提问' ,'url':url,'menuFlag':3};
        parent.Tab.open(opts);
      } 
	  
    /** 
	 * 知道更多专家
	 **/
    function moreSpecialist(){
        var url = "/cms/tmpl/1T4m9Ehul1QrkF85KV19wl.html" ;
        var opts={'scrollFlag':true ,'tTitle':'知道专家' ,'url':url,'menuFlag':3};
        parent.Tab.open(opts);
      } 
    
    /**
     * 他人的知道
     * @param user 用户id
     * t:0显示文档 1显示文辑
     */
    function othersZhidao(tmplId,user){
    	var data=parent.FireFly.byId("SY_ORG_USER",user);
   	 	var url = "/cms/SY_COMM_CMS_TMPL/"+tmplId+".html?who="+user;
        var opts={'scrollFlag':true , 'url':url,'tTitle':data.USER_NAME+'的知道首页','menuFlag':3};
        parent.Tab.open(opts);
    }
    /**
     * 我的提问
     **/
    function openMyAsk() {
        var opts={'url':'SY_COMM_ZHIDAO_MYASK.list.do','tTitle':'我的提问','menuFlag':2, 'menuId':'SY_COMM_ZHIDAO_MYASK__ruaho'};
        parent.Tab.open(opts); 
    }
    
    /**
     * 我的回答
     **/
    function openAnswer() {
        var opts={'url':'SY_COMM_ZHIDAO_MYANSWER.list.do','tTitle':'我的回答','menuFlag':2, 'menuId':'SY_COMM_ZHIDAO_MYANSWER__ruaho'};
        parent.Tab.open(opts); 
    }
    
    /**
     * 侧栏--我的关注
     **/
    function openFollow(tmplId,user) {
        var opts={'url':'SY_COMM_ZHIDAO_MYFOLLOW.list.do','tTitle':'我的关注','menuFlag':2, 'menuId':'SY_COMM_ZHIDAO_MYFLLOW__ruaho'};
        parent.Tab.open(opts); 
    }
    /**
     * 导航--我的关注
     **/
    function openMyFollow() {
        var opts={'url':'SY_COMM_ZHIDAO_MYFOLLOW.list.do','tTitle':'我的关注','menuFlag':2, 'menuId':'SY_COMM_ZHIDAO_MYFLLOW__ruaho'};
        parent.Tab.open(opts); 
    }
    
    /**
     *分类管理
     */
    function categoryManager(){
    	var opts={'url':'SY_COMM_ZHIDAO_CHNL.list.do','tTitle':'分类管理','menuFlag':2, 'menuId':'SY_COMM_ZHIDAO_CHNL__ruaho'};
    	parent.Tab.open(opts);
    }

     /**
      * 我的积分
      **/
    function myZhidaoIntegral() {
    	var opts={'url':'SY_COMM_ZHIDAO_MY_INTEGRAL.list.do','tTitle':'我的积分','menuFlag':2, 'menuId':'SY_COMM_ZHIDAO_MY_INTEGRAL__ruaho'};
        parent.Tab.open(opts);
    }
    /**
     * 我的个人中心-知道
     */
    function myZhiDaoCenter(tmplId) {
    	var url = "/cms/SY_COMM_CMS_TMPL/"+tmplId+".html?who="+parent.System.getVar("@USER_CODE@");
//            var opts={'scrollFlag':true , 'url':url,'tTitle':'他的知道首页','menuFlag':3};
//            parent.Tab.open(opts);
            
    	var opts={'url':url,'tTitle':'我的个人中心','menuFlag':2,'menuId':'MY_ZHIDAO_CENTER__ruaho',"scrollFlag":true};
        parent.Tab.open(opts); 
    }
    /**
     * 知道分类
     */
    function viewCategory(chnlId) {
    	var opts={'url':'/cms/SY_COMM_CMS_CHNL/'+chnlId+'/index_1.html','tTitle':'知道分类','menuFlag':3,"scrollFlag":true};
        parent.Tab.open(opts); 
    }
    /**
     * 搜索
     **/
    function search() {
    	var filterArray = [];
    	var filter = {};
		filter["id"] = "service";
		filter["data"] = "SY_COMM_ZHIDAO_QUESTION";
		filterArray.push(filter);
    	
    	var keywords = $("#kw").val();
    	var param = {};
    	param["KEYWORDS"] = keywords;
    	param["FILTER"] = filterArray;
    	document.getElementById("data").value = jQuery.toJSON(param);
    	document.getElementById("searchForm").submit();
    }