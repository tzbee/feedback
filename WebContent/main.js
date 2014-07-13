$(document).ready(function() {
	ItemElements.init();
	ItemElements.updateItemList();

	$('#go').click(function() {

		var data = $('#createItemForm').serialize();

		ItemElements.createItem(data, ItemElements.updateItemList);
	});

	$('#delete').click(function() {
		var itemID = ItemElements.itemToDelete.value;
		ItemElements.deleteItem(itemID, ItemElements.updateItemList);
	});

	$('#newFBSButton').click(ItemElements.createFeedbackSession);
});
