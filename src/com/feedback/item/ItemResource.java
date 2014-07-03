package com.feedback.item;

import java.util.ArrayList;
import java.util.Collection;

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
	public Collection<Item> findAll() {
		Collection<Item> items = new ArrayList<Item>();
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
	 * Create a new item
	 * 
	 * @param uriInfo
	 */
	@POST
	public void createItem(@Context UriInfo uriInfo) {

	}
}
