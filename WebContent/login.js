/**
 * Login script
 */

$(document).ready(
		function() {
			$('#login').click(
					function() {
						fb.account.login($('#userEmail').val(), $(
								'#userPassword').val(), function() {
							fb.notification('login successful', 'info');
						}, function() {
							fb.notification('login failure', 'error');
						});
					});
		});