/**
 * 
 */
$(document)
		.ready(
				function() {

					$.ajax({
						url : 'rest/items/' + fb.getQueryParam('itemID')
								+ '/sessions/current/config',
						type : 'GET',
						success : function(config) {
							$.each(config.scale.scaleValues, function(i,
									scaleValue) {
								$('#scale').append(
										'<option value=' + scaleValue + '>'
												+ scaleValue + '</option>');
							});
						},
						error : function(xhr, status, error) {
							fb.notification(" An error has occured ", "error");
						}

					});

					$('#rate')
							.click(
									function() {
										var jqxhr = $
												.post(

														'rest/items/'
																+ fb
																		.getQueryParam('itemID')
																+ '/rate',

														$('#giveFeedbackForm')
																.serialize(),

														function() {

														})

												.done(
														function() {

															window.location.href = 'sessionInfoUI.html?itemID='
																	+ fb
																			.getQueryParam('itemID')
																	+ '&sessionIndex=current';
														})

												.fail(
														function() {
															fb
																	.notification(
																			"An error has occurred",
																			"error");
														});

									});

				});
