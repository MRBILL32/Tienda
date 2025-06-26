package com.tienda.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.tienda.entity.Usuario;


@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {
	
		// Iniciar Sesion
		@Query("SELECT u FROM Usuario u WHERE u.login = :login AND u.password = :password")
		Usuario findByLoginAndPassword(@Param("login") String login, @Param("password") String password);
    
    	// Validar Login
    	@Query("SELECT u FROM Usuario u WHERE u.login = :login")
    	Usuario findByLogin(@Param("login") String login);

    	//Validar DNI
    	@Query("SELECT u FROM Usuario u WHERE u.dni = :dni")
    	Usuario findByDni(@Param("dni") String dni);
    
    	//Validar Correo
    	@Query("SELECT u FRoM Usuario u WHERE u.correo = :correo")
    	Usuario findByCorreo(@Param("correo")String correo);
    
    	//Paginar Busqueda Con filtro
    	@Query("SELECT u FROM Usuario u WHERE " +
    	       "LOWER(u.nombres) LIKE LOWER(CONCAT('%', :filtro, '%')) OR " +
    	       "LOWER(u.apellidos) LIKE LOWER(CONCAT('%', :filtro, '%')) OR " +
    	       "LOWER(u.rol.tipoRol) LIKE LOWER(CONCAT('%', :filtro, '%'))")
    	Page<Usuario> buscarPorNombreApellidoORRolPaginado(@Param("filtro") String filtro, Pageable pageable);

    	//Contador Con Filtro
    	@Query("SELECT COUNT(u) FROM Usuario u WHERE " +
    	       "LOWER(u.nombres) LIKE LOWER(CONCAT('%', :filtro, '%')) OR " +
    	       "LOWER(u.apellidos) LIKE LOWER(CONCAT('%', :filtro, '%')) OR " +
    	       "LOWER(u.rol.tipoRol) LIKE LOWER(CONCAT('%', :filtro, '%'))")
     	long contarPorNombreApellidoORRol(@Param("filtro") String filtro);
    
    	@Query("SELECT u FROM Usuario u WHERE " +
    		       "(LOWER(u.nombres) LIKE %:filtro% OR LOWER(u.apellidos) LIKE %:filtro% OR LOWER(u.rol.tipoRol) LIKE %:filtro%) " +
    		       "AND u.estado <> 'Pendiente' AND " +
    		       "(u.rol.tipoRol NOT IN ('Administrador', 'Empleado') OR u.idUser = :idLogueado)")
    		Page<Usuario> buscarUsuariosFiltrados(@Param("filtro") String filtro, @Param("idLogueado") int idLogueado, Pageable pageable);

    	
    	@Query("SELECT u FROM Usuario u WHERE u.estado <> 'Pendiente' AND " +
    		       "(u.rol.tipoRol NOT IN ('Administrador', 'Empleado') OR u.idUser = :idLogueado)")
    		Page<Usuario> listarUsuariosFiltrados(@Param("idLogueado") int idLogueado, Pageable pageable);

    	


    
}

