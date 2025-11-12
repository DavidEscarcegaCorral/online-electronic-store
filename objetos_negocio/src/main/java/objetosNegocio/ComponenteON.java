package objetosNegocio;

import dto.ComponenteDTO;

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


    }
}
