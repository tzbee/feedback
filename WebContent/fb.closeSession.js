$(document).ready(
		function() {

			$.ajax({
				url : 'rest/items/' + fb.getQueryParam('itemID')
						+ '/sessions/current/config',
				type : 'GET',
				success : function(config) {
					$('#startValue').val(config.scale.startValue);
					$('#endValue').val(config.scale.endValue);
					$('#interval').val(config.scale.interval);
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