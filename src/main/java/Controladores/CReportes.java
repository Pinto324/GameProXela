/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controladores;

import DB.DBClientes;
import DB.DBReportes;
import Objetos.clientes;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 *
 * @author branp
 */
public class CReportes {
    private DBReportes db = new DBReportes();
    
    public CReportes() {
    }
   
    
    public ArrayList<String> ReporteDescuentos(String FechaI, String FechaF){
        try{return db.ReporteDescuento(FechaI, FechaF);}catch(SQLException e){e.printStackTrace();return null;}
    }
    
    public ArrayList<String> ReporteTop10(String FechaI, String FechaF){
        try{return db.Reporte10Ventas(FechaI, FechaF);}catch(SQLException e){e.printStackTrace();return null;}
    }
}
