/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DB;

import Objetos.productoVenta;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 *
 * @author branp
 */
public class DBProductosSucur extends DB {

    public DBProductosSucur() {
    }

    public ArrayList<String> informacionRellenarSucursal(int sucursal) throws SQLException {
        try {
            // Inicializar la conexión con PostgreSQL
            Con = new Conexion("lector", "lectorpass");
            Conn = Con.IniciarConexion();
            String query = "SELECT * FROM gamerprosc.filtrar_rellenarinfo(?)";
            stmt = Conn.prepareStatement(query);
            stmt.setInt(1, sucursal);
            Rs = stmt.executeQuery();
            ArrayList<String> datos = new ArrayList<>();
            while (Rs.next()) {
                datos.add(Rs.getString("id_ps"));
                datos.add(Rs.getString("nombre"));
            }
            return datos;
        } finally {
            CerrarRecursos();
        }
    }
    
    public ArrayList<String> informacionRellenarSucursalC(int sucursal) throws SQLException {
        try {
            // Inicializar la conexión con PostgreSQL
            Con = new Conexion("lector", "lectorpass");
            Conn = Con.IniciarConexion();
            String query = "SELECT * FROM gamerprosc.filtrar_rellenarinfo(?)";
            stmt = Conn.prepareStatement(query);
            stmt.setInt(1, sucursal);
            Rs = stmt.executeQuery();
            ArrayList<String> datos = new ArrayList<>();
            while (Rs.next()) {
                datos.add(Rs.getString("nombre"));
                datos.add(Rs.getString("precio"));
                datos.add(Rs.getString("stock_bodega"));
                datos.add(Rs.getString("stock_estanteria"));
                datos.add(Rs.getString("pasillo"));
            }
            return datos;
        } finally {
            CerrarRecursos();
        }
    }
    
    public ArrayList<productoVenta> informacionRellenarVentas(int sucursal) throws SQLException {
        try {
            // Inicializar la conexión con PostgreSQL
            Con = new Conexion("lector", "lectorpass");
            Conn = Con.IniciarConexion();
            String query = "SELECT * FROM gamerprosc.filtrar_rellenarinfo(?)";
            stmt = Conn.prepareStatement(query);
            stmt.setInt(1, sucursal);
            Rs = stmt.executeQuery();
            ArrayList<productoVenta> info = new ArrayList<>();
            while (Rs.next()) {
                productoVenta datos = new productoVenta(Rs.getInt("id_ps"), Rs.getString("nombre"), Rs.getInt("id_producto"),Rs.getInt("stock_estanteria"), Rs.getDouble("precio"));
                info.add(datos);
            }
            return info;
        } finally {
            CerrarRecursos();
        }
    }

    public void modificarEstanteria(int id, int cantidad, int pasillo) throws SQLException {
        try {
            Con = new Conexion("modificador", "modpass");
            Conn = Con.IniciarConexion();
            String query = "SELECT gamerprosc.actualizarinventario(?,?,?)";
            stmt = Conn.prepareStatement(query);
            stmt.setInt(1, id);
            stmt.setInt(2, cantidad);
            stmt.setInt(3, pasillo);
            Rs = stmt.executeQuery();
        } finally {
            CerrarRecursos();
        }
    }
    
    public void modificarBodega(int id, int cantidad) throws SQLException {
        try {
            Con = new Conexion("modificador", "modpass");
            Conn = Con.IniciarConexion();
            String query = "SELECT gamerprosc.aumentarBodega(?,?)";
            stmt = Conn.prepareStatement(query);
            stmt.setInt(1, id);
            stmt.setInt(2, cantidad);
            Rs = stmt.executeQuery();
        } finally {
            CerrarRecursos();
        }
    }
}
