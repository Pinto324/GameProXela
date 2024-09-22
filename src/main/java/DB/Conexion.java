/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DB;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *
 * @author branp
 */
public class Conexion {
    private String Nick;
    private String Pass;
    private final String URLuser = "jdbc:postgresql://localhost:5433/GamerProXela";
    private ResultSet Rs;
    private Statement s;
    private Connection Conexion;

    public Conexion(String Nick, String Pass) {
        this.Nick = Nick;
        this.Pass = Pass;
    }

    public Conexion() {
        
    }

    public ResultSet getRs() {
        return Rs;
    }

    public void setRs(ResultSet Rs) {
        this.Rs = Rs;
    }

    public Statement getS() {
        return s;
    }

    public void setS(Statement s) {
        this.s = s;
    }

    public Connection getConexion() {
        return Conexion;
    }

    public void setConexion(Connection Conexion) {
        this.Conexion = Conexion;
    }

    // Método para iniciar la conexión
    public Connection IniciarConexion(){
        try{
            // Cargar el driver de PostgreSQL
            Class.forName("org.postgresql.Driver");

            // Establecer la conexión
            Conexion = DriverManager.getConnection(URLuser, Nick, Pass);
            System.out.println("Conexión exitosa a la base de datos PostgreSQL!");
        }catch(SQLException e){
             System.out.println("Error al abrir Conexión: " + e.getMessage());   
        } catch (ClassNotFoundException ex) {
            System.out.println("Error: No se encontró el driver de PostgreSQL.");
        }
        return Conexion;
    }

    // Método para cerrar las conexiones
    public void CerrarConexiones() throws SQLException{
        if (Conexion != null && !Conexion.isClosed()) {
            Conexion.close();
        }
        if (s != null && !s.isClosed()) {
            s.close();
        }
    }
}
