/**
 * post scale info on button click
 */
$(document).ready(

function() {

	$('#enableSession').click(function() {
		var jqxhr = $.post(

		'rest/items/' + fb.getQueryParam('itemID') + '/sessions',

		$('#configScaleForm').serialize(),

		function() {

		})

		.done(function() {
			window.location.href = 'ItemCreation.html';

		})

		.fail(function() {
			fb.notification("error: check inputted values", "error");
		});

	});

});
