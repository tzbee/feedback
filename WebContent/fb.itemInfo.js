$(document).ready(
		function() {

			var itemAjax = fb.item;
			var ajax = fb.session.ajax;
			var itemIDParam = fb.getQueryParam('itemID');
			var sessionIndexParam = fb.getQueryParam('sessionIndex');

			/*
			 * get item information and display in selected element
			 * 
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

			/**
			 * DATA
			 */

			var itemID = itemIDParam;
			var sessionIndex = sessionIndexParam !== null ? sessionIndexParam
					: 'current';

			/**
			 * Configure and update elements at document load
			 */

			fb.session.ajax.updateCurrentSessionData(itemID, sessionIndex,
					$('#listDataView'), 'list');

			fb.session.ajax.updateCurrentSessionData(itemID, sessionIndex,
					$('#chartDataView'), 'chart');

			/**
			 * Update button updates all data view elements
			 */

			$('#updateViewButton').click(function() {
				fb.update($('.dataView'));
			});

			/**
			 * Enable auto-update button
			 */

			$('#startAutoUpdateButton').click(
					function() {
						fb.setAutoUpdate($('.dataView'), true);

						// Notification
						fb.createPopupWindow(
								'<span>AutoUpdate Enabled! </span>', 'info');
						fb.showPopup($('.popup'), 500, 2000);
					});

			/**
			 * Disable auto-update button
			 */

			$('#stopAutoUpdateButton').click(
					function() {
						fb.setAutoUpdate($('.dataView'), false);

						// Notification
						fb.createPopupWindow(
								'<span>AutoUpdate Disabled! </span>', 'info');
						fb.showPopup($('.popup'), 500, 2000);
					});

			/**
			 * Select dataStrategy component
			 */

			$('#selectDataStrategy').click(function() {
				// Get the chosen data strategy
				var strategy = $(this).val(), element = $('.dataView');

				fb.configureElement(element, {
					dataStrategy : strategy
				});
				
				fb.update(element);
			});

			/**
			 * Select current session button
			 */

			$('#updateCurrentSessionButton').click(
					function() {
						fb.session.ajax.updateCurrentSessionData(itemID,
								'current', $('#listDataView'), 'list');

						fb.session.ajax.updateCurrentSessionData(itemID,
								'current', $('#chartDataView'), 'chart');
					});

		});
