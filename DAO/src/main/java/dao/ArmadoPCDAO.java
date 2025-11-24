package dao;

import ConfigDb.MongoClientProvider;
import com.mongodb.MongoException;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import dao.interfaces.IArmadoPCDAO;
import entities.ArmadoPC;
import org.bson.types.ObjectId;
import Exception.DAOException;
import Exception.EntityNotFoundException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


public class ArmadoPCDAO implements IArmadoPCDAO {
    private final MongoCollection<ArmadoPC> collection;

    public ArmadoPCDAO(){
        this.collection= MongoClientProvider.INSTANCE.getCollection("armados", ArmadoPC.class);
    }


    @Override
    public ObjectId create(ArmadoPC pc) throws DAOException {
        try{
            if(pc.get_id()==null) pc.set_id(new ObjectId());
            pc.setCreatedIn(Instant.now());
            collection.insertOne(pc);
            return pc.get_id();
        } catch (MongoException e) {
            throw new DAOException("PC insert error: ",e);
        }
    }

    @Override
    public Optional<ArmadoPC> findById(ObjectId id) throws DAOException {
        try{
            return Optional.ofNullable(collection.find(Filters.eq("_id",id)).first());
        } catch (MongoException e) {
            throw new DAOException("PC find error: ",e);
        }      }

    @Override
    public List<ArmadoPC> findAll() throws DAOException {
        try{
            return collection.find().into(new ArrayList<>());
        }catch (MongoException e) {
            throw new DAOException("PC collection error: ",e);
        }    }

    @Override
    public boolean update(ArmadoPC pc) throws DAOException, EntityNotFoundException {
        try{
            pc.setUpdatedIn(Instant.now());
            var result=collection.updateOne(
                    Filters.eq("_id",pc.get_id()),
                    Updates.combine(
                            Updates.set("components",pc.getComponents()),
                            Updates.set("totalPrice",pc.getTotalPrice()),
                            Updates.set("updated_In",pc.getUpdatedIn())
                    )
            );
            if(result.getMatchedCount()==0)
                throw new EntityNotFoundException("PC not found: "+ pc.get_id());
            return result.getModifiedCount()>0;
        }catch (MongoException e) {
            throw new DAOException("PC update error: ",e);
        }    }

    @Override
    public boolean deleteById(ObjectId id) throws DAOException, EntityNotFoundException {
        try{
            var result=collection.deleteOne(Filters.eq("_id",id));
            if(result.getDeletedCount()==0)
                throw new EntityNotFoundException("PC not found: "+ id);
            return true;
        }catch (MongoException e) {
            throw new DAOException("PC delete error: ",e);
        }    }

    @Override
    public long deleteAll() throws DAOException {
        try{
            return collection.deleteMany(Filters.exists("_id")).getDeletedCount();
        } catch (MongoException e) {
            throw new DAOException("PC delete error: ",e);
        }      }
}
