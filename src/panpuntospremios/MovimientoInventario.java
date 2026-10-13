
package panpuntospremios;

public class MovimientoInventario {

    int idMovimiento;
    int idProducto;
    String nombreProducto;
    String fecha;
    String tipo;
    int cantidad;

    public MovimientoInventario(
            int idMovimiento,
            int idProducto,
            String nombreProducto,
            String fecha,
            String tipo,
            int cantidad) {

        this.idMovimiento = idMovimiento;
        this.idProducto = idProducto;
        this.nombreProducto = nombreProducto;
        this.fecha = fecha;
        this.tipo = tipo;
        this.cantidad = cantidad;
    }
}
