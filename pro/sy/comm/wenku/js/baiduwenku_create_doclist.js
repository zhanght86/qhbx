jQuery(document).ready(function(){
	 //生成list_id
	 var newId=parent.FireFly.byId("SY_COMM_WENKU_DOCLIST",null).LIST_ID;
	 
	 //标题验证
	 jQuery("#list-title").bind("blur",function(){ 
	 	 if(!jQuery(this).val()){
	 	 	jQuery(this).addClass("border-warning").next().html("请输入标题");
	 	 }else{
	 	    if(jQuery(this).hasClass("border-warning")){
	 	 		jQuery(this).removeClass("border-warning").next().html("");
	 	    }
	 	 }
	 });
	//选择部门
		jQuery("#dept-btn").unbind("click").bind("click", function(event) {
			if(jQuery("#publish_dept").attr("disabled")){//如果已提交，不允许再选择
				return false;
			}
			var _this = jQuery(this);
			 
			//1.构造树形选择参数
			var configStr = 'SY_ORG_DEPT,{"TYPE":"single"}';
			var extendTreeSetting = "{'rhexpand':false,'expandLevel':2,'cascadecheck':false,'checkParent':false,'childOnly':true}";
			var options = {
				"config" :configStr,
				"extendDicSetting":StrToJson(extendTreeSetting),//非必须参数，一般用不到
				"replaceCallBack":function(idArray,nameArray){//回调，idArray为选中记录的相应字段的数组集合
					var param = {};
					param["DEPT_CODES"] = idArray.join(",");
					param["DEPT_NAMES"] = nameArray.join(",");
					_this.html(param["DEPT_NAMES"]);
					jQuery("#publish_dept").val(param["DEPT_CODES"]);
				}
			};
			//2.显示树形
			var dictView = new parent.rh.vi.rhDictTreeView(options);			
			dictView.show(event);	
			//调整弹出框样式，避免透明背景（引入的系统page.css common.css样式冲突）
			var box = parent.jQuery("#SY_ORG_DEPT-dictDialog").parent();
			box.css({"z-index":"9999","background":"white","top":"20%","left":"40%","position":"absolute"});
		});	
	//选择文辑分类
	jQuery("#cate-btn").unbind("click").bind("click", function(event) {
		if(jQuery("#list_chnl").attr("disabled")){//如果已提交，不允许再选择
			return false;
		}
		var _this = jQuery(this);
		 
		//1.构造树形选择参数
		var configStr = 'SY_COMM_WENKU_CHNL_MANAGE,{"TYPE":"single","EXTWHERE":" and SITE_ID=\'SY_COMM_CMS\'"}';
		var extendTreeSetting = "{'rhexpand':false,'expandLevel':2,'cascadecheck':false,'checkParent':false,'childOnly':true}";
		var options = {
			"config" :configStr,
			"extendDicSetting":StrToJson(extendTreeSetting),//非必须参数，一般用不到
			"replaceCallBack":function(idArray,nameArray){//回调，idArray为选中记录的相应字段的数组集合
				var param = {};
				param["CHANNEL_ID"] = idArray.join(",");
				param["CHANNEL_NAME"] = nameArray.join(",");
				_this.html(param["CHANNEL_NAME"]);
				jQuery("#list_chnl").val(param["CHANNEL_ID"]);
				//显示添加文档
				jQuery(".doc-add-more").show("slow");
			}
		};
		//2.显示树形
		var dictView = new parent.rh.vi.rhDictTreeView(options);			
		dictView.show(event);	
		//调整弹出框样式，避免透明背景（引入的系统page.css common.css样式冲突）
		var box = parent.jQuery("#SY_COMM_WENKU_CHNL_MANAGE-dictDialog").parent();
		box.css({"z-index":"9999","background":"white","top":"20%","left":"40%","position":"absolute"});
	});			
	
    
	//添加文档
	jQuery("#add-more").unbind("click").click(function(event){
		//0.获取文辑类型
		var list_chnl=jQuery("#list_chnl").val();
		//1.构造查询选择参数
		var configStr = "SY_COMM_WENKU_DOCUMENT,{'SOURCE':'DOCUMENT_ID~DOCUMENT_TITLE~DOCUMENT_FILE_SIZE','TYPE':'multi'}";
		var options = {
			"title":"相关文档",
			"config" :configStr,
			"parHandler":this,
		    "replaceCallBack":function(rawData) {
		    		//回调
		    		callBack(rawData);
		    		if(jQuery("#document-part").is(":hidden")){
		    			jQuery("#document-part").show("slow");
		    			jQuery(this).html("继续添加");
		    		}
			}
		};
		//3.用系统的查询选择组件 rh.vi.rhSelectListView()
		var queryView = new parent.rh.vi.rhSelectListView(options);
		parent.chnlName=jQuery("#cate-btn").html();
		queryView.show(event);
	
	});    
	//回调,用于添加文档
	function callBack(rawData){
		var doc_ids=rawData.DOCUMENT_ID.split(","),//文档id
		 	doc_titles=rawData.DOCUMENT_TITLE.split(","),//文档名称
		 	doc_size=rawData.DOCUMENT_FILE_SIZE.split(",");//文档大小
		if(doc_ids.length>0){
			jQuery.each(doc_ids,function(index,obj) {
				var $record=jQuery("<div class='doc-list'>"+
			                "<div class='doc-common doc-title'>"+
			                    "<a href='javasript:void(0);' id='"+obj+"' title='"+doc_titles[index]+"' target='_self' class='doc-info'>"+
			                    	Format.limit(15,doc_titles[index])   +                    
			                    "</a>"+
			                "</div>"+
			                "<div class='doc-common doc-size'>"+
			                    	Format.formatSize(1048576,2,doc_size[index])+
			                "</div>"+
			                "<div class='doc-common doc-opt'>"+
			                    "<a class='del-btn'>删除 </a>"+
			                "</div>"+
				       "</div>").appendTo(jQuery(".list-doc-content"));
				
				$record.find(".doc-info").bind("click",function(){
				    	view(jQuery(this).attr("id"),jQuery(this).attr("title"));
				});
			    $record.find(".del-btn").bind("click",function(){
			    	if(confirm("您确定要删除吗?")){
					    	//删除成功后，淡出--移除
					    	jQuery(this).parent().parent().fadeOut("fast",function(){
								jQuery(this).remove();
							});
					}
				});//end fo del-btn
			    
			  
			 });//end of each
		}else{
			alert("数据已存在！");
		}
	}//end of callback		
			 
		//提交
	    jQuery("#submit-btn").bind("click",saveNotice);	 
	    //保存文辑
		function saveNotice(){
			var list_title=jQuery("#list-title").val(),
				list_chnl=jQuery("#list_chnl").val(),
				publish_dept=jQuery("#publish_dept").val();		    	
	    	if(!list_title){//标题验证
	    	    jQuery("#list-title").addClass("border-warning");
	    	    jQuery("#submit-tip").html("请输入标题").addClass("color-warning").fadeOut(3000,function(){
	    	    	 jQuery(this).removeClass("color-warning").html("").show();
	    	    });
	    	    return false;
	    	}
	    	if(!list_chnl){//分类验证
	    	    jQuery("#submit-tip").html("请点击选择分类").css("color","red").fadeOut(1000,function(){
	    	    	 jQuery(this).removeClass("color-warning").html("").show();
	    	    });
	    	    return false;
	    	}
	    	
	    	/*if(!publish_dept){//发布部门 默认为本人所在部门
	    		publish_dept=parent.System.getVar("@DEPT_CODE@");
	    	}*/
	    	
			var param = {
				"LIST_ID"			: newId,						//id
				"LIST_TITLE"		: list_title,				   //标题
				"LIST_DESCRIPTION"	: jQuery("#describe").val(),  //介绍
				"LIST_CHNL"			: list_chnl,			     //分类
				"LIST_KEYWORDS"		: jQuery("#keyword").val(), //关键词
				"PUBLISH_DEPT"		: publish_dept			   //发布部门
			};
		 
			//保存
			var result = parent.FireFly.doAct("SY_COMM_WENKU_DOCLIST","save",param,false,false,function(data){
							if (data[UIConst.RTN_MSG].indexOf(UIConst.RTN_OK) == 0) {
								//成功后保存文档
//					    		 alert("提交成功");
					    		//设置输入框disabled
							    jQuery("#doclist-form input,textarea").prop("disabled", true);
//					    		//获取文辑ID
//					    		jQuery("#doclist-pk").data("LIST_ID","");
//							    jQuery("#doclist-pk").data("LIST_ID",data.LIST_ID);
								saveNoticeItem();
					   		} 
			});//end of result
		} //end of saveNotice
		
		//保存文辑项  SY_COMM_WENKU_DOCLIST_ITEM
		function saveNoticeItem(){
			var batchData = {},
		    tempArray = [];
			jQuery(".doc-info").each(function() {
				   var temp ={};
					   temp.DOCUMENT_ID=jQuery(this).attr("id");
					   temp.DOCUMENT_TITLE=jQuery(this).attr("title");
					   temp.LIST_ID=newId;
					   tempArray.push(temp);
			 });
			batchData["BATCHDATAS"] = tempArray;
		
		//批量保存
		var resultData=parent.FireFly.batchSave("SY_COMM_WENKU_DOCLIST_ITEM",batchData,null,null,false);
		//如果保存成功
		if(resultData[UIConst.RTN_MSG].indexOf(UIConst.RTN_OK) == 0){
			//设置输入框disabled
			if(!jQuery("#doclist-form input,textarea").prop("disabled")){
				jQuery("#doclist-form input,textarea").prop("disabled", true);
			}
			if(confirm("您的文辑创建成功！是否继续创建？")){
				window.location.href="/cms/SY_COMM_CMS_TMPL/0WXXRGwDx6MVKrJzwPklfyz.html";
			}else{
				window.location.href="/cms/SY_COMM_WENKU_DOCLIST/"+newId+"/index_1.html";
			}
		}
	  }//end of saveNoticeItem
	
	});