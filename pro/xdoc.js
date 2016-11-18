/*---------------*\
 * XDOC 客户端
\*---------------*/
var XDoc = {};
XDoc.version = "9.2.0";
//XDocServer地址
XDoc.server = "";
/*---------------*\
 * XDOC 基础服务
\*---------------*/
//显示
XDoc.show = function(xdoc, param, target) {
	XDoc.to(xdoc, "flash", param, target);
}
//转换
XDoc.to = function(xdoc, tarFormat, param, target) {
	if (!param) {
		param = {};
	}
	param._func = "to";
	param._format = tarFormat;
	if (xdoc != "xdoc") {
		param._url = xdoc;
	}
	XDoc._submit(param, target);
}
//运行
XDoc.run = function(xdoc, param, target) {
	if (!param) {
		param = {};
	}
	param._func = "run";
	if (xdoc != "xdoc") {
		param._url = xdoc;
	}
	XDoc._submit(param, target);
}
//打印
XDoc.print = function(xdoc, param) {
	var frameId = "__xdocframe";
	var frame = document.getElementById(frameId);
	if (frame == null) {
		/*
		if (!+[1,]) {
			frame = document.createElement("<iframe frameborder=0 name=\"" + frameId + "\"><iframe>")
			frame.style.width = 10;
			frame.style.height = 10;
		} else {
		}
		*/
		frame = document.createElement("iframe");
		frame.name = frameId;
		frame.style.width = 0;
		frame.style.height = 0;
		frame.style.border = "none";
		frame.id = frameId;
		document.body.appendChild(frame);
	}
	if (!param) {
		param = {};
	}
	param._func = "run";
	if (xdoc != "xdoc") {
		param._url = xdoc;
	}
	param._format = "flash";
	param._print = true;
	XDoc._submit(param, frameId);
}
/*---------------*\
 * XDOC 文档模型
\*---------------*/
//元件
XDoc.comp = function(_tag, obj, child) {
	this.init = function(_tag, obj, child) {
		this._tag = _tag;
		if (obj) {
			if (typeof(obj) == "object") {
				for(a in obj) {
					this[a] = obj[a];
				}
			}
		}
		if (child) {
			if (typeof(child) == "array") {
				for (var i = 0; i < child.length; i++) {
					this.add(child[i]);
				}
			} else {
				this.add(child);
			}
		}
	}
	this.add = function(comp) {
		if (!this._items) {
			this._items = new Array();
		}
		this._items[this._items.length] = comp;
	}
	this.init(_tag, obj, child);
	this.toXml = function(writer) {
		writer.beginNode(this._tag);
		for(a in this) {
			if (a != "_tag" && a != "_text") {
				if (typeof(this[a])=="string") {
					writer.att(a, this[a]);
				} else if (typeof(this[a]) == "number" || typeof(this[a]) == "boolean") {
					writer.att(a, this[a].toString());
				}
			}
		}
		if (this._items) {
			for(var i = 0; i < this._items.length; i++) {
				this._items[i].toXml(writer);
			}
		}
		if (this._text) {
			writer.writeString(this._text);
		}
		writer.endNode();
	}
}
//段落
XDoc.para = function(obj, child) {
	this.init("para", obj, child);
}
XDoc.para.prototype = new XDoc.comp();
//文字
XDoc.text = function(text, obj) {
	this.init("text", obj, null);
	if (text) {
		this._text = text;
	} 
}
XDoc.text.prototype = new XDoc.comp();
//方框
XDoc.rect = function(obj, child) {
	this.init("rect", obj, child);
}
XDoc.rect.prototype = new XDoc.comp();
//条形码
XDoc.barcode = function(value, obj, child) {
	this.value = value;
	this.init("barcode", obj, child);
	if (!this.type) {
		this.type = "QRCode";
	}
	if (!this.width) {
		this.width = 120;
	}
	if (!this.height) {
		this.height = 120;
	}
}
XDoc.barcode.prototype = new XDoc.rect();
//图表
XDoc.chart = function(data, obj, child) {
	this.data = data;
	this.init("chart", obj, child);
	if (!this.width) {
		this.width = 300;
	}
	if (!this.height) {
		this.height = 200;
	}
}
XDoc.chart.prototype = new XDoc.rect();
//椭圆
XDoc.ellipse = function(obj, child) {
	this.init("ellipse", obj, child);
	if (!this.width) {
		this.width = 120;
	}
	if (!this.height) {
		this.height = 120;
	}
}
XDoc.ellipse.prototype = new XDoc.rect();
//图片
XDoc.img = function(src, obj, child) {
	this.src = src;
	this.init("img", obj, child);
	if (!this.sizeType) {
		this.sizeType = "autosize";
	}
}
XDoc.img.prototype = new XDoc.rect();
//线
XDoc.line = function(startX, startY, endX, endY, obj, child) {
	this.startX = startX;
	this.startY = startY;
	this.endX = endX;
	this.endY = endY;
	this.init("line", obj, child);
}
XDoc.line.prototype = new XDoc.rect();
//路径
XDoc.path = function(path, obj, child) {
	this.path = path;
	this.init("path", obj, child);
	if (!this.width) {
		this.width = 120;
	}
	if (!this.height) {
		this.height = 120;
	}
}
XDoc.path.prototype = new XDoc.rect();
//多边形
XDoc.polygon = function(points, obj, child) {
	this.points = points;
	this.init("polygon", obj, child);
	if (!this.width) {
		this.width = 120;
	}
	if (!this.height) {
		this.height = 120;
	}
}
XDoc.polygon.prototype = new XDoc.rect();
//图形文字
XDoc.stext = function(text, obj, child) {
	this.text = text;
	this.init("stext", obj, child);
	if (!this.width) {
		this.width = 200;
	}
	if (!this.height) {
		this.height = 100;
	}
}
XDoc.stext.prototype = new XDoc.rect();
//表格
XDoc.table = function(obj, child) {
	this.init("table", obj, child);
	this.add = function(comp, row, col, rowSpan, colSpan) {
		if (comp._tag == "text" || comp._tag == "para") {
			comp = new XDoc.rect({}, comp);
		}
		if (row) {
			comp.row = row + 1;
		}
		if (col) {
			comp.col = col + 1;
		}
		if (rowSpan) {
			comp.rowSpan = rowSpan;
		}
		if (colSpan) {
			comp.colSpan = colSpan;
		}
		if (!this._items) {
			this._items = new Array();
		}
		this._items[this._items.length] = comp;
	}
}
XDoc.table.prototype = new XDoc.rect();
//元数据
XDoc.meta = function(obj) {
	this.init("meta", obj, null);
}
XDoc.meta.prototype = new XDoc.comp();
//纸张
XDoc.paper = function(obj) {
	if (obj && obj.margin) {
		this.leftMargin = obj.margin;
		this.rightMargin = obj.margin;
		this.topMargin = obj.margin;
		this.bottomMargin = obj.margin;
	}
	this.init("paper", obj, null);
}
XDoc.paper.prototype = new XDoc.comp();
//XDOC
XDoc.xdoc = function() {
	this.back = new XDoc.comp("back");
	this.front = new XDoc.comp("front");
	this.body = new XDoc.comp("body");
	this.toXml = function() {
		var writer = new XDoc._Writer();
		writer.beginNode("xdoc");
		writer.att("version", XDoc.version);
		if (this.meta) {
			this.meta.toXml(writer);
		}
		if (this.paper) {
			this.paper.toXml(writer);
		}
		this.back.toXml(writer);
		this.front.toXml(writer);
		this.body.toXml(writer);
		writer.endNode();
		writer.close();
		return writer.toString();
	}
	this.show = function(target) {
		this.toFlash(target);
	}
	this.print = function() {
		XDoc.print("xdoc", {_xdoc:this.toXml()});
	}
	this.to = function(format, target) {
		var param = {};
		if (format) {
			param._format = format;
		}
		param._func = "to";
		param._xdoc = this.toXml();
		XDoc._submit(param, target);
	}
	this.toFlash = function(target) {
		this.to("flash", target);
	}
	this.toPdf = function(target) {
		this.to("pdf", target);
	}
	this.toDocx = function(target) {
		this.to("docx", target);
	}
	this.toPpt = function(target) {
		this.to("ppt", target);
	}
	this.toXls = function(target) {
		this.to("xls", target);
	}
	this.toHtml = function(target) {
		this.to("html", target);
	}
	this.toPng = function(target) {
		this.to("png", target);
	}
	this.toSvg = function(target) {
		this.to("svg", target);
	}
}
/*---------------*\
 * XDOC 表单服务
\*---------------*/
//创建表单,xdoc必须部署在xdocserver
XDoc.createForm = function(xdoc, tarid, id) {
	var fpdurl = encodeURIComponent(XDoc.server + "/xdoc?_func=to&_format=fpd&_url=" + encodeURIComponent(xdoc));
	var container;
	if (tarid) {
		container = document.getElementById(tarid);
	}
	if (!container) {
		container = document.body;
	}
	if (!id) {
		id = "XDocForm";
	}
	container.innerHTML = "<object id='" + id + "' classid='clsid:D27CDB6E-AE6D-11cf-96B8-444553540000' codebase='http://fpdownload.macromedia.com/get/flashplayer/current/swflash.cab' width='100%' height='100%'>"
		+ "<param name='movie' value='" + XDoc.server + "/" + "fpd.swf?id=" + id + "&url=" + fpdurl + "'>"
		+ "<param name='quality' value='high'>"
		+ "<param name='bgcolor' value='#869ca7'>"
		+ "<param name='allowScriptAccess' value='always'>"
		+ "<param name='allowFullScreen' value='true'>"
		+ "<comment>"
		+ "<embed id='" + id + "__' src='" + XDoc.server + "/" + "fpd.swf?id=" + id + "&url=" + fpdurl + "' quality='high' bgcolor='#869ca7'"
		+ "width='100%' height='100%' name='" + id + "' align='middle'"
		+ "play='true' loop='false' allowScriptAccess='always' allowFullScreen='true' type='application/x-shockwave-flash'"
		+ "pluginspage='http://www.adobe.com/go/getflashplayer'>"
		+ "</embed>"
		+ "</comment>"
		+ "</object>";
}
//获取表单
XDoc.form = function(id) {
	if (!id) {
		id = "XDocForm";
	}
	var form = document.getElementById(id + "__");
	if (form == null || navigator.appName == "Microsoft Internet Explorer" && navigator.appVersion.match(/9./i)=="9.")  {
		form = document.getElementById(id);
	}
	return form;
}
/**
 * onXDocFormOpen
 * XDoc表单加载完成自动调用,XDocForm创建时的id
 * 例如：id为XDocForm，调用方法为onXDocFormOpen
 */
/**
 * onXDocFormDataChange
 * 输入项修改后自动调用,XDocForm创建时的id
 * 例如：id为XDocForm，调用方法为onXDocFormDataChange
 */
/**
 * print
 * 打印
 * 例如：XDoc.form().print();
 */
/**
 * getItemValue
 * 获取输入项值
 * @param name 输入项名称
 * @return 输入项值
 * 例如：XDoc.form().getItemValue("名称");
 */
/**
 * setItemValue
 * 设置输入项值
 * @param name 输入项名称
 * @param value 值
 * 例如：XDoc.form().setItemValue("名称", "值");
 */
/**
 * getXmlData
 * 获取输入项值xml
 * @return xml 例如：<data><名称>值</名称></data>
 * 例如：XDoc.form().getXmlData();
 */
/**
 * setXmlData
 * 设置输入项值xml
 * @param xml
 * 例如：XDoc.form().setXmlData("<data><名称>值</名称></data>");
 */
/**
 * setItemEnable
 * 设置输入项是否启用
 * @param name 输入项名称
 * @param enable 是否启用
 * 例如：XDoc.form().setItemEnable("名称", false);
 */
/**
 * setItemsEnable
 * 设置所有输入项是否启用
 * @param enable 是否启用
 * 例如：XDoc.form().setItemsEnable(false);
 */
/**
 * clearItemsValue
 * 清空输入值，设置为缺省值
 * 例如：XDoc.form().clearItemsValue();
 */
/**
 * setBarVisible
 * 设置工具条是否显示
 * @param visible 是否显示
 * 例如：XDoc.form().setBarVisible(false);
 */
/**
 * isBarVisible
 * 工具条是否显示
 * 例如：XDoc.form().isBarVisible();
 */
/**
 * setOpenEnable
 * 设置是否可打开
 * @param enable 是否可打开
 * 例如：XDoc.form().setOpenEnable(false);
 */
/**
 * setSaveEnable
 * 设置是否可保存
 * @param enable 是否可保存
 * 例如：XDoc.form().setSaveEnable(false);
 */
/**
 * setOpenEnable
 * 设置是否可打印
 * @param enable 是否可打印
 * 例如：XDoc.form().setPrintEnable(false);
 */
/**
 * validateItems
 * 校验输入项值
 * @return 是否有效
 * 例如：XDoc.form().validateItems();
 */
//创建编辑器
XDoc.createBuilder = function(tarid) {
	var container;
	if (tarid) {
		container = document.getElementById(tarid);
	}
	if (!container) {
		container = document.body;
	}
	var id = "XDocBuilder";
	container.innerHTML = "<object id=\"" + id + "\" classid=\"clsid:8AD9C840-044E-11D1-B3E9-00805F499D93\" codebase=\"" 
							+ XDoc.server + "/jre.exe" + "\" width=\"100%\" height=\"100%\">"
	    + "<param name=\"type\" value=\"application/x-java-applet\">"
	    + "<param name=\"code\" value=\"com.hg.xdoc.XDocApplet.class\">"
		+ "<param name=\"archive\" value=\"" + XDoc.server + "/_lib/xdoc.jar\">"
		+ "<param name=\"serverUrl\" value=\"" + XDoc.server + "\">"
	    + "<param name=\"scriptable\" value=\"true\">"
	    + "<comment>"
	    + "<embed id=\"" + id + "__\""
	    + " type=\"application/x-java-applet\"" 
	    + " code=\"com.hg.xdoc.XDocApplet.class\""
	    + " archive=\"" + XDoc.server + "/_lib/xdoc.jar\""
	    + " width=\"100%\" height=\"100%\""
	    + " scriptable=\"true\"" 
	    + " serverUrl=\"" + XDoc.server + "\""
	    + " pluginspage=\"http://java.sun.com/products/plugin/index.html#download\">"
	    + "</embed>"
	    + "</comment>"
	    + "</object>";
}
//获取编辑器
XDoc.builder = function() {
	var id = "XDocBuilder";
	var builder = document.getElementById(id + "__");
	if (builder == null || navigator.appName == "Microsoft Internet Explorer" && navigator.appVersion.match(/9./i)=="9.")  {
		builder = document.getElementById(id);
	}
	return builder.builder;
}
/*---------------*\
 * XDOC 基础JS
 * 不要直接引用
\*---------------*/
XDoc._Writer = function() {
    this.XML=[];
    this.nodes=[];
    this.state="";
    this.formatXML = function(str) {
        if (str)
            return str.replace(/&/g, "&amp;").replace(/\"/g, "&quot;").replace(/</g, "&lt;").replace(/>/g, "&gt;");
        return ""
    }
    this.beginNode = function(name) {
        if (!name) return;
        if (this.state=="beg") this.XML.push(">");
        this.state="beg";
        this.nodes.push(name);
        this.XML.push("<"+name);
    }
    this.endNode = function() {
        if (this.state=="beg")
        {
            this.XML.push("/>");
            this.nodes.pop();
        }
        else if (this.nodes.length>0)
            this.XML.push("</"+this.nodes.pop()+">");
        this.state="";
    }
    this.att = function(name, value) {
        if (this.state!="beg" || !name) return;
        var ev = "";
        for (var i = 0; i < value.length; i++) {
        	if (value.charAt(i) == "\\") {
        		ev += "\\\\";
        	} else if (value.charAt(i) == "\t") {
        		ev += "\\t";
        	} else if (value.charAt(i) == "\n") {
        		ev += "\\n";
        	} else {
        		ev += value.charAt(i);
        	}
        }
        this.XML.push(" "+name+"=\""+this.formatXML(ev)+"\"");
    }
    this.writeString = function(value)
    {
        if (this.state=="beg") this.XML.push(">");
        this.XML.push(this.formatXML(value));
        this.state="";
    }
    this.node = function(name, value)
    {
        if (!name) return;
        if (this.state=="beg") this.XML.push(">");
        this.XML.push((value=="" || !value)?"<"+name+"/>":"<"+name+">"+this.formatXML(value)+"</"+name+">");
        this.state="";
    }
    this.close = function() {
        while (this.nodes.length>0)
            this.endNode();
        this.state="closed";
    }
    this.toString = function(){return this.XML.join("");}
}
XDoc._submit = function(param, target) {
	if (XDoc.server != "") {
		if (param && typeof(param._xdata) == "string" 
			&& param._xdata.length > 0 
			&& param._xdata.charAt(0) != "{"
			&& param._xdata.charAt(0) != "<") {
			//url地址，用Ajax获取内容
			XDoc._ajax.post({"url":param._xdata,"xtra":{"param":param,"target":target},"callback":function(success, http, xtra) {
				var text = http.responseText;
				if (text.length > 0 
					&& text.charAt(0) != "{"
					&& text.charAt(0) != "<") {
					alert("无效的xdata数据:" + text);
					xtra.param._xdata = "";
				} else {
					xtra.param._xdata = text;
				}
				XDoc._submit(xtra.param, xtra.target);
			}});
			return;
		}
		var formId = "__xdocform";
		var form = document.getElementById(formId);
		if (form == null) {
			form = document.createElement("form");
			form.id = formId;
			form.style.display = "none";
			document.body.appendChild(form);
			form.method = 'post';
			//使用utf-8
			form.acceptCharset = "UTF-8";
			if (!+[1,]) {
				//让IE支持acceptCharset
				var el = document.createElement("input");
				el.setAttribute("name", "_charset_");
				el.setAttribute("value", "♥");
				form.appendChild(el);
			}
		} else {
			form.innerHTML = "";
		}
		form.action = XDoc.server + "/xdoc";
		if (target == undefined) {
			target = "_self";
		}
		form.target = target;
		if (param) {
			for(a in param) {
				var el = document.createElement("input");
				el.setAttribute("id", formId + a);
				el.setAttribute("name", a);
				el.setAttribute("type", "hidden");
				form.appendChild(el);
				if (typeof(param[a]) == "object") {
					document.getElementById(formId + a).value = XDoc._JSON.stringify(param[a]);
				} else {
					document.getElementById(formId + a).value = param[a];
				}
			}
		}
		form.submit();
	} else {
		alert("XDoc.server未设置");
	}
}
XDoc._ajax = {
    /**
     * Starts a request
     * args = {};
     * @param string args.method        GET or POST
     * @param string args.url           Request URL
     * @param integer args.tries        Maximum request tries
     * @param mixed args.params         Params that will be sent to URL
     * @param function args.callback    Function that will receive the request object
     * @param function args.filter      Function that will receive every param you send
     * @param function args.onload
     * @param function args.onrequest
     * @param function args.xtra        Callback arguments
     */
    'request': function(args)
    {
        var http = this.create(), self = this, tried = 0, tmp, i, j;
        var onload, onrequest, filter, callback, tries, url, method, xtra, params;
        
        onload = args.onload;
        onrequest = args.onrequest;
        filter = args.filter;
        callback = args.callback;
        tries = args.tries;
        url = args.url;
        method = args.method;
        xtra = args.xtra;
        params = args.params;
        method = method.toLowerCase();
        if (params) {
            if (typeof params == 'object') {
                tmp = [];
                for (i in params) {
                    j = params[i];
                    tmp.push(i + '=' + (typeof filter == 'function'? filter.call(null, j) : encodeURIComponent(j)));
                }
                params = tmp.join('&');
            }
            if (method == 'get') {
				url += url.indexOf('?') == -1? '?' + params : '&' + params;
			}
        }
        http.open(method, url, true);
        if (method == 'post') {
			http.setRequestHeader('Method', 'POST ' + url + ' HTTP/1.1');
			http.setRequestHeader('Content-type', 'application/x-www-form-urlencoded');
		} else {
			params = null;
		}
        http.onreadystatechange = function()
        {
            if (http.readyState != 4) {
                return;
            }
            if (typeof onload == 'function') {
				onload.call(null);
			}
            if (http.status == 200) {
                if (typeof callback == 'function') {
                    callback.call(null, true, http, xtra);
                }
            } else {
                if (tries > 0) {
                    if (tried < tries) {
						tried++;
						http.abort();
						http.send(params);
					}
                } else if (typeof callback == 'function') {
					callback.call(false, http, xtra);
				}
            }
        };
        if (typeof onrequest == 'function') {
			onrequest.call(null);
		}
        http.send(params);
        return http;
    }
    ,
    /**
     * Creates XMLHttpRequest
     */
    'create': function()
    {
        var http;
        try {
            http = new XMLHttpRequest();
        } catch (e) {
            try {
                http = new ActiveXObject('Msxml2.XMLHTTP');
            } catch (f) {
                try {
                    http = new ActiveXObject('Microsoft.XMLHTTP');
                } catch (g) { null; }
            }
        }
        return http;
    }
    ,
    'get': function(args)
    {
        args.method = 'GET';
        this.request(args);
    }
    ,
    'post': function(args)
    {
        args.method = 'POST';
        this.request(args);
    }
};
XDoc._JSON = {};
(function () {
    'use strict';

    function f(n) {
        // Format integers to have at least two digits.
        return n < 10 ? '0' + n : n;
    }

    if (typeof Date.prototype.toJSON !== 'function') {

        Date.prototype.toJSON = function (key) {

            return isFinite(this.valueOf())
                ? this.getYear()     + '-' +
                    f(this.getMonth() + 1) + '-' +
                    f(this.getDate())      + ' ' +
                    f(this.getHours())     + ':' +
                    f(this.getMinutes())   + ':' +
                    f(this.getSeconds()) 
                : null;
        };

        String.prototype.toJSON      =
            Number.prototype.toJSON  =
            Boolean.prototype.toJSON = function (key) {
                return this.valueOf();
            };
    }

    var cx = /[\u0000\u00ad\u0600-\u0604\u070f\u17b4\u17b5\u200c-\u200f\u2028-\u202f\u2060-\u206f\ufeff\ufff0-\uffff]/g,
        escapable = /[\\\"\x00-\x1f\x7f-\x9f\u00ad\u0600-\u0604\u070f\u17b4\u17b5\u200c-\u200f\u2028-\u202f\u2060-\u206f\ufeff\ufff0-\uffff]/g,
        gap,
        indent,
        meta = {    // table of character substitutions
            '\b': '\\b',
            '\t': '\\t',
            '\n': '\\n',
            '\f': '\\f',
            '\r': '\\r',
            '"' : '\\"',
            '\\': '\\\\'
        },
        rep;


    function quote(string) {

        escapable.lastIndex = 0;
        return escapable.test(string) ? '"' + string.replace(escapable, function (a) {
            var c = meta[a];
            return typeof c === 'string'
                ? c
                : '\\u' + ('0000' + a.charCodeAt(0).toString(16)).slice(-4);
        }) + '"' : '"' + string + '"';
    }

    function str(key, holder) {

        var i,          // The loop counter.
            k,          // The member key.
            v,          // The member value.
            length,
            mind = gap,
            partial,
            value = holder[key];

        if (value && typeof value === 'object' &&
                typeof value.toJSON === 'function') {
            value = value.toJSON(key);
        }

        if (typeof rep === 'function') {
            value = rep.call(holder, key, value);
        }

        switch (typeof value) {
        case 'string':
            return quote(value);

        case 'number':

            return isFinite(value) ? String(value) : 'null';

        case 'boolean':
        case 'null':

            return String(value);

        case 'object':

            if (!value) {
                return 'null';
            }

            gap += indent;
            partial = [];

            if (Object.prototype.toString.apply(value) === '[object Array]') {

                length = value.length;
                for (i = 0; i < length; i += 1) {
                    partial[i] = str(i, value) || 'null';
                }

                v = partial.length === 0
                    ? '[]'
                    : gap
                    ? '[\n' + gap + partial.join(',\n' + gap) + '\n' + mind + ']'
                    : '[' + partial.join(',') + ']';
                gap = mind;
                return v;
            }

            if (rep && typeof rep === 'object') {
                length = rep.length;
                for (i = 0; i < length; i += 1) {
                    if (typeof rep[i] === 'string') {
                        k = rep[i];
                        v = str(k, value);
                        if (v) {
                            partial.push(quote(k) + (gap ? ': ' : ':') + v);
                        }
                    }
                }
            } else {

                for (k in value) {
                    if (Object.prototype.hasOwnProperty.call(value, k)) {
                        v = str(k, value);
                        if (v) {
                            partial.push(quote(k) + (gap ? ': ' : ':') + v);
                        }
                    }
                }
            }

            v = partial.length === 0
                ? '{}'
                : gap
                ? '{\n' + gap + partial.join(',\n' + gap) + '\n' + mind + '}'
                : '{' + partial.join(',') + '}';
            gap = mind;
            return v;
        }
    }

    if (typeof XDoc._JSON.stringify !== 'function') {
        XDoc._JSON.stringify = function (value, replacer, space) {

            var i;
            gap = '';
            indent = '';

            if (typeof space === 'number') {
                for (i = 0; i < space; i += 1) {
                    indent += ' ';
                }

            } else if (typeof space === 'string') {
                indent = space;
            }

            rep = replacer;
            if (replacer && typeof replacer !== 'function' &&
                    (typeof replacer !== 'object' ||
                    typeof replacer.length !== 'number')) {
                throw new Error('XDoc._JSON.stringify');
            }

            return str('', {'': value});
        };
    }


    if (typeof XDoc._JSON.parse !== 'function') {
        XDoc._JSON.parse = function (text, reviver) {

            var j;

            function walk(holder, key) {

                var k, v, value = holder[key];
                if (value && typeof value === 'object') {
                    for (k in value) {
                        if (Object.prototype.hasOwnProperty.call(value, k)) {
                            v = walk(value, k);
                            if (v !== undefined) {
                                value[k] = v;
                            } else {
                                delete value[k];
                            }
                        }
                    }
                }
                return reviver.call(holder, key, value);
            }

            text = String(text);
            cx.lastIndex = 0;
            if (cx.test(text)) {
                text = text.replace(cx, function (a) {
                    return '\\u' +
                        ('0000' + a.charCodeAt(0).toString(16)).slice(-4);
                });
            }

            if (/^[\],:{}\s]*$/
                    .test(text.replace(/\\(?:["\\\/bfnrt]|u[0-9a-fA-F]{4})/g, '@')
                        .replace(/"[^"\\\n\r]*"|true|false|null|-?\d+(?:\.\d*)?(?:[eE][+\-]?\d+)?/g, ']')
                        .replace(/(?:^|:|,)(?:\s*\[)+/g, ''))) {

                j = eval('(' + text + ')');

                return typeof reviver === 'function'
                    ? walk({'': j}, '')
                    : j;
            }

            throw new SyntaxError('XDoc._JSON.parse');
        };
    }
}());
