/**
 * post scale info on button click
 */
$(document).ready(
		function() {
			//var self = fb.item;
			//self.init();
			//self.updateItemList();
			//var self = fb.session.ajax;
			
			$('#enableSession').click(
					function() {
						$.post(fb.host + '/Feedback/rest/items/'
								+ fb.getQueryParam('itemID')+'/sessions',
								$('#configScaleForm').serialize(), function() {
									alert("Feedback Enabled!");
									
									
									window.location.href = fb.host
											+ '/Feedback/ItemCreation.html';
								});

					});
			
			
		});
