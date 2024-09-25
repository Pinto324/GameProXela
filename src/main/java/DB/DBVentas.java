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
    
}
