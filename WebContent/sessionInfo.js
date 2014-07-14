$(document).ready(
		function() {
			var ajax = fb.session.ajax;
			var itemID = fb.getQueryParam('itemID');

			ajax.updateCurrentSessionData(parseInt(itemID), $('#dataView'),
					fb.session.dataView.rawDataView);
		});
