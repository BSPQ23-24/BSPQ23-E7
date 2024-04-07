package es.deusto.spq.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import es.deusto.spq.db.resources.*;
import java.time.LocalDate;

public class Database {

    private static Database instance;
    private Connection connection;
    
    private final String url = "jdbc:mysql://localhost:3306/deustoCarsDB";
    private final String user = "spq";
    private final String password = "spq";

    private Database() {
    	try {
            // Connection establishment with the database
            this.connection = DriverManager.getConnection(url, user, password);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /*
     * Database Singleton Pattern
     * 
     **/
    public static Database getInstance() {
        if (instance == null) {
            instance = new Database();
        } else {
            try {
                if (instance.connection.isClosed()) {
                    instance = new Database();
                }
            } catch (SQLException e) {
                System.out.println("Error al verificar el estado de la conexión: " + e.getMessage());
            }
        }
        return instance;
    }
    
    /*
     * Database query method
     * 
     * @param sql Query
     * @param parametros Query parameters
     * 
     **/
    public ResultSet ejecutarConsulta(String sql, Parameter... parametros) {
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
        	for (int i = 0; i < parametros.length; i++) {
                Parameter p = parametros[i];
                switch (p.getTipo()) {
                    case STRING:
                        stmt.setString(i + 1, (String) p.getValor());
                        break;
                    case BOOLEAN:
                        stmt.setBoolean(i + 1, (Boolean) p.getValor());
                        break;
                    case DATE:
                        stmt.setDate(i + 1, java.sql.Date.valueOf((LocalDate) p.getValor()));
                        break;
                    default:
                        throw new IllegalArgumentException("Type not supported: " + p.getTipo());
                }
            }
            return stmt.executeQuery();
        } catch (SQLException e) {
            System.out.println("Error executing query: " + e.getMessage());
            return null;
        }
    }
    
    /*
     * Database update method
     * 
     * @param sql Statement
     * @param parametros Statement parameters
     * 
     **/
    public boolean ejecutarActualizacion(String sql, Parameter... parametros) {
    	try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            for (int i = 0; i < parametros.length; i++) {
                Parameter p = parametros[i];
                switch (p.getTipo()) {
                    case STRING:
                        stmt.setString(i + 1, (String) p.getValor());
                        break;
                    case BOOLEAN:
                        stmt.setBoolean(i + 1, (Boolean) p.getValor());
                        break;
                    case DATE:
                        stmt.setDate(i + 1, java.sql.Date.valueOf((LocalDate) p.getValor()));
                        break;
                    default:
                        throw new IllegalArgumentException("Type not supported: " + p.getTipo());
                }
            }
            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            System.out.println("Error executing update: " + e.getMessage());
            return false;
        }
    }
}
