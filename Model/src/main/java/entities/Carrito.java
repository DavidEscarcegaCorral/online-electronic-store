package entities;

import org.bson.codecs.pojo.annotations.BsonProperty;
import org.bson.types.ObjectId;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class Carrito {

    private ObjectId _id;
    private Integer totalCost;
    private Cliente client;

    @BsonProperty("created_In")
    private Instant createdIn;

    @BsonProperty("updated_In")
    private Instant updatedIn;

    public Carrito(ObjectId _id, Integer totalCost, Cliente client) {
        this._id = _id;
        this.totalCost = totalCost;
        this.client = client;
        this.createdIn = Instant.now();
    }

    public ObjectId get_id() {
        return _id;
    }

    public void set_id(ObjectId _id) {
        this._id = _id;
    }

    public Integer getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(Integer totalCost) {
        this.totalCost = totalCost;
    }

    public Cliente getClient() {
        return client;
    }

    public void setClient(Cliente client) {
        this.client = client;
    }

    public Instant getUpdatedIn() {
        return updatedIn;
    }

    public void setUpdatedIn(Instant updatedIn) {
        this.updatedIn = updatedIn;
    }
}
