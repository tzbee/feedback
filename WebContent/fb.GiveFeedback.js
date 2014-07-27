/**
 * 
 */
$(document).ready(
		function() {

			$.ajax({
				url : 'rest/items/' + fb.getQueryParam('itemID')
						+ '/sessions/current/config',
				type : 'GET',
				success : function(config) {
					$.each(config.scale.scaleValues, function(i, scaleValue) {
						$('#scale').append(
								'<option value=' + scaleValue + '>'
										+ scaleValue + '</option>');
					});
				}

			});

			$('#rate').click(
					function() {
						$.post('rest/items/' + fb.getQueryParam('itemID')
								+ '/rate', $('#giveFeedbackForm').serialize(),
								function() {
							
									window.location.href = 'sessionInfoUI.html?itemID='+fb.getQueryParam('itemID')+'&sessionIndex=current';
								});

					});

		});
