$(document).ready(function() {
	ItemElements.init();
	ItemElements.updateItemList();

	$('#go').click(function() {

		var data = {
			itemName : ItemElements.itemName.value,
			itemDescription : ItemElements.itemDescription.value,
		};

		ItemElements.createItem(data, ItemElements.updateItemList);
	});

	$('#delete').click(function() {
		var itemID = ItemElements.itemToDelete.value;
		ItemElements.deleteItem(itemID, ItemElements.updateItemList);
	});

	$('#newFBSButton').click(ItemElements.createFeedbackSession);
});
