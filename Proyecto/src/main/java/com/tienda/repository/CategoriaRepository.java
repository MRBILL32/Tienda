package com.tienda.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tienda.entity.Categoria;

@Repository
public interface CategoriaRepository extends JpaRepository< Categoria,Integer>{
}
