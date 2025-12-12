package dao;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import conexion.ConexionMongoDB;
import entidades.CarritoEntidad;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CarritoDAO {

    /** Logger para registrar operaciones del DAO */
    private static final Logger logger = LoggerFactory.getLogger(CarritoDAO.class);

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

        // lista de IDs de configuraciones
        List<ObjectId> ids = carrito.getConfiguracionesIds() != null
                ? new ArrayList<>(carrito.getConfiguracionesIds())
                : new ArrayList<>();

        // Convertir productos a formato Document de MongoDB
        List<Document> productos = new ArrayList<>();
        if (carrito.getProductos() != null) {
            for (java.util.Map<String, Object> prod : carrito.getProductos()) {
                Document prodDoc = new Document();
                prodDoc.append("productoId", prod.get("productoId"));
                prodDoc.append("cantidad", prod.get("cantidad"));
                productos.add(prodDoc);
            }
        }

        // CASO 1: Inserción de nuevo carrito
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

            // Insertar en MongoDB
            coleccion.insertOne(doc);

            // Asignar el ID generado por MongoDB al objeto carrito
            carrito.setId(doc.getObjectId("_id"));
            logger.info("Carrito insertado exitosamente. ID: {}", carrito.getId());
        } else {
            // CASO 2: Actualización de carrito existente
            // Crear documento con todos los campos a actualizar
            // Nota: Incluimos arrays vacíos para limpiar el carrito cuando sea necesario
            Document setDoc = new Document()
                    .append("clienteId", carrito.getClienteId())
                    .append("fechaActualizacion", fechaActualizacion)
                    .append("configuracionesIds", ids)
                    .append("productos", productos);

            // Operación $set de MongoDB para actualizar campos específicos
            Document updateDoc = new Document("$set", setDoc);

            logger.info("Actualizando carrito existente. ID: {}", carrito.getId());

            // Ejecutar actualización en MongoDB
            var result = coleccion.updateOne(
                    new Document("_id", carrito.getId()),
                    updateDoc
            );

            // Logging de resultados de la operación
            logger.debug("Documentos modificados: {}", result.getModifiedCount());
            logger.debug("Documentos coincidentes: {}", result.getMatchedCount());

            // Fallback: Si no se encontró el documento, intentar con replaceOne
            if (result.getMatchedCount() == 0) {
                logger.warn("No se encontró el carrito en BD para actualizar. ID: {}", carrito.getId());
                logger.info("Intentando con replaceOne como fallback...");

                // Crear documento completo para reemplazo
                Document docReplace = new Document();
                docReplace.append("clienteId", carrito.getClienteId())
                        .append("fechaActualizacion", fechaActualizacion)
                        .append("configuracionesIds", ids)
                        .append("productos", productos);

                // Intentar reemplazar el documento completo
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
        // Buscar documento en MongoDB por clienteId
        Document doc = coleccion.find(Filters.eq("clienteId", clienteId)).first();

        if (doc == null) {
            return null;
        }

        // Crear objeto CarritoEntidad y mapear campos básicos
        CarritoEntidad carrito = new CarritoEntidad();
        carrito.setId(doc.getObjectId("_id"));
        carrito.setClienteId(doc.getString("clienteId"));

        // Mapear lista de IDs de configuraciones
        List<ObjectId> ids = (List<ObjectId>) doc.get("configuracionesIds");
        if (ids != null) {
            carrito.setConfiguracionesIds(ids);
        }

        // Mapear lista de productos individuales
        List<Document> productosDoc = (List<Document>) doc.get("productos");
        if (productosDoc != null) {
            List<java.util.Map<String, Object>> productos = new ArrayList<>();
            for (Document prodDoc : productosDoc) {
                // Convertir cada Document de producto a Map
                java.util.Map<String, Object> prod = new java.util.HashMap<>();
                prod.put("productoId", prodDoc.getString("productoId"));
                prod.put("cantidad", prodDoc.getInteger("cantidad"));
                productos.add(prod);
            }
            carrito.setProductos(productos);
        }

        return carrito;
    }
}
