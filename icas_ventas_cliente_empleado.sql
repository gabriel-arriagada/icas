CREATE TABLE rol
(
	idRol serial not null,
	nombre character varying(20) not null,
	primary key(idRol)
);

CREATE TABLE empleado
(
	rut character varying(10) not null,
	idRol int not null,
	clave bytea not null,
	nombre character varying(50) not null,
	apellido character varying(50) not null,
	correo character varying(50) not null,
	primary key(rut),
	constraint FK_empleado_rol
	foreign key (idRol)
	references rol(idRol)
);


CREATE TABLE cliente
(
	rut character varying(10) not null,/*Rut con guion*/
	clave bytea not null,
	nombre character varying(50) not null,
	apellido character varying(50) not null,
	correo character varying(50) not null,
	primary key(rut)
);


CREATE TABLE cuenta
(
	idCuenta serial not null,
	rutCliente character varying(10) not null,/*Rut con guión*/
	saldo integer not null,
	primary key(idCuenta),
	constraint FK_cuenta_usuario
	foreign key (rutCliente)
	references cliente(rut)
);

CREATE TABLE formaPago
(
	idFormaPago serial not null,
	nombre character varying(50) not null,
	primary key(idFormaPago)
);

CREATE TABLE venta
(
	idVenta serial not null,
	rutVendedor character varying(10) not null,/*Rut con guión*/
	rutCliente character varying(10) null,/*Rut con guión*/
	total int not null,
	fecha timestamp without time zone not null,
	idFormaPago int not null,
	pagada boolean not null,
	primary key(idVenta),
	constraint FK_venta_vendedor
	foreign key (rutVendedor)
	references empleado(rut),
	constraint FK_venta_cliente
	foreign key (rutCliente)
	references cliente(rut),
	constraint FK_venta_formaPago
	foreign key (idFormaPago)
	references formaPago(idFormaPago)
);

CREATE TABLE categoria
(
	idCategoria serial not null,
	categoria character varying(20) not null,
	primary key(idCategoria)
);

CREATE TABLE producto
(
	idProducto character varying(20) not null,
	idCategoria integer not null,
	nombre character varying(25) not null,
	stock integer null,
	precioCompra integer null,
	precioVenta integer null,
	urlImagen character varying(100),
	vigente boolean not null,
	primary key(idProducto),
	constraint FK_producto_categoria
	foreign key (idCategoria)
	references categoria(idCategoria)
);

CREATE TABLE detalle
(
	idDetalle serial not null,
	idVenta integer not null,
	idProducto character varying(20) not null,
	cantidad integer not null,
	precio integer not null,
	subTotal integer not null,
	primary key(idDetalle),
	constraint FK_detalle_venta
	foreign key (idVenta)
	references venta(idVenta) ON DELETE CASCADE,
	constraint FK_detalle_producto
	foreign key (idProducto)
	references producto(idProducto)
);

CREATE TABLE estado
(
	idEstado serial not null,
	estado character varying(20) not null,
	primary key(idEstado) 
);

CREATE TABLE pedido
(
	idPedido serial not null,
	idVenta integer not null,
	idEstado integer not null,
	horaRetiro time without time zone not null,	
	comentario character varying(50) null,
	primary key(idPedido),
	constraint FK_pedido_venta
	foreign key (idVenta)
	references venta(idVenta) ON DELETE CASCADE,
	constraint FK_pedido_estado
	foreign key (idEstado)
	references estado(idEstado)
);

CREATE TABLE razonVigenciaEmpleado
(
	idRazon serial not null,
	razon character varying(50) not null,
	positiva boolean null,
	primary key(idRazon)
);

CREATE TABLE vigenciaEmpleado
(
	rutEmpleado character varying(10) not null,
	fecha timestamp without time zone not null,
	idRazon int not null,
	vigente boolean not null,
	primary key(rutEmpleado, fecha),
	constraint FK_empleado_vigenciaempleado
	foreign key (rutEmpleado)
	references empleado(rut),
	constraint FK_razon_vigencia_empleado
	foreign key (idRazon)
	references razonVigenciaEmpleado(idRazon)
);

CREATE TABLE razonAutoVenta
(
	idRazon serial not null,
	razon character varying(50) not null,
	positiva boolean null,
	primary key(idRazon)
);

CREATE TABLE autoVenta
(
	rutEmpleado character varying(10) not null,
	fecha timestamp without time zone not null,
	idRazon int not null,
	autoVenta boolean not null,
	primary key(rutEmpleado, fecha),
	constraint FK_empleado_autoventa
	foreign key (rutEmpleado)
	references empleado(rut),
	constraint FK_razon_auto_venta
	foreign key (idRazon)
	references razonAutoVenta(idRazon)
);

CREATE TABLE razonVigenciaCliente
(
	idRazon serial not null,
	razon character varying(50) not null,
	positiva boolean null,
	primary key(idRazon)
);

CREATE TABLE vigenciaCliente
(
	rutCliente character varying(10) not null,
	fecha timestamp without time zone not null,
	idRazon int not null,
	vigente boolean not null,
	primary key(rutCliente, fecha),
	constraint FK_cliente_vigenciacliente
	foreign key (rutCliente)
	references cliente(rut),
	constraint FK_razon_vigencia_cliente
	foreign key (idRazon)
	references razonVigenciaCliente(idRazon)
);


/*Muestra los productos junto a su categoría*/
CREATE OR REPLACE VIEW productoView AS SELECT p.idProducto, c.categoria, p.nombre, p.stock,
p.precioCompra, p.precioVenta, p.vigente, p.urlImagen FROM producto p INNER JOIN
categoria c ON p.idCategoria = c.idCategoria;

/*Muestra al adminitrador las ventas con los datos correspondientes*/
CREATE OR REPLACE VIEW ventaView AS SELECT idVenta, 
(SELECT e.nombre || ' ' || e.apellido FROM empleado e WHERE e.rut = v.rutVendedor) AS vendedor,  
v.rutCliente AS cliente,
v.total,
to_char(v.fecha, 'DD-MM-YYYY hh24:MI:ss') AS fecha,
fp.nombre as formaPago,
v.pagada
FROM venta v, formaPago fp WHERE v.idFormaPago = fp.idFormaPago;

/*Muestra el detalle de la venta al administrador*/
CREATE OR REPLACE VIEW detalleView AS
SELECT p.nombre AS producto, d.idVenta ,d.cantidad, d.precio, d.subTotal, p.urlImagen AS imagen
FROM detalle d INNER JOIN producto p ON
d.idProducto = p.idProducto;


/*Para cargar comandas sólo del día de hoy*/
CREATE OR REPLACE VIEW pedidoView AS
SELECT p.idPedido, p.horaRetiro, p.idEstado, e.estado, v.fecha, (c.nombre || ' ' || c.apellido) AS nombre, c.rut 
FROM pedido p, estado e, venta v, cliente c WHERE to_char(v.fecha, 'YYYY-MM-DD') = to_char(now(), 'YYYY-MM-DD')
AND p.idVenta = v.idVenta AND v.rutCliente = c.rut
AND p.idEstado = e.idEstado ORDER BY p.horaRetiro;


/*Función para anular una venta*/
CREATE OR REPLACE FUNCTION anularVenta(param_idVenta integer) 
RETURNS VOID AS $$
	DECLARE
		row_detalle detalle%ROWTYPE;		
		idFormaPago int;
		total int;		
	BEGIN
		FOR row_detalle IN 
			SELECT * FROM detalle WHERE idVenta = param_idVenta
		LOOP
			BEGIN
				UPDATE producto SET stock = (stock + row_detalle.cantidad) WHERE idProducto = row_detalle.idProducto;
			END;		
		END LOOP;		

		idFormaPago := (SELECT v.idFormaPago from venta v where v.idVenta = param_idVenta);		

		IF idFormaPago = 1 THEN
			total := (SELECT SUM(d.subTotal) FROM detalle d WHERE d.idVenta = param_idVenta);
			UPDATE cuenta SET saldo = (saldo + total) WHERE rutCliente = (SELECT rutCliente FROM venta WHERE idVenta = param_idVenta);
		END IF;
		
		BEGIN
			DELETE FROM venta WHERE idVenta = param_idVenta;					
		END;
		
	END;
$$ LANGUAGE plpgsql;


/*Función pedidos frecuentes*/
CREATE OR REPLACE FUNCTION getComprasFrecuentes(param_rutCliente character varying(10))
  RETURNS TABLE (
    res_idVenta   integer 
  , res_idFormaPago   integer
  , res_total  bigint) AS
$func$
BEGIN
   RETURN QUERY
   SELECT /*recurrente,*/ ventas[1] AS res_idVenta, 
	(SELECT formaPago FROM unnest(formasDePago) AS formaPago GROUP BY formaPago ORDER BY COUNT(formaPago) DESC LIMIT 1) AS res_idFormaPago,
	total AS res_total
	FROM
	(
		SELECT COUNT(hashDetalle) AS recurrente, 
			total,
			array_agg(hash.idVenta) AS ventas, 
			array_agg(hash.idFormaPago) AS formasDePago
		FROM (
			SELECT v.idVenta, v.idFormaPago,
			(SELECT SUM(d.subTotal) FROM detalle d WHERE d.idVenta = v.idVenta GROUP BY d.idVenta) AS total,
			(
				SELECT string_agg
				(
					'idProducto:'|| d.idProducto ||'-cantidad:'|| d.cantidad, 
					',' ORDER BY d.idProducto
				) 
				FROM detalle d WHERE d.idVenta = v.idVenta
			) AS hashDetalle
			FROM venta v WHERE v.rutCliente = param_rutCliente 
			AND (SELECT idProducto FROM detalle d WHERE d.idVenta = v.idVenta LIMIT 1) <> 'recarga'
			GROUP BY v.idVenta ORDER BY v.idVenta
		     ) AS hash
		GROUP BY hashDetalle, total
		ORDER BY recurrente DESC LIMIT 3
	) AS pedidosFrecuentes;
END
$func$  LANGUAGE plpgsql;



INSERT INTO categoria(categoria) VALUES ('Otro');/*Categoría n° 1*/
INSERT INTO categoria(categoria) VALUES ('Snack');/*Categoría n° 2*/
INSERT INTO categoria(categoria) VALUES ('Cafetería');/*Categoría n° 3*/
INSERT INTO categoria(categoria) VALUES ('Sandwich');/*Categoría n° 4*/

INSERT INTO producto(idProducto, idCategoria, nombre, vigente) VALUES('recarga', 1, 'recarga', true);

INSERT INTO rol(nombre) VALUES ('Administrador');/*Administrador: rol n° 1*/
INSERT INTO rol(nombre) VALUES ('Vendedor');/*Vendedor: rol n° 2*/

INSERT INTO formaPago(nombre) VALUES ('Pre pago');/*Forma de pago n° 1*/
INSERT INTO formaPago(nombre) VALUES ('JUNAEB');/*Forma de pago n° 2*/
INSERT INTO formaPago(nombre) VALUES ('Efectivo');/*Forma de pago n° 3*/

INSERT INTO estado(estado) VALUES ('Recibido');/*Estado n°1*/
INSERT INTO estado(estado) VALUES ('En preparación');/*Estado n° 2*/
INSERT INTO estado(estado) VALUES ('Preparado');/*Estado n° 3*/
INSERT INTO estado(estado) VALUES ('Entregado');/*Estado n° 4*/

INSERT INTO razonVigenciaEmpleado(razon, positiva) VALUES ('Despido', false);/*Razón n° 1*/
INSERT INTO razonVigenciaEmpleado(razon, positiva) VALUES ('Vacaciones', false);/*Razón n° 2*/
INSERT INTO razonVigenciaEmpleado(razon, positiva) VALUES ('Licencia médica', false);/*Razón n° 3*/
INSERT INTO razonVigenciaEmpleado(razon, positiva) VALUES ('Ingreso al sistema', true);/*Razón n° 4*/
INSERT INTO razonVigenciaEmpleado(razon, positiva) VALUES ('Recontratado', true);/*Razón n° 5*/
INSERT INTO razonVigenciaEmpleado(razon, positiva) VALUES ('Reintegración', true);/*Razón n° 6*/
INSERT INTO razonVigenciaEmpleado(razon, positiva) VALUES ('Otro', null);/*Razón n° 7*/

INSERT INTO razonVigenciaCliente(razon, positiva) VALUES ('Mala conducta', false);/*Razón n° 1*/
INSERT INTO razonVigenciaCliente(razon, positiva) VALUES ('Egresado', false);/*Razón n° 2*/
INSERT INTO razonVigenciaCliente(razon, positiva) VALUES ('Abandona carrera', false);/*Razón n° 3*/
INSERT INTO razonVigenciaCliente(razon, positiva) VALUES ('Compromiso a buena conducta', true);/*Razón n° 4*/
INSERT INTO razonVigenciaCliente(razon, positiva) VALUES ('Ingreso al sistema', true);/*Razón n° 5*/
INSERT INTO razonVigenciaCliente(razon, positiva) VALUES ('Otro', null);/*Razón n° 6*/

INSERT INTO razonAutoVenta(razon, positiva) VALUES ('Malas prácticas', false);/*Razón n° 1*/
INSERT INTO razonAutoVenta(razon, positiva) VALUES ('Ingreso al sistema', true);/*Razón n° 2*/
INSERT INTO razonAutoVenta(razon, positiva) VALUES ('Compromiso a buena conducta', true);/*Razón n° 3*/
INSERT INTO razonAutoVenta(razon, positiva) VALUES ('Otro', null);/*Razón n° 4*/

/*Recordar insertar los usuarios administrador y sistema de ventas con el script en java*/


/*Consultas que están en sl sistema*/
/*Lista de empleados según rol*/
/*
SELECT e.rut, r.nombre AS rol, e.nombre, e.apellido, e.correo, 
(SELECT ve.vigente FROM vigenciaEmpleado ve WHERE ve.rutEmpleado = e.rut ORDER BY ve.fecha DESC LIMIT 1) AS vigente,
(SELECT av.autoVenta FROM autoVenta av WHERE av.rutEmpleado = e.rut ORDER BY av.fecha DESC LIMIT 1) AS autoVenta
FROM empleado e, rol r                                              
WHERE e.idRol = r.idRol AND e.idRol <> 1 AND e.rut <> '1-9' AND e.idRol = 2; */

/*Lista de clientes*/
/*SELECT c.rut, c.nombre, c.apellido, c.correo, 
(SELECT vc.vigente FROM vigenciaCliente vc WHERE vc.rutCliente = c.rut ORDER BY vc.fecha DESC LIMIT 1) AS vigente				
FROM cliente c;  */

