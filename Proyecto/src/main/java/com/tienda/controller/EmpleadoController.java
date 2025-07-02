package com.tienda.controller;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.tienda.entity.Categoria;
import com.tienda.entity.DetallePedido;
import com.tienda.entity.DetallePedidoId;
import com.tienda.entity.Pedido;
import com.tienda.entity.Producto;
import com.tienda.entity.Usuario;
import com.tienda.service.CategoriaService;
import com.tienda.service.DetallePedidoService;
import com.tienda.service.PedidoService;
import com.tienda.service.ProductoService;
import com.tienda.service.UsuarioService;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

@Controller
@RequestMapping("/empleado")
public class EmpleadoController {
   
	@Autowired
	private PedidoService pedidoService;
	
	@Autowired
	private UsuarioService usuarioService;
	
	@Autowired
	private ProductoService productoService;
	
	@Autowired
	private CategoriaService categoriaService;
	
	@Autowired
	private DetallePedidoService detallePedidoService;
	
		//=========================== USUARIOS ===================================//
	
		// Listar todas las vistas 
	@GetMapping("/inicio")
		public String dashboard(@RequestParam(value = "seccion", defaultValue = "usuarios") String seccion,
	                        // usuarios
	                        @RequestParam(value = "filtro", required = false) String filtro,
	                        @RequestParam(value = "page", defaultValue = "1") int pagina,
	                        // productos
	                        @RequestParam(value = "filtroProducto", required = false) String filtroProducto,
	                        @RequestParam(value = "pageProducto", defaultValue = "1") int paginaProducto,
	                        // detalle pedido
	                        @RequestParam(value = "filtroDetallePedido", required = false) String filtroDetallePedido,
	                        @RequestParam(value = "pageDetallePedido", defaultValue = "1") int paginaDetallePedido,
	                        @RequestParam(value = "size", defaultValue = "10") int tamanio,
	                        Model model, HttpSession session) {

		Usuario logueado = (Usuario) session.getAttribute("usuarioLogueado");
		if (logueado == null) {
		    return "redirect:/";
		}
		model.addAttribute("idActual", logueado.getIdUser());

	    model.addAttribute("seccionActiva", seccion);

	    // Normalizar filtros a minúsculas
	    if (filtro != null) filtro = filtro.toLowerCase();
	    if (filtroProducto != null) filtroProducto = filtroProducto.toLowerCase();
	    if (filtroDetallePedido != null) filtroDetallePedido = filtroDetallePedido.toLowerCase();

	    if (seccion.equals("usuarios")) {
	    	int idActual = logueado.getIdUser();
	    	Page<Usuario> usuariosPage = (filtro == null || filtro.isBlank())
	    		    ? usuarioService.listarUsuariosFiltrados(idActual, pagina - 1, tamanio)
	    		    : usuarioService.buscarUsuariosFiltrados(filtro, idActual , pagina - 1, tamanio);

	    		model.addAttribute("usuarios", usuariosPage.getContent());
	    		model.addAttribute("paginaActual", pagina);
	    		model.addAttribute("totalPaginas", usuariosPage.getTotalPages());
	    		model.addAttribute("totalUsuarios", usuariosPage.getTotalElements());
	    		System.out.println("Vista Usuarios...");

	    }

	    if (seccion.equals("productos")) {
	        Page<Producto> productosPage = (filtroProducto == null || filtroProducto.isBlank())
	                ? productoService.listarPaginado(paginaProducto - 1, tamanio)
	                : productoService.buscarTodosPorFiltro(filtroProducto, paginaProducto - 1, tamanio);

	        model.addAttribute("productos", productosPage.getContent());
	        model.addAttribute("filtroProducto", filtroProducto);
	        model.addAttribute("paginaActualProducto", paginaProducto);
	        model.addAttribute("totalPaginasProducto", productosPage.getTotalPages());
	        model.addAttribute("totalProductos", productosPage.getTotalElements());
	        System.out.println("Vista Productos...");
	    }

	    if (seccion.equals("detallePedidos")) {
	        Page<DetallePedido> detallePedidosPage = (filtroDetallePedido == null || filtroDetallePedido.isBlank())
	                ? detallePedidoService.buscarTodosPorFiltro("", paginaDetallePedido - 1, tamanio)
	                : detallePedidoService.buscarTodosPorFiltro(filtroDetallePedido, paginaDetallePedido - 1, tamanio);

	        List<DetallePedido> listaDetallePedidos = detallePedidosPage.getContent();

	        // Calcular sumatoria por pedido
	        Map<Integer, BigDecimal> totalPorPedido = new HashMap<>();
	        for (DetallePedido d : listaDetallePedidos) {
	            int idPedido = d.getPedido().getIdPedido();
	            BigDecimal subtotal = d.getPrecioUnit().multiply(BigDecimal.valueOf(d.getCantidad()));
	            totalPorPedido.merge(idPedido, subtotal, BigDecimal::add);
	        }

	        // Enviar a la vista
	        model.addAttribute("detallePedidos", listaDetallePedidos);
	        model.addAttribute("totalPorPedido", totalPorPedido);
	        model.addAttribute("filtroDetallePedido", filtroDetallePedido);
	        model.addAttribute("paginaActualDetallePedido", paginaDetallePedido);
	        model.addAttribute("totalPaginasDetallePedido", detallePedidosPage.getTotalPages());
	        model.addAttribute("totalDetallePedido", detallePedidosPage.getTotalElements());
	        System.out.println("Vista Pedidos...");
	    }


	    return "/empleado/inicio";
		}
	
	    // Conteo Usuarios Por Filtro
	    @GetMapping("/contarUsuarios")
	    @ResponseBody
	    public long contarUsuariosPorFiltro(@RequestParam("filtro") String filtro) {
	    	return usuarioService.contarPorNombreApellidoORRol(filtro);
	    }
	    
	    // Eliminar Usuario
	    @PostMapping("/eliminarUsuario")
	    public String eliminarUsuario(@RequestParam("id") int id, RedirectAttributes redirect) {
	    	Usuario usuario = usuarioService.buscarById(id);
	    	
	    	if(usuario != null && "Aprobado".equalsIgnoreCase(usuario.getEstado())) {
	    	   usuario.setEstado("Rechazado");
	    	   usuarioService.eliminarUsuarioConPedidos(id);
	    	   
	    	   String nombreCompleto = usuario.getNombres() + " " + usuario.getApellidos();
	    	   redirect.addFlashAttribute("eliminar", "La Cuenta de "+nombreCompleto+" fue Eliminada.");
	    	}
	    	return "redirect:/empleado/inicio";
	    }
		
	    //=========================== PRODUCTOS ===================================//
	    
	    // Mostrar formulario de registro de producto
	    @GetMapping("/formularioRegistrarProducto")
		public String mostrarFormularioRegistroProducto(Model model) {
	    	model.addAttribute("producto", new Producto());
	    	model.addAttribute("categorias", categoriaService.listarCategorias());
	    	System.out.println("Bienvenido Empleado al Registro de Productos...");
	    	return "empleado/formularioRegistrarProducto";
		}

		// Guardar nuevo producto
	    @PostMapping("/registrarProducto")
	    public String guardarProducto(@ModelAttribute("producto") Producto producto,
	                                  RedirectAttributes redirect, Model model) {

	        // Validar nombre repetido
	        Producto productoPorNombre = productoService.buscarPorNombre(producto.getNomProd());
	        if (productoPorNombre != null) {
	            model.addAttribute("error", "El nombre del producto ya está registrado.");
	            model.addAttribute("categorias", categoriaService.listarCategorias());
	            return "empleado/formularioRegistrarProducto";
	        }
	        
	        // Guardar producto
	        producto.setActivo(producto.getStock() > 0);
	        productoService.guardarProducto(producto);

	        redirect.addFlashAttribute("mensaje", "Producto registrado exitosamente.");
	        return "redirect:/empleado/inicio?seccion=productos";
	    }

		//Conteo Producto Por filtro
	    @GetMapping("/contarProductos")
	    @ResponseBody
	    public long contarProductosPorFiltro(@RequestParam("filtro") String filtro) {
	    	return productoService.contadorProducto(filtro);
	    }
		
		// Mostrar formulario para actualizar un producto
		@GetMapping("/formularioActualizarProducto/{id}")
		public String mostrarFormularioActualizarProducto(@PathVariable("id") Integer idProd,
	                                                  Model model, RedirectAttributes redirect) {
	    	Producto producto = productoService.buscarPorId(idProd);
	    	if (producto == null) {
	        	redirect.addFlashAttribute("error", "Producto no encontrado.");
	        	return "redirect:/empleado/inicio?seccion=productos";
	    	}
	    	producto.setActivo(producto.getStock() > 0);
	    	model.addAttribute("producto", producto);
	    	model.addAttribute("categorias", categoriaService.listarCategorias());
	    	System.out.println("Bienvenido Empleado al editos de Productos...");
	    	return "empleado/formularioActualizarProducto";
		}

		// Actualizar producto
		@PostMapping("/actualizarProducto/{id}")
		public String actualizarProducto(@ModelAttribute("producto") Producto productoFormulario,
		                                 @PathVariable("id") int idProducto,
		                                 RedirectAttributes redirect) {

		    Producto productoExistente = productoService.buscarPorId(idProducto);

		    if (productoExistente != null) {
		        // Guardar nombre anterior
		        String nombreAnterior = productoExistente.getNomProd();
		        String nombreNuevo = productoFormulario.getNomProd();

		        // Actualizar campos
		        productoExistente.setNomProd(nombreNuevo);
		        productoExistente.setDetalles(productoFormulario.getDetalles());
		        productoExistente.setPrecioUnit(productoFormulario.getPrecioUnit());
		        productoExistente.setStock(productoFormulario.getStock());
		        productoExistente.setCategoria(productoFormulario.getCategoria());

		        // Activar si tiene stock
		        boolean nuevoEstado = productoExistente.getStock() > 0;
		        productoExistente.setActivo(nuevoEstado);

		        // Guardar cambios
		        productoService.actualizarProducto(productoExistente);

		        // Mensaje informativo
		        String mensaje;
		        if (!nombreAnterior.equals(nombreNuevo)) {
		            mensaje = "Producto actualizado: se cambió el nombre de '" + nombreAnterior + "' a '" + nombreNuevo + "'.";
		        } else {
		            mensaje = "Producto '" + nombreNuevo + "' actualizado correctamente.";
		        }

		        if (nuevoEstado) {
		            mensaje += " El producto está activo.";
		        } else {
		            mensaje += " El producto está inactivo por falta de stock.";
		        }

		        redirect.addFlashAttribute("mensaje", mensaje);
		    } else {
		        redirect.addFlashAttribute("error", "Producto no encontrado.");
		    }

		    return "redirect:/empleado/inicio?seccion=productos";
		}

		// Activar producto
		@PostMapping("/activarProducto/{id}")
		public String activarProducto(@PathVariable("id") int idProducto,
	                              	  RedirectAttributes redirect) {
			
			Producto producto = productoService.buscarPorId(idProducto);
			
	    	try {
	        	productoService.activar(idProducto);
	        	
	        	String nombreProducto = producto.getNomProd();
	        	redirect.addFlashAttribute("mensaje", "Producto '" + nombreProducto + "' activado correctamente.");
	    	} catch (Exception e) {
	        	redirect.addFlashAttribute("error", "Error al activar el producto.");
	    	}
	    	return "redirect:/empleado/inicio?seccion=productos";
		}

		// Desactivar producto
		@PostMapping("/desactivarProducto/{id}")
		public String desactivarProducto(@PathVariable("id") int idProducto,
		                                 RedirectAttributes redirect) {
			
		    Producto producto = productoService.buscarPorId(idProducto);
		    
		    try {
		        productoService.desactivar(idProducto);
		        
		        String nombreProducto = producto.getNomProd();

		        redirect.addFlashAttribute("mensaje", "Producto '" + nombreProducto + "' desactivado correctamente.");
		    } catch (Exception e) {
		        redirect.addFlashAttribute("error", "Error al desactivar el producto.");
		    }
		    
		    return "redirect:/empleado/inicio?seccion=productos";
		}
		
	    //=========================== CATEGORIAS ===================================//
	    
		// Listar
		@GetMapping("/listarCategorias")
    	public String listarCategorias(Model model) {
        model.addAttribute("categorias", categoriaService.listarCategorias());
        System.out.println("Bienvenido Empleado al Listado Categorias...");
        return "empleado/listarCategorias";
    	}

    	// formulario de registro sin ID
		@GetMapping("/formularioRegistrarCategoria")
		public String mostrarFormularioRegistroCategoria(Model model) {
			Categoria categoria = new Categoria();
			model.addAttribute("categoria", categoria);
    	model.addAttribute("origen", "categorias");
    	System.out.println("Bienvenido Empleado al Registro Categorias...");
    	return "empleado/formularioRegistrarCategoria";
		}

		// formulario para editar
		@GetMapping("/formularioActualizarCategoria/{id}")
		public String mostrarFormularioEditarCategoria(@PathVariable("id") int id, Model model) {
			Categoria categoria = categoriaService.buscarPorId(id);
			model.addAttribute("categoria", categoria);
			model.addAttribute("origen", "categorias");
			System.out.println("Bienvenido Empleado al Editor Categorias...");
			return "empleado/formularioRegistrarCategoria";
		}

		// al guardar: vuelve a /admin/listarCategorias
		@PostMapping("/registrarCategoria")
		public String guardarCategoria(@ModelAttribute("categoria") Categoria categoria,
	                               RedirectAttributes redirect) {
	    categoriaService.guardarCategoria(categoria);
	    redirect.addFlashAttribute("mensaje", "Categoría registrada/actualizada exitosamente");
	    return "redirect:/empleado/listarCategorias";
		}
		
		//=========================== PEDIDO ===================================//
	    
		//exportar reporte producto
	    @GetMapping("/pedido/{idPedido}/producto/{idProducto}")
	    public void exportarReporteProducto(
	        @PathVariable("idPedido") int idPedido,
	        @PathVariable("idProducto") int idProducto,
	        HttpServletResponse response) {

	        try {
	            InputStream jasperStream = getClass().getResourceAsStream("/reportes/pdfProducto.jasper");
	            if (jasperStream == null) {
	                throw new FileNotFoundException("No se encontró el archivo del reporte.");
	            }

	            DetallePedidoId detalleId = new DetallePedidoId(idPedido, idProducto);
	            DetallePedido detalle = detallePedidoService.buscarPorId(detalleId);
	            List<DetallePedido> detalles = Collections.singletonList(detalle);

	            Pedido pedido = pedidoService.buscarPorId(idPedido).orElse(null);
	            Usuario usuario = null;
	            String nombreCliente = "";

	            if (pedido != null && pedido.getUsuario() != null) {
	                usuario = pedido.getUsuario();
	                nombreCliente = usuario.getNombres() + " " + usuario.getApellidos();
	            }

	            Map<String, Object> parametros = new HashMap<>();
	            parametros.put("idPedido", idPedido);
	            parametros.put("nomCli", nombreCliente);

	            if (usuario != null) {
	                parametros.put("dni", usuario.getDni());
	                parametros.put("login", usuario.getLogin());
	                parametros.put("correo", usuario.getCorreo());
	            }

	            System.out.println("Cargando Reporte de Producto en Formato PDF.");

	            JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(detalles);
	            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperStream, parametros, dataSource);

	            // Tipo de contenido PDF sin encabezado personalizado
	            response.setContentType("application/pdf");

	            // Exportar directamente el PDF (sin Content-Disposition)
	            JasperExportManager.exportReportToPdfStream(jasperPrint, response.getOutputStream());

	        } catch (Exception ex) {
	            ex.printStackTrace();
	        }
	    }

	    //Exportar Reporte Pedido
	    @GetMapping("/pedido/{id}")
	    public void exportarReportePedido(@PathVariable("id") int idPedido, HttpServletResponse response) {
	        try {
	            InputStream jasperStream = getClass().getResourceAsStream("/reportes/pdfPedido.jasper");
	            if (jasperStream == null) {
	                throw new FileNotFoundException("No se encontró el archivo del reporte.");
	            }

	            // 1. Obtén los detalles del pedido
	            List<DetallePedido> detalles = detallePedidoService.buscarPorIdPedido(idPedido);

	            // 2. Obtén el pedido y el cliente
	            Pedido pedido = pedidoService.buscarPorId(idPedido).orElse(null);
	            Usuario usuario = null;
	            String nombreCliente = "";

	            if (pedido != null && pedido.getUsuario() != null) {
	                usuario = pedido.getUsuario();
	                nombreCliente = usuario.getNombres() + " " + usuario.getApellidos();
	            }

	            System.out.println("Cargando Reporte de Pedido en Formato PDF.");

	            // 3. Crea el DataSource
	            JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(detalles);

	            // 4. Parámetros
	            Map<String, Object> parametros = new HashMap<>();
	            parametros.put("idPedido", idPedido);
	            parametros.put("nomCli", nombreCliente);
	            parametros.put("dni", usuario.getDni());
	            parametros.put("login", usuario.getLogin());
	            parametros.put("correo", usuario.getCorreo());
	            

	            // 5. Llena el reporte
	            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperStream, parametros, dataSource);

	            // 6. Exporta a PDF
	            response.setContentType("application/pdf");
	            JasperExportManager.exportReportToPdfStream(jasperPrint, response.getOutputStream());

	        } catch (Exception ex) {
	            ex.printStackTrace();
	        }
	    }
	    
		
}