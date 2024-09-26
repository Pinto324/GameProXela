/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DB;

import Objetos.clientes;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 *
 * @author branp
 */
public class DBReportes extends DB {

    public DBReportes() {
    }

public ArrayList<String> ReporteDescuento(String FechaI, String FechaF) throws SQLException {
    ArrayList<String> tabla = new ArrayList<>();
    try {
        Con = new Conexion("lector", "lectorpass");
        Conn = Con.IniciarConexion();
        String query = "SELECT * FROM gamerprosc.reporte_descuentos(?, ?)";
        stmt = Conn.prepareStatement(query);
        stmt.setDate(1, java.sql.Date.valueOf(FechaI));
        stmt.setDate(2, java.sql.Date.valueOf(FechaF));
        Rs = stmt.executeQuery();
        // Iterar sobre el ResultSet
        while (Rs.next()) {
            tabla.add(String.valueOf(Rs.getInt("no_factura")));
            tabla.add(Rs.getString("fecha"));
            tabla.add(Rs.getString("nombre_cliente"));
            tabla.add(Rs.getString("nombre"));
            tabla.add(Rs.getBoolean("consumidor") ? "C/F" : "nit");
            tabla.add(Rs.getString("total_sind"));
            tabla.add(Rs.getString("descuento"));
            tabla.add(Rs.getString("total_cond"));
        }
    } finally {
        CerrarRecursos();
    }
    return tabla; // Retorna todas las filas
}


    public ArrayList<String> Reporte10Ventas(String FechaI, String FechaF) throws SQLException {
        ArrayList<String> tabla = new ArrayList();
        try {
            Con = new Conexion("lector", "lectorpass");
            Conn = Con.IniciarConexion();
            String query = "SELECT gamerprosc.top_10_ventas(?, ?)";
            stmt = Conn.prepareStatement(query);
            stmt.setDate(1, java.sql.Date.valueOf(FechaI));
            stmt.setDate(2, java.sql.Date.valueOf(FechaF));
            Rs = stmt.executeQuery();
            while (Rs.next()) {
                tabla.add(String.valueOf(Rs.getInt("no_factura")));
                tabla.add(Rs.getString("fecha"));
                tabla.add(Rs.getString("nombre_cliente"));
                tabla.add(Rs.getString("nombre"));
                if(Rs.getBoolean("consumidor")){tabla.add("C/F");}else{tabla.add("nit");}
                tabla.add(Rs.getString("total_cond"));
            }
        } finally {
            CerrarRecursos();
        }
        return tabla;
    }
}
