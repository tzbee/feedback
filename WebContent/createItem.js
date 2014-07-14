$(document).ready(function() {
	var self = fb.item;

	self.init();
	self.updateItemList();

	$('#go').click(function() {
		var data = $('#createItemForm').serialize();
		self.createItem(data, self.updateItemList);
	});

	$('#delete').click(function() {
		var itemID = self.itemToDelete.value;
		self.deleteItem(itemID, self.updateItemList);
	});

	$('#newFBSButton').click(function() {
		var itemID = self.fbsItem.value;
		self.createFeedbackSession(itemID);
	});
});
