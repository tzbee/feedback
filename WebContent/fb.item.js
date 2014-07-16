fb.item = {};

fb.item.itemName = document.getElementById('itemName');

fb.item.itemDescription = document.getElementById('itemDescription');

fb.item.ratingEnabled = document.getElementById('ratingEnabled');

fb.item.itemToDelete = document.getElementById('itemToDelete');

fb.item.fbsItem = document.getElementById('FBSItem');

fb.item.itemToEdit = document.getElementById('itemToEdit');

fb.item.init = function() {

	fb.item.itemName.value = 'item';
	fb.item.itemDescription.value = 'item';
};

fb.item.updateItemElement = function(element, items) {
	element.empty();
	$.each(items, function(index, item) {
		element.append('Item: ' + '<span id="item' + (index + 1) + '">'
				+ item.id + ' ' + item.name + ' ' + item.description + ' '
				+ item.ratingEnabled + ' ' + item.state + '</span>' + '<br/>');
	});
};

// @Isaac: Code to update item list to table and append with buttons

fb.item.updateItemElementToTable = function(element, items) {
	element.empty();
	$.each(items, function(index, item) {

		var editButton = $('<input />', {
			type : 'button',
			value : 'Edit',
			id : 'edit',
			on : {
				click : function(itemID, next) {
					alert("Item should navigate to edit page");

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

		table_row.append(table_cell1, table_cell2, table_cell3, table_cell4,
				table_cell5);
		element.append(table_row);
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

fb.item.deleteItem = function(itemID, next) {
	$.ajax({
		url : fb.host + '/Feedback/rest/items/' + itemID,
		type : 'DELETE',
		success : next
	});
};

fb.item.createFeedbackSession = function(itemID) {
	$.post(fb.host + '/Feedback/rest/items/' + itemID + '/sessions');
};
