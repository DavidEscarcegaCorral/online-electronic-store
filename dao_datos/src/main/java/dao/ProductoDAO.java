package dao;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import conexion.ConexionMongoDB;
import entidades.ProductoEntidad;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Implementación del DAO para Productos usando MongoDB.
 */
public class ProductoDAO implements IProductoDAO {
    private final MongoCollection<Document> coleccion;

    public ProductoDAO() {
        this.coleccion = ConexionMongoDB.getInstance()
                .getDatabase()
                .getCollection("productos");
    }

    @Override
    public List<ProductoEntidad> obtenerPorCategoria(String categoria) {
        List<ProductoEntidad> productos = new ArrayList<>();
        try (MongoCursor<Document> cursor = coleccion
                .find(Filters.eq("categoria", categoria))
                .iterator()) {
            while (cursor.hasNext()) {
                productos.add(documentoAEntidad(cursor.next()));
            }
        }
        return productos;
    }

    @Override
    public List<ProductoEntidad> obtenerTodos() {
        List<ProductoEntidad> productos = new ArrayList<>();
        try (MongoCursor<Document> cursor = coleccion.find().iterator()) {
            while (cursor.hasNext()) {
                productos.add(documentoAEntidad(cursor.next()));
            }
        }
        return productos;
    }

    @Override
    public List<ProductoEntidad> obtenerPorCategoriaYMarca(String categoria, String marca) {
        List<ProductoEntidad> productos = new ArrayList<>();
        try (MongoCursor<Document> cursor = coleccion
                .find(Filters.and(
                        Filters.eq("categoria", categoria),
                        Filters.eq("marca", marca),
                        Filters.gt("stock", 0)
                ))
                .iterator()) {
            while (cursor.hasNext()) {
                productos.add(documentoAEntidad(cursor.next()));
            }
        }
        return productos;
    }

    @Override
    public List<String> obtenerMarcasPorCategoria(String categoria) {
        return coleccion.distinct("marca",
                        Filters.eq("categoria", categoria),
                        String.class)
                .into(new ArrayList<>());
    }

    @Override
    public ProductoEntidad obtenerPorId(String id) {
        Document doc = coleccion.find(Filters.eq("_id", new ObjectId(id))).first();
        return doc != null ? documentoAEntidad(doc) : null;
    }

    @Override
    public boolean actualizarStock(String id, int cantidad) {
        return coleccion.updateOne(
                Filters.eq("_id", new ObjectId(id)),
                Updates.inc("stock", -cantidad)
        ).getModifiedCount() > 0;
    }

    private ProductoEntidad documentoAEntidad(Document doc) {
        ProductoEntidad producto = new ProductoEntidad();
        producto.setId(doc.getObjectId("_id"));
        producto.setNombre(doc.getString("nombre"));
        producto.setCategoria(doc.getString("categoria"));
        producto.setMarca(doc.getString("marca"));
        producto.setPrecio(doc.getDouble("precio"));
        producto.setStock(doc.getInteger("stock"));
        producto.setDescripcion(doc.getString("descripcion"));
        producto.setImagenUrl(doc.getString("imagenUrl"));

        Document especsDoc = doc.get("especificaciones", Document.class);
        if (especsDoc != null) {
            Map<String, String> especificaciones = new HashMap<>();
            for (String key : especsDoc.keySet()) {
                especificaciones.put(key, especsDoc.getString(key));
            }
            producto.setEspecificaciones(especificaciones);
        }

        return producto;
    }
}

