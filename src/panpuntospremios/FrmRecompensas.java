
package panpuntospremios;

import java.util.ArrayList;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JComboBox;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.JScrollPane;
import javax.swing.table.DefaultTableModel;

public class FrmRecompensas extends JFrame {

    JLabel lblTitulo = new JLabel("PUNTOS Y RECOMPENSAS");
    JLabel lblCliente = new JLabel("Cliente:");
    JLabel lblPuntos = new JLabel("Puntos disponibles: 0");
    JLabel lblRecompensa = new JLabel("Recompensa:");

    JComboBox<String> cbCliente = new JComboBox<>();
    JComboBox<String> cbRecompensa = new JComboBox<>();

    JButton btnCanjear = new JButton("Canjear Recompensa");
    JButton btnActualizar = new JButton("Actualizar");
    JButton btnLimpiar = new JButton("Limpiar");

    DefaultTableModel modeloPuntos = new DefaultTableModel(
            new Object[]{"ID", "Fecha", "Tipo", "Puntos"}, 0
    ) {
        @Override
        public boolean isCellEditable(int fila, int columna) {
            return false;
        }
    };

    DefaultTableModel modeloCanjes = new DefaultTableModel(
            new Object[]{"ID Canje", "ID Recompensa", "Fecha", "Puntos"}, 0
    ) {
        @Override
        public boolean isCellEditable(int fila, int columna) {
            return false;
        }
    };

    JTable tablaPuntos = new JTable(modeloPuntos);
    JTable tablaCanjes = new JTable(modeloCanjes);

    JScrollPane scrollPuntos = new JScrollPane(tablaPuntos);
    JScrollPane scrollCanjes = new JScrollPane(tablaCanjes);

    Conexion conexion = new Conexion();

    ArrayList<Cliente> clientes = new ArrayList<>();
    ArrayList<Recompensa> recompensas = new ArrayList<>();

    public FrmRecompensas() {

        setTitle("Pan, Puntos y Premios - Recompensas");
        setSize(1000, 740);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(null);

        lblTitulo.setBounds(390, 20, 250, 30);
        add(lblTitulo);

        lblCliente.setBounds(40, 80, 120, 25);
        cbCliente.setBounds(180, 80, 330, 25);

        lblPuntos.setBounds(550, 80, 300, 25);

        lblRecompensa.setBounds(40, 125, 120, 25);
        cbRecompensa.setBounds(180, 125, 330, 25);

        btnCanjear.setBounds(550, 125, 200, 35);
        btnActualizar.setBounds(770, 125, 160, 35);
        btnLimpiar.setBounds(770, 175, 160, 35);

        add(lblCliente);
        add(cbCliente);
        add(lblPuntos);
        add(lblRecompensa);
        add(cbRecompensa);
        add(btnCanjear);
        add(btnActualizar);
        add(btnLimpiar);

        JLabel lblHistorialPuntos = new JLabel("HISTORIAL DE PUNTOS");
        lblHistorialPuntos.setBounds(40, 240, 250, 25);
        add(lblHistorialPuntos);

        scrollPuntos.setBounds(40, 270, 900, 160);
        add(scrollPuntos);

        JLabel lblHistorialCanjes = new JLabel("HISTORIAL DE CANJES");
        lblHistorialCanjes.setBounds(40, 460, 250, 25);
        add(lblHistorialCanjes);

        scrollCanjes.setBounds(40, 490, 900, 160);
        add(scrollCanjes);

        cbCliente.addActionListener(e -> actualizarCliente());

        btnCanjear.addActionListener(e -> canjear());
        btnActualizar.addActionListener(e -> cargarDatos());
        btnLimpiar.addActionListener(e -> limpiar());

        cargarDatos();
    }

    public int obtenerId(String texto) {

        return Integer.parseInt(
                texto.split(" - ")[0]
        );
    }

    public void cargarDatos() {

        cbCliente.removeAllItems();
        cbRecompensa.removeAllItems();

        clientes = conexion.mostrarClientes();
        recompensas = conexion.mostrarRecompensas();

        for (Cliente cliente : clientes) {

            if ("ACTIVO".equals(cliente.estado)) {

                cbCliente.addItem(
                        cliente.idCliente + " - " + cliente.nombre
                );
            }
        }

        for (Recompensa recompensa : recompensas) {

            if ("ACTIVO".equals(recompensa.estado)) {

                cbRecompensa.addItem(
                        recompensa.idRecompensa + " - "
                        + recompensa.nombre + " ("
                        + recompensa.puntosNecesarios + " puntos)"
                );
            }
        }

        actualizarCliente();
    }

    public void actualizarCliente() {

        modeloPuntos.setRowCount(0);
        modeloCanjes.setRowCount(0);

        if (cbCliente.getSelectedItem() == null) {
            lblPuntos.setText("Puntos disponibles: 0");
            return;
        }

        int idCliente = obtenerId(
                cbCliente.getSelectedItem().toString()
        );

        for (Cliente cliente : clientes) {

            if (cliente.idCliente == idCliente) {

                lblPuntos.setText(
                        "Puntos disponibles: " + cliente.puntos
                );

                break;
            }
        }

        ArrayList<MovimientoPuntos> movimientos =
                conexion.mostrarMovimientosPuntos(idCliente);

        for (MovimientoPuntos movimiento : movimientos) {

            modeloPuntos.addRow(new Object[]{
                movimiento.idMovimiento,
                movimiento.fecha,
                movimiento.tipo,
                movimiento.puntos
            });
        }

        ArrayList<Canje> canjes =
                conexion.mostrarCanjes(idCliente);

        for (Canje canje : canjes) {

            modeloCanjes.addRow(new Object[]{
                canje.idCanje,
                canje.idRecompensa,
                canje.fecha,
                canje.puntosUtilizados
            });
        }
    }

    public void canjear() {

        if (cbCliente.getSelectedItem() == null
                || cbRecompensa.getSelectedItem() == null) {

            JOptionPane.showMessageDialog(
                    this,
                    "Seleccione un cliente y una recompensa"
            );

            return;
        }

        int idCliente = obtenerId(
                cbCliente.getSelectedItem().toString()
        );

        int idRecompensa = obtenerId(
                cbRecompensa.getSelectedItem().toString()
        );

        int confirmar = JOptionPane.showConfirmDialog(
                this,
                "Desea canjear esta recompensa?",
                "Confirmar Canje",
                JOptionPane.YES_NO_OPTION
        );

        if (confirmar != JOptionPane.YES_OPTION) {
            return;
        }

        boolean realizado = conexion.canjearRecompensa(
                idCliente,
                idRecompensa
        );

        if (realizado) {

            JOptionPane.showMessageDialog(
                    this,
                    "Recompensa canjeada correctamente"
            );

            cargarDatos();

        } else {

            JOptionPane.showMessageDialog(
                    this,
                    "No se pudo realizar el canje. Revise la consola."
            );
        }
    }

    public void limpiar() {

        if (cbCliente.getItemCount() > 0) {
            cbCliente.setSelectedIndex(0);
        }

        if (cbRecompensa.getItemCount() > 0) {
            cbRecompensa.setSelectedIndex(0);
        }

        actualizarCliente();
    }
}
