package com.feedback.item;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
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
	public void saveItem(@Context UriInfo uriInfo) {
		Item item = createItem(uriInfo);

		// TODO Save item in db

		System.out.println(item);
	}

	/**
	 * Create an item based on the request
	 * 
	 * @param uriInfo
	 *            request context
	 * @return
	 */
	private Item createItem(UriInfo uriInfo) {
		Item item = new Item();
		String itemName = uriInfo.getQueryParameters().getFirst("itemName");
		item.setItemName(itemName);

		return item;
	}

}
