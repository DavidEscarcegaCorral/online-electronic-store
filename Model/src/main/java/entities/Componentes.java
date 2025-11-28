package entities;

import org.bson.codecs.pojo.annotations.BsonProperty;
import org.bson.types.ObjectId;

import java.time.Instant;

public class Componentes {
    private ObjectId _id;
    private String name;
    private String description;
    private Integer price;
    private Integer Stock;
    private TipoComponente componentType;
    @BsonProperty("created_In")
    private Instant created_In;

    @BsonProperty("updated_In")
    private Instant updated_In;

    public Componentes() {
    }

    public Componentes(ObjectId _id, String name, String description, Integer price, Integer stock, TipoComponente componentType) {
        this._id = _id;
        this.name = name;
        this.description = description;
        this.price = price;
        Stock = stock;
        this.componentType = componentType;
    }

    public ObjectId get_id() {
        return _id;
    }

    public void set_id(ObjectId _id) {
        this._id = _id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public Integer getStock() {
        return Stock;
    }

    public void setStock(Integer stock) {
        Stock = stock;
    }

    public TipoComponente getComponentType() {
        return componentType;
    }

    public void setComponentType(TipoComponente componentType) {
        this.componentType = componentType;
    }
}
