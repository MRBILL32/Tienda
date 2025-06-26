package com.tienda.serviceImpl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tienda.entity.Carrito;
import com.tienda.repository.CarritoRepository;
import com.tienda.repository.DetalleCarritoRepository;
import com.tienda.service.CarritoService;

@Service
public class CarritoServiceImpl implements CarritoService {

    private final DetalleCarritoRepository detalleCarritoRepository;

    @Autowired
    private CarritoRepository carritoRepository;

    CarritoServiceImpl(DetalleCarritoRepository detalleCarritoRepository) {
        this.detalleCarritoRepository = detalleCarritoRepository;
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
        return carritoRepository.findAll();
    }

    @Override
    public Optional<Carrito> buscarPorId(Integer idCarrito) {
        return carritoRepository.findById(idCarrito);
    }

    @Override
    public void eliminarPorId(Integer idCarrito) {
        carritoRepository.deleteById(idCarrito);
    }

    @Override
    public Carrito obtenerCarritoActivoPorUsuario(int idUsuario) {
        return carritoRepository.buscarCarritoActivoPorUsuario(idUsuario);
    }
    
    @Override
    public Carrito buscarPorUsuario(int idUser) {
        return carritoRepository.buscarPorUsuario(idUser);
    }


    @Override
    @Transactional
    public void vaciarCarrito(int idCarrito) {
        detalleCarritoRepository.eliminarPorCarrito(idCarrito);
    }

}
