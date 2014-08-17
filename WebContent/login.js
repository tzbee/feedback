/**
 * Login script
 */

$(document).ready(
		function() {
			$('#login').click(
					function() {
						fb.account.login($('#userEmail').val(), $(
								'#userPassword').val(), function() {
							window.location.replace('home');
						}, function() {
							fb.notification('login failure', 'error');
						});
					});
		});