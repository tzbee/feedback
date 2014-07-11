package com.feedback.rest;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;

import com.feedback.beans.FeedbackConfig;
import com.feedback.beans.FeedbackSession;
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
	 * Create a new feedback session for a specific item with the given form
	 * parameters
	 * 
	 * @param itemID
	 *            id of the item to create the feedback session for
	 */
	@POST
	@Path("{itemID}/sessions")
	public void saveSession(@PathParam("itemID") int itemID) {
		this.itemDAO.saveFeedbackSession(itemID, createFeedbackSession());
	}

	/**
	 * Create a feedback session object based on form parameters
	 * 
	 * @param formParams
	 *            form parameters
	 */
	private FeedbackSession createFeedbackSession() {

		// Create the feedback session object
		FeedbackSession feedbackSession = new FeedbackSession();

		// Create the feedbackConfig object
		FeedbackConfig feedbackConfig = new FeedbackConfig();
		feedbackConfig.setRatingEnabled(false);

		feedbackSession.setFeedbackConfig(feedbackConfig);

		return feedbackSession;
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
	public FeedbackSession getCurrentItemFeedbackSession(
			@PathParam("itemID") int itemID) {
		return this.itemDAO.getCurrentFeedbackSession(itemID);
	}
}
