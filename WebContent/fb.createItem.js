/*
 * Create item on button click
 */
$(document).ready(
		function() {
			var self = fb.item;

			self.updateItemList();

			$('#go').click(function() {
				var data = $('#createItemForm').serialize();
				self.createItem(data, self.updateItemList);

			});

			var showDataButton = $('#showDataButton');
			var dataComponent = $('#data');
			dataComponent.hide();

			showDataButton.click(function() {

				if (!dataComponent.is(":visible")) {
					dataComponent.show();
					showDataButton.attr('value', 'Hide Data');

					var dataViewID = 'dataViewBlock', //
					dataSource = 'rest/items/data', //
					dataView = 'barChart', // 
					dataStrategy = 'singleAverage';

					fb.createUpdateBlock(dataComponent, dataViewID,
							[ dataView ], [ dataStrategy ]);

					var dataViewElement = $('#' + dataViewID);

					fb.configureElement(dataViewElement, {
						dataSource : dataSource,
						dataView : dataView,
						dataStrategy : dataStrategy
					});

					fb.update(dataViewElement);
				} else {
					dataComponent.hide();
					showDataButton.attr('value', 'Show Data');
				}
			});
		});
