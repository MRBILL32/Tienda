package com.tienda.service;

import java.util.List;
import java.util.Optional;

import com.tienda.entity.Pedido;

public interface PedidoService {
	
    Pedido guardar(Pedido pedido);
    Optional<Pedido> buscarPorId(Integer idPedido);
    List<Pedido> listarTodos();
    List<Pedido> listarPorUsuario(Integer idUser);
    List<Pedido> listarPorEstado(String estado);
    Pedido cambiarEstado(Integer idPedido, String nuevoEstado);
}
