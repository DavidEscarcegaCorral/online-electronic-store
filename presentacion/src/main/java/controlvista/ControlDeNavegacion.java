package controlvista;

import compartido.FramePrincipal;
import armadoPC.ArmarPcPanel;
import carrito.CarritoPanel;
import compartido.BarraNavegacion;
import menuprincipal.MenuPrincipalPanel;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import dao.ProductoDAO;
import fachada.ArmadoFacade;
import fachada.IArmadoFacade;

public class ControlDeNavegacion {
    private static final int INDICE_PROCESADOR = 2;
    private static final int INDICE_TARJETA_MADRE = 3;
    private static final int INDICE_MEMORIA_RAM = 4;
    private static final int INDICE_ALMACENAMIENTO = 5;
    private static final int INDICE_UNIDAD_SSD = 6;
    private static final int INDICE_TARJETA_VIDEO = 7;
    private static final int INDICE_FUENTE_PODER = 8;
    private static final int INDICE_DISIPADOR = 9;
    private static final int INDICE_VENTILADOR = 10;
    private static final int INDICE_MONITOR = 11;
    private static final int INDICE_KIT_TECLADO_RATON = 12;
    private static final int INDICE_RED = 13;

    private final FramePrincipal framePrincipal;
    private final MenuPrincipalPanel menuPrincipalPanel;
    private final ArmarPcPanel armarEquipoPantalla;
    private final CarritoPanel carritoPantalla;

    private int indiceActual = 0;

    public ControlDeNavegacion(FramePrincipal framePrincipal) {
        this.framePrincipal = framePrincipal;
        this.menuPrincipalPanel = new MenuPrincipalPanel();
        this.armarEquipoPantalla = new ArmarPcPanel();
        this.carritoPantalla = new CarritoPanel();

        inicializarVista();
        configurarBarraNavegacion();
        configurarBotonesComponentes();
        configurarNavegacionInterna();
        configurarCallbacksNegocio();
    }

    private void inicializarVista() {
        framePrincipal.setPanelContenido(menuPrincipalPanel);
        framePrincipal.setVisible(true);

        armarEquipoPantalla.mostrarMenusLaterales();
        habilitarMenusLaterales(false);
    }

    private void configurarBarraNavegacion() {
        BarraNavegacion barra = framePrincipal.getBarraNavegacion();

        barra.getBoton().addActionListener(e -> mostrarNuevaPantalla(menuPrincipalPanel));

        barra.getArmarPcLbl().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                mostrarNuevaPantalla(armarEquipoPantalla);
                mostrarPasoInicialArmar();
            }
        });

        barra.getCarritoLbl().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                mostrarNuevaPantalla(carritoPantalla);
            }
        });
    }

    private void configurarBotonesComponentes() {
        var menuComponentes = armarEquipoPantalla.getMenuComponentesPanel();
        if (menuComponentes == null) return;

        menuComponentes.getProcesadorBtn()
            .addActionListener(e -> navegarAIndice(INDICE_PROCESADOR));

        menuComponentes.getTarjetaMadreBtn()
            .addActionListener(e -> navegarAIndice(INDICE_TARJETA_MADRE));

        menuComponentes.getMemoriaRAMBtn()
            .addActionListener(e -> navegarAIndice(INDICE_MEMORIA_RAM));

        menuComponentes.getAlmacenamientoBtn()
            .addActionListener(e -> navegarAIndice(INDICE_ALMACENAMIENTO));

        menuComponentes.getUnidadSSDBtn()
            .addActionListener(e -> navegarAIndice(INDICE_UNIDAD_SSD));

        menuComponentes.getTarjetaDeVideoBtn()
            .addActionListener(e -> navegarAIndice(INDICE_TARJETA_VIDEO));

        menuComponentes.getFuenteDePoderBtn()
            .addActionListener(e -> navegarAIndice(INDICE_FUENTE_PODER));

        menuComponentes.getDisipadorBtn()
            .addActionListener(e -> navegarAIndice(INDICE_DISIPADOR));

        menuComponentes.getVentiladorBtn()
            .addActionListener(e -> navegarAIndice(INDICE_VENTILADOR));

        menuComponentes.getMonitorBtn()
            .addActionListener(e -> navegarAIndice(INDICE_MONITOR));

        menuComponentes.getKitTecladoRatonBtn()
            .addActionListener(e -> navegarAIndice(INDICE_KIT_TECLADO_RATON));

        menuComponentes.getRedBtn()
            .addActionListener(e -> navegarAIndice(INDICE_RED));
    }

    private void configurarNavegacionInterna() {
        var siguiente = armarEquipoPantalla.getContinuarBtn();
        var retroceder = armarEquipoPantalla.getRetrocederBtn();
        if (siguiente != null) {
            siguiente.addActionListener(e -> avanzarPaso());
        }
        if (retroceder != null) {
            retroceder.addActionListener(e -> retrocederPaso());
        }
    }

    private void configurarCallbacksNegocio() {
        armarEquipoPantalla.setOnProductoSelected(productoId -> {
            try {
                dto.ComponenteDTO componente = convertirAComponenteDTO(productoId);
                if (componente == null) {
                    JOptionPane.showMessageDialog(framePrincipal, "Producto no encontrado", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                IArmadoFacade armadoFacade = ArmadoFacade.getInstance();
                java.util.List<String> errores = armadoFacade.agregarComponente(componente);
                if (!errores.isEmpty()) {
                    JOptionPane.showMessageDialog(framePrincipal, String.join("\n", errores), "Error de compatibilidad", JOptionPane.ERROR_MESSAGE);
                } else {
                    armarEquipoPantalla.updateResumen(armadoFacade.getEnsamblajeActual());
                    var btn = armarEquipoPantalla.getContinuarBtn();
                    if (btn != null) btn.setEnabled(true);
                    habilitarMenusLaterales(true);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(framePrincipal, "Error procesando selección: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
    }

    private void mostrarNuevaPantalla(JPanel nuevoPanel) {
        framePrincipal.setPanelContenido(nuevoPanel);
        if (nuevoPanel == armarEquipoPantalla) {
            armarEquipoPantalla.mostrarMenusLaterales();
            var contBtn = armarEquipoPantalla.getContinuarBtn();
            if (contBtn != null) contBtn.setEnabled(true);
            habilitarMenusLaterales(false);
        }
    }

    private void mostrarPasoInicialArmar() {
        String[] pasos = armarEquipoPantalla.getListaPasosArmado();
        if (pasos != null && pasos.length > 0) {
            indiceActual = 0;
            armarEquipoPantalla.getCardLayout().show(armarEquipoPantalla.getCardsPanel(), pasos[0]);
            var cont = armarEquipoPantalla.getContinuarBtn();
            if (cont != null) cont.setEnabled(true);
            habilitarMenusLaterales(false);
        }
    }

    private void navegarAIndice(int nuevoIndice) {
        if (!esIndiceValido(nuevoIndice)) return;

        armarEquipoPantalla.mostrarMenusLaterales();
        habilitarMenusLaterales(nuevoIndice >= 2);

        String[] pasos = armarEquipoPantalla.getListaPasosArmado();
        indiceActual = nuevoIndice;
        armarEquipoPantalla.getCardLayout().show(armarEquipoPantalla.getCardsPanel(), pasos[nuevoIndice]);

        var contBtn = armarEquipoPantalla.getContinuarBtn();
        if (contBtn != null) {
            contBtn.setEnabled(nuevoIndice <= 1);
        }

        String paso = pasos[nuevoIndice];
        int maxIndice = pasos.length - 1;
        if (nuevoIndice >= 2 && nuevoIndice < maxIndice) {
            armarEquipoPantalla.cargarCatalogo(paso);
        }
    }

    private dto.ComponenteDTO convertirAComponenteDTO(String productoId) {
        try {
            ProductoDAO productoDAO = new ProductoDAO();
            entidades.ProductoEntidad producto = productoDAO.obtenerPorId(productoId);
            if (producto == null) return null;
            dto.ComponenteDTO dto = new dto.ComponenteDTO();
            dto.setId(producto.getId().toString());
            dto.setNombre(producto.getNombre());
            dto.setPrecio(producto.getPrecio());
            dto.setCategoria(producto.getCategoria());
            dto.setMarca(producto.getMarca());
            if (producto.getEspecificaciones() != null) {
                dto.setSocket(producto.getEspecificaciones().get("socket"));
                dto.setTipoRam(producto.getEspecificaciones().get("tipoRam"));
                dto.setFormFactor(producto.getEspecificaciones().get("formFactor"));
            }
            return dto;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    private boolean esIndiceValido(int indice) {
        String[] pasos = armarEquipoPantalla.getListaPasosArmado();
        return pasos != null && indice >= 0 && indice < pasos.length;
    }

    private void avanzarPaso() {
        int siguiente = Math.min(indiceActual + 1, armarEquipoPantalla.getListaPasosArmado().length - 1);
        navegarAIndice(siguiente);
    }

    private void retrocederPaso() {
        int anterior = Math.max(indiceActual - 1, 0);
        navegarAIndice(anterior);
    }

    private void habilitarMenusLaterales(boolean habilitar) {
        var menu = armarEquipoPantalla.getMenuComponentesPanel();
        if (menu == null) return;

        menu.getProcesadorBtn().setEnabled(habilitar);
        menu.getTarjetaMadreBtn().setEnabled(habilitar);
        menu.getMemoriaRAMBtn().setEnabled(habilitar);
        menu.getAlmacenamientoBtn().setEnabled(habilitar);
        menu.getUnidadSSDBtn().setEnabled(habilitar);
        menu.getTarjetaDeVideoBtn().setEnabled(habilitar);
        menu.getFuenteDePoderBtn().setEnabled(habilitar);
        menu.getDisipadorBtn().setEnabled(habilitar);
        menu.getVentiladorBtn().setEnabled(habilitar);
        menu.getMonitorBtn().setEnabled(habilitar);
        menu.getKitTecladoRatonBtn().setEnabled(habilitar);
        menu.getRedBtn().setEnabled(habilitar);
    }
}
