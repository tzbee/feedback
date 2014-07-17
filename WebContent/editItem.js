/**
 * @Isaac repopulate edit item form
 */
$( document ).ready(function() {
	//alert("Form should be populated");
	$.ajax({
		url : fb.host + '/Feedback/rest/items/' + item.id,
		type : 'GET',
		success : function() {
			
			alert("Form should be populated");
			//alert("Form should be populated");
			//this.fb.item.itemName.value = 'Testing';
			//fb.item.populateItemForm();

		}
	});
});

	