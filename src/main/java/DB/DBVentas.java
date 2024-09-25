/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DB;

import Objetos.DetalleVentas;
import Objetos.productoVenta;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Stack;

/**
 *
 * @author branp
 */
public class DBVentas extends DB {

    public DBVentas() {
    }

    public int numeroFactura() throws SQLException {
        try {
            // Inicializar la conexión con PostgreSQL
            Con = new Conexion("lector", "lectorpass");
            Conn = Con.IniciarConexion();
            String query = "SELECT last_value FROM gamerprosc.ventas_no_factura_seq";
            stmt = Conn.prepareStatement(query);
            Rs = stmt.executeQuery();
            Rs.next();
            return Rs.getInt("last_value");          
        } finally {
            CerrarRecursos();
        }
    }
    
    public boolean InsertarVenta(int NoFac, int idCliente, int idCajero, int sucursal, double total, int descuento, Stack<DetalleVentas> Carrito){        
        try {
            Con = new Conexion("modificador", "modpass");
            Conn = Con.IniciarConexion();
            String query = "SELECT gamerprosc.funcion_insertar_ventas(?,?,?,?,?,?);";
            stmt = Conn.prepareStatement(query);
            stmt.setDate(1,  new java.sql.Date(System.currentTimeMillis()));
            stmt.setInt(2,  idCliente);
            stmt.setInt(3,  idCajero);
            stmt.setInt(4,  sucursal);
            stmt.setDouble(5,  total);
            stmt.setInt(6,  descuento);
            Rs = stmt.executeQuery();
            query = "SELECT gamerprosc.funcion_insertar_detalles_de_ventas(?,?,?,?);";
            while(!Carrito.isEmpty()){
                DetalleVentas dv = Carrito.pop();
                stmt = Conn.prepareStatement(query);
                stmt.setInt(1,  NoFac);
                stmt.setInt(2,  dv.getIdProducto());
                stmt.setInt(3,  dv.getCantidad());
                stmt.setDouble(4,  dv.getPrecioU());
                Rs = stmt.executeQuery();
            }   
            CerrarRecursos();
            return true;
        }catch(SQLException e){
            e.printStackTrace();
            EliminadorVenta(NoFac);
            return false;
        } 
    }
    
    public void EliminadorVenta(int NoFac){        
        try {
            Con = new Conexion("modificador", "modpass");
            Conn = Con.IniciarConexion();
            String query = "SELECT gamerprosc.eliminar_venta(?);";
            stmt = Conn.prepareStatement(query);
            stmt.setInt(1,  NoFac);
            Rs = stmt.executeQuery();
            CerrarRecursos();
        }catch(SQLException e){
            e.printStackTrace();
        } 
    }
    
}
