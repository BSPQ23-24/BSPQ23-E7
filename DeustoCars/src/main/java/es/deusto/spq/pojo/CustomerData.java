package es.deusto.spq.pojo;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Represents customer data.
 */
public class CustomerData {
    private String eMail;
    private String name;
    private String surname;
    private Date dateOfBirth;

    /**
     * Default constructor required for serialization.
     */
    public CustomerData() {
        // Required for serialization
    }

    /**
     * Constructs a customer data object with the specified email, name, surname, and date of birth.
     *
     * @param eMail       The email of the customer.
     * @param name        The name of the customer.
     * @param surname     The surname of the customer.
     * @param dateOfBirth The date of birth of the customer.
     */
    public CustomerData(String eMail, String name, String surname, Date dateOfBirth) {
        this.eMail = eMail;
        this.name = name;
        this.surname = surname;
        this.dateOfBirth = dateOfBirth;
    }

    /**
     * Constructs a customer data object with the specified email, name, and surname.
     *
     * @param eMail   The email of the customer.
     * @param name    The name of the customer.
     * @param surname The surname of the customer.
     */
    public CustomerData(String eMail, String name, String surname) {
        this.eMail = eMail;
        this.name = name;
        this.surname = surname;
    }

    /**
     * Retrieves the email of the customer.
     *
     * @return The email of the customer.
     */
    public String geteMail() {
        return eMail;
    }

    /**
     * Sets the email of the customer.
     *
     * @param eMail The email to set.
     */
    public void seteMail(String eMail) {
        this.eMail = eMail;
    }

    /**
     * Retrieves the name of the customer.
     *
     * @return The name of the customer.
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the customer.
     *
     * @param name The name to set.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Retrieves the surname of the customer.
     *
     * @return The surname of the customer.
     */
    public String getSurname() {
        return surname;
    }

    /**
     * Sets the surname of the customer.
     *
     * @param surname The surname to set.
     */
    public void setSurname(String surname) {
        this.surname = surname;
    }

    /**
     * Retrieves the date of birth of the customer.
     *
     * @return The date of birth of the customer.
     */
    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    /**
     * Sets the date of birth of the customer.
     *
     * @param dateOfBirth The date of birth to set.
     */
    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    /**
     * Returns a string representation of the CustomerData object.
     *
     * @return A string representation of the CustomerData object.
     */
    @Override
    public String toString() {
        return "CustomerData{" +
                "eMail='" + eMail + '\'' +
                ", name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                //", dateOfBirth=" + dateOfBirth +
                '}';
    }
}
