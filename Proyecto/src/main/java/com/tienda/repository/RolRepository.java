package com.tienda.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.tienda.entity.Rol;

@Repository
public interface RolRepository extends JpaRepository<Rol, Integer>{

	@Query(value = "select * from rol", nativeQuery = true)
	List<Rol> listarRoles();
}
