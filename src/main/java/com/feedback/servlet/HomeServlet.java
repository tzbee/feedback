package com.feedback.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.NotFoundException;

import com.feedback.beans.User;
import com.feedback.beans.UserAccountType;
import com.feedback.rest.UserResource;

/**
 * Servlet implementation class HomeServlet
 */

public class HomeServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private static final String LOGGED_OUT_HOME_PAGE = "/testLoginPage.html";
	private static final String ADMIN_HOME_PAGE = "/createAccount";
	private static final String OWNER_HOME_PAGE = "/test_ItemCreation.html";
	private static final String USER_HOME_PAGE = "/test_ItemList.html";

	private static UserResource userResource;

	@Override
	public void init() throws ServletException {
		super.init();

		// Create the initial administrator account
		//
		userResource = new UserResource();
		int userKey = userResource.createUserKey("admin", "admin");
		System.out.println(userKey);
		userResource.createUserAccount(userKey);
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		String homePage = LOGGED_OUT_HOME_PAGE;

		try {
			User loggedUser = userResource.getLoggedUser(request);

			UserAccountType accountType = loggedUser.getAccountType();

			switch (accountType) {
			case ADMIN:
				homePage = ADMIN_HOME_PAGE;
				break;
			case OWNER:
				homePage = OWNER_HOME_PAGE;
				break;
			case USER:
				homePage = USER_HOME_PAGE;
				break;

			default:
				break;
			}
		} catch (NotFoundException e) {
			// If no user is logged, do nothing, default home page should
			// be used
		}

		//disableResponseCache(response);

		getServletContext().getRequestDispatcher(homePage).forward(request,
				response);
	}

	private static void disableResponseCache(HttpServletResponse response) {
		// Set to expire far in the past.
		response.setHeader("Expires", "Sat, 6 May 1995 12:00:00 GMT");

		// Set standard HTTP/1.1 no-cache headers.
		response.setHeader("Cache-Control",
				"no-store, no-cache, must-revalidate");

		// Set IE extended HTTP/1.1 no-cache headers (use addHeader).
		response.addHeader("Cache-Control", "post-check=0, pre-check=0");

		// Set standard HTTP/1.0 no-cache header.
		response.setHeader("Pragma", "no-cache");
	}
}
