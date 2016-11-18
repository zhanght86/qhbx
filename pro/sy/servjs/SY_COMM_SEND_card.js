var _viewer = this;

var titleDiv = jQuery("<div></div>").css({"width":"100%","margin":"10px 0px"});
var table = jQuery("<table width='100%'></table>").appendTo(titleDiv);
var tr = jQuery("<tr></tr>").appendTo(table);
var td = jQuery("<td width='60%' align='left'><font style='color: #FF0000;font-family: 方正小标宋简体;font-size: 14px;margin-left:63px;'>操作说明：请先填写方案名称，保存，然后再按用户、部门、角色三种方式添加人员组里的人员。</font></td>").appendTo(tr);
_viewer.form.obj.find("fieldset").first().prepend(titleDiv);
