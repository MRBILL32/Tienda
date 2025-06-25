package com.tienda.serviceImpl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.tienda.entity.Rol;
import com.tienda.repository.RolRepository;
import com.tienda.service.RolService;

@Service
public class RolServiceImpl implements RolService{

    private final RolRepository rolRepository;

    public RolServiceImpl(RolRepository rolRepository) {
        this.rolRepository = rolRepository;
    }

    @Override
    public List<Rol> listarRoles() {
        return rolRepository.listarRoles();
    }
}
