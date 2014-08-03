$(document).ready(
		function() {

			var itemAjax = fb.item;
			var ajax = fb.session.ajax;
			var itemIDParam = fb.getQueryParam('itemID');
			var sessionIndexParam = fb.getQueryParam('sessionIndex');

			/*
			 * get item information and display in selected element
			 */

			$.getJSON(
					'rest/items/' + fb.getQueryParam('itemID'),
					function(data) {

						itemAjax.updateItemElementForInfoDisplay(
								$('#itemInformation'), data);
					})

			.fail(
					function() {
						fb.createPopupWindow(
								'<span>An error has occured</span>', 'error');
						fb.showPopup($('.popup'), 500, 2000);

					});

			/*
			 * Get session archive and display session 'buttons' in selected
			 * element
			 */

			$
					.getJSON(
							'rest/items/' + fb.getQueryParam('itemID')
									+ '/sessions/archive',
							function(sessions) {
								itemAjax.displaySession($('#sessionArchive'),
										sessions);
							}).done(function() {
						fb.showPopup($('.popup'), 500, 2000);
					})

					.fail(
							function() {
								fb.createPopupWindow(
										'<span>An error has occured</span>',
										'error');
								fb.showPopup($('.popup'), 500, 2000);
							});

			/**
			 * DATA
			 */

			var itemID = itemIDParam;
			var sessionIndex = sessionIndexParam !== null ? sessionIndexParam
					: 'current';

			/**
			 * Create the data block
			 */

			// ID of the data view block to create
			var dataViewBlockID = 'dataViewBlock';

			// Create the data component
			fb.createUpdateBlock($('#updateBlock'), dataViewBlockID, [ 'list',
					'chart', 'barChart' ],
					[ 'none', 'average', 'singleAverage' ]);

			/**
			 * Configure and update the data block at document load
			 */

			fb.session.ajax.updateCurrentSessionData(itemID, sessionIndex,
					$('#' + dataViewBlockID), 'chart');

			/**
			 * Select current session button
			 */

			$('#updateCurrentSessionButton').click(
					function() {
						fb.session.ajax.updateCurrentSessionData(itemID,
								'current', $('#' + dataViewBlockID), 'chart');
					});

		});
