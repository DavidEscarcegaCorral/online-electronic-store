package dao;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
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
        Document doc = new Document();
        if (usuario.getId() != null) {
            doc.append("_id", usuario.getId());
        }
        doc.append("nombre", usuario.getNombre())
            .append("email", usuario.getEmail());

        if (usuario.getId() == null) {
            coleccion.insertOne(doc);
            usuario.setId(doc.getObjectId("_id"));
        } else {
            coleccion.replaceOne(new Document("_id", usuario.getId()), doc);
        }
    }
}
