$(document).ready(function() {
	var ajax = fb.session.ajax;
	var itemID = fb.getQueryParam('itemID');

	ajax.updateCurrentSessionData(parseInt(itemID), ajax.rawDataView);
});
