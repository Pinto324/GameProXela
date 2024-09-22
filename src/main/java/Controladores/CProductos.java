/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controladores;
import DB.DBProductosSucur;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.ArrayList;
/**
 *
 * @author branp
 */
public class CProductos {

    private DBProductosSucur db = new DBProductosSucur();
    
    public CProductos() {
    }
    public ArrayList<String> Informacionproductos(int sucursal) throws SQLException{
        return db.informacionRellenarSucursal(sucursal);
    }
}
