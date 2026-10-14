
package panpuntospremios;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JComboBox;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.JScrollPane;
import javax.swing.table.DefaultTableModel;
import java.util.ArrayList;

public class FrmProducto extends JFrame {

    JLabel lblTitulo = new JLabel("REGISTRO DE PRODUCTOS");

    JLabel lblCodigo = new JLabel("Codigo:");
    JLabel lblNombre = new JLabel("Nombre:");
    JLabel lblCategoria = new JLabel("Categoria:");
    JLabel lblPrecio = new JLabel("Precio:");
    JLabel lblExistencia = new JLabel("Existencia:");
    JLabel lblEstado = new JLabel("Estado:");

    JTextField txtCodigo = new JTextField();
    JTextField txtNombre = new JTextField();
    JTextField txtPrecio = new JTextField();
    JTextField txtExistencia = new JTextField();
    JTextField txtEstado = new JTextField("ACTIVO");

    JComboBox<String> cbCategoria = new JComboBox<>(
            new String[]{
                "SANDWICH",
                "BEBIDA",
                "ACOMPANAMIENTO"
            }
    );

    JButton btnGuardar = new JButton("Guardar");
    JButton btnActualizar = new JButton("Actualizar");
    JButton btnMostrar = new JButton("Mostrar");
    JButton btnLimpiar = new JButton("Limpiar");

    DefaultTableModel modelo = new DefaultTableModel();

    JTable tabla = new JTable(modelo);
    JScrollPane scroll = new JScrollPane(tabla);

    Conexion conexion = new Conexion();

    int idProducto = 0;

    public FrmProducto() {

        setTitle("Pan, Puntos y Premios - Productos");
        setSize(950, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);

        lblTitulo.setBounds(370, 20, 250, 30);
        add(lblTitulo);

        lblCodigo.setBounds(40, 80, 100, 25);
        txtCodigo.setBounds(150, 80, 200, 25);

        lblNombre.setBounds(40, 120, 100, 25);
        txtNombre.setBounds(150, 120, 200, 25);

        lblCategoria.setBounds(40, 160, 100, 25);
        cbCategoria.setBounds(150, 160, 200, 25);

        lblPrecio.setBounds(40, 200, 100, 25);
        txtPrecio.setBounds(150, 200, 200, 25);

        lblExistencia.setBounds(40, 240, 100, 25);
        txtExistencia.setBounds(150, 240, 200, 25);

        lblEstado.setBounds(40, 280, 100, 25);
        txtEstado.setBounds(150, 280, 200, 25);
        txtEstado.setEditable(false);

        add(lblCodigo);
        add(txtCodigo);

        add(lblNombre);
        add(txtNombre);

        add(lblCategoria);
        add(cbCategoria);

        add(lblPrecio);
        add(txtPrecio);

        add(lblExistencia);
        add(txtExistencia);

        add(lblEstado);
        add(txtEstado);

        btnGuardar.setBounds(420, 80, 120, 30);
        btnActualizar.setBounds(560, 80, 120, 30);
        btnMostrar.setBounds(420, 130, 120, 30);
        btnLimpiar.setBounds(560, 130, 120, 30);

        add(btnGuardar);
        add(btnActualizar);
        add(btnMostrar);
        add(btnLimpiar);
        
        btnActualizar.setEnabled(false);

        modelo.addColumn("ID");
        modelo.addColumn("Codigo");
        modelo.addColumn("Nombre");
        modelo.addColumn("Categoria");
        modelo.addColumn("Precio");
        modelo.addColumn("Existencia");
        modelo.addColumn("Estado");

        scroll.setBounds(40, 340, 850, 180);
        add(scroll);

        btnGuardar.addActionListener(e -> guardarProducto());

        btnActualizar.addActionListener(e -> actualizarProducto());

        btnMostrar.addActionListener(e -> mostrarProductos());

        btnLimpiar.addActionListener(e -> limpiar());

        tabla.getSelectionModel().addListSelectionListener(e -> {

            if (!e.getValueIsAdjusting()
                    && tabla.getSelectedRow() >= 0) {

                int fila = tabla.getSelectedRow();

                idProducto = Integer.parseInt(
                        tabla.getValueAt(fila, 0).toString()
                );

                txtCodigo.setText(
                        tabla.getValueAt(fila, 1).toString()
                );

                txtNombre.setText(
                        tabla.getValueAt(fila, 2).toString()
                );

                cbCategoria.setSelectedItem(
                        tabla.getValueAt(fila, 3).toString()
                );

                txtPrecio.setText(
                        tabla.getValueAt(fila, 4).toString()
                );

                txtExistencia.setText(
                        tabla.getValueAt(fila, 5).toString()
                );

                txtEstado.setText(
                        tabla.getValueAt(fila, 6).toString()
                );
                btnActualizar.setEnabled(true);
            }
        });

        mostrarProductos();
    }

    public void mostrarProductos() {

        modelo.setRowCount(0);

        ArrayList<Producto> productos =
                conexion.mostrarProductos();

        for (Producto producto : productos) {

            modelo.addRow(new Object[]{
                producto.idProducto,
                producto.codigo,
                producto.nombre,
                producto.categoria,
                producto.precio,
                producto.existencia,
                producto.estado
            });
        }
    }

    public void guardarProducto() {

        String codigo = txtCodigo.getText().trim();
        String nombre = txtNombre.getText().trim();

        String categoria =
                cbCategoria.getSelectedItem().toString();

        if (codigo.isEmpty() || nombre.isEmpty()
                || txtPrecio.getText().trim().isEmpty()
                || txtExistencia.getText().trim().isEmpty()) {

            JOptionPane.showMessageDialog(
                    this,
                    "Complete todos los campos"
            );

            return;
        }

        try {

            double precio = Double.parseDouble(
                    txtPrecio.getText().trim()
            );

            int existencia = Integer.parseInt(
                    txtExistencia.getText().trim()
            );

            if (!Double.isFinite(precio)
                    || precio < 0 || existencia < 0) {

                JOptionPane.showMessageDialog(
                        this,
                        "El precio y la existencia no pueden ser negativos"
                );

                return;
            }

            conexion.insertarProducto(
                    codigo,
                    nombre,
                    categoria,
                    precio,
                    existencia
            );

            mostrarProductos();
            limpiar();

        } catch (NumberFormatException e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Ingrese un precio y una existencia validos"
            );
        }
    }

    public void actualizarProducto() {

        if (idProducto == 0) {

            JOptionPane.showMessageDialog(
                    this,
                    "Seleccione un producto de la tabla"
            );

            return;
        }

        String codigo = txtCodigo.getText().trim();
        String nombre = txtNombre.getText().trim();

        String categoria =
                cbCategoria.getSelectedItem().toString();

        if (codigo.isEmpty() || nombre.isEmpty()
                || txtPrecio.getText().trim().isEmpty()
                || txtExistencia.getText().trim().isEmpty()) {

            JOptionPane.showMessageDialog(
                    this,
                    "Complete todos los campos"
            );

            return;
        }

        try {

            double precio = Double.parseDouble(
                    txtPrecio.getText().trim()
            );

            int existencia = Integer.parseInt(
                    txtExistencia.getText().trim()
            );

            if (!Double.isFinite(precio)
                    || precio < 0 || existencia < 0) {

                JOptionPane.showMessageDialog(
                        this,
                        "El precio y la existencia no pueden ser negativos"
                );

                return;
            }

            conexion.actualizarProducto(
                    idProducto,
                    codigo,
                    nombre,
                    categoria,
                    precio,
                    existencia
            );

            mostrarProductos();
            limpiar();

        } catch (NumberFormatException e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Ingrese un precio y una existencia validos"
            );
        }
    }

    public void limpiar() {

        idProducto = 0;

        tabla.clearSelection();

        txtCodigo.setText("");
        txtNombre.setText("");
        cbCategoria.setSelectedIndex(0);
        txtPrecio.setText("");
        txtExistencia.setText("");
        txtEstado.setText("ACTIVO");
    }
}
