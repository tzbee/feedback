fb.session = {};
fb.session.ajax = {};

fb.session.ajax.updateCurrentSessionData = function(itemID, dataView) {
	$.getJSON(fb.host + '/Feedback/rest/items/' + itemID + '/sessions/current',
			function(fbs) {
				dataView($('#dataView'), fbs);
			});
};

fb.session.ajax.rawDataView = function(viewElement, fbs) {
	var fbUnits = fbs.feedbackUnits;
	$.each(fbUnits, function(index, fbu) {
		viewElement.append('Feedback unit (value: ' + fbu.value
				+ ', created at: ' + fbu.formattedCreatedAt + ')<br/>');
	});
};

fb.session.ajax.chartDataView = function(fbs) {
	// TODO Chart presentation
};
