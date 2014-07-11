package com.feedback.rest;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;

import com.feedback.beans.FeedbackSession;
import com.feedback.beans.FeedbackUnit;
import com.feedback.beans.Item;
import com.feedback.dao.ItemDAO;

/**
 * Restful service Handling all high level item operations
 */
@Path("items")
public class ItemResource {
	private static final String ITEM_NAME_FORM_PARAM = "itemName";
	private static final String ITEM_DESCRIPTION_FORM_PARAM = "itemDescription";

	private ItemDAO itemDAO = new ItemDAO();

	/**
	 * Find all registered items
	 * 
	 * @return a collection of all registered items
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public List<Item> findAll() {
		return this.itemDAO.findAll();
	}

	/**
	 * Create and save a new item
	 * 
	 * @param formParams
	 *            form parameters
	 */
	@POST
	@Consumes("application/x-www-form-urlencoded")
	public void saveItem(MultivaluedMap<String, String> formParams) {
		this.itemDAO.saveItem(createItem(formParams));
	}

	/**
	 * Create an item based on the form parameters
	 * 
	 * @param formParams
	 *            form parameters
	 * 
	 * @return The item created
	 */
	private Item createItem(MultivaluedMap<String, String> formParams) {
		Item item = new Item();

		String itemName = formParams.getFirst(ITEM_NAME_FORM_PARAM);
		String itemDescription = formParams
				.getFirst(ITEM_DESCRIPTION_FORM_PARAM);

		item.setName(itemName);
		item.setDescription(itemDescription);
		item.setRatingEnabled(false);

		return item;
	}

	/**
	 * Find an registered item by its id
	 * 
	 * @param itemId
	 *            id of the item to find
	 * 
	 * @return The item found
	 */
	@GET
	@Path("{itemId}")
	@Produces(MediaType.APPLICATION_JSON)
	public Item findItemById(@PathParam("itemId") int itemID) {
		return this.itemDAO.findItemByID(itemID);
	}

	/**
	 * Edit an item with given form parameters
	 * 
	 * @param formParams
	 *            the form parameters representing the attributes to change
	 *
	 * @param itemID
	 *            the id of the item to edit
	 */
	@POST
	@Path("{itemID}")
	@Consumes("application/x-www-form-urlencoded")
	public void editItem(MultivaluedMap<String, String> formParams,
			@PathParam("itemID") int itemID) {
		Item newItem = createItem(formParams);

		this.itemDAO.editItem(itemID, newItem);
	}

	/**
	 * Delete an item from the system
	 * 
	 * @param itemID
	 *            id of the item to delete
	 */
	@DELETE
	@Path("{itemID}")
	public void freezeItem(@PathParam("itemID") int itemID) {
		this.itemDAO.freezeItem(itemID);
	}

	/**
	 * Create a new feedback session for a specific item
	 * 
	 * @param itemID
	 *            id of the item to create the feedback session for
	 */
	@POST
	@Path("{itemID}/sessions")
	public void createNewSession(@PathParam("itemID") int itemID) {
		FeedbackSession feedbackSession = new FeedbackSession();
		feedbackSession.setName("Default");
		feedbackSession.setDescription("Default");

		this.itemDAO.createFeedbackSession(itemID, feedbackSession);
	}

	/**
	 * Get all feedbacks sessions from a specific item
	 * 
	 * @param itemID
	 *            id of the item
	 * 
	 * @return A list of all feedback sessions from the item
	 */
	@GET
	@Path("{itemID}/sessions")
	@Produces(MediaType.APPLICATION_JSON)
	public List<FeedbackSession> getItemFeedbackSessions(
			@PathParam("itemID") int itemID) {
		return this.itemDAO.findFeedbackSessionsByItem(itemID);
	}

	/**
	 * Get the current feedback session of a particular item
	 * 
	 * @param itemID
	 *            id of the item
	 * @return the Feedback session object found
	 */
	@GET
	@Path("{itemID}/sessions/current")
	@Produces(MediaType.APPLICATION_JSON)
	public FeedbackSession getCurrentFeedbackSession(
			@PathParam("itemID") int itemID) {
		return this.itemDAO.getCurrentFeedbackSession(itemID);
	}

	/**
	 * Delete(Freeze) current item feedback session
	 * 
	 * @param itemID
	 *            id of the item
	 */
	@DELETE
	@Path("{itemID}/sessions/current")
	public void freezeCurrentFeedbackSession(@PathParam("itemID") int itemID) {
		FeedbackSession feedbackSession = getCurrentFeedbackSession(itemID);
		freezeItem(feedbackSession.getId());
	}

	/**
	 * Check if rating is enabled for a specific item
	 * 
	 * @param itemID
	 *            id of the item
	 */
	@GET
	@Path("{itemID}/rating")
	@Produces(MediaType.TEXT_PLAIN)
	public String isRatingEnabled(@PathParam("itemID") int itemID) {
		return String.valueOf(this.itemDAO.isItemRatingEnabled(itemID));
	}

	/**
	 * Edit rating state of an item
	 * 
	 * @param itemID
	 *            id of the item
	 * @param toEnable
	 *            If true, the item rating is to be enabled, if false, it is to
	 *            be disabled
	 */
	@POST
	@Path("{itemID}/ratingconfig")
	public void editRating(@PathParam("itemID") int itemID,
			@FormParam("toEnable") boolean toEnable) {

		// Check if rating is enabled in DB
		boolean ratingEnabled = this.itemDAO.isItemRatingEnabled(itemID);

		// If a change is made in the rating state
		if (ratingEnabled != toEnable) {

			// Edit item rating state
			this.itemDAO.editItemRating(itemID, toEnable);

			// If the rating is to be enabled
			if (toEnable) {
				createNewSession(itemID);
			}
			// If the rating is to be disabled
			else {
				freezeCurrentFeedbackSession(itemID);
			}
		}
	}

	/**
	 * Rate an item with the data in form parameters
	 * 
	 * @param itemID
	 *            id of the item to rate
	 * 
	 * @param formParams
	 *            form data
	 */
	@POST
	@Path("{itemID}/rating")
	public void rate(int itemID, MultivaluedMap<String, String> formParams) {

		// Create a feedback unit with the form data
		FeedbackUnit feedbackUnit = createFeedbackUnit(formParams);

		// TODO Check if FB Unit is valid (Based on configuration)

		// Get item's current feedback session 
		FeedbackSession feedbackSession = getCurrentFeedbackSession(itemID);

		// Save the feedback unit in the DB
		this.itemDAO.createFeedbackUnit(feedbackSession.getId(), feedbackUnit);
	}

	/**
	 * Create a feedback unit with the given form data
	 * 
	 * @param formParams
	 *            form data
	 * @return The feedback unit object created
	 */
	private FeedbackUnit createFeedbackUnit(
			MultivaluedMap<String, String> formParams) {
		// createFeedbackUnit
		return null;
	}
}
