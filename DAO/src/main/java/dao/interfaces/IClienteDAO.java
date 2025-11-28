package dao.interfaces;

import org.bson.types.ObjectId;
import entities.Cliente;
import Exception.DAOException;
import Exception.EntityNotFoundException;
import java.util.List;
import java.util.Optional;

public interface IClienteDAO {
    ObjectId create(Cliente client) throws DAOException;
    Optional<Cliente> findById(ObjectId id) throws DAOException;
    List<Cliente>  findAll() throws DAOException;
    boolean update(Cliente client) throws DAOException,EntityNotFoundException;
    boolean deleteById(ObjectId id) throws DAOException, EntityNotFoundException;
    long deleteAll() throws DAOException;
    Optional<Cliente> findByName(String name) throws DAOException;
}
