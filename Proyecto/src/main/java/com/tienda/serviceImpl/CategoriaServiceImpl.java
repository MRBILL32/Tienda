package com.tienda.serviceImpl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.tienda.entity.Categoria;
import com.tienda.repository.CategoriaRepository;
import com.tienda.service.CategoriaService;

@Service
public class CategoriaServiceImpl implements CategoriaService {

	 private final CategoriaRepository categoriaRepository;

	    public CategoriaServiceImpl(CategoriaRepository categoriaRepository) {
	        this.categoriaRepository = categoriaRepository;
	    }
	
	@Override
	public Categoria guardarCategoria(Categoria categoria) {
		return categoriaRepository.save(categoria);
	}

	@Override
	public List<Categoria> listarCategorias() {
		return categoriaRepository.findAll();
	}

	@Override
    public Categoria buscarPorId(Integer idCate) {
        return categoriaRepository.findById(idCate)
                .orElseThrow(() -> new RuntimeException("Categoría no encontrada con ID: " + idCate));
    }

	@Override
	public void eliminarCategoria(Integer idCate) {
		categoriaRepository.deleteById(idCate);
		
	}
}
