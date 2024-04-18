package es.deusto.spq.server;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.Consumes;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import java.util.ArrayList;
import java.util.List;
import java.time.LocalDate;


import es.deusto.spq.serialization.Customer;
import es.deusto.spq.serialization.Vehicle;

@Path("/server")
@Produces(MediaType.APPLICATION_JSON)
public class DCServer {

	public DCServer() {
		// When adding any pattern or class to connect to the database, instatiate and create the connection here 
	}

	@POST
	@Path("/customers")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response addCustomer() {
		// Receive the info from Client and add the parameters to the function
		Customer c1 = new Customer("test@gmail.com", "Billy", "Bob", LocalDate.parse("2002-10-14"));
		System.out.println("Adding client: " + c1.toString());
		return Response.ok().build();
	}
	@POST
	@Path("/vehicles")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response addVehicle() {
		// Receive the info from Client and add the parameters to the function
		Vehicle v1 = new Vehicle("9872SLY", "Toyota", "Corolla");
		System.out.println("Adding vehicle: " + v1.toString());
		return Response.ok().build();
	}

	@GET
	@Path("/getcustomers")
	public List<Customer> getCustomers() {
		// This data will be retrieved from the database
		List<Customer> customers = new ArrayList<Customer>();
		customers.add(new Customer("test@gmail.com", "Billy", "Bob"));
		customers.add(new Customer("test2@gmail.com", "Johnny", "Jones"));
		customers.add(new Customer("test3@gmail.com", "Evelyn", "Easton"));
		return customers;
	}
	@GET
	@Path("/getvehicles")
	public List<Vehicle> getVehicles() {
		// This data will be retrieved from the database
		List<Vehicle> vehicles = new ArrayList<Vehicle>();
		vehicles.add(new Vehicle("9872SLY", "Toyota", "Corolla"));
		vehicles.add(new Vehicle("1234QWR", "Opel", "Corsa"));
		vehicles.add(new Vehicle("5678BNM", "Volkswagen", "Golf"));
		return vehicles;
	}
}