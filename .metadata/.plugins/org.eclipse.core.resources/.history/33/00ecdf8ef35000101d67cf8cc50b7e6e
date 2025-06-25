package com.tienda.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Id;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "usuario")
public class Usuario implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idUser")
    private Integer idUser;
	
	@Column(name = "nombres", length = 100, nullable = false )
	@Size(max = 100, message = "Maximo 100 Caracters")
	private String nombres;
	
	@Column(name = "apellidos", length = 100, nullable = false)
	@Size(max = 100, message = "Maximo 100 Caracteres")
	private String apellidos;
	
	@Column(name = "dni", length = 8, nullable = false)
	@Size(max = 8, message = "Maximo 8 caracteres")
	private String dni;
	
	//Relacion con tabla Rol
	@ManyToOne
	@JoinColumn(name = "idRol")
	private Rol rol;
	
	@Column(name = "login", length = 30, nullable = false)
	@Size(max = 30, message = "Maximo 30 caracteres")
	private String login;
	
	@Column(name = "password", length = 20, nullable = false)
	@Size(max = 20, message = "Maximo 20 caracteres")
	private String password;

	@Column(name = "correo", length = 150, nullable = false)
	@Size(max = 150, message = "Maximo 150 caracteres")
	private String correo;
	
	@Column(name = "estado", length = 20, nullable = false)
	@Size(max = 20, message = "Maximo 20 caracteres")
	private String estado = "Pendiente";
	
	//Relacion con tabla Pedido
	@OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL)
	@JsonIgnore
	private List<Pedido> pedidos = new ArrayList<>();

	//Relacion con tabla Carrito
	@OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL)
	@JsonIgnore
	private List<Carrito> carritos = new ArrayList<>();

	
	
	// Constructor vacío
    public Usuario() {
    }

    // Constructor básico para login
    public Usuario(String login, String password) {
        this.login = login;
        this.password = password;
    }

    // Constructor para registro
    public Usuario(String nombres, String apellidos, String dni, String login, String password, String correo, Rol rol) {
        this.nombres = nombres;
        this.apellidos = apellidos;
        this.dni = dni;
        this.login = login;
        this.password = password;
        this.correo = correo;
        this.rol = rol;
    }

    // Constructor para pruebas o admin
    public Usuario(String nombres, String apellidos, String dni, String login, String password, String correo, Rol rol, String estado) {
        this.nombres = nombres;
        this.apellidos = apellidos;
        this.dni = dni;
        this.login = login;
        this.password = password;
        this.correo = correo;
        this.rol = rol;
        this.estado = estado;
    }

    // Constructor para resumen de usuarios
    public Usuario(String nombres, Rol rol) {
        this.nombres = nombres;
        this.rol = rol;
    }
	
}
