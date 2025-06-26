package com.tienda.service;

import java.util.List;
import java.util.Optional;

import com.tienda.entity.Carrito;

public interface CarritoService {

	   Carrito guardar(Carrito carrito);
	   
	    List<Carrito> listarPorUsuario(Integer idUser);
	    
	    List<Carrito> listarTodo();
	    
	    Optional<Carrito> buscarPorId(Integer idCarrito);
	    
	    Carrito buscarPorUsuario(int idUser);

	    
	    Carrito obtenerCarritoActivoPorUsuario(int idUsuario);
	    
	    void eliminarPorId(Integer idCarrito);

		void vaciarCarrito(int idCarrito);
}
