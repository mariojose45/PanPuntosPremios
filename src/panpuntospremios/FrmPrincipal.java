
package panpuntospremios;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JButton;

public class FrmPrincipal extends JFrame {

    JLabel lblTitulo = new JLabel("PAN, PUNTOS Y PREMIOS");
    JLabel lblSubtitulo = new JLabel("MENU PRINCIPAL DEL SISTEMA");

    JButton btnClientes = new JButton("CLIENTES");
    JButton btnProductos = new JButton("PRODUCTOS");
    JButton btnInventario = new JButton("INVENTARIO");
    JButton btnMenus = new JButton("MENUS");
    JButton btnPedidos = new JButton("PEDIDOS Y PAGOS");
    JButton btnRecompensas = new JButton("PUNTOS Y RECOMPENSAS");
    JButton btnReportes = new JButton("REPORTES");
    JButton btnSalir = new JButton("SALIR");

    public FrmPrincipal() {

        setTitle("Pan, Puntos y Premios - Menu Principal");
        setSize(650, 650);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);
        setResizable(false);

        lblTitulo.setBounds(230, 30, 250, 30);
        lblSubtitulo.setBounds(215, 65, 300, 30);

        add(lblTitulo);
        add(lblSubtitulo);

        btnClientes.setBounds(80, 130, 220, 60);
        btnProductos.setBounds(340, 130, 220, 60);

        btnInventario.setBounds(80, 210, 220, 60);
        btnMenus.setBounds(340, 210, 220, 60);

        btnPedidos.setBounds(80, 290, 480, 60);
        btnRecompensas.setBounds(80, 365, 480, 45);
        btnReportes.setBounds(80, 425, 480, 45);
        btnSalir.setBounds(220, 495, 200, 45);

        add(btnClientes);
        add(btnProductos);
        add(btnInventario);
        add(btnMenus);
        add(btnPedidos);
        add(btnRecompensas);
        add(btnReportes);
        add(btnSalir);

        btnClientes.addActionListener(e -> {

            FrmCliente ventana = new FrmCliente();
            ventana.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            ventana.setVisible(true);
        });

        btnProductos.addActionListener(e -> {

            FrmProducto ventana = new FrmProducto();
            ventana.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            ventana.setVisible(true);
        });

        btnInventario.addActionListener(e -> {

            FrmInventario ventana = new FrmInventario();
            ventana.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            ventana.setVisible(true);
        });

        btnMenus.addActionListener(e -> {

            FrmMenu ventana = new FrmMenu();
            ventana.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            ventana.setVisible(true);
        });

        btnPedidos.addActionListener(e -> {

            FrmPedido ventana = new FrmPedido();
            ventana.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            ventana.setVisible(true);
        });

        btnRecompensas.addActionListener(e -> {

            FrmRecompensas ventana = new FrmRecompensas();
            ventana.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            ventana.setVisible(true);
        });

        btnReportes.addActionListener(e -> {

            FrmReportes ventana = new FrmReportes();
            ventana.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            ventana.setVisible(true);
        });

        btnSalir.addActionListener(e -> {

            System.exit(0);
        });
    }
}
