function layoutBiSize() {
	var themeLayoutViewFlag = false;
	if (typeof (theme_layoutMaxView) == "function"
			&& typeof (theme_layoutNormalView) == "function") {
		themeLayoutViewFlag = true;
	}

	var wrapper = document.getElementById('wrapper');
	var banner = document.getElementById('banner');
	var navigation = document.getElementById('navigation');
	var footer = document.getElementById('footer');

	var div = document.getElementById('bi_div');
	var normalButton = document.getElementById('bi_normal');
	var maxButton = document.getElementById('bi_max');

	if (banner.style.display == "none") {
		div.style.height = document.getElementById('bi_portlet_height').value;
		if (themeLayoutViewFlag) {
			theme_layoutNormalView();
		} else {
			banner.style.display = "block";
			navigation.style.display = "block";
			footer.style.display = "block";
			wrapper.style.width = document
					.getElementById('bi_theme_wrapper_width').value;
		}
		normalButton.style.display = "none";
		maxButton.style.display = "block";
	} else {
		var hiddenWrapper = document.getElementById('bi_theme_wrapper_width');
		if (hiddenWrapper.value == '') {
			hiddenWrapper.value = wrapper.style.width;
		}
		div.style.height = document.documentElement.clientHeight - 10;
		if (themeLayoutViewFlag) {
			theme_layoutMaxView();
		} else {
			banner.style.display = "none";
			navigation.style.display = "none";
			footer.style.display = "none";
			wrapper.style.width = "100%";
		}
		normalButton.style.display = "block";
		maxButton.style.display = "none";
	}
}

function styleTreeView() {
	var divider = document.getElementById('bi_treeViewTD');
	if (divider.style.display == "none") {
		divider.style.display = "block";
	} else {
		divider.style.display = "none";
	}
}

var lastSelectedTreeNode = null;

function mouseOverTreeNode(target) {
	if (target == lastSelectedTreeNode) {
		return;
	}
	// target.style.color = "#3333FF";
}

function mouseOutTreeNode(target) {
	if (target == lastSelectedTreeNode) {
		return;
	}
	// target.style.color = "#ffffff";
}

function styleTreeNode(target) {
	if (lastSelectedTreeNode != null) {
		// lastSelectedTreeNode.style.color = "#ffffff";
		// lastSelectedTreeNode.style.background = "transparent";
	}
	// target.style.color = "#3333FF";
	// target.style.background = "#CCCCCC";
	lastSelectedTreeNode = target;
}

var BiTree = {
			initialize : function(options) {
				var instance = this;
				instance.outputId = options.outputId;
				instance.icons = options.icons;

				instance.create();

				instance._attachEventDelegation();
			},

			create : function() {
				var instance = this;

				var outputEl = jQuery(instance.outputId);
				var mainLi = '';

				var tree = outputEl.find('> ul');
				var treeEl = tree[0];

				tree.prepend(mainLi);

				instance.tree = tree;

			},

			_attachEventDelegation : function() {
				var instance = this;
				var treeEl = instance.tree;

				treeEl.mouseup(function(event) {
					var target = event.target;
					var nodeName = target.nodeName || '';

					if (nodeName.toLowerCase() == 'input' && target.className) {
						if (target.className.indexOf('expand-image') != -1) {
							instance.toggle(target);
						}
					}
				});
			},

			toggle : function(obj) {
				var instance = this;

				if (obj.src.indexOf('spacer') < 0) {
					var icons = instance.icons;
					var treeId = instance.treeId;

					var openNode = false;

					var currentLi = obj.parentNode;

					var nodeId = jQuery(currentLi).attr('nodeId');

					var parentFolderId = jQuery(currentLi).attr('layoutId');

					var subBranch = jQuery('ul', currentLi).eq(0);

					var empty = subBranch.find("li").length == 0;

					currentLi.childrenDraggable = false;

					var branchInteraction = function() {
						if (subBranch.is(':hidden')) {
							subBranch.show();
							obj.src = icons.minus;
							openNode = true;

							if (!currentLi.childrenDraggable) {
								subBranch.addClass('node-open');
								// instance.setInteraction(currentLi);
								currentLi.childrenDraggable = true;
							}
						} else {
							subBranch.hide();
							obj.src = icons.plus;
						}
					};

					var sessionClick = function() {
						jQuery.ajax({
							url : themeDisplay.getPathMain()
									+ '/portal/tree_js_click',
							data : {
								nodeId : nodeId,
								openNode : openNode,
								treeId : treeId,
								portletResource : instance.portletResource,
								plid : instance.plid
							}
						});
					};

					branchInteraction();

					if (instance.url && empty) {
						var url = instance.url + '&nodeId=' + nodeId
								+ '&parentFolderId=' + parentFolderId;
						var loadingGif = themeDisplay.getPathContext()
								+ '/html/themes/classic/images/application/loading_indicator.gif';
						var pageImg = jQuery(currentLi).find('a input:first');
						var pageImgSrc = pageImg.attr('src');

						pageImg.attr('src', loadingGif);

						jQuery.ajax({
							cache : false,
							type : 'GET',
							url : url,
							success : function(html) {
								subBranch.html(html);
								branchInteraction();
								pageImg.attr('src', pageImgSrc);
								sessionClick();
							}
						});
					} else {
						//sessionClick();
					}
				}
			},

			_onUpdate : function(ui) {
				var instance = this;

				var currentNode = ui.item[0];
				var parent = currentNode.parentNode;
				var parentNotUpdated = (instance._originalParentNode
						&& instance._originalParentNode != parent && !instance._wasDropped);

				var currentId = currentNode.getAttribute('branchId');
				var parentId = parent.parentNode.getAttribute('branchId') || '';

				if (parentNotUpdated) {
					instance._saveParentNode({
						plid : currentId,
						parentPlid : parentId
					});
				}

				var index = jQuery('> li', parent).index(currentNode);

				var data = {
					cmd : 'priority',
					plid : currentId,
					priority : index
				};

				jQuery.ajax({
					data : data,
					url : instance._updateURL
				});

				instance._originalParentNode = null;
				instance._wasDropped = false;
			},

			_saveParentNode : function(options) {
				var instance = this;

				jQuery.ajax({
					url : themeDisplay.getPathMain()
							+ '/layout_management/update_page',
					data : {
						cmd : 'parent_layout_id',
						parentPlid : options.parentPlid,
						plid : options.plid
					}
				});
			}
		};