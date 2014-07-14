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

fb.item.updateItemList = function() {
	$.getJSON(fb.host + '/Feedback/rest/items', function(data) {
		fb.item.updateItemElement($('#items'), data);
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
