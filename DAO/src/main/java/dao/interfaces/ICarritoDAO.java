package dao.interfaces;

import entities.Carrito;
import entities.Cliente;
import org.bson.types.ObjectId;
import Exception.DAOException;
import Exception.EntityNotFoundException;
import java.util.List;
import java.util.Optional;

public interface ICarritoDAO {
    ObjectId create(Carrito car) throws DAOException;
    Optional<Carrito> findById(ObjectId id) throws DAOException;
    List<Carrito> findAll() throws DAOException;
    boolean update(Carrito car) throws DAOException, EntityNotFoundException;
    boolean deleteById(ObjectId id) throws DAOException, EntityNotFoundException;
    long deleteAll() throws DAOException;
    Optional<Carrito> findByClient(ObjectId id) throws DAOException;
}
