var Sketch=function () {
	function bm(a,b) {
		var c=a.x-b.x;
		var d=a.y-b.y;
		return Math.sqrt(c*c+d*d)
	}function bl(a,b) {
		if(a>b) {
			return -b+a%b
		}if(a<-b) {
			return b+a%b
		}return a
	}function bk(a) {
		if(a.length!=7||a.charAt(0)!="#")return [0,0,0];
		var b=a.substring(1,3);
		var c=a.substring(3,5);
		var d=a.substring(5,7);
		b=parseInt(b,16);
		c=parseInt(c,16);
		d=parseInt(d,16);
		return [b,c,d]
	}function bj(a,b,c) {
		H.lineCap="round";
		H.lineJoin="round";
		H.lineWidth=a;
		H.strokeStyle="rgba("+b[0]+","+b[1]+","+b[2]+","+c+")";
		H.stroke()
	}function bi() {
		a=true;
		var b=s.length;
		for(var c=0;c<b;c++) {
			var d=s[c];
			var e=d.thickness;
			var f=bl((C-d.perspective)*2,2);
			var g=d.dashed;
			var h=bk(d.color);
			var i=d.alpha;
			var l=d.points;
			var m=l[0];
			var n=l[1];
			if(!g) {
				H.beginPath()
			}for(var p=1,q=l.length;p<q;p++) {
				if(g) {
					H.beginPath()
				}if(bm(m.position,m.normal)<1) {
					m.position.x+=(Math.random()-.5)*z;
					m.position.y+=(Math.random()-.5)*z
				}m.position.x+=(m.normal.x-m.position.x)*A;
				m.position.y+=(m.normal.y-m.position.y)*A;
				var t=m.position.x;
				var u=n.position.x;
				t+=f*(m.position.x-o.width*.5)*(f<0?1:-1);
				u+=f*(n.position.x-o.width*.5)*(f<0?1:-1);
				if(p==1||g) {
					H.moveTo(t,m.position.y)
				}H.quadraticCurveTo(t,m.position.y,t+(u-t)/2,m.position.y+(n.position.y-m.position.y)/2);
				m=l[p];
				n=l[p+1];
				if(g) {
					bj((e*1).toFixed(2),h,i)
				}
			}if(!g) {
				bj(e,h,i)
			}
		}for(var p=0;p<r.length;p++) {
			var v=r[p];
			var f=bl((C-v.perspective)*2,2);
			v.position.x+=v.velocity.x;
			v.position.y+=v.velocity.y;
			var w=v.position.x;
			w+=f*(v.position.x-o.width*.5)*(f<0?1:-1);
			v.alpha*=.94;
			H.fillStyle="rgba(0,0,0,"+v.alpha+")";
			H.fillRect(w,v.position.y,1,1);
			if(v.alpha<.05) {
				r.splice(p,1);
				p--
			}
		}if(E>0) {
			H.save();
			H.globalAlpha=Math.max(E,0);
			H.beginPath();
			H.scale(1,.5);
			var x=C*Math.PI-Math.PI/2;
			for(var p=0;p<2;p++) {
				var y=p==0?k:0;
				H.moveTo(o.width/2,o.height*1.7+2);
				H.arc(o.width/2,o.height*1.7+y,j,x-.2,x+.2,true);
				H.closePath();
				H.lineWidth=4;
				H.strokeStyle="rgb(165,106,70)";
				H.stroke();
				H.fillStyle="rgba(240,150,105,0.85)";
				H.fill();
				H.beginPath()
			}H.restore();
			E-=.05
		}a=false
	}function bh() {
		var a=999;
		var b=s[s.length-1];
		if(b) {
			var c=b.points[b.points.length-1];
			if(c) {
				a=bm(I,c.position)
			}
		}if(v) {
			C+=(x-C)*.08;
			if(y==x&&Math.abs(C-x)<.003&&w) {
				C=y;
				O()
			}
		}C=bl(C+D,1);
		D*=.8;
		if(J.spaceDown||J.leftDown||J.rightDown) {
			E=1
		}if(I.down&&J.spaceDown) {
			D+=I.diff.x/o.width
		}else if(J.leftDown&&!J.rightDown) {
			D-=.005
		}else if(J.rightDown&&!J.leftDown) {
			D+=.005
		}else if(I.down&&Math.abs(a)>l) {
			function d() {
				b.points.push({
					position:{
						x:I.x,y:I.y
					},normal:{
						x:I.x,y:I.y
					}
				})
			}d();
			if(b.points.length<2) {
				d()
			}u++;
			bf(I.x,I.y,5,C)
		}I.diff.x=0;
		I.diff.y=0
	}function bg() {
		H.clearRect(0,0,G.width,G.height);
		bh();
		bi();
		requestAnimFrame(bg)
	}function bf(a,b,c,d) {
		var e=Math.round(Math.random()*c);
		while(e--) {
			r.push({
				position:{
					x:a,y:b
				},velocity:{
					x:.5*Math.random()-.25,y:3*Math.random()
				},alpha:.98,perspective:d
			})
		}
	}function be() {
		q.width=Math.max(window.innerWidth,c);
		q.height=Math.max(window.innerHeight,d);
		o.width=f;
		o.height=g;
		G.width=o.width;
		G.height=o.height;
		var a=Math.round((q.width-o.width)*.5);
		var b=Math.round((q.height-o.height)*.5)-e;
		G.style.position="relative";
		G.style.left=a+"px";
		G.style.top=b+"px";
		L.options.style.position="relative";
		L.options.style.width=o.width+"px";
		L.options.style.left=a+"px";
		L.options.style.top=b-16+"px"
	}function bd(a) {
		if(!a) {
			a=window.event
		}if(u>4) {
			var b="Your current drawing will be lost.";
			a.cancelBubble=true;
			a.returnValue=b;
			if(a.stopPropagation) {
				a.stopPropagation();
				a.preventDefault()
			}return b
		}
	}function bc(a) {
		I.press.x=I.x;
		I.press.y=I.y;
		if(a.target==G) {
			if(v) {
				O(true)
			}B=sizeDropdown.getValue()||3;
			s.push({
				thickness:B,perspective:C,dashed:K,color:colorDropdown.getValue(),alpha:alphaDropdown.getValue(),points:[]
			});
			a.preventDefault()
		}
	}function bb(a) {
		I.down=false
	}function ba(a) {
		if(a.touches.length==1) {
			a.preventDefault();
			I.prev.x=I.x;
			I.prev.y=I.y;
			I.x=a.touches[0].pageX-(window.innerWidth-o.width)*.5;
			I.y=a.touches[0].pageY-(window.innerHeight-o.height)*.5;
			I.diff.x=I.x-I.prev.x;
			I.diff.y=I.y-I.prev.y
		}
	}function _(a) {
		if(a.touches.length==1) {
			I.down=true;
			I.x=a.touches[0].pageX-(window.innerWidth-o.width)*.5;
			I.y=a.touches[0].pageY-(window.innerHeight-o.height)*.5;
			bc(a);
			a.preventDefault()
		}
	}function Z() {
		return {
			x:document.body.scrollLeft||window.pageXOffset,y:(document.body.scrollTop||window.pageYOffset)-e+2
		}
	}function Y(a) {
		I.down=false;
		I.x=a.clientX-(q.width-o.width)*.5+Z().x;
		I.y=a.clientY-(q.height-o.height)*.5+Z().y
	}function X(a) {
		I.down=true;
		I.x=a.clientX-(q.width-o.width)*.5+Z().x;
		I.y=a.clientY-(q.height-o.height)*.5+Z().y;
		bc(a)
	}function W(a) {
		I.prev.x=I.x;
		I.prev.y=I.y;
		I.x=a.clientX-(q.width-o.width)*.5+Z().x;
		I.y=a.clientY-(q.height-o.height)*.5+Z().y;
		I.diff.x=I.x-I.prev.x;
		I.diff.y=I.y-I.prev.y
	}function V(a) {
		if($("#save_box").css("display")=="none") {
			switch(a.keyCode) {
				case 32:J.spaceDown=false;
				a.preventDefault();
				break;
				case 37:J.leftDown=false;
				a.preventDefault();
				break;
				case 39:J.rightDown=false;
				a.preventDefault();
				break
			}
		}
	}function U(a) {
		s.pop()
	}function T(a) {
		if($("#save_box").css("display")=="none") {
			switch(a.keyCode) {
				case 32:J.spaceDown=true;
				a.preventDefault();
				break;
				case 37:J.leftDown=true;
				a.preventDefault();
				break;
				case 39:J.rightDown=true;
				a.preventDefault();
				break;
				case 90:if(a.ctrlKey||a.metaKey) {
					s.pop()
				}break
			}
		}if(v) {
			O()
		}
	}function S(a) {
		K=L.dashToggle.getAttribute("class")=="switch";
		L.dashToggle.setAttribute("class",K?"switch on":"switch");
		L.dashToggleState.innerHTML=K?"ON":"OFF";
		a.preventDefault()
	}function R(a) {
		if(confirm("重置将清空画板上所有内容，是否继续？")==true) {
			t=[];
			s=[];
			r=[];
			document.location.hash="";
			u=0;
			a.preventDefault()
		}
	}function Q(c) {
		var d=$("#tuya_title").val();
		if(d.length==0) {
			$("#title_div").addClass("error");
			$("#title_help").show();
			return false
		}var e;
		while(a)e=G.toDataURL();
		e=G.toDataURL();
		var f=$("#publish_feed").attr("checked")?1:0;
		var g=$("#upload_to_album").attr("checked")?1:0;
		b.saveSketch(d,f,g,e);
		$("#post_button").attr("disabled",true);
		$("#post_button").text("正在提交...");
		return false
	}function P(a) {
		a.preventDefault();
		if(s.length==0) {
			alert("你需要画点东西才能保存.");
			return
		}$("#post_button").attr("disabled",false);
		$("#post_button").text("保存并发布");
		$("#save_box").show(500)
	}function O() {
		N(true)
	}function N(a) {
		var b=0;
		mainLoop:for(var c=0;c<t.length;c++) {
			s[c]=s[c]||{
				thickness:t[c].thickness,perspective:t[c].perspective,dashed:t[c].dashed,color:t[c].color,alpha:t[c].alpha,points:[]
			};
			x=s[c].perspective;
			var d=t[c].points;
			while(d.length) {
				p=d.shift();
				s[c].points.push(p);
				if(!a&&c%2) {
					bf(p.position.x,p.position.y,2,s[c].perspective)
				}if(!a&&b++>n) {
					breakmainLoop
				}
			}
		}var e=t[t.length-1];
		if(e&&e.points.length&&!a) {
			v=true;
			setTimeout(function () {
				N()
			},m)
		}else {
			if(a) {
				v=false;
				C=y;
				x=y
			}else {
				w=true;
				x=y
			}
		}
	}function M(a) {
		SketchIO.loadSketch(a,function (a,b) {
			if(a=="success") {
				if(b.perspective) {
					C=b.perspective;
					y=b.perspective
				}if(b.amplitude) {
					z=b.amplitude;
					vibrationDropdown.setValue(Math.round(b.amplitude/h))
				}t=b.lines;
				v=true;
				N()
			}else {
				s=[]
			}
		})
	}var a=false;
	var b={
	};
	var c=780;
	var d=260;
	var e=18;
	var f=780;
	var g=260;
	var h=3;
	var i=.7;
	var j=50;
	var k=6;
	var l=2;
	var m=20;
	var n=2;
	var o={
		x:0,y:0,width:f,height:g
	};
	var q={
		width:0,height:0
	};
	var r=[];
	var s=[];
	var t=[];
	var u=0;
	var v=false;
	var w=false;
	var x=0;
	var y=0;
	var z=h;
	var A=i;
	var B=3;
	var C=0;
	var D=0;
	var E=0;
	var F=-1;
	var G;
	var H;
	var I={
		x:0,y:0,prev:{
			x:0,y:0
		},diff:{
			x:0,y:0
		},press:{
			x:0,y:0
		},down:false
	};
	var J={
		spaceDown:false,leftDown:false,rightDown:false
	};
	var K=false;
	var L={
		header:null,options:null,saveButton:null,resetButton:null,undoButton:null,dashToggle:null,dashToggleState:null,vibrationDropdown:null,sizeDropdown:null,colorDropdown:null
	};
	b.initialize=function () {
		G=document.getElementById("world");
		L.options=document.getElementById("options");
		L.saveButton=document.getElementById("save-button");
		L.resetButton=document.getElementById("reset-button");
		L.undoButton=document.getElementById("undo-button");
		L.dashToggle=document.getElementById("dash-toggle");
		L.dashToggleState=L.dashToggle.getElementsByTagName("span")[0];
		sizeDropdown=new DropDown("size-dropdown","size-dropdown-title","size-dropdown-list");
		sizeDropdown.setValue(B);
		colorDropdown=new DropDown("color-dropdown","color-dropdown-title","color-dropdown-list");
		colorDropdown.setValue("#000000");
		alphaDropdown=new DropDown("alpha-dropdown","alpha-dropdown-title","alpha-dropdown-list");
		alphaDropdown.setValue("0.9");
		vibrationDropdown=new DropDown("vibration-dropdown","vibration-dropdown-title","vibration-dropdown-list");
		vibrationDropdown.setValue(0);
		vibrationDropdown.setSelectionCallback(function (a) {
			z=h*a
		});
		vibrationDropdown.setClick(0);//ljk-增加特殊处理
		if(G&&G.getContext) {
			H=G.getContext("2d");
			document.addEventListener("mousemove",W,false);
			G.addEventListener("mousedown",X,false);
			document.addEventListener("mouseup",Y,false);
			G.addEventListener("touchstart",_,false);
			document.addEventListener("touchmove",ba,false);
			document.addEventListener("touchend",bb,false);
			document.addEventListener("keydown",T,false);
			document.addEventListener("keyup",V,false);
			L.saveButton.addEventListener("click",Q,false);//原始：L.saveButton.addEventListener("click",P,false);
			L.resetButton.addEventListener("click",R,false);
			L.undoButton.addEventListener("click",U,false);
			L.dashToggle.addEventListener("click",S,false);
			$("#post_button").click(Q);
			$(document).ready(function () {
				$("#publish_feed").attr("checked",true);
				$("#upload_to_album").attr("checked",true);
				$("#title_help").hide();
				$("#tuya_title").focus(function () {
					$("#title_div").removeClass("error");
					$("#title_help").hide()
				});
				$("#tuya_title").blur(function () {
					if($("#tuya_title").val().length==0) {
						$("#title_div").addClass("error");
						$("#title_help").show()
					}
				});
				$("#cancel_button").click(function () {
					$("#save_box").hide();
					return false
				});
				$("#x_button").click(function () {
					$("#save_box").hide();
					return false
				})
			});
			window.addEventListener("resize",be,false);
			window.addEventListener("beforeunload",bd,false);
			be();
			L.options.style.display="block";
			bg();
			if(/^.*\?id=(\d+).*$/gi.test(window.location.toString ())) {
				var a=RegExp.$1;
				M(a)
			}
		}
	};
	b.linesToJSON=function () {
		for(var a=0;a<s.length;a++) {
			if(!s[a]||!s[a].points||s[a].points.length==0) {
				s.splice(a,1);
				a--
			}
		}var b="{";
		b+='"p":'+C.toFixed(6)+",";
		b+='"a":'+z.toFixed(6)+",";
		b+='"l":[';
		for(var a=0;a<s.length;a++) {
			var c=s[a].points;
			b+="{";
			b+='"t":'+s[a].thickness+",";
			b+='"p":'+(s[a].perspective==0?1e-4:s[a].perspective.toFixed(6))+",";
			b+='"d":'+s[a].dashed+",";
			b+='"c":"'+s[a].color+'",';
			b+='"a":'+s[a].alpha+",";
			b+='"points":[';
			for(var d=0;d<c.length;d++) {
				var e=c[d];
				var f=Math.round(e.normal.x);
				var g=Math.round(e.normal.y);
				b+='"'+f+"x"+g+'"';
				b+=d<c.length-1?",":""
			}b+="]}";
			b+=a<s.length-1?",":""
		}b+="]}";
		return b
	};
	b.saveSketch=function (a,b,c,d) {
		if(s.length>0) {
			SketchIO.saveSketch(a,b,c,d);
			u=0
		}else {
			alert("你需要画点东西才能保存.")
		}
	};
	return b
}();
window.requestAnimFrame=function () {
	return window.requestAnimationFrame||window.webkitRequestAnimationFrame||window.mozRequestAnimationFrame||window.oRequestAnimationFrame||window.msRequestAnimationFrame||function (a,b) {
		window.setTimeout(a,1e3/60)
	}
}();
Sketch.initialize()