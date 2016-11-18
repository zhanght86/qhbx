    /** 
	 * 上传文档
	 **/
    function upload(tmplId, siteId){
       // var url = "/wenku/tmpl/${upload_tmpl_id}.html?SITE_ID=${site_id}";
    	 var url = "/cms/SY_COMM_CMS_TMPL/" + tmplId + ".html?SITE_ID=" + siteId;
        var opts={'scrollFlag':true , 'url':url,'tTitle':"上传文档",'menuFlag':3};
        parent.Tab.open(opts);
      } 
    
    
    /**
     * 创建文辑
     * 
     */
    function createDoclist(){
    	//myDoclist();
    	var url="/cms/SY_COMM_CMS_TMPL/3alE7Vs2Jf2q10DBanQYjWtI.html";
    	var opts={'scrollFlag':true , 'url':url,'tTitle':"创建文辑",'menuFlag':3};
        parent.Tab.open(opts);
     } 
    
	/**
	 * 公告查看
	 **/
    function openNotice(tmplId, noticeId) {
    	var url = "/cms/SY_COMM_CMS_TMPL/" + tmplId + ".html?NOTICE_ID=" + noticeId;
    	  var opts={'scrollFlag':true , 'url':url,'tTitle':"文库公告",'menuFlag':3};
          parent.Tab.open(opts);
    }
	
    /** 
	 * 阅读文档
	 **/
    function view(docId, title){
        var url = "/cms/SY_COMM_WENKU_DOCUMENT/" + docId + ".html";
        var opts={'scrollFlag':true , 'url':url,'tTitle':title,'menuFlag':3};
        parent.Tab.open(opts);
      } 
    
    /** 
	 * 查看文辑
	 **/
    function viewDoclist(listId, title){
        var url = "/cms/SY_COMM_WENKU_DOCLIST/" + listId + ".html";
        var opts={'scrollFlag':true , 'url':url,'tTitle':title,'menuFlag':3};
        parent.Tab.open(opts);
      } 
    
	
    /** 
	 * 我的下载
	 **/
    function myDownload(){
    	var opts={'url':'SY_COMM_WENKU_MYDOWNLOAD.list.do','tTitle':'我的下载','menuFlag':3, 'menuId':'SY_COMM_WENKU_MYDOWNLOAD'};
        parent.Tab.open(opts);
      }
    
    /**
     * 我的浏览历史
     */
    function myReadHis() {
    	var opts={'url':'SY_COMM_WENKU_MYREAD.list.do','tTitle':'我的浏览','menuFlag':3, 'menuId':'SY_COMM_WENKU_MYREAD'};
        parent.Tab.open(opts);
    }
    
    /**
     *分类管理
     */
    function categoryManager(){
    	var opts={'url':'SY_COMM_WENKU_CHNL.list.do','tTitle':'分类管理','menuFlag':3};
    	parent.Tab.open(opts);
    /* 	var servId = "SY_COMM_WENKU_CHNL";
    	var readOnly = "false";
    	var params = "";
    	var url = FireFly.getContextPath() + "/sy/base/view/stdListView.jsp?sId=" + servId + "&readOnly=" + readOnly + "&params=" + params;
    	window.location.href= url; */
    }
    /**
     * 我的个人中心
     */
    function myWenkuCenter() {
    	var opts={'url':'/cms/SY_COMM_CMS_TMPL/0ScMtoIfp0CESGEVvmM5hd.html','tTitle':'我的个人中心','menuFlag':3,'menuId':'MY_WENKU_CENTER__ruaho',"scrollFlag":true};
        parent.Tab.open(opts); 
    }
    
    /**
     * 我的文档
     **/
     function myDocuments() {
        var opts={'url':'SY_COMM_WENKU_MYDOCUMENT.list.do','tTitle':'我的文档','menuFlag':3,'menuId':'SY_DOCUMENT_CENTER__zhbx'};
        parent.Tab.open(opts); 
    /* 	var servId = "SY_COMM_WENKU_MYDOCUMENT";
    	var readOnly = "false";
    	var params = "";
    	var url = FireFly.getContextPath() + "/sy/base/view/stdListView.jsp?sId=" + servId + "&readOnly=" + readOnly + "&params=" + params;
    	window.location.href= url; */
    }
     /**
      * 他人的文档
      * @param user 用户id
      * t:0显示文档 1显示文辑
      */
     function othersDocuments(tmplId,user){
    	 var url = "/cms/SY_COMM_CMS_TMPL/"+tmplId+".html?who="+user+"&t=0";
         var opts={'scrollFlag':true , 'url':url,'tTitle':'他在文库','menuFlag':3};
         parent.Tab.open(opts);
     }
     /**
      * 我的积分
      **/
    function myWenkuIntegral() {
    	var opts={'url':'SY_COMM_WENKU_MY_INTEGRAL.list.do','tTitle':'我的积分','menuFlag':3, 'menuId':'SY_COMM_WENKU_MY_INTEGRAL__ruaho'};
        parent.Tab.open(opts);
    	/* var servId = "SY_COMM_WENKU_MY_INTEGRAL";
    	var readOnly = "true";
    	var params = "";
    	var url = FireFly.getContextPath() + "/sy/base/view/stdListView.jsp?sId=" + servId + "&readOnly=" + readOnly + "&params=" + params;
    	window.location.href= url; */
    }
    /**
     * 我的文辑
     **/
    function myDoclist() {
    	var opts={'url':'SY_COMM_WENKU_MYDOCLIST.list.do','tTitle':'我的文辑','menuFlag':3, 'menuId':'SY_COMM_WENKU_MYDOCLIST'};
        parent.Tab.open(opts);
        
    /* 	var servId = "SY_COMM_WENKU_MYDOCLIST";
    	var readOnly = "false";
    	var params = "";
    	var url = FireFly.getContextPath() + "/sy/base/view/stdListView.jsp?sId=" + servId + "&readOnly=" + readOnly + "&params=" + params;
    	window.location.href= url; */
    }
 
    /**
     * 搜索
     * @param $this 即提交按钮
     **/
    function search($this) {
    	//获取form 区分顶部搜索和底部搜索
    	var $form=$($this).parent().parent("#searchForm"),
    		filterArray = [],
    	    filter = {};
		filter["id"] = "service";
		filter["data"] = "SY_COMM_WENKU_DOCUMENT";
		filterArray.push(filter);
		 
    	var keywords =$form.find("#kw").val();
    	var param = {};
    	param["KEYWORDS"] = keywords;
    	param["FILTER"] = filterArray;
    	$form.find("#data").val(jQuery.toJSON(param));
    	$form.submit();
    }
    
    
    /** 
	 * 向TA提问
	 * targetUser - 指定回答用户
	 **/
    function askQuestion(targetUser){
        var url = "/cms/tmpl/3O0F5DwFV5yoWFL5CwR_yb.html" ;
        if (targetUser) {
        	url += "?spec=" + targetUser;
        }
        var opts={'scrollFlag':true ,'tTitle':'知道提问' ,'url':url,'menuFlag':3};
        parent.Tab.open(opts);
      } 

	  function openZhidaoTab(){
	var tabLis = parent.jQuery("#homeTabs").find(".ui-state-default");
	//模拟点击事件
	for(var i=0; i<tabLis.length; i++){
		var pretabid= jQuery(tabLis[i]).attr("pretabid");
		if(pretabid && pretabid.indexOf("28bfWlGS13cpnlf1oEwixT") >= 0 ){
			jQuery(tabLis[i]).children("a").children("span").trigger("click");
			break;
		}
	}
}
	  