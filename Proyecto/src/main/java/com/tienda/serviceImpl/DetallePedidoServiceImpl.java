package com.tienda.serviceImpl;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.tienda.entity.DetallePedido;
import com.tienda.entity.DetallePedidoId;
import com.tienda.repository.DetallePedidoRepository;
import com.tienda.repository.PedidoRepository;
import com.tienda.service.DetallePedidoService;

@Service
public class DetallePedidoServiceImpl implements DetallePedidoService{

	  private final DetallePedidoRepository detallePedidoRepository;

	  private final PedidoRepository pedidoRepository;

	  public DetallePedidoServiceImpl(DetallePedidoRepository detallePedidoRepository,
	                                  PedidoRepository pedidoRepository) {
	      this.detallePedidoRepository = detallePedidoRepository;
	      this.pedidoRepository = pedidoRepository;
	  }

	
	@Override
	public DetallePedido guardar(DetallePedido detalle) {
		return detallePedidoRepository.save(detalle);
	}

	@Override
	public DetallePedido buscarPorId(DetallePedidoId id) {
		return detallePedidoRepository.findById(id)
					.orElseThrow(() -> new RuntimeException("Detalle no encontrado: " + id));
	}

	//listar paginas
	@Override
	public Page<DetallePedido> listarPaginado(int numeroPagina, int tamanioPagina) {
		Pageable pageable = PageRequest.of(numeroPagina, tamanioPagina);
		return detallePedidoRepository.findAll(pageable);
	}

	//buscador con filtro
	@Override
	public Page<DetallePedido> buscarTodosPorFiltro(String filtro, int pagina, int tamanio) {
		Pageable pageable = PageRequest.of(pagina, tamanio);
		return detallePedidoRepository.buscarTodosPorFiltro(filtro, pageable);
	}

	@Override
	public long contadorDetallePedido(String filtro) {
		return detallePedidoRepository.contadorDetallePedido(filtro);
	}

	//eliminar
	@Override
	public void eliminar(DetallePedidoId idDetallePedido) {
	    Optional<DetallePedido> opt = detallePedidoRepository.findById(idDetallePedido);
	    if (opt.isPresent()) {
	        DetallePedido detalle = opt.get();
	        int idPedido = detalle.getPedido().getIdPedido();

	        detallePedidoRepository.deleteById(idDetallePedido);

	        List<DetallePedido> restantes = detallePedidoRepository.findByPedido_IdPedido(idPedido);
	        if (restantes.isEmpty()) {
	            pedidoRepository.deleteById(idPedido);
	        }

	    } else {
	        throw new RuntimeException("No se encontró el detalle con ID: " + idDetallePedido);
	    }
	}

	@Override
	public Page<DetallePedido> buscarPorFiltroYUsuario(String filtro, int pagina, int tamanio, int idUser) {
	    Pageable pageable = PageRequest.of(pagina, tamanio);
	    return detallePedidoRepository.buscarTodosPorFiltroyUsuario(filtro, idUser, pageable);
	}


	@Override
	public List<DetallePedido> buscarPorIdPedido(int idPedido) {
	    return detallePedidoRepository.findByPedido_IdPedido(idPedido);
	}

	
}
