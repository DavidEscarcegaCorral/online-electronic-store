package objetosNegocio.pedidoON;

public class PedidoON implements IPedidoON {
    private String id;
    private String clienteId;
    private Double total;
    private String estado;

    public PedidoON() {}

    public PedidoON(String id, String clienteId, Double total, String estado) {
        this.id = id;
        this.clienteId = clienteId;
        this.total = total;
        this.estado = estado;
    }

    @Override
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String getClienteId() {
        return clienteId;
    }

    public void setClienteId(String clienteId) {
        this.clienteId = clienteId;
    }

    @Override
    public Double getTotal() {
        return total;
    }

    public void setTotal(Double total) {
        this.total = total;
    }

    @Override
    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }
}

