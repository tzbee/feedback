/**
 * post scale info on button click
 */
$(document).ready(
		
		function() {

			$('#enableSession').click(
					function() {
						var jqxhr = $.post(

								'rest/items/' + fb.getQueryParam('itemID')
								        + '/sessions', 

								$('#configScaleForm')
								        .serialize(),

								 function() {
									fb.createPopupWindow('Feedback Enabled');
									 fb.showPopup($('.popup'), 500, 2000);
									
								})

								  .done(function() {
									  
									  window.location.href = 'ItemCreation.html';
								  })

								  .fail(function() {
								    alert( "error: Check Inputted values" );
								  })

								  .always(function() {

								});
						

					});

		});
