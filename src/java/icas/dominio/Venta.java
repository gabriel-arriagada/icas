/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package icas.dominio;

import java.sql.Date;

/**
 *
 * @author Gabriel
 */
public class Venta {
    
    private int idVenta;        
    private String rutVendedor;        
    private String rutCliente;
    private int idFormaPago;
    private int total;
    private Date fecha;
    private boolean pagada;
    private String horaDeRetiro;
    private boolean tienePedido;
    private String comentario;

    public Venta() {
    }          


    public boolean tienePedido() {
        return tienePedido;
    }

    public void setTienePedido(boolean tienePedido) {
        this.tienePedido = tienePedido;
    }
    
    public int getIdVenta() {
        return idVenta;
    }

    public void setIdVenta(int idVenta) {
        this.idVenta = idVenta;
    }

    public String getRutVendedor() {
        return rutVendedor;
    }

    public void setRutVendedor(String rutVendedor) {
        this.rutVendedor = rutVendedor;
    }

    public String getRutCliente() {
        return rutCliente;
    }

    public void setRutCliente(String rutCliente) {
        this.rutCliente = rutCliente;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public boolean isPagada() {
        return pagada;
    }

    public void setPagada(boolean pagada) {
        this.pagada = pagada;
    }

    public int getIdFormaPago() {
        return idFormaPago;
    }

    public void setIdFormaPago(int idFormaPago) {
        this.idFormaPago = idFormaPago;
    }

    public String getHoraDeRetiro() {
        return horaDeRetiro;
    }

    public void setHoraDeRetiro(String horaDeRetiro) {
        this.horaDeRetiro = horaDeRetiro;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }
    
    
}
