
package panpuntospremios;

public class PanPuntosPremios {

    public static void main(String[] args) {

        Conexion c = new Conexion();
        c.conectar();

        //FrmCliente ventana = new FrmCliente();
        //ventana.setVisible(true);
        
           // FrmProducto ventana = new FrmProducto();
        //ventana.setVisible(true);
        
                /*FrmInventario ventana = new FrmInventario();
        ventana.setVisible(true);*/
        
                 /*FrmMenu ventana = new FrmMenu();
        ventana.setVisible(true);*/
                 
         /*FrmPedido ventana = new FrmPedido();
        ventana.setVisible(true);*/
         
          FrmPrincipal ventana = new FrmPrincipal();
        ventana.setVisible(true);

    }
}
