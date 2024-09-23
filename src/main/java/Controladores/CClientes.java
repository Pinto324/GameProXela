/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controladores;

import DB.DBClientes;
import java.sql.SQLException;

/**
 *
 * @author branp
 */
public class CClientes {
    DBClientes db = new DBClientes();
    
    public CClientes() {
    }
    
    public boolean ingeresoClientes(String Nombre, int nit, int tarjeta){
        try{
            db.InsertarCliente(Nombre,nit,tarjeta);
            return true;
        }catch(SQLException e){
            return false;
        }      
    }
}
