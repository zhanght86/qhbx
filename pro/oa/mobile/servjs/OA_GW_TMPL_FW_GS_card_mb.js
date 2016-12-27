//@ sourceURL=OA_GW_TMPL_FW_GS_card_mb.js

// 公司发文

var mTCDiv = $(_viewer.getItem('GW_MAIN_TO').getContainer())
,	mTECDiv = $(_viewer.getItem('GW_MAIN_TO_EXT').getContainer())
,	cTCDiv = $(_viewer.getItem('GW_COPY_TO').getContainer())
,	cTECDiv = $(_viewer.getItem('GW_COPY_TO_EXT').getContainer());

var mTCField = mTCDiv.parent()
,	mTECField = mTECDiv.parent()
,	cTCField = cTCDiv.parent()
,	cTECField = cTECDiv.parent();

var mainToField = $("<div data-role='fieldcontain' class='ui-field-contain'></div>")
					.append(mTCField.html()).append("<label></label>").append(mTECDiv);
mTECField.after(mainToField);

var copyToField = $("<div data-role='fieldcontain' class='ui-field-contain'></div>")
					.append(cTCField.html()).append("<label></label>").append(cTECDiv);
cTECField.after(copyToField);

mTCField.hide();
mTECField.hide();
cTCField.hide();
cTECField.hide();
