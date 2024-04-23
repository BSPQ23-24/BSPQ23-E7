package es.deusto.spq.serialization;

public class Retrieval {
	private static int nextId = 1;
    private int id;
    private String email;
    private String licensePlate;

    public Retrieval(String email, String licensePlate) {
		super();
		this.id = nextId++;
        this.email = email;
        this.licensePlate = licensePlate;
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

    @Override
    public String toString() {
        return "Retrieval [id=" + id + ", email=" + email + ", licensePlate=" + licensePlate + "]";
    }
}
