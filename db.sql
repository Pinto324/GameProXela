-- Usuarios que controlaran la DB
CREATE USER admin WITH PASSWORD 'adminpass';
CREATE USER lector WITH PASSWORD 'lectorpass';
CREATE USER modificador WITH PASSWORD 'modpass';

CREATE DATABASE "GamerProXela"
    WITH
    OWNER = "admin"
    ENCODING = 'UTF8'
    LOCALE_PROVIDER = 'libc'
    CONNECTION LIMIT = -1
    IS_TEMPLATE = False;

CREATE SCHEMA IF NOT EXISTS gamerprosc;

CREATE TABLE IF NOT EXISTS gamerprosc.clientes (
  id_cliente serial NOT NULL PRIMARY KEY,
  nit integer,
  nombre varchar,
  tipo_tarjeta smallint,
  puntos integer,
  fecha_tarjeta date
);

CREATE TABLE IF NOT EXISTS gamerprosc.detalle_ventas (
  id serial NOT NULL PRIMARY KEY,
  no_factura integer,
  id_producto integer,
  cantidad integer,
  precio_u decimal(10, 2)
);

CREATE TABLE IF NOT EXISTS gamerprosc.empleados (
  id serial NOT NULL PRIMARY KEY,
  nombre varchar,
  rol smallint,
  username varchar,
  pass varchar,
  id_sucursal integer,
  no_caja smallint
);

CREATE TABLE IF NOT EXISTS gamerprosc.productos (
  id_producto serial NOT NULL PRIMARY KEY,
  nombre varchar,
  precio decimal(10, 2)
);

CREATE TABLE IF NOT EXISTS gamerprosc.productos_sucursal (
  id serial NOT NULL PRIMARY KEY,
  id_producto integer,
  id_sucursal integer,
  stock_bodega integer,
  stock_estanteria integer,
  pasillo integer
);

CREATE TABLE IF NOT EXISTS gamerprosc.sucursales (
  id_sucursal serial NOT NULL PRIMARY KEY,
  ubicacion varchar
);

CREATE TABLE IF NOT EXISTS gamerprosc.ventas (
  no_factura serial PRIMARY KEY,
  fecha date,
  id_cliente integer,
  id_cajero integer,
  nit boolean,
  sucursal integer,
  total_sind decimal(10, 2),
  descuento integer,
  total_cond decimal(10, 2)
);

ALTER TABLE gamerprosc.detalle_ventas ADD CONSTRAINT detalle_ventas_id_producto_fk FOREIGN KEY (id_producto) REFERENCES gamerprosc.productos (id_producto);
ALTER TABLE gamerprosc.detalle_ventas ADD CONSTRAINT detalle_ventas_no_factura_fk FOREIGN KEY (no_factura) REFERENCES gamerprosc.ventas (no_factura);
ALTER TABLE gamerprosc.empleados ADD CONSTRAINT empleados_id_sucursal_fk FOREIGN KEY (id_sucursal) REFERENCES gamerprosc.sucursales (id_sucursal);
ALTER TABLE gamerprosc.productos_sucursal ADD CONSTRAINT productos_sucursal_id_producto_fk FOREIGN KEY (id_producto) REFERENCES gamerprosc.productos (id_producto);
ALTER TABLE gamerprosc.productos_sucursal ADD CONSTRAINT productos_sucursal_id_sucursal_fk FOREIGN KEY (id_sucursal) REFERENCES gamerprosc.sucursales (id_sucursal);
ALTER TABLE gamerprosc.ventas ADD CONSTRAINT ventas_id_cajero_fk FOREIGN KEY (id_cajero) REFERENCES gamerprosc.empleados (id);
ALTER TABLE gamerprosc.ventas ADD CONSTRAINT ventas_id_cliente_fk FOREIGN KEY (id_cliente) REFERENCES gamerprosc.clientes (id_cliente);

--seleccionar una secuencia
SELECT last_value FROM gamerprosc.ventas_no_factura_seq;
--Views:
--Encargada del traer la información para las utilidades de inventario:
CREATE OR REPLACE VIEW gamerprosc.vista_inventario_rellenarinfo AS
SELECT ps.id, p.nombre, p.precio, ps.id_producto, ps.id_sucursal, ps.stock_bodega, ps.stock_estanteria, ps.pasillo
FROM gamerprosc.productos p
JOIN gamerprosc.productos_sucursal ps ON p.id_producto = ps.id_producto
JOIN gamerprosc.sucursales s ON ps.id_sucursal = s.id_sucursal;

--Encargada de mostrar solo nits de clientes:
CREATE OR REPLACE VIEW gamerprosc.vista_nit_clientes AS
SELECT id_cliente, nit
FROM gamerprosc.clientes;

--Encargada de darme los datos para modificar clientes:
CREATE OR REPLACE VIEW gamerprosc.vista_modificar_clientes AS
SELECT id_cliente, nit, nombre, tipo_tarjeta
FROM gamerprosc.clientes;

--Encargada de darme los datos para ventas:
CREATE OR REPLACE VIEW gamerprosc.vista_venta_productos AS
SELECT id_cliente, nit, nombre, tipo_tarjeta
FROM gamerprosc.clientes;
-------------------
--vista para reportes
--top 2 sucursales
--Encargada de mostrar solo nits de clientes:
CREATE OR REPLACE VIEW gamerprosc.mejores_2_sucursales AS
SELECT s.ubicacion,  SUM(v.total_cond) AS total_acumulado
FROM gamerprosc.ventas v
JOIN gamerprosc.sucursales s ON v.sucursal = s.id_sucursal
GROUP BY s.ubicacion
ORDER BY total_acumulado DESC
LIMIT 2;

-----------------------------------------------------------------------
--Funciones:
--encargada del manejo de la view inventario_rellenarinfo
CREATE OR REPLACE FUNCTION gamerprosc.filtrar_rellenarinfo(_id_sucursal integer)
RETURNS TABLE(
	  id_ps integer,
    nombre varchar,
    precio decimal(10, 2),
    id_producto integer,
    id_sucursal integer,
    stock_bodega integer,
    stock_estanteria integer,
    pasillo integer
) AS $$
BEGIN
    RETURN QUERY
    SELECT 
		    v.id,
        v.nombre,
        v.precio,
        v.id_producto,
        v.id_sucursal,
        v.stock_bodega,
        v.stock_estanteria,
        v.pasillo
    FROM 
        gamerprosc.vista_inventario_rellenarinfo v
    WHERE
        v.id_sucursal = _id_sucursal;
END;
$$ LANGUAGE plpgsql;

SELECT * FROM gamerprosc.filtrar_rellenarinfo(1);

--encargada de verificar que hayan suficientes productos en bodega y eliminar para mover a estanteria
CREATE OR REPLACE FUNCTION gamerprosc.actualizarinventario(_id integer,
    _cantidad integer, _pasillo integer)
RETURNS void AS $$
DECLARE
  stock_bodega_actual integer;
BEGIN
  SELECT stock_bodega INTO stock_bodega_actual FROM gamerprosc.productos_sucursal WHERE id=_id;
  IF _cantidad > stock_bodega_actual THEN
    RAISE EXCEPTION 'cantidad insuficiente, actualmente solo hay %',v.stock_bodega;
  END IF;
  UPDATE gamerprosc.productos_sucursal 
  SET stock_bodega = stock_bodega - _cantidad
  WHERE id=_id;
  UPDATE gamerprosc.productos_sucursal 
  SET stock_estanteria = stock_estanteria + _cantidad
  WHERE id=_id;
  UPDATE gamerprosc.productos_sucursal 
  SET pasillo = _pasillo
  WHERE id=_id;
END;
$$ LANGUAGE plpgsql;

--encargada de modificiar bodega
CREATE OR REPLACE FUNCTION gamerprosc.aumentarBodega(_id integer,
    _cantidad integer)
RETURNS void AS $$
DECLARE
  stock_bodega_actual integer;
BEGIN
  UPDATE gamerprosc.productos_sucursal 
  SET stock_bodega = stock_bodega + _cantidad
  WHERE id=_id;
END;
$$ LANGUAGE plpgsql;

---Encargada de actualizar información de un cliente sin tarjeta previa
CREATE OR REPLACE FUNCTION gamerprosc.actualizar_cliente_sin_tarjeta(
    p_id_cliente INT,
    p_nombre VARCHAR,
    p_nit INT,
    p_tipo_tarjeta SMALLINT,
    p_fecha_tarjeta DATE
)
RETURNS VOID AS $$
BEGIN
    UPDATE gamerprosc.clientes
    SET 
        nit = p_nit,
        nombre = p_nombre,
        tipo_tarjeta = p_tipo_tarjeta,
        fecha_tarjeta = p_fecha_tarjeta
    WHERE id_cliente = p_id_cliente;
END;
$$ LANGUAGE plpgsql;
---Encargada de actualizar información de un cliente con tarjeta previa
CREATE OR REPLACE FUNCTION gamerprosc.actualizar_cliente_con_tarjeta(
    p_id_cliente INT,
    p_nombre VARCHAR,
    p_nit INT
)
RETURNS VOID AS $$
BEGIN
    UPDATE gamerprosc.clientes
    SET 
        nit = p_nit,
        nombre = p_nombre
    WHERE id_cliente = p_id_cliente;
END;
$$ LANGUAGE plpgsql;
--encargada de insertar las ventas
CREATE OR REPLACE FUNCTION gamerprosc.funcion_insertar_ventas(
    fecha DATE,
    id_cliente INT,
    id_cajero INT,
    nit boolean,
    sucursal integer,
    total_sind decimal(10, 2),
    descuento integer
)
RETURNS VOID AS $$
BEGIN
    INSERT INTO gamerprosc.ventas (fecha, id_cliente, id_cajero, nit,sucursal, total_sind, descuento, total_cond)
    VALUES (fecha, id_cliente, id_cajero, nit,sucursal, total_sind,descuento, total_sind-descuento);
END;
$$ LANGUAGE plpgsql;

--encargada de insertar los detalles de ventas
CREATE OR REPLACE FUNCTION gamerprosc.funcion_insertar_detalles_de_ventas(
  no_factura integer,
  id_producto integer,
  cantidad integer,
  precio_u decimal(10, 2)
)
RETURNS VOID AS $$
BEGIN   
    INSERT INTO gamerprosc.detalle_ventas (no_factura, id_producto, cantidad, precio_u)
    VALUES (no_factura, id_producto, cantidad, precio_u);
END;
$$ LANGUAGE plpgsql;
--encargada de borrar ventas:
CREATE OR REPLACE FUNCTION gamerprosc.eliminar_venta(p_no_factura INT)
RETURNS VOID AS $$
BEGIN 
    IF EXISTS (SELECT 1 FROM gamerprosc.detalle_ventas WHERE no_factura = p_no_factura) THEN  
        DELETE FROM gamerprosc.detalle_ventas WHERE no_factura = p_no_factura;
    END IF;

    
    IF EXISTS (SELECT 1 FROM gamerprosc.ventas WHERE no_factura = p_no_factura) THEN
        DELETE FROM gamerprosc.ventas WHERE no_factura = p_no_factura;
    END IF;
END;
$$ LANGUAGE plpgsql;

--encargada de traer los puntos de un cliente en especifico
CREATE OR REPLACE FUNCTION gamerprosc.puntos_cliente(_id_cliente integer)
RETURNS TABLE(
	  puntos integer
) AS $$
BEGIN
    RETURN QUERY
    SELECT 
		    v.puntos
    FROM 
        gamerprosc.clientes v
    WHERE
        v.id_cliente = _id_cliente;
END;
$$ LANGUAGE plpgsql;
--encargada de insertar usuarios:
CREATE OR REPLACE FUNCTION gamerprosc.funcion_insertar_empleados(
  nombre varchar,
  rol smallint,
  username varchar,
  pass varchar,
  id_sucursal integer,
  no_caja smallint
)
RETURNS VOID AS $$
BEGIN 
    INSERT INTO gamerprosc.empleados (nombre, rol, username, pass, id_sucursal, no_caja)
    VALUES (nombre, rol, username, pass, id_sucursal, no_caja);
END;
$$ LANGUAGE plpgsql;
----------------------------------------------------------------
--funcion para reportes:
CREATE OR REPLACE FUNCTION gamerprosc.reporte_descuentos(fecha_inicio DATE, fecha_fin DATE)
RETURNS TABLE (
  no_factura INT,
  fecha DATE,
  nombre_cliente VARCHAR,
  nombre VARCHAR,
  nit INT,
  consumidor boolean,
  total_sind DECIMAL(10, 2),
  descuento INT,
  total_cond DECIMAL(10, 2)
) AS $$
BEGIN
  RETURN QUERY
  SELECT 
    v.no_factura AS no_factura,
    v.fecha AS fecha,
    c.nombre AS nombre_cliente,
    e.nombre AS nombre,
    c.nit AS nit,
    v.nit AS consumidor,
    v.total_sind AS total_sind,
    v.descuento AS descuento,
    v.total_cond AS total_cond
  FROM 
    gamerprosc.ventas v
  JOIN 
    gamerprosc.clientes c ON v.id_cliente = c.id_cliente
  JOIN 
    gamerprosc.empleados e ON v.id_cajero = e.id
  WHERE 
    v.descuento > 0 AND
    v.fecha BETWEEN fecha_inicio AND fecha_fin
  ORDER BY 
    v.fecha;
END;
$$ LANGUAGE plpgsql;
--top 10 ventas:
CREATE OR REPLACE FUNCTION gamerprosc.top_10_ventas(fecha_inicio DATE, fecha_fin DATE)
RETURNS TABLE ( 
  no_factura INT,
  fecha DATE,
  nombre_cliente VARCHAR,
  nombre VARCHAR,
  nit INT,
  consumidor boolean,
  total_cond DECIMAL(10, 2)
) AS $$
BEGIN
  RETURN QUERY
  SELECT 
    v.no_factura as no_factura,
    v.fecha as fecha,
    c.nombre AS nombre_cliente,
    e.nombre AS nombre,
    c.nit AS nit,
    v.nit AS consumidor,
    v.total_cond
  FROM 
    gamerprosc.ventas v
  JOIN 
    gamerprosc.clientes c ON v.id_cliente = c.id_cliente
  JOIN 
    gamerprosc.empleados e ON v.id_cajero = e.id
  WHERE 
    v.fecha BETWEEN fecha_inicio AND fecha_fin
  ORDER BY 
    v.total_cond DESC
LIMIT 10;
END;
$$ LANGUAGE plpgsql;


--------------------------------------------------------------------------------------------------------
--trigers:
--encargada de verificar que hayan suficientes productos en bodega y eliminar para mover a estanteria
CREATE OR REPLACE FUNCTION gamerprosc.verificar_nit_cliente()
RETURNS trigger AS $$
DECLARE
  nit_existente integer;
BEGIN
  PERFORM 1 
  FROM gamerprosc.vista_nit_clientes 
  WHERE nit = NEW.nit AND id_cliente != NEW.id_cliente;

  IF FOUND THEN
    RAISE EXCEPTION 'El NIT % ya existe', NEW.nit;
  END IF;
  RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trigger_verificar_nit
BEFORE INSERT OR UPDATE ON gamerprosc.clientes
FOR EACH ROW
EXECUTE FUNCTION gamerprosc.verificar_nit_cliente();

--encargada de verificar q hayan existencias en detalle venta y descontarlas
CREATE OR REPLACE FUNCTION gamerprosc.verificar_cantidad_venta()
RETURNS trigger AS $$
DECLARE
  stock_estanteria_actual integer;
  id_sucursal_actual integer;
BEGIN
  SELECT sucursal INTO id_sucursal_actual 
  FROM gamerprosc.ventas 
  WHERE no_factura = NEW.no_factura;

  SELECT stock_estanteria INTO stock_estanteria_actual 
  FROM gamerprosc.productos_sucursal 
  WHERE id_producto = NEW.id_producto AND id_sucursal = id_sucursal_actual;

  IF NEW.cantidad > stock_estanteria_actual THEN
    RAISE EXCEPTION 'Cantidad insuficiente, actualmente solo hay %', stock_estanteria_actual;
  END IF;

  UPDATE gamerprosc.productos_sucursal 
  SET stock_estanteria = stock_estanteria - NEW.cantidad
  WHERE id_producto = NEW.id_producto AND id_sucursal = id_sucursal_actual;

  RETURN NEW; 
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trigger_verificar_cantidad_venta
BEFORE INSERT ON gamerprosc.detalle_ventas
FOR EACH ROW
EXECUTE FUNCTION gamerprosc.verificar_cantidad_venta();
--trigger para regresar a estantería cuando se revierte una venta
CREATE OR REPLACE FUNCTION gamerprosc.actualizar_stock_eliminar_venta()
RETURNS trigger AS $$
BEGIN
    UPDATE gamerprosc.productos_sucursal
    SET stock_estanteria = stock_estanteria + OLD.cantidad
    WHERE id_producto = OLD.id_producto AND id_sucursal = (
        SELECT sucursal FROM gamerprosc.ventas WHERE no_factura = OLD.no_factura
    );
    RETURN OLD;  
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trigger_eliminar_venta
BEFORE DELETE ON gamerprosc.detalle_ventas
FOR EACH ROW
EXECUTE FUNCTION gamerprosc.actualizar_stock_eliminar_venta();
--trigger para restar los puntos de descuento usados si hace una venta
CREATE OR REPLACE FUNCTION gamerprosc.restar_puntos_venta()
RETURNS trigger AS $$
BEGIN
    UPDATE gamerprosc.clientes 
    SET puntos = puntos - NEW.descuento
    WHERE id_cliente = NEW.id_cliente;
    RETURN NEW;  
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trigger_restar_puntos
AFTER INSERT ON gamerprosc.ventas
FOR EACH ROW
EXECUTE FUNCTION gamerprosc.restar_puntos_venta();
--trigger para regresar los puntos de descuento usados si se revierte una venta
CREATE OR REPLACE FUNCTION gamerprosc.retornar_puntos_eliminar_venta()
RETURNS trigger AS $$
BEGIN
    UPDATE gamerprosc.clientes 
    SET puntos = puntos + OLD.descuento
    WHERE id_cliente = OLD.id_cliente;
    RETURN OLD;  
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trigger_retornar_puntos
AFTER DELETE ON gamerprosc.ventas
FOR EACH ROW
EXECUTE FUNCTION gamerprosc.retornar_puntos_eliminar_venta();
--trigger para sumar los puntos de una nueva venta 
CREATE OR REPLACE FUNCTION gamerprosc.sumar_puntos_venta()
RETURNS trigger AS $$
BEGIN
    -- Bronce
    IF (SELECT tipo_tarjeta FROM gamerprosc.clientes WHERE id_cliente = NEW.id_cliente) = 1 THEN
        UPDATE gamerprosc.clientes 
        SET puntos = puntos + CAST((NEW.total_cond/200)*5 AS integer)
        WHERE id_cliente = NEW.id_cliente;
    -- Oro
    ELSIF (SELECT tipo_tarjeta FROM gamerprosc.clientes WHERE id_cliente = NEW.id_cliente) = 2 THEN
        UPDATE gamerprosc.clientes 
        SET puntos = puntos + CAST((NEW.total_cond/200)*10 AS integer)
        WHERE id_cliente = NEW.id_cliente;
    -- Platino
    ELSIF (SELECT tipo_tarjeta FROM gamerprosc.clientes WHERE id_cliente = NEW.id_cliente) = 3 THEN
        UPDATE gamerprosc.clientes 
        SET puntos = puntos + CAST((NEW.total_cond/200)*20 AS integer)
        WHERE id_cliente = NEW.id_cliente;
    -- Diamante
    ELSIF (SELECT tipo_tarjeta FROM gamerprosc.clientes WHERE id_cliente = NEW.id_cliente) = 4 THEN
        UPDATE gamerprosc.clientes 
        SET puntos = puntos + CAST((NEW.total_cond/200)*30 AS integer)
        WHERE id_cliente = NEW.id_cliente;
    END IF;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trigger_sumar_puntos_venta
AFTER INSERT ON gamerprosc.ventas
FOR EACH ROW
EXECUTE FUNCTION gamerprosc.sumar_puntos_venta();

--trigger para restar los puntos si se aborta una venta 
CREATE OR REPLACE FUNCTION gamerprosc.restar_puntos_venta()
RETURNS trigger AS $$
BEGIN
    -- Bronce
    IF (SELECT tipo_tarjeta FROM gamerprosc.clientes WHERE id_cliente = OLD.id_cliente) = 1 THEN
        UPDATE gamerprosc.clientes 
        SET puntos = puntos - CAST((OLD.total_cond/200)*5 AS integer)
        WHERE id_cliente = OLD.id_cliente;
    -- Oro
    ELSIF (SELECT tipo_tarjeta FROM gamerprosc.clientes WHERE id_cliente = OLD.id_cliente) = 2 THEN
        UPDATE gamerprosc.clientes 
        SET puntos = puntos - CAST((OLD.total_cond/200)*10 AS integer)
        WHERE id_cliente = OLD.id_cliente;
    -- Platino
    ELSIF (SELECT tipo_tarjeta FROM gamerprosc.clientes WHERE id_cliente = OLD.id_cliente) = 3 THEN
        UPDATE gamerprosc.clientes 
        SET puntos = puntos - CAST((OLD.total_cond/200)*20 AS integer)
        WHERE id_cliente = OLD.id_cliente;
    -- Diamante
    ELSIF (SELECT tipo_tarjeta FROM gamerprosc.clientes WHERE id_cliente = OLD.id_cliente) = 4 THEN
        UPDATE gamerprosc.clientes 
        SET puntos = puntos - CAST((OLD.total_cond/200)*30 AS integer)
        WHERE id_cliente = OLD.id_cliente;
    END IF;
    RETURN OLD;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trigger_restar_puntos_venta
BEFORE DELETE ON gamerprosc.ventas
FOR EACH ROW
EXECUTE FUNCTION gamerprosc.restar_puntos_venta();

--permiso para usar el schema 
GRANT USAGE ON SCHEMA gamerprosc TO admin;
GRANT USAGE ON SCHEMA gamerprosc TO lector;
GRANT USAGE ON SCHEMA gamerprosc TO modificador;

GRANT ALL PRIVILEGES ON SEQUENCE gamerprosc.ventas_no_factura_seq TO lector;

GRANT ALL PRIVILEGES ON ALL TABLES IN SCHEMA gamerprosc TO admin;
GRANT ALL PRIVILEGES ON ALL SEQUENCES IN SCHEMA gamerprosc TO admin;

GRANT SELECT ON ALL TABLES IN SCHEMA gamerprosc TO lector;

GRANT SELECT, INSERT, UPDATE, DELETE ON ALL TABLES IN SCHEMA gamerprosc TO modificador;
GRANT ALL PRIVILEGES ON ALL SEQUENCES IN SCHEMA gamerprosc TO modificador;
GRANT EXECUTE ON ALL FUNCTIONS IN SCHEMA gamerprosc TO modificador;

--insert de las sucursales--
INSERT INTO gamerprosc.sucursales (ubicacion) 
VALUES 
    ('Parque'),
    ('Centro1'),
    ('Centro2');
  
INSERT INTO gamerprosc.clientes (nit , nombre ,tipo_tarjeta ,puntos ,fecha_tarjeta )
VALUES
  (11112233,'Luis Maldonado',0,0,NULL),
  (55552135,'Branson Jonson',0,0,NULL),
  (11114425,'Luis Perez',0,0,NULL),
  (11135480,'Javier Maldonado',0,0,NULL),
  (47806846,'Josué Melendez',1,0,'2024-07-11'),
  (78089845,'Pancho Villa',0,0,NULL),
  (18746850,'Jeferson Gutierritos',1,0,'2018-04-14'),
  (50505050,'Aldani Melendez',1,0,'2024-01-15');  

INSERT INTO gamerprosc.empleados (nombre ,rol,username,pass,id_sucursal, no_caja)
VALUES
   ('Cajero 1 Caja 1',2,'Cajero1Parque','fea740101dbb727886b6908e7bc196a55054374c6827b41a60081c2525975b4d',1,1),
('Cajero 2 Caja 2',2,'Cajero2Parque','fea740101dbb727886b6908e7bc196a55054374c6827b41a60081c2525975b4d',1,2),
('Cajero 3 Caja 3',2,'Cajero3Parque','fea740101dbb727886b6908e7bc196a55054374c6827b41a60081c2525975b4d',1,3),
('Cajero 4 Caja 4',2,'Cajero4Parque','fea740101dbb727886b6908e7bc196a55054374c6827b41a60081c2525975b4d',1,4),
('Cajero 5 Caja 5',2,'Cajero5Parque','fea740101dbb727886b6908e7bc196a55054374c6827b41a60081c2525975b4d',1,5),
('Cajero 6 Caja 6',2,'Cajero6Parque','fea740101dbb727886b6908e7bc196a55054374c6827b41a60081c2525975b4d',1,6),
('Cajero 1 Caja 1',2,'Cajero1Centro1','fea740101dbb727886b6908e7bc196a55054374c6827b41a60081c2525975b4d',2,1),
('Cajero 2 Caja 2',2,'Cajero2Centro1','fea740101dbb727886b6908e7bc196a55054374c6827b41a60081c2525975b4d',2,2),
('Cajero 3 Caja 3',2,'Cajero3Centro1','fea740101dbb727886b6908e7bc196a55054374c6827b41a60081c2525975b4d',2,3),
('Cajero 4 Caja 4',2,'Cajero4Centro1','fea740101dbb727886b6908e7bc196a55054374c6827b41a60081c2525975b4d',2,4),
('Cajero 5 Caja 5',2,'Cajero5Centro1','fea740101dbb727886b6908e7bc196a55054374c6827b41a60081c2525975b4d',2,5),
('Cajero 6 Caja 6',2,'Cajero6Centro1','fea740101dbb727886b6908e7bc196a55054374c6827b41a60081c2525975b4d',2,6),
('Cajero 1 Caja 1',2,'Cajero1Centro2','fea740101dbb727886b6908e7bc196a55054374c6827b41a60081c2525975b4d',3,1),
('Cajero 2 Caja 2',2,'Cajero2Centro2','fea740101dbb727886b6908e7bc196a55054374c6827b41a60081c2525975b4d',3,2),
('Cajero 3 Caja 3',2,'Cajero3Centro2','fea740101dbb727886b6908e7bc196a55054374c6827b41a60081c2525975b4d',3,3),
('Cajero 4 Caja 4',2,'Cajero4Centro2','fea740101dbb727886b6908e7bc196a55054374c6827b41a60081c2525975b4d',3,4),
('Cajero 5 Caja 5',2,'Cajero5Centro2','fea740101dbb727886b6908e7bc196a55054374c6827b41a60081c2525975b4d',3,5),
('Cajero 6 Caja 6',2,'Cajero6Centro2','fea740101dbb727886b6908e7bc196a55054374c6827b41a60081c2525975b4d',3,6),
('Bodega Parque',3,'BodegaParque','4320c94aae458be916a158a1b4a465345b1af5331e1bff8124b997a02bd15421',1,NULL),
('Bodega Centro1',3,'BodegaCentro1','4320c94aae458be916a158a1b4a465345b1af5331e1bff8124b997a02bd15421',2,NULL),
('Bodega Centro2',3,'BodegaCentro2','4320c94aae458be916a158a1b4a465345b1af5331e1bff8124b997a02bd15421',3,NULL),
('Inventario 1',4,'Inventario1Parque','98b400a96cb84215f4ced0b9c9bd7298f44dd36d6fe19b8751bd745eac926369',1,NULL),
('Inventario 2',4,'Inventario2Parque','98b400a96cb84215f4ced0b9c9bd7298f44dd36d6fe19b8751bd745eac926369',1,NULL),
('Inventario 3',4,'Inventario3Parque','98b400a96cb84215f4ced0b9c9bd7298f44dd36d6fe19b8751bd745eac926369',1,NULL),
('Inventario 4',4,'Inventario4Parque','98b400a96cb84215f4ced0b9c9bd7298f44dd36d6fe19b8751bd745eac926369',1,NULL),
('Inventario 1',4,'Inventario1Centro1','98b400a96cb84215f4ced0b9c9bd7298f44dd36d6fe19b8751bd745eac926369',2,NULL),
('Inventario 2',4,'Inventario2Centro1','98b400a96cb84215f4ced0b9c9bd7298f44dd36d6fe19b8751bd745eac926369',2,NULL),
('Inventario 3',4,'Inventario3Centro1','98b400a96cb84215f4ced0b9c9bd7298f44dd36d6fe19b8751bd745eac926369',2,NULL),
('Inventario 4',4,'Inventario4Centro1','98b400a96cb84215f4ced0b9c9bd7298f44dd36d6fe19b8751bd745eac926369',2,NULL),
('Inventario 1',4,'Inventario1Centro2','98b400a96cb84215f4ced0b9c9bd7298f44dd36d6fe19b8751bd745eac926369',3,NULL),
('Inventario 2',4,'Inventario2Centro2','98b400a96cb84215f4ced0b9c9bd7298f44dd36d6fe19b8751bd745eac926369',3,NULL),
('Inventario 3',4,'Inventario3Centro2','98b400a96cb84215f4ced0b9c9bd7298f44dd36d6fe19b8751bd745eac926369',3,NULL),
('Inventario 4',4,'Inventario4Centro2','98b400a96cb84215f4ced0b9c9bd7298f44dd36d6fe19b8751bd745eac926369',3,NULL),
('Admin',1,'admin','8c6976e5b5410415bde908bd4dee15dfb167a9c873fc4bb8a81f6f2ab448a918',1,NULL);


  INSERT INTO gamerprosc.productos (nombre, precio)
VALUES
  ('Consola Xbox Series X', 3899.95),
  ('Consola PlayStation 5', 3899.85),
  ('Nintendo Switch OLED', 2729.75),
  ('Consola Xbox Series S', 2339.65),
  ('Juego: The Legend of Zelda - Breath of the Wild', 467.45),
  ('Juego: Elden Ring', 467.75),
  ('Juego: FIFA 24', 545.25),
  ('Juego: God of War Ragnarok', 467.95),
  ('Accesorio: Control Inalámbrico PS5', 545.15),
  ('Accesorio: Control Inalámbrico Xbox', 467.85),
  ('Accesorio: Volante Logitech G923', 2729.55),
  ('Accesorio: Auriculares HyperX Cloud II', 779.65),
  ('Accesorio: Teclado Mecánico Razer BlackWidow', 1013.25),
  ('Accesorio: Mouse Gaming Logitech G502', 389.75),
  ('Accesorio: Monitor Gaming 27" ASUS TUF', 2339.35),
  ('Juego: Call of Duty Modern Warfare II', 545.45),
  ('Juego: Resident Evil Village', 467.65),
  ('Juego: Halo Infinite', 467.35),
  ('Accesorio: Silla Gaming Secretlab Titan Evo', 3509.15),
  ('Accesorio: Webcam Logitech C922 Pro', 779.55),
  ('Accesorio: Teclado Mecánico Corsair K95 RGB', 1559.45),
  ('Accesorio: Mouse Pad XXL SteelSeries', 233.55),
  ('Juego: Forza Horizon 5', 467.25),
  ('Accesorio: Capturadora Elgato HD60 S+', 1403.75),
  ('Accesorio: Disco Duro Externo 2TB Seagate', 701.35),
  ('Juego: Assassins Creed Valhalla', 467.55),
  ('Accesorio: Adaptador Ethernet para Nintendo Switch', 233.65),
  ('Accesorio: Base de Carga para Control PS5', 233.85),
  ('Accesorio: Memoria RAM Corsair Vengeance 16GB', 623.35),
  ('Juego: Cyberpunk 2077', 389.85),
  ('Juego: Red Dead Redemption 2', 467.15),
  ('Accesorio: Mando Pro para Nintendo Switch', 545.75),
  ('Accesorio: Tarjeta de Expansión SSD 1TB para Xbox', 2729.95),
  ('Accesorio: Dock para Nintendo Switch', 779.35),
  ('Juego: Super Smash Bros Ultimate', 467.35),
  ('Juego: Mario Kart 8 Deluxe', 467.65),
  ('Accesorio: Adaptador DualSense para PC', 155.75),
  ('Accesorio: Funda Protectora para Nintendo Switch', 155.85),
  ('Accesorio: Teclado Gaming Redragon Kumara', 389.95),
  ('Accesorio: Monitor Curvo 32" Samsung', 3119.25),
  ('Juego: Mortal Kombat 11', 467.95),
  ('Juego: Horizon Forbidden West', 467.85),
  ('Juego: Spider-Man: Miles Morales', 467.75),
  ('Accesorio: Cargador para Mandos de Xbox', 233.95),
  ('Accesorio: Adaptador USB para Teclado y Ratón', 155.95),
  ('Accesorio: Set de Luces LED para Setup Gamer', 311.85),
  ('Juego: NBA 2K24', 545.95),
  ('Accesorio: Disco Duro Externo 4TB Seagate', 1403.55),
  ('Accesorio: Soporte para Auriculares RGB', 311.55),
  ('Juego: DOOM Eternal', 389.45),
  ('Juego: The Witcher 3: Wild Hunt', 389.65),
  ('Juego: Dark Souls III', 389.55),
  ('Accesorio: Teclado Mecánico HyperX Alloy FPS', 935.25),
  ('Juego: Animal Crossing New Horizons', 467.35),
  ('Accesorio: Cámara HD para PS5', 623.45),
  ('Accesorio: Auriculares Corsair HS60', 623.85),
  ('Juego: Ghost of Tsushima', 467.75),
  ('Accesorio: Cable HDMI 4K para Consolas', 155.45),
  ('Accesorio: Base Refrigeradora para Laptop', 311.25),
  ('Juego: Demon’s Souls', 545.75),
  ('Juego: Sekiro: Shadows Die Twice', 467.15),
  ('Accesorio: Memoria MicroSD 128GB SanDisk', 389.95),
  ('Accesorio: Mochila Gaming Targus', 623.65),
  ('Accesorio: Teclado Mecánico Logitech G Pro X', 1169.55),
  ('Accesorio: Router Gaming Asus ROG Rapture', 2729.15),
  ('Juego: Final Fantasy VII Remake', 467.95),
  ('Juego: Death Stranding', 389.85),
  ('Juego: Star Wars Jedi: Fallen Order', 467.55),
  ('Accesorio: Capturadora AVerMedia Live Gamer Portable 2', 1403.35),
  ('Accesorio: Silla Gaming Cougar Armor One', 1949.15),
  ('Accesorio: Kit de Ventiladores RGB para PC', 389.65),
  ('Juego: Far Cry 6', 467.65),
  ('Accesorio: Disco Duro Externo 5TB Western Digital', 1403.75),
  ('Accesorio: Alfombrilla para Teclado y Ratón Corsair', 155.65),
  ('Accesorio: Teclado Gaming Logitech G915', 2729.85),
  ('Accesorio: Disco SSD NVMe 1TB Samsung', 1559.25),
  ('Accesorio: Monitor UltraWide LG 34"', 3509.55),
  ('Juego: The Last of Us Part II', 467.85),
  ('Accesorio: Fuente de Poder Corsair 750W', 935.95),
  ('Juego: Bloodborne', 389.35),
  ('Accesorio: Auriculares SteelSeries Arctis 7', 935.45),
  ('Juego: Nioh 2', 389.65),
  ('Accesorio: Cámara Web Razer Kiyo Pro', 1403.95),
  ('Juego: Watch Dogs Legion', 389.75),
  ('Accesorio: Control Scuf PS5', 1169.75),
  ('Accesorio: Tarjeta de Captura HDMI 4K', 935.65),
  ('Juego: Persona 5 Royal', 467.25),
  ('Accesorio: Mousepad Gamer Razer Firefly', 545.55),
  ('Juego: Gears of War 5', 467.95),
  ('Juego: Yakuza: Like a Dragon', 467.45),
  ('Accesorio: Adaptador de Audio para Xbox', 311.75),
  ('Accesorio: Docking Station para Steam Deck', 1403.55),
  ('Juego: Returnal', 545.35),
  ('Juego: Ratchet & Clank: Rift Apart', 545.85),
  ('Accesorio: Cable de Carga Rápida USB-C', 155.55),
  ('Juego: Ghostwire Tokyo', 467.85),
  ('Juego: Dying Light 2', 467.15),
  ('Accesorio: Cooler para PS5', 389.55),
  ('Accesorio: Almohadillas de Repuesto para Auriculares', 155.35),
  ('Juego: Marvel’s Avengers', 389.45),
  ('Juego: Monster Hunter World', 389.85),
  ('Juego: Dragon Ball Z: Kakarot', 467.55),  
('Juego: Tales of Arise', 467.35),
  ('Juego: Devil May Cry 5', 467.75),
  ('Juego: Metro Exodus', 389.55),
  ('Accesorio: Tarjeta de Memoria 256GB Kingston', 779.25),
  ('Accesorio: Ratón Gaming Corsair Harpoon RGB', 389.65),
  ('Accesorio: Base de Carga para Mandos Xbox Series', 389.85),
  ('Juego: Battlefield 2042', 467.95),
  ('Accesorio: Mouse Razer Viper Mini', 389.35),
  ('Accesorio: Soporte Vertical para PS5', 467.15),
  ('Accesorio: Protector de Pantalla para Nintendo Switch', 155.25),
  ('Juego: Destiny 2', 389.95),
  ('Accesorio: Cable HDMI 10ft para Consolas', 155.45),
  ('Accesorio: Control Inalámbrico Scuf Xbox', 1169.55),
  ('Juego: Gran Turismo 7', 467.55),
  ('Accesorio: Controlador FightStick Hori PS5', 1403.75),
  ('Juego: Spider-Man Remastered', 467.85),
  ('Accesorio: Soporte para Mandos y Auriculares', 155.95),
  ('Accesorio: Volante Thrustmaster T150', 1949.25),
  ('Accesorio: Teclado RGB Redragon K552', 389.75),
  ('Accesorio: Auriculares Logitech G733 Lightspeed', 1169.75),
  ('Juego: NieR Replicant', 467.45),
  ('Accesorio: Tarjeta Gráfica RTX 3080 10GB', 31199.55),
  ('Juego: Mass Effect Legendary Edition', 467.65),
  ('Accesorio: SSD Externo 500GB Samsung T7', 1169.25),
  ('Accesorio: Cable USB-C Trenzado 10ft', 155.75),
  ('Juego: Final Fantasy XIV Online', 389.75),
  ('Accesorio: Control Inalámbrico 8Bitdo SN30 Pro', 545.95),
  ('Juego: Pokémon Legends: Arceus', 467.55),
  ('Accesorio: Kit de Limpiador para Consolas y PC', 155.35),
  ('Accesorio: Soporte de Pared para Consola PS5', 623.25),
  ('Accesorio: Kit de Reparación para Consolas', 155.85),
  ('Accesorio: Ventilador para PS4 Pro', 389.45),
  ('Accesorio: Cargador Dual para Joy-Con', 233.75),
  ('Accesorio: Memoria RAM G.Skill Trident Z 32GB', 1403.95),
  ('Juego: Dragon Quest XI', 389.35),
  ('Juego: Immortals Fenyx Rising', 389.25),
  ('Juego: Outriders', 467.25),
  ('Juego: Diablo III Eternal Collection', 467.35),
  ('Juego: Super Mario Odyssey', 467.95),
  ('Accesorio: Enfriador Líquido para CPU Cooler Master', 1559.75),
  ('Accesorio: Fuente de Poder EVGA 600W', 779.75),
  ('Juego: Fire Emblem Three Houses', 467.15),
  ('Juego: Splatoon 3', 467.85),
  ('Juego: The Outer Worlds', 467.45),
  ('Juego: Fallout 76', 389.85),
  ('Accesorio: Ventilador RGB para PC', 155.65),
  ('Juego: Octopath Traveler', 467.55),
  ('Juego: Dragon Age Inquisition', 389.65),
  ('Juego: Days Gone', 467.25),
  ('Accesorio: Cable DisplayPort 1.4 6ft', 155.35),
  ('Juego: Kingdom Hearts III', 389.95),
  ('Accesorio: Hub USB 3.0 7 Puertos', 389.25),
  ('Juego: Monster Hunter Rise', 467.95),
  ('Accesorio: Adaptador de Tarjeta MicroSD a USB', 155.35),
  ('Accesorio: Cable de Audio 3.5mm 10ft', 155.85),
  ('Juego: Catherine Full Body', 389.75),
  ('Juego: Hades', 389.95),
  ('Accesorio: Enfriador Vertical para PS5', 545.35),
  ('Accesorio: Disco SSD 500GB Crucial MX500', 779.55),
  ('Juego: Judgment', 467.45),
  ('Juego: Shin Megami Tensei V', 467.85),
  ('Juego: Xenoblade Chronicles 2', 467.25),
  ('Accesorio: Cargador Portátil 10000mAh Anker', 623.15),
  ('Accesorio: Cargador Inalámbrico para Mandos Xbox', 311.55),
  ('Accesorio: Adaptador USB-C a HDMI', 311.95),
  ('Accesorio: Batería Recargable para Mandos PS4', 233.45),
  ('Accesorio: Base de Carga para Control Xbox Elite', 389.15),
  ('Accesorio: Soporte para Auriculares con Cargador USB', 467.35),
  ('Juego: Ys IX: Monstrum Nox', 389.55),
  ('Juego: Astral Chain', 467.75),
  ('Accesorio: Protector de Pantalla para Steam Deck', 155.95),
  ('Accesorio: Estación de Anclaje para PC Gaming', 1403.45),
  ('Accesorio: Control Retro-Bit Saturn USB', 545.95),
  ('Juego: LEGO Star Wars: The Skywalker Saga', 467.55),
  ('Accesorio: Estuche para Steam Deck', 389.85),
  ('Juego: Scarlet Nexus', 467.15),
  ('Juego: Kena: Bridge of Spirits', 467.65),
  ('Accesorio: Soporte para Monitores Dual', 935.55),
  ('Juego: Guilty Gear Strive', 467.35),
  ('Accesorio: Mouse Logitech G Pro Wireless', 1169.35),
  ('Juego: Valkyria Chronicles 4', 389.95),
  ('Juego: Tales of Berseria', 389.25),
  ('Juego: Atelier Ryza 2', 467.85),
  ('Accesorio: Teclado Mecánico SteelSeries Apex Pro', 1949.75),
  ('Accesorio: Base para Monitor con Puertos USB', 779.35),
  ('Accesorio: Cámara Web 4K para Streaming', 1559.45),
  ('Juego: Nioh Complete Edition', 389.45),
  ('Juego: Ghostrunner', 389.35),
  ('Accesorio: Cargador Rápido para Mandos Xbox Series X', 389.55),
  ('Juego: The Elder Scrolls V: Skyrim', 389.65),
  ('Juego: Persona 4 Golden', 389.85),
  ('Juego: Dark Souls Remastered', 389.55),
  ('Accesorio: Soporte Vertical para Xbox Series X', 233.15),
  ('Accesorio: Hub USB-C para Nintendo Switch', 389.35),
  ('Accesorio: Cable Ethernet 25ft para Consolas y PC', 233.95),
  ('Accesorio: Memoria USB 64GB Kingston', 155.45),
  ('Accesorio: Tarjeta MicroSDXC 512GB SanDisk', 1949.95),
  ('Juego: Disgaea 6', 467.25),
  ('Juego: NieR Automata', 389.75),
  ('Juego: Little Nightmares II', 389.35),
  ('Accesorio: Mochila para Consola PS5 y Accesorios', 623.75),
  ('Accesorio: Dock de Refrigeración para Switch', 389.65),
('Juego: Sekiro GOTY Edition', 545.35),
  ('Juego: Dragon Quest Builders 2', 467.75),
  ('Accesorio: Hub USB 3.1 Tipo-C para PC', 545.55),
  ('Accesorio: Set de Herramientas para Reparación', 311.25),
  ('Accesorio: Kit de Limpiador de Pantallas', 155.75),
  ('Juego: Final Fantasy XII The Zodiac Age', 389.85),
  ('Juego: Nioh 2 Complete Edition', 467.95),
  ('Accesorio: Stand para Control y Auriculares', 389.35),
  ('Accesorio: Reposa Muñecas Ergonómico', 233.15),
  ('Accesorio: Base Giratoria para Consolas', 389.95),
  ('Accesorio: Cable USB-A a USB-C 15ft', 155.85),
  ('Juego: Dead Cells', 389.55),
  ('Juego: Hollow Knight', 389.25),
  ('Juego: Blasphemous', 389.65),
  ('Accesorio: Cargador para DualShock 4', 311.95),
  ('Accesorio: Control Remoto Media para Xbox', 389.75),
  ('Juego: Phoenix Wright: Ace Attorney Trilogy', 389.35),
  ('Juego: AI: The Somnium Files', 389.95),
  ('Juego: Ace Combat 7: Skies Unknown', 389.15),
  ('Accesorio: Cable de Red Trenzado Cat7 10ft', 155.95),
  ('Accesorio: Maletín de Transporte para PS5', 935.55),
  ('Accesorio: Adaptador de Red para PS5', 389.55),
  ('Juego: Code Vein', 389.45),
  ('Juego: Trials of Mana', 467.85),
  ('Juego: Devil May Cry HD Collection', 389.25),
  ('Juego: 13 Sentinels: Aegis Rim', 467.45),
  ('Accesorio: Protector de Polvo para Consolas', 155.35),
  ('Accesorio: Base para Teclado y Ratón Ajustable', 779.55),
  ('Accesorio: Cable de Alimentación para Consolas', 155.65),
  ('Juego: Catherine Classic', 389.85),
  ('Juego: Gravity Rush 2', 389.35),
  ('Juego: NieR Replicant ver.1.22474487139', 467.55),
  ('Juego: Detroit: Become Human', 467.25),
  ('Juego: Until Dawn', 389.15),
  ('Accesorio: Soporte para Mandos con Iluminación LED', 545.15),
  ('Accesorio: Guía de Montaje para Consolas', 155.25),
  ('Juego: Control Ultimate Edition', 467.95),
  ('Juego: Bayonetta & Vanquish 10th Anniversary', 389.75),
  ('Juego: The Wonderful 101 Remastered', 389.95),
  ('Accesorio: Cojín Lumbar para Sillas Gaming', 389.25),
  ('Accesorio: Cable de Carga Magnético USB-C', 233.45),
  ('Juego: A Plague Tale: Innocence', 389.45),
  ('Juego: Planet Coaster', 467.35),
  ('Juego: Tales of Vesperia', 389.75),
  ('Accesorio: Base de Carga Inalámbrica para Switch Pro', 623.55),
  ('Accesorio: Adaptador para Teclado y Ratón en Consolas', 545.65),
  ('Juego: Ys VIII: Lacrimosa of Dana', 467.15);

--insert de los productos en las sucursales
INSERT INTO gamerprosc.productos_sucursal (id_producto, id_sucursal, stock_bodega, stock_estanteria, pasillo)
VALUES
  (1, 1, 100, 100, 0),
  (2, 1, 50, 500, 0),
  (3, 1, 80, 800, 0),
  (4, 1, 40, 40, 0),
  (5, 1, 60, 60, 0),
  (6, 1, 70, 70, 0),
  (7, 1, 10, 10, 0),
  (8, 1, 45, 50, 0),
  (9, 1, 35, 40, 0),
  (10, 1, 20, 0, 0),
  (11, 1, 0, 10, 0),
  (12, 1, 0, 100, 0),
  (13, 1, 0, 200, 0),
  (14, 1, 0, 300, 0),
  (15, 1, 0, 400, 0),
  (16, 1, 0, 0, 0),
  (17, 1, 0, 0, 0),
  (18, 1, 0, 0, 0),
  (19, 1, 0, 0, 0),
  (20, 1, 0, 0, 0),
  (21, 1, 0, 0, 0),
  (22, 1, 0, 0, 0),
  (23, 1, 0, 0, 0),
  (24, 1, 0, 0, 0),
  (25, 1, 0, 0, 0),
  (26, 1, 0, 0, 0),
  (27, 1, 0, 0, 0),
  (28, 1, 0, 0, 0),
  (29, 1, 0, 0, 0),
  (30, 1, 0, 0, 0),
  (31, 1, 0, 0, 0),
  (32, 1, 0, 0, 0),
  (33, 1, 0, 0, 0),
  (34, 1, 0, 0, 0),
  (35, 1, 0, 0, 0),
  (36, 1, 0, 0, 0),
  (37, 1, 0, 0, 0),
  (38, 1, 0, 0, 0),
  (39, 1, 0, 0, 0),
  (40, 1, 0, 0, 0),
  (41, 1, 0, 0, 0),
  (42, 1, 0, 0, 0),
  (43, 1, 0, 0, 0),
  (44, 1, 0, 0, 0),
  (45, 1, 0, 0, 0),
  (46, 1, 0, 0, 0),
  (47, 1, 0, 0, 0),
  (48, 1, 0, 0, 0),
  (49, 1, 0, 0, 0),
  (50, 1, 0, 0, 0),
  (51, 1, 0, 0, 0),
  (52, 1, 0, 0, 0),
  (53, 1, 0, 0, 0),
  (54, 1, 0, 0, 0),
  (55, 1, 0, 0, 0),
  (56, 1, 0, 0, 0),
  (57, 1, 0, 0, 0),
  (58, 1, 0, 0, 0),
  (59, 1, 0, 0, 0),
  (60, 1, 0, 0, 0),
  (61, 1, 0, 0, 0),
  (62, 1, 0, 0, 0),
  (63, 1, 0, 0, 0),
  (64, 1, 0, 0, 0),
  (65, 1, 0, 0, 0),
  (66, 1, 0, 0, 0),
  (67, 1, 0, 0, 0),
  (68, 1, 0, 0, 0),
  (69, 1, 0, 0, 0),
  (70, 1, 0, 0, 0),
  (71, 1, 0, 0, 0),
  (72, 1, 0, 0, 0),
  (73, 1, 0, 0, 0),
  (74, 1, 0, 0, 0),
  (75, 1, 0, 0, 0),
  (76, 1, 0, 0, 0),
  (77, 1, 0, 0, 0),
  (78, 1, 0, 0, 0),
  (79, 1, 0, 0, 0),
  (80, 1, 0, 0, 0),
  (81, 1, 0, 0, 0),
  (82, 1, 0, 0, 0),
  (83, 1, 0, 0, 0),
  (84, 1, 0, 0, 0),
  (85, 1, 0, 0, 0),
  (86, 1, 0, 0, 0),
  (87, 1, 0, 0, 0),
  (88, 1, 0, 0, 0),
  (89, 1, 0, 0, 0),
  (90, 1, 0, 0, 0),
  (91, 1, 0, 0, 0),
  (92, 1, 0, 0, 0),
  (93, 1, 0, 0, 0),
  (94, 1, 0, 0, 0),
  (95, 1, 0, 0, 0),
  (96, 1, 0, 0, 0),
  (97, 1, 0, 0, 0),
  (98, 1, 0, 0, 0),
  (99, 1, 0, 0, 0),
  (100, 1, 0, 0, 0),
  (101, 2, 50, 20, 0),
(101, 2, 70, 20, 0),
  (102, 2, 80, 60, 0),
  (103, 2, 10, 80, 0),
  (104, 2, 20, 70, 0),
  (105, 2, 30, 40, 0),
  (106, 2, 40, 50, 0),
  (107, 2, 55, 60, 0),
  (108, 2, 80, 10, 0),
  (109, 2, 4, 0, 0),
  (110, 2, 60, 0, 0),
  (111, 2, 0, 0, 0),
  (112, 2, 0, 0, 0),
  (113, 2, 0, 0, 0),
  (114, 2, 0, 0, 0),
  (115, 2, 0, 0, 0),
  (116, 2, 0, 0, 0),
  (117, 2, 0, 0, 0),
  (118, 2, 0, 0, 0),
  (119, 2, 0, 0, 0),
  (120, 2, 0, 0, 0),
  (121, 2, 0, 0, 0),
  (122, 2, 0, 0, 0),
  (123, 2, 0, 0, 0),
  (124, 2, 0, 0, 0),
  (125, 2, 0, 0, 0),
  (126, 2, 0, 0, 0),
  (127, 2, 0, 0, 0),
  (128, 2, 0, 0, 0),
  (129, 2, 0, 0, 0),
  (130, 2, 0, 0, 0),
  (131, 2, 0, 0, 0),
  (132, 2, 0, 0, 0),
  (133, 2, 0, 0, 0),
  (134, 2, 0, 0, 0),
  (135, 2, 0, 0, 0),
  (136, 2, 0, 0, 0),
  (137, 2, 0, 0, 0),
  (138, 2, 0, 0, 0),
  (139, 2, 0, 0, 0),
  (140, 2, 0, 0, 0),
  (141, 2, 0, 0, 0),
  (142, 2, 0, 0, 0),
  (143, 2, 0, 0, 0),
  (144, 2, 0, 0, 0),
  (145, 2, 0, 0, 0),
  (146, 2, 0, 0, 0),
  (147, 2, 0, 0, 0),
  (148, 2, 0, 0, 0),
  (149, 2, 0, 0, 0),
  (150, 2, 0, 0, 0),
  (151, 2, 0, 0, 0),
  (152, 2, 0, 0, 0),
  (153, 2, 0, 0, 0),
  (154, 2, 0, 0, 0),
  (155, 2, 0, 0, 0),
  (156, 2, 0, 0, 0),
  (157, 2, 0, 0, 0),
  (158, 2, 0, 0, 0),
  (159, 2, 0, 0, 0),
  (160, 2, 0, 0, 0),
  (161, 2, 0, 0, 0),
  (162, 2, 0, 0, 0),
  (163, 2, 0, 0, 0),
  (164, 2, 0, 0, 0),
  (165, 2, 0, 0, 0),
  (166, 2, 0, 0, 0),
  (167, 2, 0, 0, 0),
  (168, 2, 0, 0, 0),
  (169, 2, 0, 0, 0),
  (170, 2, 0, 0, 0),
  (171, 2, 0, 0, 0),
  (172, 2, 0, 0, 0),
  (173, 2, 0, 0, 0),
  (174, 2, 0, 0, 0),
  (175, 2, 0, 0, 0),
  (176, 3, 50, 40, 0),
  (176, 3, 300, 50, 0),
  (177, 3, 20, 80, 0),
  (178, 3, 40, 90, 0),
  (179, 3, 10, 70, 0),
  (180, 3, 50, 30, 0),
  (181, 3, 20, 50, 0),
  (182, 3, 30, 20, 0),
  (183, 3, 40, 40, 0),
  (184, 3, 70, 0, 0),
  (185, 3, 60, 0, 0),
  (186, 3, 10, 0, 0),
  (187, 3, 0, 0, 0),
  (188, 3, 0, 0, 0),
  (189, 3, 0, 0, 0),
  (190, 3, 0, 0, 0),
  (191, 3, 0, 0, 0),
  (192, 3, 0, 0, 0),
  (193, 3, 0, 0, 0),
  (194, 3, 0, 0, 0),
  (195, 3, 0, 0, 0),
  (196, 3, 0, 0, 0),
  (197, 3, 0, 0, 0),
  (198, 3, 0, 0, 0),
  (199, 3, 0, 0, 0),
  (200, 3, 0, 0, 0),
  (201, 3, 0, 0, 0),
  (202, 3, 0, 0, 0),
  (203, 3, 0, 0, 0),
  (204, 3, 0, 0, 0),
  (205, 3, 0, 0, 0),
  (206, 3, 0, 0, 0),
  (207, 3, 0, 0, 0),
  (208, 3, 0, 0, 0),
  (209, 3, 0, 0, 0),
  (210, 3, 0, 0, 0),
  (211, 3, 0, 0, 0),
  (212, 3, 0, 0, 0),
  (213, 3, 0, 0, 0),
  (214, 3, 0, 0, 0),
  (215, 3, 0, 0, 0),
  (216, 3, 0, 0, 0),
  (217, 3, 0, 0, 0),
  (218, 3, 0, 0, 0),
  (219, 3, 0, 0, 0),
  (220, 3, 0, 0, 0),
  (221, 3, 0, 0, 0),
  (222, 3, 0, 0, 0),
  (223, 3, 0, 0, 0),
  (224, 3, 0, 0, 0),
  (225, 3, 0, 0, 0),
  (226, 3, 0, 0, 0),
  (227, 3, 0, 0, 0),
  (228, 3, 0, 0, 0),
  (229, 3, 0, 0, 0),
  (230, 3, 0, 0, 0),
  (231, 3, 0, 0, 0),
  (232, 3, 0, 0, 0),
  (233, 3, 0, 0, 0),
  (234, 3, 0, 0, 0),
  (235, 3, 0, 0, 0),
  (236, 3, 0, 0, 0),
  (237, 3, 0, 0, 0),
  (238, 3, 0, 0, 0),
  (239, 3, 0, 0, 0),
  (240, 3, 0, 0, 0),
  (241, 3, 0, 0, 0),
  (242, 3, 0, 0, 0),
  (243, 3, 0, 0, 0),
  (244, 3, 0, 0, 0),
  (245, 3, 0, 0, 0),
  (246, 3, 0, 0, 0),
  (247, 3, 0, 0, 0),
  (248, 3, 0, 0, 0),
  (249, 3, 0, 0, 0),
  (250, 3, 0, 0, 0);
--insert de ventas
INSERT INTO gamerprosc.ventas (fecha ,id_cliente ,id_cajero ,nit ,sucursal ,total_sind , descuento ,total_cond )
VALUES
('2024-09-26 -06',21,21,TRUE,1,10529.55,0),
-----------------------------------------------------------------------