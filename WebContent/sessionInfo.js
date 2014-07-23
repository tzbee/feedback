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
						console.log(sessionIndex);

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

					$('#listViewButton').click(function() {
						window.location.href = '#';
						$('#chartDataView').hide();
						$('#listDataView').show();
					});

					$('#graphViewButton').click(function() {
						window.location.href = '#';
						$('#listDataView').hide();
						$('#chartDataView').show();
						
					});
				});
