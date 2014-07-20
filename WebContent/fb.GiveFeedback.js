/**
 * repopulate edit item form
 */
$(document).ready(
		function() {
			var self = fb.session.ajax;
			self.loadCurrentSessionInfo(fb.getQueryParam('itemID'), sessionIndex, "#feedbackScale");
			
			
			
			
		});
