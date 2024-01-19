package service.impl;

import dtupay.dto.Payment;
import service.lib.Factory;
import dtupay.dto.Account;

import java.util.*;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;

@Path("/manager")
public class Resource {
	Service service = new Factory().getService();

	@GET
	@Produces("application/json")
	@Path("/test")
	public Response test() {
		return Response.ok("Hello from Manager").build();
	}

	@GET
	@Produces("application/json")
	@Path("/payments")
	public Response getPayments() {
		try {
			List<Payment> report = service.getPayments().join();
			return Response.ok(report).build();
		} catch (Exception e) {
			return Response.serverError().entity(e.getMessage()).build();
		}
	}

	@GET
	@Produces("application/json")
	@Path("/payments/merchant/{id}")
	public Response getMerchantPayments(@PathParam("id") String id) {
		try {
			List<Payment> report = service.getMerchantPayments(id).join();
			return Response.ok(report).build();
		} catch (Exception e) {
			return Response.serverError().entity(e.getMessage()).build();
		}
	}

	@GET
	@Produces("application/json")
	@Path("/payments/customer/{id}")
	public Response getCustomerPayments(@PathParam("id") String id) {
		try {
			List<Payment> report = service.getCustomerPayments(id).join();
			return Response.ok(report).build();
		} catch (Exception e) {
			return Response.serverError().entity(e.getMessage()).build();
		}
	}

	@GET
	@Produces("application/json")
	@Path("/accounts")
	public Response getAccounts() {
		try {
			List<Account> accounts = service.getAccounts().join();
			return Response.ok(accounts).build();
		} catch (Exception e) {
			return Response.serverError().entity(e.getMessage()).build();
		}
	}

	@DELETE
	@Path("/accounts")
	public Response deleteAccounts() {
		try {
			service.deleteAccounts().join();
			return Response.ok().build();
		} catch (Exception e) {
			return Response.serverError().entity(e.getMessage()).build();
		}
	}
}
