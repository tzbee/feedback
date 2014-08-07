/**
 * Script handling all account operations for the feedback system
 */

if (!fb) {
	var fb = {};
}

fb.account = {};

(function($, document, window) {

	/**
	 * Create a new account key
	 * 
	 * @param mail
	 *            mail address identifying the account
	 * @param accountType
	 *            account type (user, owner)
	 * 
	 * @param keyContainerElement
	 *            element to display the generated key in
	 */
	fb.account.createNewKey = function(mail, accountType, keyContainerElement) {

		// Rest URL to post on
		var url = 'rest/users/key';

		// Data to send
		var postData = {
			mail : mail,
			accountType : accountType
		};

		$.post(url, postData, function(responseData) {
			keyContainerElement.empty();
			keyContainerElement.append(responseData);
		});

	};
}(jQuery, document, window));