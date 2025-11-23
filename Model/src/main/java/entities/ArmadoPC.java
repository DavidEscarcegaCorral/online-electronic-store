package entities;

import org.bson.codecs.pojo.annotations.BsonProperty;
import org.bson.types.ObjectId;

import java.time.Instant;
import java.util.List;

public class ArmadoPC {
    private ObjectId _id;
    private List<Componentes> components;

    private Integer totalPrice;

    @BsonProperty("created_In")
    private Instant created_In;
    @BsonProperty("updated_In")
    private Instant updated_In;

    public ArmadoPC() {
    }

    public ArmadoPC(ObjectId _id, List<Componentes> components) {
        this._id = _id;
        this.components = components;

        for(Componentes c : components){
            this.totalPrice += c.getPrice();
        }
    }

    public ObjectId get_id() {
        return _id;
    }

    public void set_id(ObjectId _id) {
        this._id = _id;
    }

    public List<Componentes> getComponents() {
        return components;
    }

    public void setComponents(List<Componentes> components) {
        this.components = components;
    }

    public Integer getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(Integer totalPrice) {
        this.totalPrice = totalPrice;
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
