var fb = {};

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

/**
 * Pop up creation and animation functions
 */

/**
 * Create a popup element
 * 
 * @param text
 *            content of the popup
 */
fb.createPopupWindow = function(text) {
	$('body').append($('<div>', {
		html : $('<span>', {
			class : 'popupText',
			html : text
		}),
		class : 'popup'
	}));
};

/**
 * Show an element and hide it after a while
 * 
 * @param element
 *            the element to show
 * 
 * @param speed
 *            speed of the animation
 * 
 * @param duration
 *            the time the element is shown
 */
fb.showPopup = function(element, speed, duration) {
	element.fadeIn(speed, function() {
		setTimeout(function() {
			fb.hidePopup(element, speed);
		}, duration);
	});
};

/**
 * Hide an element
 * 
 * @param element
 *            the element to hide
 * 
 * @param speed
 *            the speed of the animation
 * 
 */
fb.hidePopup = function(element, speed) {
	element.fadeOut(speed, function() {
	});
};
