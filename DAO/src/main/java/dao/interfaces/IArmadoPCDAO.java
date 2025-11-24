package dao.interfaces;

import entities.ArmadoPC;
import org.bson.types.ObjectId;
import Exception.DAOException;
import Exception.EntityNotFoundException;
import java.util.List;
import java.util.Optional;

public interface IArmadoPCDAO {
    ObjectId create(ArmadoPC pc) throws DAOException;
    Optional<ArmadoPC> findById(ObjectId id) throws DAOException;
    List<ArmadoPC> findAll() throws DAOException;
    boolean update(ArmadoPC pc) throws DAOException, EntityNotFoundException;
    boolean deleteById(ObjectId id) throws DAOException, EntityNotFoundException;
    long deleteAll() throws DAOException;

}
