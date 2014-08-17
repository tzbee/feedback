/**
 * 
 */

$('#registerAccountButton').click(
		function() {
			//testing
			alert("i am working");
			fb.account.createNewKey($('#mail').val(), $('#accountType').val(),
					$('#keyContainer'));
		});