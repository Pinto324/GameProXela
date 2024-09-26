/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DB;

import Objetos.DetalleVentas;
import Objetos.productoVenta;
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Stack;
import org.postgresql.util.PSQLException;

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
    
    public boolean InsertarVenta(int NoFac, int idCliente, int idCajero, boolean cf ,int sucursal, double total, int descuento, Stack<DetalleVentas> Carrito){        
        try {
            Con = new Conexion("modificador", "modpass");
            Conn = Con.IniciarConexion();
            String query = "SELECT gamerprosc.funcion_insertar_ventas(?,?,?,?,?,?,?);";
            stmt = Conn.prepareStatement(query);
            Date d = new java.sql.Date(System.currentTimeMillis());
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String fechaComoString = sdf.format(d);
            stmt.setDate(1,  new java.sql.Date(System.currentTimeMillis()));
            stmt.setInt(2,  idCliente);
            stmt.setInt(3,  idCajero);
            stmt.setBoolean(4, cf);
            stmt.setInt(5,  sucursal);
            stmt.setBigDecimal(6, BigDecimal.valueOf(total));
            stmt.setInt(7,  descuento);
            Rs = stmt.executeQuery();
            String bool = "FALSE";
            if(cf){bool = "TRUE";}
            getQueryVenta(stmt,fechaComoString, ""+ idCliente, ""+ idCajero, bool,""+ sucursal, ""+ total, ""+ descuento);
            query = "SELECT gamerprosc.funcion_insertar_detalles_de_ventas(?,?,?,?);";
            while(!Carrito.isEmpty()){
                DetalleVentas dv = Carrito.pop();
                stmt = Conn.prepareStatement(query);
                stmt.setInt(1,  NoFac);
                stmt.setInt(2,  dv.getIdProducto());
                stmt.setInt(3,  dv.getCantidad());
                stmt.setBigDecimal(4,  BigDecimal.valueOf(dv.getPrecioU()));
                Rs =stmt.executeQuery();
                getQueryDetalleVenta(stmt, ""+NoFac, ""+dv.getIdProducto(), ""+dv.getCantidad(),""+dv.getPrecioU());
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
    
    public void getQueryDetalleVenta(PreparedStatement stmt, String NoFac, String idProd, String cantidad, String precio) throws SQLException {
        String sql = stmt.toString();
        String query = sql.substring(sql.indexOf(":") + 1).trim();
        query = query.replace("?", NoFac)
                                  .replace("?", idProd)
                                  .replace("?", cantidad)
                                  .replace("?", precio);
        System.out.println(query);
    }
    public void getQueryVenta(PreparedStatement stmt, String Fecha, String idCliente, String idCajero, String cf ,String sucursal, String total, String descuento) throws SQLException {
        String sql = stmt.toString();
        String query = sql.substring(sql.indexOf(":") + 1).trim();
        query = query.replace("?", "'" + Fecha + "'" )
                                  .replace("?", idCliente)
                                  .replace("?", idCajero)
                                  .replace("?", cf)
                                  .replace("?", sucursal)
                                  .replace("?", total)
                                  .replace("?", descuento);
        System.out.println(query);
    }
}
