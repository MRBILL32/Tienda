package com.tienda.serviceImpl;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.tienda.entity.Pedido;
import com.tienda.repository.PedidoRepository;
import com.tienda.service.PedidoService;

@Service
public class PedidoServiceImpl implements PedidoService{

    private final PedidoRepository pedidoRepository;

    public PedidoServiceImpl(PedidoRepository pedidoRepository) {
        this.pedidoRepository = pedidoRepository;
    }

    @Override
    public Pedido guardar(Pedido pedido) {
        return pedidoRepository.save(pedido);
    }

    @Override
    public Optional<Pedido> buscarPorId(Integer idPedido) {
        return pedidoRepository.findById(idPedido);
    }

    @Override
    public List<Pedido> listarTodos() {
        return pedidoRepository.findAll();
    }

    @Override
    public List<Pedido> listarPorUsuario(Integer idUser) {
        return pedidoRepository.listarPorUsuario(idUser);
    }

    @Override
    public List<Pedido> listarPorEstado(String estado) {
        return pedidoRepository.listarPorEstado(estado);
    }

    @Override
    public Pedido cambiarEstado(Integer idPedido, String nuevoEstado) {
        Optional<Pedido> optional = pedidoRepository.findById(idPedido);
        if (optional.isPresent()) {
            Pedido pedido = optional.get();
            pedido.setEstado(nuevoEstado);
            return pedidoRepository.save(pedido);
        } else {
            throw new RuntimeException("Pedido con ID: " + idPedido + " no encontrado");
        }
    }
}
