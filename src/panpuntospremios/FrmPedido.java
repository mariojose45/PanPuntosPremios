
package panpuntospremios;

import java.util.ArrayList;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JComboBox;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.JScrollPane;
import javax.swing.table.DefaultTableModel;

public class FrmPedido extends JFrame {

    JLabel lblTitulo = new JLabel("REGISTRO DE PEDIDOS");
    JLabel lblCliente = new JLabel("Cliente:");
    JLabel lblTipo = new JLabel("Tipo:");
    JLabel lblArticulo = new JLabel("Producto / Menu:");
    JLabel lblCantidad = new JLabel("Cantidad:");
    JLabel lblTotal = new JLabel("Total: Q0.00");

    JLabel lblFormaPago = new JLabel("Forma de pago:");
    JLabel lblRecibido = new JLabel("Monto recibido:");
    JLabel lblCambio = new JLabel("Cambio: Q0.00");

    JComboBox<String> cbCliente = new JComboBox<>();
    JComboBox<String> cbTipo = new JComboBox<>(
            new String[]{"PRODUCTO", "MENU"}
    );
    JComboBox<String> cbArticulo = new JComboBox<>();
    JComboBox<String> cbFormaPago = new JComboBox<>(
            new String[]{"EFECTIVO", "TARJETA"}
    );

    JTextField txtCantidad = new JTextField("1");
    JTextField txtRecibido = new JTextField();

    JButton btnAgregar = new JButton("Agregar");
    JButton btnQuitar = new JButton("Quitar");
    JButton btnGuardar = new JButton("Guardar Pedido");
    JButton btnCobrar = new JButton("Cobrar Pedido");
    JButton btnMostrar = new JButton("Mostrar Pedidos");
    JButton btnLimpiar = new JButton("Limpiar");

    DefaultTableModel modeloDetalle = new DefaultTableModel(
            new Object[]{
                "Tipo", "ID", "Descripcion",
                "Cantidad", "Precio", "Subtotal"
            }, 0
    ) {
        @Override
        public boolean isCellEditable(int fila, int columna) {
            return false;
        }
    };

    JTable tablaDetalle = new JTable(modeloDetalle);
    JScrollPane scrollDetalle = new JScrollPane(tablaDetalle);

    DefaultTableModel modeloPedidos = new DefaultTableModel(
            new Object[]{
                "ID", "Cliente", "Fecha", "Total", "Estado"
            }, 0
    ) {
        @Override
        public boolean isCellEditable(int fila, int columna) {
            return false;
        }
    };

    JTable tablaPedidos = new JTable(modeloPedidos);
    JScrollPane scrollPedidos = new JScrollPane(tablaPedidos);

    Conexion conexion = new Conexion();

    ArrayList<Cliente> clientes = new ArrayList<>();
    ArrayList<Producto> productos = new ArrayList<>();
    ArrayList<Menu> menus = new ArrayList<>();
    ArrayList<DetallePedido> detalles = new ArrayList<>();

    boolean consultandoPedido = false;

    public FrmPedido() {

        setTitle("Pan, Puntos y Premios - Pedidos");
        setSize(1100, 790);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);

        lblTitulo.setBounds(450, 15, 250, 30);
        add(lblTitulo);

        lblCliente.setBounds(40, 65, 120, 25);
        cbCliente.setBounds(170, 65, 320, 25);

        lblTipo.setBounds(40, 105, 120, 25);
        cbTipo.setBounds(170, 105, 180, 25);

        lblArticulo.setBounds(40, 145, 130, 25);
        cbArticulo.setBounds(170, 145, 320, 25);

        lblCantidad.setBounds(40, 185, 120, 25);
        txtCantidad.setBounds(170, 185, 100, 25);

        add(lblCliente);
        add(cbCliente);
        add(lblTipo);
        add(cbTipo);
        add(lblArticulo);
        add(cbArticulo);
        add(lblCantidad);
        add(txtCantidad);

        btnAgregar.setBounds(550, 65, 160, 35);
        btnQuitar.setBounds(730, 65, 160, 35);
        btnGuardar.setBounds(550, 115, 160, 35);
        btnCobrar.setBounds(730, 115, 160, 35);
        btnLimpiar.setBounds(550, 160, 160, 30);

        add(btnAgregar);
        add(btnQuitar);
        add(btnGuardar);
        add(btnCobrar);
        add(btnLimpiar);

        lblFormaPago.setBounds(550, 205, 130, 25);
        cbFormaPago.setBounds(690, 205, 200, 25);

        lblRecibido.setBounds(550, 240, 130, 25);
        txtRecibido.setBounds(690, 240, 200, 25);

        lblCambio.setBounds(690, 275, 200, 25);

        add(lblFormaPago);
        add(cbFormaPago);
        add(lblRecibido);
        add(txtRecibido);
        add(lblCambio);

        JLabel lblDetalle = new JLabel("DETALLE DEL PEDIDO");
        lblDetalle.setBounds(40, 305, 250, 25);
        add(lblDetalle);

        scrollDetalle.setBounds(40, 335, 1000, 130);
        add(scrollDetalle);

        lblTotal.setBounds(800, 470, 240, 30);
        add(lblTotal);

        JLabel lblPedidos = new JLabel("PEDIDOS REGISTRADOS");
        lblPedidos.setBounds(40, 510, 250, 25);
        add(lblPedidos);

        btnMostrar.setBounds(850, 505, 190, 30);
        add(btnMostrar);

        scrollPedidos.setBounds(40, 545, 1000, 155);
        add(scrollPedidos);

        cbTipo.addActionListener(e -> cargarArticulos());

        btnAgregar.addActionListener(e -> agregarDetalle());
        btnQuitar.addActionListener(e -> quitarDetalle());
        btnGuardar.addActionListener(e -> guardarPedido());
        btnCobrar.addActionListener(e -> cobrarPedido());
        btnMostrar.addActionListener(e -> mostrarPedidos());
        btnLimpiar.addActionListener(e -> limpiar());

        txtRecibido.addCaretListener(e -> calcularCambio());

        cbFormaPago.addActionListener(e -> {

            boolean efectivo = "EFECTIVO".equals(
                    cbFormaPago.getSelectedItem()
            );

            txtRecibido.setEnabled(efectivo);

            if (!efectivo) {
                txtRecibido.setText("");
            }

            calcularCambio();
        });

        tablaPedidos.getSelectionModel().addListSelectionListener(e -> {

            if (!e.getValueIsAdjusting()
                    && tablaPedidos.getSelectedRow() >= 0) {

                int fila = tablaPedidos.getSelectedRow();

                int idPedido = Integer.parseInt(
                        tablaPedidos.getValueAt(fila, 0).toString()
                );

                mostrarDetalleGuardado(idPedido);

                calcularCambio();
            }
        });

        cargarClientes();
        cargarDatos();
        mostrarPedidos();
    }

    public void cargarClientes() {

        cbCliente.removeAllItems();

        clientes = conexion.mostrarClientes();

        for (Cliente cliente : clientes) {

            if ("ACTIVO".equals(cliente.estado)) {

                cbCliente.addItem(
                        cliente.idCliente + " - " + cliente.nombre
                );
            }
        }
    }

    public void cargarDatos() {

        productos = conexion.mostrarProductos();
        menus = conexion.mostrarMenus();

        cargarArticulos();
    }

    public void cargarArticulos() {

        cbArticulo.removeAllItems();

        if ("PRODUCTO".equals(cbTipo.getSelectedItem())) {

            for (Producto producto : productos) {

                if ("ACTIVO".equals(producto.estado)) {

                    cbArticulo.addItem(
                            producto.idProducto + " - " + producto.nombre
                    );
                }
            }

        } else {

            for (Menu menu : menus) {

                if ("ACTIVO".equals(menu.estado)) {

                    cbArticulo.addItem(
                            menu.idMenu + " - " + menu.nombre
                    );
                }
            }
        }
    }

    public int obtenerId(String texto) {

        return Integer.parseInt(
                texto.split(" - ")[0]
        );
    }

    public void agregarDetalle() {

        if (consultandoPedido) {

            JOptionPane.showMessageDialog(
                    this,
                    "Presione Limpiar para crear un pedido nuevo"
            );

            return;
        }

        if (cbArticulo.getSelectedItem() == null) {

            JOptionPane.showMessageDialog(
                    this,
                    "Seleccione un producto o menu"
            );

            return;
        }

        int cantidad;

        try {

            cantidad = Integer.parseInt(
                    txtCantidad.getText().trim()
            );

            if (cantidad <= 0) {
                throw new NumberFormatException();
            }

        } catch (NumberFormatException e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Ingrese una cantidad mayor que cero"
            );

            return;
        }

        String tipo = cbTipo.getSelectedItem().toString();

        int id = obtenerId(
                cbArticulo.getSelectedItem().toString()
        );

        String descripcion = "";
        double precio = 0;

        if ("PRODUCTO".equals(tipo)) {

            for (Producto producto : productos) {

                if (producto.idProducto == id) {

                    descripcion = producto.nombre;
                    precio = producto.precio;
                    break;
                }
            }

        } else {

            for (Menu menu : menus) {

                if (menu.idMenu == id) {

                    descripcion = menu.nombre;
                    precio = menu.precio;
                    break;
                }
            }
        }

        if (descripcion.isEmpty()) {
            return;
        }

        int idProducto = "PRODUCTO".equals(tipo) ? id : 0;
        int idMenu = "MENU".equals(tipo) ? id : 0;

        double subtotal = Math.round(
                precio * cantidad * 100.0
        ) / 100.0;

        DetallePedido detalle = new DetallePedido(
                0,
                0,
                idProducto,
                idMenu,
                descripcion,
                cantidad,
                precio,
                subtotal
        );

        detalles.add(detalle);

        actualizarTablaDetalle();
    }

    public void actualizarTablaDetalle() {

        modeloDetalle.setRowCount(0);

        double total = 0;

        for (DetallePedido detalle : detalles) {

            String tipo = detalle.idProducto > 0
                    ? "PRODUCTO" : "MENU";

            int id = detalle.idProducto > 0
                    ? detalle.idProducto : detalle.idMenu;

            modeloDetalle.addRow(new Object[]{
                tipo,
                id,
                detalle.descripcion,
                detalle.cantidad,
                detalle.precio,
                detalle.subtotal
            });

            total += detalle.subtotal;
        }

        lblTotal.setText(
                String.format("Total: Q%.2f", total)
        );
    }

    public void quitarDetalle() {

        if (consultandoPedido) {

            JOptionPane.showMessageDialog(
                    this,
                    "No puede modificar un pedido guardado"
            );

            return;
        }

        int fila = tablaDetalle.getSelectedRow();

        if (fila < 0 || fila >= detalles.size()) {

            JOptionPane.showMessageDialog(
                    this,
                    "Seleccione un detalle para quitar"
            );

            return;
        }

        detalles.remove(fila);

        actualizarTablaDetalle();
    }

    public void guardarPedido() {

        if (consultandoPedido) {

            JOptionPane.showMessageDialog(
                    this,
                    "Presione Limpiar para crear un pedido nuevo"
            );

            return;
        }

        if (cbCliente.getSelectedItem() == null) {

            JOptionPane.showMessageDialog(
                    this,
                    "Seleccione un cliente"
            );

            return;
        }

        if (detalles.isEmpty()) {

            JOptionPane.showMessageDialog(
                    this,
                    "Agregue productos o menus al pedido"
            );

            return;
        }

        int idCliente = obtenerId(
                cbCliente.getSelectedItem().toString()
        );

        boolean guardado = conexion.insertarPedido(
                idCliente,
                detalles
        );

        if (guardado) {

            JOptionPane.showMessageDialog(
                    this,
                    "Pedido registrado correctamente"
            );

            limpiar();
            mostrarPedidos();

        } else {

            JOptionPane.showMessageDialog(
                    this,
                    "No se pudo guardar el pedido. Revise la consola."
            );
        }
    }

    public void mostrarPedidos() {

        modeloPedidos.setRowCount(0);

        ArrayList<Pedido> pedidos = conexion.mostrarPedidos();

        for (Pedido pedido : pedidos) {

            modeloPedidos.addRow(new Object[]{
                pedido.idPedido,
                pedido.nombreCliente,
                pedido.fecha,
                pedido.total,
                pedido.estado
            });
        }
    }

    public void mostrarDetalleGuardado(int idPedido) {

        consultandoPedido = true;

        detalles.clear();
        modeloDetalle.setRowCount(0);

        ArrayList<DetallePedido> guardados =
                conexion.mostrarDetallePedido(idPedido);

        double total = 0;

        for (DetallePedido detalle : guardados) {

            String tipo = detalle.idProducto > 0
                    ? "PRODUCTO" : "MENU";

            int id = detalle.idProducto > 0
                    ? detalle.idProducto : detalle.idMenu;

            modeloDetalle.addRow(new Object[]{
                tipo,
                id,
                detalle.descripcion,
                detalle.cantidad,
                detalle.precio,
                detalle.subtotal
            });

            total += detalle.subtotal;
        }

        lblTotal.setText(
                String.format("Total: Q%.2f", total)
        );

        btnAgregar.setEnabled(false);
        btnQuitar.setEnabled(false);
        btnGuardar.setEnabled(false);
    }

    public void calcularCambio() {

        int fila = tablaPedidos.getSelectedRow();

        if (fila < 0
                || !"EFECTIVO".equals(cbFormaPago.getSelectedItem())) {

            lblCambio.setText("Cambio: Q0.00");
            return;
        }

        try {

            double total = Double.parseDouble(
                    tablaPedidos.getValueAt(fila, 3).toString()
            );

            double recibido = Double.parseDouble(
                    txtRecibido.getText().trim()
            );

            if (!Double.isFinite(recibido)) {
                throw new NumberFormatException();
            }

            double cambio = recibido - total;

            lblCambio.setText(
                    String.format(
                            "Cambio: Q%.2f",
                            Math.max(0, cambio)
                    )
            );

        } catch (NumberFormatException e) {

            lblCambio.setText("Cambio: Q0.00");
        }
    }

    public void cobrarPedido() {

        int fila = tablaPedidos.getSelectedRow();

        if (fila < 0) {

            JOptionPane.showMessageDialog(
                    this,
                    "Seleccione un pedido de la tabla inferior"
            );

            return;
        }

        int idPedido = Integer.parseInt(
                tablaPedidos.getValueAt(fila, 0).toString()
        );

        String estado = tablaPedidos.getValueAt(fila, 4).toString();

        if (!"PENDIENTE".equals(estado)) {

            JOptionPane.showMessageDialog(
                    this,
                    "Solamente puede cobrar pedidos pendientes"
            );

            return;
        }

        String metodo = cbFormaPago.getSelectedItem().toString();

        double recibido = 0;
        boolean aprobada = true;

        if ("EFECTIVO".equals(metodo)) {

            try {

                recibido = Double.parseDouble(
                        txtRecibido.getText().trim()
                );

                if (!Double.isFinite(recibido) || recibido < 0) {
                    throw new NumberFormatException();
                }

                double total = Double.parseDouble(
                        tablaPedidos.getValueAt(fila, 3).toString()
                );

                if (recibido < total) {

                    JOptionPane.showMessageDialog(
                            this,
                            "El monto recibido es menor que el total"
                    );

                    return;
                }

            } catch (NumberFormatException e) {

                JOptionPane.showMessageDialog(
                        this,
                        "Ingrese un monto recibido valido"
                );

                return;
            }

        } else {

            int respuesta = JOptionPane.showConfirmDialog(
                    this,
                    "Desea simular la aprobacion de la tarjeta?",
                    "Pago con Tarjeta",
                    JOptionPane.YES_NO_OPTION
            );

            aprobada = respuesta == JOptionPane.YES_OPTION;

            if (!aprobada) {

                JOptionPane.showMessageDialog(
                        this,
                        "Tarjeta rechazada. El pedido sigue pendiente."
                );

                return;
            }
        }

        int confirmar = JOptionPane.showConfirmDialog(
                this,
                "Desea cobrar el pedido No. " + idPedido + "?",
                "Confirmar Cobro",
                JOptionPane.YES_NO_OPTION
        );

        if (confirmar != JOptionPane.YES_OPTION) {
            return;
        }

        boolean cobrado = conexion.cobrarPedido(
                idPedido,
                metodo,
                recibido,
                aprobada
        );

        if (cobrado) {

            JOptionPane.showMessageDialog(
                    this,
                    "Pago realizado correctamente"
            );

            limpiar();
            mostrarPedidos();
            cargarDatos();
            cargarClientes();

        } else {

            JOptionPane.showMessageDialog(
                    this,
                    "No se pudo cobrar el pedido. Revise la consola."
            );
        }
    }

    public void limpiar() {

        consultandoPedido = false;

        detalles.clear();
        modeloDetalle.setRowCount(0);
        tablaPedidos.clearSelection();

        txtCantidad.setText("1");
        txtRecibido.setText("");

        lblTotal.setText("Total: Q0.00");
        lblCambio.setText("Cambio: Q0.00");

        btnAgregar.setEnabled(true);
        btnQuitar.setEnabled(true);
        btnGuardar.setEnabled(true);

        if (cbTipo.getItemCount() > 0) {
            cbTipo.setSelectedIndex(0);
        }

        if (cbCliente.getItemCount() > 0) {
            cbCliente.setSelectedIndex(0);
        }

        cbFormaPago.setSelectedItem("EFECTIVO");
    }
}
