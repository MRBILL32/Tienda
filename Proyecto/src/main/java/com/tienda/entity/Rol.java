package com.tienda.entity;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Id;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@Entity
@Table(name = "rol")
public class Rol {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "idRol")
	private Integer idRol;
	
	@Column(name = "tipoRol")
	private String tipoRol;
	
	//Relacion con tabla Usuario
	@OneToMany(mappedBy = "rol", cascade = CascadeType.ALL)
	@JsonIgnore
	private List<Usuario> usuarios = new ArrayList<>();

}
