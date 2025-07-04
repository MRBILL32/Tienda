package com.tienda.repository;

import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.tienda.entity.DetallePedido;
import com.tienda.entity.DetallePedidoId;
import com.tienda.entity.Pedido;

@Repository
public interface DetallePedidoRepository extends JpaRepository< DetallePedido, DetallePedidoId> {

	//busca como detallePedido.pedido.idPedido(compuesta)
		List<DetallePedido> findByPedido_IdPedido(int idPedido);
	
	//Paginar Busqueda con Filtro
		@Query("SELECT d from DetallePedido d WHERE("+
			   "LOWER(d.producto.nomProd) LIKE LOWER(CONCAT('%', :filtro, '%')) OR " +
			    "CAST(d.pedido.idPedido AS string) LIKE CONCAT('%', :filtro, '%')) OR " +
			    "LOWER(d.pedido.usuario.nombres) LIKE LOWER(CONCAT('%', :filtro, '%')) OR " +
			    "LOWER(d.pedido.usuario.apellidos) LIKE LOWER(CONCAT('%', :filtro, '%'))")
		Page<DetallePedido> buscarTodosPorFiltro(@Param("filtro") String filtro, Pageable pageable);
		
	@Modifying
	@Transactional
	@Query("DELETE FROM DetallePedido d WHERE d.pedido = :pedido")
	void deleteByPedido(@Param("pedido") Pedido pedido);

	//Contador Con FIltro
	@Query("SELECT COUNT(d) FROM DetallePedido d WHERE " +
		       "LOWER(d.producto.nomProd) LIKE LOWER(CONCAT('%', :filtro, '%')) OR " +
		       "CAST(d.pedido.idPedido AS string) LIKE CONCAT('%', :filtro, '%') OR " +
		       "LOWER(d.pedido.usuario.nombres) LIKE LOWER(CONCAT('%', :filtro, '%')) OR " +
		       "LOWER(d.pedido.usuario.apellidos) LIKE LOWER(CONCAT('%', :filtro, '%'))")
		long contadorDetallePedido(@Param("filtro") String filtro);

	//paginas busqueda con filtro y usuario
	@Query("SELECT d FROM DetallePedido d WHERE " +
		       "(LOWER(d.producto.nomProd) LIKE LOWER(CONCAT('%', :filtro, '%')) " +
		       "OR CAST(d.pedido.idPedido AS string) LIKE CONCAT('%', :filtro, '%') " +
		       "OR LOWER(d.pedido.usuario.nombres) LIKE LOWER(CONCAT('%', :filtro, '%')) " +
		       "OR LOWER(d.pedido.usuario.apellidos) LIKE LOWER(CONCAT('%', :filtro, '%'))) " +
		       "AND d.pedido.usuario.idUser = :idUser")
		Page<DetallePedido> buscarTodosPorFiltroyUsuario(@Param("filtro") String filtro,
		                                                 @Param("idUser") int idUser,
		                                                 Pageable pageable);




}
