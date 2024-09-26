/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controladores;

import DB.DBusuarios;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;

/**
 *
 * @author branp
 */
public class CUsuarios {

    private DBusuarios User = new DBusuarios();

    public CUsuarios() {
    }

    //Metodos:
    public String[] ComprobarUsuario(String Nick, String Contraseña) throws SQLException, NoSuchAlgorithmException {
        try {
            return User.comprobarContraseña(Nick, Contraseña); 
            
        } catch (NullPointerException e) {
            
        }
        String[]info = new String[1];
        info[0] = "-3";
        return info;
    }
        public int BuscarRol(int id) throws SQLException, NoSuchAlgorithmException {
        try {
            return User.BuscarRolPorId(id);     
        } catch (NullPointerException e) {
            
        }
        return -3;
    }
        
    public boolean IngresarUsuario(String nombre, int rol, String username, String pass, int id_sucursal, int noCaja){
        try{User.InsertarUsuario(nombre, rol, username, pass, id_sucursal, noCaja);return true;}catch(SQLException ex){ex.printStackTrace();return false;}
    }
    
    
}
