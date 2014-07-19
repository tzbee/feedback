$(document).ready(
		function() {
			var ajax = fb.session.ajax;
			var itemID = fb.getQueryParam('itemID');
			var sessionIndex = fb.getQueryParam('sessionIndex');

			ajax
					.loadCurrentSessionInfo(itemID, sessionIndex,
							$('#sessionInfo'));
			ajax.updateCurrentSessionData(itemID, sessionIndex,
					$('#listDataView'), fb.session.dataView.rawDataView);
			ajax.updateCurrentSessionData(itemID, sessionIndex,
					$('#chartDataView'), fb.session.dataView.chartDataView);

			$('#updateViewButton').click(
					function() {
						ajax.updateCurrentSessionData(itemID, sessionIndex,
								$('#listDataView'),
								fb.session.dataView.rawDataView);
						ajax.updateCurrentSessionData(itemID, sessionIndex,
								$('#chartDataView'),
								fb.session.dataView.chartDataView);
					});
		});
