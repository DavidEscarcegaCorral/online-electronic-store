package objetosNegocio.carritoON;

import java.util.List;

public class CarritoON implements ICarritoON {
    private String id;
    private String clienteId;
    private List<Object> configuracionesIds;

    public CarritoON() {}

    public CarritoON(String id, String clienteId, List<Object> configuracionesIds) {
        this.id = id;
        this.clienteId = clienteId;
        this.configuracionesIds = configuracionesIds;
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
    public List<Object> getConfiguracionesIds() {
        return configuracionesIds;
    }

    public void setConfiguracionesIds(List<Object> configuracionesIds) {
        this.configuracionesIds = configuracionesIds;
    }
}

