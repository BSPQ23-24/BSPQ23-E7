/**
 * Clase que representa un vehículo.
 */

public class Vehicle {
    private static int lastID = 0;
    private int ID;
    private String brand;
    private String numberPlate;
    private String model;
    private boolean readyToBorrow;
    private boolean onRepair;
    
    /**
     * Constructor de la clase Vehicle.
     * 
     */
    
    public Vehicle(String brand, String numberPlate, String model) {
        this.ID = lastID++;
        this.brand = brand;
        this.numberPlate = numberPlate;
        this.model = model;
        this.readyToBorrow = true;
        this.onRepair = false;
    }
    
    
    /**
     * Métodos getters y setters de la clase Vehicle.
     * 
     */

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getNumberPlate() {
        return numberPlate;
    }

    public void setNumberPlate(String numberPlate) {
        this.numberPlate = numberPlate;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public boolean isReadyToBorrow() {
        return readyToBorrow;
    }

    public void setReadyToBorrow(boolean readyToBorrow) {
        this.readyToBorrow = readyToBorrow;
    }

    public boolean isOnRepair() {
        return onRepair;
    }

    public void setOnRepair(boolean onRepair) {
        this.onRepair = onRepair;
    }
    
    /**
     * Devuelve una representación en forma de cadena del objeto Vehicle.
     * 
     */
    
    public String toString() {
        return "Vehicle ID: " + ID + 
               "\nBrand: " + brand + 
               "\nNumber Plate: " + numberPlate + 
               "\nModel: " + model + 
               "\nReady to Borrow: " + readyToBorrow + 
               "\nOn Repair: " + onRepair;
    }
}
