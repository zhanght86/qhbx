
function first(){
	if(currentPage==1){
		return false;
	}
	else{
		currentPage = 1;
	}
	doPage(currentPage);
}

function pre(){
	if(currentPage==1){
		return false;
	}
	else{
		currentPage = currentPage-1;
	}
	
	doPage(currentPage);
}

function next(){
	if(currentPage==pageNum){
		return false;
	}
	else{
		currentPage = currentPage-0+1;
	}
	
	doPage(currentPage);
}

function last(){
	if(currentPage==pageNum){
		return false;
	}
	else{
		currentPage = pageNum;
	}
	
	doPage(currentPage);
}

function doG0(){
	var go = jQuery('#query').val();
	if(go<=0||go>pageNum){
		return false;
	}	
	doPage(go);
}