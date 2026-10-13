
package panpuntospremios;

public class DetalleMenu {

    int idDetalleMenu;
    int idMenu;
    int idProducto;
    String nombreProducto;
    int cantidad;

    public DetalleMenu(
            int idDetalleMenu,
            int idMenu,
            int idProducto,
            String nombreProducto,
            int cantidad) {

        this.idDetalleMenu = idDetalleMenu;
        this.idMenu = idMenu;
        this.idProducto = idProducto;
        this.nombreProducto = nombreProducto;
        this.cantidad = cantidad;
    }
}
