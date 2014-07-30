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
	var ITEM_ROOT_RESOURCE = 'rest/items/';

	var getItemResource = function(itemID) {
		return ITEM_ROOT_RESOURCE + itemID + '/';
	};

	var getSessionRootResource = function(itemID) {
		return getItemResource(itemID) + 'sessions/';
	};

	var getSessionResource = function(itemID, sessionIndex) {
		return getSessionRootResource(itemID) + sessionIndex + '/'
	};

	var getSessionDataResource = function(itemID, sessionIndex, dataStrategy) {
		return getSessionResource(itemID, sessionIndex) + 'data/'
				+ (dataStrategy ? '?strategy=' + dataStrategy : '');
	};

	/**
	 * Data configuration
	 */
	fb.session.data = {
		itemID : '',
		sessionIndex : 'current',
		dataView : null,
		autoUpdate : false,
		updateInterval : -1,
		dataStrategy : ''
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
			element, dataView, dataStrategy) {

		// Set the session index of the component
		fb.session.data.sessionIndex = sessionIndex;

		$.getJSON(getSessionDataResource(itemID, sessionIndex, dataStrategy),
				function(data) {
					element.empty();
					dataView(element, data);
				}).fail(
				function() {
					fb.createPopupWindow(
							'<span> An error has occurred!</span>', 'error');
					fb.showPopup($('.popup'), 500, 2000);
					// alert("An error has occurred");
				});
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
						ITEM_RESOURCE + itemID + '/sessions/' + sessionIndex,
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