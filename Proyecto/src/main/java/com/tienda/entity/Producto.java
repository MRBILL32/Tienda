package com.tienda.entity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;



import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Id;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "producto")
public class Producto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idProd")
    private Integer idProd;

    @Column(name = "nomProd", length = 200, nullable = false)
    @Size(max = 200, message = "Máximo 200 caracteres")
    private String nomProd;

    @Column(name = "marcaProd", length = 50, nullable = false)
    @Size(max = 50, message = "Máximo 50 caracteres")
    private String marcaProd;

    @Column(name = "precioUnit", nullable = false)
    private BigDecimal precioUnit;

    @Column(name = "stock", nullable = false)
    private int stock;

    //Relacion con tabla Categoria
    @ManyToOne
    @JoinColumn(name = "idCate")
    private Categoria categoria;

    // "boolean" Para usar isActivo() 
    @Column(name = "activo", nullable = false)
    private boolean activo = true;

    //Relacion con tabla DetallePedido
    @OneToMany(mappedBy = "producto", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<DetallePedido> detalles = new ArrayList<>();
    
    // Constructor vacío
    public Producto() {}

    // Constructor para registrar un producto nuevo
    public Producto(String nomProd, String marcaProd, BigDecimal precioUnit, int stock, Categoria categoria, Boolean activo) {
        this.nomProd = nomProd;
        this.marcaProd = marcaProd;
        this.precioUnit = precioUnit;
        this.stock = stock;
        this.categoria = categoria;
        this.activo = activo;
    }

    // Constructor sin "activo" (por defecto será true)
    public Producto(String nomProd, String marcaProd, BigDecimal precioUnit, int stock, Categoria categoria) {
        this.nomProd = nomProd;
        this.marcaProd = marcaProd;
        this.precioUnit = precioUnit;
        this.stock = stock;
        this.categoria = categoria;
        this.activo = true;
    }

    // Constructor completo
    public Producto(Integer idProd, String nomProd, String marcaProd, BigDecimal precioUnit, int stock, Categoria categoria, Boolean activo) {
        this.idProd = idProd;
        this.nomProd = nomProd;
        this.marcaProd = marcaProd;
        this.precioUnit = precioUnit;
        this.stock = stock;
        this.categoria = categoria;
        this.activo = activo;
    }

    // Constructor para mostrar nombre y precio
    public Producto(String nomProd, BigDecimal precioUnit) {
        this.nomProd = nomProd;
        this.precioUnit = precioUnit;
    }
}

