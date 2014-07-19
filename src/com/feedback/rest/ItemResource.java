package com.feedback.rest;

import java.util.List;

import javax.ws.rs.BadRequestException;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.ForbiddenException;
import javax.ws.rs.GET;
import javax.ws.rs.InternalServerErrorException;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;

import com.feedback.beans.ConfigurationException;
import com.feedback.beans.FeedbackConfig;
import com.feedback.beans.FeedbackSession;
import com.feedback.beans.FeedbackUnit;
import com.feedback.beans.Item;
import com.feedback.beans.Scale;
import com.feedback.beans.ScaleException;
import com.feedback.beans.State;
import com.feedback.dao.DAOException;
import com.feedback.dao.ItemDAO;
import com.feedback.dao.NoSessionFoundException;

/**
 * Restful service Handling all high level item operations
 */
@Path("items")
public class ItemResource {
	private static final String ITEM_NAME_FORM_PARAM = "itemName";
	private static final String ITEM_DESCRIPTION_FORM_PARAM = "itemDescription";

	private ItemDAO itemDAO = new ItemDAO();

	/**
	 * Find all active registered items
	 * 
	 * @return a collection of all active registered items
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public List<Item> findAllActive() {
		return this.itemDAO.findItemsByState(State.ACTIVE);
	}

	/**
	 * Find all frozen registered items
	 * 
	 * @return a collection of all frozen registered items
	 */

	@GET
	@Path("archive")
	@Produces(MediaType.APPLICATION_JSON)
	public List<Item> findAllFrozen() {
		return this.itemDAO.findItemsByState(State.FROZEN);
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
		try {
			return this.itemDAO.findItemByID(itemID);
		} catch (DAOException e) {
			// TODO exception in case no item found
			return null;
		}
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
	 * Delete(Freeze) an item from the system
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
	 * @param formParams
	 * 
	 * @throws BadRequestException
	 *             The query parameters are malformed
	 */
	@POST
	@Path("{itemID}/sessions")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public void saveFeedbackSession(@PathParam("itemID") int itemID,
			MultivaluedMap<String, String> formParams)
			throws BadRequestException {
		try {
			FeedbackSession currentFeedbackSession = this.itemDAO
					.getCurrentFeedbackSession(itemID);
			FeedbackSession feedbackSession = createFeedbackSession(formParams);

			if (null != currentFeedbackSession) {
				feedbackSession.setLocalIndex(currentFeedbackSession
						.getLocalIndex() + 1);
			}

			this.itemDAO.saveFeedbackSession(itemID, feedbackSession);
		} catch (DAOException e) {
			// TODO exception in case no item found
		} catch (QueryParamException | ScaleException e) {
			throw new BadRequestException();
		}
	}

	/**
	 * Create a feedback session object based on form parameters
	 * 
	 * @param formParams
	 *            form parameters
	 * 
	 * @return The created feedback session
	 * @throws QueryParamException
	 *             the form parameters are malformed
	 * @throws ScaleException
	 *             The feedback scale is malformed
	 */
	private FeedbackSession createFeedbackSession(
			MultivaluedMap<String, String> formParams)
			throws QueryParamException, ScaleException {

		// Create the feedback session object
		FeedbackSession feedbackSession = new FeedbackSession();

		// Create the feedback config object
		FeedbackConfig feedbackConfig = new FeedbackConfig();

		// Create the feedback scale object
		Scale feedbackScale = createFeedbackScale(formParams);

		feedbackConfig.setScale(feedbackScale);
		feedbackSession.setFeedbackConfig(feedbackConfig);

		return feedbackSession;
	}

	/**
	 * Create a feedback scale object based on form parameters
	 * 
	 * @param formParams
	 *            form parameters
	 * 
	 * @return The created feedback scale
	 * @throws QueryParamException
	 *             the form parameters are malformed
	 * @throws ScaleException
	 *             The feedback scale is malformed
	 */
	private Scale createFeedbackScale(MultivaluedMap<String, String> formParams)
			throws QueryParamException, ScaleException {
		int startValue;
		int endValue;
		int interval;

		try {
			startValue = Integer.valueOf(formParams.getFirst("startValue"));
			endValue = Integer.valueOf(formParams.getFirst("endValue"));
			interval = Integer.valueOf(formParams.getFirst("interval"));
		} catch (NumberFormatException e) {
			throw new QueryParamException();
		}

		Scale scale = new Scale();

		if (startValue > endValue || interval < 0) {
			throw new ScaleException();
		}

		scale.setStartValue(startValue);
		scale.setEndValue(endValue);
		scale.setInterval(interval);

		return scale;
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
	public List<FeedbackSession> getFeedbackSessions(
			@PathParam("itemID") int itemID) {
		return this.itemDAO.findFeedbackSessionsByItem(itemID);
	}

	/**
	 * Get a feedback session given it index relative to the item it belongs to
	 * 
	 * @param itemID
	 *            id of the item the feedback session belongs to
	 * @param localSessionIndex
	 *            local index of the session
	 * @return the session object found
	 */
	@GET
	@Path("{itemID}/sessions/{localSessionIndex}")
	@Produces(MediaType.APPLICATION_JSON)
	public FeedbackSession getFeedbackSessionByLocalIndex(
			@PathParam("itemID") int itemID,
			@PathParam("localSessionIndex") int localSessionIndex)
			throws NotFoundException {
		try {
			return this.itemDAO.getFeedbackSessionByLocalIndex(itemID,
					localSessionIndex);
		} catch (NoSessionFoundException e) {
			throw new NotFoundException();
		}
	}

	/**
	 * Get feedback configuration preferences of the session identified by its
	 * local index
	 * 
	 * @param itemID
	 *            id of the item
	 * @param localSessionIndex
	 *            index of the session relative to the item it belongs to
	 */
	@GET
	@Path("{itemID}/sessions/{localSessionIndex}/config")
	@Produces(MediaType.APPLICATION_JSON)
	public FeedbackConfig getFeedbackConfigByLocalIndex(
			@PathParam("itemID") int itemID,
			@PathParam("localSessionIndex") int localSessionIndex)
			throws NotFoundException {
		return getFeedbackSessionByLocalIndex(itemID, localSessionIndex)
				.getFeedbackConfig();
	}

	/**
	 * Get feedback data of the session identified by its local index
	 * 
	 * @param itemID
	 *            id of the item
	 * @param localSessionIndex
	 *            index of the session relative to the item it belongs to
	 */
	@GET
	@Path("{itemID}/sessions/{localSessionIndex}/data")
	@Produces(MediaType.APPLICATION_JSON)
	public List<FeedbackUnit> getFeedbackUnitsByLocalIndex(
			@PathParam("itemID") int itemID,
			@PathParam("localSessionIndex") int localSessionIndex)
			throws NotFoundException {
		return getFeedbackSessionByLocalIndex(itemID, localSessionIndex)
				.getFeedbackUnits();
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
			@PathParam("itemID") int itemID) throws NotFoundException {
		FeedbackSession currentFeedbackSession = this.itemDAO
				.getCurrentFeedbackSession(itemID);
		if (null == currentFeedbackSession) {
			throw new NotFoundException();
		}

		return currentFeedbackSession;
	}

	/**
	 * Get the configuration properties of the current feedback session of an
	 * item
	 * 
	 * @param itemID
	 *            id of the item
	 * @return The Feedback configuration object belonging to the current
	 *         feedback session
	 */
	@GET
	@Path("{itemID}/sessions/current/config")
	@Produces(MediaType.APPLICATION_JSON)
	public FeedbackConfig getCurrentFeedbackSessionConfig(
			@PathParam("itemID") int itemID) throws NotFoundException {
		return getCurrentFeedbackSession(itemID).getFeedbackConfig();
	}

	/**
	 * Get only the data of the current feedback session of an item
	 * 
	 * @param itemID
	 *            id of the item
	 * @return The Feedback data object belonging to the current feedback
	 *         session
	 */
	@GET
	@Path("{itemID}/sessions/current/data")
	@Produces(MediaType.APPLICATION_JSON)
	public List<FeedbackUnit> getCurrentFeedbackSessionData(
			@PathParam("itemID") int itemID) throws NotFoundException {
		return getCurrentFeedbackSession(itemID).getFeedbackUnits();
	}

	/**
	 * Freeze the current feedback session of a particular item
	 * 
	 * @param itemID
	 *            id of the item
	 */
	@DELETE
	@Path("{itemID}/sessions/current")
	@Produces(MediaType.APPLICATION_JSON)
	public void freezeCurrentFeedbackSession(@PathParam("itemID") int itemID)
			throws NotFoundException {
		FeedbackSession feedbackSession = this.itemDAO
				.getCurrentFeedbackSession(itemID);
		this.itemDAO.freezeItem(feedbackSession.getId());
	}

	/**
	 * Evaluate an item
	 * 
	 * @param itemID
	 *            id of the item
	 * 
	 * @throws ForbiddenException
	 *             The feedback value does not respect the current configuration
	 *             properties
	 * 
	 * @throws InternalServerErrorException
	 *             The current feedback scale is malformed
	 */
	@POST
	@Path("{itemID}/rate")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public void rateItem(@PathParam("itemID") int itemID,
			MultivaluedMap<String, String> formParams)
			throws ForbiddenException, InternalServerErrorException {
		final String VALUE_FORM_PARAM = "value";

		String value = formParams.getFirst(VALUE_FORM_PARAM);

		FeedbackUnit feedbackUnit = new FeedbackUnit();
		feedbackUnit.setValue(Integer.valueOf(value));

		try {
			this.itemDAO.rateItem(itemID, feedbackUnit);
		} catch (ConfigurationException e) {
			throw new ForbiddenException();
		} catch (ScaleException e) {
			throw new InternalServerErrorException();
		}
	}
}
