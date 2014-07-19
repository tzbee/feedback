$(document).ready(function() {
	//var self = fb.item;

	//self.init();
	//self.updateItemListForRating();

		$.getJSON(fb.host + '/Feedback/rest/items', function(data) {
			fb.item.updateItemElementToTableForRating($('#itemsToRate'), data);
		});
	

	
	
	$("#itemList").click(function() {

		window.location.href = fb.host + '/Feedback/ItemCreation.html';

	});

	
});
