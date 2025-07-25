package com.tienda.service;

import java.util.List;

import com.tienda.entity.Categoria;

public interface CategoriaService {

	Categoria guardarCategoria (Categoria categoria);
	
	List<Categoria> listarCategorias();
	
	Categoria buscarPorId(Integer idCate);
	
	void eliminarCategoria(Integer idCate);
	
}
