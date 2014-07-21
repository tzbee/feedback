package com.feedback.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.feedback.beans.Item;
import com.feedback.rest.ItemResource;

/**
 * Servlet implementation class ConfigServlet
 */
@WebServlet("/sessionConfig")
public class ConfigServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private static final String CREATE_NEW_SESSION_PAGE = "/test1.html";
	private static final String CLOSE_SESSION_PAGE = "/test2.html";

	private static final String ITEM_ID_PARAM = "itemID";

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		String itemID = request.getParameter(ITEM_ID_PARAM);

		ItemResource itemResource = new ItemResource();

		boolean isItemRatingEnabled;

		try {
			isItemRatingEnabled = itemResource.findItemById(
					Integer.valueOf(itemID)).isRatingEnabled();

			getServletContext().getRequestDispatcher(
					isItemRatingEnabled ? CLOSE_SESSION_PAGE
							: CREATE_NEW_SESSION_PAGE).forward(request,
					response);
		} catch (NumberFormatException e) {
			// TODO handle query param error
		}
	}
}
