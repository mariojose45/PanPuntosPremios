
package panpuntospremios;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class Conexion {

    public Connection conectar() {

        Connection conexion = null;

        String url = "jdbc:oracle:thin:@//localhost:1521/FREEPDB1";
        String usuario = "SYSTEM";
        String password = "Admin--";

        try {

            conexion = DriverManager.getConnection(
                    url,
                    usuario,
                    password
            );

            System.out.println("Conexion realizada correctamente");

        } catch (SQLException e) {

            System.out.println(
                    "Error de conexion: " + e.getMessage()
            );
        }

        return conexion;
    }

    public ArrayList<Cliente> mostrarClientes() {

        String sql = "SELECT * FROM CLIENTE ORDER BY ID_CLIENTE";

        ArrayList<Cliente> clientes = new ArrayList<>();

        try {

            Connection conexion = conectar();

            if (conexion == null) {
                return clientes;
            }

            try (
                Connection con = conexion;
                PreparedStatement ps = con.prepareStatement(sql);
                ResultSet resultado = ps.executeQuery()
            ) {

                while (resultado.next()) {

                    int id = resultado.getInt("ID_CLIENTE");

                    String identificador =
                            resultado.getString("IDENTIFICADOR");

                    String nombre =
                            resultado.getString("NOMBRE");

                    String telefono =
                            resultado.getString("TELEFONO");

                    int puntos =
                            resultado.getInt("PUNTOS");

                    String estado =
                            resultado.getString("ESTADO");

                    Cliente cliente = new Cliente(
                            id,
                            identificador,
                            nombre,
                            telefono,
                            puntos,
                            estado
                    );

                    clientes.add(cliente);
                }
            }

        } catch (SQLException e) {

            System.out.println(
                    "Error al consultar clientes: "
                    + e.getMessage()
            );
        }

        return clientes;
    }

    public void insertarCliente(
            String identificador,
            String nombre,
            String telefono) {

        String sql =
                "INSERT INTO CLIENTE "
                + "(ID_CLIENTE, IDENTIFICADOR, NOMBRE, TELEFONO, PUNTOS, ESTADO) "
                + "VALUES ((SELECT NVL(MAX(ID_CLIENTE), 0) + 1 FROM CLIENTE), "
                + "?, ?, ?, 0, 'ACTIVO')";

        try {

            Connection conexion = conectar();

            if (conexion == null) {
                return;
            }

            try (
                Connection con = conexion;
                PreparedStatement ps = con.prepareStatement(sql)
            ) {

                ps.setString(1, identificador);
                ps.setString(2, nombre);
                ps.setString(3, telefono);

                ps.executeUpdate();

                System.out.println(
                        "Cliente insertado correctamente"
                );
            }

        } catch (SQLException e) {

            System.out.println(
                    "Error al insertar cliente: "
                    + e.getMessage()
            );
        }
    }

    public void actualizarCliente(
            int idCliente,
            String identificador,
            String nombre,
            String telefono) {

        String sql =
                "UPDATE CLIENTE "
                + "SET IDENTIFICADOR = ?, "
                + "NOMBRE = ?, "
                + "TELEFONO = ? "
                + "WHERE ID_CLIENTE = ?";

        try {

            Connection conexion = conectar();

            if (conexion == null) {
                return;
            }

            try (
                Connection con = conexion;
                PreparedStatement ps = con.prepareStatement(sql)
            ) {

                ps.setString(1, identificador);
                ps.setString(2, nombre);
                ps.setString(3, telefono);
                ps.setInt(4, idCliente);

                int filas = ps.executeUpdate();

                if (filas > 0) {

                    System.out.println(
                            "Cliente actualizado correctamente"
                    );

                } else {

                    System.out.println(
                            "No se encontro el cliente"
                    );
                }
            }

        } catch (SQLException e) {

            System.out.println(
                    "Error al actualizar cliente: "
                    + e.getMessage()
            );
        }
    }
    

public ArrayList<Producto> mostrarProductos() {

    String sql = "SELECT * FROM PRODUCTO ORDER BY ID_PRODUCTO";

    ArrayList<Producto> productos = new ArrayList<>();

    try {

        Connection conexion = conectar();

        if (conexion == null) {
            return productos;
        }

        PreparedStatement ps =
                conexion.prepareStatement(sql);

        ResultSet resultado =
                ps.executeQuery();

        while (resultado.next()) {

            int idProducto =
                    resultado.getInt("ID_PRODUCTO");

            String codigo =
                    resultado.getString("CODIGO");

            String nombre =
                    resultado.getString("NOMBRE");

            String categoria =
                    resultado.getString("CATEGORIA");

            double precio =
                    resultado.getDouble("PRECIO");

            int existencia =
                    resultado.getInt("EXISTENCIA");

            String estado =
                    resultado.getString("ESTADO");

            Producto producto = new Producto(
                    idProducto,
                    codigo,
                    nombre,
                    categoria,
                    precio,
                    existencia,
                    estado
            );

            productos.add(producto);
        }

        resultado.close();
        ps.close();
        conexion.close();

    } catch (SQLException e) {

        System.out.println(
                "Error al consultar productos: "
                + e.getMessage()
        );
    }

    return productos;
}


public void insertarProducto(
        String codigo,
        String nombre,
        String categoria,
        double precio,
        int existencia) {

    String sql =
            "INSERT INTO PRODUCTO "
            + "(ID_PRODUCTO, CODIGO, NOMBRE, CATEGORIA, PRECIO, EXISTENCIA, ESTADO) "
            + "VALUES ((SELECT NVL(MAX(ID_PRODUCTO), 0) + 1 FROM PRODUCTO), "
            + "?, ?, ?, ?, ?, 'ACTIVO')";

    if (codigo.trim().isEmpty() || nombre.trim().isEmpty()
            || precio < 0 || existencia < 0) {
        System.out.println("Datos de producto invalidos");
        return;
    }

    try {

        Connection conexion = conectar();

        if (conexion == null) {
            return;
        }

        PreparedStatement ps = conexion.prepareStatement(sql);

        ps.setString(1, codigo);
        ps.setString(2, nombre);
        ps.setString(3, categoria);
        ps.setDouble(4, precio);
        ps.setInt(5, existencia);

        ps.executeUpdate();

        ps.close();
        conexion.close();

        System.out.println("Producto insertado correctamente");

    } catch (SQLException e) {

        System.out.println(
                "Error al insertar producto: " + e.getMessage()
        );
    }
}



public void actualizarProducto(
        int idProducto,
        String codigo,
        String nombre,
        String categoria,
        double precio,
        int existencia) {

    String sql =
            "UPDATE PRODUCTO SET "
            + "CODIGO = ?, "
            + "NOMBRE = ?, "
            + "CATEGORIA = ?, "
            + "PRECIO = ?, "
            + "EXISTENCIA = ? "
            + "WHERE ID_PRODUCTO = ?";

    if (codigo.trim().isEmpty() || nombre.trim().isEmpty()
            || precio < 0 || existencia < 0) {
        System.out.println("Datos de producto invalidos");
        return;
    }

    try {

        Connection conexion = conectar();

        if (conexion == null) {
            return;
        }

        PreparedStatement ps = conexion.prepareStatement(sql);

        ps.setString(1, codigo);
        ps.setString(2, nombre);
        ps.setString(3, categoria);
        ps.setDouble(4, precio);
        ps.setInt(5, existencia);
        ps.setInt(6, idProducto);

        ps.executeUpdate();

        ps.close();
        conexion.close();

        System.out.println("Producto actualizado correctamente");

    } catch (SQLException e) {

        System.out.println(
                "Error al actualizar producto: " + e.getMessage()
        );
    }
}



public void registrarEntradaInventario(
        int idProducto,
        int cantidad) {

    if (cantidad <= 0) {
        System.out.println("La cantidad debe ser mayor que cero");
        return;
    }

    Connection conexion = conectar();

    if (conexion == null) {
        return;
    }

    try {

        conexion.setAutoCommit(false);

        String sqlProducto =
                "UPDATE PRODUCTO "
                + "SET EXISTENCIA = EXISTENCIA + ? "
                + "WHERE ID_PRODUCTO = ? AND ESTADO = 'ACTIVO'";

        PreparedStatement psProducto =
                conexion.prepareStatement(sqlProducto);

        psProducto.setInt(1, cantidad);
        psProducto.setInt(2, idProducto);

        int filas = psProducto.executeUpdate();
        psProducto.close();

        if (filas == 0) {
            throw new SQLException("Producto no encontrado o inactivo");
        }

        String sqlMovimiento =
                "INSERT INTO MOVIMIENTO_INVENTARIO "
                + "(ID_MOVIMIENTO, ID_PRODUCTO, FECHA, TIPO, CANTIDAD, ID_PEDIDO) "
                + "VALUES ((SELECT NVL(MAX(ID_MOVIMIENTO), 0) + 1 "
                + "FROM MOVIMIENTO_INVENTARIO), ?, SYSDATE, 'ENTRADA', ?, NULL)";

        PreparedStatement psMovimiento =
                conexion.prepareStatement(sqlMovimiento);

        psMovimiento.setInt(1, idProducto);
        psMovimiento.setInt(2, cantidad);

        psMovimiento.executeUpdate();
        psMovimiento.close();

        conexion.commit();

        System.out.println("Entrada de inventario registrada correctamente");

    } catch (SQLException e) {

        try {
            conexion.rollback();
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }

        System.out.println(
                "Error al registrar entrada: " + e.getMessage()
        );

    } finally {

        try {
            conexion.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}


public ArrayList<MovimientoInventario> mostrarMovimientosInventario() {

    ArrayList<MovimientoInventario> movimientos = new ArrayList<>();

    String sql =
            "SELECT m.ID_MOVIMIENTO, m.ID_PRODUCTO, "
            + "p.NOMBRE, m.FECHA, m.TIPO, m.CANTIDAD "
            + "FROM MOVIMIENTO_INVENTARIO m "
            + "INNER JOIN PRODUCTO p ON m.ID_PRODUCTO = p.ID_PRODUCTO "
            + "ORDER BY m.ID_MOVIMIENTO DESC";

    Connection conexion = conectar();

    if (conexion == null) {
        return movimientos;
    }

    try {

        PreparedStatement ps =
                conexion.prepareStatement(sql);

        ResultSet resultado =
                ps.executeQuery();

        while (resultado.next()) {

            MovimientoInventario movimiento =
                    new MovimientoInventario(
                            resultado.getInt("ID_MOVIMIENTO"),
                            resultado.getInt("ID_PRODUCTO"),
                            resultado.getString("NOMBRE"),
                            resultado.getString("FECHA"),
                            resultado.getString("TIPO"),
                            resultado.getInt("CANTIDAD")
                    );

            movimientos.add(movimiento);
        }

        resultado.close();
        ps.close();

    } catch (SQLException e) {

        System.out.println(
                "Error al consultar movimientos: " + e.getMessage()
        );

    } finally {

        try {
            conexion.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    return movimientos;
}



public ArrayList<Menu> mostrarMenus() {

    ArrayList<Menu> menus = new ArrayList<>();

    String sql = "SELECT * FROM MENU ORDER BY ID_MENU";

    try {

        Connection conexion = conectar();

        if (conexion == null) {
            return menus;
        }

        PreparedStatement ps = conexion.prepareStatement(sql);
        ResultSet resultado = ps.executeQuery();

        while (resultado.next()) {

            Menu menu = new Menu(
                    resultado.getInt("ID_MENU"),
                    resultado.getString("NOMBRE"),
                    resultado.getDouble("PRECIO"),
                    resultado.getString("ESTADO")
            );

            menus.add(menu);
        }

        resultado.close();
        ps.close();
        conexion.close();

    } catch (SQLException e) {

        System.out.println(
                "Error al consultar menus: " + e.getMessage()
        );
    }

    return menus;
}




public ArrayList<DetalleMenu> mostrarDetalleMenu(int idMenu) {

    ArrayList<DetalleMenu> detalles = new ArrayList<>();

    String sql =
            "SELECT d.ID_DETALLE_MENU, d.ID_MENU, "
            + "d.ID_PRODUCTO, p.NOMBRE, d.CANTIDAD "
            + "FROM DETALLE_MENU d "
            + "INNER JOIN PRODUCTO p "
            + "ON d.ID_PRODUCTO = p.ID_PRODUCTO "
            + "WHERE d.ID_MENU = ? "
            + "ORDER BY d.ID_DETALLE_MENU";

    try {

        Connection conexion = conectar();

        if (conexion == null) {
            return detalles;
        }

        PreparedStatement ps = conexion.prepareStatement(sql);
        ps.setInt(1, idMenu);

        ResultSet resultado = ps.executeQuery();

        while (resultado.next()) {

            DetalleMenu detalle = new DetalleMenu(
                    resultado.getInt("ID_DETALLE_MENU"),
                    resultado.getInt("ID_MENU"),
                    resultado.getInt("ID_PRODUCTO"),
                    resultado.getString("NOMBRE"),
                    resultado.getInt("CANTIDAD")
            );

            detalles.add(detalle);
        }

        resultado.close();
        ps.close();
        conexion.close();

    } catch (SQLException e) {

        System.out.println(
                "Error al consultar detalle de menu: "
                + e.getMessage()
        );
    }

    return detalles;
}




public void insertarMenu(
        String nombre,
        double precio,
        int idSandwich,
        int idBebida,
        int idAcompanamiento) {

    if (nombre == null || nombre.trim().isEmpty()
            || !Double.isFinite(precio) || precio < 0) {

        System.out.println("Datos del menu invalidos");
        return;
    }

    Connection conexion = conectar();

    if (conexion == null) {
        return;
    }

    try {

        conexion.setAutoCommit(false);

        String sqlValidar =
                "SELECT COUNT(*) FROM PRODUCTO "
                + "WHERE ID_PRODUCTO = ? "
                + "AND CATEGORIA = ? AND ESTADO = 'ACTIVO'";

        String[] categorias = {
            "SANDWICH",
            "BEBIDA",
            "ACOMPANAMIENTO"
        };

        int[] productos = {
            idSandwich,
            idBebida,
            idAcompanamiento
        };

        PreparedStatement psValidar =
                conexion.prepareStatement(sqlValidar);

        for (int i = 0; i < productos.length; i++) {

            psValidar.setInt(1, productos[i]);
            psValidar.setString(2, categorias[i]);

            ResultSet resultado = psValidar.executeQuery();

            resultado.next();

            if (resultado.getInt(1) == 0) {

                resultado.close();
                throw new SQLException(
                        "Producto invalido para la categoria "
                        + categorias[i]
                );
            }

            resultado.close();
        }

        psValidar.close();

        String sqlId =
                "SELECT NVL(MAX(ID_MENU), 0) + 1 FROM MENU";

        PreparedStatement psId =
                conexion.prepareStatement(sqlId);

        ResultSet resultadoId = psId.executeQuery();
        resultadoId.next();

        int idMenu = resultadoId.getInt(1);

        resultadoId.close();
        psId.close();

        String sqlMenu =
                "INSERT INTO MENU "
                + "(ID_MENU, NOMBRE, PRECIO, ESTADO) "
                + "VALUES (?, ?, ?, 'ACTIVO')";

        PreparedStatement psMenu =
                conexion.prepareStatement(sqlMenu);

        psMenu.setInt(1, idMenu);
        psMenu.setString(2, nombre.trim());
        psMenu.setDouble(3, precio);

        psMenu.executeUpdate();
        psMenu.close();

        String sqlDetalle =
                "INSERT INTO DETALLE_MENU "
                + "(ID_DETALLE_MENU, ID_MENU, ID_PRODUCTO, CANTIDAD) "
                + "VALUES ((SELECT NVL(MAX(ID_DETALLE_MENU), 0) + 1 "
                + "FROM DETALLE_MENU), ?, ?, 1)";

        PreparedStatement psDetalle =
                conexion.prepareStatement(sqlDetalle);

        for (int idProducto : productos) {

            psDetalle.setInt(1, idMenu);
            psDetalle.setInt(2, idProducto);
            psDetalle.executeUpdate();
        }

        psDetalle.close();

        conexion.commit();

        System.out.println("Menu registrado correctamente");

    } catch (SQLException e) {

        try {
            conexion.rollback();
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }

        System.out.println(
                "Error al registrar menu: " + e.getMessage()
        );

    } finally {

        try {
            conexion.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}



public boolean insertarPedido(
        int idCliente,
        ArrayList<DetallePedido> detalles) {

    if (idCliente <= 0 || detalles == null || detalles.isEmpty()) {
        return false;
    }

    Connection conexion = conectar();

    if (conexion == null) {
        return false;
    }

    try {

        conexion.setAutoCommit(false);

        String sqlCliente =
                "SELECT COUNT(*) FROM CLIENTE "
                + "WHERE ID_CLIENTE = ? AND ESTADO = 'ACTIVO'";

        try (PreparedStatement ps = conexion.prepareStatement(sqlCliente)) {

            ps.setInt(1, idCliente);

            try (ResultSet resultado = ps.executeQuery()) {

                resultado.next();

                if (resultado.getInt(1) == 0) {
                    throw new SQLException("Cliente no encontrado o inactivo");
                }
            }
        }

        String sqlId =
                "SELECT NVL(MAX(ID_PEDIDO), 0) + 1 FROM PEDIDO";

        int idPedido;

        try (PreparedStatement ps = conexion.prepareStatement(sqlId);
             ResultSet resultado = ps.executeQuery()) {

            resultado.next();
            idPedido = resultado.getInt(1);
        }

        double total = 0;

        String sqlProducto =
                "SELECT PRECIO FROM PRODUCTO "
                + "WHERE ID_PRODUCTO = ? AND ESTADO = 'ACTIVO'";

        String sqlMenu =
                "SELECT PRECIO FROM MENU "
                + "WHERE ID_MENU = ? AND ESTADO = 'ACTIVO'";

        for (DetallePedido detalle : detalles) {

            if (detalle.cantidad <= 0) {
                throw new SQLException("Cantidad invalida");
            }

            boolean esProducto = detalle.idProducto > 0;
            boolean esMenu = detalle.idMenu > 0;

            if (esProducto == esMenu) {
                throw new SQLException("Detalle de pedido invalido");
            }

            String sql = esProducto ? sqlProducto : sqlMenu;
            int id = esProducto ? detalle.idProducto : detalle.idMenu;

            try (PreparedStatement ps = conexion.prepareStatement(sql)) {

                ps.setInt(1, id);

                try (ResultSet resultado = ps.executeQuery()) {

                    if (!resultado.next()) {
                        throw new SQLException("Producto o menu no disponible");
                    }

                    detalle.precio = resultado.getDouble("PRECIO");
                }
            }

            detalle.subtotal = Math.round(
                    detalle.precio * detalle.cantidad * 100.0
            ) / 100.0;

            total += detalle.subtotal;
        }

        total = Math.round(total * 100.0) / 100.0;

        String sqlPedido =
                "INSERT INTO PEDIDO "
                + "(ID_PEDIDO, ID_CLIENTE, FECHA, TOTAL, ESTADO) "
                + "VALUES (?, ?, SYSDATE, ?, 'PENDIENTE')";

        try (PreparedStatement ps = conexion.prepareStatement(sqlPedido)) {

            ps.setInt(1, idPedido);
            ps.setInt(2, idCliente);
            ps.setDouble(3, total);
            ps.executeUpdate();
        }

        String sqlDetalle =
                "INSERT INTO DETALLE_PEDIDO "
                + "(ID_DETALLE, ID_PEDIDO, ID_PRODUCTO, ID_MENU, "
                + "CANTIDAD, PRECIO, SUBTOTAL) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?)";

        String sqlIdDetalle =
                "SELECT NVL(MAX(ID_DETALLE), 0) FROM DETALLE_PEDIDO";

        int idDetalle;

        try (PreparedStatement ps = conexion.prepareStatement(sqlIdDetalle);
             ResultSet resultado = ps.executeQuery()) {

            resultado.next();
            idDetalle = resultado.getInt(1);
        }

        try (PreparedStatement ps = conexion.prepareStatement(sqlDetalle)) {

            for (DetallePedido detalle : detalles) {

                idDetalle++;

                ps.setInt(1, idDetalle);
                ps.setInt(2, idPedido);

                if (detalle.idProducto > 0) {
                    ps.setInt(3, detalle.idProducto);
                    ps.setNull(4, java.sql.Types.NUMERIC);
                } else {
                    ps.setNull(3, java.sql.Types.NUMERIC);
                    ps.setInt(4, detalle.idMenu);
                }

                ps.setInt(5, detalle.cantidad);
                ps.setDouble(6, detalle.precio);
                ps.setDouble(7, detalle.subtotal);

                ps.executeUpdate();
            }
        }

        conexion.commit();

        System.out.println("Pedido registrado correctamente: " + idPedido);

        return true;

    } catch (SQLException e) {

        try {
            conexion.rollback();
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }

        System.out.println("Error al registrar pedido: " + e.getMessage());

        return false;

    } finally {

        try {
            conexion.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}



public ArrayList<Pedido> mostrarPedidos() {

    ArrayList<Pedido> pedidos = new ArrayList<>();

    String sql =
            "SELECT p.ID_PEDIDO, p.ID_CLIENTE, "
            + "c.NOMBRE, p.FECHA, p.TOTAL, p.ESTADO "
            + "FROM PEDIDO p "
            + "INNER JOIN CLIENTE c ON p.ID_CLIENTE = c.ID_CLIENTE "
            + "ORDER BY p.ID_PEDIDO DESC";

    Connection conexion = conectar();

    if (conexion == null) {
        return pedidos;
    }

    try {

        PreparedStatement ps = conexion.prepareStatement(sql);
        ResultSet resultado = ps.executeQuery();

        while (resultado.next()) {

            Pedido pedido = new Pedido(
                    resultado.getInt("ID_PEDIDO"),
                    resultado.getInt("ID_CLIENTE"),
                    resultado.getString("NOMBRE"),
                    resultado.getString("FECHA"),
                    resultado.getDouble("TOTAL"),
                    resultado.getString("ESTADO")
            );

            pedidos.add(pedido);
        }

        resultado.close();
        ps.close();

    } catch (SQLException e) {

        System.out.println("Error al consultar pedidos: " + e.getMessage());

    } finally {

        try {
            conexion.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    return pedidos;
}


public ArrayList<DetallePedido> mostrarDetallePedido(int idPedido) {

    ArrayList<DetallePedido> detalles = new ArrayList<>();

    String sql =
            "SELECT d.ID_DETALLE, d.ID_PEDIDO, "
            + "d.ID_PRODUCTO, d.ID_MENU, "
            + "CASE WHEN d.ID_PRODUCTO IS NOT NULL "
            + "THEN p.NOMBRE ELSE m.NOMBRE END AS DESCRIPCION, "
            + "d.CANTIDAD, d.PRECIO, d.SUBTOTAL "
            + "FROM DETALLE_PEDIDO d "
            + "LEFT JOIN PRODUCTO p ON d.ID_PRODUCTO = p.ID_PRODUCTO "
            + "LEFT JOIN MENU m ON d.ID_MENU = m.ID_MENU "
            + "WHERE d.ID_PEDIDO = ? "
            + "ORDER BY d.ID_DETALLE";

    Connection conexion = conectar();

    if (conexion == null) {
        return detalles;
    }

    try {

        PreparedStatement ps = conexion.prepareStatement(sql);
        ps.setInt(1, idPedido);

        ResultSet resultado = ps.executeQuery();

        while (resultado.next()) {

            DetallePedido detalle = new DetallePedido(
                    resultado.getInt("ID_DETALLE"),
                    resultado.getInt("ID_PEDIDO"),
                    resultado.getInt("ID_PRODUCTO"),
                    resultado.getInt("ID_MENU"),
                    resultado.getString("DESCRIPCION"),
                    resultado.getInt("CANTIDAD"),
                    resultado.getDouble("PRECIO"),
                    resultado.getDouble("SUBTOTAL")
            );

            detalles.add(detalle);
        }

        resultado.close();
        ps.close();

    } catch (SQLException e) {

        System.out.println(
                "Error al consultar detalle de pedido: " + e.getMessage()
        );

    } finally {

        try {
            conexion.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    return detalles;
}



public boolean cobrarPedido(
        int idPedido,
        String metodo,
        double recibido,
        boolean tarjetaAprobada) {

    Connection conexion = conectar();

    if (conexion == null) {
        return false;
    }

    try {

        conexion.setAutoCommit(false);

        String sqlPedido =
                "SELECT ID_CLIENTE, TOTAL, ESTADO "
                + "FROM PEDIDO WHERE ID_PEDIDO = ? FOR UPDATE";

        int idCliente;
        java.math.BigDecimal total;

        try (PreparedStatement ps = conexion.prepareStatement(sqlPedido)) {

            ps.setInt(1, idPedido);

            try (ResultSet rs = ps.executeQuery()) {

                if (!rs.next()) {
                    throw new SQLException("Pedido no encontrado");
                }

                idCliente = rs.getInt("ID_CLIENTE");
                total = rs.getBigDecimal("TOTAL");

                if (!"PENDIENTE".equals(rs.getString("ESTADO"))) {
                    throw new SQLException("El pedido no esta pendiente");
                }
            }
        }

        if (!"EFECTIVO".equals(metodo) && !"TARJETA".equals(metodo)) {
            throw new SQLException("Forma de pago invalida");
        }

        if ("TARJETA".equals(metodo) && !tarjetaAprobada) {
            throw new SQLException("Tarjeta rechazada");
        }

        java.math.BigDecimal montoRecibido =
                java.math.BigDecimal.valueOf(recibido);

        if ("EFECTIVO".equals(metodo)
                && montoRecibido.compareTo(total) < 0) {
            throw new SQLException("Efectivo insuficiente");
        }

        if ("TARJETA".equals(metodo)) {
            montoRecibido = total;
        }

        java.math.BigDecimal cambio =
                montoRecibido.subtract(total);

        java.util.TreeMap<Integer, Integer> cantidades =
                new java.util.TreeMap<>();

        int puntos = 0;

        String sqlDetalle =
                "SELECT ID_PRODUCTO, ID_MENU, CANTIDAD "
                + "FROM DETALLE_PEDIDO WHERE ID_PEDIDO = ?";

        try (PreparedStatement ps = conexion.prepareStatement(sqlDetalle)) {

            ps.setInt(1, idPedido);

            try (ResultSet rs = ps.executeQuery()) {

                boolean tieneDetalles = false;

                while (rs.next()) {

                    tieneDetalles = true;

                    int cantidad = rs.getInt("CANTIDAD");

                    if (cantidad <= 0) {
                        throw new SQLException("Cantidad invalida");
                    }

                    int idProducto = rs.getInt("ID_PRODUCTO");
                    boolean esProducto = !rs.wasNull();

                    if (esProducto) {

                        String sqlCategoria =
                                "SELECT CATEGORIA FROM PRODUCTO "
                                + "WHERE ID_PRODUCTO = ?";

                        try (PreparedStatement psCategoria =
                                conexion.prepareStatement(sqlCategoria)) {

                            psCategoria.setInt(1, idProducto);

                            try (ResultSet rc = psCategoria.executeQuery()) {

                                if (!rc.next()) {
                                    throw new SQLException("Producto no encontrado");
                                }

                                if ("SANDWICH".equals(rc.getString("CATEGORIA"))) {
                                    puntos += cantidad * 2;
                                }
                            }
                        }

                        cantidades.merge(idProducto, cantidad, Integer::sum);

                    } else {

                        int idMenu = rs.getInt("ID_MENU");

                        if (rs.wasNull()) {
                            throw new SQLException("Detalle invalido");
                        }

                        puntos += cantidad * 8;

                        String sqlComponentes =
                                "SELECT ID_PRODUCTO, CANTIDAD "
                                + "FROM DETALLE_MENU WHERE ID_MENU = ?";

                        int componentes = 0;

                        try (PreparedStatement psComponentes =
                                conexion.prepareStatement(sqlComponentes)) {

                            psComponentes.setInt(1, idMenu);

                            try (ResultSet rc = psComponentes.executeQuery()) {

                                while (rc.next()) {

                                    int unidades = rc.getInt("CANTIDAD");

                                    if (unidades <= 0) {
                                        throw new SQLException("Componente invalido");
                                    }

                                    cantidades.merge(
                                            rc.getInt("ID_PRODUCTO"),
                                            unidades * cantidad,
                                            Integer::sum
                                    );

                                    componentes++;
                                }
                            }
                        }

                        if (componentes == 0) {
                            throw new SQLException("Menu sin componentes");
                        }
                    }
                }

                if (!tieneDetalles) {
                    throw new SQLException("Pedido sin detalles");
                }
            }
        }

        String sqlDescontar =
                "UPDATE PRODUCTO "
                + "SET EXISTENCIA = EXISTENCIA - ? "
                + "WHERE ID_PRODUCTO = ? "
                + "AND ESTADO = 'ACTIVO' "
                + "AND EXISTENCIA >= ?";

        String sqlMovimiento =
                "INSERT INTO MOVIMIENTO_INVENTARIO "
                + "(ID_MOVIMIENTO, ID_PRODUCTO, FECHA, TIPO, CANTIDAD, ID_PEDIDO) "
                + "VALUES (?, ?, SYSDATE, 'SALIDA', ?, ?)";

        int idMovimiento;

        try (PreparedStatement ps = conexion.prepareStatement(
                "SELECT NVL(MAX(ID_MOVIMIENTO), 0) FROM MOVIMIENTO_INVENTARIO");
             ResultSet rs = ps.executeQuery()) {

            rs.next();
            idMovimiento = rs.getInt(1);
        }

        for (java.util.Map.Entry<Integer, Integer> entrada :
                cantidades.entrySet()) {

            int idProducto = entrada.getKey();
            int cantidad = entrada.getValue();

            try (PreparedStatement ps =
                    conexion.prepareStatement(sqlDescontar)) {

                ps.setInt(1, cantidad);
                ps.setInt(2, idProducto);
                ps.setInt(3, cantidad);

                if (ps.executeUpdate() != 1) {
                    throw new SQLException(
                            "Existencia insuficiente del producto " + idProducto
                    );
                }
            }

            idMovimiento++;

            try (PreparedStatement ps =
                    conexion.prepareStatement(sqlMovimiento)) {

                ps.setInt(1, idMovimiento);
                ps.setInt(2, idProducto);
                ps.setInt(3, cantidad);
                ps.setInt(4, idPedido);
                ps.executeUpdate();
            }
        }

        String sqlPago =
                "INSERT INTO PAGO "
                + "(ID_PAGO, ID_PEDIDO, FECHA, METODO, MONTO, "
                + "RECIBIDO, CAMBIO, REFERENCIA, ESTADO) "
                + "VALUES ((SELECT NVL(MAX(ID_PAGO), 0) + 1 FROM PAGO), "
                + "?, SYSDATE, ?, ?, ?, ?, ?, 'APROBADO')";

        try (PreparedStatement ps = conexion.prepareStatement(sqlPago)) {

            ps.setInt(1, idPedido);
            ps.setString(2, metodo);
            ps.setBigDecimal(3, total);
            ps.setBigDecimal(4, montoRecibido);
            ps.setBigDecimal(5, cambio);
            ps.setString(6, "TARJETA".equals(metodo)
                    ? "SIMULACION APROBADA" : null);

            ps.executeUpdate();
        }

        if (puntos > 0) {

            String sqlPuntos =
                    "UPDATE CLIENTE SET PUNTOS = PUNTOS + ? "
                    + "WHERE ID_CLIENTE = ? AND ESTADO = 'ACTIVO'";

            try (PreparedStatement ps = conexion.prepareStatement(sqlPuntos)) {

                ps.setInt(1, puntos);
                ps.setInt(2, idCliente);

                if (ps.executeUpdate() != 1) {
                    throw new SQLException("Cliente no disponible");
                }
            }

            String sqlMovimientoPuntos =
                    "INSERT INTO MOVIMIENTO_PUNTOS "
                    + "(ID_MOVIMIENTO, ID_CLIENTE, FECHA, TIPO, PUNTOS) "
                    + "VALUES ((SELECT NVL(MAX(ID_MOVIMIENTO), 0) + 1 "
                    + "FROM MOVIMIENTO_PUNTOS), ?, SYSDATE, 'GANADO', ?)";

            try (PreparedStatement ps =
                    conexion.prepareStatement(sqlMovimientoPuntos)) {

                ps.setInt(1, idCliente);
                ps.setInt(2, puntos);
                ps.executeUpdate();
            }
        }

        try (PreparedStatement ps = conexion.prepareStatement(
                "UPDATE PEDIDO SET ESTADO = 'PAGADO' WHERE ID_PEDIDO = ?")) {

            ps.setInt(1, idPedido);
            ps.executeUpdate();
        }

        conexion.commit();

        System.out.println(
                "Pedido cobrado correctamente. Puntos ganados: " + puntos
        );

        return true;

    } catch (SQLException | ArithmeticException e) {

        try {
            conexion.rollback();
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }

        System.out.println("Error al cobrar pedido: " + e.getMessage());
        return false;

    } finally {

        try {
            conexion.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}



public ArrayList<Recompensa> mostrarRecompensas() {

    ArrayList<Recompensa> recompensas = new ArrayList<>();

    String sql = "SELECT * FROM RECOMPENSA ORDER BY ID_RECOMPENSA";

    try (Connection conexion = conectar()) {

        if (conexion == null) {
            return recompensas;
        }

        try (PreparedStatement ps = conexion.prepareStatement(sql);
             ResultSet resultado = ps.executeQuery()) {

            while (resultado.next()) {

                Recompensa recompensa = new Recompensa(
                        resultado.getInt("ID_RECOMPENSA"),
                        resultado.getString("NOMBRE"),
                        resultado.getInt("PUNTOS_NECESARIOS"),
                        resultado.getInt("ID_PRODUCTO"),
                        resultado.getInt("ID_MENU"),
                        resultado.getString("ESTADO")
                );

                recompensas.add(recompensa);
            }
        }

    } catch (SQLException e) {
        System.out.println("Error al mostrar recompensas: " + e.getMessage());
    }

    return recompensas;
}




public ArrayList<MovimientoPuntos> mostrarMovimientosPuntos(int idCliente) {

    ArrayList<MovimientoPuntos> movimientos = new ArrayList<>();

    String sql =
            "SELECT ID_MOVIMIENTO, ID_CLIENTE, FECHA, TIPO, PUNTOS "
            + "FROM MOVIMIENTO_PUNTOS "
            + "WHERE ID_CLIENTE = ? "
            + "ORDER BY ID_MOVIMIENTO DESC";

    try (Connection conexion = conectar()) {

        if (conexion == null) {
            return movimientos;
        }

        try (PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setInt(1, idCliente);

            try (ResultSet resultado = ps.executeQuery()) {

                while (resultado.next()) {

                    MovimientoPuntos movimiento = new MovimientoPuntos(
                            resultado.getInt("ID_MOVIMIENTO"),
                            resultado.getInt("ID_CLIENTE"),
                            resultado.getString("FECHA"),
                            resultado.getString("TIPO"),
                            resultado.getInt("PUNTOS")
                    );

                    movimientos.add(movimiento);
                }
            }
        }

    } catch (SQLException e) {
        System.out.println("Error al consultar puntos: " + e.getMessage());
    }

    return movimientos;
}



public ArrayList<Canje> mostrarCanjes(int idCliente) {

    ArrayList<Canje> canjes = new ArrayList<>();

    String sql =
            "SELECT ID_CANJE, ID_CLIENTE, ID_RECOMPENSA, "
            + "FECHA, PUNTOS_UTILIZADOS "
            + "FROM CANJE WHERE ID_CLIENTE = ? "
            + "ORDER BY ID_CANJE DESC";

    try (Connection conexion = conectar()) {

        if (conexion == null) {
            return canjes;
        }

        try (PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setInt(1, idCliente);

            try (ResultSet resultado = ps.executeQuery()) {

                while (resultado.next()) {

                    Canje canje = new Canje(
                            resultado.getInt("ID_CANJE"),
                            resultado.getInt("ID_CLIENTE"),
                            resultado.getInt("ID_RECOMPENSA"),
                            resultado.getString("FECHA"),
                            resultado.getInt("PUNTOS_UTILIZADOS")
                    );

                    canjes.add(canje);
                }
            }
        }

    } catch (SQLException e) {
        System.out.println("Error al consultar canjes: " + e.getMessage());
    }

    return canjes;
}



public boolean canjearRecompensa(int idCliente, int idRecompensa) {

    Connection conexion = conectar();

    if (conexion == null) {
        return false;
    }

    try {

        conexion.setAutoCommit(false);

        int puntosDisponibles;
        int puntosNecesarios;
        int idProducto = 0;
        int idMenu = 0;

        String sqlCliente =
                "SELECT PUNTOS FROM CLIENTE "
                + "WHERE ID_CLIENTE = ? AND ESTADO = 'ACTIVO' "
                + "FOR UPDATE";

        try (PreparedStatement ps = conexion.prepareStatement(sqlCliente)) {

            ps.setInt(1, idCliente);

            try (ResultSet rs = ps.executeQuery()) {

                if (!rs.next()) {
                    throw new SQLException("Cliente no disponible");
                }

                puntosDisponibles = rs.getInt("PUNTOS");
            }
        }

        String sqlRecompensa =
                "SELECT PUNTOS_NECESARIOS, ID_PRODUCTO, ID_MENU "
                + "FROM RECOMPENSA "
                + "WHERE ID_RECOMPENSA = ? AND ESTADO = 'ACTIVO'";

        try (PreparedStatement ps = conexion.prepareStatement(sqlRecompensa)) {

            ps.setInt(1, idRecompensa);

            try (ResultSet rs = ps.executeQuery()) {

                if (!rs.next()) {
                    throw new SQLException("Recompensa no disponible");
                }

                puntosNecesarios = rs.getInt("PUNTOS_NECESARIOS");

                idProducto = rs.getInt("ID_PRODUCTO");
                boolean tieneProducto = !rs.wasNull();

                idMenu = rs.getInt("ID_MENU");
                boolean tieneMenu = !rs.wasNull();

                if (tieneProducto == tieneMenu) {
                    throw new SQLException("Recompensa mal configurada");
                }
            }
        }

        if (puntosNecesarios <= 0) {
            throw new SQLException("Puntos de recompensa invalidos");
        }

        if (puntosDisponibles < puntosNecesarios) {
            throw new SQLException("El cliente no tiene suficientes puntos");
        }

        java.util.TreeMap<Integer, Integer> productos =
                new java.util.TreeMap<>();

        if (idProducto > 0) {

            productos.put(idProducto, 1);

        } else {

            String sqlMenu =
                    "SELECT ESTADO FROM MENU WHERE ID_MENU = ?";

            try (PreparedStatement ps = conexion.prepareStatement(sqlMenu)) {

                ps.setInt(1, idMenu);

                try (ResultSet rs = ps.executeQuery()) {

                    if (!rs.next() || !"ACTIVO".equals(rs.getString("ESTADO"))) {
                        throw new SQLException("Menu no disponible");
                    }
                }
            }

            String sqlDetalle =
                    "SELECT ID_PRODUCTO, CANTIDAD "
                    + "FROM DETALLE_MENU WHERE ID_MENU = ?";

            try (PreparedStatement ps = conexion.prepareStatement(sqlDetalle)) {

                ps.setInt(1, idMenu);

                try (ResultSet rs = ps.executeQuery()) {

                    while (rs.next()) {

                        int cantidad = rs.getInt("CANTIDAD");

                        if (cantidad <= 0) {
                            throw new SQLException("Cantidad invalida");
                        }

                        productos.merge(
                                rs.getInt("ID_PRODUCTO"),
                                cantidad,
                                Integer::sum
                        );
                    }
                }
            }

            if (productos.isEmpty()) {
                throw new SQLException("Menu sin productos");
            }
        }

        String sqlStock =
                "UPDATE PRODUCTO "
                + "SET EXISTENCIA = EXISTENCIA - ? "
                + "WHERE ID_PRODUCTO = ? "
                + "AND ESTADO = 'ACTIVO' "
                + "AND EXISTENCIA >= ?";

        String sqlMovimiento =
                "INSERT INTO MOVIMIENTO_INVENTARIO "
                + "(ID_MOVIMIENTO, ID_PRODUCTO, FECHA, TIPO, CANTIDAD, ID_PEDIDO) "
                + "VALUES (?, ?, SYSDATE, 'CANJE', ?, NULL)";

        int idMovimiento;

        try (PreparedStatement ps = conexion.prepareStatement(
                "SELECT NVL(MAX(ID_MOVIMIENTO), 0) FROM MOVIMIENTO_INVENTARIO");
             ResultSet rs = ps.executeQuery()) {

            rs.next();
            idMovimiento = rs.getInt(1);
        }

        for (java.util.Map.Entry<Integer, Integer> item : productos.entrySet()) {

            int producto = item.getKey();
            int cantidad = item.getValue();

            try (PreparedStatement ps = conexion.prepareStatement(sqlStock)) {

                ps.setInt(1, cantidad);
                ps.setInt(2, producto);
                ps.setInt(3, cantidad);

                if (ps.executeUpdate() != 1) {
                    throw new SQLException(
                            "Existencia insuficiente del producto " + producto
                    );
                }
            }

            idMovimiento++;

            try (PreparedStatement ps = conexion.prepareStatement(sqlMovimiento)) {

                ps.setInt(1, idMovimiento);
                ps.setInt(2, producto);
                ps.setInt(3, cantidad);
                ps.executeUpdate();
            }
        }

        String sqlDescontarPuntos =
                "UPDATE CLIENTE SET PUNTOS = PUNTOS - ? "
                + "WHERE ID_CLIENTE = ? AND PUNTOS >= ?";

        try (PreparedStatement ps = conexion.prepareStatement(sqlDescontarPuntos)) {

            ps.setInt(1, puntosNecesarios);
            ps.setInt(2, idCliente);
            ps.setInt(3, puntosNecesarios);

            if (ps.executeUpdate() != 1) {
                throw new SQLException("No se pudieron descontar los puntos");
            }
        }

        String sqlCanje =
                "INSERT INTO CANJE "
                + "(ID_CANJE, ID_CLIENTE, ID_RECOMPENSA, FECHA, PUNTOS_UTILIZADOS) "
                + "VALUES ((SELECT NVL(MAX(ID_CANJE), 0) + 1 FROM CANJE), "
                + "?, ?, SYSDATE, ?)";

        try (PreparedStatement ps = conexion.prepareStatement(sqlCanje)) {

            ps.setInt(1, idCliente);
            ps.setInt(2, idRecompensa);
            ps.setInt(3, puntosNecesarios);
            ps.executeUpdate();
        }

        String sqlPuntos =
                "INSERT INTO MOVIMIENTO_PUNTOS "
                + "(ID_MOVIMIENTO, ID_CLIENTE, FECHA, TIPO, PUNTOS) "
                + "VALUES ((SELECT NVL(MAX(ID_MOVIMIENTO), 0) + 1 "
                + "FROM MOVIMIENTO_PUNTOS), ?, SYSDATE, 'CANJE', ?)";

        try (PreparedStatement ps = conexion.prepareStatement(sqlPuntos)) {

            ps.setInt(1, idCliente);
            ps.setInt(2, -puntosNecesarios);
            ps.executeUpdate();
        }

        conexion.commit();

        System.out.println("Recompensa canjeada correctamente");

        return true;

    } catch (SQLException e) {

        try {
            conexion.rollback();
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }

        System.out.println("Error al canjear recompensa: " + e.getMessage());

        return false;

    } finally {

        try {
            conexion.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}


public ArrayList<Object[]> mostrarReporte(
        int tipo,
        String fechaInicio,
        String fechaFin) {

    ArrayList<Object[]> datos = new ArrayList<>();

    String sql = "";

    switch (tipo) {

        case 1:
            sql = "SELECT p.ID_PEDIDO, c.NOMBRE, "
                    + "TO_CHAR(pa.FECHA, 'YYYY-MM-DD HH24:MI:SS') FECHA, "
                    + "pa.METODO, pa.MONTO "
                    + "FROM PAGO pa "
                    + "INNER JOIN PEDIDO p ON pa.ID_PEDIDO = p.ID_PEDIDO "
                    + "INNER JOIN CLIENTE c ON p.ID_CLIENTE = c.ID_CLIENTE "
                    + "WHERE pa.ESTADO = 'APROBADO' "
                    + "AND pa.FECHA >= TO_DATE(?, 'YYYY-MM-DD') "
                    + "AND pa.FECHA < TO_DATE(?, 'YYYY-MM-DD') + 1 "
                    + "ORDER BY pa.FECHA DESC";
            break;

        case 2:
            sql = "SELECT p.ID_PRODUCTO, p.NOMBRE, "
                    + "SUM(v.CANTIDAD) CANTIDAD "
                    + "FROM PRODUCTO p "
                    + "INNER JOIN ("
                    + "SELECT dp.ID_PRODUCTO, dp.CANTIDAD "
                    + "FROM DETALLE_PEDIDO dp "
                    + "INNER JOIN PEDIDO pe ON dp.ID_PEDIDO = pe.ID_PEDIDO "
                    + "WHERE pe.ESTADO IN ('PAGADO', 'ENTREGADO') "
                    + "AND dp.ID_PRODUCTO IS NOT NULL "
                    + "UNION ALL "
                    + "SELECT dm.ID_PRODUCTO, dp.CANTIDAD * dm.CANTIDAD "
                    + "FROM DETALLE_PEDIDO dp "
                    + "INNER JOIN PEDIDO pe ON dp.ID_PEDIDO = pe.ID_PEDIDO "
                    + "INNER JOIN DETALLE_MENU dm ON dp.ID_MENU = dm.ID_MENU "
                    + "WHERE pe.ESTADO IN ('PAGADO', 'ENTREGADO')"
                    + ") v ON p.ID_PRODUCTO = v.ID_PRODUCTO "
                    + "GROUP BY p.ID_PRODUCTO, p.NOMBRE "
                    + "ORDER BY CANTIDAD DESC";
            break;

        case 3:
            sql = "SELECT ID_PRODUCTO, CODIGO, NOMBRE, "
                    + "CATEGORIA, EXISTENCIA "
                    + "FROM PRODUCTO "
                    + "WHERE EXISTENCIA <= 5 "
                    + "AND ESTADO = 'ACTIVO' "
                    + "ORDER BY EXISTENCIA ASC";
            break;

        case 4:
            sql = "SELECT ID_CLIENTE, IDENTIFICADOR, NOMBRE, "
                    + "PUNTOS, ESTADO "
                    + "FROM CLIENTE "
                    + "ORDER BY PUNTOS DESC";
            break;

        case 5:
            sql = "SELECT ca.ID_CANJE, c.NOMBRE, r.NOMBRE, "
                    + "TO_CHAR(ca.FECHA, 'YYYY-MM-DD HH24:MI:SS'), "
                    + "ca.PUNTOS_UTILIZADOS "
                    + "FROM CANJE ca "
                    + "INNER JOIN CLIENTE c ON ca.ID_CLIENTE = c.ID_CLIENTE "
                    + "INNER JOIN RECOMPENSA r "
                    + "ON ca.ID_RECOMPENSA = r.ID_RECOMPENSA "
                    + "ORDER BY ca.FECHA DESC";
            break;

        case 6:
            sql = "SELECT m.ID_MENU, m.NOMBRE, "
                    + "SUM(dp.CANTIDAD) CANTIDAD, "
                    + "SUM(dp.SUBTOTAL) TOTAL "
                    + "FROM DETALLE_PEDIDO dp "
                    + "INNER JOIN MENU m ON dp.ID_MENU = m.ID_MENU "
                    + "INNER JOIN PEDIDO p ON dp.ID_PEDIDO = p.ID_PEDIDO "
                    + "WHERE p.ESTADO IN ('PAGADO', 'ENTREGADO') "
                    + "GROUP BY m.ID_MENU, m.NOMBRE "
                    + "ORDER BY CANTIDAD DESC";
            break;

        default:
            return datos;
    }

    try (Connection conexion = conectar()) {

        if (conexion == null) {
            return datos;
        }

        try (PreparedStatement ps = conexion.prepareStatement(sql)) {

            if (tipo == 1) {
                ps.setString(1, fechaInicio);
                ps.setString(2, fechaFin);
            }

            try (ResultSet resultado = ps.executeQuery()) {

                int columnas = resultado.getMetaData().getColumnCount();

                while (resultado.next()) {

                    Object[] fila = new Object[columnas];

                    for (int i = 0; i < columnas; i++) {
                        fila[i] = resultado.getObject(i + 1);
                    }

                    datos.add(fila);
                }
            }
        }

    } catch (SQLException e) {

        System.out.println(
                "Error al consultar reporte: " + e.getMessage()
        );
    }

    return datos;
}




}
