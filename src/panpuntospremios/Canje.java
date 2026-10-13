
package panpuntospremios;

public class Canje {

    int idCanje;
    int idCliente;
    int idRecompensa;
    String fecha;
    int puntosUtilizados;

    public Canje(
            int idCanje,
            int idCliente,
            int idRecompensa,
            String fecha,
            int puntosUtilizados) {

        this.idCanje = idCanje;
        this.idCliente = idCliente;
        this.idRecompensa = idRecompensa;
        this.fecha = fecha;
        this.puntosUtilizados = puntosUtilizados;
    }
}
