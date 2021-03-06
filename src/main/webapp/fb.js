/**
 * Data handling module
 */

var fb = {};
fb.session = {};
fb.session.ajax = {};
fb.session.dataView = {};

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
	 * Get URL query parameter
	 * 
	 * @param key
	 *            the name of parameter
	 */
	fb.getQueryParam = function(key) {
		key = key.replace(/[*+?^$.\[\]{}()|\\\/]/g, "\\$&"); // escape RegEx
		// meta
		// chars
		var match = location.search.match(new RegExp("[?&]" + key
				+ "=([^&]+)(&|$)"));
		return match && decodeURIComponent(match[1].replace(/\+/g, " "));
	};

	/**
	 * Updates a query parameter with the specified value
	 * 
	 * @param uri
	 *            URI to modify
	 * @param key
	 *            name of the parameter to modify
	 * @param value
	 *            new value to assign to the parameter
	 */
	fb.updateQueryParameter = function(uri, key, value) {
		var re = new RegExp("([?|&])" + key + "=.*?(&|#|$)", "i");
		if (uri.match(re)) {
			return uri.replace(re, '$1' + key + "=" + value + '$2');
		} else {
			var hash = '';
			var separator = uri.indexOf('?') !== -1 ? "&" : "?";
			if (uri.indexOf('#') !== -1) {
				hash = uri.replace(/.*#/, '#');
				uri = uri.replace(/#.*/, '');
			}
			return uri + separator + key + "=" + value + hash;
		}
	};

	/**
	 * NOTIFICATIONS
	 */

	/**
	 * Creates a notification element
	 * 
	 * @param text
	 *            content of the notification element
	 */
	fb.createPopupWindow = function(text, type) {
		$('body').append($('<div>', {
			html : $('<span>', {
				class : 'popupText',
				html : text
			}),
			class : 'popup ' + (type ? type : 'info')
		}));
	};

	/**
	 * Shows an element and hides it after a while
	 * 
	 * @param element
	 *            the element to show
	 * 
	 * @param speed
	 *            speed of the animation
	 * 
	 * @param duration
	 *            the time the element is shown
	 */
	fb.showPopup = function(element, speed, duration) {
		element.fadeIn(speed, function() {
			setTimeout(function() {
				hidePopup(element, speed);
			}, duration);
		});
	};

	/**
	 * Hides an element
	 * 
	 * @param element
	 *            the element to hide
	 * 
	 * @param speed
	 *            the speed of the animation
	 * 
	 */
	var hidePopup = function(element, speed) {
		element.fadeOut(speed, function() {
			element.remove();
		});
	};

	/**
	 * Displays a notification
	 */
	fb.notification = function(text, type) {
		fb.createPopupWindow(text, type);
		fb.showPopup($('.popup'), 500, 2000);
	};

	/**
	 * DATA CONFIGURATION
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

		case 'barChart':
			return fb.session.dataView.barChartDataView;

		case 'maChart':
			return fb.session.dataView.maDataView;

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
		var dataSource = dataConfig.dataSource, //
		dataView = dataConfig.dataView, //
		dataStrategy = dataConfig.dataStrategy;

		elements.each(function() {
			var element = $(this);

			if (dataSource) {
				element.attr(DATA_SOURCE_ATTR, dataSource);
			}

			if (dataView) {
				element.attr(DATA_VIEW_ATTR, dataView);
			}

			if (dataStrategy) {
				var dataSourceAttr = element.attr(DATA_SOURCE_ATTR);

				if (dataSourceAttr != null) {
					dataSourceAttr = fb.updateQueryParameter(dataSourceAttr,
							'strategy', dataStrategy);

					element.attr(DATA_SOURCE_ATTR, dataSourceAttr);
				}
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

				fb.notification('Auto-update enabled', 'info');

			} else {
				clearInterval(element.attr('data-auto-update'));

				fb.notification('Auto-update disabled', 'info');
			}
		});
	};

	/**
	 * Create moving average series based on input data
	 */

	fb.createTimeWindowData = function(data, timePeriod) {
		var tp = parseInt(timePeriod);
		var resultData = [];

		function TimeWindow(start, end) {
			this.start = start;

			this.end = end;

			this.data = [];

			this.includes = function(value) {
				return value >= this.start && value < this.end;
			};

			this.addData = function(dataUnit) {
				this.data.push(dataUnit);
			};

			this.translate = function(dt) {
				return new TimeWindow(this.start + dt, this.end + dt);
			};

		}

		var getDataTimeStamp = function(dataUnit) {
			return dataUnit.createdAt;
		};

		if (data.length <= 0) {
			return [];
		}

		var startTime = getDataTimeStamp(data[0]);

		var currentTimeWindow = new TimeWindow(startTime, startTime + tp);

		var currentDataUnitIndex = 0;
		var currentDataUnit;
		var currentDataTimeStamp;

		while (currentDataUnitIndex < data.length) {
			currentDataUnit = data[currentDataUnitIndex];
			currentDataTimeStamp = getDataTimeStamp(currentDataUnit);

			if (currentDataTimeStamp) {
				if (currentTimeWindow.includes(currentDataTimeStamp)) {

					currentTimeWindow.addData({
						value : currentDataUnit.value,
						createdAt : currentDataUnit.createdAt
					});

					currentDataUnitIndex++;
				} else {
					if (currentTimeWindow.data.length > 0) {
						resultData.push(currentTimeWindow.data);
					}

					currentTimeWindow = currentTimeWindow.translate(tp);
				}

				if (currentDataUnitIndex == data.length - 1) {
					if (currentTimeWindow.data.length > 0) {
						resultData.push(currentTimeWindow.data);
					}
				}

			} else {
				currentDataUnitIndex++;
			}
		}

		return resultData;
	};

	/**
	 * Returns the average of a the values of a data set
	 */

	var average = function(data) {

		return (function(data) {
			var sum = 0;

			$.each(data, function(i, dataUnit) {
				sum += dataUnit.value;
			});

			return sum;
		})(data) / data.length;
	};

	/**
	 * Create moving average data
	 */
	fb.createMovingAverageData = function(timeStamps, timeWindow) {
		var resultData = [];
		var timeSubLists = fb.createTimeWindowData(timeStamps, timeWindow);

		$.each(timeSubLists, function(i, timeSubList) {
			if (timeSubList.length > 0) {
				resultData.push({
					value : average(timeSubList),
					createdAt : timeSubList[0].createdAt,
					end : timeSubList[timeSubList.length - 1].createdAt
				});
			}
		});

		return resultData;
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
	 * create an Update Block
	 * 
	 * @param element
	 *            jQuery element to create the block into
	 * @param dataBlockID
	 *            id of the block containing the data view
	 * @param dataViewOptions
	 *            list of data views available (list, chart, barChart)
	 * @param dataStrategyOptions
	 *            list of data strategies available (none, average,
	 *            singleAverage)
	 */

	fb.createUpdateBlock = function(element, dataBlockID, dataViewOptions,
			dataStrategyOptions) {
		element.empty();

		var dataViewBlockID = dataBlockID;

		// Creates a new button
		var createButton = function(text, action) {
			return $('<input>', {
				type : 'button',
				value : text,
				on : {
					click : action
				}
			});
		};

		// Create the data view block element
		element.append($('<div>', {
			id : dataViewBlockID,
			class : 'dataViewBlock'
		}));

		var dataViewBlockElement = $('#' + dataViewBlockID);

		/**
		 * BUTTONS
		 */
		var updateButton = createButton('Update', function() {
			fb.update(dataViewBlockElement);
		});

		var startAutoUpdateButton = createButton('Start auto update',
				function() {
					fb.setAutoUpdate(dataViewBlockElement, true);
				});

		var stopAutoUpdateButton = createButton('Stop auto update', function() {
			fb.setAutoUpdate(dataViewBlockElement, false);
		});

		/**
		 * SELECT COMPONENT
		 */

		var createSelectComponent = function(optionValues) {

			var selectElement = (function() {
				var e = $('<select>', {});

				$.each(optionValues, function(i, optionValue) {
					e.append($('<option>', {
						value : optionValue,
						html : optionValue
					}));
				});

				return e;
			})();

			selectElement.attr('Class', 'selectDynamic');
			return selectElement;
		};

		/**
		 * Create the select element if there is more than one data strategy
		 * option
		 */
		if (dataStrategyOptions.length > 1) {
			var selectDataStrategyElement = createSelectComponent(dataStrategyOptions);

			selectDataStrategyElement.click(function() {
				// Get the chosen data strategy
				var strategy = $(this).val(), element = dataViewBlockElement;

				fb.configureElement(element, {
					dataStrategy : strategy
				});

				fb.update(element);
			});

			// Add the select element to the container
			element.prepend(selectDataStrategyElement);
		}

		/**
		 * Create the select element if there is more than one data view option
		 */
		if (dataViewOptions.length > 1) {
			var selectDataViewElement = createSelectComponent(dataViewOptions);

			selectDataViewElement.click(function() {
				var dataView = $(this).val(), element = dataViewBlockElement;

				fb.configureElement(element, {
					dataView : dataView
				});

				fb.update(element);
			});

			// Add the select element to the container
			element.prepend(selectDataViewElement);
		}

		/**
		 * Prepend all buttons
		 */

		element.prepend(stopAutoUpdateButton);
		element.prepend(startAutoUpdateButton);
		element.prepend(updateButton);
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
	 *            element that will contain the view
	 * @param fbData
	 *            data to present
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
	 *            element that will contain the view
	 * @param fbData
	 *            data to present
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

	/**
	 * Data view presenting the data in a bar chart form. X axis -> data tag, Y
	 * axis -> data values
	 * 
	 * @param element
	 *            element to create the chart into
	 * @param data
	 *            data sent to the chart
	 */
	fb.session.dataView.barChartDataView = function(element, data) {
		element
				.highcharts({
					chart : {
						zoomType : 'y'
					},
					title : {
						text : 'Feedback Session'
					},
					subtitle : {
						text : document.ontouchstart === undefined ? 'Click and drag in the plot area to zoom in'
								: 'Pinch the chart to zoom in'
					},
					xAxis : {
						type : 'category',
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
						type : 'bar',
						name : 'Feedback unit',
						data : (function() {
							var tmpData = [];
							$.each(data.dataUnits, function(index, dataUnit) {
								tmpData.push([ dataUnit.tag, dataUnit.value ]);
							});
							return tmpData;
						})()
					} ]
				});
	};

	/**
	 * Data view presenting the data in a area chart form. X axis -> time stamp,
	 * Y -> average of a specific time window
	 */
	fb.session.dataView.maDataView = function(container, data) {
		container.attr('class', 'basic-grey');

		var translate = function(data) {
			var outputData = [];
			var value, ts;

			var toLocalTime = function(ts) {
				var dateTime = new Date(ts);

				return dateTime.getTime()
						+ (-dateTime.getTimezoneOffset() * 60 * 1000);
			};

			$.each(data.dataUnits, function(i, dataUnit) {
				value = dataUnit.value;
				ts = dataUnit.createdAt;

				outputData.push([ toLocalTime(ts), value ]);
			});

			return outputData;
		};

		var translatedData = translate(data);

		var timePeriodInput = $('<input>', {
			type : 'text',
			placeHolder : 'Time window (s)'
		});

		var timePeriodButton = $('<input>', {
			type : 'button',
			value : 'Apply',
			on : {
				click : function() {
					var timeWindow = timePeriodInput.val() * 1000;

					var maData = fb.createMovingAverageData(data.dataUnits,
							timeWindow);

					updateChart(translate({
						dataUnits : maData
					}));
				}
			}
		});

		var createPeriodButton = function(value, period) {
			return $('<input>', {
				type : 'button',
				value : value,
				on : {
					click : function() {
						var timeWindow = period;

						var maData = fb.createMovingAverageData(data.dataUnits,
								timeWindow);

						updateChart(translate({
							dataUnits : maData
						}));
					}
				}
			});
		};

		container.append(timePeriodInput);
		container.append(timePeriodButton);
		container.append(createPeriodButton('1 hour', 3600000));
		container.append(createPeriodButton('1 day', 86400000));
		container.append(createPeriodButton('1 week', 604800000));
		container.append(createPeriodButton('1 month', 2592000000));

		var chartContainer = $('<div>');

		container.append(chartContainer);

		var updateChart = function(data) {
			chartContainer.highcharts('StockChart', {

				rangeSelector : {
					selected : 1,
					inputEnabled : container.width() > 480
				},

				title : {
					text : 'Moving average'
				},

				series : [ {
					data : data
				} ],

				xAxis : {
					title : {
						text : 'Time'
					},

					dateTimeLabelFormats : {
						millisecond : '%H:%M:%S',
					}
				}

			});
		};

		updateChart(translatedData);
	};

	/**
	 * ACCOUNT OPERATIONS
	 */

	fb.account = {};

	/**
	 * Create a new account key
	 * 
	 * @param mail
	 *            mail address identifying the account
	 * @param accountType
	 *            account type (user, owner)
	 * 
	 * @param keyContainerElement
	 *            element to display the generated key in
	 */
	fb.account.createNewKey = function(mail, accountType, keyContainerElement) {

		// Rest URL to post on
		var url = 'rest/users/key';

		// Data to send
		var postData = {
			mail : mail,
			accountType : accountType
		};

		// Ajax post
		$.post(url, postData, function(responseData) {

			// Delete the previous key link
			keyContainerElement.empty();

			var accountKey = responseData;

			// Create the link
			keyContainerElement.append($('<a>').attr('href', '#').html(
					accountKey).attr('id', 'keyLink').click(
					function() {

						// Notification when an error occurred
						var failNotification = function(jqXHR, textStatus,
								errorThrown) {

							fb.notification(
									'An error occurred: ' + errorThrown,
									'error');
						};

						// Create the account on click
						fb.account.createAccount(accountKey, function() {

							// After account creation..

							// Delete the key
							deleteKey(accountKey,

							// Key has been deleted successfully
							function() {

								// Notify user if the account has been created
								// successfully
								fb.notification('New account created', 'info');

								// Delete the key link
								keyContainerElement.empty();

							},

							// An error occurred during key deletion
							failNotification);

						},

						// An error occurred during account creation
						failNotification);

					}));

			// Notify user when the key is successfully created
			fb.notification('New key created', 'info');
		});
	};

	/**
	 * Delete a key
	 */
	var deleteKey = function(key, success, error) {

		// Rest URL to delete on
		var url = 'rest/users/key/' + key;

		// Ajax delete
		$.ajax({
			url : url,
			type : 'DELETE',
			success : success,
			error : error
		});
	};

	/**
	 * Create a new account based on the account key
	 * 
	 * @param accountKey
	 *            the key to use to create the account
	 */
	fb.account.createAccount = function(accountKey, success, error) {

		// Rest URL to post on
		var url = 'rest/users/';

		// Data to send
		var postData = {
			userKey : accountKey
		};

		// Ajax post
		$.post(url, postData, success).fail(error);
	};

	/**
	 * Login to the system
	 */

	fb.account.login = function(userID, password) {

		// Rest URL to post on
		var url = REST_ROOT + 'users/login';

		// Data to send
		var postData = {
			userID : userID,
			password : password
		};

		// Ajax post
		$.post(url, postData, function() {
			window.location.replace('home');
		}).fail(function() {
			fb.notification('login failure', 'error');
		});
	};

	/**
	 * Log out of the system
	 */

	fb.account.logout = function() {

		// Rest URL to post on
		var url = REST_ROOT + 'users/logout';

		// Ajax post
		$.post(url);
		fb.notification('Logged Out!', 'info');

	};

	/**
	 * HTML modules
	 */

	fb.html = {};

	/**
	 * Create the basic html template
	 */
	fb.html.simpleInitHTML = function(content, navContent, loggedUser, success) {

		$.get('template.mst', function(template) {

			var rendered = Mustache.render(template, {
				pageContent : content,
				testNav : navContent,
				loggedUserContainer : loggedUser,
			});

			$('body').html(rendered);

			success();
		});
	};
	// fb.html.initHTML = function(contentURL, success) {
	//
	// $
	// .get(
	// 'template.mst',
	// function(template) {
	// $
	// .get(
	// contentURL,
	// function(content) {
	//
	// var loggedUserContainer = '';
	//
	// $
	// .get(
	// 'rest/users/logged')
	// .done(
	// function(user) {
	// loggedUserContainer = Mustache
	// .render(
	// '<span id="loggedContainer">'
	// + 'Logged as <span class="{{accountType}}">{{userName}}</span>'
	// + '<span>',
	// user);
	//
	// })
	// .always(
	// function() {
	//
	// var rendered = Mustache
	// .render(
	// template,
	// {
	// loggedUserContainer : loggedUserContainer,
	// pageContent : content,
	// });
	//
	// $('body')
	// .html(
	// rendered);
	//
	// success();
	// });
	// });
	// });
	// };

	/**
	 * Common starting script for all views
	 */

	$(document).ready(function() {

		$('#loginButton').click(function() {
			window.location.replace('login.html');
		});

		$('#home').click(function() {
			window.location.replace('home');
		});
	});

})(jQuery, window, document);
