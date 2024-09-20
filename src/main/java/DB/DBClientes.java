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
public class DBClientes extends DB{
    
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
}
