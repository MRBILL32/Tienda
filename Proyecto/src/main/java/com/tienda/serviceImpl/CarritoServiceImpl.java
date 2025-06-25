package com.tienda.serviceImpl;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.tienda.entity.Carrito;
import com.tienda.repository.CarritoRepository;
import com.tienda.service.CarritoService;

@Service
public class CarritoServiceImpl implements CarritoService {

    private final CarritoRepository carritoRepository;

    public CarritoServiceImpl(CarritoRepository carritoRepository) {
        this.carritoRepository = carritoRepository;
    }

    @Override
    public Carrito guardar(Carrito carrito) {
        return carritoRepository.save(carrito);
    }

    @Override
    public List<Carrito> listarPorUsuario(Integer idUser) {
        return carritoRepository.listarPorUsuario(idUser);
    }

    @Override
    public List<Carrito> listarTodo() {
        return carritoRepository.listarTodoOrdenado();
    }

    @Override
    public Optional<Carrito> buscarPorId(Integer idCarrito) {
        return carritoRepository.findById(idCarrito);
    }

    @Override
    public void eliminarPorId(Integer idCarrito) {
        carritoRepository.deleteById(idCarrito);
    }
}