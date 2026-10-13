
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

public class FrmInventario extends JFrame {

    JLabel lblTitulo = new JLabel("CONTROL DE INVENTARIO");

    JLabel lblProducto = new JLabel("Producto:");
    JLabel lblCantidad = new JLabel("Cantidad:");
    JLabel lblExistencia = new JLabel("Existencia actual:");

    JComboBox<String> cbProducto = new JComboBox<>();

    JTextField txtCantidad = new JTextField();
    JTextField txtExistencia = new JTextField();

    JButton btnRegistrar = new JButton("Registrar Entrada");
    JButton btnMostrar = new JButton("Mostrar Movimientos");
    JButton btnLimpiar = new JButton("Limpiar");

    DefaultTableModel modelo = new DefaultTableModel();

    JTable tabla = new JTable(modelo);
    JScrollPane scroll = new JScrollPane(tabla);

    Conexion conexion = new Conexion();

    ArrayList<Producto> productos = new ArrayList<>();

    public FrmInventario() {

        setTitle("Pan, Puntos y Premios - Inventario");
        setSize(950, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);

        lblTitulo.setBounds(370, 20, 250, 30);
        add(lblTitulo);

        lblProducto.setBounds(40, 80, 120, 25);
        cbProducto.setBounds(170, 80, 250, 25);

        lblCantidad.setBounds(40, 120, 120, 25);
        txtCantidad.setBounds(170, 120, 250, 25);

        lblExistencia.setBounds(40, 160, 120, 25);
        txtExistencia.setBounds(170, 160, 250, 25);
        txtExistencia.setEditable(false);

        add(lblProducto);
        add(cbProducto);

        add(lblCantidad);
        add(txtCantidad);

        add(lblExistencia);
        add(txtExistencia);

        btnRegistrar.setBounds(480, 80, 170, 30);
        btnMostrar.setBounds(670, 80, 190, 30);
        btnLimpiar.setBounds(480, 130, 170, 30);

        add(btnRegistrar);
        add(btnMostrar);
        add(btnLimpiar);

        modelo.addColumn("ID Movimiento");
        modelo.addColumn("ID Producto");
        modelo.addColumn("Producto");
        modelo.addColumn("Fecha");
        modelo.addColumn("Tipo");
        modelo.addColumn("Cantidad");

        scroll.setBounds(40, 230, 850, 280);
        add(scroll);

        btnRegistrar.addActionListener(e -> registrarEntrada());

        btnMostrar.addActionListener(e -> mostrarMovimientos());

        btnLimpiar.addActionListener(e -> limpiar());

        cbProducto.addActionListener(e -> mostrarExistencia());

        cargarProductos();
        mostrarMovimientos();
    }

    public void cargarProductos() {

        cbProducto.removeAllItems();

        productos = conexion.mostrarProductos();

        for (Producto producto : productos) {

            if (producto.estado.equals("ACTIVO")) {

                cbProducto.addItem(
                        producto.idProducto + " - " + producto.nombre
                );
            }
        }

        mostrarExistencia();
    }

    public void mostrarExistencia() {

        int posicion = cbProducto.getSelectedIndex();

        if (posicion < 0) {
            txtExistencia.setText("");
            return;
        }

        String seleccionado =
                cbProducto.getSelectedItem().toString();

        int idProducto = Integer.parseInt(
                seleccionado.split(" - ")[0]
        );

        for (Producto producto : productos) {

            if (producto.idProducto == idProducto) {

                txtExistencia.setText(
                        String.valueOf(producto.existencia)
                );

                break;
            }
        }
    }

    public void registrarEntrada() {

        if (cbProducto.getSelectedIndex() < 0) {

            JOptionPane.showMessageDialog(
                    this,
                    "Seleccione un producto"
            );

            return;
        }

        if (txtCantidad.getText().trim().isEmpty()) {

            JOptionPane.showMessageDialog(
                    this,
                    "Ingrese la cantidad"
            );

            return;
        }

        try {

            int cantidad = Integer.parseInt(
                    txtCantidad.getText().trim()
            );

            if (cantidad <= 0) {

                JOptionPane.showMessageDialog(
                        this,
                        "La cantidad debe ser mayor que cero"
                );

                return;
            }

            String seleccionado =
                    cbProducto.getSelectedItem().toString();

            int idProducto = Integer.parseInt(
                    seleccionado.split(" - ")[0]
            );

            conexion.registrarEntradaInventario(
                    idProducto,
                    cantidad
            );

            cargarProductos();
            mostrarMovimientos();
            limpiar();

        } catch (NumberFormatException e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Ingrese una cantidad valida"
            );
        }
    }

    public void mostrarMovimientos() {

        modelo.setRowCount(0);

        ArrayList<MovimientoInventario> movimientos =
                conexion.mostrarMovimientosInventario();

        for (MovimientoInventario movimiento : movimientos) {

            modelo.addRow(new Object[]{
                movimiento.idMovimiento,
                movimiento.idProducto,
                movimiento.nombreProducto,
                movimiento.fecha,
                movimiento.tipo,
                movimiento.cantidad
            });
        }
    }

    public void limpiar() {

        txtCantidad.setText("");

        if (cbProducto.getItemCount() > 0) {
            cbProducto.setSelectedIndex(0);
        }

        mostrarExistencia();
    }
}
