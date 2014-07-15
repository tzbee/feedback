var fb = {};

fb.host = 'http://localhost:8080';

/**
 * Get URL query parameter
 * 
 * @param key
 *            the name of parameter
 */
fb.getQueryParam = function(key) {
	key = key.replace(/[*+?^$.\[\]{}()|\\\/]/g, "\\$&"); // escape RegEx meta
	// chars
	var match = location.search
			.match(new RegExp("[?&]" + key + "=([^&]+)(&|$)"));
	return match && decodeURIComponent(match[1].replace(/\+/g, " "));
};
