package entities;

import org.bson.codecs.pojo.annotations.BsonProperty;
import org.bson.types.ObjectId;

import java.time.Instant;
import java.util.Date;

public class Producto {
    private ObjectId _id;
    private String name;
    private String description;
    private Integer stock;
    private Integer price;

    @BsonProperty("created_In")
    private Instant created_In;

    @BsonProperty("updated_In")
    private Instant updated_In;

    public Producto() {
    }

    public Producto(ObjectId _id, String name, String description, Integer stock, Integer price) {
        this._id = _id;
        this.name = name;
        this.description = description;
        this.stock = stock;
        this.price = price;
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

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
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
