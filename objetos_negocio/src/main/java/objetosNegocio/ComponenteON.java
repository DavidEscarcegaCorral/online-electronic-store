package objetosnegocio;

import dto.ComponenteDTO;
import objetosnegocio.interfaces.IComponenteON;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ComponenteON implements IComponenteON {

    private static ComponenteON componenteON;

    /**
     * Metodo estático para obtener la única instancia de la clase.
     */
    public static synchronized ComponenteON getInstance() {
        if (componenteON == null) {
            componenteON = new ComponenteON();
        }
        return componenteON;
    }

    /**
     * Se llama solo una vez y carga los datos de prueba.
     */
    private ComponenteON() {
        this.listaComponentes = new ArrayList<>();
        cargarDatosDePrueba(); // Llena nuestra "BD"
    }

    private List<ComponenteDTO> listaComponentes;

    @Override
    public List<ComponenteDTO> obtenerTodos() {
        return this.listaComponentes;
    }

    @Override
    public List<ComponenteDTO> obtenerPorCategoria(String categoria) {
        return this.listaComponentes.stream()
                .filter(componente -> componente.getCategoria().equalsIgnoreCase(categoria))
                .collect(Collectors.toList());
    }

    @Override
    public ComponenteDTO obtenerPorId(String id) {
        return this.listaComponentes.stream()
                .filter(componente -> componente.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    // Cargar datos (Mock)
    private void cargarDatosDePrueba() {

        // Procesadores
        ComponenteDTO intelI5 = new ComponenteDTO();
        intelI5.setId("CPU-IN-11600K");
        intelI5.setNombre("Intel Core i5-11600K");
        intelI5.setPrecio(220.0);
        intelI5.setCategoria("Procesador");
        intelI5.setSocket("LGA1200");
        intelI5.setTipoRam("DDR4");

        ComponenteDTO intelI7 = new ComponenteDTO();
        intelI7.setId("CPU-IN-11700K");
        intelI7.setNombre("Intel Core i7-11700K");
        intelI7.setPrecio(350.0);
        intelI7.setCategoria("Procesador");
        intelI7.setSocket("LGA1200");
        intelI7.setTipoRam("DDR4");

        ComponenteDTO amdRyzen5 = new ComponenteDTO();
        amdRyzen5.setId("CPU-AMD-5600X");
        amdRyzen5.setNombre("AMD Ryzen 5 5600X");
        amdRyzen5.setPrecio(200.0);
        amdRyzen5.setCategoria("Procesador");
        amdRyzen5.setSocket("AM4");
        amdRyzen5.setTipoRam("DDR4");

        // Placas madre
        ComponenteDTO moboLGA = new ComponenteDTO();
        moboLGA.setId("MB-ASUS-LGA1200");
        moboLGA.setNombre("ASUS PRIME Z590 (LGA1200)");
        moboLGA.setPrecio(180.0);
        moboLGA.setCategoria("Tarjeta Madre");
        moboLGA.setSocket("LGA1200");
        moboLGA.setTipoRam("DDR4");
        moboLGA.setFormFactor("ATX");

        ComponenteDTO moboAM4 = new ComponenteDTO();
        moboAM4.setId("MB-MSI-AM4");
        moboAM4.setNombre("MSI B550 (AM4)");
        moboAM4.setPrecio(140.0);
        moboAM4.setCategoria("Tarjeta Madre");
        moboAM4.setSocket("AM4");
        moboAM4.setTipoRam("DDR4");
        moboAM4.setFormFactor("ATX");

        // RAM
        ComponenteDTO ram8 = new ComponenteDTO();
        ram8.setId("RAM-8GB-DDR4");
        ram8.setNombre("Corsair Vengeance 8GB DDR4");
        ram8.setPrecio(45.0);
        ram8.setCategoria("Memoria RAM");
        ram8.setTipoRam("DDR4");

        // GPU
        ComponenteDTO gpu = new ComponenteDTO();
        gpu.setId("GPU-RTX3060");
        gpu.setNombre("NVIDIA RTX 3060 12GB");
        gpu.setPrecio(329.0);
        gpu.setCategoria("Tarjeta de video");

        // Añadir a la 'base de datos' mock
        this.listaComponentes.add(intelI5);
        this.listaComponentes.add(intelI7);
        this.listaComponentes.add(amdRyzen5);
        this.listaComponentes.add(moboLGA);
        this.listaComponentes.add(moboAM4);
        this.listaComponentes.add(ram8);
        this.listaComponentes.add(gpu);

    }
}
