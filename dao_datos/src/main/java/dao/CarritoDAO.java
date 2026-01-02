package dao;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import conexion.ConexionMongoDB;
import entidades.CarritoEntidad;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

import static org.slf4j.LoggerFactory.getLogger;

public class CarritoDAO {

    /** Logger para registrar operaciones del DAO */
    private static final Logger logger = getLogger(CarritoDAO.class);

    /** Colección de MongoDB donde se almacenan los carritos */
    private final MongoCollection<Document> coleccion;

    public CarritoDAO() {
        this.coleccion = ConexionMongoDB.getInstance()
                .getDatabase()
                .getCollection("carritos");
    }

    public synchronized CarritoEntidad obtenerCarrito(String clienteId) {
        if (clienteId == null || clienteId.trim().isEmpty()) {
            logger.error("El clienteId no puede ser null o vacío");
            throw new IllegalArgumentException("El clienteId es requerido");
        }

        logger.debug("Buscando carrito para clienteId: {}", clienteId);

        CarritoEntidad carrito = obtenerPorClienteId(clienteId);

        if (carrito == null) {
            logger.info("Carrito no encontrado para clienteId: {}. Creando carrito nuevo.", clienteId);
            carrito = new CarritoEntidad();
            carrito.setClienteId(clienteId);
            try {
                guardar(carrito);
                logger.info("Carrito creado exitosamente. ID: {}", carrito.getId());
            } catch (com.mongodb.MongoWriteException e) {
                if (e.getError().getCategory().equals(com.mongodb.ErrorCategory.DUPLICATE_KEY)) {
                    logger.info("Carrito duplicado detectado (race condition prevenida). Recuperando carrito existente...");
                    carrito = obtenerPorClienteId(clienteId);
                    if (carrito != null) {
                        logger.info("Carrito recuperado. ID: {}", carrito.getId());
                    }
                } else {
                    throw e;
                }
            }
        } else {
            logger.debug("Carrito encontrado. ID: {}", carrito.getId());
        }

        return carrito;
    }

    public void guardar(CarritoEntidad carrito) {
        logger.debug("Preparando guardar carrito. ID: {}", carrito.getId());
        logger.debug("ClienteId: {}", carrito.getClienteId());
        logger.debug("Configuraciones en carrito: {}",
                (carrito.getConfiguracionesIds() != null ? carrito.getConfiguracionesIds().size() : 0));

        if (carrito.getConfiguracionesIds() != null && !carrito.getConfiguracionesIds().isEmpty()) {
            logger.debug("IDs de configuraciones a guardar: {}", carrito.getConfiguracionesIds().size());
        }

        Date fechaActualizacion = null;
        if (carrito.getFechaActualizacion() != null) {
            fechaActualizacion = Date.from(
                    carrito.getFechaActualizacion()
                            .atZone(ZoneId.systemDefault())
                            .toInstant()
            );
        } else {
            fechaActualizacion = new Date();
        }

        // preparar lista de IDs de configuraciones
        List<ObjectId> ids = carrito.getConfiguracionesIds() != null
                ? new ArrayList<>(carrito.getConfiguracionesIds())
                : new ArrayList<>();

        List<Document> productos = new ArrayList<>();
        if (carrito.getProductos() != null) {
            for (java.util.Map<String, Object> prod : carrito.getProductos()) {
                Document prodDoc = new Document();
                prodDoc.append("productoId", prod.get("productoId"));
                prodDoc.append("nombre", prod.get("nombre"));
                prodDoc.append("precio", prod.get("precio"));
                prodDoc.append("cantidad", prod.get("cantidad"));
                productos.add(prodDoc);
            }
        }

        // nuevo carrito
        if (carrito.getId() == null) {
            Document doc = new Document();
            doc.append("clienteId", carrito.getClienteId())
                    .append("fechaActualizacion", fechaActualizacion);

            if (!ids.isEmpty()) {
                doc.append("configuracionesIds", ids);
            }
            if (!productos.isEmpty()) {
                doc.append("productos", productos);
            }

            logger.info("Insertando nuevo carrito para clienteId: {}", carrito.getClienteId());
            coleccion.insertOne(doc);

            carrito.setId(doc.getObjectId("_id"));
            logger.info("Carrito insertado exitosamente. ID: {}", carrito.getId());
        } else {
            Document setDoc = new Document()
                    .append("clienteId", carrito.getClienteId())
                    .append("fechaActualizacion", fechaActualizacion)
                    .append("configuracionesIds", ids)
                    .append("productos", productos);

            Document updateDoc = new Document("$set", setDoc);

            logger.info("Actualizando carrito existente. ID: {}", carrito.getId());

            var result = coleccion.updateOne(
                    new Document("_id", carrito.getId()),
                    updateDoc
            );

            logger.debug("Documentos modificados: {}", result.getModifiedCount());
            logger.debug("Documentos coincidentes: {}", result.getMatchedCount());

            if (result.getMatchedCount() == 0) {
                logger.warn("No se encontró el carrito en BD para actualizar. ID: {}", carrito.getId());
                logger.info("Intentando con replaceOne como fallback...");

                Document docReplace = new Document();
                docReplace.append("clienteId", carrito.getClienteId())
                        .append("fechaActualizacion", fechaActualizacion)
                        .append("configuracionesIds", ids)
                        .append("productos", productos);

                var resultReplace = coleccion.replaceOne(
                        new Document("_id", carrito.getId()),
                        docReplace
                );
                logger.debug("ReplaceOne - Documentos modificados: {}", resultReplace.getModifiedCount());
            } else {
                logger.info("Carrito actualizado exitosamente. ID: {}", carrito.getId());
            }
        }
    }

    public CarritoEntidad obtenerPorClienteId(String clienteId) {
        Document doc = coleccion.find(Filters.eq("clienteId", clienteId)).first();

        if (doc == null) {
            return null;
        }

        CarritoEntidad carrito = new CarritoEntidad();
        carrito.setId(doc.getObjectId("_id"));
        carrito.setClienteId(doc.getString("clienteId"));

        List<ObjectId> ids = (List<ObjectId>) doc.get("configuracionesIds");
        if (ids != null) {
            carrito.setConfiguracionesIds(ids);
        }

        List<Document> productosDoc = (List<Document>) doc.get("productos");
        if (productosDoc != null) {
            List<Map<String, Object>> productos = new ArrayList<>();
            for (Document prodDoc : productosDoc) {
                Map<String, Object> prod = new HashMap<>();
                prod.put("productoId", prodDoc.getString("productoId"));
                prod.put("nombre", prodDoc.getString("nombre"));
                prod.put("precio", prodDoc.getDouble("precio"));
                prod.put("cantidad", prodDoc.getInteger("cantidad"));
                productos.add(prod);
            }
            carrito.setProductos(productos);
        }

        return carrito;
    }
}
