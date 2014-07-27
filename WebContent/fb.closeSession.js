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
				},
				
				error : function(xhr,status,error) {
					
					alert(error+ " An error has occured ");
				}
			});

			$('#closeSession').click(
					function() {

						$.ajax({
							url : 'rest/items/' + fb.getQueryParam('itemID')
									+ '/sessions/current',
							type : 'DELETE',
							success : function(config) {
								  fb.createPopupWindow('Session Deleted');
									 fb.showPopup($('.popup'), 500, 2000);
								window.location.href = 'ItemCreation.html';
							},
							
							error : function(xhr,status,error) {
								
								alert(error+ " An error has occured ");
							}
						});
					});

		});