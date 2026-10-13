
package panpuntospremios;

public class Menu {

    int idMenu;
    String nombre;
    double precio;
    String estado;

    public Menu(
            int idMenu,
            String nombre,
            double precio,
            String estado) {

        this.idMenu = idMenu;
        this.nombre = nombre;
        this.precio = precio;
        this.estado = estado;
    }
}
