
package panpuntospremios;

public class MovimientoPuntos {

    int idMovimiento;
    int idCliente;
    String fecha;
    String tipo;
    int puntos;

    public MovimientoPuntos(
            int idMovimiento,
            int idCliente,
            String fecha,
            String tipo,
            int puntos) {

        this.idMovimiento = idMovimiento;
        this.idCliente = idCliente;
        this.fecha = fecha;
        this.tipo = tipo;
        this.puntos = puntos;
    }
}
