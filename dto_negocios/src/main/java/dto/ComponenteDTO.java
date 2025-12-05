package dto;

/**
 * DTO que representa un componente de hardware.
 */
public class ComponenteDTO {
    private String id;
    private String nombre;
    private double precio;
    private String categoria;
    private String socket;
    private String tipoRam;
    private String formFactor;
    private int watts;

    public ComponenteDTO() {}

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public double getPrecio() { return precio; }
    public void setPrecio(double precio) { this.precio = precio; }

    public String getCategoria() { return categoria; }
    public void setCategoria(String categoria) { this.categoria = categoria; }

    public String getSocket() { return socket; }
    public void setSocket(String socket) { this.socket = socket; }

    public String getTipoRam() { return tipoRam; }
    public void setTipoRam(String tipoRam) { this.tipoRam = tipoRam; }

    public String getFormFactor() { return formFactor; }
    public void setFormFactor(String formFactor) { this.formFactor = formFactor; }

    public int getWatts() { return watts; }
    public void setWatts(int watts) { this.watts = watts; }
}
