$(document).ready(function() {
	var ajax = fb.session.ajax;
	var itemID = fb.getQueryParam('itemID');

	ajax.loadCurrentSessionInfo(itemID, $('#sessionInfo'));
});
