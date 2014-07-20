/**
 * 
 */
$(document).ready(
		function() {

			$.ajax({
				url : fb.host + '/Feedback/rest/items/'
						+ fb.getQueryParam('itemID')
						+ '/sessions/current/config',
				type : 'GET',
				success : function(config) {
					$.each(config.scale.scaleValues, function(i, scaleValue) {
						$('#scale').append(
								'<option value=' + i + '>' + scaleValue
										+ '</option>');
					});
				}

			});
			
			$('#rate').click(
					function() {
						$.post(fb.host + '/Feedback/rest/items/'
								+ fb.getQueryParam('itemID')+'/rate',
								$('#giveFeedbackForm').serialize(), function() {	
								alert("Feedback given");
									window.location.href = fb.host
											+ '/Feedback/itemList.html';
								});

					});
			
});
