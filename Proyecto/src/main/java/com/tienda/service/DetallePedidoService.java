package com.tienda.service;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import com.tienda.entity.DetallePedido;
import com.tienda.entity.DetallePedidoId;

@Service
public interface DetallePedidoService {

	DetallePedido guardar(DetallePedido detalle);
	
	DetallePedido buscarPorId(DetallePedidoId id);
	
	Page<DetallePedido> buscarTodosPorFiltro(String filtro, int pagina, int tamanio);
	
	long contadorDetallePedido(String filtro);
	
	Page<DetallePedido> listarPaginado(int numeroPagina, int tamanioPagina);
	
	void eliminar(DetallePedidoId idDetallePedido);
	
	Page<DetallePedido> buscarPorFiltroYUsuario(String filtro, int page, int size, int idUser);

}
