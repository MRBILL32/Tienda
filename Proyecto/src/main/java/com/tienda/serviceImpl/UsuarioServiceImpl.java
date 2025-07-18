package com.tienda.serviceImpl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tienda.entity.Pedido;
import com.tienda.entity.Usuario;
import com.tienda.repository.DetallePedidoRepository;
import com.tienda.repository.PedidoRepository;
import com.tienda.repository.UsuarioRepository;
import com.tienda.service.UsuarioService;

@Service
public class UsuarioServiceImpl implements UsuarioService {

	private final UsuarioRepository usuarioRepository;
	
	@Autowired 
	private PedidoRepository pedidoRepository;
	
	@Autowired
	private DetallePedidoRepository detallePedidoRepository;
	
	public UsuarioServiceImpl(UsuarioRepository usuarioRepository) {
	        this.usuarioRepository = usuarioRepository;
	}
	
	//Registrar Usuario
	@Override
	public Usuario guardarUsuario(Usuario usuario) {
		return usuarioRepository.save(usuario);
	}


	//Login Validacion(Iniciar Sesion)
	@Override
	public Usuario login(String login, String password) {
	    Usuario usuario = usuarioRepository.findByLoginAndPassword(login, password);
	    return (usuario != null && "Aprobado".equalsIgnoreCase(usuario.getEstado())) ? usuario : null;
	}


	//Buscar por Login
	@Override
	public Usuario buscarByLogin(String login) {
		return usuarioRepository.findByLogin(login);
	}

	@Override
	public Usuario actualizarUsuario(Usuario usuario) {
		Optional<Usuario> existente =usuarioRepository.findById(usuario.getIdUser());
	
		if(existente.isPresent()) {
			return usuarioRepository.save(usuario); //actualiza el usuario
		} else {
			throw new RuntimeException("Usuario no encontrado, con ID:" + usuario.getIdUser());
		}
		
	}
	
	@Override
	public void eliminarUsuario(Integer idUser) {
		usuarioRepository.deleteById(idUser);
	}
	
	@Override
	public Usuario cambiarEstado(Integer idUser, String nuevoEstado) {
	    Optional<Usuario> optionalUsuario = usuarioRepository.findById(idUser);

	    if(optionalUsuario.isPresent()) {
	        Usuario usuario = optionalUsuario.get();
	        usuario.setEstado(nuevoEstado);

	        if(nuevoEstado.equalsIgnoreCase("Rechazado")) {
	            usuarioRepository.deleteById(idUser);
	            return null; // usuario eliminado
	        } else {
	            return usuarioRepository.save(usuario); // estado actualizado
	        }
	    } else {
	        throw new RuntimeException("Usuario con ID: " + idUser + " no encontrado");
	    }
	}

	@Override
	public Usuario buscarByDni(String dni) {
		return usuarioRepository.findByDni(dni);
	}

	@Override
	public Usuario buscarByCorreo(String correo) {
		return usuarioRepository.findByCorreo(correo);
	}

	@Override
	public Usuario buscarById(int id) {
		return usuarioRepository.findById(id).orElse(null);
	}

	@Override
	@Transactional
	public void eliminarUsuarioConPedidos(int id) {
	    Usuario usuario = usuarioRepository.findById(id).orElse(null);
	    if (usuario != null) {
	    	List<Pedido> pedidos = pedidoRepository.findByUsuario(usuario);
	    	for (Pedido pedido : pedidos) {
	    		detallePedidoRepository.deleteByPedido(pedido);
	    	    pedidoRepository.delete(pedido);
	    	}
	    	usuarioRepository.delete(usuario);
	    }
	}


	@Override
	public Page<Usuario> buscarPorNombreApellidoORRolPaginado(String filtro, int pagina, int tamanio) {
		 Pageable pageable = PageRequest.of(pagina, tamanio);
		    return usuarioRepository.buscarPorNombreApellidoORRolPaginado(filtro, pageable);
	}

	@Override
	public long contarPorNombreApellidoORRol(String filtro) {
		return usuarioRepository.contarPorNombreApellidoORRol(filtro);
	}

	@Override
	public Usuario aprobarUsuario(int idUser) {
		Usuario u = usuarioRepository.findById(idUser).orElse(null);
		if(u != null) {
			u.setEstado("Aprobado");
			return usuarioRepository.save(u);
		}
		
		return null;
	}

	@Override
	public Usuario rechazarUsuario(int idUser) {
	    Usuario u = usuarioRepository.findById(idUser).orElse(null);
	    if (u != null) {
	        u.setEstado("Rechazado");
	        return usuarioRepository.save(u);
	    }
	    return null;
	}

	@Override
	public Page<Usuario> listarPaginado(int numeroPagina, int tamanioPagina) {
		Pageable pageable = PageRequest.of(numeroPagina, tamanioPagina);
		return usuarioRepository.findAll(pageable);
	}

	@Override
	public Page<Usuario> buscarUsuariosFiltrados(String filtro, int idLogueado, int page, int size) {
	    Pageable pageable = PageRequest.of(page, size);
	    return usuarioRepository.buscarUsuariosFiltrados(filtro, idLogueado, pageable);
	}

	@Override
	public Page<Usuario> listarUsuariosFiltrados(int idLogueado, int page, int size) {
	    Pageable pageable = PageRequest.of(page, size);
	    return usuarioRepository.listarUsuariosFiltrados(idLogueado, pageable);
	}



	
}
