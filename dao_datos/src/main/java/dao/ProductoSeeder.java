package dao;

import com.mongodb.client.MongoCollection;
import conexion.ConexionMongoDB;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Clase para insertar productos masivamente en la base de datos MongoDB.
 */
public class ProductoSeeder {

    private final MongoCollection<Document> coleccion;

    public ProductoSeeder() {
        this.coleccion = ConexionMongoDB.getInstance()
                .getDatabase()
                .getCollection("productos");
    }

    public static void main(String[] args) {
        ProductoSeeder seeder = new ProductoSeeder();

        System.out.println("Iniciando carga de productos...\n");

        // Insertar productos por categoría
        seeder.insertarProcesadores();
//        seeder.insertarTarjetasMadre();
//        seeder.insertarMemoriasRAM();
//        seeder.insertarTarjetasVideo();
//        seeder.insertarAlmacenamiento();
//        seeder.insertarFuentesPoder();
//        seeder.insertarGabinetes();
//        seeder.insertarDisipadores();
//        seeder.insertarVentiladores();
//        seeder.insertarMonitores();
//        seeder.insertarKitsTecladoRaton();
//        seeder.insertarRedes();

        System.out.println();
        System.out.println("Carga de productos completada exitosamente!\n");

        long totalProductos = seeder.coleccion.countDocuments();
        System.out.println("Total de productos en la base de datos: " + totalProductos+"\n");
        System.out.println();
    }

    private void insertarProcesadores() {
        System.out.println("Insertando Procesadores...");
        List<Document> procesadores = new ArrayList<>();

        // Intel
        procesadores.add(crearProducto("Intel Core i5-12400F", "Procesador", "Intel", 179.99, 25,
            "Procesador de 6 núcleos y 12 hilos, 2.5GHz base / 4.4GHz turbo",
            Map.of("socket", "LGA1700", "nucleos", "6", "hilos", "12", "frecuencia", "2.5-4.4 GHz")));

        procesadores.add(crearProducto("Intel Core i7-13700K", "Procesador", "Intel", 409.99, 15,
            "Procesador de 16 núcleos (8P+8E) y 24 hilos, 3.4GHz base / 5.4GHz turbo",
            Map.of("socket", "LGA1700", "nucleos", "16", "hilos", "24", "frecuencia", "3.4-5.4 GHz")));

        procesadores.add(crearProducto("Intel Core i9-13900K", "Procesador", "Intel", 589.99, 10,
            "Procesador de 24 núcleos (8P+16E) y 32 hilos, 3.0GHz base / 5.8GHz turbo",
            Map.of("socket", "LGA1700", "nucleos", "24", "hilos", "32", "frecuencia", "3.0-5.8 GHz")));

        // AMD
        procesadores.add(crearProducto("AMD Ryzen 5 5600X", "Procesador", "AMD", 199.99, 30,
            "Procesador de 6 núcleos y 12 hilos, 3.7GHz base / 4.6GHz turbo",
            Map.of("socket", "AM4", "nucleos", "6", "hilos", "12", "frecuencia", "3.7-4.6 GHz")));

        procesadores.add(crearProducto("AMD Ryzen 7 7700X", "Procesador", "AMD", 349.99, 20,
            "Procesador de 8 núcleos y 16 hilos, 4.5GHz base / 5.4GHz turbo",
            Map.of("socket", "AM5", "nucleos", "8", "hilos", "16", "frecuencia", "4.5-5.4 GHz")));

        procesadores.add(crearProducto("AMD Ryzen 9 7950X", "Procesador", "AMD", 699.99, 12,
            "Procesador de 16 núcleos y 32 hilos, 4.5GHz base / 5.7GHz turbo",
            Map.of("socket", "AM5", "nucleos", "16", "hilos", "32", "frecuencia", "4.5-5.7 GHz")));

        // Intel adicionales
        procesadores.add(crearProducto("Intel Core i5-13600K", "Procesador", "Intel", 289.99, 20,
            "Procesador de 14 núcleos (6P+8E) y 20 hilos, 3.5GHz base / 5.1GHz turbo",
            Map.of("socket", "LGA1700", "nucleos", "14", "hilos", "20", "frecuencia", "3.5-5.1 GHz")));

        procesadores.add(crearProducto("Intel Core i7-12700", "Procesador", "Intel", 349.99, 18,
            "Procesador de 12 núcleos (8P+4E) y 20 hilos, 3.2GHz base / 4.9GHz turbo",
            Map.of("socket", "LGA1700", "nucleos", "12", "hilos", "20", "frecuencia", "3.2-4.9 GHz")));

        procesadores.add(crearProducto("Intel Core i3-13100F", "Procesador", "Intel", 109.99, 40,
            "Procesador de 4 núcleos y 8 hilos, 3.4GHz base / 4.5GHz turbo",
            Map.of("socket", "LGA1700", "nucleos", "4", "hilos", "8", "frecuencia", "3.4-4.5 GHz")));

        // AMD adicionales
        procesadores.add(crearProducto("AMD Ryzen 7 5700X", "Procesador", "AMD", 249.99, 25,
            "Procesador de 8 núcleos y 16 hilos, 3.6GHz base / 4.7GHz turbo",
            Map.of("socket", "AM4", "nucleos", "8", "hilos", "16", "frecuencia", "3.6-4.7 GHz")));

        procesadores.add(crearProducto("AMD Ryzen 5 5500", "Procesador", "AMD", 139.99, 35,
            "Procesador de 6 núcleos y 12 hilos, 3.6GHz base / 4.2GHz turbo",
            Map.of("socket", "AM4", "nucleos", "6", "hilos", "12", "frecuencia", "3.6-4.2 GHz")));

        procesadores.add(crearProducto("AMD Ryzen 9 5900X", "Procesador", "AMD", 449.99, 16,
            "Procesador de 12 núcleos y 24 hilos, 3.7GHz base / 4.7GHz turbo",
            Map.of("socket", "AM4", "nucleos", "12", "hilos", "24", "frecuencia", "3.7-4.7 GHz")));

        procesadores.add(crearProducto("AMD Ryzen 5 7600", "Procesador", "AMD", 229.99, 22,
            "Procesador de 6 núcleos y 12 hilos, 3.8GHz base / 5.3GHz turbo",
            Map.of("socket", "AM5", "nucleos", "6", "hilos", "12", "frecuencia", "3.8-5.3 GHz")));

        procesadores.add(crearProducto("Intel Core i9-13900KS", "Procesador", "Intel", 699.99, 8,
            "Procesador de 24 núcleos (8P+16E) y 32 hilos, 3.2GHz base / 6.0GHz turbo",
            Map.of("socket", "LGA1700", "nucleos", "24", "hilos", "32", "frecuencia", "3.2-6.0 GHz")));

        procesadores.add(crearProducto("AMD Ryzen 7 5800X3D", "Procesador", "AMD", 399.99, 14,
            "Procesador de 8 núcleos y 16 hilos con cache 3D V-Cache, 3.4GHz base / 4.5GHz turbo",
            Map.of("socket", "AM4", "nucleos", "8", "hilos", "16", "frecuencia", "3.4-4.5 GHz")));

        insertarDocumentos(procesadores);
    }

    private void insertarTarjetasMadre() {
        System.out.println("Insertando Tarjetas Madre...");
        List<Document> tarjetas = new ArrayList<>();

        tarjetas.add(crearProducto("ASUS ROG Strix B660-A", "Tarjeta Madre", "ASUS", 189.99, 15,
            "Placa madre ATX para Intel Gen 12/13, DDR5, PCIe 5.0",
            Map.of("socket", "LGA1700", "formFactor", "ATX", "tipoRam", "DDR5", "chipset", "B660")));

        tarjetas.add(crearProducto("MSI MPG Z790 EDGE WIFI", "Tarjeta Madre", "MSI", 329.99, 12,
            "Placa madre ATX para Intel Gen 12/13/14, DDR5, PCIe 5.0, WiFi 6E",
            Map.of("socket", "LGA1700", "formFactor", "ATX", "tipoRam", "DDR5", "chipset", "Z790")));

        tarjetas.add(crearProducto("Gigabyte B550 AORUS ELITE", "Tarjeta Madre", "Gigabyte", 149.99, 20,
            "Placa madre ATX para AMD Ryzen, DDR4, PCIe 4.0",
            Map.of("socket", "AM4", "formFactor", "ATX", "tipoRam", "DDR4", "chipset", "B550")));

        tarjetas.add(crearProducto("ASRock X670E Taichi", "Tarjeta Madre", "ASRock", 449.99, 8,
            "Placa madre ATX para AMD Ryzen 7000, DDR5, PCIe 5.0",
            Map.of("socket", "AM5", "formFactor", "ATX", "tipoRam", "DDR5", "chipset", "X670E")));

        insertarDocumentos(tarjetas);
    }

    private void insertarMemoriasRAM() {
        System.out.println("Insertando Memorias RAM...");
        List<Document> memorias = new ArrayList<>();

        memorias.add(crearProducto("Corsair Vengeance RGB 16GB (2x8GB) DDR4", "RAM", "Corsair", 79.99, 40,
            "Memoria RAM DDR4 3200MHz, RGB, 16GB Kit (2x8GB)",
            Map.of("tipoRam", "DDR4", "capacidad", "16GB", "velocidad", "3200MHz", "kit", "2x8GB")));

        memorias.add(crearProducto("G.Skill Trident Z5 32GB (2x16GB) DDR5", "RAM", "G.Skill", 159.99, 25,
            "Memoria RAM DDR5 6000MHz, RGB, 32GB Kit (2x16GB)",
            Map.of("tipoRam", "DDR5", "capacidad", "32GB", "velocidad", "6000MHz", "kit", "2x16GB")));

        memorias.add(crearProducto("Kingston Fury Beast 32GB (2x16GB) DDR4", "RAM", "Kingston", 109.99, 30,
            "Memoria RAM DDR4 3600MHz, 32GB Kit (2x16GB)",
            Map.of("tipoRam", "DDR4", "capacidad", "32GB", "velocidad", "3600MHz", "kit", "2x16GB")));

        memorias.add(crearProducto("Corsair Dominator Platinum 64GB (2x32GB) DDR5", "RAM", "Corsair", 329.99, 15,
            "Memoria RAM DDR5 5600MHz, RGB, 64GB Kit (2x32GB)",
            Map.of("tipoRam", "DDR5", "capacidad", "64GB", "velocidad", "5600MHz", "kit", "2x32GB")));

        insertarDocumentos(memorias);
    }

    private void insertarTarjetasVideo() {
        System.out.println("Insertando Tarjetas de Video...");
        List<Document> tarjetas = new ArrayList<>();

        // NVIDIA
        tarjetas.add(crearProducto("NVIDIA GeForce RTX 4060 Ti", "Tarjeta de video", "NVIDIA", 449.99, 20,
            "Tarjeta gráfica con 8GB GDDR6, ideal para gaming 1080p/1440p",
            Map.of("memoria", "8GB GDDR6", "tdp", "160W", "conectores", "1x8pin")));

        tarjetas.add(crearProducto("NVIDIA GeForce RTX 4070", "Tarjeta de video", "NVIDIA", 599.99, 15,
            "Tarjeta gráfica con 12GB GDDR6X, perfecta para 1440p",
            Map.of("memoria", "12GB GDDR6X", "tdp", "200W", "conectores", "1x8pin")));

        tarjetas.add(crearProducto("NVIDIA GeForce RTX 4080", "Tarjeta de video", "NVIDIA", 1199.99, 10,
            "Tarjeta gráfica con 16GB GDDR6X, gaming 4K extremo",
            Map.of("memoria", "16GB GDDR6X", "tdp", "320W", "conectores", "1x16pin")));

        // AMD
        tarjetas.add(crearProducto("AMD Radeon RX 7600", "Tarjeta de video", "AMD", 269.99, 25,
            "Tarjeta gráfica con 8GB GDDR6, excelente para 1080p",
            Map.of("memoria", "8GB GDDR6", "tdp", "165W", "conectores", "1x8pin")));

        tarjetas.add(crearProducto("AMD Radeon RX 7800 XT", "Tarjeta de video", "AMD", 499.99, 18,
            "Tarjeta gráfica con 16GB GDDR6, potencia para 1440p/4K",
            Map.of("memoria", "16GB GDDR6", "tdp", "263W", "conectores", "2x8pin")));

        insertarDocumentos(tarjetas);
    }

    private void insertarAlmacenamiento() {
        System.out.println("Insertando Almacenamiento...");
        List<Document> almacenamiento = new ArrayList<>();

        almacenamiento.add(crearProducto("Samsung 970 EVO Plus 1TB", "Almacenamiento", "Samsung", 89.99, 35,
            "SSD NVMe M.2 1TB, lectura 3500MB/s, escritura 3300MB/s",
            Map.of("tipo", "SSD NVMe", "capacidad", "1TB", "interfaz", "M.2", "velocidadLectura", "3500MB/s")));

        almacenamiento.add(crearProducto("WD Black SN850X 2TB", "Almacenamiento", "Western Digital", 179.99, 25,
            "SSD NVMe M.2 Gen4 2TB, lectura 7300MB/s, escritura 6600MB/s",
            Map.of("tipo", "SSD NVMe Gen4", "capacidad", "2TB", "interfaz", "M.2", "velocidadLectura", "7300MB/s")));

        almacenamiento.add(crearProducto("Seagate Barracuda 4TB", "Almacenamiento", "Seagate", 84.99, 40,
            "Disco duro HDD SATA 3.5\" 4TB, 7200RPM",
            Map.of("tipo", "HDD", "capacidad", "4TB", "interfaz", "SATA 3.5\"", "rpm", "7200")));

        almacenamiento.add(crearProducto("Crucial P5 Plus 1TB", "Almacenamiento", "Crucial", 99.99, 30,
            "SSD NVMe M.2 Gen4 1TB, lectura 6600MB/s",
            Map.of("tipo", "SSD NVMe Gen4", "capacidad", "1TB", "interfaz", "M.2", "velocidadLectura", "6600MB/s")));

        insertarDocumentos(almacenamiento);
    }

    private void insertarFuentesPoder() {
        System.out.println("Insertando Fuentes de Poder...");
        List<Document> fuentes = new ArrayList<>();

        fuentes.add(crearProducto("Corsair RM850x 850W", "Fuente de poder", "Corsair", 139.99, 20,
            "Fuente modular 850W, certificación 80+ Gold",
            Map.of("potencia", "850W", "certificacion", "80+ Gold", "modular", "Totalmente Modular")));

        fuentes.add(crearProducto("EVGA SuperNOVA 750 G5", "Fuente de poder", "EVGA", 109.99, 25,
            "Fuente modular 750W, certificación 80+ Gold",
            Map.of("potencia", "750W", "certificacion", "80+ Gold", "modular", "Totalmente Modular")));

        fuentes.add(crearProducto("Seasonic FOCUS GX-1000", "Fuente de poder", "Seasonic", 179.99, 15,
            "Fuente modular 1000W, certificación 80+ Gold",
            Map.of("potencia", "1000W", "certificacion", "80+ Gold", "modular", "Totalmente Modular")));

        fuentes.add(crearProducto("Thermaltake Toughpower GF1 650W", "Fuente de poder", "Thermaltake", 89.99, 30,
            "Fuente modular 650W, certificación 80+ Gold",
            Map.of("potencia", "650W", "certificacion", "80+ Gold", "modular", "Totalmente Modular")));

        insertarDocumentos(fuentes);
    }

    private void insertarGabinetes() {
        System.out.println("Insertando Gabinetes...");
        List<Document> gabinetes = new ArrayList<>();

        gabinetes.add(crearProducto("NZXT H510 Flow", "Gabinete", "NZXT", 89.99, 20,
            "Gabinete ATX Mid Tower con panel lateral de vidrio templado",
            Map.of("formFactor", "ATX", "tipo", "Mid Tower", "ventiladores", "2x 120mm incluidos")));

        gabinetes.add(crearProducto("Corsair 4000D Airflow", "Gabinete", "Corsair", 104.99, 25,
            "Gabinete ATX Mid Tower con excelente flujo de aire",
            Map.of("formFactor", "ATX", "tipo", "Mid Tower", "ventiladores", "2x 120mm incluidos")));

        gabinetes.add(crearProducto("Lian Li O11 Dynamic EVO", "Gabinete", "Lian Li", 179.99, 12,
            "Gabinete ATX Mid Tower premium, diseño dual chamber",
            Map.of("formFactor", "ATX", "tipo", "Mid Tower", "ventiladores", "No incluidos")));

        gabinetes.add(crearProducto("Fractal Design Meshify 2", "Gabinete", "Fractal Design", 139.99, 18,
            "Gabinete ATX Mid Tower con frontal mesh, diseño moderno",
            Map.of("formFactor", "ATX", "tipo", "Mid Tower", "ventiladores", "3x 140mm incluidos")));

        insertarDocumentos(gabinetes);
    }

    private void insertarDisipadores() {
        System.out.println("Insertando Disipadores...");
        List<Document> disipadores = new ArrayList<>();

        disipadores.add(crearProducto("Noctua NH-D15", "Disipador", "Noctua", 109.99, 20,
            "Disipador de aire de doble torre, ultra silencioso",
            Map.of("tipo", "Aire", "compatibilidad", "LGA1700/AM5/AM4", "tdp", "250W+")));

        disipadores.add(crearProducto("Cooler Master Hyper 212", "Disipador", "Cooler Master", 39.99, 40,
            "Disipador de aire económico y eficiente",
            Map.of("tipo", "Aire", "compatibilidad", "LGA1700/AM5/AM4", "tdp", "150W")));

        disipadores.add(crearProducto("Corsair iCUE H150i RGB", "Disipador", "Corsair", 179.99, 15,
            "Refrigeración líquida AIO 360mm con RGB",
            Map.of("tipo", "Líquida AIO", "tamaño", "360mm", "compatibilidad", "LGA1700/AM5/AM4")));

        disipadores.add(crearProducto("NZXT Kraken X63", "Disipador", "NZXT", 149.99, 18,
            "Refrigeración líquida AIO 280mm con pantalla LCD",
            Map.of("tipo", "Líquida AIO", "tamaño", "280mm", "compatibilidad", "LGA1700/AM5/AM4")));

        insertarDocumentos(disipadores);
    }

    private void insertarVentiladores() {
        System.out.println("Insertando Ventiladores...");
        List<Document> ventiladores = new ArrayList<>();

        ventiladores.add(crearProducto("Corsair LL120 RGB 3-Pack", "Ventilador", "Corsair", 99.99, 25,
            "Pack de 3 ventiladores RGB 120mm, alta performance",
            Map.of("tamaño", "120mm", "rgb", "Sí", "rpm", "600-1500", "cantidad", "3")));

        ventiladores.add(crearProducto("Noctua NF-A12x25 PWM", "Ventilador", "Noctua", 34.99, 30,
            "Ventilador premium 120mm, ultra silencioso",
            Map.of("tamaño", "120mm", "rgb", "No", "rpm", "450-2000", "cantidad", "1")));

        ventiladores.add(crearProducto("Lian Li UNI FAN SL120 3-Pack", "Ventilador", "Lian Li", 89.99, 20,
            "Ventiladores RGB 120mm con conexión modular",
            Map.of("tamaño", "120mm", "rgb", "Sí", "rpm", "800-1900", "cantidad", "3")));

        insertarDocumentos(ventiladores);
    }

    private void insertarMonitores() {
        System.out.println("Insertando Monitores...");
        List<Document> monitores = new ArrayList<>();

        monitores.add(crearProducto("ASUS TUF Gaming VG27AQ", "Monitor", "ASUS", 329.99, 15,
            "Monitor gaming 27\" 1440p 165Hz IPS, G-Sync compatible",
            Map.of("tamaño", "27\"", "resolucion", "2560x1440", "frecuencia", "165Hz", "panel", "IPS")));

        monitores.add(crearProducto("LG 27GL850-B", "Monitor", "LG", 379.99, 12,
            "Monitor gaming 27\" 1440p 144Hz IPS Nano, HDR",
            Map.of("tamaño", "27\"", "resolucion", "2560x1440", "frecuencia", "144Hz", "panel", "IPS Nano")));

        monitores.add(crearProducto("Samsung Odyssey G7", "Monitor", "Samsung", 599.99, 10,
            "Monitor curvo gaming 32\" 1440p 240Hz VA, G-Sync",
            Map.of("tamaño", "32\"", "resolucion", "2560x1440", "frecuencia", "240Hz", "panel", "VA Curvo")));

        monitores.add(crearProducto("Dell S2721DGF", "Monitor", "Dell", 399.99, 18,
            "Monitor gaming 27\" 1440p 165Hz IPS, G-Sync Premium",
            Map.of("tamaño", "27\"", "resolucion", "2560x1440", "frecuencia", "165Hz", "panel", "IPS")));

        insertarDocumentos(monitores);
    }

    private void insertarKitsTecladoRaton() {
        System.out.println("Insertando Kits de Teclado y Ratón...");
        List<Document> kits = new ArrayList<>();

        kits.add(crearProducto("Logitech G Pro X Combo", "Kit de teclado/raton", "Logitech", 229.99, 15,
            "Combo profesional: Teclado mecánico + Mouse inalámbrico",
            Map.of("tipoTeclado", "Mecánico", "switchesTeclado", "GX Blue", "dpiMouse", "25600", "inalambrico", "Sí")));

        kits.add(crearProducto("Razer BlackWidow + DeathAdder", "Kit de teclado/raton", "Razer", 159.99, 20,
            "Combo gaming: Teclado mecánico RGB + Mouse ergonómico",
            Map.of("tipoTeclado", "Mecánico", "switchesTeclado", "Razer Green", "dpiMouse", "20000", "inalambrico", "No")));

        kits.add(crearProducto("Corsair K70 + M65 RGB", "Kit de teclado/raton", "Corsair", 189.99, 18,
            "Combo premium: Teclado mecánico + Mouse gaming RGB",
            Map.of("tipoTeclado", "Mecánico", "switchesTeclado", "Cherry MX", "dpiMouse", "18000", "inalambrico", "No")));

        kits.add(crearProducto("HyperX Alloy Origins + Pulsefire", "Kit de teclado/raton", "HyperX", 139.99, 22,
            "Combo gaming: Teclado mecánico compacto + Mouse gaming",
            Map.of("tipoTeclado", "Mecánico", "switchesTeclado", "HyperX Red", "dpiMouse", "16000", "inalambrico", "No")));

        insertarDocumentos(kits);
    }

    private void insertarRedes() {
        System.out.println("Insertando Redes e Internet...");
        List<Document> redes = new ArrayList<>();

        redes.add(crearProducto("TP-Link Archer AX73 WiFi 6", "Redes e internet", "TP-Link", 139.99, 20,
            "Router WiFi 6 AX5400, Gigabit, cobertura amplia",
            Map.of("tipo", "Router", "estandar", "WiFi 6 (AX)", "velocidad", "5400Mbps", "puertos", "4x Gigabit")));

        redes.add(crearProducto("ASUS RT-AX86U Pro", "Redes e internet", "ASUS", 249.99, 15,
            "Router gaming WiFi 6, AiMesh, 2.5G WAN",
            Map.of("tipo", "Router Gaming", "estandar", "WiFi 6 (AX)", "velocidad", "5700Mbps", "puertos", "1x 2.5G + 4x Gigabit")));

        redes.add(crearProducto("Intel AX210 WiFi 6E", "Redes e internet", "Intel", 29.99, 35,
            "Tarjeta de red WiFi 6E + Bluetooth 5.2, M.2",
            Map.of("tipo", "Tarjeta WiFi", "estandar", "WiFi 6E (AXE)", "velocidad", "2400Mbps", "interfaz", "M.2")));

        redes.add(crearProducto("TP-Link UE300 USB 3.0 Gigabit", "Redes e internet", "TP-Link", 14.99, 40,
            "Adaptador Ethernet USB 3.0 a Gigabit",
            Map.of("tipo", "Adaptador Ethernet", "velocidad", "1000Mbps", "interfaz", "USB 3.0")));

        insertarDocumentos(redes);
    }

    /**
     * Limpia (elimina) todos los productos de la colección.
     */
    private void limpiarColeccion() {
        try {
            long cantidadEliminada = coleccion.deleteMany(new Document()).getDeletedCount();
            System.out.println("  🗑️  " + cantidadEliminada + " productos eliminados");
        } catch (Exception e) {
            System.err.println("  ✗ Error al limpiar colección: " + e.getMessage());
        }
    }

    private Document crearProducto(String nombre, String categoria, String marca,
                                   double precio, int stock, String descripcion,
                                   Map<String, String> especificaciones) {
        Document doc = new Document();
        doc.append("nombre", nombre);
        doc.append("categoria", categoria);
        doc.append("marca", marca);
        doc.append("precio", precio);
        doc.append("stock", stock);
        doc.append("descripcion", descripcion);
        doc.append("imagenUrl", "/img/productos/default.png");
        doc.append("especificaciones", new Document(especificaciones));
        return doc;
    }

    private void insertarDocumentos(List<Document> documentos) {
        try {
            coleccion.insertMany(documentos);
            System.out.println(documentos.size() + " productos insertados");
        } catch (Exception e) {
            System.err.println("Error al insertar productos: " + e.getMessage());
        }
    }
}

