package dto;

/**
 * DTO para metodo de pago (mock sencillo).
 */
public class MetodoPagoDTO {
    public enum Tipo {TARJETA, TRANSFERENCIA, EFECTIVO}

    private Tipo tipo;
    private String detalles;

    public MetodoPagoDTO() {
    }

    public Tipo getTipo() {
        return tipo;
    }

    public void setTipo(Tipo tipo) {
        this.tipo = tipo;
    }

    public String getDetalles() {
        return detalles;
    }

    public void setDetalles(String detalles) {
        this.detalles = detalles;
    }
}
