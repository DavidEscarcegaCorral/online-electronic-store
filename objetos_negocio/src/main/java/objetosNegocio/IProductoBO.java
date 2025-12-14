package objetosNegocio;

import java.math.BigDecimal;
import java.util.Map;

/**
 * Interfaz para el Business Object de Producto.
 * Define los métodos públicos para la lógica de negocio de productos.
 */
public interface IProductoBO {

    String getId();

    void setId(String id);

    String getNombre();

    void setNombre(String nombre);

    String getCategoria();

    void setCategoria(String categoria);

    String getMarca();

    void setMarca(String marca);

    BigDecimal getPrecio();

    void setPrecio(BigDecimal precio);

    Integer getStock();

    void setStock(Integer stock);

    Map<String, String> getEspecificaciones();

    void setEspecificaciones(Map<String, String> especificaciones);

    String getDescripcion();

    void setDescripcion(String descripcion);

    String getImagenUrl();

    void setImagenUrl(String imagenUrl);

    boolean tieneStock();

    boolean tieneStockSuficiente(int cantidad);

    void reducirStock(int cantidad);
}

