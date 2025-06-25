package com.tienda.service;

import java.util.List;
import java.util.Optional;

import com.tienda.entity.DetalleCarrito;

public interface DetalleCarritoService {

	DetalleCarrito guardar(DetalleCarrito detalle);
	
	List<DetalleCarrito> listarPorCarrito(Integer idCarrito);
	
	Optional<DetalleCarrito> buscarPorCarritoYProducto(Integer idCarrito, Integer idProd);
	
	void eliminar(Integer idDetalleCarrito);
}
