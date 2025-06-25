package com.tienda.entity;

import java.io.Serializable;
import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "detallePedido")
public class DetallePedido implements Serializable{

	private static final long serialVersionUID = 1L;
	
	@EmbeddedId
	private DetallePedidoId id;
	
	@ManyToOne
	@MapsId("idPedido")
	@JoinColumn(name = "idPedido")
	private Pedido pedido;
	
	@ManyToOne
	@MapsId("idProd")
	@JoinColumn(name = "idProd")
	private Producto producto;
	
	@Column(name = "cantidad")
	private int cantidad;
	
	@Column(name = "precioUnit")
	private BigDecimal precioUnit;
	
	public DetallePedido() {
	}
	
	public DetallePedido(Pedido pedido, Producto producto, int cantidad, BigDecimal precioUnit) {
	    this.pedido = pedido;
	    this.producto = producto;
	    this.cantidad = cantidad;
	    this.precioUnit = precioUnit;
	    this.id = new DetallePedidoId(pedido.getIdPedido(), producto.getIdProd());
	}
	
	@Override
	public String toString() {
	    return "DetallePedido{" +
	            "pedidoId=" + pedido.getIdPedido() +
	            ", productoId=" + producto.getIdProd() +
	            ", cantidad=" + cantidad +
	            ", precioUnit=" + precioUnit +
	            '}';
	}

}
