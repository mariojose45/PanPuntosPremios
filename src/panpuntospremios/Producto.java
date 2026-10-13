
package panpuntospremios;
//Nuestra clase Producto representa los productos que venderemos en el sistema.
public class Producto {

    int idProducto;
    String codigo;
    String nombre;
    String categoria;
    double precio;
    int existencia;
    String estado;

    public Producto(
            int idProducto,
            String codigo,
            String nombre,
            String categoria,
            double precio,
            int existencia,
            String estado) {

        this.idProducto = idProducto;
        this.codigo = codigo;
        this.nombre = nombre;
        this.categoria = categoria;
        this.precio = precio;
        this.existencia = existencia;
        this.estado = estado;
    }
}
