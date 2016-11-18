jQuery(document).ready(function(){
	
	 //文档贡献---文辑贡献   切换
	  jQuery(".uc-tabs-control a").on("click",function(){
			 jQuery(this).siblings().removeClass("current").addClass("disabled");
			 jQuery(this).addClass("current").removeClass("disabled");
			 var curr=jQuery(this).attr("href");
			 jQuery(curr).siblings(".content").hide();
			 jQuery(curr).show();
			 if(jQuery(this).index()){//文辑贡献
			 	 jQuery("#myContribution-more").on("click",myDoclist);
			 }else{
			 	  jQuery("#myContribution-more").on("click",myDocuments);
			 }
			 return false;
	 });
	//设置头像和名称  
	var imgSrc = parent.System.getVar("@USER_IMG@");
	$("#currentUsImg").attr("src",imgSrc);
	$("#currentUsName").text(parent.System.getVar("@USER_NAME@"));  
	  
	  
	  /* 我的文库信息 */
	var result = parent.FireFly.doAct("SY_COMM_WENKU_DOCUMENT","getMyInfo",{"DOCUMENT_STATUS":2},false,false);
		var userIntegral = result["USER_INTEGRAL"],
		    pubDocs = result["PUB_DOC_COUNT"],
		    doclistCount = result["DOCLIST_COUNT"],
		    readHisArray = result["READ_HIS"],
		    myDocArray = result["MY_DOC_LIST"];
	    //积分
	    jQuery(".wealth a").text(userIntegral).bind("click", function() {
	    	myWenkuIntegral();
	    });
	    //公共文档数量
	    jQuery(".public a").text(pubDocs).bind("click", function() {
	    	myDocuments();
	    });
	    //文辑数量
	     jQuery(".doc a").text(doclistCount).bind("click", function() {
	    	myDoclist();
	    });
	 
	    //我的贡献--文档贡献
	    jQuery.each(myDocArray,function(index,obj) {
			jQuery("<div class='gd-g gd-g-list'>" +
		        		"<div class='gd-g-u gd-u-title'>"+
		            		"<a href='#' title='"+obj.DOCUMENT_TITLE+"' target='_self'>"+
		                		Format.limit(25, obj.DOCUMENT_TITLE) +
		           			"</a>"+
		        		"</div>"+
		        		"<div class='gd-g-u wp10'>"+obj.DOCUMENT_READ_COUNTER+"</div>"+
		        		"<div class='gd-g-u wp10'><div class='div2' title='文档被评为"+obj.DOCUMENT_AVG_SCORE+"分'><span style='width:"+obj.DOCUMENT_AVG_SCORE*20+"%;'></span></div></div>"+
		        		"<div class='gd-g-u wp10 tc'>"+obj.DOCUMENT_STATUS__NAME+"</div>"+
		        		"<div class='gd-g-u wp15'>"+obj.S_MTIME.split(" ")[0] +"</div>"+
		   		  "</div>"
		   		  ).appendTo(jQuery("#doc-list"))
		   		   .find("a").on("click",function(){
		   		   		view(obj.DOCUMENT_ID, obj.DOCUMENT_TITLE);
		   		   });
		});
		 //我的贡献--文辑贡献
	    var page = {'_PAGE_':{'SHOWNUM':5},'_NOPAGE_':'true'};
		var myAlbum = parent.FireFly.getPageData("SY_COMM_WENKU_MYDOCLIST",page);
		if(myAlbum._DATA_.length>0){
		    jQuery.each(myAlbum._DATA_,function(index,obj) {
				jQuery("<div class='gd-g gd-g-list'>" +
			        		"<div class='gd-g-u gd-u-title'>"+
			            		"<a href='#' title='"+obj.LIST_TITLE+"' target='_self'>"+
			                		Format.limit(25,  obj.LIST_TITLE) +
			           			"</a>"+
			        		"</div>"+
			        		"<div class='gd-g-u wp10'>"+obj.LIST_READ_COUNTER+"</div>"+
			        		"<div class='gd-g-u wp10'><div class='div2' title='文辑被评为"+obj.LIST_SCORE_AVG+"分'><span style='width:"+obj.LIST_SCORE_AVG*20+"%;'></span></div></div>"+
			        		"<div class='gd-g-u wp10 tc'>"+obj.LIST_STATUS__NAME+"</div>"+
			        		"<div class='gd-g-u wp15'>"+obj.S_MTIME.split(" ")[0] +"</div>"+
			   		  "</div>"
			   		  ).appendTo(jQuery("#doclist-list"))
			   		   .find("a").on("click",function(){
			   			viewDoclist(obj.LIST_ID, obj.LIST_TITLE);
			   		   });
			});
		}else{
			jQuery("<div class='ph20 pl10 c-gray6'>无相关记录！</div>").appendTo(jQuery("#myContribution-album"));
		}
		/**
		*	我下载的文档
		*/
		var myDownloadDoc = parent.FireFly.doAct("SY_COMM_WENKU_MYDOWNLOAD","query",{"SHOWNUM":5},false,false);
		jQuery.each(myDownloadDoc._DATA_,function(index,obj) {
			jQuery("<div class='gd-g gd-g-list'>" +
		        		"<div class='gd-g-u gd-u-title'>"+
		            		"<a href='#' title='"+obj.DATA_DIS_NAME+"' target='_self'>"+
		                		Format.limit(25, obj.DATA_DIS_NAME) + 
		           			"</a>"+
		        		"</div>"+
		        		"<div class='gd-g-u wp10'>"+obj.DOCUMENT_DOWNLOAD_COUNTER +"</div>"+
		        		"<div class='gd-g-u wp10'><div class='div2' title='文档被评为"+obj.DOCUMENT_AVG_SCORE+"分'><span style='width:"+obj.DOCUMENT_AVG_SCORE*20+"%;'></span></div></div>"+
		        		"<div class='gd-g-u wp10 tc'>"+obj.DATA_OWNER__NAME +"</div>"+
		        		"<div class='gd-g-u wp15'>"+obj.S_MTIME.split(" ")[0] +"</div>"+
		        	"</div>"
		   		  ).appendTo(jQuery("#myDownload"))
		   		   .find("a").on("click",function(){
						view(obj.DATA_ID, obj.DATA_DIS_NAME);
		   		 });
		});
	    //浏览历史
		jQuery.each(readHisArray,function(index,obj) {
			jQuery("<div class='gd-g gd-g-list'>" +
		        		"<div class='gd-g-u gd-u-title'>"+
		            		"<a href='#' title='"+obj.DATA_DIS_NAME+"' target='_self'>"+
		                		Format.limit(25, obj.DATA_DIS_NAME) +
		           			"</a>"+
		        		"</div>"+
		        		"<div class='gd-g-u wp10'>"+obj.DOCUMENT_READ_COUNTER +"</div>"+
		        		"<div class='gd-g-u wp10'><div class='div2' title='文档被评为"+obj.DOCUMENT_AVG_SCORE+"分'><span style='width:"+obj.DOCUMENT_AVG_SCORE*20+"%;'></span></div></div>"+
		        		"<div class='gd-g-u wp10 tc'>"+obj.DATA_OWNER__NAME +"</div>"+
		        		"<div class='gd-g-u wp15'>"+obj.S_MTIME.split(" ")[0] +"</div>"+
		        	"</div>"
		   		  ).appendTo(jQuery("#recentlyRead"))
		   		   .find("a").on("click",function(){
						view(obj.DATA_ID, obj.DATA_DIS_NAME);
		   		 });
		});
});