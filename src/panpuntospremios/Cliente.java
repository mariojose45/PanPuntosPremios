
package panpuntospremios;

public class Cliente {

    int idCliente;
    String identificador;
    String nombre;
    String telefono;
    int puntos;
    String estado;

    public Cliente(
            int idCliente,
            String identificador,
            String nombre,
            String telefono,
            int puntos,
            String estado) {

        this.idCliente = idCliente;
        this.identificador = identificador;
        this.nombre = nombre;
        this.telefono = telefono;
        this.puntos = puntos;
        this.estado = estado;
    }
}
