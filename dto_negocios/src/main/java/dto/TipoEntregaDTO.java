package dto;

public class TipoEntregaDTO {
    public enum Tipo{
        RETIRO_TIENDA,
        ENVIO_ESTANDAR
    }

    private Tipo tipo;
    private double costoAdicional;
    private String direccion;

    public TipoEntregaDTO() {
    }

    public TipoEntregaDTO(double costoAdicional, Tipo tipo) {
        this.costoAdicional = costoAdicional;
        this.tipo = tipo;
    }

    public Tipo getTipo() {
        return tipo;
    }

    public void setTipo(Tipo tipo) {
        this.tipo = tipo;
    }

    public double getCostoAdicional() {
        return costoAdicional;
    }

    public void setCostoAdicional(double costoAdicional) {
        this.costoAdicional = costoAdicional;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }
}
