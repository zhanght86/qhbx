// JavaScript Document 文库-公告-阅读
jQuery(document).ready(function(e) {
	   //添加当前选中样式
	   var curr=jQuery("#"+currId).addClass('current');
	   //为li 添加last样式  用于最后li显示完全
	   jQuery("li:last").addClass("last");
	   
	   //进入页面时浏览量+1 TODO 数据库+1
	   jQuery("#"+currId).find(".view-count").html(parseInt(jQuery("#"+currId).attr("data"))+1);
	   
	   
	  
	   var param={
	   		noticeId:currId,
	   		imgSrc:curr.find("img").attr("src")
	   };
	   loadNotice(param);
	   
	   //左侧导航click
       jQuery("#wk-gg-aside-nav a").on("click",function(){
       		 handler(jQuery(this));
	   });
	   function handler($a){
			if($a.hasClass('current')){//如果处于选中状态
       			return false;
       		}
			$a.parent().siblings().find("a").removeClass('current'); 
			$a.addClass('current');
			var newParam={
				noticeId:$a.attr("id"),
				imgSrc:$a.find("img").attr("src")
			};
			var view_count=$a.find(".view-count");
   		 		view_count.html(parseInt(view_count.html(),10)+1);
   		 		
			changeParam(newParam);
			//加载公告内容
			loadNotice(param);
		}
	    //加载公告内容
 		function loadNotice(param){
 			var obj=FireFly.byId("SY_COMM_WENKU_NOTICE",param.noticeId);
 				//加载内容并对图片进行处理
				jQuery(".artical-body-content").html(obj.NOTICE_CONTENT)
											   .find("img").each(function(){
												   adjustImgSize(jQuery(this),694);
											   });
				//设置标题
				jQuery("#wk-gg-main h1").html(obj.NOTICE_TITLE);
				//设置作者名称
				jQuery(".author-name").html(obj.S_UNAME);
				//作者头像
				jQuery(".author-img").attr("src",param.imgSrc);
				//创建时间
				jQuery(".artical-body-foot").html(obj.S_ATIME.split(" ")[0]);
				//浏览量
				jQuery(".js-read-count").html(jQuery("#"+param.noticeId).find(".view-count").html());
				//浏览人数
				jQuery(".js-read-person-count").html(obj.NOTICE_READ_PERSON_COUNTER);
			//阅读量+1
			FireFly.doAct("SY_COMM_WENKU_NOTICE","increaseReadCounter",{"_PK_":param.noticeId},false,false);
 		}
 		function changeParam(obj){
 			for(var s in obj){
 				param[s]=obj[s];
 			}
 		}
 		function calc(o){//计算所有li高度
 			var h=0;
			jQuery.each(o,function(){
				h+=jQuery(this).innerHeight();
			});
			return h;
 		}
 		 
        var pager={
            	showNum		:10,
            	currPage    :2,
            	allNum		:0,
            	hasMore		:true
 		};
        //实现无刷新无限加载
 		$("#wk-gg-aside-nav ul").scroll(function(){
		   // return false;
 			
		    var self=jQuery(this),
		    	$ul_height=self.height(), 
		    	offsetToTop=self.scrollTop(),
		    	$li_height=calc(self.find("li")),
		        $li_len=self.find("li").length;
		    if($ul_height + offsetToTop == $li_height){//如果垂直滚动条已到底部
		    	
		    	/*if(!self.find("li:last").hasClass("last")){
		    		return false;
		    	}*/
		    	
		    	//显示正在加载中
		    	if(jQuery(this).find(".loading").length>0){
		    		return false;
		    	}
			   	jQuery("<li class='loading'>正在加载内容</li>").appendTo(jQuery(this));
		    	//加载数据
		     	var queryParam={
		     			showNum:pager.showNum,
		     			currPage:pager.currPage
		     	};
		     	self.find("li.last").removeClass();	  
		     	
				var obj=FireFly.doAct("SY_COMM_WENKU_NOTICE","getNoticeList",queryParam,false,false,function(rawData){
	    			//删除正在加载中
	    			if(rawData._DATA_.length > 0){//如果有数据
	    				
		    				var data=rawData._DATA_;
		    				
							pager.allNum=rawData._PAGE_.ALLNUM;
							pager.currPage=parseInt(rawData._PAGE_.NOWPAGE,10)+1;	
		    				
		    				jQuery.each(data,function(index,notice){
		    					var $li=jQuery("<li>"+
	            						"<a href='#' id='"+notice.NOTICE_ID+"' data='"+notice.NOTICE_READ_COUNTER+"'>"+
	                						"<h2>"+notice.NOTICE_TITLE+"</h2>"+
	                						"<p>"+
	                							"<img src='"+notice.S_USER__IMG+"' alt='"+notice.S_USER__NAME+"'/>"+
	                							"<strong>"+notice.S_USER__NAME+"</strong><strong>("+notice.DEPT_NAME+")</strong>"+
	                						"</p>"+
	                    					"<span class='view-count'>"+notice.NOTICE_READ_COUNTER+"</span>"+
							              " </a>"+
							            "</li>").appendTo(self);
						           $li.bind("click",function(){
						           			handler(jQuery(this).find("a"));
						           });
		    				});
		    				
	    			    }//end of if
	    			 self.find(".loading").remove();
	    			});
	 
		    	self.find("li:last").addClass("last");	
		    }
		});//---------end of scroll
 		
 		
 		//调整图片大小，如果超过最大宽度，等比例缩放
 		function adjustImgSize($img,maxWidth){
 			//原始图像大小
 			var width,
 				height,
 				originWidth=$img.width(),
 				originHeight=$img.height();
 			if(originWidth > maxWidth){ 
 				//如果原始图像大小  大于 maxWidth
 			    width=maxWidth;
 			    height=( originHeight * maxWidth ) / originWidth;
 			    $img.css({"width":width,"height":height});
 			} 
 			
 		}//---- end of adjustImgSize
    });