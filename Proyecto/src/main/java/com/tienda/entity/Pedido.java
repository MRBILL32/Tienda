package com.tienda.entity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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
@Table(name = "pedido")
public class Pedido {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "idPedido")
	private Integer idPedido;
	
	//Relacion con tabla Usuarios
	@ManyToOne
	@JoinColumn(name = "idUser")
	private Usuario usuario;
	
	@Column(name = "fecha")
	private LocalDate fecha;
	
	@Column(name = "total")
	private BigDecimal total;
	
	@Column(name = "estado", length = 20)
	@Size(max = 20, message="Maximo 20 caracters")
	private String estado = "Pendiente";
	
	//Relacion con tabla DetallePedido
	@OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<DetallePedido> detalles;

	
    // Constructor vacío obligatorio
    public Pedido() {}

    // Constructor para registrar nuevo pedido
    public Pedido(Usuario usuario, LocalDate fecha, BigDecimal total) {
        this.usuario = usuario;
        this.fecha = fecha;
        this.total = total;
        this.estado = "Pendiente";
    }

    // Constructor completo
    public Pedido(Integer idPedido, Usuario usuario, LocalDate fecha, BigDecimal total, String estado) {
        this.idPedido = idPedido;
        this.usuario = usuario;
        this.fecha = fecha;
        this.total = total;
        this.estado = estado;
    }

    // Constructor para vista o resumen
    public Pedido(LocalDate fecha, BigDecimal total) {
        this.fecha = fecha;
        this.total = total;
    }
}
