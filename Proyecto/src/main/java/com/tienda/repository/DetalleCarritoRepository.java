package com.tienda.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.tienda.entity.DetalleCarrito;

@Repository
public interface DetalleCarritoRepository extends JpaRepository<DetalleCarrito, Integer> {

	@Query("SELECT d FROM DetalleCarrito d WHERE d.carrito.idCarrito = :idCarrito")
	List<DetalleCarrito> listarPorCarrito(Integer idCarrito);

	
	@Query("Select d From DetalleCarrito d where d.carrito.idCarrito = :idCarrito AND d.producto.idProd = :idProd")
	Optional<DetalleCarrito> buscarPorCarritoYProducto(@Param("idCarrito") Integer idCarrito,@Param("idProd") Integer idProd);


	@Query("SELECT d FROM DetalleCarrito d WHERE d.carrito.usuario.idUser = :idUsuario AND d.producto.idProd = :idProducto")
	DetalleCarrito findByUsuarioAndProducto(@Param("idUsuario") int idUsuario, @Param("idProducto") int idProducto);

	@Query("SELECT d FROM DetalleCarrito d WHERE d.carrito.usuario.idUser = :idUsuario")
	List<DetalleCarrito> findByUsuario(@Param("idUsuario") int idUsuario);

	@Modifying
	@Query("DELETE FROM DetalleCarrito dc WHERE dc.carrito.idCarrito = :idCarrito")
	void eliminarPorCarrito(@Param("idCarrito") int idCarrito);

	
	

	    
}
