var ItemElements = {
	itemName : document.getElementById('itemName'),
	itemDescription : document.getElementById('itemDescription'),
	ratingEnabled : document.getElementById('ratingEnabled'),
	itemToDelete : document.getElementById('itemToDelete'),
	fbsItem : document.getElementById('FBSItem'),
	itemToEdit : document.getElementById('itemToEdit'),

	init : function() {
		ItemElements.itemName.value = 'item';
		ItemElements.itemDescription.value = 'item';
	},

	updateItemElement : function(element, items) {
		element.empty();
		$.each(items, function(index, item) {
			element.append('Item: ' + '<span id="item' + (index + 1) + '">'
					+ item.id + ' ' + item.name + ' ' + item.description + ' '
					+ item.ratingEnabled + ' ' + item.state + '</span>'
					+ '<br/>');
		});
	},

	updateItemList : function() {
		$.getJSON('http://localhost:8080/Feedback/rest/items', function(data) {
			ItemElements.updateItemElement($('#items'), data);
		});
	},

	createItem : function(data, next) {
		$.post('http://localhost:8080/Feedback/rest/items/', data, next);
	},

	deleteItem : function(itemID, next) {
		$.ajax({
			url : 'http://localhost:8080/Feedback/rest/items/' + itemID,
			type : 'DELETE',
			success : next
		});
	},

	createFeedbackSession : function() {
		$.post('http://localhost:8080/Feedback/rest/items/'
				+ ItemElements.fbsItem.value + '/sessions');
	}
};