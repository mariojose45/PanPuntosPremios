
package panpuntospremios;

public class Pedido {

    int idPedido;
    int idCliente;
    String nombreCliente;
    String fecha;
    double total;
    String estado;

    public Pedido(
            int idPedido,
            int idCliente,
            String nombreCliente,
            String fecha,
            double total,
            String estado) {

        this.idPedido = idPedido;
        this.idCliente = idCliente;
        this.nombreCliente = nombreCliente;
        this.fecha = fecha;
        this.total = total;
        this.estado = estado;
    }
}
