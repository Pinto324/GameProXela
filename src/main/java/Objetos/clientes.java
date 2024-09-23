/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Objetos;

import java.sql.Date;

/**
 *
 * @author branp
 */
public class clientes {
    private String id;  
    private String nit;          
    private String nombre;     
    private String tipoTarjeta;  
    private String puntos;        
    private String fechaTarjeta;  

    public clientes(String id, String nit, String nombre, String tipoTarjeta, String puntos, String fechaTarjeta) {
        this.id = id;
        this.nit = nit;
        this.nombre = nombre;
        this.tipoTarjeta = tipoTarjeta;
        this.puntos = puntos;
        this.fechaTarjeta = fechaTarjeta;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNit() {
        return nit;
    }

    public void setNit(String nit) {
        this.nit = nit;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getTipoTarjeta() {
        return tipoTarjeta;
    }

    public void setTipoTarjeta(String tipoTarjeta) {
        this.tipoTarjeta = tipoTarjeta;
    }

    public String getPuntos() {
        return puntos;
    }

    public void setPuntos(String puntos) {
        this.puntos = puntos;
    }

    public String getFechaTarjeta() {
        return fechaTarjeta;
    }

    public void setFechaTarjeta(String fechaTarjeta) {
        this.fechaTarjeta = fechaTarjeta;
    }
    
}
