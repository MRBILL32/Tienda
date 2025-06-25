package com.tienda.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.tienda.entity.DetalleCarrito;

@Repository
public interface DetalleCarritoRepository extends JpaRepository<DetalleCarrito, Integer> {

	@Query("Select d From DetalleCarrito d where d.carrito.idCarrito = :idCarrito")
	List<DetalleCarrito> listarPorCarrito(@Param("idCarrtio") Integer idCarrito);
	
	@Query("Select d From DetalleCarrito d where d.carrito.idCarrito = :idCarrito AND d.producto.idProd = :idProd")
	Optional<DetalleCarrito> buscarPorCarritoYProducto(@Param("idCarrito") Integer idCarrito,@Param("idProd") Integer idProd);

}
