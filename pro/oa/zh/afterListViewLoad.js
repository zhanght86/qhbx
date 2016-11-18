//隐藏添加按钮
jQuery(document).bind("afterListViewLoad", function(event, _viewer) {
			if (_viewer) {
			var addBtn = _viewer.getBtn("add");
				if(addBtn){
				  addBtn.hide();
			  }
			}
		});