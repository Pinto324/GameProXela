-- Crear usuario administrador con todos los permisos
CREATE USER Admin WITH PASSWORD 'adminpass';

-- Crear usuario solo lectura
CREATE USER lector WITH PASSWORD 'lectorpass';

-- Crear usuario para modificar tablas (INSERT, UPDATE, DELETE)
CREATE USER modificador WITH PASSWORD 'modificador_password';

CREATE DATABASE "GamerProXela"
    WITH
    OWNER = "Admin"
    ENCODING = 'UTF8'
    LOCALE_PROVIDER = 'libc'
    CONNECTION LIMIT = -1
    IS_TEMPLATE = False;

CREATE SCHEMA IF NOT EXISTS gamerprosc;

CREATE SEQUENCE IF NOT EXISTS gamerprosc.clientes_nit_seq;

CREATE TABLE IF NOT EXISTS gamerprosc.clientes (
  nit integer NOT NULL PRIMARY KEY,
  nombre varchar,
  tipo smallint,
  puntos integer
);

CREATE SEQUENCE IF NOT EXISTS gamerprosc.descuentos_id_descuento_seq;

CREATE TABLE IF NOT EXISTS gamerprosc.descuentos (
  id_descuento serial NOT NULL PRIMARY KEY,
  nit_cliente integer,
  puntos_usados integer,
  valor_descuento decimal(10, 2),
  fecha date
);

CREATE SEQUENCE IF NOT EXISTS gamerprosc.detalle_ventas_id_seq;

CREATE TABLE IF NOT EXISTS gamerprosc.detalle_ventas (
  id serial NOT NULL PRIMARY KEY,
  no_factura integer,
  id_producto integer,
  cantidad integer,
  precio_u decimal(10, 2)
);

CREATE SEQUENCE IF NOT EXISTS gamerprosc.empleados_id_seq;

CREATE TABLE IF NOT EXISTS gamerprosc.empleados (
  id serial NOT NULL PRIMARY KEY,
  nombre varchar,
  rol smallint,
  username varchar,
  pass varchar,
  id_sucursal integer,
  no_caja smallint
);

CREATE SEQUENCE IF NOT EXISTS gamerprosc.productos_id_producto_seq;

CREATE TABLE IF NOT EXISTS gamerprosc.productos (
  id_producto serial NOT NULL PRIMARY KEY,
  nombre varchar,
  precio decimal(10, 2)
);

CREATE SEQUENCE IF NOT EXISTS gamerprosc.productos_sucursal_id_seq;

CREATE TABLE IF NOT EXISTS gamerprosc.productos_sucursal (
  id serial NOT NULL PRIMARY KEY,
  id_producto integer,
  id_sucursal integer,
  stock_bodega integer,
  stock_estanteria integer,
  pasillo integer
);

CREATE SEQUENCE IF NOT EXISTS gamerprosc.sucursales_id_sucursal_seq;

CREATE TABLE IF NOT EXISTS gamerprosc.sucursales (
  id_sucursal serial NOT NULL PRIMARY KEY,
  ubicacion varchar
);

CREATE SEQUENCE IF NOT EXISTS gamerprosc.ventas_no_factura_seq;

CREATE TABLE IF NOT EXISTS gamerprosc.ventas (
  no_factura serial PRIMARY KEY,
  fecha date,
  nit_cliente integer,
  id_cajero integer,
  total_sind decimal(10, 2),
  total_cond decimal(10, 2)
);

ALTER TABLE gamerprosc.descuentos ADD CONSTRAINT descuentos_nit_cliente_fk FOREIGN KEY (nit_cliente) REFERENCES gamerprosc.clientes (nit);
ALTER TABLE gamerprosc.detalle_ventas ADD CONSTRAINT detalle_ventas_id_producto_fk FOREIGN KEY (id_producto) REFERENCES gamerprosc.productos (id_producto);
ALTER TABLE gamerprosc.detalle_ventas ADD CONSTRAINT detalle_ventas_no_factura_fk FOREIGN KEY (no_factura) REFERENCES gamerprosc.ventas (no_factura);
ALTER TABLE gamerprosc.empleados ADD CONSTRAINT empleados_id_sucursal_fk FOREIGN KEY (id_sucursal) REFERENCES gamerprosc.sucursales (id_sucursal);
ALTER TABLE gamerprosc.productos_sucursal ADD CONSTRAINT productos_sucursal_id_producto_fk FOREIGN KEY (id_producto) REFERENCES gamerprosc.productos (id_producto);
ALTER TABLE gamerprosc.productos_sucursal ADD CONSTRAINT productos_sucursal_id_sucursal_fk FOREIGN KEY (id_sucursal) REFERENCES gamerprosc.sucursales (id_sucursal);
ALTER TABLE gamerprosc.ventas ADD CONSTRAINT ventas_id_cajero_fk FOREIGN KEY (id_cajero) REFERENCES gamerprosc.empleados (id);
ALTER TABLE gamerprosc.ventas ADD CONSTRAINT ventas_nit_cliente_fk FOREIGN KEY (nit_cliente) REFERENCES gamerprosc.clientes (nit);


GRANT ALL PRIVILEGES ON ALL TABLES IN SCHEMA gamerprosc TO admin;
GRANT ALL PRIVILEGES ON ALL SEQUENCES IN SCHEMA gamerprosc TO admin;

GRANT USAGE ON SCHEMA gamerprosc TO lector;
GRANT SELECT ON ALL TABLES IN SCHEMA gamerprosc TO lector;

GRANT INSERT, UPDATE, DELETE ON ALL TABLES IN SCHEMA gamerprosc TO modificador;
--insert de las sucursales--
INSERT INTO gamerprosc.sucursales (ubicacion) 
VALUES 
    ('Parque'),
    ('Centro1'),
    ('Centro2');

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

