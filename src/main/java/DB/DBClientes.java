/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DB;

import java.sql.SQLException;

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
}
