package com.feedback.item;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/items")
public class ItemResource {

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public List<Item> findItems() {
		List<Item> items = new ArrayList<Item>();
		Item item;

		item = new Item();

		item.setItemName("Item1");
		item.setItemDescription("This is item 1");

		items.add(item);

		item = new Item();

		item.setItemName("Item2");
		item.setItemDescription("This is item 2");

		items.add(item);

		return items;
	}

	@GET
	@Path("test")
	@Produces(MediaType.TEXT_HTML)
	public String test() {
		return "<html><head></head><body>hahaha</body></html>";
	}
}
