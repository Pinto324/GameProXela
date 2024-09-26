/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DB;

import Objetos.clientes;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

/**
 *
 * @author branp
 */
public class DBClientes extends DB {

    public void InsertarCliente(String nombre, int nit, int tarjeta) throws SQLException {
        try {
            Con = new Conexion("modificador", "modpass");
            Conn = Con.IniciarConexion();
            String query = "INSERT INTO gamerprosc.clientes(nit,nombre,tipo_tarjeta,puntos,fecha_tarjeta) VALUES (?,?,?,?,?)";
            stmt = Conn.prepareStatement(query);
            stmt.setInt(1, nit);
            stmt.setString(2, nombre);
            stmt.setInt(3, tarjeta);
            stmt.setInt(4, 0);
            Date d = new java.sql.Date(System.currentTimeMillis());
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String fechaComoString = "";
            if (tarjeta == 0) {
                stmt.setDate(5, null);
                fechaComoString = "NULL";
            } else {
                stmt.setDate(5, new java.sql.Date(System.currentTimeMillis()));
                fechaComoString = sdf.format(d);
            }
            stmt.executeUpdate();
            getQuery(stmt, ""+nit, nombre, ""+tarjeta, ""+0,fechaComoString);
        }finally {
            CerrarRecursos();
        }
    }
    
public void modificarCliente(int id, String nombre, int nit, boolean tarjeta) throws SQLException {
    try {
        Con = new Conexion("modificador", "modpass");
        Conn = Con.IniciarConexion();
        String query = tarjeta ?  "SELECT gamerprosc.actualizar_cliente_sin_tarjeta(?, ?, ?, ?, ?)" : "SELECT gamerprosc.actualizar_cliente_con_tarjeta(?, ?, ?)";
        stmt = Conn.prepareStatement(query);        
        stmt.setInt(1, id);
        stmt.setString(2, nombre);
        stmt.setInt(3, nit);        
        if(tarjeta){
            stmt.setShort(4, (short) 1);
            stmt.setDate(5, new java.sql.Date(System.currentTimeMillis()));           
        }
        stmt.executeUpdate();
        
    } finally {
        CerrarRecursos();
    }
}

    
    public ArrayList<clientes> informacionClientes() throws SQLException {
        try {
            Con = new Conexion("lector", "lectorpass");
            Conn = Con.IniciarConexion();
            String query = "SELECT * FROM gamerprosc.vista_modificar_clientes";
            stmt = Conn.prepareStatement(query);
            Rs = stmt.executeQuery();
            ArrayList<clientes> Info = new ArrayList();
            while(Rs.next()){
                clientes dato = new clientes(Rs.getString("id_cliente"),Rs.getString("nit"),Rs.getString("nombre"),Rs.getString("tipo_tarjeta"),"","");
                Info.add(dato);
            }
            CerrarRecursos();
            return Info;
        }finally {
            CerrarRecursos();
        }
    }
    
    public int puntosCliente(int idCliente) throws SQLException {
        try {
            // Inicializar la conexión con PostgreSQL
            Con = new Conexion("lector", "lectorpass");
            Conn = Con.IniciarConexion();
            String query = "SELECT * FROM gamerprosc.puntos_cliente(?)";
            stmt = Conn.prepareStatement(query);
            stmt.setInt(1, idCliente);
            Rs = stmt.executeQuery();
            Rs.next();
            return Rs.getInt("puntos");          
        } finally {
            CerrarRecursos();
        }
    }
    
        public void getQuery(PreparedStatement stmt, String NoFac, String idProd, String cantidad, String precio, String fecha) throws SQLException {
        String sql = stmt.toString();
        String query = sql.substring(sql.indexOf(":") + 1).trim();
        query = query.replace("?", NoFac)
                                  .replace("?", "'" + idProd + "'")
                                  .replace("?", cantidad)
                                  .replace("?", precio)
                                  .replace("?", "'" +fecha+ "'");
        System.out.println(query);
    }
}
