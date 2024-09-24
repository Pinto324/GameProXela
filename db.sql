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
  (1, 1, 0, 0, -1),
  (2, 1, 0, 0, -1),
  (3, 1, 0, 0, -1),
  (4, 1, 0, 0, -1),
  (5, 1, 0, 0, -1),
  (6, 1, 0, 0, -1),
  (7, 1, 0, 0, -1),
  (8, 1, 0, 0, -1),
  (9, 1, 0, 0, -1),
  (10, 1, 0, 0, -1);
-----------------------------------------------------------------------
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
-----------------------------------------------------------------------
--Funciones:
CREATE OR REPLACE FUNCTION llenar_estanteria()
RETURNS TRIGGER AS $$
DECLARE stock_actual INTEGER;
BEGIN

  SELECT stock_bodega INTO stock_actual 
  FROM gamerprosc.productos_sucursal 
  WHERE id_producto = NEW.id_producto AND id_sucursal = NEW.id_sucursal;

  IF NEW.stock_estanteria > stock_actual THEN
    RAISE EXCEPTION 'Stock insuficiente en bodega. Solo hay % unidades en stock.', stock_actual;
  END IF;


  UPDATE gamerprosc.productos_sucursal
  SET stock_bodega = stock_bodega - NEW.stock_estanteria
  WHERE id_producto = NEW.id_producto AND id_sucursal = NEW.id_sucursal;


  RETURN NEW;
END;
$$ LANGUAGE plpgsql;

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

-- encargada del trigger para la tabla clientes
CREATE TRIGGER trigger_verificar_nit
BEFORE INSERT OR UPDATE ON gamerprosc.clientes
FOR EACH ROW
EXECUTE FUNCTION gamerprosc.verificar_nit_cliente();

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

--permiso para usar el schema 
GRANT USAGE ON SCHEMA gamerprosc TO admin;
GRANT USAGE ON SCHEMA gamerprosc TO lector;
GRANT USAGE ON SCHEMA gamerprosc TO modificador;
GRANT ALL PRIVILEGES ON ALL TABLES IN SCHEMA gamerprosc TO admin;
GRANT ALL PRIVILEGES ON ALL SEQUENCES IN SCHEMA gamerprosc TO admin;

GRANT SELECT ON ALL TABLES IN SCHEMA gamerprosc TO lector;

GRANT SELECT, INSERT, UPDATE, DELETE ON ALL TABLES IN SCHEMA gamerprosc TO modificador;
GRANT ALL PRIVILEGES ON ALL SEQUENCES IN SCHEMA gamerprosc TO modificador;
GRANT EXECUTE ON ALL FUNCTIONS IN SCHEMA gamerprosc TO modificador;
