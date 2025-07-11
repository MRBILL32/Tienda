package com.tienda.service;

import java.util.List;

import org.springframework.data.domain.Page;

import com.tienda.entity.Producto;

public interface ProductoService {

	// Agregar Producto
    Producto guardarProducto(Producto producto);
    
    // Listar Producto
    List<Producto> listarProductos();
    
    // Validar Producto
    Producto buscarPorId(Integer idProd);
    
    // validar por nombre
    Producto buscarPorNombre(String nomProd);
    
    // Actuarlizar Productos 
    Producto actualizarProducto(Producto producto);
    
    // Eliminar Producto
    void eliminarProducto(Integer idProd);
    
    //Paginar Busqueda con Filtro(solo activos)
    Page<Producto> buscarActivoPorFiltro(String filtro, int pagina, int tamanio, boolean disponibilidad);
    
    //Paginar Busqueda con filtro(todos)
    Page<Producto> buscarTodosPorFiltro(String filtro, int pagina, int tamanio);
    
    //Contador Productos
    long contadorProducto (String filtro);
    
    //Listado Paginado 1,5
    Page<Producto> listarPaginado(int numeroPagina, int tamanioPagina);
    
    void activar(int idProducto);
    
    void desactivar(int idProducto);


    
}
