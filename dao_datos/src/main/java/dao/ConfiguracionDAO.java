package dao;

import com.mongodb.client.MongoCollection;
import conexion.ConexionMongoDB;
import entidades.ConfiguracionEntidad;
import org.bson.Document;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ConfiguracionDAO {
    private final MongoCollection<Document> coleccion;

    public ConfiguracionDAO() {
        this.coleccion = ConexionMongoDB.getInstance()
                .getDatabase()
                .getCollection("configuraciones");
    }

    public void guardar(ConfiguracionEntidad configuracion) {
        System.out.println("=== GUARDANDO CONFIGURACION ===");
        System.out.println("Configuracion ID: " + configuracion.getId());
        System.out.println("Nombre: " + configuracion.getNombre());
        System.out.println("Precio Total: $" + configuracion.getPrecioTotal());
        System.out.println("Componentes: " +
            (configuracion.getComponentes() != null ? configuracion.getComponentes().size() : 0));

        // Convertir LocalDateTime a Date para MongoDB
        java.util.Date fechaCreacion = null;
        if (configuracion.getFechaCreacion() != null) {
            fechaCreacion = java.util.Date.from(
                configuracion.getFechaCreacion()
                    .atZone(java.time.ZoneId.systemDefault())
                    .toInstant()
            );
        } else {
            fechaCreacion = new java.util.Date();
        }

        // Convertir componentes
        List<Document> componentesDocs = null;
        if (configuracion.getComponentes() != null) {
            componentesDocs = new ArrayList<>();
            for (Map<String, Object> componente : configuracion.getComponentes()) {
                Document compDoc = new Document(componente);
                componentesDocs.add(compDoc);
            }
        }

        if (configuracion.getId() == null) {
            // INSERT - MongoDB asignará _id automáticamente
            Document doc = new Document();
            doc.append("nombre", configuracion.getNombre())
               .append("precioTotal", configuracion.getPrecioTotal())
               .append("fechaCreacion", fechaCreacion);

            if (componentesDocs != null) {
                doc.append("componentes", componentesDocs);
            }

            System.out.println("Insertando nueva configuración...");
            coleccion.insertOne(doc);
            configuracion.setId(doc.getObjectId("_id"));
            System.out.println("Configuración insertada con ID: " + configuracion.getId());
        } else {
            // UPDATE - NO incluir _id en el documento
            Document doc = new Document();
            doc.append("nombre", configuracion.getNombre())
               .append("precioTotal", configuracion.getPrecioTotal())
               .append("fechaCreacion", fechaCreacion);

            if (componentesDocs != null) {
                doc.append("componentes", componentesDocs);
            }

            System.out.println("Actualizando configuración existente...");
            var result = coleccion.replaceOne(
                new Document("_id", configuracion.getId()),
                doc
            );
            System.out.println("Documentos modificados: " + result.getModifiedCount());
            System.out.println("Documentos coincidentes: " + result.getMatchedCount());
        }
    }

    public ConfiguracionEntidad obtenerPorId(org.bson.types.ObjectId id) {
        Document doc = coleccion.find(new Document("_id", id)).first();
        if (doc == null) return null;

        ConfiguracionEntidad config = new ConfiguracionEntidad();
        config.setId(doc.getObjectId("_id"));
        config.setNombre(doc.getString("nombre"));
        config.setPrecioTotal(doc.getDouble("precioTotal"));

        Object fechaObj = doc.get("fechaCreacion");
        if (fechaObj != null) {
            if (fechaObj instanceof java.util.Date) {
                java.util.Date date = (java.util.Date) fechaObj;
                config.setFechaCreacion(date.toInstant()
                    .atZone(java.time.ZoneId.systemDefault())
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

    public boolean eliminar(org.bson.types.ObjectId id) {
        try {
            var result = coleccion.deleteOne(new Document("_id", id));
            return result.getDeletedCount() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean eliminarMultiples(List<org.bson.types.ObjectId> ids) {
        try {
            if (ids == null || ids.isEmpty()) {
                return true;
            }
            var result = coleccion.deleteMany(new Document("_id", new Document("$in", ids)));
            return result.getDeletedCount() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
