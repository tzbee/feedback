/*
 * Collation of onclick functions
 */
$(document)
		.ready(
				function() {

					$("#home").click(function() {

						window.location.href = 'home';

					});

					$("#rateItemList").click(function() {

						window.location.href = 'itemList.html';

					});

					$("#itemList").click(function() {

						window.location.href = 'ItemCreation.html';

					});

					var loginButton = '</label> <span>&nbsp;</span><span>&nbsp;</span> <label> <span>&nbsp;</span><input type="button" id="loginButton" class="loginButton" value="Login" />';

					$('.testNav').append(loginButton);

					$("#loginButton").click(function() {
						window.location.href = 'login.html';
					});
				});
