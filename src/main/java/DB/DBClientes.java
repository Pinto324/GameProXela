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
public class DBClientes extends DB {

    /*public String[] BuscarPorNit(int Nit) throws SQLException{
         try {
            // Inicializar la conexión con PostgreSQL
            Con = new Conexion("lector", "lectorpass"); // Usuario y contraseña para PostgreSQL
            Conn = Con.IniciarConexion(); // Inicia la conexión a PostgreSQL
            // Preparar la consulta usando PreparedStatement
            String query = "SELECT * FROM Usuarios WHERE id = ?";
            stmt = Conn.prepareStatement(query);
            stmt.setInt(1, Nit); // Reemplaza el primer ? con el valor de 'id'
            Rs = stmt.executeQuery();
            if (Rs.next()) {
                return Rs.getInt(3); 
            } else {
                return -1;
            }
        } finally {
            CerrarRecursos();
        }
    }*/
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
            if (tarjeta == 0) {
                stmt.setDate(5, null);
            } else {
                stmt.setDate(5, new java.sql.Date(System.currentTimeMillis()));
            }
            Rs = stmt.executeQuery();
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
                clientes dato = new clientes(Rs.getString("id_cliente"),Rs.getString("nit"),Rs.getString("nombre"),Rs.getString("tipo_tarjeta"),Rs.getString("puntos"),"");
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
}
