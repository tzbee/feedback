fb.item = {};

fb.item.itemName = document.getElementById('itemName');

fb.item.itemDescription = document.getElementById('itemDescription');

fb.item.ratingEnabled = document.getElementById('ratingEnabled');

fb.item.itemToDelete = document.getElementById('itemToDelete');

fb.item.fbsItem = document.getElementById('FBSItem');

fb.item.itemToEdit = document.getElementById('itemToEdit');

fb.item.updateItemElement = function(element, items) {
	element.empty();
	$.each(items, function(index, item) {
		element.append('Item: ' + '<span id="item' + (index + 1) + '">'
				+ item.id + ' ' + item.name + ' ' + item.description + ' '
				+ item.ratingEnabled + ' ' + item.state + '</span>' + '<br/>');
	});
};

/*
 * Function to display item information information @param element element used
 * to display retrieved data
 * 
 * @param item item retrieved in order to display its info
 */
fb.item.updateItemElementForInfoDisplay = function(element, item) {
	element.empty();
	element.append('<span>Item ID: ' + item.id
			+ '</span> <br /> <span>Item Name: ' + item.name
			+ '</span> <br /> <span>Description:  ' + item.description
			+ '</span>');

};

/*
 * Function to create buttons dynamically, used to display specific session
 * information @param element element used to display retrieved data
 * 
 * @param sessions amount to sessions in the archive
 */
fb.item.displaySession = function(element, sessions) {
	element.empty();

	var dataViewBlock = $('#dataViewBlock');
	var itemID = fb.getQueryParam('itemID');

	$.each(sessions, function(index, session) {

		var sessionButton = $('<input />', {
			type : 'button',
			value : 'Session ' + session.localIndex,
			id : 'sessionButton',
			class : 'sessionButton',
			on : {
				click : function() {
					fb.session.ajax.updateCurrentSessionData(itemID,
							session.localIndex, dataViewBlock, 'chart');
				}
			}
		});

		var row = $('<p>', {
			html : sessionButton
		});

		element.append(row);
	});

	// Add Button to check all sessions data
	element.append($('<input />', {
		type : 'button',
		value : 'All',
		class : 'session',
		on : {
			click : function() {
				// Configure and update data view element
				fb.configureElement(dataViewBlock, {
					dataSource : 'rest/items/' + itemID + '/data'
				});
				fb.update(dataViewBlock);
			}
		}
	}));
};

/*
 * Function to display item information in tabular form, appended with buttons
 * @param element element used to display retrieved data
 * 
 * @param items items to be retrieved
 */

fb.item.updateItemElementToTable = function(element, items) {
	element.empty();
	$.each(items, function(index, item) {

		var editButton = $('<input />', {
			type : 'button',
			value : 'Edit',
			id : 'edit',
			on : {
				click : function() {

					window.location.href = 'test_EditItem.html?itemID=' + item.id;

				}
			}

		});

		var deleteButton = $('<input />', {
			type : 'button',
			value : 'delete',
			id : 'delete',
			on : {
				click : function() {
					fb.notification('Item Deleted!!', 'info');
					$.ajax({
						url : 'rest/items/' + item.id,
						type : 'DELETE',
						success : function() {
							fb.item.updateItemList();

						},
						error : function(jqxhr, status, error) {
							fb.notification(" An error has occured ", "error");
						}
					});
				}
			}

		});

		var configButton = $('<input />', {
			type : 'button',
			value : 'config',
			id : 'config',
			on : {
				click : function() {

					window.location.href = 'sessionConfig?itemID=' + item.id;
				}
			}

		});

		var itemInfoButton = $('<input />', {
			type : 'button',
			value : 'info',
			id : 'info',
			on : {
				click : function() {

					window.location.href = 'test_ItemInfo.html?itemID=' + item.id;
				}
			}

		});

		var table_row = $('<tr>', {});

		if (!item.ratingEnabled)
			table_row.css({
				background : '#d2dfec'
			});

		var table_cell2 = $('<td>', {
			html : item.name
		});
		var table_cell3 = $('<td>', {
			html : item.description
		});
		var table_cell4 = $('<td>', {
			html : editButton
		});
		var table_cell5 = $('<td>', {
			html : deleteButton
		});
		var table_cell6 = $('<td>', {
			html : configButton
		});
		var table_cell7 = $('<td>', {
			html : itemInfoButton
		});
		table_row.append(table_cell2, table_cell3, table_cell4, table_cell5,
				table_cell6, table_cell7);
		element.append(table_row);
	});
};

/*
 * Function to display item information in tabular form, appended with rate
 * button
 * 
 * @param element element used to display retrieved data
 * 
 * @param items
 * 
 */
fb.item.updateItemElementToTableForRating = function(element, items) {
	element.empty();
	$.each(items, function(index, item) {

		var rateButton = $('<input />', {
			type : 'button',
			value : 'Rate',
			id : 'rate',
			on : {
				click : function() {

					window.location.href = 'test_GiveFeedback.html?itemID='
							+ item.id;

				}
			}

		});

		var table_row = $('<tr>', {});

		var table_cell2 = $('<td>', {
			html : item.name
		});// result.yourDataAttributes
		var table_cell3 = $('<td>', {
			html : item.description
		});
		var table_cell4 = $('<td>', {
			html : rateButton
		});

		table_row.append(table_cell2, table_cell3, table_cell4);
		element.append(table_row);
	});
};

/*
 * Function to update item list and append retrieved items to a table
 * 
 */
fb.item.updateItemList = function() {
	$.getJSON('rest/items', function(data) {
		fb.item.updateItemElementToTable($('#items'), data);
	}).error(function() {
		fb.notification("An error has occurred", "error");
	});
};

/*
 * Function to create new item
 * 
 * @param data
 * 
 * @param next
 */
fb.item.createItem = function(data, next) {
	var jqxhr = $.post(

	'rest/items/', data, next).done(function() {
		fb.notification('<span>Item Created!!</span>', 'info');
	})

	.fail(function() {
		fb.notification("An error has occurred, Check item name!", "error");
	});

};
