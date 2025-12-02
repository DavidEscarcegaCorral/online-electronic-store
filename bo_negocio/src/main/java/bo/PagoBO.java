package bo;

import dto.CompraDTO;
import dto.MetodoPagoDTO;

/**
 * Lógica de negocio para procesar pagos (mock).
 */
public class PagoBO {
    public boolean procesarPagoMock(MetodoPagoDTO metodo, CompraDTO compra) {
        // Lógica mock: siempre acepta tarjetas con detalle no vacío
        if (metodo == null) return false;
        if (metodo.getTipo() == MetodoPagoDTO.Tipo.TARJETA && (metodo.getDetalles() == null || metodo.getDetalles().isEmpty())) {
            return false;
        }
        return true;
    }
}
