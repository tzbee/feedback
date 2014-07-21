/**
 * post scale info on button click
 */
$(document).ready(
		function() {

			$('#enableSession').click(
					function() {
						$.post('rest/items/' + fb.getQueryParam('itemID')
								+ '/sessions', $('#configScaleForm')
								.serialize(), function() {
							alert("Feedback Enabled!");

							window.location.href = 'ItemCreation.html';
						});

					});

		});
