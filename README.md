# online-electronic-store — Tienda Online de Componentes Electrónicos

Este repositorio contiene una aplicación modular Java (Maven) para una tienda online especializada en armado de PCs con validación de compatibilidad de componentes y gestión de ventas.

## Requisitos
- Java JDK 23+
- Maven 3.6+
- MongoDB 4.x+ (corriendo en localhost:27017)

## Arquitectura del Proyecto

La aplicación utiliza una arquitectura en capas modular con los siguientes subsistemas:

### Módulos de Datos

- **`dominio_datos`** — Entidades de dominio mapeadas a MongoDB:
  - ProductoEntidad: Productos del catálogo
  - PedidoEntidad: Órdenes de compra

- **`dao_datos`** — Capa de acceso a datos:
  - ProductoDAO: CRUD de productos con filtros por categoría y marca
  - PedidoDAO: Gestión de pedidos
  - ConexionMongoDB: Singleton para conexión a base de datos

### Módulos de Negocio

- **`dto_negocios`** — Objetos de transferencia de datos (DTOs):
  - ComponenteDTO, EnsamblajeDTO, CarritoDTO, ItemCarritoDTO, CompraDTO, MetodoPagoDTO, ClienteDTO

- **`objetos_negocio`** — Capa de lógica de negocio:
  - ComponenteON: Gestión del catálogo de componentes
  - Interfaces de negocio (IComponenteON)

- **`negocio_configuracion`** — **[NUEVO]** Subsistema de configuración secuencial:
  - Flujo: Categoría → Marca → Productos
  - Validación de disponibilidad de productos
  - ConfiguracionFacade: Punto de entrada único

- **`negocio_venta`** — **[NUEVO]** Subsistema de ventas y pago:
  - Gestión de carrito de compras
  - Proceso de pago (mock - happy path)
  - Actualización de stock
  - VentaFacade: Punto de entrada único

- **`negocio_armarPC`** — **[ACTUALIZADO]** Fachada de armado de PC:
  - Validación de compatibilidad entre componentes
  - Filtrado por tipo de uso (GAMER, OFFICE)
  - **Nuevo**: Soporte para cambiar componentes ya seleccionados
  - **Nuevo**: Revalidación de ensamblaje completo
  - ArmadoFacade: Punto de entrada único

### Módulo de Presentación

- **`presentacion`** — Interfaz gráfica de usuario (Swing):
  - Paneles de armado de PC paso a paso
  - Catálogos de componentes con cards
  - Sistema de navegación por tarjetas (CardLayout)
  - Carrito de compras interactivo
  - Estilos personalizados y componentes reutilizables

## Características Principales

### Flujo de Configuración Secuencial
1. Usuario selecciona categoría de componente
2. Sistema muestra marcas disponibles
3. Usuario selecciona marca
4. Sistema muestra productos disponibles
5. Si no hay productos → Notificación y fin del flujo
6. Usuario hace clic en ProductCard → Resaltado visual
7. Flecha de avance habilitada solo con componente seleccionado
8. Validación de compatibilidad al agregar componente

### Sistema de Ventas
- Carrito de compras con gestión de items
- Verificación de stock en tiempo real
- Proceso de pago mock (happy path)
- Creación automática de pedidos
- Actualización de inventario

### Validaciones de Compatibilidad
- Socket de procesador vs tarjeta madre
- Tipo de RAM (DDR4/DDR5)
- Form factor (ATX/Micro-ATX)
- Consumo eléctrico vs fuente de poder

## Tecnologías Utilizadas

- **Java 23**
- **Maven** — Gestión de dependencias y construcción
- **MongoDB** — Base de datos NoSQL
- **MongoDB Java Driver 4.11.1** — Conexión a base de datos
- **Swing** — Interfaz gráfica de usuario
- **Patrón Facade** — Aislamiento de subsistemas
- **Patrón Singleton** — Gestión de instancias únicas
- **Patrón DAO** — Acceso a datos

## Estructura del Proyecto

```
online-electronic-store/
├── dominio_datos/          # Entidades MongoDB
├── dao_datos/              # Acceso a datos
├── dto_negocios/           # DTOs
├── objetos_negocio/        # Lógica de negocio base
├── negocio_configuracion/  # Flujo secuencial
├── negocio_venta/          # Gestión de ventas
├── negocio_armarPC/        # Validación de compatibilidad
├── presentacion/           # UI (Swing)
└── README.md               # Este archivo
```
## Notas Importantes

1. MongoDB debe estar corriendo antes de ejecutar la aplicación
2. Ejecutar el script para tener datos de prueba
3. Las imágenes deben estar en `presentacion/src/main/resources/img/`
4. Conexión a MongoDB: `mongodb://localhost:27017/highspecs_db`

## Funcionalidades Principales

1. **Armado de PC Guiado**:
   - Selección de tipo de PC (Gamer, Office, etc.)
   - Validación de stock suficiente
   - Selección de componentes paso a paso
   - Validación automática de compatibilidad

2. **Validaciones de Compatibilidad**:
   - Socket del procesador con tarjeta madre
   - Tipo de RAM (DDR4, DDR5)
   - Form factor (ATX, Micro-ATX)
   - Otros criterios específicos por categoría

3. **Filtrado**:
   - Solo se muestran componentes compatibles con la selección actual
   - Prevención de selecciones incompatibles

