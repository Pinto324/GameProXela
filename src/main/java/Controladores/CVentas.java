/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controladores;

import DB.DBProductosSucur;
import DB.DBVentas;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 *
 * @author branp
 */
public class CVentas {
    private DBVentas db = new DBVentas();

    public CVentas() {
    }
    
    
    public int numeroFactura(){
        try{return db.numeroFactura();}catch(SQLException e){e.printStackTrace();return -1;}  
    }
}
