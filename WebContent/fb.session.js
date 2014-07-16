/**
 * Feedback Session handling module
 */

fb.session = {};
fb.session.ajax = {};
fb.session.dataView = {};

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
fb.session.ajax.updateCurrentSessionData = function(itemID, element, dataView) {
	$.getJSON(fb.host + '/Feedback/rest/items/' + itemID + '/sessions/current',
			function(fbs) {
				dataView(element, fbs);
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
fb.session.ajax.loadCurrentSessionInfo = function(itemID, element) {
	$.getJSON(fb.host + '/Feedback/rest/items/' + itemID + '/sessions/current',
			function(fbs) {
				var createdAt = fbs.formattedCreatedAt;
				var closedAt = fbs.formattedClosedAt;
				var sessionID = fbs.id;

				var sessionInfoList = $('<ul>');
				var createSessionInfoElement = function(content) {
					return $('<li>', {
						html : content
					});
				};

				sessionInfoList.append(createSessionInfoElement('id: '
						+ sessionID));
				sessionInfoList.append(createSessionInfoElement('start: '
						+ createdAt));
				sessionInfoList.append(createSessionInfoElement('closed: '
						+ (closedAt === '' ? 'still active' : closedAt)));

				element.append(sessionInfoList);
			});
};

/**
 * 
 * @param viewElement
 * @param fbs
 */
fb.session.dataView.rawDataView = function(viewElement, fbs) {
	var fbUnits = fbs.feedbackUnits;
	$.each(fbUnits, function(index, fbu) {
		viewElement.append('Feedback unit (value: ' + fbu.value
				+ ', created at: ' + fbu.formattedCreatedAt + ')<br/>');
	});
};

/**
 * 
 * @param viewElement
 * @param fbs
 */
fb.session.dataView.chartDataView = function(viewElement, fbs) {
	var chartInfo = {
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
									Highcharts.Color(
											Highcharts.getOptions().colors[0])
											.setOpacity(0).get('rgba') ] ]
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
			name : 'USD to EUR',
			pointInterval : 24 * 3600 * 1000,
			pointStart : Date.UTC(2006, 0, 01),
			data : []
		} ]
	};

	$.each(fbs.feedbackUnits, function(index, fbu) {
		chartInfo.series[0].data.push(fbu.value);
	});

	alert(chartInfo.series[0].data);

	$(function() {
		viewElement.highcharts(chartInfo);
	});
};
