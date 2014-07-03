package com.feedback.item;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/")
public class ItemResource {
	@GET
	@Produces(MediaType.TEXT_PLAIN)
	public String test() {
		return "test";
	}
}
