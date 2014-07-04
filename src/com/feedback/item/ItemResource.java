package com.feedback.item;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.UriInfo;

@Path("/item")
public class ItemResource {

	/**
	 * Find all registered items
	 * 
	 * @return a collection of all registered items
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("list")
	public List<Item> findAll() {
		List<Item> items = new ArrayList<Item>();
		Item item;

		item = new Item();

		item.setItemName("Item1");
		item.setItemDescription("This is item 1");
		item.setRatingEnabled(false);

		items.add(item);

		item = new Item();

		item.setItemName("Item2");
		item.setItemDescription("This is item 2");
		item.setRatingEnabled(true);

		items.add(item);

		return items;
	}

	/**
	 * Create and save a new item
	 * 
	 * @param uriInfo
	 *            query information about the item to create
	 */
	@POST
	@Consumes("application/x-www-form-urlencoded")
	public void saveItem(MultivaluedMap<String, String> formParams) {
		Item item = createItem(formParams);
		System.out.println(item);
	}

	/**
	 * Create an item based on the form params
	 * 
	 * @param uriInfo
	 *            forom params
	 * @return
	 */
	private Item createItem(MultivaluedMap<String, String> formParams) {
		Item item = new Item();
		String itemName = formParams.getFirst("itemName");
		item.setItemName(itemName);

		return item;
	}
}
