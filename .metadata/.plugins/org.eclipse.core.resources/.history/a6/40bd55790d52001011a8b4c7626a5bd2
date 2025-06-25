package com.tienda.entity;

import java.time.LocalDateTime;
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
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "carrito")
public class Carrito {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idCarrito")
    private Integer idCarrito;

    @ManyToOne
    @JoinColumn(name = "idUser", nullable = false)
    private Usuario usuario;

    @Column(name = "fechaCreacion", nullable = false)
    private LocalDateTime fechaCreacion;

    @PrePersist
    public void generarFecha() {
        this.fechaCreacion = LocalDateTime.now();
    }
    
    //Relacion con tabla DetalleCarrito
    @OneToMany(mappedBy = "carrito", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<DetalleCarrito> detalles = new ArrayList<>();

    // Constructor vacío
    public Carrito() {}

    // Constructor útil para crear un nuevo carrito
    public Carrito(Usuario usuario) {
        this.usuario = usuario;
    }
}

