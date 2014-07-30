/*
 * Create item on button click
 */
$(document).ready(function() {
	var self = fb.item;

	self.updateItemList();

	$('#go').click(function() {
		var data = $('#createItemForm').serialize();
		self.createItem(data, self.updateItemList);
		 
	});
});
