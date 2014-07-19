fb.item = {};

fb.item.itemName = document.getElementById('itemName');

fb.item.itemDescription = document.getElementById('itemDescription');

fb.item.ratingEnabled = document.getElementById('ratingEnabled');

fb.item.itemToDelete = document.getElementById('itemToDelete');

fb.item.fbsItem = document.getElementById('FBSItem');

fb.item.itemToEdit = document.getElementById('itemToEdit');

fb.item.init = function() {

	fb.item.itemName.value = '';
	fb.item.itemDescription.value = '';
};

fb.item.updateItemElement = function(element, items) {
	element.empty();
	$.each(items, function(index, item) {
		element.append('Item: ' + '<span id="item' + (index + 1) + '">'
				+ item.id + ' ' + item.name + ' ' + item.description + ' '
				+ item.ratingEnabled + ' ' + item.state + '</span>' + '<br/>');
	});
};

fb.item.updateItemElementToTable = function(element, items) {
	element.empty();
	$.each(items, function(index, item) {

		var editButton = $('<input />', {
			type : 'button',
			value : 'Edit',
			id : 'edit',
			on : {
				click : function() {
					alert("Edit Item");
					window.location.href = fb.host
							+ '/Feedback/editItem.html?itemID=' + item.id;

				}
			}

		});

		var deleteButton = $('<input />', {
			type : 'button',
			value : 'delete',
			id : 'delete',
			on : {
				click : function() {
					alert("are you sure you want to delete Item?");
					$.ajax({
						url : fb.host + '/Feedback/rest/items/' + item.id,
						type : 'DELETE',
						success : function() {
							fb.item.updateItemList();

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
					alert("configure?");
					window.location.href = fb.host
					+ '/Feedback/configureItem.html?itemID=' + item.id;
					
				}
			}

		});


		var table_row = $('<tr>', {});
		var table_cell1 = $('<td>', {
			html : item.id
		});
		var table_cell2 = $('<td>', {
			html : item.name
		});// result.yourDataAttributes
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

		table_row.append(table_cell1, table_cell2, table_cell3, table_cell4,
				table_cell5, table_cell6);
		element.append(table_row);
	});
};

fb.item.updateItemElementToTableForRating = function(element, items) {
	element.empty();
	$.each(items, function(index, item) {

		var rateButton = $('<input />', {
			type : 'button',
			value : 'Rate',
			id : 'rate',
			on : {
				click : function() {
					alert("rate Item");
					window.location.href = fb.host
							+ '/Feedback/giveFeedback.html?itemID=' + item.id;

				}
			}

		});


		var table_row = $('<tr>', {});
		var table_cell1 = $('<td>', {
			html : item.id
		});
		var table_cell2 = $('<td>', {
			html : item.name
		});// result.yourDataAttributes
		var table_cell3 = $('<td>', {
			html : item.description
		});
		var table_cell4 = $('<td>', {
			html : rateButton
		});

		table_row.append(table_cell1, table_cell2, table_cell3, table_cell4);
		element.append(table_row);
	});
};

fb.item.updateItemListForRating = function() {
	$.getJSON(fb.host + '/Feedback/rest/items', function(data) {
		fb.item.updateItemElementToTableForRating($('#itemsToRate'), data);
	});
};

fb.item.updateItemList = function() {
	$.getJSON(fb.host + '/Feedback/rest/items', function(data) {
		fb.item.updateItemElementToTable($('#items'), data);
	});
};

fb.item.createItem = function(data, next) {
	$.post(fb.host + '/Feedback/rest/items/', data, next);
};


fb.item.createFeedbackSession = function(itemID) {
	$.post(fb.host + '/Feedback/rest/items/' + itemID + '/sessions');
};
