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
import es.deusto.spq.serialization.Customer;
import es.deusto.spq.serialization.Vehicle;
import java.util.stream.Collectors;

@Path("/server")
@Produces(MediaType.APPLICATION_JSON)
public class DCServer {
    protected static final Logger logger = LogManager.getLogger();

    public DCServer() {}

    @POST
    @Path("/customers")
    public Response addCustomer(CustomerData customerData) {
        Customer customer = new Customer(
            customerData.geteMail(), 
            customerData.getName(), 
            customerData.getSurname(), 
            customerData.getDateOfBirth()
        );
        CustomerDAO.getInstance().store(customer);
        
        logger.info("Adding customer: " + customer);
        return Response.ok().entity(customerData).build(); // Respond with the DTO for consistency
    }

    @POST
    @Path("/vehicles")
    public Response addVehicle(VehicleData vehicleData) {
        Vehicle vehicle = new Vehicle(
            vehicleData.getNumberPlate(), 
            vehicleData.getBrand(), 
            vehicleData.getModel()
        );
        VehicleDAO.getInstance().store(vehicle);
        
        logger.info("Adding vehicle: " + vehicle);
        return Response.ok().entity(vehicleData).build(); // Respond with the DTO for consistency
    }

    @GET
    @Path("/getcustomers")
    public Response getCustomers() {
        List<Customer> customers = CustomerDAO.getInstance().findAll();
        List<CustomerData> customerDataList = customers.stream()
            .map(c -> new CustomerData(c.geteMail(), c.getName(), c.getSurname(), c.getDateOfBirth()))
            .collect(Collectors.toList());
        return Response.ok(customerDataList).build();
    }

    @GET
    @Path("/getvehicles")
    public Response getVehicles() {
        List<Vehicle> vehicles = VehicleDAO.getInstance().findAll();
        List<VehicleData> vehicleDataList = vehicles.stream()
            .map(v -> new VehicleData(v.getNumberPlate(), v.getBrand(), v.getModel()))
            .collect(Collectors.toList());
        return Response.ok(vehicleDataList).build();
    }

    @GET
    @Path("/getcustomer")
    public Response getCustomer(String eMail) {
        Customer customer = CustomerDAO.getInstance().find(eMail);
        if (customer != null) {
            CustomerData customerData = new CustomerData(customer.geteMail(), customer.getName(), customer.getSurname(), customer.getDateOfBirth());
            return Response.ok(customerData).build();
        }
        return Response.status(Response.Status.NOT_FOUND).build();
    }

    @GET
    @Path("/getvehicle")
    public Response getVehicle(String numberPlate) {
        Vehicle vehicle = VehicleDAO.getInstance().find(numberPlate);
        if (vehicle != null) {
            VehicleData vehicleData = new VehicleData(vehicle.getNumberPlate(), vehicle.getBrand(), vehicle.getModel());
            return Response.ok(vehicleData).build();
        }
        return Response.status(Response.Status.NOT_FOUND).build();
    }

    @DELETE
    @Path("/deletevehicle")
    public Response deleteVehicle(String numberPlate) {
        VehicleDAO.getInstance().delete(numberPlate); // Assume DAO delete method accepts just the ID now
        logger.info("Deleting vehicle with number plate: " + numberPlate);
        return Response.ok().build();
    }

    @DELETE
    @Path("/deletecustomer")
    public Response deleteCustomer(String eMail) {
        CustomerDAO.getInstance().delete(eMail); // Assume DAO delete method accepts just the ID now
        logger.info("Deleting customer with eMail: " + eMail);
        return Response.ok().build();
    }
}
