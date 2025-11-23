package entities;

import org.bson.codecs.pojo.annotations.BsonProperty;
import org.bson.types.ObjectId;

import java.time.Instant;
import java.util.List;

public class Carrito {

    private ObjectId _id;
    private List<Producto> products;
    private List<ArmadoPC> pcS;
    private Integer totalCost;
    private Cliente client;

    @BsonProperty("created_In")
    private Instant created_In;

    @BsonProperty("updated_In")
    private Instant updated_In;

    public Carrito() {}

    public Carrito(ObjectId _id, List<Producto> products, List<ArmadoPC> pcS, Cliente client) {
        this._id = _id;
        this.products = products;
        this.pcS = pcS;
        this.client = client;

        for(Producto p :  products) {
            this.totalCost += p.getPrice();
        }

        for(ArmadoPC a :  pcS) {
            this.totalCost += a.getTotalPrice();
        }
    }

    public ObjectId get_id() {
        return _id;
    }

    public void set_id(ObjectId _id) {
        this._id = _id;
    }

    public List<Producto> getProducts() {
        return products;
    }

    public void setProducts(List<Producto> products) {
        this.products = products;
    }

    public List<ArmadoPC> getPcS() {
        return pcS;
    }

    public void setPcS(List<ArmadoPC> pcS) {
        this.pcS = pcS;
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

    public Instant getCreated_In() {
        return created_In;
    }

    public void setCreated_In(Instant created_In) {
        this.created_In = created_In;
    }

    public Instant getUpdated_In() {
        return updated_In;
    }

    public void setUpdated_In(Instant updated_In) {
        this.updated_In = updated_In;
    }
}
