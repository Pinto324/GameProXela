/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controladores;

import DB.DBProductosSucur;
import DB.DBVentas;
import Objetos.DetalleVentas;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Stack;

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
    
    public boolean IngreasarVenta(int NoFac, int idCliente, int idCajero, int sucursal, double total, int descuento, Stack<DetalleVentas> Carrito){
        return db.InsertarVenta(NoFac, idCliente, idCajero, sucursal,  total,  descuento,Carrito);
    }
}
