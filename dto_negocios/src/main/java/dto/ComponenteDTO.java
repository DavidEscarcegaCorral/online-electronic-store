package dto;

public class ComponenteDTO {
    private String id;
    private String nombre;
    private double precioCompra;
    private double costoCompra;
    private String categoria;
    private String imagenUrl;

    // Atributos para validar compatibilidad
    private String socket;
    private String tipoRam;
    private String formFactor;
    private int watts;

    public ComponenteDTO() {

    }

    public ComponenteDTO(String id, String nombre, double precioCompra, double costoCompra, String categoria, String imagenUrl) {
        this.id = id;
        this.nombre = nombre;
        this.precioCompra = precioCompra;
        this.costoCompra = costoCompra;
        this.categoria = categoria;
        this.imagenUrl = imagenUrl;
    }

    public String getId() { return id; }
    public String getNombre() { return nombre; }
    public double getPrecioCompra() { return precioCompra; }
    public String getCategoria() { return categoria; }


    public String getSocket() { return socket; }
    public void setSocket(String socket) { this.socket = socket; }

    public String getTipoRam() { return tipoRam; }
    public void setTipoRam(String tipoRam) { this.tipoRam = tipoRam; }

    public String getFormFactor() { return formFactor; }
    public void setFormFactor(String formFactor) { this.formFactor = formFactor; }

    public int getWatts() { return watts; }
    public void setWatts(int watts) { this.watts = watts; }
}
