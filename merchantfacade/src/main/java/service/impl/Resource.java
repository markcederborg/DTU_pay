package service.impl;

import dtupay.dto.*;
import service.lib.Factory;
import java.util.concurrent.ExecutionException;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;

@Path("/merchant")
public class Resource {
	Service service = new Factory().getService();

	@GET
	@Produces("application/json")
	@Path("/test")
	public Response test() {
		return Response.ok("Hello from Merchant").build();
	}

	@POST
	@Consumes("application/json")
	@Produces("application/json")
	public Response register(Account account) {
		try {
			String id = service.registerAccount(account).join();
			return Response.ok(id).build();
		} catch (Exception e) {
			return Response.serverError().entity(e.getMessage()).build();
		}
	}

	@DELETE
	@Path("/{id}")
	public Response deregister(@PathParam("id") String id) throws InterruptedException, ExecutionException {
		try {
			service.retireAccount(id).get();
			return Response.ok().encoding(id + " succesfully deleted").build();
		} catch (ExecutionException e) {
			return Response.serverError().entity(e.getMessage()).build();

		}
	}

	@POST
	@Consumes("application/json")
	@Produces("application/json")
	@Path("/payment")
	public Response pay(Payment payment) {
		try {
			Boolean completed = service.pay(payment).join();
			if (completed) {
				System.out.println("Payment failed");
				return Response.ok("Payment succeeded").build();
			} else {
				throw new Exception("Payment failed");
			}
		} catch (Exception e) {

			return Response.serverError().entity(e.getMessage()).build();
		}
	}
}
