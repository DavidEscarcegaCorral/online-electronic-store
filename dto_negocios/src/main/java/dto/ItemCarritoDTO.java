package dto;

/**
 * Item de carrito que contiene referencia a producto y cantidad.
 */
public class ItemCarritoDTO {
    private String productoId;
    private String nombre;
    private int cantidad;
    private double precioUnitario;

    public ItemCarritoDTO() {}

    public String getProductoId() { return productoId; }
    public void setProductoId(String productoId) { this.productoId = productoId; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public int getCantidad() { return cantidad; }
    public void setCantidad(int cantidad) { this.cantidad = cantidad; }

    public double getPrecioUnitario() { return precioUnitario; }
    public void setPrecioUnitario(double precioUnitario) { this.precioUnitario = precioUnitario; }

    public double getSubtotal() { return this.cantidad * this.precioUnitario; }
}
