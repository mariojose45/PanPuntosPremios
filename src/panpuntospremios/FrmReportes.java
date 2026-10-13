
package panpuntospremios;

import java.util.ArrayList;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JComboBox;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JTable;
import javax.swing.JScrollPane;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

public class FrmReportes extends JFrame {

    JLabel lblTitulo = new JLabel("REPORTES GENERALES");
    JLabel lblReporte = new JLabel("Tipo de reporte:");
    JLabel lblFechaInicio = new JLabel("Fecha inicio:");
    JLabel lblFechaFin = new JLabel("Fecha fin:");
    JLabel lblTotal = new JLabel("Registros encontrados: 0");

    JComboBox<String> cbReporte = new JComboBox<>(
            new String[]{
                "Ventas por Fecha",
                "Productos Mas Vendidos",
                "Inventario Bajo",
                "Puntos por Cliente",
                "Historial de Canjes",
                "Ventas de Menus"
            }
    );

    JTextField txtFechaInicio = new JTextField();
    JTextField txtFechaFin = new JTextField();

    JButton btnConsultar = new JButton("Consultar");
    JButton btnLimpiar = new JButton("Limpiar");

    DefaultTableModel modelo = new DefaultTableModel() {

        @Override
        public boolean isCellEditable(int fila, int columna) {
            return false;
        }
    };

    JTable tabla = new JTable(modelo);
    JScrollPane scroll = new JScrollPane(tabla);

    Conexion conexion = new Conexion();

    public FrmReportes() {

        setTitle("Pan, Puntos y Premios - Reportes");
        setSize(1100, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(null);

        lblTitulo.setBounds(450, 20, 250, 30);
        add(lblTitulo);

        lblReporte.setBounds(40, 80, 130, 25);
        cbReporte.setBounds(170, 80, 280, 25);

        lblFechaInicio.setBounds(480, 80, 100, 25);
        txtFechaInicio.setBounds(580, 80, 130, 25);

        lblFechaFin.setBounds(730, 80, 100, 25);
        txtFechaFin.setBounds(830, 80, 130, 25);

        btnConsultar.setBounds(170, 130, 150, 35);
        btnLimpiar.setBounds(340, 130, 150, 35);

        add(lblReporte);
        add(cbReporte);
        add(lblFechaInicio);
        add(txtFechaInicio);
        add(lblFechaFin);
        add(txtFechaFin);
        add(btnConsultar);
        add(btnLimpiar);

        scroll.setBounds(40, 195, 1000, 390);
        add(scroll);

        lblTotal.setBounds(40, 600, 400, 25);
        add(lblTotal);

        txtFechaInicio.setText(
                java.time.LocalDate.now().withDayOfMonth(1).toString()
        );

        txtFechaFin.setText(
                java.time.LocalDate.now().toString()
        );

        cbReporte.addActionListener(e -> cambiarReporte());

        btnConsultar.addActionListener(e -> consultar());
        btnLimpiar.addActionListener(e -> limpiar());

        cambiarReporte();
    }

    public void cambiarReporte() {

        boolean usaFechas = cbReporte.getSelectedIndex() == 0;

        txtFechaInicio.setEnabled(usaFechas);
        txtFechaFin.setEnabled(usaFechas);

        lblFechaInicio.setEnabled(usaFechas);
        lblFechaFin.setEnabled(usaFechas);

        modelo.setRowCount(0);
        modelo.setColumnCount(0);

        lblTotal.setText("Registros encontrados: 0");
    }

    public void consultar() {

        int tipo = cbReporte.getSelectedIndex() + 1;

        if (tipo == 1) {

            try {

                java.time.LocalDate inicio =
                        java.time.LocalDate.parse(
                                txtFechaInicio.getText().trim()
                        );

                java.time.LocalDate fin =
                        java.time.LocalDate.parse(
                                txtFechaFin.getText().trim()
                        );

                if (inicio.isAfter(fin)) {

                    JOptionPane.showMessageDialog(
                            this,
                            "La fecha inicial no puede ser mayor que la final"
                    );

                    return;
                }

            } catch (java.time.format.DateTimeParseException e) {

                JOptionPane.showMessageDialog(
                        this,
                        "Ingrese fechas con formato YYYY-MM-DD"
                );

                return;
            }
        }

        String[] columnas = obtenerColumnas(tipo);

        modelo.setColumnIdentifiers(columnas);
        modelo.setRowCount(0);

        ArrayList<Object[]> datos = conexion.mostrarReporte(
                tipo,
                txtFechaInicio.getText().trim(),
                txtFechaFin.getText().trim()
        );

        for (Object[] fila : datos) {
            modelo.addRow(fila);
        }

        lblTotal.setText(
                "Registros encontrados: " + datos.size()
        );

        if (datos.isEmpty()) {

            JOptionPane.showMessageDialog(
                    this,
                    "No se encontraron registros para este reporte"
            );
        }
    }

    public String[] obtenerColumnas(int tipo) {

        switch (tipo) {

            case 1:
                return new String[]{
                    "ID Pedido", "Cliente", "Fecha",
                    "Forma de Pago", "Total"
                };

            case 2:
                return new String[]{
                    "ID Producto", "Producto", "Cantidad Vendida"
                };

            case 3:
                return new String[]{
                    "ID Producto", "Codigo", "Producto",
                    "Categoria", "Existencia"
                };

            case 4:
                return new String[]{
                    "ID Cliente", "Identificador", "Cliente",
                    "Puntos", "Estado"
                };

            case 5:
                return new String[]{
                    "ID Canje", "Cliente", "Recompensa",
                    "Fecha", "Puntos Utilizados"
                };

            case 6:
                return new String[]{
                    "ID Menu", "Menu", "Cantidad Vendida", "Total"
                };

            default:
                return new String[]{};
        }
    }

    public void limpiar() {

        modelo.setRowCount(0);
        modelo.setColumnCount(0);

        cbReporte.setSelectedIndex(0);

        txtFechaInicio.setText(
                java.time.LocalDate.now().withDayOfMonth(1).toString()
        );

        txtFechaFin.setText(
                java.time.LocalDate.now().toString()
        );

        lblTotal.setText("Registros encontrados: 0");
    }
}
