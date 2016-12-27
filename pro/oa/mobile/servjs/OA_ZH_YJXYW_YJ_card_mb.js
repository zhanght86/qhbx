//@ sourceURL=OA_ZH_YJXYW_YJ_card_mb.js

// 意健险业务签报审批单
var _viewer = this;

var gwYearCode = _viewer.itemValue('GW_YEAR_CODE'), 	// 文件编号
    gwYear = _viewer.itemValue('GW_YEAR'),				// 流水号
    gwYearNumber = _viewer.itemValue('GW_YEAR_NUMBER'); // 年号

_viewer.getItem('GW_YEAR_CODE').setValue(gwYearCode + ' -（' + gwYear + '）-' + gwYearNumber + ' 号');

