/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import UI.login;

/**
 *
 * @author branp
 */
public class main {
    public static void main(String[] args){
        // Desde acá ejecutamos la parte gráfica
       login ventana = new login();
       ventana.setVisible(true);
    }
}
