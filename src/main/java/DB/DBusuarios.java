/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DB;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author branp
 */
public class DBusuarios extends DB {

    public DBusuarios() {
    }

    public int BuscarRolPorId(int id) throws SQLException {
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
                String[] info = new String[1];
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

public void InsertarUsuario(String nombre, int rol, String username, String pass, int id_sucursal, int noCaja) throws SQLException {
    try {
        Con = new Conexion("modificador", "modpass");
        Conn = Con.IniciarConexion();
        String query = "SELECT gamerprosc.funcion_insertar_empleados(?,?,?,?,?,?)";
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] valorHashCalculado = digest.digest(pass.getBytes());
        String valorHashCalculadoHex = bytesToHex(valorHashCalculado);
        stmt = Conn.prepareStatement(query);
        stmt.setString(1, nombre);
        stmt.setShort(2, (short) rol);  // Asegúrate que rol sea el tipo correcto
        stmt.setString(3, username);
        stmt.setString(4, valorHashCalculadoHex);
        stmt.setInt(5, id_sucursal);
         String nosCaja = "";
        if (noCaja == -1) {
            stmt.setNull(6, java.sql.Types.SMALLINT);  // Asegúrate de que la función pueda manejar NULL
        } else {
            stmt.setShort(6, (short) noCaja);  // Asegúrate de que noCaja sea del tipo correcto
            nosCaja = ""+noCaja;
        }        
        stmt.executeUpdate(); 
        getQuery(stmt,nombre,""+rol,username,valorHashCalculadoHex,""+id_sucursal,nosCaja);
    } catch (NoSuchAlgorithmException ex) {
        Logger.getLogger(DBusuarios.class.getName()).log(Level.SEVERE, null, ex);
    } finally {
        CerrarRecursos();
    }
}


    public void getQuery(PreparedStatement stmt, String nombre, String rol, String username, String pass, String id_sucursal, String noCaja) throws SQLException {
        String sql = stmt.toString();
        String query = sql.substring(sql.indexOf(":") + 1).trim();
        query = query.replace("?", "'" + nombre + "'")
                                  .replace("?", rol)
                                  .replace("?", "'" + username + "'")
                                  .replace("?", "'" + pass + "'")
                                  .replace("?", id_sucursal)
                                  .replace("?", noCaja);
        System.out.println(query);
    }
}
