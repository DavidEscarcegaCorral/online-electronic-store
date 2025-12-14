package dao;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import conexion.ConexionMongoDB;
import entidades.PedidoEntidad;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * DAO para Pedidos usando MongoDB.
 */
public class PedidoDAO {
    private final MongoCollection<Document> coleccion;

    public PedidoDAO() {
        this.coleccion = ConexionMongoDB.getInstance()
                .getDatabase()
                .getCollection("pedidos");
    }

    public String crearPedido(PedidoEntidad pedido) {
        System.out.println("ClienteId: " + pedido.getClienteId());
        System.out.println("Total: $" + pedido.getTotal());
        System.out.println("Estado: " + pedido.getEstado());
        System.out.println("Items: " + (pedido.getItems() != null ? pedido.getItems().size() : 0));

        try {
            Document doc = new Document();
            doc.append("clienteId", pedido.getClienteId());
            doc.append("items", convertirItems(pedido.getItems()));
            doc.append("total", pedido.getTotal() != null ? pedido.getTotal().doubleValue() : 0.0);
            doc.append("estado", pedido.getEstado() != null ? pedido.getEstado().name() : "PENDIENTE");
            doc.append("fechaCreacion", pedido.getFechaCreacion() != null ?
                Date.from(pedido.getFechaCreacion().atZone(ZoneId.systemDefault()).toInstant()) :
                new Date());

            if (pedido.getMetodoPago() != null) {
                doc.append("metodoPago", new Document()
                        .append("tipo", pedido.getMetodoPago().getTipo())
                        .append("detalles", pedido.getMetodoPago().getDetalles()));
                System.out.println("Método de pago: " + pedido.getMetodoPago().getTipo());
            }

            if (pedido.getDireccionEntrega() != null) {
                doc.append("direccionEntrega", new Document()
                        .append("calle", pedido.getDireccionEntrega().getCalle())
                        .append("ciudad", pedido.getDireccionEntrega().getCiudad())
                        .append("estado", pedido.getDireccionEntrega().getEstado())
                        .append("codigoPostal", pedido.getDireccionEntrega().getCodigoPostal()));
            }

            System.out.println("Documento del pedido: " + doc.toJson());
            System.out.println("Insertando en colección 'pedidos'...");

            coleccion.insertOne(doc);

            ObjectId pedidoId = doc.getObjectId("_id");
            System.out.println("Pedido insertado con ID: " + pedidoId);

            return pedidoId.toString();
        } catch (Exception e) {
            System.err.println("ERROR al crear pedido:");
            e.printStackTrace();
            return null;
        }
    }

    public PedidoEntidad obtenerPorId(String id) {
        Document doc = coleccion.find(Filters.eq("_id", new ObjectId(id))).first();
        return doc != null ? documentoAEntidad(doc) : null;
    }

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

    public boolean actualizarEstado(String id, PedidoEntidad.EstadoPedido nuevoEstado) {
        return coleccion.updateOne(
                Filters.eq("_id", new ObjectId(id)),
                Updates.set("estado", nuevoEstado.name())
        ).getModifiedCount() > 0;
    }

    private List<Document> convertirItems(List<PedidoEntidad.ItemPedido> items) {
        List<Document> docs = new ArrayList<>();
        for (PedidoEntidad.ItemPedido item : items) {
            docs.add(new Document()
                    .append("productoId", item.getProductoId())
                    .append("nombre", item.getNombre())
                    .append("cantidad", item.getCantidad())
                    .append("precioUnitario", item.getPrecioUnitario() != null ?
                        item.getPrecioUnitario().doubleValue() : 0.0));
        }
        return docs;
    }

    private PedidoEntidad documentoAEntidad(Document doc) {
        PedidoEntidad pedido = new PedidoEntidad();
        pedido.setId(doc.getObjectId("_id"));
        pedido.setClienteId(doc.getString("clienteId"));

        Double totalDouble = doc.getDouble("total");
        pedido.setTotal(totalDouble != null ? BigDecimal.valueOf(totalDouble) : BigDecimal.ZERO);

        String estadoStr = doc.getString("estado");
        if (estadoStr != null) {
            try {
                pedido.setEstado(PedidoEntidad.EstadoPedido.valueOf(estadoStr));
            } catch (IllegalArgumentException e) {
                pedido.setEstado(PedidoEntidad.EstadoPedido.PENDIENTE);
            }
        } else {
            pedido.setEstado(PedidoEntidad.EstadoPedido.PENDIENTE);
        }

        Date fechaDate = doc.getDate("fechaCreacion");
        if (fechaDate != null) {
            pedido.setFechaCreacion(LocalDateTime.ofInstant(fechaDate.toInstant(), ZoneId.systemDefault()));
        }

        List<?> itemsDocs = doc.getList("items", Document.class);
        if (itemsDocs != null) {
            List<PedidoEntidad.ItemPedido> items = new ArrayList<>();
            for (Object obj : itemsDocs) {
                Document itemDoc = (Document) obj;
                PedidoEntidad.ItemPedido item = new PedidoEntidad.ItemPedido();
                item.setProductoId(itemDoc.getString("productoId"));
                item.setNombre(itemDoc.getString("nombre"));
                item.setCantidad(itemDoc.getInteger("cantidad"));

                Double precioDouble = itemDoc.getDouble("precioUnitario");
                item.setPrecioUnitario(precioDouble != null ? BigDecimal.valueOf(precioDouble) : BigDecimal.ZERO);

                items.add(item);
            }
            pedido.setItems(items);
        }

        Document pagoDoc = doc.get("metodoPago", Document.class);
        if (pagoDoc != null) {
            PedidoEntidad.MetodoPagoInfo metodoPago = new PedidoEntidad.MetodoPagoInfo();
            metodoPago.setTipo(pagoDoc.getString("tipo"));
            metodoPago.setDetalles(pagoDoc.getString("detalles"));
            pedido.setMetodoPago(metodoPago);
        }

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

