$(document)
		.ready(
				function() {

					var itemAjax = fb.item;

					var ajax = fb.session.ajax;
					var itemIDParam = fb.getQueryParam('itemID');
					var sessionIndexParam = fb.getQueryParam('sessionIndex');

					$.getJSON(
							'rest/items/' + fb.getQueryParam('itemID'),
							function(data) {

								itemAjax.updateItemElementForInfoDisplay(
										$('#itemInformation'), data);
							}).done(function() {
						fb.createPopupWindow('<span>!!!!</span>', 'info');
						fb.showPopup($('.popup'), 500, 2000);
					})

					.fail(
							function() {
								fb.createPopupWindow(
										'<span>An error has occured</span>',
										'error');
								fb.showPopup($('.popup'), 500, 2000);
							});
					// get session archive

					$.getJSON(
							'rest/items/' + fb.getQueryParam('itemID')
									+ '/sessions/archive',
							function(sessions) {
								itemAjax.displaySession($('#sessionArchive'),
										sessions);
							}).done(function() {
						fb.createPopupWindow('<span>!!!!</span>', 'info');
						fb.showPopup($('.popup'), 500, 2000);
					})

					.fail(
							function() {
								fb.createPopupWindow(
										'<span>An error has occured</span>',
										'error');
								fb.showPopup($('.popup'), 500, 2000);
							});

					var updateAll = function() {
						var itemID = itemIDParam;
						var sessionIndex = sessionIndexParam !== null ? sessionIndexParam
								: 'current';

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
					// setInterval(updateAll, 5000);

					$('#updateCurrentSessionButton').click(updateAll);
					$('#updateViewButton').click(ajax.updateData);
				});
