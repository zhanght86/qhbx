function PageMask() {
    this.shield = document.createElement("DIV"); 
	this.alertFram = document.createElement("DIV"); 

	this.init = function(){
		this.shield.style.position = "absolute"; 
		this.shield.style.left = "0px"; 
		this.shield.style.top = "0px"; 
		this.shield.style.width = "0px"; 
		this.shield.style.height = "0px"; 
		this.shield.style.background = "#333"; 
		this.shield.style.textAlign = "center"; 
		this.shield.style.zIndex = "1"; 
		this.shield.style.filter = "alpha(opacity=50)";
		
		this.alertFram.style.position = "absolute"; 
		this.alertFram.style.left = "50%"; 
		this.alertFram.style.top = "-300px"; 
		this.alertFram.style.width = "60%"; 
		this.alertFram.style.height = "80px"; 
		this.alertFram.style.background = "#ccc"; 
		this.alertFram.style.textAlign = "center"; 
		this.alertFram.style.zIndex = "80"; 
		var strHtml  = "<ul style=\"list-style:none;margin:0px;padding:0px;width:100%\">"; 
		
		strHtml += "<li  style='background:#fff;height:80px;border:1px solid #888888;' >"
				 + "<div id='li_msg_box' "
				 + "style='text-align:center;font-weight:bold;line-height:20px;"
				 + "margin-top:30px;margin-right:5px;margin-bottom:30px;margin-left:5px;'>"
			     + "</div></li>"; 
		
		strHtml += "</ul>"; 
		this.alertFram.innerHTML = strHtml; 
		document.body.appendChild(this.alertFram); 
		document.body.appendChild(this.shield);
        this.alertFram.style.display = "none"; 
	};
	
	this.show = function(){
		this.selectInputDisp(0);  
		this.shield.style.display="block";
		this.resizeMask();
		this.alertFram.style.display="block";
	};
	
	this.close = function(){
		this.selectInputDisp(1);
		this.shield.style.width = "0px";
		this.shield.style.height = "0px";
		this.alertFram.style.top = "-300px";
		this.alertFram.style.display = "none"; 
		this.alertFram.style.display = "none"; 
	};
	
	this.resizeMask = function(){
	
		this.shield.style.width = "100%";
		this.shield.style.height = "100%";
		var aleft=(document.body.offsetWidth - this.alertFram.offsetWidth)/2 + document.body.scrollLeft;     
        var atop=(document.body.offsetHeight  - this.alertFram.offsetHeight)/2 + document.body.scrollTop;  
		this.alertFram.style.left = aleft/2 - 30;
		this.alertFram.style.top = atop + 40;
	};
	
	this.showMessage = function(msg){
		li_msg_box.innerHTML = msg;
	};
	
	this.selectInputDisp=function(val){
		var dType=["hidden","visible"];
		var obj=document.getElementsByTagName("select");
		for (i=0;i<obj.length;i++){
			obj[i].style.visibility=dType[val];
		}
	};

}
var pageMask ;

function initPageMask(){
	if(!pageMask){
		pageMask = new PageMask();
		pageMask.init();
	}
}

function showMask(){
	initPageMask();
	pageMask.show();
}

function closeMask(){
	initPageMask();
	pageMask.close();
}

function showMessage(msg){
	initPageMask();
	pageMask.showMessage(msg + "……");
}

function showErrorMessage(msg){
	initPageMask();
	pageMask.showMessage("<font color='#FF0000'>" + msg + "……</font>");
}