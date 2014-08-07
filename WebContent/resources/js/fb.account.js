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

		// Ajax post
		$.post(url, postData, function(responseData) {
			keyContainerElement.empty();

			var accountKey = responseData;
			
			// Create the link
			keyContainerElement.append($('<a>').attr('href', '#').html(
					accountKey).attr('id', 'keyLink').click(function() {

				// Create the account on click
				fb.account.createAccount(accountKey);
				
			}));
		});
	};
	
	/**
	 * Create a new account based on the account key
	 * 
	 * @param accountKey
	 *            the key to use to create the account
	 */
	fb.account.createAccount = function(accountKey) {

		// Rest URL to post on
		var url = 'rest/users/';

		// Data to send
		var postData = {
			userKey : accountKey
		};

		// Ajax post
		$.post(url, postData);

	};

}(jQuery, document, window));