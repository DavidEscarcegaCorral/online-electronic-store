package dao;

import dto.CompraDTO;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

/**
 * Implementación en memoria para persistir pedidos.
 */
public class InMemoryPedidoDAO implements IPedidoDAO {
    private final Map<String, CompraDTO> storage = new HashMap<>();

    @Override
    public void save(CompraDTO compra) {
        if (compra.getId() == null) {
            compra.setId(UUID.randomUUID().toString());
        }
        storage.put(compra.getId(), compra);
    }

    @Override
    public Optional<CompraDTO> findById(String id) {
        return Optional.ofNullable(storage.get(id));
    }
}
