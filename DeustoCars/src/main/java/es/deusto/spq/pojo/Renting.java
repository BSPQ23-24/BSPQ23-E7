package es.deusto.spq.pojo;

import java.util.Date;

/**
 * Represents a renting.
 */
public class Renting {
    private static int nextId = 1;
    private int id;
    private String email;
    private String licensePlate;
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
    public Renting(String email, String licensePlate, Date startDate, Date endDate) {
        this.id = nextId++;
        this.email = email;
        this.licensePlate = licensePlate;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    /**
     * Retrieves the ID of the renting.
     *
     * @return The ID of the renting.
     */
    public int getId() {
        return id;
    }

    /**
     * Sets the ID of the renting.
     *
     * @param id The ID to set.
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Retrieves the email associated with the renting.
     *
     * @return The email associated with the renting.
     */
    public String getEmail() {
        return email;
    }

    /**
     * Sets the email associated with the renting.
     *
     * @param email The email to set.
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Retrieves the license plate associated with the renting.
     *
     * @return The license plate associated with the renting.
     */
    public String getLicensePlate() {
        return licensePlate;
    }

    /**
     * Sets the license plate associated with the renting.
     *
     * @param licensePlate The license plate to set.
     */
    public void setLicensePlate(String licensePlate) {
        this.licensePlate = licensePlate;
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
                "id=" + id +
                ", email='" + email + '\'' +
                ", licensePlate='" + licensePlate + '\'' +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                '}';
    }
}
