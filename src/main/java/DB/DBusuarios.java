/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DB;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author branp
 */
public class DBusuarios extends DB {

    public DBusuarios() {
    }

    public int BuscarRolPorId(int id) throws SQLException{
         try {
            // Inicializar la conexión con PostgreSQL
            Con = new Conexion("lector", "lectorpass"); // Usuario y contraseña para PostgreSQL
            Conn = Con.IniciarConexion(); // Inicia la conexión a PostgreSQL
            // Preparar la consulta usando PreparedStatement
            String query = "SELECT * FROM Usuarios WHERE id = ?";
            stmt = Conn.prepareStatement(query);
            stmt.setInt(1, id); // Reemplaza el primer ? con el valor de 'id'
            Rs = stmt.executeQuery();
            if (Rs.next()) {
                return Rs.getInt(3); 
            } else {
                return -1;
            }
        } finally {
            CerrarRecursos();
        }
    }
    
    public String[] comprobarContraseña(String usuario, String contra) throws SQLException, NoSuchAlgorithmException {
        try {
            // Inicializamos la conexión (cambiando para que funcione con PostgreSQL)
            Con = new Conexion("lector", "lectorpass"); // Usuario de lectura (PostgreSQL)
            Conn = Con.IniciarConexion(); // Inicia la conexión a PostgreSQL
            // Usar PreparedStatement para evitar inyección SQL
            String query = "SELECT * FROM gamerprosc.empleados WHERE username = ?";
            stmt = Conn.prepareStatement(query);
            stmt.setString(1, usuario); // Reemplaza el primer ? con el valor de 'usuario'            
            Rs = stmt.executeQuery();
            // Si existe un usuario con ese nombre
            if (Rs.next()) {
                // Obtener el hash almacenado de la contraseña
                String valorHashAlmacenado = Rs.getString("pass");
                // Calcular el hash de la contraseña proporcionada
                MessageDigest digest = MessageDigest.getInstance("SHA-256");
                byte[] valorHashCalculado = digest.digest(contra.getBytes());
                String valorHashCalculadoHex = bytesToHex(valorHashCalculado);
                System.out.println(valorHashCalculadoHex);
                String[] info;
                if (valorHashAlmacenado.equals(valorHashCalculadoHex)) {
                    info = new String[6]; 
                    info[0] = String.valueOf(Rs.getInt("id"));
                    info[1] = Rs.getString("nombre");
                    info[2] = String.valueOf(Rs.getInt("rol"));
                    info[3] = Rs.getString("username");
                    info[4] = Rs.getString("id_sucursal");
                    info[5] = String.valueOf(Rs.getString("no_caja"));
                } else {
                    info = new String[1];
                    info[0] = "-1";
                }
                // Comparar los hashes
                return info;
            } else {
                // Si no se encontró el usuario
                String[]info = new String[1];
                info[0] = "-2";
                return info;
            }
        } finally {
            CerrarRecursos();
        }
    }

    private static String bytesToHex(byte[] bytes) {
        StringBuilder stringBuilder = new StringBuilder();
        for (byte b : bytes) {
            stringBuilder.append(String.format("%02x", b));
        }
        return stringBuilder.toString();
    }
}
