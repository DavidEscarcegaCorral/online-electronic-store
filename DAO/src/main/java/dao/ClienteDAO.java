package dao;

import com.mongodb.MongoException;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import dao.interfaces.IClienteDAO;
import entities.Cliente;
import org.bson.types.ObjectId;
import Exception.DAOException;
import Exception.EntityNotFoundException;
import ConfigDb.MongoClientProvider;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ClienteDAO implements IClienteDAO {

    private final MongoCollection<Cliente> collection;

    public ClienteDAO() {
        this.collection=MongoClientProvider.INSTANCE.getCollection("Clientes",Cliente.class);
    }
    @Override
    public ObjectId create(Cliente client) throws DAOException {
        try{
            if(client.get_id()==null) client.set_id(new ObjectId());
            client.setCreatedIn(Instant.now());
            collection.insertOne(client);
            return client.get_id();
        } catch (MongoException e) {
            throw new DAOException("Client insert error: ",e);
        }
    }

    @Override
    public Optional<Cliente> findById(ObjectId id) throws DAOException {
        try{
            return Optional.ofNullable(collection.find(Filters.eq("_id",id)).first());
        } catch (MongoException e) {
            throw new DAOException("Client find error: ",e);
        }
    }

    @Override
    public List<Cliente> findAll() throws DAOException {
        try{
            return collection.find().into(new ArrayList<>());
        }catch (MongoException e) {
            throw new DAOException("Client collection error: ",e);
        }
    }

    @Override
    public boolean update(Cliente client) throws DAOException, EntityNotFoundException {
        try{
            client.setUpdatedIn(Instant.now());
            var result=collection.updateOne(
                    Filters.eq("_id",client.get_id()),
                    Updates.combine(
                            Updates.set("name",client.getName()),
                            Updates.set("paternalLastName",client.getPaternalLastName()),
                            Updates.set("maternalLastName",client.getMaternalLastName()),
                            Updates.set("email",client.getEmail()),
                            Updates.set("address",client.getAddress()),
                            Updates.set("car",client.getCar()),
                            Updates.set("upodated_In",client.getUpdatedIn())
                    )
            );
            if(result.getMatchedCount()==0)
                throw new EntityNotFoundException("Client not found: "+ client.get_id());
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
        }
    }

    @Override
    public long deleteAll() throws DAOException {
        try{
            return collection.deleteMany(Filters.exists("_id")).getDeletedCount();
        } catch (MongoException e) {
            throw new DAOException("Client delete error: ",e);
        }
    }

    @Override
    public Optional<Cliente> findByName(String name) throws DAOException {
        try{
            return Optional.ofNullable(collection.find(Filters.eq("name",name)).first());
        } catch (MongoException e) {
            throw new DAOException("Name Consulting by name error: ",e);
        }

    }
}
