package objetosNegocio.mappers;

import entidades.PedidoEntidad;
import objetosNegocio.PedidoBO;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

/**
 * Este es el "Adaptador de Datos" que traduce de "Idioma Base de Datos" a "Idioma Negocio".
 */
public class PedidoMapper {

    /**
     * Convierte una PedidoEntidad a PedidoBO.
     */
    public static PedidoBO entidadABO(PedidoEntidad entidad) {
        if (entidad == null) {
            return null;
        }

        PedidoBO bo = new PedidoBO();

        bo.setId(entidad.getId() != null ? entidad.getId().toString() : null);
        bo.setClienteId(entidad.getClienteId());

        if (entidad.getItems() != null) {
            List<PedidoBO.ItemPedido> itemsBo = new ArrayList<>();
            for (PedidoEntidad.ItemPedido itemEntidad : entidad.getItems()) {
                itemsBo.add(convertirItemEntidadABO(itemEntidad));
            }
            bo.setItems(itemsBo);
        }

        bo.setTotal(entidad.getTotal());

        if (entidad.getEstado() != null) {
            try {
                bo.setEstado(PedidoBO.EstadoPedido.valueOf(entidad.getEstado().name()));
            } catch (IllegalArgumentException e) {
                bo.setEstado(PedidoBO.EstadoPedido.PENDIENTE);
            }
        }

        bo.setFechaCreacion(entidad.getFechaCreacion());

        if (entidad.getMetodoPago() != null) {
            bo.setMetodoPago(convertirMetodoPagoEntidadABO(entidad.getMetodoPago()));
        }

        if (entidad.getDireccionEntrega() != null) {
            bo.setDireccionEntrega(convertirDireccionEntidadABO(entidad.getDireccionEntrega()));
        }

        return bo;
    }

    /**
     * Convierte un PedidoBO a PedidoEntidad.
     */
    public static PedidoEntidad boAEntidad(PedidoBO bo) {
        if (bo == null) {
            return null;
        }

        PedidoEntidad entidad = new PedidoEntidad();

        if (bo.getId() != null) {
            entidad.setId(new ObjectId(bo.getId()));
        }

        entidad.setClienteId(bo.getClienteId());

        if (bo.getItems() != null) {
            List<PedidoEntidad.ItemPedido> itemsEntidad = new ArrayList<>();
            for (PedidoBO.ItemPedido itemBo : bo.getItems()) {
                itemsEntidad.add(convertirItemBOAEntidad(itemBo));
            }
            entidad.setItems(itemsEntidad);
        }

        entidad.setTotal(bo.getTotal());

        if (bo.getEstado() != null) {
            try {
                entidad.setEstado(PedidoEntidad.EstadoPedido.valueOf(bo.getEstado().name()));
            } catch (IllegalArgumentException e) {
                entidad.setEstado(PedidoEntidad.EstadoPedido.PENDIENTE);
            }
        }

        entidad.setFechaCreacion(bo.getFechaCreacion());

        if (bo.getMetodoPago() != null) {
            entidad.setMetodoPago(convertirMetodoPagoBOAEntidad(bo.getMetodoPago()));
        }

        if (bo.getDireccionEntrega() != null) {
            entidad.setDireccionEntrega(convertirDireccionBOAEntidad(bo.getDireccionEntrega()));
        }

        return entidad;
    }

    private static PedidoBO.ItemPedido convertirItemEntidadABO(PedidoEntidad.ItemPedido itemEntidad) {
        if (itemEntidad == null) return null;

        PedidoBO.ItemPedido itemBo = new PedidoBO.ItemPedido();
        itemBo.setProductoId(itemEntidad.getProductoId());
        itemBo.setNombre(itemEntidad.getNombre());
        itemBo.setCantidad(itemEntidad.getCantidad());
        itemBo.setPrecioUnitario(itemEntidad.getPrecioUnitario());

        return itemBo;
    }

    private static PedidoEntidad.ItemPedido convertirItemBOAEntidad(PedidoBO.ItemPedido itemBo) {
        if (itemBo == null) return null;

        PedidoEntidad.ItemPedido itemEntidad = new PedidoEntidad.ItemPedido();
        itemEntidad.setProductoId(itemBo.getProductoId());
        itemEntidad.setNombre(itemBo.getNombre());
        itemEntidad.setCantidad(itemBo.getCantidad());
        itemEntidad.setPrecioUnitario(itemBo.getPrecioUnitario());

        return itemEntidad;
    }

    private static PedidoBO.MetodoPagoInfo convertirMetodoPagoEntidadABO(PedidoEntidad.MetodoPagoInfo entidad) {
        if (entidad == null) return null;

        PedidoBO.MetodoPagoInfo bo = new PedidoBO.MetodoPagoInfo();
        bo.setTipo(entidad.getTipo());
        bo.setDetalles(entidad.getDetalles());

        return bo;
    }

    private static PedidoEntidad.MetodoPagoInfo convertirMetodoPagoBOAEntidad(PedidoBO.MetodoPagoInfo bo) {
        if (bo == null) return null;

        PedidoEntidad.MetodoPagoInfo entidad = new PedidoEntidad.MetodoPagoInfo();
        entidad.setTipo(bo.getTipo());
        entidad.setDetalles(bo.getDetalles());

        return entidad;
    }

    private static PedidoBO.DireccionEntrega convertirDireccionEntidadABO(PedidoEntidad.DireccionEntrega entidad) {
        if (entidad == null) return null;

        PedidoBO.DireccionEntrega bo = new PedidoBO.DireccionEntrega();
        bo.setCalle(entidad.getCalle());
        bo.setCiudad(entidad.getCiudad());
        bo.setEstado(entidad.getEstado());
        bo.setCodigoPostal(entidad.getCodigoPostal());

        return bo;
    }

    private static PedidoEntidad.DireccionEntrega convertirDireccionBOAEntidad(PedidoBO.DireccionEntrega bo) {
        if (bo == null) return null;

        PedidoEntidad.DireccionEntrega entidad = new PedidoEntidad.DireccionEntrega();
        entidad.setCalle(bo.getCalle());
        entidad.setCiudad(bo.getCiudad());
        entidad.setEstado(bo.getEstado());
        entidad.setCodigoPostal(bo.getCodigoPostal());

        return entidad;
    }
}

