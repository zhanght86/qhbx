var _viewer = this;

$(window).load(function () {
	if (System.getVar('@ROLE_CODES@').indexOf(
			System.getVar('@C_INFOS_MANAGER_ROLE@',
					'INFOS_MANAGER_ROLE')) >= 0) {
		addCheckBox();
	}
});


/**
 * 追加Radio并绑定点击事件
 */
function addCheckBox () {
	var $tr = $($(document).find('.searchDiv tbody tr'));
	
	$tr.prepend('<td style="padding:0 10px;"><input name="fetchCheck" style="vertical-align:middle;" checked="checked" type="radio" value="zong" />公司本部</td>'+
				'<td style="padding:0 10px;"><input name="fetchCheck" style="vertical-align:middle;" type="radio" value="fen" />各分公司</td>'+
				'<td style="padding:0 10px;"><input name="fetchCheck" style="vertical-align:middle;"  type="radio" value="all" />全部</td>');
	
	_viewer.resetWhere = function(where) {
		// 判断checkbox是否选中：选中--只查看分公司，未选中--只查看总公司
		//	if(!!$('#fetchCheck').attr('checked')) 判断checkbox选中的方法
		var caseStr = $('input[name=fetchCheck]:checked').val();
		if (caseStr == 'all') {
			return where + 'and 1 = 1';
		}
		if (caseStr == 'zong') {
			return where + "and (S_ODEPT = '24' or IS_FETCH = '1')";
		}
		if (caseStr == 'fen') {
			return where + "and (S_ODEPT != '24' and IS_FETCH != '1')";
		}
	};
};