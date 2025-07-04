package com.tienda.entity;

import lombok.Getter;
import lombok.Setter;

import java.beans.Transient;
import java.io.Serializable;
import java.math.BigDecimal;

import jakarta.persistence.Id;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Getter
@Setter
@Entity
@Table(name = "detalleCarrito")
public class DetalleCarrito implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idDetalleCarrito")
    private Integer idDetalleCarrito;

    @ManyToOne
    @JoinColumn(name = "idCarrito", nullable = false)
    private Carrito carrito;

    @ManyToOne
    @JoinColumn(name = "idProd", nullable = false)
    private Producto producto;

    @Column(name = "cantidad", nullable = false)
    private Integer cantidad;

    @Column(name = "precioUnit", nullable = false, precision = 10, scale = 2)
    private BigDecimal precioUnit;

    @Transient
    public BigDecimal getSubtotal() {
        if (cantidad != null && precioUnit != null) {
            return precioUnit.multiply(BigDecimal.valueOf(cantidad));
        }
        return BigDecimal.ZERO;
    }
}
