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
        System.out.println("=== GET CARRITO ACTUAL ===");
        if (carritoActual == null) {
            System.out.println("Carrito actual es null, inicializando...");
            UsuarioDAO usuarioDAO = new UsuarioDAO();
            UsuarioEntidad usuario = usuarioDAO.obtenerPorEmail("cliente_default@local");
            if (usuario == null) {
                System.out.println("Usuario default no existe, creándolo...");
                usuario = new UsuarioEntidad();
                usuario.setNombre("Cliente Default");
                usuario.setEmail("cliente_default@local");
                usuarioDAO.guardar(usuario);
            }
            System.out.println("Usuario ID: " + usuario.getId());

            CarritoDAO dao = new CarritoDAO();
            String clienteId = usuario.getId() != null ? usuario.getId().toString() : "cliente_default";
            System.out.println("Buscando carrito por clienteId: " + clienteId);
            carritoActual = dao.obtenerPorClienteId(clienteId);

            if (carritoActual == null) {
                System.out.println("Carrito no encontrado con clienteId nuevo, buscando con 'cliente_default'...");
                CarritoEntidad antiguo = dao.obtenerPorClienteId("cliente_default");
                if (antiguo != null) {
                    System.out.println("Carrito antiguo encontrado, actualizando clienteId...");
                    antiguo.setClienteId(clienteId);
                    dao.guardar(antiguo);
                    carritoActual = antiguo;
                } else {
                    System.out.println("No existe carrito, creando nuevo...");
                    carritoActual = new CarritoEntidad();
                    carritoActual.setClienteId(clienteId);
                    dao.guardar(carritoActual);
                }
            }
            System.out.println("Carrito inicializado - ID: " + carritoActual.getId() + ", ConfigsIDs: " +
                (carritoActual.getConfiguracionesIds() != null ? carritoActual.getConfiguracionesIds().size() : 0));
        } else {
            System.out.println("Carrito ya existe en memoria - ID: " + carritoActual.getId() + ", ConfigsIDs: " +
                (carritoActual.getConfiguracionesIds() != null ? carritoActual.getConfiguracionesIds().size() : 0));
        }
        System.out.println("=== FIN GET CARRITO ACTUAL ===\n");
        return carritoActual;
    }

    /**
     * Fuerza la recarga del carrito desde la base de datos.
     * Útil para sincronizar después de operaciones en BD.
     */
    public static void recargarCarrito() {
        System.out.println("Forzando recarga del carrito desde BD...");
        carritoActual = null;
        getCarritoActual();
    }

    public void guardar(CarritoEntidad carrito) {
         System.out.println("Carrito creado");
        System.out.println("Carrito ID: " + carrito.getId());
        System.out.println("ClienteId: " + carrito.getClienteId());
        System.out.println("Configuraciones en carrito: " +
            (carrito.getConfiguracionesIds() != null ? carrito.getConfiguracionesIds().size() : 0));

        if (carrito.getConfiguracionesIds() != null && !carrito.getConfiguracionesIds().isEmpty()) {
            System.out.println("IDs de configuraciones:");
            for (ObjectId id : carrito.getConfiguracionesIds()) {
                System.out.println("  - " + id.toString());
            }
        }

        // Convertir fecha a Date
        java.util.Date fechaActualizacion = null;
        if (carrito.getFechaActualizacion() != null) {
            fechaActualizacion = java.util.Date.from(
                carrito.getFechaActualizacion()
                    .atZone(java.time.ZoneId.systemDefault())
                    .toInstant()
            );
        } else {
            fechaActualizacion = new java.util.Date();
        }

        List<ObjectId> ids = carrito.getConfiguracionesIds() != null
            ? new ArrayList<>(carrito.getConfiguracionesIds())
            : new ArrayList<>();

        List<Document> productos = new ArrayList<>();
        if (carrito.getProductos() != null) {
            for (java.util.Map<String, Object> prod : carrito.getProductos()) {
                Document prodDoc = new Document();
                prodDoc.append("productoId", prod.get("productoId"));
                prodDoc.append("cantidad", prod.get("cantidad"));
                productos.add(prodDoc);
            }
        }

        if (carrito.getId() == null) {
            Document doc = new Document();
            doc.append("clienteId", carrito.getClienteId())
               .append("fechaActualizacion", fechaActualizacion)
               .append("configuracionesIds", ids)
               .append("productos", productos);

            System.out.println("Insertando nuevo carrito...");
            System.out.println("Documento a insertar: " + doc.toJson());
            coleccion.insertOne(doc);
            carrito.setId(doc.getObjectId("_id"));
            System.out.println("Carrito insertado con ID: " + carrito.getId());
        } else {
            Document updateDoc = new Document("$set", new Document()
               .append("clienteId", carrito.getClienteId())
               .append("fechaActualizacion", fechaActualizacion)
               .append("configuracionesIds", ids)
               .append("productos", productos));

            System.out.println("Actualizando carrito existente con $set...");
            System.out.println("Filtro: _id = " + carrito.getId());
            System.out.println("Documento a actualizar: " + updateDoc.toJson());

            var result = coleccion.updateOne(
                new Document("_id", carrito.getId()),
                updateDoc
            );
            System.out.println("Documentos modificados: " + result.getModifiedCount());
            System.out.println("Documentos coincidentes: " + result.getMatchedCount());
            System.out.println("Documentos upsertados: " + result.getUpsertedId());

            if (result.getMatchedCount() == 0) {
                System.err.println("ADVERTENCIA: No se encontró el carrito en BD para actualizar!");
                System.err.println("Intentando con replaceOne como fallback...");

                Document docReplace = new Document();
                docReplace.append("clienteId", carrito.getClienteId())
                   .append("fechaActualizacion", fechaActualizacion)
                   .append("configuracionesIds", ids)
                   .append("productos", productos);

                var resultReplace = coleccion.replaceOne(
                    new Document("_id", carrito.getId()),
                    docReplace
                );
                System.out.println("ReplaceOne - Documentos modificados: " + resultReplace.getModifiedCount());
            }
        }

        if (carritoActual != null && carritoActual.getId() != null &&
            carritoActual.getId().equals(carrito.getId())) {
            carritoActual = carrito;
            System.out.println("Variable estática actualizada con el carrito guardado");
        }

        System.out.println("Carrito guardado\n");
    }

    public CarritoEntidad obtenerPorClienteId(String clienteId) {
        System.out.println("Buscando carrito en BD por clienteId: " + clienteId);
        Document doc = coleccion.find(Filters.eq("clienteId", clienteId)).first();

        if (doc == null) {
            System.out.println("No se encontró carrito en BD para clienteId: " + clienteId);
            return null;
        }

        System.out.println("Carrito encontrado en BD: " + doc.toJson());

        CarritoEntidad carrito = new CarritoEntidad();
        carrito.setId(doc.getObjectId("_id"));
        carrito.setClienteId(doc.getString("clienteId"));

        List<ObjectId> ids = (List<ObjectId>) doc.get("configuracionesIds");
        if (ids != null) {
            carrito.setConfiguracionesIds(ids);
            System.out.println("ConfiguracionesIds cargados: " + ids.size() + " configuraciones");
        } else {
            System.out.println("ConfiguracionesIds es null en BD");
        }

        List<Document> productosDoc = (List<Document>) doc.get("productos");
        if (productosDoc != null) {
            List<java.util.Map<String, Object>> productos = new ArrayList<>();
            for (Document prodDoc : productosDoc) {
                java.util.Map<String, Object> prod = new java.util.HashMap<>();
                prod.put("productoId", prodDoc.getString("productoId"));
                prod.put("cantidad", prodDoc.getInteger("cantidad"));
                productos.add(prod);
            }
            carrito.setProductos(productos);
            System.out.println("Productos cargados: " + productos.size() + " productos");
        } else {
            System.out.println("Productos es null en BD");
        }

        return carrito;
    }
}
