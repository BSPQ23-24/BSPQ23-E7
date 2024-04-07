package es.deusto.spq;

import java.time.LocalDate;

/*
 * Class of the customer
 * */

public class Customer {
    private static int lastID = 0;
    private int ID;
    private String name;
    private String surname;
    private LocalDate dateOfBirth;

    /*
     * Constructor of the customer class
     * */
    public Customer(String name, String surname, LocalDate dateOfBirth) {
        this.ID = ++lastID;
        this.name = name;
        this.surname = surname;
        this.dateOfBirth = dateOfBirth;
    }

    /*
     * Getters and setters for the customer class
     * */
    public int getId() {
        return ID;
    }

    public void setId(int id) {
        this.ID = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

	public void setSurname(String surname) {
        this.surname = surname;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }
    
    /*
     * Returns a string representation of the Customer object.
     * */
    
    @Override
	public String toString() {
		return "Customer [name=" + name + ", surname=" + surname + ", dateOfBirth=" + dateOfBirth + "]";
	}
}