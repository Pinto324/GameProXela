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
public class DetalleVentas {
    private int noFactura;
    private int idProducto;
    private String NombreProducto;
    private int cantidad;
    private double precioU;

    public DetalleVentas(int noFactura, int idProducto, String NombreProducto, int cantidad, double precioU) {
        this.noFactura = noFactura;
        this.idProducto = idProducto;
        this.NombreProducto = NombreProducto;
        this.cantidad = cantidad;
        this.precioU = precioU;
    }

    public String getNombreProducto() {
        return NombreProducto;
    }

    public void setNombreProducto(String NombreProducto) {
        this.NombreProducto = NombreProducto;
    }
    
    

    public int getNoFactura() {
        return noFactura;
    }

    public void setNoFactura(int noFactura) {
        this.noFactura = noFactura;
    }

    public int getIdProducto() {
        return idProducto;
    }

    public void setIdProducto(int idProducto) {
        this.idProducto = idProducto;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public double getPrecioU() {
        return precioU;
    }

    public void setPrecioU(double precioU) {
        this.precioU = precioU;
    }
    
}
