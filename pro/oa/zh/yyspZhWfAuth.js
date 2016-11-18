function initYyWfAuth(yyCard){
	yyCard.yyAuth = {
		"uploadActFlag": false, //上传用印材料
		"delPriFileActFlag": false, //删除用印材料
		"readPriFileActFlag": true, //查看用印材料
		"modPubFileActFlag": false, //添加/删除公共文档
		"readPubFileActFlag": true, //查看公共文档
		"modPriPrintNumActFlag": false, //修改用印材料打印份数
		"modPubPrintNumActFlag": false, //修改公共文档打印份数
		"sealPriFileActFlag": false, //用印材料盖章
		"sealPubFileActFlag": false, //公共文档盖章
		"cancelSealPriFileActFlag": false, //用印材料取消盖章
		"cancelSealPubFileActFlag": false, //公共文档取消盖章
		"printPriFileActFlag": false, //打印用印材料
		"printPubFileActFlag": false,  //打印公共文档
		"appendPrintActFlag": false //追加打印份数
	};
}

function getYyWfAuth(yyCard){
	//工作流
	if(yyCard.wfCard){
		var wfCard = yyCard.wfCard;
		if(wfCard.getCustomVarContent("YYUPLOAD") == "true"){
			yyCard.yyAuth.uploadActFlag = true;
		}
		if(wfCard.getCustomVarContent("YYDELPRI") == "true"){
			yyCard.yyAuth.delPriFileActFlag = true;
		}
		if(wfCard.getCustomVarContent("YYREADPRI") == "false"){
			yyCard.yyAuth.readPriFileActFlag = false;
		}
		if(wfCard.getCustomVarContent("YYMODPUB") == "true"){
			yyCard.yyAuth.modPubFileActFlag = true;
		}
		if(wfCard.getCustomVarContent("YYREADPUB") == "false"){
			yyCard.yyAuth.readPubFileActFlag = false;
		}
		if(wfCard.getCustomVarContent("YYMODPRINUM") == "true"){
			yyCard.yyAuth.modPriPrintNumActFlag = true;
		}
		if(wfCard.getCustomVarContent("YYMODPUBNUM") == "true"){
			yyCard.yyAuth.modPubPrintNumActFlag = true;
		}
		if(wfCard.getCustomVarContent("YYSEALPRI") == "true"){
			yyCard.yyAuth.sealPriFileActFlag = true;
		}
		if(wfCard.getCustomVarContent("YYSEALPUB") == "true"){
			yyCard.yyAuth.sealPubFileActFlag = true;
		}	
		if(wfCard.getCustomVarContent("YYUNSEALPRI") == "true"){
			yyCard.yyAuth.cancelSealPriFileActFlag = true;
		}
		if(wfCard.getCustomVarContent("YYUNSEALPUB") == "true"){
			yyCard.yyAuth.cancelSealPubFileActFlag = true;
		}
		if(wfCard.getCustomVarContent("YYPRINTPRI") == "true"){
			yyCard.yyAuth.printPriFileActFlag = true;
		}
		if(wfCard.getCustomVarContent("YYPRINTPUB") == "true"){
			yyCard.yyAuth.printPubFileActFlag = true;
		}
		if(wfCard.getCustomVarContent("YYADDFILE") == "true"){
			yyCard.yyAuth.uploadActFlag = true;
			yyCard.yyAuth.modPubFileActFlag = true;
		}	
		if(wfCard.getCustomVarContent("YYREADFILE") == "false"){
			yyCard.yyAuth.readPriFileActFlag = false;
			yyCard.yyAuth.readPubFileActFlag = false;
		}
		if(wfCard.getCustomVarContent("YYDELFILE") == "true"){
			yyCard.yyAuth.delPriFileActFlag = true;
			yyCard.yyAuth.modPubFileActFlag = true;
		}
		if(wfCard.getCustomVarContent("YYMODFILE") == "true"){
			yyCard.yyAuth.uploadActFlag = true;
			yyCard.yyAuth.delPriFileActFlag = true;
			yyCard.yyAuth.modPubFileActFlag = true;
		}
		if(wfCard.getCustomVarContent("YYMODNUM") == "true"){
			yyCard.yyAuth.modPriPrintNumActFlag = true;
			yyCard.yyAuth.modPubPrintNumActFlag = true;
		}
		if(wfCard.getCustomVarContent("YYSEAL") == "true"){
			yyCard.yyAuth.sealPriFileActFlag = true;
			yyCard.yyAuth.sealPubFileActFlag = true;
		}	
		if(wfCard.getCustomVarContent("YYUNSEAL") == "true"){
			yyCard.yyAuth.cancelSealPriFileActFlag = true;
			yyCard.yyAuth.cancelSealPubFileActFlag = true;
		}
		if(wfCard.getCustomVarContent("YYPRINT") == "true"){
			yyCard.yyAuth.printPriFileActFlag = true;
			yyCard.yyAuth.printPubFileActFlag = true;
		}
		if(wfCard.getCustomVarContent("YYFILE") == "true"){
			yyCard.yyAuth.uploadActFlag = true;
			yyCard.yyAuth.delPriFileActFlag = true;
			yyCard.yyAuth.modPubFileActFlag = true;
			yyCard.yyAuth.modPriPrintNumActFlag = true;
			yyCard.yyAuth.modPubPrintNumActFlag = true;
		}
		if(wfCard.getCustomVarContent("YYSEALUNSEAL") == "true" && _getCustomAuth(yyCard, "YYSEALUNSEAL")){
			yyCard.yyAuth.sealPriFileActFlag = true;
			yyCard.yyAuth.sealPubFileActFlag = true;
			yyCard.yyAuth.cancelSealPriFileActFlag = true;
			yyCard.yyAuth.cancelSealPubFileActFlag = true;
		}else if(wfCard.getCustomVarContent("YYSEALUNSEAL") == "true" && !_getCustomAuth(yyCard, "YYSEALUNSEAL")){
			yyCard.yyAuth.sealPriFileActFlag = true;
			yyCard.yyAuth.cancelSealPriFileActFlag = true;
		}
	}
	
	//一般
	if(_getCustomAuth(yyCard, "YYPRINT")){
		yyCard.yyAuth.printPriFileActFlag = true;
		yyCard.yyAuth.printPubFileActFlag = true;
	}	
	if(_getCustomAuth(yyCard, "YYAPPENDPRINT")){
		yyCard.yyAuth.appendPrintActFlag = true;
	}	
}

function _getCustomAuth(yyCard, key){
	var auth = yyCard.getByIdData("customAuth");
	if(auth){
		var flag = auth[key];
		if(flag && flag=="true"){
			return true;
		}
	}
	return false;
}