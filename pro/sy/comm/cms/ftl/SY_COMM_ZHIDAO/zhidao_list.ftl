<!DOCTYPE html>
<!--STATUS OK-->
<html style='background:#fff'>
<head>
<meta http-equiv="X-UA-Compatible" content="IE=Edge">
<meta http-equiv="content-type" content="text/html; charset=UTF-8">
<title></title> 
<#include "global.ftl"/>
<#include "/SY_COMM_ZHIDAO/config_constant.ftl" />
<script type="text/javascript">
	var FireFlyContextPath = '';
</script>
<script type="text/javascript" src="/sy/base/frame/jquery-1.8.2.min.js"></script>
<script type="text/javascript" src="/sy/base/frame/constant.js"></script>
<script type="text/javascript" src="/sy/base/frame/tools.js"></script>
<script type="text/javascript" src="/sy/base/frame/platform.js"></script>
<script type="text/javascript" src="/sy/comm/zhidao/zhihu_style_files/zhidao_comment.js"></script>
<script type="text/javascript" src="/sy/comm/cms/js/zhidao.js"></script>
<script type="text/javascript" src="/sy/comm/zhidao/js/zhidao_list.js"></script>
<script type="text/javascript" src="/sy/base/frame/jquery.tmpl.js"></script>
<link rel="stylesheet" type="text/css" href="/sy/comm/zhidao/css/zhidao_index.css">
<link rel="stylesheet" type="text/css" href="/sy/comm/zhidao/zhihu_style_files/z1303291140368123.css">
<link rel="stylesheet" type="text/css" href="/sy/comm/zhidao/baidu_style_files/module_fe8dd525.css">
<link rel="stylesheet" type="text/css" href="/sy/comm/zhidao/baidu_style_files/module_c595d1cf.css">
<link rel="stylesheet" type="text/css" href="/sy/comm/wenku/baidu_style_files/framework_7d9033ac.css">
<link rel="stylesheet" type="text/css" href="/sy/comm/wenku/baidu_style_files/module_index_777ae95d.css">
<link rel="stylesheet" type="text/css" href="/sy/comm/wenku/baidu_style_files/index_50da8c9a.css">
<link rel="stylesheet" type="text/css" href="/sy/comm/zhidao/css/userzhidao_center.css">
<link rel="stylesheet" type="text/css" href="/sy/comm/zhidao/css/zhidao_list.css">
<!---------------返回顶部的Js/CSS------开始--------------->
<link rel="stylesheet" type="text/css" href="/sy/comm/zhidao/css/zd-goto-top.css">
<script type="text/javascript" src="/sy/comm/zhidao/js/zd-goto-top.js"></script>
<!---------------返回顶部的Js/CSS------结束--------------->
<!------------赞同、反对的样式--------开始------------------->
<link rel="stylesheet" type="text/css" href="/sy/comm/zhidao/zhihu_style_files/c60843d5fb7d7aeb2bfa3619734d9b18.css">
<!------------赞同、反对的样式--------结束------------------->
<!-----------------在线状态的JS/CSS----开始------------------------>
<script type="text/javascript" src="/sy/comm/zhidao/js/user_img.js"></script>
<link rel="stylesheet" type="text/css" href="/sy/theme/default/common.css">
<!-----------------在线状态的JS/CSS----结束------------------------>
<script>
	var chnlId = '${data.CHNL_ID}';
	var noAnswer = '${noAnswer!''}';
	var page = '${page}';
</script>
<#noparse>
<script id="js-quesList-Template" type="text/x-jquery-tmpl">
	<tr class="quick-tr" show="no" alog-action="br-quick-btn">
		<td class="align-l"	id="a-${Q_ID}" cid="872">
			<a class="title" target="_self" href="javascript:view('${Q_ID}','${Q_TITLE}');"> ${Q_TITLE} </a>
		</td>
		<td>
		{{if Q_ANSWER_COUNTER == 0}}
			
		{{else}}
			<!--<a id="a2-${Q_ID}" qId="${Q_ID}" class="q js-best-answer" href="javascript:void(0);" style="display:none;"  param="0">精选答案</a>-->
		{{/if}}
		</td>
		<td class="quick-num">
		{{if Q_ANSWER_COUNTER == 0}}
			0
		{{else}}
			${Q_ANSWER_COUNTER}
		{{/if}}
		回复</td>
		<td class="quick-time timeago">${parent.$.timeago(parent.Format.substr(0,19,S_MTIME))}</td>
	</tr>
	<tr id="tr-${Q_ID}" style="display:none;">
		
	</tr>
</script>
</#noparse>
<meta name="keywords" content="软虹知道">
<script type="text/javascript">
	(function() {
		function b(f) {
			if (f >= 0) {
				for ( var e = 0; e < 75; e += 25) {
					if (f >= e && f < e + 25) {
						return e / 25
					}
				}
				if (f >= 75 && f < 500) {
					return 3
				}
				return 4
			}
		}
		var d = function(e) {
			this.name = e;
			this.dataArray = [ 0, 0, 0, 0, 0 ]
		};
		d.prototype.start = function() {
			var g = 50, j, f, i = this;
			function e() {
				f = new Date;
				var k = f - j - g;
				k = k < 0 ? 0 : k;
				i.dataArray[b(k)]++;
				h()
			}
			function h() {
				j = new Date;
				i.timer = setTimeout(e, g)
			}
			h()
		};
		d.prototype.end = function() {
			this.timer = clearTimeout(this.timer)
		};
		d.prototype.toString = function() {
			return '"' + this.name + '":[' + this.dataArray.join(",") + "]"
		};
		var a = {};
		var c = {
			start : function(e, f) {
				f = f || a;
				if (f) {
					if (!f[e]) {
						f[e] = new d(e)
					}
					f[e].start()
				}
			},
			end : function(e, f) {
				f = f || a;
				if (f) {
					if (f[e]) {
						f[e].end()
					}
				}
			},
			toString : function(g) {
				g = g || a;
				var e = [];
				if (g) {
					for ( var f in g) {
						if (g.hasOwnProperty(f)) {
							e.push(g[f].toString())
						}
					}
				}
				return "{" + e.join(",") + "}"
			},
			length : function() {
				return a.length
			}
		};
		window.CPU_MONITOR = c
	})(window);
	var PDC = {
		_version : "0.8",
		_render_start : new Date().getTime(),
		_analyzer : {
			loaded : false,
			url : "http://static.tieba.baidu.com/tb/pms/wpo.pda.js?v=0.8",
			callbacks : []
		},
		_opt : {
			is_login : false,
			sample : 0,
			product_id : 0,
			page_id : 0,
			special_pages : [],
			display : false
		},
		_cpupool : {},
		_timingkey : "start",
		_timing : {},
		init : function(d) {
			for ( var c in d) {
				if (d.hasOwnProperty(c)) {
					this._opt[c] = d[c]
				}
			}
		},
		mark : function(b) {
			this._timing[b] = new Date().getTime();
			if (window.CPU_MONITOR) {
				CPU_MONITOR.end(this._lastkey + "-" + this._timingkey,
						this._cpupool);
				if (b != "let") {
					CPU_MONITOR.start(this._timingkey + "-" + b, this._cpupool);
					this._lastkey = this._timingkey;
					this._timingkey = b
				}
			}
		},
		view_start : function() {
			this.mark("vt")
		},
		tti : function() {
			this.mark("tti")
		},
		page_ready : function() {
			this.mark("fvt")
		},
		metadata : function() {
			var c = this._opt;
			var e = {
				env : {
					user : (c.is_login == true ? 1 : 0),
					product_id : c.product_id,
					page_id : PDC._is_sample(c.sample) ? c.page_id : 0
				},
				common_resources : c.common_resources,
				special_resources : c.special_resources,
				render_start : this._render_start,
				timing : this._timing,
				display : c.display
			};
			var a = [];
			var d = c.special_pages;
			for ( var b = 0; b < d.length; b++) {
				if (PDC._is_sample(d[b]["sample"])) {
					a.push(d[b]["id"])
				}
			}
			if (a.length > 0) {
				e.env["special_id"] = "[" + a.join(",") + "]"
			}
			return e
		}
	};
	(function() {
		if (document.attachEvent) {
			window.attachEvent("onload", function() {
				PDC.mark("let");
				PDC._load_analyzer()
			})
		} else {
			window.addEventListener("load", function() {
				PDC.mark("lt")
			}, false)
		}
	})();
</script>
<!--[if lte IE 8]> <script>(function(){var e="abbr,article,aside,audio,canvas,datalist,details,dialog,eventsource,figure,footer,header,hgroup,mark,menu,meter,nav,output,progress,section,time,video".split(","),i=e.length;while(i--){document.createElement(e[i])}})();</script> <![endif]-->
<link rel="stylesheet" type="text/css"
	href="/sy/comm/zhidao/baidu_style_files/module_bb2ea40e.css">
<link rel="stylesheet" type="text/css"
	href="/sy/comm/zhidao/baidu_style_files/module_b443dbab.css">
<link rel="stylesheet" type="text/css"
	href="/sy/comm/zhidao/baidu_style_files/module-normal_9f9cb319.css">




<script type="text/javascript"
	src="/sy/comm/zhidao/baidu_style_files/base_aa43e0c6.js"></script>
<script type="text/javascript">
	F
			._fileMap({
				'/sy/comm/zhidao/baidu_style_files/framework_ae621eb1.js' : [
						'/static/common/lib/magic/control/Dialog/Dialog.js',
						'/static/common/lib/magic/Dialog/Dialog.js',
						'/static/common/lib/magic/control/Dialog/$mask/$mask.js',
						'/static/common/lib/magic/control/Dialog/$button/$button.js',
						'/static/common/lib/magic/setup/suggestion/suggestion.js',
						'/static/common/lib/magic/control/Carousel/Carousel.js',
						'/static/common/lib/magic/Carousel/Carousel.js',
						'/static/common/lib/magic/control/Carousel/$fx/$fx.js',
						'/static/common/lib/magic/control/Carousel/$autoScroll/$autoScroll.js',
						'/static/common/lib/magic/magic.js',
						'/static/common/lib/magic/Base/Base.js',
						'/static/common/lib/magic/Calendar/Calendar.js',
						'/static/common/lib/magic/control/control.js',
						'/static/common/lib/magic/control/Carousel/$button/$button.js',
						'/static/common/lib/magic/Carousel/$button/$button.js',
						'/static/common/lib/magic/control/Layer/Layer.js',
						'/static/common/lib/magic/control/Popup/Popup.js',
						'/static/common/lib/magic/Background/Background.js',
						'/static/common/lib/magic/Popup/Popup.js',
						'/static/common/lib/magic/control/DatePicker/DatePicker.js',
						'/static/common/lib/magic/control/DatePicker/$title/$title.js',
						'/static/common/lib/magic/Mask/Mask.js',
						'/static/common/lib/magic/Pager/Pager.js',
						'/static/common/lib/magic/setup/setup.js',
						'/static/common/lib/magic/setup/datePicker/datePicker.js',
						'/static/common/lib/magic/control/Suggestion/Suggestion.js',
						'/static/common/lib/magic/control/Tab/Tab.js',
						'/static/common/lib/magic/setup/tab/tab.js' ]
			});
</script>
<script type="text/javascript">
	F._fileMap({
		'/sy/comm/zhidao/baidu_style_files/module_c1889813.js' : [
				'/static/common/ui/util/util.js',
				'/static/common/ui/event/event.js',
				'/static/common/ui/tip/tip.js',
				'/static/common/ui/baidu/fx/fx.js',
				'/static/common/ui/baidu/baidu.js',
				'/static/common/ui/log/log.js',
				'/static/common/ui/baidu/form/form.js',
				'/static/common/ui/dialog/dialog.js',
				'/static/common/ui/category/category.js',
				'/static/common/ui/uri/uri.js',
				'/static/common/ui/login/login.js',
				'/static/common/ui/set-care-field/set-care-field.js',
				'/static/common/ui/store/store.js',
				'/static/common/ui/tip/move/move.js',
				'/static/common/ui/msg/msg.js',
				'/static/common/ui/suggestion/suggestion.js',
				'/static/common/ui/form/form.js', '/static/common/ui/ut/ut.js',
				'/static/common/ui/search-box/search-box.js',
				'/static/common/ui/tip/fade/fade.js',
				'/static/common/ui/set-class/set-class.js',
				'/static/common/ui/hi/hi.js',
				'/static/common/ui/submit/submit.js',
				'/static/common/ui/fixed-ask/fixed-ask.js',
				'/static/common/ui/template/template.js',
				'/static/widget/common/userbar/twbanner/twbanner.js',
				'/static/widget/common/userbar/userbar.js',
				'/static/widget/common/menu/menu.js' ]
	});
</script>
<script type="text/javascript">
	F._fileMap({
		'/sy/comm/zhidao/baidu_style_files/module_0affc023.js' : [
				'/static/browse/ui/related-list/related-list.js',
				'/static/widget/browse/path/path.js',
				'/static/browse/ui/fixed-ask/fixed-ask.js',
				'/static/widget/browse/expert/expert.js',
				'/static/widget/browse/category/category.js',
				'/static/widget/browse/process/process.js',
				'/static/browse/ui/position-select/position-select.js',
				'/static/widget/browse/admin/admin.js',
				'/static/browse/ui/show-map/show-map.js',
				'/static/browse/ui/quick-answer/quick-answer.js',
				'/static/widget/browse/rank-browse/rank-browse.js',
				'/static/widget/browse/care-field/care-field.js',
				'/static/widget/browse/rank/rank.js',
				'/static/widget/browse/reply/reply.js' ]
	});
</script>
<script type="text/javascript">
	F
			._fileMap({
				'/sy/comm/zhidao/baidu_style_files/module-normal_ec0bfd4e.js' : [ '/static/widget/browse/list/list.js' ]
			});
</script>
<script type="text/javascript">
	PDC.mark("ht");
	(function() {
		var a = PDC.ready = function() {
			var g = false, f = [], c;
			if (document.addEventListener) {
				c = function() {
					document.removeEventListener("DOMContentLoaded", c, false);
					d()
				}
			} else {
				if (document.attachEvent) {
					c = function() {
						if (document.readyState === "complete") {
							document.detachEvent("onreadystatechange", c);
							d()
						}
					}
				}
			}
			function d() {
				if (!d.isReady) {
					d.isReady = true;
					for ( var k = 0, h = f.length; k < h; k++) {
						f[k]()
					}
				}
			}
			function b() {
				try {
					document.documentElement.doScroll("left")
				} catch (h) {
					setTimeout(b, 1);
					return
				}
				d()
			}
			function e() {
				if (g) {
					return
				}
				g = true;
				if (document.addEventListener) {
					document.addEventListener("DOMContentLoaded", c, false);
					window.addEventListener("load", d, false)
				} else {
					if (document.attachEvent) {
						document.attachEvent("onreadystatechange", c);
						window.attachEvent("onload", d);
						var h = false;
						try {
							h = window.frameElement == null
						} catch (i) {
						}
						if (document.documentElement.doScroll && h) {
							b()
						}
					}
				}
			}
			e();
			return function(h) {
				d.isReady ? h() : f.push(h)
			}
		}();
		a.isReady = false
	})();
	PDC.ready(function() {
		PDC.mark("drt")
	});
	PDC.mark("vt");
</script>
<!-- 
<script src="/sy/comm/zhidao/baidu_style_files/zhidao.js"></script>
 -->
</head>
<body style="background-color: rgb(222, 226, 231); background-image: none; background-position: initial initial; background-repeat: initial initial;" class="pageBody">
<div class="layout-center has-menu" >
	<script type="text/javascript">
		F.context('user', {
			isLogin : '',
			name : '',
			id : '',
			gradeIndex : ''
		});
	</script>
	<script type="text/javascript">
		F.context({
			'cid' : 74,
			'isTag' : 0,
			'isEmpty' : 0
		});
	</script>	
	<div class="line">
		<div id="userbar" class="userbar">
			<ul class="main-list">

			</ul>
		</div>
	</div>
	
	<div id="body" class="container" style="width:100%;background-color:#FFF!important;">

		<#include "/SY_COMM_ZHIDAO/zhidao_navigation.ftl">
		<script type="text/javascript">
			jQuery("#menu").find("#category").addClass("current");
		</script>
		<script type="text/javascript">
			F.use('/static/widget/common/menu/menu.js');
			//alert(document.getElementById("body").style.width);
		</script>
		<div class="path" id="path" style="width:1160px;margin:0 auto;">
			<ul>
				
				<!----------------一级导航----开始------------------->
				<!--
				<li>
					<a href="<@chnlUrl root_channel_id 1 />" style="color: #878787">平台知道</a>&nbsp;&nbsp;&gt;
				</li>
				-->
				<!----------------一级导航----结束------------------->
				
				<!----------------二级导航----开始------------------->
				<li style="margin-left: 12px" alog-group="br-cate-1">
					<@channel_navigation debugName="二级导航" channelId="${data.CHNL_ID}">
						<#list tag_list as navi> 
							<#if navi.CHNL_ID == root_channel_id>
								<a href="<@chnlUrl root_channel_id 1/>" style="color: #878787">全部主题</a>&nbsp;&nbsp;&gt;
							<#else>
								<a href="<@chnlUrl navi.CHNL_ID 1/>" style="color: #878787">${navi.CHNL_NAME}</a>&nbsp;&nbsp;&gt;
							</#if>
						</#list>
					</@channel_navigation>
				</li>
				<!----------------二级导航----结束------------------->
				
				<!----------------三级导航----开始------------------->
				<li style="margin-left: 12px" alog-group="br-cate-1">
					<#if data.CHNL_ID != root_channel_id>
					<@channel_list debugName="下一级栏目导航" count="15" channelId="${data.CHNL_ID}">
					<#if tag_list?size == 0>
						<strong>${data.CHNL_NAME}</strong>
					<#else>
					<span class="path-downlist">${data.CHNL_NAME}</span>
					<div class="related-category"
						style="display: none; left: 218.167px; top: 143px;">
						<dl>
							<dt style="">
								<span class="i-downlist" style="color: #666666;">${data.CHNL_NAME}</span>
								
								<span style="color: #999999"></span>
							</dt>
							<#if data.CHNL_PID != ''>
								<@channel_list debugName="同级栏目导航" count="15" channelId="${data.CHNL_PID}"> 
									<#list tag_list as chnlObj> 
										<#if chnlObj.CHNL_ID != data.CHNL_ID>
											<dd style="">
												<a href="<@chnlUrl chnlObj.CHNL_ID 1/>">${chnlObj.CHNL_NAME}</a>
											</dd>
										</#if>
									</#list>
								</@channel_list>
							</#if>
						</dl>
					</div>
					</#if>
					</@channel_list>
					<#else> 
						<!--<a href="<@chnlUrl root_channel_id 1/>" style="color: #878787">全部问题</a>&nbsp;&nbsp;-->
						<script>
							jQuery(document).ready(function(){
								jQuery("#path").hide();
							});
						</script>
					</#if>
				</li>
				<!----------------三级导航----结束------------------->
			</ul>
		</div>
		<script type="text/javascript">
			F.use('/static/widget/browse/path/path.js');
		</script>
		<div class="l-main-container" style="width:1160px;margin:0 auto;">
			<div class="l-main-col" style="width:900px;">
				<div class="category top-box">
					<div class="hd">
						<#if data.CHNL_ID == root_channel_id>
							<h2>全部分类</h2>
						<#else>
							<h2>${data.CHNL_NAME}</h2>
						</#if>
						<#if data.CHNL_ID != root_channel_id>
						
						<!--------------添加关注的按钮-------开始-------------------->
						<!---点击按钮后将class改成btn_have_cared,未点击是btn_add_care--->
						<a id="btn_add_care" chnl_id="${data.CHNL_ID}" chnl_name="${data.CHNL_NAME}" class="btn_add_care"></a>
						<!--------------添加关注的按钮-------结束-------------------->
						</#if>
						<span class="add_care_error"></span>
					</div>
					<div class="bd">
						<ul alog-group="br-cate-10" class="clearfix">

							<@channel_list debugName="二级栏目导航" count="15" channelId="${data.CHNL_ID}">
								<#if tag_list?size == 0>
									<!------------------------当这级栏目下面的字栏目为空时--查询同级栏目-----开始------------------->
									<@channel_list debugName="子栏目为空，查同级栏目" count="15" channelId="${data.CHNL_PID}">
										<div style="left: 596.5px; top: 246px;" class="related-category">
											<#list tag_list as chnlObj>
											<li>
											<@channel_list debugName="子栏目导航" count="15" channelId="${chnlObj.CHNL_ID}">
											 	<#if tag_list?size == 0 >
											 		<#if data.CHNL_ID == chnlObj.CHNL_ID>
											 			<strong>${chnlObj.CHNL_NAME}</strong>
											 		<#else>
													  	<a href="<@chnlUrl chnlObj.CHNL_ID 1/>">${chnlObj.CHNL_NAME}</a> 
											 		</#if>
												  	<span style="color: #999999">
												  		<@ask_count debugName="栏目下主题统计" chnlId="${chnlObj.CHNL_ID}">
															(${count})
														</@ask_count> 
												  	</span>
												<#else>
													<div style="left: 596.5px; top: 246px;" class="related-category">
														<dl>
															<dt style="">
																<span class="i-downlist" style="color: #666666;"> 
																<#if data.CHNL_ID == chnlObj.CHNL_ID>
																	<strong>${chnlObj.CHNL_NAME}</strong>
																<#else>
																	<a href="<@chnlUrl chnlObj.CHNL_ID 1/>">${chnlObj.CHNL_NAME}</a>
																</#if>
																</span> 
																<span style="color: #999999">
																	<@ask_count debugName="栏目下主题统计" chnlId="${chnlObj.CHNL_ID}">
																		(${count})
																	</@ask_count> 
																</span>
															</dt>
															<#list tag_list as subChnlObj>
																<dd style="">
																	<a href="<@chnlUrl subChnlObj.CHNL_ID 1/>">${subChnlObj.CHNL_NAME}</a>
																</dd>
															</#list>
														</dl>
													</div> 
												</#if> 
											</@channel_list>
											</li> 
											</#list>
										</div>
									</@channel_list>
									<!------------------------当这级栏目下面的字栏目为空时--查询同级栏目-----结束------------------->
								<#else>
								 	<#list tag_list as chnlObj>
									<li>
									<@channel_list debugName="子栏目导航" count="15" channelId="${chnlObj.CHNL_ID}">
									 	<#if tag_list?size == 0 >
										  	<a href="<@chnlUrl chnlObj.CHNL_ID 1/>">${chnlObj.CHNL_NAME}</a> 
										  	<span style="color: #999999">
										  		<@ask_count debugName="栏目下主题统计" chnlId="${chnlObj.CHNL_ID}">
													(${count})
												</@ask_count> 
										  	</span>
										<#else>
											<div style="left: 596.5px; top: 246px;" class="related-category">
												<dl>
													<dt style="">
														<span class="i-downlist" style="color: #666666;"> 
														<a href="<@chnlUrl chnlObj.CHNL_ID 1/>">${chnlObj.CHNL_NAME}</a>
														</span> 
														<span style="color: #999999">
															<@ask_count debugName="栏目下主题统计" chnlId="${chnlObj.CHNL_ID}">
																(${count})
															</@ask_count> 
														</span>
													</dt>
													<#list tag_list as subChnlObj>
														<dd style="">
															<a href="<@chnlUrl subChnlObj.CHNL_ID 1/>">${subChnlObj.CHNL_NAME}</a>
														</dd>
													</#list>
												</dl>
											</div> 
										</#if> 
									</@channel_list>
									</li> 
									</#list> 
								</#if>
							</@channel_list>
						</ul>
					</div>
				</div>
				<script type="text/javascript">
					F.use('/static/widget/browse/category/category.js',
							function(Cate) {
								Cate.init();
							});
				</script>
				<a name="list"></a>
				<div class="tab-container qustion-list list-container">
					<h3 style="border-bottom:2px solid #49A804; padding-bottom:5px;">
						<#if data.CHNL_ID == root_channel_id>
							所有主题
						<#else>
							${data.CHNL_NAME}主题
						</#if>
					</h3>
					<!---------------问答---开始---------------------->
					<!--
					<ul class="tab-title line f-14">
			          <li class="current grid">新出题</li>
			        </ul>
			        -->
					<!---------------问答---结束---------------------->
					<!----------------添加一个关键字筛选和tab切换--------开始-------------------->
					<div class="tab-header line">
			          <div class="opt-sort"> 
			          	<!---不能点击sort-default---可以点击sort-ansnum------------->
			          	<a href="/cms/SY_COMM_CMS_CHNL/${data.CHNL_ID}/index_1.html" id="all_question" class="">全部 </a>
			          	<span id="pipe-after" class="sort-pipe" style="visibility: visible">|</span>
			          	<a href="/cms/SY_COMM_CMS_CHNL/${data.CHNL_ID}/index_1.html?noAnswer=noAnswer" id="noAnswer_question" class="">零回复&nbsp;</a> 
			          	<a rel="nofollow" href="/cms/SY_COMM_CMS_CHNL/${data.CHNL_ID}/index_${page}.html?noAnswer=${noAnswer!''}" class="btn-refresh"></a> 
			          </div>
			          <!---------------问题筛选--先隐藏掉------开始----------------->
			          <!--
			          <div id="keyword-search-box" class="searchbox-list">
			            <form action="/browse" name="" style="float:left">
			              <div class="keyword-field-wrapper">
			                <input placeholder="按关键字筛选" class="txt-keyword" name="word" id="" type="text">
			                <a class="keyword-cancelbutton" href="#"></a> </div>
			              <button type="submit" class="btn-search" alog-alias="br-tags-search"><b><span>筛选</span></b></button>
			              <input name="lm" value="8960" type="hidden">
			              <input name="pn" value="0" type="hidden">
			              <input name="cid" value="74" type="hidden">
			            </form>
			          </div>
			          -->
			          <!---------------问题筛选--先隐藏掉------结束----------------->
			        </div>
					<!----------------添加一个关键字筛选和tab切换--------开始-------------------->
				
				<@zhidao_ask_list debugName="栏目下出题列表" chnlId="${data.CHNL_ID}" noAnswer="${noAnswer!''}" page="${page}" count="20">
					<div class="tab-panels">
						<div id="tab-panel">
							<div class="list-panel">
								<table class="table-list" border="0" cellspacing="0"
									width="100%">
									<thead>
										<tr>
											<td style="padding: 0; line-height: 0; font-size: 0"
												height="0" width="440">&nbsp;</td>
											<td style="padding: 0; line-height: 0; font-size: 0"
												height="0" width="90">&nbsp;</td>
											<td style="padding: 0; line-height: 0; font-size: 0"
												height="0" width="70">&nbsp;</td>
											<td style="padding: 0; line-height: 0; font-size: 0"
												height="0" width="70"></td>
										</tr>
									</thead>
									<tbody id="js-ques-list-tbody">
										<#if tag_list?size==0>
										<tr show="no" alog-action="br-quick-btn">
											<h3 style="text-align:center;">暂无此类出题！</h3>
										</tr>
										</#if>
										<#list tag_list as ask>

										<tr class="quick-tr" show="no" alog-action="br-quick-btn">
											<td class="align-l"
												id="a-${ask.Q_ID}" cid="872">
												<a class="title" target="_self" href="javascript:view('${ask.Q_ID}','${ask.Q_TITLE}');"> ${ask.Q_TITLE} </a>
											</td>
											<td style="position:relative;">
											<#if ask.Q_ANSWER_COUNTER == 0>
												
											<#else>
												<!--<a id="a2-${ask.Q_ID}" qId="${ask.Q_ID}" class="q js-best-answer" href="javascript:void(0);" style="display:none;"  param="0">精选答案</a>-->
											</#if>
											</td>
											<td class="quick-num">${ask.Q_ANSWER_COUNTER!0}回复</td>
											<td class="quick-time timeago">${ask.S_MTIME}</td>
										</tr>
										<tr id="tr-${ask.Q_ID}" style="display:none;">
											
										</tr>
										
										

										</#list>
									</tbody>
								</table>
								<#if tag_list?size == 20>
								<div>
									<a id="zd-load-more" class="zd-btn-white zd-btn-more">
						            	<!--<i class="spinner-gray"></i>正在加载-->
						            	加载更多
						            </a>
								</div>
								</#if>
							</div>
						</div>
					</div>
				</@zhidao_ask_list>					
					
				</div>
			</div>
			<div class="l-aside-col " style="border:0px;width:250px;">
				<!-----------------------我的关注领域----------开始------------------->
				<div class="box-rod carefield" id="carefield-panel">
				  <div class="rod" style="padding:0px;">
				    <h2 class="right-title-h2-css">
				    	<span style="height:39px;background-color:#ffffff;line-height:39px;text-align:center;display:inline-block;border-right:rgb(214,214,214) 1px solid;width:100px;">我的关注领域</span>
				    	<span style="height:39px;line-height:39px;float:right;display:inline-block;"><a alog-alias="#" style="font-size:12px;color:#999999;" href="#" id="setCategory">设置</a><span class="red-more-css"></span></span>
				    </h2>
				    
				    <div class="clearfix care-class" id="my-carefield-class" style="padding:10px;">
				      <table style="overflow:normal" class="may-careclass" border="0" cellpadding="0" cellspacing="0" width="100%">
				        <tbody id="category_span">
				          <tr class="table-head">
				            <td width="60%"></td>
				            <td align="left">
				            	<!--<a alog-alias="#" style="float:right;" href="#" id="setCategory">设置</a>-->
				            </td>
				          </tr>
			          		<!-----------向里面追加关注分类内容---开始----------------->
				         	<!-----------向里面追加关注分类内容---结束----------------->
				        </tbody>
				      </table>
				    </div>
				  </div>
				</div>
				
				
			  <!-------------------分类的设置JS----------开始------------------->
			  <script>
			  	jQuery("#setCategory").unbind("click").bind("click",function(event){
					//1.构造树形选择参数
					var configStr = "SY_COMM_ZHIDAO_CHNL_MANAGE,{'TYPE':'multi'}";
					var options = {
						"config" :configStr,
						"replaceCallBack":function(idArray,nameArray){//回调，idArray为选中记录的相应字段的数组集合
							dictCallBack(idArray,nameArray);
						}
					};
					//2.显示树形
					var dictView = new parent.rh.vi.rhDictTreeView(options);
					dictView.show(event);	
					
					//构造回写的值
					var nodes = [];
					var ids = jQuery("input[name='categoryID']");
					var names = jQuery("input[name='categoryName']");
					if(ids.length > 0){
						for(var category_i=0; category_i<ids.length; category_i++){
							var node = {};
							node["ID"] = jQuery(ids[category_i]).val();
							node["NAME"] = jQuery(names[category_i]).val();
							nodes.push(node);
						}
						dictView.setRightSelect(nodes);
					}
					
					//设置样式
					var box = parent.jQuery("#SY_COMM_ZHIDAO_CHNL_MANAGE-dictDialog").parent();
					box.css({"z-index":"9999","background":"white","top":"20%","left":"20%","position":"absolute"});
					jQuery(box).find(".ui-widget-content").removeClass("ui-widget-content");					
				});
				
				
				//回调函数
				var dictCallBack = function(idArray,nameArray){
					var idsStr = "'"+idArray+"'";
					var cate_ids = idsStr.split(",");
					if(cate_ids.length > 5){
						alert("最多关注5个分类，请精选！");
						return;
					}else{
						//批量删除原数据
						var old_obj = jQuery("input[name='categoryID']");
						var old_ids = [];
						for(var i=0; i<old_obj.length; i++){
							old_ids[i] = jQuery(old_obj[i]).val();
						}
						var old_dataIds = old_ids.join(",");
						if(old_dataIds.length > 0){
							var old_param = {"DATA_IDS":old_dataIds};
							parent.FireFly.doAct("SY_COMM_ZHIDAO","deleteBatchCategoryFollow",old_param,false);
						}
						
						//批量增加新数据
						var new_dataIds = idArray.toString();
						if(new_dataIds.length > 0){
							var new_param = {"DATA_IDS":new_dataIds};
							parent.FireFly.doAct("SY_COMM_ZHIDAO","addBatchCategoryFollow",new_param,false);
						}
						
						//动态加载显示新数据
						printCategory();
					}
				};
			  </script>
			  <!-------------------分类的设置JS----------结束------------------->
	          <!------------------获取分类关注的JS-------开始------------------------>
	          <script>
					jQuery(document).ready(function(){

						printCategory();
						
						//为"关注"按钮绑定事件
						jQuery("#btn_add_care").click(
							function(){
								if(jQuery(this).attr("class") == 'btn_add_care'){
									//如果是未关注
									
									//如果关注数量大于5
									if(jQuery("input[name='categoryID']").length >= 5){
										alert("最多关注5个分类，请精选！");
										return;
									}
									jQuery(this).removeClass("btn_add_care").addClass("btn_have_cared");
									jQuery("#category_span").append(
										"<tr class='mark_remove' alog-group='br-care-cate'>"+
											"<td class='isCur'><span>"+jQuery(this).attr("chnl_name")+"</span></td>"+
											"<td align='left'>599266</td>"+
										"</tr>"+
										"<input type='hidden' name='categoryID' value='"+jQuery(this).attr("chnl_id")+"' />"+
										"<input type='hidden' name='categoryName' value='"+jQuery(this).attr("chnl_name")+"' />"
									);
									var param  = {};
									param["DATA_ID"] = jQuery(this).attr("chnl_id");
									FireFly.doAct("SY_COMM_ZHIDAO","addCategoryFollow",param,true);
								}else{
									//如果是以关注
									jQuery(this).removeClass("btn_have_cared").addClass("btn_add_care");
									var tr = jQuery("#category_span .isCur").parent();
									var input_id = jQuery(tr).next();
									var input_name = jQuery(input_id).next();
									jQuery(tr).remove();
									jQuery(input_id).remove();
									jQuery(input_name).remove();
									
									var param  = {};
									param["DATA_ID"] = jQuery(this).attr("chnl_id");
									FireFly.doAct("SY_COMM_ZHIDAO","deleteCategoryFollow",param,true);
								}
							});
						
					});	
					
					var printCategory = function(){
						//页面加载后就执行，从后台读取分类关注信息
						var param = {};
						param["_searchWhere"] = " and USER_CODE='@USER_CODE@'";
						var result = FireFly.doAct("SY_COMM_ZHIDAO_CATEGORY_FOLLOW","query",param,true);
						var datas = jQuery(result["_DATA_"]);
						
						//删除以前的元素
						jQuery(".mark_remove").remove();
						jQuery("input[name='categoryID']").remove();
					    jQuery("input[name='categoryName']").remove();
						
						jQuery(datas).each(function(index,category){
							
							if(category["DATA_ID"]=="" || category["DATA_ID"] == null){
								return;
							}
							
							if(jQuery("#btn_add_care").attr("chnl_id") == category["DATA_ID"]){
								jQuery("#btn_add_care").removeClass("btn_add_care").addClass("btn_have_cared");
								jQuery("#category_span").append(
									"<tr class='mark_remove' alog-group='br-care-cate'>"+
										"<td class='isCur'><span>"+category["DATA_DIS_NAME"]+"</span></td>"+
										"<td align='left'>599266</td>"+
									"</tr>"+
									"<input type='hidden' name='categoryID' value='"+category["DATA_ID"]+"' />"+
									"<input type='hidden' name='categoryName' value='"+category["DATA_DIS_NAME"]+"' />"
								);
							}else{
								jQuery("#category_span").append(
									"<tr class='mark_remove' alog-group='br-care-cate'><td style='padding-left:2px;'><a href=/cms/SY_COMM_CMS_CHNL/"+category["DATA_ID"]+"/index_1.html>"+category["DATA_DIS_NAME"]+"</a></td></tr>"+
									"<input type='hidden' name='categoryID' value='"+category["DATA_ID"]+"' />"+
									"<input type='hidden' name='categoryName' value='"+category["DATA_DIS_NAME"]+"' />"
								);
							}
						});
						
						if(jQuery(".isCur").length == 0){
							jQuery("#btn_add_care").removeClass("btn_have_cared").addClass("btn_add_care");
						}
					};			          
	          </script>
	          <!------------------获取分类关注的JS-------结束------------------------>
	          	<!---------------界限div--------------------->
	          	<div style="width:auto;height:10px;background-color:white;"></div>
				<!-----------------------我的关注领域----------结束------------------->
				
				<div class="box-rod expert" id="expert-panel">
					<div class="rod" style="padding:0px;">
						<h2 class="right-title-h2-css">
					    	<span style="height:39px;background-color:#ffffff;line-height:39px;text-align:center;display:inline-block;width:100px;border-right:rgb(214,214,214) 1px solid;">推荐专家</span>
					    	<span style="height:39px;line-height:39px;float:right;display:inline-block;"><a alog-alias="#" style="font-size:12px;color:#999999;" href="<@tmplUrl specialist_tmpl_id />">更多</a><span class="red-more-css"></span></span>
				    	</h2>
						<@zhidao_specialist_subject debugName="推荐专家" channelId="${data.CHNL_ID}" count="3">
							<#list specia_list as specia>
							<#if specia_index == 0>
							<dl style="width:auto;height:5px;background-color:white;"></dl>
							<dl class="d-gride-hover" style="margin:0;overflow:hidden;padding:10px;">
							<#else>
							<dl style="width:auto;height:5px;background-image:url(/sy/comm/zhidao/baidu_style_files/line-x.png);"></dl>
							<dl class="d-gride-hover" style="margin:0;overflow:hidden;padding:10px;">
							</#if>
								<span style="width:90px; height:auto; display:block; float:left; position:relative;">
									<#if specia.SPEC_TYPE == 1>
										<!--<div class="leader-logo"></div>-->
										<div></div>
									<#elseif specia.SPEC_TYPE == 2>
										<!--<div class="dept-logo"></div>-->
										<div></div>
									<#elseif specia.SPEC_TYPE == 3>
										<!--<div class="busi-logo"></div>-->
										<div></div>
									<#else>
										<div></div>
									</#if>
									<a href="javascript:othersZhidao('${userZhidao_center_tmpl_id}','${specia.USER_CODE}')">
										<@formatUserImg specia.USER_CODE specia '' 'spec-img' '80px' '80px' />
								 	</a>
								</span>
								<span style="float:left; width:120px; height:auto;">
									<dt style="padding-top:0;padding-bottom:10px;">
										<a href="javascript:othersZhidao('${userZhidao_center_tmpl_id}','${specia.USER_CODE}')" alog-action="br-cate-expert" target="_self">
											<h4>${specia.USER_NAME}</h4>
										</a> 
										<span class="fr">${specia.chnl}</span>
									</dt>
									<dd>
										<#if specia.USER_POST == ''>
											
										<#else>
											${specia.USER_POST}
										</#if>
									</dd>
									<dd>
										${specia.DEPT_NAME}
									</dd>
								</span>
								<span style="width:100%; height:30px;display:block;float:left;">
									<#if (specia.SPEC_SUB?length > 30)>
										<p>${specia.SPEC_SUB?substring(0,30)}...</p>
									<#else>
										<p>${specia.SPEC_SUB}</p>
									</#if>
									<#if (specia.SPEC_DESC?length > 30)>
										<p>${specia.SPEC_DESC?substring(0,30)}...</p>
									<#else>
										<p>${specia.SPEC_DESC}</p>
									</#if>
								</span>
							</dl>
							</#list>
						</@zhidao_specialist_subject>
						<script type="text/javascript">
							F.use('/static/widget/browse/expert/expert.js');
						</script>
					</div>
				</div>
						<!---------------界限div--------------------->
	          			<div style="width:auto;height:10px;background-color:white;"></div>
				  		<!---------------把总积分排行移到这里----开始------------->
						<div class="mod rank tabs ui-tabs-aside user rank3" style="padding:0px;background-color:white;">
								<div class="hd topper">
									<ul class="out-ul">
										<li class="li-div" style="width:67px;">
											<span class="li-span">专家排行</span>
										</li>
										<li class="li-gap"></li>
										<li class="li-div show li-selected" num="1" style="width:59px;">
											<a>
												<span class="li-span">总积分</span>
											</a>
										</li>
										<li class="li-gap"></li>
										<li class="li-div show" num="2" style="width:59px;">
											<a>
												<span class="li-span">本月</span>
											</a>
										</li>
										<li class="li-gap"></li>
										<li class="li-div show" num="3" style="width:59px;">
											<a>
												<span class="li-span">本周</span>
											</a>
										</li>
									</ul>
								</div>
								<div class="bd">
									<ul class="tabContent clearfix">
										<li class="current show_1" id="content_1">
											<ul>
											<@zhidao_integralTopByChnl debugName="总积分排行" type="total" count="5" chnlId="${data.CHNL_ID}"> 
											<#list tag_list as integral>
											<li>
												<dl class="integralTop-dl-css">
													<dt class="rank-color">
														<div class="custom-left">
															<a href="javascript:othersZhidao('${userZhidao_center_tmpl_id}','${integral.USER.USER_CODE}')">
														        <!--<img src="/sy/theme/default/images/common/rh-lady-icon.png" data-original="<@setUserImg integral.USER/>" style="1px solid #e5e5e5" width="60px;" height="60px;">-->
														        <@formatUserImg integral.USER.USER_CODE integral.USER '' '' '60px' '60px' />
														     </a>
														</div>
													</dt>
													<dd>
														<div class="custom-right">
															<a href="javascript:othersZhidao('${userZhidao_center_tmpl_id}','${integral.USER.USER_CODE}')">
																${integral.USER_NAME}
															</a>
															<p style="font-size:12px;">积分：<strong style="color:red;">${integral.INTEGRAL_VALUE}</strong></p>
														</div>	 
													</dd>
												</dl>
											</li>
											</#list>
											</@zhidao_integralTopByChnl>
											</ul>
										</li>
										<li class="disabled show_2" id="content_2">
											<ul>
												<@zhidao_integralTopByChnl debugName="本月积分排行" type="month" count="5" chnlId="${data.CHNL_ID}">
												<#list tag_list as integral>
												<li>
												   <dl class="integralTop-dl-css">
														<dt class="rank-color">
															<div class="custom-left">
																	<a href="javascript:othersZhidao('${userZhidao_center_tmpl_id}','${integral.USER.USER_CODE}')">
																        <!--<img src="/sy/theme/default/images/common/rh-lady-icon.png" data-original="<@setUserImg integral.USER/>" style="1px solid #e5e5e5" width="60px;" height="60px;">-->
																    	<@formatUserImg integral.USER.USER_CODE integral.USER '' '' '60px' '60px' />
																    </a>
															</div>
														</dt>
														<dd>
															<div class="custom-right">
																	<a href="javascript:othersZhidao('${userZhidao_center_tmpl_id}','${integral.USER.USER_CODE}')">
																		${integral.USER.USER_NAME}
																	</a>
																	<p style="font-size:12px;">积分：<strong style="color:red;">${integral.SCORE}</strong></p>
																</div>	
														</dd>
													 </dl>
													</li>
												</#list>
												</@zhidao_integralTopByChnl>
												</ul>
										 </li>
										 <li class="disabled show_3" id="content_3">
											<ul>
												<@zhidao_integralTopByChnl debugName="本周积分排行" type="week" count="5" chnlId="${data.CHNL_ID}"> 
												<#list tag_list as integral>
												<li>
												   <dl class="integralTop-dl-css">
														<dt class="rank-color">
															<div class="custom-left">
																	<a href="javascript:othersZhidao('${userZhidao_center_tmpl_id}','${integral.USER.USER_CODE}')">
																        <!--<img src="/sy/theme/default/images/common/rh-lady-icon.png" data-original="<@setUserImg integral.USER/>" style="1px solid #e5e5e5" width="60px;" height="60px;">-->
																    	<@formatUserImg integral.USER.USER_CODE integral.USER '' '' '60px' '60px' />
																    </a>
																</div>
														</dt>
														<dd>
															<div class="custom-right">
																	<a href="javascript:othersZhidao('${userZhidao_center_tmpl_id}','${integral.USER.USER_CODE}')">
																		${integral.USER_NAME}
																	</a>
																	<p style="font-size:12px;">积分：<strong style="color:red;">${integral.SCORE}</strong></p>
																</div>	
														</dd>
													 </dl>
													</li>
												</#list>
												</@zhidao_integralTopByChnl>
												</ul>
											</li>
									</ul>
								</div>
							</div>
							<!---------------把总积分排行移到这里----结束------------->
			</div>
		</div>
	</div>
		<!---------------返回顶部的div------开始--------------->
		<div id="zd-goto-top" style="position:fixed;bottom:30px; right:20px; margin-top:5px;"></div>
		<!---------------返回顶部的div------结束--------------->

		<script type="text/javascript">
		PDC._load_js = function(b, c) {
			var a = document.createElement("script");
			a.setAttribute("type", "text/javascript");
			a.setAttribute("src", b);
			a.onload = a.onreadystatechange = function() {
				if (!this.readyState || this.readyState == "loaded"
						|| this.readyState == "complete") {
					a.onload = a.onreadystatechange = null;
					if (typeof c === "function") {
						c(b, true)
					}
				}
			};
			a.onerror = function(d) {
				if (typeof c === "function") {
					c(b, false)
				}
			};
			document.getElementsByTagName("head")[0].appendChild(a)
		};
		PDC._load_analyzer = function() {
			var c = this._opt.special_pages;
			var d = [ this._opt.sample ];
			for ( var b = 0; b < c.length; b++) {
				d.push(c[b]["sample"])
			}
			var a = Math.max.apply(null, d);
			if (PDC._is_sample(a) == false) {
				return
			}
			PDC._analyzer.loaded = true;
			PDC._load_js(PDC._analyzer.url, function() {
				var g = PDC._analyzer.callbacks;
				for ( var f = 0, e = g.length; f < e; f++) {
					g[f]()
				}
			})
		};
		PDC.send = function() {
			if (PDC._analyzer.loaded == true) {
				WPO_PDA.send()
			} else {
				PDC._analyzer.callbacks.push(function() {
					WPO_PDA.send()
				})
			}
		};
		PDC._is_sample = function(a) {
			if (!PDC._random) {
				PDC._random = Math.random()
			}
			return PDC._random <= a
		};
		(function() {
			if (document.attachEvent) {
				window.attachEvent("onload", function() {
					PDC.mark("lt")
				}, false)
			} else {
				window.addEventListener("load", function() {
					PDC.mark("let");
					PDC._load_analyzer()
				})
			}
		})();
	</script>
	
</div>
<#include "/SY_COMM_ZHIDAO/zhidao_footer.ftl">
</body>
<!--------------------给"全部/零回复"两个按钮绑定点击事件-------开始------------------------->
<script>
	jQuery(document).ready(function(){
		var noAnswer = "${noAnswer}";
		if(noAnswer == 'noAnswer'){
			jQuery("#all_question").addClass("sort-ansnum");
			jQuery("#noAnswer_question").addClass("sort-default");
		}else{
			jQuery("#all_question").addClass("sort-default");
			jQuery("#noAnswer_question").addClass("sort-ansnum");
		}
 
		//tab切换的图片加载事件
		jQuery(window).bind("scroll",function(){
			var scrollTop = window.pageYOffset  
			                || document.documentElement.scrollTop  
			                || document.body.scrollTop  
			                || 0;
			if(scrollTop >= 300){
				jQuery(".tabContent li img").each(function(index,img){
					if(jQuery(img).attr("src") == '/sy/theme/default/images/common/rh-lady-icon.png' && jQuery(img).attr("data-original") != '/sy/theme/default/images/common/rh-lady-icon.png'){
						jQuery(img).attr("src",jQuery(img).attr("data-original"));
					}
				});
			}
		});
		
		//tab切换的方法
		jQuery(".show").mouseover(function(){
			jQuery(".current").removeClass("current").addClass("disabled");
			jQuery(".li-selected").removeClass("li-selected");
			var num = jQuery(this).addClass("li-selected").attr("num");
			jQuery("#content_"+num).addClass("current");
		})
 
		jQuery(".timeago").each(function(index,temp){
			var temp_time = jQuery.trim(jQuery(this).html());
			var time = "";
			if(temp_time.length > 19){
				time = temp_time.substring(0,19);
			}else{
				time = temp_time;
			}
			var timeago =  jQuery.timeago(time);
			jQuery(this).html(timeago);
		});
});
</script>
<!------------前端控制时间显示格式------结束-------------->
<!---------------------控制外层滚动条-------开始------------------->
<!--<script type="text/javascript" src="/sy/comm/cms/js/iframeAutoHeight.js"></script>-->
<!---------------------控制外层滚动条-------结束------------------->
<script>
	//将当前iFrame中的Id设置成全局变量，因为在Tab中要用到它来定位高度
	var pid = $(window.top.document).find(".ui-tabs-selected").attr("pretabid");
	var windowName = $("#"+pid,window.top.document).children("iframe").attr("id");
	GLOBAL.setFrameId(windowName);
	jQuery(document).ready(function(){
		//Tab对象的方法，用来定位页面的高度
		//alert(GLOBAL.getFrameId());
		Tab.setFrameHei(10);
		Tab.setFrameHei();
	});
</script>
</html>
