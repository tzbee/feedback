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
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;

import com.feedback.beans.ConfigurationException;
import com.feedback.beans.DataSource;
import com.feedback.beans.DataUnit;
import com.feedback.beans.FeedbackConfig;
import com.feedback.beans.FeedbackSession;
import com.feedback.beans.Item;
import com.feedback.beans.Scale;
import com.feedback.beans.ScaleException;
import com.feedback.beans.State;
import com.feedback.dao.FrozenResourceException;
import com.feedback.dao.ItemDAO;
import com.feedback.dao.NoResourceFoundException;
import com.feedback.data.AverageDataProcessingStrategy;
import com.feedback.data.Data;
import com.feedback.data.DataComposite;
import com.feedback.data.DataProcessingStrategy;
import com.feedback.data.SingleAverageDataStrategy;

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
	 * Find all ratable items
	 * 
	 * @return a list of all ratable items
	 */
	@GET
	@Path("ratable")
	@Produces(MediaType.APPLICATION_JSON)
	public List<Item> findAllRatableItems() {
		return this.itemDAO.findAllRatableItems();
	}

	/**
	 * Create and save a new item
	 * 
	 * @param formParams
	 *            form parameters
	 * 
	 * @throws BadRequestException
	 *             if the form parameters are wrong
	 */
	@POST
	@Consumes("application/x-www-form-urlencoded")
	public void saveItem(MultivaluedMap<String, String> formParams)
			throws BadRequestException {
		try {
			this.itemDAO.saveItem(createItem(formParams));
		} catch (EmptyItemNameException e) {
			throw new BadRequestException(e.getMessage());
		}
	}

	/**
	 * Create an item based on the form parameters
	 * 
	 * @param formParams
	 *            form parameters
	 * 
	 * @throws EmptyItemNameException
	 *             if the item name is empty
	 * 
	 * @return The item created
	 */
	private Item createItem(MultivaluedMap<String, String> formParams)
			throws EmptyItemNameException {
		Item item = new Item();

		String itemName = formParams.getFirst(ITEM_NAME_FORM_PARAM);

		if (null == itemName || "".equals(itemName)) {
			throw new EmptyItemNameException();
		}

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
	 * 
	 * @throws NotFoundException
	 *             if no item was found
	 */
	@GET
	@Path("{itemId}")
	@Produces(MediaType.APPLICATION_JSON)
	public Item findItemById(@PathParam("itemId") int itemID) {
		try {
			return this.itemDAO.findItemByID(itemID);
		} catch (NoResourceFoundException e) {
			throw new NotFoundException();
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
	 * 
	 * @throws BadRequestException
	 *             if query form parameters are wrong
	 * 
	 * @throws ForbiddenException
	 *             if the item edited is frozen / deleted
	 * 
	 * @throws NotFoundException
	 *             if the edited item does not exist
	 * 
	 */
	@POST
	@Path("{itemID}")
	@Consumes("application/x-www-form-urlencoded")
	public void editItem(MultivaluedMap<String, String> formParams,
			@PathParam("itemID") int itemID) throws BadRequestException,
			NotFoundException, ForbiddenException {
		Item newItem;
		try {
			newItem = createItem(formParams);
			this.itemDAO.editItem(itemID, newItem);
		} catch (EmptyItemNameException e) {
			throw new BadRequestException(e.getMessage());
		} catch (NoResourceFoundException e) {
			throw new NotFoundException();
		} catch (FrozenResourceException e) {
			throw new ForbiddenException();
		}
	}

	/**
	 * Delete(Freeze) an item from the system
	 * 
	 * @param itemID
	 *            id of the item to delete
	 * 
	 * @throws NotFoundException
	 *             if no item was found
	 */
	@DELETE
	@Path("{itemID}")
	public void freezeItem(@PathParam("itemID") int itemID) {
		try {
			this.itemDAO.freezeItem(itemID);
		} catch (NoResourceFoundException e) {
			throw new NotFoundException();
		} catch (FrozenResourceException e) {
			throw new ForbiddenException();
		}
	}

	/**
	 * Create a new feedback session for a specific item with the given form
	 * parameters
	 * 
	 * @param itemID
	 *            id of the item to create the feedback session for
	 * @param formParams
	 * 
	 * @throws NotFoundException
	 *             if no item was found
	 * @throws BadRequestException
	 *             The query parameters are malformed
	 */
	@POST
	@Path("{itemID}/sessions")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public void saveFeedbackSession(@PathParam("itemID") int itemID,
			MultivaluedMap<String, String> formParams)
			throws BadRequestException, NotFoundException {
		try {
			FeedbackSession currentFeedbackSession = null;

			try {
				currentFeedbackSession = this.itemDAO
						.getCurrentFeedbackSession(itemID);
			} catch (NoResourceFoundException e) {
				// The session being saved is the first one
				// Ignore
			}
			FeedbackSession feedbackSession = createFeedbackSession(formParams);

			if (null != currentFeedbackSession) {
				feedbackSession.setLocalIndex(currentFeedbackSession
						.getLocalIndex() + 1);
			}

			try {
				this.itemDAO.saveFeedbackSession(itemID, feedbackSession);
			} catch (NoResourceFoundException e) {
				throw new NotFoundException();
			} catch (FrozenResourceException e) {
				throw new ForbiddenException();
			}

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
	 * 
	 * @throws NotFoundException
	 *             if no item was found
	 */
	@GET
	@Path("{itemID}/sessions")
	@Produces(MediaType.APPLICATION_JSON)
	public List<FeedbackSession> getFeedbackSessions(
			@PathParam("itemID") int itemID) {
		try {
			return this.itemDAO.findFeedbackSessionsByItem(itemID);
		} catch (NoResourceFoundException e) {
			throw new NotFoundException();
		}
	}

	/**
	 * Get all frozen feedbacks sessions from a specific item
	 * 
	 * @param itemID
	 *            id of the item
	 * 
	 * @return A list of all frozen feedback sessions from the item
	 * 
	 * @throws NotFoundException
	 *             if no item was found
	 */
	@GET
	@Path("{itemID}/sessions/archive")
	@Produces(MediaType.APPLICATION_JSON)
	public List<FeedbackSession> getFrozenFeedbackSessions(
			@PathParam("itemID") int itemID) {
		return this.itemDAO.getFrozenFeedbackSessions(itemID);
	}

	/**
	 * Get all local indexes for all frozen sessions of an item
	 * 
	 * @param itemID
	 *            id of the item
	 */
	@GET
	@Path("{itemID}/sessions/archive/localIndex")
	@Produces(MediaType.APPLICATION_JSON)
	public List<Integer> getAllFeedbackSessionLocalIndexes(
			@PathParam("itemID") int itemID) {
		return this.itemDAO.getAllFeedbackSessionLocalIndexes(itemID);
	}

	/**
	 * Get a feedback session given its index relative to the item it belongs to
	 * 
	 * @param itemID
	 *            id of the item the feedback session belongs to
	 * @param localSessionIndex
	 *            local index of the session
	 * @return the session object found
	 * 
	 * @throws NotFoundException
	 *             if no item was found
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
		} catch (NoResourceFoundException e) {
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
	 * 
	 * @throws NotFoundException
	 *             if no item was found
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
	 * Get the current feedback session of a particular item
	 * 
	 * @param itemID
	 *            id of the item
	 * @return the Feedback session object found
	 * 
	 * @throws NotFoundException
	 *             if no item was found or no feedback session has opened yet
	 */
	@GET
	@Path("{itemID}/sessions/current")
	@Produces(MediaType.APPLICATION_JSON)
	public FeedbackSession getCurrentFeedbackSession(
			@PathParam("itemID") int itemID) throws NotFoundException {
		FeedbackSession currentFeedbackSession;
		try {
			currentFeedbackSession = this.itemDAO
					.getCurrentFeedbackSession(itemID);
		} catch (NoResourceFoundException e) {
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
	 * 
	 * @throws NotFoundException
	 *             if no item was found or no feedback session has opened yet
	 */
	@GET
	@Path("{itemID}/sessions/current/config")
	@Produces(MediaType.APPLICATION_JSON)
	public FeedbackConfig getCurrentFeedbackSessionConfig(
			@PathParam("itemID") int itemID) throws NotFoundException {
		return getCurrentFeedbackSession(itemID).getFeedbackConfig();
	}

	/**
	 * Freeze the current feedback session of a particular item
	 * 
	 * @param itemID
	 *            id of the item
	 * 
	 * @throws NotFoundException
	 *             if no item was found or no feedback session has opened yet
	 */
	@DELETE
	@Path("{itemID}/sessions/current")
	@Produces(MediaType.APPLICATION_JSON)
	public void freezeCurrentFeedbackSession(@PathParam("itemID") int itemID)
			throws NotFoundException {
		try {
			FeedbackSession feedbackSession = this.itemDAO
					.getCurrentFeedbackSession(itemID);
			this.itemDAO.freezeItem(feedbackSession.getId());
		} catch (NoResourceFoundException e) {
			throw new NotFoundException();
		} catch (FrozenResourceException e) {
			throw new ForbiddenException();
		}
	}

	/**
	 * Evaluate an item
	 * 
	 * @param itemID
	 *            id of the item
	 * 
	 * @throws ForbiddenException
	 *             if the feedback value does not respect the current
	 *             configuration properties or rating is disabled for the item
	 * 
	 * @throws InternalServerErrorException
	 *             if the current feedback scale is malformed
	 * 
	 * @throws NotFoundException
	 *             if no item was found
	 * 
	 */
	@POST
	@Path("{itemID}/rate")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public void rateItem(@PathParam("itemID") int itemID,
			MultivaluedMap<String, String> formParams)
			throws ForbiddenException, InternalServerErrorException,
			NotFoundException, RatingDisabledException {
		final String VALUE_FORM_PARAM = "value";

		String value = formParams.getFirst(VALUE_FORM_PARAM);

		DataUnit feedbackUnit = new DataUnit();
		feedbackUnit.setValue(Integer.valueOf(value));

		try {
			this.itemDAO.rateItem(itemID, feedbackUnit);
		} catch (ConfigurationException | RatingDisabledException
				| FrozenResourceException e) {
			throw new ForbiddenException();
		} catch (ScaleException e) {
			throw new InternalServerErrorException();
		} catch (NoResourceFoundException e) {
			throw new NotFoundException();
		}
	}

	/**
	 * DATA RESOURCES
	 */

	/**
	 * Creates a data strategy from a String key
	 * 
	 * @param str
	 *            The string to create the data strategy from
	 * @return The data strategy object created
	 * @throws NoDataStrategyException
	 *             if no data strategy is mapped to the string
	 */
	private DataProcessingStrategy valueOf(String str)
			throws NoDataStrategyException {

		if (null == str) {
			throw new NoDataStrategyException();
		}

		switch (str) {

		case "average":
			return new AverageDataProcessingStrategy();

		case "singleAverage":
			return new SingleAverageDataStrategy();

		default:
			throw new NoDataStrategyException();
		}
	}

	/**
	 * 
	 * Get the processed data given original data and a strategy to follow
	 * 
	 * @param data
	 *            the original data to process
	 * 
	 * @param strategyKey
	 *            the strategy used to process data
	 * 
	 * @return The processed data
	 */
	private Data getProcessedData(Data data, String strategyKey) {

		try {

			// Get the strategy from the key
			DataProcessingStrategy dataProcessingStrategy = valueOf(strategyKey);

			// Process the data if a data strategy is used
			data = dataProcessingStrategy.process(data);

		} catch (NoDataStrategyException e) {
			// Do nothing if no data processing strategy was found
		}

		return data;
	}

	/**
	 * Get feedback data of the session identified by its local index
	 * 
	 * @param itemID
	 *            id of the item
	 * @param localSessionIndex
	 *            index of the session relative to the item it belongs to
	 * 
	 * @throws NotFoundException
	 *             if no item was found
	 */
	@GET
	@Path("{itemID}/sessions/{localSessionIndex}/data")
	@Produces(MediaType.APPLICATION_JSON)
	public Data getFeedbackUnitsByLocalIndex(@PathParam("itemID") int itemID,
			@PathParam("localSessionIndex") int localSessionIndex,
			@QueryParam("strategy") String strategy) throws NotFoundException {

		// Get the data from the feedback session of this index
		Data data = getFeedbackSessionByLocalIndex(itemID, localSessionIndex)
				.getData();

		return getProcessedData(data, strategy);
	}

	/**
	 * Get only the data of the current feedback session of an item
	 * 
	 * @param itemID
	 *            id of the item
	 * @return The Feedback data object belonging to the current feedback
	 *         session
	 * 
	 * @throws NotFoundException
	 *             if no item was found or no feedback session has opened yet
	 */
	@GET
	@Path("{itemID}/sessions/current/data")
	@Produces(MediaType.APPLICATION_JSON)
	public Data getCurrentFeedbackSessionData(@PathParam("itemID") int itemID,
			@QueryParam("strategy") String strategy) throws NotFoundException {

		// Get the data from the current feedback sessions
		Data data = getCurrentFeedbackSession(itemID).getData();

		return getProcessedData(data, strategy);
	}

	/**
	 * Get data from the whole item
	 * 
	 * @param itemID
	 *            id of the item
	 * @param strategy
	 *            strategy to use on the data
	 * @return The data object belonging to the item
	 */
	@GET
	@Path("{itemID}/data")
	@Produces(MediaType.APPLICATION_JSON)
	public Data getItemData(@PathParam("itemID") int itemID,
			@QueryParam("strategy") String strategy) {

		// Get data from the found item
		Data data = findItemById(itemID).getData();

		return getProcessedData(data, strategy);
	}

	/**
	 * Get data from all active items
	 * 
	 * @param strategyKey
	 *            strategy to use on each item data
	 * 
	 * @return The data object created
	 */
	@GET
	@Path("data")
	@Produces(MediaType.APPLICATION_JSON)
	public Data getApplicationData(@QueryParam("strategy") String strategyKey) {

		// Create empty data
		DataComposite data = new DataComposite();

		// Find all active items
		List<Item> activeItems = findAllActive();

		Data itemData;

		for (Item activeItem : activeItems) {

			// Get the data from the item
			itemData = activeItem.getData();

			// Process data
			itemData = getProcessedData(itemData, strategyKey);

			// Tag the data for each item
			for (DataUnit dataUnit : itemData.getDataUnits()) {
				dataUnit.setTag(activeItem.getName());
			}

			data.addData(itemData);
		}

		return data;
	}
}
