/*
 * Popoulate table with ratable items on page load
 */
$(document).ready(function() {

	$.getJSON('rest/items/ratable', function(data) {
		fb.item.updateItemElementToTableForRating($('#itemsToRate'), data);
	}).error(function() {
		alert("An error has occurred");
	});
	$("#itemList").click(function() {

		window.location.href = 'ItemCreation.html';

	});

});
