
var _viewer = this;
// 构建审批单头部
var gwHeader = new rh.vi.gwHeader({
	"servId" : _viewer.opts.sId,
	"parHandler" : _viewer
});
gwHeader.init();

// 如果存在主键，则可以构建文件编号框
if (_viewer._pkCode) {

	var gwExtCard = new rh.vi.gwExtCardView({
		"parHandler" : _viewer
	});
	gwExtCard.init();
}
