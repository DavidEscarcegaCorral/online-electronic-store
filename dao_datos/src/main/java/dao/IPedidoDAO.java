package dao;

import entidades.PedidoEntidad;

import java.util.List;

/**
 * Interfaz para operaciones de acceso a datos de Pedidos.
 */
public interface IPedidoDAO {
    String crearPedido(PedidoEntidad pedido);

    PedidoEntidad obtenerPorId(String id);

    List<PedidoEntidad> obtenerPorCliente(String clienteId);

    boolean actualizarEstado(String id, String nuevoEstado);
}

