$(document).ready(function() {
	// var self = fb.item;

	// self.init();
	// self.updateItemListForRating();

	$.getJSON('rest/items/ratable', function(data) {
		fb.item.updateItemElementToTableForRating($('#itemsToRate'), data);
	});

	$("#itemList").click(function() {

		window.location.href = 'ItemCreation.html';

	});

});
