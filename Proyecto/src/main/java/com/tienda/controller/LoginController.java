package com.tienda.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.tienda.entity.Rol;
import com.tienda.entity.Usuario;
import com.tienda.service.UsuarioService;

import jakarta.servlet.http.HttpSession;

@Controller
public class LoginController {

	@Autowired
	private UsuarioService usuarioService;
	
    // Mostrar formulario login
    @GetMapping("/")
    public String mostrarLogin(Model model) {
    	System.out.println("Bienvenido al Inicio de Sesion Tienda...");
        model.addAttribute("usuario", new Usuario());
        return "sesion/login";
    }
	
    // Procesar login
    @PostMapping("/procesarLogin")
    public String procesarLogin(@ModelAttribute("usuario") Usuario usuario,
                                Model model, HttpSession session) {

        Usuario usuarioAutenticado = usuarioService.login(usuario.getLogin(), usuario.getPassword());

        if (usuarioAutenticado != null && "Aprobado".equalsIgnoreCase(usuarioAutenticado.getEstado())) {
          
        	session.setAttribute("usuarioLogueado", usuarioAutenticado);

            String rol = usuarioAutenticado.getRol().getTipoRol();
            if ("Administrador".equalsIgnoreCase(rol)) {
                return "redirect:/admin/inicio";
            } else if ("Empleado".equalsIgnoreCase(rol)) {
                return "redirect:/empleado/inicio";
            } else {
                return "redirect:/cliente/inicio";
            }
        } else {
            model.addAttribute("error", "Credenciales inválidas o cuenta no aprobada");
            return "sesion/login";
        }
    }



    
    // Mostrar Formulario Registro
    @GetMapping("/registro")
    public String mostrarRegistro(Model model) {
    	System.out.println("Bienvenido al Formulario de Registro...");
        model.addAttribute("usuario", new Usuario());
        return "sesion/registro";
    }
    
    // Procesar Registro
    @PostMapping("/registro")
    public String registrar(@ModelAttribute Usuario usuario,
                            @RequestParam("tipoCuenta") String tipoCuenta,
                            RedirectAttributes redirect, Model model) {

        // Validaciones de usuario repetido
        if (usuarioService.buscarByLogin(usuario.getLogin()) != null) {
            model.addAttribute("error", "El Nombre de Usuario ya está registrado.");
            return "sesion/registro";
        }

        if (usuarioService.buscarByDni(usuario.getDni()) != null) {
            model.addAttribute("error", "El DNI de Usuario ya está registrado.");
            return "sesion/registro";
        }

        if (usuarioService.buscarByCorreo(usuario.getCorreo()) != null) {
            model.addAttribute("error", "El Correo de Usuario ya está registrado.");
            return "sesion/registro";
        }

        // Asignar Rol y Estado
        Rol rol = new Rol();
        if ("cliente".equals(tipoCuenta)) {
            rol.setIdRol(2);
            usuario.setEstado("Aprobado");
        } else if ("empleado".equals(tipoCuenta)) {
            rol.setIdRol(3);
            usuario.setEstado("Pendiente");
        }

        usuario.setRol(rol);

        Usuario nuevoUsuario = usuarioService.guardarUsuario(usuario);
        if (nuevoUsuario != null) {
            if (nuevoUsuario.getRol().getIdRol() == 3) { // ID 3 para EMPLEADO
                redirect.addFlashAttribute("mensaje", "Cuenta creada correctamente. La cuenta será verificada por un administrador.");
            } else {
                redirect.addFlashAttribute("mensaje", "Cuenta creada correctamente. Ya puedes iniciar sesión.");
            }

            return "redirect:/";
        }

        model.addAttribute("error", "Ocurrió un error al registrar la cuenta.");
        return "sesion/registro";
    }

    // Mostrar Formulario Correo
    @GetMapping("/recuperar")
    public String mostrarRecuperar() {
    	System.out.println("Bienvenido al Formulario de Validacion...");
    	return "sesion/recuperar";
    }
    
    // Procesar Correo
    @PostMapping("/recuperar")
    public String procesarCorreo(@RequestParam("correo") String correo, Model model) {

    	Usuario usuario = usuarioService.buscarByCorreo(correo);
    	
    	if(usuario != null) {
    		model.addAttribute("usuario", usuario);
    		return "sesion/actualizarCuenta";
    	} else {
    		model.addAttribute("error", "Correo de Usuario no encontrado.");
    		return "sesion/recuperar";
    	}	
    }
    
    // Mostrar Formulario Recuperacion
    @PostMapping("/actualizarCuenta")
    public String actualizarCuenta(@RequestParam("id") int id,
    							   @RequestParam("nuevoLogin") String nuevoLogin,
    							   @RequestParam("nuevoPassword") String nuevoPassword,
    							   RedirectAttributes redirect, Model model) {
    	Usuario usuario = usuarioService.buscarById(id);
    	
    	if(usuario == null) {
    		model.addAttribute("error", "Usuario no encontrado.");
    		return "sesion/recuperar";
    	}
    	
    	if(!nuevoLogin.trim().isEmpty()) {
    		Usuario existente = usuarioService.buscarByLogin(nuevoLogin.trim());
    		if (existente != null && existente.getIdUser() !=usuario.getIdUser()) {
    			model.addAttribute("error", "El nombre de Usuario ya esta registrado.");
    			model.addAttribute("usuario", usuario);
    			return "sesion/actualizarCuenta";
    		}
    		usuario.setLogin(nuevoLogin.trim());
    	}
    	
    	if(!nuevoPassword.trim().isEmpty()) {
    		usuario.setPassword(nuevoPassword.trim());
    	}
    	
    	usuarioService.guardarUsuario(usuario);
    	redirect.addFlashAttribute("mensaje", "Cuenta Actualizada correctamente. Ya puedes iniciar sesión.");
    	return "redirect:/";
    	
    }
   
    //Cerrar Sesion
    @GetMapping("/logout")
    public String cerrarSesion(HttpSession session) {
        session.invalidate();
        return "redirect:/";  // Redirige al login
    }

}
