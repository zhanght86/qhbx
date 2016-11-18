function RedHead(){

	this.myDocApp = null; //word App对象
	this.zwDoc = null;
	this.tempDoc = null;  //模板文件
	this.tempDocObj = null;
	this.downLoadFileName = null; //模板文件的下载路径
	this.zwDownloadURL = null;  //正文下载路径

	
	this.createWord=function(downUrl,TempDownloadURL,tempFileName){
		this.downLoadFileName = tempFileName;
		this.zwDownloadURL =  downUrl;
		try {
			myDocApp = new ActiveXObject("Word.Application");
			myDocApp.Application.Visible = false; //设置word打开后可见;
			myDocApp.DisplayAlerts = 0;
			tempDoc  = myDocApp.Documents.Open(TempDownloadURL,false,false,false);//打开word模板
			this.tempDocObj = tempDoc;
			
		} catch(exception) {
			alert("createWord:" + exception.message);
			return false;
		}
		
		return true;
	};
	
	
	//替换正文
	this.replaceText=function(){
		//downLoadFileName需要取值
		try{
			if(this.downLoadFileName.length > 0 ){
				this.replaceContent();
			}
		}catch(e){
			
		}
    };
	
	this.replaceContent = function(){
		try{
			myDocApp.DisplayAlerts = 0;
			tempDoc.Activate();
			tempDoc.Content.Select();
			myDocApp.selection.Find.ClearFormatting();
			myDocApp.selection.Find.Replacement.ClearFormatting();
			myDocApp.selection.Find.Text = "#text#";
			myDocApp.selection.Find.Execute();
			var templateFont;
			if(myDocApp.selection.Find.Found){  //如果能找到正文，则
				this.copyContent(tempDoc);
			}else{
				//var j;
				//var sharpName;
				//var shapeCount = tempDoc.Shapes.count;
			//	for(j = 0; j < shapeCount; j++){
				//	sharpName = tempDoc.Shapes(j).Name().substring(0,8);
				//	if(sharpName == "Text Box"){
						tempDoc.Shapes.SelectAll();
						myDocApp.selection.Find.ClearFormatting();
						myDocApp.selection.Find.Replacement.ClearFormatting();
						myDocApp.selection.Find.Text = "#text#";
						myDocApp.selection.Find.Execute();
						if(myDocApp.selection.Find.Found){
							this.copyContent(tempDoc);
						}
				//	}
			//	}
			}
			
			myDocApp.NormalTemplate.Saved = true;
			//zwDoc.Close(); 
			//wdDoNotSaveChanges;			
			//zwDoc = null;
		}catch(exception){
			myDocApp.NormalTemplate.Saved = false;
			alert("replaceContent:" + exception.message);
		}
	};
	
	this.copyContent = function(dstWordDoc){
		this.zwDoc = myDocApp.Documents.Open(this.zwDownloadURL,false,false,false);

		this.zwDoc.acceptAllRevisions();
		try{
			if(this.zwDoc.comments.count > 0) {
				// 删除批注
				this.zwDoc.DeleteAllComments();
			}
		} catch(exception) {
//			alert(exception.message);
		}

		tempDoc.Activate();
		this.zwDoc.Content.Select();		
		var templateFont;
		if(this.ifChangeFont){
			//templateFont = dstWordDoc.Application.selection.Font;
		}
//		this.zwDoc.Activate();
		if(this.ifChangeFont){
			//this.zwDoc.Application.selection.Font = templateFont;
		}
		this.zwDoc.Application.selection.Copy();
		dstWordDoc.Activate();
		while(true){
			var wdFormatOriginalFormatting = 16; //保留原文件格式
			dstWordDoc.Application.selection.PasteAndFormat(wdFormatOriginalFormatting);
			myDocApp.selection.Find.ClearFormatting();
			myDocApp.selection.Find.Replacement.ClearFormatting();
			myDocApp.selection.Find.Text = "#text#";
			myDocApp.selection.Find.Execute();
			if(!myDocApp.selection.Find.Found){
				break;
			}
		}
	};
	
	//替换marker
	this.repleaceMarker = function(source,dest){
		try{
			tempDoc.Content.Select();
			this.replaceSelect(source,dest);
			tempDoc.Shapes.SelectAll();
			this.replaceSelect(source,dest);
			var shapeCount = tempDoc.Shapes.count;
			for(var j = 1; j <= shapeCount; j++){
				var sharpName = tempDoc.Shapes.item(j).Name.substring(0,8);
				if(sharpName == "Text Box"){
					tempDoc.Shapes(j).Select();
					this.replaceSelect(source,dest);
				}
			}
		}catch(exception){
			alert("repleaceMarker:" + exception.message);
		}
	};
	
	this.repleaceBigValMarker = function(source, dest){
		var maxSize = 100;
		var count = Math.floor(dest.length/maxSize);
		if(count > 1){
			for(var i=0;i<=count;i++){
				var strDst = "";
				if(i==count){
					strDst = dest.substring(i * maxSize) ;
				}else{
					strDst = dest.substring(i * maxSize,(i+1) * maxSize) + source;
				}
				this.replaceSelect(source,strDst);				
			}
		}else{
			this.replaceSelect(source,dest);
		}
	}
	
	this.replaceSelect = function(source,dest){
		myDocApp.selection.Find.ClearFormatting();
		myDocApp.selection.Find.Replacement.ClearFormatting();
		myDocApp.selection.Find.Execute(source, false, false, false, false, false, true, 1, true, dest, 2);
	};
	
	this.clearResource = function(){
		try{
			if(this.zwDoc != null) {
				this.zwDoc.Close();
				this.zwDoc = null;
				
			}
		}catch(e){
			
		}
		
		try{
			if(tempDoc != null) {
				tempDoc.Close();
				tempDoc = null;
			}		
		}catch(e){
			
		}
		try{
			if(myDocApp != null){
				myDocApp.Quit();
				myDocApp = null;
			}
		}catch(e){
			
		}
		
		this.downLoadFileName = null;
		this.zwDownloadURL = null;
	}
}


 

