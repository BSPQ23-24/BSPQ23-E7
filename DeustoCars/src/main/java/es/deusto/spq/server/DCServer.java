package es.deusto.spq.server;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.DELETE;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.Consumes;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import java.util.ArrayList;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import es.deusto.spq.serialization.Customer;
import es.deusto.spq.serialization.Vehicle;


import es.deusto.spq.pojo.CustomerData;
import es.deusto.spq.pojo.VehicleData;
import es.deusto.spq.pojo.Renting;
import java.util.Date;


@Path("/server") // general extension of the path for the server
@Produces(MediaType.APPLICATION_JSON)
public class DCServer {
	protected static final Logger logger = LogManager.getLogger();

	public DCServer() {
		// When adding any pattern or class to connect to the database, instatiate and create the connection here 
	}

	@POST
	@Path("/customers") // extension for the method bellow
	public Response addCustomer(CustomerData customerData) {
		// Receive the info from Client and add the parameters to the function
		String eMail = customerData.geteMail();
		String name = customerData.getName();
		String surname = customerData.getSurname();
		Date birthDate = customerData.getDateOfBirth();
    	CustomerData c1 = new CustomerData(eMail, name, surname, birthDate);
   
		logger.info("Adding customer: " + c1.toString());
		System.out.println("Customer registered.");
		return Response.ok().build();
	}
	@POST
	@Path("/vehicles")
	public Response addVehicle(VehicleData vehicleData) {
		String numberPlate = vehicleData.getNumberPlate();
		String brand = vehicleData.getBrand();
		String model = vehicleData.getModel();

		// Receive the info from Client and add the parameters to the function
		VehicleData v1 = new VehicleData(numberPlate, brand, model);
		logger.info("Adding vehicle: " + v1.toString());
		System.out.println("Vehicle registered.");
		return Response.ok().build();
	}

	@GET
	@Path("/getcustomers")
	public Response getCustomers() {
		// This data will be retrieved from the database
		List<CustomerData> customers = new ArrayList<CustomerData>();
		customers.add(new CustomerData("test@gmail.com", "Billy", "Bob"));
		customers.add(new CustomerData("test2@gmail.com", "Johnny", "Jones"));
		customers.add(new CustomerData("test3@gmail.com", "Evelyn", "Easton"));
		return Response.ok(customers).build();
	}
	@GET
	@Path("/getvehicles")
	public Response getVehicles() {
		// This data will be retrieved from the database
		List<VehicleData> vehicles = new ArrayList<VehicleData>();
		vehicles.add(new VehicleData("9872SLY", "Toyota", "Corolla"));
		vehicles.add(new VehicleData("1234QWR", "Opel", "Corsa"));
		vehicles.add(new VehicleData("5678BNM", "Volkswagen", "Golf"));
		return Response.ok(vehicles).build();
	}

	@GET
	@Path("/getcustomer")
	public Response getCustomer(String eMail) {
		// This data will be retrieved from the database
		CustomerData customer = new CustomerData(eMail, "Billy", "Bob");
		return Response.ok(customer).build();
	}
	
	@GET
	@Path("/getvehicle")
	public Response getVehicle(String numberPlate) {
		// This data will be retrieved from the database
		VehicleData vehicle = new VehicleData(numberPlate, "Toyota", "Corolla");
		return Response.ok(vehicle).build();
	}


	@DELETE
	@Path("/deletevehicle")
	public Response deleteVehicle(String numberPlate) {
		logger.info("Deleting vehicle with number plate: " + numberPlate);
		return Response.ok().build();
	}

	@DELETE
	@Path("/deletecustomer")
	public Response deleteCustomer(String eMail) {
		logger.info("Deleting customer with eMail: " + eMail);
		return Response.ok().build();
	}
	
}