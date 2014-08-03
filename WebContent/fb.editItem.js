/**
 * repopulate edit item form
 */
$(document).ready(function() {
	var self = fb.item;
	self.updateItemList();

	$.ajax({
		url : 'rest/items/' + fb.getQueryParam('itemID'),
		type : 'GET',
		success : function(item) {
			$('#itemName').val(item.name);
			$('#itemDescription').val(item.description);
		},
		error : function(xhr, status, error) {
			fb.notification(" An error has occured ", "error");
		}
	});

	$('#edit').click(function() {

		var jqxhr = $.post(

		'rest/items/' + fb.getQueryParam('itemID'),

		$('#editItemForm').serialize(),

		function() {

			window.location.href = 'ItemCreation.html';
		}).fail(function() {
			fb.notification("An error has occurred, check item name", "error");
		});

	});

});
