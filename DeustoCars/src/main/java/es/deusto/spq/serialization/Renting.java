package es.deusto.spq.serialization;

import java.util.Date;


public class Renting {
	private int id;
    private Customer customer;
    private Vehicle vehicle;
    private Date startDate;
    private Date endDate;
    private String status;
    
    
	public Renting(Customer customer, Vehicle vehicle, Date startDate, Date endDate, String status) {
		super();
		this.customer = customer;
		this.vehicle = vehicle;
		this.startDate = startDate;
		this.endDate = endDate;
		this.status = status;
	}


	public int getId() {
		return id;
	}


	public void setId(int id) {
		this.id = id;
	}


	public Customer getCustomer() {
		return customer;
	}


	public void setCustomer(Customer customer) {
		this.customer = customer;
	}


	public Vehicle getVehicle() {
		return vehicle;
	}


	public void setVehicle(Vehicle vehicle) {
		this.vehicle = vehicle;
	}


	public Date getStartDate() {
		return startDate;
	}


	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}


	public Date getEndDate() {
		return endDate;
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


	@Override
	public String toString() {
		return "Renting [customer=" + customer + ", vehicle=" + vehicle + ", startDate=" + startDate + ", endDate="
				+ endDate + ", status=" + status + "]";
	}
    

}
