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

CREATE SEQUENCE IF NOT EXISTS gamerprosc.clientes_nit_seq;
CREATE SEQUENCE IF NOT EXISTS gamerprosc.descuentos_id_descuento_seq;
CREATE SEQUENCE IF NOT EXISTS gamerprosc.detalle_ventas_id_seq;
CREATE SEQUENCE IF NOT EXISTS gamerprosc.empleados_id_seq;
CREATE SEQUENCE IF NOT EXISTS gamerprosc.productos_id_producto_seq;
CREATE SEQUENCE IF NOT EXISTS gamerprosc.productos_sucursal_id_seq;
CREATE SEQUENCE IF NOT EXISTS gamerprosc.sucursales_id_sucursal_seq;
CREATE SEQUENCE IF NOT EXISTS gamerprosc.ventas_no_factura_seq;

--insert de las sucursales--
INSERT INTO gamerprosc.sucursales (ubicacion) 
VALUES 
    ('Parque'),
    ('Centro1'),
    ('Centro2');

INSERT INTO gamerprosc.productos (nombre, precio)
VALUES
  ('Consola Xbox Series X', 3899),
  ('Consola PlayStation 5', 3899),
  ('Nintendo Switch OLED', 2729),
  ('Consola Xbox Series S', 2339),
  ('Juego: The Legend of Zelda - Breath of the Wild', 467),
  ('Juego: Elden Ring', 467),
  ('Juego: FIFA 24', 545),
  ('Juego: God of War Ragnarok', 467),
  ('Accesorio: Control Inalámbrico PS5', 545),
  ('Accesorio: Control Inalámbrico Xbox', 467);    
  
INSERT INTO gamerprosc.productos_sucursal (id_producto, id_sucursal, stock_bodega, stock_estanteria, pasillo)
VALUES
  (1, 1, 0, 0, 0),
  (2, 1, 0, 0, 0),
  (3, 1, 0, 0, 0),
  (4, 1, 0, 0, 0),
  (5, 1, 0, 0, 0),
  (6, 1, 0, 0, 0),
  (7, 1, 0, 0, 0),
  (8, 1, 0, 0, 0),
  (9, 1, 0, 0, 0),
  (10, 1, 0, 0, 0);
-----------------------------------------------------------------------
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
    sucursal integer,
    total_sind decimal(10, 2),
    descuento integer
)
RETURNS VOID AS $$
BEGIN
    
    INSERT INTO gamerprosc.ventas (fecha, id_cliente, id_cajero, sucursal, total_sind, descuento, total_cond)
    VALUES (fecha, id_cliente, id_cajero, sucursal, total_sind, descuento, total_sind-descuento);
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
