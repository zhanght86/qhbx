var SketchIO=function () {
	function e(a,b) {
		if(a||a==0)return a;
		return b
//	}var a="/renrenapp/save";
	}var a="/SY_COMM_MIND.addTuyaMind.do";
	var b="/renrenapp/getone";
	var c="/renrenapp/gallery";
	var d="x";
	return {
		//encode base64 img data
		saveSketch:function (b,c,d,e) {
			var f=Sketch.linesToJSON();
			var dat ="{\"SERV_ID\":\"SY_COMM_MIND\",\"FILE_NAME\":\"mind.png\"}";
			$.post(a,{
				save:f,title:b,publish_feed:c,upload_album:d,img_data:encodeURIComponent(e),data:dat
			},function (a,b) {
			//TODO validate upload result
				if(1 == 1) {
					alert("保存成功！");
//					window.returnValue = true;
//	                window.close();
   					 //调用父页面的刷新按钮
   					 window.opener.document.getElementById("mind-pen-input").value = a ;
   					 window.opener.document.getElementById("mind-pen-refresh").click();
  					 window.close();
				}else if(a.msg!=undefined) {
					alert(a.msg)
				}else {
					alert("未知错误")
				}
			})
		},loadSketch:function (a,c) {
			$.get(b,{
				id:a
			},function (a,b) {
				if(a.code==0) {
					var b="success";
					try{
						var d=SketchIO.parseSketch(a.data)
					}catch(e) {
						b="error";
						alert("加载数据出错.")
					}c(b,d)
				}else {
					alert("加载数据出错.")
				}
			})
		},parseSketch:function (a) {
			var b=e(a.p,a.perspective);
			var c=e(a.a,a.amplitude);
			var f=e(a.l,a.lines);
			for(var g=0;g<f.length;g++) {
				f[g].thickness=e(f[g].t,f[g].thickness);
				f[g].perspective=e(f[g].p,f[g].perspective);
				f[g].dashed=e(f[g].d,f[g].dashed);
				f[g].color=e(f[g].c,"#000000");
				f[g].alpha=e(f[g].a,1);
				f[g].points=e(e(f[g].l,f[g].points),[]);
				var h=f[g].points;
				for(var i=0;i<h.length;i++) {
					var j={
						position:{
							x:0,y:0
						},normal:{
							x:0,y:0
						}
					};
					if(typeofh[i]=="string") {
						j.position.x=parseFloat(h[i].slice(0,h[i].indexOf(d)));
						j.position.y=parseFloat(h[i].slice(h[i].indexOf(d)+1))
					}else {
						j.position.x=h[i].x;
						j.position.y=h[i].y
					}j.normal.x=j.position.x;
					j.normal.y=j.position.y;
					h[i]=j
				}
			}for(var g=0;g<f.length;g++) {
				var h=f[g].points;
				if(h.length<2) {
					f.splice(g,1);
					g--
				}
			}return {
				perspective:b,amplitude:c,lines:f
			}
		},loadGalleryItems:function (a,b,d,e) {
			$.get(c,{
				load:a,page:b
			},function (a,b) {
				var b="success";
				var c=1;
				var d=[];
				try{
					c=a.totalRows;
					for(var f=0;f<a.sketches.length;f++) {
						var g=SketchIO.parseSketch(a.sketches[f].value);
						g.id=a.sketches[f].id;
						g.title=a.sketches[f].title;
						g.date=a.sketches[f].date;
						g.views=a.sketches[f].views;
						g.author=a.sketches[f].author;
						g.accountid=a.sketches[f].accountid;
						d.push(g)
					}
				}catch(h) {
					b="error";
					alert("解析数据出错.")
				}e(b,d,c)
			})
		}
	}
}()
