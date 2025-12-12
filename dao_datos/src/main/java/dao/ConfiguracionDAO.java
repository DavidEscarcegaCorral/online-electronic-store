package dao;

import com.mongodb.client.MongoCollection;
import conexion.ConexionMongoDB;
import entidades.ConfiguracionEntidad;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class ConfiguracionDAO {
    private static final Logger logger = LoggerFactory.getLogger(ConfiguracionDAO.class);
    private final MongoCollection<Document> coleccion;

    public ConfiguracionDAO() {
        this.coleccion = ConexionMongoDB.getInstance()
                .getDatabase()
                .getCollection("configuraciones");
    }

    public void guardar(ConfiguracionEntidad configuracion) {
        logger.debug("Preparando guardar configuración. ID: {}", configuracion.getId());
        logger.debug("Nombre: {}", configuracion.getNombre());
        logger.debug("Usuario ID: {}", configuracion.getUsuarioId());
        logger.debug("Precio Total: ${}", configuracion.getPrecioTotal());
        logger.debug("Componentes: {}",
                (configuracion.getComponentes() != null ? configuracion.getComponentes().size() : 0));

        Date fechaCreacion = null;
        if (configuracion.getFechaCreacion() != null) {
            fechaCreacion = Date.from(
                    configuracion.getFechaCreacion()
                            .atZone(ZoneId.systemDefault())
                            .toInstant()
            );
        } else {
            fechaCreacion = new Date();
        }

        List<Document> componentesDocs = null;
        if (configuracion.getComponentes() != null) {
            componentesDocs = new ArrayList<>();
            for (Map<String, Object> componente : configuracion.getComponentes()) {
                Document compDoc = new Document(componente);
                componentesDocs.add(compDoc);
            }
        }

        if (configuracion.getId() == null) {
            Document doc = new Document();
            doc.append("usuarioId", configuracion.getUsuarioId())
                    .append("nombre", configuracion.getNombre())
                    .append("precioTotal", configuracion.getPrecioTotal())
                    .append("fechaCreacion", fechaCreacion);

            if (componentesDocs != null) {
                doc.append("componentes", componentesDocs);
            }

            logger.info("Insertando nueva configuración para usuarioId: {}", configuracion.getUsuarioId());
            coleccion.insertOne(doc);
            configuracion.setId(doc.getObjectId("_id"));
            logger.info("Configuración insertada exitosamente. ID: {}", configuracion.getId());
        } else {
            Document doc = new Document();
            doc.append("usuarioId", configuracion.getUsuarioId())
                    .append("nombre", configuracion.getNombre())
                    .append("precioTotal", configuracion.getPrecioTotal())
                    .append("fechaCreacion", fechaCreacion);

            if (componentesDocs != null) {
                doc.append("componentes", componentesDocs);
            }

            logger.info("Actualizando configuración existente. ID: {}", configuracion.getId());
            var result = coleccion.replaceOne(
                    new Document("_id", configuracion.getId()),
                    doc
            );
            logger.debug("Documentos modificados: {}", result.getModifiedCount());
            logger.debug("Documentos coincidentes: {}", result.getMatchedCount());
        }
    }

    public ConfiguracionEntidad obtenerPorId(ObjectId id) {
        Document doc = coleccion.find(new Document("_id", id)).first();
        if (doc == null) return null;

        ConfiguracionEntidad config = new ConfiguracionEntidad();
        config.setId(doc.getObjectId("_id"));
        config.setUsuarioId(doc.getString("usuarioId"));
        config.setNombre(doc.getString("nombre"));
        config.setPrecioTotal(doc.getDouble("precioTotal"));

        Object fechaObj = doc.get("fechaCreacion");
        if (fechaObj != null) {
            if (fechaObj instanceof Date) {
                Date date = (Date) fechaObj;
                config.setFechaCreacion(date.toInstant()
                        .atZone(ZoneId.systemDefault())
                        .toLocalDateTime());
            }
        }

        List<Document> compsDocs = (List<Document>) doc.get("componentes");
        if (compsDocs != null) {
            List<Map<String, Object>> componentesList = new ArrayList<>();
            for (Document compDoc : compsDocs) {
                componentesList.add(compDoc);
            }
            config.setComponentes(componentesList);
        }

        return config;
    }

    /**
     * Obtiene todas las configuraciones guardadas de un usuario específico.
     *
     * @param usuarioId El ID del usuario propietario de las configuraciones
     * @return Lista de ConfiguracionEntidad asociadas al usuario
     */
    public List<ConfiguracionEntidad> obtenerPorUsuarioId(String usuarioId) {
        List<ConfiguracionEntidad> configuraciones = new ArrayList<>();

        if (usuarioId == null || usuarioId.trim().isEmpty()) {
            logger.warn("usuarioId es null o vacío. Retornando lista vacía.");
            return configuraciones;
        }

        try {
            logger.debug("Buscando configuraciones para usuarioId: {}", usuarioId);

            var documentos = coleccion.find(new Document("usuarioId", usuarioId)).into(new ArrayList<>());
            logger.info("Se encontraron {} configuraciones para usuarioId: {}", documentos.size(), usuarioId);

            for (Document doc : documentos) {
                ConfiguracionEntidad config = new ConfiguracionEntidad();
                config.setId(doc.getObjectId("_id"));
                config.setUsuarioId(doc.getString("usuarioId"));
                config.setNombre(doc.getString("nombre"));
                config.setPrecioTotal(doc.getDouble("precioTotal"));

                Object fechaObj = doc.get("fechaCreacion");
                if (fechaObj != null && fechaObj instanceof Date) {
                    Date date = (Date) fechaObj;
                    config.setFechaCreacion(date.toInstant()
                            .atZone(ZoneId.systemDefault())
                            .toLocalDateTime());
                }

                List<Document> compsDocs = (List<Document>) doc.get("componentes");
                if (compsDocs != null) {
                    List<Map<String, Object>> componentesList = new ArrayList<>();
                    for (Document compDoc : compsDocs) {
                        componentesList.add(compDoc);
                    }
                    config.setComponentes(componentesList);
                }

                configuraciones.add(config);
            }
        } catch (Exception e) {
            logger.error("Error al obtener configuraciones para usuarioId: {}", usuarioId, e);
        }

        return configuraciones;
    }

    public boolean eliminar(ObjectId id) {
        try {
            logger.info("Eliminando configuración con ID: {}", id);
            var result = coleccion.deleteOne(new Document("_id", id));
            boolean eliminado = result.getDeletedCount() > 0;
            if (eliminado) {
                logger.info("Configuración eliminada exitosamente. ID: {}", id);
            } else {
                logger.warn("No se encontró configuración para eliminar. ID: {}", id);
            }
            return eliminado;
        } catch (Exception e) {
            logger.error("Error al eliminar configuración con ID: {}", id, e);
            return false;
        }
    }

    public boolean eliminarMultiples(List<ObjectId> ids) {
        try {
            if (ids == null || ids.isEmpty()) {
                logger.debug("Lista de IDs vacía. No hay nada que eliminar.");
                return true;
            }
            logger.info("Eliminando {} configuraciones", ids.size());
            var result = coleccion.deleteMany(new Document("_id", new Document("$in", ids)));
            logger.info("Se eliminaron {} configuraciones", result.getDeletedCount());
            return result.getDeletedCount() > 0;
        } catch (Exception e) {
            logger.error("Error al eliminar múltiples configuraciones", e);
            return false;
        }
    }

    /**
     * Obtiene todas las configuraciones guardadas de un usuario específico y las ordena por fecha.
     *
     * @param usuarioId El ID del usuario propietario de las configuraciones
     * @return Lista de ConfiguracionEntidad asociadas al usuario ordenadas por fecha descendente
     */
    public List<ConfiguracionEntidad> obtenerPorUsuarioIdOrdenado(String usuarioId) {
        List<ConfiguracionEntidad> configuraciones = obtenerPorUsuarioId(usuarioId);
        if (configuraciones != null) {
            configuraciones.sort((c1, c2) -> {
                if (c1.getFechaCreacion() == null || c2.getFechaCreacion() == null) {
                    return 0;
                }
                return c2.getFechaCreacion().compareTo(c1.getFechaCreacion());
            });
        }
        return configuraciones;
    }
}
