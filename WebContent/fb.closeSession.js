$(document).ready(
		function() {

			/*
			 * Get session configuration and display information
			 */

			$.ajax({
				url : 'rest/items/' + fb.getQueryParam('itemID')
						+ '/sessions/current/config',
				type : 'GET',
				success : function(config) {
					$('#startValue').html(
							"Start Value :" + config.scale.startValue);
					$('#endValue').html("End Value: " + config.scale.endValue);
					$('#interval').html("Interval: " + config.scale.interval);
				},

				error : function(xhr, status, error) {
					fb.notification(" An error has occured ", "error");
				}
			});

			$('#closeSession').click(
					function() {

						$.ajax({
							url : 'rest/items/' + fb.getQueryParam('itemID')
									+ '/sessions/current',
							type : 'DELETE',
							success : function(config) {

								window.location = 'ItemCreation.html', 2010;
							},

							error : function(xhr, status, error) {
								fb.notification(" An error has occured ",
										"error");
							}
						});
					});

		});