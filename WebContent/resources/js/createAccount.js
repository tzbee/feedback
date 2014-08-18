/**
 * 
 */

$('#registerAccountButton').click(
		function() {
			fb.account.createNewKey($('#mail').val(), $('#accountType').val(),
					$('#keyContainer'));
		});