package es.deusto.spq.pojo;

import java.util.Date;
import java.text.SimpleDateFormat;


/**
 * Class that represents a renting.
 */

public class Renting {
    private int id;
    private CustomerData customerData;
    private VehicleData vehicleData;
    private Date startDate;
    private Date endDate;
    private String status;
    
    /**
     * Renting class constructor.
     * 
     */
    
    public Renting() {
    
    }
    
    /**
     * Getter and setter methods of the Renting class.
     * 
     */

    public CustomerData getCustomerData() {
        return customerData;
    }

    public void setCustomerData(CustomerData customerData) {
        this.customerData = customerData;
    }


    public VehicleData getVehicleData() {
        return vehicleData;
    }

    public void setVehicleData(VehicleData vehicleData) {
        this.vehicleData = vehicleData;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }



    /**
     * Returns a string representation of the Renting object.
     * 
     */
    
    public String toString() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String formattedStartDate = dateFormat.format(startDate);
        String formattedEndDate = dateFormat.format(endDate);
    
        return "Renting" + 
               "\nID: " + Integer.toString(id) + 
               "\nCustomer: " + customerData.toString() + 
               "\nVehicle: " + vehicleData.toString() + 
               "\nStart date: " + formattedStartDate + 
               "\nEnd date: " + formattedEndDate +
               "\nStatus: " + status;
    }
}

