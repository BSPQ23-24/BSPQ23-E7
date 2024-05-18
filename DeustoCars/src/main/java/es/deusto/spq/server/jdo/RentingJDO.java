package es.deusto.spq.server.jdo;

import java.util.Date;
import javax.jdo.annotations.PersistenceCapable;

/**
 * Represents a renting.
 */
@PersistenceCapable
public class RentingJDO {
    private CustomerJDO customer;
    private VehicleJDO vehicle;
    private Date startDate;
    private Date endDate;

    /**
     * Constructs a renting object with the specified email, license plate, start date, and end date.
     *
     * @param email        The email associated with the renting.
     * @param licensePlate The license plate associated with the renting.
     * @param startDate    The start date of the renting.
     * @param endDate      The end date of the renting.
     */
    public RentingJDO(CustomerJDO customer, VehicleJDO vehicle, Date startDate, Date endDate) {
        this.customer = customer;
        this.vehicle = vehicle;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    /**
     * Retrieves the customer associated with the renting.
     *
     * @return The customer associated with the renting.
     */
    public CustomerJDO getCustomer() {
        return customer;
    }

    /**
     * Sets the customer associated with the renting.
     *
     * @param customer The customer to set.
     */
    public void setCustomer(CustomerJDO customer) {
        this.customer = customer;
    }

    /**
     * Retrieves the vehicle associated with the renting.
     *
     * @return The vehicle associated with the renting.
     */
    public VehicleJDO getVehicle() {
        return vehicle;
    }

    /**
     * Sets the vehicle associated with the renting.
     *
     * @param vehicle The vehicle to set.
     */
    public void setVehicle(VehicleJDO vehicle) {
        this.vehicle = vehicle;
    }

    /**
     * Retrieves the start date of the renting.
     *
     * @return The start date of the renting.
     */
    public Date getStartDate() {
        return startDate;
    }

    /**
     * Sets the start date of the renting.
     *
     * @param startDate The start date to set.
     */
    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    /**
     * Retrieves the end date of the renting.
     *
     * @return The end date of the renting.
     */
    public Date getEndDate() {
        return endDate;
    }

    /**
     * Sets the end date of the renting.
     *
     * @param endDate The end date to set.
     */
    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    /**
     * Returns a string representation of the Renting object.
     *
     * @return A string representation of the Renting object.
     */
    @Override
    public String toString() {
        return "Renting{" +
                ", email='" + customer.geteMail() + '\'' +
                ", licensePlate='" + vehicle.getNumberPlate() + '\'' +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                '}';
    }
}
