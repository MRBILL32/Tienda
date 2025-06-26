package com.tienda.controller;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
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

import jakarta.servlet.http.HttpSession;

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
		    return "redirect:/";
		}
		model.addAttribute("idActual", logueado.getIdUser());

        int idUser = logueado.getIdUser();
        model.addAttribute("seccionActiva", seccion);

        if (seccion.equals("productos")) {
            Page<Producto> productosPage = (filtroProducto == null || filtroProducto.isBlank())
                    ? productoService.buscarActivoPorFiltro("", paginaProducto - 1, tamanio, true)
                    : productoService.buscarActivoPorFiltro(filtroProducto, paginaProducto - 1, tamanio, true);

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

            model.addAttribute("detallePedidos", detallePedidosPage.getContent());
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
    public String verCarrito(HttpSession session, Model model) {
        Usuario usuario = (Usuario) session.getAttribute("usuarioLogueado");

        if (usuario == null) {
            return "redirect:/";
        }

        Carrito carrito = carritoService.buscarPorUsuario(usuario.getIdUser());
        List<DetalleCarrito> detalle = detalleCarritoService.listarPorCarrito(carrito.getIdCarrito());

        BigDecimal total = detalle.stream()
                .map(DetalleCarrito::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

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

        redirectAttrs.addFlashAttribute("mensaje", "Compra realizada con éxito.");
        return "redirect:/cliente/inicio?seccion=detallePedidos";
    }


    
}
