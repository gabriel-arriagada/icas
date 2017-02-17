/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package icas.dominio;

import java.sql.Date;

/**
 *
 * @author gabo
 */
public class AutoVenta {
    private String rutVendedor;
    private Date fecha;
    private int idRazon;
    private boolean autoVenta;

    public AutoVenta() {
    }

    public AutoVenta(String rutVendedor) {
        this.rutVendedor = rutVendedor;
    }

    public String getRutVendedor() {
        return rutVendedor;
    }

    public void setRutVendedor(String rutVendedor) {
        this.rutVendedor = rutVendedor;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public int getIdRazon() {
        return idRazon;
    }

    public void setIdRazon(int idRazon) {
        this.idRazon = idRazon;
    }

    public boolean isAutoVenta() {
        return autoVenta;
    }

    public void setAutoVenta(boolean autoVenta) {
        this.autoVenta = autoVenta;
    }
}
