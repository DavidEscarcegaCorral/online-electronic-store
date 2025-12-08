package dao;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import conexion.ConexionMongoDB;
import entidades.CarritoEntidad;
import org.bson.Document;
import org.bson.types.ObjectId;
import java.util.ArrayList;
import java.util.List;

public class CarritoDAO {
    private final MongoCollection<Document> coleccion;
    private static CarritoEntidad carritoActual;

    public CarritoDAO() {
        this.coleccion = ConexionMongoDB.getInstance()
                .getDatabase()
                .getCollection("carritos");
    }

    public static CarritoEntidad getCarritoActual() {
        if (carritoActual == null) {
            CarritoDAO dao = new CarritoDAO();
            carritoActual = dao.obtenerPorClienteId("cliente_default");

            if (carritoActual == null) {
                carritoActual = new CarritoEntidad();
                carritoActual.setClienteId("cliente_default");
            }
        }
        return carritoActual;
    }

    public void guardar(CarritoEntidad carrito) {
        Document doc = new Document();

        if (carrito.getId() != null) {
            doc.append("_id", carrito.getId());
        }

        doc.append("clienteId", carrito.getClienteId())
           .append("fechaActualizacion", carrito.getFechaActualizacion());

        if (carrito.getConfiguracionesIds() != null) {
            List<ObjectId> ids = new ArrayList<>(carrito.getConfiguracionesIds());
            doc.append("configuracionesIds", ids);
        }

        if (carrito.getId() == null) {
            coleccion.insertOne(doc);
            carrito.setId(doc.getObjectId("_id"));
        } else {
            coleccion.replaceOne(
                new Document("_id", carrito.getId()),
                doc
            );
        }
    }

    public CarritoEntidad obtenerPorClienteId(String clienteId) {
        Document doc = coleccion.find(Filters.eq("clienteId", clienteId)).first();
        if (doc == null) return null;

        CarritoEntidad carrito = new CarritoEntidad();
        carrito.setId(doc.getObjectId("_id"));
        carrito.setClienteId(doc.getString("clienteId"));

        List<ObjectId> ids = (List<ObjectId>) doc.get("configuracionesIds");
        if (ids != null) {
            carrito.setConfiguracionesIds(ids);
        }

        return carrito;
    }
}

