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
					alert("test");
					$.each(config.scaleValues, function(i, scaleValues) {
						$('#scale').append(scaleValues);
					});
				}

			});
		});
