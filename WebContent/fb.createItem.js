$(document).ready(function() {
	var self = fb.item;

	self.updateItemList();

	$('#go').click(function() {
		var data = $('#createItemForm').serialize();
		self.createItem(data, self.updateItemList);
		 fb.createPopupWindow('<span>Item Created!!</span>');
		 fb.showPopup($('.popup'), 500, 2000);
	});
});
