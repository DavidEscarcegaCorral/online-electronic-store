package dao;

import dto.ComponenteDTO;

import java.util.List;
import java.util.Optional;

/**
 * Interfaz DAO para obtener componentes del catálogo.
 */
public interface ICatalogoDAO {
    List<ComponenteDTO> findByCategoria(String categoria);
    Optional<ComponenteDTO> findById(String id);
}
