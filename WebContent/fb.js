var fb = {};

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
	$.getJSON('http://localhost:8080/Feedback/rest/items', function(data) {
		fb.updateItemElement($('#items'), data);
	});
};

fb.createItem = function(data, next) {
	$.post('http://localhost:8080/Feedback/rest/items/', data, next);
};

fb.deleteItem = function(itemID, next) {
	$.ajax({
		url : 'http://localhost:8080/Feedback/rest/items/' + itemID,
		type : 'DELETE',
		success : next
	});
};

fb.createFeedbackSession = function() {
	$.post('http://localhost:8080/Feedback/rest/items/' + fb.fbsItem.value
			+ '/sessions');
};
