package dao;

import entidades.ProductoEntidad;

import java.util.List;

/**
 * Interfaz para operaciones de acceso a datos de Productos.
 */
public interface IProductoDAO {
    List<ProductoEntidad> obtenerTodos();

    List<ProductoEntidad> obtenerPorCategoria(String categoria);

    List<ProductoEntidad> obtenerPorCategoriaYMarca(String categoria, String marca);

    List<String> obtenerMarcasPorCategoria(String categoria);

    ProductoEntidad obtenerPorId(String id);

    boolean actualizarStock(String id, int cantidad);

    /**
     * Cuenta la cantidad de productos disponibles (stock > 0) para una categoría.
     *
     * @param categoria la categoría a consultar
     * @return número de productos con stock > 0 en esa categoría
     */
    long contarDisponiblesPorCategoria(String categoria);
}
