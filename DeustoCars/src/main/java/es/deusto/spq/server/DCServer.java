/**
 * @file DCServer.java
 * @brief Contains the implementation of the DCServer class.
 */

package es.deusto.spq.server;

import javax.jdo.PersistenceManager;
import javax.jdo.PersistenceManagerFactory;
import javax.jdo.Query;
import javax.jdo.JDOHelper;
import javax.jdo.JDOObjectNotFoundException;
import javax.jdo.Transaction;

import es.deusto.spq.server.jdo.VehicleJDO;
import es.deusto.spq.server.services.CustomerService;
import es.deusto.spq.server.services.RentingService;
import es.deusto.spq.server.services.VehicleService;
import es.deusto.spq.server.jdo.CustomerJDO;
import es.deusto.spq.server.jdo.RentingJDO;
import es.deusto.spq.server.jdo.User;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.DELETE;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import es.deusto.spq.pojo.CustomerAssembler;
import es.deusto.spq.pojo.CustomerData;
import es.deusto.spq.pojo.VehicleAssembler;
import es.deusto.spq.pojo.VehicleData;
import es.deusto.spq.pojo.Renting;
import es.deusto.spq.pojo.RentingAssembler;

/**
 * @class DCServer
 * @brief Represents the server-side implementation for managing customers and vehicles. (Facade)
 */
@Path("/server")
@Produces(MediaType.APPLICATION_JSON)
public class DCServer {

    /**
     * Adds a new customer to the database.
     * @param customerData The data of the customer to be added.
     * @return Response indicating success or failure of the operation.
     */ 
    @POST
    @Path("/addcustomer")
    public Response addCustomer(CustomerData customerData) {
        return CustomerService.getInstance().addCustomer(customerData);
    }

    /**
     * Adds a new vehicle to the database.
     * @param vehicleData The data of the vehicle to be added.
     * @return Response indicating success or failure of the operation.
     */
    @POST
    @Path("/addvehicle")
    public Response addVehicle(VehicleData vehicleData) {   
        return VehicleService.getInstance().addVehicle(vehicleData);
    }


    /**
     * Adds a new Renting to the database.
     * @param renting The data of the Renting to be added.
     * @return Response indicating success or failure of the operation.
     */
    @POST
    @Path("/addrenting")
    public Response addRenting(Renting renting) {   
        return RentingService.getInstance().addRenting(renting);
    }


    /**
     * Returns a rented vehicle.
     * @param numberPlate The number plate of the vehicle to be returned.
     * @param email The email of the customer returning the vehicle.
     * @return Response indicating success or failure of the operation.
     */
    @POST
    @Path("/returnvehicle")
    public Response returnVehicle(@QueryParam("numberPlate") String numberPlate, @QueryParam("eMail") String email) {
        return RentingService.getInstance().returnVehicle(numberPlate, email);
    }
    /**
     * Retrieves all rentings from a customer from the database.
     * @return Response containing the list of rentings of a customer or an error message if no rentings are found.
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/getcustomerrents")
    public Response getCustomerRents(@QueryParam("eMail") String eMail) {
        return RentingService.getInstance().getCustomerRents(eMail);
    }
    
    /**
     * Retrieves all rentings of a vehicle from the database.
     * @return Response containing the list of rentings and customers or an error message if no rentings are found.
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/getvehiclerents")
    public Response getVehicleRents(@QueryParam("numberPlate") String numberPlate) {
    	return RentingService.getInstance().getVehicleRents(numberPlate);
    }
    
    
    /**
     * Retrieves all customers from the database.
     * @return Response containing the list of customers or an error message if no customers are found.
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/getcustomers")
    public Response getCustomers() {
        return CustomerService.getInstance().getCustomers();

    }
    
    /**
     * Retrieves all vehicles from the database.
     * @return Response containing the list of vehicles or an error message if no vehicles are found.
     */ 
    @GET
    @Path("/getvehicles")
    public Response getVehicles() {
        return VehicleService.getInstance().getVehicles();
    }
    
    /**
     * Retrieves a customer by email from the database.
     * @param eMail The email of the customer to retrieve.
     * @return Response containing the customer data or an error message if the customer is not found.
     */
    @GET
    @Path("/getcustomer")
    public Response getCustomer(@QueryParam("eMail") String eMail) {
        return CustomerService.getInstance().getCustomer(eMail);
    }
    
    /**
     * Retrieves a vehicle by number plate from the database.
     * @param numberPlate The number plate of the vehicle to retrieve.
     * @return Response containing the vehicle data or an error message if the vehicle is not found.
     */
    @GET
    @Path("/getvehicle")
    public Response getVehicle(@QueryParam("numberPlate") String numberPlate) {
        return VehicleService.getInstance().getVehicle(numberPlate);

    }
    
    /**
     * Deletes a vehicle from the database.
     * @param email The email of the vehicle to delete.
     * @return Response indicating success or failure of the operation.
     */
    @DELETE
    @Path("/deletevehicle")
    public Response deleteVehicle(@QueryParam("numberPlate") String numberPlate) {
        return VehicleService.getInstance().deleteVehicle(numberPlate);

    }
    
    /**
     * Deletes a customer from the database.
     * @param email The email of the customer to delete.
     * @return Response indicating success or failure of the operation.
     */
    @DELETE
    @Path("/deletecustomer")
    public Response deleteCustomer(@QueryParam("eMail") String email) {
        return CustomerService.getInstance().deleteCustomer(email);

    }


}
