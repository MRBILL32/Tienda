package com.tienda.serviceImpl;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.tienda.entity.DetalleCarrito;
import com.tienda.repository.DetalleCarritoRepository;
import com.tienda.service.DetalleCarritoService;

@Service
public class DetalleCarritoServiceImpl  implements DetalleCarritoService{

	private final DetalleCarritoRepository detalleCarritoRepository;

	public DetalleCarritoServiceImpl(DetalleCarritoRepository detalleCarritoRepository) {
	        this.detalleCarritoRepository = detalleCarritoRepository;
	}
	
	@Override
	public DetalleCarrito guardar(DetalleCarrito detalle) {
		return detalleCarritoRepository.save(detalle);
	}

	@Override
	public List<DetalleCarrito> listarPorCarrito(Integer idCarrito) {
		return detalleCarritoRepository.listarPorCarrito(idCarrito);
	}

	@Override
	public Optional<DetalleCarrito> buscarPorCarritoYProducto(Integer idCarrito, Integer idProd) {
		return detalleCarritoRepository.buscarPorCarritoYProducto(idCarrito, idProd);
	}

	@Override
	public void eliminar(Integer idDetalleCarrito) {
		detalleCarritoRepository.deleteById(idDetalleCarrito);
		
	}




   
}