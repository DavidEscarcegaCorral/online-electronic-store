package entities;

import org.bson.types.ObjectId;
import org.bson.codecs.pojo.annotations.BsonProperty;

import java.time.Instant;

public class Cliente {

    private ObjectId _id;
    private String name;
    private String paternalLastName;
    private String maternalLastName;
    private String email;
    private String address;
    private Carrito car;

    @BsonProperty("created_In")
    private Instant createdIn;

    @BsonProperty("updated_In")
    private Instant updatedIn;

    public Cliente() {
    }

    public Cliente(ObjectId _id, String name, String paternalLastName, String maternalLastName, String email, String address, Carrito car) {
        this._id = _id;
        this.name = name;
        this.paternalLastName = paternalLastName;
        this.maternalLastName = maternalLastName;
        this.email = email;
        this.address = address;
        this.car = car;
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

    public String getPaternalLastName() {
        return paternalLastName;
    }

    public void setPaternalLastName(String paternalLastName) {
        this.paternalLastName = paternalLastName;
    }

    public String getMaternalLastName() {
        return maternalLastName;
    }

    public void setMaternalLastName(String maternalLastName) {
        this.maternalLastName = maternalLastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Carrito getCar() {
        return car;
    }

    public void setCar(Carrito car) {
        this.car = car;
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
