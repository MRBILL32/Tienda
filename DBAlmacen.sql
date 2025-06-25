CREATE DATABASE IF NOT EXISTS DBAlmacen;
USE DBAlmacen;

-- Tabla Rol
CREATE TABLE rol (
    idRol INT AUTO_INCREMENT PRIMARY KEY,
    tipoRol VARCHAR(20) NOT NULL
);

-- Tabla Usuario
CREATE TABLE usuario (
    idUser INT AUTO_INCREMENT PRIMARY KEY,
    nombres VARCHAR(100) NOT NULL,
    apellidos VARCHAR(100) NOT NULL,
    dni VARCHAR(8) NOT NULL,
    idRol INT,
    login VARCHAR(30) NOT NULL,
    password VARCHAR(20) NOT NULL,
    correo VARCHAR(150) NOT NULL UNIQUE,
    estado VARCHAR(20) NOT NULL DEFAULT 'Aprobado',
    CONSTRAINT fk_usuario_rol FOREIGN KEY (idRol) REFERENCES rol(idRol)
);

-- Tabla Categoria
CREATE TABLE categoria (
    idCate INT AUTO_INCREMENT PRIMARY KEY,
    nomCate VARCHAR(50) NOT NULL
);

-- Tabla Producto
CREATE TABLE producto (
    idProd INT AUTO_INCREMENT PRIMARY KEY,
    nomProd VARCHAR(200) NOT NULL,
    marcaProd VARCHAR(50) NOT NULL,
    precioUnit DECIMAL(10,2) NOT NULL,
    stock INT NOT NULL,
    idCate INT,
    activo BIT NOT NULL DEFAULT 1,
    CONSTRAINT fk_producto_categoria FOREIGN KEY (idCate) REFERENCES categoria(idCate)
);

-- Tabla Pedido
CREATE TABLE pedido (
    idPedido INT AUTO_INCREMENT PRIMARY KEY,
    idUser INT,
    fecha DATE,
    total DECIMAL(12,2),
    estado VARCHAR(20) DEFAULT 'Pendiente',
    CONSTRAINT fk_pedido_usuario FOREIGN KEY (idUser) REFERENCES usuario(idUser)
);

-- Tabla DetallePedido
CREATE TABLE detallePedido (
    idPedido INT,
    idProd INT,
    cantidad INT,
    precioUnit DECIMAL(10,2),
    PRIMARY KEY (idPedido, idProd),
    CONSTRAINT fk_detallePedido_pedido FOREIGN KEY (idPedido) REFERENCES pedido(idPedido),
    CONSTRAINT fk_detallePedido_producto FOREIGN KEY (idProd) REFERENCES producto(idProd)
);

-- Tabla Carrito
CREATE TABLE carrito (
    idCarrito INT AUTO_INCREMENT PRIMARY KEY,
    idUser INT,
    fechaCreacion DATETIME,
    CONSTRAINT fk_carrito_usuario FOREIGN KEY (idUser) REFERENCES usuario(idUser)
);

-- Tabla DetalleCarrito
CREATE TABLE detalleCarrito (
    idDetalleCarrito INT AUTO_INCREMENT PRIMARY KEY,
    idCarrito INT NOT NULL,
    idProd INT NOT NULL,
    cantidad INT NOT NULL,
    precioUnit DECIMAL(10,2) NOT NULL,
    subtotal DECIMAL(10,2) GENERATED ALWAYS AS (cantidad * precioUnit) STORED,
    CONSTRAINT fk_detalleCarrito_carrito FOREIGN KEY (idCarrito) REFERENCES carrito(idCarrito),
    CONSTRAINT fk_detalleCarrito_croducto FOREIGN KEY (idProd) REFERENCES producto(idProd)
);


