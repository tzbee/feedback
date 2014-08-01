$(document)
		.ready(
				function() {
					var ajax = fb.session.ajax;
					var itemIDParam = fb.getQueryParam('itemID');
					var sessionIndexParam = fb.getQueryParam('sessionIndex');

					var updateAll = function() {
						var itemID = itemIDParam;
						var sessionIndex = sessionIndexParam !== null ? sessionIndexParam
								: 'current';

						ajax.updateCurrentSessionData(itemID, sessionIndex,
								$('#listDataView'), 'list');
						ajax.updateCurrentSessionData(itemID, sessionIndex,
								$('#chartDataView'), 'chart');

						ajax.loadCurrentSessionInfo(itemID, sessionIndex,
								$('#sessionInfo'));
					};

					updateAll();

					$('#updateViewButton').click(updateAll);
				});