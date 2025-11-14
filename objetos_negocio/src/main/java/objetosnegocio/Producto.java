package objetosnegocio;

public abstract class Producto {
    protected Long id;
    protected String nombre;
    protected Double precioVenta;
    protected Double costoCompra;
    protected Double cantidadEnInventario;

    public Producto() {
    }

    public Producto(Long id, String nombre, Double precioVenta, Double costoCompra, Double cantidadEnInventario) {
        this.id = id;
        this.nombre = nombre;
        this.precioVenta = precioVenta;
        this.costoCompra = costoCompra;
        this.cantidadEnInventario = cantidadEnInventario;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Double getPrecioVenta() {
        return precioVenta;
    }

    public void setPrecioVenta(Double precioVenta) {
        this.precioVenta = precioVenta;
    }

    public Double getCostoCompra() {
        return costoCompra;
    }

    public void setCostoCompra(Double costoCompra) {
        this.costoCompra = costoCompra;
    }

    public Double getCantidadEnInventario() {
        return cantidadEnInventario;
    }

    public void setCantidadEnInventario(Double cantidadEnInventario) {
        this.cantidadEnInventario = cantidadEnInventario;
    }
}


