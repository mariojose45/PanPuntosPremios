
package panpuntospremios;

public class Recompensa {

    int idRecompensa;
    String nombre;
    int puntosNecesarios;
    int idProducto;
    int idMenu;
    String estado;

    public Recompensa(
            int idRecompensa,
            String nombre,
            int puntosNecesarios,
            int idProducto,
            int idMenu,
            String estado) {

        this.idRecompensa = idRecompensa;
        this.nombre = nombre;
        this.puntosNecesarios = puntosNecesarios;
        this.idProducto = idProducto;
        this.idMenu = idMenu;
        this.estado = estado;
    }
}
