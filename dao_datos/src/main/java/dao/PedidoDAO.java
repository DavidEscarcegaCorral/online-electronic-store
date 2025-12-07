package dao;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import conexion.ConexionMongoDB;
import entidades.PedidoEntidad;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Implementación del DAO para Pedidos usando MongoDB.
 */
public class PedidoDAO implements IPedidoDAO {
    private final MongoCollection<Document> coleccion;

    public PedidoDAO() {
        this.coleccion = ConexionMongoDB.getInstance()
                .getDatabase()
                .getCollection("pedidos");
    }

    @Override
    public String crearPedido(PedidoEntidad pedido) {
        Document doc = new Document();
        doc.append("clienteId", pedido.getClienteId());
        doc.append("items", convertirItems(pedido.getItems()));
        doc.append("total", pedido.getTotal());
        doc.append("estado", pedido.getEstado());
        doc.append("fechaCreacion", new Date());

        if (pedido.getMetodoPago() != null) {
            doc.append("metodoPago", new Document()
                    .append("tipo", pedido.getMetodoPago().getTipo())
                    .append("detalles", pedido.getMetodoPago().getDetalles()));
        }

        if (pedido.getDireccionEntrega() != null) {
            doc.append("direccionEntrega", new Document()
                    .append("calle", pedido.getDireccionEntrega().getCalle())
                    .append("ciudad", pedido.getDireccionEntrega().getCiudad())
                    .append("estado", pedido.getDireccionEntrega().getEstado())
                    .append("codigoPostal", pedido.getDireccionEntrega().getCodigoPostal()));
        }

        coleccion.insertOne(doc);
        return doc.getObjectId("_id").toString();
    }

    @Override
    public PedidoEntidad obtenerPorId(String id) {
        Document doc = coleccion.find(Filters.eq("_id", new ObjectId(id))).first();
        return doc != null ? documentoAEntidad(doc) : null;
    }

    @Override
    public List<PedidoEntidad> obtenerPorCliente(String clienteId) {
        List<PedidoEntidad> pedidos = new ArrayList<>();
        try (MongoCursor<Document> cursor = coleccion
                .find(Filters.eq("clienteId", clienteId))
                .iterator()) {
            while (cursor.hasNext()) {
                pedidos.add(documentoAEntidad(cursor.next()));
            }
        }
        return pedidos;
    }

    @Override
    public boolean actualizarEstado(String id, String nuevoEstado) {
        return coleccion.updateOne(
                Filters.eq("_id", new ObjectId(id)),
                Updates.set("estado", nuevoEstado)
        ).getModifiedCount() > 0;
    }

    private List<Document> convertirItems(List<PedidoEntidad.ItemPedido> items) {
        List<Document> docs = new ArrayList<>();
        for (PedidoEntidad.ItemPedido item : items) {
            docs.add(new Document()
                    .append("productoId", item.getProductoId())
                    .append("nombre", item.getNombre())
                    .append("cantidad", item.getCantidad())
                    .append("precioUnitario", item.getPrecioUnitario()));
        }
        return docs;
    }

    private PedidoEntidad documentoAEntidad(Document doc) {
        PedidoEntidad pedido = new PedidoEntidad();
        pedido.setId(doc.getObjectId("_id"));
        pedido.setClienteId(doc.getString("clienteId"));
        pedido.setTotal(doc.getDouble("total"));
        pedido.setEstado(doc.getString("estado"));
        pedido.setFechaCreacion(doc.getDate("fechaCreacion"));

        // Convertir items
        List<?> itemsDocs = doc.getList("items", Document.class);
        if (itemsDocs != null) {
            List<PedidoEntidad.ItemPedido> items = new ArrayList<>();
            for (Object obj : itemsDocs) {
                Document itemDoc = (Document) obj;
                PedidoEntidad.ItemPedido item = new PedidoEntidad.ItemPedido();
                item.setProductoId(itemDoc.getString("productoId"));
                item.setNombre(itemDoc.getString("nombre"));
                item.setCantidad(itemDoc.getInteger("cantidad"));
                item.setPrecioUnitario(itemDoc.getDouble("precioUnitario"));
                items.add(item);
            }
            pedido.setItems(items);
        }

        // Convertir método de pago
        Document pagoDoc = doc.get("metodoPago", Document.class);
        if (pagoDoc != null) {
            PedidoEntidad.MetodoPagoInfo metodoPago = new PedidoEntidad.MetodoPagoInfo();
            metodoPago.setTipo(pagoDoc.getString("tipo"));
            metodoPago.setDetalles(pagoDoc.getString("detalles"));
            pedido.setMetodoPago(metodoPago);
        }

        // Convertir dirección
        Document dirDoc = doc.get("direccionEntrega", Document.class);
        if (dirDoc != null) {
            PedidoEntidad.DireccionEntrega direccion = new PedidoEntidad.DireccionEntrega();
            direccion.setCalle(dirDoc.getString("calle"));
            direccion.setCiudad(dirDoc.getString("ciudad"));
            direccion.setEstado(dirDoc.getString("estado"));
            direccion.setCodigoPostal(dirDoc.getString("codigoPostal"));
            pedido.setDireccionEntrega(direccion);
        }

        return pedido;
    }
}

