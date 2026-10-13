
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

public class FrmMenu extends JFrame {

    JLabel lblTitulo = new JLabel("REGISTRO DE MENUS");

    JLabel lblNombre = new JLabel("Nombre del Menu:");
    JLabel lblPrecio = new JLabel("Precio:");
    JLabel lblSandwich = new JLabel("Sandwich:");
    JLabel lblBebida = new JLabel("Bebida:");
    JLabel lblAcompanamiento = new JLabel("Acompanamiento:");

    JTextField txtNombre = new JTextField();
    JTextField txtPrecio = new JTextField();

    JComboBox<String> cbSandwich = new JComboBox<>();
    JComboBox<String> cbBebida = new JComboBox<>();
    JComboBox<String> cbAcompanamiento = new JComboBox<>();

    JButton btnGuardar = new JButton("Guardar");
    JButton btnMostrar = new JButton("Mostrar Menus");
    JButton btnLimpiar = new JButton("Limpiar");

    DefaultTableModel modeloMenu = new DefaultTableModel();
    JTable tablaMenu = new JTable(modeloMenu);
    JScrollPane scrollMenu = new JScrollPane(tablaMenu);

    DefaultTableModel modeloDetalle = new DefaultTableModel();
    JTable tablaDetalle = new JTable(modeloDetalle);
    JScrollPane scrollDetalle = new JScrollPane(tablaDetalle);

    Conexion conexion = new Conexion();

    ArrayList<Producto> productos = new ArrayList<>();

    public FrmMenu() {

        setTitle("Pan, Puntos y Premios - Menus");
        setSize(1050, 720);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);

        lblTitulo.setBounds(420, 15, 250, 30);
        add(lblTitulo);

        lblNombre.setBounds(40, 65, 150, 25);
        txtNombre.setBounds(200, 65, 240, 25);

        lblPrecio.setBounds(40, 105, 150, 25);
        txtPrecio.setBounds(200, 105, 240, 25);

        lblSandwich.setBounds(40, 145, 150, 25);
        cbSandwich.setBounds(200, 145, 300, 25);

        lblBebida.setBounds(40, 185, 150, 25);
        cbBebida.setBounds(200, 185, 300, 25);

        lblAcompanamiento.setBounds(40, 225, 150, 25);
        cbAcompanamiento.setBounds(200, 225, 300, 25);

        add(lblNombre);
        add(txtNombre);
        add(lblPrecio);
        add(txtPrecio);
        add(lblSandwich);
        add(cbSandwich);
        add(lblBebida);
        add(cbBebida);
        add(lblAcompanamiento);
        add(cbAcompanamiento);

        btnGuardar.setBounds(560, 65, 150, 35);
        btnMostrar.setBounds(730, 65, 170, 35);
        btnLimpiar.setBounds(560, 115, 150, 35);

        add(btnGuardar);
        add(btnMostrar);
        add(btnLimpiar);

        JLabel lblLista = new JLabel("MENUS REGISTRADOS");
        lblLista.setBounds(40, 285, 250, 25);
        add(lblLista);

        modeloMenu.addColumn("ID");
        modeloMenu.addColumn("Nombre");
        modeloMenu.addColumn("Precio");
        modeloMenu.addColumn("Estado");

        scrollMenu.setBounds(40, 315, 950, 130);
        add(scrollMenu);

        JLabel lblDetalle = new JLabel("PRODUCTOS DEL MENU SELECCIONADO");
        lblDetalle.setBounds(40, 465, 350, 25);
        add(lblDetalle);

        modeloDetalle.addColumn("ID Detalle");
        modeloDetalle.addColumn("ID Producto");
        modeloDetalle.addColumn("Producto");
        modeloDetalle.addColumn("Cantidad");

        scrollDetalle.setBounds(40, 495, 950, 140);
        add(scrollDetalle);

        btnGuardar.addActionListener(e -> guardarMenu());
        btnMostrar.addActionListener(e -> mostrarMenus());
        btnLimpiar.addActionListener(e -> limpiar());

        tablaMenu.getSelectionModel().addListSelectionListener(e -> {

            if (!e.getValueIsAdjusting()
                    && tablaMenu.getSelectedRow() >= 0) {

                int fila = tablaMenu.getSelectedRow();

                int idMenu = Integer.parseInt(
                        tablaMenu.getValueAt(fila, 0).toString()
                );

                mostrarDetalleMenu(idMenu);
            }
        });

        cargarProductos();
        mostrarMenus();
    }

    public void cargarProductos() {

        cbSandwich.removeAllItems();
        cbBebida.removeAllItems();
        cbAcompanamiento.removeAllItems();

        productos = conexion.mostrarProductos();

        for (Producto producto : productos) {

            if (!"ACTIVO".equals(producto.estado)) {
                continue;
            }

            String opcion =
                    producto.idProducto + " - " + producto.nombre;

            if ("SANDWICH".equals(producto.categoria)) {

                cbSandwich.addItem(opcion);

            } else if ("BEBIDA".equals(producto.categoria)) {

                cbBebida.addItem(opcion);

            } else if ("ACOMPANAMIENTO".equals(producto.categoria)) {

                cbAcompanamiento.addItem(opcion);
            }
        }
    }

    public int obtenerIdProducto(JComboBox<String> combo) {

        String seleccionado = combo.getSelectedItem().toString();

        return Integer.parseInt(
                seleccionado.split(" - ")[0]
        );
    }

    public void guardarMenu() {

        String nombre = txtNombre.getText().trim();

        if (nombre.isEmpty() || txtPrecio.getText().trim().isEmpty()) {

            JOptionPane.showMessageDialog(
                    this,
                    "Ingrese el nombre y el precio del menu"
            );

            return;
        }

        if (cbSandwich.getSelectedIndex() < 0
                || cbBebida.getSelectedIndex() < 0
                || cbAcompanamiento.getSelectedIndex() < 0) {

            JOptionPane.showMessageDialog(
                    this,
                    "Debe seleccionar un sandwich, una bebida y un acompanamiento"
            );

            return;
        }

        try {

            double precio = Double.parseDouble(
                    txtPrecio.getText().trim()
            );

            if (!Double.isFinite(precio) || precio < 0) {

                JOptionPane.showMessageDialog(
                        this,
                        "Ingrese un precio valido"
                );

                return;
            }

            int idSandwich = obtenerIdProducto(cbSandwich);
            int idBebida = obtenerIdProducto(cbBebida);
            int idAcompanamiento = obtenerIdProducto(cbAcompanamiento);

            conexion.insertarMenu(
                    nombre,
                    precio,
                    idSandwich,
                    idBebida,
                    idAcompanamiento
            );

            mostrarMenus();
            limpiar();

        } catch (NumberFormatException e) {

            JOptionPane.showMessageDialog(
                    this,
                    "El precio debe ser numerico"
            );
        }
    }

    public void mostrarMenus() {

        modeloMenu.setRowCount(0);
        modeloDetalle.setRowCount(0);

        ArrayList<Menu> menus = conexion.mostrarMenus();

        for (Menu menu : menus) {

            modeloMenu.addRow(new Object[]{
                menu.idMenu,
                menu.nombre,
                menu.precio,
                menu.estado
            });
        }
    }

    public void mostrarDetalleMenu(int idMenu) {

        modeloDetalle.setRowCount(0);

        ArrayList<DetalleMenu> detalles =
                conexion.mostrarDetalleMenu(idMenu);

        for (DetalleMenu detalle : detalles) {

            modeloDetalle.addRow(new Object[]{
                detalle.idDetalleMenu,
                detalle.idProducto,
                detalle.nombreProducto,
                detalle.cantidad
            });
        }
    }

    public void limpiar() {

        txtNombre.setText("");
        txtPrecio.setText("");

        tablaMenu.clearSelection();
        modeloDetalle.setRowCount(0);

        if (cbSandwich.getItemCount() > 0) {
            cbSandwich.setSelectedIndex(0);
        }

        if (cbBebida.getItemCount() > 0) {
            cbBebida.setSelectedIndex(0);
        }

        if (cbAcompanamiento.getItemCount() > 0) {
            cbAcompanamiento.setSelectedIndex(0);
        }
    }
}
