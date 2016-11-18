<!--平台级的jsp文件-->
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<!--StdListView.jsp列表页面-->
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>


<head>
<meta http-equiv="content-type" content="text/html; charset=UTF-8"> 
        <meta charset="utf-8"> 
        <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1"> 
        <meta name="description" content="使用HTML5 canvas涂鸦。"> 
        <title>手写板</title> 
        <link rel="stylesheet" href="./bootstrap-1.css">
        <link href="./reset.css" rel="stylesheet" media="screen"> 
        <link href="./tuya.css" rel="stylesheet" media="screen">
        <script src="./jquery-1.js"></script>
        <script src="./detectHTML5.js"></script>
        <script type="text/javascript" src="./FeatureLoader.js"></script>     
        <div style="position: relative; width: 780px; left: 530px; top: -3px; display: block;" id="options"> 
            <a id="save-button" class="switch" title="Save &amp; share" href="#" name="save-button">保存</a>
           <!--
            <a id="post_button" class="switch" title="Save &amp; share" href="#" name="post_button">保存并关闭</a>
            -->
             
            <a id="undo-button" class="switch" title="Undo" href="#" name="undo-button">撤销</a> 
            <a id="reset-button" class="switch" title="Reset" href="#" name="reset-button">重置</a> 
            
            <a id="dash-toggle" class="switch" title="Dash" href="#" name="dash-toggle">虚线: <span>OFF</span></a> 
			
            <div id="vibration-dropdown" class="dropdown"> 
                <div id="vibration-dropdown-title" class="dropdown-title"><span>抖动:1</span></div> 
                <div id="vibration-dropdown-list" class="dropdown-list"> 
                    <ul> 
                        <li><a href="#" data-value="0">0</a></li> 
                        <li><a href="#" data-value="1">1</a></li> 
                        <li><a href="#" data-value="2">2</a></li> 
                        <li><a href="#" data-value="3">3</a></li> 
                        <li><a href="#" data-value="5">5</a></li> 
                        <li><a href="#" data-value="10">10</a></li> 
                        <li><a href="#" data-value="20">20</a></li> 
                    </ul> 
                </div> 
            </div> 

            <div id="size-dropdown" class="dropdown"> 
                <div id="size-dropdown-title" class="dropdown-title"><span>线条大小:3</span></div> 
                <div id="size-dropdown-list" class="dropdown-list"> 
                    <ul> 
                        <li><a href="#" data-value="0.5">0.5 px</a></li> 
                        <li><a href="#" data-value="1">1 px</a></li> 
                        <li><a href="#" data-value="2">2 px</a></li> 
                        <li><a href="#" data-value="3">3 px</a></li> 
                        <li><a href="#" data-value="4">4 px</a></li> 
                        <li><a href="#" data-value="5">5 px</a></li> 
                        <li><a href="#" data-value="6">6 px</a></li> 
                        <li><a href="#" data-value="7">7 px</a></li> 
                        <li><a href="#" data-value="8">8 px</a></li> 
                        <li><a href="#" data-value="9">9 px</a></li> 
                    </ul> 
                </div> 
            </div> 

            <div id="color-dropdown" class="dropdown"> 
                <div id="color-dropdown-title" class="dropdown-title"><span>颜色:黑色</span></div> 
                <div id="color-dropdown-list" class="dropdown-list"> 
                    <ul> 
                        <li style="background-color: #000000"><a href="#" data-value="#000000">黑色</a></li>
                        <li style="background-color: #FFFFFF"><a href="#" data-value="#FFFFFF">白色</a></li>
                        <li style="background-color: #7F7F7F"><a href="#" data-value="#7F7F7F">灰色-50%</a></li> 
                        <li style="background-color: #880150"><a href="#" data-value="#880150">深褐色</a></li> 
                        <li style="background-color: #ED1C24"><a href="#" data-value="#ED1C24">红色</a></li>
                        <li style="background-color: #FF7F27"><a href="#" data-value="#FF7F27">橙色</a></li> 
                        <li style="background-color: #FFF200"><a href="#" data-value="#FFF200">黄色</a></li> 
                        <li style="background-color: #22B14C"><a href="#" data-value="#22B14C">绿色</a></li> 
                        <li style="background-color: #00A2E8"><a href="#" data-value="#00A2E8">青绿</a></li> 
                        <li style="background-color: #3F48CC"><a href="#" data-value="#3F48CC">靛色</a></li> 
                        <li style="background-color: #A349A4"><a href="#" data-value="#A349A4">紫色</a></li> 
                        <li style="background-color: #C3C3C3"><a href="#" data-value="#C3C3C3">灰色-25%</a></li> 
                        <li style="background-color: #B97A57"><a href="#" data-value="#B97A57">褐色</a></li>
                        <li style="background-color: #FFAEC9"><a href="#" data-value="#FFAEC9">玫瑰色</a></li> 
                        <li style="background-color: #FFC90E"><a href="#" data-value="#FFC90E">金色</a></li>
                        <li style="background-color: #EFE4B0"><a href="#" data-value="#EFE4B0">浅黄色</a></li>
                        <li style="background-color: #B5E61D"><a href="#" data-value="#B5E61D">酸橙色</a></li>
                        <li style="background-color: #99D9EA"><a href="#" data-value="#99D9EA">淡青绿色</a></li>
                        <li style="background-color: #7092BE"><a href="#" data-value="#7092BE">蓝灰色</a></li>
                        <li style="background-color: #C8BFE7"><a href="#" data-value="#C8BFE7">淡紫色</a></li>
                    </ul> 
                </div> 
            </div> 

            <div id="alpha-dropdown" class="dropdown"> 
                <div id="alpha-dropdown-title" class="dropdown-title"><span>透明度:0.9</span></div> 
                <div id="alpha-dropdown-list" class="dropdown-list"> 
                    <ul> 
                        <li><a href="#" data-value="0.0">0.0</a></li> 
                        <li><a href="#" data-value="0.1">0.1</a></li> 
                        <li><a href="#" data-value="0.2">0.2</a></li> 
                        <li><a href="#" data-value="0.3">0.3</a></li> 
                        <li><a href="#" data-value="0.4">0.4</a></li> 
                        <li><a href="#" data-value="0.5">0.5</a></li> 
                        <li><a href="#" data-value="0.6">0.6</a></li> 
                        <li><a href="#" data-value="0.7">0.7</a></li> 
                        <li><a href="#" data-value="0.8">0.8</a></li> 
                        <li><a href="#" data-value="0.9">0.9</a></li> 
                        <li><a href="#" data-value="1.0">1.0</a></li> 
                    </ul> 
                </div> 
            </div> 
            <a id="gallery-link" class="link" href="javascript:window.close();" title="关闭">关闭</a>
        </div> 

        <canvas style="position: relative; left: 250px; top: 13px;" height="230" width="780" id="world"> 
            <div class="alert-message block-message error"><a href="#" class="close">×</a><p class="noCanvas">您的浏览器不支持html5 canvas，不能使用涂鸦功能，请下载一个更加先进的浏览器。<br>如<a href="http://www.google.com/chrome">Chrome</a>、<a href="http://www.firefox.com.cn/download/">Firefox</a>、<a href="http://www.opera.com/download/">Opera</a>、<a href="http://www.apple.com.cn/safari/">Safari</a>，推荐使用Chrome(谷歌浏览器)。</p></div>
        </canvas> 

        <div class="modal" id="save_box">
            <div class="modal-header">
                <h3>保存并发布</h3>
                <a class="close" href="javascript:void(0);" id="x_button">×</a>
            </div>
            <div class="modal-body">
                <form class="form-stacked">
                    <fieldset>
                        <div class="clearfix" id="title_div">
                            <label for="xlInput4">涂鸦标题</label>
                            <div class="input">
                                <input size="30" id="tuya_title" class="xlarge error" value="test" type="text">
                                <span style="display: none;" class="help-inline" id="title_help">请输入标题</span>
                            </div>
                        </div>
                    </fieldset>
                    <div class="actions">
                        <button class="btn primary" id="post_button">保存并发布</button>&nbsp;<button class="btn" id="cancel_button">取消</button>
                    </div>
                </form>
            </div>
        </div>
        <script>detectHTML5();</script>
        <script src="./dropdown.js"></script> 
        <script src="./sketch.js"></script> 
        <script src="./sketch_002.js"></script> 
        <script type="text/javascript">
            XN_RequireFeatures(["Connect","CanvasUtil"], function() {
	            XN.Main.init("d6b7a80f51694334b0092177fb92b4fe", "/renrenapp/xdreceiver");
	        	XN.CanvasClient.setCanvasHeight("600px");
            });
        </script>
    
    </body>
    
