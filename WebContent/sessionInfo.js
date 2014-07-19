$(document)
		.ready(
				function() {
					var ajax = fb.session.ajax;
					var itemID = fb.getQueryParam('itemID');
					var sessionIndex = fb.getQueryParam('sessionIndex');

					var updateAll = function() {
						ajax.updateCurrentSessionData(itemID, sessionIndex,
								$('#listDataView'),
								fb.session.dataView.rawDataView);
						ajax.updateCurrentSessionData(itemID, sessionIndex,
								$('#chartDataView'),
								fb.session.dataView.chartDataView);

						ajax.loadCurrentSessionInfo(itemID, sessionIndex,
								$('#sessionInfo'));
					};

					updateAll();
					setInterval(updateAll, 5000);

					$('#updateViewButton').click(updateAll);
				});