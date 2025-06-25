package com.tienda.serviceImpl;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.tienda.entity.Producto;
import com.tienda.repository.ProductoRepository;
import com.tienda.service.ProductoService;

@Service
public class ProductoServiceImpl implements ProductoService {

	private final ProductoRepository productoRepository;

	public ProductoServiceImpl(ProductoRepository productoRepository) {
		this.productoRepository = productoRepository;
	}	
	
	//Registrar Producto
	@Override
	public Producto guardarProducto(Producto producto) {
		return productoRepository.save(producto);
	}
	
	@Override
	public List<Producto> listarProductos() {
		return productoRepository.findAll();
	}

	//Eliminar Producto
	@Override
	public void eliminarProducto(Integer idProd) {
		productoRepository.deleteById(idProd);
		
	}

	//Actualizar Producto
	@Override
	public Producto actualizarProducto(Producto producto) {
	    Optional<Producto> existente = productoRepository.findById(producto.getIdProd());

	    
	    
	    if (existente.isPresent()) {
	        // Calcula si debe estar activo o no según el stock
	        boolean nuevoActivo = producto.getStock() > 0;
	        producto.setActivo(nuevoActivo);

	        return productoRepository.save(producto);
	    } else {
	        throw new RuntimeException("Producto no encontrado, con ID: " + producto.getIdProd());
	    }
	}
	
	//Buscar Productos Disponibles
	@Override
	public Page<Producto> buscarActivoPorFiltro(String filtro, int pagina, int tamanio, boolean disponibilidad) {
		Pageable pageable = PageRequest.of(pagina, tamanio);
		return productoRepository.buscarActivoPorFiltro(filtro, pageable);
	}
	
	//Buscar Todos los Productos
	@Override
	public Page<Producto> buscarTodosPorFiltro(String filtro, int pagina, int tamanio) {
		Pageable pageable = PageRequest.of(pagina, tamanio);
		return productoRepository.buscarTodosPorFiltro(filtro, pageable);
	}
	
	//Conteo de Productos
	@Override
	public long contadorProducto(String filtro) {
		return productoRepository.contadorProducto(filtro);
	}

	//Listar Paginas
	@Override
	public Page<Producto> listarPaginado(int numeroPagina, int tamanioPagina) {
		Pageable pageable = PageRequest.of(numeroPagina, tamanioPagina);
		return productoRepository.findAll(pageable);
	}

	@Override
	public void activar(int idProducto) {
	    Optional<Producto> opt = productoRepository.findById(idProducto);
	    if (opt.isPresent()) {
	        Producto p = opt.get();
	        p.setActivo(true);
	        p.setStock(p.getStock() + 1);
	        productoRepository.save(p);
	    }
	}

	@Override
	public void desactivar(int idProducto) {
	    Optional<Producto> opt = productoRepository.findById(idProducto);
	    if (opt.isPresent()) {
	        Producto p = opt.get();
	        p.setActivo(false);
	        p.setStock(0);
	        productoRepository.save(p);
	    }
	}
	
	//Validar Productos
	@Override
	public Producto buscarPorId(Integer idProd) {
		return productoRepository.findById(idProd)
				.orElseThrow(() -> new RuntimeException("Producto no encontrado con ID: " + idProd));
	}

	@Override
	public Producto buscarPorNombre(String nomProd) {
		return productoRepository.findByNombre(nomProd);
	}



}
