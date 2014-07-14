var fb = {};

fb.host = 'http://localhost:8080';

fb.itemName = document.getElementById('itemName');

fb.itemDescription = document.getElementById('itemDescription');

fb.ratingEnabled = document.getElementById('ratingEnabled');

fb.itemToDelete = document.getElementById('itemToDelete');

fb.fbsItem = document.getElementById('FBSItem');

fb.itemToEdit = document.getElementById('itemToEdit');

fb.init = function() {
	fb.itemName.value = 'item';
	fb.itemDescription.value = 'item';
};

fb.updateItemElement = function(element, items) {
	element.empty();
	$.each(items, function(index, item) {
		element.append('Item: ' + '<span id="item' + (index + 1) + '">'
				+ item.id + ' ' + item.name + ' ' + item.description + ' '
				+ item.ratingEnabled + ' ' + item.state + '</span>' + '<br/>');
	});
};

fb.updateItemList = function() {
	$.getJSON(fb.host + '/Feedback/rest/items', function(data) {
		fb.updateItemElement($('#items'), data);
	});
};

fb.createItem = function(data, next) {
	$.post(fb.host + '/Feedback/rest/items/', data, next);
};

fb.deleteItem = function(itemID, next) {
	$.ajax({
		url : fb.host + '/Feedback/rest/items/' + itemID,
		type : 'DELETE',
		success : next
	});
};

fb.createFeedbackSession = function(itemID) {
	$.post(fb.host + '/Feedback/rest/items/' + itemID + '/sessions');
};