//package es.deusto.spq;
//
//import static org.junit.Assert.*;
//
//import java.util.Date;
//
//import org.junit.Before;
//import org.junit.Test;
//
//import es.deusto.spq.pojo.Renting;
//import es.deusto.spq.pojo.VehicleData;
//
//public class VehicleRentingFormTest {
//
//	private VehicleRentingForm VehicleRentingForm;
//
//    @Before
//    public void setUp() {
//    	VehicleRentingForm = new VehicleRentingForm();
//    }
//
//    @Test
//    public void testVehicleRentingFormWindowOpening() {
//        // Verificar que la ventana esté visible después de crear la instancia
//        assertTrue(VehicleRentingForm.isVisible());
//    }
//
//    @Test
//    public void testCreateRentingWithConstructor() {
//        // Crear un objeto Vehicle llamando al constructor
//        Renting renting = new Renting("@gmail.com", "123ABC", new Date(), new Date());
//
//        // Verificar que los atributos se hayan establecido correctamente
//        assertEquals("@gmail.com", renting.getEmail());
//        assertEquals("123ABC", renting.getLicensePlate());
//        assertEquals(new Date(), renting.getStartDate());
//        assertEquals(new Date(), renting.getEndDate());
//    }
//
//    @Test
//    public void testUpdateDatabase() {
//        // Crear una instancia de VehicleData para simular los datos de un vehículo
//    	Renting renting = new Renting("@gmail.com", "123ABC", new Date(), new Date());
//
//        // Llamar a la función updateDatabase con los datos del vehículo
//        boolean result = VehicleRentingForm.updateDatabase(renting);
//
//        // Verificar si la actualización de la base de datos fue exitosa
//        assertTrue(result);
//    }
//
//}
//
//
