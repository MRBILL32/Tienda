package com.tienda.controller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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

import com.tienda.entity.DetalleCarrito;
import com.tienda.entity.DetallePedido;
import com.tienda.entity.Producto;
import com.tienda.entity.Usuario;
import com.tienda.service.DetallePedidoService;
import com.tienda.service.ProductoService;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/cliente")
public class ClienteController {

    @Autowired
    private ProductoService productoService;

    @Autowired
    private DetallePedidoService detallePedidoService;

    // Obtener el carrito desde sesión de forma segura
    private List<DetalleCarrito> obtenerCarritoDesdeSesion(HttpSession session) {
        Object carritoObj = session.getAttribute("carrito");
        if (carritoObj instanceof List<?>) {
            return ((List<?>) carritoObj).stream()
                    .filter(o -> o instanceof DetalleCarrito)
                    .map(o -> (DetalleCarrito) o)
                    .collect(Collectors.toList());
        }
        return new ArrayList<>();
    }

    // Listar todas las vistas
    @GetMapping("/inicio")
    public String dashboard(@RequestParam(value = "seccion", defaultValue = "productos") String seccion,
                            @RequestParam(value = "filtroProducto", required = false) String filtroProducto,
                            @RequestParam(value = "pageProducto", defaultValue = "1") int paginaProducto,
                            @RequestParam(value = "filtroDetallePedido", required = false) String filtroDetallePedido,
                            @RequestParam(value = "pageDetallePedido", defaultValue = "1") int paginaDetallePedido,
                            @RequestParam(value = "size", defaultValue = "8") int tamanio,
                            Model model, HttpSession session) {

        Usuario usuario = (Usuario) session.getAttribute("usuario");
        if (usuario == null) {
            return "redirect:/";
        }

        int idUser = usuario.getIdUser();
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
                                   HttpSession session, RedirectAttributes redirectAttrs) {
        
    	Producto producto = productoService.buscarPorId(idProd);
    	if (producto == null || Boolean.FALSE.equals(producto.isActivo()) || producto.getStock() <= 0) {
    	    redirectAttrs.addFlashAttribute("error", "Producto no válido o sin stock.");
    	    return "redirect:/cliente/inicio?seccion=productos";
    	}


        // Obtener carrito desde sesión
        Object carritoObj = session.getAttribute("carrito");
        List<DetalleCarrito> carrito;

        if (carritoObj instanceof List<?>) {
            carrito = ((List<?>) carritoObj).stream()
                .filter(o -> o instanceof DetalleCarrito)
                .map(o -> (DetalleCarrito) o)
                .collect(Collectors.toList());
        } else {
            carrito = new ArrayList<>();
        }

        // Verificar si ya existe en el carrito
        Optional<DetalleCarrito> itemExistente = carrito.stream()
            .filter(i -> i.getProducto().getIdProd() == idProd)
            .findFirst();

        int cantidadTotal = cantidad;
        if (itemExistente.isPresent()) {
            cantidadTotal += itemExistente.get().getCantidad();
        }

        // Validar que no exceda el stock
        if (cantidadTotal > producto.getStock()) {
            redirectAttrs.addFlashAttribute("error", "No puedes agregar más de lo disponible en stock.");
            return "redirect:/cliente/detalleProducto/" + idProd;
        }

        // Agregar o actualizar
        if (itemExistente.isPresent()) {
            DetalleCarrito existente = itemExistente.get();
            existente.setCantidad(cantidadTotal);
            existente.setPrecioUnit(producto.getPrecioUnit());
        } else {
            DetalleCarrito nuevo = new DetalleCarrito();
            nuevo.setProducto(producto);
            nuevo.setCantidad(cantidad);
            nuevo.setPrecioUnit(producto.getPrecioUnit());
            carrito.add(nuevo);
        }

        session.setAttribute("carrito", carrito);
        redirectAttrs.addFlashAttribute("mensaje", "Producto agregado al carrito.");
        return "redirect:/cliente/inicio?seccion=productos";
    }


    // Ver carrito
    @GetMapping("/verCarrito")
    public String verCarrito(HttpSession session, Model model) {
        List<DetalleCarrito> carrito = obtenerCarritoDesdeSesion(session);
        BigDecimal total = carrito.stream()
                .map(DetalleCarrito::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        model.addAttribute("carrito", carrito);
        model.addAttribute("total", total);
        return "cliente/verCarrito";
    }

    // Eliminar producto del carrito
    @PostMapping("/eliminarDelCarrito")
    public String eliminarDelCarrito(@RequestParam("idProd") int idProd, HttpSession session) {
        List<DetalleCarrito> carrito = obtenerCarritoDesdeSesion(session);
        carrito.removeIf(item -> item.getProducto().getIdProd() == idProd);
        session.setAttribute("carrito", carrito);
        return "redirect:/cliente/verCarrito";
    }

    // Vaciar carrito
    @PostMapping("/vaciarCarrito")
    public String vaciarCarrito(HttpSession session) {
        session.removeAttribute("carrito");
        return "redirect:/cliente/verCarrito";
    }
}
