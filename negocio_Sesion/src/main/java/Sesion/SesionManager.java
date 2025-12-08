package Sesion;

import dto.ClienteDTO;
import dto.CarritoDTO;
import dto.EnsamblajeDTO;
import dto.TipoEntregaDTO;

public class SesionManager {
    private static SesionManager instance;

    private ClienteDTO clienteActual;
    private CarritoDTO carritoActual;
    private EnsamblajeDTO ensamblajeActual;
    private TipoEntregaDTO tipoEntregaActual;

    public SesionManager() {
        this.tipoEntregaActual= new  TipoEntregaDTO(
                0.0,
                TipoEntregaDTO.Tipo.RETIRO_TIENDA
        );
    }

    public static synchronized SesionManager getInstance() {
        if(instance == null) {
            instance = new SesionManager();
        }
        return instance;
    }

    //CLIENTE

    public void setClienteActual(ClienteDTO cliente){
        this.clienteActual = cliente;
    }
    public ClienteDTO getClienteActual() {
        return this.clienteActual;
    }

    public boolean haySesionActiva(){
        return this.clienteActual != null;
    }

    public String getClienteId(){
        return this.clienteActual != null ? this.clienteActual.getId() : null;
    }

    //CARRITO

    public void setCarritoActual(CarritoDTO carrito){
        this.carritoActual = carrito;
    }

    public CarritoDTO getCarritoActual(){
        return this.carritoActual;
    }

    public int getCantidadItemsCarrito(){
        if(this.carritoActual == null || this.carritoActual.getItems()==null || this.carritoActual.getItems().isEmpty()){
            return 0;
        }

        return this.carritoActual.getItems().size();
    }

    public double getTotalCarrito(){
        return this.carritoActual != null ? this.carritoActual.getTotal() : 0.0;
    }

    public void limpiarCarrito(){
        this.carritoActual = null;
    }

    //ENSAMBLE

    public void setEnsamblajeActual(EnsamblajeDTO ensamblaje){
        this.ensamblajeActual = ensamblaje;
    }

    public EnsamblajeDTO getEnsamblajeActual(){
        return this.ensamblajeActual;
    }

    public boolean hayEnsamblajeActivo(){
        return this.ensamblajeActual != null &&
                this.ensamblajeActual.getComponentes() != null &&
                !this.ensamblajeActual.getComponentes().isEmpty();
    }

    public void limpiarEnsamblaje(){
        this.ensamblajeActual = null;
    }

    //ENTREGA

    public void setTipoEntrega(TipoEntregaDTO tipoEntrega){
        this.tipoEntregaActual=tipoEntrega;
    }

    public TipoEntregaDTO getTipoEntrega(){
        return this.tipoEntregaActual;
    }

    public double getCostoEnvio(){
        return this.tipoEntregaActual != null ?
                this.tipoEntregaActual.getCostoAdicional() : 0.0;
    }

    public double getTotalFinal(){
        return getTotalCarrito() + getCostoEnvio();
    }

    public void cerrarSesion(){
        this.clienteActual=null;
        this.carritoActual=null;
        this.ensamblajeActual=null;
        this.tipoEntregaActual=new  TipoEntregaDTO(
                0.0,
                TipoEntregaDTO.Tipo.RETIRO_TIENDA
        );
    }
}
