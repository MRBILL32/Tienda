package com.tienda.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.tienda.entity.Carrito;

@Repository
public interface CarritoRepository extends JpaRepository<Carrito, Integer> {

	@Query("SELECT c FROM Carrito c WHERE c.usuario.idUser = :idUser")
	List<Carrito> listarPorUsuario(@Param("idUser") Integer idUser);

	@Query("SELECT c FROM Carrito c ORDER BY c.fechaCreacion DESC")
    List<Carrito> listarTodoOrdenado();
}

