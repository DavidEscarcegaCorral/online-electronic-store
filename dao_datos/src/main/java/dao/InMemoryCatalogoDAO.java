package dao;

import dto.ComponenteDTO;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Implementación en memoria simple del catálogo de componentes.
 */
public class InMemoryCatalogoDAO implements ICatalogoDAO {
    private final List<ComponenteDTO> datos = new ArrayList<>();

    public InMemoryCatalogoDAO() {
        // Seed básico
        ComponenteDTO m1 = new ComponenteDTO();
        m1.setId("cpu-intel-i5"); m1.setNombre("Intel i5"); m1.setCategoria("Procesador"); m1.setSocket("LGA1200"); m1.setPrecio(200);
        ComponenteDTO m2 = new ComponenteDTO();
        m2.setId("mb-asus-atx"); m2.setNombre("ASUS Prime ATX"); m2.setCategoria("Tarjeta Madre"); m2.setSocket("LGA1200"); m2.setFormFactor("ATX"); m2.setTipoRam("DDR4"); m2.setPrecio(150);
        ComponenteDTO m3 = new ComponenteDTO();
        m3.setId("ram-8gb-ddr4"); m3.setNombre("8GB DDR4"); m3.setCategoria("RAM"); m3.setTipoRam("DDR4"); m3.setPrecio(40);
        ComponenteDTO g1 = new ComponenteDTO();
        g1.setId("gab-micro"); g1.setNombre("Gabinete Micro-ATX"); g1.setCategoria("Gabinete"); g1.setFormFactor("Micro-ATX"); g1.setPrecio(60);

        datos.add(m1); datos.add(m2); datos.add(m3); datos.add(g1);
    }

    @Override
    public List<ComponenteDTO> findByCategoria(String categoria) {
        return datos.stream().filter(d -> categoria.equals(d.getCategoria())).collect(Collectors.toList());
    }

    @Override
    public Optional<ComponenteDTO> findById(String id) {
        return datos.stream().filter(d -> id.equals(d.getId())).findFirst();
    }
}
