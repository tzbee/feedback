$(document).ready(function() {
	fb.init();
	fb.updateItemList();

	$('#go').click(function() {
		var data = $('#createItemForm').serialize();
		fb.createItem(data, fb.updateItemList);
	});

	$('#delete').click(function() {
		var itemID = fb.itemToDelete.value;
		fb.deleteItem(itemID, fb.updateItemList);
	});

	$('#newFBSButton').click(function() {
		var itemID = fb.fbsItem.value;
		fb.createFeedbackSession(itemID);
	});
});
