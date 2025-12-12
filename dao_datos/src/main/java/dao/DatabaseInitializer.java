package dao;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import conexion.ConexionMongoDB;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.util.*;

/**
 * Inicializador completo de la base de datos MongoDB.
 * Crea todas las colecciones, el usuario por defecto, carrito y productos.
 */
public class DatabaseInitializer {

    private final MongoDatabase database;
    private ObjectId usuarioDefaultId;
    private ObjectId carritoDefaultId;
    private List<ObjectId> productosIds;

    public DatabaseInitializer() {
        this.database = ConexionMongoDB.getInstance().getDatabase();
        this.productosIds = new ArrayList<>();
    }

    public static void main(String[] args) {
        DatabaseInitializer initializer = new DatabaseInitializer();

        try {
            initializer.inicializarBaseDatos();
            System.out.println("Base de datos inicializada");
        } catch (Exception e) {
            System.err.println("ERROR al inicializar la base de datos:");
            e.printStackTrace();
        }
    }

    /**
     * Inicializa completamente la base de datos
     */
    private void inicializarBaseDatos() {
        System.out.println("\nLimpiando base de datos anterior...");
        limpiarBaseDatos();

        System.out.println("\nCreando colecciones...");
        crearColecciones();

        System.out.println("\nInsertando usuario por defecto...");
        insertarUsuarioDefault();

        System.out.println("\nInsertando carrito del usuario...");
        insertarCarritoDefault();

        System.out.println("\nInsertando productos...");
        insertarProductos();

//        insertarPedidosEjemplo();

        mostrarEstadisticas();
    }

    /**
     * Limpia todas las colecciones
     */
    private void limpiarBaseDatos() {
        String[] colecciones = {"usuarios", "carritos", "configuraciones", "productos", "pedidos"};

        for (String coleccion : colecciones) {
            try {
                MongoCollection<Document> col = database.getCollection(coleccion);
                long eliminados = col.deleteMany(new Document()).getDeletedCount();
                if (eliminados > 0) {
                    System.out.println("  ✓ Colección '" + coleccion + "': " + eliminados + " documentos eliminados");
                }
            } catch (Exception e) {
                System.out.println("  Colección '" + coleccion + "' no existía, se creará nueva");
            }
        }
    }

    /**
     * Crea las colecciones principales
     */
    private void crearColecciones() {
        String[] colecciones = {"usuarios", "carritos", "configuraciones", "productos", "pedidos"};

        for (String nombreColeccion : colecciones) {
            try {
                database.createCollection(nombreColeccion);
                System.out.println("Colección '" + nombreColeccion + "' creada");
            } catch (Exception e) {
                System.out.println("Colección '" + nombreColeccion + "' ya existe");
            }
        }
    }

    /**
     * Inserta el usuario por defecto
     */
    private void insertarUsuarioDefault() {
        MongoCollection<Document> usuariosCol = database.getCollection("usuarios");

        Document usuario = new Document();
        usuario.append("nombre", "Cliente Default");
        usuario.append("email", "cliente_default@local");
        usuario.append("fechaCreacion", new Date());

        usuariosCol.insertOne(usuario);
        usuarioDefaultId = usuario.getObjectId("_id");
        System.out.println("Usuario creado: " + usuario.getString("nombre"));
        System.out.println("ID: " + usuarioDefaultId);
    }

    /**
     * Inserta el carrito del usuario por defecto (vacío)
     */
    private void insertarCarritoDefault() {
        MongoCollection<Document> carritosCol = database.getCollection("carritos");

        Document carrito = new Document();
        carrito.append("clienteId", usuarioDefaultId.toString())
               .append("fechaActualizacion", new Date());

        carritosCol.insertOne(carrito);
        carritoDefaultId = carrito.getObjectId("_id");
        System.out.println("Carrito creado para el usuario");
        System.out.println("ID: " + carritoDefaultId);
    }

    /**
     * Inserta todos los productos
     */
    private void insertarProductos() {
        MongoCollection<Document> productosCol = database.getCollection("productos");

        List<Document> todosProductos = new ArrayList<>();

        // Procesadores
        todosProductos.addAll(crearProcesadores());

        // Tarjetas Madre
        todosProductos.addAll(crearTarjetasMadre());

        // Memorias RAM
        todosProductos.addAll(crearMemoriasRAM());

        // Tarjetas de Video
        todosProductos.addAll(crearTarjetasVideo());

        // Almacenamiento
        todosProductos.addAll(crearAlmacenamiento());

        // Fuentes de Poder
        todosProductos.addAll(crearFuentesPoder());

        // Gabinetes
        todosProductos.addAll(crearGabinetes());

        // Disipadores
        todosProductos.addAll(crearDisipadores());

        // Ventiladores
        todosProductos.addAll(crearVentiladores());

        // Monitores
        todosProductos.addAll(crearMonitores());

        // Kits Teclado/Ratón
        todosProductos.addAll(crearKitsTecladoRaton());

        // Redes e Internet
        todosProductos.addAll(crearRedes());

        // Insertar todos
        if (!todosProductos.isEmpty()) {
            productosCol.insertMany(todosProductos);

            for (Document doc : todosProductos) {
                productosIds.add(doc.getObjectId("_id"));
            }

            System.out.println(todosProductos.size() + " productos insertados");
        }
    }

    /**
     * Inserta pedidos de ejemplo
     */
    private void insertarPedidosEjemplo() {
        MongoCollection<Document> pedidosCol = database.getCollection("pedidos");

        List<Document> pedidosEjemplo = new ArrayList<>();

        // Pedido 1: Compra de 2 procesadores
        Document pedido1 = new Document();
        pedido1.append("clienteId", usuarioDefaultId.toString());
        pedido1.append("estado", "COMPLETADO");
        pedido1.append("fechaCreacion", new Date(System.currentTimeMillis() - 86400000));

        List<Document> items1 = new ArrayList<>();
        items1.add(new Document()
            .append("nombre", "Intel Core i5-12400F")
            .append("cantidad", 1)
            .append("precioUnitario", 179.99));
        items1.add(new Document()
            .append("nombre", "AMD Ryzen 5 5600X")
            .append("cantidad", 1)
            .append("precioUnitario", 199.99));

        pedido1.append("items", items1);
        pedido1.append("total", 379.98);
        pedido1.append("metodoPago", new Document()
            .append("tipo", "TARJETA")
            .append("detalles", "Mock - Pago procesado exitosamente"));

        pedidosEjemplo.add(pedido1);

        // Pedido 2: Compra de RAM
        Document pedido2 = new Document();
        pedido2.append("clienteId", usuarioDefaultId.toString());
        pedido2.append("estado", "COMPLETADO");
        pedido2.append("fechaCreacion", new Date(System.currentTimeMillis() - 172800000));

        List<Document> items2 = new ArrayList<>();
        items2.add(new Document()
            .append("nombre", "Corsair Vengeance RGB 16GB (2x8GB) DDR4")
            .append("cantidad", 1)
            .append("precioUnitario", 79.99));

        pedido2.append("items", items2);
        pedido2.append("total", 79.99);
        pedido2.append("metodoPago", new Document()
            .append("tipo", "TARJETA")
            .append("detalles", "Mock - Pago procesado exitosamente"));

        pedidosEjemplo.add(pedido2);

        if (!pedidosEjemplo.isEmpty()) {
            pedidosCol.insertMany(pedidosEjemplo);
            System.out.println(pedidosEjemplo.size() + " pedidos de ejemplo insertados");
        }
    }

    /**
     * Muestra estadísticas finales de la base de datos
     */
    private void mostrarEstadisticas() {
        System.out.println("\nRegistros insertados:");
        System.out.println("Usuarios:         " + database.getCollection("usuarios").countDocuments());
        System.out.println("Carritos:         " + database.getCollection("carritos").countDocuments());
        System.out.println("Productos:        " + database.getCollection("productos").countDocuments());
        System.out.println("Configuraciones:  " + database.getCollection("configuraciones").countDocuments());
        System.out.println("Pedidos:          " + database.getCollection("pedidos").countDocuments());
    }

    private List<Document> crearProcesadores() {
        List<Document> procesadores = new ArrayList<>();

        procesadores.add(crearProducto("Intel Core i5-12400F", "Procesador", "Intel", 179.99, 25,
            "Procesador de 6 núcleos y 12 hilos, 2.5GHz base / 4.4GHz turbo",
            Map.of("socket", "LGA1700", "nucleos", "6", "hilos", "12", "frecuencia", "2.5-4.4 GHz")));

        procesadores.add(crearProducto("Intel Core i7-13700K", "Procesador", "Intel", 409.99, 15,
            "Procesador de 16 núcleos (8P+8E) y 24 hilos, 3.4GHz base / 5.4GHz turbo",
            Map.of("socket", "LGA1700", "nucleos", "16", "hilos", "24", "frecuencia", "3.4-5.4 GHz")));

        procesadores.add(crearProducto("Intel Core i9-13900K", "Procesador", "Intel", 589.99, 10,
            "Procesador de 24 núcleos (8P+16E) y 32 hilos, 3.0GHz base / 5.8GHz turbo",
            Map.of("socket", "LGA1700", "nucleos", "24", "hilos", "32", "frecuencia", "3.0-5.8 GHz")));

        procesadores.add(crearProducto("AMD Ryzen 5 5600X", "Procesador", "AMD", 199.99, 30,
            "Procesador de 6 núcleos y 12 hilos, 3.7GHz base / 4.6GHz turbo",
            Map.of("socket", "AM4", "nucleos", "6", "hilos", "12", "frecuencia", "3.7-4.6 GHz")));

        procesadores.add(crearProducto("AMD Ryzen 7 7700X", "Procesador", "AMD", 349.99, 20,
            "Procesador de 8 núcleos y 16 hilos, 4.5GHz base / 5.4GHz turbo",
            Map.of("socket", "AM5", "nucleos", "8", "hilos", "16", "frecuencia", "4.5-5.4 GHz")));

        return procesadores;
    }

    private List<Document> crearTarjetasMadre() {
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

        return tarjetas;
    }

    private List<Document> crearMemoriasRAM() {
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

        return memorias;
    }

    private List<Document> crearTarjetasVideo() {
        List<Document> tarjetas = new ArrayList<>();

        tarjetas.add(crearProducto("NVIDIA GeForce RTX 4060 Ti", "Tarjeta de video", "NVIDIA", 449.99, 20,
            "Tarjeta gráfica con 8GB GDDR6, ideal para gaming 1080p/1440p",
            Map.of("memoria", "8GB GDDR6", "tdp", "160W", "conectores", "1x8pin")));

        tarjetas.add(crearProducto("NVIDIA GeForce RTX 4070", "Tarjeta de video", "NVIDIA", 599.99, 15,
            "Tarjeta gráfica con 12GB GDDR6X, perfecta para 1440p",
            Map.of("memoria", "12GB GDDR6X", "tdp", "200W", "conectores", "1x8pin")));

        tarjetas.add(crearProducto("AMD Radeon RX 7600", "Tarjeta de video", "AMD", 269.99, 25,
            "Tarjeta gráfica con 8GB GDDR6, excelente para 1080p",
            Map.of("memoria", "8GB GDDR6", "tdp", "165W", "conectores", "1x8pin")));

        return tarjetas;
    }

    private List<Document> crearAlmacenamiento() {
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

        return almacenamiento;
    }

    private List<Document> crearFuentesPoder() {
        List<Document> fuentes = new ArrayList<>();

        fuentes.add(crearProducto("Corsair RM850x 850W", "Fuente de poder", "Corsair", 139.99, 20,
            "Fuente modular 850W, certificación 80+ Gold",
            Map.of("potencia", "850W", "certificacion", "80+ Gold", "modular", "Totalmente Modular")));

        fuentes.add(crearProducto("EVGA SuperNOVA 750 G5", "Fuente de poder", "EVGA", 109.99, 25,
            "Fuente modular 750W, certificación 80+ Gold",
            Map.of("potencia", "750W", "certificacion", "80+ Gold", "modular", "Totalmente Modular")));

        return fuentes;
    }

    private List<Document> crearGabinetes() {
        List<Document> gabinetes = new ArrayList<>();

        gabinetes.add(crearProducto("NZXT H510 Flow", "Gabinete", "NZXT", 89.99, 20,
            "Gabinete ATX Mid Tower con panel lateral de vidrio templado",
            Map.of("formFactor", "ATX", "tipo", "Mid Tower", "ventiladores", "2x 120mm incluidos")));

        gabinetes.add(crearProducto("Corsair 4000D Airflow", "Gabinete", "Corsair", 104.99, 25,
            "Gabinete ATX Mid Tower con excelente flujo de aire",
            Map.of("formFactor", "ATX", "tipo", "Mid Tower", "ventiladores", "2x 120mm incluidos")));

        return gabinetes;
    }

    private List<Document> crearDisipadores() {
        List<Document> disipadores = new ArrayList<>();

        disipadores.add(crearProducto("Noctua NH-D15", "Disipador", "Noctua", 109.99, 20,
            "Disipador de aire de doble torre, ultra silencioso",
            Map.of("tipo", "Aire", "compatibilidad", "LGA1700/AM5/AM4", "tdp", "250W+")));

        disipadores.add(crearProducto("Cooler Master Hyper 212", "Disipador", "Cooler Master", 39.99, 40,
            "Disipador de aire económico y eficiente",
            Map.of("tipo", "Aire", "compatibilidad", "LGA1700/AM5/AM4", "tdp", "150W")));

        return disipadores;
    }

    private List<Document> crearVentiladores() {
        List<Document> ventiladores = new ArrayList<>();

        ventiladores.add(crearProducto("Corsair LL120 RGB 3-Pack", "Ventilador", "Corsair", 99.99, 25,
            "Pack de 3 ventiladores RGB 120mm, alta performance",
            Map.of("tamaño", "120mm", "rgb", "Sí", "rpm", "600-1500", "cantidad", "3")));

        ventiladores.add(crearProducto("Noctua NF-A12x25 PWM", "Ventilador", "Noctua", 34.99, 30,
            "Ventilador premium 120mm, ultra silencioso",
            Map.of("tamaño", "120mm", "rgb", "No", "rpm", "450-2000", "cantidad", "1")));

        return ventiladores;
    }

    private List<Document> crearMonitores() {
        List<Document> monitores = new ArrayList<>();

        monitores.add(crearProducto("ASUS TUF Gaming VG27AQ", "Monitor", "ASUS", 329.99, 15,
            "Monitor gaming 27\" 1440p 165Hz IPS, G-Sync compatible",
            Map.of("tamaño", "27\"", "resolucion", "2560x1440", "frecuencia", "165Hz", "panel", "IPS")));

        monitores.add(crearProducto("LG 27GL850-B", "Monitor", "LG", 379.99, 12,
            "Monitor gaming 27\" 1440p 144Hz IPS Nano, HDR",
            Map.of("tamaño", "27\"", "resolucion", "2560x1440", "frecuencia", "144Hz", "panel", "IPS Nano")));

        return monitores;
    }

    private List<Document> crearKitsTecladoRaton() {
        List<Document> kits = new ArrayList<>();

        kits.add(crearProducto("Logitech G Pro X Combo", "Kit de teclado/raton", "Logitech", 229.99, 15,
            "Combo profesional: Teclado mecánico + Mouse inalámbrico",
            Map.of("tipoTeclado", "Mecánico", "switchesTeclado", "GX Blue", "dpiMouse", "25600", "inalambrico", "Sí")));

        kits.add(crearProducto("Razer BlackWidow + DeathAdder", "Kit de teclado/raton", "Razer", 159.99, 20,
            "Combo gaming: Teclado mecánico RGB + Mouse ergonómico",
            Map.of("tipoTeclado", "Mecánico", "switchesTeclado", "Razer Green", "dpiMouse", "20000", "inalambrico", "No")));

        return kits;
    }

    private List<Document> crearRedes() {
        List<Document> redes = new ArrayList<>();

        redes.add(crearProducto("TP-Link Archer AX73 WiFi 6", "Redes e internet", "TP-Link", 139.99, 20,
            "Router WiFi 6 AX5400, Gigabit, cobertura amplia",
            Map.of("tipo", "Router", "estandar", "WiFi 6 (AX)", "velocidad", "5400Mbps", "puertos", "4x Gigabit")));

        redes.add(crearProducto("ASUS RT-AX86U Pro", "Redes e internet", "ASUS", 249.99, 15,
            "Router gaming WiFi 6, AiMesh, 2.5G WAN",
            Map.of("tipo", "Router Gaming", "estandar", "WiFi 6 (AX)", "velocidad", "5700Mbps", "puertos", "1x 2.5G + 4x Gigabit")));

        return redes;
    }

    /**
     * Crea un documento de producto
     */
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
        doc.append("fechaCreacion", new Date());
        return doc;
    }
}

