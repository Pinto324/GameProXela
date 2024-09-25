/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Objetos;

/**
 *
 * @author branp
 */
public class productoVenta {
    private int id;
    private String nombre;
    private int id_producto;
    private int stock_estanteria;
    private double precio;

    public productoVenta(int id, String nombre, int id_producto, int stock_estanteria, double precio) {
        this.id = id;
        this.nombre = nombre;
        this.id_producto = id_producto;
        this.stock_estanteria = stock_estanteria;
        this.precio = precio;
    }

    public int getId() {
        return id;
    }

    public int getId_producto() {
        return id_producto;
    }

    public void setId_producto(int id_producto) {
        this.id_producto = id_producto;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getStock_estanteria() {
        return stock_estanteria;
    }

    public void setStock_estanteria(int stock_estanteria) {
        this.stock_estanteria = stock_estanteria;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }
    
}
