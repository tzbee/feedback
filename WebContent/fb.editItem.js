/**
 * repopulate edit item form
 */
$(document).ready(
		function() {
			var self = fb.item;
			self.init();
			self.updateItemList();

			$.ajax({
				url : 'rest/items/' + fb.getQueryParam('itemID'),
				type : 'GET',
				success : function(item) {
					$('#itemName').val(item.name);
					$('#itemDescription').val(item.description);
				}
			});

			$('#edit').click(
					function() {
						$.post('rest/items/' + fb.getQueryParam('itemID'), $(
								'#editItemForm').serialize(), function() {
							window.location.href = 'ItemCreation.html';
						});

					});

		});
