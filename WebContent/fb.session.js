/**
 * Data handling module
 */

fb.session = {};
fb.session.ajax = {};
fb.session.dataView = {};
fb.session.data;

(function($, window, document) {

	/**
	 * 
	 * Constants & util functions
	 * 
	 */

	var REST_ROOT = 'rest/';
	var ITEM_ROOT_RESOURCE = REST_ROOT + 'items/';

	var getItemResource = function(itemID) {
		return ITEM_ROOT_RESOURCE + itemID + '/';
	};

	var getSessionRootResource = function(itemID) {
		return getItemResource(itemID) + 'sessions/';
	};

	var getSessionResource = function(itemID, sessionIndex) {
		return getSessionRootResource(itemID) + sessionIndex + '/';
	};

	var getSessionDataResource = function(itemID, sessionIndex, dataStrategy) {
		return getSessionResource(itemID, sessionIndex) + 'data/'
				+ (dataStrategy != null ? '?strategy=' + dataStrategy : '');
	};

	/**
	 * DATA CONFIGURATION
	 */

	/**
	 * CONSTANTS
	 */

	/**
	 * HTML5 Data attributes
	 */

	var DATA_SOURCE_ATTR = 'data-source';
	var DATA_VIEW_ATTR = 'data-view';

	/**
	 * UTILITY FUNCTIONS
	 */

	/**
	 * Convert a string to a dataView object
	 */

	var stringToDataView = function(str) {
		var DEFAULT_DATA_VIEW = fb.session.dataView.chartDataView;

		if (str == null) {
			return DEFAULT_DATA_VIEW;
		}

		switch (str) {

		case 'list':
			return fb.session.dataView.rawDataView;

		case 'chart':
			return fb.session.dataView.chartDataView;

		default:
			return DEFAULT_DATA_VIEW;
		}
	};

	/**
	 * Configure a jQuery element with the given configuration properties
	 * 
	 * @param element
	 *            the jQuery element to configure
	 * 
	 * @param dataConfig
	 *            configuration data
	 */
	fb.configureElement = function(elements, dataConfig) {
		dataSource = dataConfig.dataSource;
		dataView = dataConfig.dataView;

		elements.each(function() {
			var element = $(this);

			if (dataSource) {
				element.attr(DATA_SOURCE_ATTR, dataSource);
			}

			if (dataView) {
				element.attr(DATA_VIEW_ATTR, dataView);
			}
		});
	};

	/**
	 * Update elements using their nested configuration properties
	 * 
	 * @param element
	 *            elements to update
	 */
	fb.update = function(elements) {
		var dataSource, dataView;

		// Ajax call function
		var ajaxCall = function(ds, e, dv) {
			$.getJSON(ds, function(data) {
				e.empty();
				dv(e, data);
			})
			// If request fails
			.fail(
					function() {
						fb
								.createPopupWindow(
										'<span> An error has occurred!</span>',
										'error');
						fb.showPopup($('.popup'), 500, 2000);
					});

		};

		elements.each(function() {
			var element = $(this);

			// Get the data properties from the element
			dataSource = element.attr(DATA_SOURCE_ATTR); //
			dataView = stringToDataView(element.attr(DATA_VIEW_ATTR));

			// Do the ajax call
			ajaxCall(dataSource, element, dataView);
		});
	};

	/**
	 * Enable / Disable auto update of the element
	 */
	fb.setAutoUpdate = function(elements, enabled) {
		// 5 sec delay between updates
		var DELAY = 5000;

		elements.each(function() {
			var element = $(this);

			if (enabled) {
				element.attr('data-auto-update', setInterval(function() {
					fb.update(element);
				}, DELAY));
			} else {
				clearInterval(element.attr('data-auto-update'));
			}
		});
	};

	/**
	 * Update an element's content with the current feedback session data of the
	 * item.
	 * 
	 * @param itemID
	 *            id of the item holding the data
	 * @param element
	 *            element to update the content of
	 * 
	 * @param dataView
	 *            the choice of presentation for the data
	 */
	fb.session.ajax.updateCurrentSessionData = function(itemID, sessionIndex,
			elements, dataView, dataStrategy) {

		// Create the REST URL
		var sourceUrl = getSessionDataResource(itemID, sessionIndex,
				dataStrategy);

		// Create configuration object
		var dataConfig = {
			dataSource : sourceUrl,
			dataView : dataView
		};

		// Configure the element
		fb.configureElement(elements, dataConfig);

		// Update the element
		fb.update(elements);
	};

	/**
	 * Load current session info
	 * 
	 * @param itemID
	 *            id of the item the feedback session belongs to
	 * 
	 * @param element
	 *            element to be updated
	 */
	fb.session.ajax.loadCurrentSessionInfo = function(itemID, sessionIndex,
			element) {
		$
				.getJSON(
						getSessionResource(itemID, sessionIndex),
						function(fbs) {
							element.empty();
							var sessionID = fbs.id;
							var createdAt = fbs.formattedCreatedAt;
							var closedAt = fbs.formattedClosedAt;
							var localIndex = fbs.localIndex;

							var scaleValues = fbs.feedbackConfig.scale.scaleValues;

							var sessionInfoList = $('<ul>');
							var createSessionInfoElement = function(content) {
								return $('<li>', {
									html : content
								});
							};

							sessionInfoList
									.append(createSessionInfoElement('id: '
											+ sessionID));
							sessionInfoList
									.append(createSessionInfoElement('start: '
											+ createdAt));
							sessionInfoList
									.append(createSessionInfoElement('closed: '
											+ (closedAt === '' ? 'still active'
													: closedAt)));
							sessionInfoList
									.append(createSessionInfoElement('configured scale values: '
											+ (function() {
												var scaleValueList = '';
												$
														.each(
																scaleValues,
																function(index,
																		scaleValue) {
																	scaleValueList += scaleValue
																			+ ' ';
																});
												return scaleValueList;
											})()));

							sessionInfoList
									.append(createSessionInfoElement('local index: '
											+ localIndex));

							element.append(sessionInfoList);
						}).fail(
						function() {
							fb.createPopupWindow(
									'<span> An error has occurred!</span>',
									'error');
							fb.showPopup($('.popup'), 500, 2000);
						});
	};

	/**
	 * Data view presenting the data in a simple list form
	 * 
	 * @param viewElement
	 * @param fbData
	 */
	fb.session.dataView.rawDataView = function(viewElement, fbData) {
		$.each(fbData.dataUnits, function(index, fbu) {
			viewElement.append('Feedback unit (value: ' + fbu.value
					+ ', created at: ' + fbu.formattedCreatedAt + ')<br/>');
		});
	};

	/**
	 * Data view presenting the data in a area chart form. X axis -> time stamp,
	 * Y axis -> feedback values
	 * 
	 * @param viewElement
	 * @param fbData
	 */
	fb.session.dataView.chartDataView = function(viewElement, fbData) {
		viewElement
				.highcharts({
					chart : {
						zoomType : 'x'
					},
					title : {
						text : 'Feedback Session'
					},
					subtitle : {
						text : document.ontouchstart === undefined ? 'Click and drag in the plot area to zoom in'
								: 'Pinch the chart to zoom in'
					},
					xAxis : {
						type : 'datetime',
						minRange : 20
					// fourteen days
					},
					yAxis : {
						title : {
							text : 'FeedbackValue'
						}
					},
					legend : {
						enabled : false
					},
					plotOptions : {
						area : {
							fillColor : {
								linearGradient : {
									x1 : 0,
									y1 : 0,
									x2 : 0,
									y2 : 1
								},
								stops : [
										[ 0, Highcharts.getOptions().colors[0] ],
										[
												1,
												Highcharts
														.Color(
																Highcharts
																		.getOptions().colors[0])
														.setOpacity(0).get(
																'rgba') ] ]
							},
							marker : {
								radius : 2
							},
							lineWidth : 1,
							states : {
								hover : {
									lineWidth : 1
								}
							},
							threshold : null
						}
					},

					series : [ {
						type : 'area',
						name : 'Feedback unit',
						data : (function() {
							var tmpData = [];
							$.each(fbData.dataUnits, function(index, fbu) {
								tmpData.push([ fbu.createdAt, fbu.value ]);
							});
							return tmpData;
						})()
					} ]
				});
	};
})(jQuery, window, document);