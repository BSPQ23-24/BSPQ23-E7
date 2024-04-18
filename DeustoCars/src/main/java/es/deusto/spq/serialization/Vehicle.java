package es.deusto.spq.serialization;

/**
 * Clase que representa un vehículo.
 */

public class Vehicle {
    private String numberPlate;
    private String brand;
    private String model;
    private boolean readyToBorrow;
    private boolean onRepair;
    
    /**
     * Constructor de la clase Vehicle.
     * 
     */
    
    
    public Vehicle(String numberPlate, String brand,  String model) {
        this.numberPlate = numberPlate;
        this.brand = brand;
        this.model = model;
        this.readyToBorrow = true;
        this.onRepair = false;
    }
    /**
     * Métodos getters y setters de la clase Vehicle.
     * 
     */

    public String getNumberPlate() {
        return numberPlate;
    }

    public void setNumberPlate(String numberPlate) {
        this.numberPlate = numberPlate;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
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
        return "Vehicle" + 
               "\nNumber Plate: " + numberPlate + 
               "\nBrand: " + brand + 
               "\nModel: " + model + 
               "\nReady to Borrow: " + readyToBorrow + 
               "\nOn Repair: " + onRepair;
    }
}

