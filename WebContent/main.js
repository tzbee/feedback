var itemElements = {
	itemName : document.getElementById('itemName'),
	itemDescription : document.getElementById('itemDescription'),
	ratingEnabled : document.getElementById('ratingEnabled'),
	itemToDelete : document.getElementById('itemToDelete'),
	fbsItem : document.getElementById('FBSItem'),
	itemToEdit : document.getElementById('itemToEdit'),
};

function init() {
	itemElements.itemName.value = 'item';
	itemElements.itemDescription.value = 'item';
}

function updateItemList() {
	$.getJSON('http://localhost:8080/Feedback/rest/items', function(data) {
		$('#items').empty();
		$.each(data, function(index, item) {
			$('#items').append(
					'Item: ' + item.id + ' ' + item.name + ' '
							+ item.description + ' ' + item.ratingEnabled + ' '
							+ item.state + ' ' + '<br/>');
		});
	});
}

function createItem(data, next) {
	$.post('http://localhost:8080/Feedback/rest/items/', data, next);
}

function deleteItem(itemID, next) {
	$.ajax({
		url : 'http://localhost:8080/Feedback/rest/items/' + itemID,
		type : 'DELETE',
		success : next
	});
}

var goCreateFeedbackSession = function() {
	$.post('http://localhost:8080/Feedback/rest/items/'
			+ itemElements.fbsItem.value + '/sessions');
};

var goDelete = function() {
	var itemID = itemElements.itemToDelete.value;
	deleteItem(itemID, updateItemList);
};

var goEdit = function() {
	var itemToEdit = itemElements.itemToEdit.value;
	var ratingEnabled = itemElements.ratingEnabled.checked;

	var data = {
		toEnable : ratingEnabled
	};

	$.post(
			'http://localhost:8080/Feedback/rest/items/' + itemToEdit
					+ '/rating', data, updateItemList);
};

var go = function() {

	var data = {
		itemName : itemElements.itemName.value,
		itemDescription : itemElements.itemDescription.value,
	};

	createItem(data, updateItemList);
};

$(document).ready(function() {
	init();
	updateItemList();
	$('#go').click(go);
	$('#delete').click(goDelete);
	$('#newFBSButton').click(goCreateFeedbackSession);
	$('#editButton').click(goEdit);
});
