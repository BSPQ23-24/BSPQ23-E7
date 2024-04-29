package es.deusto.spq.server;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.DELETE;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import es.deusto.spq.dao.CustomerDAO;
import es.deusto.spq.dao.VehicleDAO;
import es.deusto.spq.pojo.CustomerData;
import es.deusto.spq.pojo.VehicleData;
import java.util.Date;

/**
 * Represents the server for handling customer and vehicle data.
 */
@Path("/server") // general extension of the path for the server
@Produces(MediaType.APPLICATION_JSON)
public class DCServer {
    protected static final Logger logger = LogManager.getLogger();

    public DCServer() {
        // When adding any pattern or class to connect to the database, instantiate and create the connection here
    }

    @POST
    @Path("/customers") // extension for the method below
    public Response addCustomer(CustomerData customerData) {
        // Receive DTO and create CustomerData
        String eMail = customerData.geteMail();
        String name = customerData.getName();
        String surname = customerData.getSurname();
        Date birthDate = customerData.getDateOfBirth();
        CustomerData c1 = new CustomerData(eMail, name, surname, birthDate);

        CustomerDAO.getInstance().store(c1);
        
        logger.info("Adding customer: " + c1.toString());
        logger.info("Customer registered.");
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
        
        VehicleDAO.getInstance().store(v1);
        
        logger.info("Adding vehicle: " + v1.toString());
        logger.info("Vehicle registered.");
        return Response.ok().build();
    }

    @GET
    @Path("/getcustomers")
    public Response getCustomers() {
        // This data will be retrieved from the database
        List<CustomerData> customers = CustomerDAO.getInstance().findAll();
        return Response.ok(customers).build();
    }

    @GET
    @Path("/getvehicles")
    public Response getVehicles() {
        // This data will be retrieved from the database
    	List<VehicleData> vehicles = VehicleDAO.getInstance().findAll();
        return Response.ok(vehicles).build();
    }

    @GET
    @Path("/getcustomer")
    public Response getCustomer(String eMail) {
        // This data will be retrieved from the database
        CustomerData customer = CustomerDAO.getInstance().find(eMail);
        return Response.ok(customer).build();
    }

    @GET
    @Path("/getvehicle")
    public Response getVehicle(String numberPlate) {
        // This data will be retrieved from the database
    	VehicleData vehicle = VehicleDAO.getInstance().find(numberPlate);
        return Response.ok(vehicle).build();
    }

    @DELETE
    @Path("/deletevehicle")
    public Response deleteVehicle(String numberPlate) {
    	VehicleData vehicleData = new VehicleData(numberPlate, null, null);
        VehicleDAO.getInstance().delete(vehicleData);
        logger.info("Deleting vehicle with number plate: " + numberPlate);
        return Response.ok().build();
    }

    @DELETE
    @Path("/deletecustomer")
    public Response deleteCustomer(String eMail) {
    	CustomerData customerData = new CustomerData(eMail, null, null);
        CustomerDAO.getInstance().delete(customerData);
        logger.info("Deleting customer with eMail: " + eMail);
        return Response.ok().build();
    }
}
