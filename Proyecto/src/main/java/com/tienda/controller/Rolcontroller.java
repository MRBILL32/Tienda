package com.tienda.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.tienda.service.RolService;

@Controller
@RequestMapping("/rol")
public class Rolcontroller {

	@Autowired
	private RolService rolService;
	
	@GetMapping("/listar")
	public String listarRoles(Model model) {
		model.addAttribute("roles", rolService.listarRoles());
		return "rol/listar";
	}
	
}
