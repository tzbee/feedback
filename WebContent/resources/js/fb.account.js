/**
 * Script handling all account operations
 */

if (!fb) {
	var fb = {};
}

fb.account = {};

(function($, document, window) {

	fb.account.createNewKey = function(mail, accountType) {
		console.log("Key " + mail + " " + accountType + " created");
	};

}(jQuery, document, window));