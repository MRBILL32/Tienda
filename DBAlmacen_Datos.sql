use DBAlmacen;
-- Tabla Rol
INSERT INTO rol (tipoRol) VALUES
('Administrador'),
('Cliente'),
('Empleado');

-- Tabla Usuario (10)
INSERT INTO usuario (nombres, apellidos, dni, idRol, login, password, correo, estado) VALUES
('Juan', 'Pérez', '10000001', 1, 'admin1', 'admin123', 'admin1@email.com', 'Aprobado'),
('Laura', 'González', '10000002', 2, 'cliente1', 'cliente123', 'laura1@email.com', 'Aprobado'),
('Carlos', 'Ramírez', '10000003', 3, 'empleado1', 'empleado123', 'carlos1@email.com', 'Pendiente'),
('Ana', 'Torres', '10000004', 2, 'ana', 'ana123', 'ana@email.com', 'Aprobado'),
('Luis', 'Mendoza', '10000005', 2, 'luis', 'luis123', 'luis@email.com', 'Pendiente'),
('Marta', 'Lopez', '10000006', 3, 'marta', 'marta123', 'marta@email.com', 'Aprobado'),
('Pedro', 'Diaz', '10000007', 2, 'pedro', 'pedro123', 'pedro@email.com', 'Aprobado'),
('Bill', 'Clave', '10000008', 3, 'bill', '12345', 'bills@email.com', 'Aprobado'),
('Lucía', 'Zamora', '10000009', 2, 'lucia', 'lucia123', 'lucia@email.com', 'Pendiente'),
('Diego', 'Nuñez', '10000010', 3, 'diego', 'diego123', 'diego@email.com', 'Aprobado');

-- Tabla Categoria
INSERT INTO categoria (nomCate) VALUES 
('Bebidas'),
('Limpieza'),
('Snacks');

-- Tabla Producto (10)
INSERT INTO producto (nomProd, marcaProd, precioUnit, stock, idCate, activo) VALUES
('Coca-Cola 500ml', 'Coca-Cola', 2.50, 100, 1, 1),
('Pepsi 500ml', 'Pepsi', 2.30, 90, 1, 1),
('Agua Cielo 1L', 'Cielo', 1.00, 150, 1, 1),
('Detergente Ariel 1L', 'Ariel', 5.00, 50, 2, 1),
('Jabón Bolívar', 'Bolívar', 3.80, 70, 2, 1),
('Papas Lays', 'Lays', 1.20, 200, 3, 1),
('Chifles Karinto', 'Karinto', 1.00, 180, 3, 1),
('Inka Kola 500ml', 'Inka Kola', 2.60, 110, 1, 1),
('Cloro Sapolio', 'Sapolio', 2.50, 60, 2, 1),
('Galletas Oreo', 'Oreo', 1.50, 90, 3, 1);

-- Tabla Pedido (10) - asignados a usuario ID 2 y 4 (Laura y Ana)
INSERT INTO pedido (idUser, fecha, total, estado) VALUES
(2, '2025-06-01', 7.50, 'Pendiente'),
(2, '2025-06-15', 2.50, 'Aprobado'),
(4, '2025-06-05', 3.80, 'Pendiente'),
(4, '2025-06-10', 5.00, 'Aprobado'),
(2, '2025-06-18', 4.60, 'Pendiente'),
(4, '2025-06-20', 2.40, 'Aprobado'),
(2, '2025-06-21', 6.00, 'Pendiente'),
(4, '2025-06-22', 5.50, 'Aprobado'),
(2, '2025-06-23', 3.00, 'Pendiente'),
(4, '2025-06-24', 4.70, 'Aprobado');

-- Tabla DetallePedido (10)
INSERT INTO detallePedido (idPedido, idProd, cantidad, precioUnit) VALUES
(1, 1, 2, 2.50),
(1, 6, 1, 1.20),
(2, 1, 1, 2.50),
(3, 5, 1, 3.80),
(4, 4, 1, 5.00),
(5, 3, 2, 1.00),
(6, 9, 1, 2.40),
(7, 2, 2, 2.30),
(8, 10, 2, 1.50),
(9, 7, 3, 1.00);

-- Tabla Carrito (10) - 5 usuarios distintos
INSERT INTO carrito (idUser, fechaCreacion) VALUES
(2, NOW()),
(4, NOW()),
(5, NOW()),
(6, NOW()),
(7, NOW()),
(8, NOW()),
(9, NOW()),
(10, NOW()),
(1, NOW()),
(3, NOW());

-- Tabla DetalleCarrito (10)
INSERT INTO detalleCarrito (idCarrito, idProd, cantidad, precioUnit) VALUES
(1, 4, 1, 5.00),
(1, 6, 3, 1.20),
(2, 2, 2, 2.30),
(3, 8, 1, 2.60),
(4, 9, 1, 2.50),
(5, 7, 2, 1.00),
(6, 3, 4, 1.00),
(7, 5, 1, 3.80),
(8, 10, 2, 1.50),
(9, 1, 1, 2.50);