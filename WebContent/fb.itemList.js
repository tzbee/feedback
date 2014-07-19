$(document).ready(function() {
	var self = fb.item;

	self.init();
	self.updateItemListForRating();

	$('#update').click(function() {
		self.updateItemListForRating();
	});

	
	
	$("#itemList").click(function() {

		window.location.href = fb.host + '/Feedback/ItemCreation.html';

	});

	
});
