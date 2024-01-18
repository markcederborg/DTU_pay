package dtupay.service.impl;

import dtupay.service.lib.Factory;
import dtupay.dto.*;
import java.util.concurrent.ExecutionException;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.util.Stack;

@Path("/customer")
public class Resource {
	Service service = new Factory().getService();

	@GET
	@Produces("application/json")
	@Path("/test")
	public Response test() {
		return Response.ok("Hello from Customer").build();
	}

	@POST
	@Consumes("application/json")
	@Produces("application/json")
	@Path("/register")
	public Response register(Account account) {
		try {
			String id = service.register(account).join();
			System.out.println("Created account: " + id);
			return Response.ok(id).build();
		} catch (Exception e) {
			return Response.serverError().entity(e.getMessage()).build();
		}
	}

	@DELETE
	@Path("/retire/{id}")
	public Response retire(@PathParam("id") String id) throws InterruptedException, ExecutionException {
		try {
			service.retireAccount(id).get();
			return Response.ok().encoding(id + " succesfully retired").build();
		} catch (ExecutionException e) {
			return Response.serverError().entity(e.getMessage()).build();
		}
	}

	@GET
	@Produces("application/json")
	@Path("/get-tokens/{id}")
	public Response getTokens(@PathParam("id") String id) throws Exception {
		try {
			Stack<String> tokens = service.getTokens(id).join();
			System.out.println("Tokens: " + tokens);
			return Response.ok(tokens).build();
		} catch (Exception e) {
			return Response.serverError().entity(e.getMessage()).build();
		}
	}
}
