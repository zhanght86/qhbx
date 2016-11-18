var T, baidu = T = baidu || {
	version : "1.3.9"
};
baidu.guid = "$BAIDU$";
window[baidu.guid] = window[baidu.guid] || {};
baidu.ajax = baidu.ajax || {};
baidu.fn = baidu.fn || {};
baidu.fn.blank = function() {
};
baidu.ajax.request = function(N, A) {
	A = A || {};
	var I = A.data || "", K = !(A.async === false), J = A.username || "", C = A.password
			|| "", Q = (A.method || "GET").toUpperCase(), L = A.headers || {}, E = A.timeout || 0, O = {}, G, D, B;
	function H() {
		if (B.readyState == 4) {
			try {
				var R = B.status
			} catch (S) {
				M("failure");
				return

			}
			M(R);
			if ((R >= 200 && R < 300) || R == 304 || R == 1223) {
				M("success")
			} else {
				M("failure")
			}
			window.setTimeout(function() {
				B.onreadystatechange = baidu.fn.blank;
				if (K) {
					B = null
				}
			}, 0)
		}
	}
	function P() {
		if (window.ActiveXObject) {
			try {
				return new ActiveXObject("Msxml2.XMLHTTP")
			} catch (R) {
				try {
					return new ActiveXObject("Microsoft.XMLHTTP")
				} catch (R) {
				}
			}
		}
		if (window.XMLHttpRequest) {
			return new XMLHttpRequest()
		}
	}
	function M(S) {
		S = "on" + S;
		var U = O[S], R = baidu.ajax[S];
		if (U) {
			if (G) {
				clearTimeout(G)
			}
			if (S != "onsuccess") {
				U(B)
			} else {
				try {
					B.responseText
				} catch (V) {
					return U(B)
				}
				U(B, B.responseText)
			}
		} else {
			if (R) {
				if (S == "onsuccess") {
					return

				}
				R(B)
			}
		}
	}
	for (D in A) {
		O[D] = A[D]
	}
	L["X-Requested-With"] = "XMLHttpRequest";
	try {
		B = P();
		if (Q == "GET") {
			if (I) {
				N += (N.indexOf("?") >= 0 ? "&" : "?") + I;
				I = null
			}
			if (A.noCache) {
				N += (N.indexOf("?") >= 0 ? "&" : "?") + "b" + (+new Date)
						+ "=1"
			}
		}
		if (J) {
			B.open(Q, N, K, J, C)
		} else {
			B.open(Q, N, K)
		}
		if (K) {
			B.onreadystatechange = H
		}
		if (Q == "POST") {
			B.setRequestHeader("Content-Type",
					"application/x-www-form-urlencoded")
		}
		for (D in L) {
			if (L.hasOwnProperty(D)) {
				B.setRequestHeader(D, L[D])
			}
		}
		M("beforerequest");
		if (E) {
			G = setTimeout(function() {
				B.onreadystatechange = baidu.fn.blank;
				B.abort();
				M("timeout")
			}, E)
		}
		B.send(I);
		if (!K) {
			H()
		}
	} catch (F) {
		M("failure")
	}
	return B
};
baidu.ajax.form = function(S, Q) {
	Q = Q || {};
	var N = S.elements, G = N.length, R = S.getAttribute("method"), O = S
			.getAttribute("action"), A = Q.replacer || function(U, V) {
		return U
	}, D = {}, B = [], I, E, C, H, P, M, L, J, K;
	function F(V, U) {
		B.push(V + "=" + U)
	}
	for (I in Q) {
		if (Q.hasOwnProperty(I)) {
			D[I] = Q[I]
		}
	}
	for (I = 0; I < G; I++) {
		E = N[I];
		H = E.name;
		if (!E.disabled && H) {
			C = E.type;
			P = E.value;
			switch (C) {
			case "radio":
			case "checkbox":
				if (!E.checked) {
					break
				}
			case "textarea":
			case "text":
			case "password":
			case "hidden":
			case "select-one":
				F(H, A(P, H));
				break;
			case "select-multiple":
				M = E.options;
				J = M.length;
				for (L = 0; L < J; L++) {
					K = M[L];
					if (K.selected) {
						F(H, A(K.value, H))
					}
				}
				break
			}
		}
	}
	D.data = B.join("&");
	D.method = S.getAttribute("method") || "GET";
	return baidu.ajax.request(O, D)
};
baidu.ajax.get = function(A, B) {
	return baidu.ajax.request(A, {
		onsuccess : B
	})
};
baidu.ajax.post = function(A, C, B) {
	return baidu.ajax.request(A, {
		onsuccess : B,
		method : "POST",
		data : C
	})
};
baidu.array = baidu.array || {};
baidu.array.indexOf = function(C, A, D) {
	var B = C.length, E = A;
	D = D | 0;
	if (D < 0) {
		D = Math.max(0, B + D)
	}
	for (; D < B; D++) {
		if (D in C && C[D] === A) {
			return D
		}
	}
	return -1
};
baidu.array.contains = function(B, A) {
	return (baidu.array.indexOf(B, A) >= 0)
};
baidu.each = baidu.array.forEach = baidu.array.each = function(C, E, A) {
	var F, D, G, B = C.length;
	if ("function" == typeof E) {
		for (G = 0; G < B; G++) {
			D = C[G];
			F = E.call(A || C, D, G);
			if (F === false) {
				break
			}
		}
	}
	return C
};
baidu.array.empty = function(A) {
	A.length = 0
};
baidu.array.every = function(C, D, A) {
	var E = 0, B = C.length;
	for (; E < B; E++) {
		if (E in C && !D.call(A || C, C[E], E)) {
			return false
		}
	}
	return true
};
baidu.array.filter = function(C, E, G) {
	var H = [], A = 0, B = C.length, D, F;
	if ("function" == typeof E) {
		for (F = 0; F < B; F++) {
			D = C[F];
			if (true === E.call(G || C, D, F)) {
				H[A++] = D
			}
		}
	}
	return H
};
baidu.array.find = function(C, E) {
	var D, A, B = C.length;
	if ("function" == typeof E) {
		for (A = 0; A < B; A++) {
			D = C[A];
			if (true === E.call(C, D, A)) {
				return D
			}
		}
	}
	return null
};
baidu.array.hash = function(D, A) {
	var C = {}, E = A && A.length, F = 0, B = D.length;
	for (; F < B; F++) {
		C[D[F]] = (E && E > F) ? A[F] : true
	}
	return C
};
baidu.array.lastIndexOf = function(C, A, D) {
	var B = C.length;
	D = D | 0;
	if (!D || D >= B) {
		D = B - 1
	}
	if (D < 0) {
		D += B
	}
	for (; D >= 0; D--) {
		if (D in C && C[D] === A) {
			return D
		}
	}
	return -1
};
baidu.array.map = function(C, D, A) {
	var E = [], F = 0, B = C.length;
	for (; F < B; F++) {
		E[F] = D.call(A || C, C[F], F)
	}
	return E
};
baidu.array.reduce = function(C, F, E) {
	var A = 0, B = C.length, D = 0;
	if (arguments.length < 3) {
		for (; A < B; A++) {
			E = C[A++];
			D = 1;
			break
		}
		if (!D) {
			return

		}
	}
	for (; A < B; A++) {
		if (A in C) {
			E = F(E, C[A], A, C)
		}
	}
	return E
};
baidu.array.remove = function(C, A) {
	var B = C.length;
	while (B--) {
		if (B in C && C[B] === A) {
			C.splice(B, 1)
		}
	}
	return C
};
baidu.array.removeAt = function(A, B) {
	return A.splice(B, 1)[0]
};
baidu.array.some = function(C, D, A) {
	var E = 0, B = C.length;
	for (; E < B; E++) {
		if (E in C && D.call(A || C, C[E], E)) {
			return true
		}
	}
	return false
};
baidu.array.unique = function(D, C) {
	var A = D.length, B = D.slice(0), E, F;
	if ("function" != typeof C) {
		C = function(G, H) {
			return G === H
		}
	}
	while (--A > 0) {
		F = B[A];
		E = A;
		while (E--) {
			if (C(F, B[E])) {
				B.splice(A, 1);
				break
			}
		}
	}
	return B
};
baidu.browser = baidu.browser || {};
if (/chrome\/(\d+\.\d)/i.test(navigator.userAgent)) {
	baidu.browser.chrome = +RegExp["\x241"]
}
if (/firefox\/(\d+\.\d)/i.test(navigator.userAgent)) {
	baidu.browser.firefox = +RegExp["\x241"]
}
if (/msie (\d+\.\d)/i.test(navigator.userAgent)) {
	baidu.browser.ie = baidu.ie = document.documentMode || +RegExp["\x241"]
}
baidu.browser.isGecko = /gecko/i.test(navigator.userAgent)
		&& !/like gecko/i.test(navigator.userAgent);
baidu.browser.isStrict = document.compatMode == "CSS1Compat";
baidu.browser.isWebkit = /webkit/i.test(navigator.userAgent);
try {
	if (/(\d+\.\d)/.test(external.max_version)) {
		baidu.browser.maxthon = +RegExp["\x241"]
	}
} catch (e) {
}
if (/opera\/(\d+\.\d)/i.test(navigator.userAgent)) {
	baidu.browser.opera = +RegExp["\x241"]
}
(function() {
	var A = navigator.userAgent;
	if (/(\d+\.\d)?(?:\.\d)?\s+safari\/?(\d+\.\d+)?/i.test(A)
			&& !/chrome/i.test(A)) {
		baidu.browser.safari = +(RegExp["\x241"] || RegExp["\x242"])
	}
})();
baidu.cookie = baidu.cookie || {};
baidu.cookie._isValidKey = function(A) {
	return (new RegExp(
			'^[^\\x00-\\x20\\x7f\\(\\)<>@,;:\\\\\\"\\[\\]\\?=\\{\\}\\/\\u0080-\\uffff]+\x24'))
			.test(A)
};
baidu.cookie.getRaw = function(A) {
	if (baidu.cookie._isValidKey(A)) {
		var C = new RegExp("(^| )" + A + "=([^;]*)(;|\x24)"), B = C
				.exec(document.cookie);
		if (B) {
			return B[2] || null
		}
	}
	return null
};
baidu.cookie.get = function(B) {
	var A = baidu.cookie.getRaw(B);
	if ("string" == typeof A) {
		A = decodeURIComponent(A);
		return A
	}
	return null
};
baidu.cookie.setRaw = function(D, C, A) {
	if (!baidu.cookie._isValidKey(D)) {
		return

	}
	A = A || {};
	var B = A.expires;
	if ("number" == typeof A.expires) {
		B = new Date();
		B.setTime(B.getTime() + A.expires)
	}
	document.cookie = D + "=" + C + (A.path ? "; path=" + A.path : "")
			+ (B ? "; expires=" + B.toGMTString() : "")
			+ (A.domain ? "; domain=" + A.domain : "")
			+ (A.secure ? "; secure" : "")
};
baidu.cookie.remove = function(A, B) {
	B = B || {};
	B.expires = new Date(0);
	baidu.cookie.setRaw(A, "", B)
};
baidu.cookie.set = function(A, C, B) {
	baidu.cookie.setRaw(A, encodeURIComponent(C), B)
};
baidu.date = baidu.date || {};
baidu.number = baidu.number || {};
baidu.number.pad = function(D, E) {
	var C = "", A = (D < 0), B = String(Math.abs(D));
	if (B.length < E) {
		C = (new Array(E - B.length + 1)).join("0")
	}
	return (A ? "-" : "") + C + B
};
baidu.date.format = function(J, E) {
	if ("string" != typeof E) {
		return J.toString()
	}
	function G(K, L) {
		E = E.replace(K, L)
	}
	var I = baidu.number.pad, D = J.getFullYear(), F = J.getMonth() + 1, A = J
			.getDate(), C = J.getHours(), H = J.getMinutes(), B = J
			.getSeconds();
	G(/yyyy/g, I(D, 4));
	G(/yy/g, I(parseInt(D.toString().slice(2), 10), 2));
	G(/MM/g, I(F, 2));
	G(/M/g, F);
	G(/dd/g, I(A, 2));
	G(/d/g, A);
	G(/HH/g, I(C, 2));
	G(/H/g, C);
	G(/hh/g, I(C % 12, 2));
	G(/h/g, C % 12);
	G(/mm/g, I(H, 2));
	G(/m/g, H);
	G(/ss/g, I(B, 2));
	G(/s/g, B);
	return E
};
baidu.date.parse = function(E) {
	var B = new RegExp("^\\d+(\\-|\\/)\\d+(\\-|\\/)\\d+\x24");
	if ("string" == typeof E) {
		if (B.test(E) || isNaN(Date.parse(E))) {
			var C = E.split(/ |T/), A = C.length > 1 ? C[1].split(/[^\d]/) : [
					0, 0, 0 ], D = C[0].split(/[^\d]/);
			return new Date(D[0] - 0, D[1] - 1, D[2] - 0, A[0] - 0, A[1] - 0,
					A[2] - 0)
		} else {
			return new Date(E)
		}
	}
	return new Date()
};
baidu.dom = baidu.dom || {};
baidu.dom._NAME_ATTRS = (function() {
	var A = {
		cellpadding : "cellPadding",
		cellspacing : "cellSpacing",
		colspan : "colSpan",
		rowspan : "rowSpan",
		valign : "vAlign",
		usemap : "useMap",
		frameborder : "frameBorder"
	};
	if (baidu.browser.ie < 8) {
		A["for"] = "htmlFor";
		A["class"] = "className"
	} else {
		A.htmlFor = "for";
		A.className = "class"
	}
	return A
})();
baidu.lang = baidu.lang || {};
baidu.lang.isString = function(A) {
	return "[object String]" == Object.prototype.toString.call(A)
};
baidu.isString = baidu.lang.isString;
baidu.dom._g = function(A) {
	if (baidu.lang.isString(A)) {
		return document.getElementById(A)
	}
	return A
};
baidu._g = baidu.dom._g;
baidu.dom.g = function(A) {
	if ("string" == typeof A || A instanceof String) {
		return document.getElementById(A)
	} else {
		if (A && A.nodeName && (A.nodeType == 1 || A.nodeType == 9)) {
			return A
		}
	}
	return null
};
baidu.g = baidu.G = baidu.dom.g;
baidu.dom._matchNode = function(B, D, C) {
	B = baidu.dom.g(B);
	for ( var A = B[C]; A; A = A[D]) {
		if (A.nodeType == 1) {
			return A
		}
	}
	return null
};
baidu.dom._styleFilter = baidu.dom._styleFilter || [];
baidu.dom._styleFilter[baidu.dom._styleFilter.length] = {
	get : function(E, D) {
		if (/color/i.test(E) && D.indexOf("rgb(") != -1) {
			var C = D.split(",");
			D = "#";
			for ( var A = 0, B; B = C[A]; A++) {
				B = parseInt(B.replace(/[^\d]/gi, ""), 10).toString(16);
				D += B.length == 1 ? "0" + B : B
			}
			D = D.toUpperCase()
		}
		return D
	}
};
baidu.dom._styleFilter.filter = function(A, D, C) {
	for ( var B = 0, E = baidu.dom._styleFilter, F; F = E[B]; B++) {
		if (F = F[C]) {
			D = F(A, D)
		}
	}
	return D
};
baidu.dom._styleFilter[baidu.dom._styleFilter.length] = {
	set : function(B, A) {
		if (A.constructor == Number
				&& !/zIndex|fontWeight|opacity|zoom|lineHeight/i.test(B)) {
			A = A + "px"
		}
		return A
	}
};
baidu.dom._styleFixer = baidu.dom._styleFixer || {};
baidu.dom._styleFixer.display = baidu.browser.ie && baidu.browser.ie < 8 ? {
	set : function(B, A) {
		B = B.style;
		if (A == "inline-block") {
			B.display = "inline";
			B.zoom = 1
		} else {
			B.display = A
		}
	}
} : baidu.browser.firefox && baidu.browser.firefox < 3 ? {
	set : function(B, A) {
		B.style.display = A == "inline-block" ? "-moz-inline-box" : A
	}
} : null;
baidu.dom._styleFixer["float"] = baidu.browser.ie ? "styleFloat" : "cssFloat";
baidu.dom._styleFixer.opacity = baidu.browser.ie ? {
	get : function(B) {
		var A = B.style.filter;
		return A && A.indexOf("opacity=") >= 0 ? (parseFloat(A
				.match(/opacity=([^)]*)/)[1]) / 100)
				+ "" : "1"
	},
	set : function(B, C) {
		var A = B.style;
		A.filter = (A.filter || "").replace(/alpha\([^\)]*\)/gi, "")
				+ (C == 1 ? "" : "alpha(opacity=" + C * 100 + ")");
		A.zoom = 1
	}
} : null;
baidu.dom.getDocument = function(A) {
	A = baidu.dom.g(A);
	return A.nodeType == 9 ? A : A.ownerDocument || A.document
};
baidu.dom.getComputedStyle = function(A, B) {
	A = baidu.dom._g(A);
	var C = baidu.dom.getDocument(A), D;
	if (C.defaultView && C.defaultView.getComputedStyle) {
		D = C.defaultView.getComputedStyle(A, null);
		if (D) {
			return D[B] || D.getPropertyValue(B)
		}
	}
	return ""
};
baidu.string = baidu.string || {};
baidu.string.toCamelCase = function(A) {
	if (A.indexOf("-") < 0 && A.indexOf("_") < 0) {
		return A
	}
	return A.replace(/[-_][^-_]/g, function(B) {
		return B.charAt(1).toUpperCase()
	})
};
baidu.dom.getStyle = function(E, A) {
	var C = baidu.dom;
	E = C.g(E);
	A = baidu.string.toCamelCase(A);
	var D = E.style[A] || (E.currentStyle ? E.currentStyle[A] : "")
			|| C.getComputedStyle(E, A);
	if (!D) {
		var B = C._styleFixer[A];
		if (B) {
			D = B.get ? B.get(E) : baidu.dom.getStyle(E, B)
		}
	}
	if (B = C._styleFilter) {
		D = B.filter(A, D, "get")
	}
	return D
};
baidu.getStyle = baidu.dom.getStyle;
baidu.dom._styleFixer.textOverflow = (function() {
	var A = {};
	function B(F) {
		var E = F.length;
		if (E > 0) {
			E = F[E - 1];
			F.length--
		} else {
			E = null
		}
		return E
	}
	function D(F, E) {
		F[baidu.browser.firefox ? "textContent" : "innerText"] = E
	}
	function C(H, L, R) {
		var J = baidu.browser.ie ? H.currentStyle || H.style
				: getComputedStyle(H, null), S = J.fontWeight, E = "font-family:"
				+ J.fontFamily
				+ ";font-size:"
				+ J.fontSize
				+ ";word-spacing:"
				+ J.wordSpacing
				+ ";font-weight:"
				+ ((parseInt(S) || 0) == 401 ? 700 : S)
				+ ";font-style:"
				+ J.fontStyle + ";font-variant:" + J.fontVariant, O = A[E];
		if (!O) {
			J = H.appendChild(document.createElement("div"));
			J.style.cssText = "float:left;" + E;
			O = A[E] = [];
			for ( var G = 0; G < 256; G++) {
				G == 32 ? (J.innerHTML = "&nbsp;") : D(J, String
						.fromCharCode(G));
				O[G] = J.offsetWidth
			}
			D(J, "\u4e00");
			O[256] = J.offsetWidth;
			D(J, "\u4e00\u4e00");
			O[257] = J.offsetWidth - O[256] * 2;
			O[258] = O[".".charCodeAt(0)] * 3 + O[257] * 3;
			H.removeChild(J)
		}
		for ( var I = H.firstChild, F = O[256], M = O[257], N = O[258], P = [], R = R ? N
				: 0; I; I = I.nextSibling) {
			if (L < R) {
				H.removeChild(I)
			} else {
				if (I.nodeType == 3) {
					for ( var G = 0, Q = I.nodeValue, K = Q.length; G < K; G++) {
						J = Q.charCodeAt(G);
						P[P.length] = [ L, I, G ];
						L -= (G ? M : 0) + (J < 256 ? O[J] : F);
						if (L < R) {
							break
						}
					}
				} else {
					J = I.tagName;
					if (J == "IMG" || J == "TABLE") {
						J = I;
						I = I.previousSibling;
						H.removeChild(J)
					} else {
						P[P.length] = [ L, I ];
						L -= I.offsetWidth
					}
				}
			}
		}
		if (L < R) {
			while (J = B(P)) {
				L = J[0];
				I = J[1];
				J = J[2];
				if (I.nodeType == 3) {
					if (L >= N) {
						I.nodeValue = I.nodeValue.substring(0, J) + "...";
						return true
					} else {
						if (!J) {
							H.removeChild(I)
						}
					}
				} else {
					if (C(I, L, true)) {
						return true
					} else {
						H.removeChild(I)
					}
				}
			}
			H.innerHTML = ""
		}
	}
	return {
		get : function(E) {
			var F = baidu.browser, G = dom.getStyle;
			return (F.opera ? G("OTextOverflow") : F.firefox ? E._baiduOverflow
					: G("textOverflow"))
					|| "clip"
		},
		set : function(H, F) {
			var I = baidu.browser;
			if (H.tagName == "TD" || H.tagName == "TH" || I.firefox) {
				H._baiduHTML && (H.innerHTML = H._baiduHTML);
				if (F == "ellipsis") {
					H._baiduHTML = H.innerHTML;
					var E = document.createElement("div"), G = H.appendChild(E).offsetWidth;
					H.removeChild(E);
					C(H, G)
				} else {
					H._baiduHTML = ""
				}
			}
			E = H.style;
			I.opera ? (E.OTextOverflow = F)
					: I.firefox ? (H._baiduOverflow = F) : (E.textOverflow = F)
		}
	}
})();
(function() {
	var A = new RegExp("(^[\\s\\t\\xa0\\u3000]+)|([\\u3000\\xa0\\s\\t]+\x24)",
			"g");
	baidu.string.trim = function(B) {
		return String(B).replace(A, "")
	}
})();
baidu.trim = baidu.string.trim;
baidu.dom.addClass = function(D, C) {
	D = baidu.dom.g(D);
	var A = C.split(/\s+/), B = D.className, E = " " + B + " ", F = 0, G = A.length;
	for (; F < G; F++) {
		if (E.indexOf(" " + A[F] + " ") < 0) {
			B += (B ? " " : "") + A[F]
		}
	}
	D.className = B;
	return D
};
baidu.addClass = baidu.dom.addClass;
baidu.dom.children = function(A) {
	A = baidu.dom.g(A);
	for ( var B = [], C = A.firstChild; C; C = C.nextSibling) {
		if (C.nodeType == 1) {
			B.push(C)
		}
	}
	return B
};
baidu.dom.contains = function(B, A) {
	var C = baidu.dom._g;
	B = C(B);
	A = C(A);
	return B.contains ? B != A && B.contains(A) : !!(B
			.compareDocumentPosition(A) & 16)
};
baidu.dom.setAttr = function(A, B, C) {
	A = baidu.dom.g(A);
	if ("style" == B) {
		A.style.cssText = C
	} else {
		B = baidu.dom._NAME_ATTRS[B] || B;
		A.setAttribute(B, C)
	}
	return A
};
baidu.setAttr = baidu.dom.setAttr;
baidu.dom.setAttrs = function(C, B) {
	C = baidu.dom.g(C);
	for ( var A in B) {
		baidu.dom.setAttr(C, A, B[A])
	}
	return C
};
baidu.setAttrs = baidu.dom.setAttrs;
baidu.dom.create = function(D, B) {
	var C = document.createElement(D), A = B || {};
	return baidu.dom.setAttrs(C, A)
};
(function() {
	var A = window[baidu.guid];
	baidu.lang.guid = function() {
		return "TANGRAM__" + (A._counter++).toString(36)
	};
	A._counter = A._counter || 1
})();
window[baidu.guid]._instances = window[baidu.guid]._instances || {};
baidu.lang.isFunction = function(A) {
	return "[object Function]" == Object.prototype.toString.call(A)
};
baidu.lang.Class = function(A) {
	this.guid = A || baidu.lang.guid();
	window[baidu.guid]._instances[this.guid] = this
};
window[baidu.guid]._instances = window[baidu.guid]._instances || {};
baidu.lang.Class.prototype.dispose = function() {
	delete window[baidu.guid]._instances[this.guid];
	for ( var A in this) {
		if (!baidu.lang.isFunction(this[A])) {
			delete this[A]
		}
	}
	this.disposed = true
};
baidu.lang.Class.prototype.toString = function() {
	return "[object " + (this._className || "Object") + "]"
};
baidu.lang.Event = function(B, A) {
	this.type = B;
	this.returnValue = true;
	this.target = A || null;
	this.currentTarget = null
};
baidu.lang.Class.prototype.addEventListener = function(D, E, A) {
	if (!baidu.lang.isFunction(E)) {
		return

	}
	!this.__listeners && (this.__listeners = {});
	var B = this.__listeners, C;
	if (typeof A == "string" && A) {
		if (/[^\w\-]/.test(A)) {
			throw ("nonstandard key:" + A)
		} else {
			E.hashCode = A;
			C = A
		}
	}
	D.indexOf("on") != 0 && (D = "on" + D);
	typeof B[D] != "object" && (B[D] = {});
	C = C || baidu.lang.guid();
	E.hashCode = C;
	B[D][C] = E
};
baidu.lang.Class.prototype.removeEventListener = function(C, D) {
	if (typeof D != "undefined") {
		if ((baidu.lang.isFunction(D) && !(D = D.hashCode))
				|| (!baidu.lang.isString(D))) {
			return

		}
	}
	!this.__listeners && (this.__listeners = {});
	C.indexOf("on") != 0 && (C = "on" + C);
	var A = this.__listeners;
	if (!A[C]) {
		return

	}
	if (typeof D != "undefined") {
		A[C][D] && delete A[C][D]
	} else {
		for ( var B in A[C]) {
			delete A[C][B]
		}
	}
};
baidu.lang.Class.prototype.dispatchEvent = function(D, B) {
	if (baidu.lang.isString(D)) {
		D = new baidu.lang.Event(D)
	}
	!this.__listeners && (this.__listeners = {});
	B = B || {};
	for ( var E in B) {
		D[E] = B[E]
	}
	var E, A = this.__listeners, C = D.type;
	D.target = D.target || this;
	D.currentTarget = this;
	C.indexOf("on") != 0 && (C = "on" + C);
	baidu.lang.isFunction(this[C]) && this[C].apply(this, arguments);
	if (typeof A[C] == "object") {
		for (E in A[C]) {
			A[C][E].apply(this, arguments)
		}
	}
	return D.returnValue
};
baidu.lang.createSingle = function(A) {
	var C = new baidu.lang.Class();
	for ( var B in A) {
		C[B] = A[B]
	}
	return C
};
baidu.dom.ddManager = baidu.lang.createSingle({
	_targetsDroppingOver : {}
});
baidu.event = baidu.event || {};
baidu.event._listeners = baidu.event._listeners || [];
baidu.event.on = function(A, F, D) {
	F = F.replace(/^on/i, "");
	A = baidu.dom._g(A);
	var E = function(I) {
		D.call(A, I)
	}, B = baidu.event._listeners, G = baidu.event._eventFilter, C, H = F;
	F = F.toLowerCase();
	if (G && G[F]) {
		C = G[F](A, F, E);
		H = C.type;
		E = C.listener
	}
	if (A.addEventListener) {
		A.addEventListener(H, E, false)
	} else {
		if (A.attachEvent) {
			A.attachEvent("on" + H, E)
		}
	}
	B[B.length] = [ A, F, D, E, H ];
	return A
};
baidu.on = baidu.event.on;
baidu.event.un = function(G, D, H) {
	G = baidu.dom._g(G);
	D = D.replace(/^on/i, "").toLowerCase();
	var A = baidu.event._listeners, F = A.length, E = !H, B, C, I;
	while (F--) {
		B = A[F];
		if (B[1] === D && B[0] === G && (E || B[2] === H)) {
			C = B[4];
			I = B[3];
			if (G.removeEventListener) {
				G.removeEventListener(C, I, false)
			} else {
				if (G.detachEvent) {
					G.detachEvent("on" + C, I)
				}
			}
			A.splice(F, 1)
		}
	}
	return G
};
baidu.un = baidu.event.un;
baidu.event.preventDefault = function(A) {
	if (A.preventDefault) {
		A.preventDefault()
	} else {
		A.returnValue = false
	}
};
baidu.object = baidu.object || {};
baidu.extend = baidu.object.extend = function(C, B) {
	for ( var A in B) {
		if (B.hasOwnProperty(A)) {
			C[A] = B[A]
		}
	}
	return C
};
baidu.page = baidu.page || {};
baidu.page.getScrollTop = function() {
	var A = document;
	return window.pageYOffset || A.documentElement.scrollTop
			|| A.body.scrollTop
};
baidu.page.getScrollLeft = function() {
	var A = document;
	return window.pageXOffset || A.documentElement.scrollLeft
			|| A.body.scrollLeft
};
(function() {
	baidu.page.getMousePosition = function() {
		return {
			x : baidu.page.getScrollLeft() + A.x,
			y : baidu.page.getScrollTop() + A.y
		}
	};
	var A = {
		x : 0,
		y : 0
	};
	baidu.event.on(document, "onmousemove", function(B) {
		B = window.event || B;
		A.x = B.clientX;
		A.y = B.clientY
	})
})();
baidu.dom.getPosition = function(L) {
	L = baidu.dom.g(L);
	var C = baidu.dom.getDocument(L), I = baidu.browser, F = baidu.dom.getStyle, J = I.isGecko > 0
			&& C.getBoxObjectFor
			&& F(L, "position") == "absolute"
			&& (L.style.top === "" || L.style.left === ""), E = {
		left : 0,
		top : 0
	}, G = (I.ie && !I.isStrict) ? C.body : C.documentElement, B, K;
	if (L == G) {
		return E
	}
	if (L.getBoundingClientRect) {
		K = L.getBoundingClientRect();
		E.left = Math.floor(K.left)
				+ Math.max(C.documentElement.scrollLeft, C.body.scrollLeft);
		E.top = Math.floor(K.top)
				+ Math.max(C.documentElement.scrollTop, C.body.scrollTop);
		E.left -= C.documentElement.clientLeft;
		E.top -= C.documentElement.clientTop;
		var D = C.body, A = parseInt(F(D, "borderLeftWidth")), H = parseInt(F(
				D, "borderTopWidth"));
		if (I.ie && !I.isStrict) {
			E.left -= isNaN(A) ? 2 : A;
			E.top -= isNaN(H) ? 2 : H
		}
	} else {
		B = L;
		do {
			E.left += B.offsetLeft;
			E.top += B.offsetTop;
			if (I.isWebkit > 0 && F(B, "position") == "fixed") {
				E.left += C.body.scrollLeft;
				E.top += C.body.scrollTop;
				break
			}
			B = B.offsetParent
		} while (B && B != L);
		if (I.opera > 0 || (I.isWebkit > 0 && F(L, "position") == "absolute")) {
			E.top -= C.body.offsetTop
		}
		B = L.offsetParent;
		while (B && B != C.body) {
			E.left -= B.scrollLeft;
			if (!I.opera || B.tagName != "TR") {
				E.top -= B.scrollTop
			}
			B = B.offsetParent
		}
	}
	return E
};
(function() {
	var E, F, K, M, B, J, A, Q, C, L = baidu.lang.isFunction, N, H, O;
	baidu.dom.drag = function(S, U) {
		C = Q = null;
		if (!(E = baidu.dom.g(S))) {
			return false
		}
		F = baidu.object.extend({
			autoStop : true,
			capture : true,
			interval : 20
		}, U);
		H = baidu.dom.getPosition(E.offsetParent);
		O = baidu.dom.getPosition(E);
		if (baidu.getStyle(E, "position") == "absolute") {
			B = O.top - (E.offsetParent == document.body ? 0 : H.top);
			J = O.left - (E.offsetParent == document.body ? 0 : H.left)
		} else {
			B = parseFloat(baidu.getStyle(E, "top"))
					|| -parseFloat(baidu.getStyle(E, "bottom")) || 0;
			J = parseFloat(baidu.getStyle(E, "left"))
					|| -parseFloat(baidu.getStyle(E, "right")) || 0
		}
		if (F.mouseEvent) {
			K = baidu.page.getScrollLeft() + F.mouseEvent.clientX;
			M = baidu.page.getScrollTop() + F.mouseEvent.clientY
		} else {
			var R = baidu.page.getMousePosition();
			K = R.x;
			M = R.y
		}
		N = setInterval(P, F.interval);
		F.autoStop && baidu.event.on(document, "mouseup", D);
		baidu.event.on(document.body, "selectstart", I);
		if (F.capture && E.setCapture) {
			E.setCapture()
		} else {
			if (F.capture && window.captureEvents) {
				window.captureEvents(Event.MOUSEMOVE | Event.MOUSEUP)
			}
		}
		A = document.body.style.MozUserSelect;
		document.body.style.MozUserSelect = "none";
		if (L(F.ondragstart)) {
			F.ondragstart(E, F)
		}
		return {
			stop : D,
			update : G
		}
	};
	function G(R) {
		baidu.extend(F, R)
	}
	function D() {
		clearTimeout(N);
		if (F.capture && E.releaseCapture) {
			E.releaseCapture()
		} else {
			if (F.capture && window.releaseEvents) {
				window.releaseEvents(Event.MOUSEMOVE | Event.MOUSEUP)
			}
		}
		document.body.style.MozUserSelect = A;
		baidu.event.un(document.body, "selectstart", I);
		F.autoStop && baidu.event.un(document, "mouseup", D);
		if (L(F.ondragend)) {
			F.ondragend(E, F)
		}
	}
	function P(R) {
		var W = F.range, S = baidu.page.getMousePosition(), V = J + S.x - K, U = B
				+ S.y - M;
		if (typeof W == "object" && W && W.length == 4) {
			V = Math.max(W[3], V);
			V = Math.min(W[1] - E.offsetWidth, V);
			U = Math.max(W[0], U);
			U = Math.min(W[2] - E.offsetHeight, U)
		}
		E.style.top = U + "px";
		E.style.left = V + "px";
		if ((Q !== V || C !== U) && (Q !== null || C !== null)) {
			if (L(F.ondrag)) {
				F.ondrag(E, F)
			}
		}
		Q = V;
		C = U
	}
	function I(R) {
		return baidu.event.preventDefault(R, false)
	}
})();
baidu.dom.setStyle = function(E, A, D) {
	var C = baidu.dom, B;
	E = C.g(E);
	A = baidu.string.toCamelCase(A);
	if (B = C._styleFilter) {
		D = B.filter(A, D, "set")
	}
	B = C._styleFixer[A];
	(B && B.set) ? B.set(E, D) : (E.style[B || A] = D);
	return E
};
baidu.setStyle = baidu.dom.setStyle;
baidu.dom.draggable = function(I, A) {
	A = baidu.object.extend({
		toggle : function() {
			return true
		}
	}, A || {});
	A.autoStop = true;
	I = baidu.dom.g(I);
	A.handler = A.handler || I;
	var J, C = [ "ondragstart", "ondrag", "ondragend" ], H = C.length - 1, G, B, E = {
		dispose : function() {
			B && B.stop();
			baidu.event.un(A.handler, "onmousedown", D);
			baidu.lang.Class.prototype.dispose.call(E)
		}
	}, F = this;
	if (J = baidu.dom.ddManager) {
		for (; H >= 0; H--) {
			G = C[H];
			A[G] = (function(L) {
				var K = A[L];
				return function() {
					baidu.lang.isFunction(K) && K.apply(F, arguments);
					J.dispatchEvent(L, {
						DOM : I
					})
				}
			})(G)
		}
	}
	if (I) {
		function D(K) {
			var L = A.mouseEvent = window.event || K;
			if (L.button > 1
					|| (baidu.lang.isFunction(A.toggle) && !A.toggle())) {
				return

			}
			if (baidu.dom.getStyle(I, "position") == "static") {
				baidu.dom.setStyle(I, "position", "relative")
			}
			if (baidu.lang.isFunction(A.onbeforedragstart)) {
				A.onbeforedragstart(I)
			}
			B = baidu.dom.drag(I, A);
			E.stop = B.stop;
			E.update = B.update;
			baidu.event.preventDefault(L)
		}
		baidu.event.on(A.handler, "onmousedown", D)
	}
	return {
		cancel : function() {
			E.dispose()
		}
	}
};
baidu.dom.intersect = function(C, D) {
	var E = baidu.dom.g, F = baidu.dom.getPosition, B = Math.max, H = Math.min;
	C = E(C);
	D = E(D);
	var G = F(C), A = F(D);
	return B(G.left, A.left) <= H(G.left + C.offsetWidth, A.left
			+ D.offsetWidth)
			&& B(G.top, A.top) <= H(G.top + C.offsetHeight, A.top
					+ D.offsetHeight)
};
baidu.dom.droppable = function(E, G) {
	G = G || {};
	var F = baidu.dom.ddManager, C = baidu.dom.g(E), A = baidu.lang.guid(), D = function(
			H) {
		var I = F._targetsDroppingOver, J = {
			trigger : H.DOM,
			reciever : C
		};
		if (baidu.dom.intersect(C, H.DOM)) {
			if (!I[A]) {
				(typeof G.ondropover == "function") && G.ondropover.call(C, J);
				F.dispatchEvent("ondropover", J);
				I[A] = true
			}
		} else {
			if (I[A]) {
				(typeof G.ondropout == "function") && G.ondropout.call(C, J);
				F.dispatchEvent("ondropout", J)
			}
			delete I[A]
		}
	}, B = function(H) {
		var I = {
			trigger : H.DOM,
			reciever : C
		};
		if (baidu.dom.intersect(C, H.DOM)) {
			typeof G.ondrop == "function" && G.ondrop.call(C, I);
			F.dispatchEvent("ondrop", I)
		}
		delete F._targetsDroppingOver[A]
	};
	F.addEventListener("ondrag", D);
	F.addEventListener("ondragend", B);
	return {
		cancel : function() {
			F.removeEventListener("ondrag", D);
			F.removeEventListener("ondragend", B)
		}
	}
};
baidu.dom.empty = function(A) {
	A = baidu.dom.g(A);
	while (A.firstChild) {
		A.removeChild(A.firstChild)
	}
	return A
};
baidu.dom.first = function(A) {
	return baidu.dom._matchNode(A, "nextSibling", "firstChild")
};
baidu.dom.getAncestorBy = function(B, A) {
	B = baidu.dom.g(B);
	while ((B = B.parentNode) && B.nodeType == 1) {
		if (A(B)) {
			return B
		}
	}
	return null
};
baidu.dom.getAncestorByClass = function(B, A) {
	B = baidu.dom.g(B);
	A = new RegExp("(^|\\s)" + baidu.string.trim(A) + "(\\s|\x24)");
	while ((B = B.parentNode) && B.nodeType == 1) {
		if (A.test(B.className)) {
			return B
		}
	}
	return null
};
baidu.dom.getAncestorByTag = function(A, B) {
	A = baidu.dom.g(A);
	B = B.toUpperCase();
	while ((A = A.parentNode) && A.nodeType == 1) {
		if (A.tagName == B) {
			return A
		}
	}
	return null
};
baidu.dom.getAttr = function(A, B) {
	A = baidu.dom.g(A);
	if ("style" == B) {
		return A.style.cssText
	}
	B = baidu.dom._NAME_ATTRS[B] || B;
	return A.getAttribute(B)
};
baidu.getAttr = baidu.dom.getAttr;
baidu.dom.getParent = function(A) {
	A = baidu.dom._g(A);
	return A.parentElement || A.parentNode || null
};
baidu.dom.getText = function(D) {
	var A = "", C, E = 0, B;
	D = baidu._g(D);
	if (D.nodeType === 3 || D.nodeType === 4) {
		A += D.nodeValue
	} else {
		if (D.nodeType !== 8) {
			C = D.childNodes;
			for (B = C.length; E < B; E++) {
				A += baidu.dom.getText(C[E])
			}
		}
	}
	return A
};
baidu.dom.getWindow = function(B) {
	B = baidu.dom.g(B);
	var A = baidu.dom.getDocument(B);
	return A.parentWindow || A.defaultView || null
};
baidu.dom.hasAttr = function(C, A) {
	C = baidu.g(C);
	var B = C.attributes.getNamedItem(A);
	return !!(B && B.specified)
};
baidu.dom.hasClass = function(D, C) {
	D = baidu.dom.g(D);
	var A = baidu.string.trim(C).split(/\s+/), B = A.length;
	C = D.className.split(/\s+/).join(" ");
	while (B--) {
		if (!(new RegExp("(^| )" + A[B] + "( |\x24)")).test(C)) {
			return false
		}
	}
	return true
};
baidu.dom.hide = function(A) {
	A = baidu.dom.g(A);
	A.style.display = "none";
	return A
};
baidu.hide = baidu.dom.hide;
baidu.dom.insertAfter = function(C, D) {
	var A, B;
	A = baidu.dom._g;
	C = A(C);
	D = A(D);
	B = D.parentNode;
	if (B) {
		B.insertBefore(C, D.nextSibling)
	}
	return C
};
baidu.dom.insertBefore = function(C, D) {
	var A, B;
	A = baidu.dom._g;
	C = A(C);
	D = A(D);
	B = D.parentNode;
	if (B) {
		B.insertBefore(C, D)
	}
	return C
};
baidu.dom.insertHTML = function(D, B, E) {
	D = baidu.dom.g(D);
	var A, C;
	if (D.insertAdjacentHTML) {
		D.insertAdjacentHTML(B, E)
	} else {
		A = D.ownerDocument.createRange();
		B = B.toUpperCase();
		if (B == "AFTERBEGIN" || B == "BEFOREEND") {
			A.selectNodeContents(D);
			A.collapse(B == "AFTERBEGIN")
		} else {
			C = B == "BEFOREBEGIN";
			A[C ? "setStartBefore" : "setEndAfter"](D);
			A.collapse(C)
		}
		A.insertNode(A.createContextualFragment(E))
	}
	return D
};
baidu.insertHTML = baidu.dom.insertHTML;
baidu.dom.last = function(A) {
	return baidu.dom._matchNode(A, "previousSibling", "lastChild")
};
baidu.dom.next = function(A) {
	return baidu.dom._matchNode(A, "nextSibling", "nextSibling")
};
baidu.dom.prev = function(A) {
	return baidu.dom._matchNode(A, "previousSibling", "previousSibling")
};
baidu.string.escapeReg = function(A) {
	return String(A).replace(new RegExp("([.*+?^=!:\x24{}()|[\\]/\\\\])", "g"),
			"\\\x241")
};
baidu.dom.q = function(B, E, H) {
	var A = [], F = baidu.string.trim, C, D, I, G;
	if (!(B = F(B))) {
		return A
	}
	if ("undefined" == typeof E) {
		E = document
	} else {
		E = baidu.dom.g(E);
		if (!E) {
			return A
		}
	}
	H && (H = F(H).toUpperCase());
	if (E.getElementsByClassName) {
		I = E.getElementsByClassName(B);
		C = I.length;
		for (D = 0; D < C; D++) {
			G = I[D];
			if (H && G.tagName != H) {
				continue
			}
			A[A.length] = G
		}
	} else {
		B = new RegExp("(^|\\s)" + baidu.string.escapeReg(B) + "(\\s|\x24)");
		I = H ? E.getElementsByTagName(H) : (E.all || E
				.getElementsByTagName("*"));
		C = I.length;
		for (D = 0; D < C; D++) {
			G = I[D];
			B.test(G.className) && (A[A.length] = G)
		}
	}
	return A
};
baidu.q = baidu.Q = baidu.dom.q;
(function() {
	var B = /((?:\((?:\([^()]+\)|[^()]+)+\)|\[(?:\[[^\[\]]*\]|['"][^'"]*['"]|[^\[\]'"]+)+\]|\\.|[^ >+~,(\[\\]+)+|[>+~])(\s*,\s*)?((?:.|\r|\n)*)/g, I = 0, N = Object.prototype.toString, C = false, J = true;
	[ 0, 0 ].sort(function() {
		J = false;
		return 0
	});
	var P = function(Y, j, V, U) {
		V = V || [];
		j = j || document;
		var R = j;
		if (j.nodeType !== 1 && j.nodeType !== 9) {
			return []
		}
		if (!Y || typeof Y !== "string") {
			return V
		}
		var f, h, a, g, i, c, d, W, Z = true, b = P.isXML(j), X = [], S = Y;
		do {
			B.exec("");
			f = B.exec(S);
			if (f) {
				S = f[3];
				X.push(f[1]);
				if (f[2]) {
					g = f[3];
					break
				}
			}
		} while (f);
		if (X.length > 1 && H.exec(Y)) {
			if (X.length === 2 && M.relative[X[0]]) {
				h = K(X[0] + X[1], j)
			} else {
				h = M.relative[X[0]] ? [ j ] : P(X.shift(), j);
				while (X.length) {
					Y = X.shift();
					if (M.relative[Y]) {
						Y += X.shift()
					}
					h = K(Y, h)
				}
			}
		} else {
			if (!U && X.length > 1 && j.nodeType === 9 && !b
					&& M.match.ID.test(X[0])
					&& !M.match.ID.test(X[X.length - 1])) {
				i = P.find(X.shift(), j, b);
				j = i.expr ? P.filter(i.expr, i.set)[0] : i.set[0]
			}
			if (j) {
				i = U ? {
					expr : X.pop(),
					set : Q(U)
				} : P.find(X.pop(),
						X.length === 1 && (X[0] === "~" || X[0] === "+")
								&& j.parentNode ? j.parentNode : j, b);
				h = i.expr ? P.filter(i.expr, i.set) : i.set;
				if (X.length > 0) {
					a = Q(h)
				} else {
					Z = false
				}
				while (X.length) {
					c = X.pop();
					d = c;
					if (!M.relative[c]) {
						c = ""
					} else {
						d = X.pop()
					}
					if (d == null) {
						d = j
					}
					M.relative[c](a, d, b)
				}
			} else {
				a = X = []
			}
		}
		if (!a) {
			a = h
		}
		if (!a) {
			P.error(c || Y)
		}
		if (N.call(a) === "[object Array]") {
			if (!Z) {
				V.push.apply(V, a)
			} else {
				if (j && j.nodeType === 1) {
					for (W = 0; a[W] != null; W++) {
						if (a[W]
								&& (a[W] === true || a[W].nodeType === 1
										&& P.contains(j, a[W]))) {
							V.push(h[W])
						}
					}
				} else {
					for (W = 0; a[W] != null; W++) {
						if (a[W] && a[W].nodeType === 1) {
							V.push(h[W])
						}
					}
				}
			}
		} else {
			Q(a, V)
		}
		if (g) {
			P(g, R, V, U);
			P.uniqueSort(V)
		}
		return V
	};
	P.uniqueSort = function(R) {
		if (O) {
			C = J;
			R.sort(O);
			if (C) {
				for ( var S = 1; S < R.length; S++) {
					if (R[S] === R[S - 1]) {
						R.splice(S--, 1)
					}
				}
			}
		}
		return R
	};
	P.matches = function(S, R) {
		return P(S, null, null, R)
	};
	P.matchesSelector = function(S, R) {
		return P(R, null, null, [ S ]).length > 0
	};
	P.find = function(S, a, R) {
		var U;
		if (!S) {
			return []
		}
		for ( var X = 0, Y = M.order.length; X < Y; X++) {
			var W, V = M.order[X];
			if ((W = M.leftMatch[V].exec(S))) {
				var Z = W[1];
				W.splice(1, 1);
				if (Z.substr(Z.length - 1) !== "\\") {
					W[1] = (W[1] || "").replace(/\\/g, "");
					U = M.find[V](W, a, R);
					if (U != null) {
						S = S.replace(M.match[V], "");
						break
					}
				}
			}
		}
		if (!U) {
			U = a.getElementsByTagName("*")
		}
		return {
			set : U,
			expr : S
		}
	};
	P.filter = function(R, S, d, a) {
		var X, j, f = R, Z = [], V = S, W = S && S[0] && P.isXML(S[0]);
		while (R && S.length) {
			for ( var U in M.filter) {
				if ((X = M.leftMatch[U].exec(R)) != null && X[2]) {
					var c, h, g = M.filter[U], b = X[1];
					j = false;
					X.splice(1, 1);
					if (b.substr(b.length - 1) === "\\") {
						continue
					}
					if (V === Z) {
						Z = []
					}
					if (M.preFilter[U]) {
						X = M.preFilter[U](X, V, d, Z, a, W);
						if (!X) {
							j = c = true
						} else {
							if (X === true) {
								continue
							}
						}
					}
					if (X) {
						for ( var Y = 0; (h = V[Y]) != null; Y++) {
							if (h) {
								c = g(h, X, Y, V);
								var i = a ^ !!c;
								if (d && c != null) {
									if (i) {
										j = true
									} else {
										V[Y] = false
									}
								} else {
									if (i) {
										Z.push(h);
										j = true
									}
								}
							}
						}
					}
					if (c !== undefined) {
						if (!d) {
							V = Z
						}
						R = R.replace(M.match[U], "");
						if (!j) {
							return []
						}
						break
					}
				}
			}
			if (R === f) {
				if (j == null) {
					P.error(R)
				} else {
					break
				}
			}
			f = R
		}
		return V
	};
	P.error = function(R) {
		throw "Syntax error, unrecognized expression: " + R
	};
	var M = P.selectors = {
		order : [ "ID", "NAME", "TAG" ],
		match : {
			ID : /#((?:[\w\u00c0-\uFFFF\-]|\\.)+)/,
			CLASS : /\.((?:[\w\u00c0-\uFFFF\-]|\\.)+)/,
			NAME : /\[name=['"]*((?:[\w\u00c0-\uFFFF\-]|\\.)+)['"]*\]/,
			ATTR : /\[\s*((?:[\w\u00c0-\uFFFF\-]|\\.)+)\s*(?:(\S?=)\s*(['"]*)(.*?)\3|)\s*\]/,
			TAG : /^((?:[\w\u00c0-\uFFFF\*\-]|\\.)+)/,
			CHILD : /:(only|nth|last|first)-child(?:\(\s*(even|odd|(?:[+\-]?\d+|(?:[+\-]?\d*)?n\s*(?:[+\-]\s*\d+)?))\s*\))?/,
			POS : /:(nth|eq|gt|lt|first|last|even|odd)(?:\((\d*)\))?(?=[^\-]|$)/,
			PSEUDO : /:((?:[\w\u00c0-\uFFFF\-]|\\.)+)(?:\((['"]?)((?:\([^\)]+\)|[^\(\)]*)+)\2\))?/
		},
		leftMatch : {},
		attrMap : {
			"class" : "className",
			"for" : "htmlFor"
		},
		attrHandle : {
			href : function(R) {
				return R.getAttribute("href")
			}
		},
		relative : {
			"+" : function(Z, W) {
				var U = typeof W === "string", R = U && !/\W/.test(W), Y = U
						&& !R;
				if (R) {
					W = W.toLowerCase()
				}
				for ( var V = 0, X = Z.length, S; V < X; V++) {
					if ((S = Z[V])) {
						while ((S = S.previousSibling) && S.nodeType !== 1) {
						}
						Z[V] = Y || S && S.nodeName.toLowerCase() === W ? S || false
								: S === W
					}
				}
				if (Y) {
					P.filter(W, Z, true)
				}
			},
			">" : function(Y, W) {
				var R, S = typeof W === "string", V = 0, X = Y.length;
				if (S && !/\W/.test(W)) {
					W = W.toLowerCase();
					for (; V < X; V++) {
						R = Y[V];
						if (R) {
							var U = R.parentNode;
							Y[V] = U.nodeName.toLowerCase() === W ? U : false
						}
					}
				} else {
					for (; V < X; V++) {
						R = Y[V];
						if (R) {
							Y[V] = S ? R.parentNode : R.parentNode === W
						}
					}
					if (S) {
						P.filter(W, Y, true)
					}
				}
			},
			"" : function(U, W, R) {
				var S, V = I++, X = A;
				if (typeof W === "string" && !/\W/.test(W)) {
					W = W.toLowerCase();
					S = W;
					X = D
				}
				X("parentNode", W, V, U, S, R)
			},
			"~" : function(U, W, R) {
				var S, V = I++, X = A;
				if (typeof W === "string" && !/\W/.test(W)) {
					W = W.toLowerCase();
					S = W;
					X = D
				}
				X("previousSibling", W, V, U, S, R)
			}
		},
		find : {
			ID : function(U, S, R) {
				if (typeof S.getElementById !== "undefined" && !R) {
					var V = S.getElementById(U[1]);
					return V && V.parentNode ? [ V ] : []
				}
			},
			NAME : function(V, R) {
				if (typeof R.getElementsByName !== "undefined") {
					var W = [], S = R.getElementsByName(V[1]);
					for ( var U = 0, X = S.length; U < X; U++) {
						if (S[U].getAttribute("name") === V[1]) {
							W.push(S[U])
						}
					}
					return W.length === 0 ? null : W
				}
			},
			TAG : function(S, R) {
				return R.getElementsByTagName(S[1])
			}
		},
		preFilter : {
			CLASS : function(U, W, V, X, Z, Y) {
				U = " " + U[1].replace(/\\/g, "") + " ";
				if (Y) {
					return U
				}
				for ( var S = 0, R; (R = W[S]) != null; S++) {
					if (R) {
						if (Z
								^ (R.className && (" " + R.className + " ")
										.replace(/[\t\n\r]/g, " ").indexOf(U) >= 0)) {
							if (!V) {
								X.push(R)
							}
						} else {
							if (V) {
								W[S] = false
							}
						}
					}
				}
				return false
			},
			ID : function(R) {
				return R[1].replace(/\\/g, "")
			},
			TAG : function(R, S) {
				return R[1].toLowerCase()
			},
			CHILD : function(S) {
				if (S[1] === "nth") {
					if (!S[2]) {
						P.error(S[0])
					}
					S[2] = S[2].replace(/^\+|\s*/g, "");
					var R = /(-?)(\d*)(?:n([+\-]?\d*))?/.exec(S[2] === "even"
							&& "2n" || S[2] === "odd" && "2n+1"
							|| !/\D/.test(S[2]) && "0n+" + S[2] || S[2]);
					S[2] = (R[1] + (R[2] || 1)) - 0;
					S[3] = R[3] - 0
				} else {
					if (S[2]) {
						P.error(S[0])
					}
				}
				S[0] = I++;
				return S
			},
			ATTR : function(S, W, V, X, R, Y) {
				var U = S[1].replace(/\\/g, "");
				if (!Y && M.attrMap[U]) {
					S[1] = M.attrMap[U]
				}
				if (S[2] === "~=") {
					S[4] = " " + S[4] + " "
				}
				return S
			},
			PSEUDO : function(S, W, V, X, R) {
				if (S[1] === "not") {
					if ((B.exec(S[3]) || "").length > 1 || /^\w/.test(S[3])) {
						S[3] = P(S[3], null, null, W)
					} else {
						var U = P.filter(S[3], W, V, true ^ R);
						if (!V) {
							X.push.apply(X, U)
						}
						return false
					}
				} else {
					if (M.match.POS.test(S[0]) || M.match.CHILD.test(S[0])) {
						return true
					}
				}
				return S
			},
			POS : function(R) {
				R.unshift(true);
				return R
			}
		},
		filters : {
			enabled : function(R) {
				return R.disabled === false && R.type !== "hidden"
			},
			disabled : function(R) {
				return R.disabled === true
			},
			checked : function(R) {
				return R.checked === true
			},
			selected : function(R) {
				R.parentNode.selectedIndex;
				return R.selected === true
			},
			parent : function(R) {
				return !!R.firstChild
			},
			empty : function(R) {
				return !R.firstChild
			},
			has : function(R, S, U) {
				return !!P(U[3], R).length
			},
			header : function(R) {
				return (/h\d/i).test(R.nodeName)
			},
			text : function(R) {
				return "text" === R.type
			},
			radio : function(R) {
				return "radio" === R.type
			},
			checkbox : function(R) {
				return "checkbox" === R.type
			},
			file : function(R) {
				return "file" === R.type
			},
			password : function(R) {
				return "password" === R.type
			},
			submit : function(R) {
				return "submit" === R.type
			},
			image : function(R) {
				return "image" === R.type
			},
			reset : function(R) {
				return "reset" === R.type
			},
			button : function(R) {
				return "button" === R.type
						|| R.nodeName.toLowerCase() === "button"
			},
			input : function(R) {
				return (/input|select|textarea|button/i).test(R.nodeName)
			}
		},
		setFilters : {
			first : function(R, S) {
				return S === 0
			},
			last : function(S, U, V, R) {
				return U === R.length - 1
			},
			even : function(R, S) {
				return S % 2 === 0
			},
			odd : function(R, S) {
				return S % 2 === 1
			},
			lt : function(R, S, U) {
				return S < U[3] - 0
			},
			gt : function(R, S, U) {
				return S > U[3] - 0
			},
			nth : function(R, S, U) {
				return U[3] - 0 === S
			},
			eq : function(R, S, U) {
				return U[3] - 0 === S
			}
		},
		filter : {
			PSEUDO : function(Y, S, U, R) {
				var a = S[1], Z = M.filters[a];
				if (Z) {
					return Z(Y, U, S, R)
				} else {
					if (a === "contains") {
						return (Y.textContent || Y.innerText
								|| P.getText([ Y ]) || "").indexOf(S[3]) >= 0
					} else {
						if (a === "not") {
							var X = S[3];
							for ( var V = 0, W = X.length; V < W; V++) {
								if (X[V] === Y) {
									return false
								}
							}
							return true
						} else {
							P.error(a)
						}
					}
				}
			},
			CHILD : function(b, Y) {
				var V = Y[1], a = b;
				switch (V) {
				case "only":
				case "first":
					while ((a = a.previousSibling)) {
						if (a.nodeType === 1) {
							return false
						}
					}
					if (V === "first") {
						return true
					}
					a = b;
				case "last":
					while ((a = a.nextSibling)) {
						if (a.nodeType === 1) {
							return false
						}
					}
					return true;
				case "nth":
					var Z = Y[2], R = Y[3];
					if (Z === 1 && R === 0) {
						return true
					}
					var W = Y[0], S = b.parentNode;
					if (S && (S.sizcache !== W || !b.nodeIndex)) {
						var X = 0;
						for (a = S.firstChild; a; a = a.nextSibling) {
							if (a.nodeType === 1) {
								a.nodeIndex = ++X
							}
						}
						S.sizcache = W
					}
					var U = b.nodeIndex - R;
					if (Z === 0) {
						return U === 0
					} else {
						return (U % Z === 0 && U / Z >= 0)
					}
				}
			},
			ID : function(R, S) {
				return R.nodeType === 1 && R.getAttribute("id") === S
			},
			TAG : function(R, S) {
				return (S === "*" && R.nodeType === 1)
						|| R.nodeName.toLowerCase() === S
			},
			CLASS : function(R, S) {
				return (" " + (R.className || R.getAttribute("class")) + " ")
						.indexOf(S) > -1
			},
			ATTR : function(R, U) {
				var V = U[1], X = M.attrHandle[V] ? M.attrHandle[V](R)
						: R[V] != null ? R[V] : R.getAttribute(V), Y = X + "", S = U[2], W = U[4];
				return X == null ? S === "!="
						: S === "=" ? Y === W
								: S === "*=" ? Y.indexOf(W) >= 0
										: S === "~=" ? (" " + Y + " ")
												.indexOf(W) >= 0
												: !W ? Y && X !== false
														: S === "!=" ? Y !== W
																: S === "^=" ? Y
																		.indexOf(W) === 0
																		: S === "$=" ? Y
																				.substr(Y.length
																						- W.length) === W
																				: S === "|=" ? Y === W
																						|| Y
																								.substr(
																										0,
																										W.length + 1) === W
																								+ "-"
																						: false
			},
			POS : function(S, W, V, R) {
				var X = W[2], U = M.setFilters[X];
				if (U) {
					return U(S, V, W, R)
				}
			}
		}
	};
	var H = M.match.POS, L = function(R, S) {
		return "\\" + (S - 0 + 1)
	};
	for ( var E in M.match) {
		M.match[E] = new RegExp(M.match[E].source
				+ (/(?![^\[]*\])(?![^\(]*\))/.source));
		M.leftMatch[E] = new RegExp(/(^(?:.|\r|\n)*?)/.source
				+ M.match[E].source.replace(/\\(\d+)/g, L))
	}
	var Q = function(R, S) {
		R = Array.prototype.slice.call(R, 0);
		if (S) {
			S.push.apply(S, R);
			return S
		}
		return R
	};
	try {
		Array.prototype.slice.call(document.documentElement.childNodes, 0)[0].nodeType
	} catch (G) {
		Q = function(R, S) {
			var U = 0, V = S || [];
			if (N.call(R) === "[object Array]") {
				Array.prototype.push.apply(V, R)
			} else {
				if (typeof R.length === "number") {
					for ( var W = R.length; U < W; U++) {
						V.push(R[U])
					}
				} else {
					for (; R[U]; U++) {
						V.push(R[U])
					}
				}
			}
			return V
		}
	}
	var O, F;
	if (document.documentElement.compareDocumentPosition) {
		O = function(R, S) {
			if (R === S) {
				C = true;
				return 0
			}
			if (!R.compareDocumentPosition || !S.compareDocumentPosition) {
				return R.compareDocumentPosition ? -1 : 1
			}
			return R.compareDocumentPosition(S) & 4 ? -1 : 1
		}
	} else {
		O = function(S, U) {
			var W, a, Z = [], b = [], X = S.parentNode, V = U.parentNode, R = X;
			if (S === U) {
				C = true;
				return 0
			} else {
				if (X === V) {
					return F(S, U)
				} else {
					if (!X) {
						return -1
					} else {
						if (!V) {
							return 1
						}
					}
				}
			}
			while (R) {
				Z.unshift(R);
				R = R.parentNode
			}
			R = V;
			while (R) {
				b.unshift(R);
				R = R.parentNode
			}
			W = Z.length;
			a = b.length;
			for ( var Y = 0; Y < W && Y < a; Y++) {
				if (Z[Y] !== b[Y]) {
					return F(Z[Y], b[Y])
				}
			}
			return Y === W ? F(S, b[Y], -1) : F(Z[Y], U, 1)
		};
		F = function(U, V, S) {
			if (U === V) {
				return S
			}
			var R = U.nextSibling;
			while (R) {
				if (R === V) {
					return -1
				}
				R = R.nextSibling
			}
			return 1
		}
	}
	P.getText = function(V) {
		var U = "", R;
		for ( var S = 0; V[S]; S++) {
			R = V[S];
			if (R.nodeType === 3 || R.nodeType === 4) {
				U += R.nodeValue
			} else {
				if (R.nodeType !== 8) {
					U += P.getText(R.childNodes)
				}
			}
		}
		return U
	};
	(function() {
		var S = document.createElement("div"), R = "script"
				+ (new Date()).getTime(), U = document.documentElement;
		S.innerHTML = "<a name='" + R + "'/>";
		U.insertBefore(S, U.firstChild);
		if (document.getElementById(R)) {
			M.find.ID = function(W, V, Y) {
				if (typeof V.getElementById !== "undefined" && !Y) {
					var X = V.getElementById(W[1]);
					return X ? X.id === W[1]
							|| typeof X.getAttributeNode !== "undefined"
							&& X.getAttributeNode("id").nodeValue === W[1] ? [ X ]
							: undefined
							: []
				}
			};
			M.filter.ID = function(V, X) {
				var W = typeof V.getAttributeNode !== "undefined"
						&& V.getAttributeNode("id");
				return V.nodeType === 1 && W && W.nodeValue === X
			}
		}
		U.removeChild(S);
		U = S = null
	})();
	(function() {
		var R = document.createElement("div");
		try {
			R.appendChild(document.createComment(""))
		} catch (S) {
		}
		if (R.getElementsByTagName("*").length > 0) {
			M.find.TAG = function(X, Y) {
				var U = Y.getElementsByTagName(X[1]);
				if (X[1] === "*") {
					var V = [];
					for ( var W = 0; U[W]; W++) {
						if (U[W].nodeType === 1) {
							V.push(U[W])
						}
					}
					U = V
				}
				return U
			}
		}
		R.innerHTML = "<a href='#'></a>";
		if (R.firstChild && typeof R.firstChild.getAttribute !== "undefined"
				&& R.firstChild.getAttribute("href") !== "#") {
			M.attrHandle.href = function(U) {
				return U.getAttribute("href", 2)
			}
		}
		R = null
	})();
	if (document.querySelectorAll) {
		(function() {
			var V = P, R = document.createElement("div"), S = "__sizzle__";
			R.innerHTML = "<p class='TEST'></p>";
			if (R.querySelectorAll && R.querySelectorAll(".TEST").length === 0) {
				return

			}
			P = function(W, c, a, X) {
				c = c || document;
				W = W.replace(/\=\s*([^'"\]]*)\s*\]/g, "='$1']");
				if (!X && !P.isXML(c)) {
					if (c.nodeType === 9) {
						try {
							return Q(c.querySelectorAll(W), a)
						} catch (Z) {
						}
					} else {
						if (c.nodeType === 1
								&& c.nodeName.toLowerCase() !== "object") {
							var b = c.getAttribute("id"), d = b || S, f = c.parentNode, g = /^\s*[+~]/
									.test(W);
							if (!b) {
								c.setAttribute("id", d)
							} else {
								d = d.replace(/'/g, "\\$&")
							}
							if (g && f) {
								c = c.parentNode
							}
							try {
								if (!g || f) {
									return Q(c.querySelectorAll("[id='" + d
											+ "'] " + W), a)
								}
							} catch (Y) {
							} finally {
								if (!b) {
									c.removeAttribute("id")
								}
							}
						}
					}
				}
				return V(W, c, a, X)
			};
			for ( var U in V) {
				P[U] = V[U]
			}
			R = null
		})()
	}
	(function() {
		var V = document.documentElement, S = V.matchesSelector
				|| V.mozMatchesSelector || V.webkitMatchesSelector
				|| V.msMatchesSelector, U = false;
		try {
			S.call(document.documentElement, "[test!='']:sizzle")
		} catch (R) {
			U = true
		}
		if (S) {
			P.matchesSelector = function(X, Y) {
				Y = Y.replace(/\=\s*([^'"\]]*)\s*\]/g, "='$1']");
				if (!P.isXML(X)) {
					try {
						if (U || !M.match.PSEUDO.test(Y) && !/!=/.test(Y)) {
							return S.call(X, Y)
						}
					} catch (W) {
					}
				}
				return P(Y, null, null, [ X ]).length > 0
			}
		}
	})();
	(function() {
		var R = document.createElement("div");
		R.innerHTML = "<div class='test e'></div><div class='test'></div>";
		if (!R.getElementsByClassName
				|| R.getElementsByClassName("e").length === 0) {
			return

		}
		R.lastChild.className = "e";
		if (R.getElementsByClassName("e").length === 1) {
			return

		}
		M.order.splice(1, 0, "CLASS");
		M.find.CLASS = function(V, U, S) {
			if (typeof U.getElementsByClassName !== "undefined" && !S) {
				return U.getElementsByClassName(V[1])
			}
		};
		R = null
	})();
	function D(a, V, W, R, U, S) {
		for ( var Y = 0, Z = R.length; Y < Z; Y++) {
			var b = R[Y];
			if (b) {
				var X = false;
				b = b[a];
				while (b) {
					if (b.sizcache === W) {
						X = R[b.sizset];
						break
					}
					if (b.nodeType === 1 && !S) {
						b.sizcache = W;
						b.sizset = Y
					}
					if (b.nodeName.toLowerCase() === V) {
						X = b;
						break
					}
					b = b[a]
				}
				R[Y] = X
			}
		}
	}
	function A(a, V, W, R, U, S) {
		for ( var Y = 0, Z = R.length; Y < Z; Y++) {
			var b = R[Y];
			if (b) {
				var X = false;
				b = b[a];
				while (b) {
					if (b.sizcache === W) {
						X = R[b.sizset];
						break
					}
					if (b.nodeType === 1) {
						if (!S) {
							b.sizcache = W;
							b.sizset = Y
						}
						if (typeof V !== "string") {
							if (b === V) {
								X = true;
								break
							}
						} else {
							if (P.filter(V, [ b ]).length > 0) {
								X = b;
								break
							}
						}
					}
					b = b[a]
				}
				R[Y] = X
			}
		}
	}
	if (document.documentElement.contains) {
		P.contains = function(R, S) {
			return R !== S && (R.contains ? R.contains(S) : true)
		}
	} else {
		if (document.documentElement.compareDocumentPosition) {
			P.contains = function(R, S) {
				return !!(R.compareDocumentPosition(S) & 16)
			}
		} else {
			P.contains = function() {
				return false
			}
		}
	}
	P.isXML = function(S) {
		var R = (S ? S.ownerDocument || S : 0).documentElement;
		return R ? R.nodeName !== "HTML" : false
	};
	var K = function(X, Y) {
		var R, U = [], S = "", V = Y.nodeType ? [ Y ] : Y;
		while ((R = M.match.PSEUDO.exec(X))) {
			S += R[0];
			X = X.replace(M.match.PSEUDO, "")
		}
		X = M.relative[X] ? X + "*" : X;
		for ( var Z = 0, W = V.length; Z < W; Z++) {
			P(X, V[Z], U)
		}
		return P.filter(S, U)
	};
	baidu.dom.query = P
})();
(function() {
	var A = baidu.dom.ready = function() {
		var C = false, D = [], G;
		if (document.addEventListener) {
			G = function() {
				document.removeEventListener("DOMContentLoaded", G, false);
				F()
			}
		} else {
			if (document.attachEvent) {
				G = function() {
					if (document.readyState === "complete") {
						document.detachEvent("onreadystatechange", G);
						F()
					}
				}
			}
		}
		function F() {
			if (!F.isReady) {
				F.isReady = true;
				for ( var H = 0, I = D.length; H < I; H++) {
					D[H]()
				}
			}
		}
		function B() {
			try {
				document.documentElement.doScroll("left")
			} catch (H) {
				setTimeout(B, 1);
				return

			}
			F()
		}
		function E() {
			if (C) {
				return

			}
			C = true;
			if (document.addEventListener) {
				document.addEventListener("DOMContentLoaded", G, false);
				window.addEventListener("load", F, false)
			} else {
				if (document.attachEvent) {
					document.attachEvent("onreadystatechange", G);
					window.attachEvent("onload", F);
					var I = false;
					try {
						I = window.frameElement == null
					} catch (H) {
					}
					if (document.documentElement.doScroll && I) {
						B()
					}
				}
			}
		}
		E();
		return function(H) {
			F.isReady ? H() : (D[D.length] = H)
		}
	}();
	A.isReady = false
})();
baidu.dom.remove = function(B) {
	B = baidu.dom._g(B);
	var A = B.parentNode;
	A && A.removeChild(B)
};
baidu.dom.removeClass = function(E, D) {
	E = baidu.dom.g(E);
	var G = E.className.split(/\s+/), C = D.split(/\s+/), A, B = C.length, H, F = 0;
	for (; F < B; ++F) {
		for (H = 0, A = G.length; H < A; ++H) {
			if (G[H] == C[F]) {
				G.splice(H, 1);
				break
			}
		}
	}
	E.className = G.join(" ");
	return E
};
baidu.removeClass = baidu.dom.removeClass;
baidu.dom.removeStyle = function() {
	var A = document.createElement("DIV"), B, C = baidu.dom._g;
	if (A.style.removeProperty) {
		B = function(D, E) {
			D = C(D);
			D.style.removeProperty(E);
			return D
		}
	} else {
		if (A.style.removeAttribute) {
			B = function(D, E) {
				D = C(D);
				D.style.removeAttribute(baidu.string.toCamelCase(E));
				return D
			}
		}
	}
	A = null;
	return B
}();
baidu.object.each = function(C, E) {
	var A, B, D;
	if ("function" == typeof E) {
		for (B in C) {
			if (C.hasOwnProperty(B)) {
				D = C[B];
				A = E.call(C, D, B);
				if (A === false) {
					break
				}
			}
		}
	}
	return C
};
baidu.dom.setStyles = function(A, C) {
	A = baidu.dom.g(A);
	for ( var B in C) {
		baidu.dom.setStyle(A, B, C[B])
	}
	return A
};
baidu.setStyles = baidu.dom.setStyles;
baidu.lang.isNumber = function(A) {
	return "[object Number]" == Object.prototype.toString.call(A)
			&& isFinite(A)
};
baidu.event.getTarget = function(A) {
	return A.target || A.srcElement
};
baidu.dom.setBorderBoxSize = function(D, A) {
	var B = {};
	A.width && (B.width = parseFloat(A.width));
	A.height && (B.height = parseFloat(A.height));
	function C(E, F) {
		return parseFloat(baidu.getStyle(E, F)) || 0
	}
	if (baidu.browser.isStrict) {
		if (A.width) {
			B.width = parseFloat(A.width) - C(D, "paddingLeft")
					- C(D, "paddingRight") - C(D, "borderLeftWidth")
					- C(D, "borderRightWidth");
			B.width < 0 && (B.width = 0)
		}
		if (A.height) {
			B.height = parseFloat(A.height) - C(D, "paddingTop")
					- C(D, "paddingBottom") - C(D, "borderTopWidth")
					- C(D, "borderBottomWidth");
			B.height < 0 && (B.height = 0)
		}
	}
	return baidu.dom.setStyles(D, B)
};
baidu.dom.setOuterHeight = baidu.dom.setBorderBoxHeight = function(A, B) {
	return baidu.dom.setBorderBoxSize(A, {
		height : B
	})
};
baidu.dom.setOuterWidth = baidu.dom.setBorderBoxWidth = function(B, A) {
	return baidu.dom.setBorderBoxSize(B, {
		width : A
	})
};
baidu.dom.resizable = function(W, S) {
	var A, N, Q = {}, X, Z = {}, I, C, F, Y, V, P, L, H = false, E = {
		direction : [ "e", "s", "se" ],
		minWidth : 16,
		minHeight : 16,
		classPrefix : "tangram",
		directionHandlePosition : {}
	};
	if (!(A = baidu.dom.g(W)) && baidu.getStyle(A, "position") == "static") {
		return false
	}
	Y = A.offsetParent;
	var M = baidu.getStyle(A, "position");
	N = baidu.extend(E, S);
	baidu.each([ "minHeight", "minWidth", "maxHeight", "maxWidth" ],
			function(a) {
				N[a] && (N[a] = parseFloat(N[a]))
			});
	I = [ N.minWidth || 0, N.maxWidth || Number.MAX_VALUE, N.minHeight || 0,
			N.maxHeight || Number.MAX_VALUE ];
	B();
	function B() {
		P = baidu.extend({
			e : {
				right : "-5px",
				top : "0px",
				width : "7px",
				height : A.offsetHeight
			},
			s : {
				left : "0px",
				bottom : "-5px",
				height : "7px",
				width : A.offsetWidth
			},
			n : {
				left : "0px",
				top : "-5px",
				height : "7px",
				width : A.offsetWidth
			},
			w : {
				left : "-5px",
				top : "0px",
				height : A.offsetHeight,
				width : "7px"
			},
			se : {
				right : "1px",
				bottom : "1px",
				height : "16px",
				width : "16px"
			},
			sw : {
				left : "1px",
				bottom : "1px",
				height : "16px",
				width : "16px"
			},
			ne : {
				right : "1px",
				top : "1px",
				height : "16px",
				width : "16px"
			},
			nw : {
				left : "1px",
				top : "1px",
				height : "16px",
				width : "16px"
			}
		}, N.directionHandlePosition);
		baidu.each(N.direction, function(a) {
			var d = N.classPrefix.split(" ");
			d[0] = d[0] + "-resizable-" + a;
			var b = baidu.dom.create("div", {
				className : d.join(" ")
			}), c = P[a];
			c.cursor = a + "-resize";
			c.position = "absolute";
			baidu.setStyles(b, c);
			b.key = a;
			b.style.MozUserSelect = "none";
			A.appendChild(b);
			Q[a] = b;
			baidu.on(b, "mousedown", R)
		});
		H = false
	}
	function U() {
		V && G();
		baidu.object.each(Q, function(a) {
			baidu.un(a, "mousedown", R);
			baidu.dom.remove(a)
		});
		H = true
	}
	function O(a) {
		if (!H) {
			N = baidu.extend(N, a || {});
			U();
			B()
		}
	}
	function R(c) {
		var d = baidu.event.getTarget(c), a = d.key;
		V = d;
		if (d.setCapture) {
			d.setCapture()
		} else {
			if (window.captureEvents) {
				window.captureEvents(Event.MOUSEMOVE | Event.MOUSEUP)
			}
		}
		F = baidu.getStyle(document.body, "cursor");
		baidu.setStyle(document.body, "cursor", a + "-resize");
		baidu.on(document, "mouseup", G);
		baidu.on(document.body, "selectstart", K);
		C = document.body.style.MozUserSelect;
		document.body.style.MozUserSelect = "none";
		var b = baidu.page.getMousePosition();
		Z = J();
		L = setInterval(function() {
			D(a, b)
		}, 20);
		baidu.lang.isFunction(N.onresizestart) && N.onresizestart();
		baidu.event.preventDefault(c)
	}
	function G() {
		if (V.releaseCapture) {
			V.releaseCapture()
		} else {
			if (window.releaseEvents) {
				window.releaseEvents(Event.MOUSEMOVE | Event.MOUSEUP)
			}
		}
		baidu.un(document, "mouseup", G);
		baidu.un(document, "selectstart", K);
		document.body.style.MozUserSelect = C;
		baidu.un(document.body, "selectstart", K);
		clearInterval(L);
		baidu.setStyle(document.body, "cursor", F);
		V = null;
		baidu.lang.isFunction(N.onresizeend) && N.onresizeend()
	}
	function D(i, b) {
		var c = baidu.page.getMousePosition(), h = Z.width, a = Z.height, d = Z.top, f = Z.left, g;
		if (i.indexOf("e") >= 0) {
			h = Math.max(c.x - b.x + Z.width, I[0]);
			h = Math.min(h, I[1])
		} else {
			if (i.indexOf("w") >= 0) {
				h = Math.max(b.x - c.x + Z.width, I[0]);
				h = Math.min(h, I[1]);
				f -= h - Z.width
			}
		}
		if (i.indexOf("s") >= 0) {
			a = Math.max(c.y - b.y + Z.height, I[2]);
			a = Math.min(a, I[3])
		} else {
			if (i.indexOf("n") >= 0) {
				a = Math.max(b.y - c.y + Z.height, I[2]);
				a = Math.min(a, I[3]);
				d -= a - Z.height
			}
		}
		g = {
			width : h,
			height : a,
			top : d,
			left : f
		};
		baidu.dom.setOuterHeight(A, a);
		baidu.dom.setOuterWidth(A, h);
		baidu.setStyles(A, {
			top : d,
			left : f
		});
		Q.n && baidu.setStyle(Q.n, "width", h);
		Q.s && baidu.setStyle(Q.s, "width", h);
		Q.e && baidu.setStyle(Q.e, "height", a);
		Q.w && baidu.setStyle(Q.w, "height", a);
		baidu.lang.isFunction(N.onresize) && N.onresize({
			current : g,
			original : Z
		})
	}
	function K(a) {
		return baidu.event.preventDefault(a, false)
	}
	function J() {
		var a = baidu.dom.getPosition(A.offsetParent), d = baidu.dom
				.getPosition(A), b, c;
		if (M == "absolute") {
			b = d.top - (A.offsetParent == document.body ? 0 : a.top);
			c = d.left - (A.offsetParent == document.body ? 0 : a.left)
		} else {
			b = parseFloat(baidu.getStyle(A, "top"))
					|| -parseFloat(baidu.getStyle(A, "bottom")) || 0;
			c = parseFloat(baidu.getStyle(A, "left"))
					|| -parseFloat(baidu.getStyle(A, "right")) || 0
		}
		baidu.setStyles(A, {
			top : b,
			left : c
		});
		return {
			width : A.offsetWidth,
			height : A.offsetHeight,
			top : b,
			left : c
		}
	}
	return {
		cancel : U,
		update : O,
		enable : B
	}
};
baidu.dom.setPosition = function(A, B) {
	return baidu.dom
			.setStyles(A,
					{
						left : B.left
								- (parseFloat(baidu.dom.getStyle(A,
										"margin-left")) || 0),
						top : B.top
								- (parseFloat(baidu.dom.getStyle(A,
										"margin-top")) || 0)
					})
};
baidu.dom.show = function(A) {
	A = baidu.dom.g(A);
	A.style.display = "";
	return A
};
baidu.show = baidu.dom.show;
baidu.dom.toggle = function(A) {
	A = baidu.dom.g(A);
	A.style.display = A.style.display == "none" ? "" : "none";
	return A
};
baidu.dom.toggleClass = function(B, A) {
	if (baidu.dom.hasClass(B, A)) {
		baidu.dom.removeClass(B, A)
	} else {
		baidu.dom.addClass(B, A)
	}
};
baidu.lang.isArray = function(A) {
	return "[object Array]" == Object.prototype.toString.call(A)
};
baidu.lang.toArray = function(A) {
	if (A === null || A === undefined) {
		return []
	}
	if (baidu.lang.isArray(A)) {
		return A
	}
	if (typeof A.length !== "number" || typeof A === "string"
			|| baidu.lang.isFunction(A)) {
		return [ A ]
	}
	if (A.item) {
		var B = A.length, C = new Array(B);
		while (B--) {
			C[B] = A[B]
		}
		return C
	}
	return [].slice.call(A)
};
baidu.fn.methodize = function(A, B) {
	return function() {
		return A.apply(this, [ (B ? this[B] : this) ].concat([].slice
				.call(arguments)))
	}
};
baidu.fn.wrapReturnValue = function(B, C, A) {
	A = A | 0;
	return function() {
		var D = B.apply(this, arguments);
		if (A > 0) {
			return new C(arguments[A - 1])
		}
		if (!A) {
			return new C(D)
		}
		return D
	}
};
baidu.fn.multize = function(C, A, B) {
	var D = function() {
		var E = arguments[0], H = A ? D : C, J = [], F = [].slice.call(
				arguments, 0), I = 0, K, G;
		if (E instanceof Array) {
			for (K = E.length; I < K; I++) {
				F[0] = E[I];
				G = H.apply(this, F);
				if (B) {
					if (G) {
						J = J.concat(G)
					}
				} else {
					J.push(G)
				}
			}
			return J
		} else {
			return C.apply(this, arguments)
		}
	};
	return D
};
baidu.element = baidu.e = function(A) {
	var B = baidu._g(A);
	if (!B && baidu.dom.query) {
		B = baidu.dom.query(A)
	}
	return new baidu.element.Element(B)
};
baidu.element.Element = function(A) {
	if (!baidu.element._init) {
		baidu.element._makeChain();
		baidu.element._init = true
	}
	this._dom = (A.tagName || "").toLowerCase() == "select" ? [ A ]
			: baidu.lang.toArray(A)
};
baidu.element.Element.prototype.each = function(A) {
	baidu.array.each(this._dom, function(C, B) {
		A.call(C, C, B)
	})
};
baidu.element._toChainFunction = function(C, A, B) {
	return baidu.fn.methodize(baidu.fn.wrapReturnValue(baidu.fn
			.multize(C, 0, 1), baidu.element.Element, A), "_dom")
};
baidu.element._makeChain = function() {
	var A = baidu.element.Element.prototype, C = baidu.element._toChainFunction;
	baidu.each(("draggable droppable resizable").split(" "), function(D) {
		A[D] = C(baidu.dom[D], 1)
	});
	baidu
			.each(
					("remove getText contains getAttr getPosition getStyle hasClass intersect hasAttr getComputedStyle")
							.split(" "), function(D) {
						A[D] = A[D.replace(/^get[A-Z]/g, B)] = C(baidu.dom[D],
								-1)
					});
	baidu
			.each(
					("addClass empty hide show insertAfter insertBefore insertHTML removeClass setAttr setAttrs setStyle setStyles show toggleClass toggle next first getAncestorByClass getAncestorBy getAncestorByTag getDocument getParent getWindow last next prev g removeStyle setBorderBoxSize setOuterWidth setOuterHeight setBorderBoxWidth setBorderBoxHeight setPosition children query")
							.split(" "), function(D) {
						A[D] = A[D.replace(/^get[A-Z]/g, B)] = C(baidu.dom[D],
								0)
					});
	A.q = A.Q = C(function(D, E) {
		return baidu.dom.q.apply(this, [ E, D ].concat([].slice.call(arguments,
				2)))
	}, 0);
	baidu.each(("on un").split(" "), function(D) {
		A[D] = C(baidu.event[D], 0)
	});
	baidu
			.each(
					("blur focus focusin focusout load resize scroll unload click dblclick mousedown mouseup mousemove mouseover mouseout mouseenter mouseleave change select submit keydown keypress keyup error")
							.split(" "), function(D) {
						A[D] = function(E) {
							return this.on(D, E)
						}
					});
	function B(D) {
		return D.charAt(3).toLowerCase()
	}
};
baidu.element.extend = function(B) {
	var A = baidu.element;
	baidu.object.each(B, function(C, D) {
		A.Element.prototype[D] = baidu.element._toChainFunction(C, -1)
	})
};
baidu.event.EventArg = function(E, C) {
	C = C || window;
	E = E || C.event;
	var D = C.document;
	this.target = (E.target) || E.srcElement;
	this.keyCode = E.which || E.keyCode;
	for ( var B in E) {
		var A = E[B];
		if ("function" != typeof A) {
			this[B] = A
		}
	}
	if (!this.pageX && this.pageX !== 0) {
		this.pageX = (E.clientX || 0)
				+ (D.documentElement.scrollLeft || D.body.scrollLeft);
		this.pageY = (E.clientY || 0)
				+ (D.documentElement.scrollTop || D.body.scrollTop)
	}
	this._event = E
};
baidu.event.EventArg.prototype.preventDefault = function() {
	if (this._event.preventDefault) {
		this._event.preventDefault()
	} else {
		this._event.returnValue = false
	}
	return this
};
baidu.event.EventArg.prototype.stopPropagation = function() {
	if (this._event.stopPropagation) {
		this._event.stopPropagation()
	} else {
		this._event.cancelBubble = true
	}
	return this
};
baidu.event.EventArg.prototype.stop = function() {
	return this.stopPropagation().preventDefault()
};
baidu.event._eventFilter = baidu.event._eventFilter || {};
baidu.event._eventFilter._crossElementBoundary = function(B, C) {
	var D = C.relatedTarget, A = C.currentTarget;
	if (D === false || A == D
			|| (D && (D.prefix == "xul" || baidu.dom.contains(A, D)))) {
		return

	}
	return B.call(A, C)
};
baidu.fn.bind = function(A, B) {
	var C = arguments.length > 2 ? [].slice.call(arguments, 2) : null;
	return function() {
		var D = baidu.lang.isString(A) ? B[A] : A, E = (C) ? C.concat([].slice
				.call(arguments, 0)) : arguments;
		return D.apply(B || D, E)
	}
};
baidu.event._eventFilter.mouseenter = window.attachEvent ? null : function(B,
		A, C) {
	return {
		type : "mouseover",
		listener : baidu.fn.bind(
				baidu.event._eventFilter._crossElementBoundary, this, C)
	}
};
baidu.event._eventFilter.mouseleave = window.attachEvent ? null : function(B,
		A, C) {
	return {
		type : "mouseout",
		listener : baidu.fn.bind(
				baidu.event._eventFilter._crossElementBoundary, this, C)
	}
};
baidu.event._unload = function() {
	var E = baidu.event._listeners, B = E.length, A = !!window.removeEventListener, C, D;
	while (B--) {
		C = E[B];
		if (C[1] == "unload") {
			continue
		}
		if (!(D = C[0])) {
			continue
		}
		if (D.removeEventListener) {
			D.removeEventListener(C[1], C[3], false)
		} else {
			if (D.detachEvent) {
				D.detachEvent("on" + C[1], C[3])
			}
		}
	}
	if (A) {
		window.removeEventListener("unload", baidu.event._unload, false)
	} else {
		window.detachEvent("onunload", baidu.event._unload)
	}
};
if (window.attachEvent) {
	window.attachEvent("onunload", baidu.event._unload)
} else {
	window.addEventListener("unload", baidu.event._unload, false)
}
baidu.object.values = function(C) {
	var B = [], D = 0, A;
	for (A in C) {
		if (C.hasOwnProperty(A)) {
			B[D++] = C[A]
		}
	}
	return B
};
(function() {
	var I = baidu.browser, B = {
		keydown : 1,
		keyup : 1,
		keypress : 1
	}, L = {
		click : 1,
		dblclick : 1,
		mousedown : 1,
		mousemove : 1,
		mouseup : 1,
		mouseover : 1,
		mouseout : 1
	}, E = {
		abort : 1,
		blur : 1,
		change : 1,
		error : 1,
		focus : 1,
		load : I.ie ? 0 : 1,
		reset : 1,
		resize : 1,
		scroll : 1,
		select : 1,
		submit : 1,
		unload : I.ie ? 0 : 1
	}, G = {
		scroll : 1,
		resize : 1,
		reset : 1,
		submit : 1,
		change : 1,
		select : 1,
		error : 1,
		abort : 1
	}, C = {
		KeyEvents : [ "bubbles", "cancelable", "view", "ctrlKey", "altKey",
				"shiftKey", "metaKey", "keyCode", "charCode" ],
		MouseEvents : [ "bubbles", "cancelable", "view", "detail", "screenX",
				"screenY", "clientX", "clientY", "ctrlKey", "altKey",
				"shiftKey", "metaKey", "button", "relatedTarget" ],
		HTMLEvents : [ "bubbles", "cancelable" ],
		UIEvents : [ "bubbles", "cancelable", "view", "detail" ],
		Events : [ "bubbles", "cancelable" ]
	};
	baidu.object.extend(G, B);
	baidu.object.extend(G, L);
	function J(M, O) {
		var P = 0, Q = M.length, N = {};
		for (; P < Q; P++) {
			N[M[P]] = O[M[P]];
			delete O[M[P]]
		}
		return N
	}
	function H(O, P, Q) {
		Q = baidu.object.extend({}, Q);
		var N = baidu.object.values(J(C[P], Q)), M = document.createEvent(P);
		N.unshift(O);
		if ("KeyEvents" == P) {
			M.initKeyEvent.apply(M, N)
		} else {
			if ("MouseEvents" == P) {
				M.initMouseEvent.apply(M, N)
			} else {
				if ("UIEvents" == P) {
					M.initUIEvent.apply(M, N)
				} else {
					M.initEvent.apply(M, N)
				}
			}
		}
		baidu.object.extend(M, Q);
		return M
	}
	function K(N) {
		var M;
		if (document.createEventObject) {
			M = document.createEventObject();
			baidu.object.extend(M, N)
		}
		return M
	}
	function F(N, Q) {
		Q = J(C.KeyEvents, Q);
		var M;
		if (document.createEvent) {
			try {
				M = H(N, "KeyEvents", Q)
			} catch (O) {
				try {
					M = H(N, "Events", Q)
				} catch (P) {
					M = H(N, "UIEvents", Q)
				}
			}
		} else {
			Q.keyCode = Q.charCode > 0 ? Q.charCode : Q.keyCode;
			M = K(Q)
		}
		return M
	}
	function A(N, O) {
		O = J(C.MouseEvents, O);
		var M;
		if (document.createEvent) {
			M = H(N, "MouseEvents", O);
			if (O.relatedTarget && !M.relatedTarget) {
				if ("mouseout" == N.toLowerCase()) {
					M.toElement = O.relatedTarget
				} else {
					if ("mouseover" == N.toLowerCase()) {
						M.fromElement = O.relatedTarget
					}
				}
			}
		} else {
			O.button = O.button == 0 ? 1 : O.button == 1 ? 4 : baidu.lang
					.isNumber(O.button) ? O.button : 0;
			M = K(O)
		}
		return M
	}
	function D(O, Q) {
		Q.bubbles = G.hasOwnProperty(O);
		Q = J(C.HTMLEvents, Q);
		var M;
		if (document.createEvent) {
			try {
				M = H(O, "HTMLEvents", Q)
			} catch (N) {
				try {
					M = H(O, "UIEvents", Q)
				} catch (P) {
					M = H(O, "Events", Q)
				}
			}
		} else {
			M = K(Q)
		}
		return M
	}
	baidu.event.fire = function(O, N, P) {
		var M;
		N = N.replace(/^on/i, "");
		O = baidu.dom._g(O);
		P = baidu.object.extend({
			bubbles : true,
			cancelable : true,
			view : window,
			detail : 1,
			screenX : 0,
			screenY : 0,
			clientX : 0,
			clientY : 0,
			ctrlKey : false,
			altKey : false,
			shiftKey : false,
			metaKey : false,
			keyCode : 0,
			charCode : 0,
			button : 0,
			relatedTarget : null
		}, P);
		if (B[N]) {
			M = F(N, P)
		} else {
			if (L[N]) {
				M = A(N, P)
			} else {
				if (E[N]) {
					M = D(N, P)
				} else {
					throw (new Error(N + " is not support!"))
				}
			}
		}
		if (M) {
			if (O.dispatchEvent) {
				O.dispatchEvent(M)
			} else {
				if (O.fireEvent) {
					O.fireEvent("on" + N, M)
				}
			}
		}
	}
})();
baidu.event.get = function(B, A) {
	return new baidu.event.EventArg(B, A)
};
baidu.event.getKeyCode = function(A) {
	return A.which || A.keyCode
};
baidu.event.getPageX = function(A) {
	var B = A.pageX, C = document;
	if (!B && B !== 0) {
		B = (A.clientX || 0)
				+ (C.documentElement.scrollLeft || C.body.scrollLeft)
	}
	return B
};
baidu.event.getPageY = function(A) {
	var B = A.pageY, C = document;
	if (!B && B !== 0) {
		B = (A.clientY || 0)
				+ (C.documentElement.scrollTop || C.body.scrollTop)
	}
	return B
};
baidu.event.once = function(B, A, D) {
	B = baidu.dom._g(B);
	function C(E) {
		D.call(B, E);
		baidu.event.un(B, A, C)
	}
	baidu.event.on(B, A, C);
	return B
};
baidu.event.stopPropagation = function(A) {
	if (A.stopPropagation) {
		A.stopPropagation()
	} else {
		A.cancelBubble = true
	}
};
baidu.event.stop = function(B) {
	var A = baidu.event;
	A.stopPropagation(B);
	A.preventDefault(B)
};
baidu.fn.abstractMethod = function() {
	throw Error("unimplemented abstract method")
};
baidu.json = baidu.json || {};
baidu.json.parse = function(A) {
	return (new Function("return (" + A + ")"))()
};
baidu.json.decode = baidu.json.parse;
baidu.json.stringify = (function() {
	var A = {
		"\b" : "\\b",
		"\t" : "\\t",
		"\n" : "\\n",
		"\f" : "\\f",
		"\r" : "\\r",
		'"' : '\\"',
		"\\" : "\\\\"
	};
	function B(F) {
		if (/["\\\x00-\x1f]/.test(F)) {
			F = F.replace(/["\\\x00-\x1f]/g, function(H) {
				var G = A[H];
				if (G) {
					return G
				}
				G = H.charCodeAt();
				return "\\u00" + Math.floor(G / 16).toString(16)
						+ (G % 16).toString(16)
			})
		}
		return '"' + F + '"'
	}
	function D(K) {
		var I = [ "[" ], H = K.length, J, G, F;
		for (G = 0; G < H; G++) {
			F = K[G];
			switch (typeof F) {
			case "undefined":
			case "function":
			case "unknown":
				break;
			default:
				if (J) {
					I.push(",")
				}
				I.push(baidu.json.stringify(F));
				J = 1
			}
		}
		I.push("]");
		return I.join("")
	}
	function E(F) {
		return F < 10 ? "0" + F : F
	}
	function C(F) {
		return '"' + F.getFullYear() + "-" + E(F.getMonth() + 1) + "-"
				+ E(F.getDate()) + "T" + E(F.getHours()) + ":"
				+ E(F.getMinutes()) + ":" + E(F.getSeconds()) + '"'
	}
	return function(F) {
		switch (typeof F) {
		case "undefined":
			return "undefined";
		case "number":
			return isFinite(F) ? String(F) : "null";
		case "string":
			return B(F);
		case "boolean":
			return String(F);
		default:
			if (F === null) {
				return "null"
			} else {
				if (F instanceof Array) {
					return D(F)
				} else {
					if (F instanceof Date) {
						return C(F)
					} else {
						var J = [ "{" ], G = baidu.json.stringify, K, H;
						for ( var I in F) {
							if (Object.prototype.hasOwnProperty.call(F, I)) {
								H = F[I];
								switch (typeof H) {
								case "undefined":
								case "unknown":
								case "function":
									break;
								default:
									if (K) {
										J.push(",")
									}
									K = 1;
									J.push(G(I) + ":" + G(H))
								}
							}
						}
						J.push("}");
						return J.join("")
					}
				}
			}
		}
	}
})();
baidu.json.encode = baidu.json.stringify;
baidu.lang.Class.prototype.addEventListeners = function(E, D) {
	if (typeof D == "undefined") {
		for ( var A in E) {
			this.addEventListener(A, E[A])
		}
	} else {
		E = E.split(",");
		var A = 0, B = E.length, C;
		for (; A < B; A++) {
			this.addEventListener(baidu.trim(E[A]), D)
		}
	}
};
baidu.lang.createClass = function(E, A) {
	A = A || {};
	var F = A.superClass || baidu.lang.Class;
	var G = function() {
		if (F != baidu.lang.Class) {
			F.apply(this, arguments)
		} else {
			F.call(this)
		}
		E.apply(this, arguments)
	};
	G.options = A.options || {};
	var C = function() {
	}, D = E.prototype;
	C.prototype = F.prototype;
	var B = G.prototype = new C();
	for ( var H in D) {
		B[H] = D[H]
	}
	typeof A.className == "string" && (B._className = A.className);
	B.constructor = D.constructor;
	G.extend = function(I) {
		for ( var J in I) {
			G.prototype[J] = I[J]
		}
		return G
	};
	return G
};
baidu.lang.decontrol = function(A) {
	var B = window[baidu.guid];
	B._instances && (delete B._instances[A])
};
baidu.lang.eventCenter = baidu.lang.eventCenter || baidu.lang.createSingle();
baidu.lang.getModule = function(A, E) {
	var D = A.split("."), C = E || window, B;
	for (; B = D.shift();) {
		if (C[B] != null) {
			C = C[B]
		} else {
			return null
		}
	}
	return C
};
baidu.lang.inherits = function(C, E, F) {
	var G, D, B = C.prototype, A = new Function();
	A.prototype = E.prototype;
	D = C.prototype = new A();
	for (G in B) {
		D[G] = B[G]
	}
	C.prototype.constructor = C;
	C.superClass = E.prototype;
	if ("string" == typeof F) {
		D._className = F
	}
};
baidu.inherits = baidu.lang.inherits;
baidu.lang.instance = function(A) {
	return window[baidu.guid]._instances[A] || null
};
baidu.lang.isBoolean = function(A) {
	return typeof A === "boolean"
};
baidu.lang.isDate = function(A) {
	return {}.toString.call(A) === "[object Date]"
			&& A.toString() !== "Invalid Date" && !isNaN(A)
};
baidu.lang.isElement = function(A) {
	return !!(A && A.nodeName && A.nodeType == 1)
};
baidu.lang.isObject = function(A) {
	return "function" == typeof A || !!(A && "object" == typeof A)
};
baidu.isObject = baidu.lang.isObject;
baidu.lang.module = function(name, module, owner) {
	var packages = name.split("."), len = packages.length - 1, packageName, i = 0;
	if (!owner) {
		try {
			if (!(new RegExp("^[a-zA-Z_\x24][a-zA-Z0-9_\x24]*\x24"))
					.test(packages[0])) {
				throw ""
			}
			owner = eval(packages[0]);
			i = 1
		} catch (e) {
			owner = window
		}
	}
	for (; i < len; i++) {
		packageName = packages[i];
		if (!owner[packageName]) {
			owner[packageName] = {}
		}
		owner = owner[packageName]
	}
	if (!owner[packages[len]]) {
		owner[packages[len]] = module
	}
};
baidu.number.comma = function(A, B) {
	if (!B || B < 1) {
		B = 3
	}
	A = String(A).split(".");
	A[0] = A[0].replace(new RegExp("(\\d)(?=(\\d{" + B + "})+$)", "ig"), "$1,");
	return A.join(".")
};
baidu.number.randomInt = function(A, B) {
	return Math.floor(Math.random() * (B - A + 1) + A)
};
baidu.object.isPlain = function(C) {
	var A = Object.prototype.hasOwnProperty, B;
	if (!C || Object.prototype.toString.call(C) !== "[object Object]"
			|| !("isPrototypeOf" in C)) {
		return false
	}
	if (C.constructor && !A.call(C, "constructor")
			&& !A.call(C.constructor.prototype, "isPrototypeOf")) {
		return false
	}
	for (B in C) {
	}
	return B === undefined || A.call(C, B)
};
baidu.object.clone = function(C) {
	var A = C, E, B;
	if (!C || C instanceof Number || C instanceof String
			|| C instanceof Boolean) {
		return A
	} else {
		if (baidu.lang.isArray(C)) {
			A = [];
			var D = 0;
			for (E = 0, B = C.length; E < B; E++) {
				A[D++] = baidu.object.clone(C[E])
			}
		} else {
			if (baidu.object.isPlain(C)) {
				A = {};
				for (E in C) {
					if (C.hasOwnProperty(E)) {
						A[E] = baidu.object.clone(C[E])
					}
				}
			}
		}
	}
	return A
};
baidu.object.isEmpty = function(A) {
	for ( var B in A) {
		return false
	}
	return true
};
baidu.object.keys = function(C) {
	var B = [], D = 0, A;
	for (A in C) {
		if (C.hasOwnProperty(A)) {
			B[D++] = A
		}
	}
	return B
};
baidu.object.map = function(C, D) {
	var A = {};
	for ( var B in C) {
		if (C.hasOwnProperty(B)) {
			A[B] = D(C[B], B)
		}
	}
	return A
};
(function() {
	var A = function(C) {
		return baidu.lang.isObject(C) && !baidu.lang.isFunction(C)
	};
	function B(C, D, E, F, G) {
		if (D.hasOwnProperty(E)) {
			if (G && A(C[E])) {
				baidu.object.merge(C[E], D[E], {
					overwrite : F,
					recursive : G
				})
			} else {
				if (F || !(E in C)) {
					C[E] = D[E]
				}
			}
		}
	}
	baidu.object.merge = function(F, K, D) {
		var I = 0, C = D || {}, G = C.overwrite, E = C.whiteList, J = C.recursive, H;
		if (E && E.length) {
			H = E.length;
			for (; I < H; ++I) {
				B(F, K, E[I], G, J)
			}
		} else {
			for (I in K) {
				B(F, K, I, G, J)
			}
		}
		return F
	}
})();
baidu.page.createStyleSheet = function(B) {
	var C = B || {}, E = C.document || document, F;
	if (baidu.browser.ie) {
		if (!C.url) {
			C.url = ""
		}
		return E.createStyleSheet(C.url, C.index)
	} else {
		F = "<style type='text/css'></style>";
		C.url
				&& (F = "<link type='text/css' rel='stylesheet' href='" + C.url
						+ "'/>");
		baidu.dom.insertHTML(E.getElementsByTagName("HEAD")[0], "beforeEnd", F);
		if (C.url) {
			return null
		}
		var A = E.styleSheets[E.styleSheets.length - 1], D = A.rules
				|| A.cssRules;
		return {
			self : A,
			rules : A.rules || A.cssRules,
			addRule : function(I, G, H) {
				if (A.addRule) {
					return A.addRule(I, G, H)
				} else {
					if (A.insertRule) {
						isNaN(H) && (H = D.length);
						return A.insertRule(I + "{" + G + "}", H)
					}
				}
			},
			removeRule : function(G) {
				if (A.removeRule) {
					A.removeRule(G)
				} else {
					if (A.deleteRule) {
						isNaN(G) && (G = 0);
						A.deleteRule(G)
					}
				}
			}
		}
	}
};
baidu.page.getHeight = function() {
	var C = document, B = C.body, D = C.documentElement, A = C.compatMode == "BackCompat" ? B
			: C.documentElement;
	return Math.max(D.scrollHeight, B.scrollHeight, A.clientHeight)
};
baidu.page.getViewHeight = function() {
	var A = document, B = A.compatMode == "BackCompat" ? A.body
			: A.documentElement;
	return B.clientHeight
};
baidu.page.getViewWidth = function() {
	var A = document, B = A.compatMode == "BackCompat" ? A.body
			: A.documentElement;
	return B.clientWidth
};
baidu.page.getWidth = function() {
	var C = document, B = C.body, D = C.documentElement, A = C.compatMode == "BackCompat" ? B
			: C.documentElement;
	return Math.max(D.scrollWidth, B.scrollWidth, A.clientWidth)
};
baidu.page.lazyLoadImage = function(A) {
	A = A || {};
	A.preloadHeight = A.preloadHeight || 0;
	baidu.dom
			.ready(function() {
				var G = document.getElementsByTagName("IMG"), F = G, E = G.length, H = 0, B = I(), C = "data-tangram-ori-src", D;
				if (A.className) {
					F = [];
					for (; H < E; ++H) {
						if (baidu.dom.hasClass(G[H], A.className)) {
							F.push(G[H])
						}
					}
				}
				function I() {
					return baidu.page.getScrollTop()
							+ baidu.page.getViewHeight() + A.preloadHeight
				}
				for (H = 0, E = F.length; H < E; ++H) {
					D = F[H];
					if (baidu.dom.getPosition(D).top > B) {
						D.setAttribute(C, D.src);
						A.placeHolder ? D.src = A.placeHolder : D
								.removeAttribute("src")
					}
				}
				var J = function() {
					var O = I(), M, L = true, N = 0, K = F.length;
					for (; N < K; ++N) {
						D = F[N];
						M = D.getAttribute(C);
						M && (L = false);
						if (baidu.dom.getPosition(D).top < O && M) {
							D.src = M;
							D.removeAttribute(C);
							baidu.lang.isFunction(A.onlazyload)
									&& A.onlazyload(D)
						}
					}
					L && baidu.un(window, "scroll", J)
				};
				baidu.on(window, "scroll", J)
			})
};
baidu.page.load = function(H, A, F) {
	A = A || {};
	var C = baidu.page.load, J = C._cache = C._cache || {}, D = C._loadingCache = C._loadingCache
			|| {}, E = A.parallel;
	function G() {
		for ( var K = 0, L = H.length; K < L; ++K) {
			if (!J[H[K].url]) {
				setTimeout(arguments.callee, 10);
				return

			}
		}
		A.onload()
	}
	function I(O, M) {
		var N, K, L;
		switch (O.type.toLowerCase()) {
		case "css":
			N = document.createElement("link");
			N.setAttribute("rel", "stylesheet");
			N.setAttribute("type", "text/css");
			break;
		case "js":
			N = document.createElement("script");
			N.setAttribute("type", "text/javascript");
			N.setAttribute("charset", O.charset || C.charset);
			break;
		case "html":
			N = document.createElement("iframe");
			N.frameBorder = "none";
			break;
		default:
			return

		}
		L = function() {
			if (!K
					&& (!this.readyState || this.readyState === "loaded" || this.readyState === "complete")) {
				K = true;
				baidu.un(N, "load", L);
				baidu.un(N, "readystatechange", L);
				M.call(window, N)
			}
		};
		baidu.on(N, "load", L);
		baidu.on(N, "readystatechange", L);
		if (O.type == "css") {
			(function() {
				if (K) {
					return

				}
				try {
					N.sheet.cssRule
				} catch (P) {
					setTimeout(arguments.callee, 20);
					return

				}
				K = true;
				M.call(window, N)
			})()
		}
		N.href = N.src = O.url;
		document.getElementsByTagName("head")[0].appendChild(N)
	}
	baidu.lang.isString(H) && (H = [ {
		url : H
	} ]);
	if (!(H && H.length)) {
		return

	}
	function B(O) {
		var K = O.url, N = !!E, L, M = function(P) {
			J[O.url] = P;
			delete D[O.url];
			if (baidu.lang.isFunction(O.onload)) {
				if (false === O.onload.call(window, P)) {
					return

				}
			}
			!E && C(H.slice(1), A, true);
			if ((!F) && baidu.lang.isFunction(A.onload)) {
				G()
			}
		};
		O.type = O.type || K.substr(K.lastIndexOf(".") + 1);
		O.requestType = O.requestType || (O.type == "html" ? "ajax" : "dom");
		if (L = J[O.url]) {
			M(L);
			return N
		}
		if (!A.refresh && D[O.url]) {
			setTimeout(function() {
				B(O)
			}, 10);
			return N
		}
		D[O.url] = true;
		if (O.requestType.toLowerCase() == "dom") {
			I(O, M)
		} else {
			baidu.ajax.get(O.url, function(P, Q) {
				M(Q)
			})
		}
		return N
	}
	baidu.each(H, B)
};
baidu.page.load.charset = "UTF8";
baidu.page.loadCssFile = function(A) {
	var B = document.createElement("link");
	B.setAttribute("rel", "stylesheet");
	B.setAttribute("type", "text/css");
	B.setAttribute("href", A);
	document.getElementsByTagName("head")[0].appendChild(B)
};
baidu.page.loadJsFile = function(A) {
	var B = document.createElement("script");
	B.setAttribute("type", "text/javascript");
	B.setAttribute("src", A);
	B.setAttribute("defer", "defer");
	document.getElementsByTagName("head")[0].appendChild(B)
};
baidu.platform = baidu.platform || {};
baidu.platform.isAndroid = /android/i.test(navigator.userAgent);
baidu.platform.isIpad = /ipad/i.test(navigator.userAgent);
baidu.platform.isIphone = /iphone/i.test(navigator.userAgent);
baidu.platform.isMacintosh = /macintosh/i.test(navigator.userAgent);
baidu.platform.isWindows = /windows/i.test(navigator.userAgent);
baidu.platform.isX11 = /x11/i.test(navigator.userAgent);
baidu.sio = baidu.sio || {};
baidu.sio._createScriptTag = function(A, B, C) {
	A.setAttribute("type", "text/javascript");
	C && A.setAttribute("charset", C);
	A.setAttribute("src", B);
	document.getElementsByTagName("head")[0].appendChild(A)
};
baidu.sio._removeScriptTag = function(A) {
	if (A.clearAttributes) {
		A.clearAttributes()
	} else {
		for ( var B in A) {
			if (A.hasOwnProperty(B)) {
				delete A[B]
			}
		}
	}
	if (A && A.parentNode) {
		A.parentNode.removeChild(A)
	}
	A = null
};
baidu.sio.callByBrowser = function(J, D, B) {
	var G = document.createElement("SCRIPT"), F = 0, A = B || {}, H = A.charset, C = D
			|| function() {
			}, E = A.timeOut || 0, I;
	G.onload = G.onreadystatechange = function() {
		if (F) {
			return

		}
		var K = G.readyState;
		if ("undefined" == typeof K || K == "loaded" || K == "complete") {
			F = 1;
			try {
				C();
				clearTimeout(I)
			} finally {
				G.onload = G.onreadystatechange = null;
				baidu.sio._removeScriptTag(G)
			}
		}
	};
	if (E) {
		I = setTimeout(function() {
			G.onload = G.onreadystatechange = null;
			baidu.sio._removeScriptTag(G);
			A.onfailure && A.onfailure()
		}, E)
	}
	baidu.sio._createScriptTag(G, J, H)
};
baidu.sio.callByServer = function(O, C, B) {
	var G = document.createElement("SCRIPT"), H = "bd__cbs__", E, K, A = B
			|| {}, L = A.charset, J = A.queryField || "callback", D = A.timeOut || 0, N, M = new RegExp(
			"(\\?|&)" + J + "=([^&]*)"), I;
	if (baidu.lang.isFunction(C)) {
		E = H + Math.floor(Math.random() * 2147483648).toString(36);
		window[E] = F(0)
	} else {
		if (baidu.lang.isString(C)) {
			E = C
		} else {
			if (I = M.exec(O)) {
				E = I[2]
			}
		}
	}
	if (D) {
		N = setTimeout(F(1), D)
	}
	O = O.replace(M, "\x241" + J + "=" + E);
	if (O.search(M) < 0) {
		O += (O.indexOf("?") < 0 ? "?" : "&") + J + "=" + E
	}
	baidu.sio._createScriptTag(G, O, L);
	function F(P) {
		return function() {
			try {
				if (P) {
					A.onfailure && A.onfailure()
				} else {
					C.apply(window, arguments);
					clearTimeout(N)
				}
				window[E] = null;
				delete window[E]
			} catch (Q) {
			} finally {
				baidu.sio._removeScriptTag(G)
			}
		}
	}
};
baidu.sio.log = function(A) {
	var B = new Image(), C = "tangram_sio_log_"
			+ Math.floor(Math.random() * 2147483648).toString(36);
	window[C] = B;
	B.onload = B.onerror = B.onabort = function() {
		B.onload = B.onerror = B.onabort = null;
		window[C] = null;
		B = null
	};
	B.src = A
};
baidu.string.decodeHTML = function(B) {
	var A = String(B).replace(/&quot;/g, '"').replace(/&lt;/g, "<").replace(
			/&gt;/g, ">").replace(/&amp;/g, "&");
	return A.replace(/&#([\d]+);/g, function(C, D) {
		return String.fromCharCode(parseInt(D, 10))
	})
};
baidu.decodeHTML = baidu.string.decodeHTML;
baidu.string.encodeHTML = function(A) {
	return String(A).replace(/&/g, "&amp;").replace(/</g, "&lt;").replace(/>/g,
			"&gt;").replace(/"/g, "&quot;").replace(/'/g, "&#39;")
};
baidu.encodeHTML = baidu.string.encodeHTML;
baidu.string.filterFormat = function(D, B) {
	var A = Array.prototype.slice.call(arguments, 1), C = Object.prototype.toString;
	if (A.length) {
		A = A.length == 1 ? (B !== null
				&& (/\[object Array\]|\[object Object\]/.test(C.call(B))) ? B
				: A) : A;
		return D.replace(/#\{(.+?)\}/g, function(J, G) {
			var E, H, I, K, F;
			if (!A) {
				return ""
			}
			E = G.split("|");
			H = A[E[0]];
			if ("[object Function]" == C.call(H)) {
				H = H(E[0])
			}
			for (I = 1, K = E.length; I < K; ++I) {
				F = baidu.string.filterFormat[E[I]];
				if ("[object Function]" == C.call(F)) {
					H = F(H)
				}
			}
			return (("undefined" == typeof H || H === null) ? "" : H)
		})
	}
	return D
};
baidu.string.filterFormat.escapeJs = function(C) {
	if (!C || "string" != typeof C) {
		return C
	}
	var D, B, A, E = [];
	for (D = 0, B = C.length; D < B; ++D) {
		A = C.charCodeAt(D);
		if (A > 255) {
			E.push(C.charAt(D))
		} else {
			E.push("\\x" + A.toString(16))
		}
	}
	return E.join("")
};
baidu.string.filterFormat.js = baidu.string.filterFormat.escapeJs;
baidu.string.filterFormat.escapeString = function(A) {
	if (!A || "string" != typeof A) {
		return A
	}
	return A.replace(/["'<>\\\/`]/g, function(B) {
		return "&#" + B.charCodeAt(0) + ";"
	})
};
baidu.string.filterFormat.e = baidu.string.filterFormat.escapeString;
baidu.string.filterFormat.toInt = function(A) {
	return parseInt(A, 10) || 0
};
baidu.string.filterFormat.i = baidu.string.filterFormat.toInt;
baidu.string.format = function(D, B) {
	D = String(D);
	var A = Array.prototype.slice.call(arguments, 1), C = Object.prototype.toString;
	if (A.length) {
		A = A.length == 1 ? (B !== null
				&& (/\[object Array\]|\[object Object\]/.test(C.call(B))) ? B
				: A) : A;
		return D.replace(/#\{(.+?)\}/g, function(G, E) {
			var F = A[E];
			if ("[object Function]" == C.call(F)) {
				F = F(E)
			}
			return ("undefined" == typeof F ? "" : F)
		})
	}
	return D
};
baidu.format = baidu.string.format;
(function() {
	var C = /^\#[\da-f]{6}$/i, A = /^rgb\((\d+), (\d+), (\d+)\)$/, B = {
		black : "#000000",
		silver : "#c0c0c0",
		gray : "#808080",
		white : "#ffffff",
		maroon : "#800000",
		red : "#ff0000",
		purple : "#800080",
		fuchsia : "#ff00ff",
		green : "#008000",
		lime : "#00ff00",
		olive : "#808000",
		yellow : "#ffff0",
		navy : "#000080",
		blue : "#0000ff",
		teal : "#008080",
		aqua : "#00ffff"
	};
	baidu.string.formatColor = function(H) {
		if (C.test(H)) {
			return H
		} else {
			if (A.test(H)) {
				for ( var D, E = 1, H = "#"; E < 4; E++) {
					D = parseInt(RegExp["\x24" + E]).toString(16);
					H += ("00" + D).substr(D.length)
				}
				return H
			} else {
				if (/^\#[\da-f]{3}$/.test(H)) {
					var F = H.charAt(1), G = H.charAt(2), I = H.charAt(3);
					return "#" + F + F + G + G + I + I
				} else {
					if (B[H]) {
						return B[H]
					}
				}
			}
		}
		return ""
	}
})();
baidu.string.getByteLength = function(A) {
	return String(A).replace(/[^\x00-\xff]/g, "ci").length
};
baidu.string.stripTags = function(A) {
	return String(A || "").replace(/<[^>]+>/g, "")
};
baidu.string.subByte = function(C, A, B) {
	C = String(C);
	B = B || "";
	if (A < 0 || baidu.string.getByteLength(C) <= A) {
		return C + B
	}
	C = C.substr(0, A).replace(/([^\x00-\xff])/g, "\x241 ").substr(0, A)
			.replace(/[^\x00-\xff]$/, "").replace(/([^\x00-\xff]) /g, "\x241");
	return C + B
};
baidu.string.toHalfWidth = function(A) {
	return String(A).replace(/[\uFF01-\uFF5E]/g, function(B) {
		return String.fromCharCode(B.charCodeAt(0) - 65248)
	}).replace(/\u3000/g, " ")
};
baidu.string.wbr = function(A) {
	return String(A).replace(/(?:<[^>]+>)|(?:&#?[0-9a-z]{2,6};)|(.{1})/gi,
			"$&<wbr>").replace(/><wbr>/g, ">")
};
baidu.swf = baidu.swf || {};
baidu.swf.getMovie = function(C) {
	var B = document[C], A;
	return baidu.browser.ie == 9 ? B && B.length ? (A = baidu.array.remove(
			baidu.lang.toArray(B), function(D) {
				return D.tagName.toLowerCase() != "embed"
			})).length == 1 ? A[0] : A : B : B || window[C]
};
baidu.swf.Proxy = function(C, F, E) {
	var A = this, B = this._flash = baidu.swf.getMovie(C), D;
	if (!F) {
		return this
	}
	D = setInterval(function() {
		try {
			if (B[F]) {
				A._initialized = true;
				clearInterval(D);
				if (E) {
					E()
				}
			}
		} catch (G) {
		}
	}, 100)
};
baidu.swf.Proxy.prototype.getFlash = function() {
	return this._flash
};
baidu.swf.Proxy.prototype.isReady = function() {
	return !!this._initialized
};
baidu.swf.Proxy.prototype.call = function(B, C) {
	try {
		var E = this.getFlash(), A = Array.prototype.slice.call(arguments);
		A.shift();
		if (E[B]) {
			E[B].apply(E, A)
		}
	} catch (D) {
	}
};
baidu.swf.version = (function() {
	var C = navigator;
	if (C.plugins && C.mimeTypes.length) {
		var F = C.plugins["Shockwave Flash"];
		if (F && F.description) {
			return F.description.replace(/([a-zA-Z]|\s)+/, "").replace(
					/(\s)+r/, ".")
					+ ".0"
		}
	} else {
		if (window.ActiveXObject && !window.opera) {
			for ( var A = 12; A >= 2; A--) {
				try {
					var D = new ActiveXObject("ShockwaveFlash.ShockwaveFlash."
							+ A);
					if (D) {
						var B = D.GetVariable("$version");
						return B.replace(/WIN/g, "").replace(/,/g, ".")
					}
				} catch (E) {
				}
			}
		}
	}
})();
baidu.swf.createHTML = function(P) {
	P = P || {};
	var G = baidu.swf.version, I = P.ver || "6.0.0", J, L, K, M, H, Q, O = {}, C = baidu.string.encodeHTML;
	for (M in P) {
		O[M] = P[M]
	}
	P = O;
	if (G) {
		G = G.split(".");
		I = I.split(".");
		for (K = 0; K < 3; K++) {
			J = parseInt(G[K], 10);
			L = parseInt(I[K], 10);
			if (L < J) {
				break
			} else {
				if (L > J) {
					return ""
				}
			}
		}
	} else {
		return ""
	}
	var E = P.vars, F = [ "classid", "codebase", "id", "width", "height",
			"align" ];
	P.align = P.align || "middle";
	P.classid = "clsid:d27cdb6e-ae6d-11cf-96b8-444553540000";
	P.codebase = "http://fpdownload.macromedia.com/pub/shockwave/cabs/flash/swflash.cab#version=6,0,0,0";
	P.movie = P.url || "";
	delete P.vars;
	delete P.url;
	if ("string" == typeof E) {
		P.flashvars = E
	} else {
		var B = [];
		for (M in E) {
			Q = E[M];
			B.push(M + "=" + encodeURIComponent(Q))
		}
		P.flashvars = B.join("&")
	}
	var D = [ "<object " ];
	for (K = 0, H = F.length; K < H; K++) {
		Q = F[K];
		D.push(" ", Q, '="', C(P[Q]), '"')
	}
	D.push(">");
	var N = {
		wmode : 1,
		scale : 1,
		quality : 1,
		play : 1,
		loop : 1,
		menu : 1,
		salign : 1,
		bgcolor : 1,
		base : 1,
		allowscriptaccess : 1,
		allownetworking : 1,
		allowfullscreen : 1,
		seamlesstabbing : 1,
		devicefont : 1,
		swliveconnect : 1,
		flashvars : 1,
		movie : 1
	};
	for (M in P) {
		Q = P[M];
		M = M.toLowerCase();
		if (N[M] && (Q || Q === false || Q === 0)) {
			D.push('<param name="' + M + '" value="' + C(Q) + '" />')
		}
	}
	P.src = P.movie;
	P.name = P.id;
	delete P.id;
	delete P.movie;
	delete P.classid;
	delete P.codebase;
	P.type = "application/x-shockwave-flash";
	P.pluginspage = "http://www.macromedia.com/go/getflashplayer";
	D.push("<embed");
	var A;
	for (M in P) {
		Q = P[M];
		if (Q || Q === false || Q === 0) {
			if ((new RegExp("^salign\x24", "i")).test(M)) {
				A = Q;
				continue
			}
			D.push(" ", M, '="', C(Q), '"')
		}
	}
	if (A) {
		D.push(' salign="', C(A), '"')
	}
	D.push("></embed></object>");
	return D.join("")
};
baidu.swf.create = function(B, C) {
	B = B || {};
	var A = baidu.swf.createHTML(B) || B.errorMessage || "";
	if (C && "string" == typeof C) {
		C = document.getElementById(C)
	}
	if (C) {
		C.innerHTML = A
	} else {
		document.write(A)
	}
};
baidu.url = baidu.url || {};
baidu.url.escapeSymbol = function(A) {
	return String(A).replace(/\%/g, "%25").replace(/&/g, "%26").replace(/\+/g,
			"%2B").replace(/\ /g, "%20").replace(/\//g, "%2F").replace(/\#/g,
			"%23").replace(/\=/g, "%3D")
};
baidu.url.getQueryValue = function(A, D) {
	var C = new RegExp("(^|&|\\?|#)" + baidu.string.escapeReg(D)
			+ "=([^&#]*)(&|\x24|#)", "");
	var B = A.match(C);
	if (B) {
		return B[2]
	}
	return null
};
baidu.url.jsonToQuery = function(E, C) {
	var B = [], D, A = C || function(F) {
		return baidu.url.escapeSymbol(F)
	};
	baidu.object.each(E, function(F, G) {
		if (baidu.lang.isArray(F)) {
			D = F.length;
			while (D--) {
				B.push(G + "=" + A(F[D], G))
			}
		} else {
			B.push(G + "=" + A(F, G))
		}
	});
	return B.join("&")
};
baidu.url.queryToJson = function(J) {
	var E = J.substr(J.lastIndexOf("?") + 1), H = E.split("&"), F = H.length, A = {}, G = 0, C, D, B, I;
	for (; G < F; G++) {
		if (!H[G]) {
			continue
		}
		I = H[G].split("=");
		C = I[0];
		D = I[1];
		B = A[C];
		if ("undefined" == typeof B) {
			A[C] = D
		} else {
			if (baidu.lang.isArray(B)) {
				B.push(D)
			} else {
				A[C] = [ B, D ]
			}
		}
	}
	return A
};
baidu.async = baidu.async || {};
baidu.async._isDeferred = function(A) {
	var B = baidu.lang.isFunction;
	return A && B(A.success) && B(A.then) && B(A.fail) && B(A.cancel)
};
baidu.async.Deferred = function() {
	var A = this;
	baidu.extend(A, {
		_fired : 0,
		_firing : 0,
		_cancelled : 0,
		_resolveChain : [],
		_rejectChain : [],
		_result : [],
		_isError : 0
	});
	function B() {
		if (A._cancelled || A._firing) {
			return

		}
		if (A._nextDeferred) {
			A._nextDeferred.then(A._resolveChain[0], A._rejectChain[0]);
			return

		}
		A._firing = 1;
		var C = A._isError ? A._rejectChain : A._resolveChain, F = A._result[A._isError ? 1
				: 0];
		while (C[0] && (!A._cancelled)) {
			try {
				var E = C.shift().call(A, F);
				if (baidu.async._isDeferred(E)) {
					A._nextDeferred = E;
					[].push.apply(E._resolveChain, A._resolveChain);
					[].push.apply(E._rejectChain, A._rejectChain);
					C = A._resolveChain = [];
					A._rejectChain = []
				}
			} catch (D) {
				throw D
			} finally {
				A._fired = 1;
				A._firing = 0
			}
		}
	}
	A.resolve = A.fireSuccess = function(C) {
		A._result[0] = C;
		B();
		return A
	};
	A.reject = A.fireFail = function(C) {
		A._result[1] = C;
		A._isError = 1;
		B();
		return A
	};
	A.then = function(D, C) {
		A._resolveChain.push(D);
		A._rejectChain.push(C);
		if (A._fired) {
			B()
		}
		return A
	};
	A.success = function(C) {
		return A.then(C, baidu.fn.blank)
	};
	A.fail = function(C) {
		return A.then(baidu.fn.blank, C)
	};
	A.cancel = function() {
		A._cancelled = 1
	}
};
baidu.async.get = function(A) {
	var B = new baidu.async.Deferred();
	baidu.ajax.request(A, {
		onsuccess : function(C, D) {
			B.resolve({
				xhr : C,
				responseText : D
			})
		},
		onfailure : function(C) {
			B.reject({
				xhr : C
			})
		}
	});
	return B
};
baidu.async.post = function(A, C) {
	var B = new baidu.async.Deferred();
	baidu.ajax.request(A, {
		method : "POST",
		data : C,
		onsuccess : function(D, E) {
			B.resolve({
				xhr : D,
				responseText : E
			})
		},
		onfailure : function(D) {
			B.reject({
				xhr : D
			})
		}
	});
	return B
};
baidu.async.when = function(D, A, C) {
	if (baidu.async._isDeferred(D)) {
		D.then(A, C);
		return D
	}
	var B = new baidu.async.Deferred();
	B.then(A, C).resolve(D);
	return B
};
(function(D, E) {
	if (!(T && baidu)) {
		return false
	}
	var B = D.WenkuApp, A = D.WenkuApp = T.object.clone(T && baidu);
	var C = A.mixin = function(J, I, H) {
		if (T.lang.isArray(I)) {
			for ( var G = 0, F = I.length; G < F; G++) {
				C(J, I[G], H)
			}
			return J
		}
		for ( var K in I) {
			if (H || !(J[G] || (G in J))) {
				J[K] = I[K]
			}
		}
		return J
	};
	A.mixin(A, {
		punch : function(I, K, G, J) {
			var F;
			if (A.lang.isString(J) && !/^(before|after)$/.test(J)) {
				return function() {
					console.error("auto arguments error!");
					return false
				}
			}
			if (A.lang.isFunction(I)) {
				F = I;
				J = G;
				G = K;
				K = F;
				I = null
			} else {
				F = I[K]
			}
			var H = {
				after : function() {
					F.apply(I, arguments);
					return G.apply(I, arguments)
				},
				before : function() {
					G.apply(I, arguments);
					return F.apply(I, arguments)
				}
			};
			return J && (H.hasOwnProperty(J)) ? H[J] : function() {
				var L = [].slice.call(arguments, 0);
				L.unshift(function() {
					return F.apply(I, arguments)
				});
				return G.apply(I, L)
			}
		},
		noConflict : function() {
			D.WenkuApp = B;
			return A
		}
	});
	baidu.on = function(M, K, J, L) {
		if (!(M = baidu._g(M)) || typeof J != "function") {
			return M
		}
		K = K.replace(/^on/i, "").toLowerCase();
		function G(N) {
			return N || window.event
		}
		var I = function(N) {
			J.call(I.src, G(N))
		};
		I.src = M;
		var H = baidu.on._listeners;
		var F = [ M, K, J, I ];
		H[H.length] = F;
		if (M.addEventListener) {
			M.addEventListener(K, I, false)
		} else {
			if (M.attachEvent) {
				M.attachEvent("on" + K, I)
			}
		}
		return M
	};
	baidu.on._listeners = [];
	baidu.un = function(G, K, O) {
		if (!(G = this.G(G)) || typeof O != "function") {
			return G
		}
		K = K.replace(/^on/i, "").toLowerCase();
		var L = baidu.on._listeners;
		if (!O) {
			var M;
			for ( var H = 0, J = L.length; H < J; H++) {
				M = L[H];
				if (M && M[0] === G && M[1] === K) {
					baidu.un(G, K, M[2])
				}
			}
			return G
		}
		function F(S, R, V) {
			for ( var U = 0, Q = L.length; U < Q; ++U) {
				var P = L[U];
				if (P && P[2] === O && P[0] === G && P[1] === K) {
					return U
				}
			}
			return -1
		}
		var I = F(G, K, O);
		var N = null;
		if (I >= 0) {
			N = L[I]
		} else {
			return G
		}
		if (G.removeEventListener) {
			G.removeEventListener(K, N[3], false)
		} else {
			if (G.detachEvent) {
				G.detachEvent("on" + K, N[3])
			}
		}
		delete N[3];
		delete N[2];
		L.splice(I, 1);
		return G
	};
	baidu.body = function() {
		var F = 0, N = 0, K = 0, I = 0, G = 0, O = 0;
		var L = window, J = document, M = J.documentElement;
		F = M.clientWidth || J.body.clientWidth;
		N = L.innerHeight || M.clientHeight || J.body.clientHeight;
		I = J.body.scrollTop || M.scrollTop;
		K = J.body.scrollLeft || M.scrollLeft;
		G = Math.max(J.body.scrollWidth, M.scrollWidth || 0);
		O = Math.max(J.body.scrollHeight, M.scrollHeight || 0, N);
		return {
			scrollTop : I,
			scrollLeft : K,
			documentWidth : G,
			documentHeight : O,
			viewWidth : F,
			viewHeight : N
		}
	};
	baidu.stopPropagation = function(F) {
		F = F || window.event;
		if (F.stopPropagation) {
			F.stopPropagation()
		} else {
			F.cancelBubble = true
		}
	};
	baidu.hide = function() {
		baidu.each(arguments, function(F) {
			if (F = baidu.G(F)) {
				F.style.display = "none"
			}
		})
	};
	baidu.show = function() {
		baidu.each(arguments, function(F) {
			if (F = baidu.G(F)) {
				F.style.display = ""
			}
		})
	};
	baidu.each = function(K, G) {
		if (typeof G != "function") {
			return K
		}
		if (K) {
			var F;
			if (K.length === E) {
				for ( var H in K) {
					if (H in {}) {
						continue
					}
					F = G.call(K[H], K[H], H);
					if (F == "break") {
						break
					}
				}
			} else {
				for ( var I = 0, J = K.length; I < J; I++) {
					F = G.call(K[I], K[I], I);
					if (F == "break") {
						break
					}
				}
			}
		}
		return K
	};
	baidu.trim = function(G, F) {
		if (F == "left") {
			return G.replace(/(^[\s\t\xa0\u3000]+)/g, "")
		}
		if (F == "right") {
			return G.replace(new RegExp("[\\u3000\\xa0\\s\\t]+\x24", "g"), "")
		}
		return G.replace(new RegExp(
				"(^[\\s\\t\\xa0\\u3000]+)|([\\u3000\\xa0\\s\\t]+\x24)", "g"),
				"")
	};
	baidu.getCurrentStyle = function(F, H) {
		var J = null;
		if (!(F = baidu.G(F))) {
			return null
		}
		if (J = F.style[H]) {
			return J
		} else {
			if (F.currentStyle) {
				J = F.currentStyle[H]
			} else {
				var I = F.nodeType == 9 ? F : F.ownerDocument || F.document;
				if (I.defaultView && I.defaultView.getComputedStyle) {
					var G = I.defaultView.getComputedStyle(F, "");
					if (G) {
						J = G[H]
					}
				}
			}
		}
		return J
	};
	baidu.isElement = function(F) {
		if (F === E || F === null) {
			return false
		}
		return F && F.nodeName && F.nodeType == 1
	};
	baidu.isDocument = function(F) {
		if (F === E || F === null) {
			return false
		}
		return F && F.nodeType == 9
	};
	baidu.swf.getVersion = A.punch(baidu.swf, "getVersion", function() {
		var J = navigator;
		if (J.plugins && J.mimeTypes.length) {
			var F = J.plugins["Shockwave Flash"];
			if (F && F.description) {
				return F.description.replace(/([a-zA-Z]|\s)+/, "").replace(
						/(\s)+r/, ".")
						+ ".0"
			}
		} else {
			if (window.ActiveXObject && !window.opera) {
				for ( var G = 10; G >= 2; G--) {
					try {
						var I = new ActiveXObject(
								"ShockwaveFlash.ShockwaveFlash." + G);
						if (I) {
							return G + ".0.0";
							break
						}
					} catch (H) {
					}
				}
			}
		}
	});
	baidu.ac = baidu.addClass;
	baidu.rc = baidu.removeClass;
	baidu.ajax.get = function(G, F) {
		return baidu.ajax.request(G, {
			onsuccess : F,
			noCache : true
		})
	};
	A.ajax.get = baidu.ajax.get
})(window);
function G(A) {
	return baidu.G(A)
}
baidu.preventDefault = function(A) {
	A = A || window.event;
	if (A.preventDefault) {
		A.preventDefault()
	} else {
		A.returnValue = false
	}
};
baidu.stopPropagation = function(A) {
	A = A || window.event;
	if (A.stopPropagation) {
		A.stopPropagation()
	} else {
		A.cancelBubble = true
	}
};
baidu.GT = function(A, B) {
	var C = B || document;
	return C.getElementsByTagName(A)
};
baidu.ges = function(B, C, H) {
	var I = (H) ? G(H) : document;
	var E = baidu.GT(B, I);
	var F = [];
	for ( var D = 0; D < E.length; D++) {
		for ( var A in C) {
			if (!(E[D][A] && C[A] == E[D][A])) {
				break
			}
			F.push(E[D])
		}
	}
	if (F.length == 0) {
		return false
	} else {
		if (F.length == 1) {
			return F[0]
		}
	}
	return F
};
baidu.loadJS = function(A, D) {
	if (!A) {
		return

	}
	var B = baidu.GT("head")[0];
	var C = document.createElement("script");
	C.type = "text/javascript";
	C.src = A;
	if (D) {
		if (D.id) {
			if (G(D.id) && G(D.id).tagName.toLowerCase() == "script") {
				baidu.rm(D.id)
			}
			C.id = D.id
		}
	}
	B.appendChild(C);
	C = null
};
baidu.proxy = function() {
	var C = [];
	for ( var B = 0, A = arguments.length; B < A; B++) {
		C[B] = arguments[B]
	}
	var D = C[0];
	C.shift();
	return function() {
		D.apply(null, C)
	}
};
baidu.breakWord = function(A, B) {
	baidu.each(A, function(F) {
		if (F.offsetWidth > B) {
			var C = 1, E = F.innerHTML;
			while (F.offsetWidth > B) {
				var D = "";
				D += E.substr(0, E.length - C) + "<br>";
				D += E.substr(E.length - C, E.length);
				F.innerHTML = D;
				C++
			}
		}
	})
};
baidu.insertWbr = function(D, A) {
	if (D.length <= A) {
		return D
	}
	var B = document.createElement("textarea");
	try {
		B.innerHTML = D
	} catch (E) {
		B.value = D
	}
	var C = B.value;
	C = C.replace(new RegExp("(\\S{" + A + "})", "img"), "$1<wbr>");
	C = C.replace(/<wbr>/img, "\u0001").replace(/<br>/img, "\u0002").replace(
			/</img, "&lt;").replace(/>/img, "&gt;")
			.replace(/\u0002/img, "<br>").replace(/\u0001/img, "<wbr>");
	return C
};
baidu.C = function(C, B) {
	var A = document.createElement(C);
	for (p in B) {
		A[p] = B[p]
	}
	return A
};
baidu.rm = function() {
	baidu.each(arguments, function(A) {
		if (A = baidu.G(A)) {
			A.parentNode.removeChild(A)
		}
	})
};
baidu.sug = function(A) {
	sug_opt.getdata(A.q, A.s);
	return {
		query : A.q,
		arr : A.s
	}
};
String.prototype.byteLength = function() {
	return this.replace(/[^\u0000-\u007f]/g, "\u0061\u0061").length
};
String.prototype.trim = function() {
	return this.replace(/(^[\s\t\xa0\u3000]+)|([\u3000\xa0\s\t]+$)/g, "")
};
function $(A) {
	if (/^#/.test(A)) {
	}
}
baidu.extend = function(C, A) {
	for ( var B in A) {
		if (A.hasOwnProperty(B)) {
			C[B] = A[B]
		}
	}
	return C
};
baidu.enableCustomEvent = function(A) {
	var B = {
		addEvent : function(E, D) {
			if (typeof D != "function") {
				return

			}
			!this.__listeners && (this.__listeners = {});
			var C = this.__listeners;
			typeof C[E] != "object" && (C[E] = []);
			C[E].push(D)
		},
		removeEvent : function(F, E) {
			var D = this.__listeners;
			if (!D[F]) {
				return

			}
			if (!E) {
				D[F] = []
			}
			for ( var C in D[F]) {
				if (D[F][C] == E) {
					D[F][C] = null
				}
			}
		},
		fireEvent : function(E) {
			!this.__listeners && (this.__listeners = {});
			var D = this.__listeners;
			if (typeof D[E] == "object") {
				for ( var C in D[E]) {
					D[E][C] && D[E][C].apply(this, arguments)
				}
			}
		}
	};
	baidu.extend(A, B)
};
document
		.write('<style>#shadowDiv{display:none;width:100%;height:100%;position:absolute;top:0:left:0;background:#000;filter: alpha(opacity=25);opacity:0.25;z-index:65534}#popDiv{display:none;position:absolute;width:450px;height:300px;z-index:65535;background:#fff;line-height:20px}#popBody{display:none;text-align:center;padding:17px;font-size:14px}.pop_r1,.pop_r2,.pop_r3,.pop_r4{width:10px;height:5px;display:bolck;overflow:hidden;background:url(http://img.baidu.com/img/iknow/docshare/bg_popup.gif)}.pop_r1{background-position:0 -62px;float:left;margin-top:-3px}.pop_r2{background-position:-10px -62px;float:right;margin-top:-3px}.pop_r3{height:10px;background-position:0 -67px;float:left}.pop_r4{height:10px;background-position:-10px -67px;float:right}.pop_bottom{background:url(http://img.baidu.com/img/iknow/docshare/bg_popup.gif) repeat-x 0 -82px}.pop_bg1{width:10px;background:url(http://img.baidu.com/img/iknow/docshare/bg_popup2.gif) repeat-y}.pop_bg2{width:10px;background:url(http://img.baidu.com/img/iknow/docshare/bg_popup2.gif) repeat-y right center}.pop_holder1{width:100%;height:100%;background:#fff;border:2px solid #68995a;position:absolute;top:0;left:0;z-index:1}.pop_holder2{width:100%;height:100%;background:#000;position:absolute;left:7px;top:7px;filter:alpha(opacity=25);opacity:0.25}.pop_title{height:33px;line-height:33px;padding:0 10px;color:#333;font-size:14px;font-weight:bold;background:url(http://img.baidu.com/img/iknow/docshare/bg_popup.gif) repeat-x}.pop_close{width:19px;height:19px;display:block;float:right;background:url(http://img.baidu.com/img/iknow/docshare/bg_popup.gif) no-repeat 0 -38px;border:0;margin:5px 0 0 0;padding:0;cursor:pointer}/*pop v2 style*/.v2 .pop_r1,.v2 .pop_r2,.v2 .pop_r3,.v2 .pop_r4{display:none;}.v2 .pop_bg1,.v2 .pop_bg2,.v2 .pop_bottom {display:none;}.v2 .pop_holder1{ border:none;border-top:5px solid #2f805d;background:#fafafa; }.v2 .pop_head { padding:15px 25px 0 25px;}.v2 .pop_title{ background:none;border-bottom:1px solid #E5E5E5;padding:0; }.v2 .pop_close{ width:15px;height:15px;background:url(http://img.baidu.com/img/iknow/docshare/btn_wk_pop_close.png) no-repeat 0 0; }.v2 .pop_close:hover{ background-position:-15px 0; }.v2 #popContent { background:#fafafa; }.v2 #ifrDiv { margin:0 25px; }.v2 .ok,.v2 .cancle{    background: url(http://img.baidu.com/img/iknow/wenku/ugc/icon-class-ok.png) left top no-repeat;    display: inline-block;    width: 82px;    height: 26px;    margin: 0 15px;	cursor: pointer;}.v2 .ok{    background-position: -1px top;}.v2 .cancle{    background-position: -86px top;}.v2 .newpopbt{    text-align: center;	padding-top: 15px;}#popBody p{    text-align: left;    padding-left: 10px;}.v2 .pop_holder2 { display:none; }/*tasknote style*/.tasknote{padding-left:10px;position:absolute;z-index:100;}.tasknote .arrow{background:url(http://img.baidu.com/img/iknow/task/arrow.gif) 0 0 no-repeat;overflow:hidden;position:absolute;top:9px;left:0;z-index:1;}.tasknote .side{width:11px;height:21px;}.tasknote .up{top:0;left:40px;width:21px;height:11px;}.tasknote .down{bottom:2px;top:auto;left:40px;width:21px;height:11px;background-position:0 -10px}.tasknotebody{background:#FFF9E3; border:1px solid #FBE5A3; width:100px; padding-left:4px; font-size:12px; }.noticeInfo{background:url(http://img.baidu.com/img/iknow/task/note.gif) no-repeat 0 center; padding-left:20px; margin:8px 8px 0; line-height:22px; }.noticelist{ margin:0;padding:0; margin-left:26px; }.noticelist li{ margin:0;padding:0; line-height:22px; }.tasknote .close{width:13px;height:13px;background:url(http://img.baidu.com/img/iknow/task/close.gif) no-repeat  0 0;overflow:hidden;position:absolute;top:2px;right:2px;cursor:pointer;z-index:2};/* tangram-dialog */.tangram-dialog-main{position:absolute;z-index:65535;background:#68995a;line-height:20px}.tangram-dialog-content{text-align:center;font-size:14px;}.tangram-dialog{    background:#FFFFFF repeat scroll 0 0 ;    border: 2px solid #68995A;    height: 100%;    position: absolute;    width: 100%;    z-index: 1;}.tangram-dialog-title {	height:33px;	line-height:33px;	padding:0 10px;	color:#2f5124;	font-size:14px;	font-weight:bold;	background:url(http://img.baidu.com/img/iknow/docshare/bg_popup.gif) repeat-x}.tangram-dialog-close {width:19px;height:19px;display:block;position:absolute;top:5px;right:10px;background:url(http://img.baidu.com/img/iknow/docshare/bg_popup.gif) no-repeat 0 -38px;border:0;padding:0;cursor:pointer;}.tangram-dialog-title .pop_r1{background-position:0 -62px;position:absolute;left:0;top:30px;z-index:2;margin:0;}.tangram-dialog-title .pop_r2{background-position:-10px -62px;position:absolute;right:0;top:30px;z-index:2;margin:0;}.tangram-dialog-footer {position:relative;display:block;margin-top:-10px;width:200px;margin:0 auto;padding:10px 0 30px 0;margin-top:-10px;}.tangram-dialog-footer:after {    content:".";    display:block;    visibility:hidden;    clear:both;    height:0;}.submit {text-align:center;background:#fff;}.tangram-dialog-button-label {    background: url("http://img.baidu.com/img/iknow/docshare/bg_popbtn.gif") no-repeat scroll 0 -37px transparent;    border: medium none;    color: #2F5124;    cursor: pointer;    font-size: 14px;    font-weight: bold;    height: 32px;    line-height: 32px;    width: 92px;    display:inline-block;}.tangram-dialog-accept {    display:inline;    float:left;}.tangram-dialog-cancel {    display:inline;    float:right;}</style>');
var pop = {
	onOk : function() {
	},
	onclosed : function() {
	},
	create : function() {
		if (!G("popDiv")) {
			var B = document.createElement("div");
			B.id = "shadowDiv";
			var C = document.createElement("div");
			C.id = "popDiv";
			C.style.width = C.style.height = "500px";
			var A = document.createElement("iframe");
			A.id = "shadowIframe";
			A.style.filter = "alpha(opacity=0)";
			A.style.opacity = "0";
			A.style.position = "absolute";
			A.style.zIndex = "1";
			var D = [
					'<iframe id="maskLayer" frameborder="0"></iframe>',
					'<div class="pop_holder1">',
					'<table border="0" cellspacing="0" cellpadding="0" style="width:100%;height:100%">',
					'<tr><td id="pop_head" class="pop_head" height="35"valign="top" align="left" colspan="3">',
					'<div class="pop_title"><a id="dialogBoxClose" onclick="pop.hide();return false" title="�ر�" class="pop_close"></a><span id="dialogBoxTitle">��ʾ</span></div>',
					'<div><div class="pop_r1"></div><div class="pop_r2"></div></div></td></tr>',
					'<tr><td class="pop_bg1">&nbsp;</td><td id="popContent">',
					'<div id="popBody"></div>',
					'<div id="ifrDiv" style="height:100%"><iframe id="popIframe" name="popIframe" width="100%" frameborder="0" height="100%" scrolling="no" src="about:blank"></iframe></div>',
					'</td><td class="pop_bg2">&nbsp;</td></tr>',
					'<tr><td height="10" valign="top" colspan="3" class="pop_bottom"><div><div class="pop_r3"></div><div class="pop_r4"></div></div></td></tr>',
					"</table>", "</div>",
					'<div id="pop_holder2" class="pop_holder2"></div>' ]
					.join("");
			C.innerHTML = D;
			document.body.insertBefore(A, document.body.firstChild);
			document.body.insertBefore(C, document.body.firstChild);
			document.body.insertBefore(B, document.body.firstChild)
		}
	},
	resize : function(C, B, M) {
		pop.create();
		var E = baidu.body();
		var Q = E.scrollTop;
		var N = E.documentWidth || 500;
		var F = E.documentHeight || 500;
		var P = G("shadowDiv");
		var K = G("popDiv");
		var D = G("popContent");
		var J = G("shadowIframe");
		var A = G("maskLayer");
		var O = B || parseInt(K.style.width, 10) || 500;
		var H = M || parseInt(K.style.height, 10) || 500;
		P.style.width = N + "px";
		P.style.height = F + "px";
		P.style.left = P.style.top = "0px";
		A.width = O;
		A.height = H;
		J.style.width = (N - 20) + "px";
		J.style.height = (F - 20) + "px";
		J.style.top = J.style.left = "5px";
		var L = (E.viewWidth - O) / 2;
		var I = (E.viewHeight - H) / 2 + Q;
		if (L < 1) {
			L = Q
		}
		if (I < 1) {
			I = "20"
		}
		K.style.left = L + "px";
		K.style.top = I + "px";
		K.style.width = O + "px";
		K.style.height = H + "px";
		D.style.height = (H - 45) + "px";
		G("pop_holder2").style.height = H + "px"
	},
	hide : function() {
		pop.onclosed();
		try {
			baidu.hide("popDiv");
			baidu.hide("shadowDiv");
			baidu.hide("shadowIframe");
			G("shadowIframe").style.width = "0px";
			G("shadowIframe").style.height = "0px"
		} catch (C) {
		}
		try {
			G("popIframe").src = "about:blank"
		} catch (B) {
			try {
				document.frames.popIframe.location = "about:blank"
			} catch (B) {
			}
		}
		var A = G("readerContainer");
		if (A) {
			A.style.cssText = ""
		}
		baidu.un(window, "resize", pop.resize);
		baidu.un(document, "onkeydown", pop.keyListener)
	},
	keyListener : function(B) {
		B = window.event || B;
		var A = B.which || B.keyCode;
		if (27 == A) {
			pop.hide()
		}
	},
	config : function(F, A) {
		if (F) {
			G("dialogBoxTitle").innerHTML = F
		}
		pop.onclosed = A.onclosed || function() {
		};
		if (A.url) {
			if (A.url != true) {
				try {
					G("popIframe").src = A.url
				} catch (D) {
					try {
						document.frames.popIframe.location = A.url
					} catch (D) {
					}
				}
			}
			G("ifrDiv").style.display = "block";
			baidu.hide("popBody")
		} else {
			if (A.info) {
				baidu.hide("ifrDiv");
				G("popBody").style.display = "block";
				G("popBody").innerHTML = A.info
			}
		}
		var B = A.width || 450;
		var E = A.height || 400;
		var C = G("popDiv");
		C.style.width = B + "px";
		C.style.height = E + "px"
	},
	show : function(D, B, C) {
		if (typeof flashresush != "undefined" && flashresush) {
			flashresush()
		}
		if (typeof flashresush != "undefined" && flashresush) {
			flashresush()
		}
		pop.create();
		if ("v2" == C) {
			G("popDiv").className = "v2"
		} else {
			G("popDiv").className = ""
		}
		pop.config(D, B);
		pop.resize();
		G("popDiv").style.display = "block";
		G("shadowDiv").style.display = "block";
		G("shadowIframe").style.display = "";
		baidu.on(window, "resize", pop.resize);
		baidu.on(document, "onkeydown", pop.keyListener);
		var A = G("readerContainer");
		if (A) {
			A.style.cssText = ";border: 0; clip: rect(0 0 0 0); height: 1px; margin: -1px; overflow: hidden; padding: 0; position: absolute; width: 1px;";
			window.scrollTo(0, baidu.body().scrollTop + 1)
		}
	},
	confirm : function(C, B) {
		pop.onOk = baidu.proxy(function(D) {
			pop.onclosed = function() {
			};
			pop.hide();
			D()
		}, B.ok || pop.hide);
		var A = "<span class='f14'>$2$</span><br><br><p align='center'><input type='button'value='$0$' onclick='pop.onOk();'>&nbsp;&nbsp;&nbsp;&nbsp;<input type='button'value='$1$' onclick='pop.hide()'></p>";
		A = A.format(B.okInfo || "ȷ��", B.cancelInfo || "ȡ��", C);
		pop.show(B.title || "��Ϣȷ��", {
			info : A,
			width : (B.width || 300),
			height : (B.height || 150),
			onclosed : B.cancel
		})
	}
};
var log = {};
log.imageReq = function(A) {
	var C = "doclog_" + (new Date()).getTime();
	var B = window[C] = new Image();
	B.onload = B.onerror = function() {
		window[C] = null
	};
	B.src = A;
	B = null
};
log.send = function(D, B, A) {
	var C = (new Date()).getTime();
	var E = [ "http://" + location.host + "/tongji/" + D + ".html?type=" + B,
			"t=" + C ];
	if (A) {
		baidu.object.each(A, function(H, F) {
			E.push(F + "=" + H)
		})
	}
	log.imageReq(E.join("&"))
};
log.nslog = function(B, D, A) {
	var C = (new Date()).getTime();
	var E = [ "http://nsclick.baidu.com/v.gif?pid=112",
			"url=" + encodeURIComponent(B), "type=" + D, "t=" + C ];
	baidu.object.each(A, function(H, F) {
		E.push(F + "=" + H)
	});
	log.imageReq(E.join("&"))
};
var SHOW_FILTEL = [ "����˺���Ϊ�ϴ��˺�������ʵ��ĵ�������ʱ���ܽ����κβ���",
		"����˺���Ϊ�ϴ��˴������������ĵ�������ʱ���ܽ����κβ���",
		"����˺���Ϊ����ˢ�����׶�����ʱ���ܽ����κβ���",
		"����˺���Ϊ�ϴ��˰�ɫ�顢�����Ȳ��������ݵ��ĵ�������ʱ���ܽ����κβ���",
		"����˺���Ϊ�ϴ��˰�Υ�����ݵ��ĵ�������ʱ���ܽ����κβ���",
		"����ʺ���Ϊ����ٱ�������ʱ���ܽ����κβ���" ];
login = {
	hide : function(A) {
		if (parent.pop) {
			parent.pop.hide();
			if (A) {
				parent.location.reload(true)
			}
		} else {
			history.go(-1)
		}
	},
	onLoginSuccess : function() {
		window.location.reload(true)
	},
	onLoginFailed : function() {
		window.location = "http://passport.baidu.com/v2/?login&tpl=do&u="
				+ escape("http://wenku.baidu.com/")
	},
	defalutLoginSuccess : function() {
		window.location.reload(true)
	},
	verlogin : function(hasLogin, notLogin, patchment, extObj, targetEl) {
		var extObj = extObj || {};
		function onOk(req, hasLogin) {
			var res = eval("(" + req.responseText + ")");
			if (res[0]["isLogin"] == "1") {
				if (res[0]["isLock"] == "1") {
					var isNA = (res[0]["lockDay"] == 0) ? "" : "���������"
							+ res[0]["lockDay"] + "�죬�����ĵȴ���", lockReasonIndex = parseInt(res[0]["lockReason"]) - 1, msg = SHOW_FILTEL[lockReasonIndex]
							|| SHOW_FILTEL[4];
					pop
							.show(
									"��ʾ",
									{
										info : "<p>"
												+ msg
												+ isNA
												+ "��</p><br><input type='button' class='pop_btn_short' onclick='pop.hide()' value='ȷ ��'>",
										width : 440,
										height : 270
									})
				} else {
					if (extObj.user && extObj.user.isLimit) {
						login.notifyToVerificationMail();
						return

					} else {
						if (extObj.user && extObj.user.isNoUsername) {
							login.notifyToFillinUsername();
							return

						} else {
							if (hasLogin) {
								hasLogin()
							}
							return true
						}
					}
				}
			} else {
				if (hasLogin && extObj.actionName) {
					baidu.cookie.set("BAIDU_DOC_NACT", extObj.actionName)
				} else {
					if (hasLogin
							&& hasLogin.toString().indexOf("location.href")) {
						baidu.cookie.set("BAIDU_DOC_NACT", "upload")
					}
				}
				login.log(hasLogin, patchment, targetEl)
			}
			return false
		}
		baidu.ajax.get("/login?_t=" + (new Date()).getTime(), function(req) {
			onOk(req, hasLogin)
		})
	},
	check : function(hasLogin, notLogin, patchment, extObj) {
		var extObj = extObj || {};
		function onOk(req, hasLogin, notLogin) {
			var res = eval("(" + req.responseText + ")");
			if (res[0]["isLogin"] == "1") {
				if (extObj.user && extObj.user.isLimit) {
					login.notifyToVerificationMail();
					return

				} else {
					if (extObj.user && extObj.user.isNoUsername) {
						login.notifyToFillinUsername();
						return

					} else {
						if (hasLogin) {
							hasLogin()
						}
						return true
					}
				}
			}
			if (notLogin) {
				notLogin()
			} else {
				login.log(hasLogin, patchment)
			}
			return false
		}
		baidu.ajax.get("/login?_t=" + (new Date()).getTime(), function(req) {
			onOk(req, hasLogin, notLogin)
		})
	},
	checksynch : function(hasLogin, notLogin, patchment) {
		function onOk(req, hasLogin, notLogin) {
			var res = eval("(" + req.responseText + ")");
			if (res[0]["isLogin"] == "1") {
				if (res[0]["isLock"] === "1") {
					var isNA = (res[0]["lockDay"] == 0) ? "" : "���������"
							+ res[0]["lockDay"] + "�죬�����ĵȴ���", lockReasonIndex = parseInt(res[0]["lockReason"]) - 1, msg = SHOW_FILTEL[lockReasonIndex]
							|| SHOW_FILTEL[4];
					pop
							.show(
									"��ʾ",
									{
										info : "<p>"
												+ msg
												+ isNA
												+ "��</p><br><input type='button' class='pop_btn_short' onclick='pop.hide()' value='ȷ ��'>",
										width : 440,
										height : 270
									});
					return false
				}
				if (hasLogin) {
					hasLogin()
				}
				return true
			}
			if (notLogin) {
				notLogin()
			} else {
				login.log(hasLogin, patchment)
			}
			return false
		}
		baidu.ajax.request("/login?_t=" + (new Date()).getTime(), {
			onsuccess : function(req) {
				onOk(req, hasLogin, notLogin)
			},
			method : "get",
			async : false
		})
	},
	log : function(D, B, C) {
		if (D) {
			login.onLoginSuccess = login.defalutLoginSuccess
		} else {
			login.onLoginSuccess = login.defalutLoginSuccess
		}
		WkLogin.show();
		var A = baidu.dom.g("pass_login_hidden_u_0");
		if (A) {
			if (C) {
				login.addSuccessLog(C)
			} else {
				A.value = baidu.encodeHTML(location.href)
						.replace(/&amp;/g, "&").replace(/#.*$/g, "")
			}
		}
		log.send("view", "show", {})
	},
	regiest : function() {
	},
	notifyToVerificationMail : function(A) {
		A = A || "http://" + location.host + location.pathname;
		var B = [
				"���email��δ��֤����֤�ɹ���ˢ��ҳ�漴�ɼ��������",
				'<a href="https://passport.baidu.com/v2/?accountbindemail&tpl=do&u='
						+ encodeURIComponent(A)
						+ '" target="_blank" onclick="setTimeout(function(){pop.hide()},0);" style="color:#E87301">���������֤</a>' ]
				.join("");
		pop.show("��ʾ", {
			info : B,
			width : 520,
			height : 220
		})
	},
	notifyToFillinUsername : function(B) {
		var B = B || "default";
		log.send("general", "showFillinUsername", {
			action : B
		});
		var A = "?t=" + (new Date()).getTime() + "&action=" + B;
		pop.show("�װ������ѣ����û�����ܹ���Ŷ������ϲ�������ְɣ�", {
			url : "/static/html/user_fillinUsername.html" + A,
			width : 560,
			height : 220
		}, "v2")
	},
	addSuccessLog : function(N) {
		var A = baidu.dom.g("pass_login_hidden_u_0"), M = baidu.encodeHTML(
				location.href).replace(/&amp;/g, "&").replace(/#.*$/g, ""), L = M, F = location.pathname;
		var E = {
			home : /^\/$/i,
			search : /^\/search/i,
			view : /^\/view\//i,
			zone : /^\/(edu|ppt|pro|form)\/index$/i
		};
		var H = {
			upload : /^(upload-icon|uploadDoc-\d+|upload)$/,
			download : /^downDoc-\d+$/,
			fav_up : /^fav_btn_TANGRAM\$\d+$/,
			fav_down : /^fav-\d+$/
		};
		for ( var B in E) {
			if (E.hasOwnProperty(B)) {
				var J = E[B];
				if (J.test(F)) {
					var C = "from_page=" + B;
					L = login.getConUrl(L, C)
				}
			}
		}
		for ( var I in H) {
			if (H.hasOwnProperty(I)) {
				var K = H[I];
				if (K.test(N.id)) {
					var D = "from_mod=" + I;
					L = login.getConUrl(L, D)
				}
			}
		}
		A.value = L
	},
	getConUrl : function(B, A) {
		var C = (B.indexOf("?") == -1) ? "?" : "&";
		return B + C + A
	}
};
var cookie = {
	defaultExpires : new Date(Date.parse("Jan 1, 2026")),
	defaultPath : "/",
	defaultDomain : location.host.replace(/:\d+/, ""),
	set : function(F, H, B, C, A, E) {
		var D = F + "=" + encodeURIComponent(H);
		if (B) {
			D += "; expires=" + B.toGMTString()
		}
		if (C) {
			D += "; path=" + C
		}
		if (A) {
			D += "; domain=" + A
		}
		if (E) {
			D += "; secure"
		}
		document.cookie = D
	},
	get : function(C) {
		var B = "(?:; )?" + C + "=([^;]*);?";
		var A = new RegExp(B);
		if (A.test(document.cookie)) {
			return decodeURIComponent(RegExp["$1"])
		} else {
			return null
		}
	},
	del : function(D, B, A, C) {
		cookie.set(D, "", new Date(0), B, A, C)
	}
};
function classTable(F) {
	var B = "classTable";
	var C = null;
	var H = (F == 1) ? false : true;
	function I(N) {
		N = N || B;
		B = N;
		if (typeof class_level_1 == "undefined") {
			return

		}
		var O = "";
		for ( var M = 1; M <= 4; M++) {
			var P = (M == 0) ? "short" : "long";
			O += '<select id="classTableLevel' + M
					+ '" name="class_table_level_' + M + '" size="9" class="'
					+ P + '">';
			O += "</select>"
		}
		baidu.G(N).innerHTML = O;
		A(class_level_1, false, 1, G("classTableLevel1"));
		A(L(class_level_1[0][0], 1), H, 2, G("classTableLevel2"));
		A(L(class_level_2[0][1], 2), H, 3, G("classTableLevel3"));
		A(L(class_level_3[0][1], 3), H, 4, G("classTableLevel4"));
		C = baidu.GT("span", G("classTable"))[1];
		if (H) {
			baidu.hide("classTableLevel2");
			baidu.hide("classTableLevel3");
			baidu.hide("classTableLevel4");
			baidu.hide(C)
		}
		J("classTableLevel1", "classTableLevel2", "classTableLevel3",
				"classTableLevel4");
		sug_opt.getSugTag()
	}
	function A(N, P, T, S) {
		var R = 0;
		if (!S) {
			return

		}
		var O = (T < 3) ? "��ѡ�����" : "��ѡ";
		if (P) {
			S.options[0] = new Option(O, "0-" + T);
			R++
		}
		if (T == 1) {
			S.options[0] = new Option(O, "0-" + T);
			R++
		}
		for ( var Q = 0; Q < N.length; Q++) {
			var M = N[Q];
			S.options[R] = new Option(M[M.length - 1], M[M.length - 2] + "-"
					+ T);
			R++
		}
		S.length = R;
		S.options[0].selected = true
	}
	function J() {
		var M = arguments;
		baidu.each(M, function(N) {
			baidu.on(N, "change", function() {
				if (this.value == "0-1") {
					baidu.hide("classTableLevel2");
					baidu.hide("classTableLevel3");
					baidu.hide("classTableLevel4")
				} else {
					E(this.value)
				}
			})
		})
	}
	function E(M) {
		if (M == "") {
			baidu.hide(P, C);
			sug_opt.getSugTag();
			return false
		}
		var R = M.replace(/-\d+/, "");
		var Q = M.replace(/\d+-/, "");
		if (Q == 4) {
			sug_opt.getSugTag();
			return false
		}
		var N = parseInt(Q) + 1;
		var P = G("classTableLevel" + N);
		var O = L(R, Q);
		if (N == 3 && R == 0) {
			baidu.hide("classTableLevel3")
		}
		if (!H) {
			setVersion(D()[1])
		}
		if (O && O.length != 0) {
			A(O, H, N, P);
			baidu.show("classTableLevel2");
			if (!H && Q == 1) {
				A(next_level_data, H, 3, G("classTableLevel3"))
			}
			if (N == 2) {
				baidu.hide("classTableLevel3");
				baidu.hide("classTableLevel4")
			}
			if (N == 3) {
				baidu.show("classTableLevel3", C);
				baidu.hide("classTableLevel4")
			}
			if (N == 4) {
				baidu.show("classTableLevel4", C)
			}
		} else {
			G("classTableLevel4").options[0].selected = true;
			baidu.hide("classTableLevel4", C)
		}
		sug_opt.getSugTag()
	}
	function L(Q, O) {
		if (O == 4) {
			return false
		}
		var M = [];
		if (O == 1) {
			var P = class_level_2
		} else {
			if (O == 2) {
				var P = class_level_3
			} else {
				if (O == 3) {
					var P = class_level_4
				}
			}
		}
		for ( var N = 0; N < P.length; N++) {
			if (Q == P[N][0]) {
				M.push(P[N])
			}
		}
		return M
	}
	function D() {
		var M = [];
		for ( var N = 1; N < 5; N++) {
			var P = G("classTableLevel" + N);
			var O = (P.value != "") ? P.value.replace(/-\d+/, "") : 0;
			M.push(O)
		}
		return M
	}
	function K(Q) {
		var M = Q.split("-");
		for ( var O = 1; O <= M.length; O++) {
			var P = baidu.GT("option", G("classTableLevel" + O));
			for ( var N = 0; N < P.length; N++) {
				if (P[N].value == M[O - 1] + "-" + O) {
					P[N].selected = true;
					E(P[N].value)
				}
			}
		}
	}
	this.create = I;
	this.get = D;
	this.set = K
}
var global = {};
global.statusMsg = function(A) {
	var B = "http://msg.baidu.com/msg/msg_dataGetmsgCount?from=msg&un=" + A;
	baidu.page.loadJsFile(B);
	setTimeout(function() {
		if (typeof tv != "undefined") {
			baidu.g("mnum").innerHTML = tv
		}
	}, 300)
};
global.checkSearchBoxQuery = function() {
	var B = [ "topSearchBox", "bottomSearchBox" ];
	for ( var A = 0; A < B.length; A++) {
		if (baidu.G(B[A])) {
			baidu.on(baidu.G(B[A]), "submit", function(C) {
				if (document[this.name].word.value.trim() == "") {
					pop.show("��ʾ", {
						url : "/static/html/emptySearchQuery.html?form="
								+ this.name,
						width : 440,
						height : 270
					})
				} else {
					this.submit()
				}
				baidu.preventDefault(C)
			})
		}
	}
	if (baidu.G("lec_SearchBox")) {
		baidu.on(baidu.G("lec_SearchBox"), "submit", function(C) {
			if (document[this.name].word.value.trim() == "") {
				pop.show("��ʾ", {
					url : "/static/html/emptySearchQuery.html?form="
							+ this.name,
					width : 440,
					height : 270
				})
			} else {
				this.submit()
			}
			baidu.preventDefault(C)
		})
	}
};
global.go = function(E, D, F) {
	if (document.ftop.word.value.length > 0) {
		var C = E.href;
		var B = encodeURIComponent(document.ftop.word.value);
		if (C.indexOf("q=") != -1) {
			E.href = C.replace(new RegExp("q=[^&$]*"), "q=" + B)
		} else {
			var A = D ? "&" : "?";
			E.href = E.href + A + "q=" + B
		}
	}
	if (F) {
		log.nslog(E.href, "100", {
			to : F
		})
	}
};
function gourl(A) {
	window.open(A)
};
(function(G, M, E) {
	if (G.helper === E) {
		G.helper = {}
	}
	var C = G.helper;
	var K = function(Q, O) {
		for ( var P in O) {
			if (O.hasOwnProperty(P)) {
				Q[P] = O[P]
			}
		}
		return Q
	};
	K(C, {
		wbr : function(O) {
			return String(O).replace(
					/(?:<[^>]+>)|(?:&#?[0-9a-z]{2,6};)|(.{1})/gi, "$&<wbr>")
					.replace(/><wbr>/g, ">")
		},
		onFooEndFunc : function(O, R, S) {
			var P = S || 50, Q = R || null, T;
			return function() {
				var U = Array.prototype.slice.call(arguments, 0);
				if (!!T) {
					clearTimeout(T)
				}
				T = setTimeout(function() {
					O.apply(R, U)
				}, P)
			}
		}
	});
	var H = baidu, I = H.dom, D = H.lang, L = H.array, B = H.object, N = H.string, A = H.page, J = H.browser;
	var F = function(O) {
		if (!(this instanceof F)) {
			return new F(O)
		}
		this._init.call(this, O)
	};
	baidu.lang.inherits(F, D.Class);
	K(
			F.prototype,
			{
				_init : function(O) {
					K(this, O);
					this.timer = null
				},
				create : function() {
					var O = this.render();
					if (O) {
						this.moveTo(this.getY().foot)
					}
				},
				render : function() {
					var Q = this.dispatchEvent("onBeforeRender"), P;
					if (Q && this.content) {
						P = this.content;
						var O = this.wrap = I.create("div", {
							"class" : "wk-pop-wrap",
							style : ";position:fixed;z-index:99;"
						});
						if (J.ie && J.ie < 7) {
							O.style.cssText = ";position:absolute;z-index:99;"
						}
						O.innerHTML = P;
						M.body.appendChild(O);
						this.dispatchEvent("onAfterRender");
						return true
					}
					return false
				},
				_fx : function(R, O, Q) {
					var P = Math[(R - O) > 0 ? "floor" : "ceil"], Q = Q || 0.1;
					return function() {
						return [ R += P((O - R) * Q), R - O ]
					}
				},
				moveTo : function(S) {
					clearInterval(this.timer);
					var R = this, O = parseInt(I.getStyle(this.wrap, "bottom")) || 0, Q = this
							._fx(O, parseInt(S), this.factor), P = 0;
					this.dispatchEvent("onBeforeMoveTo");
					this.timer = setInterval(function() {
						P = Q();
						R.wrap.style.bottom = P[1] + "px";
						if (P[1] == 0) {
							clearInterval(R.timer);
							var T = R.dispatchEvent("onAfterMoveTo");
							if (T) {
								R.bind()
							}
						}
					}, 10)
				},
				getY : function() {
					var Q = A.getScrollTop(), O = A.getViewHeight(), P = this.wrap ? this.wrap.offsetHeight
							: 0;
					return {
						foot : Q + O + P + "px",
						top : Q + O - P + "px"
					}
				},
				close : function() {
					var P = this.dispatchEvent("onBeforeClose"), O = this.wrap;
					if (P && O) {
						I.hide(O);
						this.unbind()
					}
					this.dispatchEvent("onAfterClose")
				},
				bind : function() {
					if (this.wrap && (J.ie && J.ie < 7)) {
						this.wrap.style.cssText = "top:expression(documentElement.scrollTop+documentElement.clientHeight-this.offsetHeight);";
						M.documentElement.style.cssText = ";background:url(about:blank) fixed;"
					}
				},
				unbind : function() {
					if (this.wrap && (J.ie && J.ie < 7)) {
						this.wrap.style.top = "0px";
						this.wrap.style.display = "none"
					}
				},
				dispose : function() {
					if (this.wrap) {
						var O = I.create("div");
						O.appendChild(this.wrap);
						O.innerHTML = "";
						O = this.wrap = null
					}
				}
			});
	D.module("helper.popMessage", F);
	K(C, {
		callByBrowser : baidu.sio.callByBrowser,
		callByServer : baidu.sio.callByServer,
		q : baidu.q,
		format : baidu.string.format,
		lang : baidu.lang
	});
	C.array = C.array || {};
	K(C.array, {
		indexOf : baidu.array.indexOf,
		find : baidu.array.find
	})
})(window, document);
baidu.ui = baidu.ui || {
	version : "1.3.9"
};
baidu.ui.getUI = function(C) {
	var C = C.split("-"), B = baidu.ui, A = C.length, D = 0;
	for (; D < A; D++) {
		B = B[C[D].charAt(0).toUpperCase() + C[D].slice(1)]
	}
	return B
};
baidu.ui.create = function(B, A) {
	if (baidu.lang.isString(B)) {
		B = baidu.ui.getUI(B)
	}
	return new B(A)
};
baidu.ui.Base = {
	id : "",
	getId : function(A) {
		var C = this, B;
		B = "tangram-" + C.uiType + "--" + (C.id ? C.id : C.guid);
		return A ? B + "-" + A : B
	},
	getClass : function(B) {
		var D = this, C = D.classPrefix, A = D.skin;
		if (B) {
			C += "-" + B;
			A += "-" + B
		}
		if (D.skin) {
			C += " " + A
		}
		return C
	},
	getMain : function() {
		return baidu.g(this.mainId)
	},
	getBody : function() {
		return baidu.g(this.getId())
	},
	uiType : "",
	getCallRef : function() {
		return "window['$BAIDU$']._instances['" + this.guid + "']"
	},
	getCallString : function(D) {
		var C = 0, B = Array.prototype.slice.call(arguments, 1), A = B.length;
		for (; C < A; C++) {
			if (typeof B[C] == "string") {
				B[C] = "'" + B[C] + "'"
			}
		}
		return this.getCallRef() + "." + D + "(" + B.join(",") + ");"
	},
	on : function(A, B, C) {
		baidu.on(A, B, C);
		this.addEventListener("ondispose", function() {
			baidu.un(A, B, C)
		})
	},
	renderMain : function(B) {
		var D = this, C = 0, A;
		if (D.getMain()) {
			return

		}
		B = baidu.g(B);
		if (!B) {
			B = document.createElement("div");
			document.body.appendChild(B);
			B.style.position = "absolute";
			B.className = D.getClass("main")
		}
		if (!B.id) {
			B.id = D.getId("main")
		}
		D.mainId = B.id;
		B.setAttribute("data-guid", D.guid);
		return B
	},
	dispose : function() {
		this.dispatchEvent("dispose");
		baidu.lang.Class.prototype.dispose.call(this)
	}
};
baidu.ui.createUI = function(D, J) {
	J = J || {};
	var H = J.superClass || baidu.lang.Class, G = H == baidu.lang.Class ? 1 : 0, E, B, I = function(
			K, C) {
		var L = this;
		K = K || {};
		H.call(L, !G ? K : (K.guid || ""), true);
		baidu.object.extend(L, I.options);
		baidu.object.extend(L, K);
		L.classPrefix = L.classPrefix || "tangram-" + L.uiType.toLowerCase();
		for (E in baidu.ui.behavior) {
			if (typeof L[E] != "undefined" && L[E]) {
				baidu.object.extend(L, baidu.ui.behavior[E]);
				if (baidu.lang.isFunction(L[E])) {
					L.addEventListener("onload", function() {
						baidu.ui.behavior[E].call(L[E].apply(L))
					})
				} else {
					baidu.ui.behavior[E].call(L)
				}
			}
		}
		D.apply(L, arguments);
		for (E = 0, B = I._addons.length; E < B; E++) {
			I._addons[E](L)
		}
		if (K.parent && L.setParent) {
			L.setParent(K.parent)
		}
		if (!C && K.autoRender) {
			L.render(K.element)
		}
	}, A = function() {
	};
	A.prototype = H.prototype;
	var F = I.prototype = new A();
	for (E in baidu.ui.Base) {
		F[E] = baidu.ui.Base[E]
	}
	I.extend = function(C) {
		for (E in C) {
			I.prototype[E] = C[E]
		}
		return I
	};
	I._addons = [];
	I.register = function(C) {
		if (typeof C == "function") {
			I._addons.push(C)
		}
	};
	I.options = {};
	return I
};
baidu.ui.get = function(A) {
	var B;
	if (baidu.lang.isString(A)) {
		return baidu.lang.instance(A)
	} else {
		do {
			if (!A || A.nodeType == 9) {
				return null
			}
			if (B = baidu.dom.getAttr(A, "data-guid")) {
				return baidu.lang.instance(B)
			}
		} while ((A = A.parentNode) != document.body)
	}
};
baidu.ui.Suggestion = baidu.ui
		.createUI(function(A) {
			var B = this;
			B.addEventListener("onload", function() {
				B.on(document, "mousedown", B.documentMousedownHandler);
				B.on(window, "blur", B.windowBlurHandler)
			});
			B.documentMousedownHandler = B._getDocumentMousedownHandler();
			B.windowBlurHandler = B._getWindowBlurHandler();
			B.enableIndex = [];
			B.currentIndex = -1
		})
		.extend(
				{
					uiType : "suggestion",
					onbeforepick : new Function,
					onpick : new Function,
					onconfirm : new Function,
					onhighlight : new Function,
					onshow : new Function,
					onhide : new Function,
					getData : function() {
						return []
					},
					prependHTML : "",
					appendHTML : "",
					currentData : {},
					tplDOM : "<div id='#{0}' class='#{1}' style='position:relative; top:0px; left:0px'></div>",
					tplPrependAppend : "<div id='#{0}' class='#{1}'>#{2}</div>",
					tplBody : '<table cellspacing="0" cellpadding="2"><tbody>#{0}</tbody></table>',
					tplRow : '<tr><td id="#{0}" onmouseover="#{2}" onmouseout="#{3}" onmousedown="#{4}" onclick="#{5}" class="#{6}">#{1}</td></tr>',
					getString : function() {
						var A = this;
						return baidu.format(A.tplDOM, A.getId(), A.getClass(),
								A.guid)
					},
					render : function(C) {
						var B = this, A, C = baidu.g(C);
						if (B.getMain() || !C) {
							return

						}
						if (C.id) {
							B.targetId = C.id
						} else {
							B.targetId = C.id = B.getId("input")
						}
						A = B.renderMain();
						A.style.display = "none";
						A.innerHTML = B.getString();
						this.dispatchEvent("onload")
					},
					_isShowing : function() {
						var B = this, A = B.getMain();
						return A && A.style.display != "none"
					},
					pick : function(A) {
						var D = this, B = D.currentData, E = B
								&& typeof A == "number"
								&& typeof B[A] != "undefined" ? B[A].value : A, C = {
							data : {
								item : E == A ? {
									value : A,
									content : A
								} : B[A],
								index : A
							}
						};
						if (D.dispatchEvent("onbeforepick", C)) {
							D.dispatchEvent("onpick", C)
						}
					},
					show : function(F, E, C) {
						var B = 0, A = E.length, D = this;
						D.enableIndex = [];
						D.currentIndex = -1;
						if (A == 0 && !C) {
							D.hide()
						} else {
							D.currentData = [];
							for (; B < A; B++) {
								if (typeof E[B].value != "undefined") {
									D.currentData.push(E[B])
								} else {
									D.currentData.push({
										value : E[B],
										content : E[B]
									})
								}
								if (typeof E[B]["disable"] == "undefined"
										|| E[B]["disable"] == false) {
									D.enableIndex.push(B)
								}
							}
							D.getBody().innerHTML = D._getBodyString();
							D.getMain().style.display = "block";
							D.dispatchEvent("onshow")
						}
					},
					hide : function() {
						var E = this;
						if (!E._isShowing()) {
							return

						}
						if (E.currentIndex >= 0 && E.holdHighLight) {
							console.log(E.currentIndex);
							console.log(E.currentData);
							var D = E.currentData, B = -1;
							for ( var C = 0, A = D.length; C < A; C++) {
								if (typeof D[C].disable == "undefined"
										|| D[C].disable == false) {
									B++;
									console.log(B + "    " + C);
									if (B == E.currentIndex) {
										E.pick(C)
									}
								}
							}
						}
						E.getMain().style.display = "none";
						E.dispatchEvent("onhide")
					},
					highLight : function(A) {
						var C = this, D = C.enableIndex, B = null;
						if (!C._isEnable(A)) {
							return

						}
						C.currentIndex >= 0 && C._clearHighLight();
						B = C._getItem(A);
						baidu.addClass(B, C.getClass("current"));
						C.currentIndex = baidu.array.indexOf(D, A);
						C.dispatchEvent("onhighlight", {
							index : A,
							data : C.getDataByIndex(A)
						})
					},
					clearHighLight : function() {
						var C = this, A = C.currentIndex, B = C.enableIndex[A];
						C._clearHighLight()
								&& C.dispatchEvent("onclearhighlight", {
									index : B,
									data : C.getDataByIndex(B)
								})
					},
					_clearHighLight : function() {
						var C = this, A = C.currentIndex, D = C.enableIndex, B = null;
						if (A >= 0) {
							B = C._getItem(D[A]);
							baidu.removeClass(B, C.getClass("current"));
							C.currentIndex = -1;
							return true
						}
						return false
					},
					confirm : function(A, C) {
						var B = this;
						if (C != "keyboard") {
							if (!B._isEnable(A)) {
								return

							}
						}
						B.pick(A);
						B.dispatchEvent("onconfirm", {
							data : B.getDataByIndex(A) || A,
							source : C
						});
						B.currentIndex = -1;
						B.hide()
					},
					getDataByIndex : function(A) {
						return {
							item : this.currentData[A],
							index : A
						}
					},
					getTargetValue : function() {
						return this.getTarget().value
					},
					getTarget : function() {
						return baidu.g(this.targetId)
					},
					_getItem : function(A) {
						return baidu.g(this.getId("item" + A))
					},
					_getBodyString : function() {
						var F = this, E = "", D = [], G = F.currentData, A = G.length, C = 0;
						function B(H) {
							return baidu.format(F.tplPrependAppend, F.getId(H),
									F.getClass(H), F[H + "HTML"])
						}
						E += B("prepend");
						for (; C < A; C++) {
							D
									.push(baidu
											.format(
													F.tplRow,
													F.getId("item" + C),
													G[C].content,
													F.getCallRef()
															+ "._itemOver(event, "
															+ C + ")",
													F.getCallRef()
															+ "._itemOut(event, "
															+ C + ")",
													F.getCallRef()
															+ "._itemDown(event, "
															+ C + ")",
													F.getCallRef()
															+ "._itemClick(event, "
															+ C + ")",
													(typeof G[C]["disable"] == "undefined" || G[C]["disable"] == false) ? ""
															: F
																	.getClass("disable")))
						}
						E += baidu.format(F.tplBody, D.join(""));
						E += B("append");
						return E
					},
					_itemOver : function(C, A) {
						var B = this;
						baidu.event.stop(C || window.event);
						B._isEnable(A) && B.highLight(A);
						B.dispatchEvent("onmouseoveritem", {
							index : A,
							data : B.getDataByIndex(A)
						})
					},
					_itemOut : function(C, A) {
						var B = this;
						baidu.event.stop(C || window.event);
						if (!B.holdHighLight) {
							B._isEnable(A) && B.clearHighLight()
						}
						B.dispatchEvent("onmouseoutitem", {
							index : A,
							data : B.getDataByIndex(A)
						})
					},
					_itemDown : function(C, A) {
						var B = this;
						baidu.event.stop(C || window.event);
						B.dispatchEvent("onmousedownitem", {
							index : A,
							data : B.getDataByIndex(A)
						})
					},
					_itemClick : function(C, A) {
						var B = this;
						baidu.event.stop(C || window.event);
						B.dispatchEvent("onitemclick", {
							index : A,
							data : B.getDataByIndex(A)
						});
						B._isEnable(A) && B.confirm(A, "mouse")
					},
					_isEnable : function(A) {
						var B = this;
						return baidu.array.contains(B.enableIndex, A)
					},
					_getDocumentMousedownHandler : function() {
						var A = this;
						return function(D) {
							D = D || window.event;
							var B = D.target || D.srcElement, C = baidu.ui
									.get(B);
							if (B == A.getTarget()
									|| (C && C.uiType == A.uiType)) {
								return

							}
							A.hide()
						}
					},
					_getWindowBlurHandler : function() {
						var A = this;
						return function() {
							A.hide()
						}
					},
					dispose : function() {
						var A = this;
						A.dispatchEvent("dispose");
						baidu.dom.remove(A.mainId);
						baidu.lang.Class.prototype.dispose.call(this)
					}
				});
baidu.ui.behavior = baidu.ui.behavior || {};
(function() {
	var A = baidu.ui.behavior.coverable = function() {
	};
	A.Coverable_isShowing = false;
	A.Coverable_iframe;
	A.Coverable_container;
	A.Coverable_iframeContainer;
	A.Coverable_show = function() {
		var H = this;
		if (H.Coverable_iframe) {
			H.Coverable_update();
			baidu.setStyle(H.Coverable_iframe, "display", "block");
			return

		}
		var F = H.coverableOptions || {}, C = H.Coverable_container = F.container
				|| H.getMain(), E = F.opacity || "0", D = F.color || "", G = H.Coverable_iframe = document
				.createElement("iframe"), B = H.Coverable_iframeContainer = document
				.createElement("div");
		baidu.dom.children(C).length > 0 ? baidu.dom.insertBefore(B,
				C.firstChild) : C.appendChild(B);
		baidu.setStyles(B, {
			position : "absolute",
			top : "0px",
			left : "0px"
		});
		baidu.dom.setBorderBoxSize(B, {
			width : C.offsetWidth,
			height : C.offsetHeight
		});
		baidu.dom.setBorderBoxSize(G, {
			width : B.offsetWidth
		});
		baidu.dom.setStyles(G, {
			zIndex : -1,
			display : "block",
			border : 0,
			backgroundColor : D,
			filter : "progid:DXImageTransform.Microsoft.Alpha(style=0,opacity="
					+ E + ")"
		});
		B.appendChild(G);
		G.src = "javascript:void(0)";
		G.frameBorder = "0";
		G.scrolling = "no";
		G.height = "97%";
		H.Coverable_isShowing = true
	};
	A.Coverable_hide = function() {
		var C = this, B = C.Coverable_iframe;
		if (!C.Coverable_isShowing) {
			return

		}
		baidu.setStyle(B, "display", "none");
		C.Coverable_isShowing = false
	};
	A.Coverable_update = function(D) {
		var F = this, D = D || {}, C = F.Coverable_container, B = F.Coverable_iframeContainer, E = F.Coverable_iframe;
		baidu.dom.setBorderBoxSize(B, {
			width : C.offsetWidth,
			height : C.offsetHeight
		});
		baidu.dom.setBorderBoxSize(E, baidu.extend({
			width : baidu.getStyle(B, "width")
		}, D))
	}
})();
baidu.extend(baidu.ui.Suggestion.prototype, {
	coverable : true,
	coverableOptions : {}
});
baidu.ui.Suggestion.register(function(A) {
	if (A.coverable) {
		A.addEventListener("onshow", function() {
			A.Coverable_show()
		});
		A.addEventListener("onhide", function() {
			A.Coverable_hide()
		})
	}
});
baidu.ui.Suggestion.extend({
	setData : function(D, C, B) {
		var A = this;
		A.dataCache[D] = C;
		if (!B) {
			A.show(D, A.dataCache[D])
		}
	}
});
baidu.ui.Suggestion.register(function(A) {
	A.dataCache = {}, A.addEventListener("onneeddata", function(C, D) {
		var B = A.dataCache;
		if (typeof B[D] == "undefined") {
			A.getData(D)
		} else {
			A.show(D, B[D])
		}
	})
});
(function() {
	var B = baidu.ui.behavior.posable = function() {
	};
	B.setPosition = function(G, E, D) {
		E = baidu.g(E) || this.getMain();
		D = D || {};
		var F = this, C = [ E, G, D ];
		F.__execPosFn(E, "_positionByCoordinate", D.once, C)
	};
	B._positionByCoordinate = function(C, Q, R, E) {
		Q = Q || [ 0, 0 ];
		R = R || {};
		var N = this, O = {}, L = baidu.page.getViewHeight(), P = baidu.page
				.getViewWidth(), I = baidu.page.getScrollLeft(), G = baidu.page
				.getScrollTop(), F = C.offsetWidth, H = C.offsetHeight, D = C.offsetParent, M = (!D || D == document.body) ? {
			left : 0,
			top : 0
		}
				: baidu.dom.getPosition(D);
		R.position = (typeof R.position !== "undefined") ? R.position
				.toLowerCase() : "bottomright";
		Q = A(Q || [ 0, 0 ]);
		R.offset = A(R.offset || [ 0, 0 ]);
		Q.x += (R.position.indexOf("right") >= 0 ? (Q.width || 0) : 0);
		Q.y += (R.position.indexOf("bottom") >= 0 ? (Q.height || 0) : 0);
		O.left = Q.x + R.offset.x - M.left;
		O.top = Q.y + R.offset.y - M.top;
		switch (R.insideScreen) {
		case "surround":
			O.left += O.left < I ? F + (Q.width || 0)
					: ((O.left + F) > (I + P) ? -F - (Q.width || 0) : 0);
			O.top += O.top < G ? H + (Q.height || 0)
					: ((O.top + H) > (G + L) ? -H - (Q.height || 0) : 0);
			break;
		case "fix":
			O.left = Math.max(0 - parseFloat(baidu.dom
					.getStyle(C, "marginLeft")) || 0, Math.min(O.left,
					baidu.page.getViewWidth() - F - M.left));
			O.top = Math.max(
					0 - parseFloat(baidu.dom.getStyle(C, "marginTop")) || 0,
					Math.min(O.top, baidu.page.getViewHeight() - H - M.top));
			break;
		case "verge":
			var K = {
				width : (R.position.indexOf("right") > -1 ? Q.width : 0),
				height : (R.position.indexOf("bottom") > -1 ? Q.height : 0)
			}, J = {
				width : (R.position.indexOf("bottom") > -1 ? Q.width : 0),
				height : (R.position.indexOf("right") > -1 ? Q.height : 0)
			};
			O.left -= (R.position.indexOf("right") >= 0 ? (Q.width || 0) : 0);
			O.top -= (R.position.indexOf("bottom") >= 0 ? (Q.height || 0) : 0);
			O.left += O.left + K.width + F - I > P - M.left ? J.width - F
					: K.width;
			O.top += O.top + K.height + H - G > L - M.top ? J.height - H
					: K.height;
			break
		}
		baidu.dom.setPosition(C, O);
		if (!E
				&& (L != baidu.page.getViewHeight() || P != baidu.page
						.getViewWidth())) {
			N._positionByCoordinate(C, Q, {}, true)
		}
		E || N.dispatchEvent("onpositionupdate")
	};
	B.__execPosFn = function(D, G, E, C) {
		var F = this;
		if (typeof E == "undefined" || !E) {
			baidu.event.on(baidu.dom.getWindow(D), "resize", baidu.fn.bind
					.apply(F, [ G, F ].concat([].slice.call(C))))
		}
		F[G].apply(F, C)
	};
	function A(C) {
		C.x = C[0] || C.x || C.left || 0;
		C.y = C[1] || C.y || C.top || 0;
		return C
	}
})();
baidu.ui.Suggestion.extend({
	posable : true,
	fixWidth : true,
	getWindowResizeHandler : function() {
		var A = this;
		return function() {
			A.adjustPosition(true)
		}
	},
	adjustPosition : function(B) {
		var D = this, E = D.getTarget(), C, A = D.getMain(), F;
		if (!D._isShowing() && B) {
			return

		}
		C = baidu.dom.getPosition(E), F = {
			top : (C.top + E.offsetHeight - 1),
			left : C.left,
			width : E.offsetWidth
		};
		F = typeof D.view == "function" ? D.view(F) : F;
		D.setPosition([ F.left, F.top ], null, {
			once : true
		});
		baidu.dom.setOuterWidth(A, F.width)
	}
});
baidu.ui.Suggestion.register(function(A) {
	A.windowResizeHandler = A.getWindowResizeHandler();
	A.addEventListener("onload", function() {
		A.adjustPosition();
		if (A.fixWidth) {
			A.fixWidthTimer = setInterval(function() {
				var B = A.getMain(), C = A.getTarget();
				if (B.offsetWidth != 0 && C && C.offsetWidth != B.offsetWidth) {
					A.adjustPosition();
					B.style.display = "block"
				}
			}, 100)
		}
		A.on(window, "resize", A.windowResizeHandler)
	});
	A.addEventListener("onshow", function() {
		A.adjustPosition()
	});
	A.addEventListener("ondispose", function() {
		clearInterval(A.fixWidthTimer)
	})
});
baidu.ui.Suggestion.register(function(D) {
	var G, B = "", F, C, A = false, E = false;
	D.addEventListener("onload", function() {
		G = this.getTarget();
		F = G.value;
		D.targetKeydownHandler = D.getTargetKeydownHandler();
		D.on(G, "keydown", D.targetKeydownHandler);
		G.setAttribute("autocomplete", "off");
		D.circleTimer = setInterval(function() {
			if (E) {
				return

			}
			if (baidu.g(G) == null) {
				D.dispose()
			}
			var H = G.value;
			if (H == B && H != "" && H != F && H != C) {
				if (D.requestTimer == 0) {
					D.requestTimer = setTimeout(function() {
						D.dispatchEvent("onneeddata", H)
					}, 100)
				}
			} else {
				clearTimeout(D.requestTimer);
				D.requestTimer = 0;
				if (H == "" && B != "") {
					C = "";
					D.hide()
				}
				B = H;
				if (H != C) {
					D.defaultIptValue = H
				}
				if (F != G.value) {
					F = ""
				}
			}
		}, 10);
		D.on(G, "beforedeactivate", D.beforedeactivateHandler)
	});
	D.addEventListener("onitemclick", function() {
		E = false;
		D.defaultIptValue = B = D.getTargetValue()
	});
	D.addEventListener("onpick", function(H) {
		if (A) {
			G.blur()
		}
		G.value = C = H.data.item.value;
		if (A) {
			G.focus()
		}
	});
	D.addEventListener("onmousedownitem", function(H) {
		A = true;
		E = true;
		setTimeout(function() {
			E = false;
			A = false
		}, 500)
	});
	D.addEventListener("ondispose", function() {
		clearInterval(D.circleTimer)
	})
});
baidu.ui.Suggestion.extend({
	beforedeactivateHandler : function() {
		return function() {
			if (mousedownView) {
				window.event.cancelBubble = true;
				window.event.returnValue = false
			}
		}
	},
	getTargetKeydownHandler : function() {
		var B = this;
		function A(C) {
			if (!B._isShowing()) {
				B.dispatchEvent("onneeddata", B.getTargetValue());
				return

			}
			var E = B.enableIndex, D = B.currentIndex;
			if (E.length == 0) {
				return

			}
			if (C) {
				switch (D) {
				case -1:
					D = E.length - 1;
					B.pick(E[D]);
					B.highLight(E[D]);
					break;
				case 0:
					D = -1;
					B.pick(B.defaultIptValue);
					B.clearHighLight();
					break;
				default:
					D--;
					B.pick(E[D]);
					B.highLight(E[D]);
					break
				}
			} else {
				switch (D) {
				case -1:
					D = 0;
					B.pick(E[D]);
					B.highLight(E[D]);
					break;
				case E.length - 1:
					D = -1;
					B.pick(B.defaultIptValue);
					B.clearHighLight();
					break;
				default:
					D++;
					B.pick(E[D]);
					B.highLight(E[D]);
					break
				}
			}
			B.currentIndex = D
		}
		return function(E) {
			var C = false, D;
			E = E || window.event;
			switch (E.keyCode) {
			case 9:
			case 27:
				B.hide();
				break;
			case 13:
				baidu.event.stop(E);
				B.confirm(B.currentIndex == -1 ? B.getTarget().value
						: B.enableIndex[B.currentIndex], "keyboard");
				break;
			case 38:
				C = true;
			case 40:
				baidu.event.stop(E);
				A(C);
				break;
			default:
				B.currentIndex = -1
			}
		}
	},
	defaultIptValue : ""
});
var commonHeaderConf = {
	sugProdName : "wenku",
	searchInputId : "kw"
};
(function(A) {
	var B = {
		url : {}
	};
	B.url.escapeSymbol = function(C) {
		return String(C).replace(
				/[#%&+=\/\\\ \��\f\r\n\t]/g,
				function(D) {
					return "%"
							+ (256 + D.charCodeAt()).toString(16).substring(1)
									.toUpperCase()
				})
	};
	window.setHeadUrl = function(C) {
		var E = C.getAttribute("data-link-to"), G = "/tongji/general.html?type=toptablink&to="
				+ E + "&t=" + (+new Date());
		log.imageReq(G);
		if (!document.getElementById(A).value) {
			var F = new RegExp("[?]");
			if (F.test(C.href)) {
				var D = C.href;
				C.href = C.getAttribute("data-href");
				C.setAttribute("data-href", D)
			}
		} else {
			var F = new RegExp("[?]");
			if (!F.test(C.href)) {
				var D = C.href;
				C.href = C.getAttribute("data-href");
				C.setAttribute("data-href", D)
			}
			C.href = C.href.replace("?newmap=1&ie=utf-8&s=s%26wd%3D",
					"?newmap=1&ie=utf-8&s=s&wd=");
			C.href = C.href
					.replace(
							new RegExp("(" + C.getAttribute("wdfield")
									+ "=)[^&]*"),
							"$1"
									+ encodeURIComponent(document
											.getElementById(A).value)).replace(
							"?newmap=1&ie=utf-8&s=s&wd=",
							"?newmap=1&ie=utf-8&s=s%26wd%3D");
			C.href = C.href.replace(new RegExp("(" + C.getAttribute("wdfield")
					+ "=)[^&]*"), "$1"
					+ T.url.escapeSymbol(document.getElementById(A).value))
		}
	}
})(commonHeaderConf.searchInputId);
baidu.dom
		.ready(function() {
			
			var B = window.WK_INFO || {
				tpl : ""
			};
			switch (B.tpl) {
			case "upload":
			case "multiupload":
			case "albumNew":
			case "albumView":
			case "lecture#lec_upload":
			case "lecture#lec_upload_mod":
			case "usercenter#userinfo":
			case "usercenter#useralbum":
			case "usercenter#userdoc":
			case "usercenter#usercredit":
			case "usercenter#userteacher":
				return

			}
			var A = baidu.topSug = new baidu.ui.Suggestion({
				view : function(F) {
					var E = baidu.browser.ie == 6 || baidu.browser.ie == 7;
					var D = 408;
					var C = 8;
					if (baidu.browser.chrome != undefined) {
						var C = 7
					}
					if (baidu.browser.firefox != undefined) {
						var C = 9
					}
					if (B.tpl == "search" || B.tpl == "lecture#lec_search") {
						D = 535;
						C = 8
					}
					return {
						top : F.top + (E ? 3 : 4),
						left : F.left - C,
						width : D
					}
				},
				getData : function(C) {
					baidu.sio.callByServer("http://nssug.baidu.com/su?wd="
							+ encodeURIComponent(C) + "&prod="
							+ commonHeaderConf.sugProdName + "&oe=utf-8&t="
							+ Math.random())
				},
				onconfirm : function(D) {
					if (!isNaN(D.data.index)) {
						var E = "/tongji/sug.gif?action=confirm&index="
								+ (D.data.index + 1) + "&t="
								+ Math.round(Math.random() * 2147483647);
						log.imageReq(E)
					}
					var C = baidu.g(commonHeaderConf.searchInputId).form;
					if (C.onsubmit && C.onsubmit() === false) {
						return

					}
					C.submit()
				},
				onbeforepick : function(C) {
					return function(D) {
						C.innerHTML = D.data.item.content;
						D.data.item.content = D.data.item.value = baidu.dom
								.getText(C)
					}
				}(document.createElement("div"))
			});
			A.render(commonHeaderConf.searchInputId);
			baidu.sug = function(D) {
				var F = D.s, E = D.q, C = [];
				baidu.array.each(F, function(I, H) {
					if (B.tpl == "view" && H >= 4) {
						return

					}
					if (E && I.indexOf(E) != -1) {
						C.push(I.replace(E, "<b>" + E + "</b>"))
					} else {
						C.push("<b>" + I + "</b>")
					}
				});
				A.appendHTML = '<a href="javascript:baidu.topSug.hide();void(0);">�ر�</a>';
				A.show(E, C);
				if (F.length) {
					var G = "/tongji/sug.gif?action=render&t="
							+ Math.round(Math.random() * 2147483647);
					log.imageReq(G)
				}
			}
			
			
			
			
		});
(function(G, N, C) {
	var H = baidu, I = H.dom, F = H.event, J = H.browser, L = H.array, A = H.object, B = H.lang, O = baidu.string, E = H.ajax, K = H.json, M = function(
			P) {
		if (!(this instanceof arguments.callee)) {
			return new arguments.callee(P)
		}
		this._init.call(this, P)
	};
	baidu.lang.inherits(M, B.Class);
	A.extend(M.prototype, {
		_init : function(P) {
			P = A.extend({
				element : "",
				eventName : "click",
				onClassName : "current",
				unClassName : "disabled",
				hdElement : I.query(P.element + ".tabControl>li"),
				bdElement : I.query(P.element + ".tabContent>li")
			}, P);
			A.extend(this, P);
			this._action()
		},
		_change : function(Q) {
			var P = this;
			H.each(this.bdElement, function(T, R) {
				var S = P.hdElement[R];
				if (Q === R) {
					P.dispatchEvent("onAfterCurrent", {
						hdElement : S,
						bdElement : T
					});
					P._current(T, S);
					P.dispatchEvent("onBeforeCurrent", {
						hdElement : S,
						bdElement : T
					})
				} else {
					P.dispatchEvent("onAfterDisabled", {
						hdElement : S,
						bdElement : T
					});
					P._disabled(T, S);
					P.dispatchEvent("onBeforeDisabled", {
						hdElement : S,
						bdElement : T
					})
				}
			})
		},
		_action : function() {
			var P = this;
			H.each(this.hdElement, function(R, Q) {
				H.on(R, P.eventName, function(S) {
					var T = baidu.event.get(S);
					P._change(Q);
					T.preventDefault()
				})
			})
		},
		_current : function(Q, P) {
			I.removeClass(Q, "disabled current");
			I.addClass(Q, "current");
			I.removeClass(P, "disabled current");
			I.addClass(P, "current")
		},
		_disabled : function(Q, P) {
			I.removeClass(Q, "disabled current");
			I.addClass(Q, "disabled");
			I.removeClass(P, "disabled current");
			I.addClass(P, "disabled")
		}
	});
	var D = function(P) {
		if (!(this instanceof arguments.callee)) {
			return new arguments.callee(P)
		}
		this._init.call(this, P)
	};
	B.inherits(D, M);
	A.extend(D.prototype, {
		_init : function(P) {
			P = A.extend({
				element : "",
				eventName : "click",
				onClassName : "current",
				unClassName : "disabled",
				hdElement : I.query(P.element + ".tabControl>li"),
				bdElement : I.query(P.element + ".tabContent>li"),
				time : 3000
			}, P);
			A.extend(this, P);
			this._run = true;
			this._num = this.hdElement.length - 1;
			this._action();
			this._start = 0
		},
		_action : function() {
			var P = this;
			setTimeout(function() {
				if (P._run) {
					P._start = P._start === P._num ? 0 : ++P._start;
					P._change(P._start)
				}
				setTimeout(arguments.callee, P.time)
			}, this.time);
			H.each(this.hdElement, function(R, Q) {
				H.each([ R, P.bdElement[Q] ], function(T, S) {
					H.on(T, "mouseover", function() {
						P._run = false
					});
					H.on(R, "mouseout", function() {
						P._run = true
					})
				})
			});
			D.superClass._action.call(this)
		}
	});
	B.module("wenku.tabs", M);
	B.module("wenku.slide", D)
})(window, document);
wenku = wenku || {};
wenku.slide2 = (function() {
	this.config = {
		width : 100,
		speed : 10,
		auto : true
	};
	var K = 10;
	var E = 0;
	var G = 0;
	var N = false;
	var P = null;
	var O = null;
	var X = "right";
	var M, D, C, B, S;
	var Y, U, K, J;
	function T(Z) {
		var Z = Z || {};
		Y = Z.width || config.width;
		U = Z.speed || config.speed;
		if (Z.auto === false) {
			J = false
		} else {
			J = config.auto
		}
		G = D.offsetHeight;
		if (G >= M.offsetHeight) {
			C.innerHTML = D.innerHTML
		}
	}
	function W(d, b, a, e, Z, c) {
		if (d != "" && baidu.G(d)) {
			M = baidu.G(d)
		} else {
			return

		}
		if (b != "" && baidu.G(b)) {
			D = baidu.G(b)
		} else {
			return

		}
		if (a != "" && baidu.G(a)) {
			C = baidu.G(a)
		} else {
			return

		}
		if (e != "" && baidu.G(e)) {
			B = baidu.G(e)
		} else {
			return

		}
		if (Z != "" && baidu.G(Z)) {
			S = baidu.G(Z)
		} else {
			return

		}
		T(c);
		if (G >= M.offsetHeight) {
			baidu.on(B, "mousedown", V);
			baidu.on(B, "mouseup", H);
			baidu.on(B, "mouseout", H);
			baidu.on(S, "mousedown", L);
			baidu.on(S, "mouseup", A);
			baidu.on(S, "mouseout", A);
			if (J) {
				M.onmouseover = function() {
					clearInterval(O)
				};
				M.onmouseout = R;
				R()
			}
		}
	}
	function V() {
		clearInterval(P);
		if (N) {
			return

		}
		if (J) {
			clearInterval(O)
		}
		N = true;
		X = "left";
		P = setInterval(I, U)
	}
	function I() {
		if (M.scrollTop <= 0) {
			M.scrollTop = G
		}
		M.scrollTop -= K
	}
	function H() {
		if (X == "right") {
			return

		}
		clearInterval(P);
		if (M.scrollTop % Y != 0) {
			E = -(M.scrollTop % Y);
			F()
		} else {
			N = false
		}
		if (J) {
			R()
		}
	}
	function L() {
		clearInterval(P);
		if (N) {
			return

		}
		if (J) {
			clearInterval(O)
		}
		N = true;
		X = "right";
		Q();
		P = setInterval(Q, U)
	}
	function Q() {
		if (M.scrollTop >= G) {
			M.scrollTop = 0
		}
		M.scrollTop += K
	}
	function A() {
		if (X == "left") {
			return

		}
		clearInterval(P);
		if (M.scrollTop % Y != 0) {
			E = Y - M.scrollTop % Y;
			F()
		} else {
			N = false
		}
		if (J) {
			R()
		}
	}
	function R() {
		clearInterval(O);
		O = setInterval(function() {
			L();
			A()
		}, 3000)
	}
	function F() {
		if (E == 0) {
			N = false;
			return

		}
		var Z, a = K;
		if (Math.abs(E) < Y / 2) {
			a = Math.round(Math.abs(E / K));
			if (a < 1) {
				a = 1
			}
		}
		if (E < 0) {
			if (E < -a) {
				E += a;
				Z = a
			} else {
				Z = -E;
				E = 0
			}
			M.scrollTop -= Z;
			setTimeout(F, U)
		} else {
			if (E > a) {
				E -= a;
				Z = a
			} else {
				Z = E;
				E = 0
			}
			M.scrollTop += Z;
			setTimeout(F, U)
		}
	}
	return {
		play : W,
		autoPlay : R,
		moveUp : V,
		moveUpStop : H,
		moveUpStop : H,
		moveDown : L,
		moveDownStop : A,
		moveDownStop : A
	}
})();
baidu.dom.ready(function() {
	var D = baidu.q("cate-menu-box-more"), B = baidu.dom
			.query(".tab-content .subtitle"), E = baidu.dom
			.query(".tab-control li"), F = baidu.q("rank-tab"), C = baidu
			.q("rank-content");
	baidu.array.each(D, function(H, G) {
		baidu.event.on(H, "mouseenter", function(J) {
			baidu.dom.addClass(H, "cate-menu-box-on");
			var I = baidu.q("popup-cate", H);
			if (I) {
				baidu.dom.show(I[0])
			}
		});
		baidu.event.on(H, "mouseleave", function(J) {
			baidu.dom.removeClass(H, "cate-menu-box-on");
			var I = baidu.q("popup-cate", H);
			if (I) {
				baidu.dom.hide(I[0])
			}
		})
	});
	wenku.tabs({
		element : baidu.dom.q("skin-feature-topic")[0],
		hdElement : baidu.dom.query(".tab-control>li"),
		bdElement : baidu.dom.query(".tab-content>li")
	});
	baidu.each(B, function(H, G) {
		E[G].innerHTML = "<p>" + H.innerHTML + "</p>"
	});
	baidu.each(E, function(H, G) {
		baidu.event.on(H, "mouseenter", function(I) {
			if (baidu.dom.hasClass(H, "disabled")) {
				baidu.dom.addClass(H, "tab-item-hover")
			}
		});
		baidu.event.on(H, "mouseleave", function(I) {
			baidu.dom.removeClass(H, "tab-item-hover")
		})
	});
	baidu.array.each(F, function(H, G) {
		var I = C[G];
		baidu.array.each(baidu.dom.children(H), function(L, K) {
			var M = baidu.dom.children(I);
			var J = K;
			baidu.event.on(L, "mouseover", function(O) {
				var N = baidu.dom.children(L.parentNode);
				for (K = 0; K < N.length; K++) {
					baidu.dom.removeClass(N[K], "tab-on");
					baidu.hide(M[K])
				}
				baidu.dom.addClass(L, "tab-on");
				baidu.show(M[J])
			})
		})
	});
	if (WK_INFO && WK_INFO.tpl != "topic#index") {
		if (baidu.cookie.get("BAIDU_DOC_NACT")) {
			var A = baidu.cookie.get("BAIDU_DOC_NACT");
			baidu.cookie.remove("BAIDU_DOC_NACT");
			if (!baidu.dom.g("login")) {
				if (A == "download") {
					view.download()
				} else {
					if (A == "upload") {
						location.href = "/new?fr=login"
					}
				}
			}
		}
	}
});