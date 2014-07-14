fb.session = {};
fb.session.ajax = {};

fb.session.ajax.updateCurrentSessionData = function(itemID) {
	$.getJSON(fb.host + '/Feedback/rest/items/' + itemID + '/sessions/current',
			function(fbs) {
				var fbUnits = fbs.feedbackUnits;

				$.each(fbUnits, function(index, fbu) {
					$('#fbu').append(
							'Feedback unit (value: ' + fbu.value
									+ ', created at: ' + fbu.formattedCreatedAt
									+ ')<br/>');
				});
			});
};

$(document).ready(function() {
	var itemID = fb.getQueryParam('itemID');

	fb.session.ajax.updateCurrentSessionData(parseInt(itemID));
});
