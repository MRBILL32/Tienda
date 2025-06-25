package com.tienda.service;

import java.util.List;
import java.util.Optional;

import com.tienda.entity.Carrito;

public interface CarritoService {

	   Carrito guardar(Carrito carrito);
	    List<Carrito> listarPorUsuario(Integer idUser);
	    List<Carrito> listarTodo();
	    Optional<Carrito> buscarPorId(Integer idCarrito);
	    void eliminarPorId(Integer idCarrito);
}
