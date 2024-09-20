/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package UI;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.JTextField;

/**
 *
 * @author branp
 */
public class Utilidades {

    public Utilidades() {
    }
    
    public static void SoloNumeros(JTextField text){
    text.addKeyListener(new KeyAdapter(){
        @Override
        public void keyTyped(KeyEvent e) {
            char c = e.getKeyChar();
            if (!Character.isDigit(c)) {
                e.consume(); 
            }
        }
    });
    }
}
