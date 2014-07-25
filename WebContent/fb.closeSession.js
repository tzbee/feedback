$(document).ready(
		function() {

			$.ajax({
				url : 'rest/items/' + fb.getQueryParam('itemID')
						+ '/sessions/current/config',
				type : 'GET',
				success : function(config) {
					$('#startValue').html("Start Value :"+config.scale.startValue);
					$('#endValue').html("End Value: "+config.scale.endValue);
					$('#interval').html("Interval: "+config.scale.interval);
				}
			});

			$('#closeSession').click(
					function() {

						$.ajax({
							url : 'rest/items/' + fb.getQueryParam('itemID')
									+ '/sessions/current',
							type : 'DELETE',
							success : function(config) {
								alert("Session Deleted");
								window.location.href = 'ItemCreation.html';
							}
						});
					});

		});