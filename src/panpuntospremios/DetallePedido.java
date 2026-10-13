
package panpuntospremios;

public class DetallePedido {

    int idDetalle;
    int idPedido;
    int idProducto;
    int idMenu;
    String descripcion;
    int cantidad;
    double precio;
    double subtotal;

    public DetallePedido(
            int idDetalle,
            int idPedido,
            int idProducto,
            int idMenu,
            String descripcion,
            int cantidad,
            double precio,
            double subtotal) {

        this.idDetalle = idDetalle;
        this.idPedido = idPedido;
        this.idProducto = idProducto;
        this.idMenu = idMenu;
        this.descripcion = descripcion;
        this.cantidad = cantidad;
        this.precio = precio;
        this.subtotal = subtotal;
    }
}
