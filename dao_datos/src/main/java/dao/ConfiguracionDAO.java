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
        Document doc = new Document();

        if (configuracion.getId() != null) {
            doc.append("_id", configuracion.getId());
        }

        doc.append("nombre", configuracion.getNombre())
           .append("precioTotal", configuracion.getPrecioTotal())
           .append("fechaCreacion", configuracion.getFechaCreacion());

        if (configuracion.getComponentes() != null) {
            List<Document> componentesDocs = new ArrayList<>();
            for (Map<String, Object> componente : configuracion.getComponentes()) {
                Document compDoc = new Document(componente);
                componentesDocs.add(compDoc);
            }
            doc.append("componentes", componentesDocs);
        }

        if (configuracion.getId() == null) {
            coleccion.insertOne(doc);
            configuracion.setId(doc.getObjectId("_id"));
        } else {
            coleccion.replaceOne(
                new Document("_id", configuracion.getId()),
                doc
            );
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
}
