package dao;

import ConfigDb.MongoClientProvider;
import com.mongodb.MongoException;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import dao.interfaces.ICarritoDAO;
import entities.Carrito;
import entities.Cliente;
import org.bson.types.ObjectId;
import Exception.DAOException;
import Exception.EntityNotFoundException;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CarritoDAO implements ICarritoDAO {
    private final MongoCollection<Carrito> collection;

    public CarritoDAO() {
        collection=MongoClientProvider.INSTANCE.getCollection("carritos",Carrito.class);
    }

    @Override
    public ObjectId create(Carrito car) throws DAOException {
        try{
            if(car.get_id()==null) car.set_id(new ObjectId());
            car.setCreatedIn(Instant.now());
            collection.insertOne(car);
            return car.get_id();
        } catch (MongoException e) {
            throw new DAOException("Client insert error: ",e);
        }
    }

    @Override
    public Optional<Carrito> findById(ObjectId id) throws DAOException {
        try{
            return Optional.ofNullable(collection.find(Filters.eq("_id",id)).first());
        } catch (MongoException e) {
            throw new DAOException("Client find error: ",e);
        }    }

    @Override
    public List<Carrito> findAll() throws DAOException {
        try{
            return collection.find().into(new ArrayList<>());
        }catch (MongoException e) {
            throw new DAOException("Client collection error: ",e);
        }
    }

    @Override
    public boolean update(Carrito car) throws DAOException, EntityNotFoundException {
        try{
            car.setUpdatedIn(Instant.now());
            var result=collection.updateOne(
                    Filters.eq("_id",car.get_id()),
                    Updates.combine(
                            Updates.set("products",car.getProducts()),
                            Updates.set("PCs",car.getPcS()),
                            Updates.set("totalCost",car.getTotalCost()),
                            Updates.set("client",car.getClient()),
                            Updates.set("updated_In",car.getUpdatedIn())
                    )
            );
            if(result.getMatchedCount()==0)
                throw new EntityNotFoundException("Client not found: "+ car.get_id());
            return result.getModifiedCount()>0;
        }catch (MongoException e) {
            throw new DAOException("Client update error: ",e);
        }
    }

    @Override
    public boolean deleteById(ObjectId id) throws DAOException, EntityNotFoundException {
        try{
            var result=collection.deleteOne(Filters.eq("_id",id));
            if(result.getDeletedCount()==0)
                throw new DAOException("Client not found: "+ id);
            return true;
        }catch (MongoException e) {
            throw new DAOException("Client delete error: ",e);
        }    }

    @Override
    public long deleteAll() throws DAOException {
        try{
            return collection.deleteMany(Filters.exists("_id")).getDeletedCount();
        } catch (MongoException e) {
            throw new DAOException("Client delete error: ",e);
        }
    }

    @Override
    public Optional<Carrito> findByClient(ObjectId id) throws DAOException {
        try{
            return Optional.ofNullable(collection.find(Filters.eq("client,_id",id)).first());
        } catch (MongoException e) {
            throw new DAOException(" Consulting by name error: ",e);
        }
    }
}
