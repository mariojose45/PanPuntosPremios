
package panpuntospremios;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.JScrollPane;
import javax.swing.table.DefaultTableModel;
import java.util.ArrayList;

public class FrmCliente extends JFrame {

    JLabel lblTitulo = new JLabel("REGISTRO DE CLIENTES");

    JLabel lblIdentificador = new JLabel("Identificador:");
    JTextField txtIdentificador = new JTextField();

    JLabel lblNombre = new JLabel("Nombre:");
    JTextField txtNombre = new JTextField();

    JLabel lblTelefono = new JLabel("Telefono:");
    JTextField txtTelefono = new JTextField();

    JLabel lblPuntos = new JLabel("Puntos:");
    JTextField txtPuntos = new JTextField("0");

    JButton btnGuardar = new JButton("Guardar");
    JButton btnActualizar = new JButton("Actualizar");
    JButton btnMostrar = new JButton("Mostrar");
    JButton btnLimpiar = new JButton("Limpiar");

    JTable tablaClientes = new JTable();
    JScrollPane scroll = new JScrollPane(tablaClientes);

    DefaultTableModel modelo = new DefaultTableModel();

    Conexion conexion = new Conexion();

    int idCliente = 0;

    public FrmCliente() {

        setTitle("Pan, Puntos y Premios - Clientes");
        setSize(850, 550);
        setLayout(null);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        lblTitulo.setBounds(300, 20, 250, 30);
        add(lblTitulo);

        lblIdentificador.setBounds(30, 70, 100, 25);
        txtIdentificador.setBounds(140, 70, 200, 25);

        add(lblIdentificador);
        add(txtIdentificador);

        lblNombre.setBounds(30, 110, 100, 25);
        txtNombre.setBounds(140, 110, 200, 25);

        add(lblNombre);
        add(txtNombre);

        lblTelefono.setBounds(30, 150, 100, 25);
        txtTelefono.setBounds(140, 150, 200, 25);

        add(lblTelefono);
        add(txtTelefono);

        lblPuntos.setBounds(30, 190, 100, 25);
        txtPuntos.setBounds(140, 190, 200, 25);
        txtPuntos.setEditable(false);

        add(lblPuntos);
        add(txtPuntos);

        btnGuardar.setBounds(400, 70, 120, 30);
        btnActualizar.setBounds(540, 70, 120, 30);
        btnMostrar.setBounds(400, 120, 120, 30);
        btnLimpiar.setBounds(540, 120, 120, 30);

        add(btnGuardar);
        add(btnActualizar);
        add(btnMostrar);
        add(btnLimpiar);

        modelo.addColumn("ID");
        modelo.addColumn("Identificador");
        modelo.addColumn("Nombre");
        modelo.addColumn("Telefono");
        modelo.addColumn("Puntos");
        modelo.addColumn("Estado");

        tablaClientes.setModel(modelo);

        scroll.setBounds(30, 250, 780, 220);
        add(scroll);

        btnMostrar.addActionListener(e -> mostrarClientes());

        btnGuardar.addActionListener(e -> guardarCliente());

        btnActualizar.addActionListener(e -> actualizarCliente());

        btnLimpiar.addActionListener(e -> limpiar());

        tablaClientes.getSelectionModel().addListSelectionListener(e -> {

            if (!e.getValueIsAdjusting()) {

                int fila = tablaClientes.getSelectedRow();

                if (fila >= 0) {

                    idCliente = Integer.parseInt(
                            tablaClientes.getValueAt(fila, 0).toString()
                    );

                    txtIdentificador.setText(
                            tablaClientes.getValueAt(fila, 1).toString()
                    );

                    txtNombre.setText(
                            tablaClientes.getValueAt(fila, 2).toString()
                    );

                    txtTelefono.setText(
                            tablaClientes.getValueAt(fila, 3).toString()
                    );

                    txtPuntos.setText(
                            tablaClientes.getValueAt(fila, 4).toString()
                    );
                }
            }
        });

        mostrarClientes();
    }

    public void mostrarClientes() {

        modelo.setRowCount(0);

        ArrayList<Cliente> clientes = conexion.mostrarClientes();

        for (Cliente cliente : clientes) {

            modelo.addRow(new Object[]{
                cliente.idCliente,
                cliente.identificador,
                cliente.nombre,
                cliente.telefono,
                cliente.puntos,
                cliente.estado
            });
        }
    }

    public void guardarCliente() {

        String identificador = txtIdentificador.getText().trim();
        String nombre = txtNombre.getText().trim();
        String telefono = txtTelefono.getText().trim();

        if (identificador.isEmpty() || nombre.isEmpty()) {

            JOptionPane.showMessageDialog(
                    this,
                    "Ingrese identificador y nombre"
            );

            return;
        }

        conexion.insertarCliente(
                identificador,
                nombre,
                telefono
        );

        mostrarClientes();
        limpiar();
    }

    public void actualizarCliente() {

        if (idCliente == 0) {

            JOptionPane.showMessageDialog(
                    this,
                    "Seleccione un cliente"
            );

            return;
        }

        conexion.actualizarCliente(
                idCliente,
                txtIdentificador.getText().trim(),
                txtNombre.getText().trim(),
                txtTelefono.getText().trim()
        );

        mostrarClientes();
        limpiar();
    }

    public void limpiar() {

        idCliente = 0;

        txtIdentificador.setText("");
        txtNombre.setText("");
        txtTelefono.setText("");
        txtPuntos.setText("0");

        tablaClientes.clearSelection();
    }
}
