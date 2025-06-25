package com.tienda.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.tienda.entity.Pedido;
import com.tienda.entity.Usuario;

@Repository
public interface PedidoRepository extends JpaRepository<Pedido, Integer>{


    @Query("SELECT p FROM Pedido p WHERE p.usuario.idUser = :idUser ORDER BY p.fecha DESC")
    List<Pedido> listarPorUsuario(@Param("idUser") Integer idUser);

    @Query("SELECT p FROM Pedido p WHERE p.estado = :estado")
    List<Pedido> listarPorEstado(@Param("estado") String estado);

    List<Pedido> findByUsuario(Usuario usuario);

    
}
