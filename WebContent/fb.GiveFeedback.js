/**
 * repopulate edit item form
 */
$(document).ready(
		function() {
			var self = fb.item;
			self.init();
			self.updateItemList();

			$.ajax({
				url : fb.host + '/Feedback/rest/items/'
						+ fb.getQueryParam('itemID')+'/sessions',
				type : 'GET',
				success : function(item) {
					$('#feedbackscale').val(item.name);
					$('#itemDescription').val(item.description);
				}
			});
		
			$('#edit').click(
					function() {
						$.post(fb.host + '/Feedback/rest/items/'
								+ fb.getQueryParam('itemID'),
								$('#editItemForm').serialize(), function() {
									window.location.href = fb.host
											+ '/Feedback/ItemCreation.html';
								});

					});
			
			
			
		});
