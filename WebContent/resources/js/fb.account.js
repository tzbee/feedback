/**
 * Script handling all account operations
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
	 */
	fb.account.createNewKey = function(mail, accountType) {
		console.log("Key " + mail + " " + accountType + " created");
	};
}(jQuery, document, window));