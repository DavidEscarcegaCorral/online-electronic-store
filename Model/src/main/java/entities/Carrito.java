package entities;

import org.bson.codecs.pojo.annotations.BsonProperty;
import org.bson.types.ObjectId;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class Carrito {

    private ObjectId _id;
    private List<Producto> products;
    private List<ArmadoPC> pcS;
    private Integer totalCost;
    private Cliente client;

    @BsonProperty("created_In")
    private Instant createdIn;

    @BsonProperty("updated_In")
    private Instant updatedIn;

    public Carrito() {
        this.products = new ArrayList<>();
        this.pcS = new ArrayList<>();
        this.totalCost = 0;
        this.createdIn = Instant.now();
        this.updatedIn = Instant.now();
    }

    public Carrito(ObjectId _id, List<Producto> products, List<ArmadoPC> pcS, Cliente client) {
        this._id = _id;
        this.products = (products!=null)?products: new ArrayList<>();
        this.pcS = (pcS != null)?pcS: new ArrayList<>();
        this.client = client;

        this.createdIn = Instant.now();
        this.updatedIn = Instant.now();

        this.totalCost = 0;

    }

    private void recalcularTotal(){
        this.totalCost = 0;
        for(Producto p : products){
            this.totalCost+=p.getPrice();
        }
        for(ArmadoPC a : pcS){
            this.totalCost+=a.getTotalPrice();
        }
    }

    public void agregarProducto(Producto p) {
        this.products.add(p);
        this.totalCost += p.getPrice();
        this.updatedIn = Instant.now();
    }

    public void agregarPC(ArmadoPC pc) {
        this.pcS.add(pc);
        this.totalCost += pc.getTotalPrice();
        this.updatedIn = Instant.now();
    }

    public void removerProducto(Producto p) {
        if (this.products.remove(p)) {
            this.totalCost -= p.getPrice();
            this.updatedIn = Instant.now();
        }
    }

    public void removerPC(ArmadoPC pc) {
        if (this.pcS.remove(pc)) {
            this.totalCost -= pc.getTotalPrice();
            this.updatedIn = Instant.now();
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

    public Instant getCreatedIn() {
        return createdIn;
    }

    public void setCreatedIn(Instant createdIn) {
        this.createdIn = createdIn;
    }

    public Instant getUpdatedIn() {
        return updatedIn;
    }

    public void setUpdatedIn(Instant updatedIn) {
        this.updatedIn = updatedIn;
    }
}
