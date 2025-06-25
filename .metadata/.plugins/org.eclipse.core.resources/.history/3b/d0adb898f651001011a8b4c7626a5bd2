package com.tienda.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tienda.entity.Producto;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, Integer>{

	//Validar nombre Producto
	@Query("SELECT p FROM Producto p WHERE p.nomProd = :nomProd")
	Producto findByNombre(@Param("nomProd") String nomProd);
	
	//Paginar Busqueda Con Filtro(solo activos)
	@Query("SELECT p FROM Producto p WHERE (" +
			"LOWER(p.nomProd) LIKE LOWER(CONCAT('%', :filtro, '%')) OR " +
			"LOWER(p.marcaProd) LIKE LOWER(CONCAT('%', :filtro, '%')) OR " +
			"LOWER(p.categoria.nomCate) LIKE LOWER(CONCAT('%', :filtro, '%')))"  +
			"AND p.activo = true")
	Page<Producto> buscarActivoPorFiltro(@Param("filtro") String filtro, Pageable pageable);
	
	//Paginar Busqueda Con Filtro(todos)
	@Query("SELECT p FROM Producto p WHERE " +
		       "LOWER(p.nomProd) LIKE LOWER(CONCAT('%', :filtro, '%')) OR " +
		       "LOWER(p.marcaProd) LIKE LOWER(CONCAT('%', :filtro, '%')) OR " +
		       "LOWER(p.categoria.nomCate) LIKE LOWER(CONCAT('%', :filtro, '%'))")
	Page<Producto> buscarTodosPorFiltro(@Param("filtro") String filtro, Pageable pageable);

	//Contardor Con Filtro
	@Query("SELECT COUNT(p) FROM Producto p WHERE " +
		   "LOWER(p.nomProd) LIKE LOWER(CONCAT('%', :filtro, '%')) OR " +
		   "LOWER(p.marcaProd) LIKE LOWER(CONCAT('%', :filtro, '%')) OR " +
		   "LOWER(p.categoria.nomCate) LIKE LOWER(CONCAT('%', :filtro, '%'))")
	long contadorProducto(@Param("filtro") String filtro);

	
    
	
}
