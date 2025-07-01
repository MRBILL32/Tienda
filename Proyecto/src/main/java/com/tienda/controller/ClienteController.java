package com.tienda.controller;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.tienda.entity.Carrito;
import com.tienda.entity.DetalleCarrito;
import com.tienda.entity.DetallePedido;
import com.tienda.entity.DetallePedidoId;
import com.tienda.entity.Pedido;
import com.tienda.entity.Producto;
import com.tienda.entity.Usuario;
import com.tienda.service.CarritoService;
import com.tienda.service.DetalleCarritoService;
import com.tienda.service.DetallePedidoService;
import com.tienda.service.PedidoService;
import com.tienda.service.ProductoService;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

@Controller
@RequestMapping("/cliente")
public class ClienteController {

    @Autowired
    private ProductoService productoService;
    
    @Autowired
    private PedidoService pedidoService;

    @Autowired
    private DetallePedidoService detallePedidoService;
    
    @Autowired
    private DetalleCarritoService detalleCarritoService;
    
    @Autowired
    private CarritoService carritoService;
    

    // Listar todas las vistas
    @GetMapping("/inicio")
    public String dashboard(@RequestParam(value = "seccion", defaultValue = "productos") String seccion,
                            @RequestParam(value = "filtroProducto", required = false) String filtroProducto,
                            @RequestParam(value = "pageProducto", defaultValue = "1") int paginaProducto,
                            @RequestParam(value = "filtroDetallePedido", required = false) String filtroDetallePedido,
                            @RequestParam(value = "pageDetallePedido", defaultValue = "1") int paginaDetallePedido,
                            @RequestParam(value = "size", defaultValue = "8") int tamanio,
                            Model model, HttpSession session) {

    	Usuario logueado = (Usuario) session.getAttribute("usuarioLogueado");
		
    	if (logueado == null) {
			System.out.println("Error al Buscar Usuario...");
		    return "redirect:/";
		}
		model.addAttribute("idActual", logueado.getIdUser());

        int idUser = logueado.getIdUser();
        model.addAttribute("seccionActiva", seccion);

        if (seccion.equals("productos")) {
        	
            Page<Producto> productosPage = (filtroProducto == null || filtroProducto.isBlank())
                    ? productoService.buscarActivoPorFiltro("", paginaProducto - 1, tamanio, true)
                    : productoService.buscarActivoPorFiltro(filtroProducto, paginaProducto - 1, tamanio, true);

            System.out.println("Cargando Lista de Productos...");
            model.addAttribute("productos", productosPage.getContent());
            model.addAttribute("filtroProducto", filtroProducto);
            model.addAttribute("paginaActualProducto", paginaProducto);
            model.addAttribute("totalPaginasProducto", productosPage.getTotalPages());
            model.addAttribute("totalProductos", productosPage.getTotalElements());
        }

        if (seccion.equals("detallePedidos")) {
            Page<DetallePedido> detallePedidosPage = (filtroDetallePedido == null || filtroDetallePedido.isBlank())
                    ? detallePedidoService.buscarPorFiltroYUsuario("", paginaDetallePedido - 1, tamanio, idUser)
                    : detallePedidoService.buscarPorFiltroYUsuario(filtroDetallePedido, paginaDetallePedido - 1, tamanio, idUser);

            System.out.println("Cargando Reporte de Pedidos...");


            // Filtrar para evitar nulos y luego quedarnos con uno por pedido
            List<DetallePedido> todosLosDetalles = detallePedidosPage.getContent().stream()
            	    .filter(d -> d != null && d.getPedido() != null && d.getProducto() != null)
            	    .toList(); // O usa .collect(Collectors.toList()) si estás en Java 8


            // Agrupar
            Map<Integer, DetallePedido> primerosPorPedido = new LinkedHashMap<>();
            Map<Integer, BigDecimal> totalPorPedido = new HashMap<>();

            for (DetallePedido d : todosLosDetalles) {
                int idPedido = d.getPedido().getIdPedido();

                // Solo agregamos el primer DetallePedido por pedido
                if (!primerosPorPedido.containsKey(idPedido)) {
                    primerosPorPedido.put(idPedido, d);
                }

                // Calculamos total por pedido si aún no se ha hecho
                if (!totalPorPedido.containsKey(idPedido)) {
                    BigDecimal total = d.getPedido().getDetalles().stream()
                        .map(dp -> dp.getPrecioUnit().multiply(BigDecimal.valueOf(dp.getCantidad())))
                        .reduce(BigDecimal.ZERO, BigDecimal::add);

                    totalPorPedido.put(idPedido, total);
                }
            }


            model.addAttribute("detallePedidos", primerosPorPedido.values());
            model.addAttribute("totalPorPedido", totalPorPedido);
            model.addAttribute("filtroDetallePedido", filtroDetallePedido);
            model.addAttribute("paginaActualDetallePedido", paginaDetallePedido);
            model.addAttribute("totalPaginasDetallePedido", detallePedidosPage.getTotalPages());
            model.addAttribute("totalDetallePedido", detallePedidosPage.getTotalElements());
        }


        return "cliente/inicio";
    }

    // Ver detalle de producto antes de comprar
    @GetMapping("/detalleProducto/{id}")
    public String detalleProducto(@PathVariable("id") int id, Model model) {
        Producto producto = productoService.buscarPorId(id);
        
        System.out.println("Cargando Seleccion de Producto...");
        model.addAttribute("producto", producto);
        model.addAttribute("cantidad", 1);
        return "cliente/detalleProducto";
    }

    // Agregar producto al carrito
    @PostMapping("/agregarCarrito")
    public String agregarAlCarrito(@RequestParam("idProd") int idProd,
                                   @RequestParam("cantidad") int cantidad,
                                   HttpSession session, RedirectAttributes redirect) {

        Usuario usuario = (Usuario) session.getAttribute("usuarioLogueado");
        if (usuario == null) return "redirect:/";

        Producto producto = productoService.buscarPorId(idProd);
        if (producto == null || Boolean.FALSE.equals(producto.isActivo()) || producto.getStock() <= 0) {
            redirect.addFlashAttribute("error", "Producto no válido o sin stock.");
            return "redirect:/cliente/inicio?seccion=productos";
        }

        // 1. Buscar carrito persistente
        Carrito carrito = carritoService.buscarPorUsuario(usuario.getIdUser());
        if (carrito == null) {
            carrito = new Carrito(usuario); // constructor ya asigna usuario
            carrito = carritoService.guardar(carrito); // persiste y genera ID
        }

        // 2. Buscar si ya existe ese producto en el carrito
        Optional<DetalleCarrito> optExistente = detalleCarritoService
            .buscarPorCarritoYProducto(carrito.getIdCarrito(), idProd);

        int cantidadTotal = cantidad;
        if (optExistente.isPresent()) {
            cantidadTotal += optExistente.get().getCantidad();
        }

        // 3. Validar stock
        if (cantidadTotal > producto.getStock()) {
        	redirect.addFlashAttribute("error", "No puedes agregar más de lo disponible en stock.");
            return "redirect:/cliente/detalleProducto/" + idProd;
        }

        // 4. Insertar o actualizar detalle del carrito
        if (optExistente.isPresent()) {
            DetalleCarrito existente = optExistente.get();
            existente.setCantidad(cantidadTotal);
            existente.setPrecioUnit(producto.getPrecioUnit());
            detalleCarritoService.guardar(existente);
        } else {
            DetalleCarrito nuevo = new DetalleCarrito();
            nuevo.setCarrito(carrito);
            nuevo.setProducto(producto);
            nuevo.setCantidad(cantidad);
            nuevo.setPrecioUnit(producto.getPrecioUnit());
            detalleCarritoService.guardar(nuevo);
        }

        redirect.addFlashAttribute("mensaje", "Producto agregado al carrito.");
        return "redirect:/cliente/inicio?seccion=productos";
    }

    // Ver carrito
    @GetMapping("/verCarrito")
    public String verCarrito(HttpSession session, Model model, RedirectAttributes redirectAttributes) {

        Usuario usuario = (Usuario) session.getAttribute("usuarioLogueado");

        if (usuario == null) {
            return "redirect:/";
        }

        Carrito carrito = carritoService.buscarPorUsuario(usuario.getIdUser());

        if (carrito == null) {
        	System.out.println("Error al Cargar Carrito de Compras...");
            return "redirect:/cliente/inicio";
        }

        List<DetalleCarrito> detalle = detalleCarritoService.listarPorCarrito(carrito.getIdCarrito());

        if (detalle == null || detalle.isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "Tu carrito está vacío. Agrega productos para continuar.");
            System.out.println("Carrito Vacio...");
            return "redirect:/cliente/inicio";
        }

        BigDecimal total = detalle.stream()
                .map(DetalleCarrito::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        System.out.println("Cargando Carrito de Compras...");
        model.addAttribute("carrito", detalle);
        model.addAttribute("total", total);
        return "cliente/verCarrito";
    }

    // Eliminar producto del carrito
    @PostMapping("/eliminarDelCarrito")
    public String eliminarDelCarrito(@RequestParam("idDetalle") int idDetalle) {
        detalleCarritoService.eliminar(idDetalle);
        return "redirect:/cliente/verCarrito";
    }

    // Vaciar carrito
    @PostMapping("/vaciarCarrito")
    public String vaciarCarrito(HttpSession session,  RedirectAttributes redirect) {
        Usuario usuario = (Usuario) session.getAttribute("usuarioLogueado");
        if (usuario != null) {
            Carrito carrito = carritoService.buscarPorUsuario(usuario.getIdUser());
            if (carrito != null) {
                // Eliminar los detalles del carrito desde la BD
                carritoService.vaciarCarrito(carrito.getIdCarrito());
            }
        }

        session.removeAttribute("carrito");
        redirect.addFlashAttribute("mensaje", "Productos eliminados del carrito.");
        return "redirect:/cliente/inicio";
    }
    
    // Realizar Compra
    @PostMapping("/comprar")
    public String comprar(HttpSession session, RedirectAttributes redirectAttrs) {
        Usuario usuario = (Usuario) session.getAttribute("usuarioLogueado");
        if (usuario == null) return "redirect:/";

        Carrito carrito = carritoService.buscarPorUsuario(usuario.getIdUser());
        if (carrito == null) {
            redirectAttrs.addFlashAttribute("error", "No hay carrito activo.");
            return "redirect:/cliente/verCarrito";
        }

        List<DetalleCarrito> detalles = detalleCarritoService.listarPorCarrito(carrito.getIdCarrito());
        if (detalles.isEmpty()) {
            redirectAttrs.addFlashAttribute("error", "El carrito está vacío.");
            return "redirect:/cliente/verCarrito";
        }

        // Crear Pedido
        Pedido pedido = new Pedido();
        pedido.setUsuario(usuario);
        pedido.setFecha(LocalDate.now());
        pedido.setEstado("Pendiente");
        pedido = pedidoService.guardar(pedido);

        for (DetalleCarrito detalle : detalles) {
            Producto producto = detalle.getProducto();

            // Verificar stock antes de restar
            if (producto.getStock() < detalle.getCantidad()) {
                redirectAttrs.addFlashAttribute("error", "Stock insuficiente para: " + producto.getNomProd());
                return "redirect:/cliente/verCarrito";
            }

            // Restar stock
            int nuevoStock = producto.getStock() - detalle.getCantidad();
            producto.setStock(nuevoStock);

            // Si el stock llega a 0, desactivar el producto automáticamente
            if (nuevoStock <= 0) {
                producto.setActivo(false);
            }

            productoService.guardarProducto(producto);

            // Crear DetallePedido
            DetallePedido dp = new DetallePedido();
            dp.setPedido(pedido);
            dp.setProducto(producto);
            dp.setCantidad(detalle.getCantidad());
            dp.setPrecioUnit(detalle.getPrecioUnit());

            // Establecer ID compuesto
            DetallePedidoId id = new DetallePedidoId();
            id.setIdPedido(pedido.getIdPedido());
            id.setIdProd(producto.getIdProd());
            dp.setId(id);

            detallePedidoService.guardar(dp);
        }


        // Marcar carrito como cerrado
        carritoService.vaciarCarrito(carrito.getIdCarrito());

        // Eliminar carrito de la sesión
        session.removeAttribute("carrito");

        System.out.println("Compra Procesada, FElicidades :D...");
        redirectAttrs.addFlashAttribute("mensaje", "Compra realizada con éxito.");
        return "redirect:/cliente/inicio?seccion=detallePedidos";
    }
    
    //Conteo Detalle Pedido por filtro
    @GetMapping("/contarDetallePedido")
    @ResponseBody
    public long contadorDetallePedido(@RequestParam("filtro") String filtro) {
    	return detallePedidoService.contadorDetallePedido(filtro);
    }
    
    //exportar reporte producto
    @GetMapping("/pedido/{idPedido}/producto/{idProducto}")
    public void exportarReporteProducto(
        @PathVariable("idPedido") int idPedido,
        @PathVariable("idProducto") int idProducto,
        HttpServletResponse response) {
        try {
            InputStream jasperStream = getClass().getResourceAsStream("/reportes/individual.jasper");
            if (jasperStream == null) {
                throw new FileNotFoundException("No se encontró el archivo del reporte.");
            }

            // Crea el ID compuesto
            DetallePedidoId detalleId = new DetallePedidoId(idPedido, idProducto);

            // Busca el detalle específico
            DetallePedido detalle = detallePedidoService.buscarPorId(detalleId);
            List<DetallePedido> detalles = Collections.singletonList(detalle);

            // Obtén el pedido y el usuario para el encabezado
            Pedido pedido = pedidoService.buscarPorId(idPedido).orElse(null);
            Usuario usuario = null;
            String nombreCliente = "";

            if (pedido != null && pedido.getUsuario() != null) {
                usuario = pedido.getUsuario();
                nombreCliente = usuario.getNombres() + " " + usuario.getApellidos();
            }

            // Parámetros
            Map<String, Object> parametros = new HashMap<>();
            parametros.put("idPedido", idPedido);
            parametros.put("nomCli", nombreCliente);

            // Agregar otros datos si el usuario no es null
            if (usuario != null) {
                parametros.put("dni", usuario.getDni());
                parametros.put("login", usuario.getLogin());
                parametros.put("correo", usuario.getCorreo());
            }

            System.out.println("Cargando Reporte de Producto en Formato PDF.");

            // DataSource
            JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(detalles);

            // Genera el PDF
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperStream, parametros, dataSource);
            response.setContentType("application/pdf");
            response.setHeader("Content-Disposition", "inline; filename=producto_" + idProducto + ".pdf");
            JasperExportManager.exportReportToPdfStream(jasperPrint, response.getOutputStream());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    
    //Exportar Reporte Pedido
    @GetMapping("/pedido/{id}")
    public void exportarReportePedido(@PathVariable("id") int idPedido, HttpServletResponse response) {
        try {
            InputStream jasperStream = getClass().getResourceAsStream("/reportes/individual.jasper");
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
            response.setHeader("Content-Disposition", "inline; filename=pedido_" + idPedido + ".pdf");
            JasperExportManager.exportReportToPdfStream(jasperPrint, response.getOutputStream());

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    
    //Eliminar detalle de pedido
    @PostMapping("/eliminarDetallePedido")
    public String eliminarDetallePedido(
            @RequestParam("idPedido") int idPedido,
            @RequestParam("idProducto") int idProducto,
            @RequestParam("seccion") String seccion,
            RedirectAttributes redirect) {

        DetallePedidoId id = new DetallePedidoId(idPedido, idProducto);

        // 1. Obtener el detalle del pedido antes de eliminarlo
        DetallePedido detalle = detallePedidoService.buscarPorId(id); // Este método debe existir
        if (detalle == null) {
            redirect.addFlashAttribute("error", "No se encontró el detalle del pedido.");
            return "redirect:/cliente/inicio?seccion=" + seccion;
        }

        // 2. Recuperar producto y actualizar stock
        Producto producto = productoService.buscarPorId(idProducto);
        if (producto == null) {
            redirect.addFlashAttribute("error", "No se encontró el producto.");
            return "redirect:/cliente/inicio?seccion=" + seccion;
        }

        int cantidadRecuperada = detalle.getCantidad(); // Asumiendo que DetallePedido tiene un campo `cantidad`
        producto.setStock(producto.getStock() + cantidadRecuperada);
        productoService.actualizarProducto(producto); // Asegúrate de que este método persista el cambio

        // 3. Eliminar el detalle del pedido
        detallePedidoService.eliminar(id);

        redirect.addFlashAttribute("mensaje", "El producto '" + producto.getNomProd() +
                "' ha sido cancelado y su stock (" + cantidadRecuperada + ") ha sido restaurado.");
        return "redirect:/cliente/inicio?seccion=" + seccion;
    }
}