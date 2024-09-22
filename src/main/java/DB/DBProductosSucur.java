/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DB;

import java.sql.SQLException;
import java.util.ArrayList;

/**
 *
 * @author branp
 */
public class DBProductosSucur extends DB {

    public DBProductosSucur() {
    }
    
    public ArrayList<String> informacionRellenarSucursal(int sucursal) throws SQLException{
         try {
            // Inicializar la conexión con PostgreSQL
            Con = new Conexion("lector", "lectorpass");
            Conn = Con.IniciarConexion(); 
            String query = "SELECT * FROM gamerprosc.filtrar_rellenarinfo(?)";
            stmt = Conn.prepareStatement(query);
            stmt.setInt(1, sucursal);
            Rs = stmt.executeQuery();
            ArrayList<String> datos = new ArrayList<>();
            while(Rs.next()) {
                datos.add(Rs.getString("id_ps"));
                datos.add(Rs.getString("nombre"));
            }
            return datos; 
        } finally {
            CerrarRecursos();
        }
    }
}
