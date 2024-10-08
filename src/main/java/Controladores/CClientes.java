/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controladores;

import DB.DBClientes;
import Objetos.clientes;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 *
 * @author branp
 */
public class CClientes {
    private DBClientes db = new DBClientes();
    
    public CClientes() {
    }
    
    public boolean ingeresoClientes(String Nombre, int nit, int tarjeta){
        try{
            db.InsertarCliente(Nombre,nit,tarjeta);
            return true;
        }catch(SQLException e){
            e.printStackTrace();
            return false;
        }      
    }
    
    public boolean modificarCliente(int id,String Nombre, int nit, boolean tarjeta){
        try{
            db.modificarCliente(id,Nombre,nit,tarjeta);
            return true;
        }catch(SQLException e){
            return true;
        }      
    }
    
    public ArrayList<clientes> buscarClientes(){
        try{return db.informacionClientes();}catch(SQLException e){e.printStackTrace();return null;}
    }
    public int traerPuntos(int id){
        try{return db.puntosCliente(id);}catch(SQLException e){return 0;}
    }
}
