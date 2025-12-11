package dao;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import conexion.ConexionMongoDB;
import entidades.UsuarioEntidad;
import org.bson.Document;
import org.bson.types.ObjectId;

public class UsuarioDAO {
    private final MongoCollection<Document> coleccion;

    public UsuarioDAO() {
        this.coleccion = ConexionMongoDB.getInstance()
                .getDatabase()
                .getCollection("usuarios");
    }

    public UsuarioEntidad obtenerPorEmail(String email) {
        Document doc = coleccion.find(Filters.eq("email", email)).first();
        if (doc == null) return null;

        UsuarioEntidad u = new UsuarioEntidad();
        u.setId(doc.getObjectId("_id"));
        u.setNombre(doc.getString("nombre"));
        u.setEmail(doc.getString("email"));
        return u;
    }

    public UsuarioEntidad obtenerPorId(String id) {
        try {
            ObjectId oid = new ObjectId(id);
            Document doc = coleccion.find(Filters.eq("_id", oid)).first();
            if (doc == null) return null;
            UsuarioEntidad u = new UsuarioEntidad();
            u.setId(doc.getObjectId("_id"));
            u.setNombre(doc.getString("nombre"));
            u.setEmail(doc.getString("email"));
            return u;
        } catch (Exception e) {
            return null;
        }
    }

    public void guardar(UsuarioEntidad usuario) {
        if (usuario.getId() == null) {
            Document doc = new Document();
            doc.append("nombre", usuario.getNombre())
               .append("email", usuario.getEmail());

            coleccion.insertOne(doc);
            usuario.setId(doc.getObjectId("_id"));
        } else {
            coleccion.updateOne(
                Filters.eq("_id", usuario.getId()),
                Updates.combine(
                    Updates.set("nombre", usuario.getNombre()),
                    Updates.set("email", usuario.getEmail())
                )
            );
        }
    }
}
