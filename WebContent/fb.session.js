fb.session = {};
fb.session.ajax = {};

fb.session.ajax.updateCurrentSessionData = function(itemID) {
	$.getJSON(fb.host + '/Feedback/rest/items/' + itemID + '/sessions/current',
			function(fbs) {
				var fbUnits = fbs.feedbackUnits;

				$.each(fbUnits, function(index, fbu) {
					$('#fbu').append(fbu.value + '<br/>');
				});
			});
};

$(document).ready(function() {
	ajax = fb.session.ajax;
	ajax.updateCurrentSessionData(1);
});
