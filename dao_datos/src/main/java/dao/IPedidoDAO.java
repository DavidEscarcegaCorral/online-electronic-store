package dao;

import dto.CompraDTO;

import java.util.Optional;

/**
 * Interfaz DAO para persistir pedidos/compras.
 */
public interface IPedidoDAO {
    void save(CompraDTO compra);
    Optional<CompraDTO> findById(String id);
}
