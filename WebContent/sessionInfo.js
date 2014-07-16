$(document).ready(
		function() {
			var ajax = fb.session.ajax;
			var itemID = fb.getQueryParam('itemID');

			ajax.loadCurrentSessionInfo(itemID, $('#sessionInfo'));
			ajax.updateCurrentSessionData(itemID, $('#listDataView'),
					fb.session.dataView.rawDataView);
			ajax.updateCurrentSessionData(itemID, $('#chartDataView'),
					fb.session.dataView.chartDataView);
		});
