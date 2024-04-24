package es.deusto.spq.pojo;

import java.util.Date;


public class Renting {
	private static int nextId = 1;
    private int id;
    private String email;
    private String licensePlate;
    private Date startDate;
    private Date endDate;
 
    
    
	public Renting(String email, String licensePlate, Date startDate, Date endDate) {
		super();
		this.id = nextId++;
		this.email = email;
		this.licensePlate = licensePlate;
		this.startDate = startDate;
		this.endDate = endDate;
	}



	public int getId() {
		return id;
	}



	public void setId(int id) {
		this.id = id;
	}



	public String getEmail() {
		return email;
	}



	public void setEmail(String email) {
		this.email = email;
	}



	public String getLicensePlate() {
		return licensePlate;
	}



	public void setLicensePlate(String licensePlate) {
		this.licensePlate = licensePlate;
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



	@Override
	public String toString() {
		return "Renting [id=" + id + ", email=" + email + ", licensePlate=" + licensePlate + ", startDate=" + startDate
				+ ", endDate=" + endDate + "]";
	}

	
    

}