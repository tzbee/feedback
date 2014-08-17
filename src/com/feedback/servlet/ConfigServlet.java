package com.feedback.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.ForbiddenException;

import com.feedback.rest.ItemResource;

/**
 * Servlet implementation class ConfigServlet
 */
@WebServlet("/sessionConfig")
public class ConfigServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private static final String CREATE_NEW_SESSION_PAGE = "createFeedbackSession.html";
	private static final String CLOSE_SESSION_PAGE = "closeFeedbackSession.html";

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
			isItemRatingEnabled = itemResource.findItemById(request,
					Integer.valueOf(itemID)).isRatingEnabled();

			String nextPage = isItemRatingEnabled ? CLOSE_SESSION_PAGE
					: CREATE_NEW_SESSION_PAGE;

			response.sendRedirect(nextPage + "?" + ITEM_ID_PARAM + "=" + itemID);
		} catch (NumberFormatException e) {
			// Send bad request (400) if query itemID is not a number
			response.sendError(400);
		} catch (ForbiddenException e) {
			response.sendError(301);
		}
	}
}
