package dao;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import conexion.ConexionMongoDB;
import entidades.CarritoEntidad;
import entidades.UsuarioEntidad;
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
            // Asegurar que exista un usuario por defecto
            UsuarioDAO usuarioDAO = new UsuarioDAO();
            UsuarioEntidad usuario = usuarioDAO.obtenerPorEmail("cliente_default@local");
            if (usuario == null) {
                usuario = new UsuarioEntidad();
                usuario.setNombre("Cliente Default");
                usuario.setEmail("cliente_default@local");
                usuarioDAO.guardar(usuario);
            }

            CarritoDAO dao = new CarritoDAO();
            // Buscar carrito por clienteId (usamos el id del usuario como string)
            String clienteId = usuario.getId() != null ? usuario.getId().toString() : "cliente_default";
            carritoActual = dao.obtenerPorClienteId(clienteId);

            if (carritoActual == null) {
                // Si existe un carrito con el antiguo identificador "cliente_default", migrarlo
                CarritoEntidad antiguo = dao.obtenerPorClienteId("cliente_default");
                if (antiguo != null) {
                    antiguo.setClienteId(clienteId);
                    dao.guardar(antiguo);
                    carritoActual = antiguo;
                } else {
                    carritoActual = new CarritoEntidad();
                    carritoActual.setClienteId(clienteId);
                    // Persistir nuevo carrito
                    dao.guardar(carritoActual);
                }
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
