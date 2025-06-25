package com.tienda.service;


import org.springframework.data.domain.Page;

import com.tienda.entity.Usuario;

public interface UsuarioService {

	Usuario guardarUsuario(Usuario usuario);
	
	Usuario login(String login, String password);

	Usuario buscarById(int id);
	
	Usuario buscarByLogin(String login);
	
	Usuario buscarByDni(String dni);
	
	Usuario buscarByCorreo(String correo);
	
	Usuario actualizarUsuario(Usuario usuario);
	
	void eliminarUsuario(Integer idUser);
	
	Usuario cambiarEstado(Integer idUser, String nuevoEstado);

	void eliminarUsuarioConPedidos(int idUser);
	
	Page<Usuario> buscarPorNombreApellidoORRolPaginado(String filtro, int pagina, int tamanio);
	
	long contarPorNombreApellidoORRol(String filtro);
	
	Usuario aprobarUsuario(int idUser);
	
	Usuario rechazarUsuario(int idUser);
	
	Page<Usuario> listarPaginado(int numeroPagina, int tamanioPagina);
	
	Page<Usuario> buscarUsuariosFiltrados(String filtro, int page, int size);
	Page<Usuario> listarUsuariosFiltrados(int page, int size);

	
}
